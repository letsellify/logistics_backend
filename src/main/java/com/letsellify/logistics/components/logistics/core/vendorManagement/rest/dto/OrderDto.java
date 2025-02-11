package com.letsellify.logistics.components.logistics.core.vendorManagement.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.letsellify.logistics.components.logistics.core.paymentManagement.data.PaymentMethod;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:09:45
 */

@Data
public class OrderDto {
    @NotNull
    private ItemDto item;
    @NotBlank
    private String currentState;
    @NotBlank
    private String currentLga;
    @NotBlank
    private String shippingState;
    @NotBlank
    private String shippingLga;
    @NotBlank
    private LocalDate startDate;
    @NotBlank
    private LocalDate endDate;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
    @Digits(integer = 10, fraction = 2, message = "Invalid amount format. Max 10 digits with up to 2 decimal places.")
    private BigDecimal amountForShipping;
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
    @Digits(integer = 10, fraction = 2, message = "Invalid amount format. Max 10 digits with up to 2 decimal places.")
    private BigDecimal amountForStorage;
    @NotNull
    private PaymentMethod modeOfPayment;
}
