package com.letsellify.logistics.components.logistic.core.vendor.rest.dto;

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
  ReceiverInformation receiverInformation,
  @NotNull
  OtherDetail otherDetails
) {

}
