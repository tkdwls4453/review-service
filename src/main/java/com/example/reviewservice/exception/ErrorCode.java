package com.example.reviewservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_INPUT_VALUE(400, "유효하지 않은 입력입니다."),
    INTERNAL_ERROR(500, "예상치 못한 서버 에러"),
    NOT_FOUNT_PRODUCT(1400, "존재하지 않는 상품입니다."),
    ALREADY_REVIEWED(1401, "이미 작성한 리뷰가 있습니다."),
    REVIEWER_NOT_FOUND(1402, "존재하지 않는 유저입니다."),
    INVALID_REVIEW_SCORE(1403, "리뷰의 스코어가 유효하지 않습니다."),
    EMPTY_REVIEW_CONTENT(1404, "리뷰 내용이 비어있습니다.");

    private final Integer code;
    private final String message;
}
