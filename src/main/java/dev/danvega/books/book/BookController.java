package dev.danvega.books.book;

import dev.danvega.books.author.AuthorRepository;
import dev.danvega.books.review.Review;
import dev.danvega.books.review.ReviewRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class BookController {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final ReviewRepository reviewRepository;

    public BookController(BookRepository bookRepository, AuthorRepository authorRepository, ReviewRepository reviewRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.reviewRepository = reviewRepository;
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

    @BatchMapping
    public List<List<Review>> reviews(List<Book> books) {
        List<Long> bookIds = books.stream()
                .map(Book::getId)
                .toList();

        // One query for the reviews of every book in this request
        Map<Long, List<Review>> reviewsByBookId = reviewRepository.findByBookIdIn(bookIds).stream()
                .collect(Collectors.groupingBy(review -> review.getBook().getId()));

        return books.stream()
                .map(book -> reviewsByBookId.getOrDefault(book.getId(), Collections.emptyList()))
                .toList();
    }

}
