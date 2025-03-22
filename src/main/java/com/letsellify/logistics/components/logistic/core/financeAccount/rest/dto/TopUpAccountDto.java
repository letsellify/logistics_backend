package com.letsellify.logistics.components.logistic.core.financeAccount.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/10/25
 * Time:10:44
 */

@Data
public class TopUpAccountDto {

    @NotBlank(message = "Amount cannot be blank")
    @Pattern(regexp = "\\d+(\\.\\d{1,2})?", message = "Amount must be a positive number with up to two decimal places")
    private String amount;

}
