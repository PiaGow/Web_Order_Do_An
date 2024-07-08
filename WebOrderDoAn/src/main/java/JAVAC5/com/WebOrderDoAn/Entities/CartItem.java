package JAVAC5.com.WebOrderDoAn.Entities;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItem {
    private Long foodId;
    private String foodName;
    private Double price;
    private int quantity;
}
