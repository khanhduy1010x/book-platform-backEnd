package thebook.fshop.validation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;

import com.nimbusds.jose.Payload;

import thebook.fshop.validation.handle.EnumValueValidator;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = EnumValueValidator.class)
public @interface EnumValue {
    String name();

    String message() default "INVALID_ENUM";

    Class<? extends Enum<?>> enumClass();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
