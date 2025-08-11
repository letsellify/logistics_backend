package com.letsellify.logistics.components.user.core.userManagement.event;

import org.springframework.context.ApplicationEvent;

import com.letsellify.logistics.components.user.core.userManagement.data.LogisticsAppUser;

import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:15:37
 */

@Getter
public class UserOfRoleVendorCreated extends ApplicationEvent {
    private final String name;
    private final String userEmail;

    public UserOfRoleVendorCreated(final LogisticsAppUser logisticsAppUser) {
        super(logisticsAppUser);
        this.name = logisticsAppUser.getName();
        this.userEmail = logisticsAppUser.getEmail();
    }

}
