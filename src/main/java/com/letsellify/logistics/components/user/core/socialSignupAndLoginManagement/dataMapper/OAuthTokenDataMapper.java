package com.letsellify.logistics.components.user.core.socialSignupAndLoginManagement.dataMapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.letsellify.logistics.components.user.core.socialSignupAndLoginManagement.data.OAuthToken;
import com.letsellify.logistics.components.user.core.socialSignupAndLoginManagement.rest.resource.OAuthTokenResource;

/**
 * @author AHMAD BUBA
 * Date:6/19/25
 * Time:22:05
 */

@Mapper
public interface OAuthTokenDataMapper {
    OAuthTokenDataMapper INSTANCE = Mappers.getMapper(OAuthTokenDataMapper.class);

    OAuthTokenResource toResource(OAuthToken data);
}
