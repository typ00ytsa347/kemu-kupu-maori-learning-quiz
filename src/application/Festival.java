package application;

import javafx.application.Platform;

import java.io.*;

/**
 * This class provides the interface between the Java application and the Festival program.
 */

public class Festival {

    private static Process process;
    private static BufferedWriter writer;
    private static BufferedReader reader;
    private static Runnable taskAfterSpeaking;
    private static Thread finishedListener;
    private static boolean running;
    public static float speed = 1;
    public static boolean isSpeaking;

    // this method initializes the Festival process and starts the auxiliary thread.
    public static void init() {
        running = true;
        ProcessBuilder pb = new ProcessBuilder("festival");
        try {
            process = pb.start();
            writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        } catch (IOException e) {
            throw new RuntimeException("Festival not installed!");
        }

        // This thread continuously waits for Festival to finish speaking then runs the completion task.
        finishedListener = new Thread(() -> {
            while (running) {
                try {
                    reader.readLine();
                } catch (IOException e) {
                    throw new RuntimeException();
                }
                isSpeaking = false;
                if (taskAfterSpeaking != null) {
                    Platform.runLater(taskAfterSpeaking);
                    taskAfterSpeaking = null;
                }
            }
        });
        finishedListener.start();
    }

    // This method safely shuts down the Festival process and auxiliary threads.
    public static void stop() {
        running = false;
        finishedListener.interrupt();
        process.destroy();
    }

    public static void speak(String word, boolean Maori) {
        //Festival doesn't like words with hyphens or capitals.
        word = word.toLowerCase().replaceAll("[-]", " ");
        try {
            if (Maori) {
                writer.write("(voice_akl_mi_pk06_cg)\n");
            } else {
                writer.write("(voice_kal_diphone)\n");
            }
            writer.write("(Parameter.set 'Duration_Stretch " + (1 / speed) + ")\n");
            writer.write("(SayText \"" + word + "\")\nabc\n");
            writer.flush();
            isSpeaking = true;
        } catch (IOException ignored) {
            throw new RuntimeException("Couldn't write to Festival process!");
        }
    }

    // speak a word and then run the task once finished.
    public static void speak(String word, boolean Maori, Runnable task) {
        if (!Festival.isSpeaking) {
            taskAfterSpeaking = task;
            speak(word, Maori);
        }
    }

}
