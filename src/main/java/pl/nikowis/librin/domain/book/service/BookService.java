package pl.nikowis.librin.domain.book.service;

import pl.nikowis.librin.domain.book.model.Book;

import java.util.List;

public interface BookService {
    List<Book> findAuthor(String authorQuery);

    List<Book> findBooksByTitle(String titleQuery);

    List<Book> findBooksByAuthorAndTitle(String author, String titleQuery);
}
