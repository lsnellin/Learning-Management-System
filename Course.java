import java.io.*;
import java.util.ArrayList;

/**
 * Course.java Class -- Project 5
 *
 * This is a class that stores a course and has methods to create them from a
 * file
 * and save them to a file, among other methods
 *
 * @author Luke Cooley, Logan Snelling, Mason Minnich, Anushka Gupta -- L07
 *
 * @version 05/02/22
 *
 */
public class Course implements Serializable {
    private String name; // The name of the course and course.txt file
    private ArrayList<Quiz> course; // ArrayList of quizzes
    /*
     * The following four variables are used for course.txt file formatting.
     * I used final variables, so you can change every instance of the string at
     * once.
     */
    public static final String QUIZ_START_STRING = "--Quiz Start--";
    public static final String QUESTION_START_STRING = "--Question Start--";
    public static final String QUIZ_END_STRING = "--Quiz End--";
    public static final String COURSE_END_STRING = "--Course End--";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Quiz> getCourse() {
        return course;
    }

    public void setCourse(ArrayList<Quiz> course) {
        this.course = course;
    }

    /*
     * constructs a new course given the name and initial quiz arraylist
     */
    public Course(String name, ArrayList<Quiz> quizzes) {
        this.name = name;
        this.course = quizzes;
    }

