// ServerControllerIntro.java
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ServerControllerIntro {
    @FXML private TextField portField;

    private ServerMain mainApp;

    // Set the reference to the main application
    public void setMain(ServerMain main) {
        this.mainApp = main;
    }

    // Handle the start server button action
    @FXML
    private void startServer() {
        try {
            int port = Integer.parseInt(portField.getText().trim());
            mainApp.startServerAndShowStatusScene(port);
        } catch (NumberFormatException e) {
            System.err.println("Invalid port number. Please enter a valid integer.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Handle the exit button action
    @FXML
    private void handleExit() {
        Stage stage = (Stage) portField.getScene().getWindow();
        stage.close();
    }
}