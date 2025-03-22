package com.letsellify.logistics.components.user.core.verificationCode.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:2/10/25
 * Time:15:40
 */

public class InvalidVerificationCodeException extends LogisticsException {

    public InvalidVerificationCodeException(final Exception cause) {
        super(cause);
    }

    public InvalidVerificationCodeException(final String msg, final Object... args) {
        super(msg, args);
    }

    public InvalidVerificationCodeException(final String msg) {
        super(msg);
    }

}
