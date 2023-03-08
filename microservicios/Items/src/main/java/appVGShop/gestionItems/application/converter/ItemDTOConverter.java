package appVGShop.gestionItems.application.converter;

import appVGShop.gestionItems.domain.Item;
import appVGShop.gestionItems.domain.dto.ItemDTO;
import appVGShop.gestionItems.domain.dto.ItemDTOCreator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemDTOConverter {

    private final ModelMapper modelMapper;

    /**
     * Toma un Item y lo transforma en DTO
     * @param item
     * @return
     */
    public ItemDTO fromVOToDTO(Item item) {
        return modelMapper.map(item, ItemDTO.class);
    }

    /**
     * Convierte un ItemDTOCreator en un Item
     * @param itemDTOCreator
     * @return
     */
    public Item convertToItem(ItemDTOCreator itemDTOCreator) {
        return modelMapper.map(itemDTOCreator, Item.class);
    }
}
