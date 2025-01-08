package com.letsellify.logistics.common.restException;

import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@Data
public class LogisticsErrorResponse {
    private String errorCode;

    private String message;
}
