// Server.java
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private ServerSocket serverSocket;
    private boolean running = false;
    private ServerMain mainApp;
    private CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

    // Constructor initializes the server with a reference to the main application
    public Server(ServerMain mainApp) {
        this.mainApp = mainApp;
    }

    // Start the server on the specified port
    public void startServer(int port) throws Exception {
        serverSocket = new ServerSocket(port);
        running = true;

        // Thread to accept incoming client connections
        Thread acceptThread = new Thread(() -> {
            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    ClientHandler handler = new ClientHandler(clientSocket, this);
                    clients.add(handler);
                    updateClientCount();
                    handler.start(); // Start handling the client in a new thread
                } catch (Exception e) {
                    if (!running) break; // Exit if server is stopped
                }
            }
        });
        acceptThread.start();
    }

    // Stop the server and disconnect all clients
    public void stopServer() {
        running = false;
        try {
            serverSocket.close();
            for (ClientHandler c : clients) {
                c.closeConnection();
            }
            clients.clear();
            updateClientCount();
        } catch (Exception e) {}
    }

    // Remove a client from the active clients list
    public void removeClient(ClientHandler c) {
        clients.remove(c);
        updateClientCount();
    }

    // Update the client count in the main application UI
    public void updateClientCount() {
        mainApp.updateClientCount(clients.size());
    }

    // Add an event message to the main application UI
    public void addEvent(String event) {
        mainApp.addEvent(event);
    }
}