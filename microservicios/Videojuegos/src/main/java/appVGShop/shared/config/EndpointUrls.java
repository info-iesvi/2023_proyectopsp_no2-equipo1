package appVGShop.shared.config;

/**
 * Clase destinada a mantener las URLs de la aplicación para más sencillo acceso.
 */
public class EndpointUrls {

    public static final String Base = "/appVGShop"; //URL base
    public static final String VIDEOGAME_RESOURCE = "/videogame"; //Declara la segunda parte de la URL. Debería ser localhost:8080/appVGShop/videogame
    public static final String GetAll = "/getAll"; //URL de obtención de todos los usuarios en un listado
    public static final String GetVideogame = "/getVideogame/{id}"; //URL de obtención de un único videojuego por ID
    public static final String EditVideogame = "/updateVideogame/{id}"; //URL de actualización de videojuegos.
    public static final String DeleteVideogame = "/deleteVideogame/{id}"; //URL de borrado de un videojuego

}
