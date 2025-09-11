package com.letsellify.logistics.components.logistics.core.dispatcherManagement.dataMapper;

import com.letsellify.logistics.components.logistics.core.dispatcherManagement.data.*;
import com.letsellify.logistics.components.logistics.core.dispatcherManagement.rest.resource.*;
import com.letsellify.logistics.components.logistics.core.logisticRequestManagement.rest.resource.DispatcherProfileInfoResources;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author AHMAD BUBA
 * Date:1/19/25
 * Time:13:04
 */

@Mapper
public interface DispatcherMapper {
    DispatcherMapper INSTANCE = Mappers.getMapper(DispatcherMapper.class);

    DispatcherProfileInfoResource getResource(DispatcherInfo data);

    LogisticDispatcherInfoResource getResource(LogisticDispatcherInfo data);

    DispatcherResource getResource(Dispatcher data);

    DispatcherProfileInfoResources getResources(DispatchersInfo data);

    DispatcherGuarantorResource getResource(DispatcherGuarantor data);

    DispatcherBusinessInformationResource getResource(DispatcherBusinessInformation data);

    DispatcherContactInformationResource getResource(DispatcherContactInformation data);

    DispatcherPersonalInformationResource getResource(DispatcherPersonalInformation data);

    DispatcherKycResource getResource(DispatcherKyc data);

    DispatcherLgaPreferenceResource dataToResource(DispatcherLgaPreference data);

    DispatcherLgaPreferenceResources dataToResources(DispatcherLgaPreferences data);

}
