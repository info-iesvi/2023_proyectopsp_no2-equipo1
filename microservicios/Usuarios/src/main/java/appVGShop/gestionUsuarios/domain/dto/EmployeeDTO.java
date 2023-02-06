package appVGShop.gestionUsuarios.domain.dto;

import lombok.*;

@Getter
@Setter
public class EmployeeDTO {

    private int empleadoID;
    private String nombreEmpleado;
    private String correoEmpleado;
    private String passwdEmpleado;

    private boolean esSuperior;

}
