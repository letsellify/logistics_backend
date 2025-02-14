package com.letsellify.logistics.components.user.core.verificationCodeManagement.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:2/12/25
 * Time:16:09
 */

public class UnableToCreateVerificationCodeException extends LogisticsException {

    public UnableToCreateVerificationCodeException(final String msg) {
        super(msg);
    }

    public UnableToCreateVerificationCodeException(final String msg, final Object... args) {
        super(msg, args);
    }

    public UnableToCreateVerificationCodeException(final Exception cause) {
        super(cause);
    }

}
