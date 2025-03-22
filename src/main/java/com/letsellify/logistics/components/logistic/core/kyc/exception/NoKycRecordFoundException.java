package com.letsellify.logistics.components.logistic.core.kyc.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:2/22/25
 * Time:21:45
 */

public class NoKycRecordFoundException extends LogisticsException {

    public NoKycRecordFoundException(final String msg) {
        super(msg);
    }

    public NoKycRecordFoundException(final String msg, final Object... args) {
        super(msg, args);
    }

    public NoKycRecordFoundException(final Exception cause) {
        super(cause);
    }

}
