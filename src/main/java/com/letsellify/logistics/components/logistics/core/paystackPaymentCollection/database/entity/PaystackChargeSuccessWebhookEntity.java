package com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.database.entity;

import com.letsellify.logistics.components.logistics.core.auditing.database.entity.Auditable;
import com.letsellify.logistics.components.logistics.core.paystackPaymentCollection.rest.dto.ChargeSuccessPayload;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author AHMAD BUBA
 * Date:1/9/25
 * Time:11:43
 */

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class PaystackChargeSuccessWebhookEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String event;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "data_payload_id")
    private PaystackChargeSuccessDataEntity data;

    // No-arg constructor for JPA
    public PaystackChargeSuccessWebhookEntity() {
    }

    public PaystackChargeSuccessWebhookEntity(final ChargeSuccessPayload payload) {
        this.event = payload.getEvent();
        this.data = new PaystackChargeSuccessDataEntity(payload.getData()); // Deep copy of DataPayload
    }

}
