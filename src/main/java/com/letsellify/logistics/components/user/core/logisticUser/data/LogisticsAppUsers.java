package com.letsellify.logistics.components.user.core.logisticUser.data;

import org.springframework.data.domain.Page;

import com.letsellify.logistics.components.user.core.logisticUser.database.entity.UserEntity;
import com.letsellify.logistics.components.user.core.logisticUser.dataMapper.UserDataMapper;
import com.letsellify.logistics.components.user.core.logisticUser.rest.resource.UserResources;

import lombok.Data;

/**
 * @author AHMAD BUBA
 * Date:1/5/25
 * Time:17:32
 */

@Data
public class LogisticsAppUsers {
    final Page<UserEntity> entityListPage;

    public UserResources getResource() {
        return UserDataMapper.INSTANCE.entityListPageToResources(this.entityListPage);
    }

}
