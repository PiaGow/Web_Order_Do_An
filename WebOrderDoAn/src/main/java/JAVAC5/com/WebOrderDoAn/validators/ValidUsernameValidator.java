package JAVAC5.com.WebOrderDoAn.validators;

import JAVAC5.com.WebOrderDoAn.Services.UserService;
import JAVAC5.com.WebOrderDoAn.validators.annotation.ValidUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidUsernameValidator implements ConstraintValidator<ValidUsername, String> {
    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (userService == null) {
            return true;
        }

        return userService.findByUsername(username) == null;
    }
}