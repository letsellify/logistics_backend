package com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource;


import com.letsellify.logistics.components.logistics.core.guarantorManagement.data.GuarantorCareer;
import com.letsellify.logistics.components.logistics.core.guarantorManagement.data.GuarantorRelationship;

/**
 * Author: Ahmad Buba
 * Date:8/14/25
 */


public record DispatcherGuarantorResource(
        String name,
        String state,
        String lga,
        String address,
        GuarantorRelationship relationship,
        String phoneNumber,
        String whatsAppNumber,
        String email,
        GuarantorCareer career
) {
}
