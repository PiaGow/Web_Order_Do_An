package JAVAC5.com.WebOrderDoAn.Repositories;

import JAVAC5.com.WebOrderDoAn.Entities.Category;
import JAVAC5.com.WebOrderDoAn.Entities.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IFoodRepository extends PagingAndSortingRepository<Food, Long>, JpaRepository<Food, Long> {
    @Query("""
                SELECT b FROM Food b
                WHERE b.name LIKE %:keyword%
                OR b.category.name LIKE %:keyword%
            """)
    List<Food> searchFood(@Param("keyword") String keyword, Pageable pageable);

    default List<Food> findAllFoods(Integer pageNo,
                                    Integer pageSize,
                                    String sortBy) {
        return findAll(PageRequest.of(pageNo,
                pageSize,
                Sort.by(sortBy)))
                .getContent();
    }

    @Query("""
                SELECT b FROM Food b
                WHERE b.name LIKE %:keyword%
                OR b.category.name LIKE %:keyword%
            """)
    List<Food> searchFood2(@Param("keyword") String keyword);

    @Query("SELECT COUNT(b) FROM Food b WHERE b.category.id = :categoryId")
    long countByCategoryId(@Param("categoryId") Long categoryId);

    List<Food> findAllByOrderByIdAsc();

    List<Food> findByCategoryIdOrderByIdAsc(Long categoryId);

    List<Food> findFirst9ByOrderByIdAsc();
    List<Food> findFirst9ByOrderByIdAsc(Pageable pageable);
    List<Food> findByCategoryId(Long categoryId);
    List<Food> findByCategory(Category category);

}
