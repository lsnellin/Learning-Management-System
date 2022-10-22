import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.net.Socket;

/**
 * LogInGUI Class -- Project 5
 *
 * This is a class that welcomes the user. It allows the user to log in or
 * create an account. It talks with the server
 * to either create the account or verify login information. It then opens the
 * main menu under the correct user.
 *
 * @author Luke Cooley, Logan Snelling, Mason Minnich, Anushka Gupta -- L07
 *
 * @version 04/28/22
 *
 */
public class LogInGUI extends JComponent implements Runnable {
    // GUI Fields
    JButton logInButtonStudent;
    JButton logInButtonTeacher;
    JButton createAccountStudentButton;
    JButton createAccountTeacherButton;
    JLabel usernameLabel;
    JLabel passwordLabel;
    JLabel fullNameLabel;
    JTextField fullNameTextField;
    JTextField usernameTextField;
    JTextField passwordTextField;

    JFrame frame;
    // Fields
    private ArrayList<User> users;

    User user;
    Socket socket;

    ObjectOutputStream oos;
    ObjectInputStream ois;
    String[] userInfo = new String[5];

    ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            // if student wants to log in, do this
            if (e.getSource() == logInButtonStudent) {
                String fullName = fullNameTextField.getText();
                String username = usernameTextField.getText();
                String password = passwordTextField.getText();
                boolean isTeacher = false;
                userInfo[0] = "login";
                userInfo[1] = fullName;
                userInfo[2] = username;
                userInfo[3] = password;
                userInfo[4] = Boolean.toString(isTeacher);
                if (password.indexOf(',') != -1 || password == null) {
                    JOptionPane.showMessageDialog(null, "Error with password", "Error!",
                            JOptionPane.ERROR_MESSAGE);
                } else if (fullName.indexOf(',') != -1 || fullName == null) {
                    JOptionPane.showMessageDialog(null, "Error with Name", "Error!",
                            JOptionPane.ERROR_MESSAGE);
                } else if (username.indexOf(',') != -1 || username == null) {
                    JOptionPane.showMessageDialog(null, "Error with username", "Error!",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    user = new User(fullName, username, password, isTeacher);
                    boolean logIn = writeServer("LogIn", user);
                    if ((Boolean) logIn == false) {
                        JOptionPane.showMessageDialog(null, "Log In Failed", "Error!",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        SwingUtilities.invokeLater(new MainMenuGUI(user, socket, oos, ois));
                        frame.dispose();
                    }
                }

            }
            // if teacher wants to log in, do this
            if (e.getSource() == logInButtonTeacher) {
                String fullName = fullNameTextField.getText();
                String username = usernameTextField.getText();
                String password = passwordTextField.getText();
                boolean isTeacher = true;
                userInfo[0] = "login";
                userInfo[1] = fullName;
                userInfo[2] = username;
                userInfo[3] = password;
                userInfo[4] = Boolean.toString(isTeacher);
                if (password.indexOf(',') != -1 || password == null) {
                    JOptionPane.showMessageDialog(null, "Error with password", "Error!",
                            JOptionPane.ERROR_MESSAGE);
                } else if (fullName.indexOf(',') != -1 || fullName == null) {
                    JOptionPane.showMessageDialog(null, "Error with Name", "Error!",
                            JOptionPane.ERROR_MESSAGE);
                } else if (username.indexOf(',') != -1 || username == null) {
                    JOptionPane.showMessageDialog(null, "Error with username", "Error!",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    user = new User(fullName, username, password, isTeacher);
                    boolean logIn = writeServer("LogIn", user);
                    if ((Boolean) logIn == false) {
                        JOptionPane.showMessageDialog(null, "Log In Failed", "Error!",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        SwingUtilities.invokeLater(new MainMenuGUI(user, socket, oos, ois));
                        frame.dispose();
                    }
                }
            }

            // if student wants to create account, do this
            if (e.getSource() == createAccountStudentButton) {
                String fullName = fullNameTextField.getText();
                String username = usernameTextField.getText();
                String password = passwordTextField.getText();
                boolean isTeacher = false;
                userInfo[0] = "create";
                userInfo[1] = fullName;
                userInfo[2] = username;
                userInfo[3] = password;
                userInfo[4] = Boolean.toString(isTeacher);
                if (password.indexOf(',') != -1 || password == null || password.equals("")) {
                    JOptionPane.showMessageDialog(null, "Error with password", "Error!",
                            JOptionPane.ERROR_MESSAGE);
                } else if (fullName.indexOf(',') != -1 || fullName == null || fullName.equals("")) {
                    JOptionPane.showMessageDialog(null, "Error with Name", "Error!",
                            JOptionPane.ERROR_MESSAGE);
                } else if (username.indexOf(',') != -1 || username == null || username.equals("")) {
                    JOptionPane.showMessageDialog(null, "Error with username", "Error!",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    user = new User(fullName, username, password, isTeacher);
                    boolean logIn = writeServer("CreateAccount", user);
                    if ((Boolean) logIn == false) {
                        JOptionPane.showMessageDialog(null, "Please try again", "Error!",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        SwingUtilities.invokeLater(new MainMenuGUI(user, socket, oos, ois));
                        frame.dispose();
                    }
                }

            }
            // if teacher wants to create account, do this
            if (e.getSource() == createAccountTeacherButton) {
                String fullName = fullNameTextField.getText();
                String username = usernameTextField.getText();
                String password = passwordTextField.getText();
                boolean isTeacher = true;
                userInfo[0] = "create";
                userInfo[1] = fullName;
                userInfo[2] = username;
                userInfo[3] = password;
                userInfo[4] = Boolean.toString(isTeacher);
                if (password.indexOf(',') != -1 || password == null || password.equals("")) {
                    JOptionPane.showMessageDialog(null, "Error with password", "Error!",
                            JOptionPane.ERROR_MESSAGE);
                } else if (fullName.indexOf(',') != -1 || fullName == null || fullName.equals("")) {
                    JOptionPane.showMessageDialog(null, "Error with Name", "Error!",
                            JOptionPane.ERROR_MESSAGE);
                } else if (username.indexOf(',') != -1 || username == null || username.equals("")) {
                    JOptionPane.showMessageDialog(null, "Error with username", "Error!",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    user = new User(fullName, username, password, isTeacher);
                    boolean logIn = writeServer("CreateAccount", user);
                    if ((Boolean) logIn == false) {
                        JOptionPane.showMessageDialog(null, "Please try again", "Error!",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        SwingUtilities.invokeLater(new MainMenuGUI(user, socket, oos, ois));
                        frame.dispose();
                    }
                }
            }
        }
    };

    // Methods:
    public static void main(String[] args) {

        SwingUtilities.invokeLater(new LogInGUI());
    }

    public void run() {
        // filling up the array of already-existing users

        Object[] options = { "Log in!", "Create Account" };

        JButton logInButton; // button for login
        int option1 = JOptionPane.showOptionDialog(null,
                "Welcome! Would you like to log in or create an account?",
                "BrightSpace",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (option1 == JOptionPane.YES_OPTION) {
            // menu to log in
            frame = new JFrame("Log In");

            Container content = frame.getContentPane();

            content.setLayout(new BorderLayout());

            frame.setSize(500, 150);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);

            logInButtonStudent = new JButton("Log In (Student)");
            logInButtonStudent.addActionListener(actionListener);

            logInButtonTeacher = new JButton("Log In (Teacher)");
            logInButtonTeacher.addActionListener(actionListener);

            fullNameLabel = new JLabel("Full Name: ");
            fullNameTextField = new JTextField(25);

            usernameLabel = new JLabel("Username: ");
            usernameTextField = new JTextField(15);

            passwordLabel = new JLabel("Password: ");
            passwordTextField = new JPasswordField(15);

            JPanel panel = new JPanel();
            panel.add(fullNameLabel);
            panel.add(fullNameTextField);

            JPanel panel2 = new JPanel();
            panel2.add(usernameLabel);
            panel2.add(usernameTextField);
            panel2.add(passwordLabel);
            panel2.add(passwordTextField);

            JPanel panel3 = new JPanel();
            panel3.add(logInButtonStudent);
            panel3.add(logInButtonTeacher);

            content.add(panel, BorderLayout.NORTH);
            content.add(panel2, BorderLayout.CENTER);
            content.add(panel3, BorderLayout.SOUTH);

        } else {
            // make create account instead if needed
            frame = new JFrame("Create Account");

            Container content = frame.getContentPane();

            content.setLayout(new BorderLayout());

            frame.setSize(500, 150);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setVisible(true);

            createAccountStudentButton = new JButton("Create Account (Student)");
            createAccountStudentButton.addActionListener(actionListener);

            createAccountTeacherButton = new JButton("Create Account (Teacher)");
            createAccountTeacherButton.addActionListener(actionListener);

            fullNameLabel = new JLabel("Full Name: ");
            fullNameTextField = new JTextField(25);

            usernameLabel = new JLabel("Username: ");
            usernameTextField = new JTextField(15);

            passwordLabel = new JLabel("Password: ");
            passwordTextField = new JPasswordField(15);

            JPanel panel = new JPanel();
            panel.add(fullNameLabel);
            panel.add(fullNameTextField);

            JPanel panel2 = new JPanel();
            panel2.add(usernameLabel);
            panel2.add(usernameTextField);
            panel2.add(passwordLabel);
            panel2.add(passwordTextField);

            JPanel panel3 = new JPanel();
            panel3.add(createAccountStudentButton);
            panel3.add(createAccountTeacherButton);

            content.add(panel, BorderLayout.NORTH);
            content.add(panel2, BorderLayout.CENTER);
            content.add(panel3, BorderLayout.SOUTH);

        }

    }

    // constructor to open the GUI with socket, oos, and ois
    public LogInGUI(Socket socket, ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream) {
        this.socket = socket;
        this.oos = objectOutputStream;
        this.ois = objectInputStream;

    }

    // empty constructor
    public LogInGUI() {

    }

    public String[] getUserLogin() {
        return userInfo;
    }

    // method to write to the server
    public boolean writeServer(String instructions, User user) {
        try {

            oos.writeObject(instructions);
            oos.writeObject(user);
            oos.flush();

            Object object1 = ois.readObject();
            if ((Boolean) object1 == true) {
                return true;
            } else {
                return false;
            }

        } catch (IOException f) {
            f.printStackTrace();
            return false;
        } catch (ClassNotFoundException f) {
            f.printStackTrace();
            return false;
        } catch (Exception f) {
            f.printStackTrace();
            return false;
        }
    }

}
