package application;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * This class handles reading from the excel wordlist file, stores the word lists and provides a method
 * of getting random words from a given topic.
 */

public class Words {

    private static HashMap<String, ArrayList<String>> wordLists;

    public static ArrayList<String> getTopics() {
        return new ArrayList<>(wordLists.keySet());
    }

    // get 5 random words from the given topic.
    public static ArrayList<Word> getWords(String topic) {
        if (!wordLists.containsKey(topic)) {
            throw new RuntimeException("No such topic!");
        }

        ArrayList<String> list = new ArrayList<>(wordLists.get(topic));
        ArrayList<Word> result = new ArrayList<>();
        Random rand = new Random();

        while (result.size() < 5 && !list.isEmpty()) {
            int idx = rand.nextInt(list.size());
            result.add(new Word(list.get(idx)));
            list.remove(idx);
        }

        return result;
    }

    public static void importWordLists() {
        wordLists = new HashMap<>();
        XSSFWorkbook workbook = getWorkbook();

        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            XSSFSheet sheet = workbook.getSheetAt(i);
            Iterator<Row> rowIterator = sheet.iterator();

            wordLists.put(workbook.getSheetName(i), new ArrayList<>());

            while (rowIterator.hasNext()) {
                String word = rowIterator.next().getCell(0).getStringCellValue().trim();
                if (word.length() > 0) {
                    wordLists.get(workbook.getSheetName(i)).add(word);
                };
            }
        }

    }

    private static XSSFWorkbook getWorkbook() {
        File myFile = new File("lists.xlsx");

        try {
            FileInputStream fis = new FileInputStream(myFile);
            return new XSSFWorkbook(fis);
        } catch (IOException e) {
            throw new RuntimeException("lists.xlsx wordlist file not found!");
        }
    }

}

