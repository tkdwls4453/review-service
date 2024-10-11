package com.example.reviewservice.dto;

import org.springframework.web.multipart.MultipartFile;

public record CreateReviewRequestDto(Long userId, int score, String content) {

}
