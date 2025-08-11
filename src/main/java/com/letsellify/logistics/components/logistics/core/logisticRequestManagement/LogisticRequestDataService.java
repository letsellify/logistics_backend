package com.letsellify.logistics.components.logistics.core.logisticRequestManagement;

import com.letsellify.logistics.common.restException.LogisticsBadRequestException;
import com.letsellify.logistics.common.restException.LogisticsRestException;
import com.letsellify.logistics.components.logistics.core.agentManagement.exception.NoSuchAgentException;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.exception.NoSuchDispatcherException;
import com.letsellify.logistics.components.logistics.core.financeAccountManagement.exception.InsufficientFundsException;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.exception.IllegalLGAException;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.exception.NoSuchStateException;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.data.LogisticRequest;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.eventStore.query.CompleteLogisticQuery;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.exception.*;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.dto.LogisticRequestDto;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource.LogisticItemImageResource;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource.FullLogisticRequestResource;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource.LogisticRequestResources;
import com.letsellify.logistics.components.logistics.core.vendorManagement.exception.InCompleteVendorProfileException;
import com.letsellify.logistics.components.logistics.core.vendorManagement.exception.VendorNotFoundException;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.dto.OrderDto;
import com.letsellify.logistics.common.util.SecurityUserRoleExtractorHandler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:13:06
 */

@Service
@RequiredArgsConstructor
public class LogisticRequestDataService {
    private final QueryGateway queryGateway;
    private final LogisticRequestManager logisticRequestManager;

    public LogisticItemImageResource uploadLogisticItemImage(final @NonNull Authentication authentication, final @NonNull MultipartFile file) {
        try {
            return this.logisticRequestManager.uploadLogisticsItemImage(authentication.getName(),SecurityUserRoleExtractorHandler.extractUserRole(authentication),file)
                    .getResource();

        } catch (InCompleteVendorProfileException e) {
            throw new RuntimeException(e);
        } catch (VendorNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvalidRoleException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<String> order(final @NonNull Authentication authentication, final @NonNull OrderDto orderDto) {
        try {
            return this.logisticRequestManager.order(
                    authentication.getName(),
                    SecurityUserRoleExtractorHandler.extractUserRole(authentication),
                    orderDto.goodInformation().itemName(),
                    orderDto.goodInformation().quantity(),
                    orderDto.goodInformation().description(),
                    orderDto.goodInformation().fragility(),
                    orderDto.goodInformation().condition(),
                    orderDto.goodInformation().weight(),
                    orderDto.goodInformation().imageUrl1(),
                    orderDto.goodInformation().imageUrl2(),
                    orderDto.receiverInformation().fullName(),
                    orderDto.receiverInformation().location(),
                    orderDto.receiverInformation().state(),
                    orderDto.receiverInformation().lga(),
                    orderDto.receiverInformation().email(),
                    orderDto.receiverInformation().callPhoneNumber(),
                    orderDto.receiverInformation().whatsAppPhoneNumber(),
                    orderDto.otherDetails()
                            .agentPay(),
                    orderDto.otherDetails()
                            .dispatcherPay(),
                    orderDto.otherDetails()
                            .dispatcherPickUpDate(),
                    orderDto.otherDetails()
                            .dispatcherDeliveryDate(),
                    orderDto.otherDetails().pickUpState(),
                    orderDto.otherDetails().pickUpLga(),
                    orderDto.otherDetails().pickUpAddress()
            );
        } catch (VendorNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvalidLogisticItemImageException e) {
            throw new RuntimeException(e);
        } catch (InCompleteVendorProfileException e) {
            throw new RuntimeException(e);
        } catch (InsufficientFundsException e) {
            throw new RuntimeException(e);
        } catch (ImageConflictException e) {
            throw new RuntimeException(e);
        } catch (InvalidRoleException e) {
            throw new RuntimeException(e);
        } catch (NoSuchStateException e) {
            throw new RuntimeException(e);
        } catch (IllegalLGAException e) {
            throw new RuntimeException(e);
        }
    }

    public FullLogisticRequestResource getLogistics(final @NonNull LogisticRequestDto requestDto) {
        final CompleteLogisticQuery query = new CompleteLogisticQuery(requestDto.getShippingRequestId());
        final LogisticRequest logisticsRequest = this.queryGateway.query(query, LogisticRequest.class).join();
        return logisticsRequest.getResource();
    }

//    public LogisticRequestResource getLogisticRequests(Authentication authentication, Pageable pageable) {
//        try {
//            return this.logisticRequestManager.getVendorLogisticRequest(Sec);
//        }
//    }

    public FullLogisticRequestResource getLogisticRequest(final @NonNull Authentication authentication, final @NonNull String shippingRequestId) {
        try {
            System.out.println("The role of the user is " + SecurityUserRoleExtractorHandler.extractUserRole(authentication));
            return this.logisticRequestManager.getLogisticRequest(authentication.getName(), SecurityUserRoleExtractorHandler.extractUserRole(authentication), shippingRequestId)
                    .getResource();
        } catch (NoSuchLogisticRequestException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAgentException e) {
            throw new RuntimeException(e);
        } catch (VendorNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchDispatcherException e) {
            throw new RuntimeException(e);
        } catch (LogisticRequestAccessDeniedException e) {
            throw new RuntimeException(e);
        } catch (InvalidRoleException e) {
            throw new RuntimeException(e);
        }
    }

    // work on this
    public LogisticRequestResources getLogisticRequests(final @NonNull Authentication authentication, final @NonNull Pageable pageable) {
        try {
            return this.logisticRequestManager.getLogisticRequests(authentication.getName(), SecurityUserRoleExtractorHandler.extractUserRole(authentication) ,pageable).getResource();
        } catch (VendorNotFoundException e) {
            throw new LogisticsBadRequestException(e.getMessage());
        } catch (InCompleteVendorProfileException e) {
            throw new LogisticsRestException(HttpStatus.FORBIDDEN, "403", e.getMessage());
        } catch (NoSuchAgentException e) {
            throw new RuntimeException(e);
        } catch (InvalidRoleException e) {
            throw new RuntimeException(e);
        } catch (NoSuchDispatcherException e) {
            throw new RuntimeException(e);
        }
    }

//    public LogisticRequestResource getLogisticRequests(Authentication authentication) {
//        try {
//            return this.logisticRequestManager.getLogisticRequest();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
