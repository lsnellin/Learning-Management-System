import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * ServerThread.java Class -- Project 5
 *
 * This is a class that is called by Server.java every time a new client
 * connects. This class waits for instructions from the client, and upon
 * recieving the instructions will send and store data from the client
 *
 * @author Luke Cooley, Logan Snelling, Mason Minnich, Anushka Gupta -- L07
 *
 * @version 05/02/22
 *
 */
public class ServerThread extends Thread {
    private static final Object gatekeeper = new Object();
    final ObjectInputStream ois;
    final ObjectOutputStream oos;
    final Socket socket;
    private ArrayList<Submission> submissionList;
    private User currentUser;

    public ServerThread(Socket socket, ObjectInputStream ois, ObjectOutputStream oos) {
        this.socket = socket;
        this.ois = ois;
        this.oos = oos;
    }

    public void run() {
        try {
            oos.writeObject("Successfully created New Client connection\nWaiting for client response...");
            oos.flush();
            while (true) {
                String instructions = (String) ois.readObject();

                if (instructions.equalsIgnoreCase("Login")) {
                    User currentUser = (User) ois.readObject();
                    boolean logIn;
                    synchronized (gatekeeper) {
                        logIn = currentUser.checkUserExists();
                    }
                    oos.writeObject(logIn);
                    oos.flush();

                } else if (instructions.equalsIgnoreCase("CreateAccount")) {
                    User currentUser = (User) ois.readObject();
                    boolean createAccount;
                    boolean logIn;
                    synchronized (gatekeeper) {
                        logIn = currentUser.checkUserExists();
                    }
                    if (logIn) {
                        oos.writeObject(false);
                        oos.flush();
                    } else {
                        synchronized (gatekeeper) {
                            createAccount = currentUser.createNewUser();
                        }
                        oos.writeObject(createAccount);
                        oos.flush();
                    }
                } else if (instructions.equalsIgnoreCase("CreateCourse")) { // Create a new Course
                    Course newCourse = (Course) ois.readObject();

                    newCourse.saveCourse();

                } else if (instructions.equalsIgnoreCase("EditStudent")) { // Edit Student Account
                    User currentUser = (User) ois.readObject();
                    User newUser = (User) ois.readObject();
                    synchronized (gatekeeper) {
                        currentUser.deleteUserData();
                    }
                    boolean createAccount;
                    synchronized (gatekeeper) {
                        createAccount = newUser.createNewUser();
                    }
                    oos.writeObject(createAccount);
                    oos.flush();

                } else if (instructions.equalsIgnoreCase("EditTeacher")) { // Edit Teacher Account
                    User currentUser = (User) ois.readObject();
                    User newUser = (User) ois.readObject();
                    synchronized (gatekeeper) {
                        currentUser.deleteUserData();
                    }
                    boolean createAccount;
                    synchronized (gatekeeper) {
                        createAccount = newUser.createNewUser();
                    }
                    oos.writeObject(createAccount);
                    oos.flush();

                } else if (instructions.equalsIgnoreCase("DeleteAccount")) { // Delete Current User
                    User currentUser = (User) ois.readObject();
                    boolean deleted;
                    synchronized (gatekeeper) {
                        deleted = currentUser.deleteUserData();
                    }

                    oos.writeObject(deleted);
                    oos.flush();

                } else if (instructions.equalsIgnoreCase("GetCourse")) { // Get a SINGLE course
                    String courseName = (String) ois.readObject();
                    Course course; // Course to be sent back to client
                    synchronized (gatekeeper) {
                        course = new Course(courseName);
                    }

                    oos.writeObject(course);
                    oos.flush();

                } else if (instructions.equalsIgnoreCase("GetSubmissions")) {
                    ArrayList<Submission> submissionList;
                    synchronized (gatekeeper) {
                        submissionList = updateSubmissionList();
                    }

                    Submission[] submissionArray = new Submission[submissionList.size()];
                    for (int i = 0; i < submissionList.size(); i++) {
                        submissionArray[i] = submissionList.get(i);
                    }

                    oos.writeObject(submissionArray);
                    oos.flush();

                } else if (instructions.equalsIgnoreCase("GetCourses")) { // Get List of ALL courses
                    ArrayList<Course> courseList;
                    synchronized (gatekeeper) {
                        courseList = updateCourseList();
                    }

                    Course[] courseArray = new Course[courseList.size()];
                    for (int i = 0; i < courseList.size(); i++) {
                        courseArray[i] = courseList.get(i);
                    }

                    oos.writeObject(courseArray);
                    oos.flush();

                } else if (instructions.equalsIgnoreCase("SaveCourse")) {
                    Course savedCourse = (Course) ois.readObject();

                    synchronized (gatekeeper) {
                        savedCourse.saveCourse();
                    }
                } else if (instructions.equalsIgnoreCase("SaveSubmission")) {
                    Submission newSubmission = (Submission) ois.readObject();

                    synchronized (gatekeeper) {
                        newSubmission.saveSubmission();
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public ArrayList<Submission> updateSubmissionList() {

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

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        submissionList = submissions;
        return submissionList;
    }

    public ArrayList<Course> updateCourseList() {
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

        for (String courseName : courseNames) {
            if (courseName != null) {
                try {
                    BufferedReader bfr = new BufferedReader(new FileReader(courseName + ".txt"));
                    ArrayList<String> lines = new ArrayList<>();
                    while (true) {
                        String line = bfr.readLine();
                        if (line == null) {
                            break;
                        }
                        lines.add(line);
                    }

                    courseList.add(new Course(courseName, lines, 0));

                    bfr.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return courseList;
    }
}
