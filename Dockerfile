FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /build

COPY pom.xml ./

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

RUN ls -lh target

FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=build /build/target/google-leads-transmitter*.jar google-leads-transmitter.jar

EXPOSE 8140

CMD ["java", "-jar", "google-leads-transmitter.jar"]
