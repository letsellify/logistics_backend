package com.letsellify.logistics.components.logistics.core.kycManagement.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:1/12/25
 * Time:16:06
 */

public class KycBadRequestException extends LogisticsException {

    public KycBadRequestException(final String msg) {
        super(msg);
    }

    public KycBadRequestException(final String msg, final Object... args) {
        super(msg, args);
    }

    public KycBadRequestException(final Exception cause) {
        super(cause);
    }

}
