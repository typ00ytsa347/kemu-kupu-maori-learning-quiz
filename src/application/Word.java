package application;

/**
 * This class encapsulates the statistics recorded for each word so that it can easily be
 * displayed by a CellValueFactory.
 */

public class Word {
    private String value;
    public float time;
    public boolean correct;

    public Word(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public String getWord() {
        return value;
    }

    public int getScore() {
        if (correct) {
            return ((int) (20 - Math.min(20f, time)));
        } else {
            return 0;
        }
    }

    public String getTime() {
        return String.format("%.2f", time);
    }

    public String getResult() {
        if (correct) {
            return "Correct";
        } else {
            return "Incorrect";
        }
    }
}
