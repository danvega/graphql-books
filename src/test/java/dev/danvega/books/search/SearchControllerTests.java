package dev.danvega.books.search;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.ExecutionGraphQlService;
import org.springframework.graphql.test.tester.ExecutionGraphQlServiceTester;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class SearchControllerTests {

    private final GraphQlTester graphQlTester;

    @Autowired
    SearchControllerTests(ExecutionGraphQlService graphQlService) {
        this.graphQlTester = ExecutionGraphQlServiceTester.builder(graphQlService).build();
    }

    @Test
    void shouldSearchAcrossAuthorsAndBooks() {
        var document = """
            query($text: String!) {
                search(text: $text) {
                    ... on Book {
                        title
                    }
                    ... on Author {
                        name
                    }
                }
            }
        """;

        graphQlTester.document(document)
                .variable("text", "Spring")
                .execute()
                .path("search")
                .entityList(Object.class)
                .hasSize(5);
    }
}
