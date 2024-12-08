// ServerControllerStatus.java
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.application.Platform;
import javafx.stage.Stage;

public class ServerControllerStatus {
    @FXML private Label clientCountLabel;
    @FXML private ListView<String> eventsListView;
    @FXML private Label serverPortLabel;
    @FXML private Label connectedClientsLabel;

    private ServerMain mainApp;
    private int currentPort;

    // Set the reference to the main application
    public void setMain(ServerMain main) {
        this.mainApp = main;
    }

    // Set and display the server port
    public void setPort(int port) {
        this.currentPort = port;
        Platform.runLater(() -> serverPortLabel.setText("Server running on port " + port));
    }

    // Update the connected client count in the UI
    public void updateClientCount(int count) {
        Platform.runLater(() -> connectedClientsLabel.setText("Connected Clients: " + count));
    }

    // Add an event message to the events list view
    public void addEvent(String event) {
        Platform.runLater(() -> eventsListView.getItems().add(event));
    }

    // Handle the stop server button action
    @FXML
    private void stopServer() {
        mainApp.stopServer();
        addEvent("Server stopped.");
        try {
            mainApp.showIntroScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}