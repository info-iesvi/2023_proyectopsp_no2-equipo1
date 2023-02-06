package appVGShop.gestionVideojuegos.infra.controller;

import appVGShop.gestionVideojuegos.domain.dto.VideogameDTOCreator;
import appVGShop.shared.config.EndpointUrls;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(EndpointUrls.Base + EndpointUrls.VIDEOGAME_RESOURCE)
public interface VideogameAPI {

    /**
     * Lista todos los videojuegos
     * @return 404 si no encuentra nada, 200 y el listado si hay más de uno
     */
    @GetMapping(EndpointUrls.GetAll)
    public ResponseEntity<?> getAll();

    /**
     * Muestra un juego según su ID
     * @param id
     * @return 404 si no encuentra nada // 200 si el videojuego existe
     */
    @GetMapping(EndpointUrls.GetVideogame)
    public ResponseEntity<?> getVideogame(@PathVariable Integer id);

    /**
     * Crea un videojuego
     * @param newVideogameCreator
     * @return 201 con los datos del nuevo videojuego
     */
    @PostMapping("/{id}")
    public ResponseEntity<?> newVideogame(@RequestBody VideogameDTOCreator newVideogameCreator, @PathVariable Integer id);

    /**
     * Edita un videojuego
     * @param newVideogameCreator
     * @return 200 si se ha podido editar // 404 si no encuentra el videojuego
     */
    @PutMapping(EndpointUrls.EditVideogame)
    public ResponseEntity<?> editVideogame(@RequestBody VideogameDTOCreator newVideogameCreator, @PathVariable Integer id);

    /**
     * Elimina un videojuego
     * @param id
     * @return 204 sin contenido
     */
    @DeleteMapping(EndpointUrls.DeleteVideogame)
    public ResponseEntity<?> deleteVideogame(@PathVariable Integer id);
}