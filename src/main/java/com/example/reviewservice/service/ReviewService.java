package com.example.reviewservice.service;

import com.example.reviewservice.domain.Product;
import com.example.reviewservice.domain.Review;
import com.example.reviewservice.dto.CreateReviewRequestDto;
import com.example.reviewservice.dto.ReviewPageResponseDto;
import com.example.reviewservice.dto.ReviewResponseDto;
import com.example.reviewservice.exception.CustomErrorException;
import com.example.reviewservice.exception.ErrorCode;
import com.example.reviewservice.repository.ProductRepository;
import com.example.reviewservice.repository.ReviewRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final S3Service s3Service;
    private final ProductService productService;
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    public void createReview(Long productId, MultipartFile file, CreateReviewRequestDto dto) {

        Product product = productService.getProduct(productId);

        if(reviewRepository.existsByAuthorIdAndProduct(dto.userId(), product)){
            throw new CustomErrorException(ErrorCode.ALREADY_REVIEWED);
        }

        Review review = Review.createReview(dto, product);
        String imagePath = s3Service.uploadImage(file);
        review.updateImage(imagePath);
        product.addReview(review);
//        productRepository.updateScoreAndReviewCnt(productId, review.getScore());

        reviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public ReviewPageResponseDto getReviews(Long productId, Long cursor, int size){
        PageRequest pageRequest = PageRequest.of(0, size);
        Product product = productService.getProduct(productId);
        List<Review> reviews = reviewRepository.findByProductIdAndCursor(productId, cursor, pageRequest);

        List<ReviewResponseDto> reviewResponseDtos = reviews.stream()
            .map(ReviewResponseDto::from)
            .collect(Collectors.toList());

        int reviewCount = product.getReviewCount();
        Float score = product.getScore();

        Long lastReviewId = reviews.size() > 0 ? reviews.get(reviews.size() - 1).getId() : null;

        return ReviewPageResponseDto.builder()
            .totalCount(reviewCount)
            .score(score)
            .cursor(lastReviewId)
            .reviews(reviewResponseDtos)
            .build();
    }

}
