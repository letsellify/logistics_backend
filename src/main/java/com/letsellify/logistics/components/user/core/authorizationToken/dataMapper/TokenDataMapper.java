package com.letsellify.logistics.components.user.core.authorizationToken.dataMapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.letsellify.logistics.components.user.core.authorizationToken.data.LogisticsAppSecurityToken;
import com.letsellify.logistics.components.user.core.authorizationToken.rest.resource.TokenResource;

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
