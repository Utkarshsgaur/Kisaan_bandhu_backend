# ============================================================
# Stage 1: Build
# ============================================================
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /build

# Install Maven
RUN apk add --no-cache maven

# Copy Maven pom first (layer caching for dependencies)
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests -q

# ============================================================
# Stage 2: Run
# ============================================================
FROM eclipse-temurin:17-jre-alpine AS runner

WORKDIR /app

# Copy the built JAR
COPY --from=builder /build/target/procurement-backend-1.0.0.jar app.jar

# Expose port (Render uses PORT env var — overridden by SERVER_PORT)
EXPOSE 8080

ENTRYPOINT ["java", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
