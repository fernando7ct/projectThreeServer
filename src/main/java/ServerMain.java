// ServerMain.java
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerMain extends Application {
    private Stage primaryStage;
    private Server server;
    private ServerControllerStatus statusController;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        showIntroScene();
    }

    // Display the initial intro scene where server can be started
    public void showIntroScene() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ServerIntro.fxml"));
        Scene scene = new Scene(loader.load(), 500, 300);
        scene.getRoot().setStyle("-fx-background-color: green;");
        ServerControllerIntro controller = loader.getController();
        controller.setMain(this);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Three Card Poker Server - Intro");
        primaryStage.show();
    }

    // Start the server and display the status scene
    public void startServerAndShowStatusScene(int port) throws Exception {
        server = new Server(this);
        server.startServer(port);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ServerStatus.fxml"));
        Scene scene = new Scene(loader.load(), 600, 400);
        statusController = loader.getController();
        statusController.setMain(this);
        statusController.setPort(port);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Three Card Poker Server - Status");
        primaryStage.show();
    }

    // Stop the server
    public void stopServer() {
        if (server != null) {
            server.stopServer();
            server = null;
        }
    }

    // Update the client count in the UI
    public void updateClientCount(int count) {
        if (statusController != null) {
            statusController.updateClientCount(count);
        }
    }

    // Add an event message to the UI
    public void addEvent(String event) {
        if (statusController != null) {
            statusController.addEvent(event);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}