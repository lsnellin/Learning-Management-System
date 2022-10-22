import java.io.Serializable;

/**
 * Question Class -- Project 4
 *
 * This is a class that contains all the information needed for a single
 * question within a quiz.
 * The question can be multiple choice, fill in the blank, or true/false. It
 * allows the quick building
 * of questions
 *
 * @author Luke Cooley, Logan Snelling, Mason Minnich, Anushka Gupta -- L07
 *
 * @version 04/04/22
 *
 */
public class Question implements Serializable {

    String type; // type of question (MC, TF, or FIB)
    String correctAnswer; // if MC (A,B,C, or D) if TF (T or F) if FIB (a word/words)
    String question; // the question or statement
    String optionA; // if multiple choice (option A)
    String optionB; // if multiple choice (option B)
    String optionC; // if multiple choice (option C)
    String optionD; // if multiple choice (option D)

    /**
     * A constructor that generates a question
     *
     * @param type          type of question
     * @param correctAnswer the correct answer
     * @param question      the question
     * @param optionA       the question option A
     * @param optionB       the question option B
     * @param optionC       the question option C
     * @param optionD       the question option D
     * @return None
     */
    public Question(String type, String correctAnswer, String question, String optionA, String optionB,
            String optionC, String optionD) {
        this.type = type;
        this.correctAnswer = correctAnswer;
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
    }

    /**
     * Returns the type of question
     *
     * @return A string
     */
    public String getType() {
        return type;
    }

    /**
     * Set the type of the question
     *
     * @param type the type of the question
     *
     * @return None
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns the correct answer
     *
     * @return A string
     */
    public String getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * Set the correct answer
     *
     * @param correctAnswer the correct answer of the question
     *
     * @return None
     */
    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    /**
     * Returns the question
     *
     * @return A string
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Set the question
     *
     * @param question the question
     *
     * @return None
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Returns option A
     *
     * @return A string
     */
    public String getOptionA() {
        return optionA;
    }

    /**
     * Set option A of the question
     *
     * @param optionA option A of the question
     *
     * @return None
     */
    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    /**
     * Returns option B
     *
     * @return A string
     */
    public String getOptionB() {
        return optionB;
    }

    /**
     * Set option B of the question
     *
     * @param optionB option B of the question
     *
     * @return None
     */
    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    /**
     * Returns option C
     *
     * @return A string
     */
    public String getOptionC() {
        return optionC;
    }

    /**
     * Set option C of the question
     *
     * @param optionC option C of the question
     *
     * @return None
     */
    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    /**
     * Returns option D
     *
     * @return A string
     */
    public String getOptionD() {
        return optionD;
    }

    /**
     * Set option D of the question
     *
     * @param optionD option D of the question
     *
     * @return None
     */
    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    /**
     * Returns a string representation of a question (used for printing the
     * questions)
     *
     *
     * @return String
     */
    @Override
    public String toString() {
        return switch (type) {
            case "MC" ->
                question + "\n" + "A. " + optionA + "\n" + "B. " + optionB + "\n" + "C. " + optionC + "\n" + "D. "
                        + optionD + "\n";
            case "TF", "FIB" -> question + "\n";
            default -> null;
        };
    }

    /**
     * Returns a string representation of a question (used for reading/writing a
     * file to save)
     *
     *
     * @return String
     */
    public String toString2() {
        return switch (type) {
            case "MC" -> type + "\n" + correctAnswer + "\n" +
                    question + "\n" + "A. " + optionA + "\n" + "B. " + optionB + "\n" + "C. " + optionC + "\n" + "D. "
                    + optionD + "\n";
            case "TF", "FIB" -> type + "\n" + correctAnswer + "\n" + question + "\n";
            default -> null;
        };
    }
}
