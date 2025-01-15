package dev.danvega.books.review;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

//@Controller
//public class ReviewController {
//
//    private final ReviewRepository reviewRepository;
//    private static final Logger log = LoggerFactory.getLogger(ReviewController.class);
//
//    public ReviewController(ReviewRepository reviewRepository) {
//        this.reviewRepository = reviewRepository;
//    }
//
//    @QueryMapping
//    public List<Review> reviews(@Argument(name = "filter") ReviewFilter filter) {
//        log.info("Searching for reviews with filter: {}", filter);
//
//        // If no filter, return all reviews
//        if (filter == null) {
//            return reviewRepository.findAll();
//        }
//
//        // Create an example review based on the filter
//        Review probe = new Review();
//
//        if (filter.rating() != null) {
//            probe.setRating(filter.rating());
//            log.info("Setting rating filter: {}", filter.rating());
//        }
//        if (filter.verified() != null) {
//            probe.setVerified(filter.verified());
//        }
//        if (filter.reviewerName() != null) {
//            probe.setReviewerName(filter.reviewerName());
//        }
//
//        // Create an ExampleMatcher that ignores all unset fields
//        ExampleMatcher matcher = ExampleMatcher.matching()
//                .withIgnoreNullValues()
//                .withIgnorePaths("createdAt")  // Ignore this field
//                .withMatcher("rating", ExampleMatcher.GenericPropertyMatchers.exact())
//                .withMatcher("reviewerName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
//                .withMatcher("verified", ExampleMatcher.GenericPropertyMatchers.exact());
//
//        Example<Review> example = Example.of(probe, matcher);
//        List<Review> results = reviewRepository.findAll(example);
//        log.info("Found {} reviews", results.size());
//        return results;
//    }
//}