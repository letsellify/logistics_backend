package com.letsellify.logistics.components.logistic.core.request.data;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:06:31
 */


@Data
@AllArgsConstructor
@NoArgsConstructor // Add this
@Embeddable // Add this if missing
public class LogisticsItem {
    private String itemName;
    private String itemDescription;
}
