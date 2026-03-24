package dev.danvega.books.book;

import dev.danvega.books.author.Author;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface BookRepository extends JpaRepository<Book,Long> {

    @Override
    @EntityGraph(attributePaths = "author")
    List<Book> findAll();

    List<Book> findAllByTitleContainsIgnoreCase(String title);

    Arrays findAllByAuthorIdIn(List<Long> authorIds);

    List<Book> findByAuthorIdIn(List<Long> authorIds);

    List<Book> findByAuthor(Author author);
}
