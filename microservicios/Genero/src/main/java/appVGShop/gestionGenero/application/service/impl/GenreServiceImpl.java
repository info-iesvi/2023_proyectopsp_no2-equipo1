package appVGShop.gestionGenero.application.service.impl;

import appVGShop.gestionGenero.application.converter.GenreDTOConverter;
import appVGShop.gestionGenero.application.service.GenreService;
import appVGShop.gestionGenero.domain.Genre;
import appVGShop.gestionGenero.domain.dto.GenreDTO;
import appVGShop.gestionGenero.domain.dto.GenreDTOCreator;
import appVGShop.gestionGenero.infra.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {

    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private GenreDTOConverter genreDTOConverter;

    @Override
    public ResponseEntity<?> getAll() {
        List<Genre> genreList = genreRepository.findAll();
        if (genreList.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            List<GenreDTO> dtoList = genreList.stream().map(genreDTOConverter::fromPropertyToDTO).collect(Collectors.toList());
            return ResponseEntity.ok(dtoList);
        }
    }

    @Override
    public ResponseEntity<?> getGenre(Integer id) {
        Genre genre = genreRepository.findById(id).orElse(null);
        if (genre == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(genre);
        }
    }

    @Override
    public ResponseEntity<?> newGenre(GenreDTOCreator newGenreCreator) {
        Genre newGenre = new Genre();
        newGenre.setNombre(newGenreCreator.getNombre());
        newGenre.setDescripcion(newGenreCreator.getDescripcion());
        return ResponseEntity.status(HttpStatus.CREATED).body(genreRepository.save(newGenre));
    }

    @Override
    public ResponseEntity<?> editGenre(GenreDTOCreator editData, Integer id) {
        return genreRepository.findById(id).map(genre -> {
            genre.setNombre(editData.getNombre());
            genre.setDescripcion(editData.getDescripcion());
            return ResponseEntity.ok(genreRepository.save(genre));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<?> deleteGenre(Integer id) {
        if (genreRepository.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            genreRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }
}
