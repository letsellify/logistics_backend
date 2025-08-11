package com.letsellify.logistics.components.logistics.core.nigerianBankAccountManagement.exception;

import com.letsellify.logistics.common.exception.LogisticsException;

/**
 * @author AHMAD BUBA
 * Date:3/1/25
 * Time:12:43
 */

public class BankAccountExistException extends LogisticsException {

    public BankAccountExistException(final String msg) {
        super(msg);
    }

}
