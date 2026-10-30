package pt.seixal.carlos.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.seixal.carlos.controller.docs.StockMovementControllerDocs;
import pt.seixal.carlos.dto.v1.StockMovementDTO;
import pt.seixal.carlos.dto.v1.UserDTO;
import pt.seixal.carlos.model.StockMovement;
import pt.seixal.carlos.repository.StockMovementRepository;
import pt.seixal.carlos.services.StockMovementService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stock-movements/v1")
@Tag(name = "StockMovement", description = "Endpoints for StockMovement")
public class StockMovementController implements StockMovementControllerDocs {

    @Autowired
    private StockMovementService service;

    @Override
    public StockMovementDTO create(StockMovementDTO user) {
        return service.create(user);
    }

    @Override
    public ResponseEntity<Page<StockMovementDTO>> findAll(Map<String, String> allParams) {
        return ResponseEntity.ok(service.findAll(allParams));
    }

    @Override
    public StockMovementDTO findById(Long id) {
        return service.findById(id);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
