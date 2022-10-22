/*
 * This class's main method is used for testing the following 3 methods.
 *
 *
 * -course constructor from file name
 * -quiz remover
 * -course saver to file
 *
 *
 * IMPORTANT: if you want to run the main method again, copy the contents of "Example Course
 * Original.txt" over the existing contents of "Example Course.txt" in between executions of
 * the main method.
 */
public class TestCourse {
    public static void main(String[] args) {
        Course testCourse = new Course("Example Course");

        System.out.println("____________Pre-removal course____________");
        for (int i = 0; i < testCourse.getCourse().size(); i++) {
            System.out.println("starting quiz " + i + ", which has " +
                    testCourse.getCourse().get(i).getQuiz().size() + "questions");
            for (int j = 0; j < testCourse.getCourse().get(i).getQuiz().size(); j++) {
                System.out.println("getting quiz " + i + ", question " + j);
                System.out.println(testCourse.getCourse().get(i).getQuiz().get(j).toString2());
            }
        }

        System.out.println("____________Removing Quiz2____________");
        System.out.print("number of quizzes went from " + testCourse.getCourse().size());
        testCourse.removeQuiz("Example Quiz 2");
        System.out.println(" to " + testCourse.getCourse().size() + "\n");

        System.out.println("____________Post-removal course____________");
        for (int i = 0; i < testCourse.getCourse().size(); i++) {
            for (int j = 0; j < testCourse.getCourse().get(i).getQuiz().size(); j++) {
                System.out.println(testCourse.getCourse().get(i).getQuiz().get(j).toString2());
            }
        }

        testCourse.saveCourse();
    }
}
