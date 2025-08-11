package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.data;

import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.Dispatcher;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.dataMapper.LogisticRequestDataMapper;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource.LogisticDispatcherResource;

import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:6/22/25
 * Time:21:03
 */


public record LogisticDispatcher(
        UUID id,
        String name,
        String email,
        String whatsAppPhone,
        String phone,
        String state,
        String lga,
        String address
) {
    public LogisticDispatcher(Dispatcher dispatcher) {
        this(dispatcher.id(), dispatcher.name(), dispatcher.email(), dispatcher.whatsAppPhone(), dispatcher.phone(), dispatcher.state(), dispatcher.lga(), dispatcher.address());
    }

    public LogisticDispatcherResource toResource() {
        return LogisticRequestDataMapper.INSTANCE.dataToResource(this);
    }
}
