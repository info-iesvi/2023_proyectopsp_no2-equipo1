package appVGShop.shared.config;

/**
 * Clase destinada a mantener las URLs de la aplicación para más sencillo acceso.
 */
public class EndpointUrls {

    public static final String Base = "/appVGShop"; //URL base
    public static final String USER_RESOURCE = "/employee"; //Declara la segunda parte de la URL. Debería ser localhost:8080/appVGShop/employee
    public static final String GetAll = "/getAll"; //URL de obtención de todos los usuarios en un listado
    public static final String GetUser = "/getUser/{id}"; //URL de obtención de un único usuario por ID
    public static final String EditUser = "/updateUser/{id}"; //URL de actualización de un usuario por ID
    public static final String DeleteUser = "/deleteUser/{id}"; //URL de borrado de un usuario por ID
    public static final String Login = "/login"; //URL para comprobar si un usuario existe en la base de datos

}
