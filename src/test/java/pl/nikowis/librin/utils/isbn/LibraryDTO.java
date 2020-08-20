package pl.nikowis.librin.utils.isbn;

import lombok.Data;

import java.util.List;

@Data
public class LibraryDTO {

    public static final String BOOKS_RAW_FILENAME = "books-raw.json";

    private List<BookDTO> books;

    public LibraryDTO() {
    }

    public LibraryDTO(List<BookDTO> books) {
        this.books = books;
    }

}
