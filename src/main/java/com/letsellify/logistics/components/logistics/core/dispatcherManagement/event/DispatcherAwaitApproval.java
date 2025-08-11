package com.letsellify.logistics.components.logistics.core.dispatcherManagement.event;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.LogisticDispatcherInfo;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

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
