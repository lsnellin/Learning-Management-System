import java.io.*;
import java.net.*;

/**
 * Server.java Class -- Project 5
 *
 * This is a class that starts a server on port 4243. The server listens for
 * clients to connect, and when a client succesfully connects, a new thread is
 * created for the client to interact with the server using ServerThread.java
 *
 * @author Luke Cooley, Logan Snelling, Mason Minnich, Anushka Gupta -- L07
 *
 * @version 05/02/22
 *
 */
public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(4243);
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.flush();
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    Thread thread = new ServerThread(socket, ois, oos);
                    thread.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
