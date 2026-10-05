package dev.danvega.books.book;

import dev.danvega.books.author.AuthorRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class BookController {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookController(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    // @SchemaMapping(typeName = "Query", field = "books")
    @QueryMapping
    public List<Book> books() {
        return bookRepository.findAll();
    }

    @QueryMapping
    public Optional<Book> book(@Argument Long id) {
        return bookRepository.findById(id);
    }

    @MutationMapping
    public Book addBook(@Argument BookInput bookInput) {
        var author = authorRepository.findById(bookInput.authorId());
        var book = new Book();
        book.setTitle(bookInput.title());
        book.setAuthor(author.orElseThrow());
        return bookRepository.save(book);
    }

}
