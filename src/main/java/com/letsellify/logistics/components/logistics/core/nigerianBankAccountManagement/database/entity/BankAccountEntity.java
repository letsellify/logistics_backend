package com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.database.entity;

import com.letsellify.logistics.common.entityAudit.entity.Auditable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:3/1/25
 * Time:12:27
 */

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BankAccountEntity extends Auditable {
    @Id
    private UUID id;

    private String username;

    private String accountNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bank_id", referencedColumnName = "id")
    private BankEntity bank;

    public BankAccountEntity(final @NonNull String username, final @NonNull String accountNumber, final @NonNull BankEntity bank) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.accountNumber = accountNumber;
        this.bank = bank;
    }

    public void update(final @NonNull String accountNumber, final BankEntity bankEntity) {
        this.accountNumber = accountNumber;
        this.bank = bankEntity;
    }

}
