FROM eclipse-temurin:17-jdk-jammy
ENV TZ=Asia/Seoul

# 필요한 패키지 설치
RUN apt-get update && \
    apt-get install -y git fonts-nanum && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# 작업 디렉토리 설정
WORKDIR /app

RUN git clone -b main https://github.com/sparta-rambo/delivery_project.git /app

COPY ./application.properties ./src/main/resources/

# 소스 코드 복사
COPY . /app

# Gradle 파일에 실행 권한 부여
RUN chmod +x ./gradlew

# Gradle 빌드 (테스트 생략)
RUN ./gradlew build -x test

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "./build/libs/delivery_project-0.0.1-SNAPSHOT.jar"]
