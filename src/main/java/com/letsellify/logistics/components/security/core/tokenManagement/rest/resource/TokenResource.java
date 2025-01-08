package com.letsellify.logistics.components.security.core.tokenManagement.rest.resource;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@Data
public class TokenResource {
    @NotBlank
    private String issuer;
    @NotBlank
    private String accessToken;
    @NotBlank
    private String refreshToken;
}
