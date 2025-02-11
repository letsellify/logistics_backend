package com.letsellify.logistics.components.logistics.core.shippingRequestManagement.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:2/9/25
 * Time:17:53
 */

public class InvalidLogisticsItemImageException extends LogisticsException {

    public InvalidLogisticsItemImageException(final String msg) {
        super(msg);
    }

    public InvalidLogisticsItemImageException(final String msg, final Object... args) {
        super(msg, args);
    }

    public InvalidLogisticsItemImageException(final Exception cause) {
        super(cause);
    }

}
