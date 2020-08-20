package pl.nikowis.librin.utils.isbn;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static pl.nikowis.librin.utils.isbn.LibraryDTO.BOOKS_RAW_FILENAME;

public class BooksFilterRunner {

    private String entryFilePath;
    private List<BookDTO> books;
    private List<Step> steps = new LinkedList<>();

    public BooksFilterRunner(String entryFilePath) {
        this.entryFilePath = entryFilePath;
    }

    public List<BookDTO> run() throws IOException {
        try {
            books = new ObjectMapper().readValue(new File(entryFilePath + BOOKS_RAW_FILENAME), LibraryDTO.class).getBooks();
            System.out.println("Running book filters, initial count " + books.size());
        } catch (IOException e) {
            System.out.println("ERROR READING ENTRY FILE");
            throw e;
        }

        for (Step s : steps) {
            books = s.filter.run(this.books);
            ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
            writer.writeValue(new File(entryFilePath + "books-" + s.stepName + ".json"), books);
            System.out.println("Step '"+s.stepName+"' filtered books size " + books.size());
        }
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        writer.writeValue(new File(entryFilePath + "books-final.json"), books);
        return books;
    }

    public BooksFilterRunner addStep(String stepName, LibraryFilter filter) {
        steps.add(new Step(stepName, filter));
        return this;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Step {
        String stepName;
        LibraryFilter filter;
    }

    public interface LibraryFilter {
        List<BookDTO> run(List<BookDTO> library);
    }


}
