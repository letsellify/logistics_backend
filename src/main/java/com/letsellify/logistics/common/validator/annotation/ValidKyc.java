package com.letsellify.logistics.common.validator.annotation;


import com.letsellify.logistics.common.validator.KycValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Author: Ahmad Buba
 * Date:8/13/25
 */

@Documented
@Constraint(validatedBy = KycValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidKyc {
    String message() default "Invalid Kyc";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
