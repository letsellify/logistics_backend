package com.letsellify.logistics.components.user.core.logisticUser.rest.resource;

import java.util.List;

import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@Data
public class UserResources {
    // takes a page of user entity
    private List<UserResource> users;
    private int size;
    private int number;
    private long totalElements;
    private int totalPages;
}
