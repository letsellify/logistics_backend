package com.letsellify.logistics.components.logistic.core.vendor.rest.dto;

import java.util.List;

import lombok.Data;

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
