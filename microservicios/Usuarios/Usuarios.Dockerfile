FROM openjdk:19

COPY target/Usuarios-1.0.0.jar /Usuarios.jar

CMD ["java", "-jar", "/Usuarios.jar"]