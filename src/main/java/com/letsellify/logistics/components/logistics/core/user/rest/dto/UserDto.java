package com.letsellify.logistics.components.logistics.core.user.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@Data
public class UserDto {
    @NotBlank
    @Email
    private String email;
}
