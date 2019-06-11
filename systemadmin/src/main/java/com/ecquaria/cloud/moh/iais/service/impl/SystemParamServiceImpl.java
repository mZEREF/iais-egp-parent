package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.dao.SystemParamDAO;
import com.ecquaria.cloud.moh.iais.dto.SystemParamDTO;
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

    public List<SystemParamDTO> listSystemParam(){
        List<SystemParamDTO> dtoList = new ArrayList<>();
        systemParamDAO.listSystemParam().forEach(i -> {
            SystemParamDTO param = new SystemParamDTO();
            param.setId(i.getRowguid());
            param.setValue(i.getValue());
            param.setDescription(i.getDescription());
            dtoList.add(param);
        });
        return dtoList;
    }

    /**
     * update column value
     * @param id
     * @param value
     */
    @Transactional
    public void updateParamByPkId(String id, String value) {
        try {
            systemParamDAO.updateValueByKey(id, Integer.valueOf(value));
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void insertRecord(SystemParam sys) {
        systemParamDAO.save(sys);
    }
}
