package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.data;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:6/28/25
 * Time:17:40
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class Receiver {
    private String receiverFullName;

    private String receiverLocation;

    private String receiverState;

    private String receiverLga;

    private String receiverEmail;

    private String receiverCallNumber;

    private String receiverWhatsAppNumber;
}
