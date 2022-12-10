package application;

import javafx.application.Platform;
import javafx.fxml.FXML;

/**
 * This class is the controller for the menu scene.
 */

public class Menu {

    @FXML
    private void newGame() {
        Main.loadScene("TopicSelection");
    }

    @FXML
    private void quit() {
        Main.exit();
    }

    @FXML
    private void help() {
        Main.loadScene("HelpPage");
    }

}
