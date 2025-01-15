package dev.danvega.books.author;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureGraphQlTester
class AuthorControllerTests {

    @Autowired
    private GraphQlTester graphQlTester;

    @Test
    void shouldBatchLoadBooksForAuthors() {
        String document = """
            query {
                authors {
                    id
                    name
                    books {
                        id
                        title
                    }
                }
            }
        """;

        graphQlTester.document(document)
                .execute()
                .path("authors")
                .entityList(Author.class)
                .satisfies(authors -> {
                    assertThat(authors).isNotEmpty();
                    assertThat(authors).allMatch(author -> !author.getBooks().isEmpty());
                });
    }
}
