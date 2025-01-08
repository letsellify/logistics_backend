package com.letsellify.logistics.components.security.core.tokenManagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.letsellify.logistics.components.security.core.tokenManagement.data.LogisticsAppSecurityToken;
import com.letsellify.logistics.components.security.core.tokenManagement.rest.resource.TokenResource;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@Mapper
public interface TokenDataMapper {
    TokenDataMapper INSTANCE = Mappers.getMapper(TokenDataMapper.class);

    TokenResource dataToResource(LogisticsAppSecurityToken data);
}
