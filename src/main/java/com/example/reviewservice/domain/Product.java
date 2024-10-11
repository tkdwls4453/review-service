package com.example.reviewservice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends TimeBaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int reviewCount;
    private Float score;

    public Product(int reviewCount, Float score) {
        this.reviewCount = reviewCount;
        this.score = score;
    }

    // 동시성 이슈 발생 가능
    public synchronized void addReview(Review review) {
        float totalScore = this.score * this.reviewCount;
        totalScore += review.getScore();
        this.reviewCount++;

        this.score = Math.round((totalScore / this.reviewCount) * 10) / 10.0f;
    }
}
