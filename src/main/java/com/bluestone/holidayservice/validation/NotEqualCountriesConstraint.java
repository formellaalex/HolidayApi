package com.bluestone.holidayservice.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CommonHolidayValidator.class)
@Target( { ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEqualCountriesConstraint {
    String message() default "Country codes cannot be the same";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}