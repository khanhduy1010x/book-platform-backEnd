package thebook.fshop.validation.handle;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import thebook.fshop.validation.NumberPhoneValidation;

public class NumberPhoneValidationHandle implements ConstraintValidator<NumberPhoneValidation, String> {
    @Override
    public void initialize(NumberPhoneValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String numberPhone, ConstraintValidatorContext constraintValidatorContext) {
        return numberPhone.matches("^0\\d{9,10}$");
    }
}
