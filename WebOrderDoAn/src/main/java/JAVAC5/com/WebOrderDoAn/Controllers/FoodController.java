package JAVAC5.com.WebOrderDoAn.Controllers;

import JAVAC5.com.WebOrderDoAn.Entities.Category;
import JAVAC5.com.WebOrderDoAn.Entities.Food;
import JAVAC5.com.WebOrderDoAn.RegexFileName.StringUtils;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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
            @NotNull BindingResult bindingResult, Model model,@RequestParam("image")MultipartFile file) {
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
        if(!file.isEmpty()) {
            try{
                // Lưu ảnh mới
                String fileName = StringUtils.normalizeFileName(food.getName());
                Path filePath = Paths.get("src/main/resources/static/img/" + fileName +".png");
                Files.copy(file.getInputStream(), filePath);
                food.setImage_url( "/img/" + fileName+".png");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
    public String deleteFood(@PathVariable long id) throws IOException {
        foodService.getFoodById(id)
                .ifPresentOrElse(
                        food ->
                        {
                            if (food.getImage_url() != null && !food.getImage_url().isEmpty()) {
                                Path oldFilePath = Paths.get("src/main/resources/static" + food.getImage_url());
                                try {
                                    Files.deleteIfExists(oldFilePath);
                                } catch (IOException e) {
                                    throw new RuntimeException("Failed to delete image file", e);
                                }
                            }
                            foodService.deleteFoodById(id);
                        },
                        () -> {
                            throw new IllegalArgumentException("Food not found");
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
                           Model model,@RequestParam("image")MultipartFile file) {
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
        if(!file.isEmpty()) {
            try{
                // Xóa ảnh cũ nếu tồn tại
                if (food.getImage_url() != null && !food.getImage_url().isEmpty()) {
                    Path oldFilePath = Paths.get("src/main/resources/static" + food.getImage_url());
                    if (Files.exists(oldFilePath)) {
                        Files.deleteIfExists(oldFilePath);
                        System.out.println("Deleted old image file: " + oldFilePath.toString());
                    } else {
                        System.out.println("Old image file does not exist: " + oldFilePath.toString());
                    }
                }

                // Lưu ảnh mới
                String fileName = StringUtils.normalizeFileName(food.getName());
                Path filePath = Paths.get("src/main/resources/static/img/" + fileName+".png");
                Files.copy(file.getInputStream(), filePath);
                food.setImage_url( "/img/" + fileName+".png");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            // Nếu không có ảnh mới, cập nhật tên tệp ảnh cũ theo tên mới của Food
            if (food.getImage_url() != null) {
                Path oldFilePath = Paths.get("src/main/resources/static" + food.getImage_url());
                String newFileName = StringUtils.normalizeFileName(food.getName());
                Path newFilePath = Paths.get("src/main/resources/static/img/" + newFileName+".png");

                try {
                    Files.copy(oldFilePath,newFilePath);
                    Files.deleteIfExists(oldFilePath);
                    food.setImage_url("/img/" + newFileName+".png");
                } catch (IOException e) {
                    throw new RuntimeException("Failed to rename image file", e);
                }
            }
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

    @GetMapping("/foods/category/{id}")
    public String listFoodsByCategory(@PathVariable Long id, Model model) {
        List<Food> foods;
        if (id == null || id == 0) {
            foods = foodService.getFirst9FoodsOrderedByIdAsc();
        } else {
            foods = foodService.getFoodsByCategoryId(id);
        }
        model.addAttribute("foods", foods);
        return "fragments/foodList :: foodList"; // Ensure fragment name matches HTML
    }

}