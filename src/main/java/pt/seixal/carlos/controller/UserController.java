package pt.seixal.carlos.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.seixal.carlos.controller.docs.UserControllerDocs;
import pt.seixal.carlos.dto.v1.UserDTO;
import pt.seixal.carlos.model.User;
import pt.seixal.carlos.repository.UserRepository;
import pt.seixal.carlos.services.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user/v1")
@Tag(name = "User", description = "Endpoints for user")
public class UserController implements UserControllerDocs {

    @Autowired
    UserService service;

    @Override
    public UserDTO create(UserDTO user) {
        return service.create(user);
    }

    @Override
    public ResponseEntity<Page<UserDTO>> findAll(Map<String, String> allParams) {
        return ResponseEntity.ok(service.findAll(allParams));
    }

    @Override
    public UserDTO findById(Long id) {
        return service.findById(id);
    }

    @Override
    public UserDTO update(UserDTO user) {
        return service.update(user);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
