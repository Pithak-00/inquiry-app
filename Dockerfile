# ── ステージ1: Mavenでビルド ──────────────────────────
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# 依存関係を先にキャッシュ（ソース変更時の再ビルドを高速化）
COPY pom.xml .
RUN mvn dependency:go-offline -B

# ソースをコピーしてビルド
COPY src ./src
RUN mvn clean package -DskipTests -B

# ── ステージ2: 実行イメージ（軽量） ──────────────────────
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=build /app/target/inquiry-app-1.0.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
