package com.letsellify.logistics.components.user.core.verificationCodeManagement.event;

import org.springframework.context.ApplicationEvent;

import com.letsellify.logistics.common.data.LogisticAppRole;

import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:2/12/25
 * Time:17:41
 */

@Getter
public class UserVerifiedEvent extends ApplicationEvent {

    private final String userEmail;
    private final LogisticAppRole role;

    public UserVerifiedEvent(final String userEmail, final LogisticAppRole role) {
        super(userEmail);
        this.userEmail = userEmail;
        this.role = role;
    }

}
