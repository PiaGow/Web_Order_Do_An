package JAVAC5.com.WebOrderDoAn.Controllers;

import JAVAC5.com.WebOrderDoAn.Entities.Category;
import JAVAC5.com.WebOrderDoAn.Entities.Food;
import JAVAC5.com.WebOrderDoAn.Services.CategoryService;
import JAVAC5.com.WebOrderDoAn.Services.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
@RequestMapping("/menu")
public class MenuController {

    private final CategoryService categoryService;
    private final FoodService foodService;

    @Autowired
    public MenuController(CategoryService categoryService, FoodService foodService) {
        this.categoryService = categoryService;
        this.foodService = foodService;
    }

    @GetMapping
    public String showMenu(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "Menu/index";
    }

    @GetMapping("/foods/{categoryId}")
    public String getFoodsByCategory(@PathVariable("categoryId") Long categoryId,
                                     Model model,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "9") int size) {
        List<Food> foods = List.of();
        if (categoryId == 0) { // Load all foods
            foods = foodService.getFirstNineFoodsOrderedById(PageRequest.of(page, size));
        } else { // Load foods by category
            //foods = foodService.getFoodsByCategoryId(categoryId, pageable);
        }
        model.addAttribute("foods", foods);
        return "Menu/foodListContainer :: foodList";
    }

    @ModelAttribute("categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
