FROM openjdk:11
EXPOSE 9090
ADD target/bookstore-0.0.1-SNAPSHOT.jar bookstore.jar
ENTRYPOINT ["java","-jar","/bookstore.jar"]