import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.awt.FlowLayout;
import java.net.*;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * MainMenuGUI.java Class -- Project 5
 *
 * This GUI allows a student to view their grades.
 *
 * @author Luke Cooley, Logan Snelling, Mason Minnich, Anushka Gupta -- L07
 *
 * @version 05/02/22
 *
 */
public class ViewGradesGUI extends JComponent implements Runnable {
    private ArrayList<Submission> submissions;
    private Submission currentSubmission;
    private Course currentCourse;
    private Quiz currentQuiz;

    Socket socket;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    JButton closeButton;
    JButton editButton;
    JTextArea textArea;
    JFrame frame2 = new JFrame("Submission");

    public ViewGradesGUI(Course currentCourse, Socket socket, ObjectOutputStream oos, ObjectInputStream ois) {

        this.currentCourse = currentCourse;

        this.socket = socket;
        this.oos = oos;
        this.ois = ois;
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == editButton) {
                int numQuestions = currentSubmission.getAnswers().size();
                System.out.println(numQuestions);

                Integer[] options = new Integer[numQuestions];
                for (int i = 0; i < numQuestions; i++) {
                    options[i] = i + 1;
                }

                int questionNum = (int) JOptionPane.showInputDialog(null, "Select Question Number", "Edit Grade",
                        JOptionPane.PLAIN_MESSAGE, null, options, options[0]) - 1;

                double pointsEarned = currentSubmission.getAnswers().get(questionNum).getPointsEarned();
                double pointsAvailable = currentSubmission.getAnswers().get(questionNum).getPointValue();

                String pointsAvailableString = JOptionPane.showInputDialog(null, "Maximum Points Earned: ",
                        Double.toString(pointsAvailable));
                String pointsEarnedString = JOptionPane.showInputDialog(null, "Maximum Points Earned: ",
                        Double.toString(pointsEarned));

                try {
                    pointsEarned = Double.parseDouble(pointsEarnedString);
                    pointsAvailable = Double.parseDouble(pointsAvailableString);

                    Answer replaceAnswer = new Answer(questionNum,
                            currentSubmission.getAnswers().get(questionNum).getGivenAnswer(), pointsEarned,
                            pointsAvailable);
                    currentSubmission.getAnswers().set(questionNum, replaceAnswer);
                    currentSubmission.calculatePoints();

                    oos.writeObject("SaveSubmission");
                    oos.writeObject(currentSubmission);
                    oos.flush();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                textArea.setText(currentSubmission.toString(currentQuiz, currentCourse));
            }
        }
    };

    public void run() {
        // creating an array list of all submissions by current course
        submissions = getSubmissionList();
        ArrayList<Submission> courseSubmissions = new ArrayList<>();
        for (Submission submission : submissions) {
            if (currentCourse.getName().equals(submission.getCourseName())) {
                courseSubmissions.add(submission);
            }
        }

        // creating an object array of all submission ID's of current user
        Object[] options = new Object[submissions.size() - 1];
        for (int i = 0; i < courseSubmissions.size(); i++) {
            options[i] = courseSubmissions.get(i).getID();
        }

        // selecting submission
        Object selectedSubmission = JOptionPane.showInputDialog(null, "Select Submission",
                "View/Grade", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        // assigning the right submission to currentSubmission
        for (Submission userSubmission : courseSubmissions) {
            if (selectedSubmission.equals(userSubmission.getID())) {
                currentSubmission = userSubmission;
                break;
            }
        }

        // setting currentQuiz to the correct option based on the
        // submission ID
        for (int i = 0; i < currentCourse.getCourse().size(); i++) {
            if (currentSubmission.getQuizName().equals(currentCourse.getCourse().get(i).getName())) {
                currentQuiz = currentCourse.getCourse().get(i);
                break;
            }
        }

        displayQuiz();
    }

    public Quiz getCurrentQuiz() {
        return currentQuiz;
    }

    public void setCurrentQuiz(Quiz currentQuiz) {
        this.currentQuiz = currentQuiz;
    }

    public ArrayList<Submission> getSubmissionList() {
        ArrayList<Submission> submissionList = new ArrayList<>();

        try {
            oos.writeObject("GetSubmissions");
            oos.flush();

            Submission[] submissionArray = (Submission[]) ois.readObject();

            for (int i = 0; i < submissionArray.length; i++) {
                submissionList.add(submissionArray[i]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return submissionList;
    }

    private void displayQuiz() {
        // Display the window.
        frame2.setSize(600, 400);
        frame2.setLocationRelativeTo(null);
        frame2.setVisible(true);
        frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // set flow layout for the frame
        frame2.getContentPane().setLayout(new FlowLayout());

        textArea = new JTextArea(18, 50);
        textArea.setEditable(false);
        JScrollPane scrollableTextArea = new JScrollPane(textArea);

        // setting up the close and edit button
        closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame2.dispose();
            }
        });

        editButton = new JButton("Edit Grades");
        editButton.addActionListener(actionListener);
        JPanel closeEditPanel = new JPanel();
        closeEditPanel.setLayout(new BoxLayout(closeEditPanel, BoxLayout.X_AXIS));
        closeEditPanel.add(closeButton);
        closeEditPanel.add(editButton);

        Container content = frame2.getContentPane();

        content.setLayout(new BorderLayout());
        content.add(closeEditPanel, BorderLayout.SOUTH);

        textArea.setText(currentSubmission.toString(currentQuiz, currentCourse));

        frame2.getContentPane().add(scrollableTextArea);
    }
}
