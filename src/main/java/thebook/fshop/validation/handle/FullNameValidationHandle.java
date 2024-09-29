package thebook.fshop.validation.handle;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import thebook.fshop.validation.FullNameValidation;

public class FullNameValidationHandle implements ConstraintValidator<FullNameValidation, String> {
    @Override
    public void initialize(FullNameValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String fullName, ConstraintValidatorContext constraintValidatorContext) {
        return fullName.matches("^[A-Za-z\\s]{1,40}$");
    }
}
