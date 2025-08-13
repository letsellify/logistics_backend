package com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.validator;


import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.KycType;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.dto.DispatcherProfileDto;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.validator.annotation.ValidKyc;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Author: Ahmad Buba
 * Date: 8/13/25
 */


public class KycValidator implements ConstraintValidator<ValidKyc, DispatcherProfileDto> {

    @Override
    public boolean isValid(DispatcherProfileDto profileDto, ConstraintValidatorContext context) {
        if (profileDto.kyc() == null) {
            return false;
        }
        KycType type = profileDto.kyc().type();
        String number = profileDto.kyc().number();

        if (type == null || number == null) {
            return true; // Let @NonNull/NotBlank handle these
        }

        boolean valid = true;

        switch (type) {
            case NIN ->
            {
                if (!number.matches("\\d{11}")) {
                    addViolation(context, "NIN must be exactly 11 digits", "kyc.number");
                    valid = false;
                }
                break;
            }
            case BVN ->
            {
                if (!number.matches("\\d{11}")) {
                    addViolation(context, "BVN must be exactly 11 digits", "kyc.number");
                    valid = false;
                }
                break;
            }
            default ->
                addViolation(context, "KYC type must be either 'NIN' or 'BVN'", "kyc.type");
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
