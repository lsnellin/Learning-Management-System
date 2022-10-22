import java.time.Instant;
import java.lang.Math;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Session Class -- Project 4
 *
 * This is a class that contains all the information needed to guide the user
 * through the learning management system to manage quizzes
 *
 * @author Luke Cooley, Logan Snelling, Mason Minnich, Anushka Gupta -- L07
 *
 * @version 04/11/22
 *
 */
public class Session {
    private User user;
    private ArrayList<Course> courses;
    private Course currentCourse;
    private Quiz currentQuiz;
    // read through Submission_List.txt and generate a submission object for each
    // submission
    // private static ArrayList<Submission> submissions =
    // Submission.readSubmissionList();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<User> userList = new ArrayList<>();
        String userSelection; // Variable to keep track of which option the user selects in the terminal
        User user;
        String name;
        String username;
        String password;
        boolean isTeacher;

        Session session = new Session(null);

        // Populate userList arraylist with all user data from file
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("User_info.txt")));
            String s;
            ArrayList<String> array = new ArrayList<>();
            while ((s = br.readLine()) != null) {
                array.add(s);
            }
            br.close();
            for (String a : array) {

                String[] info = a.split(",");

                String newName = info[0];
                String newUsername = info[1];
                String newPassWord = info[2];
                boolean newIsTeacher;
                if (info[3].equals("true")) {
                    newIsTeacher = true;
                } else {
                    newIsTeacher = false;
                }
                userList.add(new User(newName, newUsername, newPassWord, newIsTeacher));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        do {
            // Welcome the user to the program and allow them to log in or create a new
            // account
            System.out.println("Hello, welcome to the learning management homepage, please select an option:");
            System.out.println("1. Login");
            System.out.println("2. Create New Account");
            System.out.println("3. Quit");

            userSelection = scanner.nextLine();

            // If user decides to log in, prompt for username and password
            if (userSelection.equals("1")) {

                boolean found; // TF variable relating to whether or not the username and password were both
                               // found in the file
                do {
                    // Prompt
                    System.out.println("Enter name:");
                    name = scanner.nextLine();
                    System.out.println("Enter username:");
                    username = scanner.nextLine();

                    // Check for commas in password that would interfere with storing user data
                    do {
                        System.out.println("Enter password:");
                        password = scanner.nextLine();

                        if (password.indexOf(',') != -1) {
                            System.out.println("Error! Password cannot contain a comma!");
                        }
                    } while (password.indexOf(',') != -1);

                    System.out.println("Are you a teacher(0) or student(1)?");
                    isTeacher = (scanner.nextLine().equals("0"));

                    user = new User(name, username, password, isTeacher);

                    found = false;
                    for (int i = 0; i < userList.size(); i++) {
                        if (user.getUserID().equals(userList.get(i).getUserID())) {
                            found = true;
                        }
                    }

                    if (found) {
                        System.out.println("Welcome, " + user.getName() + "!");
                    } else {
                        System.out.println("Error! Incorrect username or password. Options: ");
                        System.out.println("1. Try again");
                        System.out.println("2. Quit");

                        userSelection = scanner.nextLine();
                    }
                } while (!found && !userSelection.equals("2"));

            } else if (userSelection.equals("2")) {
                // Call createNewUser method
                user = session.createNewUser(scanner, userList);

            } else if (!userSelection.equals("3")) {
                user = null;
                System.out.println("Error! Please select a valid option");
                continue;
            } else {
                user = null;
                return;
            }

            // Once User is found or created, update the session's user:
            session.setUser(user);
            // Session session = new Session(user);

            if (session.getUser().getIsTeacher()) {
                // Menu for teachers
                do {
                    System.out.println("Please select an option: ");
                    System.out.println("1. Create Course");
                    System.out.println("2. Modify Course");
                    System.out.println("3. Edit Account");
                    System.out.println("4. Delete Account");
                    System.out.println("5. Logout");
                    userSelection = scanner.nextLine();

                    if (userSelection.equals("1")) {
                        System.out.println("Enter the name of the course");
                        String courseName = scanner.nextLine();
                        Course newCourse = new Course(courseName, new ArrayList<>());
                        newCourse.saveCourse();
                        session.setCurrentCourse(newCourse);
                        System.out.println("Course Created");
                        session.setCourses(session.readCourseListFile());

                    } else if (userSelection.equals("2")) {
                        session.selectCourse(scanner);

                        do {
                            System.out.println("Options");
                            System.out.println("1. Create Quiz"); // Teacher
                            System.out.println("2. Edit Quiz"); // Teacher
                            System.out.println("3. Delete Quiz");
                            System.out.println("4. View / Grade Submissions");
                            System.out.println("5. Exit");

                            userSelection = scanner.nextLine();

                            if (userSelection.equals("1")) {
                                session.constructQuiz(scanner);
                            } else if (userSelection.equals("2")) {
                                session.selectQuiz(scanner);
                                session.modifyQuestion(scanner);
                                session.getCurrentCourse().removeQuiz(session.getCurrentQuiz().getName());
                                session.getCurrentCourse().getCourse().add(session.getCurrentQuiz());
                                session.getCurrentCourse().saveCourse();
                            } else if (userSelection.equals("3")) {
                                session.selectQuiz(scanner);
                                session.getCurrentCourse().removeQuiz(session.getCurrentQuiz().getName());
                                session.getCurrentCourse().saveCourse();
                            }
                            /*
                             * else if (userSelection.equals("4")) {
                             * String submissionString;
                             * String studentUsername;
                             * String filename;
                             * 
                             * session.selectQuiz(scanner);
                             * System.out.println("Enter the student username");
                             * studentUsername = scanner.nextLine();
                             * 
                             * filename = studentUsername + session.getCurrentQuiz().getName() + ".txt";
                             * 
                             * submissionString = session.getCurrentQuiz().readSubmission(filename);
                             * System.out.println(studentUsername + "'s Submission: ");
                             * System.out.println();
                             * System.out.println(submissionString);
                             * 
                             * do {
                             * // Ask Teacher to modify student's grade
                             * System.out.println("Would you like to modify the student's grade? ");
                             * System.out.println("1. Yes");
                             * System.out.println("2. No");
                             * 
                             * userSelection = scanner.nextLine();
                             * 
                             * if (userSelection.equals("1")) {
                             * System.out.println("Enter student's updated grade:");
                             * double newGrade = scanner.nextDouble();
                             * scanner.nextLine();
                             * 
                             * session.getCurrentQuiz().modifyGrade(newGrade, filename);
                             * 
                             * } else if (userSelection.equals("2")) {
                             * System.out.println("Error! please select a valid option");
                             * }
                             * 
                             * } while (!userSelection.equals("2"));
                             * 
                             * }
                             */
                            else if (userSelection.equals("4")) {
                                String studentUsername;

                                Submission viewedSub = null;
                                do {
                                    session.selectQuiz(scanner);
                                    System.out.println("Enter the student username");
                                    studentUsername = scanner.nextLine();

                                    String ID = studentUsername + "," + session.getCurrentQuiz().getName();

                                    // this is commented out to avoid errors
                                    // since we're using a GUI now the text based stuff shouldn't ever matter
                                    /*
                                     * for (Submission submission : submissions) {
                                     * if (submission.getID().equals(ID)) {
                                     * viewedSub = submission;
                                     * break;
                                     * }
                                     * }
                                     */
                                    if (viewedSub == null) {
                                        System.out.println("Error! The user has not taken that quiz.");
                                    }
                                } while (viewedSub != null);

                                // String s = viewedSub.toString(session.currentQuiz, session.currentCourse);
                                // System.out.println(s);

                                do {
                                    // Ask Teacher to modify student's grade
                                    System.out.println("Would you like to modify the student's grade? ");
                                    System.out.println("1. Yes");
                                    System.out.println("2. No");

                                    userSelection = scanner.nextLine();

                                    // (need to be able to set specific point values for each answer)
                                    if (userSelection.equals("1")) {
                                        System.out.println("Enter student's updated point value (grade):");
                                        viewedSub.setPoints(scanner.nextDouble());
                                        scanner.nextLine();

                                    } else if (userSelection.equals("2")) {
                                        System.out.println("Error! please select a valid option");
                                    }

                                } while (!userSelection.equals("2"));

                            } else if (!userSelection.equals("5")) {
                                System.out.println("Error! Please select a valid option");
                            }

                        } while (!userSelection.equals("5"));

                    } else if (userSelection.equals("3")) {
                        user = session.createNewUser(scanner, userList);
                        session.getUser().deleteUserData();
                        session.setUser(user);

                    } else if (userSelection.equals("4")) {
                        System.out.println("Are you sure you want to delete your account? You will be logged out");
                        System.out.println("1. Yes");
                        System.out.println("2. No");
                        int studentSelection = scanner.nextInt();
                        scanner.nextLine();

                        if (studentSelection == 1) {
                            session.getUser().deleteUserData();
                        } else {
                            // Update userSelection so user is not logged out
                            userSelection = "0";
                        }

                    } else if (!userSelection.equals("5")) {
                        System.out.println("Error! Please select a valid option");
                    }

                } while (!userSelection.equals("4") && !userSelection.equals("5"));
            } else {
                do { // Menu For Students
                    System.out.println("Please select an option: ");
                    System.out.println("1. Select Course");
                    System.out.println("2. Delete Account");
                    System.out.println("3. Edit Account");
                    System.out.println("4. Logout");
                    if (!scanner.hasNext()) {
                        break;
                    }

                    userSelection = scanner.nextLine();

                    if (userSelection.equals("1")) {
                        session.selectCourse(scanner);
                        session.selectQuiz(scanner);

                        System.out.println("Would you like to start the quiz?");
                        System.out.println("1. Start Quiz");
                        System.out.println("2. Exit");

                        int studentSelection = scanner.nextInt();
                        scanner.nextLine();

                        if (studentSelection == 1) {
                            Submission submission = session.takeQuiz(scanner);
                            submission.saveSubmission();
                            // submissions.add(submission);
                        }

                    } else if (userSelection.equals("2")) {
                        System.out.println("Are you sure you want to delete your account? You will be logged out");
                        System.out.println("1. Yes");
                        System.out.println("2. No");
                        int studentSelection = scanner.nextInt();
                        scanner.nextLine();

                        if (studentSelection == 1) {
                            session.getUser().deleteUserData();
                        } else {
                            // Update userSelection so user is not logged out
                            userSelection = "0";
                        }

                    } else if (userSelection.equals("3")) {
                        user = session.createNewUser(scanner, userList);
                        session.getUser().deleteUserData();
                        session.setUser(user);

                    } else if (!userSelection.equals("4")) {
                        System.out.println("Error! Please select a valid option");
                    }
                } while (!userSelection.equals("4") && !userSelection.equals("2"));
            }

        } while (!userSelection.equals("3"));

        System.out.println("Goodbye, thank you!");

    }

    public Session(User user) {
        this.user = user;
        courses = readCourseListFile();
        currentCourse = null;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public User getUser() {
        return user;
    }

    public Course getCurrentCourse() {
        return currentCourse;
    }

    public Quiz getCurrentQuiz() {
        return currentQuiz;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCurrentCourse(Course currentCourse) {
        this.currentCourse = currentCourse;
    }

    public void setCurrentQuiz(Quiz currentQuiz) {
        this.currentQuiz = currentQuiz;
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

        for (int i = 0; i < courseNames.size(); i++) {
            String courseName = courseNames.get(i);

            Course course = new Course(courseName);
            courseList.add(course);

        }

        return courseList;
    }

    public void selectCourse(Scanner scanner) {
        boolean courseFound = false;
        do {
            System.out.println("Enter the course name. Available courses:");
            for (int i = 0; i < getCourses().size(); i++) {
                System.out.println(getCourses().get(i).getName());
            }
            String currentCourseName = scanner.nextLine();

            for (int i = 0; i < getCourses().size(); i++) {
                if (currentCourseName.equalsIgnoreCase(getCourses().get(i).getName())) {
                    setCurrentCourse(getCourses().get(i));
                    courseFound = true;
                }
            }
            if (!courseFound) {
                System.out.println("Error! Course not found");
            }

        } while (!courseFound);

    }

    public void selectQuiz(Scanner scanner) {

        boolean quizFound = false;
        do {
            System.out.println("Enter a quiz name. Available quizzes:");
            for (int i = 0; i < getCurrentCourse().getCourse().size(); i++) {
                System.out.println(getCurrentCourse().getCourse().get(i).getName());
            }

            String quizName = scanner.nextLine();

            for (int i = 0; i < getCurrentCourse().getCourse().size(); i++) {
                if (quizName.equalsIgnoreCase(getCurrentCourse().getCourse().get(i).getName())) {
                    setCurrentQuiz(getCurrentCourse().getCourse().get(i));
                    quizFound = true;
                }
            }
        } while (!quizFound);

    }

    public void constructQuiz(Scanner scanner) {
        String userSelection;

        do { // Import file or prompt teacher for questions
            System.out.println("How would you like to create the quiz?");
            System.out.println("1. Create quiz manually");
            System.out.println("2. Import quiz from file");

            userSelection = scanner.nextLine();
            if (userSelection.equals("1")) {
                String quizName;

                System.out.println("Enter a name for the quiz");
                quizName = scanner.nextLine();

                Quiz quiz = new Quiz(null, quizName); // new quiz object with no questions
                ArrayList<Question> questionList = new ArrayList<>(); // Array list of questions

                boolean moreQuestions;
                do { // Loop to add questions to quiz as long as the teacher wants to add more

                    questionList.add(createQuestion(scanner));

                    // Give the teacher a chance to input more questions:
                    System.out.println("Would you like to enter another question? (Y/N)");
                    String YN = scanner.nextLine().toUpperCase();

                    moreQuestions = YN.equals("Y");

                } while (moreQuestions); // repeat if more questions need to be added

                quiz.setQuiz(questionList);
                getCurrentCourse().getCourse().add(quiz);
                getCurrentCourse().saveCourse();

            } else if (userSelection.equals("2")) {
                System.out.println("Enter the filename:");
                String fileName = scanner.nextLine(); // Get name of file

                importQuizFromFile(fileName);

            } else {
                System.out.println("Error! Please select a valid option");
            }

        } while (!userSelection.equals("1") && !userSelection.equals("2"));

    }

    public void importQuizFromFile(String fileName) {
        try (BufferedReader bfr = new BufferedReader(new FileReader(fileName))) {
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
            } while (!line.equals("--Quiz End--"));

            setCurrentQuiz(new Quiz(questionList, quizName));

        } catch (FileNotFoundException e) {
            System.out.println("Error! File not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Question createQuestion(Scanner scanner) {
        Question newQuestion;
        boolean validType; // TF Variable if the question type is valid
        String questionType; // Type of question MC, TF, FIB
        String question;
        String correctAnswer;
        String optionA = null;
        String optionB = null;
        String optionC = null;
        String optionD = null;

        do { // Get question type
            System.out.println("Enter Question type:");
            System.out.println("Multiple choice (MC)");
            System.out.println("True / False (TF)");
            System.out.println("Fill in the blank (FIB)");

            questionType = scanner.nextLine().toUpperCase(); // Get question type and convert to UC

            if (questionType.equals("MC") || questionType.equals("TF")
                    || questionType.equals("FIB")) { // Set valid type true if type equals MF,TF,FIB
                validType = true;
            } else {
                System.out.println("Error! Please select a valid question type");
                validType = false;
            }
        } while (!validType); // Repeat if question type is invalid

        System.out.println("Enter the question:");
        question = scanner.nextLine();

        // If it is a multiple choice question, allow user to input all the options
        if (questionType.equals("MC")) {
            System.out.println("Enter option A:");
            optionA = scanner.nextLine();

            System.out.println("Enter option B:");
            optionB = scanner.nextLine();

            System.out.println("Enter option C:");
            optionC = scanner.nextLine();

            System.out.println("Enter option D:");
            optionD = scanner.nextLine();
        }

        System.out.println("Enter the correct answer");
        correctAnswer = scanner.nextLine();

        newQuestion = new Question(questionType, correctAnswer, question, optionA, optionB, optionC, optionD);

        return newQuestion;

    }

    public void modifyQuestion(Scanner scanner) {
        // Initialize Variables
        Quiz currentQuiz = getCurrentQuiz();
        int numQuestions = currentQuiz.getQuiz().size();
        int questionNumber;

        // Get number of question to modify
        do {
            System.out.println("Enter the question number: (1 - " + numQuestions + ")");
            questionNumber = scanner.nextInt();
            scanner.nextLine();

            if (!(questionNumber > 0 && questionNumber <= numQuestions)) {
                System.out.println("Error! Enter a valid question number");
            }
        } while (!(questionNumber > 0 && questionNumber <= numQuestions));

        // Get New Question
        Question newQ = createQuestion(scanner);

        // Replace desired question with new question using Quiz.modifyQuiz()
        getCurrentQuiz().modifyQuiz(questionNumber, newQ.getType(), newQ.getCorrectAnswer(), newQ.getQuestion(),
                newQ.getOptionA(), newQ.getOptionB(), newQ.getOptionC(), newQ.getOptionD());
    }

    public Submission takeQuiz(Scanner scanner) {
        // Initialize Variables
        ArrayList<Question> quizQuestions = currentQuiz.getQuiz();
        ArrayList<Answer> answers = new ArrayList<>();
        String questionType;

        // Get time before quiz
        Instant instant1 = Instant.now();

        // Take each quiz question
        for (int i = 0; i < quizQuestions.size(); i++) {
            System.out.println("Question #" + (i + 1));

            questionType = quizQuestions.get(i).getType();
            System.out.println("(" + questionType + ")");
            System.out.println(quizQuestions.get(i).getQuestion());

            if (questionType.equals("MC")) {
                System.out.println("A: " + quizQuestions.get(i).getOptionA());
                System.out.println("B: " + quizQuestions.get(i).getOptionB());
                System.out.println("C: " + quizQuestions.get(i).getOptionC());
                System.out.println("D: " + quizQuestions.get(i).getOptionD());
            }

            // code to allow the input of files for an answer
            boolean keepGoing = true;
            do {
                System.out.println("Do you want to input a file for your answer? If yes, input 1. If no, input 0");
                String userSelection = scanner.nextLine();
                if (userSelection.equals("1")) {
                    do {
                        System.out.println("Please input the filename that contains your answer!");
                        String filename = scanner.nextLine();
                        File answerFile = new File(filename);
                        if (answerFile.exists()) {
                            keepGoing = false;
                            try (BufferedReader bfr = new BufferedReader(new FileReader(filename))) {
                                String line;
                                line = bfr.readLine();
                                System.out.println("You answered: " + line);
                                answers.add(new Answer(i, line, line.equals(quizQuestions.get(i).getCorrectAnswer())));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println("Please input a valid file name. File not found.");
                        }
                    } while (keepGoing);

                    keepGoing = false;

                } else if (userSelection.equals("0")) {
                    keepGoing = false;
                    System.out.println("Please input your answer below: ");
                    String line;
                    line = scanner.nextLine();
                    answers.add(new Answer(i, line, line.equals(quizQuestions.get(i).getCorrectAnswer())));
                } else {
                    System.out.println("Invalid input!");
                    keepGoing = true;
                }
            } while (keepGoing);

        }
        // Get time after quiz and calculate/round time taken to complete quiz
        Instant instant2 = Instant.now();
        double quizTime = (instant2.getEpochSecond() - instant1.getEpochSecond()) / (double) 60;
        quizTime = Math.round(quizTime * 100.0) / 100.0;

        System.out.println("Quiz Complete!");
        System.out.println("Quiz time (Minutes): " + quizTime);

        // return the submission object
        return (new Submission(
                getUser().getUsername(),
                getCurrentCourse().getName(),
                getCurrentQuiz().getName(),
                answers,
                quizTime));
    }

    /*
     * public void takeQuiz(Scanner scanner) {
     * // Initialize Variables
     * ArrayList<Question> quizQuestions = currentQuiz.getQuiz();
     * ArrayList<String> answers = new ArrayList<>();
     * String questionType;
     * int questionNumber = 1;
     * 
     * // Get time before quiz
     * Instant instant1 = Instant.now();
     * 
     * // Take each quiz question
     * for (int i = 0; i < quizQuestions.size(); i++) {
     * System.out.println("Question #" + questionNumber++);
     * 
     * questionType = quizQuestions.get(i).getType();
     * System.out.println("(" + questionType + ")");
     * System.out.println(quizQuestions.get(i).getQuestion());
     * 
     * if (questionType.equals("MC")) {
     * System.out.println("A: " + quizQuestions.get(i).getOptionA());
     * System.out.println("B: " + quizQuestions.get(i).getOptionB());
     * System.out.println("C: " + quizQuestions.get(i).getOptionC());
     * System.out.println("D: " + quizQuestions.get(i).getOptionD());
     * }
     * 
     * // code to allow the input of files for an answer
     * boolean keepGoing = true;
     * do {
     * System.out.
     * println("Do you want to input a file for your answer? If yes, input 1. If no, input 0"
     * );
     * String userSelection = scanner.nextLine();
     * if (userSelection.equals("1")) {
     * do {
     * System.out.println("Please input the filename that contains your answer!");
     * String filename = scanner.nextLine();
     * File answerFile = new File(filename);
     * if (answerFile.exists()) {
     * keepGoing = false;
     * try (BufferedReader bfr = new BufferedReader(new FileReader(filename))) {
     * String line;
     * line = bfr.readLine();
     * System.out.println("You answered: " + line);
     * answers.add(line);
     * } catch (IOException e) {
     * e.printStackTrace();
     * }
     * } else {
     * System.out.println("Please input a valid file name. File not found.");
     * }
     * } while (keepGoing);
     * 
     * keepGoing = false;
     * 
     * } else if (userSelection.equals("0")) {
     * keepGoing = false;
     * System.out.println("Please input your answer below: ");
     * answers.add(scanner.nextLine());
     * } else {
     * System.out.println("Invalid input!");
     * keepGoing = true;
     * }
     * } while (keepGoing);
     * 
     * }
     * // Get time after quiz and calculate/round time taken to complete quiz
     * Instant instant2 = Instant.now();
     * double quizTime = (instant2.getEpochSecond() - instant1.getEpochSecond()) /
     * (double) 60;
     * quizTime = Math.round(quizTime * 100.0) / 100.0;
     * 
     * System.out.println("Quiz Complete!");
     * System.out.println("Quiz time (Minutes): " + quizTime);
     * 
     * // Save submission
     * currentQuiz.saveSubmission(getUser().getUsername(), answers, quizTime);
     * }
     */

    public User createNewUser(Scanner scanner, ArrayList<User> userList) {
        User userHere;
        String name;
        String username;
        String password;
        boolean isTeacher;
        int userSelection = -1;

        boolean found; // TF variable relating to whether or not the username and password were both
                       // found in the file
        do {
            // Prompt
            System.out.println("Enter name:");
            name = scanner.nextLine();
            System.out.println("Enter username:");
            username = scanner.nextLine();

            // Check for commas in password that would interfere with storing user data
            do {
                System.out.println("Enter password:");
                password = scanner.nextLine();

                if (password.indexOf(',') != -1) {
                    System.out.println("Error! Password cannot contain a comma!");
                }
            } while (password.indexOf(',') != -1);

            System.out.println("Are you a teacher(0) or student(1)?");
            isTeacher = (scanner.nextInt() == 0);
            scanner.nextLine();

            userHere = new User(name, username, password, isTeacher);

            found = false;
            for (int i = 0; i < userList.size(); i++) {
                if (userHere.getUserID().equals(userList.get(i).getUserID())) {
                    found = true;
                }
            }

            if (!found) {
                userList.add(userHere);
                System.out.println("Account Successfully Created");
                userHere.writeUserData();
                System.out.println("Welcome, " + userHere.getName() + "!");
            } else {
                System.out.println("Error! User Already Exists. Options: ");
                System.out.println("1. Try again");
                System.out.println("2. Quit");

                userSelection = scanner.nextInt();
                scanner.nextLine();
            }
        } while (found && userSelection != 2);

        return userHere;

    }

    public ArrayList<Submission> readSubmissionList() {
        ArrayList<Submission> submissions = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("Submission_List.txt"));
            String line;
            ArrayList<String> submissionLines;
            do {
                line = br.readLine();
                submissionLines = new ArrayList<>();
                if (line.equals("--Start Submission--")) {
                    do {
                        line = br.readLine();
                        submissionLines.add(line);
                    } while (!line.equals("--End Submission--"));
                    submissions.add(new Submission(submissionLines));
                }
            } while (!line.equals("--End Submission List--"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return submissions;
    }
}
