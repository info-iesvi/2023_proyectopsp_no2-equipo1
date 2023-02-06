package appVGShop.gestionVideojuegos.infra.controller;

import appVGShop.gestionVideojuegos.application.service.VideogameService;
import appVGShop.gestionVideojuegos.domain.dto.VideogameDTOCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VideogameController implements VideogameAPI {
    @Autowired
    private VideogameService videogameService;

    @Override
    public ResponseEntity<?> getAll() {
        return videogameService.getAll();
    }

    @Override
    public ResponseEntity<?> getVideogame(Integer id) {
        return videogameService.getVideogame(id);
    }

    @Override
    public ResponseEntity<?> newVideogame(VideogameDTOCreator newVideogameCreator, Integer id) {
        return videogameService.newVideogame(newVideogameCreator, id);
    }

    @Override
    public ResponseEntity<?> editVideogame(VideogameDTOCreator newVideogameCreator, Integer id) {
        return videogameService.editVideogame(newVideogameCreator, id);
    }

    @Override
    public ResponseEntity<?> deleteVideogame(Integer id) {
        return videogameService.deleteVideogame(id);
    }
}
