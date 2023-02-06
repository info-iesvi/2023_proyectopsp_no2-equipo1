package appVGShop.gestionVideojuegos.application.service.impl;

import appVGShop.gestionVideojuegos.application.converter.VideogameDTOConverter;
import appVGShop.gestionVideojuegos.application.service.VideogameService;
import appVGShop.gestionVideojuegos.domain.Videogame;
import appVGShop.gestionVideojuegos.domain.dto.VideogameDTO;
import appVGShop.gestionVideojuegos.domain.dto.VideogameDTOCreator;
import appVGShop.gestionVideojuegos.infra.VideogameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideogameServiceImpl implements VideogameService {

    @Autowired
    private VideogameRepository videogameRepository; //Declaración del repositorio de videojuegos

    @Autowired
    private VideogameDTOConverter videogameDTOConverter; //Declaración del convertidor

    @Override
    public ResponseEntity<?> getAll() {
        List<Videogame> videogameList = videogameRepository.findAll(); //Crea una lista de videojuegos desde la base de datos
        if (videogameList.isEmpty()) {
            return ResponseEntity.notFound().build(); //Devuelve un ResponseEntity 404 al no encontrar la lista vacía
        } else {
            List<VideogameDTO> dtoList = videogameList.stream().map(videogameDTOConverter::fromPropertyToDTO).collect(Collectors.toList()); //Convierte la lista a DTO
            return ResponseEntity.ok(dtoList); //Devuelve un ResponseEntity 200 con la lista de DTO.
        }
    }

    @Override
    public ResponseEntity<?> getVideogame(Integer id) {
        Videogame videogame = videogameRepository.findById(id).orElse(null); //Busca al videojuego en la base de datos. Devuelve null si no está
        if (videogame == null) {
            return ResponseEntity.notFound().build(); //Devuelve un ResponseEntity 404 al no encontrarlo.
        } else {
            return ResponseEntity.ok(videogame); //Devuelve un ResponseEntity 200 incluyendo el videojuego si lo encuentra
        }
    }

    @Override
    public ResponseEntity<?> newVideogame(VideogameDTOCreator newVideogameCreator, Integer id) {

            Videogame newVideogame = new Videogame(); //Declara un nuevo videojuego
            newVideogame.setNombreVg(newVideogameCreator.getNombreVg());
            newVideogame.setDescripcionVg(newVideogameCreator.getDescripcionVg());
            newVideogame.setFechaLanzVg(newVideogameCreator.getFechaLanzVg());
            newVideogame.setPegi(newVideogameCreator.getPegi());
            return ResponseEntity.status(HttpStatus.CREATED).body(videogameRepository.save(newVideogame));

         //Devuelve un ResponseEntity 201 con el videojuego creado
    }

    @Override
    public ResponseEntity<?> editVideogame(VideogameDTOCreator editData, Integer id) {
        return videogameRepository.findById(id).map(vg -> {
            vg.setNombreVg(editData.getNombreVg());
            vg.setDescripcionVg(editData.getDescripcionVg());
            vg.setFechaLanzVg(editData.getFechaLanzVg());
            vg.setPegi(editData.getPegi());

            return ResponseEntity.ok(videogameRepository.save(vg)); //Devuelve un ResponseEntity 200 con el videojuego actualizado.
        }).orElseGet(() -> {
            return ResponseEntity.notFound().build(); //Devuelve un ResponseEntity 404 si no encuentra el videojuego
        });
    }

    @Override
    public ResponseEntity<?> deleteVideogame(Integer id) {
        videogameRepository.deleteById(id); //Borra el videojuego según id
        return ResponseEntity.noContent().build(); //Devuelve un ResponseEntity 204 con la info de que no hay contenido.
    }
}
