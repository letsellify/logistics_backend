package com.letsellify.logistics.common.validator;


import com.letsellify.logistics.common.validator.annotation.ValidKyc;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.KycType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Author: Ahmad Buba
 * Date: 8/13/25
 */


public class KycValidator implements ConstraintValidator<ValidKyc, HasKyc> {

    @Override
    public boolean isValid(HasKyc dto, ConstraintValidatorContext context) {
        if (dto == null || dto.getKyc() == null) {
            return false;
        }

        KycType type = dto.getKyc().type();
        String number = dto.getKyc().number();

        if (type == null || number == null) {
            return true; // let @NotNull / @NotBlank handle this
        }

        boolean valid = true;
        switch (type) {
            case NIN -> {
                if (!number.matches("\\d{11}")) {
                    addViolation(context, "NIN must be exactly 11 digits", "kyc.number");
                    valid = false;
                }
            }
            case BVN -> {
                if (!number.matches("\\d{11}")) {
                    addViolation(context, "BVN must be exactly 11 digits", "kyc.number");
                    valid = false;
                }
            }
            default -> addViolation(context, "KYC type must be either 'NIN' or 'BVN'", "kyc.type");
        }

        return valid;
    }

    private void addViolation(ConstraintValidatorContext context, String message, String propertyPath) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(propertyPath)
                .addConstraintViolation();
    }
}
