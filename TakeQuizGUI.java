import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * TakeQuizGUI Class -- Project 5
 *
 * This is a class that creates a GUI that allows students to access and take
 * quizzes
 * 
 * @author Luke Cooley, Logan Snelling, Mason Minnich, Anushka Gupta -- L07
 *
 * @version 05/02/22
 *
 */
public class TakeQuizGUI extends JComponent implements Runnable {
    // Fields
    Socket socket;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    // Top Panel
    JButton submitButton;
    JButton importButton;
    JButton exitButton;

    // Right Panel
    Timer timer;
    JLabel timerLabel;
    JLabel timerTime;

    // Bottom Panel
    JButton nextQuestionButton;
    JLabel questionNumberText;
    JButton previousQuestionButton;

    // Center Panel (Question)
    final JLabel questionLabel = new JLabel("Question:");
    JTextPane questionText;

    final JLabel answerLabel = new JLabel("Answer:");
    JTextPane questionAnswerText; // For Fill in the blank

    // Multiple Choice buttons
    JRadioButton buttonA;
    JRadioButton buttonB;
    JRadioButton buttonC;
    JRadioButton buttonD;

    // Quiz GUI
    int currentTime;
    TakeQuizGUI takeQuizGUI;
    private Quiz currentQuiz;
    private User currentUser;
    private Course currentCourse;
    private int currentQuestionNum;
    private ArrayList<Answer> answerList = new ArrayList<>();

    ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == previousQuestionButton) {
                currentQuestionNum = takeQuizGUI.getCurrentQuestionNum();
                // Saves current question and updates the current question if the user is not on
                // the first question
                if (currentQuestionNum + 1 > 1) {
                    saveCurrentQuestion();
                    currentQuestionNum--;
                    takeQuizGUI.setCurrentQuestionNum(currentQuestionNum);
                    updateQuestionInfo();
                }

                String text = takeQuizGUI.getCurrentQuestionNum() + 1 + "/"
                        + takeQuizGUI.getCurrentQuiz().getQuiz().size();
                questionNumberText.setText(text);
            }
            if (e.getSource() == nextQuestionButton) {
                currentQuestionNum = takeQuizGUI.getCurrentQuestionNum();
                // Saves current question and updates the current question if the user is not on
                // the last question
                if (currentQuestionNum + 1 < takeQuizGUI.getCurrentQuiz().getQuiz().size()) {
                    saveCurrentQuestion();
                    currentQuestionNum++;
                    takeQuizGUI.setCurrentQuestionNum(currentQuestionNum);
                    updateQuestionInfo();
                }

                String text = takeQuizGUI.getCurrentQuestionNum() + 1 + "/"
                        + takeQuizGUI.getCurrentQuiz().getQuiz().size();
                questionNumberText.setText(text);

            }
            if (e.getSource() == importButton) {
                JFileChooser chooseFile = new JFileChooser();
                chooseFile.setCurrentDirectory(new File(System.getProperty("user.dir")));
                chooseFile.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int option = chooseFile.showOpenDialog(takeQuizGUI);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File quizFile = chooseFile.getSelectedFile();

                    try (BufferedReader bfr = new BufferedReader(new FileReader(quizFile))) {
                        String line;
                        line = bfr.readLine();

                        String questionType = takeQuizGUI.getCurrentQuiz().getQuiz()
                                .get(takeQuizGUI.getCurrentQuestionNum()).getType();

                        if (questionType.equals("MC")) {
                            switch (line) {
                                case "A":
                                    takeQuizGUI.getOptionA().setSelected(true);
                                    break;
                                case "B":
                                    takeQuizGUI.getOptionB().setSelected(true);
                                    break;
                                case "C":
                                    takeQuizGUI.getOptionC().setSelected(true);
                                    break;
                                case "D":
                                    takeQuizGUI.getOptionD().setSelected(true);
                                    break;
                            }
                        } else if (questionType.equals("TF")) {
                            switch (line) {
                                case "T":
                                    takeQuizGUI.getOptionA().setSelected(true);
                                    break;
                                case "F":
                                    takeQuizGUI.getOptionB().setSelected(true);
                                    break;
                            }
                        } else if (questionType.equals("FIB")) {
                            takeQuizGUI.getQuestionAnswerPane().setText(line);
                        }

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                } else if (option != JFileChooser.CANCEL_OPTION) {
                    JOptionPane.showMessageDialog(takeQuizGUI, "Error! Invalid File!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (e.getSource() == timer) {
                String timeString;
                int time = takeQuizGUI.getCurrentTime();
                time++;
                takeQuizGUI.setCurrentTime(time);

                int secondsLeft = time % 60;
                int minutes = time / 60;
                int minutesLeft = minutes % 60;
                int hours = minutes / 60;

                if (secondsLeft > 9 && minutesLeft > 9) {
                    timeString = hours + ":" + minutesLeft + ":" + secondsLeft;

                } else if (secondsLeft > 9 && minutesLeft < 10) {
                    timeString = hours + ":0" + minutesLeft + ":" + secondsLeft;

                } else if (secondsLeft < 10 && minutesLeft > 9) {
                    timeString = hours + ":" + minutesLeft + ":0" + secondsLeft;

                } else {
                    timeString = hours + ":0" + minutesLeft + ":0" + secondsLeft;
                }
                // JOptionPane.showConfirmDialog(takeQuizGUI, timeString);
                timerTime.setText(timeString);
            }
            if (e.getSource() == submitButton) {
                timer.stop();

                Submission newSubmission = new Submission(takeQuizGUI.getCurrentUser().getUsername(),
                        takeQuizGUI.getCurrentCourse().getName(), takeQuizGUI.getCurrentQuiz().getName(),
                        takeQuizGUI.getAnswerList(), (double) takeQuizGUI.getCurrentTime());

                // Save Submission to Server
                try {
                    oos.writeObject("SaveSubmission");
                    oos.writeObject(newSubmission);
                    oos.flush();

                    JOptionPane.showMessageDialog(null, "Quiz Submitted!",
                            "Take Quiz", JOptionPane.INFORMATION_MESSAGE);

                    exitButton.doClick();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }
    };

    // Saves the current question info and updates the current quiz
    public void saveCurrentQuestion() {
        String givenAnswer = null;
        int questionNum = takeQuizGUI.getCurrentQuestionNum();
        Question currentQuestion = takeQuizGUI.getCurrentQuiz().getQuiz().get(questionNum);
        String correctAnswer = currentQuestion.getCorrectAnswer(); // if MC (A,B,C, or D) if TF (T or F) if FIB (a
                                                                   // word/words)

        String type = currentQuestion.getType();
        if (type.equals("MC")) {
            if (takeQuizGUI.getOptionA().isSelected()) {
                givenAnswer = "A";
            } else if (takeQuizGUI.getOptionB().isSelected()) {
                givenAnswer = "B";
            } else if (takeQuizGUI.getOptionC().isSelected()) {
                givenAnswer = "C";
            } else if (takeQuizGUI.getOptionD().isSelected()) {
                givenAnswer = "D";
            }

        } else if (type.equals("TF")) {
            if (takeQuizGUI.getOptionA().isSelected()) {
                givenAnswer = "T";
            } else if (takeQuizGUI.getOptionB().isSelected()) {
                givenAnswer = "F";
            }

        } else if (type.equals("FIB")) {
            givenAnswer = takeQuizGUI.getQuestionAnswerPane().getText();
        }
        if (givenAnswer != null) {
            boolean isCorrect = givenAnswer.equals(correctAnswer);
            takeQuizGUI.getAnswerList().set(questionNum, new Answer(questionNum, givenAnswer, isCorrect));

        } else {
            takeQuizGUI.getAnswerList().set(questionNum, new Answer(questionNum, givenAnswer, false));
        }

    }

    // Update Fields available based off the question type
    public void updateQuestionTexts(Boolean answerText, boolean A, boolean B, boolean C, boolean D) {

        takeQuizGUI.getQuestionAnswerPane().setVisible(answerText);

        takeQuizGUI.getOptionA().setVisible(A);
        takeQuizGUI.getOptionB().setVisible(B);
        takeQuizGUI.getOptionC().setVisible(C);
        takeQuizGUI.getOptionD().setVisible(D);
    }

    // Update the information within the available fields based off the current
    // question
    public void updateQuestionInfo() {
        String type = takeQuizGUI.getCurrentQuiz().getQuiz().get(takeQuizGUI.getCurrentQuestionNum()).getType();
        Answer currentAnswer = takeQuizGUI.getAnswerList().get(takeQuizGUI.getCurrentQuestionNum());

        String givenAnswer = null;

        if (type.equals("MC")) {
            updateQuestionTexts(false, true, true, true, true);
        } else if (type.equals("TF")) {
            updateQuestionTexts(false, true, true, false, false);
        } else if (type.equals("FIB")) {
            updateQuestionTexts(true, false, false, false, false);
        }

        givenAnswer = currentAnswer.getGivenAnswer();

        if (givenAnswer != null) {
            if (type.equals("MC")) {

                if (givenAnswer.equals("A")) {
                    takeQuizGUI.getOptionA().setSelected(true);
                } else if (givenAnswer.equals("B")) {
                    takeQuizGUI.getOptionB().setSelected(true);
                } else if (givenAnswer.equals("C")) {
                    takeQuizGUI.getOptionC().setSelected(true);
                } else if (givenAnswer.equals("D")) {
                    takeQuizGUI.getOptionD().setSelected(true);
                }

            } else if (type.equals("TF")) {

                if (givenAnswer.equals("T")) {
                    takeQuizGUI.getOptionA().setSelected(true);
                } else if (givenAnswer.equals("F")) {
                    takeQuizGUI.getOptionB().setSelected(true);
                }

                takeQuizGUI.getOptionA().setText("T");
                takeQuizGUI.getOptionB().setText("F");

            } else if (type.equals("FIB")) {
                takeQuizGUI.getQuestionAnswerPane().setText(givenAnswer);

            }

        } else {
            takeQuizGUI.getOptionA().setSelected(false);
            takeQuizGUI.getOptionB().setSelected(false);
            takeQuizGUI.getOptionC().setSelected(false);
            takeQuizGUI.getOptionD().setSelected(false);
        }

        String question = takeQuizGUI.getCurrentQuiz().getQuiz().get(takeQuizGUI.getCurrentQuestionNum())
                .getQuestion();
        takeQuizGUI.getQuestionTextPane().setText(question);

        String optionA = takeQuizGUI.getCurrentQuiz().getQuiz().get(getCurrentQuestionNum())
                .getOptionA();
        takeQuizGUI.getOptionA().setText(optionA);

        String optionB = takeQuizGUI.getCurrentQuiz().getQuiz().get(getCurrentQuestionNum())
                .getOptionB();
        takeQuizGUI.getOptionB().setText(optionB);

        String optionC = takeQuizGUI.getCurrentQuiz().getQuiz().get(getCurrentQuestionNum())
                .getOptionC();
        takeQuizGUI.getOptionC().setText(optionC);

        String optionD = takeQuizGUI.getCurrentQuiz().getQuiz().get(getCurrentQuestionNum())
                .getOptionD();
        takeQuizGUI.getOptionD().setText(optionD);

        if (type.equals("TF")) {
            takeQuizGUI.getOptionA().setText("T");
            takeQuizGUI.getOptionB().setText("F");
        }
    }

    // Methods

    // Run method which constructs the GUI and adds action listeners to each
    // component
    public void run() {
        JFrame frame = new JFrame("Quiz in Progress");

        Container content = frame.getContentPane();

        content.setLayout(new BorderLayout());
        takeQuizGUI = new TakeQuizGUI(currentQuiz, currentUser, currentCourse, socket, oos, ois);
        content.add(takeQuizGUI, BorderLayout.CENTER);

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        // Top Panel for File operations
        submitButton = new JButton("Submit Quiz");
        submitButton.addActionListener(actionListener);

        importButton = new JButton("Import Answers");
        importButton.addActionListener(actionListener);

        exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        JPanel topPanel = new JPanel();
        topPanel.add(submitButton);
        topPanel.add(importButton);
        topPanel.add(exitButton);
        content.add(topPanel, BorderLayout.NORTH);

        // Left Panel For Spacing
        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(50, 400));

        content.add(leftPanel, BorderLayout.WEST);

        // Bottom Panel to navigate through the quiz
        previousQuestionButton = new JButton("<");
        previousQuestionButton.addActionListener(actionListener);

        String questionNumber = takeQuizGUI.getCurrentQuestionNum() + 1 + "/"
                + takeQuizGUI.getCurrentQuiz().getQuiz().size();
        questionNumberText = new JLabel(questionNumber);

        nextQuestionButton = new JButton(">");
        nextQuestionButton.addActionListener(actionListener);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(previousQuestionButton);
        bottomPanel.add(questionNumberText);
        bottomPanel.add(nextQuestionButton);
        content.add(bottomPanel, BorderLayout.SOUTH);

        // Right Panel To Modify Current Question
        timer = new Timer(1000, actionListener);
        timerLabel = new JLabel("Time Elapsed: ");
        timerTime = new JLabel("");

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(11, 1));
        rightPanel.add(timerLabel);
        rightPanel.add(timerTime);
        rightPanel.setPreferredSize(new Dimension(100, 400));
        content.add(rightPanel, BorderLayout.EAST);

        // Middle Panel to view quiz
        takeQuizGUI.setQuestionTextPane(new JTextPane());
        takeQuizGUI.getQuestionTextPane().setEditable(false);
        takeQuizGUI.setQuestionAnswerPane(new JTextPane()); // For FIB questions
        takeQuizGUI.setOptionA(new JRadioButton());
        takeQuizGUI.getOptionA().addActionListener(actionListener);

        takeQuizGUI.setOptionB(new JRadioButton());
        takeQuizGUI.getOptionB().addActionListener(actionListener);

        takeQuizGUI.setOptionC(new JRadioButton());
        takeQuizGUI.getOptionC().addActionListener(actionListener);

        takeQuizGUI.setOptionD(new JRadioButton());
        takeQuizGUI.getOptionD().addActionListener(actionListener);

        ButtonGroup multipleChoiceButtons = new ButtonGroup();
        multipleChoiceButtons.add(takeQuizGUI.getOptionA());
        multipleChoiceButtons.add(takeQuizGUI.getOptionB());
        multipleChoiceButtons.add(takeQuizGUI.getOptionC());
        multipleChoiceButtons.add(takeQuizGUI.getOptionD());

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new GridLayout(9, 1));
        middlePanel.add(questionLabel);
        middlePanel.add(takeQuizGUI.getQuestionTextPane());
        middlePanel.add(answerLabel);
        middlePanel.add(takeQuizGUI.getQuestionAnswerPane());
        middlePanel.add(takeQuizGUI.getOptionA());
        middlePanel.add(takeQuizGUI.getOptionB());
        middlePanel.add(takeQuizGUI.getOptionC());
        middlePanel.add(takeQuizGUI.getOptionD());
        content.add(middlePanel, BorderLayout.CENTER);

        timer.start();
        // Hide all question buttons until a type is selected
        updateQuestionTexts(false, false, false, false, false);
        updateQuestionInfo();

    }

    public TakeQuizGUI(Quiz currentQuiz, User currentUser, Course currentCourse, Socket socket, ObjectOutputStream oos,
            ObjectInputStream ois) {

        this.socket = socket;
        this.oos = oos;
        this.ois = ois;

        this.currentUser = currentUser;
        this.currentCourse = currentCourse;
        this.currentQuiz = currentQuiz;
        currentQuestionNum = 0;
        currentTime = 0;

        // Populate the answer array list with null answers, since the student hasn't
        // provided any answers at the beginning of the quiz
        ArrayList<Answer> answerList = new ArrayList<>();

        for (int i = 0; i < currentQuiz.getQuiz().size(); i++) {
            answerList.add(new Answer(i, null, false));
        }
        setAnswerList(answerList);
    }

    public Quiz getCurrentQuiz() {
        return currentQuiz;
    }

    public void setCurrentQuiz(Quiz currentQuiz) {
        this.currentQuiz = currentQuiz;
    }

    public int getCurrentQuestionNum() {
        return currentQuestionNum;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentQuestionNum(int currentQuestionNum) {
        this.currentQuestionNum = currentQuestionNum;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public JTextPane getQuestionTextPane() {
        return questionText;
    }

    public JTextPane getQuestionAnswerPane() {
        return questionAnswerText;
    }

    public JRadioButton getOptionA() {
        return buttonA;
    }

    public JRadioButton getOptionB() {
        return buttonB;
    }

    public JRadioButton getOptionC() {
        return buttonC;
    }

    public JRadioButton getOptionD() {
        return buttonD;
    }

    public void setQuestionTextPane(JTextPane questionText) {
        this.questionText = questionText;
    }

    public void setQuestionAnswerPane(JTextPane questionAnswerText) {
        this.questionAnswerText = questionAnswerText;
    }

    public void setOptionA(JRadioButton buttonA) {
        this.buttonA = buttonA;
    }

    public void setOptionB(JRadioButton buttonB) {
        this.buttonB = buttonB;
    }

    public void setOptionC(JRadioButton buttonC) {
        this.buttonC = buttonC;
    }

    public void setOptionD(JRadioButton buttonD) {
        this.buttonD = buttonD;
    }

    public void setAnswerList(ArrayList<Answer> answerList) {
        this.answerList = answerList;
    }

    public ArrayList<Answer> getAnswerList() {
        return answerList;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Course getCurrentCourse() {
        return currentCourse;
    }
}
