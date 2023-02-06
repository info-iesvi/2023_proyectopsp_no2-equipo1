package appVGShop.gestionVideojuegos.application.converter;

import appVGShop.gestionVideojuegos.domain.Videogame;
import appVGShop.gestionVideojuegos.domain.dto.VideogameDTO;
import appVGShop.gestionVideojuegos.domain.dto.VideogameDTOCreator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VideogameDTOConverter {

    private final ModelMapper modelMapper;

    /**
     * Toma un Videogame  y lo convierte en VideogameDTO
     * @param videogame
     * @return
     */
    public VideogameDTO fromPropertyToDTO(Videogame videogame) {
        return modelMapper.map(videogame, VideogameDTO.class);
    }

    /**
     * Convierte un VideogameDTOCreator en un Videogame
     * @param videogameDTO
     * @return
     */
    public Videogame convertToVideogame(VideogameDTOCreator videogameDTO) {
        return modelMapper.map(videogameDTO, Videogame.class);
    }
}
