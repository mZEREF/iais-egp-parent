package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.dao.SystemParamDAO;
import com.ecquaria.cloud.moh.iais.entity.SystemParam;
import com.ecquaria.cloud.moh.iais.service.SystemParamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@EnableTransactionManagement
@Slf4j
public class SystemParamServiceImpl implements SystemParamService {

    @Autowired
    @Qualifier(value = "systemParamDAO")
    private SystemParamDAO systemParamDAO;

    public List<SystemParam> listSystemParam(){
        List<SystemParam> dtoList = new ArrayList<>();
        systemParamDAO.listSystemParam().forEach(i -> {
            SystemParam param = new SystemParam();
            param.setId(i.getId());
            param.setRowguid(i.getRowguid());
            param.setValue(i.getValue());
            param.setDescription(i.getDescription());
            dtoList.add(param);
        });
        return dtoList;
    }

    /**
     * update column value
     * @param guid
     * @param value
     */
    @Transactional
    public void updateValueByGuid(String guid, String value) {
        try {
            systemParamDAO.updateValueByGuid(guid, Integer.valueOf(value));
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void insertRecord(SystemParam sys) {
        systemParamDAO.save(sys);
    }
}
