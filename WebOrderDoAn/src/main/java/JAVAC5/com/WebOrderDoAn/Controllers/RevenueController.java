package JAVAC5.com.WebOrderDoAn.Controllers;

import JAVAC5.com.WebOrderDoAn.Entities.Invoice;
import JAVAC5.com.WebOrderDoAn.Repositories.IInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class RevenueController {

    @Autowired
    private IInvoiceRepository invoiceRepository;

    @GetMapping("/revenue/index")
    public String showRevenueChart(Model model) {
        List<Object[]> monthlyRevenue = fetchMonthlyRevenueData();

        // Convert data to chart format
        String[] months = new String[monthlyRevenue.size()];
        double[] revenues = new double[monthlyRevenue.size()];

        for (int i = 0; i < monthlyRevenue.size(); i++) {
            Object[] row = monthlyRevenue.get(i);
            months[i] = (String) row[0]; // Assuming first column is month
            revenues[i] = (double) row[1]; // Assuming second column is revenue
        }

        // Calculate total revenue
        double totalRevenue = calculateTotalRevenue();

        model.addAttribute("months", months);
        model.addAttribute("revenues", revenues);
        model.addAttribute("totalRevenue", totalRevenue);

        return "revenue/index"; // Return the view to display the chart
    }

    // Fetch monthly revenue data using Spring Data JPA
    private List<Object[]> fetchMonthlyRevenueData() {
        return invoiceRepository.getMonthlyRevenue();
    }

    // Calculate total revenue (sum of all invoice prices)
    private double calculateTotalRevenue() {
        List<Invoice> invoices = invoiceRepository.findAll();
        double totalRevenue = 0.0;
        for (Invoice invoice : invoices) {
            totalRevenue += invoice.getPrice();
        }
        return totalRevenue;
    }
}
