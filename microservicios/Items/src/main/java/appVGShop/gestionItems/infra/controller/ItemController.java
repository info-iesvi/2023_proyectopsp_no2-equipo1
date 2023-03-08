package appVGShop.gestionItems.infra.controller;

import appVGShop.gestionItems.application.service.ItemService;
import appVGShop.gestionItems.domain.dto.ItemDTOCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController implements ItemAPI {

    @Autowired
    private ItemService itemService;

    @Override
    public ResponseEntity<?> getAll() {
        return itemService.getAll();
    }

    @Override
    public ResponseEntity<?> getItem(Integer id) {
        return itemService.getItem(id);
    }

    @Override
    public ResponseEntity<?> newItem(ItemDTOCreator newItem) {
        return itemService.newItem(newItem);
    }

    @Override
    public ResponseEntity<?> editItem(ItemDTOCreator newItem, Integer id) {
        return itemService.editItem(newItem, id);
    }

    @Override
    public ResponseEntity<?> deleteItem(Integer id) {
        return itemService.deleteItem(id);
    }
}
