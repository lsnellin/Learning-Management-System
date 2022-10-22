import java.io.*;
import java.util.ArrayList;

/**
 * Quiz Class -- Project 4
 *
 * This is a class that contains all the information needed for a quiz within a
 * course. It consists
 * of an array list of questions. It allows for the quick building of a quiz for
 * a course.
 *
 * @author Luke Cooley, Logan Snelling, Mason Minnich, Anushka Gupta -- L07
 *
 * @version 04/04/22
 *
 */
public class Quiz implements Serializable {
    ArrayList<Question> quiz; // array list of questions (a quiz)
    String name; // the name of the quiz

    /**
     * A constructor that generates the quiz given an array list
     *
     * @param quiz array list of questions
     * @param name name of quiz
     *
     * @return None
     */
    public Quiz(ArrayList<Question> quiz, String name) {

        this.quiz = quiz;
        this.name = name;
    }

    /**
     * Returns the quiz array list of questions
     *
     * @return an array list representing the quiz
     */
    public ArrayList<Question> getQuiz() {
        return quiz;
    }

    /**
     * Set the quiz of this class to the quiz array list provided
     *
     * @param quiz array list of questions representing the quiz
     *
     * @return None
     */

    public void setQuiz(ArrayList<Question> quiz) {
        this.quiz = quiz;
    }

    /**
     * Returns the string name
     *
     * @return a string of the name of the quiz
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name to the new string name
     *
     * @param name new name
     *
     * @return None
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the quiz of this class to the quiz array list provided
     *
     * @param questionNumber question number of question you want to change
     * @param type           type of new question
     * @param correctAnswer  the new correct answer
     * @param question       the new question
     * @param optionA        the new question option A
     * @param optionB        the new question option B
     * @param optionC        the new question option C
     * @param optionD        the new question option D
     * @return None
     */
    public void modifyQuiz(int questionNumber, String type, String correctAnswer, String question, String optionA,
            String optionB, String optionC, String optionD) {
        quiz.get(questionNumber - 1).setType(type);
        quiz.get(questionNumber - 1).setCorrectAnswer(correctAnswer);
        quiz.get(questionNumber - 1).setQuestion(question);
        quiz.get(questionNumber - 1).setOptionA(optionA);
        quiz.get(questionNumber - 1).setOptionB(optionB);
        quiz.get(questionNumber - 1).setOptionC(optionC);
        quiz.get(questionNumber - 1).setOptionD(optionD);
    }

