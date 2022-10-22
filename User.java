import java.io.*;
import java.util.ArrayList;

/**
 * Quiz Class -- Project 4
 *
 * This is a class that contains all the information needed for a user within the
 * program, including name, user ID, username, password, and whether they are
 * a teacher or student.
 *
 * @author Luke Cooley, Logan Snelling, Mason Minnich, Anushka Gupta -- L07
 *
 * @version 04/04/22
 *
 */
public class User implements Serializable {
    private String name;
    private static final long serialVersionUID = 1L;
    private String username;
    private boolean isTeacher;
    private String password;

    public User(String name, String username, String password, boolean isTeacher) {
        this.name = name;
        this.username = username;
        this.isTeacher = isTeacher;
        this.password = password;

    }

    public User(String[] user) {
        this.name = user[1];
        this.username = user[2];
        this.password = user[3];
        this.isTeacher = user[4].equalsIgnoreCase("true");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public boolean getIsTeacher() {
        return isTeacher;
    }

    public boolean checkUserExists() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("User_info.txt"));

            ArrayList<String> array = new ArrayList<>();
            String line = br.readLine();
            // add the lines we read to the string
            while (line != null) {
                array.add(line);
                line = br.readLine();
            }
            br.close();
            for (String a : array) {
                String[] info = a.split(",");
                boolean b;
                b = info[3].equals("true");
                if (this.name.equals(info[0])) {
                    if (this.username.equals(info[1])) {
                        if (this.password.equals(info[2])) {
                            if (Boolean.toString(this.isTeacher).equals(info[3])) {
                                return true;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public void writeUserData() {
        try (PrintWriter pw = new PrintWriter(new FileOutputStream("User_info.txt", true))) {
            pw.println(getUserID());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean createNewUser() {
        ArrayList<String> array = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("User_info.txt")));
            String s;
            s = br.readLine();
            while (s != null) {
                array.add(s);
                s = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream fos = new FileOutputStream("User_info.txt", false);
            PrintWriter pw = new PrintWriter(fos);
            for (String a : array) {
                pw.println(a);
            }
            pw.println(getUserID());
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public String getUserID() {
        String userString = "";

        userString += name + ",";
        userString += username + ",";
        userString += password + ",";
        userString += Boolean.toString(isTeacher);

        return userString;
    }

    /*
     * deletes the user of specific username from the user_list and removes the
     * object
     * 
     * returns true if the user is deleted, and false if the user cannot be found
     */
    public boolean deleteUserData() {
        boolean wasDeleted = false;

        // transferring the user info list to an array list
        ArrayList<String> userListLines = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("User_info.txt"));
            String line = bufferedReader.readLine();
            while (line != null) {
                userListLines.add(line);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // deleting the old user info file
        File users = new File("User_info.txt");
        if (users.exists()) {
            users.delete();
        }

        // removing the specified user from the list
        for (int i = 0; i < userListLines.size(); i++) {
            if (userListLines.get(i).equals(getUserID())) {
                userListLines.remove(i);
                wasDeleted = true;
                break;
            }
        }

        // creating a new file without the specified user
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream("User_info.txt", false));
            for (String userListLine : userListLines) {
                pw.println(userListLine);
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // erasing the references to the user
        name = null;
        username = null;
        isTeacher = false;
        password = null;

        // returning true if user is deleted, and false if it was not
        return wasDeleted;
    }
}