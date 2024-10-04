# Step 1: 베이스 이미지 선택 (JDK 17 사용)
FROM openjdk:17-jdk-alpine

# Step 2: 애플리케이션 JAR 파일을 컨테이너로 복사
COPY build/libs/safeRealtor-0.0.1-SNAPSHOT.jar app.jar

# Step 3: 컨테이너에서 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]

