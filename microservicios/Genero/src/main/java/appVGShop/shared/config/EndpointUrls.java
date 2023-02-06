package appVGShop.shared.config;

/**
 * Clase destinada a mantener las URLs de la aplicación para más sencillo acceso.
 */
public class EndpointUrls {

    public static final String Base = "/appVGShop"; //URL base
    public static final String GetAll = "/getAll"; //URL de obtención de todos los usuarios en un listado
    public static final String GENRE_RESOURCE = "/genre";
    public static final String GetGenre = "/getGenre/{id}";
    public static final String EditGenre = "/updateGenre/{id}";
    public static final String DeleteGenre = "/deleteGenre/{id}";
}
