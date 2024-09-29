package thebook.fshop.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import thebook.fshop.validation.handle.IDValidationHandle;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IDValidationHandle.class)
public @interface IDValidation {
    String message() default "INVALID_ID";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
