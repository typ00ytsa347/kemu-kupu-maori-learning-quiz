package application;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class defines the custom quiz input system. It can be used as a JavaFX node.
 */

public class QuizInput extends Parent {

    private final HBox hbox;
    private final ArrayList<Label> labels;
    private String word;
    private int typingPos;
    private boolean[] disabled;
    private final Tooltip macron;

    // this method resets the quiz input and sets a new quiz word.
    public void nextWord(String word) {
        this.word = word;
        Arrays.fill(disabled, false);
        hbox.getChildren().clear();
        macron.setText("Click to add macron above");
        macron.setStyle("-fx-font-size: 20;" + "-fx-background-color: #560b06;");
        for (int i = 0; i < word.length(); i++) {
            setValue(i, '_');
            labels.get(i).setStyle("-fx-background-color: transparent;");
            labels.get(i).setTooltip(macron);
            hbox.getChildren().add(labels.get(i));
        }
        moveTypingPosition(0);
    }

    // this method clears the current input
    public void clear() {
        for (int i = 0; i < word.length(); i++) {
            setValue(i, '_');
        }
        moveTypingPosition(0);
    }

    public QuizInput() {
        macron = new Tooltip();
        labels = new ArrayList<>();
        hbox = new HBox();
        hbox.setStyle("-fx-border-color: transparent;");
        for (int i = 0, x = 0; x <= 1080; i++, x += 65) {
            Label label = new Label();
            label.setLayoutX(x);
            label.setPrefWidth(65);
            label.setAlignment(Pos.CENTER);
            label.getStyleClass().add("quiz-label");
            label.setOnMouseClicked(mouseEvent -> {
                Label source = (Label) mouseEvent.getSource();
                if (!disabled[labels.indexOf(source)]) {
                    char old = source.getText().charAt(0);
                    source.setText(String.valueOf(macronise(old)));
                }
            });
            labels.add(label);
        }
        disabled = new boolean[labels.size()];
        getChildren().add(hbox);
    }

    // gets the value of the input box at the given position
    public char getValue(int pos) {
        return labels.get(pos).getText().charAt(0);
    }

    // sets the value of the input box at the given position
    public void setValue(int pos, char c) {
        if (pos < labels.size() && !disabled[pos]) {
            labels.get(pos).setText(String.valueOf(c));
        }
    }

    public void reveal(int pos) {
        setValue(pos, word.charAt(pos));
        labels.get(pos).setStyle("-fx-background-color: #a79d9d;" + "-fx-font-family: openDyslexicAlta;");
        disabled[pos] = true;
    }

    public void revealWord() {
        for (int i = 0; i < word.length(); i++) {
            reveal(i);
        }
    }

    public String getText() {
        char[] result = new char[word.length()];
        for (int i = 0; i < word.length(); i++) {
            result[i] = getValue(i);
        }
        return String.valueOf(result);
    }

    public void handleKeyPress(KeyEvent key) {
        if (key.getText().length() == 1) {
            // input is a typable character
            char input = key.getText().charAt(0);

            if (Character.isLetter(input) || input == ' ' || input == '-') {
                setValue(typingPos, input);
                moveTypingPosition(typingPos + 1);
                key.consume();
            }
        } else if (key.getCode() == KeyCode.BACK_SPACE) {
            moveTypingPosition(typingPos - 1);
            setValue(typingPos, '_');
        } else if (key.getCode() == KeyCode.LEFT) {
            moveTypingPosition(typingPos - 1);
        } else if (key.getCode() == KeyCode.RIGHT) {
            moveTypingPosition(typingPos + 1);
        }
    }

    // this method adds/removes the macron from a vowel
    private char macronise(char value) {
        boolean isUpper = Character.isUpperCase(value);
        switch (Character.toLowerCase(value)) {
            case 'a': return isUpper ? 'Ā' : 'ā';
            case 'e': return isUpper ? 'Ē' : 'ē';
            case 'i': return isUpper ? 'Ī' : 'ī';
            case 'o': return isUpper ? 'Ō' : 'ō';
            case 'u': return isUpper ? 'Ū' : 'ū';
            case 'ā': return isUpper ? 'A' : 'a';
            case 'ē': return isUpper ? 'E' : 'e';
            case 'ī': return isUpper ? 'I' : 'i';
            case 'ō': return isUpper ? 'O' : 'o';
            case 'ū': return isUpper ? 'U' : 'u';
            default : return value;
        }
    }

    private void moveTypingPosition(int newPos) {
        if (newPos < 0 || newPos > word.length()) {
            // new position is out of range so do nothing.
            return;
        }

        if (typingPos < labels.size() && !disabled[typingPos]) {
            // remove the highlighting from the old typing position.
            labels.get(typingPos).setStyle("-fx-background-color: transparent;");
        }

        if (newPos < labels.size() && !disabled[newPos]) {
            // highlight the new typing position.
            labels.get(newPos).setStyle("-fx-background-color: #f8ecc2;");
        }

        // set the new typing position
        this.typingPos = newPos;
    }

}
