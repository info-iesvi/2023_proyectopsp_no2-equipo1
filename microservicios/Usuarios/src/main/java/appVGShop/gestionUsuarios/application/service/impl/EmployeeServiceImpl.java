package appVGShop.gestionUsuarios.application.service.impl;

import appVGShop.gestionUsuarios.application.converter.EmployeeDTOConverter;
import appVGShop.gestionUsuarios.application.service.EmployeeService;
import appVGShop.gestionUsuarios.domain.Employee;
import appVGShop.gestionUsuarios.domain.dto.EmployeeDTO;
import appVGShop.gestionUsuarios.domain.dto.EmployeeDTOCreator;
import appVGShop.gestionUsuarios.infra.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.net.smtp.AuthenticatingSMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.apache.commons.net.smtp.SimpleSMTPHeader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.*;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import java.io.IOException;
import java.io.Writer;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private final EmployeeRepository employeeRepository; //Declaración del repositorio de empleados

    @Autowired
    private EmployeeDTOConverter userDTOConverter; //Declaración del convertidor

    @Override
    public ResponseEntity<?> getAll() {

        List<Employee> employeeList = employeeRepository.findAll(); //Crea una lista de empleados desde la base de datos

        if (employeeList.isEmpty()) {

            return ResponseEntity.notFound().build(); //Devuelve un ResponseEntity 404 al no encontrar la lista vacía

        } else {

            List<EmployeeDTO> dtoList = employeeList.stream().map(userDTOConverter::fromPropertyToDTO).collect(Collectors.toList()); //Convierte la lista de empleados a una de DTO.
            //Esta parte utiliza la API stream, que nos permite mapear la lista previa a una de DTO utilizando el convertidor en una sola línea.

            return ResponseEntity.ok(dtoList); //Devuelve un ResponseEntity 200 con la lista de DTO.

        }
    }

    @Override
    public ResponseEntity<?> getUser(Integer id) {

        Employee employee = employeeRepository.findById(id).orElse(null); //Busca al empleado del id indicado en PathVariable. De no encontrarlo devuelve null.

        if (employee == null) {

            return ResponseEntity.notFound().build(); //Devuelve un ResponseEntity 404 al no encontrarlo.

        } else {

            return ResponseEntity.ok(employee); //Devuelve un ResponseEntity 200 incluyendo el empleado encontrado.

        }

    }

    @Override
    public ResponseEntity<?> newUser(EmployeeDTOCreator newUserCreator) {

        Employee newEmployee = new Employee(); //Declara un nuevo empleado

        newEmployee.setNombreEmpleado(newUserCreator.getNombreEmpleado()); //Establece el nombre del empleado según el cuerpo

        newEmployee.setCorreoEmpleado(newUserCreator.getCorreoEmpleado()); //Establece el correo

        newEmployee.setPasswdEmpleado(newUserCreator.getPasswdEmpleado()); //Establece la contraseña

        newEmployee.setEsSuperior(newUserCreator.getEsSuperior()); //Establece si es gerente o no

        notifyGmail("NUEVA CREACIÓN DE USUARIO: " +newEmployee.getNombreEmpleado(),
                "ALERTA DE CREACIÓN DE USUARIO " +
                        "\nNombre: " +newEmployee.getNombreEmpleado() +
                        "\nCorreo: " +newEmployee.getCorreoEmpleado());

        return ResponseEntity.status(HttpStatus.CREATED).body(employeeRepository.save(newEmployee)); //Devuelve un ResponseEntity 201 con el empleado creado.

    }

    @Override
    public ResponseEntity<?> editUser(EmployeeDTOCreator editData, Integer id) {

        Employee prevEmployee = employeeRepository.getById(id);
        String nombre = prevEmployee.getNombreEmpleado();
        String correo = prevEmployee.getCorreoEmpleado();

        return employeeRepository.findById(id).map(p -> {

            p.setNombreEmpleado(editData.getNombreEmpleado()); //Establece el nombre

            p.setCorreoEmpleado(editData.getCorreoEmpleado()); //Establece el correo

            p.setPasswdEmpleado(editData.getPasswdEmpleado()); //Establece la contraseña

            p.setEsSuperior(editData.getEsSuperior()); //Establece si es gerente

            notifyGmail("NUEVA EDICIÓN DE USUARIO: " +p.getNombreEmpleado(),
                    "ALERTA DE EDICIÓN DE USUARIO " +
                            "\nPrevio nombre: " +nombre +
                            "\nPrevio correo: " +correo +
                            "\nActual nombre: " +p.getNombreEmpleado() +
                            "\nActual correo: " +p.getCorreoEmpleado());

            return ResponseEntity.ok(employeeRepository.save(p)); // Devuelve un ResponseEntity 200 con el empleado actualizado

        }).orElseGet(() -> {

            return ResponseEntity.notFound().build(); //Devuelve un ResponseEntity 404 si no encuentra un empleado con tal ID

        });
    }

    @Override
    public ResponseEntity<?> deleteUser(Integer id) {

        notifyGmail("NUEVO BORRADO DE USUARIO: " +employeeRepository.getById(id).getNombreEmpleado(),
                "ALERTA DE BORRADO DE USUARIO " +
                        "\nNombre: " +employeeRepository.getById(id).getNombreEmpleado() +
                        "\nCorreo: " +employeeRepository.getById(id).getCorreoEmpleado());

        employeeRepository.deleteById(id); //Borra el empleado según su ID

        return ResponseEntity.noContent().build(); //Devuelve un ResponseEntity 204 con que no hay contenido.

    }

    @Override
    public void notifyGmail(String header, String body) {

        //se crea el cliente SMTP seguro
        AuthenticatingSMTPClient client = new AuthenticatingSMTPClient();

        //datos del usuario y del servidor
        String server = "smtp.gmail.com";
        String username = "psp2223equipo1@gmail.com";
        String password = "uctiktqsohvagdbi";
        int puerto = 587;
        String remitente = "psp2223equipo1@gmail.com";

        try {

            int respuesta;

            //Creación de la clave para establecer un canal seguro
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(null, null);
            KeyManager km = kmf.getKeyManagers() [0];

            //Nos conectamos al servidor SMTP
            client.connect(server, puerto);
            System.out.println("SMTP - 1 - " +client.getReplyString());

            //se establece la clave para la comunicación segura
            client.setKeyManager(km);

            respuesta = client.getReplyCode();

            if (!SMTPReply.isPositiveCompletion(respuesta)) {
                client.disconnect();
                System.err.println("SMTP - CONEXIÓN RECHAZADA.");
            }

            //Se envía el comando EHLO
            client.ehlo(server); //necesario
            System.out.println("SMTP - 2 - " +client.getReplyString());

            //NECESITA NEGOCIACIÓN TLS - MODO NO IMPLÍCITO
            //Se ejecuta el comando STARTTLS y se comprueba si es true
            if (client.execTLS()) {
                System.out.println("SMTP - 3 - " + client.getReplyString());

                //se realiza la autenticación con el servidor
                if (client.auth(AuthenticatingSMTPClient.AUTH_METHOD.PLAIN, username, password)) {
                    System.out.println("SMTP - 4 - " +client.getReplyString());
                    String destino1 = "psp2223equipo1@gmail.com";
                    String asunto = header;
                    String mensaje = body;

                    //se crea la cabecera
                    SimpleSMTPHeader cabecera = new SimpleSMTPHeader(remitente, destino1, asunto);

                    //el nombre de usuario y el email de origen coinciden
                    client.setSender(remitente);
                    client.addRecipient(destino1);

                    //TODO ACTIVAR PARA EL CORREO DE JOSE LUIS
                    String destino2 = "jlrod2pruebas@gmail.com";
                    client.addRecipient(destino2);

                    System.out.println("SMTP - 5 - " +client.getReplyString());

                    //se envia DATA
                    Writer writer = client.sendMessageData();
                    if (writer == null) {
                        System.out.println("SMTP - FALLO AL ENVIAR DATA.");
                    } else {
                        writer.write(cabecera.toString()); //cabecera
                        writer.write(mensaje); //luego mensaje
                        writer.close();
                        System.out.println("SMTP - 6 - " +client.getReplyString());

                        boolean exito = client.completePendingCommand();
                        System.out.println("SMTP - 7 - " + client.getReplyString());

                        if (!exito) { //fallo
                            System.out.println("SMTP - FALLO AL FINALIZAR TRANSACCIÓN.");
                            System.exit(1);
                        } else {
                            System.out.println("SMTP - MENSAJE ENVIADO CON ÉXITO ...");
                        }
                    }

                } else {
                    System.out.println("SMTP - USUARIO NO AUTENTICADO.");
                }

            } else {
                System.out.println("SMTP - FALLO AL EJECUTAR STARTTLS.");
            }

        } catch (IOException | NoSuchAlgorithmException | UnrecoverableKeyException | KeyStoreException |
                 InvalidKeySpecException | InvalidKeyException e) {
            System.err.println("SMTP - COULD NOT CONNECT TO SERVER.");
            e.printStackTrace();
        }

        try {
            client.disconnect();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        System.out.println("SMTP - FIN DE ENVÍO");

    }
}
