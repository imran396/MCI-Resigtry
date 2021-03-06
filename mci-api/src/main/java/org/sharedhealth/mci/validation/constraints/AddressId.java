package org.sharedhealth.mci.validation.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import org.sharedhealth.mci.validation.constraintvalidator.AddressIdValidator;
import org.sharedhealth.mci.validation.AddressType;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = AddressIdValidator.class)
@Documented
public @interface AddressId {

    String message() default "1001";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    AddressType value();
}
