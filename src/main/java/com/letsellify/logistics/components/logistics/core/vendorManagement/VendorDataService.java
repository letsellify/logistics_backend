package com.letsellify.logistics.components.logistics.core.vendorManagement;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.letsellify.logistics.common.restException.LogisticsResourceNotFoundException;
import com.letsellify.logistics.components.logistics.core.paymentManagement.exception.LogisticsInsufficientFundsException;
import com.letsellify.logistics.components.logistics.core.vendorManagement.exception.VendorNotFoundException;
import com.letsellify.logistics.components.logistics.core.vendorManagement.rest.dto.OrderDto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:12:56
 */

@Service
@RequiredArgsConstructor
public class VendorDataService {
    private final VendorManager management;

    // use dataMapper here and return orderResource
    public void order(final Authentication authentication, final @NonNull OrderDto order) throws LogisticsInsufficientFundsException {
        try {
            this.management.order(
              authentication.getName(),
              order.getItem().getName(),
              order.getItem().getDescription(),
              order.getAmountForShipping(),
              order.getAmountForStorage(),
              order.getModeOfPayment(),
              order.getItem()
                   .getImages(),
              order.getCurrentState(),
              order.getCurrentLga(),
              order.getShippingState(),
              order.getShippingLga(),
              order.getStartDate(),
              order.getEndDate()
            );
        }
        catch (final VendorNotFoundException e) {
            throw new LogisticsResourceNotFoundException(e.getMessage());
        }
    }
}
