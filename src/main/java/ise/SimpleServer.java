/**
 * Creates simple Server on specified port - which is passed as parameter to start() method
 *
 * @version 1.0
 * @author Mikey Fennelly
 * */

package ise;

import java.io.*;
import java.net.*;

public class SimpleServer {
    public static void start(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started at port " + port);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(new ClientHandler(clientSocket)).start();
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String request;
            while ((request = in.readLine()) != null) { // Read requests from the client
                System.out.println("Received: " + request);
                String response = "Echo: " + request; // Process the request and generate a response
                out.println(response); // Send response back to the client
            }
        } catch (IOException e) {
            System.out.println("An IOExcepion has occurred: " + e);
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close(); // Close connection after handling the client
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}