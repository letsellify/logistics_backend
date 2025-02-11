package com.letsellify.logistics.components.logistics.core.nigeriaStateLGA.database.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.letsellify.logistics.components.logistics.core.nigeriaStateLGA.database.entity.LGAEntity;
import com.letsellify.logistics.components.logistics.core.nigeriaStateLGA.database.entity.StateEntity;

/**
 * @author AHMAD BUBA
 * Date:1/17/25
 * Time:11:39
 */

@Repository
public interface LGARepository extends JpaRepository<LGAEntity,Long> {
    boolean existsByName(String name);

    List<LGAEntity> findByState(StateEntity state);

}
