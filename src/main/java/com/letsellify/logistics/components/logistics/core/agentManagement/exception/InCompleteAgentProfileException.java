package com.letsellify.logistics.components.logistics.core.agentManagement.exception;


import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * Author: Ahmad Buba
 * Date: 8/19/25
 */

public class InCompleteAgentProfileException extends LogisticsException {

    public InCompleteAgentProfileException(String msg) {
        super(msg);
    }

    public InCompleteAgentProfileException(String msg, Object... args) {
        super(msg, args);
    }

    public InCompleteAgentProfileException(Exception cause) {
        super(cause);
    }
}
