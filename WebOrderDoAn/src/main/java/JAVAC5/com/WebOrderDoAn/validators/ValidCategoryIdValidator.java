package JAVAC5.com.WebOrderDoAn.validators;
import JAVAC5.com.WebOrderDoAn.Entities.Category;
import JAVAC5.com.WebOrderDoAn.validators.annotation.ValidCategoryId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
public class ValidCategoryIdValidator implements
        ConstraintValidator<ValidCategoryId, Category> {
    @Override
    public boolean isValid(Category category,
                           ConstraintValidatorContext context) {
        return category != null && category.getId() != null;
    }
}