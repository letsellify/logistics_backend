package com.letsellify.logistics.components.logistic.core.kyc.dataMapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.letsellify.logistics.components.logistic.core.kyc.data.UserLogisticsKycCollection;
import com.letsellify.logistics.components.logistic.core.kyc.rest.resource.KycDocumentUploadResource;
import com.letsellify.logistics.components.logistic.core.kyc.rest.resource.KycResource;

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
