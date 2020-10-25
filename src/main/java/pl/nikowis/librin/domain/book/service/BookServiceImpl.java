package pl.nikowis.librin.domain.book.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nikowis.librin.domain.book.model.Book;
import pl.nikowis.librin.infrastructure.repository.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookServiceImpl.class);

    @Autowired
    private BookRepository bookRepository;


    @Override
    public List<Book> findAuthor(String authorQuery) {
        return bookRepository.findAuthorsByQuery(authorQuery).stream().map(s -> {
            Book book = new Book();
            book.setAuthor(s);
            return book;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Book> findBooksByTitle(String titleQuery) {
        return bookRepository.findBookByQuery(titleQuery);
    }

    @Override
    public List<Book> findBooksByAuthorAndTitle(String author, String titleQuery) {
        return bookRepository.findBookByAuthorAndQuery(author, titleQuery);
    }
}
