package com.letsellify.logistics.common.security.data;

import java.io.Serial;
import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.letsellify.logistics.components.user.core.userManagement.data.LogisticsAppUser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@RequiredArgsConstructor
@Getter
public class LogisticsAppSecurityUser implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;
    private final LogisticsAppUser user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.user.getRole() == null) {
            return Set.of(); // No authorities if no role
        }
        return Set.of(new SimpleGrantedAuthority("ROLE_" + this.user.getRole()));
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.user.isActive();
    }
}
