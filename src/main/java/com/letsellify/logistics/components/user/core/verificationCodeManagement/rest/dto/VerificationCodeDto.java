package com.letsellify.logistics.components.user.core.verificationCodeManagement.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * @author AHMAD BUBA
 * Date:2/12/25
 * Time:16:59
 */

public record VerificationCodeDto(
  @Email
  String email,
  @NotBlank
  String code
) {}
