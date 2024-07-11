package JAVAC5.com.WebOrderDoAn.Services;

import JAVAC5.com.WebOrderDoAn.Entities.Category;
import JAVAC5.com.WebOrderDoAn.Entities.Food;
import JAVAC5.com.WebOrderDoAn.Repositories.ICategoryRepository;
import JAVAC5.com.WebOrderDoAn.Repositories.IFoodRepository;
import JAVAC5.com.WebOrderDoAn.Repositories.IInvoiceItemRepository;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE,
        rollbackFor = {Exception.class, Throwable.class})
public class FoodService {
    private final IFoodRepository foodRepository;
    private final ICategoryRepository categoryRepository;
    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }
    public List<Food> getAllFoods(Integer pageNo,
                                  Integer pageSize,
                                  String sortBy) {
        return foodRepository.findAllFoods(pageNo, pageSize, sortBy);
    }
    public Optional<Food> getFoodById(Long id) {
        return foodRepository.findById(id);
    }
    public void addFood(Food Food) {
        foodRepository.save(Food);
    }
    public void updateFood(@NotNull Food food) {
        Food existingBook = foodRepository.findById(food.getId()).orElse(null);
        Objects.requireNonNull(existingBook).setName(food.getName());
        existingBook.setImage_url(food.getImage_url());
        existingBook.setPrice(food.getPrice());
        existingBook.setCategory(food.getCategory());
        foodRepository.save(existingBook);}

    public void deleteFoodById(Long id) {
        foodRepository.deleteById(id);

    }
    public List<Food> searchByName(Integer pageNo,
                                   Integer pageSize,
                                   String sortBy,
                                   String keyword) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return foodRepository.searchFood(keyword, pageable);
    }
    public List<Food> getAllFoodsSorted(Integer pageNo,
                                        Integer pageSize,
                                        String sortBy) {
        // Check if sortBy is valid, otherwise default to "id"
        if (!Arrays.asList("id", "name", "price", "category").contains(sortBy)) {
            sortBy = "id";
        }
        return foodRepository.findAllFoods(pageNo, pageSize, sortBy);
    }
    public List<Food> searchFood2(String keyword) {
        return foodRepository.searchFood2(keyword);
    }
    public List<Food> getFoodsByCategoryId(Long categoryId) {
        return foodRepository.findByCategoryIdOrderByIdAsc(categoryId);
    }

    public List<Food> getFirst9FoodsOrderedByIdAsc() {
        return foodRepository.findFirst9ByOrderByIdAsc();
    }
    public List<Food> getFirstNineFoodsOrderedById(Pageable pageable) {
        return foodRepository.findFirst9ByOrderByIdAsc(pageable);
    }
    public List<Food> findByCategory(Category category) {
        return foodRepository.findByCategory(category);
    }

    @Autowired
    private IInvoiceItemRepository invoiceItemRepository;

    public List<Object[]> getFoodStatistics() {
        return invoiceItemRepository.findFoodStatistics();
    }

}
