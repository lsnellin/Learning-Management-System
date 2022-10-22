import java.io.Serializable;

/**
 * Answer.java Class -- Project 5
 *
 * This is a class that stores an answer object, its question number, and its
 * points
 *
 * @author Luke Cooley, Logan Snelling, Mason Minnich, Anushka Gupta -- L07
 *
 * @version 05/02/22
 *
 */
public class Answer implements Serializable {
    private final int questionNumber;
    private final String givenAnswer; // this can be the answer itself or a file name
    private double pointsEarned;
    private double pointValue;

    public Answer(int questionNumber, String givenAnswer, boolean isCorrect) {
        this.questionNumber = questionNumber;
        this.givenAnswer = givenAnswer;
        this.pointsEarned = 0;
        if (isCorrect) {
            this.pointsEarned = 1;
        }
        this.pointValue = 1;
    }

    public Answer(int questionNumber, String givenAnswer, double pointsEarned, double pointValue) {
        this.questionNumber = questionNumber;
        this.givenAnswer = givenAnswer;
        this.pointsEarned = pointsEarned;
        this.pointValue = pointValue;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public String getGivenAnswer() {
        return givenAnswer;
    }

    public double getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(double pointsEarned) {
        this.pointsEarned = pointsEarned;
    }

    public double getPointValue() {
        return pointValue;
    }

    public void setPointValue(double pointValue) {
        this.pointValue = pointValue;
    }
}
