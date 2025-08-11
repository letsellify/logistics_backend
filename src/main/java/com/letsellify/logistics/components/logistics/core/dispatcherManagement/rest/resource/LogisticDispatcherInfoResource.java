package com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource;

import com.letsellify.logistics.components.logistics.core.kycManagement.data.KycDocumentType;

import java.util.Map;

/**
 * @author AHMAD BUBA
 * Date:2/23/25
 * Time:10:54
 */

public record LogisticDispatcherInfoResource(
        String whatsAppPhone,
        String phone,
        String state,
        String lga,
        String address,
        Map<KycDocumentType, String> kycs
) {
}
