package com.letsellify.logistics.components.logistic.core.dispatcher.event;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:5/3/25
 * Time:20:18
 */

@Getter
public class DispatcherNameUpdateEvent extends ApplicationEvent {
    private final String dispatcherEmail;
    private final String dispatcherNameBeforeUpdate;
    private final String dispatcherNameAfterUpdate;

    public DispatcherNameUpdateEvent(final String dispatcherEmail, final String dispatcherNameBeforeUpdate, final String dispatcherNameAfterUpdate) {
        super(dispatcherEmail);
        this.dispatcherEmail = dispatcherEmail;
        this.dispatcherNameBeforeUpdate = dispatcherNameBeforeUpdate;
        this.dispatcherNameAfterUpdate = dispatcherNameAfterUpdate;
    }

}
