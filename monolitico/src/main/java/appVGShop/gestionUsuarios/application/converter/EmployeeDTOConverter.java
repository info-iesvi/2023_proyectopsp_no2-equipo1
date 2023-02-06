package appVGShop.gestionUsuarios.application.converter;

import appVGShop.gestionUsuarios.domain.Employee;
import appVGShop.gestionUsuarios.domain.dto.EmployeeDTO;
import appVGShop.gestionUsuarios.domain.dto.EmployeeDTOCreator;
import lombok.*;
import org.modelmapper.*;
import org.springframework.stereotype.*;

@Component
@RequiredArgsConstructor
public class EmployeeDTOConverter {

    private final ModelMapper modelMapper;

    /**
     * Toma un Employee y lo convierte en un EmployeeDTO
     *
     * @param employee
     * @return
     */
    public EmployeeDTO fromPropertyToDTO(Employee employee) {

        return modelMapper.map(employee, EmployeeDTO.class);

    }

    /**
     * Convierte un EmployeeDTOCreator en un Employee
     *
     * @param employeeDTOCreator
     * @return
     */
    public Employee convertToEmployee(EmployeeDTOCreator employeeDTOCreator) {

        return modelMapper.map(employeeDTOCreator, Employee.class);

    }

}
