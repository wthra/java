import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class WordDocumentMatrix {
    private List<String> words;
    private int[][] wordDocumentMatrix;
    private int documentCount;

    public WordDocumentMatrix() {
        words = new ArrayList<>();
        documentCount = 0;
    }

    public void indexDocuments(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.isDirectory()) {
            System.err.println("Invalid folder path!");
            return;
        }

        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            System.err.println("No files found in the folder!");
            return;
        }

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                documentCount++;
                indexDocument(file);
            }
        }
    }

    private void indexDocument(File file) {
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                String word = scanner.next().toLowerCase().replaceAll("[^a-zA-Z0-9]", "");
                if (!words.contains(word)) {
                    words.add(word);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void constructWordDocumentMatrix(String folderPath) {
        Collections.sort(words);
        wordDocumentMatrix = new int[words.size()][documentCount];
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            System.err.println("No files found in the folder!");
            return;
        }

        for (int i = 0; i < documentCount; i++) {
            try (Scanner scanner = new Scanner(files[i])) {
                while (scanner.hasNext()) {
                    String word = scanner.next().toLowerCase().replaceAll("[^a-zA-Z0-9]", "");
                    int wordIndex = words.indexOf(word);
                    if (wordIndex != -1) {
                        wordDocumentMatrix[wordIndex][i]++;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void exportToCSV(String csvFilePath) {
        constructWordDocumentMatrix("C://Users//User//Downloads//data");

        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFilePath))) {
            writer.print("Words");
            for (int i = 1; i <= documentCount; i++) {
                writer.print(",doc" + i);
            }
            writer.println();

            for (int i = 0; i < words.size(); i++) {
                writer.print(words.get(i));
                for (int j = 0; j < documentCount; j++) {
                    writer.print("," + wordDocumentMatrix[i][j]);
                }
                writer.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WordDocumentMatrix searchEngine = new WordDocumentMatrix();
        searchEngine.indexDocuments("C://Users//User//Downloads//data");
        searchEngine.exportToCSV("fileresults.csv");
        System.out.println("File path: fileresults.csv");
    }
}