package com.letsellify.logistics.components.logistics.core.vendorManagement.exception;


import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * Author: Ahmad Buba
 * Date: 8/14/25
 */

public class DispatcherProfilePhotoExistsException extends LogisticsException {
    public DispatcherProfilePhotoExistsException(String msg) {
        super(msg);
    }

    public DispatcherProfilePhotoExistsException(String msg, Object... args) {
        super(msg, args);
    }

    public DispatcherProfilePhotoExistsException(Exception cause) {
        super(cause);
    }
}
