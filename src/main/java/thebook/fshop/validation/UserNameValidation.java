package thebook.fshop.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import thebook.fshop.validation.handle.UserNameValidationHandle;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserNameValidationHandle.class)
public @interface UserNameValidation {
    String message() default "INVALID_USERNAME";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
