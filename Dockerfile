# Stage 1: 애플리케이션을 빌드하는 단계
# 프로젝트에 맞는 Java 버전을 사용하세요. (예: openjdk:11, openjdk:17)
FROM openjdk:17-jdk-slim as builder

# 작업 디렉토리 설정
WORKDIR /workspace/app

# 빌드에 필요한 Gradle 관련 파일들을 먼저 복사합니다.
# 이렇게 하면 소스 코드가 변경되지 않았을 때 캐시를 활용하여 빌드 속도를 높일 수 있습니다.
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 소스 코드를 복사합니다.
COPY src src

# gradlew 스크립트에 실행 권한을 부여하고 애플리케이션을 빌드합니다.
RUN chmod +x ./gradlew && ./gradlew bootJar --no-daemon

# ---

# Stage 2: 빌드된 애플리케이션을 실행할 최종 이미지를 만드는 단계
FROM openjdk:17-jre-slim
WORKDIR /app

# builder 스테이지에서 빌드된 JAR 파일만 최종 이미지로 복사합니다.
COPY --from=builder /workspace/app/build/libs/*.jar app.jar

# 애플리케이션이 사용하는 포트를 외부에 노출합니다. (Spring Boot 기본 포트는 8080)
EXPOSE 8080

# 컨테이너가 시작될 때 애플리케이션 JAR 파일을 실행합니다.
ENTRYPOINT ["java", "-jar", "app.jar"]