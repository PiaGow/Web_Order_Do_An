package JAVAC5.com.WebOrderDoAn.Repositories;

import JAVAC5.com.WebOrderDoAn.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends
        JpaRepository<Category, Long> {
    @Query("""
        SELECT b FROM Category b
        WHERE b.name LIKE :keyword
    """)
    Category findByName(@Param("keyword") String keyword);
}
