package com.letsellify.logistics.components.logistic.core.dispatcher.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * @author AHMAD BUBA
 * Date:2/22/25
 * Time:21:31
 */

public record DispatcherPersonalInfoDto(
  @NotBlank(message = "WhatsApp phone number cannot be empty.")
  @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid WhatsApp phone number format.")
  @Size(min = 10, max = 15, message = "WhatsApp phone number must be between 10 and 15 digits.")
  String whatsAppPhone,

  @NotBlank(message = "Phone number cannot be empty.")
  @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format.")
  @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 digits.")
  String phone,

  @NotBlank(message = "State cannot be empty.")
  String state,

  @NotBlank(message = "LGA cannot be empty.")
  String lga,

  @NotBlank(message = "Address cannot be empty.")
  String address
) {}
