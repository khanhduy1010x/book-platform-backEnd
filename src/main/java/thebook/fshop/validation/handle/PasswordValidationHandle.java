package thebook.fshop.validation.handle;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import thebook.fshop.validation.PasswordValidation;

public class PasswordValidationHandle implements ConstraintValidator<PasswordValidation, String> {

    @Override
    public void initialize(PasswordValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    }
}
