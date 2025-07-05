package com.letsellify.logistics.components.logistic.core.request.data;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:6/22/25
 * Time:21:03
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class Dispatcher {
    @Column(name = "dispatcher_id")
    private UUID id;
    @Column(name = "dispatcher_name")
    private String name;
    @Column(name = "dispatcher_email")
    private String email;
    @Column(name = "dispatcher_whatsAppPhone")
    @JsonProperty("whatApp_phone_number")
    private String whatsAppPhone;
    @Column(name = "dispatcher_phone")
    private String phone;
    @Column(name = "dispatcher_state")
    private String state;
    @Column(name = "dispatcher_lga")
    private String lga;
    @Column(name = "dispatcher_address")
    private String address;
    @Column(name = "dispatcher_dispatch_identification_number")
    private String identificationNumber;
}
