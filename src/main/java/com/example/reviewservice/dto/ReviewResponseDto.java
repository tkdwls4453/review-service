package com.example.reviewservice.dto;

import com.example.reviewservice.domain.Review;

public record ReviewResponseDto(
    Long id,
    Long userId,
    int score,
    String content,
    String imageUrl,
    String createdAt
) {

    public static ReviewResponseDto from(Review review) {
        return new ReviewResponseDto(
            review.getId(),
            review.getAuthorId(),
            review.getScore(),
            review.getContent(),
            review.getImageUrl(),
            review.getCreatedAt().toString()
        );
    }

}
