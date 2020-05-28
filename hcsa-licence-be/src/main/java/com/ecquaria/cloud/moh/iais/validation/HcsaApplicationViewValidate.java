package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import java.util.Map;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HcsaApplicationViewValidate implements CustomizeValidator {
    private final String VERIFIED = ApplicationConsts.PROCESSING_DECISION_VERIFIED;
    private final String ROLLBACK = ApplicationConsts.PROCESSING_DECISION_ROLLBACK;
    private final String DECISION_APPROVAL = "decisionApproval";
    private final String DECISION_REJECT = "decisionReject";
    private final String RECOMMENDATION_REJECT = "reject";
    @Override
    public Map<String, String> validate(HttpServletRequest request) {

        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        String internalRemarks = ParamUtil.getRequestString(request, "internalRemarks");
        String date = ParamUtil.getDate(request, "tuc");
        String recommendationStr = ParamUtil.getString(request,"recommendation");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(request,"applicationViewDto");
        String status = applicationViewDto.getApplicationDto().getStatus();
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(request,"taskDto");
        String roleId = "";
        if(taskDto != null){
            roleId = taskDto.getRoleId();
        }


        if(StringUtil.isEmpty(internalRemarks)){
            //errMap.put("internalRemarks","The field is mandatory.");
        }else{
            ParamUtil.setRequestAttr(request,"internalRemarks",internalRemarks);
        }

        if(!StringUtil.isEmpty(date)){
            ParamUtil.setRequestAttr(request,"date",date);
        }else{
            date = ParamUtil.getString(request,"dateTimeShow");
            ParamUtil.setRequestAttr(request,"recomInDateOnlyShow",date);
        }

        if(!StringUtil.isEmpty(recommendationStr)){
            ParamUtil.setRequestAttr(request,"recommendationStr",recommendationStr);
        }else{
            //recommendationShow
            recommendationStr = ParamUtil.getString(request,"recommendationShow");
            ParamUtil.setRequestAttr(request,"recommendationOnlyShow",recommendationStr);
        }

        //verified recommendation other dropdown
        //0063971
        checkRecommendationOtherDropdown(errMap,recommendationStr,request);

        //DMS recommendation
        if(ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(status)){
            //verify upload file
            checkIsUploadDMS(applicationViewDto,errMap);

            String decisionValue = ParamUtil.getString(request,"decisionValues");

            if(StringUtil.isEmpty(decisionValue)){
                errMap.put("decisionValues","The field is mandatory.");
            }else{
                if(DECISION_APPROVAL.equals(decisionValue)){
                    if(StringUtil.isEmpty(recommendationStr)){
                        errMap.put("recommendation","The field is mandatory.");
                    }else if(RECOMMENDATION_REJECT.equals(recommendationStr)){
                        errMap.put("recommendation","The value of recommendation cannot be 'Reject'.");
                    }
                }else if(DECISION_REJECT.equals(decisionValue)){
//                    if(StringUtil.isEmpty(recommendationStr)){
//                        errMap.put("recommendation","Please key in recommendation");
//                    }else if(!RECOMMENDATION_REJECT.equals(recommendationStr)){
//                        errMap.put("recommendation","The value of recommendation must be 'Reject'.");
//                    }
                }
                ParamUtil.setRequestAttr(request,"selectDecisionValue",decisionValue);
            }
        }

        if (ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(status) &&
                applicationViewDto.getApplicationDto().getApplicationType().equals(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL)){
            String withdrawalDecisionValue = ParamUtil.getString(request, "withdrawalDecisionValues");
            if(StringUtil.isEmpty(withdrawalDecisionValue)){
                errMap.put("decisionValues","The field is mandatory.");
            }
        }else {
            if (isRouteBackStatus(status) || ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(status) || ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(status)) {
                String nextStageReplys = ParamUtil.getRequestString(request, "nextStageReplys");
                if (StringUtil.isEmpty(nextStageReplys)) {
                    errMap.put("nextStageReplys", "The field is mandatory.");
                } else {
                    ParamUtil.setRequestAttr(request, "selectNextStageReply", nextStageReplys);
                }
                //AO route back to
                if (isAoRouteBackStatus(status)) {
                    if (StringUtil.isEmpty(recommendationStr)) {
                        errMap.put("recommendation", "Please key in recommendation");
                    }
                }
                //ASO PSO broadcast
                if (ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(status)) {
                    checkBroadcast(roleId, errMap, status, recommendationStr, request);
                }
            } else {
                String nextStage = ParamUtil.getRequestString(request, "nextStage");
                if (StringUtil.isEmpty(nextStage)) {
                    errMap.put("nextStage", "The field is mandatory.");
                } else {
                    if (VERIFIED.equals(nextStage)) {
                        String verified = ParamUtil.getRequestString(request, "verified");
                        ParamUtil.setRequestAttr(request, "selectVerified", verified);
                        if (StringUtil.isEmpty(verified)) {
                            errMap.put("verified", "The field is mandatory.");
                        }
                        // if role is AOS or PSO ,check verified's value
                        if (RoleConsts.USER_ROLE_ASO.equals(roleId) || RoleConsts.USER_ROLE_PSO.equals(roleId)) {
                            if (RoleConsts.USER_ROLE_AO1.equals(verified) || RoleConsts.USER_ROLE_AO2.equals(verified) || RoleConsts.USER_ROLE_AO3.equals(verified)) {
                                if (StringUtil.isEmpty(recommendationStr)) {
                                    errMap.put("recommendation", "Please key in recommendation");
                                }
                            }
                        }

                    } else if (ROLLBACK.equals(nextStage)) {
                        String rollBack = ParamUtil.getRequestString(request, "rollBack");
                        ParamUtil.setRequestAttr(request, "selectRollBack", rollBack);
                        if (StringUtil.isEmpty(rollBack)) {
                            errMap.put("rollBack", "The field is mandatory.");
                        }
                    }
                }
            }
        }

        return errMap;
    }

    /**
     * private method
     */

    private void checkBroadcast(String roleId, Map<String, String> errMap,String status, String recommendationStr,HttpServletRequest request){
        boolean broadcastAsoPso = (boolean)ParamUtil.getSessionAttr(request,"broadcastAsoPso");
        if(broadcastAsoPso && ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(status)){
            if(StringUtil.isEmpty(recommendationStr)){
                errMap.put("recommendation","Please key in recommendation");
            }
        }
    }

    private void checkIsUploadDMS(ApplicationViewDto applicationViewDto, Map<String, String> errMap){
            if(applicationViewDto != null){
                if((applicationViewDto.getIsUpload() == null) || !applicationViewDto.getIsUpload()){
                    errMap.put("document","DMS's reply email is mandatory to be uploaded");
                }
            }
    }


    private void checkRecommendationOtherDropdown(Map<String, String> errMap,String recommendationStr,HttpServletRequest request){
        if("other".equals(recommendationStr)){
            ParamUtil.setRequestAttr(request,"selectDecisionValue",DECISION_APPROVAL);
            ParamUtil.setRequestAttr(request,"recommendationStr","other");
            String number = ParamUtil.getString(request,"number");
            if(StringUtil.isEmpty(number)){
                errMap.put("recomInNumber","The field is mandatory.");
            }else{
                if(!CommonValidator.isPositiveInteger(number)){
                    errMap.put("recomInNumber","The field is Invalid.");
                }else{
                    ParamUtil.setRequestAttr(request,"otherNumber",number);
                }
            }
            String chrono = ParamUtil.getString(request,"chrono");
            if(StringUtil.isEmpty(chrono)){
                errMap.put("chronoUnit","The field is mandatory.");
            }else{
                ParamUtil.setRequestAttr(request,"otherChrono",chrono);
            }
        }
    }
    //check number
    public boolean isNumeric(String string){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(string).matches();
    }

    //verify route back status
    private boolean isRouteBackStatus(String status){
        boolean flag = false;
        if(ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_AO.equals(status)
                || ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_ASO.equals(status)
                || ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_PSO.equals(status)
                || ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_INSPECTOR.equals(status)
                || ApplicationConsts.APPLICATION_STATUS_PSO_ROUTE_BACK.equals(status)
                || ApplicationConsts.APPLICATION_STATUS_INSPECTOR_ROUTE_BACK.equals(status)){
            flag = true;
        }
        return flag;
    }

    //verify AO route back status
    private boolean isAoRouteBackStatus(String status){
        boolean flag = false;
        if(ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_AO.equals(status)
                || ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_ASO.equals(status)
                || ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_PSO.equals(status)
                || ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_INSPECTOR.equals(status)){
            flag = true;
        }
        return flag;
    }

}
