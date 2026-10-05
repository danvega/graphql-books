package dev.danvega.books;

import dev.danvega.books.author.Author;
import dev.danvega.books.author.AuthorRepository;
import dev.danvega.books.book.Book;
import dev.danvega.books.book.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DataLoader implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    private record AuthorsAndBooks(
            Map<String, Author> authors,
            Map<String, Book> books
    ) {}

    public DataLoader(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadAuthorsAndBooks();
    }

    private AuthorsAndBooks loadAuthorsAndBooks() {
        // Create Authors
        Author josh = new Author();
        josh.setName("Josh Long");
        authorRepository.save(josh);

        Author mark = new Author();
        mark.setName("Mark Heckler");
        authorRepository.save(mark);

        Author greg = new Author();
        greg.setName("Greg Turnquist");
        authorRepository.save(greg);

        Author dan = new Author();
        dan.setName("Dan Vega");
        authorRepository.save(dan);

        Author nate = new Author();
        nate.setName("Nate Schutta");
        authorRepository.save(nate);

        Author craig = new Author();
        craig.setName("Craig Walls");
        authorRepository.save(craig);

        // Create Books
        Book cloudNative = new Book();
        cloudNative.setTitle("Cloud Native Java");
        cloudNative.setAuthor(josh);
        bookRepository.save(cloudNative);

        Book reactiveSpring = new Book();
        reactiveSpring.setTitle("Spring Boot: Up and Running");
        reactiveSpring.setAuthor(mark);
        bookRepository.save(reactiveSpring);

        Book springBootInAction = new Book();
        springBootInAction.setTitle("Spring Boot in Action");
        springBootInAction.setAuthor(greg);
        bookRepository.save(springBootInAction);

        Book learningSpring = new Book();
        learningSpring.setTitle("Learning Spring Boot 3.0");
        learningSpring.setAuthor(greg);
        bookRepository.save(learningSpring);

        Book fundamentals = new Book();
        fundamentals.setTitle("Fundamentals of Software Engineering");
        fundamentals.setAuthor(dan);
        bookRepository.save(fundamentals);

        Book springInAction = new Book();
        springInAction.setTitle("Spring in Action");
        springInAction.setAuthor(craig);
        bookRepository.save(springInAction);

        Book springAiInAction = new Book();
        springAiInAction.setTitle("Spring AI in Action");
        springAiInAction.setAuthor(craig);
        bookRepository.save(springAiInAction);

        return new AuthorsAndBooks(
                Map.of(
                        "josh", josh,
                        "mark", mark,
                        "greg", greg,
                        "dan", dan,
                        "nate", nate,
                        "craig", craig
                ),
                Map.of(
                        "cloudNative", cloudNative,
                        "reactiveSpring", reactiveSpring,
                        "springBootInAction", springBootInAction,
                        "learningSpring", learningSpring,
                        "fundamentals", fundamentals,
                        "springInAction", springInAction,
                        "springAiInAction", springAiInAction
                )
        );
    }

}