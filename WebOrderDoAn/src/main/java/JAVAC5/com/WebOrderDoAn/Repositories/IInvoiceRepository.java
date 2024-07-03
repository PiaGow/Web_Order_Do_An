package JAVAC5.com.WebOrderDoAn.Repositories;

import JAVAC5.com.WebOrderDoAn.Entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IInvoiceRepository extends JpaRepository<Invoice, Long> { }
