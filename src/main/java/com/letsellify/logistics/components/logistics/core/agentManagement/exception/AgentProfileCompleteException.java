package com.letsellify.logistics.components.logistics.core.agentManagement.exception;


import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * Author: Ahmad Buba
 * Date: 8/19/25
 */

public class AgentProfileCompleteException extends LogisticsException {
    public AgentProfileCompleteException(String msg) {
        super(msg);
    }

    public AgentProfileCompleteException(String msg, Object... args) {
        super(msg, args);
    }

    public AgentProfileCompleteException(Exception cause) {
        super(cause);
    }
}
