package JAVAC5.com.WebOrderDoAn.Repositories;

import JAVAC5.com.WebOrderDoAn.Entities.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IInvoiceItemRepository extends JpaRepository<InvoiceItem, Long> { }