package dev.danvega.books.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.graphql.data.GraphQlRepository;

@GraphQlRepository
public interface ReviewRepository extends JpaRepository<Review,Long>, QueryByExampleExecutor<Review> {
}
