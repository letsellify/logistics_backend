package com.letsellify.logistics.components.logistic.core.vendor.dataMapper;

import com.letsellify.logistics.components.logistic.core.vendor.data.VendorBusinessInformation;
import com.letsellify.logistics.components.logistic.core.vendor.data.VendorContactInformation;
import com.letsellify.logistics.components.logistic.core.vendor.data.VendorInformation;
import com.letsellify.logistics.components.logistic.core.vendor.data.VendorPersonalInformation;
import com.letsellify.logistics.components.logistic.core.vendor.rest.resource.VendorBusinessInformationResource;
import com.letsellify.logistics.components.logistic.core.vendor.rest.resource.VendorContactInformationResource;
import com.letsellify.logistics.components.logistic.core.vendor.rest.resource.VendorInformationResource;
import com.letsellify.logistics.components.logistic.core.vendor.rest.resource.VendorPersonalInformationResource;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:12:54
 */

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface VendorDataMapper {
    VendorDataMapper INSTANCE = Mappers.getMapper(VendorDataMapper.class);

    VendorPersonalInformationResource toResource(VendorPersonalInformation vendorPersonalInformation);
    VendorContactInformationResource toResource(VendorContactInformation vendorContactInformation);
    VendorBusinessInformationResource toResource(VendorBusinessInformation vendorBusinessInformation);
    VendorInformationResource toResource(VendorInformation vendorInformation);
}
