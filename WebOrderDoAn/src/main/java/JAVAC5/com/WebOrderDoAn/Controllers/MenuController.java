package JAVAC5.com.WebOrderDoAn.Controllers;

import JAVAC5.com.WebOrderDoAn.Entities.CartItem;
import JAVAC5.com.WebOrderDoAn.Entities.Category;
import JAVAC5.com.WebOrderDoAn.Entities.Food;
import JAVAC5.com.WebOrderDoAn.Repositories.ICategoryRepository;
import JAVAC5.com.WebOrderDoAn.Repositories.IFoodRepository;
import JAVAC5.com.WebOrderDoAn.Services.CartService;
import JAVAC5.com.WebOrderDoAn.Services.CategoryService;
import JAVAC5.com.WebOrderDoAn.Services.FoodService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import JAVAC5.com.WebOrderDoAn.Services.CartService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/menu")

public class MenuController {

    private final CartService cartService;
    @Autowired
    private ICategoryRepository categoryRepository;

    @Autowired
    private IFoodRepository foodRepository;

    @GetMapping
    public String showMenu(Model model) {
        List<Category> categories = categoryRepository.findAll();
        List<Food> foods = foodRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("foods", foods);
        return "Menu/index";
    }
    @GetMapping("/menu")
    public String showMenu2(Model model) {
        List<Category> categories = categoryRepository.findAll();
        List<Food> foods = foodRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("foods", foods);
        return "Menu/menu";
    }
    @GetMapping("/random")
    public String randomMenu(Model model) {
        List<Food> foods = foodRepository.findAll();
        model.addAttribute("foods", foods);
        return "Menu/random";
    }
    @GetMapping("/category/{id}")
    public String showFoodsByCategory(@PathVariable("id") Long id, Model model) {
        Category category = categoryRepository.findById(id).orElse(null);
        List<Food> foods = foodRepository.findByCategoryId(id);
        model.addAttribute("foods", foods);
        model.addAttribute("selectedCategory", category);
        return "Menu/index";
    }
    @PostMapping("/add-to-cart")
    public String addToCart(HttpSession session,
                            @RequestParam long id,
                            @RequestParam String name,
                            @RequestParam double price,
                            @RequestParam(defaultValue = "1") int quantity) {
        var cart = cartService.getCart(session);
        cart.addItems(new CartItem(id, name, price, quantity));
        cartService.updateCart(session, cart);
        return "redirect:/menu/menu";
    }
}
