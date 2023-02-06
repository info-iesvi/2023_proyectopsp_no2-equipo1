package appVGShop.gestionGenero.infra.controller;

import appVGShop.gestionGenero.application.service.GenreService;
import appVGShop.gestionGenero.domain.dto.GenreDTOCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GenreController implements GenreAPI {
    @Autowired
    private GenreService genreService;

    @Override
    public ResponseEntity<?> getAll() {
        return genreService.getAll();
    }

    @Override
    public ResponseEntity<?> getGenre(Integer id) {
        return genreService.getGenre(id);
    }

    @Override
    public ResponseEntity<?> newGenre(GenreDTOCreator genreDTOCreator) {
        return genreService.newGenre(genreDTOCreator);
    }

    @Override
    public ResponseEntity<?> editGenre(GenreDTOCreator newGenreCreator, Integer id) {
        return genreService.editGenre(newGenreCreator, id);
    }

    @Override
    public ResponseEntity<?> deleteGenre(Integer id) {
        return genreService.deleteGenre(id);
    }
}
