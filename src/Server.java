package src;
import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

// Multi-threadded Server
public class Server {
    // Port for server to listen on
    private static final int PORT = 8080;
    // Bool var for whether the server should be running or not
    private static AtomicBoolean running = new AtomicBoolean(true);
    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args) throws IOException {
        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started, listening on port " + PORT);

            // Shutdown hook for shutdown
            // Listens for Ctrl+C or kill signal
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutdown signal received, stoping server...");
                // Set running to false
                running.set(false);
                try {
                    // Shutdown the server
                    serverSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing server socket: " + e.getMessage());
                }
                // Shutdown all threads
                threadPool.shutdown();
            }));

            while(running.get()) {
                try {
                    // Wait for client connections
                    Socket clientSocket = serverSocket.accept();

                    // ???
                    threadPool.execute(() -> handleRequest(clientSocket));
                } catch (IOException e) {
                    if (running.get()) {
                        System.err.println("Error accepting client connection: " + e.getMessage());
                    }
                }
            }

            System.out.println("Server stopped");

        } catch (IOException e) {
            System.err.println("Could not start server on port " + PORT + ": " + e.getMessage());
        }
    }

    // Handles incoming HTTP requests
    private static void handleRequest(Socket clientSocket) {
        try (// Reads HTTP request from client via input stream
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            
             // Sends HTTP response to client via output stream
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {
                // Reads the first line of the HTTP request
                String requestLine = in.readLine();
                // Check if the line is null or empty
                if (requestLine != null && !requestLine.isEmpty()) {
                    // Print raw request 
                    System.out.println("Request Line: " + requestLine);
                }

                String headerLine;
                while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
                    // Print header lines 
                    System.out.println("Header: " + headerLine);
                }

                // Send confirmation to client
                sendResponse(out, "HTTP/1.1 200 OK", "Request received and printed in raw format");
              } catch (IOException e) {
                System.err.println("Error handling request: " + e.getMessage());
              } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
              }
    }

    // Sends HTTP response to the client
    private static void sendResponse(PrintWriter out, String status, String body) {
        out.println(status);
        out.println("Content-Type: text/html");
        out.println("Connection: close");
        out.println("Content-Length: " + body.length());
        out.println();
        out.println(body);

    }
}