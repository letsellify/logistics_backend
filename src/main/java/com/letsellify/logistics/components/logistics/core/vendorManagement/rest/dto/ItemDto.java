package com.letsellify.logistics.components.logistics.core.vendorManagement.rest.dto;

import lombok.Data;

import java.util.List;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:06:46
 */

@Data
public class ItemDto {
    private String name;
    private String description;
    private List<String> images;
}
