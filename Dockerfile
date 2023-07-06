FROM openjdk:11
ADD target/*.jar Comics.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "Comics.jar"]
