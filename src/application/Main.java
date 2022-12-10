package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage stage;

    // this method loads a new scene from an FXML file, sets the scene and returns the controller
    public static Object loadScene(String view) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/" + view + ".fxml"));
        try {
            Parent root = loader.load();
            stage.setScene(new Scene(root, 1280, 720));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FXML file!");
        }
        return loader.getController();
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setOnCloseRequest((event) -> Platform.exit());
        loadScene("Menu");
        stage.show();
    }

    public static void exit() {
        stage.close();
    }

    public static void main(String[] args) {
        Festival.init();
        Words.importWordLists();
        launch(args);
        Festival.stop();
    }
}
