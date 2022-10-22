// import java.util.ArrayList;
//
/// **
// * TestingQuestion Class -- Project 4
// *
// * This is a class that contains tests the implementations of the question
// class.
// *
// * @author Luke Cooley, Logan Snelling, Mason Minnich, Anushka Gupta -- L07
// *
// * @version 04/04/22
// *
// */
//
// public class TestingQuestion {
//
// /**
// * A main method that allows the implementation of testing the question class
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
// question1.getOptionA() + "\n" + "B. " +
// question1.getOptionB() + "\n" +
// "C. " + question1.getOptionC() + "\n" + "D. "
// + question1.getOptionD() + "\n" ;
//
// String question1String = question1.toString();
//
// // code to test if question toString() works properly
//
// if (question1String.equals(toStringTest)) {
// System.out.println("Question toString() works properly");
// } else {
// System.out.println("Error in Question toString(). Should have been: ");
// System.out.println(toStringTest);
// System.out.println("But was: ");
// System.out.println(question1String);
// }
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
// System.out.println(quiz1.readSubmission("cooleylLuke's Quiz.txt"));
//
// //testing modify quiz
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
// ArrayList<String> answers = new ArrayList<>();
// answers.add("A");
// answers.add("F");
//
// quiz1.saveSubmission("cooleyl", answers, 0);
//
//
// }
//
// }
