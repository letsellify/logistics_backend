package com.letsellify.logistics.components.logistic.core.nigerianBankAccount.database.entity;

import java.util.UUID;

import com.letsellify.logistics.common.entityAudit.entity.Auditable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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
