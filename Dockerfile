FROM maven:3.8.5-openjdk-17

WORKDIR /booking-service
COPY . .
RUN mvn clean install

CMD ["java", "-jar", "app/target/app-1.0-SNAPSHOT.jar"]