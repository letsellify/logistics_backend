package com.letsellify.logistics.components.logistic.core.vendor.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author AHMAD BUBA
 * Date:2/20/25
 * Time:11:57
 */

public record OrderDto(
  @NotBlank
  String itemName,
  @NotBlank
  String description,
  @NotNull
  BigDecimal amountForShipping,
  @NotNull
  BigDecimal amountForStorage,
  @NotNull
  List<String> images,
  @NotBlank
  String currentState,
  @NotBlank
  String currentLga,
  @NotBlank
  String shippingState,
  @NotBlank
  String shippingLga,
  @NotNull
  LocalDate possibleDeliveryDateStart,
  @NotNull
  LocalDate possibleDeliveryDateEnd
) {

}
