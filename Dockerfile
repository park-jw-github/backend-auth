# gradle:8.2.1-jdk17 이미지를 기반으로 함
FROM krmp-d2hub-idock.9rum.cc/goorm/gradle:8.2.1-jdk17 AS builder

# 작업 디렉토리 설정
WORKDIR /home/gradle/project

# 필요한 Spring 소스 코드를 이미지에 복사
COPY . .

# gradle 빌드 시 proxy 설정을 gradle.properties에 추가
RUN echo "systemProp.http.proxyHost=krmp-proxy.9rum.cc\nsystemProp.http.proxyPort=3128\nsystemProp.https.proxyHost=krmp-proxy.9rum.cc\nsystemProp.https.proxyPort=3128" > /root/.gradle/gradle.properties

# gradle 초기화
RUN gradle init

# gradle wrapper를 프로젝트에 추가
RUN gradle wrapper

# gradlew를 이용한 프로젝트 빌드
RUN chmod +x gradlew
RUN ./gradlew clean build
FROM krmp-d2hub-idock.9rum.cc/goorm/eclipse-temurin:17-jre AS final
COPY --from=builder /home/gradle/project/build/libs/backend-0.0.1.jar .

# 서버 실행 시 사용하는 포트 지정
EXPOSE 8080

# 애플리케이션 실행
CMD ["java", "-jar", "-Dhttp.proxyHost=krmp-proxy.9rum.cc", "-Dhttp.proxyPort=3128","-Dhttps.proxyHost=krmp-proxy.9rum.cc", "-Dhttps.proxyPort=3128", "-Dspring.profiles.active=prod", "backend-0.0.1.jar"]