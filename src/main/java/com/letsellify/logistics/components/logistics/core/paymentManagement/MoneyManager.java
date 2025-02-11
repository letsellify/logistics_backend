package com.letsellify.logistics.components.logistics.core.paymentManagement;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

/**
 * @author AHMAD BUBA
 * Date:1/10/25
 * Time:10:52
 */

// to be deleted soon
@Component
public class MoneyManager {
    public BigDecimal validateAndParseAmount(final String amount) {
        if (amount == null || amount.isBlank()) {
            throw new IllegalArgumentException("Amount cannot be null or blank");
        }

        if (!amount.matches("\\d+(\\.\\d{1,2})?")) {
            throw new IllegalArgumentException("Amount must be a positive number with up to two decimal places");
        }

        return new BigDecimal(amount);
    }

    public BigDecimal convertAmount(final Double amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }

        // Convert to BigDecimal and scale to 2 decimal places (common for currencies)
        return BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);
    }


}
