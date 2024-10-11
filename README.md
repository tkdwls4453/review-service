# [과제 1] 리뷰 서비스

## 비즈니스 요구 사항
- 리뷰는 존재하는 상품에만 작성할 수 있습니다.
- 유저는 하나의 상품에 대해 하나의 리뷰만 작성 가능합니다.
- 유저는 1~5점 사이의 점수와 리뷰를 남길 수 있습니다.
- 사진은 선택적으로 업로드 가능합니다.
    - 사진은 S3 에 저장된다고 가정하고, S3 적재 부분은 dummy 구현체를 생성합니다.
      (실제 S3 연동을 할 필요는 없습니다.)
- 리뷰는 '가장 최근에 작성된 리뷰' 순서대로 조회합니다.
---

## 기술 스택

- `Java17` `SpringBoot3` `JPA` `MySQL` `Docker`
---

## 인프라 구성

- `Docker`: Spring Boot 애플리케이션과 MySQL 데이터베이스를 컨테이너위에서 실행
- `Docker Compose`: 두 개의 서비스를 함께 관리하고 오케스트레이션
- `Gradle`: Spring Boot 애플리케이션 빌드

### 실행 방법
```
docker-compose up --build
```

---
## API

### **리뷰 조회 API**
GET  /products/{productId}/reviews?cursor={cursor}&size={size}

Response Body
``` json
{
  "totalCount": 15, // 해당 상품에 작성된 총리뷰 수
  "score": 4.6, // 평균 점수
  "cursor": 6,
  "reviews": [
    {
      "id": 15,
      "userId": 1, // 작성자 유저 아이디
      "score": 5,
      "content": "이걸 사용하고 제 인생이 달라졌습니다.",
      "imageUrl": "/image.png",
      "createdAt": "2024-11-25T00:00:00.000Z"
    },
    {
      "id": 14,
      "userId": 3, // 작성자 유저 아이디
      "score": 5,
      "content": "이걸 사용하고 제 인생이 달라졌습니다.",
      "imageUrl": null,
      "createdAt": "2024-11-24T00:00:00.000Z"
    }
  ]
}
```

### **리뷰 등록 API**
POST  /products/{productId}/reviews

**Request Part**

[이미지 파일]
MultipartFile 타입의 단건 이미지

[요청부]
``` json
{
  "userId": 1,
  "score": 4,
  "content": "이걸 사용하고 제 인생이 달라졌습니다.",
}
```
---
## **동시성 이슈 해결**
``` java

public void addReview(Review review) {
    float totalScore = this.score * this.reviewCount;
    totalScore += review.getScore();
    this.reviewCount++;

    this.score = Math.round((totalScore / this.reviewCount) * 10) / 10.0f;
    }

```
- 같은 상품에 동시에 여러 리뷰가 추가되면 상품의 평점과 리뷰 개수를 계산하면서 동시성 이슈가 발생
- 멀티 스레드 환경에서 발생하는 데이터 일관성 문제를 해결하기 위해 `synchronized` 키워드를 사용해 동기화 처리
- 공유 자원에 대한 안전한 접근을 보장하고, 동시성 이슈를 방지하여 시스템의 안정성을 확보
- 서버가 여러 대일 경우 해결책이 될 수 없어, 추후 확장성을 고려해 서버 간의 동시성 이슈를 해결하기 위해 낙관적 락의 도입을 검토
- 리뷰 작성 기능은 충돌 가능성이 낮다고 판단 -> 낙관적 락을 사용해도 성능 문제가 없을 것으로 예상


### 동시성 이슈 테스트
![Screenshot 2024-10-11 at 3 12 38 PM](https://github.com/user-attachments/assets/f6867190-4117-4427-a679-49566bc44d31)

100개의 스레드로 리뷰 작성 테스트 결과, 리뷰 수와 최종 점수가 맞지 않음을 확인

### 개선후
![Screenshot 2024-10-11 at 3 15 46 PM](https://github.com/user-attachments/assets/6d30e651-9cfd-42ed-90b7-e81356949f63)

---

## 커서 기반 페이지네이션
``` java
@Query(
"select r"
+ " from Review r"
+ " where r.product.id = :productId and (:cursor is null or r.id < :cursor)"
+ " order by r.createdAt desc")
List<Review> findByProductIdAndCursor(@Param("productId") Long productId, Long cursor, PageRequest pageRequest);
```

- offset 기반은 페이지가 뒤로 갈수록 읽어야 할 데이터가 증가하여 성능이 저하
- cursor 기반은 마지막으로 읽은 데이터 기준으로 데이터에 접근하여 성능상 유리

