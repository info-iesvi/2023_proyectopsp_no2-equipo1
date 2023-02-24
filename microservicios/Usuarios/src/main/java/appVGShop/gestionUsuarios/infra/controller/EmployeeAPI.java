package appVGShop.gestionUsuarios.infra.controller;

import appVGShop.gestionUsuarios.domain.dto.EmployeeDTOCreator;
import appVGShop.gestionUsuarios.domain.dto.LoginDTO;
import appVGShop.shared.config.EndpointUrls;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Esta es la clase de controlador que utilizaremos para recibir las peticiones externas.
 * Tiene las funciones de un CRUD normal.
 */
@RequestMapping(EndpointUrls.Base + EndpointUrls.USER_RESOURCE)
public interface EmployeeAPI {

    /**
     * Método de listado de todos los empleados.
     *
     * Se utiliza el repositorio actual y se obtiene una lista de empleados de este.
     *
     * @return 404 si no encuentra nada, 200 y el listado si hay más de uno.
     */
    @GetMapping(EndpointUrls.GetAll)
    public ResponseEntity<?> getAll();

    /**
     * Método para mostrar un empleado según su ID.
     * <p>
     * Se toma la id desde la url, se busca con esa id en el repositorio y devuelve un Employee.
     *
     * @param id
     * @return 404 si no se encuentra nada // 200 si el empleado existe.
     */
    @GetMapping(EndpointUrls.GetUser)
    public ResponseEntity<?> getUser(@PathVariable Integer id);

    /**
     * Método de creación de empleado.
     * <p>
     * Se toman los datos del cuerpo, para crear un nuevo empleado.
     *
     * @param newUserCreator
     * @return 201 con los datos del nuevo usuario
     */
    @PostMapping
    public ResponseEntity<?> newUser(@RequestBody EmployeeDTOCreator newUserCreator);

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
    @PutMapping(EndpointUrls.EditUser)
    public ResponseEntity<?> editUser(@RequestBody EmployeeDTOCreator editData, @PathVariable Integer id);

    /**
     * Método de eliminación de empleado.
     * <p>
     * Se recibe la ID del empleado desde la url.
     * Se toma el repositorio de empleados actual, se buscara y eliminara el empleado con la id que se toma.
     *
     * @param id
     * @return 204 sin contenido.
     */
    @DeleteMapping(EndpointUrls.DeleteUser)
    public ResponseEntity<?> deleteUser(@PathVariable Integer id);

    @GetMapping(EndpointUrls.Login)
    public ResponseEntity<?> login(@RequestBody LoginDTO login);
}
