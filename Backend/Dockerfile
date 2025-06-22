FROM eclipse-temurin:23-jdk AS build
WORKDIR /Backend
COPY . .
RUN chmod +x mvnw && ./mvnw clean package -Dmaven.test.skip=true

FROM eclipse-temurin:23-jre
WORKDIR /Backend
COPY --from=build /Backend/target/*.jar Backend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "Backend.jar"]