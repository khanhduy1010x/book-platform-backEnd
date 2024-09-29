package thebook.fshop.validation.handle;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import thebook.fshop.validation.IDValidation;

public class IDValidationHandle implements ConstraintValidator<IDValidation, Integer> {

    @Override
    public void initialize(IDValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null && s > 0;
    }
}
