FROM java:8-jdk-alpine
COPY ./flink-loader.jar /usr/app/
WORKDIR /usr/app
ENTRYPOINT ["java", "-jar", "flink-loader.jar"]
