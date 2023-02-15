# Documentación del Proyecto
Equipo 1

Año: 2022-2023

Módulo: PSP

## [Codificación-3 Etapa-3] Microservicios: Uso de Clientes Estándar

### Cambios Mayores

* Actualización en las clases EmployeeService y EmployeeServiceImpl añadiendo el método notifyGmail() que tiene dos parámetros de entrada (String header y String body) que envía un correo al dar de alta, actualizar y dar de baja a un usuario.

### Explicación del Código

#### Comandos SMTP



### Pruebas de Funcionamiento

Primero probaremos la inserción de datos, para ello mediante el ARC o Postman insertaremos un nuevo empleado.

![img.png](img/insertarSMTP.png)

Una vez creado desde el ARC, vemos en el correo que ha llegado el mensaje correctamente.

![img.png](img/correoAltaSMTP.png)

Ahora probaremos que efectivamente se ha creado el empleado correctamente, desde el ARC o Postman.

![img.png](img/getSMTP.png)

Ahora que tenemos un empleado que tratar, lo actualizaremos para probar que se envía un correo al actualizar. Hacemos la actualización correspondiente desde ARC o Postman.

![img.png](img/updateSMTP.png)

Una vez hecha, volveremos al correo y veremos que efectivamente ha llegado la notificación.

![img_1.png](img/correoUpdateSMTP.png)

Probaremos que se ha hecho la actualización correctamente, desde el ARC o Postman.

![img.png](img/getSMTP2.png)

Y por último probaremos que nos manda un mensaje al eliminar. Para ello eliminaremos el empleado que hemos tratado previamente, de igual manera en el ARC o Postman.

![img.png](img/deleteSMTP.png)

Efectivamente vemos que nos ha llegado la notificación al correo.

![img_2.png](img/correoBajaSMTP.png)

Y de igual manera probaremos que se ha eliminado correctamente.

![img.png](img/getSMTP3.png)

Ahora vemos desde la bandeja de entrada todos los correos que han llegado mientras nosotros tratábamos el empleado.

![img.png](img/todosLosCorreosSMTP.png)

Después de todas las operaciones volvemos hacer el POST añadiendo el correo del profesor (jlrod2pruebas@gmail.com) y comprobamos que se ha enviado correctamente.

![img.png](img/correoJoseLuisSMTP.png)