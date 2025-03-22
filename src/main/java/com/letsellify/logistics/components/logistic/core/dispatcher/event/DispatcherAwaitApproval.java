package com.letsellify.logistics.components.logistic.core.dispatcher.event;

import org.springframework.context.ApplicationEvent;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.logistic.core.dispatcher.data.LogisticDispatcherInfo;

import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:2/23/25
 * Time:11:00
 */

@Getter
public class DispatcherAwaitApproval extends ApplicationEvent {
    private final String dispatcherName;
    private final String dispatcherEmail;
    private final LogisticAppRole role;

    public DispatcherAwaitApproval(final LogisticDispatcherInfo dispatcherInfo) {
        super(dispatcherInfo);
        this.dispatcherName = dispatcherInfo.name();
        this.dispatcherEmail = dispatcherInfo.email();
        this.role = LogisticAppRole.DISPATCHER;
    }

}
