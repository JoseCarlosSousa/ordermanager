package pt.seixal.carlos.dto.v1;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime creationDate = LocalDateTime.now();


    private ItemDTO item;
    private int quantity;
    private UserDTO user;

    private boolean isComplete = false;
    private List<StockMovementDTO> stockMovements = new ArrayList<>();

    private int currentAllocatedQuantity;

    public double getCompletionPercentage() {
        if (quantity == 0) return 100.0;
        int allocated = stockMovements.stream().mapToInt(StockMovementDTO::getQuantity).sum();
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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public List<StockMovementDTO> getStockMovements() {
        return stockMovements;
    }

    public void setStockMovements(List<StockMovementDTO> stockMovements) {
        this.stockMovements = stockMovements;
    }

    public int getCurrentAllocatedQuantity() {
        return currentAllocatedQuantity;
    }

    public void setCurrentAllocatedQuantity(int currentAllocatedQuantity) {
        this.currentAllocatedQuantity = currentAllocatedQuantity;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OrderDTO)) return false;
        OrderDTO orderDTO = (OrderDTO) o;
        return quantity == orderDTO.quantity && isComplete == orderDTO.isComplete && currentAllocatedQuantity == orderDTO.currentAllocatedQuantity && Objects.equals(id, orderDTO.id) && Objects.equals(creationDate, orderDTO.creationDate) && Objects.equals(item, orderDTO.item) && Objects.equals(user, orderDTO.user) && Objects.equals(stockMovements, orderDTO.stockMovements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationDate, item, quantity, user, isComplete, stockMovements, currentAllocatedQuantity);
    }
}
