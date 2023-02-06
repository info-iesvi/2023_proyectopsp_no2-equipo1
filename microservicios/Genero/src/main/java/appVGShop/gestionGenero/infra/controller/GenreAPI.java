package appVGShop.gestionGenero.infra.controller;

import appVGShop.gestionGenero.domain.dto.GenreDTOCreator;
import appVGShop.shared.config.EndpointUrls;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(EndpointUrls.Base + EndpointUrls.GENRE_RESOURCE)
public interface GenreAPI {

    /**
     * Lista todos los géneros
     * @return 404 si no encuentra nada, 200 y el listado si hay más de uno
     */
    @GetMapping(EndpointUrls.GetAll)
    public ResponseEntity<?> getAll();

    /**
     * Muestra un género según su ID
     * @param id
     * @return 404 si no encuentra nada // 200 si el género existe
     */
    @GetMapping(EndpointUrls.GetGenre)
    public ResponseEntity<?> getGenre(@PathVariable Integer id);

    /**
     * Crea un género
     * @param genreDTOCreator
     * @return 201 con los datos del nuevo género
     */
    @PostMapping
    public ResponseEntity<?> newGenre(@RequestBody GenreDTOCreator genreDTOCreator);

    /**
     * Edita un género
     * @param newGenreCreator
     * @param id
     * @return 200 si se ha podido editar // 404 si no encuentra el género
     */
    @PutMapping(EndpointUrls.EditGenre)
    public ResponseEntity<?> editGenre(@RequestBody GenreDTOCreator newGenreCreator, @PathVariable Integer id);

    /**
     * Elimina un género
     * @param id
     * @return 204 sin contenido
     */
    @DeleteMapping(EndpointUrls.DeleteGenre)
    public ResponseEntity<?> deleteGenre(@PathVariable Integer id);
}
