package com.example.reviewservice.config;

import com.example.reviewservice.domain.Product;
import com.example.reviewservice.domain.Review;
import com.example.reviewservice.dto.CreateReviewRequestDto;
import com.example.reviewservice.repository.ProductRepository;
import com.example.reviewservice.service.ProductService;
import com.example.reviewservice.service.ReviewService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class AppConfig {

    @Bean
    public ApplicationRunner dataInitializer(ProductService productService, ReviewService reviewService){
        return args -> {
            for (long p = 1; p <= 2; p++) {
                Product product = productService.getProduct(p);
                for (int i = 1; i <= 100; i++) {
                    int score = i % 5 + 1;
                    CreateReviewRequestDto dto = new CreateReviewRequestDto((long) i, score,
                        "리뷰 " + i + ": 상품 " + p + "에 대한 리뷰입니다.");
                    reviewService.createReview(product.getId(), null, dto);
                }

            }
        };
    }
}
