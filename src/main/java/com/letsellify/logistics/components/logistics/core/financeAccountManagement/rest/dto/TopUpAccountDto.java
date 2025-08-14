package com.letsellify.logistics.components.logistics.core.financeAccountManagement.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author AHMAD BUBA
 * Date:1/10/25
 * Time:10:44
 */

public record TopUpAccountDto(
        @NotNull(message = "Amount is required")
        @DecimalMin(value = "5.00", message = "Amount must be at least ₦5.00")
        @Digits(integer = 10, fraction = 2, message = "Amount must have at most 10 digits and 2 decimal places")
        @JsonProperty("amount")
        BigDecimal amount
) {
}
