package com.letsellify.logistics.components.user.core.logisticUser.database.entity;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.NaturalId;

import com.letsellify.logistics.common.entityAudit.entity.Auditable;
import com.letsellify.logistics.common.data.LogisticAppRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@Entity
@Getter
public class UserEntity extends Auditable {

    @Id
    private UUID id;

    private String name;

    @Column(unique = true)
    @NaturalId
    private String email;

    private String password;

    @Setter
    @Enumerated(EnumType.STRING)
    private LogisticAppRole role;

    private boolean active;

    private String provider;

    protected UserEntity() {
        super();
    }

    public static UserEntity create(final String name, final String email, final String password, final LogisticAppRole role, final boolean enabled, final String provider) {
        Objects.requireNonNull(email, "Email cannot be null");
        final UserEntity userEntity = new UserEntity();
        userEntity.id = UUID.randomUUID();
        userEntity.name = name;
        userEntity.email = email;
        userEntity.password = password;
        userEntity.role = role;
        userEntity.active = enabled;
        return userEntity;
    }

    public void updateName(final String name) {
        this.name = name;
    }

    public void updateEmail(final @NonNull String email) {
       this.email = email;
    }

    public void activate() {
        this.active = true;
    }

    public void updatePassword(final String encodedPassword) {
        this.password = encodedPassword;
    }


    public String getCreatedBy() {
        return super.getCreatedBy();
    }

    public String getLastModifiedBy() {
        return super.getLastModifiedBy();
    }

    public Instant getCreationDate() {
        return super.getCreationDate();
    }

    public Instant getLastModifiedDate() {
        return super.getLastModifiedDate();
    }

    @Override
    public boolean equals(final Object other) {
        return other instanceof UserEntity                   // check type with instanceof, not getClass()
               && ((UserEntity) other).getEmail()
                                      .equals(this.getEmail());
        // compare natural ids
    }

    @Override
    public int hashCode() {
        return this.email.hashCode();
    }
}