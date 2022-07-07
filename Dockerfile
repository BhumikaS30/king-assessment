FROM openjdk:11
COPY ./target/king-assessment-0.0.1-SNAPSHOT.jar king-assessment-0.0.1-SNAPSHOT.jar
CMD ["java","-jar","king-assessment-0.0.1-SNAPSHOT.jar"]