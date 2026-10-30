package pt.seixal.carlos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import pt.seixal.carlos.model.Item;
import pt.seixal.carlos.model.StockMovement;

import java.util.List;
import jakarta.persistence.LockModeType;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<StockMovement> findByItemAndRemainingQuantityGreaterThanOrderByCreationDateAsc(Item item, int quantity);
}
