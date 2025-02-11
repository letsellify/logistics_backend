package com.letsellify.logistics.components.logistics.core.shippingRequestManagement.data;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/20/25
 * Time:06:40
 */

@Data
@AllArgsConstructor
public class LogisticsVendor {
    private String vendorEmail;
    private String vendorFirstName;
    private String vendorLastName;
    private String vendorPhone;
}
