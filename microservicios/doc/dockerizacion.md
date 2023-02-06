**FASE 1**

* Usando los pasos del hito anterior, debemos de aplicarlo para cada microservicio que creemos. Para esto, todos los servicios deben de tener su propio archivo Dockerfile. En nuestro caso quedaria de la siguiente manera:
  

    FROM openjdk:19
    COPY target/xxx-1.0.0.jar /xxx.jar
    CMD ["java", "-jar", "/xxx.jar"]


![generodockerfile.png](img%2Fgenerodockerfile.png)
![itemsdockerfile.png](img%2Fitemsdockerfile.png)
![usuariosdockerfile.png](img%2Fusuariosdockerfile.png)
![videojuegosdockerfile.png](img%2Fvideojuegosdockerfile.png)

* Una vez creados los dockerfiles, seguiremos los pasos del hito anterior, hasta llegar a los docker-compose, que en nuestro caso, solo tendremos uno que hace referencia a todos desde la carpeta raiz del proyecto. Quedando de la siguiente manera:

![dockercompose1.png](img%2Fdockercompose1.png)
![dockercompose2.png](img%2Fdockercompose2.png)
![dockercompose3.png](img%2Fdockercompose3.png)

* Y seguiremos con los pasos del hito anterior.