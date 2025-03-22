package com.letsellify.logistics.components.logistic.core.nigerianBankAccount.database.entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:2/28/25
 * Time:01:11
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class BankEntity {
    @Id
    private int id;
    private String name;
    private String slug;
    private String code;
    private String longcode;
    private String gateway;

    private boolean payWithBank;

    private boolean active;

    private boolean isDeleted;

    private String country;
    private String currency;
    private String type;

    private Instant createdAt;

    private Instant updatedAt;


    @Override
    public boolean equals(final Object other) {
        return other instanceof BankEntity
          && ((BankEntity) other).getCode()
               .equals(this.getCode());
    }

    @Override
    public int hashCode() {
        return this.getCode().hashCode();
    }

}
