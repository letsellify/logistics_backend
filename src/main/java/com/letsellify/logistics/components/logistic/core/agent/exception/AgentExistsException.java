package com.letsellify.logistics.components.logistic.core.agent.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:5/3/25
 * Time:17:53
 */

@Getter
public class AgentExistsException extends LogisticsException {
    private final String message;

    public AgentExistsException(final String msg) {
        super(msg);
        this.message = msg;
    }

    public AgentExistsException(final String msg, final Object... args) {
        super(msg, args);
        this.message = msg;
    }

    public AgentExistsException(final Exception cause) {
        super(cause);
        this.message = cause.getMessage();
    }

}
