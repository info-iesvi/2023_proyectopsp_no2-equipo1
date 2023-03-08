package appVGShop.gestionVideojuegos.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VideogameDTOCreator {

    private String nombreVg;
    private String descripcionVg;
    private LocalDate fechaLanzVg;
    private Integer pegi;
    private Integer generoid;

}
