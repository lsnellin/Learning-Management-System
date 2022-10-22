import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.swing.*;

/**
 * ClientSession.java Class -- Project 5
 *
 * This is a class is to be used by the client to connect to the server. After
 * connecting, the LogInGUI class is called
 *
 * @author Luke Cooley, Logan Snelling, Mason Minnich, Anushka Gupta -- L07
 *
 * @version 05/02/22
 *
 */
public class ClientSession {
    public static void main(String[] args) {

        String ip = "localhost";
        int port = 4243;
        Scanner scanner = new Scanner(System.in);

        try {
            Socket socket = new Socket(ip, port);

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            System.out.println(ois.readObject());

            SwingUtilities.invokeLater(new LogInGUI(socket, oos, ois));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void run() {

    }
}
