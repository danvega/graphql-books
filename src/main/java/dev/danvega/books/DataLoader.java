package dev.danvega.books;

import dev.danvega.books.author.Author;
import dev.danvega.books.author.AuthorRepository;
import dev.danvega.books.book.Book;
import dev.danvega.books.book.BookRepository;
import dev.danvega.books.review.Review;
import dev.danvega.books.review.ReviewRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class DataLoader implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;

    private record AuthorsAndBooks(
            Map<String, Author> authors,
            Map<String, Book> books
    ) {}

    public DataLoader(AuthorRepository authorRepository, BookRepository bookRepository, ReviewRepository reviewRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        var authorsAndBooks = loadAuthorsAndBooks();
        loadReviews(authorsAndBooks);
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

        return new AuthorsAndBooks(
                Map.of(
                        "josh", josh,
                        "mark", mark,
                        "greg", greg
                ),
                Map.of(
                        "cloudNative", cloudNative,
                        "reactiveSpring", reactiveSpring,
                        "springBootInAction", springBootInAction,
                        "learningSpring", learningSpring
                )
        );
    }

    private void loadReviews(AuthorsAndBooks data) {
        // Cloud Native Java Reviews
        Review review1 = new Review();
        review1.setBook(data.books().get("cloudNative"));
        review1.setRating(5);
        review1.setComment("Exceptional deep dive into Cloud Native Java! Josh's expertise shines through every chapter.");
        review1.setReviewerName("Sarah Chen");
        review1.setVerified(true);
        review1.setCreatedAt(LocalDateTime.now().minusDays(5));
        reviewRepository.save(review1);

        Review review2 = new Review();
        review2.setBook(data.books().get("cloudNative"));
        review2.setRating(5);
        review2.setComment("A masterpiece on Cloud Native Java. The examples are practical and the concepts are explained brilliantly!");
        review2.setReviewerName("Mike Johnson");
        review2.setVerified(true);
        review2.setCreatedAt(LocalDateTime.now().minusDays(10));
        reviewRepository.save(review2);

        // Spring Boot: Up and Running Reviews
        Review review3 = new Review();
        review3.setBook(data.books().get("reactiveSpring"));
        review3.setRating(5);
        review3.setComment("Mark delivers a perfect guide for Spring Boot - clear, concise, and incredibly practical!");
        review3.setReviewerName("John Smith");
        review3.setVerified(true);
        review3.setCreatedAt(LocalDateTime.now().minusDays(2));
        reviewRepository.save(review3);

        Review review4 = new Review();
        review4.setBook(data.books().get("reactiveSpring"));
        review4.setRating(5);
        review4.setComment("Comprehensive coverage from basics to advanced topics. A must-read for any Spring developer!");
        review4.setReviewerName("Anonymous");
        review4.setVerified(false);
        review4.setCreatedAt(LocalDateTime.now().minusDays(15));
        reviewRepository.save(review4);

        // Spring Boot in Action Reviews
        Review review5 = new Review();
        review5.setBook(data.books().get("springBootInAction"));
        review5.setRating(5);
        review5.setComment("Greg's expertise makes Spring Boot approachable and exciting. Best technical book I've read this year!");
        review5.setReviewerName("Linda Martinez");
        review5.setVerified(true);
        review5.setCreatedAt(LocalDateTime.now().minusDays(7));
        reviewRepository.save(review5);

        // Learning Spring Boot 3.0 Reviews
        Review review6 = new Review();
        review6.setBook(data.books().get("learningSpring"));
        review6.setRating(5);
        review6.setComment("Fantastic coverage of Spring Boot 3.0! Greg makes complex topics easy to understand.");
        review6.setReviewerName("David Wilson");
        review6.setVerified(true);
        review6.setCreatedAt(LocalDateTime.now().minusDays(1));
        reviewRepository.save(review6);

        Review review7 = new Review();
        review7.setBook(data.books().get("learningSpring"));
        review7.setRating(5);
        review7.setComment("Perfect balance of theory and practice. The examples are gold!");
        review7.setReviewerName("Sarah Chen");
        review7.setVerified(true);
        review7.setCreatedAt(LocalDateTime.now().minusDays(3));
        reviewRepository.save(review7);

        Review review8 = new Review();
        review8.setBook(data.books().get("learningSpring"));
        review8.setRating(5);
        review8.setComment("Comprehensive and well-structured. A perfect guide for all skill levels!");
        review8.setReviewerName("Bob");
        review8.setVerified(false);
        review8.setCreatedAt(LocalDateTime.now().minusDays(20));
        reviewRepository.save(review8);
    }

}