package pt.seixal.carlos.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.seixal.carlos.dto.v1.OrderDTO;
import pt.seixal.carlos.dto.v1.UserDTO;
import pt.seixal.carlos.exceptions.RequiredObjectIsNullException;
import pt.seixal.carlos.model.Order;
import pt.seixal.carlos.model.User;
import pt.seixal.carlos.repository.OrderRepository;
import pt.seixal.carlos.util.PageableUtils;
import pt.seixal.carlos.util.PagedModelUtils;

import java.util.*;

import static pt.seixal.carlos.mapper.DozertMapper.parseObject;

@Service
public class OrderService {

    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private static final Set<String> ALLOWED_FIELDS = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(
                    "id", "creationDate", "quantity", "isComplete", "currentAllocatedQuantity"
            ))
    );

    @Autowired
    private OrderRepository repository;
    @Autowired
    private OrderAllocationService allocationService;

    @Transactional
    public OrderDTO create(OrderDTO dto) {
        logger.info("Creating a new Order");
        if (dto == null) {
            throw new RequiredObjectIsNullException();
        }
        Order entity = parseObject(dto, Order.class);

        return parseObject(createOrder(entity), OrderDTO.class);
    }

    @Transactional(readOnly = true)
    public Page<OrderDTO> findAll(Map<String, String> params) {
        logger.info("Finding all orders!");

        Pageable pageable = PageableUtils.getPageable(params);
        Pageable cleanPageable = PagedModelUtils.sanitizePageable(pageable, ALLOWED_FIELDS, "id");

        return buildPage(params, repository.findAll(cleanPageable));
    }

    private Order createOrder(Order order) {
        Order savedOrder = repository.save(order);
        allocationService.allocateStockForItem(savedOrder.getItem().getId());
        return repository.findById(savedOrder.getId()).orElse(savedOrder);
    }

    private Page<OrderDTO> buildPage(Map<String, String> params, Page<Order> orders) {

        return PagedModelUtils.buildPageModel(
                params,
                orders,
                OrderDTO.class,
                null);
    }

}
