package com.letsellify.logistics.components.logistic.core.dispatcher.rest.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * @author AHMAD BUBA
 * Date:5/3/25
 * Time:21:36
 */

public record DispatchDetailDto(
  @NotBlank(message = "Please provide plate number of dispatch")
  String identificationNumber,
  @NotBlank(message = "State cannot be empty.")
  String state,
  @NotBlank(message = "LGA cannot be empty.")
  String lga
) {}
