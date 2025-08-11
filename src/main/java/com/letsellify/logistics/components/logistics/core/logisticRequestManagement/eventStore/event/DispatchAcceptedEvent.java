package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.eventStore.event;

import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:2/6/25
 * Time:09:30
 */

public record DispatchAcceptedEvent(
        String requestId,
        UUID dispatcherId
) {
}
