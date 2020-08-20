package pl.nikowis.librin.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.nikowis.librin.utils.isbn.BookDTO;
import pl.nikowis.librin.utils.isbn.BooksFilterRunner;

import javax.xml.bind.JAXBException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ISBNMain {


    public static final String BASE_PATH = "C:\\Users\\nikow\\Desktop\\librin-api\\src\\test\\java\\pl\\nikowis\\librin\\utils\\";
    public static final String PL_LANG_VALUE = "pol";
    public static final String BOOK_FORM_VALUE = "BA";
    public static final String HARDBACK_BOOK_FORM_VALUE = "BB";
    public static final String SOFTBACK_BOOK_FORM_VALUE = "BC";
    public static final List<String> INCORRECT_AUTHORS = Arrays.asList("autor anonimowy", "publikacja zbiorowa"
            , "autor anonimowy,", "publikacja zbiorowa,", "praca zbiorowa", "dom wydawniczy bellona", "z500 sp. z o.o."
            , "paweł boduch", "stanisław gola", "janusz ledwoch", "tomasz pisaniak", "paweł głowacki", "łukasz lelo"
            , "wiesława żaba-żabińska", "arch. tomasz sobieszuk", "aneta gibek-wiśniewska"
    );
    public static final String CSV_SEPARATOR = ",";
    public static final String CSV_HEADERS = "id,author,title,subtitle,datestamp";
    public static final int MAX_TITLE_LENGTH = 50;
    public static final int MAX_AUTHOR_LENGTH = 30;


    public static void main(String[] args) throws IOException, JAXBException {
        long time = System.currentTimeMillis();

//        IsbnXMLMapper isbnXMLMapper = new IsbnXMLMapper(BASE_PATH + "bazaISBN.xml");
//        isbnXMLMapper.map();
//        isbnXMLMapper.save(BASE_PATH + LibraryDTO.BOOKS_RAW_FILENAME);

        List<BookDTO> finalBookList = new BooksFilterRunner(BASE_PATH)
                .addStep("correct_form", (library) -> library.stream().filter(ISBNMain::isCorrectForm).collect(Collectors.toList()))
                .addStep("only_polish", (library) -> library.stream().filter(ISBNMain::isInPolish).collect(Collectors.toList()))
                .addStep("filter_contibutors", (library) -> {
                    ArrayList<BookDTO> res = new ArrayList<>();
                    for (BookDTO b : library) {
                        List<BookDTO.Contributor> contributors = b.contributors.stream().filter(c -> isCorrectContributorRole(c.getRole()) && isCorrectAuthorName(c.getName())).collect(Collectors.toList());
                        if (contributors.size() > 0) {
                            b.setContributors(contributors);
                            res.add(b);
                        }
                    }
                    return res;
                })
                .addStep("fill_author", (library) -> {
                    ArrayList<BookDTO> res = new ArrayList<>();
                    for (BookDTO b : library) {
                        List<BookDTO.Contributor> contributors = b.contributors;
                        b.setAuthor(normalizaName(contributors.get(0).getName()));
                        res.add(b);
                    }
                    return res;
                })
                .addStep("short_authors", (library -> library.stream().filter(bookDTO -> bookDTO.getAuthor().length() < MAX_AUTHOR_LENGTH).collect(Collectors.toList())))
                .addStep("short_titles", (library -> library.stream().filter(bookDTO -> bookDTO.getTitle().length() < MAX_TITLE_LENGTH && (bookDTO.getSubtitle() == null || bookDTO.getSubtitle().length() < MAX_TITLE_LENGTH)).collect(Collectors.toList())))
                .addStep("distinct_books", (library -> library.stream().distinct().collect(Collectors.toList())))
                .run();

        printDistinctCount(finalBookList);
        printBucketCounts(finalBookList);

        saveToCSV(finalBookList);
        System.out.println("Finished in " + (System.currentTimeMillis() - time) / 1000 + "s");

    }

    private static void printBucketCounts(List<BookDTO> books) {
        Map<String, Long> authorCounts = books.stream().collect(Collectors.groupingBy(BookDTO::getAuthor, Collectors.counting()));
        List<AuthorBucket> authorBuckets = new LinkedList<>();
        authorCounts.forEach((a, c) -> {
            if (c > 50) {
                authorBuckets.add(new AuthorBucket(a, c));
            }
        });

        System.out.println("Bucketed authors " + authorBuckets.size());

        authorBuckets.forEach(ab -> {
            System.out.printf("%s - \"%s\"\n", ab.count, ab.author);
        });

    }

    private static void printDistinctCount(List<BookDTO> books) {
        List<String> distinctTitles = books.stream().map(BookDTO::getTitle).distinct().collect(Collectors.toList());
        List<String> distinctAuthors = books.stream().map(BookDTO::getAuthor).distinct().collect(Collectors.toList());
        System.out.println("Distinct authors: " + distinctAuthors.size());
        System.out.println("Distinct titles: " + distinctTitles.size());
    }

    private static void saveToCSV(List<BookDTO> list) {
        StringBuilder csvRecords = new StringBuilder();
        csvRecords.append(CSV_HEADERS);
        csvRecords.append('\n');
        for (BookDTO b : list) {
            csvRecords.append(b.toCsvRecord());
            csvRecords.append('\n');
        }
        try {
            writeStrToFile(csvRecords.toString(), "books-final.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeStrToFile(String fileContent, String fileName) throws IOException {
        String tempFile = BASE_PATH + File.separator + fileName;
        File file = new File(tempFile);
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(fileContent);
        bw.close();
    }

    private static boolean isCorrectAuthorName(String author) {
        int spacesCount = author.split(" ").length - 1;
        String[] commaSplit = author.split(",");
        int commaSplitLength = commaSplit.length;
        String normalizedAuthro = author.toLowerCase().trim();
        return author.contains(" ") && !INCORRECT_AUTHORS.contains(normalizedAuthro) && (commaSplitLength < 3 || (commaSplitLength == 3 && spacesCount == 2)) && !normalizedAuthro.contains("wydawnictwo") && !normalizedAuthro.contains("zespół") && !normalizedAuthro.contains("zbiorowa");
    }

    private static String normalizaName(String name) {
        if (name != null && name.contains(",")) {
            String[] split = name.split(",");
            if (split.length == 1) {
                name = split[0].trim();
            } else if (split.length == 2) {
                name = split[1].trim() + " " + split[0].trim();
            } else if (split.length == 3) {
                name = split[1].trim() + " " + split[2].trim() + " " + split[0].trim();
            }
        }
        return name;
    }

    private static boolean isCorrectContributorRole(String contributorRole) {
        //http://www.onix-codelists.io/codelist/17
        return contributorRole != null && (contributorRole.equals("A01") || contributorRole.equals("A08") || contributorRole.equals("A07") || contributorRole.equals("A09") || contributorRole.equals("A14") || contributorRole.equals("A38"));
    }


    private static boolean isCorrectForm(BookDTO b) {
        //http://www.onix-codelists.io/codelist/150
        String form = b.getForm();
        return form.equals(BOOK_FORM_VALUE) || form.equals(HARDBACK_BOOK_FORM_VALUE) || form.equals(SOFTBACK_BOOK_FORM_VALUE);
    }

    private static boolean isInPolish(BookDTO book) {
        return PL_LANG_VALUE.equals(book.getLanguage());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class AuthorBucket {
        public String author;
        public Long count;

    }

}
