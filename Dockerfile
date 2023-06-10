FROM openjdk:11
ADD target/comics.jar comics.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "comics.jar"]
