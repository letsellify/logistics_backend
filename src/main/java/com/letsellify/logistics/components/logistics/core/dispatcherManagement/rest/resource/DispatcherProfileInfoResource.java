package com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author AHMAD BUBA
 * Date:2/22/25
 * Time:20:36
 */


public record DispatcherProfileInfoResource(
        String email,
        String profilePicture,
        DispatcherPersonalInformationResource personalInformation,
        DispatcherContactInformationResource contactInformation,
        DispatcherBusinessInformationResource businessInformation,
        @JsonProperty("guarantorInformation")
        DispatcherGuarantorResource guarantor,
        DispatcherKycResource kyc,
        boolean receiveAllNotifications,
        List<DispatcherLgaPreferenceResource> preferences,
        boolean currentlyAcceptingDelivery,
        boolean approve,
        boolean profileComplete
) {
}
