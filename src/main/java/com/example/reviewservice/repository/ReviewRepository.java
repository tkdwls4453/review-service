package com.example.reviewservice.repository;

import com.example.reviewservice.domain.Product;
import com.example.reviewservice.domain.Review;
import java.awt.print.Pageable;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByAuthorIdAndProduct(Long authorId, Product product);

    @Query(
        "select r"
            + " from Review r"
            + " where r.product.id = :productId and (:cursor is null or r.id < :cursor)"
            + " order by r.createdAt desc")
    List<Review> findByProductIdAndCursor(@Param("productId") Long productId, Long cursor, PageRequest pageRequest);

}
