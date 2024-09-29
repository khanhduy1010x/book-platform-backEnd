package thebook.fshop.validation.handle;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import thebook.fshop.validation.UserNameValidation;

public class UserNameValidationHandle implements ConstraintValidator<UserNameValidation, String> {

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return username.matches("^[a-zA-Z0-9_]{6,25}$");
    }
}
