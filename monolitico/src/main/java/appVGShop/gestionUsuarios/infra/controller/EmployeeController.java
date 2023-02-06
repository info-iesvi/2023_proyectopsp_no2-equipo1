package appVGShop.gestionUsuarios.infra.controller;

import appVGShop.gestionUsuarios.application.service.EmployeeService;
import appVGShop.gestionUsuarios.domain.dto.EmployeeDTOCreator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

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

}
