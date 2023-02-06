**FASE 1**

* Creamos un contenedor para el proyecto:

  docker run -it -d --name proyecto-mysql -p 3306:3306 -e MYSQL_DATABASE=proyecto -e MYSQL_USER=usuario -e MYSQL_PASSWORD=usuario -e MYSQL_ROOT_PASSWORD=root mysql


* Nos conectamos al contenedor:

  docker exec -it proyecto-mysql /bin/bash


* Entraremos al contenedor desde el terminal como usuario:

  mysql -u usuario -p -h 127.0.0.1

  password: root


* Usaremos proyecto:

  use proyecto


* Estará vacía la tabla, dado que no ha sido inicializada:


* Creamos el contenedor phpmyadmin y lo enlazamos con el mysql que creamos previamente:

  docker run --name phpmyadmin -d --link proyecto-mysql:db -p 8081:80 phpmyadmin/phpmyadmin


* Accedemos (Los dos contenedores deben estar inicializados) al localhost:8081 e iniciamos como Usuario:usuario / Contraseña:usuario

**FASE 2**

* Creamos el fichero Dockerfile al mismo nivel que la carpeta src:

  FROM openjdk:19

  EXPOSE 8080

  ARG JAR_FILE=target/2223_proyectopsp-equipo1-1.0-SNAPSHOT.jar

  ADD ${JAR_FILE} app.jar

  ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]


* Lanzamos la orden docker login y luego, creamos la imagen con la orden:

  docker build -t proyectopsp .


* Una vez que tenemos la imagen creada, generamos una variante de nuestra imagen con el nombre deseado:

  docker tag proyectopsp miguelchaves/proyectopsp:latest


* Una vez generada la variante, la subimos al repositorio:

  docker push miguelchaves/proyectopsp:latest


* Una vez creada la imagen, creamos el contenedor con la orden:

  docker run -it -d -p 8181:8080 --name proyectopsp miguelchaves/proyectopsp


* Y al final, tendremos nuestra app dockerizada y la podremos ver en nuestro repositorio de Docker Hub.