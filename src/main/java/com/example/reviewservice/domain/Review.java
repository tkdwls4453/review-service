package com.example.reviewservice.domain;

import com.example.reviewservice.dto.CreateReviewRequestDto;
import com.example.reviewservice.exception.CustomErrorException;
import com.example.reviewservice.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(indexes = @Index(name = "idx_created_at", columnList = "created_at"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends TimeBaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long authorId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int score;

    private String imageUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    public static Review createReview(CreateReviewRequestDto dto, Product product) {
        return Review.builder()
            .authorId(dto.userId())
            .content(dto.content())
            .score(dto.score())
            .product(product)
            .build();
    }

    @Builder
    private Review(Long id, Long authorId, int score, String content, Product product) {
        if (authorId == null) {
            throw new CustomErrorException(ErrorCode.REVIEWER_NOT_FOUND);
        }

        if (score < 1 || score > 5) {
            throw new CustomErrorException(ErrorCode.INVALID_REVIEW_SCORE);
        }

        if (content == null) {
            throw new CustomErrorException(ErrorCode.EMPTY_REVIEW_CONTENT);
        }

        this.id = id;
        this.authorId = authorId;
        this.score = score;
        this.content = content;
        this.product = product;
    }

    public void updateImage(String imagePath) {
        this.imageUrl = imagePath;
    }
}
