package thebook.fshop.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import thebook.fshop.validation.handle.NumberPhoneValidationHandle;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NumberPhoneValidationHandle.class)
public @interface NumberPhoneValidation {
    String message() default "INVALID_PHONE_NUMBER";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
