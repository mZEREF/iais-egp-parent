package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class HcsaApplicationViewValidate implements CustomizeValidator {
    private final String STATUS = "APST000";
    private final String VERIFIED = "VERIFIED";
    private final String ROLLBACK = "ROLLBACK";
    @Override
    public Map<String, String> validate(HttpServletRequest request) {

        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        String internalRemarks = ParamUtil.getRequestString(request, "internalRemarks");
        if(StringUtil.isEmpty(internalRemarks)){
            errMap.put("internalRemarks","The field is mandatory.");
        }

        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(request,"applicationViewDto");
        String status = applicationViewDto.getApplicationDto().getStatus();
        if(STATUS.equals(status)){
            String nextStageReply = ParamUtil.getRequestString(request, "nextStageReply");
            if(StringUtil.isEmpty(nextStageReply)){
                errMap.put("nextStageReply","The field is mandatory.");
            }
        }else{
            String nextStage = ParamUtil.getRequestString(request, "nextStage");
            if(StringUtil.isEmpty(nextStage)){
                errMap.put("nextStage","The field is mandatory.");
            }else{
                if(VERIFIED.equals(nextStage)){
                    String verified = ParamUtil.getRequestString(request, "verified");
                    ParamUtil.setRequestAttr(request,"selectVerified",verified);
                    if(StringUtil.isEmpty(verified)){
                        errMap.put("verified","The field is mandatory.");
                    }
                }else if(ROLLBACK.equals(nextStage)){
                    String rollBack = ParamUtil.getRequestString(request, "rollBack");
                    ParamUtil.setRequestAttr(request,"selectRollBack",rollBack);
                    if(StringUtil.isEmpty(rollBack)){
                        errMap.put("rollBack","The field is mandatory.");
                    }
                }
            }
        }
        return errMap;
    }
}
