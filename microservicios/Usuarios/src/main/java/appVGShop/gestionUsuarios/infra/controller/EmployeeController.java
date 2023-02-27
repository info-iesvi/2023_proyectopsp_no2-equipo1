package appVGShop.gestionUsuarios.infra.controller;

import appVGShop.gestionUsuarios.application.service.EmployeeService;
import appVGShop.gestionUsuarios.domain.dto.EmployeeDTOCreator;
import appVGShop.gestionUsuarios.domain.dto.LoginDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Calendar;

@RestController
@AllArgsConstructor
public class EmployeeController implements EmployeeAPI {

    private final EmployeeService employeeService;

    @Override
    public ResponseEntity<?> getAll() {
        return employeeService.getAll();
    }

    @Override
    public ResponseEntity<?> getUser(@PathVariable Integer id) {
        return employeeService.getUser(id);
    }

    @Override
    public ResponseEntity<?> newUser(@RequestBody EmployeeDTOCreator newUserCreator) {

        try {
            //Obtener texto de clave
            URL resourcekey = getClass().getClassLoader().getResource("clave.txt");
            if (resourcekey == null) {
                throw new IllegalArgumentException("No se encuentra el archivo.");
            } else {
                File archivoclave = new File(resourcekey.toURI());
                FileReader frclave = new FileReader(archivoclave);
                BufferedReader brclave = new BufferedReader(frclave);
                String clave = brclave.readLine();

                MessageDigest md = MessageDigest.getInstance("SHA-256");

                byte[] bytespass = newUserCreator.getPasswdEmpleado().getBytes();
                md.update(bytespass);
                byte[] resumenpass = md.digest(clave.getBytes());

                newUserCreator.setPasswdEmpleado(new String(resumenpass));

                generateAccessLog(newUserCreator.getNombreEmpleado(), "New User", "POST");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return employeeService.newUser(newUserCreator);
    }

    @Override
    public ResponseEntity<?> editUser(EmployeeDTOCreator editData, Integer id) {
        generateAccessLog(editData.getNombreEmpleado(), "Edit User", "PUT");
        return employeeService.editUser(editData, id);

    }

    @Override
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        return employeeService.deleteUser(id);

    }

    @Override
    public ResponseEntity<?> login(LoginDTO login) {
        return employeeService.login(login);
    }

    public void generateAccessLog(String user, String resource, String operation) {

        try {
            if (!new File("resources/accesos.log").exists()) {
                FileWriter file = new FileWriter("accesos.log", true);
                Calendar actualDate = Calendar.getInstance();
                file.write("" + user
                        + " - " + actualDate.get(Calendar.DAY_OF_MONTH)
                        + "/" + (actualDate.get(Calendar.MONTH) + 1)
                        + "/" + actualDate.get(Calendar.YEAR)
                        + " - " + actualDate.get(Calendar.HOUR_OF_DAY)
                        + ":" + actualDate.get(Calendar.MINUTE)
                        + ":" + actualDate.get(Calendar.SECOND)
                        + " - " + resource
                        + " - " + operation
                        + "\r\n");
                file.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
