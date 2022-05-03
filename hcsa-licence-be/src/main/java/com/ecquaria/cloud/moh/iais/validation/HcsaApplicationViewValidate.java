package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.BeDashboardConstant;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.impl.ApplicationServiceImpl;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
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
        String appVehicleFlag = (String)ParamUtil.getSessionAttr(request, "appVehicleFlag");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(request,"applicationViewDto");
        String status = applicationViewDto.getApplicationDto().getStatus();
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(request,"taskDto");
        String nextStage = ParamUtil.getRequestString(request, "nextStage");
        String nextStageReplys = ParamUtil.getRequestString(request, "nextStageReplys");
        String decisionValue = ParamUtil.getString(request,"decisionValues");
        String applicationType = applicationViewDto.getApplicationDto().getApplicationType();
        boolean isAudit = ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(applicationType);
        boolean isRequestForChange = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType);
        String roleId = "";
        boolean isAppealType = ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType);
        boolean isWithdrawal = ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(applicationType);
        boolean isCessation = ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationType);
        boolean isFinalStage = (boolean)ParamUtil.getSessionAttr(request,"finalStage");
        List<AppPremisesRoutingHistoryDto> rollBackHistroyList = applicationViewDto.getRollBackHistroyList();
        boolean hasRollBackHistoryList = rollBackHistroyList != null && rollBackHistroyList.size() > 0;
        boolean normalFlow = !isFinalStage || hasRollBackHistoryList;
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
        if(taskDto != null){
            checkRecommendationOtherDropdown(errMap, recommendationStr, request, applicationType, roleId, taskDto.getTaskKey(), nextStage);
        }
        //DMS recommendation
        String generalErrSix = MessageUtil.replaceMessage("GENERAL_ERR0006","Processing Decision", "field");
        if(ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(status)){
            //verify upload file
            checkIsUploadDMS(applicationViewDto,errMap);

            if(StringUtil.isEmpty(decisionValue)){
                errMap.put("decisionValues",generalErrSix);
            }else{
                if(!isRequestForChange){
                    if(DECISION_APPROVAL.equals(decisionValue)){
                        if(StringUtil.isEmpty(recommendationStr)){
                            errMap.put("recommendation","GENERAL_ERR0024");
                        }else if(RECOMMENDATION_REJECT.equals(recommendationStr)){
                            //errMap.put("recommendation","The value of recommendation cannot be 'Reject'.");
                        }
                    }else if(DECISION_REJECT.equals(decisionValue)){

                    }
                }
                ParamUtil.setRequestAttr(request,"selectDecisionValue",decisionValue);
            }
        }
        //special status
        if (isRouteBackStatus(status) || ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(status) || ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(status)) {
            if (StringUtil.isEmpty(nextStageReplys)) {
                errMap.put("nextStageReplys", generalErrSix);
            } else {
                ParamUtil.setRequestAttr(request, "selectNextStageReply", nextStageReplys);
            }
            //AO route back to
            //65189
            if (isAoRouteBackStatus(status) && !isCessation && !isAudit && !isAppealType && !isWithdrawal) {
                if (StringUtil.isEmpty(recommendationStr)) {
                    errMap.put("recommendation", "GENERAL_ERR0024");
                }
            }
            //appeal if route back to ASO or PSO
            boolean rbStatusFlag = isRouteBackStatus(status);
            //appeal broadcast
            boolean appealBroadcastStatus = (isAppealType || isWithdrawal || isCessation) && ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(status);
            if(rbStatusFlag && (isAppealType || isWithdrawal || isCessation) || appealBroadcastStatus){
                appealTypeValidate(errMap,request,applicationType,roleId,taskDto.getTaskKey());
            }
            //ASO PSO broadcast
            if (!(isAppealType || isWithdrawal || isCessation) && ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(status) ) {
                checkBroadcast(roleId, errMap, status, recommendationStr, request);
            }
        } else {
            //normal flow
            //verify appeal type
            if(!ApplicationConsts.PROCESSING_DECISION_REQUEST_FOR_INFORMATION.equals(nextStage) && !ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING.equals(status)){
                //appeal rfi recommendation is not required
                appealTypeValidate(errMap,request,applicationType,roleId,taskDto.getTaskKey());
            }
            if (StringUtil.isEmpty(nextStage)) {
                errMap.put("nextStage", generalErrSix);
            } else {
                if(normalFlow){
                    if (VERIFIED.equals(nextStage)) {
                        String verified = ParamUtil.getRequestString(request, "verified");
                        ParamUtil.setRequestAttr(request, "selectVerified", verified);
                        if (StringUtil.isEmpty(verified)) {
                            errMap.put("verified", generalErrSix);
                        }else if(RoleConsts.USER_ROLE_AO1.equals(verified) || RoleConsts.USER_ROLE_AO2.equals(verified) || RoleConsts.USER_ROLE_AO3.equals(verified)){
                           String aoSelect = ParamUtil.getRequestString(request, "aoSelect");
                            ParamUtil.setSessionAttr(request,"aoSelect",aoSelect);
                            if (StringUtil.isEmpty(aoSelect)) {
                                errMap.put("aoSelect", generalErrSix);
                                ParamUtil.setSessionAttr(request,"aoSelectError",generalErrSix);
                            }
                        }
                        // if role is AOS or PSO ,check verified's value
                        if (RoleConsts.USER_ROLE_ASO.equals(roleId) || RoleConsts.USER_ROLE_PSO.equals(roleId)) {
                            if ((RoleConsts.USER_ROLE_AO1.equals(verified) || RoleConsts.USER_ROLE_AO2.equals(verified) || RoleConsts.USER_ROLE_AO3.equals(verified)) && !isAppealType) {
                                if (StringUtil.isEmpty(recommendationStr)) {
                                    errMap.put("recommendation", "GENERAL_ERR0024");
                                }
                            }
                        }
                        //check ins
                        checkInspectionForSixMonth(request,errMap,verified);
                    } else if (ROLLBACK.equals(nextStage)) {
                        String rollBack = ParamUtil.getRequestString(request, "rollBack");
                        ParamUtil.setRequestAttr(request, "selectRollBack", rollBack);
                        if (StringUtil.isEmpty(rollBack)) {
                            //Route Back To
                            errMap.put("rollBack", MessageUtil.replaceMessage("GENERAL_ERR0006","Route Back To", "field"));
                        }
                    } else if(ApplicationConsts.PROCESSING_DECISION_REQUEST_FOR_INFORMATION.equals(nextStage)){
                        //Prevent duplicate submissions
                        ApplicationService applicationService = SpringContextHelper.getContext().getBean(ApplicationServiceImpl.class);
                        Integer rfiCount = applicationService.getAppBYGroupIdAndStatus(applicationViewDto.getApplicationDto().getAppGrpId(),
                                ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION);
                        log.debug(StringUtil.changeForLog("validate the rfiCount is -->:" + rfiCount));
                        if (!(RoleConsts.USER_ROLE_AO1.equals(taskDto.getRoleId()) || RoleConsts.USER_ROLE_AO2.equals(taskDto.getRoleId()) || RoleConsts.USER_ROLE_AO3.equals(taskDto.getRoleId()))) {
                            if (rfiCount != 0) {
                                errMap.put("nextStage","GENERAL_ERR0045");
                            }
                        }
                        boolean flowFlag = !isAppealType && !isWithdrawal && !isCessation;
                        if(flowFlag){
                            //rfiSelectValue
                            String rfiSelectValue = ParamUtil.getRequestString(request, "rfiSelectValue");
                            if(StringUtil.isEmpty(rfiSelectValue)){
                                errMap.put("nextStage", "RFI_ERR001");
                            }
                        }
                    }
                    // final stage
                }else{
                }
            }
        }
        //validate vehicle EAS / MTS
        errMap = valiVehicleEasMts(request, errMap, applicationViewDto, nextStage, nextStageReplys, appVehicleFlag, recommendationStr, decisionValue);
        tcuVerification(errMap,applicationViewDto);
        return errMap;
    }

    private Map<String, String> valiVehicleEasMts(HttpServletRequest request, Map<String, String> errMap, ApplicationViewDto applicationViewDto,
                                                  String nextStage, String nextStageReplys, String appVehicleFlag, String recommendationStr,
                                                  String decisionValue) {
        if (applicationViewDto != null && (VERIFIED.equals(nextStage) || "PROCREP".equals(nextStageReplys)) && InspectionConstants.SWITCH_ACTION_EDIT.equals(appVehicleFlag))  {
            List<String> rejectCode = IaisCommonUtils.genNewArrayList(2);
            rejectCode.add(RECOMMENDATION_REJECT);
            rejectCode.add(DECISION_REJECT);
            valiVehicleEasMtsCommon(request, errMap, applicationViewDto, recommendationStr, rejectCode, decisionValue);
        }
        return errMap;
    }

    public static void valiVehicleEasMtsCommon(HttpServletRequest request, Map<String, String> errMap, ApplicationViewDto applicationViewDto,
                                                String recommendationStr, List<String> rejectCode, String decisionValue){
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        if(applicationDto != null) {
            List<AppSvcVehicleDto> appSvcVehicleDtos;
            if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationDto.getApplicationType())) {
                appSvcVehicleDtos = applicationViewDto.getVehicleRfcShowDtos();
            } else {
                appSvcVehicleDtos = applicationViewDto.getAppSvcVehicleDtos();
            }
            if (!IaisCommonUtils.isEmpty(appSvcVehicleDtos)) {
                boolean appVeh = !rejectCode.contains(StringUtil.getNonNull(recommendationStr)) && !rejectCode.contains(StringUtil.getNonNull(decisionValue));
                for (int i = 0; i < appSvcVehicleDtos.size(); i++) {
                    String[] vehicleNoRadios = ParamUtil.getStrings(request, "vehicleNoRadio" + i);
                    String vehicleNoRemarks = ParamUtil.getRequestString(request, "vehicleNoRemarks" + i);
                    //status not empty
                    if(appVeh){
                        if (vehicleNoRadios == null || vehicleNoRadios.length == 0) {
                            errMap.put("vehicleNoRadioError" + i, "GENERAL_ERR0006");
                            appSvcVehicleDtos.get(i).setStatus(null);
                        } else {
                            String vehicleNoRadio = vehicleNoRadios[0];
                            if (StringUtil.isEmpty(vehicleNoRadio)) {
                                errMap.put("vehicleNoRadioError" + i, "GENERAL_ERR0006");
                                appSvcVehicleDtos.get(i).setStatus(null);
                            } else {
                                String vehicleNoStatusCode;
                                if(BeDashboardConstant.SWITCH_ACTION_APPROVE.equals(vehicleNoRadio)) {
                                    vehicleNoStatusCode = ApplicationConsts.VEHICLE_STATUS_APPROVE;
                                } else {
                                    vehicleNoStatusCode = ApplicationConsts.VEHICLE_STATUS_REJECT;
                                }
                                appSvcVehicleDtos.get(i).setStatus(vehicleNoStatusCode);
                            }
                        }
                    }else {
                        appSvcVehicleDtos.get(i).setStatus(ApplicationConsts.VEHICLE_STATUS_REJECT);
                    }

                    //remark length vali
                    if (StringUtil.isEmpty(vehicleNoRemarks)) {
                        appSvcVehicleDtos.get(i).setRemarks(vehicleNoRemarks);
                    } else {
                        if (vehicleNoRemarks.length() <= 400) {
                            appSvcVehicleDtos.get(i).setRemarks(vehicleNoRemarks);
                        } else {
                            Map<String, String> repMap = IaisCommonUtils.genNewHashMap();
                            repMap.put("number", "400");
                            repMap.put("fieldNo", "Remarks");
                            errMap.put("vehicleNoRemarksError" + i, MessageUtil.getMessageDesc("GENERAL_ERR0036", repMap));
                        }
                    }
                }
                //not reject, At least one approve
                if(appVeh){
                    boolean approveFlag = false;
                    for(AppSvcVehicleDto appSvcVehicleDto : appSvcVehicleDtos) {
                        if(appSvcVehicleDto != null) {
                            if(!StringUtil.isEmpty(appSvcVehicleDto.getStatus()) && ApplicationConsts.VEHICLE_STATUS_APPROVE.equals(appSvcVehicleDto.getStatus())) {
                                approveFlag = true;
                            } else if(StringUtil.isEmpty(appSvcVehicleDto.getStatus())) {
                                approveFlag = true;
                            }
                        }
                    }
                    if(!approveFlag) {
                        errMap.put("vehicleApproveOne", "NEW_ERR0033");
                    }
                }
                if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationDto.getApplicationType())) {
                    applicationViewDto.setVehicleRfcShowDtos(appSvcVehicleDtos);
                } else {
                    applicationViewDto.setAppSvcVehicleDtos(appSvcVehicleDtos);
                }
            }
        }
    }
    /**
     * private method
     */

    private void checkInspectionForSixMonth(HttpServletRequest request,Map<String, String> errMap, String verified){
        String[] chooseInspections =  ParamUtil.getStrings(request,"chooseInspection");
        if(chooseInspections!=null){
            if(RoleConsts.PROCESS_TYPE_INS.equals(verified)){
                errMap.put("verified","Can not routing to inspection or ao1");
            }
            ParamUtil.setRequestAttr(request,"chooseInspectionChecked","Y");
        }
    }

    private void checkBroadcast(String roleId, Map<String, String> errMap,String status, String recommendationStr,HttpServletRequest request){
        boolean broadcastAsoPso = (boolean)ParamUtil.getSessionAttr(request,"broadcastAsoPso");
        if(broadcastAsoPso && ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(status)){
            if(StringUtil.isEmpty(recommendationStr)){
                errMap.put("recommendation","GENERAL_ERR0024");
            }
        }
    }

    private void checkIsUploadDMS(ApplicationViewDto applicationViewDto, Map<String, String> errMap){
            if(applicationViewDto != null){
                if((applicationViewDto.getIsUpload() == null) || !applicationViewDto.getIsUpload()){
                    errMap.put("document","GENERAL_ERR0025");
                }
            }
    }

    private void appealTypeValidate(Map<String, String> errMap, HttpServletRequest request, String applicationType, String roleId, String taskKey){
        // withdrawal and cessation same as appeal logic
        if(ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType) || ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(applicationType) || ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationType)){
            boolean isAso = HcsaConsts.ROUTING_STAGE_ASO.equals(taskKey) || RoleConsts.USER_ROLE_ASO.equals(roleId);
            boolean isPso = HcsaConsts.ROUTING_STAGE_PSO.equals(taskKey) || RoleConsts.USER_ROLE_PSO.equals(roleId);
            boolean isLateFeeAppealType = (boolean)ParamUtil.getSessionAttr(request,"isLateFeeAppealType");
            boolean isFinalStage = (boolean)ParamUtil.getSessionAttr(request,"finalStage");
            if((isAso || isPso) && !isFinalStage){
                String appealRecommendationValues = ParamUtil.getString(request, "appealRecommendationValues");
                if(StringUtil.isEmpty(appealRecommendationValues)){
                    errMap.put("appealRecommendationValues","GENERAL_ERR0024");
                }else{
                    ParamUtil.setRequestAttr(request,"selectAppealRecommendationValue",appealRecommendationValues);
                    if(isLateFeeAppealType && "appealApprove".equals(appealRecommendationValues)){
                        String returnFee = ParamUtil.getString(request, "returnFee");
                        if(StringUtil.isEmpty(returnFee)){
                            errMap.put("returnFee","GENERAL_ERR0024");
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
            errMap.put("returnFee","GENERAL_ERR0027");
        }else{
            ApplicationService applicationService = SpringContextHelper.getContext().getBean(ApplicationServiceImpl.class);
            AppFeeDetailsDto appFeeDetailsDto = applicationService.getAppFeeDetailsDtoByApplicationNo(oldApplicationNo);
            if(appFeeDetailsDto != null){
                Double laterFee = appFeeDetailsDto.getLaterFee();
                Double returnFee = Double.valueOf(returnFeeStr);
                if(returnFee>laterFee){
                    errMap.put("returnFee","APPEAL_ERR004");
                }
            }
        }
        //verify less than renew late fee
        //The amount keyed in has exceeded the original amount licensee has paid
    }


    private void checkRecommendationOtherDropdown(Map<String, String> errMap,String recommendationStr,HttpServletRequest request,
                                                  String applicationType, String roleId, String taskKey, String nextStage){
        boolean isChangePeriodAppealType = (boolean)ParamUtil.getSessionAttr(request, "isChangePeriodAppealType");
        boolean isAso = HcsaConsts.ROUTING_STAGE_ASO.equals(taskKey) || RoleConsts.USER_ROLE_ASO.equals(roleId);
        boolean isPso = HcsaConsts.ROUTING_STAGE_PSO.equals(taskKey) || RoleConsts.USER_ROLE_PSO.equals(roleId);
        boolean isAsoPso = isPso || isAso;
        boolean isAppealType = ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType);
        String appealRecommendationValues = ParamUtil.getString(request, "appealRecommendationValues");
        if(StringUtil.isEmpty(appealRecommendationValues)){
            appealRecommendationValues = ParamUtil.getString(request,"decisionValues");
        }
        boolean isAppealApprove = "appealApprove".equals(appealRecommendationValues) || "decisionApproval".equals(appealRecommendationValues);
        boolean rfiProcessDecFlag = !ApplicationConsts.PROCESSING_DECISION_REQUEST_FOR_INFORMATION.equals(nextStage);
        boolean rbProcessDecFlag = !ApplicationConsts.PROCESSING_DECISION_ROLLBACK.equals(nextStage);
        if(rfiProcessDecFlag && (("other".equals(recommendationStr) && !isAppealType) || (rbProcessDecFlag && isAppealType && isChangePeriodAppealType && isAsoPso && isAppealApprove))){
            if(!isAppealType){
                ParamUtil.setRequestAttr(request,"selectDecisionValue",DECISION_APPROVAL);
                ParamUtil.setRequestAttr(request,"recommendationStr","other");
            }
            String number = ParamUtil.getString(request,"number");
            if(StringUtil.isEmpty(number)){
                errMap.put("recomInNumber","GENERAL_ERR0024");
            }else{
                if(!CommonValidator.isPositiveInteger(number)){
                    errMap.put("recomInNumber","GENERAL_ERR0027");
                }else{
                    ParamUtil.setRequestAttr(request,"otherNumber",number);
                }
            }
            String chrono = ParamUtil.getString(request,"chrono");
            if(StringUtil.isEmpty(chrono)){
                errMap.put("chronoUnit","GENERAL_ERR0024");
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


    private void tcuVerification(Map<String, String> errMap, ApplicationViewDto applicationViewDto){
        if(applicationViewDto.isShowTcu() && applicationViewDto.isTcuFlag()){
            if( StringUtil.isEmpty(applicationViewDto.getTuc())){
                errMap.put("tcuDate","GENERAL_ERR0006");
            }else {
                try {
                    Date tcuDate = Formatter.parseDate(applicationViewDto.getTuc());
                    if(tcuDate.getTime()< System.currentTimeMillis()){
                            errMap.put("tcuDate","UC_INSTA004_ERR002");
                            }
                }catch (Exception e){
                    errMap.put("tcuDate","SYSPAM_ERROR0008");
                }
            }
        }
    }

}
