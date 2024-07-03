package JAVAC5.com.WebOrderDoAn.Services;

import JAVAC5.com.WebOrderDoAn.Entities.Food;
import JAVAC5.com.WebOrderDoAn.Repositories.ICategoryRepository;
import JAVAC5.com.WebOrderDoAn.Repositories.IFoodRepository;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
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
    public List<Food> getAllBooks() {
        return foodRepository.findAll();
    }
    public List<Food> getAllBooks(Integer pageNo,
                                  Integer pageSize,
                                  String sortBy) {
        return foodRepository.findAllFoods(pageNo, pageSize, sortBy);
    }
    public Optional<Food> getBookById(Long id) {
        return foodRepository.findById(id);
    }
    public void addBook(Food book) {
        foodRepository.save(book);
    }
    public void updateBook(@NotNull Food book) {
        Food existingBook = foodRepository.findById(book.getId())
                .orElse(null);
        Objects.requireNonNull(existingBook).setName(book.getName());
        existingBook.setImage_url(book.getImage_url());
        existingBook.setPrice(book.getPrice());
        existingBook.setCategory(book.getCategory());
        foodRepository.save(existingBook);}

    public void deleteBookById(Long id) {
        foodRepository.deleteById(id);

    }
    public List<Food> searchByName(Integer pageNo,
                                            Integer pageSize,
                                            String sortBy,
                                            String keyword) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return foodRepository.searchFood(keyword, pageable);
    }
    public List<Food> getAllBooksSorted(Integer pageNo,
                                        Integer pageSize,
                                        String sortBy) {
        // Check if sortBy is valid, otherwise default to "id"
        if (!Arrays.asList("id", "name", "price", "category").contains(sortBy)) {
            sortBy = "id";
        }
        return foodRepository.findAllFoods(pageNo, pageSize, sortBy);
    }
    public List<Food> searchBook2(String keyword) {
        return foodRepository.searchFood2(keyword);
    }

}
