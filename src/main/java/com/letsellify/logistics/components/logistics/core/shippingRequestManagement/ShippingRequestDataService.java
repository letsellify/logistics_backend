package com.letsellify.logistics.components.logistics.core.shippingRequestManagement;

import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Service;

import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.data.LogisticsRequest;
import com.letsellify.logistics.components.logistics.query.CompleteLogisticQuery;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.rest.dto.LogisticRequestDto;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.rest.resource.LogisticsRequestResource;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:13:06
 */

@Service
@RequiredArgsConstructor
public class ShippingRequestDataService {
    private final QueryGateway queryGateway;

    public LogisticsRequestResource getLogistics(final @NonNull LogisticRequestDto requestDto) {
        final CompleteLogisticQuery query = new CompleteLogisticQuery(requestDto.getShippingRequestId());
        final LogisticsRequest logisticsRequest = this.queryGateway.query(query, LogisticsRequest.class).join();
        return logisticsRequest.getResource();
    }
}
