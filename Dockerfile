FROM openjdk:11
RUN ./mvnw package java -jar target/comics.jar
ADD target/comics.jar comics.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "comics.jar"]