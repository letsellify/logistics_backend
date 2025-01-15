package com.letsellify.logistics.components.logistics.core.financeAccount;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.letsellify.logistics.components.logistics.core.financeAccount.data.LogisticsAccount;
import com.letsellify.logistics.components.logistics.core.financeAccount.database.entity.LogisticsAccountEntity;
import com.letsellify.logistics.components.logistics.core.financeAccount.database.repository.LogisticsAccountRepository;
import com.letsellify.logistics.components.logistics.core.user.UserManager;
import com.letsellify.logistics.components.logistics.core.user.data.LogisticsAppUser;
import com.letsellify.logistics.components.logistics.core.user.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author AHMAD BUBA
 * Date:1/8/25
 * Time:22:47
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class AccountManager {
    private final LogisticsAccountRepository accountRepository;
    private final UserManager userManager;

    @Transactional
    public LogisticsAccount topUpAccount(final UUID userId, final BigDecimal amount) {
        final LogisticsAccountEntity userAccountEntity = this.accountRepository.findByUserId(userId)
                                                                                    .orElseThrow();
        final LogisticsAccountEntity modifiedAccountEntity = this.credit(userAccountEntity, amount);
        return new LogisticsAccount(modifiedAccountEntity);
    }

    @Transactional
    public LogisticsAccount chargeAccount(final UUID userId, final BigDecimal amount) {
        final LogisticsAccountEntity userAccountEntity = this.accountRepository.findByUserId(userId)
                                                                               .orElseThrow();
        final LogisticsAccountEntity modifiedAccountEntity = this.debit(userAccountEntity, amount);
        return new LogisticsAccount(modifiedAccountEntity);

    }


    public LogisticsAccount getAccount(final String username) throws UserNotFoundException {
        final LogisticsAppUser user = this.userManager.getUserByEmail(username);
        final LogisticsAccountEntity entity = this.accountRepository.findByUserId(user.getId())
                                                                    .orElseThrow();
        return new LogisticsAccount(entity);
    }

    public LogisticsAccount getAccountByUserId(final UUID userId) {
        final LogisticsAccountEntity entity = this.accountRepository.findByUserId(userId)
                                                                    .orElseThrow();
        return new LogisticsAccount(entity);
    }

    public BigDecimal getBalance(final UUID userId) {
        final LogisticsAccountEntity entity = this.accountRepository.findByUserId(userId)
                                                                    .orElseThrow();
        return entity.getBalance();
    }



    private LogisticsAccountEntity credit(final LogisticsAccountEntity entity, final BigDecimal amount) {
        entity.setBalance(entity.getBalance().add(amount));
        this.accountRepository.save(entity);
        return entity;
    }

    private LogisticsAccountEntity debit(final LogisticsAccountEntity entity, final BigDecimal amount) {
        entity.setBalance(entity.getBalance().subtract(amount));
        this.accountRepository.save(entity);
        return entity;
    }

}
