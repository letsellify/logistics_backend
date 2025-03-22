package com.letsellify.logistics.components.user.core.authorizationToken.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@Data
public class NewTokenDto {
    @NotBlank
    private String refreshToken;
}
