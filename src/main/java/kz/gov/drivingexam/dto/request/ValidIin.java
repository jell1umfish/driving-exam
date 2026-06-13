package kz.gov.drivingexam.dto.request;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IinValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidIin {
    String message() default "Некорректный ИИН";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}