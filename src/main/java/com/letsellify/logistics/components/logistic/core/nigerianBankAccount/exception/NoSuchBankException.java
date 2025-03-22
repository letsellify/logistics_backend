package com.letsellify.logistics.components.logistic.core.nigerianBankAccount.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:3/1/25
 * Time:12:26
 */

public class NoSuchBankException extends LogisticsException {

    public NoSuchBankException(final String msg) {
        super(msg);
    }

}
