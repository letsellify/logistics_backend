package com.letsellify.logistics.components.user.core.userManagement.rest.dto;

import com.letsellify.logistics.common.data.LogisticsAppRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@Data
public class CreateUserDto {
    @NotNull
    private String name;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 6, message = "Invalid Password")
    private String password;
    @NotNull
    private LogisticsAppRole role;
}
