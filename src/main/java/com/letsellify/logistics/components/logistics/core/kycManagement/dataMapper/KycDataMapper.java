package com.letsellify.logistics.components.logistics.core.kycManagement.dataMapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.letsellify.logistics.components.logistics.core.kycManagement.data.UserLogisticsKycCollection;
import com.letsellify.logistics.components.logistics.core.kycManagement.rest.resource.KycDocumentUploadResource;
import com.letsellify.logistics.components.logistics.core.kycManagement.rest.resource.KycResource;

/**
 * @author AHMAD BUBA
 * Date:1/11/25
 * Time:12:09
 */

@Mapper
public interface KycDataMapper {
    KycDataMapper INSTANCE = Mappers.getMapper(KycDataMapper.class);

    KycResource resource(UserLogisticsKycCollection data);

    KycDocumentUploadResource toKycDocumentUploadResource(UserLogisticsKycCollection data);

}
