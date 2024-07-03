package JAVAC5.com.WebOrderDoAn.Entities;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class Cart {
    private List<CartItem> cartItems = new ArrayList<>();
    public void addItems(CartItem item) {
        boolean isExist = cartItems.stream()
                .filter(i -> Objects.equals(i.getFoodId(),
                        item.getFoodId()))
                .findFirst()
                .map(i -> {
                    i.setQuantity(i.getQuantity() +
                            item.getQuantity());
                    return true;
                })
                .orElse(false);
        if (!isExist) {
            cartItems.add(item);
        }
    }
    public void removeItems(Long foodId) {
        cartItems.removeIf(item -> Objects.equals(item.getFoodId(),
                foodId));
    }
    public void updateItems(Long foodId, int quantity) {
        cartItems.stream()
                .filter(item -> Objects.equals(item
                        .getFoodId(), foodId))
                .forEach(item -> item.setQuantity(quantity));
    }
}