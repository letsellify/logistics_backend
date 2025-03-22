package com.letsellify.logistics.components.logistic.core.request.eventStore;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import com.letsellify.logistics.components.logistic.core.request.LogisticRequestManager;
import com.letsellify.logistics.components.logistic.core.request.eventStore.event.InDispatcherPossessionEvent;
import com.letsellify.logistics.components.logistic.core.request.eventStore.event.LogisticRequestedEvent;
import com.letsellify.logistics.components.logistic.core.request.exception.NoSuchLogisticRequestException;

import lombok.RequiredArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:2/21/25
 * Time:17:30
 */

@Component
@RequiredArgsConstructor
public class EventHandling {
    private final LogisticRequestManager manager;


    /**
     * Handles a new logistics request event. Validates financial transactions before persisting the request.
     */
    @EventHandler
    public void processLogisticsRequest(final LogisticRequestedEvent event) {
       this.manager.write(event);
    }

    /**
     * Handles the event when a dispatcher takes possession of a logistics request.
     * Uses caching to optimize data retrieval.
     */
    @EventHandler
    public void updateDispatcherPossession(final InDispatcherPossessionEvent event) throws NoSuchLogisticRequestException {
       this.manager.handle(event);
    }

}
