package JAVAC5.com.WebOrderDoAn.Controllers;
import JAVAC5.com.WebOrderDoAn.Entities.Category;
import JAVAC5.com.WebOrderDoAn.Entities.Food;
import JAVAC5.com.WebOrderDoAn.Services.CategoryService;
import JAVAC5.com.WebOrderDoAn.Services.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FoodService foodService;
    @GetMapping
    public String listCategories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        List<Food> foods = foodService.getAllFoods();
        model.addAttribute("categories", categories);
        model.addAttribute("foods", foods);
        return "Categories/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new Category());
        return "Categories/create";
    }

    @PostMapping("/create")
    public String createCategory(@ModelAttribute Category category) {
        categoryService.addCategory(category);
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Category category = categoryService.getAllCategories().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        model.addAttribute("category", category);
        return "Categories/edit";
    }

    @PostMapping("/edit/{id}")
    public String editCategory(@PathVariable Long id, @ModelAttribute Category category) {
        category.setId(id);
        categoryService.updateCategory(category);
        return "redirect:/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteCategoryById(id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/categories?error";
        }
        return "redirect:/categories";
    }
//    @GetMapping("/byCategory/{categoryId}")
//    public String getFoodsByCategory(@PathVariable Long categoryId, Model model) {
//        Category category = categoryService.getCategoryById(categoryId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + categoryId));
//        List<Food> foods = foodService.getFoodsByCategoryId(categoryId);
//        model.addAttribute("category", category);
//        model.addAttribute("foods", foods);
//        return "foodList"; // Thay thế bằng tên template của bạn
//    }
    @GetMapping("/by-category/{categoryId}")
    @ResponseBody
    public ResponseEntity<List<Food>> getFoodsByCategory(@PathVariable Long categoryId) {
        List<Food> foods = foodService.getFoodsByCategoryId(categoryId);
        return ResponseEntity.ok(foods);
    }




}
