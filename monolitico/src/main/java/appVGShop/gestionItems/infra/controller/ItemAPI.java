package appVGShop.gestionItems.infra.controller;

import appVGShop.gestionItems.domain.dto.ItemDTOCreator;
import appVGShop.shared.config.EndpointUrls;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(EndpointUrls.Base + EndpointUrls.ITEM_RESOURCE)
public interface ItemAPI {

    @GetMapping(EndpointUrls.GetAll)
    public ResponseEntity<?> getAll();

    @GetMapping(EndpointUrls.GetItem)
    public ResponseEntity<?> getItem(@PathVariable Integer id);

    @PostMapping
    public ResponseEntity<?> newItem(@RequestBody ItemDTOCreator newItem);

    @PutMapping(EndpointUrls.EditItem)
    public ResponseEntity<?> editItem(@RequestBody ItemDTOCreator newItem, @PathVariable Integer id);

    @DeleteMapping(EndpointUrls.DeleteItem)
    public ResponseEntity<?> deleteItem(@PathVariable Integer id);
}
