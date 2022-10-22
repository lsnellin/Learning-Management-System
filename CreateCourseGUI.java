import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * CreateQuizGUI Class -- Project 5
 *
 * This is a class that creates a GUI that allows teachers to create courses
 *
 * @author Luke Cooley, Logan Snelling, Mason Minnich, Anushka Gupta -- L07
 *
 * @version 05/02/22
 *
 */
public class CreateCourseGUI extends JComponent implements Runnable {
    Socket socket;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    public void run() {
        String courseName = JOptionPane.showInputDialog(null, "Enter the Course Name",
                "Create Course", JOptionPane.QUESTION_MESSAGE);

        if (!courseName.equals("")) {
            Course course = new Course(courseName, new ArrayList<>());

            try {
                oos.writeObject("CreateCourse");
                oos.writeObject(course);
                oos.flush();

                JOptionPane.showMessageDialog(null, "Course successfully created!",
                        "Create Course", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public CreateCourseGUI(Socket socket, ObjectOutputStream oos, ObjectInputStream ois) {
        this.socket = socket;
        this.oos = oos;
        this.ois = ois;
    }
}
