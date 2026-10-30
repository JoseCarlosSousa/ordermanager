package pt.seixal.carlos.dto.v1;

import com.fasterxml.jackson.annotation.JsonFormat;
import pt.seixal.carlos.model.Item;
import pt.seixal.carlos.model.Order;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class StockMovementDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime creationDate = LocalDateTime.now();
    private ItemDTO item;
    private int quantity;
    private int remainingQuantity;
    private List<OrderDTO> orders = new ArrayList<>();

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

    public ItemDTO getItem() {
        return item;
    }

    public void setItem(ItemDTO item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getRemainingQuantity() {
        return remainingQuantity;
    }

    public void setRemainingQuantity(int remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }

    public List<OrderDTO> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDTO> orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StockMovementDTO that)) return false;
        return quantity == that.quantity && remainingQuantity == that.remainingQuantity && Objects.equals(id, that.id) && Objects.equals(creationDate, that.creationDate) && Objects.equals(item, that.item) && Objects.equals(orders, that.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, item, quantity, remainingQuantity, orders);
    }
}
