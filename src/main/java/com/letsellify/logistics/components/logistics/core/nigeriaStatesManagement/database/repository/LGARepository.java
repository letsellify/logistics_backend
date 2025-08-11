package com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.database.repository;

import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.database.entity.LGAEntity;
import com.letsellify.logistics.components.logistics.core.nigeriaStatesManagement.database.entity.StateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author AHMAD BUBA
 * Date:1/17/25
 * Time:11:39
 */

@Repository
public interface LGARepository extends JpaRepository<LGAEntity, Long> {
    boolean existsByName(String name);

    List<LGAEntity> findByState(StateEntity state);

}
