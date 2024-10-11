package com.example.reviewservice.repository;

import com.example.reviewservice.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query("update Product p"
        + " set p.score = ((p.score * p.reviewCount) + :score) / (p.reviewCount+1), p.reviewCount = p.reviewCount+1"
        + " where p.id = :productId")
    void updateScoreAndReviewCnt(@Param(value = "productId") Long productId, @Param(value = "score") int score);

}
