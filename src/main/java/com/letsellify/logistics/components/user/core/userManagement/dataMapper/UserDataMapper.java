package com.letsellify.logistics.components.user.core.userManagement.dataMapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import com.letsellify.logistics.components.user.core.userManagement.database.entity.UserEntity;
import com.letsellify.logistics.components.user.core.userManagement.rest.resource.UserResource;
import com.letsellify.logistics.components.user.core.userManagement.rest.resource.UserResources;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@Mapper
public interface UserDataMapper {
    UserDataMapper INSTANCE = Mappers.getMapper(UserDataMapper.class);

    UserResource entityToResource(UserEntity entity);

    default UserResources entityListPageToResources(final Page<UserEntity> entityListPage) {
        final List<UserResource> resources = new ArrayList<>();
        final List<UserEntity> entityList = entityListPage.getContent().stream().toList();
        for (final UserEntity entity : entityList) {
            final UserResource userResource = this.entityToResource(entity);
            resources.add(userResource);
        }

        final UserResources resourcePage = new UserResources();
        resourcePage.setUsers(resources);
        resourcePage.setSize(entityListPage.getSize());
        resourcePage.setNumber(entityListPage.getNumber());
        resourcePage.setTotalElements(entityListPage.getTotalElements());
        resourcePage.setTotalPages(entityListPage.getTotalPages());
        return resourcePage;
    }

}
