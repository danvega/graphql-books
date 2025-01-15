package dev.danvega.books.book;

import dev.danvega.books.author.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class BookRepositoryTests {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldFindBooksByTitle() {
        // Setup test data
        Author author = new Author();
        author.setName("Test Author");
        entityManager.persist(author);

        Book book = new Book();
        book.setTitle("Spring Boot Testing");
        book.setAuthor(author);
        entityManager.persist(book);

        List<Book> found = bookRepository.findAllByTitleContainsIgnoreCase("Spring");
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getTitle()).contains("Spring");
    }
}
