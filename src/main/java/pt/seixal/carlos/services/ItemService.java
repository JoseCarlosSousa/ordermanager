package pt.seixal.carlos.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pt.seixal.carlos.dto.v1.ItemDTO;
import pt.seixal.carlos.exceptions.*;
import pt.seixal.carlos.model.Item;
import pt.seixal.carlos.repository.ItemRepository;
import pt.seixal.carlos.util.PageableUtils;
import pt.seixal.carlos.util.PagedModelUtils;

import java.util.Map;

import static pt.seixal.carlos.mapper.DozertMapper.parseObject;

@Service
public class ItemService {

    private final Logger logger = LoggerFactory.getLogger(ItemService.class);

    @Autowired
    private ItemRepository repository;

    public ItemDTO create(ItemDTO dto) {
        logger.info("Creating a new item");
        if (dto == null) {
            throw new RequiredObjectIsNullException();
        }

        return save(dto, null);
    }

    public ItemDTO update(ItemDTO dto) {
        logger.info("Edit item");

        if (dto == null) {
            throw new RequiredObjectIsNullException();
        }

        Item entity = getItem(dto.getId());
        return save(dto, entity);
    }

    public ItemDTO findById(Long id) {
        logger.info("Finding one User!");
        return parseObject(getItem(id), ItemDTO.class);
    }

    public void delete(Long id) {
        logger.info("delete Person");

        Item entity = getItem(id);

        try {
            repository.delete(entity);
            repository.flush();
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new MyDataIntegrityViolationException(
                    "Cannot delete this item because it has orders or records associated with it in the system."
            );
        }
    }

    private ItemDTO save(ItemDTO dto, Item entity) {
        if (dto == null) {
            throw new RequiredObjectIsNullException();
        }

        repository.findByName(dto.getName()).ifPresent(existing -> {
            if (!existing.getId().equals(dto.getId())) {
                throw new NameAlreadyExistsException("The name '" + dto.getName() + "' already in use.");
            }
        });

        if (entity == null) {
            entity = new Item();
        }
        entity.setName(dto.getName());

        return parseObject(repository.save(entity), ItemDTO.class);
    }

    private Item getItem(Long id) {
        logger.info("Getting item with id: {}", id);
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No record found for this id"));
    }

    public Page<ItemDTO> findAll(Map<String, String> params) {
        logger.info("Finding all items!");

        Pageable pageable = PageableUtils.getPageable(params);
        return buildPage(params, repository.findAll(pageable));
    }

    private Page<ItemDTO> buildPage(Map<String, String> params, Page<Item> items) {

        return PagedModelUtils.buildPageModel(
                params,
                items,
                ItemDTO.class,
                null);
    }
}

