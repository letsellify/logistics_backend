package com.letsellify.logistics.components.logistic.core.request.data;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:6/22/25
 * Time:21:13
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class Agent {
    @Column(name = "agent_id")
    private UUID id;
    @Column(name = "agent_email")
    private String email;
    @Column(name = "agent_name")
    private String name;
    @Column(name = "agent_home_state")
    private String homeState;
    @Column(name = "agent_home_lga")
    private String homeLga;
    @Column(name = "agent_home_address")
    private String homeAddress;
    @Column(name = "agent_whatsAppPhone")
    private String whatsAppPhone;
    @Column(name = "agent_phone")
    private String phone;
    @Column(name = "store_state")
    private String storeState;
    @Column(name = "store_lga")
    private String storeLga;
    @Column(name = "store_address")
    private String storeAddress;
    @Column(name = "agent_accepting_storage")
    private boolean acceptingStorage;
    @Column(name = "agent_kyc_id")
    private UUID kycId;
}
