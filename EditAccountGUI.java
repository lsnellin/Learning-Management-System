import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * EditAccountGUI Class -- Project 5
 *
 * This is a class that allows the user to edit an account and talks with the
 * server to change their account.
 * It opens the main menu back up with the edited user
 *
 * @author Luke Cooley, Logan Snelling, Mason Minnich, Anushka Gupta -- L07
 *
 * @version 04/28/22
 *
 */

public class EditAccountGUI extends JComponent implements Runnable {
    // Fields:
    JButton editAccountTeacher;
    JButton editAccountStudent;
    JLabel usernameLabel;
    JLabel passwordLabel;
    JLabel fullNameLabel;
    JTextField fullNameTextField;
    JTextField usernameTextField;
    JTextField passwordTextField;

    User currentUser;
    User newUser;

    String name;
    String username;
    String password;
    boolean isTeacher;

    Socket socket;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    JFrame frame;

    String[] userInfo = new String[4];

    ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            // if edit account student, do this
            if (e.getSource() == editAccountStudent) {
                String fullName = fullNameTextField.getText();
                String username = usernameTextField.getText();
                String password = passwordTextField.getText();
                boolean isTeacher = false;
                userInfo[0] = fullName;
                userInfo[1] = username;
                userInfo[2] = password;
                userInfo[3] = Boolean.toString(isTeacher);
                if (password.indexOf(',') != -1 | password == null) {
                    JOptionPane.showMessageDialog(null, "Error with password", "Error!",
                            JOptionPane.ERROR_MESSAGE);
                } else if (fullName.indexOf(',') != -1 | fullName == null) {
                    JOptionPane.showMessageDialog(null, "Error with Name", "Error!",
                            JOptionPane.ERROR_MESSAGE);
                } else if (username.indexOf(',') != -1 | username == null) {
                    JOptionPane.showMessageDialog(null, "Error with username", "Error!",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    newUser = new User(fullName, username, password, isTeacher);
                    boolean logIn = writeServer("EditStudent", currentUser, newUser);
                    if ((Boolean) logIn == false) {
                        JOptionPane.showMessageDialog(null, "Please try again", "Error!",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        SwingUtilities.invokeLater(new MainMenuGUI(newUser, socket, oos, ois));
                        frame.dispose();
                    }
                }

            }
            // if edit account teacher, do this
            if (e.getSource() == editAccountTeacher) {
                String fullName = fullNameTextField.getText();
                String username = usernameTextField.getText();
                String password = passwordTextField.getText();
                boolean isTeacher = true;
                userInfo[0] = fullName;
                userInfo[1] = username;
                userInfo[2] = password;
                userInfo[3] = Boolean.toString(isTeacher);
                if (password.indexOf(',') != -1 | password == null) {
                    JOptionPane.showMessageDialog(null, "Error with password", "Error!",
                            JOptionPane.ERROR_MESSAGE);
                } else if (fullName.indexOf(',') != -1 | fullName == null) {
                    JOptionPane.showMessageDialog(null, "Error with Name", "Error!",
                            JOptionPane.ERROR_MESSAGE);
                } else if (username.indexOf(',') != -1 | username == null) {
                    JOptionPane.showMessageDialog(null, "Error with username", "Error!",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    newUser = new User(fullName, username, password, isTeacher);
                    boolean logIn = writeServer("EditTeacher", currentUser, newUser);
                    if ((Boolean) logIn == false) {
                        JOptionPane.showMessageDialog(null, "Please try again", "Error!",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        SwingUtilities.invokeLater(new MainMenuGUI(newUser, socket, oos, ois));
                        frame.dispose();
                    }
                }
            }
        }
    };

    // main method to run GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new EditAccountGUI());
    }

    public void run() {
        // menu to log in
        frame = new JFrame("Edit Account");

        Container content = frame.getContentPane();

        content.setLayout(new BorderLayout());
        frame.setSize(500, 150);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        editAccountTeacher = new JButton("Edit Your Account (Teacher)");
        editAccountTeacher.addActionListener(actionListener);

        editAccountStudent = new JButton("Edit Your Account (Student)");
        editAccountStudent.addActionListener(actionListener);

        fullNameLabel = new JLabel("Full Name: ");
        fullNameTextField = new JTextField(this.name, 25);

        usernameLabel = new JLabel("Username: ");
        usernameTextField = new JTextField(this.username, 15);

        passwordLabel = new JLabel("Password: ");
        passwordTextField = new JPasswordField(this.password, 15);

        JPanel panel = new JPanel();
        panel.add(fullNameLabel);
        panel.add(fullNameTextField);

        JPanel panel2 = new JPanel();
        panel2.add(usernameLabel);
        panel2.add(usernameTextField);
        panel2.add(passwordLabel);
        panel2.add(passwordTextField);

        JPanel panel3 = new JPanel();

        panel3.add(editAccountStudent);
        panel3.add(editAccountTeacher);

        content.add(panel, BorderLayout.NORTH);
        content.add(panel2, BorderLayout.CENTER);
        content.add(panel3, BorderLayout.SOUTH);

    }

    // constructor for this GUI to call it from main menu
    public EditAccountGUI(User currentUser, Socket socket, ObjectOutputStream oos, ObjectInputStream ois) {
        this.currentUser = currentUser;
        this.username = currentUser.getUsername();
        this.name = currentUser.getName();
        this.password = currentUser.getPassword();
        this.isTeacher = currentUser.getIsTeacher();
        this.socket = socket;
        this.oos = oos;
        this.ois = ois;
    }

    // empty constructor
    public EditAccountGUI() {

    }

    // get user login method
    public String[] getUserLogin(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        return userInfo;
    }

    // method to write to the server
    public boolean writeServer(String instructions, User oldUser, User newUser) {
        try {
            oos.writeObject(instructions);
            oos.writeObject(oldUser);
            oos.writeObject(newUser);
            oos.flush();

            Object object1 = ois.readObject();
            if ((Boolean) object1 == true) {
                return true;
            } else {
                return false;
            }

        } catch (Exception f) {
            f.printStackTrace();
            return false;
        }
    }
}
