package dev.danvega.books.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.graphql.data.GraphQlRepository;

import java.util.List;

@GraphQlRepository
public interface ReviewRepository extends JpaRepository<Review,Long>, QueryByExampleExecutor<Review> {

    List<Review> findByBookIdIn(List<Long> bookIds);

}