    /**
     * Save the submission with the answers given
     *
     * @param username the username of the student who took the quiz
     * @param answers  arrayList of the student's answers
     * @param quizTime the time it took to complete the quiz
     * @return None
     */
    /*
     * public void saveSubmission(String username, ArrayList<String> answers, double
     * quizTime) {
     * // create a new file
     * File savedSubmission = new File(username + name + ".txt");
     * int correctAnswers = 0; // value for counting the number of correct answers
     * // if loop to make sure we do not append or rewrite a file that causes errors
     * if (savedSubmission.exists()) {
     * savedSubmission.delete();
     * }
     * 
     * // try catch to catch a variety of exceptions
     * try {
     * ArrayList<String> submissionList = new ArrayList<>(); // array list to build
     * the string to write to the file
     * submissionList.add("Username: " + username); // add username
     * submissionList.add("Quiz Name: " + this.name); // add name of quiz
     * 
     * // add all the question information for every question in the quiz
     * for (int i = 0; i < quiz.size(); i ++) {
     * submissionList.add("Type: " + getQuiz().get(i).getType()); // add type
     * submissionList.add("Question: " + getQuiz().get(i).getQuestion()); // add
     * question
     * if (getQuiz().get(i).getType().equalsIgnoreCase("MC")) { // if MC add the
     * options
     * submissionList.add("Option A: " + getQuiz().get(i).getOptionA());
     * submissionList.add("Option B: " + getQuiz().get(i).getOptionB());
     * submissionList.add("Option C: " + getQuiz().get(i).getOptionC());
     * submissionList.add("Option D: " + getQuiz().get(i).getOptionD());
     * }
     * submissionList.add("Correct Answer: " + getQuiz().get(i).getCorrectAnswer());
     * // add correct answer
     * submissionList.add("Student's Answer: " + answers.get(i)); // add student
     * answer
     * if (getQuiz().get(i).getCorrectAnswer().equals(answers.get(i))) { // check if
     * answer is correct
     * correctAnswers++;
     * }
     * }
     * // show the grade of the quiz and quiz time
     * submissionList.add("Grade: " + Integer.toString(correctAnswers) + "/" +
     * Integer.toString(getQuiz().size()));
     * submissionList.add("Quiz Time: " + Double.toString(quizTime) + " minutes");
     * 
     * // make fileoutputstream and printwriter to write the whole arraylist
     * FileOutputStream fos = new FileOutputStream(username + name + ".txt", false);
     * PrintWriter pw = new PrintWriter(fos);
     * for (String submissionLine : submissionList) {
     * pw.println(submissionLine);
     * }
     * 
     * // close printwriter
     * 
     * pw.close();
     * 
     * // catch exceptions
     * } catch (FileNotFoundException e) {
     * e.printStackTrace();
     * } catch (Exception e) {
     * e.printStackTrace();
     * }
     * 
     * // file to save all submission file names
     * File courses = new File("SubmissionList.txt");
     * try {
     * // arraylist for storage
     * ArrayList<String> submissionListLines = new ArrayList<>();
     * 
     * // new filereader and bufferedreader
     * FileReader reader = new FileReader("SubmissionList.txt");
     * BufferedReader bufferedReader = new BufferedReader(reader);
     * String line = bufferedReader.readLine();
     * // add lines as long as not null
     * while (line != null) {
     * submissionListLines.add(line);
     * line = bufferedReader.readLine();
     * }
     * 
     * // close bufferedreader
     * bufferedReader.close();
     * 
     * // check to make sure you do not write duplicates
     * boolean isPresent = false;
     * for (String submissionListLine : submissionListLines) {
     * if (submissionListLine.equals(username + this.name + ".txt")) {
     * isPresent = true;
     * break;
     * }
     * }
     * 
     * // write the list to the file
     * if (!isPresent) {
     * FileOutputStream fos = new FileOutputStream("SubmissionList.txt", true);
     * PrintWriter pw = new PrintWriter(fos);
     * pw.println(username + this.name + ".txt");
     * pw.close();
     * }
     * // close reader and bufferedreader
     * reader.close();
     * bufferedReader.close();
     * } catch (IOException e) {
     * e.printStackTrace();
     * } catch (Exception e) {
     * e.printStackTrace();;
     * }
     * }
     */

    /**
     * read the submission and return string representation
     *
     * @param filename the username of the student who took the quiz
     * @return String
     */
    public String readSubmission(String filename) {
        String submissionString = ""; // string to build
        try {
            // file reader and buffer reader
            FileReader reader = new FileReader(filename);
            BufferedReader bufferReader = new BufferedReader(reader);
            String line = bufferReader.readLine();
            // add the lines we read to the string
            while (line != null) {
                submissionString += line + "\n";
                line = bufferReader.readLine();
            }
            // close buffered reader
            bufferReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        // return the submission string
        return submissionString;

    }

    public void modifyGrade(double gradeValue, String filename) {
        try {
            // arraylist for storage
            ArrayList<String> submissionListLines = new ArrayList<>();

            // new filereader and bufferedreader
            FileReader reader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            // add lines as long as not null
            while (line != null) {
                submissionListLines.add(line);
                line = bufferedReader.readLine();
            }

            // close bufferedreader
            bufferedReader.close();

            submissionListLines.set(submissionListLines.size() - 2, "Grade: " + gradeValue + "/" + this.quiz.size());
            // check to make sure you do not write duplicates

            // write the list to the file
            FileOutputStream fos = new FileOutputStream(filename, false);
            PrintWriter pw = new PrintWriter(fos);
            for (int i = 0; i < submissionListLines.size(); i++) {
                pw.println(submissionListLines.get(i));
            }
            pw.close();

            // close reader and bufferedreader
            reader.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Returns a string representation of a quiz
     *
     *
     * @return String
     */
    @Override
    public String toString() {
        String build = "Name: " + name + "\n";
        for (int i = 0; i < quiz.size(); i++) {
            if (i == quiz.size() - 1) {
                build += quiz.get(i).toString();
            } else {
                build += quiz.get(i).toString() + "\n";
            }

        }
        return build;
    }
}
