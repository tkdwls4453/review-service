package com.example.reviewservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.reviewservice.domain.Product;
import com.example.reviewservice.domain.Review;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class ProductConcurrencyTest {

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product(0, 0.0f);
    }

    @Test
    public void addReview_concurrencyTest() throws InterruptedException {
        int threadCount = 100;  // 동시에 100개의 스레드가 동작하도록 설정
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int index = i;  // i 값을 새로운 final 변수로 선언
            int score = index % 5 + 1;  // 각 스레드가 다른 점수를 추가

            executorService.submit(() -> {
                try {
                    Long userId = (long) (index + 1);  // userId를 i + 1로 설정
                    Review review = Review.builder()
                        .authorId(userId)
                        .score(score)
                        .content("test")
                        .product(product)
                        .build();

                    // 강제로 지연을 주어 동시성 문제를 유발
                    Thread.sleep(100);

                    product.addReview(review);  // 동시에 여러 스레드가 addReview 메서드 호출
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();  // 작업 완료
                }
            });
        }

        latch.await();  // 모든 스레드가 종료될 때까지 대기
        executorService.shutdown();

        // 예상된 최종 점수 계산
        float expectedScore = calculateExpectedScore(threadCount);

        // 결과 확인
        System.out.println("최종 리뷰 수: " + product.getReviewCount());
        System.out.println("최종 점수: " + product.getScore());

        // 동시성 문제가 없으면 리뷰 수가 정확히 100이어야 함
        assertEquals(100, product.getReviewCount());

        // 동시성 문제가 없으면 점수는 예상된 값과 같아야 함
        assertEquals(expectedScore, product.getScore());  // 동시성 문제가 없을 때는 예상 점수와 일치해야 함
    }

    // 예상된 스코어를 계산하는 메서드
    private float calculateExpectedScore(int reviewCount) {
        float sum = 0;
        for (int i = 0; i < reviewCount; i++) {
            sum += (i % 5 + 1);  // 각 스레드에서 추가되는 score 값
        }
        return sum / reviewCount;
    }
}
