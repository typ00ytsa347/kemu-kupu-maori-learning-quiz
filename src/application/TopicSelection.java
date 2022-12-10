package application;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

/**
 * This class is the controller for the topic selection scene.
 */

public class TopicSelection {

    @FXML ChoiceBox<String> choiceBox;

    @FXML
    private void start() {
        Game game = (Game) Main.loadScene("Game");
        game.startGame(choiceBox.getValue(), false);
    }

    @FXML
    private void startPractice() {
        Game game = (Game) Main.loadScene("Game");
        game.startGame(choiceBox.getValue(), true);
    }

    @FXML
    public void initialize() {
        // set the choice box items to the available topics.
        choiceBox.setItems(FXCollections.observableArrayList(Words.getTopics()));
    }

    @FXML
    private void menu() {
        Main.loadScene("Menu");
    }
    
    @FXML
    private void help() {
        Main.loadScene("HelpPage");
    }
    
}
