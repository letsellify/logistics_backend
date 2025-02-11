package com.letsellify.logistics.components.logistics.core.dispatcherManagement.data;

import com.letsellify.logistics.components.logistics.core.dispatcherManagement.database.entity.DispatcherEntity;

import jakarta.persistence.Transient;
import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:04:29
 */

@Data
public class LogisticsDispatcher {
    @Transient
    private final DispatcherEntity dispatcherEntity;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;

    public LogisticsDispatcher(final DispatcherEntity dispatcherEntity) {
        this.dispatcherEntity = dispatcherEntity;
        this.firstName = dispatcherEntity.getFirstName();
        this.lastName = dispatcherEntity.getLastName();
        this.email = dispatcherEntity.getEmail();
        this.phone = dispatcherEntity.getPhone();
        this.address = dispatcherEntity.getAddress();
    }
}
