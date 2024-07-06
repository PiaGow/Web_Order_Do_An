package JAVAC5.com.WebOrderDoAn.Controllers;

import JAVAC5.com.WebOrderDoAn.Entities.Food;
import JAVAC5.com.WebOrderDoAn.Services.CartService;
import JAVAC5.com.WebOrderDoAn.Services.CategoryService;
import JAVAC5.com.WebOrderDoAn.Entities.CartItem;
import JAVAC5.com.WebOrderDoAn.Services.FoodService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/foods")
@RequiredArgsConstructor
public class FoodController {
    private final FoodService foodService;
    private final CategoryService categoryService;
    private final CartService cartService;

    @GetMapping
    public String showAllFoods(@NotNull Model model,
                               @RequestParam(defaultValue = "0")
                               Integer pageNo,
                               @RequestParam(defaultValue = "20")
                               Integer pageSize,
                               @RequestParam(defaultValue = "id")
                               String sortBy) {
        model.addAttribute("foods", foodService.getAllFoods(pageNo, pageSize, sortBy));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages",
                foodService.getAllFoods(pageNo, pageSize, sortBy).size() / pageSize);
        model.addAttribute("categories",
                categoryService.getAllCategories());
        return "Food/list";
    }

    @GetMapping("/add")
    public String addFoodForm(@NotNull Model model) {
        model.addAttribute("food", new Food());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "Food/add";
    }

    @PostMapping("/add")
    public String addFood(
            @Valid @ModelAttribute("food") Food food,
            @NotNull BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            model.addAttribute("categories",
                    categoryService.getAllCategories());
            return "Food/add";
        }
        foodService.addFood(food);
        return "redirect:/foods";
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
        return "redirect:/foods";
    }

    @GetMapping("/delete/{id}")
    public String deleteFood(@PathVariable long id) {
        foodService.getFoodById(id)
                .ifPresentOrElse(
                        food -> foodService.deleteFoodById(id),
                        () -> {
                            throw new IllegalArgumentException("Book not found");
                        });
        return "redirect:/foods";
    }

    @GetMapping("/edit/{id}")
    public String editFoodForm(@NotNull Model model, @PathVariable long id) {
        var food = foodService.getFoodById(id);
        model.addAttribute("food", food.orElseThrow(() -> new
                IllegalArgumentException("Food not found")));
        model.addAttribute("categories",
                categoryService.getAllCategories());
        return "Food/edit";
    }

    @PostMapping("/edit")
    public String editFood(@Valid @ModelAttribute("food") Food food,
                           @NotNull BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            var errors = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toArray(String[]::new);
            model.addAttribute("errors", errors);
            model.addAttribute("categories",
                    categoryService.getAllCategories());
            return "Food/edit";
        }
        foodService.updateFood(food);
        return "redirect:/foods";
    }

    @GetMapping("/search")
    public String searchFood(
            @NotNull Model model,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        model.addAttribute("foods", foodService.searchFood2(keyword));
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages",
                foodService
                        .getAllFoods(pageNo, pageSize, sortBy)
                        .size() / pageSize);
        model.addAttribute("categories",
                categoryService.getAllCategories());
        return "Food/list";
    }

}