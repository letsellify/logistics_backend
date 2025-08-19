package com.letsellify.logistics.components.logistics.core.agentManagement.rest.resource;

import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.*;

/**
 * @author AHMAD BUBA
 * Date:2/26/25
 * Time:13:51
 */

public record AgentProfileInfoResource(
        String email,
        String profilePicture,
        AgentPersonalInformationResource personalInformation,
        AgentContactInformationResource contactInformation,
        AgentBusinessInformationResource businessInformation,
        AgentGuarantorResource guarantor,
        AgentKycResource kyc
) {
}
