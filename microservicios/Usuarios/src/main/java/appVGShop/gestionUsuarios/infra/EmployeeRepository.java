package appVGShop.gestionUsuarios.infra;

import appVGShop.gestionUsuarios.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interfaz de repositorio para Empleados utilizando JPA
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
