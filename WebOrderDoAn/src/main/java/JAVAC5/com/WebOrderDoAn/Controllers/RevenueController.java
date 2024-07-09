package JAVAC5.com.WebOrderDoAn.Controllers;

import JAVAC5.com.WebOrderDoAn.Entities.Invoice;
import JAVAC5.com.WebOrderDoAn.Repositories.IInvoiceRepository;
import JAVAC5.com.WebOrderDoAn.Services.InvoiceService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        List<Object[]> monthlyRevenue = invoiceRepository.getMonthlyRevenue(); // Phương thức này cần được định nghĩa trong InvoiceRepository

        // Chuyển đổi dữ liệu thành mảng tháng và doanh thu
        String[] months = new String[monthlyRevenue.size()];
        double[] revenues = new double[monthlyRevenue.size()];

        for (int i = 0; i < monthlyRevenue.size(); i++) {
            Object[] row = monthlyRevenue.get(i);
            months[i] = (String) row[0]; // Giả sử cột đầu tiên là tháng
            revenues[i] = (double) row[1]; // Giả sử cột thứ hai là doanh thu
        }

        model.addAttribute("months", months);
        model.addAttribute("revenues", revenues);

        return "revenue/index"; // Trả về trang HTML để hiển thị biểu đồ
    }

    // Fetch monthly revenue data using Spring Data JPA
    private List<Object[]> fetchMonthlyRevenueData() {
        List<Object[]> results = new ArrayList<>();

        // Example logic to fetch data (adjust based on your repository and entity)
        List<Invoice> invoices = invoiceRepository.findAll(); // Replace with appropriate query

        // Example: Sum revenue by month
        for (int month = 1; month <= 12; month++) {
            double revenue = calculateRevenueForMonth(invoices, month);
            results.add(new Object[] { new SimpleDateFormat("MMMM").format(new Date(2024, month - 1, 1)), revenue });
        }

        return results;
    }

    // Example method to calculate revenue for a specific month
    private double calculateRevenueForMonth(List<Invoice> invoices, int month) {
        double totalRevenue = 0.0;
        for (Invoice invoice : invoices) {
            if (getMonth(invoice.getInvoiceDate()) == month) {
                totalRevenue += invoice.getPrice();
            }
        }
        return totalRevenue;
    }

    // Helper method to extract month from Date
    private int getMonth(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        return Integer.parseInt(dateFormat.format(date));
    }
}
