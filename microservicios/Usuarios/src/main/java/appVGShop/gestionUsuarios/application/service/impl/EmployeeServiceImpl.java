package appVGShop.gestionUsuarios.application.service.impl;

import appVGShop.gestionUsuarios.application.converter.EmployeeDTOConverter;
import appVGShop.gestionUsuarios.application.service.EmployeeService;
import appVGShop.gestionUsuarios.domain.Employee;
import appVGShop.gestionUsuarios.domain.dto.EmployeeDTO;
import appVGShop.gestionUsuarios.domain.dto.EmployeeDTOCreator;
import appVGShop.gestionUsuarios.infra.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.*;

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

        return ResponseEntity.status(HttpStatus.CREATED).body(employeeRepository.save(newEmployee)); //Devuelve un ResponseEntity 201 con el empleado creado.

    }

    @Override
    public ResponseEntity<?> editUser(EmployeeDTOCreator editData, Integer id) {

        return employeeRepository.findById(id).map(p -> {

            p.setNombreEmpleado(editData.getNombreEmpleado()); //Establece el nombre

            p.setCorreoEmpleado(editData.getCorreoEmpleado()); //Establece el correo

            p.setPasswdEmpleado(editData.getPasswdEmpleado()); //Establece la contraseña

            p.setEsSuperior(editData.getEsSuperior()); //Establece si es gerente

            return ResponseEntity.ok(employeeRepository.save(p)); // Devuelve un ResponseEntity 200 con el empleado actualizado

        }).orElseGet(() -> {

            return ResponseEntity.notFound().build(); //Devuelve un ResponseEntity 404 si no encuentra un empleado con tal ID

        });
    }

    @Override
    public ResponseEntity<?> deleteUser(Integer id) {

        employeeRepository.deleteById(id); //Borra el empleado según su ID

        return ResponseEntity.noContent().build(); //Devuelve un ResponseEntity 204 con que no hay contenido.

    }
}
