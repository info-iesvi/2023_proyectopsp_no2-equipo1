package appVGShop.gestionItems.application.service;

import appVGShop.gestionItems.domain.dto.ItemDTOCreator;
import org.springframework.http.ResponseEntity;

public interface ItemService {

    /**
     * Método para obtener todos los items
     * @return
     */
    public ResponseEntity<?> getAll();

    /**
     * Método para mostrar un item según su id
     * @param id
     * @return
     */
    public ResponseEntity<?> getItem(Integer id);

    /**
     * Método para crear un item
     * @param itemCreator
     * @return
     */
    public ResponseEntity<?> newItem(ItemDTOCreator itemCreator);

    /**
     * Método para editar un item
     * @param editData
     * @param id
     * @return
     */
    public ResponseEntity<?> editItem(ItemDTOCreator editData, Integer id);

    /**
     * Método de borrado de item
     * @param id
     * @return
     */
    public ResponseEntity<?> deleteItem(Integer id);
}
