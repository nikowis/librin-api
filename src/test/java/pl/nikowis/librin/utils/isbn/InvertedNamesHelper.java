package pl.nikowis.librin.utils.isbn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pl.nikowis.librin.utils.ISBNMain.BASE_PATH;

public class InvertedNamesHelper {

    public static final Map<String, String> AUTHOR_INVERT_NAME_MAPPINGS = new HashMap<>();

    public static void init() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(
                    BASE_PATH + "\\isbn\\invertedAuthors.csv"));
            String line = reader.readLine();
            while (line != null) {
                String[] split = line.split(";");
                if (line.trim().length() > 0 && split.length > 1) {
                    AUTHOR_INVERT_NAME_MAPPINGS.put(split[0], split[1]);
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Can't initialize InvertedNamesHelper");
        }


    }

    public static String findPossibleInvertedAuthors(List<BookDTO> finalBookList) {
        List<String> possible = new ArrayList<>();
        List<String> exactMatches = new ArrayList<>();
        System.out.println("\nFinding possible inverted author names: ");

        finalBookList.forEach(b1 -> {
            finalBookList.forEach(b2 -> {
                String b1Author = b1.getAuthor();
                String b2Author = b2.getAuthor();
                if (!b1Author.equals(b2Author)) {
                    String[] b1Split = b1Author.split(" ");

                    if (b1Split.length > 1 && b1Split[0].length() > 1 && b1Split[1].length() > 1 && b2Author.contains(b1Split[0]) && b2Author.contains(b1Split[1])) {
                        String[] b2Split = b2Author.split(" ");
                        String match = b1Author + ";" + b2Author;
                        String invertedMatch = b2Author + ";" + b1Author;
                        if (!exactMatches.contains(match) && isExactMatch(b1Split, b2Split)) {
                            exactMatches.add(match);
                            exactMatches.add(invertedMatch);
                        } else if (!exactMatches.contains(match) && !possible.contains(match)) {
                            possible.add(match);
                            possible.add(invertedMatch);
                        }
                    }
                }
            });
        });

        System.out.println("Authors to invert " + exactMatches.size());
        System.out.println("Possible authors to invert " + possible.size());

        StringBuilder possibleCsvRecords = new StringBuilder();
        StringBuilder exactCsvRecords = new StringBuilder();
        exactMatches.forEach(s -> {
            exactCsvRecords.append(s);
            exactCsvRecords.append('\n');
            exactCsvRecords.append('\n');
        });
        try {
            writeStrToFile(exactCsvRecords.toString(), "exactInvertedAuthors.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        possible.forEach(s -> {
            possibleCsvRecords.append(s);
            possibleCsvRecords.append('\n');
            possibleCsvRecords.append('\n');
        });
        try {
            writeStrToFile(possibleCsvRecords.toString(), "possibleInvertedAuthors.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //write to file
        return null;
    }

    private static boolean isExactMatch(String[] b1Split, String[] b2Split) {
        if (b1Split.length != b2Split.length) {
            return false;
        }
        boolean wordFound = false;
        for (String split1 : b1Split) {
            wordFound = false;
            for (String split2 : b2Split) {
                if (split1.equals(split2)) {
                    wordFound = true;
                }
            }
            if (!wordFound) {
                return false;
            }
        }
        return true;
    }

    private static void writeStrToFile(String fileContent, String fileName) throws IOException {
        String tempFile = BASE_PATH + File.separator + fileName;
        File file = new File(tempFile);
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(fileContent);
        bw.close();
    }


}
