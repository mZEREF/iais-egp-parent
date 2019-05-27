package sg.gov.moh.iais.service;

import sg.gov.moh.iais.dto.SystemParamDTO;
import sg.gov.moh.iais.entity.SystemParam;

import java.util.List;

public interface SystemParamService {
    List<SystemParamDTO> listSystemParam();

    void updateParam(String id, String value);

    void insertRecord(SystemParam sys);
}
