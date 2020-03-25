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
 * @Date: 2020/3/24
 */
public class AuditCancelTaskValidate implements CustomizeValidator {

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        List<AuditTaskDataFillterDto> auditTaskDataDtos = (List<AuditTaskDataFillterDto>) ParamUtil.getSessionAttr(request, "auditTaskDataDtos");
        if (auditTaskDataDtos != null && auditTaskDataDtos.size() > 0) {
            int index = 0;
            for (AuditTaskDataFillterDto auditTaskDataFillterDto : auditTaskDataDtos) {
                if (auditTaskDataFillterDto.isSelectedForAudit()) {
                    String requestSelectForAd = "selectForAd" + String.valueOf(index);
                    String selectForAd = ParamUtil.getString(request, requestSelectForAd);
                    if (StringUtil.isEmpty(selectForAd)) {
                        errMap.put(requestSelectForAd,"ERR0009");
                    }else {
                        auditTaskDataFillterDto.setSelected(true);
                    }
                    String remark = ParamUtil.getString(request, String.valueOf(index) +"reason");
                    auditTaskDataFillterDto.setCancelReason(remark);
                    index++;
                }

            }
        }
        return  errMap;
    }
}
