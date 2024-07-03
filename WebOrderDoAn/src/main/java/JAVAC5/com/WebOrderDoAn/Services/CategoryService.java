package JAVAC5.com.WebOrderDoAn.Services;

import Tuan2.TranTanPhat.Entities.Category;
import Tuan2.TranTanPhat.Repositories.IBookRepository;
import Tuan2.TranTanPhat.Repositories.ICategoryRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class CategoryService {
    private final ICategoryRepository categoryRepository;
    private final IBookRepository bookRepository;
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }
    public void addCategory(Category category) {
        categoryRepository.save(category);
    }
    public void updateCategory(@NotNull Category category) {
        Category existingCategory = categoryRepository
                .findById(category.getId())
                .orElse(null);
        Objects.requireNonNull(existingCategory)
                .setName(category.getName());
        categoryRepository.save(existingCategory);
    }
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = {Exception.class, Throwable.class})
    public void deleteCategoryById(Long categoryId) throws Exception {
        if (bookRepository.countByCategoryId(categoryId) > 0) {
            throw new Exception("Category cannot be deleted as there are books associated with it.");
        }
        categoryRepository.deleteById(categoryId);
    }
}
