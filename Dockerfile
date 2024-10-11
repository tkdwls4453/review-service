# Gradle 빌드를 위한 이미지
FROM gradle:8.0-jdk17 AS builder

# 작업 디렉토리 설정
WORKDIR /app

# 소스 코드 복사
COPY . .

# 빌드 시 테스트 비활성화
RUN gradle build --no-daemon -x test

# 최종 실행 이미지를 위한 JDK 설정
FROM eclipse-temurin:17-jdk

# 빌드된 JAR 파일을 실행 디렉토리로 복사
COPY --from=builder /app/build/libs/*.jar /app/app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
