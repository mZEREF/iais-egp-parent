/*
package sg.gov.moh.iais.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;
import SystemParamDAO;
import SystemParamDTO;
import SystemParam;
import SystemParamService;

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

    @Transactional
    public void updateParam(String id, String value) {
        try {
            systemParamDAO.updateDescriptionByKey(id, Integer.valueOf(value));
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }

    }

    @Transactional
    public void insertRecord(SystemParam sys) {
        systemParamDAO.save(sys);
    }
}
*/
