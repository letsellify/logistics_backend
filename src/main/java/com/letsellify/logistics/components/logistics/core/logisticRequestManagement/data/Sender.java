package com.letsellify.logistics.components.logistics.core.logisticRequestManagement.data;

import com.letsellify.logistics.components.logistics.core.vendorManagement.data.Vendor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

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
    private String phoneNumber;
    private String whatsAppPhoneNumber;

    public Sender(Vendor vendor) {
        this.senderId = vendor.getId();
        this.email = vendor.getEmail();
        this.fullName = vendor.getName();
        this.phoneNumber = vendor.getPhoneNumber();
        this.whatsAppPhoneNumber = vendor.getWhatsAppPhoneNumber();
    }
}
