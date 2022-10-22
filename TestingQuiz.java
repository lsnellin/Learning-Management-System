// import java.util.ArrayList;
//
/// **
// * TestingQuiz Class -- Project 4
// *
// * This is a class that contains tests the implementations of the quiz class.
// *
// * @author Luke Cooley, Logan Snelling, Mason Minnich, Anushka Gupta -- L07
// *
// * @version 04/04/22
// *
// */
// public class TestingQuiz {
//
//
// /**
// * A main method that allows the implementation of testing the quiz class
// *
// * @param args
// *
// * @return None
// */
// public static void main(String[] args) {
//
// Question question1 = new Question("MC", "A", "How long is 1 year?", "365
// days",
// "367 days", "52 days", "1 day");
// String toStringTest = question1.getQuestion() + "\n" + "A. " +
// question1.getOptionA() + "\n" + "B. " + question1.getOptionB() + "\n" +
// "C. " + question1.getOptionC() + "\n" + "D. "
// + question1.getOptionD() + "\n" ;
//
// String question1String = question1.toString();
//
//
// Question question2 = new Question("TF", "T", "CS 180 is really fun!", null,
// null, null, null);
//
// ArrayList<Question> questionArrayList = new ArrayList<>();
// questionArrayList.add(question1);
// questionArrayList.add(question2);
//
// Quiz quiz1 = new Quiz(questionArrayList, "Luke's Quiz");
//
// // testing the toString() method of Quiz
//
// String quizTestString = "Name: " + quiz1.getName() + "\n" +
// question1.toString() + "\n" + question2.toString();
// String actualQuizString = quiz1.toString();
//
// if (quizTestString.equals(actualQuizString)) {
// System.out.println("Quiz toString() works properly");
// } else {
// System.out.println("Error in Quiz toString(). Should have been: ");
// System.out.println(quizTestString);
// System.out.println("But was: ");
// System.out.println(actualQuizString);
// }
//
//
// // testing modify quiz (this also tests each setter and getter in the
// process)
// // it tests the getters and setters for both the quiz and question class
//
// // making question 1 a TF instead of MC
// quiz1.modifyQuiz(1, "TF", "F", "CNIT is better than CS", null,
// null, null, null);
//
// String modifyQuizString = "Name: " + quiz1.getName() + "\n" + "CNIT is better
// than CS" + "\n" + "\n" +
// question2.toString();
//
// String modifyQuizActual = quiz1.toString();
//
// if (modifyQuizString.equals(modifyQuizActual)) {
// System.out.println("Quiz modifyQuiz() works properly");
// } else {
// System.out.println("Error in Quiz modifyQuiz(). Should have been: ");
// System.out.println(modifyQuizString);
// System.out.println("But was: ");
// System.out.println(modifyQuizActual);
// }
//
//
// // testing read and save submission
// ArrayList<String> answers = new ArrayList<>();
// answers.add("A");
// answers.add("F");
//
// quiz1.saveSubmission("cooleyl", answers, 1);
//
// // string using the method
// String submissionActual =
// (quiz1.readSubmission("cooleyl"+quiz1.getName()+".txt"));
//
//
// // expected string
// String submissionExpected = "Username: cooleyl" + "\n" + "Quiz Name: Luke's
// Quiz" + "\n" + "Type: TF\n" +
// "Question: CNIT is better than CS\n" +
// "Correct Answer: F\n" +
// "Student's Answer: A\n" +
// "Type: TF\n" +
// "Question: CS 180 is really fun!\n" +
// "Correct Answer: T\n" +
// "Student's Answer: F\n" +
// "Grade: 0/2\n" +
// "Quiz Time: 1.0 minutes\n";
//
// if (submissionActual.equals(submissionExpected)) {
// System.out.println("Both readSubmission() and saveSubmission() work
// properly");
// } else {
// System.out.println("Error in saveSubmission() or readSubmission(). Should
// have been: ");
// System.out.println(submissionExpected);
// System.out.println("But was: ");
// System.out.println(submissionActual);
// }
//
//
//
// }
//
// }
