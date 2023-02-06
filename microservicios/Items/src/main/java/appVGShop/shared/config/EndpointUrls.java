package appVGShop.shared.config;

/**
 * Clase destinada a mantener las URLs de la aplicación para más sencillo acceso.
 */
public class EndpointUrls {

    public static final String Base = "/appVGShop"; //URL base
    public static final String GetAll = "/getAll"; //URL de obtención de todos los usuarios en un listado
    public static final String ITEM_RESOURCE = "/item";
    public static final String GetItem = "/getItem/{id}";
    public static final String EditItem = "/updateItem/{id}";
    public static final String DeleteItem = "/deleteItem/{id}";
}
