package pt.seixal.carlos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.seixal.carlos.controller.docs.OrderControllerDocs;
import pt.seixal.carlos.dto.v1.OrderDTO;
import pt.seixal.carlos.services.OrderService;

import java.util.Map;

@RestController
@RequestMapping("/api/order/v1")
public class OrderController implements OrderControllerDocs {

    @Autowired
    private OrderService service;

    @Override
    public OrderDTO create(OrderDTO order) {
        return service.create(order);
    }

    @Override
    public ResponseEntity<Page<OrderDTO>> findAll(Map<String, String> allParams) {
        return ResponseEntity.ok(service.findAll(allParams));
    }
}
