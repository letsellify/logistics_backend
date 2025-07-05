package com.letsellify.logistics.components.logistic.core.vendor.rest.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author AHMAD BUBA
 * Date:6/20/25
 * Time:18:20
 */

public record GoodInformation(
  @NotBlank
  String itemName,
  @Size(min = 1, message= "Quantity must be at least 1")
  int quantity,
  @NotBlank
  String description,
  @NotNull
  String fragility,
  @NotNull
  Set<String> condition,
  @NotNull
  int weight,
  @NotNull
  String imageUrl1,
  @NotNull
  String imageUrl2
) {
}
