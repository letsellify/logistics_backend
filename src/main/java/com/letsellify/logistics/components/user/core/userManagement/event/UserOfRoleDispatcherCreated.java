package com.letsellify.logistics.components.user.core.userManagement.event;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:15:38
 */

@Getter
public class UserOfRoleDispatcherCreated extends ApplicationEvent {
    private final String userEmail;

    public UserOfRoleDispatcherCreated(final String userEmail) {
        super(userEmail);
        this.userEmail = userEmail;
    }

}
