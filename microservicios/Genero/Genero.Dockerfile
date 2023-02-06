FROM openjdk:19

COPY target/Genero-1.0.0.jar /Genero.jar

CMD ["java", "-jar", "/Genero.jar"]