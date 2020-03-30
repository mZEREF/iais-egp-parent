package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
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
    private final String DECISION_APPROVAL = "decisionApproval";
    private final String DECISION_REJECT = "decisionReject";
    private final String RECOMMENDATION_REJECT = "reject";
    @Override
    public Map<String, String> validate(HttpServletRequest request) {

        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        String internalRemarks = ParamUtil.getRequestString(request, "internalRemarks");
        if(StringUtil.isEmpty(internalRemarks)){
            //errMap.put("internalRemarks","The field is mandatory.");
        }else{
            ParamUtil.setRequestAttr(request,"internalRemarks",internalRemarks);
        }
        String date = ParamUtil.getDate(request, "tuc");
        if(!StringUtil.isEmpty(date)){
            ParamUtil.setRequestAttr(request,"date",date);
        }

        String  taskId = (String)ParamUtil.getRequestAttr(request,"taskId");
        if(!StringUtil.isEmpty(taskId)){
            ParamUtil.setRequestAttr(request,"taskId",taskId);
        }

        String recommendationStr = ParamUtil.getString(request,"recommendation");
        if(!StringUtil.isEmpty(recommendationStr)){
            ParamUtil.setRequestAttr(request,"recommendationStr",recommendationStr);
        }

        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(request,"applicationViewDto");
        String status = applicationViewDto.getApplicationDto().getStatus();
        //DMS recommendation
        if(ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(status)){
            String decisionValue = ParamUtil.getString(request,"decisionValues");

            if(StringUtil.isEmpty(decisionValue)){
                errMap.put("decisionValues","The field is mandatory.");
            }else{
                if(DECISION_APPROVAL.equals(decisionValue)){
                    if(StringUtil.isEmpty(recommendationStr)){
                        errMap.put("recommendation","Please key in recommendation");
                    }else if(RECOMMENDATION_REJECT.equals(recommendationStr)){
                        errMap.put("recommendation","The value of recommendation cannot be 'reject'.");
                    }
                }else if(DECISION_REJECT.equals(decisionValue)){
                    if(StringUtil.isEmpty(recommendationStr)){
                        errMap.put("recommendation","Please key in recommendation");
                    }else if(!RECOMMENDATION_REJECT.equals(recommendationStr)){
                        errMap.put("recommendation","The value of recommendation must be 'reject'.");
                    }
                }
                ParamUtil.setRequestAttr(request,"selectDecisionValue",decisionValue);
            }
        }

        if(ApplicationConsts.APPLICATION_STATUS_ROLL_BACK.equals(status) || ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(status)){
            String nextStageReplys = ParamUtil.getRequestString(request, "nextStageReplys");
            if(StringUtil.isEmpty(nextStageReplys)){
                errMap.put("nextStageReplys","The field is mandatory.");
            }else{
                ParamUtil.setRequestAttr(request,"selectNextStageReply",nextStageReplys);
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
                    // if role is AOS or PSO ,check verified's value
                    TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(request,"taskDto");
                    String roleId = "";
                    if(taskDto !=null){
                        roleId = taskDto.getRoleId();
                    }
                    if("ASO".equals(roleId) || "PSO".equals(roleId)){
                        if("AO1".equals(verified) || "AO2".equals(verified) || "AO3".equals(verified)){
                            if(StringUtil.isEmpty(recommendationStr)){
                                errMap.put("recommendation","Please key in recommendation");
                            }
                        }
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
