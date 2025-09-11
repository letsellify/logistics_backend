package com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.dataMapper;

import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.data.PaystackPayment;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.database.entity.PaystackChargeSuccessWebhookEntity;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.rest.dto.ChargeSuccessPayload;
import com.letsellify.logistics.components.logistics.core.paystackPaymentGateway.rest.resource.PaystackPaymentInitializationResource;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author AHMAD BUBA
 * Date:1/9/25
 * Time:10:54
 */

@Mapper
public interface PaystackDataMapper {
    PaystackDataMapper INSTANCE = Mappers.getMapper(PaystackDataMapper.class);

    PaystackPaymentInitializationResource dataToResource(PaystackPayment payment);

    PaystackChargeSuccessWebhookEntity resourceToEntity(ChargeSuccessPayload payload);

}
