package com.letsellify.logistics.components.logistic.core.vendor.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author AHMAD BUBA
 * Date:6/21/25
 * Time:17:54
 */

public record ReceiverInformation(
  @NotBlank
  String fullName,
  @NotBlank
  String location,
  @NotBlank
  String state,
  @NotBlank
  String lga,
  @NotNull
  String email,
  @NotBlank
  String callPhoneNumber,
  @NotNull
  String whatsAppPhoneNumber
) {}
