package pt.seixal.carlos.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import pt.seixal.carlos.dto.v1.UserDTO;
import pt.seixal.carlos.exceptions.EmailAlreadyExistsException;
import pt.seixal.carlos.exceptions.MyDataIntegrityViolationException;
import pt.seixal.carlos.exceptions.RequiredObjectIsNullException;
import pt.seixal.carlos.exceptions.ResourceNotFoundException;
import pt.seixal.carlos.model.User;
import pt.seixal.carlos.repository.UserRepository;
import pt.seixal.carlos.util.PageableUtils;
import pt.seixal.carlos.util.PagedModelUtils;

import java.util.Map;

import static pt.seixal.carlos.mapper.DozertMapper.parseObject;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository repository;

    public UserDTO create(UserDTO dto) {
        logger.info("Creating a new user");
        if (dto == null) {
            throw new RequiredObjectIsNullException();
        }
        return save(dto, null);
    }

    public UserDTO update(UserDTO dto) {
        logger.info("Edit User");

        if (dto == null) {
            throw new RequiredObjectIsNullException();
        }

        User entity = getUser(dto.getId());



        return save(dto, entity);
    }

    public UserDTO findById(Long id) {
        logger.info("Finding one User!");
        return parseObject(getUser(id), UserDTO.class);
    }

    public void delete(Long id) {
        logger.info("delete user");

        User entity = getUser(id);

        try {
            repository.delete(entity);
            repository.flush();
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new MyDataIntegrityViolationException(
                    "Cannot delete this user because they have orders or records associated with them in the system."
            );
        }
    }

    private UserDTO save(UserDTO dto, User entity) {
        if (dto == null) {
            throw new RequiredObjectIsNullException();
        }

        repository.findByEmail(dto.getEmail()).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(dto.getId())) {
                throw new EmailAlreadyExistsException("the email '" + dto.getEmail() + "' already in use.");
            }
        });

        if (entity == null) {
            entity = new User();
        }

        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());

        return parseObject(repository.save(entity), UserDTO.class);
    }

    private User getUser(Long id) {
        logger.info("Getting User with id: {}", id);
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No record found for this id"));
    }

    public Page<UserDTO> findAll(Map<String, String> params) {
        logger.info("Finding all users!");

        Pageable pageable = PageableUtils.getPageable(params);
        return buildPage(params, repository.findAll(pageable));
    }

    private Page<UserDTO> buildPage(Map<String, String> params, Page<User> users) {

        return PagedModelUtils.buildPageModel(
                params,
                users,
                UserDTO.class,
                null);
    }
}

