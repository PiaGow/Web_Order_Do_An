package JAVAC5.com.WebOrderDoAn.Controllers;

import JAVAC5.com.WebOrderDoAn.Entities.Category;
import JAVAC5.com.WebOrderDoAn.Entities.Food;
import JAVAC5.com.WebOrderDoAn.Repositories.ICategoryRepository;
import JAVAC5.com.WebOrderDoAn.Repositories.IFoodRepository;
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
public class MenuController {

    @Autowired
    private ICategoryRepository categoryRepository;

    @Autowired
    private IFoodRepository foodRepository;

    @GetMapping("/menu")
    public String showMenu(Model model) {
        List<Category> categories = categoryRepository.findAll();
        List<Food> foods = foodRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("foods", foods);
        return "Menu/index";
    }

    @GetMapping("/menu/category/{id}")
    public String showFoodsByCategory(@PathVariable("id") Long id, Model model) {
        List<Category> categories = categoryRepository.findAll();
        Category category = categoryRepository.findById(id).orElse(null);
        List<Food> foods = foodRepository.findByCategoryId(id);
        model.addAttribute("categories", categories);
        model.addAttribute("foods", foods);
        model.addAttribute("selectedCategory", category);
        return "Menu/index";
    }
}
