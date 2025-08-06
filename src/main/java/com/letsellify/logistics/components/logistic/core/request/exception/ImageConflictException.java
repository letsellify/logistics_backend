package com.letsellify.logistics.components.logistic.core.request.exception;


import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * Author: Ahmad Buba
 * Date: 8/6/25
 */

public class ImageConflictException extends LogisticsException {
    public ImageConflictException(String msg) {
        super(msg);
    }

    public ImageConflictException(String msg, Object... args) {
        super(msg, args);
    }

    public ImageConflictException(Exception cause) {
        super(cause);
    }
}
