package com.letsellify.logistics.components.user.core.verificationCode.rest.dto;

import jakarta.validation.constraints.Email;

/**
 * @author AHMAD BUBA
 * Date:2/12/25
 * Time:17:04
 */

public record ResendVerificationCodeDto(@Email String email) {}
