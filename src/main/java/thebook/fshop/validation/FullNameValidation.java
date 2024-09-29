package thebook.fshop.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import thebook.fshop.validation.handle.FullNameValidationHandle;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FullNameValidationHandle.class)
public @interface FullNameValidation {
    String message() default "INVALID_FULL_NAME";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
