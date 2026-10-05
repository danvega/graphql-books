package dev.danvega.books.review;

public record ReviewFilter(
        Integer rating,
        Boolean verified,
        String reviewerName
) {}
