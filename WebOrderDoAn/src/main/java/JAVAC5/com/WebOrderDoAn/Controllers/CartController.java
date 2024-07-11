package JAVAC5.com.WebOrderDoAn.Controllers;

import JAVAC5.com.WebOrderDoAn.Services.CartService;
import JAVAC5.com.WebOrderDoAn.Services.MomoService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    @Autowired
    private MomoService momoService;
    private final CartService cartService;
    @GetMapping
    public String showCart(HttpSession session,
                           @NotNull Model model) {
        model.addAttribute("cart", cartService.getCart(session));
        model.addAttribute("totalPrice",
                cartService.getSumPrice(session));
        model.addAttribute("totalQuantity",
                cartService.getSumQuantity(session));
        return "food/cart";
    }
    @GetMapping("/removeFromCart/{id}")
    public String removeFromCart(HttpSession session, @PathVariable Long id) {
        var cart = cartService.getCart(session);
        cart.removeItems(id);
        return "redirect:/cart";
    }
    @GetMapping("/updateCart/{id}/{quantity}")
    public String updateCart(HttpSession session, @PathVariable Long id, @PathVariable int quantity) {
        var cart = cartService.getCart(session);
        cart.updateItems(id, quantity);
        return "food/cart";
    }
    @GetMapping("/clearCart")
    public String clearCart(HttpSession session) {
        cartService.removeCart(session);
        return "redirect:/cart ";
    }
    @GetMapping("/checkout")
    public String checkout(HttpSession session) {
        cartService.saveCart(session);
        return "redirect:/cart";
    }
    @GetMapping("/checkout/momo")
    public String checkoutMomo(HttpSession session, Model model) {
        try {
            String customerName = "John Doe"; // Replace with actual customer name
            String phoneCustomer = "123456789"; // Replace with actual customer phone number
            String addressCustomer = "123 Street"; // Replace with actual customer address
            String emailCustomer = "email@example.com"; // Replace with actual customer email
            String descriptionOrder = "Description of the order"; // Replace with actual order description

            String payUrl = momoService.createPayment(session, customerName, phoneCustomer, addressCustomer, emailCustomer, descriptionOrder);
            return "redirect:" + payUrl;
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error occurred: " + e.getMessage());
            return "error";
        }
    }
    }


