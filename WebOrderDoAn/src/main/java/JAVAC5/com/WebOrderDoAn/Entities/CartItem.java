package JAVAC5.com.WebOrderDoAn.Entities;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItem {
    private Long foodId;
    private String bookName;
    private Double price;
    private int quantity;
}
