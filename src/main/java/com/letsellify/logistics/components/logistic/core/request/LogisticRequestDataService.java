package com.letsellify.logistics.components.logistic.core.request;

import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Service;

import com.letsellify.logistics.components.logistic.core.request.data.LogisticsRequest;
import com.letsellify.logistics.components.logistic.core.request.eventStore.query.CompleteLogisticQuery;
import com.letsellify.logistics.components.logistic.core.request.rest.dto.LogisticRequestDto;
import com.letsellify.logistics.components.logistic.core.request.rest.resource.LogisticRequestResource;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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

    public LogisticRequestResource getLogistics(final @NonNull LogisticRequestDto requestDto) {
        final CompleteLogisticQuery query = new CompleteLogisticQuery(requestDto.getShippingRequestId());
        final LogisticsRequest logisticsRequest = this.queryGateway.query(query, LogisticsRequest.class).join();
        return logisticsRequest.getResource();
    }

}
