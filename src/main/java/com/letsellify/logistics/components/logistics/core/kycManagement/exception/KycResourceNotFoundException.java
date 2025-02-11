package com.letsellify.logistics.components.logistics.core.kycManagement.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:1/12/25
 * Time:16:05
 */

public class KycResourceNotFoundException extends LogisticsException {

    public KycResourceNotFoundException(final String msg) {
        super(msg);
    }

    public KycResourceNotFoundException(final String msg, final Object... args) {
        super(msg, args);
    }

    public KycResourceNotFoundException(final Exception cause) {
        super(cause);
    }

}