    /*
     * constructs a course by reading it from a course file
     * 
     * the name of the course file is "CourseName.txt"
     */
    public Course(String name) {
        this.name = name;
        try {
            FileReader reader = new FileReader(name + ".txt");
            BufferedReader bufferReader = new BufferedReader(reader);
            /*
             * -read from the course file according to the course file format
             * -the quiz and question constructors will need to be called here
             */
            String line;
            String quizName;
            String questionType;
            // ArrayList<Question> questions = new ArrayList<>();
            // ArrayList<Quiz> quizzes = new ArrayList<>();

            // this loop executes once per course, looping once for each quiz
            ArrayList<Quiz> quizzes = new ArrayList<>();
            while (true) {

                line = bufferReader.readLine();
                // System.out.println("\nCOURSE LOOP 1ST LINE = " + line);
                if (line.equals(COURSE_END_STRING)) {
                    break;
                } else if (line.equals(QUIZ_START_STRING)) {
                    ArrayList<Question> questions = new ArrayList<>();

                    quizName = bufferReader.readLine();
                    // System.out.println("successfully found quizName to be " + quizName);

                    // this loop executes once per quiz, looping once for each question
                    while (true) {
                        line = bufferReader.readLine();
                        // System.out.println("\nQUIZ LOOP 1ST LINE = " + line);
                        if (line.equals(QUIZ_END_STRING)) {
                            break;
                        } else if (line.equals(QUESTION_START_STRING)) {
                            questionType = bufferReader.readLine();
                            if (questionType.equals("MC")) {
                                questions.add(new Question(questionType,
                                        bufferReader.readLine(), // correct answer
                                        bufferReader.readLine(), // question
                                        bufferReader.readLine(), // option A
                                        bufferReader.readLine(), // option B
                                        bufferReader.readLine(), // option C
                                        bufferReader.readLine()));// option D
                            } else {
                                questions.add(new Question(questionType,
                                        bufferReader.readLine(), // correct answer
                                        bufferReader.readLine(), // question
                                        null, null, null, null)); // 4 options
                            }
                        }
                    } // each loop adds a question to the questions arraylist
                    quizzes.add(new Quiz(questions, quizName));
                }
            }
            this.course = quizzes;

            bufferReader.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * constructs a course by reading it from an arrayList of lines in course text
     * file format
     *
     * the name of the course file is "CourseName.txt"
     *
     * the "num" only exists to distinguish this constructor from the other one that
     * takes a string and arraylist
     */
    public Course(String name, ArrayList<String> lines, int num) {
        this.name = name;
        String line;
        String quizName;
        String questionType;

        // this loop executes once per course, looping once for each quiz
        ArrayList<Quiz> quizzes = new ArrayList<>();
        while (true) {

            line = lines.get(0);
            lines.remove(0);
            // System.out.println("\nCOURSE LOOP 1ST LINE = " + line);
            if (line.equals(COURSE_END_STRING)) {
                break;
            } else if (line.equals(QUIZ_START_STRING)) {
                ArrayList<Question> questions = new ArrayList<>();

                quizName = lines.get(0);
                lines.remove(0);
                // System.out.println("successfully found quizName to be " + quizName);

                // this loop executes once per quiz, looping once for each question
                while (true) {
                    line = lines.get(0);
                    lines.remove(0);
                    // System.out.println("\nQUIZ LOOP 1ST LINE = " + line);
                    if (line.equals(QUIZ_END_STRING)) {
                        break;
                    } else if (line.equals(QUESTION_START_STRING)) {
                        questionType = lines.get(0);
                        lines.remove(0);
                        if (questionType.equals("MC")) {
                            questions.add(new Question(questionType,
                                    lines.get(0), // correct answer
                                    lines.get(1), // question
                                    lines.get(2), // option A
                                    lines.get(3), // option B
                                    lines.get(4), // option C
                                    lines.get(5)));// option D
                            lines.subList(0, 6).clear();
                        } else {
                            questions.add(new Question(questionType,
                                    lines.get(0), // correct answer
                                    lines.get(1), // question
                                    null, null, null, null)); // 4 options
                            lines.subList(0, 2).clear();
                        }
                    }
                } // each loop adds a question to the questions arraylist
                quizzes.add(new Quiz(questions, quizName));
            }
        }
        this.course = quizzes;
    }

    /*
     * creates a file which saves a course even when the program is shut off
     * 
     * if a file already exists, this method deletes and rewrites it
     * 
     * this method also records course name to CourseList file
     * (the CourseList MUST have a blank line at the end for this step to work)
     * 
     * FOR THE FILE FORMAT CHECK "Example Course Original.txt", it should be
     * self-explanatory from there
     */
    public void saveCourse() {
        File savedCourse = new File(getName() + ".txt");
        if (savedCourse.exists()) {
            savedCourse.delete();
        }

        try {
            // System.out.println("------Printing Course...------");
            ArrayList<String> courseLines = new ArrayList<>();
            /*
             * add all necessary lines to the arraylist according to the
             * (currently-undecided) course file format
             */
            // System.out.println("NUMBER OF QUIZZES IN FOLLOWING COURSE = " +
            // course.size());
            for (int i = 0; i < course.size(); i++) {
                courseLines.add(QUIZ_START_STRING);
                // System.out.println("adding " + getCourse().get(i).getName() + "...");
                courseLines.add(getCourse().get(i).getName());
                // System.out.println("QUESTION AMOUNT IN BELOW QUIZ =
                // "+getCourse().get(i).getQuiz().size());
                for (int j = 0; j < getCourse().get(i).getQuiz().size(); j++) {
                    courseLines.add(QUESTION_START_STRING);
                    courseLines.add(getCourse().get(i).getQuiz().get(j).getType());
                    // System.out.println("adding "+getCourse().get(i).getQuiz().get(j).getType());
                    courseLines.add(getCourse().get(i).getQuiz().get(j).getCorrectAnswer());
                    // System.out.println("adding
                    // "+getCourse().get(i).getQuiz().get(j).getCorrectAnswer());
                    courseLines.add(getCourse().get(i).getQuiz().get(j).getQuestion());
                    // System.out.println("adding
                    // "+getCourse().get(i).getQuiz().get(j).getQuestion());
                    if (getCourse().get(i).getQuiz().get(j).getType().equals("MC")) {
                        // System.out.println("adding the four MC options...");
                        courseLines.add(getCourse().get(i).getQuiz().get(j).getOptionA());
                        courseLines.add(getCourse().get(i).getQuiz().get(j).getOptionB());
                        courseLines.add(getCourse().get(i).getQuiz().get(j).getOptionC());
                        courseLines.add(getCourse().get(i).getQuiz().get(j).getOptionD());
                    }
                }
                courseLines.add(QUIZ_END_STRING);
            }
            courseLines.add(COURSE_END_STRING);
            /*
             * add every line of the arraylist to the course file to create the file
             */
            FileOutputStream fos = new FileOutputStream(getName() + ".txt", false);
            PrintWriter pw = new PrintWriter(fos);
            for (String courseLine : courseLines) {
                pw.println(courseLine);
            }
            // pw.println("I'm here!");
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // write the course name to the course list
        File courses = new File("CourseList.txt");
        try {
            ArrayList<String> courseListLines = new ArrayList<>();
            FileReader reader = new FileReader("CourseList.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            while (line != null) {
                courseListLines.add(line);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();

            boolean isPresent = false;
            for (String courseListLine : courseListLines) {
                if (courseListLine.equals(getName())) {
                    isPresent = true;
                    break;
                }
            }

            if (!isPresent) {
                FileOutputStream fos = new FileOutputStream("CourseList.txt", true);
                PrintWriter pw = new PrintWriter(fos);
                pw.println(getName());
                pw.close();
            }
            reader.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * removes the specific quiz from given course
     * 
     * -if the specified quiz is found, return true
     * -if the specified quiz is not found, return false
     */
    public boolean removeQuiz(String quizName) {
        for (int i = 0; i < course.size(); i++) {
            if (getCourse().get(i).getName().equals(quizName)) {
                course.remove(i);
                return true;
            }
        }
        return false;
    }
}
