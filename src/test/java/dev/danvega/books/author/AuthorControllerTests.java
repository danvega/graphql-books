package dev.danvega.books.author;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.ExecutionGraphQlService;
import org.springframework.graphql.test.tester.ExecutionGraphQlServiceTester;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@Transactional
class AuthorControllerTests {

    private final GraphQlTester graphQlTester;

    @Autowired
    AuthorControllerTests(ExecutionGraphQlService graphQlService) {
        this.graphQlTester = ExecutionGraphQlServiceTester.builder(graphQlService).build();
    }

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
