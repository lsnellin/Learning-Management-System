import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * ModifyCourseGUI Class -- Project 5
 *
 * This is a class that creates a GUI that allows teachers to modify a course.
 * Teachers can create, edit ,and quizzes and view student submissions
 *
 * @author Luke Cooley, Logan Snelling, Mason Minnich, Anushka Gupta -- L07
 *
 * @version 05/02/22
 *
 */
public class ModifyCourseGUI extends JComponent implements Runnable {
    // Fields:
    JButton createQuizButton;
    JButton editQuizButton;
    JButton deleteQuizButton;
    JButton viewGradeQuizButton;
    JButton exitButton;
    JLabel welcomeToCourseLabel;

    ModifyCourseGUI modifyCourseGUI;

    // Server Client Fields
    Socket socket;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    // Course Fields
    Course currentCourse;
    ArrayList<Submission> submissionList;

    ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == createQuizButton) {
                CreateQuizGUI newQuizGUI = new CreateQuizGUI(modifyCourseGUI.getCurrentCourse(), socket, oos, ois);
                SwingUtilities.invokeLater(newQuizGUI);

            }
            if (e.getSource() == editQuizButton) {
                ArrayList<Quiz> availableQuizzes = modifyCourseGUI.receiveCurrentCourse().getCourse();
                String[] quizNames = new String[availableQuizzes.size()];

                for (int i = 0; i < availableQuizzes.size(); i++) {
                    quizNames[i] = availableQuizzes.get(i).getName();
                }

                try {
                    String quizName = (String) JOptionPane.showInputDialog(modifyCourseGUI,
                            "Please select a quiz to edit", "Select Quiz?", JOptionPane.QUESTION_MESSAGE, null,
                            quizNames, quizNames[0]);

                    int quizNumber = 0;
                    for (int i = 0; i < availableQuizzes.size(); i++) {
                        if (quizName.equals(availableQuizzes.get(i).getName())) {
                            quizNumber = i;
                            break;
                        }
                    }
                    CreateQuizGUI newQuizGUI = new CreateQuizGUI(availableQuizzes.get(quizNumber),
                            modifyCourseGUI.getCurrentCourse(), socket, oos, ois, quizNumber);
                    SwingUtilities.invokeLater(newQuizGUI);

                    // System.out.println(newQuizGUI.getCreateNewQuiz());

                    if (newQuizGUI.getCreateNewQuiz()) {
                        modifyCourseGUI.getCurrentCourse().getCourse().add(newQuizGUI.getCurrentQuiz());
                    } else {
                        modifyCourseGUI.getCurrentCourse().getCourse().set(quizNumber, newQuizGUI.getCurrentQuiz());
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (e.getSource() == deleteQuizButton) {
                ArrayList<Quiz> availableQuizzes = modifyCourseGUI.getCurrentCourse().getCourse();
                String[] quizNames = new String[availableQuizzes.size()];

                for (int i = 0; i < availableQuizzes.size(); i++) {
                    quizNames[i] = availableQuizzes.get(i).getName();
                }

                try {
                    String quizName = (String) JOptionPane.showInputDialog(modifyCourseGUI,
                            "Please select a quiz to delete", "Select Quiz?", JOptionPane.QUESTION_MESSAGE, null,
                            quizNames, quizNames[0]);

                    int quizNumber = 0;
                    for (int i = 0; i < availableQuizzes.size(); i++) {
                        if (quizName.equals(availableQuizzes.get(i).getName())) {
                            quizNumber = i;
                            break;
                        }
                    }
                    // Remove on client side
                    modifyCourseGUI.getCurrentCourse().getCourse().remove(quizNumber);

                    // Remove on server side
                    modifyCourseGUI.saveCourse();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                // JOption Pane with dropdown to select available quizzes
            }
            if (e.getSource() == viewGradeQuizButton) {
                // Open View Submissions GUI
                ViewGradesGUI viewGradesGUI = new ViewGradesGUI(modifyCourseGUI.getCurrentCourse(), socket, oos, ois);
                SwingUtilities.invokeLater(viewGradesGUI);
            }
            if (e.getSource() == exitButton) {
            }
        }
    };

    // Methods:
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ModifyCourseGUI());
    }

    public void run() {
        JFrame frame = new JFrame("Modify Course");

        modifyCourseGUI = new ModifyCourseGUI(socket, oos, ois, currentCourse);

        Container content = frame.getContentPane();

        content.setLayout(new BorderLayout());

        frame.setSize(300, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        // Top Panel;
        welcomeToCourseLabel = new JLabel("Welcome to " + modifyCourseGUI.getCurrentCourse().getName());
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        topPanel.add(welcomeToCourseLabel);
        topPanel.setPreferredSize(new Dimension(300, 75));
        content.add(topPanel, BorderLayout.NORTH);

        // Center Panel
        createQuizButton = new JButton("Create Quiz");
        createQuizButton.addActionListener(actionListener);

        editQuizButton = new JButton("Edit Quiz");
        editQuizButton.addActionListener(actionListener);

        deleteQuizButton = new JButton("Delete Quiz");
        deleteQuizButton.addActionListener(actionListener);

        viewGradeQuizButton = new JButton("View/Grade Submissions");
        viewGradeQuizButton.addActionListener(actionListener);

        exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));
        panel.add(createQuizButton);
        panel.add(editQuizButton);
        panel.add(deleteQuizButton);
        panel.add(viewGradeQuizButton);
        panel.add(exitButton);
        content.add(panel, BorderLayout.CENTER);

    }

    // Constructor to temporarily start the GUI with fake data
    public ModifyCourseGUI() {
        // Test Cases to develop the GUI before the server is up

        ArrayList<Question> questionList1 = new ArrayList<>();
        questionList1.add(
                new Question("MC", "A", "Test MC Question", "A", "B", "C", "D"));
        questionList1.add(new Question("TF", "F", "Test TF Question", "T", "F", null, null));
        questionList1.add(new Question("FIB", "CS18000", "Test FIB Question", null, null, null, null));

        Quiz currentQuiz1 = new Quiz(questionList1, "Test Quiz 1");

        ArrayList<Question> questionList2 = new ArrayList<>();
        questionList2.add(
                new Question("MC", "A", "Test MC Question", "A", "B", "C", "D"));
        questionList2.add(new Question("TF", "F", "Test TF Question", "T", "F", null, null));
        questionList2.add(new Question("FIB", "CS18000", "Test FIB Question", null, null, null, null));

        Quiz currentQuiz2 = new Quiz(questionList2, "Test Quiz 2");

        ArrayList<Quiz> quizList = new ArrayList<>();
        quizList.add(currentQuiz1);
        quizList.add(currentQuiz2);

        currentCourse = new Course("Test Course", quizList);

    }

    // Constructor to be called from server
    // Requires the current course, a list of available quizzes for that course, and
    // a list of student submissions for the course
    // The submission list may have to be changed? I'm not sure how submissions will
    // be stored
    public ModifyCourseGUI(Socket socket, ObjectOutputStream oos, ObjectInputStream ois, Course currentCourse) {

        this.socket = socket;
        this.oos = oos;
        this.ois = ois;
        this.currentCourse = currentCourse;
        Course newCourse = receiveCurrentCourse();

    }

    public void setCurrentCourse(Course currentCourse) {
        this.currentCourse = currentCourse;
    }

    public void setSubmissionList(ArrayList<Submission> submissionList) {
        this.submissionList = submissionList;
    }

    public Course getCurrentCourse() {
        return currentCourse;
    }

    public Course receiveCurrentCourse() { // Method to Get the most current version of the current course from the
                                           // server. Use whenever loading course info

        try {
            // Read Course File
            oos.writeObject("GetCourse");
            oos.writeObject(currentCourse.getName());
            oos.flush();

            currentCourse = (Course) ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentCourse;
    }

    public ArrayList<Submission> getSubmissionList() {

        try {
            // Get Submissions from server
            oos.writeObject("GetSubmissions");
            oos.flush();

            Submission[] submissionArray = (Submission[]) ois.readObject();

            submissionList = new ArrayList<>();

            // Convert into submissionList
            for (int i = 0; i < submissionArray.length; i++) {
                submissionList.add(submissionArray[i]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return submissionList;
    }

    public void saveCourse() {

        try {
            oos.writeObject("SaveCourse");
            oos.writeObject(currentCourse);
            oos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
