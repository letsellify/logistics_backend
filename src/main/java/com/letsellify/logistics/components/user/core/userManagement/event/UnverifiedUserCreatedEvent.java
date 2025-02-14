package com.letsellify.logistics.components.user.core.userManagement.event;

import org.springframework.context.ApplicationEvent;

import com.letsellify.logistics.common.data.LogisticsAppRole;

import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:2/12/25
 * Time:17:44
 */

@Getter
public class UnverifiedUserCreatedEvent extends ApplicationEvent {

    private final String username;
    private final LogisticsAppRole role;

    public UnverifiedUserCreatedEvent(final String email, final LogisticsAppRole role) {
        super(email);
        this.username = email;
        this.role = role;
    }

}
