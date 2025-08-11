package com.letsellify.logistics.components.logistics.core.agentManagement.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:2/26/25
 * Time:13:58
 */

public class UnapprovedAgentException extends LogisticsException {

    public UnapprovedAgentException(final String msg) {
        super(msg);
    }

}
