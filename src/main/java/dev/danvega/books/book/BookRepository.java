package dev.danvega.books.book;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book,Long> {

    @Override
    @EntityGraph(attributePaths = "author")
    List<Book> findAll();

    List<Book> findAllByTitleContainsIgnoreCase(String title);
}
