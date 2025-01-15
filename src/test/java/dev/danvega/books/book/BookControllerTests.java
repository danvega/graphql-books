package dev.danvega.books.book;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureGraphQlTester
@Transactional
class BookControllerTests {

    @Autowired
    private GraphQlTester graphQlTester;

    @Test
    void shouldGetAllBooks() {
        // language=GraphQL
        var document = """
            query {
                books {
                    id
                    title
                    author {
                        name
                    }
                }
            }
        """;

        graphQlTester.document(document)
                .execute()
                .path("books")
                .entityList(Book.class)
                .hasSize(4);
    }

    @Test
    void shouldGetBookById() {
        var document = """
            query($id: Int!) {
                book(id: $id) {
                    id
                    title
                }
            }
        """;

        graphQlTester.document(document)
                .variable("id", 1)
                .execute()
                .path("book")
                .entity(Book.class)
                .satisfies(book -> {
                    assertThat(book.getId()).isEqualTo(1L);
                    assertThat(book.getTitle()).isNotNull();
                });
    }

    @Test
    void shouldAddNewBook() {
        var document = """
        mutation($input: BookInput!) {
            addBook(bookInput: $input) {
                id
                title
                author {
                    id
                    name
                }
            }
        }
        """;
        Map<String, Object> input = Map.of(
                "title", "New Book",
                "authorId", 1
        );

        graphQlTester.document(document)
                .variable("input", input)
                .execute()
                .path("addBook")
                .entity(Book.class)
                .satisfies(book -> {
                    assertThat(book.getTitle()).isEqualTo("New Book");
                    assertThat(book.getAuthor()).isNotNull();
                });
    }

}
