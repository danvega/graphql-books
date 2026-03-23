package dev.danvega.books.review;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.ExecutionGraphQlService;
import org.springframework.graphql.test.tester.ExecutionGraphQlServiceTester;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@Transactional
class ReviewControllerTests {

    private final GraphQlTester graphQlTester;

    @Autowired
    ReviewControllerTests(ExecutionGraphQlService graphQlService) {
        this.graphQlTester = ExecutionGraphQlServiceTester.builder(graphQlService).build();
    }

    @Test
    void shouldFilterReviewsByRating() {
        graphQlTester.document("""
            query($filter: ReviewFilter!) {
                reviews(filter: $filter) {
                    rating
                    comment
                    verified
                }
            }
        """)
                .variable("filter", Map.of("rating", 5))
                .execute()
                .path("reviews")
                .entityList(Review.class)
                .hasSize(8)
                .satisfies(reviews ->
                        assertThat(reviews).allMatch(r -> r.getRating() == 5)
                );
    }
}
