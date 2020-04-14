package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author: wangyu
 * @Date: 2020/4/14
 */
public class AuditCancelOrRejectValidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        List<AuditTaskDataFillterDto> auditTaskDataDtos  = (List<AuditTaskDataFillterDto>) ParamUtil.getSessionAttr(request,"auditTaskDataDtos");
        int selectedFlagNum = 0;
        if(!IaisCommonUtils.isEmpty(auditTaskDataDtos)){
            for(int i=0;i<auditTaskDataDtos.size();i++){
                if(auditTaskDataDtos.get(i).isSelectedForAudit())
                    selectedFlagNum++;
            }
        }
        if(selectedFlagNum==0){
            errMap.put("selectedOne","AUDIT_UC_ERR0002");
        }
        return errMap;
    }
}
