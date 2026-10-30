package pt.seixal.carlos.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.seixal.carlos.controller.docs.ItemControllerDocs;
import pt.seixal.carlos.dto.v1.ItemDTO;
import pt.seixal.carlos.dto.v1.UserDTO;
import pt.seixal.carlos.model.Item;
import pt.seixal.carlos.repository.ItemRepository;
import pt.seixal.carlos.services.ItemService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/item/v1")
@Tag(name = "Item", description = "Endpoints for item")
public class ItemController implements ItemControllerDocs {

    @Autowired
    private ItemService service;

    @Override
    public ItemDTO create(ItemDTO item) {
        return service.create(item);
    }

    @Override
    public ResponseEntity<Page<ItemDTO>> findAll(Map<String, String> allParams) {
        return ResponseEntity.ok(service.findAll(allParams));
    }

    @Override
    public ItemDTO findById(Long id) {
        return service.findById(id);
    }

    @Override
    public ItemDTO update(ItemDTO item) {
        return service.update(item);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
