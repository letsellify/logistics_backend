package com.letsellify.logistics.components.logistics.core.vendorManagement.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * @author AHMAD BUBA
 * Date:2/18/25
 * Time:04:22
 */


public record VendorTopUpDto(
        @NotNull(message = "Amount is required")
        @DecimalMin(value = "5.00", message = "Amount must be at least ₦5.00")
        @Digits(integer = 10, fraction = 2, message = "Amount must have at most 10 digits and 2 decimal places")
        @JsonProperty("amount")
        BigDecimal amount
) {
}
