package JAVAC5.com.WebOrderDoAn.Repositories;

import JAVAC5.com.WebOrderDoAn.Entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
@Repository
public interface IInvoiceRepository extends JpaRepository<Invoice, Long> {
    @Query("SELECT DATE_FORMAT(i.invoiceDate, '%Y-%m') AS month, SUM(i.price) AS totalRevenue " +
            "FROM Invoice i " +
            "GROUP BY DATE_FORMAT(i.invoiceDate, '%Y-%m')")
    List<Object[]> getMonthlyRevenue();
    List<Invoice> findByInvoiceDate(Date invoiceDate);
    List<Invoice> findByInvoiceDateBetween(Date startDate, Date endDate);

}
