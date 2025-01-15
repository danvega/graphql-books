package dev.danvega.books.search;

import dev.danvega.books.author.AuthorRepository;
import dev.danvega.books.book.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {

    private static final Logger log = LoggerFactory.getLogger(SearchController.class);
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public SearchController(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @QueryMapping
    List<Object> search(@Argument String text) {
        log.debug("Searching for '" + text + "'");
        List<Object> results = new ArrayList<>();
        results.addAll(authorRepository.findAllByNameContainsIgnoreCase(text));
        results.addAll(bookRepository.findAllByTitleContainsIgnoreCase(text));
        return results;
    }
}
