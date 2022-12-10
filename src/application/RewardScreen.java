package application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.util.List;

/**
 * This class is the controller for the reward screen view.
 */

public class RewardScreen {

    @FXML private Label scoreLabel;
    @FXML private ImageView fullstar1, fullstar2, fullstar3;
    @FXML private TableView<Word> table;
    @FXML private TableColumn<Word, String> resultColumn, timeColumn, scoreColumn, wordColumn;


    public void setScore(int score) {
        scoreLabel.setText("Score: " + score);
        fullstar1.setVisible(score > 10);
        fullstar2.setVisible(score > 40);
        fullstar3.setVisible(score > 80);
    }

    @FXML
    private void initialize() {
        wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        resultColumn.setCellValueFactory(new PropertyValueFactory<>("result"));

        // set the row colours
        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Word item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                } else if (item.correct) {
                    setStyle("-fx-background-color: #c0f6c3;");
                } else {
                    setStyle("-fx-background-color: #f7d4ce;");
                }
            }
        });
    }

    @FXML
    private void play() {
        Main.loadScene("TopicSelection");
    }

    @FXML
    private void menu() {
        Main.loadScene("Menu");
    }

    public void setWordStats(List<Word> words) {
        table.getItems().setAll(words);
        int score = words.stream().mapToInt(Word::getScore).sum();
        setScore(score);
    }
}





