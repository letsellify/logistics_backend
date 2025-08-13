package com.letsellify.logistics.components.user.core.userManagement;

import com.letsellify.logistics.common.restException.LogisticsBadRequestException;
import com.letsellify.logistics.common.restException.LogisticsResourceNotFoundException;
import com.letsellify.logistics.components.logistics.core.agentManagement.exception.AgentApprovedException;
import com.letsellify.logistics.components.logistics.core.agentManagement.exception.NoSuchAgentException;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource.AgentInfoResource;
import com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource.AgentResource;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.exception.DispatcherApprovedException;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.exception.InCompleteDispatcherProfileException;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.exception.NoSuchDispatcherException;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.DispatcherResource;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.LogisticDispatcherInfoResource;
import com.letsellify.logistics.components.logistics.core.kycManagement.exception.NoKycRecordFoundException;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource.DispatcherProfileInfoResources;
import com.letsellify.logistics.components.user.core.userManagement.exception.UserNotFoundException;
import com.letsellify.logistics.components.user.core.userManagement.rest.resource.UserResource;
import com.letsellify.logistics.components.user.core.userManagement.rest.resource.UserResources;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author AHMAD BUBA
 * Date:2/22/25
 * Time:16:39
 */

@Service
@RequiredArgsConstructor
public class AdminUserDataService {
    private final AdminUserManager adminUserManager;

    public LogisticDispatcherInfoResource viewDispatcherPersonalInfo(final @NonNull String dispatcherEmail) {
        try {
            return this.adminUserManager.viewDispatcherPersonalInfo(dispatcherEmail)
                    .getResource();
        } catch (final NoKycRecordFoundException | NoSuchDispatcherException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        }
    }

    public DispatcherResource approveDispatcher(final @NonNull String dispatcherEmail) {
        try {
            return this.adminUserManager.approveDispatcher(dispatcherEmail)
                    .getResource();
        } catch (final NoSuchDispatcherException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        } catch (InCompleteDispatcherProfileException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        } catch (DispatcherApprovedException e) {
            throw new RuntimeException(e);
        }
    }


    public AgentInfoResource viewAgentPersonalInfo(final @NonNull String dispatcherEmail) {
        try {
            return this.adminUserManager.viewAgentPersonalInfo(dispatcherEmail)
                    .getResource();
        } catch (final NoKycRecordFoundException | NoSuchAgentException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        }
    }

    public DispatcherProfileInfoResources getAllDispatcherAwaitingApproval(final @NonNull Pageable pageable) {
        return this.adminUserManager.getAllDispatchersAwaitingApproval(pageable).getResource();
    }

    public AgentResource approveAgent(final @NonNull String dispatcherEmail) {
        try {
            return this.adminUserManager.approveAgent(dispatcherEmail)
                    .getResource();
        } catch (final NoSuchAgentException | NoKycRecordFoundException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        } catch (final AgentApprovedException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        }
    }


    public UserResource getUser(final @NonNull String userEmail) {
        try {
            return this.adminUserManager.getUser(userEmail)
                    .getResource();
        } catch (final UserNotFoundException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        }
    }

    public UserResources getAllUsers(final @NonNull Pageable page) {
        return this.adminUserManager.getAllUsers(page)
                .getResource();
    }

}
