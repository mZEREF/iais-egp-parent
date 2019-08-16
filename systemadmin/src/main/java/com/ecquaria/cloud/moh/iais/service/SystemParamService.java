package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.entity.SystemParam;

import java.util.List;

public interface SystemParamService {

    List<SystemParam> listSystemParam();

    void updateValueByGuid(String id, String value);

    void insertRecord(SystemParam sys);
}
