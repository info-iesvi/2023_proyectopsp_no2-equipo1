package appVGShop.gestionGenero.application.service;

import appVGShop.gestionGenero.domain.dto.GenreDTOCreator;
import org.springframework.http.ResponseEntity;

public interface GenreService {

    /**
     * Método de listado de todos los géneros
     * @return 404 si no encuentra nada, 200 y el listado si hay más de uno
     */
    public ResponseEntity<?> getAll();

    /**
     * Método para mostrar un género según su ID
     * @param id
     * @return 404 si no se encuentra nada // 200 si el género existe
     */
    public ResponseEntity<?> getGenre(Integer id);

    /**
     * Crea un género en la base de datos
     * @param newGenreCreator
     * @return 201 con los datos del nuevo género
     */
    public ResponseEntity<?> newGenre(GenreDTOCreator newGenreCreator);

    /**
     * Edita un género en la base de datos
     * @param editData
     * @param id
     * @return 200 si se ha podido editar correctamente // 404 si no se encuentra el género
     */
    public ResponseEntity<?> editGenre(GenreDTOCreator editData, Integer id);

    /**
     * Elimina un género
     * @param id
     * @return 204 sin contenido
     */
    public ResponseEntity<?> deleteGenre(Integer id);

}
