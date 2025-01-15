package com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.dataMapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.data.Payment;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.database.entity.PaystackChargeSuccessWebhookEntity;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.rest.dto.ChargeSuccessPayload;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.rest.resource.PaystackPaymentInitializationResource;

/**
 * @author AHMAD BUBA
 * Date:1/9/25
 * Time:10:54
 */

@Mapper
public interface PaystackDataMapper {
    PaystackDataMapper INSTANCE = Mappers.getMapper(PaystackDataMapper.class);

    PaystackPaymentInitializationResource dataToResource(Payment payment);

    PaystackChargeSuccessWebhookEntity resourceToEntity(ChargeSuccessPayload payload);
}
