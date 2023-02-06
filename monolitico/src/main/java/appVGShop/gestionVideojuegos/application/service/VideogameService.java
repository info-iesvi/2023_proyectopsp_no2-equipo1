package appVGShop.gestionVideojuegos.application.service;

import appVGShop.gestionVideojuegos.domain.dto.VideogameDTOCreator;
import org.springframework.http.ResponseEntity;

public interface VideogameService {

    /**
     * Método de listado de todos los videojuegos
     * @return 404 si no encuentra nada, 200 y el listado si hay más de uno
     */
    public ResponseEntity<?> getAll();

    /**
     * Método para mostrar un videojuego según su ID
     * @param id
     * @return 404 si no se encuentra nada // 200 si el videojuego existe.
     */
    public ResponseEntity<?> getVideogame(Integer id);

    /**
     * Crea un videojuego en la base de datos.
     * @param newVideogameCreator
     * @return 201 con los datos del nuevo usuario
     */
    public ResponseEntity<?> newVideogame(VideogameDTOCreator newVideogameCreator, Integer id);

    /**
     * Edita un videojuego en la base de datos.
     * @param editData
     * @param id
     * @return 200 si se ha podido editar correctamente // 404 si no encuentra el videojuego
     */
    public ResponseEntity<?> editVideogame(VideogameDTOCreator editData, Integer id);

    /**
     * Elimina un videojuego.
     * @param id
     * @return 204 sin contenido.
     */
    public ResponseEntity<?> deleteVideogame(Integer id);
}
