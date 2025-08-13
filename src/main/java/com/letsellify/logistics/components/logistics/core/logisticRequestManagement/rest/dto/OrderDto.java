package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.dto;

import com.letsellify.logistics.components.logistics.core.vendorManagement.rest.dto.GoodInformation;
import com.letsellify.logistics.components.logistics.core.vendorManagement.rest.dto.OtherDetail;
import com.letsellify.logistics.components.logistics.core.vendorManagement.rest.dto.ReceiverInfoDto;
import jakarta.validation.constraints.NotNull;

/**
 * @author AHMAD BUBA
 * Date:2/20/25
 * Time:11:57
 */

public record OrderDto(
        @NotNull
        GoodInformation goodInformation,
        @NotNull
        ReceiverInfoDto receiverInformation,
        @NotNull
        OtherDetail otherDetails
) {

}
