package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.impl.ApplicationServiceImpl;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.regex.Pattern;


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
        String applicationType = applicationViewDto.getApplicationDto().getApplicationType();
        boolean isCessation = ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationType);
        String roleId = "";
        boolean isAppealType = ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType);
        if(taskDto != null){
            roleId = taskDto.getRoleId();
        }


        if(!StringUtil.isEmpty(internalRemarks)){
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

        //route back review filling back
        String routeBackReview  = ParamUtil.getString(request,"routeBackReview");
        if(!StringUtil.isEmpty(routeBackReview)){
            ParamUtil.setRequestAttr(request,"routeBackReviewChecked",routeBackReview);
        }

        //verified recommendation other dropdown
        //0063971
        checkRecommendationOtherDropdown(errMap,recommendationStr,request,applicationType,roleId);

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
            //special status
            if (isRouteBackStatus(status) || ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(status) || ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(status)) {
                String nextStageReplys = ParamUtil.getRequestString(request, "nextStageReplys");
                if (StringUtil.isEmpty(nextStageReplys)) {
                    errMap.put("nextStageReplys", "The field is mandatory.");
                } else {
                    ParamUtil.setRequestAttr(request, "selectNextStageReply", nextStageReplys);
                }
                //AO route back to
                //65189
                if (isAoRouteBackStatus(status) && !isCessation) {
                    if (StringUtil.isEmpty(recommendationStr)) {
                        errMap.put("recommendation", "Please key in recommendation");
                    }
                }
                //appeal if route back to ASO or PSO
                boolean rbStatusFlag = isRouteBackStatus(status);
                if(rbStatusFlag && isAppealType){
                    appealTypeValidate(errMap,request,applicationType,roleId);
                }
                //ASO PSO broadcast
                if (ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(status)) {
                    checkBroadcast(roleId, errMap, status, recommendationStr, request);
                }
            } else {
                //normal flow
                String nextStage = ParamUtil.getRequestString(request, "nextStage");
                //verify appeal type
                appealTypeValidate(errMap,request,applicationType,roleId);
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
                            if (RoleConsts.USER_ROLE_AO1.equals(verified) || RoleConsts.USER_ROLE_AO2.equals(verified) || RoleConsts.USER_ROLE_AO3.equals(verified) && !isAppealType) {
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
                    } else if(ApplicationConsts.PROCESSING_DECISION_REQUEST_FOR_INFORMATION.equals(nextStage)){
                        //rfiSelectValue
                        String rfiSelectValue = ParamUtil.getRequestString(request, "rfiSelectValue");
                        if(StringUtil.isEmpty(rfiSelectValue)){
                            errMap.put("nextStage", "Please select at least 1 section to unlock");
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

    private void appealTypeValidate(Map<String, String> errMap, HttpServletRequest request, String applicationType, String roleId){
        if(ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)){
            boolean isAso = RoleConsts.USER_ROLE_ASO.equals(roleId);
            boolean isPso = RoleConsts.USER_ROLE_PSO.equals(roleId);
            boolean isLateFeeAppealType = (boolean)ParamUtil.getSessionAttr(request,"isLateFeeAppealType");
            if(isAso || isPso){
                String appealRecommendationValues = ParamUtil.getString(request, "appealRecommendationValues");
                if(StringUtil.isEmpty(appealRecommendationValues)){
                    errMap.put("appealRecommendationValues","Please key in recommendation");
                }else{
                    ParamUtil.setRequestAttr(request,"selectAppealRecommendationValue",appealRecommendationValues);
                    if(isLateFeeAppealType && "appealApprove".equals(appealRecommendationValues)){
                        String returnFee = ParamUtil.getString(request, "returnFee");
                        if(StringUtil.isEmpty(returnFee)){
                            errMap.put("returnFee","Please key in recommendation");
                        }else{
                            String oldApplicationNo = (String)ParamUtil.getSessionAttr(request, "oldApplicationNo");
                            verifyReturnFee(returnFee,errMap,oldApplicationNo);
                            ParamUtil.setRequestAttr(request,"returnFee",returnFee);
                        }
                    }
                }
            }
        }
    }

    public void verifyReturnFee(String returnFeeStr, Map<String, String> errMap, String oldApplicationNo){
        //verify numeric
        if(!CommonValidator.isCurrency(returnFeeStr)){
            errMap.put("returnFee","The field is Invalid");
        }else{
            ApplicationService applicationService = SpringContextHelper.getContext().getBean(ApplicationServiceImpl.class);
            AppFeeDetailsDto appFeeDetailsDto = applicationService.getAppFeeDetailsDtoByApplicationNo(oldApplicationNo);
            if(appFeeDetailsDto != null){
                Double laterFee = appFeeDetailsDto.getLaterFee();
                Double returnFee = Double.valueOf(returnFeeStr);
                if(returnFee>laterFee){
                    errMap.put("returnFee","The amount keyed in has exceeded the original amount licensee has paid");
                }
            }
        }
        //verify less than renew late fee
        //The amount keyed in has exceeded the original amount licensee has paid
    }


    private void checkRecommendationOtherDropdown(Map<String, String> errMap,String recommendationStr,HttpServletRequest request, String applicationType, String roleId){
        boolean isChangePeriodAppealType = (boolean)ParamUtil.getSessionAttr(request, "isChangePeriodAppealType");
        boolean isAsoPso = RoleConsts.USER_ROLE_ASO.equals(roleId) || RoleConsts.USER_ROLE_PSO.equals(roleId);
        boolean isAppealType = ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType);
        String appealRecommendationValues = ParamUtil.getString(request, "appealRecommendationValues");
        boolean isAppealApprove = "appealApprove".equals(appealRecommendationValues);
        if("other".equals(recommendationStr) || (isAppealType && isChangePeriodAppealType && isAsoPso && isAppealApprove)){
            if(!isAppealType){
                ParamUtil.setRequestAttr(request,"selectDecisionValue",DECISION_APPROVAL);
                ParamUtil.setRequestAttr(request,"recommendationStr","other");
            }
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
