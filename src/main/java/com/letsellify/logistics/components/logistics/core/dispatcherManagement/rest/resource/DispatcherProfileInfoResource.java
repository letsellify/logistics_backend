package com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource;

import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.*;

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
        DispatcherGuarantorResource guarantor,
        DispatcherKycResource kyc
) {
}
