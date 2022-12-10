package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * This class is the controller for the spelling quiz game.
 */

public class Game {

    private boolean isReattempt;
    private boolean isPractice;
    private ArrayList<Word> words;
    private QuizInput quizInput;
    private long last;
    private int index;
    private AudioClip correct, incorrect, tryAgain;
    
    @FXML Label topicLabel;
    @FXML Label progressLabel;
    @FXML Button listenButton;
    @FXML Button skipButton, nextButton, submitButton;
    @FXML AnchorPane pane;
    @FXML ChoiceBox<Float> speedSelector;

    private Word currentWord() {
        return words.get(index);
    }

    private boolean isCorrect() {
        return currentWord().toString().equalsIgnoreCase(quizInput.getText());
    }

    private boolean isAudioPlaying() {
        return Festival.isSpeaking || correct.isPlaying() || incorrect.isPlaying() || tryAgain.isPlaying();
    }
    
    @FXML
    public void startGame(String topic, boolean isPractice) {
    	topicLabel.setText(topic);
        pane.addEventFilter(KeyEvent.KEY_PRESSED, quizInput::handleKeyPress);
        this.words = Words.getWords(topic);
        this.isPractice = isPractice;
        index = -1;
        next();
    }

    @FXML
    private void initialize() {
        // initialize the quiz input system
        quizInput = new QuizInput();
        quizInput.setLayoutX(120);
        quizInput.setLayoutY(330);
        quizInput.setVisible(true);
        pane.getChildren().add(quizInput);

        // initialize the speech speed selector
        speedSelector.getItems().addAll(1f, 0.75f, 0.5f);
        speedSelector.setValue(1f);
        speedSelector.setOnAction(event -> Festival.speed = speedSelector.getValue());

        // initialize the audio clips
        correct   = new AudioClip(getClass().getResource("audio/correct.mp3").toExternalForm());
        incorrect = new AudioClip(getClass().getResource("audio/incorrect.mp3").toExternalForm());
        tryAgain  = new AudioClip(getClass().getResource("audio/tryAgain.mp3").toExternalForm());
    }

    @FXML
    private void next() {
        // record the time taken to answer the word
        long now = System.currentTimeMillis();
        if (index >= 0) {
            currentWord().time = (now - last) / 1000f;
        }
        last = now;

        // check if there are no more words left
        if (++index >= words.size()) {
            if (isPractice) {
                Main.loadScene("Menu");
            } else {
                RewardScreen rewardScreen = (RewardScreen) Main.loadScene("RewardScreen");
                rewardScreen.setWordStats(words);
            }
            return;
        }

        // reset the button properties
        submitButton.setVisible(true);
        nextButton.setVisible(false);
        submitButton.setDefaultButton(true);
        nextButton.setDefaultButton(false);

        // move on to the next word
        isReattempt = false;
        progressLabel.setText("Word " + (index+1) + " of " + words.size() + ":");
        quizInput.nextWord(currentWord().toString());
        Festival.speak(currentWord().toString(), true, () -> skipButton.setDisable(false));
    }

    @FXML
    private void submit() {

        if (isAudioPlaying()) {
            // the user must wait for the current word to finish speaking before submitting
            return;
        }
        
        if (isCorrect()) {
        	correct.play();
            currentWord().correct = true;
            sleep(1);
            next();
        } else {
            if (isReattempt) {
                incorrect.play();
                if (isPractice) {
                    submitButton.setVisible(false);
                    submitButton.setDefaultButton(false);
                    nextButton.setDefaultButton(true);
                    nextButton.setVisible(true);
                    quizInput.revealWord();
                } else {
                    sleep(1);
                    next();
                }
            } else {
                tryAgain.play();
                quizInput.clear();
                isReattempt = true;
                if (isPractice) {
                    // reveal the second letter as a hint
                    quizInput.reveal(1);
                }
            }
        }
    }

    @FXML
    private void listen() {
        if (!isAudioPlaying()) {
            listenButton.setDisable(true);
            Festival.speak(currentWord().toString(), true, () -> listenButton.setDisable(false));
        }
    }

    @FXML
    private void skip() {
        if (!isAudioPlaying()) {
            skipButton.setDisable(true);
            next();
        }
    }

    @FXML
    private void menu() {
        Main.loadScene("Menu");
    }

    private void sleep(long timeout) {
        try {
            TimeUnit.SECONDS.sleep(timeout);
        } catch (InterruptedException ignored){}
    }

}
