package pt.seixal.carlos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import pt.seixal.carlos.model.Item;
import pt.seixal.carlos.model.Order;

import java.util.List;
import jakarta.persistence.LockModeType;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Order> findByItemAndIsCompleteFalseOrderByCreationDateAsc(Item item);
}
