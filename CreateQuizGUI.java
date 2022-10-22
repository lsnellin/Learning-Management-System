import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * CreateQuizGUI Class -- Project 5
 *
 * This is a class that creates a GUI that allows teachers to create quizzes
 * with TF, MC, and FIB questions
 *
 * @author Luke Cooley, Logan Snelling, Mason Minnich, Anushka Gupta -- L07
 *
 * @version 05/02/22
 *
 */
public class CreateQuizGUI extends JComponent implements Runnable {
    // Fields
    Socket socket;
    ObjectOutputStream oos;
    ObjectInputStream ois;

    // Top Panel
    JButton saveButton;
    JButton importButton;
    JButton exitButton;

    // Left Panel
    JButton addQuestionButton;
    JButton deleteQuestionButton;

    // Right Panel
    JRadioButton multipleChoiceButton;
    JRadioButton trueFalseButton;
    JRadioButton fillInBlankButton;

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
    JButton editChoicesButton;

    // Quiz GUI
    CreateQuizGUI createQuizGUI;
    private Quiz currentQuiz;
    private int quizNumber;
    private Course currentCourse;
    private int currentQuestionNum;
    private boolean createNewQuiz; // Boolean for if the user wants to create a new quiz or edit the current one

    // Action Listener which deals with every button on GUI
    ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == addQuestionButton) { // Add Question

                int questionNum = createQuizGUI.addQuestion(); // Adds a new question to the quiz and returns the number
                                                               // of questions

                // Update the question navigator
                String text = createQuizGUI.getCurrentQuestionNum() + 1 + "/";
                text += Integer.toString(questionNum);

                questionNumberText.setText(text);

                // Allow user to delete questions, since there will be more than one question
                deleteQuestionButton.setEnabled(true);
            }

            if (e.getSource() == previousQuestionButton) {
                currentQuestionNum = createQuizGUI.getCurrentQuestionNum();
                // Saves current question and updates the current question if the user is not on
                // the first question
                if (currentQuestionNum + 1 > 1) {
                    saveCurrentQuestion();
                    currentQuestionNum--;
                    createQuizGUI.setCurrentQuestionNum(currentQuestionNum);
                    updateQuestionInfo();
                }

                String text = createQuizGUI.getCurrentQuestionNum() + 1 + "/"
                        + createQuizGUI.getCurrentQuiz().getQuiz().size();
                questionNumberText.setText(text);
            }
            if (e.getSource() == nextQuestionButton) {
                currentQuestionNum = createQuizGUI.getCurrentQuestionNum();
                // Saves current question and updates the current question if the user is not on
                // the last question
                if (currentQuestionNum + 1 < createQuizGUI.getCurrentQuiz().getQuiz().size()) {
                    saveCurrentQuestion();
                    currentQuestionNum++;
                    createQuizGUI.setCurrentQuestionNum(currentQuestionNum);
                    updateQuestionInfo();
                }

                String text = createQuizGUI.getCurrentQuestionNum() + 1 + "/"
                        + createQuizGUI.getCurrentQuiz().getQuiz().size();
                questionNumberText.setText(text);

            }

            if (e.getSource() == createQuizGUI.getFillInBlankButton()) {
                updateQuestionTexts(true, false, false, false, false);

                createQuizGUI.getCurrentQuiz().getQuiz().get(createQuizGUI.getCurrentQuestionNum()).setType("FIB");
                createQuizGUI.getEditChoicesButton().setVisible(false);
            }

            if (e.getSource() == createQuizGUI.getMultipleChoiceButton()) {
                updateQuestionTexts(false, true, true, true, true);
                createQuizGUI.getOptionA().setText("A");
                createQuizGUI.getOptionB().setText("B");
                createQuizGUI.getOptionC().setText("C");
                createQuizGUI.getOptionD().setText("D");

                createQuizGUI.getCurrentQuiz().getQuiz().get(createQuizGUI.getCurrentQuestionNum()).setType("MC");
                createQuizGUI.getEditChoicesButton().setVisible(true);

            }

            if (e.getSource() == createQuizGUI.getTrueFalseButton()) {

                updateQuestionTexts(false, true, true, false, false);
                createQuizGUI.getOptionA().setText("T");
                createQuizGUI.getOptionB().setText("F");

                createQuizGUI.getCurrentQuiz().getQuiz().get(createQuizGUI.getCurrentQuestionNum()).setType("TF");
                createQuizGUI.getEditChoicesButton().setVisible(false);

            }

            if (e.getSource() == createQuizGUI.getEditChoicesButton()) {
                String[] letters = new String[4];
                letters[0] = "A";
                letters[1] = "B";
                letters[2] = "C";
                letters[3] = "D";

                try {
                    String choiceLetter = (String) JOptionPane.showInputDialog(createQuizGUI,
                            "Choose which option to edit: ", "Choose a Letter", JOptionPane.QUESTION_MESSAGE, null,
                            letters, letters[0]);

                    String newOption = JOptionPane.showInputDialog(createQuizGUI, "Enter the new choice", "New Choice",
                            JOptionPane.QUESTION_MESSAGE);

                    if (choiceLetter.equals("A")) {
                        createQuizGUI.getOptionA().setText(choiceLetter + ": " + newOption);
                    } else if (choiceLetter.equals("B")) {
                        createQuizGUI.getOptionB().setText(choiceLetter + ": " + newOption);
                    } else if (choiceLetter.equals("C")) {
                        createQuizGUI.getOptionC().setText(choiceLetter + ": " + newOption);
                    } else if (choiceLetter.equals("D")) {
                        createQuizGUI.getOptionD().setText(choiceLetter + ": " + newOption);
                    }
                } catch (Exception er) {
                    er.printStackTrace();
                }

            }
            if (e.getSource() == deleteQuestionButton) {
                deleteCurrentQuestion();
                if (createQuizGUI.getCurrentQuiz().getQuiz().size() == 1) {
                    deleteQuestionButton.setEnabled(false);
                }
            }
            if (e.getSource() == importButton) {
                JFileChooser chooseFile = new JFileChooser();
                chooseFile.setCurrentDirectory(new File(System.getProperty("user.dir")));
                chooseFile.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int option = chooseFile.showOpenDialog(createQuizGUI);

                if (option == JFileChooser.APPROVE_OPTION) {

                    File quizFile = chooseFile.getSelectedFile();
                    createQuizGUI.importQuizFromFile(quizFile);
                    updateQuestionInfo();

                } else if (option != JFileChooser.CANCEL_OPTION) {
                    JOptionPane.showMessageDialog(createQuizGUI, "Error! Invalid File!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (e.getSource() == saveButton) {
                // Save Quiz
                saveCurrentQuestion();

                // If user has no quiz name, prompt for new name
                if (createQuizGUI.getCurrentQuiz().getName().equals("Insert Name")) {
                    String quizName = JOptionPane.showInputDialog(createQuizGUI, "Enter the quiz name");
                    createQuizGUI.getCurrentQuiz().setName(quizName);
                }

                if (createNewQuiz) {
                    System.out.println("Here");
                    createQuizGUI.getCurrentCourse().getCourse().add(createQuizGUI.getCurrentQuiz());
                } else {
                    createQuizGUI.getCurrentCourse().getCourse().set(createQuizGUI.getQuizNumber(),
                            createQuizGUI.getCurrentQuiz());
                }

                saveCourse();

                // Save and exit quiz, and replace current quiz
                JOptionPane.showMessageDialog(createQuizGUI, "Quiz Saved!", "Saved",
                        JOptionPane.INFORMATION_MESSAGE);

                createQuizGUI.setCreateNewQuiz(false);

                exitButton.doClick();
            }
        }
    };

    // Deletes the current question and switches to another question
    public void deleteCurrentQuestion() {
        int questionNum = createQuizGUI.getCurrentQuestionNum();

        if (questionNum == 0) {
            createQuizGUI.getCurrentQuiz().getQuiz().remove(questionNum);
            updateQuestionInfo();
            previousQuestionButton.doClick(); // Update the question number label, but does not actually go to the
                                              // previous question since it's the beginning of the quiz
        } else {
            previousQuestionButton.doClick();
            createQuizGUI.getCurrentQuiz().getQuiz().remove(questionNum);
            nextQuestionButton.doClick();
        }

    }

    // Saves the current question info and updates the current quiz
    public void saveCurrentQuestion() {
        String type; // type of question (MC, TF, or FIB)
        String correctAnswer; // if MC (A,B,C, or D) if TF (T or F) if FIB (a word/words)
        String question; // the question or statement
        String optionA; // if multiple choice (option A)
        String optionB; // if multiple choice (option B)
        String optionC; // if multiple choice (option C)
        String optionD; // if multiple choice (option D)

        // If question is MC, get the selected button
        if (createQuizGUI.getMultipleChoiceButton().isSelected()) {
            type = "MC";
            if (createQuizGUI.getOptionA().isSelected()) {
                correctAnswer = "A";
            } else if (createQuizGUI.getOptionB().isSelected()) {
                correctAnswer = "B";
            } else if (createQuizGUI.getOptionC().isSelected()) {
                correctAnswer = "C";
            } else if (createQuizGUI.getOptionD().isSelected()) {
                correctAnswer = "D";
            } else {
                correctAnswer = null;
            }

            // If question is TF, get selected button
        } else if (createQuizGUI.getTrueFalseButton().isSelected()) {
            type = "TF";
            if (createQuizGUI.getOptionA().isSelected()) {
                correctAnswer = "T";
            } else if (createQuizGUI.getOptionB().isSelected()) {
                correctAnswer = "F";
            } else {
                correctAnswer = null;
            }

            // If FIB, get word answer
        } else if (createQuizGUI.getFillInBlankButton().isSelected()) {
            type = "FIB";
            correctAnswer = createQuizGUI.getQuestionAnswerPane().getText();
        } else {
            type = null;
            correctAnswer = null;
        }

        question = createQuizGUI.getQuestionTextPane().getText();
        optionA = createQuizGUI.getOptionA().getText();
        optionB = createQuizGUI.getOptionB().getText();
        optionC = createQuizGUI.getOptionC().getText();
        optionD = createQuizGUI.getOptionD().getText();

        Question newQuestion = new Question(type, correctAnswer, question, optionA, optionB, optionC, optionD);

        createQuizGUI.getCurrentQuiz().getQuiz().set(createQuizGUI.getCurrentQuestionNum(), newQuestion);

    }

    // Update Fields available based off the question type
    public void updateQuestionTexts(Boolean answerText, boolean A, boolean B, boolean C, boolean D) {

        createQuizGUI.getQuestionAnswerPane().setVisible(answerText);

        createQuizGUI.getOptionA().setVisible(A);
        createQuizGUI.getOptionB().setVisible(B);
        createQuizGUI.getOptionC().setVisible(C);
        createQuizGUI.getOptionD().setVisible(D);
    }

    // Update the information within the available fields based off the current
    // question
    public void updateQuestionInfo() {
        String type = createQuizGUI.getCurrentQuiz().getQuiz().get(createQuizGUI.getCurrentQuestionNum()).getType();
        String questionAnswer = createQuizGUI.getCurrentQuiz().getQuiz().get(getCurrentQuestionNum())
                .getCorrectAnswer();

        if (type.equals("MC")) {
            updateQuestionTexts(false, true, true, true, true);

            if (questionAnswer.equals("A")) {
                createQuizGUI.getOptionA().setSelected(true);
            } else if (questionAnswer.equals("B")) {
                createQuizGUI.getOptionB().setSelected(true);
            } else if (questionAnswer.equals("C")) {
                createQuizGUI.getOptionC().setSelected(true);
            } else if (questionAnswer.equals("D")) {
                createQuizGUI.getOptionD().setSelected(true);
            }

            createQuizGUI.getMultipleChoiceButton().setSelected(true);
            createQuizGUI.getEditChoicesButton().setVisible(true);

        } else if (type.equals("TF")) {
            updateQuestionTexts(false, true, true, false, false);
            createQuizGUI.getTrueFalseButton().setSelected(true);

            if (questionAnswer.equals("T")) {
                createQuizGUI.getOptionA().setSelected(true);
            } else if (questionAnswer.equals("F")) {
                createQuizGUI.getOptionB().setSelected(true);
            }

            // createQuizGUI.getOptionA().setText("T");
            // createQuizGUI.getOptionB().setText("F");
            createQuizGUI.getEditChoicesButton().setVisible(false);

        } else if (type.equals("FIB")) {
            updateQuestionTexts(true, false, false, false, false);

            createQuizGUI.getQuestionAnswerPane().setText(questionAnswer);

            createQuizGUI.getFillInBlankButton().setSelected(true);
            createQuizGUI.getEditChoicesButton().setVisible(false);
        }

        String question = createQuizGUI.getCurrentQuiz().getQuiz().get(createQuizGUI.getCurrentQuestionNum())
                .getQuestion();
        createQuizGUI.getQuestionTextPane().setText(question);

        String optionA = createQuizGUI.getCurrentQuiz().getQuiz().get(getCurrentQuestionNum())
                .getOptionA();
        createQuizGUI.getOptionA().setText(optionA);

        String optionB = createQuizGUI.getCurrentQuiz().getQuiz().get(getCurrentQuestionNum())
                .getOptionB();
        createQuizGUI.getOptionB().setText(optionB);

        String optionC = createQuizGUI.getCurrentQuiz().getQuiz().get(getCurrentQuestionNum())
                .getOptionC();
        createQuizGUI.getOptionC().setText(optionC);

        String optionD = createQuizGUI.getCurrentQuiz().getQuiz().get(getCurrentQuestionNum())
                .getOptionD();
        createQuizGUI.getOptionD().setText(optionD);

        if (type.equals("TF")) {
            createQuizGUI.getOptionA().setText("T");
            createQuizGUI.getOptionB().setText("F");
        }
    }

    // Adds a blank new multiple choice question to the current quiz
    public int addQuestion() {
        ArrayList<Question> newQuestionList = currentQuiz.getQuiz();
        newQuestionList.add(new Question("MC", "A", "Insert Question", "A", "B", "C", "D"));
        currentQuiz.setQuiz(newQuestionList);

        return newQuestionList.size();
    }

    // Run method which constructs the GUI and adds action listeners to each
    // component
    public void run() {

        JFrame frame = new JFrame("Quiz Creator");

        Container content = frame.getContentPane();

        content.setLayout(new BorderLayout());
        createQuizGUI = new CreateQuizGUI(currentQuiz, currentCourse, socket, oos, ois, quizNumber);
        content.add(createQuizGUI, BorderLayout.CENTER);

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        // Top Panel for File operations
        saveButton = new JButton("Save Quiz");
        saveButton.addActionListener(actionListener);

        importButton = new JButton("Import Quiz");
        importButton.addActionListener(actionListener);

        exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        JPanel topPanel = new JPanel();
        topPanel.add(saveButton);
        topPanel.add(importButton);
        topPanel.add(exitButton);
        content.add(topPanel, BorderLayout.NORTH);

        // Left Panel For Question operations
        addQuestionButton = new JButton("Add Question");
        addQuestionButton.addActionListener(actionListener);
        addQuestionButton.setAlignmentY(Component.CENTER_ALIGNMENT);

        deleteQuestionButton = new JButton("Delete Question");
        deleteQuestionButton.addActionListener(actionListener);
        deleteQuestionButton.setAlignmentY(Component.CENTER_ALIGNMENT);

        if (createQuizGUI.getCurrentQuiz().getQuiz().size() == 1) {
            deleteQuestionButton.setEnabled(false);
        }

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(7, 1));
        leftPanel.add(Box.createVerticalStrut(50));
        leftPanel.add(addQuestionButton);
        leftPanel.add(deleteQuestionButton);
        // leftPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        content.add(leftPanel, BorderLayout.WEST);

        // Bottom Panel to navigate through the quiz
        previousQuestionButton = new JButton("<");
        previousQuestionButton.addActionListener(actionListener);

        String questionNumber = createQuizGUI.getCurrentQuestionNum() + 1 + "/"
                + createQuizGUI.getCurrentQuiz().getQuiz().size();
        questionNumberText = new JLabel(questionNumber);

        nextQuestionButton = new JButton(">");
        nextQuestionButton.addActionListener(actionListener);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(previousQuestionButton);
        bottomPanel.add(questionNumberText);
        bottomPanel.add(nextQuestionButton);
        content.add(bottomPanel, BorderLayout.SOUTH);

        // Right Panel To Modify Current Question
        JRadioButton multipleChoiceButton = new JRadioButton("Multiple Choice");
        multipleChoiceButton.addActionListener(actionListener);
        createQuizGUI.setMultipleChoiceButton(multipleChoiceButton);

        JRadioButton trueFalseButton = new JRadioButton("True or False");
        trueFalseButton.addActionListener(actionListener);
        createQuizGUI.setTrueFalseButton(trueFalseButton);

        JRadioButton fillInBlankButton = new JRadioButton("Fill in the Blank");
        fillInBlankButton.addActionListener(actionListener);
        createQuizGUI.setFillInBlankButton(fillInBlankButton);

        ButtonGroup questionType = new ButtonGroup();
        questionType.add(createQuizGUI.getMultipleChoiceButton());
        questionType.add(createQuizGUI.getTrueFalseButton());
        questionType.add(createQuizGUI.getFillInBlankButton());

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(11, 0));
        rightPanel.add(Box.createVerticalBox());
        rightPanel.add(Box.createVerticalBox());
        rightPanel.add(new JLabel("Question Type: "));
        rightPanel.add(createQuizGUI.getMultipleChoiceButton());
        rightPanel.add(createQuizGUI.getTrueFalseButton());
        rightPanel.add(createQuizGUI.getFillInBlankButton());
        content.add(rightPanel, BorderLayout.EAST);

        // Middle Panel to view quiz
        createQuizGUI.setQuestionTextPane(new JTextPane());
        createQuizGUI.setQuestionAnswerPane(new JTextPane()); // For FIB questions
        createQuizGUI.setOptionA(new JRadioButton());
        createQuizGUI.getOptionA().addActionListener(actionListener);

        createQuizGUI.setOptionB(new JRadioButton());
        createQuizGUI.getOptionB().addActionListener(actionListener);

        createQuizGUI.setOptionC(new JRadioButton());
        createQuizGUI.getOptionC().addActionListener(actionListener);

        createQuizGUI.setOptionD(new JRadioButton());
        createQuizGUI.getOptionD().addActionListener(actionListener);

        JButton editChoicesButton = new JButton("Edit Choices");
        editChoicesButton.addActionListener(actionListener);
        createQuizGUI.setEditChoicesButton(editChoicesButton);
        createQuizGUI.getEditChoicesButton().setVisible(false);

        ButtonGroup multipleChoiceButtons = new ButtonGroup();
        multipleChoiceButtons.add(createQuizGUI.getOptionA());
        multipleChoiceButtons.add(createQuizGUI.getOptionB());
        multipleChoiceButtons.add(createQuizGUI.getOptionC());
        multipleChoiceButtons.add(createQuizGUI.getOptionD());

        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new GridLayout(9, 1));
        middlePanel.add(questionLabel);
        middlePanel.add(createQuizGUI.getQuestionTextPane());
        middlePanel.add(answerLabel);
        middlePanel.add(createQuizGUI.getQuestionAnswerPane());
        middlePanel.add(createQuizGUI.getOptionA());
        middlePanel.add(createQuizGUI.getOptionB());
        middlePanel.add(createQuizGUI.getOptionC());
        middlePanel.add(createQuizGUI.getOptionD());
        middlePanel.add(createQuizGUI.getEditChoicesButton());
        content.add(middlePanel, BorderLayout.CENTER);

        // Hide all question buttons until a type is selected
        updateQuestionTexts(false, false, false, false, false);
        updateQuestionInfo();

    }

    // Constructor for creating a new quiz from scratch
    public CreateQuizGUI(Course currentCourse, Socket socket, ObjectOutputStream oos, ObjectInputStream ois) {

        this.socket = socket;
        this.oos = oos;
        this.ois = ois;

        createNewQuiz = true;
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(new Question("MC", "A", "Insert Question", "A", "B", "C", "D"));
        currentQuiz = new Quiz(questions, "Insert Name");
        this.currentCourse = currentCourse;
        currentQuestionNum = 0;
    }

    // Constructor for starting with a quiz
    public CreateQuizGUI(Quiz currentQuiz, Course currentCourse, Socket socket, ObjectOutputStream oos,
            ObjectInputStream ois, int quizNumber) {

        this.socket = socket;
        this.oos = oos;
        this.ois = ois;

        createNewQuiz = false;
        this.currentQuiz = currentQuiz;
        this.quizNumber = quizNumber;
        this.currentCourse = currentCourse;
        currentQuestionNum = 0;
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

    public void setCurrentQuestionNum(int currentQuestionNum) {
        this.currentQuestionNum = currentQuestionNum;
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

    public JRadioButton getMultipleChoiceButton() {
        return multipleChoiceButton;
    }

    public JRadioButton getTrueFalseButton() {
        return trueFalseButton;
    }

    public JRadioButton getFillInBlankButton() {
        return fillInBlankButton;
    }

    public JButton getEditChoicesButton() {
        return editChoicesButton;
    }

    public void setMultipleChoiceButton(JRadioButton multipleChoiceButton) {
        this.multipleChoiceButton = multipleChoiceButton;
    }

    public void setFillInBlankButton(JRadioButton fillInBlankButton) {
        this.fillInBlankButton = fillInBlankButton;
    }

    public void setTrueFalseButton(JRadioButton trueFalseButton) {
        this.trueFalseButton = trueFalseButton;
    }

    public void setEditChoicesButton(JButton editChoicesButton) {
        this.editChoicesButton = editChoicesButton;
    }

    public void setCreateNewQuiz(boolean createNewQuiz) {
        this.createNewQuiz = createNewQuiz;
    }

    public boolean getCreateNewQuiz() {
        return createNewQuiz;
    }

    public Course getCurrentCourse() {
        return currentCourse;
    }

    public int getQuizNumber() {
        return quizNumber;
    }

    public void saveCourse() {

        try {
            oos.writeObject("SaveCourse");
            oos.writeObject(getCurrentCourse());
            oos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void importQuizFromFile(File quizFile) {
        try (BufferedReader bfr = new BufferedReader(new FileReader(quizFile))) {
            String line;
            String quizName;
            String questionType;
            ArrayList<Question> questionList = new ArrayList<>();

            quizName = bfr.readLine();
            line = bfr.readLine();

            do {
                if (line.equals("--Question Start--")) {
                    questionType = bfr.readLine();
                    if (questionType.equals("MC")) {
                        questionList.add(new Question(questionType,
                                bfr.readLine(), // correct answer
                                bfr.readLine(), // question
                                bfr.readLine(), // option A
                                bfr.readLine(), // option B
                                bfr.readLine(), // option C
                                bfr.readLine()));// option D
                    } else {
                        questionList.add(new Question(questionType,
                                bfr.readLine(), // correct answer
                                bfr.readLine(), // question
                                null, null, null, null)); // 4 options
                    }
                }
                line = bfr.readLine();
            } while (!line.equals("--Quiz End--"));

            Quiz newQuiz = new Quiz(questionList, quizName);

            setCurrentQuiz(newQuiz);

            previousQuestionButton.doClick();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
