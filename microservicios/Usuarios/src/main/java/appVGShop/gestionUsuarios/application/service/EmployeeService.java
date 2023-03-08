package appVGShop.gestionUsuarios.application.service;

import appVGShop.gestionUsuarios.domain.dto.EmployeeDTOCreator;
import appVGShop.gestionUsuarios.domain.dto.LoginDTO;
import org.springframework.http.ResponseEntity;

public interface EmployeeService {

    /**
     * Método de listado de todos los empleados.
     * Se utiliza el repositorio actual y se obtiene una lista de empleados de este.
     *
     * @return 404 si no encuentra nada, 200 y el listado si hay más de uno.
     */
    public ResponseEntity<?> getAll();

    /**
     * Método para mostrar un empleado según su ID.
     *
     * Se toma la id desde la url, se busca con esa id en el repositorio y devuelve un Employee.
     *
     * @param id
     * @return 404 si no se encuentra nada // 200 si el empleado existe.
     */
    public ResponseEntity<?> getUser(Integer id);

    /**
     * Método de creación de empleado.
     * <p>
     * Se toman los datos del cuerpo, para crear un nuevo empleado.
     *
     * @param newUserCreator
     * @return 201 con los datos del nuevo usuario
     */
    public ResponseEntity<?> newUser(EmployeeDTOCreator newUserCreator);

    /**
     * Método de edición de empleado.
     * <p>
     * Se introducen los datos que se van a editar en el cuerpo y se toma la ID desde la url.
     * Se realiza un mapeado de los empleados que hay en el repositorio, cuando lo encuentra toma el empleado y
     * actualiza sus datos.
     *
     * @param editData
     * @param id
     * @return 200 si se ha podido editar correctamente // 404 si no encuentra el empleado.
     */
    public ResponseEntity<?> editUser(EmployeeDTOCreator editData, Integer id);

    /**
     * Método de eliminación de empleado.
     * <p>
     * Se recibe la ID del empleado desde la url.
     * Se toma el repositorio de empleados actual, se buscara y eliminara el empleado con la id que se toma.
     *
     * @param id
     * @return 204 sin contenido.
     */
    public ResponseEntity<?> deleteUser(Integer id);

    public ResponseEntity<?> login(LoginDTO login);

    /**
     * Método de enviado de notificaciones al gmail del servidor
     * Se indica la cabecera y el cuerpo para enviar un mensaje personalizado
     * @param header
     * @param body
     */
    public void notifyGmail(String header, String body);

}
