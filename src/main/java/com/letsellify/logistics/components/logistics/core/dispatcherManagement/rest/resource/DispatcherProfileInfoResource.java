package com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource;

import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.KycType;

/**
 * @author AHMAD BUBA
 * Date:2/22/25
 * Time:20:36
 */


public record DispatcherProfileInfoResource(
        String email,
        String name,
        String whatsAppPhone,
        String phone,
        String state,
        String lga,
        String address,
        String businessName,
        String businessOfficeAddress,
        String businessState,
        String businessLga,
        String guarantorName,
        String guarantorState,
        String guarantorLga,
        String guarantorAddress,
        String guarantorRelationship,
        String guarantorPhoneNumber,
        String guarantorWhatsAppNumber,
        String guarantorEmail,
        String guarantorCareer,
        KycType kycType,
        String kycNumber
) {
}
