package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.dto.SystemParamDTO;
import com.ecquaria.cloud.moh.iais.entity.SystemParam;

import java.util.List;

public interface SystemParamService {
    List<SystemParamDTO> listSystemParam();

    void updateParam(String id, String value);

    void insertRecord(SystemParam sys);
}
