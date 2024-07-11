package JAVAC5.com.WebOrderDoAn.Controllers;

import JAVAC5.com.WebOrderDoAn.Services.CartService;
import JAVAC5.com.WebOrderDoAn.Services.InvoiceService;
import JAVAC5.com.WebOrderDoAn.Services.MomoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class PaymentController {

    @Autowired
    private MomoService momoService;

    @Autowired
    private InvoiceService orderService;

    @Autowired
    private CartService cartService;

    @GetMapping("/payment/create")
    public String createPayment(
            @RequestParam String customerName,
            @RequestParam String phoneCustomer,
            @RequestParam String addressCustomer,
            @RequestParam String emailCustomer,
            @RequestParam String descriptionOrder,
            HttpSession session) {
        try {
            String payUrl = momoService.createPayment(session, customerName, phoneCustomer, addressCustomer, emailCustomer, descriptionOrder);
            return "redirect:" + payUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred: " + e.getMessage();
        }
    }

    @GetMapping("/momo_return")
    public ModelAndView momoReturn(
            @RequestParam(value = "errorCode", required = false) String errorCode) {

        ModelAndView modelAndView = new ModelAndView();
        if (errorCode == null || !"0".equals(errorCode)) {
            modelAndView.setViewName("failure");
            modelAndView.addObject("message", "Payment failed. Please try again.");
        } else {
            modelAndView.setViewName("success");
            modelAndView.addObject("message", "Payment successful!");
        }

        return modelAndView;
    }
}