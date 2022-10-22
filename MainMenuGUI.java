import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * MainMenuGUI.java Class -- Project 5
 *
 * This GUI is called when a user has logged in and allows them to operate the
 * program by calling other
 * GUIs as desired
 *
 * @author Luke Cooley, Logan Snelling, Mason Minnich, Anushka Gupta -- L07
 *
 * @version 05/02/22
 *
 */
public class MainMenuGUI extends JComponent implements Runnable {
    // Fields
    private User currentUser;
    private Quiz currentQuiz;
    private Course currentCourse;
    private ArrayList<Course> courses;
    Socket socket;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public MainMenuGUI(User currentUser, Socket socket, ObjectOutputStream oos, ObjectInputStream ois) {
        this.currentUser = currentUser;
        this.socket = socket;
        this.oos = oos;
        this.ois = ois;

    }

    public MainMenuGUI() {

    }

    JFrame frame = new JFrame("Main Menu");
    // Teacher buttons
    JButton createCourseButton;
    JButton modifyCourseButton;
    // Student buttons
    JButton takeQuizButton;
    // Common buttons
    JButton editAccountButton;
    JButton deleteAccountButton;
    JButton logoutButton;

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == createCourseButton) {
                SwingUtilities.invokeLater(new CreateCourseGUI(socket, oos, ois));

            } else if (e.getSource() == modifyCourseButton) {

                selectCourse();
                ModifyCourseGUI modifyCourseGUI = new ModifyCourseGUI(socket, oos, ois, currentCourse);

                SwingUtilities.invokeLater(modifyCourseGUI);
            } else if (e.getSource() == takeQuizButton) {
                if (courses.size() > 0) {
                    // Call the method for selecting a course
                    selectCourse();

                    if (currentCourse.getCourse().size() > 0) {
                        // selecting a quiz
                        Object[] options2 = new Object[currentCourse.getCourse().size()];
                        for (int i = 0; i < currentCourse.getCourse().size(); i++) {
                            options2[i] = currentCourse.getCourse().get(i).getName();
                        }
                        Object selectedQuizName = JOptionPane.showInputDialog(null,
                                "Select Quiz",
                                "Main Menu", JOptionPane.PLAIN_MESSAGE, null, options2, options2[0]);
                        for (int i = 0; i < currentCourse.getCourse().size(); i++) {
                            if (selectedQuizName.equals(currentCourse.getCourse().get(i).getName())) {
                                currentQuiz = currentCourse.getCourse().get(i);
                                break;
                            }
                        }
                        // calling the take quiz GUI

                        // New Constructor for Taking Quiz Below:
                        TakeQuizGUI takeQuizGUI = new TakeQuizGUI(currentQuiz, currentUser, currentCourse, socket, oos,
                                ois);
                        SwingUtilities.invokeLater(takeQuizGUI);

                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Error! No quizzes exist for that course.",
                                "Take Quiz",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Error! No courses exist.",
                            "Take Quiz",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else if (e.getSource() == editAccountButton) {
                SwingUtilities.invokeLater(new EditAccountGUI(currentUser, socket, oos, ois));
                frame.dispose();

            } else if (e.getSource() == deleteAccountButton) {

                JTextField passwordTextField = new JPasswordField(15);
                JLabel passwordLabel = new JLabel("Type in password to continue");
                Box enterPassword = Box.createHorizontalBox();
                enterPassword.add(passwordLabel);
                enterPassword.add(passwordTextField);

                int b = JOptionPane.showConfirmDialog(null, enterPassword, "Enter Password",
                        JOptionPane.OK_CANCEL_OPTION);

                String password = "";
                if (b == JOptionPane.OK_OPTION) {
                    password = passwordTextField.getText();
                }

                Object[] confirmOptions = { "OK", "CANCEL" };
                if (password.equals(currentUser.getPassword())) {
                    int toDelete = JOptionPane.showOptionDialog(null,
                            "Are you sure you want to delete your account?\nClick OK to continue",
                            "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                            null, confirmOptions, confirmOptions[0]);
                    if (toDelete == 0) {
                        try {
                            oos.writeObject("DeleteAccount");
                            oos.writeObject(currentUser);
                            oos.flush();

                            boolean deleted = (boolean) ois.readObject();

                            if (deleted) {
                                JOptionPane.showInternalMessageDialog(null, "Account Deleted",
                                        "Main Menu", JOptionPane.INFORMATION_MESSAGE);
                            }

                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Account could not be deleted",
                                    "Warning", JOptionPane.INFORMATION_MESSAGE);
                            ex.printStackTrace();
                        }
                        frame.dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Incorrect Password",
                            "Warning", JOptionPane.INFORMATION_MESSAGE);
                }

            } else if (e.getSource() == logoutButton) {
                JOptionPane.showInternalMessageDialog(null, "Logging out...",
                        "Main Menu", JOptionPane.INFORMATION_MESSAGE);
                currentUser = null;

                SwingUtilities.invokeLater(new LogInGUI(socket, oos, ois));

                frame.dispose();
            }
        }
    };

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new MainMenuGUI());
    }

    public ArrayList<Course> readCourseListFile() {
        ArrayList<Course> courseList = new ArrayList<>();
        ArrayList<String> courseNames = new ArrayList<>();

        try (BufferedReader bfr = new BufferedReader(new FileReader("CourseList.txt"))) {
            String line = bfr.readLine();

            while (line != null) {
                courseNames.add(line);
                line = bfr.readLine();
            }

        } catch (FileNotFoundException e) {
            System.out.println("Error! CourseList.txt not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String courseName : courseNames) {
            Course course = new Course(courseName);
            courseList.add(course);

        }

        return courseList;
    }

    public void run() {
        // JFrame frame = new JFrame("Main Menu");

        setCourses(writeServer("GetCourses"));

        Container content = frame.getContentPane();

        content.setLayout(new BorderLayout());

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        if (currentUser.getIsTeacher()) {
            createCourseButton = new JButton("Create Course");
            createCourseButton.addActionListener(actionListener);

            modifyCourseButton = new JButton("Modify Course");
            modifyCourseButton.addActionListener(actionListener);
        } else {
            takeQuizButton = new JButton("Take Quiz");
            takeQuizButton.addActionListener(actionListener);
        }

        editAccountButton = new JButton("Edit Account");
        editAccountButton.addActionListener(actionListener);

        deleteAccountButton = new JButton("Delete Account");
        deleteAccountButton.addActionListener(actionListener);

        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(actionListener);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        if (currentUser.getIsTeacher()) {
            panel.add(createCourseButton);
            panel.add(modifyCourseButton);
        } else {
            panel.add(takeQuizButton);
        }
        panel.add(editAccountButton);
        panel.add(deleteAccountButton);
        panel.add(logoutButton);
        content.add(panel, BorderLayout.WEST);
    }

    public ArrayList<Course> writeServer(String instructions) {
        Course[] courseArray;
        ArrayList<Course> courseList = new ArrayList<>();
        try {
            oos.writeObject(instructions);
            oos.flush();

            courseArray = (Course[]) ois.readObject();

            for (int i = 0; i < courseArray.length; i++) {
                courseList.add(courseArray[i]);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return courseList;
    }

    public void selectCourse() {
        // Update Course List
        setCourses(writeServer("GetCourses"));
        // selecting a course
        Object[] options = new Object[courses.size()];
        for (int i = 0; i < courses.size(); i++) {
            options[i] = courses.get(i).getName();
        }
        Object selectedCourseName = JOptionPane.showInputDialog(null,
                "Select Course",
                "Main Menu", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        for (Course course : courses) {
            if (selectedCourseName.equals(course.getName())) {
                currentCourse = course;
                break;
            }
        }

    }
}
