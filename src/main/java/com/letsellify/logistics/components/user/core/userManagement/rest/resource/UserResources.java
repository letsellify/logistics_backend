package com.letsellify.logistics.components.user.core.userManagement.rest.resource;

import java.util.List;

import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */


public record UserResources(
        // takes a page of user entity
        List<UserResource> users,
        int currentPage,
        int totalPages,
        long totalElements,
        boolean isPageFirst,
        boolean isPageLast,
        boolean isPageEmpty,
        boolean hasNext
) {

}
