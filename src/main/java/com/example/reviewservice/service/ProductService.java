package com.example.reviewservice.service;

import com.example.reviewservice.domain.Product;
import com.example.reviewservice.exception.CustomErrorException;
import com.example.reviewservice.exception.ErrorCode;
import com.example.reviewservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    public Product getProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
            () -> new CustomErrorException(ErrorCode.NOT_FOUNT_PRODUCT)
        );
        return product;
    }
}
