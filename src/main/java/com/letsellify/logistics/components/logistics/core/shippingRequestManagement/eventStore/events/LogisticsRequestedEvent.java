package com.letsellify.logistics.components.logistics.core.shippingRequestManagement.eventStore.events;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.letsellify.logistics.components.logistics.core.paymentManagement.data.PaymentMethod;
import com.letsellify.logistics.components.logistics.core.shippingRequestManagement.data.LogisticsItemImage;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:10:17
 */


public record LogisticsRequestedEvent(
  String requestId,
  String vendorEmail,
  String vendorFirstName,
  String vendorLastName,
  String vendorPhone,
  String itemName,
  String description,
  BigDecimal amountForShipping,
  BigDecimal amountForStorage,
  BigDecimal totalAmountAfterTax,
  PaymentMethod modeOfPayment,
  List<LogisticsItemImage> images,
  String currentState,
  String currentLga,
  String shippingState,
  String shippingLga,
  LocalDate possibleDeliveryDateStart,
  LocalDate possibleDeliveryDateEnd
) {}
