package pl.nikowis.librin.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.nikowis.librin.model.Book;
import pl.nikowis.librin.service.BookService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = BookController.CITIES_ENDPOINT)
public class BookController {

    public static final String CITIES_ENDPOINT = "/books";

    @Autowired
    private BookService bookService;

    @GetMapping
    public List<Book> findCities(@RequestParam(required = false) String author, @RequestParam(required = false) String title) {
        if (author != null && title != null) {
            return bookService.findBooksByAuthorAndTitle(author, title);
        } else if (author != null) {
            return bookService.findAuthor(author);
        } else if (title != null) {
            return bookService.findBooksByTitle(title);
        } else {
            return new ArrayList<>();
        }
    }

}
