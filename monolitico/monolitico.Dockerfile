FROM openjdk:19

COPY target/2223_proyectopsp-equipo1-1.0.0.jar /2223_proyectopsp-equipo1.jar

CMD ["java", "-jar", "/2223_proyectopsp-equipo1.jar"]