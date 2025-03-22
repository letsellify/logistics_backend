package com.letsellify.logistics.components.logistic.core.agent.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:2/26/25
 * Time:13:31
 */

public class NoSuchAgentException extends LogisticsException {

    public NoSuchAgentException(final String msg) {
        super(msg);
    }

}
