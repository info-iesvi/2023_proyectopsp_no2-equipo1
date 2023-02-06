FROM openjdk:19

COPY target/Items-1.0.0.jar /Items.jar

CMD ["java", "-jar", "/Items.jar"]