FROM openjdk:11
ADD target/Comics-0.0.1-SNAPSHOT.jar Comics-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "Comics-0.0.1-SNAPSHOT.jar"]