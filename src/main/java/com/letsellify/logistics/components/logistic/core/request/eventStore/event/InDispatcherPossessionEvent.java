package com.letsellify.logistics.components.logistic.core.request.eventStore.event;

import java.time.LocalDateTime;

/**
 * @author AHMAD BUBA
 * Date:2/21/25
 * Time:16:26
 */

public record InDispatcherPossessionEvent(String requestId, LocalDateTime timestamp) {}
