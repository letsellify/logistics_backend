package com.letsellify.logistics.components.logistics.core.nigeriaStateLGA.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:1/17/25
 * Time:11:49
 */

public class StateLGAFileNotFountException extends LogisticsException {

    public StateLGAFileNotFountException(final String msg) {
        super(msg);
    }

    public StateLGAFileNotFountException(final String msg, final Object... args) {
        super(msg, args);
    }

    public StateLGAFileNotFountException(final Exception cause) {
        super(cause);
    }

}
