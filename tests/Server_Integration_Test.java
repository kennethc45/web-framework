package tests;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;

import src.Server;

public class Server_Integration_Test {
    private static final int PORT = 8080;
    private static final String HOST = "localhost";

    public static void main(String[] args) throws Exception {
        // Start server in separate thread
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                // Start server
                Server.main(new String[]{});
            } catch (IOException e) {
                System.err.println("Server could not start for testing: " + e.getMessage());
            }
        });

        // Give the server some time to start
        Thread.sleep(1000);

        // Run test
        testServerResponse();

        // Shutdown executor and server
        executor.shutdownNow();
    }

    public static void testServerResponse() {
        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            
             // Sample HTTP GET Request
             out.println("GET / HTTP/1.1");
             out.println("Host: " + HOST);
             out.println("Connection: close");
             out.println();

             // Reads Response
             String responseLine;
             StringBuilder response = new StringBuilder();
             while ((responseLine = in.readLine()) != null) {
                response.append(responseLine).append("\n");
             }

             if (response != null && !response.isEmpty()) {
                System.out.println("TEST - Received Response");
                System.out.println(response.toString());
             } else {
                System.out.println("TEST - Did Not Receive Response");
             }

             if (response.toString().contains("HTTP/1.1 200 OK")) {
                System.out.println("Test passed: Server responded with 200 OK.");
             } else {
                System.out.println("Test failed: Expceted 200 OK, but got: ");
                System.err.println(response.toString());
             }

            } catch (IOException e) {
                System.err.println("Error during test: " + e.getMessage());
            }
    }

}
