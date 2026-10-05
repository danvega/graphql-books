package dev.danvega.books.author;

import dev.danvega.books.book.Book;
import dev.danvega.books.book.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AuthorController {

    private static final Logger log = LoggerFactory.getLogger(AuthorController.class);
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public AuthorController(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @QueryMapping
    public List<Author> authors() {
        return authorRepository.findAll();
    }

    // Called once for every author in the result (the N+1 problem)
    @SchemaMapping
    public List<Book> books(Author author) throws InterruptedException {
        // this could be a call to some microservice that retrieves books by authorId
        log.info("Retrieving books for author " + author.getName());
        Thread.sleep((1000));
        return bookRepository.findByAuthor(author);
    }

}
