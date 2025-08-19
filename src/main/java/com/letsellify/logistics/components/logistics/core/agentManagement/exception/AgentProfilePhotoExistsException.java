package com.letsellify.logistics.components.logistics.core.agentManagement.exception;


import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * Author: Ahmad Buba
 * Date: 8/19/25
 */

public class AgentProfilePhotoExistsException extends LogisticsException {

    public AgentProfilePhotoExistsException(String msg) {
        super(msg);
    }

    public AgentProfilePhotoExistsException(String msg, Object... args) {
        super(msg, args);
    }

    public AgentProfilePhotoExistsException(Exception cause) {
        super(cause);
    }
}
