package pl.nikowis.librin.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.nikowis.librin.utils.isbn.BookDTO;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class LibraryTest {

    private List<BookDTO> library;

    @BeforeEach
    void setUp() throws IOException {
        library = Arrays.asList(new ObjectMapper().readValue(new File("C:\\Users\\nikow\\Desktop\\librin-api\\src\\test\\java\\pl\\nikowis\\librin\\utils\\books-final.json"), BookDTO[].class));
    }

    @Test
    public void testRemigiuszMroz() {
        List<BookDTO> filtered = library.stream().filter(book -> book.getAuthor().contains("Remigiusz Mróz")).collect(Collectors.toList());

        Assertions.assertTrue(filtered.size() >= 28);
    }

    @Test
    public void testHarryPotter() {
        List<BookDTO> filtered = library.stream().filter(book -> book.getTitle().toLowerCase().contains("potter") && book.getAuthor().toLowerCase().contains("rowling")).collect(Collectors.toList());

        Assertions.assertTrue(filtered.size() > 1);
        Assertions.assertTrue(hasOneMatchingBook(filtered, "J. K Rowling", "Harry Potter i kamień filozoficzny"));
        Assertions.assertTrue(hasOneMatchingBook(filtered, "J. K Rowling", "Harry Potter i komnata tajemnic"));
        Assertions.assertTrue(hasOneMatchingBook(filtered, "J. K Rowling", "Harry Potter i wiezień Azkabanu"));
        Assertions.assertTrue(hasOneMatchingBook(filtered, "J. K Rowling", "Harry Potter i Czara Ognia"));
        Assertions.assertTrue(hasOneMatchingBook(filtered, "J. K Rowling", "Harry Potter i Zakon Feniksa"));
        Assertions.assertTrue(hasOneMatchingBook(filtered, "J. K Rowling", "Harry Potter i książę półkrwi"));
        Assertions.assertTrue(hasOneMatchingBook(filtered, "J. K Rowling", "Harry Potter i insygnia śmierci"));

    }

    private boolean hasOneMatchingBook(List<BookDTO> books, String author, String title) {
        return books != null && books.size() > 0 && books.stream().filter(bookDTO -> bookDTO.getTitle().toLowerCase().equals(title.toLowerCase()) && bookDTO.getAuthor().toLowerCase().equals(author.toLowerCase())).count() == 1;
    }

}
