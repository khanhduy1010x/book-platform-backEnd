package thebook.fshop.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import thebook.fshop.validation.handle.FileSizeValidationHandle;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileSizeValidationHandle.class)
public @interface FileSizeValidation {
    String message() default "INVALID_FILE_SIZE";

    long maxFileSize() default 1048576;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
