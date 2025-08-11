package com.letsellify.logistics.components.user.core.socialSignupAndLoginManagement.data;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:2/14/25
 * Time:15:34
 */

@Getter
public class LogisticOAuth2User implements OAuth2User {
    private final OAuth2User oAuth2User;
    private final String provider;

    public LogisticOAuth2User(final OAuth2User oAuth2User, final String provider) {
        this.oAuth2User = oAuth2User;
        this.provider = provider;
    }

    @Override
    public String getName() {
        return this.oAuth2User.getAttribute("name");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.oAuth2User.getAuthorities();
    }

    public String getEmail() {
        return this.oAuth2User.getAttribute("email");
    }

}
