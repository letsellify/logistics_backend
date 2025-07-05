package com.letsellify.logistics.components.logistic.core.request.eventStore.event;

import java.util.UUID;

/**
 * @author AHMAD BUBA
 * Date:2/7/25
 * Time:07:04
 */

public record StorageAcceptedEvent(
  String requestId,
  UUID agentId
) {}
