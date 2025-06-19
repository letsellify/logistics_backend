package com.letsellify.logistics.components.user.core.socialSignupAndLogin.rest.dto;

import com.letsellify.logistics.common.data.LogisticAppRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author AHMAD BUBA
 * Date:6/19/25
 * Time:21:43
 */

public record RoleSelectionDto(
  @NotBlank(message = "token must be present")
  String token,
  @NotNull(message = "User role must be selected")
  LogisticAppRole role
) {}
