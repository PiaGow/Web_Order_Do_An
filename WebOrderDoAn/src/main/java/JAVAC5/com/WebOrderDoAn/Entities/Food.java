package JAVAC5.com.WebOrderDoAn.Entities;

import JAVAC5.com.WebOrderDoAn.validators.annotation.ValidCategoryId;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "food")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", length = 50, nullable = false)
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    @NotBlank(message = "Name must not be blank")
    private String name;
    @Column(name = "price")
    @Positive(message = "Price must be greater than 0")
    private Double price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @ValidCategoryId
    @ToString.Exclude
    private Category category;
    @Column(name = "image_url")
    private String image_url;
    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<InvoiceItem> itemInvoices = new ArrayList<>();
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) !=
                Hibernate.getClass(o)) return false;
        Food food = (Food) o;
        return getId() != null && Objects.equals(getId(),
                food.getId());
    }
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
