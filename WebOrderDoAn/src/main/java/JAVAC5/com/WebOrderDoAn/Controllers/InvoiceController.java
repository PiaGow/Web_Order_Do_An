package JAVAC5.com.WebOrderDoAn.Controllers;

import JAVAC5.com.WebOrderDoAn.Entities.Invoice;
import JAVAC5.com.WebOrderDoAn.Services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.Optional;

import java.util.List;

@Controller

public class InvoiceController {

    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/invoices")
    public String getAllInvoices(Model model) {
        List<Invoice> invoices = invoiceService.getAllInvoices();
        model.addAttribute("invoices", invoices);

        // Tính tổng total
        double totalSum = invoices.stream().mapToDouble(Invoice::getPrice).sum();
        model.addAttribute("totalSum", totalSum);

        return "Invoice/list"; // Thymeleaf template name
    }
    @GetMapping("/invoices/{id}")
    public String getInvoiceDetails(@PathVariable Long id, Model model) {
        Optional<Invoice> optionalInvoice = invoiceService.getInvoiceById(id);
        if (optionalInvoice.isPresent()) {
            Invoice invoice = optionalInvoice.get();
            model.addAttribute("invoice", invoice);
            return "Invoice/details"; // Thymeleaf template name for invoice details
        } else {
            // Handle case when invoice is not found
            return "redirect:/invoices"; // Redirect to list page or error page
        }
    }
    @GetMapping("/invoices/by-date")
    public String getInvoicesBetweenDates(
            @RequestParam(name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            Model model) {

        if (startDate == null || endDate == null || startDate.after(endDate)) {
            model.addAttribute("error", "Invalid date range. Start Date must be before or equal to End Date.");
            return "Invoice/list"; // Return to list page with error message
        }

        List<Invoice> invoices = invoiceService.getInvoicesBetweenDates(startDate, endDate);
        model.addAttribute("invoices", invoices);

        // Tính tổng total
        double totalSum = invoices.stream().mapToDouble(Invoice::getPrice).sum();
        model.addAttribute("totalSum", totalSum);
        return "Invoice/list"; // Thymeleaf template name
    }
}
