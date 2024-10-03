package thebook.fshop.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import thebook.fshop.validation.handle.FileValidationHandle;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileValidationHandle.class)
public @interface FileValidation {
    String message() default "INVALID_FILE";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
