package pt.seixal.carlos.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.seixal.carlos.dto.v1.StockMovementDTO;
import pt.seixal.carlos.exceptions.MyDataIntegrityViolationException;
import pt.seixal.carlos.exceptions.RequiredObjectIsNullException;
import pt.seixal.carlos.exceptions.ResourceNotFoundException;
import pt.seixal.carlos.model.StockMovement;
import pt.seixal.carlos.repository.ItemRepository;
import pt.seixal.carlos.repository.StockMovementRepository;
import pt.seixal.carlos.util.PageableUtils;
import pt.seixal.carlos.util.PagedModelUtils;

import java.util.Map;
import java.util.Set;

import static pt.seixal.carlos.mapper.DozertMapper.parseObject;

@Service
public class StockMovementService {

    private final Logger logger = LoggerFactory.getLogger(StockMovementService.class);

    private static final Set<String> ALLOWED_FIELDS = Set.of(
            "id", "creationDate", "quantity", "remaining_quantity", "item_id"
    );

    @Autowired
    private StockMovementRepository repository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderAllocationService allocationService;

    @Transactional
    public StockMovementDTO create(StockMovementDTO dto) {
        logger.info("Creating a new StockMovement");
        if (dto == null) {
            throw new RequiredObjectIsNullException();
        }
        if (dto.getItem() == null || dto.getItem().getId() == null || dto.getItem().getId() < 1) {
            throw new RequiredObjectIsNullException("The item and its ID must be provided.");
        }

        Long itemId = dto.getItem().getId();
        if (!itemRepository.existsById(itemId)) {
            throw new ResourceNotFoundException("Cannot create StockMovement. No Item found with ID: " + itemId);
        }

        StockMovement entity = parseObject(dto, StockMovement.class);

        return parseObject(createStock(entity), StockMovementDTO.class);
    }

    public StockMovementDTO findById(Long id) {
        logger.info("Finding one StockMovement!");
        return parseObject(getStockMovement(id), StockMovementDTO.class);
    }

    @Transactional(readOnly = true)
    public Page<StockMovementDTO> findAll(Map<String, String> params) {
        logger.info("Finding all StockMovement!");

        Pageable pageable = PageableUtils.getPageable(params);
        Pageable cleanPageable = PagedModelUtils.sanitizePageable(pageable, ALLOWED_FIELDS, "id");

        return buildPage(params, repository.findAll(cleanPageable));
    }

    public void delete(Long id) {
        logger.info("delete StockMovement");

        StockMovement entity = getStockMovement(id);

        try {
            repository.delete(entity);
            repository.flush();
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new MyDataIntegrityViolationException(
                    "Cannot delete this stock movement because it has orders or records associated with it in the system."
            );
        }
    }

    private StockMovement createStock(StockMovement movement) {
        movement.setRemainingQuantity(movement.getQuantity());
        StockMovement saved = repository.save(movement);
        allocationService.allocateStockForItem(saved.getItem().getId());
        return saved;
    }

    private StockMovement getStockMovement(Long id) {
        logger.info("Getting StockMovement with id: {}", id);
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No record found for this id"));
    }

    private Page<StockMovementDTO> buildPage(Map<String, String> params, Page<StockMovement> stockMovements) {

        return PagedModelUtils.buildPageModel(
                params,
                stockMovements,
                StockMovementDTO.class,
                null);
    }
}
