package com.letsellify.logistics.components.user.core.userManagement.event;

import org.springframework.context.ApplicationEvent;

import com.letsellify.logistics.components.user.core.userManagement.data.LogisticsAppUser;

import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:15:38
 */

@Getter
public class UserOfRoleAgentCreated extends ApplicationEvent {
    private final String name;
    private final String userEmail;

    public UserOfRoleAgentCreated(final LogisticsAppUser logisticsAppUser) {
        super(logisticsAppUser);
        this.name = logisticsAppUser.getName();
        this.userEmail = logisticsAppUser.getEmail();
    }

}
