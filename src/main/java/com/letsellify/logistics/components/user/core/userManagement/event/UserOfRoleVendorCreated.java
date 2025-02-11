package com.letsellify.logistics.components.user.core.userManagement.event;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:15:37
 */

@Getter
public class UserOfRoleVendorCreated extends ApplicationEvent {
    private final String userEmail;

    public UserOfRoleVendorCreated(final String userEmail) {
        super(userEmail);
        this.userEmail = userEmail;
    }

}
