package com.letsellify.logistics.components.logistics.core.kyc.data;

import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:1/10/25
 * Time:12:24
 */

@Getter
public enum KycDocument {
    BVN("bvn"),
    UTILITY_BILL("utility_bill"),
    NIN("nin"),
    INTERNATIONAL_PASSPORT("international_passport"),
    DRIVER_LICENSE("driver_license");


    private final String value;

    KycDocument(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static KycDocument fromString(final String value) {
        for (final KycDocument document : KycDocument.values()) {
            if (document.value.equalsIgnoreCase(value)) {
                return document;
            }
        }
        throw new IllegalArgumentException("Unknown KYC document: " + value);
    }
}
