package com.example.reviewservice.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record ReviewPageResponseDto(
    int totalCount,
    float score,
    Long cursor,
    List<ReviewResponseDto> reviews
) {

}
