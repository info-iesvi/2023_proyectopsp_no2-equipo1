package appVGShop.gestionUsuarios.infra;

import appVGShop.gestionUsuarios.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interfaz de repositorio para Empleados utilizando JPA
 */
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
