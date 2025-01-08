package com.letsellify.logistics.components.security.core.tokenManagement.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@Data
public class LoginDto {
    @NotBlank
    @Email(message = "Invalid mail address")
    private String email;
    @NotBlank
    @Size(min = 6, message = "Invalid Password")
    private String password;
}
