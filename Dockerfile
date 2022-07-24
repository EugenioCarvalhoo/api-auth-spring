FROM openjdk:11
COPY target/apidata-0.0.1-SNAPSHOT.jar apidata-server-1.0.0.jar
ENTRYPOINT ["java","-jar","/apidata-server-1.0.0.jar"]