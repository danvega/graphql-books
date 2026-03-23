package dev.danvega.books.book;

import dev.danvega.books.author.Author;
import dev.danvega.books.author.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@Transactional
class BookRepositoryTests {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    void shouldFindBooksByTitle() {
        Author author = new Author();
        author.setName("Test Author");
        authorRepository.save(author);

        Book book = new Book();
        book.setTitle("Unique Repository Test Book");
        book.setAuthor(author);
        bookRepository.save(book);

        List<Book> found = bookRepository.findAllByTitleContainsIgnoreCase("Unique Repository Test");
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getTitle()).contains("Unique Repository Test");
    }
}
