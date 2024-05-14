FROM maven:3.8.5-openjdk-18-slim as build

WORKDIR /app

# add pom.xml and source code
ADD pom.xml .
ADD src ./src

# package jar
RUN mvn clean package -Dmaven.test.skip

FROM openjdk:23-ea-18-slim-bullseye

WORKDIR /app

COPY --from=build /app/target/scg-comments-0.0.1-SNAPSHOT.jar scg-comments-0.0.1-SNAPSHOT.jar

EXPOSE 8080

CMD ["java", "-jar", "scg-comments-0.0.1-SNAPSHOT.jar"]