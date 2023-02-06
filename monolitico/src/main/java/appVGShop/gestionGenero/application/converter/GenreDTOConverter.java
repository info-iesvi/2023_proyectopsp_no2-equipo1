package appVGShop.gestionGenero.application.converter;

import appVGShop.gestionGenero.domain.Genre;
import appVGShop.gestionGenero.domain.dto.GenreDTO;
import appVGShop.gestionGenero.domain.dto.GenreDTOCreator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenreDTOConverter {

    private final ModelMapper modelMapper;

    /**
     * Toma un género y lo convierte en GenreDTO
     * @param genre
     * @return
     */
    public GenreDTO fromPropertyToDTO(Genre genre) {
        return modelMapper.map(genre, GenreDTO.class);
    }

    /**
     * Convierte un GenreDTOCreator en un Genre
     * @param genreDTO
     * @return
     */
    public Genre convertToGenre(GenreDTOCreator genreDTO) {
        return modelMapper.map(genreDTO, Genre.class);
    }

}
