package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: jiahao
 * @Date: 2020/2/26 17:16
 */
public class AuditAssginListValidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = new HashMap<>();
        List<AuditTaskDataFillterDto> auditTaskDataDtos  = (List<AuditTaskDataFillterDto>) ParamUtil.getSessionAttr(request,"auditTaskDataDtos");
        int selectedFlagNum = 0;
        if(!IaisCommonUtils.isEmpty(auditTaskDataDtos)){
            for(int i=0;i<auditTaskDataDtos.size();i++){
                if(auditTaskDataDtos.get(i).isSelectedForAudit()){
                    selectedFlagNum++;
                    if(StringUtil.isEmpty(auditTaskDataDtos.get(i).getInspector())){
                        errMap.put(i+"insp","AUDIT_UC_ERR0001");
                    }
                    if(StringUtil.isEmpty(auditTaskDataDtos.get(i).getAuditType())){
                        errMap.put(i+"adtype","AUDIT_UC_ERR0003");
                    }
                }
            }
        }
        if(selectedFlagNum==0){
            errMap.put("selectedOne","AUDIT_UC_ERR0002");
        }
        return errMap;
    }
}
