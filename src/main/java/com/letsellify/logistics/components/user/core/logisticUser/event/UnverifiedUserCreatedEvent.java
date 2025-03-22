package com.letsellify.logistics.components.user.core.logisticUser.event;

import org.springframework.context.ApplicationEvent;

import com.letsellify.logistics.common.data.LogisticAppRole;
import com.letsellify.logistics.components.user.core.logisticUser.data.LogisticsAppUser;

import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:2/12/25
 * Time:17:44
 */

@Getter
public class UnverifiedUserCreatedEvent extends ApplicationEvent {

    private final String email;
    private final LogisticAppRole role;

    public UnverifiedUserCreatedEvent(final LogisticsAppUser logisticsAppUser) {
        super(logisticsAppUser);
        this.email = logisticsAppUser.getEmail();
        this.role = logisticsAppUser.getRole();
    }

}
