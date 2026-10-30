package pt.seixal.carlos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime creationDate = LocalDateTime.now();

    @ManyToOne(optional = false)
    private Item item;

    private int quantity;

    @ManyToOne(optional = false)
    private User user;

    private boolean isComplete = false;

    @ManyToMany
    @JoinTable(
            name = "order_stock_movements",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "stock_movement_id")
    )
    private List<StockMovement> stockMovements = new ArrayList<>();

    @Transient
    private int currentAllocatedQuantity;

    public double getCompletionPercentage() {
        if (quantity == 0) return 100.0;
        int allocated = stockMovements.stream().mapToInt(StockMovement::getQuantity).sum();
        return Math.min(100.0, (allocated * 100.0) / quantity);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public List<StockMovement> getStockMovements() {
        return stockMovements;
    }

    public void setStockMovements(List<StockMovement> stockMovements) {
        this.stockMovements = stockMovements;
    }

    public int getCurrentAllocatedQuantity() {
        return currentAllocatedQuantity;
    }

    public void setCurrentAllocatedQuantity(int currentAllocatedQuantity) {
        this.currentAllocatedQuantity = currentAllocatedQuantity;
    }
}
