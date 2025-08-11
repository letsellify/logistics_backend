package com.letsellify.logistics.components.logistics.core.kycManagement.data;

import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:1/10/25
 * Time:12:24
 */

@Getter
public enum KycDocumentType {
    BVN("bvn"),
    UTILITY_BILL("utility_bill"),
    NIN("nin"),
    INTERNATIONAL_PASSPORT("international_passport"),
    DRIVER_LICENSE("driver_license");


    private final String value;

    KycDocumentType(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static KycDocumentType fromString(final String value) {
        for (final KycDocumentType document : KycDocumentType.values()) {
            if (document.value.equalsIgnoreCase(value)) {
                return document;
            }
        }
        throw new IllegalArgumentException("Unknown KYC document: " + value);
    }
}
