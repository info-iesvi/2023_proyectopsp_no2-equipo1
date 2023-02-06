package appVGShop.gestionItems.application.service.impl;

import appVGShop.gestionItems.application.converter.ItemDTOConverter;
import appVGShop.gestionItems.application.service.ItemService;
import appVGShop.gestionItems.domain.Item;
import appVGShop.gestionItems.domain.dto.ItemDTO;
import appVGShop.gestionItems.domain.dto.ItemDTOCreator;
import appVGShop.gestionItems.infra.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemDTOConverter itemDTOConverter;


    @Override
    public ResponseEntity<?> getAll() {
        List<Item> itemList = itemRepository.findAll();
        if (itemList.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            List<ItemDTO> dtoList = itemList.stream().map(itemDTOConverter::fromVOToDTO).collect(Collectors.toList());
            return ResponseEntity.ok(dtoList);
        }
    }

    @Override
    public ResponseEntity<?> getItem(Integer id) {
        Item item = itemRepository.findById(id).orElse(null);
        if (item == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(item);
        }
    }

    @Override
    public ResponseEntity<?> newItem(ItemDTOCreator itemCreator) {
        Item item = new Item();
        item.setNombreItem(itemCreator.getNombreItem());
        item.setDescripcionItem(itemCreator.getDescripcionItem());
        item.setEsElectronico(itemCreator.getEsElectronico());

        return ResponseEntity.status(HttpStatus.CREATED).body(itemRepository.save(item));
    }

    @Override
    public ResponseEntity<?> editItem(ItemDTOCreator editData, Integer id) {
        return itemRepository.findById(id).map(item -> {
            item.setNombreItem(editData.getNombreItem());
            item.setDescripcionItem(editData.getDescripcionItem());
            item.setEsElectronico(editData.getEsElectronico());

            return ResponseEntity.ok(itemRepository.save(item));
        }).orElseGet(() -> {
            return ResponseEntity.notFound().build();
        });
    }

    @Override
    public ResponseEntity<?> deleteItem(Integer id) {
        itemRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
