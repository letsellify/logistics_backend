package com.letsellify.logistics.components.user.core.verificationCodeManagement;

import org.springframework.stereotype.Service;

import com.letsellify.logistics.common.restException.LogisticsBadRequestException;
import com.letsellify.logistics.common.restException.LogisticsInternalServerErrorException;
import com.letsellify.logistics.common.restException.LogisticsResourceNotFoundException;
import com.letsellify.logistics.components.user.core.userManagement.exception.UserNotFoundException;
import com.letsellify.logistics.components.user.core.verificationCodeManagement.exception.InvalidVerificationCodeException;
import com.letsellify.logistics.components.user.core.verificationCodeManagement.exception.UnableToCreateVerificationCodeException;
import com.letsellify.logistics.components.user.core.verificationCodeManagement.rest.dto.ResendVerificationCodeDto;
import com.letsellify.logistics.components.user.core.verificationCodeManagement.rest.dto.VerificationCodeDto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:2/10/25
 * Time:14:09
 */

@Service
@RequiredArgsConstructor
public class VerificationCodeDataService {
    private final VerificationCodeManager verificationCodeManager;

    public void resendVerificationCode(final @NonNull ResendVerificationCodeDto dto) {
        try {
            this.verificationCodeManager.resendVerificationCode(dto.email());
        }
        catch (final UserNotFoundException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
        catch (final UnableToCreateVerificationCodeException e) {
            throw new LogisticsInternalServerErrorException("Unable to generate verification code");
        }
    }

    public void verifyCode(final @NonNull VerificationCodeDto dto) {
        try {
            this.verificationCodeManager.verifyCode(dto.email(), dto.code());
        }
        catch (final InvalidVerificationCodeException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        }
    }

}
