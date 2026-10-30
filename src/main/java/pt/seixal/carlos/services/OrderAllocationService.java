package pt.seixal.carlos.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.seixal.carlos.model.Item;
import pt.seixal.carlos.model.Order;
import pt.seixal.carlos.model.StockMovement;
import pt.seixal.carlos.repository.OrderRepository;
import pt.seixal.carlos.repository.StockMovementRepository;

import java.util.List;

@Service
public class OrderAllocationService {

    private static final Logger log = LoggerFactory.getLogger(OrderAllocationService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Autowired
    private EmailNotificationService emailService;

    @Transactional
    public void allocateStockForItem(Long itemId) {
        // Encontra itens transacionados sob lock para evitar concorrência destrutiva
        List<Order> openOrders = orderRepository.findByItemAndIsCompleteFalseOrderByCreationDateAsc(new Item() {{ setId(itemId); }});
        List<StockMovement> availableStock = stockMovementRepository.findByItemAndRemainingQuantityGreaterThanOrderByCreationDateAsc(new Item() {{ setId(itemId); }}, 0);

        for (Order order : openOrders) {
            int currentAllocated = order.getStockMovements().stream().mapToInt(sm -> sm.getQuantity()).sum(); // Simplificado para este escopo
            // Nota: Para rastreio exato m:n parcial, precisaríamos de uma entidade de junção com peso/quantidade.
            // Para manter o modelo simples pedido, assumimos consumo sequencial direto:

            int needed = order.getQuantity() - currentAllocated;

            for (StockMovement stock : availableStock) {
                if (stock.getRemainingQuantity() <= 0) continue;

                if (needed > 0) {
                    int take = Math.min(needed, stock.getRemainingQuantity());
                    stock.setRemainingQuantity(stock.getRemainingQuantity() - take);

                    if (!order.getStockMovements().contains(stock)) {
                        order.getStockMovements().add(stock);
                    }

                    needed -= take;
                    stockMovementRepository.save(stock);
                    log.info("[STOCK MOVEMENT] Alocados {} do lote ID {} para a Ordem ID {}", take, stock.getId(), order.getId());
                }

                if (needed == 0) {
                    order.setComplete(true);
                    orderRepository.save(order);
                    log.info("[ORDER COMPLETED] Ordem ID {} totalmente cumprida.", order.getId());
                    emailService.sendCompletionEmail(order);
                    break;
                }
            }
        }
    }
}
