package JAVAC5.com.WebOrderDoAn.Repositories;

import JAVAC5.com.WebOrderDoAn.Entities.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IInvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {
    @Query("SELECT ii.food.name, ii.food.price, SUM(ii.quantity) as totalQuantity, ii.food.image_url " +
            "FROM InvoiceItem ii GROUP BY ii.food.id, ii.food.name, ii.food.price, ii.food.image_url")
    List<Object[]> findFoodStatistics();
}