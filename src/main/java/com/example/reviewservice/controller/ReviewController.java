package com.example.reviewservice.controller;

import com.example.reviewservice.controller.response.Response;
import com.example.reviewservice.dto.CreateReviewRequestDto;
import com.example.reviewservice.dto.ReviewPageResponseDto;
import com.example.reviewservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/products/{productId}/reviews")
    public void createReview(
        @PathVariable(name = "productId") Long productId,
        @RequestPart MultipartFile image,
        @RequestPart CreateReviewRequestDto request
    ) {
        reviewService.createReview(productId, image, request);
    }

    @GetMapping("/products/{productId}/reviews")
    public ReviewPageResponseDto getProductReviews(
        @PathVariable(name = "productId") Long productId,
        @RequestParam(required = false) Long cursor,
        @RequestParam(defaultValue = "10") int size
    ){
        return reviewService.getReviews(productId, cursor, size);
    }
}

