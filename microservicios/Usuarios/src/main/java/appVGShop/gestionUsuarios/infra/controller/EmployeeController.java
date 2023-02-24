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
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return employeeService.newUser(newUserCreator);
    }

    @Override
    public ResponseEntity<?> editUser(EmployeeDTOCreator editData, Integer id) {
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

}
