package appVGShop.gestionItems.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDTOCreator {
    private String nombreItem;
    private String descripcionItem;
    private Boolean esElectronico;
}
