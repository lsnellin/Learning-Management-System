import java.io.*;
import java.util.ArrayList;

/**
 * Submission.java Class -- Project 5
 *
 * This is a class that stores a submission for editing and displaying later
 *
 * @author Luke Cooley, Logan Snelling, Mason Minnich, Anushka Gupta -- L07
 *
 * @version 05/02/22
 *
 */
public class Submission implements Serializable {
    /*
     * THINGS WE NEED TO BE ABLE TO DO WITH SUBMISSIONS
     * -teachers can view student submissions for a quiz
     * -teachers can view the quiz submissions for individual students and assign
     * point values for each answer
     * (done)-students can view their graded quizzes, including points for each
     * individual question and total score
     */
    private final String username;
    private final String courseName;
    private final String quizName;
    private ArrayList<Answer> answers = new ArrayList<>();
    private double points;
    private double maxPoints;
    private final double time;
    private final String ID;
    public static final String START_SUBMISSION_STRING = "--Start Submission--";
    public static final String END_SUBMISSION_STRING = "--End Submission--";
    public static final String END_SUBMISSION_LIST_STRING = "--End Submission List--";

    public String getID() {
        return ID;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public String getUsername() {
        return username;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setMaxPoints(double maxPoints) {
        this.maxPoints = maxPoints;
    }

    // This method used for testing the methods in this class
    /*
     * public static void main(String[] args) {
     * ArrayList<Answer> ans = new ArrayList<>();
     * 
     * ans.add(new Answer(1, "right_ans", 0.9, 1));
     * ans.add(new Answer(2, "wrong_ans", 0.2, 1));
     * 
     * Submission test = new Submission("user_test", "course_test", "quiz_test",
     * ans, 1.2);
     * 
     * readSubmissionList();
     * test.saveSubmission();
     * }
     */

    public Submission(String username, String courseName, String quizName, ArrayList<Answer> answers, double time) {
        this.username = username;
        this.courseName = courseName;
        this.quizName = quizName;
        this.answers = answers;
        double points = 0;
        for (Answer answer : answers) {
            points += answer.getPointsEarned();
        }
        this.points = points;
        double maxPoints = 0;
        for (Answer answer : answers) {
            maxPoints += answer.getPointValue();
        }
        this.maxPoints = maxPoints;
        this.time = time;
        this.ID = username + "," + quizName;
    }

    // generate a submission from a given section of Submission_List.txt
    public Submission(ArrayList<String> lines) {
        this.username = lines.get(0);
        this.courseName = lines.get(1);
        this.quizName = lines.get(2);
        this.points = Double.parseDouble(lines.get(3));
        this.maxPoints = Double.parseDouble(lines.get(4));
        this.time = Double.parseDouble(lines.get(5));

        // get answers
        ArrayList<Answer> answers1 = new ArrayList<>();
        for (int i = 6; i < lines.size(); i = i + 3) {
            if (!lines.get(i).equals(END_SUBMISSION_STRING)) {
                answers1.add(new Answer(i, lines.get(i),
                        Double.parseDouble(lines.get(i + 1)),
                        Double.parseDouble(lines.get(i + 2))));
            }
        }
        this.answers = answers1;
        this.ID = username + "," + quizName;
    }

    // read through Submission_List.txt and generate a submission object for each
    // submission
    public ArrayList<Submission> readSubmissionList() {
        ArrayList<Submission> submissions = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("Submission_List.txt"));
            String line;
            ArrayList<String> submissionLines;
            do {
                line = br.readLine();
                submissionLines = new ArrayList<>();
                if (line.equals(START_SUBMISSION_STRING)) {
                    do {
                        line = br.readLine();
                        submissionLines.add(line);
                    } while (!line.equals(END_SUBMISSION_STRING));
                    submissions.add(new Submission(submissionLines));
                }
            } while (!line.equals(END_SUBMISSION_LIST_STRING));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return submissions;
    }

    // save the given submission to Submission_List.txt
    // removes the given submission if another one of the same ID already exists
    public void saveSubmission() {
        // reading the entire current submission list
        ArrayList<String> lines = new ArrayList<>();
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader("Submission_List.txt"));
            line = br.readLine();
            while (!line.equals(END_SUBMISSION_LIST_STRING)) {
                lines.add(line);
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // deleting the current submission list
        File savedCourse = new File("Submission_List.txt");
        if (savedCourse.exists()) {
            savedCourse.delete();
        }

        // removing a submission if already present
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).equals(START_SUBMISSION_STRING) &&
                    lines.get(i + 1).equals(username) &&
                    lines.get(i + 2).equals(courseName) &&
                    lines.get(i + 3).equals(quizName)) {
                for (int j = i; j < lines.size(); j++) {
                    if (lines.get(j).equals(END_SUBMISSION_STRING)) {
                        lines.subList(i, j + 1).clear();
                        break;
                    }
                }
            }
        }

        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream("Submission_List.txt", false));
            // rewriting the entire submission list
            for (String s : lines) {
                pw.println(s);
            }
            // adding the new submission to the list
            pw.println(START_SUBMISSION_STRING);
            pw.println(username);
            pw.println(courseName);
            pw.println(quizName);
            pw.println(points);
            pw.println(maxPoints);
            pw.println(time);
            for (Answer a : answers) {
                pw.println(a.getGivenAnswer());
                pw.println(a.getPointsEarned());
                pw.println(a.getPointValue());
            }
            pw.println(END_SUBMISSION_STRING);
            pw.println(END_SUBMISSION_LIST_STRING);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toString(Quiz quiz, Course course) {
        String s = username + "'s submission for quiz " + quiz.getName() + " in course " + course.getName() + ":\n\n";
        for (int i = 0; i < quiz.getQuiz().size(); i++) {
            s += "Question " + (i + 1) + ": (type: " + quiz.getQuiz().get(i).getType() + ")\n";
            s += quiz.getQuiz().get(i).getQuestion() + "\n";
            if (quiz.getQuiz().get(i).getType().equals("MC")) {
                s += "A: " + quiz.getQuiz().get(i).getOptionA() + "\n";
                s += "B: " + quiz.getQuiz().get(i).getOptionB() + "\n";
                s += "C: " + quiz.getQuiz().get(i).getOptionC() + "\n";
                s += "D: " + quiz.getQuiz().get(i).getOptionD() + "\n";
            }
            s += "Correct Answer: " + quiz.getQuiz().get(i).getCorrectAnswer() + "\n";
            s += "Student Answer: " + answers.get(i).getGivenAnswer() + "\n";
            s += String.format("Awarded %.2f out of %.2f points for this question\n\n",
                    answers.get(i).getPointsEarned(),
                    answers.get(i).getPointValue());
        }
        s += String.format("Obtained a total of %.2f out of %.2f points for this quiz\n", points, maxPoints);
        s += String.format("Quiz time was %.2f minutes\n", time / 60);
        return s;
    }

    public String toString() {
        return "Submission{\n" +
                "username= " + username + '\n' +
                "courseName= " + courseName + '\n' +
                "quizName= " + quizName + '\n' +
                "answers= " + answers + '\n' +
                "points= " + points + '\n' +
                "maxPoints= " + maxPoints + '\n' +
                "time= " + time + '\n' +
                "ID= " + ID + '\n' +
                '}';
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public void calculatePoints() {
        double points = 0;
        for (Answer answer : answers) {
            points += answer.getPointsEarned();
        }
        setPoints(points);
        double maxPoints = 0;
        for (Answer answer : answers) {
            maxPoints += answer.getPointValue();
        }
        setMaxPoints(maxPoints);

    }

}
