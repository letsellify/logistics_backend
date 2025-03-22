package com.letsellify.logistics.components.user.core.logisticUser.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@Data
public class UpdateUserDto {
    @NotNull
    private String name;
    @NotBlank
    @Email
    private String email;
}
