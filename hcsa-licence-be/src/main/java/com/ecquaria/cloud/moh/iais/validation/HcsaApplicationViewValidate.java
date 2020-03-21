package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

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
        }else{
            ParamUtil.setRequestAttr(request,"internalRemarks",internalRemarks);
        }
        String date = ParamUtil.getDate(request, "tuc");
        if(!StringUtil.isEmpty(date)){
            ParamUtil.setRequestAttr(request,"date",date);
        }

        String  taskId = ParamUtil.getString(request,"taskId");
        if(!StringUtil.isEmpty(taskId)){
            ParamUtil.setRequestAttr(request,"taskId",taskId);
        }

        String recommendationStr = ParamUtil.getString(request,"recommendation");
        if(!StringUtil.isEmpty(recommendationStr)){
            ParamUtil.setRequestAttr(request,"recommendationStr",recommendationStr);
        }

        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(request,"applicationViewDto");
        String status = applicationViewDto.getApplicationDto().getStatus();
        if(ApplicationConsts.APPLICATION_STATUS_ROLL_BACK.equals(status) || ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(status)){
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
