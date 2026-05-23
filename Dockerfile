FROM amazoncorretto:17
ADD target/*.jar Comics.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "Comics.jar"]
