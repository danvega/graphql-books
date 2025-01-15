package dev.danvega.books.review;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureGraphQlTester
@Transactional
class ReviewControllerTests {

    @Autowired
    private GraphQlTester graphQlTester;

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
