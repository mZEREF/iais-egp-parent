package com.ecquaria.cloud.moh.iais.dao;


import com.ecquaria.cloud.moh.iais.dto.MasterCodeDto;
import com.ecquaria.cloud.moh.iais.entity.MasterCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;


@Repository
public interface MasterCodeRepository extends JpaRepository<MasterCode,Long>,
        JpaSpecificationExecutor<MasterCode>,
        Serializable {
    @Query(value = "select * from cm_master_code where master_code_id = :master_code_key",nativeQuery = true)
    List<MasterCode> findMasterCodeByMasterCodeId(@Param("master_code_key") int masterCodeKey);

    @Query(value = "select * from cm_master_code where rowguid = :rowguid",nativeQuery = true)
    MasterCode findMasterCodeByRowguid(@Param("rowguid")String rowguid);
}
