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
    COPY target/2223_proyectopsp-equipo1-1.0.0.jar /2223_proyectopsp-equipo1.jar
    CMD ["java", "-jar", "/2223_proyectopsp-equipo1.jar"]  

![dockerfile.png](img%2Fdockerfile.png)

* Lanzamos la orden docker login y luego, creamos la imagen con la orden:

  docker build -t proyectopsp .


* Una vez que tenemos la imagen creada, generamos una variante de nuestra imagen con el nombre deseado:

  docker tag proyectopsp miguelchaves/proyectopsp:latest


* Una vez generada la variante, la subimos al repositorio:

  docker push miguelchaves/proyectopsp:latest


* Una vez creada la imagen, creamos el contenedor con la orden:

  docker run -it -d -p 8181:8080 --name proyectopsp miguelchaves/proyectopsp


* Y al final, tendremos nuestra app dockerizada y la podremos ver en nuestro repositorio de Docker Hub.

**FASE 3**

* Creamos un archivo docker-compose.yml:


    version: '3'
    services:
    main:
    image: 'miguelchaves/proyectopsp-equipo1:2223_proyectopsp-equipo1'
    container_name: proyecto-psp-app
    environment:
    spring.datasource.url: jdbc:mysql://proyecto-psp-db:3306/proyecto
    spring.datasource.username: usuario
    spring.datasource.password: usuario
    ports:
    - "8080:8080"
    depends_on:
    - db
    links:
    - db:proyecto-psp-db
    db:
    image: mysql:latest
    container_name: proyecto-psp-db
    environment:
    MYSQL_ROOT_PASSWORD: root
    MYSQL_USER: usuario
    MYSQL_PASSWORD: usuario
    MYSQL_DATABASE: proyecto
    ports:
    - "3306:3306"

![dockercompose.png](img%2Fdockercompose.png)

* Desde maven arriba a la derecha, lanzamos la orden "clean", luego "compile" y por último "install"


* Una vez instalada nos ha debido de crear dos archivos jar en el target.

![jars.png](img%2Fjars.png)
 
* Luego nos iremos al Dockerfile y ejecutaremos el
 docker-compose up, construyendolo como imagen. Lo ejecutamos y probamos el servicio.

![apagado.png](img%2Fapagado.png)

* Iniciamos el servicio

![encendido.png](img%2Fencendido.png)

* Comprobamos si hay datos

![getvacio.png](img%2Fgetvacio.png)

* Metemos datos nuevos mediante una petición POST.

![post.png](img%2Fpost.png)

* Volvemos a comprobar que los datos se han introducido correctamente.

![getlleno.png](img%2Fgetlleno.png)


