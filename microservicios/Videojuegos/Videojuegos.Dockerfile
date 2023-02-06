FROM openjdk:19

COPY target/Videojuegos-1.0.0.jar /Videojuegos.jar

CMD ["java", "-jar", "/Videojuegos.jar"]