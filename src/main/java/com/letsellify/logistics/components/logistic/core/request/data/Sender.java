package com.letsellify.logistics.components.logistic.core.request.data;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @author AHMAD BUBA
 * Date:6/22/25
 * Time:21:05
 */

@Getter
@AllArgsConstructor
@Builder
public class Sender {
    private UUID senderId;
    private String email;
    private String fullName;
    private String phone;
    private String whatsAppPhone;
}
