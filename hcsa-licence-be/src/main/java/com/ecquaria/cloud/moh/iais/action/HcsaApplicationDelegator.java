package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionReportConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageCodeKey;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppReturnFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.SmsDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.utils.TaskUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.TaskHistoryDto;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.ApplicationGroupService;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.BroadcastService;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import com.ecquaria.cloud.moh.iais.service.LicenseeService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.validation.HcsaApplicationProcessUploadFileValidate;
import com.ecquaria.cloud.moh.iais.validation.HcsaApplicationViewValidate;
import com.ecquaria.cloudfeign.FeignException;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;
import sop.util.CopyUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * HcsaApplicationDelegator
 *
 * @author suocheng
 * @date 10/17/2019
 */
@Delegator("hcsaApplicationDelegator")
@Slf4j
public class HcsaApplicationDelegator {
    @Autowired
    private TaskService taskService;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ApplicationGroupService applicationGroupService;

    @Autowired
    private AppPremisesRoutingHistoryService appPremisesRoutingHistoryService;

    @Autowired
    private BroadcastService broadcastService;

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private InsRepService insRepService;

    @Autowired
    private MsgTemplateClient msgTemplateClient;

    @Autowired
    private LicenseeService licenseeService;

    @Autowired
    private GenerateIdClient generateIdClient;

    @Autowired
    private EmailClient emailClient;

    @Autowired
    private InboxMsgService inboxMsgService;

    @Autowired
    FileRepoClient fileRepoClient;

    @Autowired
    LicenceService licenceService;
    @Autowired
    CessationClient cessationClient;
    @Autowired
    ApplicationClient applicationClient;
    @Autowired
    AppPremisesRoutingHistoryClient appPremisesRoutingHistoryClient;
    @Autowired
    OrganizationClient organizationClient;
    @Autowired
    private AppInspectionStatusClient appInspectionStatusClient;
    @Autowired
    private AppPremisesCorrClient appPremisesCorrClient;
    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;
    @Autowired
    private SystemParamConfig systemParamConfig;


    @Value("${iais.email.sender}")
    private String mailSender;

    /**
     * StartStep: doStart
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do cleanSession start ...."));
        ParamUtil.setSessionAttr(bpc.request,"taskDto",null);
        ParamUtil.setSessionAttr(bpc.request,"applicationViewDto",null);
        ParamUtil.setSessionAttr(bpc.request,"isSaveRfiSelect",null);
        ParamUtil.setSessionAttr(bpc.request, "nextStages", null);
        ParamUtil.setSessionAttr(bpc.request, "nextStageReply", null);
        ParamUtil.setSessionAttr(bpc.request, "premiseMiscDto", null);
        log.debug(StringUtil.changeForLog("the do cleanSession end ...."));

        initData(bpc);
    }

    /**
     * StartStep: prepareData
     *
     * @param bpc
     * @throws
     */
    public void prepareData(BaseProcessClass bpc) throws Exception{
        log.debug(StringUtil.changeForLog("the do prepareData start ..."));

        TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(bpc.request,"taskDto");
        String correlationId = "";
        if(taskDto != null){
            correlationId = taskDto.getRefNo();
        }else{
            throw new IaisRuntimeException("The Task Id  is Error !!!");
        }
        log.debug(StringUtil.changeForLog("the do prepareData get the NewAppPremisesCorrelationDto"));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
        if(applicationViewDto == null){
            AppPremisesCorrelationDto appPremisesCorrelationDto = applicationViewService.getLastAppPremisesCorrelationDtoById(correlationId);
            appPremisesCorrelationDto.setOldCorrelationId(correlationId);
            String newCorrelationId = appPremisesCorrelationDto.getId();
            applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(newCorrelationId);
            applicationViewDto.setNewAppPremisesCorrelationDto(appPremisesCorrelationDto);
        }
        log.debug(StringUtil.changeForLog("the do prepareData get the appEditSelectDto"));
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        if(applicationDto != null){
            if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationDto.getApplicationType()) ){
                //RFC
                String applicationNo = applicationDto.getApplicationNo();
                List<ApplicationDto> applicationDtosByApplicationNo = applicationService.getApplicationDtosByApplicationNo(applicationNo);
                List<String> list =IaisCommonUtils.genNewArrayList();
                if(applicationDtosByApplicationNo!=null){
                    for(ApplicationDto applicationDto1 : applicationDtosByApplicationNo){
                        list.add(applicationDto1.getId());
                    }
                }
                List<AppEditSelectDto> appEditSelectDtosByAppIds = applicationService.getAppEditSelectDtosByAppIds(list);
                if(!appEditSelectDtosByAppIds.isEmpty()){
                    applicationViewDto.setAppEditSelectDto(appEditSelectDtosByAppIds.get(0));
                }
            }else if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationDto.getApplicationType())){
                if (!StringUtil.isEmpty(applicationDto.getId())) {
                    List<AppEditSelectDto> appEditSelectDtos = applicationService.getAppEditSelectDtos(applicationDto.getId(), ApplicationConsts.APPLICATION_EDIT_TYPE_NEW);
                    if (!IaisCommonUtils.isEmpty(appEditSelectDtos)) {
                        applicationViewDto.setAppEditSelectDto(appEditSelectDtos.get(0));
                    }else{
                        AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
                        appEditSelectDto.setPremisesEdit(true);
                        appEditSelectDto.setDocEdit(true);
                        appEditSelectDto.setServiceEdit(true);
                        applicationViewDto.setAppEditSelectDto(appEditSelectDto);
                    }
                }

            } else{
                AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
                appEditSelectDto.setPremisesEdit(true);
                appEditSelectDto.setDocEdit(true);
                appEditSelectDto.setMedAlertEdit(true);
                appEditSelectDto.setServiceEdit(true);
                appEditSelectDto.setPoEdit(true);
                appEditSelectDto.setDpoEdit(true);
                applicationViewDto.setAppEditSelectDto(appEditSelectDto);
            }
        }
        String roleId = taskDto.getRoleId();
        log.debug(StringUtil.changeForLog("the do prepareData get the appPremisesRecommendationDto"));
        log.debug(StringUtil.changeForLog("the do prepareData roleId -->:"+roleId));

        //set selection value
        setSelectionValue(bpc.request,applicationViewDto,taskDto);
        //check inspection
        checkShowInspection(bpc,applicationViewDto,taskDto,applicationViewDto.getNewAppPremisesCorrelationDto().getOldCorrelationId());
        //set recommendation show name
        checkRecommendationShowName(bpc,applicationViewDto);
        //set session
        ParamUtil.setSessionAttr(bpc.request,"applicationViewDto", applicationViewDto);
        log.debug(StringUtil.changeForLog("the do prepareData end ...."));
    }

    /**
     * StartStep: chooseStage
     *
     * @param bpc
     * @throws
     */
    public void chooseStage(BaseProcessClass bpc) throws Exception {
        log.debug(StringUtil.changeForLog("the do chooseStage start ...."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
        //do upload file
        String doDocument = ParamUtil.getString(bpc.request,"uploadFile");
        String interalFileId = ParamUtil.getString(bpc.request,"interalFileId");
        if(!StringUtil.isEmpty(interalFileId) || "Y".equals(doDocument)){
            ParamUtil.setRequestAttr(bpc.request, "crud_action_type", "PREPARE");
            ParamUtil.setRequestAttr(bpc.request, "doDocument", "Y");
            return;
        }
        //appeal
        String applicationType = applicationViewDto.getApplicationDto().getApplicationType();
        boolean isAppealType = ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType);
        boolean isChangePeriodAppealType = false;
        boolean isLateFeeAppealType = false;
        if(isAppealType){
            isChangePeriodAppealType = (boolean)ParamUtil.getSessionAttr(bpc.request,"isChangePeriodAppealType");
            isLateFeeAppealType = (boolean)ParamUtil.getSessionAttr(bpc.request,"isLateFeeAppealType");
        }
        //validate
        HcsaApplicationViewValidate hcsaApplicationViewValidate = new HcsaApplicationViewValidate();
        Map<String, String> errorMap = hcsaApplicationViewValidate.validate(bpc.request);
        //do not have the rfi applicaiton can approve.
        String approveSelect = ParamUtil.getString(bpc.request,"nextStage");
        String nextStageReplys = ParamUtil.getString(bpc.request,"nextStageReplys");
        validateCanApprove(approveSelect,applicationViewDto,errorMap);
        if(!errorMap.isEmpty()){
            String doProcess = "Y";
            ParamUtil.setRequestAttr(bpc.request, "doProcess",doProcess);
            ParamUtil.setRequestAttr(bpc.request, "crud_action_type", "PREPARE");
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
        }else{
            TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request,"taskDto");
            String roleId = taskDto.getRoleId();
            String status = applicationViewDto.getApplicationDto().getStatus();
            boolean isAsoPso = RoleConsts.USER_ROLE_ASO.equals(roleId) || RoleConsts.USER_ROLE_PSO.equals(roleId);
//            String appPremCorreId=taskDto.getRefNo();
            String appPremCorreId = applicationViewDto.getNewAppPremisesCorrelationDto().getId();
            //save recommendation
            String recommendationStr = ParamUtil.getString(bpc.request,"recommendation");
            boolean isDMS = ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(applicationViewDto.getApplicationDto().getStatus());
            boolean isWithdrwal = false;
            if (applicationViewDto.getApplicationDto().getStatus().equals(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING)
                    && applicationViewDto.getApplicationDto().getApplicationType().equals(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL)){
                isWithdrwal = true;
            }
            String decisionValues = ParamUtil.getString(bpc.request, "decisionValues");
            String withdrawalDecisionValue = ParamUtil.getString(bpc.request, "withdrawalDecisionValues");
            boolean isRejectDMS = "decisionReject".equals(decisionValues);
            boolean isRequestForChange = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType);
            boolean isRejectWithdrawal = "decisionReject".equals(withdrawalDecisionValue);
            if (isWithdrwal){
                if(isRejectWithdrawal){
                    recommendationStr = "reject";
                }
            }
            if(isDMS){
                if(isRejectDMS){
                    recommendationStr = "reject";
                }
            }else if(isAppealType && isAsoPso){
                String appealRecommendationValues = ParamUtil.getString(bpc.request,"appealRecommendationValues");
                if("appealReject".equals(appealRecommendationValues)){
                    recommendationStr = "reject";
                }else{
                    recommendationStr = "other";
                }
            }

            String dateStr = ParamUtil.getDate(bpc.request, "tuc");
            String dateTimeShow = ParamUtil.getString(bpc.request,"dateTimeShow");
            if(StringUtil.isEmpty(recommendationStr)){
                //PSO route back to ASO,PSO clear recommendation or licence start date
                if(ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING.equals(status) && RoleConsts.USER_ROLE_PSO.equals(roleId) && ApplicationConsts.PROCESSING_DECISION_ROLLBACK.equals(approveSelect)){
                    AppPremisesRecommendationDto clearRecommendationDto = getClearRecommendationDto(appPremCorreId, dateStr, dateTimeShow);
                    insRepService.updateRecommendation(clearRecommendationDto);
                }
                //PSO route back to ASO,ASO clear recommendation or licence start date
                if(ApplicationConsts.APPLICATION_STATUS_PSO_ROUTE_BACK.equals(status) && RoleConsts.USER_ROLE_ASO.equals(roleId) && ApplicationConsts.PROCESSING_DECISION_REPLY.equals(nextStageReplys)){
                    AppPremisesRecommendationDto clearRecommendationDto = getClearRecommendationDto(appPremCorreId, dateStr, dateTimeShow);
                    insRepService.updateRecommendation(clearRecommendationDto);
                }
                //ASO route to PSO
                if(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(status) && RoleConsts.USER_ROLE_ASO.equals(roleId) && ApplicationConsts.PROCESSING_DECISION_VERIFIED.equals(approveSelect)){
                    AppPremisesRecommendationDto clearRecommendationDto = getClearRecommendationDto(appPremCorreId, dateStr, dateTimeShow);
                    insRepService.updateRecommendation(clearRecommendationDto);
                }
            }else if(("reject").equals(recommendationStr)){
                AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
                appPremisesRecommendationDto.setRecomDecision(InspectionReportConstants.REJECTED);
                appPremisesRecommendationDto.setAppPremCorreId(appPremCorreId);
                appPremisesRecommendationDto.setRecomInNumber(0);
                appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT);
                if(isAppealType){
                    appPremisesRecommendationDto.setRecomDecision("reject");
                }
                appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                //save date
                if(!StringUtil.isEmpty(dateStr)){
                    Date date = Formatter.parseDate(dateStr);
                    appPremisesRecommendationDto.setRecomInDate(date);
                }else if(!StringUtil.isEmpty(dateTimeShow) && !"-".equals(dateTimeShow)){
                    Date date = Formatter.parseDate(dateTimeShow);
                    appPremisesRecommendationDto.setRecomInDate(date);
                }
                insRepService.updateRecommendation(appPremisesRecommendationDto);
            }else{
                AppPremisesRecommendationDto appPremisesRecommendationDto=new AppPremisesRecommendationDto();
                appPremisesRecommendationDto.setAppPremCorreId(appPremCorreId);
                appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT);
                appPremisesRecommendationDto.setRecomDecision(InspectionReportConstants.APPROVED);
                if(isAppealType){
                    appPremisesRecommendationDto.setRecomDecision("approve");
                }
                if("other".equals(recommendationStr)){
                    if((isAppealType && isChangePeriodAppealType) || !isAppealType){
                        String number = ParamUtil.getString(bpc.request,"number");
                        if(!StringUtil.isEmpty(number)){
                            appPremisesRecommendationDto.setRecomInNumber(Integer.valueOf(number));
                            String chrono = ParamUtil.getString(bpc.request,"chrono");
                            appPremisesRecommendationDto.setChronoUnit(chrono);
                        }
                    }
                    if(isAppealType){
                        appPremisesRecommendationDto.setRecomDecision("approve");
                        if(isLateFeeAppealType){
                            String returnFee = ParamUtil.getString(bpc.request,"returnFee");
                            appPremisesRecommendationDto.setRemarks(returnFee);
                        }
                    }
                }else{
                    if(isRequestForChange){
                        recommendationStr = "6 DTPE002";
                    }
                    String[] strs=recommendationStr.split("\\s+");
                    appPremisesRecommendationDto.setRecomInNumber( Integer.valueOf(strs[0]));
                    appPremisesRecommendationDto.setChronoUnit(strs[1]);
                }
                //save date
                if(!StringUtil.isEmpty(dateStr)){
                    Date date = Formatter.parseDate(dateStr);
                    appPremisesRecommendationDto.setRecomInDate(date);
                }else if(!StringUtil.isEmpty(dateTimeShow) && !"-".equals(dateTimeShow)){
                    Date date = Formatter.parseDate(dateTimeShow);
                    appPremisesRecommendationDto.setRecomInDate(date);
                }
                insRepService.updateRecommendation(appPremisesRecommendationDto);
            }
            String verified = ParamUtil.getString(bpc.request,"verified");
            String rollBack = ParamUtil.getMaskedString(bpc.request,"rollBack");
            String nextStage=null;

            //62875
            String stage = ParamUtil.getString(bpc.request,"nextStage");

            if(RoleConsts.USER_ROLE_AO3.equals(taskDto.getRoleId()) &&
                    ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(applicationViewDto.getApplicationDto().getStatus())){
                if(!StringUtil.isEmpty(stage)){
                    nextStage = stage;
                }
            }

            if(!StringUtil.isEmpty(rollBack) && ApplicationConsts.PROCESSING_DECISION_ROLLBACK.equals(stage)){
                nextStage = "PROCRB";
            }else if(!StringUtil.isEmpty(verified) && ApplicationConsts.PROCESSING_DECISION_VERIFIED.equals(stage)){
                nextStage = verified;
            }

            //request for information
            if(ApplicationConsts.PROCESSING_DECISION_REQUEST_FOR_INFORMATION.equals(stage)){
                nextStage = stage;
            }

            String reply=ParamUtil.getString(bpc.request,"nextStageReplys");
            if(!StringUtil.isEmpty(reply)){
                nextStage=reply;
            }

            //DMS to end
            if(isDMS){
                if(isRejectDMS){
                    nextStage = "PROCREJ";
                }else{
                    nextStage = "PROCAP";
                }
            }

            if(isWithdrwal){
                if(isRejectWithdrawal){
                    nextStage = "PROCREJ";
                }else{
                    nextStage = "PROCAP";
                }
            }


            log.debug(StringUtil.changeForLog("the nextStage is -->:"+nextStage));
            ParamUtil.setRequestAttr(bpc.request, "crud_action_type", nextStage);

            if(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(applicationViewDto.getApplicationDto().getStatus())
                  || ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING.equals(applicationViewDto.getApplicationDto().getStatus())  ){
                String[] fastTracking =  ParamUtil.getStrings(bpc.request,"fastTracking");
                if(fastTracking!=null){
                    applicationViewDto.getApplicationDto().setFastTracking(true);
                }
            }
            log.debug(StringUtil.changeForLog("the do chooseStage end ...."));
        }
    }

    private void  validateCanApprove(String approveSelect,ApplicationViewDto applicationViewDto,Map<String, String> errMap){
        log.info(StringUtil.changeForLog("The validateCanApprove start ..."));
        log.info(StringUtil.changeForLog("The approveSelect is -->:"+approveSelect));
        if(!StringUtil.isEmpty(approveSelect) && ApplicationConsts.PROCESSING_DECISION_PENDING_APPROVAL.equals(approveSelect)){
            ApplicationDto rfiApplicationDto = applicationService.getApplicationDtoByGroupIdAndStatus(applicationViewDto.getApplicationDto().getAppGrpId(),ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION);
            if(rfiApplicationDto!=null){
                List<AppEditSelectDto>  appEditSelectDtos = applicationService.getAppEditSelectDtos(rfiApplicationDto.getId(),ApplicationConsts.APPLICATION_EDIT_TYPE_RFI);
                if(!IaisCommonUtils.isEmpty(appEditSelectDtos)){
                    AppEditSelectDto appEditSelectDto = appEditSelectDtos.get(0);
                    log.info(StringUtil.changeForLog("The appEditSelectDto id is  -->:"+appEditSelectDto.getId()));
                    if(appEditSelectDto.isPremisesEdit()){
                        errMap.put("nextStage", "You can not submit now, because there is request for information pending now.");
                    }
                }else{
                    log.error(StringUtil.changeForLog("There is the Data error for this Application id -->:"+rfiApplicationDto.getId()));
                }
            }else{
                log.info(StringUtil.changeForLog("This applicationGroup do not have the rfi -->:"+applicationViewDto.getApplicationGroupDto().getGroupNo()));
            }
        }
        log.info(StringUtil.changeForLog("The validateCanApprove end ..."));
    }

    /**
     * StartStep: chooseAckValue
     */

    public void chooseAckValue(BaseProcessClass bpc) throws Exception{
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request,"taskDto");
        String roleId = taskDto.getRoleId();
        String successInfo = "LOLEV_ACK018";
        String nextStage = ParamUtil.getString(bpc.request,"nextStage");
        String nextStageReplys = ParamUtil.getString(bpc.request,"nextStageReplys");
        String verified = "";
        String rollBack = "";
        if(ApplicationConsts.PROCESSING_DECISION_VERIFIED.equals(nextStage)){
            verified = ParamUtil.getString(bpc.request,"verified");
        }else if(ApplicationConsts.PROCESSING_DECISION_ROLLBACK.equals(nextStage)){
            rollBack = ParamUtil.getMaskedString(bpc.request,"rollBack");
        }
        String decisionValue = ParamUtil.getString(bpc.request,"decisionValues");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String status = applicationDto.getStatus();
        String crud_action_type = (String) ParamUtil.getRequestAttr(bpc.request, "crud_action_type");
        //isDMS
        String isDMS = (String) ParamUtil.getSessionAttr(bpc.request, "isDMS");
        //replay "PROCREP"
        if("isDMS".equals(isDMS)){
            if("decisionApproval".equals(decisionValue)){
                //DMS APPROVAL
                successInfo = "LOLEV_ACK026";
            }else if("decisionReject".equals(decisionValue)){
                //DMS REJECT
                successInfo = "LOLEV_ACK027";
            }else{
                successInfo = "LOLEV_ACK028";
            }
        }else{
            //ASO PSO
            if(RoleConsts.USER_ROLE_ASO.equals(roleId) || RoleConsts.USER_ROLE_PSO.equals(roleId)){
                successInfo = "LOLEV_ACK018";
            }
            //ao3 approve and reject
            if(RoleConsts.USER_ROLE_AO3.equals(roleId) && ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(status)){
                //AO3 APPROVAL
                successInfo = "LOLEV_ACK020";
            }else if(RoleConsts.USER_ROLE_AO3.equals(roleId) && ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(status)){
                //AO3 REJECT
                successInfo = "LOLEV_ACK022";
            }
            //verified
            if(!StringUtil.isEmpty(verified)){
                //AO1 -> AO2
                if(RoleConsts.USER_ROLE_AO1.equals(roleId) && RoleConsts.USER_ROLE_AO2.equals(verified)){
                    successInfo = "LOLEV_ACK009";
                }else if(RoleConsts.USER_ROLE_AO2.equals(roleId) && RoleConsts.USER_ROLE_AO3.equals(verified)){
                    //AO2 -> AO3
                    successInfo = "LOLEV_ACK013";
                }
            }else if(!StringUtil.isEmpty(rollBack)){
                //roll back
                successInfo = "LOLEV_ACK002";
                if(RoleConsts.USER_ROLE_AO1.equals(roleId)){
                    //AO1
                    successInfo = "LOLEV_ACK002";
                }else if(RoleConsts.USER_ROLE_AO2.equals(roleId)){
                    //AO2
                    successInfo = "LOLEV_ACK014";
                }else if((RoleConsts.USER_ROLE_AO3.equals(roleId))){
                    //AO3
                    successInfo = "LOLEV_ACK025";
                }
            }else if(RoleConsts.USER_ROLE_AO3.equals(roleId) && ApplicationConsts.PROCESSING_DECISION_ROUTE_TO_DMS.equals(nextStage)){
                //AO3 DMS
                successInfo = "LOLEV_ACK024";
            }
        }

        //give clarification
        if(StringUtil.isEmpty(nextStage) && ApplicationConsts.PROCESSING_DECISION_REPLY.equals(nextStageReplys)){
            successInfo = "LOLEV_ACK028";
        }

        //request for information
        if(ApplicationConsts.PROCESSING_DECISION_REQUEST_FOR_INFORMATION.equals(nextStage)){
            successInfo = MessageUtil.dateIntoMessage("ACKRFI001");
            ParamUtil.setRequestAttr(bpc.request,"rfiSuccessInfo","Y");
        }
        ParamUtil.setRequestAttr(bpc.request,"successInfo",successInfo);
    }

    /**
     * StartStep: rontingTaskToPSO
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToPSO(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException, IOException, TemplateException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToPSO start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_PSO,ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING,RoleConsts.USER_ROLE_PSO);
        log.debug(StringUtil.changeForLog("the do rontingTaskToPSO end ...."));
    }


    /**
     * StartStep: rontingTaskToINS
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToINS(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException, IOException, TemplateException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToINS start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_INS,ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT,RoleConsts.USER_ROLE_INSPECTIOR);
        log.debug(StringUtil.changeForLog("the do rontingTaskToINS end ...."));
    }


    /**
     * StartStep: rontingTaskToASO
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToASO(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException, IOException, TemplateException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToASO start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_ASO,ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING,RoleConsts.USER_ROLE_ASO);
        log.debug(StringUtil.changeForLog("the do rontingTaskToASO end ...."));
    }

    /**
     * StartStep: rontingTaskToAO1
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToAO1(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException, IOException, TemplateException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO1 start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_AO1,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01,RoleConsts.USER_ROLE_AO1);
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO1 end ...."));
    }

    /**
     * StartStep: rontingTaskToAO2
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToAO2(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException, IOException, TemplateException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO2 start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_AO2,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02,RoleConsts.USER_ROLE_AO2);
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO2 end ...."));
    }

    /**
     * StartStep: rontingTaskToAO3
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToAO3(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException, IOException, TemplateException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO3 start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_AO3,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03,RoleConsts.USER_ROLE_AO3);
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO3 end ...."));
    }

    /**
     * StartStep: approve
     *
     * @param bpc
     * @throws
     */
    public void approve(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException, IOException, TemplateException {
        log.debug(StringUtil.changeForLog("the do approve start ...."));
        routingTask(bpc,null,ApplicationConsts.APPLICATION_STATUS_APPROVED,null);
        log.debug(StringUtil.changeForLog("the do approve end ...."));
    }

    /**
     * StartStep: routeToDMS
     *
     * @param bpc
     * @throws
     */
    public void routeToDMS(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do routeToDMS start ...."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
        ApplicationDto application = applicationViewDto.getApplicationDto();
        if(application != null){
            String appNo =  application.getApplicationNo();
            log.info(StringUtil.changeForLog("The appNo is -->:"+appNo));
            //HcsaConsts.ROUTING_STAGE_INS
            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto =  appPremisesRoutingHistoryService.
                    getAppPremisesRoutingHistoryForCurrentStage(appNo,"14848A70-820B-EA11-BE7D-000C29F371DC",RoleConsts.USER_ROLE_INSPECTIOR,ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT);
            if(appPremisesRoutingHistoryDto == null){
                appPremisesRoutingHistoryDto = appPremisesRoutingHistoryService.
                        getAppPremisesRoutingHistoryForCurrentStage(appNo,HcsaConsts.ROUTING_STAGE_ASO);
            }
            if(appPremisesRoutingHistoryDto != null){
                rollBack(bpc,appPremisesRoutingHistoryDto.getStageId(),ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS,
                        appPremisesRoutingHistoryDto.getRoleId(),appPremisesRoutingHistoryDto.getWrkGrpId(),appPremisesRoutingHistoryDto.getActionby());
            }else{
                log.error(StringUtil.changeForLog("can not get the appPremisesRoutingHistoryDto ..."));
            }
        }else{
            log.error(StringUtil.changeForLog("do not have the applicaiton"));
        }
        log.debug(StringUtil.changeForLog("the do routeToDMS end ...."));
    }

    /**
     * StartStep: routeBack
     *
     * @param bpc
     * @throws
     */
    public void routeBack(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do routeBack start ...."));
        String str=ParamUtil.getMaskedString(bpc.request,"rollBack");
        String[] result=str.split(",");
        String satageId=result[0];
        String wrkGpId=result[1];
        String userId=result[2];
        String roleId=result[3];

        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request,"taskDto");
        String userRoleId = taskDto.getRoleId();
        String routeBackStatus = ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_AO;
        if(RoleConsts.USER_ROLE_AO1.equals(userRoleId) || RoleConsts.USER_ROLE_AO2.equals(userRoleId) || RoleConsts.USER_ROLE_AO3.equals(userRoleId)){
            if(HcsaConsts.ROUTING_STAGE_ASO.equals(satageId)){
                routeBackStatus = ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_ASO;
            }else if(HcsaConsts.ROUTING_STAGE_PSO.equals(satageId)){
                routeBackStatus = ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_PSO;
            }else if(HcsaConsts.ROUTING_STAGE_INS.equals(satageId)){
                routeBackStatus = ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_INSPECTOR;
            }
        }else if(RoleConsts.USER_ROLE_PSO.equals(userRoleId)){
            routeBackStatus = ApplicationConsts.APPLICATION_STATUS_PSO_ROUTE_BACK;
        }else if(RoleConsts.USER_ROLE_INSPECTIOR.equals(userRoleId)){
            routeBackStatus = ApplicationConsts.APPLICATION_STATUS_INSPECTOR_ROUTE_BACK;
        }

        //ApplicationConsts.APPLICATION_STATUS_ROUTE_BACK
        if(HcsaConsts.ROUTING_STAGE_ASO.equals(satageId)){
            rollBack(bpc,HcsaConsts.ROUTING_STAGE_ASO,routeBackStatus,RoleConsts.USER_ROLE_ASO,wrkGpId,userId);
        }else if(HcsaConsts.ROUTING_STAGE_PSO.equals(satageId)){
            rollBack(bpc,HcsaConsts.ROUTING_STAGE_PSO,routeBackStatus,RoleConsts.USER_ROLE_PSO,wrkGpId,userId);
        }else if(HcsaConsts.ROUTING_STAGE_INS.equals(satageId)){
            if(RoleConsts.USER_ROLE_AO1.equals(roleId)){
                rollBack(bpc,HcsaConsts.ROUTING_STAGE_AO1,routeBackStatus,RoleConsts.USER_ROLE_AO1,wrkGpId,userId);
            }else{
                rollBack(bpc,HcsaConsts.ROUTING_STAGE_INS,routeBackStatus,RoleConsts.USER_ROLE_INSPECTIOR,wrkGpId,userId);
            }
        }else if(HcsaConsts.ROUTING_STAGE_AO1.equals(satageId)){
            rollBack(bpc,HcsaConsts.ROUTING_STAGE_AO1,routeBackStatus,RoleConsts.USER_ROLE_AO1,wrkGpId,userId);
        }else if(HcsaConsts.ROUTING_STAGE_AO2.equals(satageId)){
            rollBack(bpc,HcsaConsts.ROUTING_STAGE_AO2,routeBackStatus,RoleConsts.USER_ROLE_AO2,wrkGpId,userId);
        }else if(HcsaConsts.ROUTING_STAGE_AO3.equals(satageId)){
            rollBack(bpc,HcsaConsts.ROUTING_STAGE_AO3,routeBackStatus,RoleConsts.USER_ROLE_AO3,wrkGpId,userId);
        }
        log.debug(StringUtil.changeForLog("the do routeBack end ...."));
    }

    /**
     * StartStep: internalEnquiry
     *
     * @param bpc
     * @throws
     */
    public void internalEnquiry(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do internalEnquiry start ...."));
        //TODO:internalEnquiry
        log.debug(StringUtil.changeForLog("the do internalEnquiry end ...."));
    }


    /**
     * StartStep: support
     *
     * @param bpc
     * @throws
     */
    public void support(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do support start ...."));
        //TODO:support

        log.debug(StringUtil.changeForLog("the do support end ...."));
    }

    /**
     * StartStep: broadcast
     *
     * @param bpc
     * @throws
     */
    public void broadcast(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do broadcast start ...."));
        //get the user for this applicationNo
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
          List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = appPremisesRoutingHistoryService.
                  getAppPremisesRoutingHistoryDtosByAppNo(applicationDto.getApplicationNo());
        List<String> userIds = getUserIds(appPremisesRoutingHistoryDtos);
        if(!IaisCommonUtils.isEmpty(userIds)){
            BroadcastOrganizationDto broadcastOrganizationDto = broadcastService.getBroadcastOrganizationDto(
                    applicationDto.getApplicationNo(),AppConsts.DOMAIN_TEMPORARY);
            BroadcastApplicationDto broadcastApplicationDto = new BroadcastApplicationDto();
            //create workgroup
            WorkingGroupDto workingGroupDto = broadcastOrganizationDto.getWorkingGroupDto();
            broadcastOrganizationDto.setRollBackworkingGroupDto((WorkingGroupDto)CopyUtil.copyMutableObject(workingGroupDto));
            if(workingGroupDto ==  null){
                workingGroupDto = new WorkingGroupDto();
                workingGroupDto.setGroupName(applicationDto.getApplicationNo());
                workingGroupDto.setGroupDomain(AppConsts.DOMAIN_TEMPORARY);
            }
            workingGroupDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            broadcastOrganizationDto.setWorkingGroupDto(workingGroupDto);

            //add this user to this workgroup
            List<UserGroupCorrelationDto> userGroupCorrelationDtoList = broadcastOrganizationDto.getUserGroupCorrelationDtoList();
            if(broadcastOrganizationDto.getWorkingGroupDto()!= null && userGroupCorrelationDtoList != null && userGroupCorrelationDtoList.size() > 0){
                List<UserGroupCorrelationDto> cloneUserGroupCorrelationDtos = IaisCommonUtils.genNewArrayList();
                CopyUtil.copyMutableObjectList(userGroupCorrelationDtoList,cloneUserGroupCorrelationDtos);
                broadcastOrganizationDto.setRollBackUserGroupCorrelationDtoList(cloneUserGroupCorrelationDtos);
                userGroupCorrelationDtoList =changeStatusUserGroupCorrelationDtos(userGroupCorrelationDtoList,AppConsts.COMMON_STATUS_ACTIVE);
            }else{
                userGroupCorrelationDtoList = IaisCommonUtils.genNewArrayList();
                for(String id : userIds) {
                    UserGroupCorrelationDto userGroupCorrelationDto = new UserGroupCorrelationDto();
                    userGroupCorrelationDto.setUserId(id);
                    userGroupCorrelationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    userGroupCorrelationDto.setIsLeadForGroup(Integer.valueOf(AppConsts.NO));
                    userGroupCorrelationDtoList.add(userGroupCorrelationDto);
                }
            }
            broadcastOrganizationDto.setUserGroupCorrelationDtoList(userGroupCorrelationDtoList);

            //complated this task and create the history
            TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request,"taskDto");
            taskDto =  completedTask(taskDto);
            broadcastOrganizationDto.setComplateTask(taskDto);
            String internalRemarks = ParamUtil.getString(bpc.request,"internalRemarks");
            String externalRemarks = ParamUtil.getString(bpc.request,"comments");
            String processDecision = ParamUtil.getString(bpc.request,"nextStage");
            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),
                    applicationDto.getStatus(),taskDto.getTaskKey(),null, taskDto.getWkGrpId(),internalRemarks,externalRemarks,processDecision,taskDto.getRoleId());
            broadcastApplicationDto.setComplateTaskHistory(appPremisesRoutingHistoryDto);
            broadcastApplicationDto.setRollBackApplicationDto((ApplicationDto)CopyUtil.copyMutableObject(applicationDto));
            //update application status
            applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST);
            broadcastApplicationDto.setApplicationDto(applicationDto);
            //create the new task and create the history
            TaskDto taskDtoNew = TaskUtil.getTaskDto(applicationDto.getApplicationNo(),taskDto.getTaskKey(),TaskConsts.TASK_TYPE_MAIN_FLOW, taskDto.getRefNo(),null,
                    null,null,0,TaskConsts.TASK_PROCESS_URL_MAIN_FLOW, RoleConsts.USER_ROLE_BROADCAST,IaisEGPHelper.getCurrentAuditTrailDto());
            broadcastOrganizationDto.setCreateTask(taskDtoNew);
            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew =getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),applicationDto.getStatus(),
                    taskDto.getTaskKey(),null, taskDto.getWkGrpId(),null,null,null,RoleConsts.USER_ROLE_AO3);
            broadcastApplicationDto.setNewTaskHistory(appPremisesRoutingHistoryDtoNew);
            //save the broadcast
            broadcastOrganizationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            broadcastApplicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            String submissionId = generateIdClient.getSeqId().getEntity();
            log.info(StringUtil.changeForLog(submissionId));
            broadcastOrganizationDto = broadcastService.svaeBroadcastOrganization(broadcastOrganizationDto,null,submissionId);
            broadcastApplicationDto  = broadcastService.svaeBroadcastApplicationDto(broadcastApplicationDto,null,submissionId);
            //0062460 update FE  application status.
            applicationService.updateFEApplicaiton(broadcastApplicationDto.getApplicationDto());
        }

        log.debug(StringUtil.changeForLog("the do broadcast end ...."));
    }


    /**
     * StartStep: broadcastReply
     *
     * @param bpc
     * @throws
     */
    public void broadcastReply(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException, IOException, TemplateException {
        log.debug(StringUtil.changeForLog("the do broadcastReply start ...."));
        //routingTask(bpc,HcsaConsts.ROUTING_STAGE_AO3,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03,RoleConsts.USER_ROLE_AO3);
        replay(bpc);
        log.debug(StringUtil.changeForLog("the do broadcastReply end ...."));
    }


    /**
     * StartStep: replay
     *
     * @param bpc
     * @throws
     */
    public void replay(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException, IOException, TemplateException {
        log.debug(StringUtil.changeForLog("the do replay start ...."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request,"taskDto");
        String nextStatus = ApplicationConsts.APPLICATION_STATUS_REPLY;
        String getHistoryStatus = applicationViewDto.getApplicationDto().getStatus();
          if(ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(getHistoryStatus)){
              getHistoryStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03;
              nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03;
          }else if(ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(getHistoryStatus)){
              nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03;
          }
        log.info(StringUtil.changeForLog("----------- route back historyStatus : " + getHistoryStatus + "----------"));
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = appPremisesRoutingHistoryService.getSecondRouteBackHistoryByAppNo(
                applicationViewDto.getApplicationDto().getApplicationNo(),getHistoryStatus);
        String wrkGrpId=appPremisesRoutingHistoryDto.getWrkGrpId();
        String roleId=appPremisesRoutingHistoryDto.getRoleId();
        String stageId=appPremisesRoutingHistoryDto.getStageId();
        String userId=appPremisesRoutingHistoryDto.getActionby();
        String subStageId = appPremisesRoutingHistoryDto.getSubStage();

        if(!ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(nextStatus) && HcsaConsts.ROUTING_STAGE_ASO.equals(stageId)){
            nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING;
        }else if(!ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(nextStatus) && HcsaConsts.ROUTING_STAGE_PSO.equals(stageId)){
            nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING;
        }else if(!ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(nextStatus) && HcsaConsts.ROUTING_STAGE_INS.equals(stageId)){
            nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS;
        }else if(!ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(nextStatus) && HcsaConsts.ROUTING_STAGE_AO1.equals(stageId)){
            nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01;
        }else if(!ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(nextStatus) && HcsaConsts.ROUTING_STAGE_AO2.equals(stageId)){
            nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02;
        }else if(!ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(nextStatus) && HcsaConsts.ROUTING_STAGE_AO3.equals(stageId)){
            nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03;
        }

        String routeHistoryId = appPremisesRoutingHistoryDto.getId();
        AppPremisesRoutingHistoryExtDto historyExtDto = appPremisesRoutingHistoryClient.getAppPremisesRoutingHistoryExtByHistoryAndComponentName(routeHistoryId, ApplicationConsts.APPLICATION_ROUTE_BACK_REVIEW).getEntity();
        if(historyExtDto == null){
            rollBack(bpc,stageId,nextStatus,roleId,wrkGrpId,userId);
        }else{
            String componentValue = historyExtDto.getComponentValue();
            if("N".equals(componentValue)){
                ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
                ApplicationGroupDto applicationGroupDto = applicationGroupService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
                List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtoList = applicationViewService.getStage(applicationDto.getServiceId(),
                        stageId,applicationDto.getApplicationType(),applicationGroupDto.getIsPreInspection());
                if(hcsaSvcRoutingStageDtoList != null){
                    HcsaSvcRoutingStageDto nextStage = hcsaSvcRoutingStageDtoList.get(0);
                    String stageCode = nextStage.getStageCode();
                    String routeNextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02;
                    String nextStageId = HcsaConsts.ROUTING_STAGE_AO2;
                    if(RoleConsts.USER_ROLE_AO3.equals(stageCode)){
                        nextStageId = HcsaConsts.ROUTING_STAGE_AO3;
                        routeNextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03;
                    }
                    routingTask(bpc,nextStageId,routeNextStatus,stageCode);
                }else{
                    log.error(StringUtil.changeForLog("RoutingStageDtoList is null"));
                }
            }else{
                rollBack(bpc,stageId,nextStatus,roleId,wrkGrpId,userId);
            }
        }
        log.debug(StringUtil.changeForLog("the do replay end ...."));
    }


    /**
     * StartStep: reject
     *
     * @param bpc
     * @throws
     */
    public void reject(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException, IOException, TemplateException {
        log.debug(StringUtil.changeForLog("the do reject start ...."));
        routingTask(bpc,null,ApplicationConsts.APPLICATION_STATUS_REJECTED,null);
        log.debug(StringUtil.changeForLog("the do reject end ...."));
    }

    private void rejectSendNotification(ApplicationViewDto applicationViewDto)throws FeignException, CloneNotSupportedException, IOException, TemplateException{
        String applicationNo = applicationViewDto.getApplicationDto().getApplicationNo();
        String appGrpId = applicationViewDto.getApplicationDto().getAppGrpId();
        String licenseeId = applicationViewDto.getApplicationGroupDto().getLicenseeId();
        //new application send email
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String applicationType = applicationDto.getApplicationType();
        String msgId = "";
        Map<String, Object> msgInfoMap = IaisCommonUtils.genNewHashMap();
        if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationType)){
            //send email
            sendRejectEmail(applicationNo,licenseeId,appGrpId);
            //send sms
            sendSMS(msgId,licenseeId,msgInfoMap);
        }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationType)){
            MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_REJECT).getEntity();
            if(msgTemplateDto != null){
                Map<String ,Object> tempMap = IaisCommonUtils.genNewHashMap();
                tempMap.put("APP_NO",StringUtil.viewHtml(applicationNo));
                String subject = " " + applicationNo + "– Rejected ";
                sendEmailHelper(tempMap,MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_REJECT,subject,licenseeId,appGrpId);
            }
            //send sms
            sendSMS(msgId,licenseeId,msgInfoMap);
            //send message
            String subject = "MOH IAIS – Renew" + applicationNo + " - Rejected ";
            String mesContext = "renew reject message";
            HashMap<String, String> maskParams = IaisCommonUtils.genNewHashMap();
            sendMessage(subject,licenseeId,mesContext,maskParams,applicationViewDto.getApplicationDto().getServiceId(),null);
        }else if(ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)){
            //send email Appeal - Send SMS to licensee when appeal application is approved
            Map<String,Object> notifyMap=IaisCommonUtils.genNewHashMap();
            sendSMS(msgId,licenseeId,notifyMap);
        }else if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType)){
            //Send notification to transferor when licence transfer application is rejected
            LicenceDto result = null;
            String originLicenceId = applicationViewDto.getApplicationDto().getOriginLicenceId();
            if(!StringUtil.isEmpty(originLicenceId)) {
                result = licenceService.getLicenceDto(originLicenceId);
            }
            Map<String,Object> notifyMap=IaisCommonUtils.genNewHashMap();
            //RFC Application - Send notification to transferor when licence transfer application is rejected
            Map<String,Object> rejectMap=IaisCommonUtils.genNewHashMap();
            rejectMap.put("applicationId",applicationNo);
            sendRFCRejectEmail(licenseeId,applicationViewDto.getApplicationDto().getServiceId());
            //RFC Application - Send SMS to transferee when licence transfer application is rejected
            sendSMS(msgId,licenseeId,notifyMap);
            //RFC Application - Send SMS to transferor when licence transfer application is rejected
            if(result != null){
                sendSMS(msgId,result.getLicenseeId(),notifyMap);
            }else {
                log.info("-----RFC Application - Send SMS to transferor when licence transfer application is rejected. licenseeId is null---------");
            }

            //send sms
            sendSMS(msgId,licenseeId,msgInfoMap);
        }

        try {
            if(ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)){
                sendAppealReject(licenseeId,applicationDto);
            }

        }catch (Exception e){
            log.error(e.getMessage()+"error",e);
        }
    }

    /**
     * StartStep: requestForInformation
     *
     * @param bpc
     * @throws
     */
    public void requestForInformation(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException, IOException, TemplateException {
        log.debug(StringUtil.changeForLog("the do requestForInformation start ...."));
        routingTask(bpc,null,ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION,null);
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String licenseeId = applicationViewDto.getApplicationGroupDto().getLicenseeId();
        LicenseeDto licenseeDto = licenseeService.getLicenseeDtoById(licenseeId);
        String externalRemarks = ParamUtil.getString(bpc.request,"comments");
        String applicationType = applicationDto.getApplicationType();
        try{
            if(ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)){
                AppPremiseMiscDto premiseMiscDto = (AppPremiseMiscDto)ParamUtil.getSessionAttr(bpc.request, "premiseMiscDto");
                if(premiseMiscDto != null){
                    String appealingFor = premiseMiscDto.getRelateRecId();
                    HashMap<String, String> maskParams = IaisCommonUtils.genNewHashMap();
                    maskParams.put("appealingFor",appealingFor);
                    String appealType = (String)ParamUtil.getSessionAttr(bpc.request, "type");
                    sendAppealMessage(appealingFor,licenseeId,maskParams,applicationDto.getServiceId(),appealType,applicationDto.getApplicationNo());
                }
            }
            applicationService.applicationRfiAndEmail(applicationViewDto, applicationDto, licenseeId, licenseeDto, loginContext, externalRemarks);
        }catch (Exception e){
            log.error(StringUtil.changeForLog("send application RfiAndEmail error"));
            log.error(e.getMessage(),e);
        }
        log.debug(StringUtil.changeForLog("the do requestForInformation end ...."));
    }

    /**
     * StartStep: lienceStartDate
     *
     * @param bpc
     * @throws
     */
    public void lienceStartDate(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do lienceStartDate start ...."));
        //TODO:lienceStartDate
        log.debug(StringUtil.changeForLog("the do lienceStartDate end ...."));
    }


    /**
     * StartStep: doDocument
     *
     * @param bpc
     * @throws
     */
    public void doDocument(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doDocument start ...."));
        String doDocument = ParamUtil.getString(bpc.request,"uploadFile");
        String interalFileId = ParamUtil.getMaskedString(bpc.request,"interalFileId");
        if(!StringUtil.isEmpty(interalFileId)){
            fillUpCheckListGetAppClient.deleteAppIntranetDocsById(interalFileId);
        }

        if("Y".equals(doDocument)){
            HcsaApplicationProcessUploadFileValidate uploadFileValidate = new HcsaApplicationProcessUploadFileValidate();
            Map<String, String> errorMap = uploadFileValidate.validate(bpc.request);
            if(!errorMap.isEmpty()){
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request,"uploadFileValidate","Y");
            }else{
                MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
                CommonsMultipartFile selectedFile = (CommonsMultipartFile) mulReq.getFile("selectedFile");
                AppIntranetDocDto appIntranetDocDto = new AppIntranetDocDto();
                //size
                long size = selectedFile.getSize();
                appIntranetDocDto.setDocSize(String.valueOf(size/1024));
                //type
                String[] fileSplit = selectedFile.getOriginalFilename().split("\\.");
                String fileType = fileSplit[fileSplit.length - 1];
                appIntranetDocDto.setDocType(fileType);
                //name
                String fileName = fileSplit[0];
//            String fileName = UUID.randomUUID().toString();
                appIntranetDocDto.setDocName(fileName);

                String fileRemark = ParamUtil.getString(bpc.request,"fileRemark");
                if(StringUtil.isEmpty(fileRemark)){
                    fileRemark = fileName;
                }
                //set document
                appIntranetDocDto.setDocDesc(fileRemark);
                //status
                appIntranetDocDto.setDocStatus(AppConsts.COMMON_STATUS_ACTIVE);
                //APP_PREM_CORRE_ID
                TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request,"taskDto");
                appIntranetDocDto.setAppPremCorrId(taskDto.getRefNo());
                //set audit
                appIntranetDocDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                appIntranetDocDto.setSubmitDt(new Date());
                appIntranetDocDto.setSubmitBy(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());

                FileRepoDto fileRepoDto = new FileRepoDto();
                fileRepoDto.setFileName(fileName);
                fileRepoDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                fileRepoDto.setRelativePath(AppConsts.FALSE);

                //save file to file DB
                String repo_id = fileRepoClient.saveFiles(selectedFile, JsonUtil.parseToJson(fileRepoDto)).getEntity();
                appIntranetDocDto.setFileRepoId(repo_id);
//            appIntranetDocDto.set
                String id = fillUpCheckListGetAppClient.saveAppIntranetDocByAppIntranetDoc(appIntranetDocDto).getEntity();
                appIntranetDocDto.setId(id);
//                ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
            }
        }
        log.debug(StringUtil.changeForLog("the do doDocument end ...."));
    }



    //***************************************
    //private methods
    //*************************************

    private void sendAppealMessage(String appealingFor, String licenseeId,HashMap<String, String> maskParams,String serviceId,String appealType,String appNo){
        if(StringUtil.isEmpty(appealingFor)
                || StringUtil.isEmpty(licenseeId)
                || StringUtil.isEmpty(serviceId)
                || StringUtil.isEmpty(appealType)
                || StringUtil.isEmpty(appNo)){
            return;
        }
        String url = HmacConstants.HTTPS +"://"+systemParamConfig.getInterServerName()+ MessageConstants.MESSAGE_CALL_BACK_URL_Appeal+appealingFor+"&type="+appealType;
        String subject = MessageConstants.MESSAGE_SUBJECT_REQUEST_FOR_INFORMATION + appNo;
        String mesContext = "Please click the link <a href='" + url + "'>here</a> for submission.";
        //send message
        sendMessage(subject,licenseeId,mesContext,maskParams,serviceId,MessageConstants.MESSAGE_TYPE_ACTION_REQUIRED);
    }

    private AppPremisesRecommendationDto getClearRecommendationDto(String appPremCorreId, String dateStr, String dateTimeShow) throws Exception{
        AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
        appPremisesRecommendationDto.setAppPremCorreId(appPremCorreId);
        appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT);
        appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        //save date
        if(!StringUtil.isEmpty(dateStr)){
            Date date = Formatter.parseDate(dateStr);
            appPremisesRecommendationDto.setRecomInDate(date);
        }else if(!StringUtil.isEmpty(dateTimeShow) && !"-".equals(dateTimeShow)){
            Date date = Formatter.parseDate(dateTimeShow);
            appPremisesRecommendationDto.setRecomInDate(date);
        }
        return appPremisesRecommendationDto;
    }

    private void checkRecommendationShowName(BaseProcessClass bpc,ApplicationViewDto applicationViewDto){
        String recommendationShowName = "Recommendation";
        if(ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(applicationViewDto.getApplicationDto().getStatus())){
            recommendationShowName = "Licence Tenure Period";
        }else if(ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationViewDto.getApplicationDto().getApplicationType())){
            recommendationShowName = "Recommended Licence Period";
        }
        ParamUtil.setSessionAttr(bpc.request,"recommendationShowName",recommendationShowName);
    }

    private void checkShowInspection(BaseProcessClass bpc,ApplicationViewDto applicationViewDto,TaskDto taskDto,String correlationId) {
        String status = applicationViewDto.getApplicationDto().getStatus();
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto =  appPremisesRoutingHistoryService.
                            getAppPremisesRoutingHistoryForCurrentStage(applicationViewDto.getApplicationDto().getApplicationNo(),HcsaConsts.ROUTING_STAGE_INS);
        if(appPremisesRoutingHistoryDto == null || ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(status)){
            ParamUtil.setRequestAttr(bpc.request,"isShowInspection","N");
        }else{
            if(ApplicationConsts.APPLICATION_STATUS_INSPECTOR_ROUTE_BACK.equals(status)){
                ParamUtil.setRequestAttr(bpc.request,"isShowInspection","N");
            }else{
                ParamUtil.setRequestAttr(bpc.request,"isShowInspection","Y");
                LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
                InspectionReportDto insRepDto = insRepService.getInsRepDto(taskDto,applicationViewDto,loginContext);
                InspectionReportDto inspectorAo = insRepService.getInspectorAo(taskDto,applicationViewDto);
                insRepDto.setInspectors(inspectorAo.getInspectors());
                ParamUtil.setRequestAttr(bpc.request,"insRepDto",insRepDto);
                String appType = applicationViewDto.getApplicationDto().getApplicationType();
                ParamUtil.setRequestAttr(bpc.request,"appType",appType);
                initAoRecommendation(correlationId,bpc,applicationViewDto.getApplicationDto().getApplicationType());
            }
        }
    }

    private void initAoRecommendation(String correlationId,BaseProcessClass bpc,String appType){
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
        AppPremisesRecommendationDto engageRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPCTION_ENGAGE).getEntity();
        AppPremisesRecommendationDto riskRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPCTION_RISK_LEVEL).getEntity();
        AppPremisesRecommendationDto followRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPCTION_FOLLOW_UP_ACTION).getEntity();

        AppPremisesRecommendationDto initRecommendationDto = new AppPremisesRecommendationDto();
        if (appPremisesRecommendationDto != null) {
            String reportRemarks = appPremisesRecommendationDto.getRemarks();
            initRecommendationDto.setRemarks(reportRemarks);
            initRecommendationDto.setRemarks(reportRemarks);
            Integer recomInNumber = appPremisesRecommendationDto.getRecomInNumber();
            if(recomInNumber != null){
                String recommendationOnlyShowStr = getRecommendationOnlyShowStr(recomInNumber);
                initRecommendationDto.setPeriod(recommendationOnlyShowStr);
            }
            String remarks = appPremisesRecommendationDto.getRemarks();
            initRecommendationDto.setRemarks(remarks);
        }
        if (engageRecommendationDto != null) {
            String remarks = engageRecommendationDto.getRemarks();
            String engage = "on";
            initRecommendationDto.setEngageEnforcement(engage);
            initRecommendationDto.setEngageEnforcementRemarks(remarks);
        }
        if (riskRecommendationDto != null) {
            String riskLevel = riskRecommendationDto.getRecomDecision();
            initRecommendationDto.setRiskLevel(riskLevel);
        }
        if (followRecommendationDto != null) {
            String followRemarks = followRecommendationDto.getRemarks();
            initRecommendationDto.setFollowUpAction(followRemarks);
        }
        if(appPremisesRecommendationDto != null&&ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)){
            String recommendation = appPremisesRecommendationDto.getRecomDecision();
            if(InspectionReportConstants.RFC_APPROVED.equals(recommendation)){
                initRecommendationDto.setPeriod(InspectionReportConstants.APPROVED);
            }
            if(InspectionReportConstants.RFC_REJECTED.equals(recommendation)){
                initRecommendationDto.setPeriod(InspectionReportConstants.REJECTED);
            }
        }
        ParamUtil.setSessionAttr(bpc.request, "appPremisesRecommendationDto", initRecommendationDto);
    }

    private void sendRejectEmail(String applicationNo,String licenseeId,String groupNo){
        //send email
        MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_REJECTED_ID).getEntity();
        if(msgTemplateDto != null){
            Map<String ,Object> tempMap = IaisCommonUtils.genNewHashMap();
            tempMap.put("applicationNumber",StringUtil.viewHtml(applicationNo));
            tempMap.put("MOH_AGENCY_NAME",AppConsts.MOH_AGENCY_NAME);

            String mesContext = null;
            try {
                mesContext = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), tempMap);
            } catch (IOException | TemplateException e) {
                log.error(e.getMessage(),e);
            }

            EmailDto emailDto = new EmailDto();
            emailDto.setContent(mesContext);
            emailDto.setSubject(" " + msgTemplateDto.getTemplateName() + " " + applicationNo + " is Rejected");
            emailDto.setSender(mailSender);
            emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
            emailDto.setClientQueryCode(groupNo);
            //send
            emailClient.sendNotification(emailDto).getEntity();
        }
    }

    public void sendRouteBackEmail(String licenseeId,ApplicationViewDto applicationViewDto) throws Exception{
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String applicationType = applicationDto.getApplicationType();
        if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationType)){
            String mesContext = "Internal route back context";
            EmailDto emailDto = new EmailDto();
            emailDto.setContent(mesContext);
            emailDto.setSubject("MOH IAIS – Internal Clarification for New Application");
            emailDto.setSender(mailSender);
            emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
            emailDto.setClientQueryCode(applicationViewDto.getApplicationDto().getAppGrpId());
            //send
            emailClient.sendNotification(emailDto).getEntity();
        }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationType)){
            String mesContext = "Internal route back context";
            EmailDto emailDto = new EmailDto();
            emailDto.setContent(mesContext);
            emailDto.setSubject("MOH IAIS – Internal Clarification for Renew Application");
            emailDto.setSender(mailSender);
            emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
            emailDto.setClientQueryCode(applicationViewDto.getApplicationDto().getAppGrpId());
            //send
            emailClient.sendNotification(emailDto).getEntity();
        }else if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType)){
            String mesContext = "Internal route back context";
            EmailDto emailDto = new EmailDto();
            emailDto.setContent(mesContext);
            emailDto.setSubject("MOH IAIS – Internal Clarification for Request for Change");
            emailDto.setSender(mailSender);
            emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
            emailDto.setClientQueryCode(applicationViewDto.getApplicationDto().getAppGrpId());
            //send
            emailClient.sendNotification(emailDto).getEntity();
        }else if(ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)){
            String mesContext = "Internal route back context";
            EmailDto emailDto = new EmailDto();
            emailDto.setContent(mesContext);
            emailDto.setSubject("MOH IAIS – Internal Clarification for Appeal Application");
            emailDto.setSender(mailSender);
            emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
            emailDto.setClientQueryCode(applicationViewDto.getApplicationDto().getAppGrpId());
            //send
            emailClient.sendNotification(emailDto).getEntity();
        }else if(ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationType)){
            String mesContext = "Internal route back context";
            EmailDto emailDto = new EmailDto();
            emailDto.setContent(mesContext);
            emailDto.setSubject("MOH IAIS – Internal Clarification for Cessation Application");
            emailDto.setSender(mailSender);
            emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
            emailDto.setClientQueryCode(applicationViewDto.getApplicationDto().getAppGrpId());
            //send
            emailClient.sendNotification(emailDto).getEntity();
        }
    }

    private void checkRecommendationDropdownValue(Integer  recomInNumber,String chronoUnit,String codeDesc,ApplicationViewDto applicationViewDto,BaseProcessClass bpc){
        if(StringUtil.isEmpty(recomInNumber)){
            ParamUtil.setRequestAttr(bpc.request,"recommendationStr","");
            return;
        }else if(recomInNumber == 0){
            ParamUtil.setRequestAttr(bpc.request,"recommendationStr","reject");
            return;
        }
        HcsaServiceDto hcsaServiceDto=applicationViewService.getHcsaServiceDtoById(applicationViewDto.getApplicationDto().getServiceId());
        String svcCode=hcsaServiceDto.getSvcCode();
        RiskAcceptiionDto riskAcceptiionDto=new RiskAcceptiionDto();
        riskAcceptiionDto.setScvCode(svcCode);
        List<RiskAcceptiionDto> listRiskAcceptiionDto = IaisCommonUtils.genNewArrayList();
        listRiskAcceptiionDto.add(riskAcceptiionDto);
        List<RiskResultDto> listRiskResultDto = hcsaConfigClient.getRiskResult(listRiskAcceptiionDto).getEntity();
        boolean flag = true;
        boolean isRequestForChange = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationViewDto.getApplicationDto().getApplicationType());
        if(listRiskResultDto!=null && !listRiskResultDto.isEmpty()) {
            for (RiskResultDto riskResultDto : listRiskResultDto) {
                String dateType = riskResultDto.getDateType();
                String count = String.valueOf(riskResultDto.getTimeCount());
                String recommTime = count + " " + dateType;
                if(recommTime.equals(recomInNumber + " "+ chronoUnit)){
                    ParamUtil.setRequestAttr(bpc.request,"recommendationStr",recommTime);
                    if(isRequestForChange){
                        ParamUtil.setRequestAttr(bpc.request,"recommendationStr","approve");
                    }
                    flag = false;
                }
            }
            if(flag){
                ParamUtil.setRequestAttr(bpc.request,"recommendationStr","other");
                ParamUtil.setRequestAttr(bpc.request,"otherNumber",recomInNumber);
                ParamUtil.setRequestAttr(bpc.request,"otherChrono",chronoUnit);
            }
        }
    }

    private List<SelectOption> getRecommendationOtherDropdown(){
        List<SelectOption> recommendationOtherSelectOption = IaisCommonUtils.genNewArrayList();
        recommendationOtherSelectOption.add(new SelectOption(RiskConsts.YEAR, "Year(s)"));
        recommendationOtherSelectOption.add(new SelectOption(RiskConsts.MONTH, "Month(s)"));
        return recommendationOtherSelectOption;
    }

    private List<SelectOption> getRecommendationDropdown(ApplicationViewDto applicationViewDto,HttpServletRequest request){
        List<SelectOption> recommendationSelectOption = IaisCommonUtils.genNewArrayList();
        HcsaServiceDto hcsaServiceDto=applicationViewService.getHcsaServiceDtoById(applicationViewDto.getApplicationDto().getServiceId());
        String svcCode=hcsaServiceDto.getSvcCode();
        RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
        riskAcceptiionDto.setScvCode(svcCode);
        riskAcceptiionDto.setRiskScore(applicationViewDto.getApplicationDto().getRiskScore());
        List<RiskAcceptiionDto> listRiskAcceptiionDto = IaisCommonUtils.genNewArrayList();
        listRiskAcceptiionDto.add(riskAcceptiionDto);
        List<RiskResultDto> listRiskResultDto = hcsaConfigClient.getRiskResult(listRiskAcceptiionDto).getEntity();
        String applicationType = applicationViewDto.getApplicationDto().getApplicationType();
        //request for change application type recommendation only has 'reject' and 'approve'
        boolean isRequestForChange = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType);
        if(listRiskResultDto!=null && !listRiskResultDto.isEmpty() && !isRequestForChange) {
            for (RiskResultDto riskResultDto : listRiskResultDto) {
                String dateType = riskResultDto.getDateType();
//                String codeDesc = MasterCodeUtil.getCodeDesc(dateType);
                String count = String.valueOf(riskResultDto.getTimeCount());
                //String recommTime = count + " " + codeDesc;
                recommendationSelectOption.add(new SelectOption(count + " " + dateType, riskResultDto.getLictureText()));
                if(riskResultDto.isDafLicture()){
                    //recommendationStr
                    ParamUtil.setRequestAttr(request,"recommendationStr",count + " " + dateType);
                }
            }
        }
        if(!isRequestForChange){
            recommendationSelectOption.add(new SelectOption("other","Others"));
        }
        if(isRequestForChange){
            recommendationSelectOption.add(new SelectOption("approve","Approve"));
            ParamUtil.setSessionAttr(request,"isRequestForChange","Y");
        }
        if(!ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(applicationViewDto.getApplicationDto().getStatus())){
            recommendationSelectOption.add(new SelectOption("reject","Reject"));
        }
        return recommendationSelectOption;
    }

    public void routingTask(BaseProcessClass bpc,String stageId,String appStatus,String roleId ) throws FeignException, CloneNotSupportedException, IOException, TemplateException {
        log.info(StringUtil.changeForLog("The routingTask start ..."));
        //get the user for this applicationNo
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        AppPremisesCorrelationDto newAppPremisesCorrelationDto = applicationViewDto.getNewAppPremisesCorrelationDto();
        String newCorrelationId = newAppPremisesCorrelationDto.getId();
        BroadcastOrganizationDto broadcastOrganizationDto = new BroadcastOrganizationDto();
        BroadcastApplicationDto broadcastApplicationDto = new BroadcastApplicationDto();
        String internalRemarks = ParamUtil.getString(bpc.request,"internalRemarks");
        String externalRemarks = ParamUtil.getString(bpc.request,"comments");
        String processDecision = ParamUtil.getString(bpc.request,"nextStage");
        String nextStageReplys = ParamUtil.getString(bpc.request, "nextStageReplys");
        if(!StringUtil.isEmpty(nextStageReplys) && StringUtil.isEmpty(processDecision)){
            processDecision = nextStageReplys;
        }
        log.info(StringUtil.changeForLog("The processDecision is -- >:"+processDecision));
        //judge the final status is Approve or Reject.
        AppPremisesRecommendationDto appPremisesRecommendationDto = applicationViewDto.getAppPremisesRecommendationDto();
        String applicationType = applicationDto.getApplicationType();
        if(appPremisesRecommendationDto!= null && ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(appStatus)){
                if(!ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)){
                    Integer recomInNumber =  appPremisesRecommendationDto.getRecomInNumber();
                    if(null != recomInNumber && recomInNumber == 0){
                        appStatus =  ApplicationConsts.APPLICATION_STATUS_REJECTED;
                    }
                }else{
                    String recomDecision = appPremisesRecommendationDto.getRecomDecision();
                    if("reject".equals(recomDecision)){
                        appStatus =  ApplicationConsts.APPLICATION_STATUS_REJECTED;
                    }
                }
         }
        //send reject email
        if(ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(appStatus)){
            try{
                rejectSendNotification(applicationViewDto);
            }catch (Exception e){
                log.error(StringUtil.changeForLog("send reject notification error"));
            }
        }
        //appeal save return fee
        if(ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(appStatus)){
            if(ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)){
                String returnFee = appPremisesRecommendationDto.getRemarks();
                if(!StringUtil.isEmpty(returnFee)){
                    String oldApplicationNo = (String)ParamUtil.getSessionAttr(bpc.request, "oldApplicationNo");
                    if(!StringUtil.isEmpty(oldApplicationNo)){
                        AppReturnFeeDto appReturnFeeDto = new AppReturnFeeDto();
                        appReturnFeeDto.setApplicationNo(oldApplicationNo);
                        appReturnFeeDto.setReturnAmount(Double.parseDouble(returnFee));
                        appReturnFeeDto.setReturnType(ApplicationConsts.APPLICATION_RETURN_FEE_TYPE_APPEAL);
                        List<AppReturnFeeDto> saveReturnFeeDtos = IaisCommonUtils.genNewArrayList();
                        saveReturnFeeDtos.add(appReturnFeeDto);
                        broadcastApplicationDto.setReturnFeeDtos(saveReturnFeeDtos);
                        broadcastApplicationDto.setRollBackReturnFeeDtos(saveReturnFeeDtos);
                    }
                }
            }else if (ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(applicationType)){
                if (applicationDto != null){
                    AppPremiseMiscDto premiseMiscDto = cessationClient.getAppPremiseMiscDtoByAppId(applicationDto.getId()).getEntity();
                    AppReturnFeeDto appReturnFeeDto = new AppReturnFeeDto();
                    String oldAppId = premiseMiscDto.getRelateRecId();
                    ApplicationDto oldApplication = applicationClient.getApplicationById(oldAppId).getEntity();
                    appReturnFeeDto.setApplicationNo(oldApplication.getApplicationNo());
                    appReturnFeeDto.setReturnAmount(oldApplication.getReturnFee());
                    appReturnFeeDto.setReturnType(ApplicationConsts.APPLICATION_RETURN_FEE_TYPE_WITHDRAW);
                    List<AppReturnFeeDto> saveReturnFeeDtos = IaisCommonUtils.genNewArrayList();
                    saveReturnFeeDtos.add(appReturnFeeDto);
                    broadcastApplicationDto.setReturnFeeDtos(saveReturnFeeDtos);
                    broadcastApplicationDto.setRollBackReturnFeeDtos(saveReturnFeeDtos);
                }
            }
        }

        //completed this task and create the history
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request,"taskDto");
        broadcastOrganizationDto.setRollBackComplateTask((TaskDto) CopyUtil.copyMutableObject(taskDto));
        taskDto =  completedTask(taskDto);
        broadcastOrganizationDto.setComplateTask(taskDto);
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),
                applicationDto.getStatus(),taskDto.getTaskKey(),null, taskDto.getWkGrpId(),internalRemarks,externalRemarks,processDecision,taskDto.getRoleId());
        broadcastApplicationDto.setComplateTaskHistory(appPremisesRoutingHistoryDto);
        //update application status
        broadcastApplicationDto.setRollBackApplicationDto((ApplicationDto) CopyUtil.copyMutableObject(applicationDto));
        String oldStatus = applicationDto.getStatus();
        applicationDto.setStatus(appStatus);
        //cessation
        if(ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(appStatus)&&ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationType)){
            applicationDto.setGroupLicenceFlag(ApplicationConsts.GROUP_LICENCE_FLAG_ORIGIN);
            applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_CESSATION_NEED_LICENCE);
            applicationDto.setNeedNewLicNo(true);
        }
        broadcastApplicationDto.setApplicationDto(applicationDto);
        // send the task
        if(!StringUtil.isEmpty(stageId)){
            //For the BROADCAST Rely
            if(ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(oldStatus)){
                AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto1 = appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryForCurrentStage(
                        applicationDto.getApplicationNo(),stageId
                );
                log.debug(StringUtil.changeForLog("The appId is-->;"+ applicationDto.getId()));
                log.debug(StringUtil.changeForLog("The stageId is-->;"+ stageId));
                if(appPremisesRoutingHistoryDto1 != null){
                    TaskDto newTaskDto = TaskUtil.getTaskDto(applicationDto.getApplicationNo(),stageId,TaskConsts.TASK_TYPE_MAIN_FLOW,
                            taskDto.getRefNo(),appPremisesRoutingHistoryDto1.getWrkGrpId(),
                            appPremisesRoutingHistoryDto1.getActionby(),new Date(),0,TaskConsts.TASK_PROCESS_URL_MAIN_FLOW,roleId,
                            IaisEGPHelper.getCurrentAuditTrailDto());
                    broadcastOrganizationDto.setCreateTask(newTaskDto);
                    //delete workgroup
                    BroadcastOrganizationDto broadcastOrganizationDto1 = broadcastService.getBroadcastOrganizationDto(
                            applicationDto.getApplicationNo(),AppConsts.DOMAIN_TEMPORARY);

                    WorkingGroupDto workingGroupDto = broadcastOrganizationDto1.getWorkingGroupDto();
                    broadcastOrganizationDto.setRollBackworkingGroupDto((WorkingGroupDto) CopyUtil.copyMutableObject(workingGroupDto));
                    workingGroupDto = changeStatusWrokGroup(workingGroupDto,AppConsts.COMMON_STATUS_DELETED);
                    broadcastOrganizationDto.setWorkingGroupDto(workingGroupDto);
                    List<UserGroupCorrelationDto> userGroupCorrelationDtos = broadcastOrganizationDto1.getUserGroupCorrelationDtoList();
                    List<UserGroupCorrelationDto> cloneUserGroupCorrelationDtos = IaisCommonUtils.genNewArrayList();
                    CopyUtil.copyMutableObjectList(userGroupCorrelationDtos,cloneUserGroupCorrelationDtos);
                    broadcastOrganizationDto.setRollBackUserGroupCorrelationDtoList(cloneUserGroupCorrelationDtos);
                    userGroupCorrelationDtos = changeStatusUserGroupCorrelationDtos(userGroupCorrelationDtos,AppConsts.COMMON_STATUS_DELETED);
                    broadcastOrganizationDto.setUserGroupCorrelationDtoList(userGroupCorrelationDtos);
                }else{
                    throw new IaisRuntimeException("This getAppPremisesCorrelationId can not get the broadcast -- >:"+applicationViewDto.getAppPremisesCorrelationId());
                }
            }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(appStatus)){
                if(applicationDto.isFastTracking()){
                    TaskDto newTaskDto = taskService.getRoutingTask(applicationDto,stageId,roleId,newCorrelationId);
                    broadcastOrganizationDto.setCreateTask(newTaskDto);
                }
                List<ApplicationDto> applicationDtoList = applicationService.getApplicaitonsByAppGroupId(applicationDto.getAppGrpId());
                applicationDtoList = removeFastTrackingAndTransfer(applicationDtoList);
                boolean isAllSubmit = applicationService.isOtherApplicaitonSubmit(applicationDtoList,applicationDto.getApplicationNo(),
                        ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03);
                if(isAllSubmit){
                    // send the task to Ao3
                    TaskHistoryDto taskHistoryDto = taskService.getRoutingTaskOneUserForSubmisison(applicationDtoList,
                            HcsaConsts.ROUTING_STAGE_AO3,roleId,IaisEGPHelper.getCurrentAuditTrailDto());
                    List<TaskDto> taskDtos = taskHistoryDto.getTaskDtoList();
                    List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = taskHistoryDto.getAppPremisesRoutingHistoryDtos();
                    broadcastOrganizationDto.setOneSubmitTaskList(taskDtos);
                    broadcastApplicationDto.setOneSubmitTaskHistoryList(appPremisesRoutingHistoryDtos);
                }
            }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT.equals(appStatus)){
                AppInspectionStatusDto appInspectionStatusDto = new AppInspectionStatusDto();
                appInspectionStatusDto.setAppPremCorreId(taskDto.getRefNo());
                appInspectionStatusDto.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_PRE);
                broadcastApplicationDto.setAppInspectionStatusDto(appInspectionStatusDto);
                TaskDto newTaskDto = taskService.getRoutingTask(applicationDto,stageId,roleId,newCorrelationId);
                broadcastOrganizationDto.setCreateTask(newTaskDto);
                AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew =getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),applicationDto.getStatus(),stageId,HcsaConsts.ROUTING_STAGE_PRE,
                        taskDto.getWkGrpId(),null,null,externalRemarks,roleId);
                broadcastApplicationDto.setNewTaskHistory(appPremisesRoutingHistoryDtoNew);
            }else{
                TaskDto newTaskDto = taskService.getRoutingTask(applicationDto,stageId,roleId,newCorrelationId);
                broadcastOrganizationDto.setCreateTask(newTaskDto);
            }
            //add history for next stage start
            if(!(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(appStatus)&&!applicationDto.isFastTracking())&&!ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT.equals(appStatus)){
                AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew =getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),applicationDto.getStatus(),stageId,null,
                        taskDto.getWkGrpId(),null,null,externalRemarks,roleId);
                broadcastApplicationDto.setNewTaskHistory(appPremisesRoutingHistoryDtoNew);
            }
        }else{
            //0065354
            if(!ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION.equals(appStatus)){
                List<ApplicationDto> applicationDtoList = applicationService.getApplicaitonsByAppGroupId(applicationDto.getAppGrpId());
                List<ApplicationDto> saveApplicationDtoList = IaisCommonUtils.genNewArrayList();
                CopyUtil.copyMutableObjectList(applicationDtoList,saveApplicationDtoList);
                applicationDtoList = removeFastTrackingAndTransfer(applicationDtoList);
                boolean isAllSubmit = applicationService.isOtherApplicaitonSubmit(applicationDtoList,applicationDto.getApplicationNo(),
                        ApplicationConsts.APPLICATION_STATUS_APPROVED);
                if(isAllSubmit || applicationDto.isFastTracking()){
                    //update application Group status
                    ApplicationGroupDto applicationGroupDto = applicationGroupService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
                    broadcastApplicationDto.setRollBackApplicationGroupDto((ApplicationGroupDto)CopyUtil.copyMutableObject(applicationGroupDto));
                    applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_APPROVED);
                    applicationGroupDto.setAo3ApprovedDt(new Date());
                    applicationGroupDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    broadcastApplicationDto.setApplicationGroupDto(applicationGroupDto);
                    if(isAllSubmit){
                        //update current application status in db search result
                        updateCurrentApplicationStatus(saveApplicationDtoList,applicationDto.getId(),appStatus);
                        //get and set return fee
                        saveApplicationDtoList = hcsaConfigClient.returnFee(saveApplicationDtoList).getEntity();
                        //save return fee
                        saveRejectReturnFee(saveApplicationDtoList,broadcastApplicationDto);
                    }
                }
            }else{
                log.info(StringUtil.changeForLog("This RFI  this application -->:"+applicationDto.getApplicationNo()));
            }

        }
        //save the broadcast
        broadcastOrganizationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        broadcastApplicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        String evenRefNum = String.valueOf(System.currentTimeMillis());
        broadcastOrganizationDto.setEventRefNo(evenRefNum);
        broadcastApplicationDto.setEventRefNo(evenRefNum);
        String submissionId = generateIdClient.getSeqId().getEntity();
        log.info(StringUtil.changeForLog(submissionId));
        broadcastOrganizationDto = broadcastService.svaeBroadcastOrganization(broadcastOrganizationDto,bpc.process,submissionId);
        broadcastApplicationDto  = broadcastService.svaeBroadcastApplicationDto(broadcastApplicationDto,bpc.process,submissionId);
        //0062460 update FE  application status.
        applicationService.updateFEApplicaiton(broadcastApplicationDto.getApplicationDto());

        //appeal save return fee


            ApplicationDto withdrawApplicationDto = broadcastApplicationDto.getApplicationDto();
            if (withdrawApplicationDto != null){
                /**
                 * Send Withdrawal Application Email
                 */
                if (ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(withdrawApplicationDto.getApplicationType())){
                    String licenseeId = applicationViewDto.getApplicationGroupDto().getLicenseeId();
                    String serviceId = applicationViewDto.getApplicationDto().getServiceId();
                    String applicationNo = applicationViewDto.getApplicationDto().getApplicationNo();
                    String serviceName = HcsaServiceCacheHelper.getServiceById(serviceId).getSvcName();
                    if (ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(withdrawApplicationDto.getStatus())){
                        applicationService.closeTaskWhenWhAppApprove(withdrawApplicationDto.getId());
                        String subjectSuppInfo = applicationNo + " is Approved";
                        Map<String, Object> msgInfoMap = IaisCommonUtils.genNewHashMap();
                        msgInfoMap.put("appNum", applicationNo);
                        msgInfoMap.put("reqAppNo",applicationNo);
                        msgInfoMap.put("S_LName",serviceName);
                        msgInfoMap.put("MOH_AGENCY_NAME",AppConsts.MOH_AGENCY_NAME);
                        EmailDto emailDto = sendEmail(MsgTemplateConstants.MSG_TEMPLATE_WITHDRAWAL_APP_APPROVE,msgInfoMap,withdrawApplicationDto.getApplicationNo(),licenseeId,subjectSuppInfo);
                        sendInboxMessage(licenseeId,null,emailDto.getContent(),serviceId,emailDto.getSubject());
                        /**
                         *  Send SMS when withdrawal Application Approve
                         */
                        Map<String, Object> smsInfoMap = IaisCommonUtils.genNewHashMap();
                        sendSMS("",taskDto.getUserId(),smsInfoMap);
                    }else{
                        String subjectSuppInfo = applicationNo + " is Rejected";
                        Map<String, Object> msgInfoMap = IaisCommonUtils.genNewHashMap();
                        msgInfoMap.put("appNum", applicationNo);
                        msgInfoMap.put("MOH_AGENCY_NAME",AppConsts.MOH_AGENCY_NAME);
                        EmailDto emailDto = sendEmail(MsgTemplateConstants.MSG_TEMPLATE_WITHDRAWAL_APP_REJECT,msgInfoMap,withdrawApplicationDto.getApplicationNo(),licenseeId,subjectSuppInfo);
                        sendInboxMessage(licenseeId,null,emailDto.getContent(),serviceId,emailDto.getSubject());
                        /**
                         *  Send SMS when withdrawal Application Reject
                         */
                        Map<String, Object> smsInfoMap = IaisCommonUtils.genNewHashMap();
                        sendSMS("",taskDto.getUserId(),smsInfoMap);
                    }
                    Map<String, Object> msgInfoMap = IaisCommonUtils.genNewHashMap();
                    sendEmail("",msgInfoMap,applicationNo,taskDto.getUserId(),"");
                }
            }

        log.info(StringUtil.changeForLog("The routingTask end ..."));
    }

    private void updateFeApplications(List<ApplicationDto> applications){
        try{
            applicationService.updateFEApplicaitons(applications);
        }catch (Exception e){
            log.error(StringUtil.changeForLog("update fe applications error"));
        }
    }

    private void sendInboxMessage(String licenseeId,HashMap<String, String> maskParams,String templateMessageByContent,String serviceId,String subject){
        HcsaServiceDto serviceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
        String refNo = inboxMsgService.getMessageNo();
        InterMessageDto interMessageDto = new InterMessageDto();
        interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY);
        interMessageDto.setMessageType(MessageConstants.MESSAGE_TYPE_NOTIFICATION);
        interMessageDto.setSubject(subject);
        interMessageDto.setRefNo(refNo);
        if(serviceDto != null){
            interMessageDto.setService_id(serviceDto.getSvcCode()+"@");
        }
        interMessageDto.setUserId(licenseeId);
        interMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        interMessageDto.setMsgContent(templateMessageByContent);
        interMessageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        interMessageDto.setMaskParams(maskParams);
        inboxMsgService.saveInterMessage(interMessageDto);
    }

    private void saveRejectReturnFee(List<ApplicationDto> applicationDtos,BroadcastApplicationDto broadcastApplicationDto){
        List<AppReturnFeeDto> saveReturnFeeDtos = IaisCommonUtils.genNewArrayList();
        //save return fee
        for(ApplicationDto applicationDto : applicationDtos){
            if(ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(applicationDto.getStatus()) && !ApplicationConsts.APPLICATION_STATUS_WITHDRAWN.equals(applicationDto.getStatus())){
                AppReturnFeeDto appReturnFeeDto = new AppReturnFeeDto();
                appReturnFeeDto.setApplicationNo(applicationDto.getApplicationNo());
                appReturnFeeDto.setReturnAmount(applicationDto.getReturnFee());
                appReturnFeeDto.setReturnType(ApplicationConsts.APPLICATION_RETURN_FEE_REJECT);
                saveReturnFeeDtos.add(appReturnFeeDto);
//                applicationService.saveAppReturnFee(appReturnFeeDto);
            }
        }
        if(!IaisCommonUtils.isEmpty(saveReturnFeeDtos)){
            broadcastApplicationDto.setReturnFeeDtos(saveReturnFeeDtos);
            broadcastApplicationDto.setRollBackReturnFeeDtos(saveReturnFeeDtos);
        }
    }

    private EmailDto sendEmail(String msgId, Map<String, Object> msgInfoMap, String applicationNo, String licenseeId,String subjectSuppInfo) {
        EmailDto emailDto=new EmailDto();
            //        MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(msgId).getEntity();
//        String templateMessageByContent = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), msgInfoMap);
            emailDto.setClientQueryCode(applicationNo);
            emailDto.setSender(mailSender);
//        emailDto.setContent(templateMessageByContent);
            emailDto.setContent("Send Email");
//        emailDto.setSubject(msgTemplateDto.getTemplateName()+subjectSuppInfo);
            emailDto.setSubject(subjectSuppInfo);
            try{
                List<String> emailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(licenseeId);
                if (emailAddrs != null && emailAddrs.size()>0){
                    emailDto.setReceipts(emailAddrs);
                    emailClient.sendNotification(emailDto).getEntity();
                }
            }catch (Exception e){
                log.error(StringUtil.changeForLog("error"));
            }
            return emailDto;
    }

    private void sendSMS(String msgId,String licenseeId,Map<String, Object> msgInfoMap){
        //MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(msgId).getEntity();
        //String templateMessageByContent = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), msgInfoMap);
        String templateMessageByContent = "send sms";
        SmsDto smsDto = new SmsDto();
        smsDto.setSender(mailSender);
        smsDto.setContent(templateMessageByContent);
        smsDto.setOnlyOfficeHour(true);
        String refNo = inboxMsgService.getMessageNo();
        try{
            List<String> recipts = IaisEGPHelper.getLicenseeMobiles(licenseeId);
            if (!IaisCommonUtils.isEmpty(recipts)) {
                emailClient.sendSMS(recipts,smsDto,refNo);
            }
        }catch (Exception e){
            log.error(StringUtil.changeForLog("error"));
        }
    }

    private List<ApplicationDto> removeFastTrackingAndTransfer(List<ApplicationDto> applicationDtos){
        List<ApplicationDto> result = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(applicationDtos)){
         for (ApplicationDto applicationDto : applicationDtos){
             if(ApplicationConsts.APPLICATION_STATUS_TRANSFER_ORIGIN.equals(applicationDto.getStatus())){
                 continue;
             }
             if(!applicationDto.isFastTracking()){
                 result.add(applicationDto);
             }
         }
        }
        return  result;
    }

    private void updateCurrentApplicationStatus(List<ApplicationDto> applicationDtos,String applicationId,String status){
        if(!IaisCommonUtils.isEmpty(applicationDtos) && !StringUtil.isEmpty(applicationId)){
            for (ApplicationDto applicationDto : applicationDtos){
                if(applicationId.equals(applicationDto.getId())){
                    applicationDto.setStatus(status);
                }
            }
        }
    }

    private ApplicationDto getCurrentApplicationDto(List<ApplicationDto> applicationDtos,String applicationId){
        ApplicationDto applicationDto = null;
        if(IaisCommonUtils.isEmpty(applicationDtos) || StringUtil.isEmpty(applicationId)){
            return null;
        }
        for(ApplicationDto app : applicationDtos){
            if(applicationId.equals(app.getId())){
                applicationDto = app;
                break;
            }
        }
        return applicationDto;
    }

    private void rollBack(BaseProcessClass bpc,String stageId,String appStatus,String roleId ,String wrkGpId,String userId) throws CloneNotSupportedException {
        //get the user for this applicationNo
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
        //send internal route back email
        String licenseeId = applicationViewDto.getApplicationGroupDto().getLicenseeId();
        try{
            sendRouteBackEmail(licenseeId,applicationViewDto);
        }catch (Exception e){
            log.error(StringUtil.changeForLog("send internal route back email error"));
        }
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        BroadcastOrganizationDto broadcastOrganizationDto = new BroadcastOrganizationDto();
        BroadcastApplicationDto broadcastApplicationDto = new BroadcastApplicationDto();

        //complated this task and create the history
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request,"taskDto");
        String refNo = taskDto.getRefNo();
        String subStageId = null;
        broadcastOrganizationDto.setRollBackComplateTask((TaskDto) CopyUtil.copyMutableObject(taskDto));
        taskDto =  completedTask(taskDto);
        broadcastOrganizationDto.setComplateTask(taskDto);
        String internalRemarks = ParamUtil.getString(bpc.request,"internalRemarks");
        String processDecision = ParamUtil.getString(bpc.request,"nextStage");
        String nextStageReplys = ParamUtil.getString(bpc.request, "nextStageReplys");
        if(!StringUtil.isEmpty(nextStageReplys) && StringUtil.isEmpty(processDecision)){
            processDecision = nextStageReplys;
        }

        String routeBackReview = (String)ParamUtil.getSessionAttr(bpc.request,"routeBackReview");
        if("canRouteBackReview".equals(routeBackReview)){
            AppPremisesRoutingHistoryExtDto appPremisesRoutingHistoryExtDto = new AppPremisesRoutingHistoryExtDto();
            appPremisesRoutingHistoryExtDto.setComponentName(ApplicationConsts.APPLICATION_ROUTE_BACK_REVIEW);
            String[] routeBackReviews =  ParamUtil.getStrings(bpc.request,"routeBackReview");
            if(routeBackReviews!=null){
                appPremisesRoutingHistoryExtDto.setComponentValue("Y");
            }else{
                appPremisesRoutingHistoryExtDto.setComponentValue("N");
                //route back and route task processing
                processDecision = ApplicationConsts.PROCESSING_DECISION_ROUTE_BACK_AND_ROUTE_TASK;
            }
            broadcastApplicationDto.setNewTaskHistoryExt(appPremisesRoutingHistoryExtDto);
        }

        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),
                applicationDto.getStatus(),taskDto.getTaskKey(),null, taskDto.getWkGrpId(),internalRemarks,null,processDecision,taskDto.getRoleId());
        broadcastApplicationDto.setComplateTaskHistory(appPremisesRoutingHistoryDto);
        //update application status
        broadcastApplicationDto.setRollBackApplicationDto((ApplicationDto) CopyUtil.copyMutableObject(applicationDto));
        applicationDto.setStatus(appStatus);
        broadcastApplicationDto.setApplicationDto(applicationDto);
        String taskType = TaskConsts.TASK_TYPE_MAIN_FLOW;
        String TaskUrl = TaskConsts.TASK_PROCESS_URL_MAIN_FLOW;
        if(HcsaConsts.ROUTING_STAGE_INS.equals(stageId) && !ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS.equals(appStatus)){
            taskType = TaskConsts.TASK_TYPE_INSPECTION;
            if(RoleConsts.USER_ROLE_AO1.equals(roleId)){
                TaskUrl = TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT_REVIEW_AO1;
            }else if((!RoleConsts.USER_ROLE_AO2.equals(roleId)) &&
                    (!RoleConsts.USER_ROLE_AO3.equals(roleId)) &&
                    (!RoleConsts.USER_ROLE_ASO.equals(roleId)) &&
                    (!RoleConsts.USER_ROLE_PSO.equals(roleId))
            ){
                TaskUrl = TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT;
            }
            subStageId = HcsaConsts.ROUTING_STAGE_POT;
            //update inspector status
            updateInspectionStatus(applicationViewDto.getAppPremisesCorrelationId(),InspectionConstants.INSPECTION_STATUS_PENDING_PREPARE_REPORT);
        }
        //reply inspector
        if(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS.equals(appStatus)){
            List<TaskDto> taskDtos = organizationClient.getTaskByAppNoStatus(applicationDto.getApplicationNo(), TaskConsts.TASK_STATUS_COMPLETED, TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION).getEntity();
            taskType = taskDtos.get(0).getTaskType();
            TaskUrl = TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION;
            subStageId = HcsaConsts.ROUTING_STAGE_PRE;
            //update inspector status
            updateInspectionStatus(applicationViewDto.getAppPremisesCorrelationId(),InspectionConstants.INSPECTION_STATUS_PENDING_PRE);
        }
        //DMS go to main flow
        if(ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(appStatus)){
            taskType = TaskConsts.TASK_TYPE_MAIN_FLOW;
            TaskUrl = TaskConsts.TASK_PROCESS_URL_MAIN_FLOW;
        }

        TaskDto newTaskDto = TaskUtil.getTaskDto(applicationDto.getApplicationNo(),stageId,taskType,
                taskDto.getRefNo(),wrkGpId, userId,new Date(),0,TaskUrl,roleId,
                IaisEGPHelper.getCurrentAuditTrailDto());
        broadcastOrganizationDto.setCreateTask(newTaskDto);

        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew =getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),applicationDto.getStatus(),stageId,subStageId,
                taskDto.getWkGrpId(),null,null,null,roleId);
        broadcastApplicationDto.setNewTaskHistory(appPremisesRoutingHistoryDtoNew);

        //save the broadcast
        broadcastOrganizationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        broadcastApplicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        String evenRefNum = String.valueOf(System.currentTimeMillis());
        broadcastOrganizationDto.setEventRefNo(evenRefNum);
        broadcastApplicationDto.setEventRefNo(evenRefNum);
        String submissionId = generateIdClient.getSeqId().getEntity();
        log.info(StringUtil.changeForLog(submissionId));
        broadcastOrganizationDto = broadcastService.svaeBroadcastOrganization(broadcastOrganizationDto,bpc.process,submissionId);
        broadcastApplicationDto  = broadcastService.svaeBroadcastApplicationDto(broadcastApplicationDto,bpc.process,submissionId);

        //0062460 update FE  application status.
        applicationService.updateFEApplicaiton(broadcastApplicationDto.getApplicationDto());
    }

    private void updateInspectionStatus(String appPremisesCorrelationId, String status) {
        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremisesCorrelationId).getEntity();
        if (appInspectionStatusDto != null) {
            appInspectionStatusDto.setStatus(status);
            appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appInspectionStatusClient.update(appInspectionStatusDto);
        }
    }

    private List<UserGroupCorrelationDto> changeStatusUserGroupCorrelationDtos(List<UserGroupCorrelationDto> userGroupCorrelationDtos,String status){
        List<UserGroupCorrelationDto> result = IaisCommonUtils.genNewArrayList();
        if(userGroupCorrelationDtos!= null && userGroupCorrelationDtos.size() >0){
            for (UserGroupCorrelationDto userGroupCorrelationDto : userGroupCorrelationDtos){
                userGroupCorrelationDto.setStatus(status);
                result.add(userGroupCorrelationDto);
            }
        }
        return  result;
    }
    private WorkingGroupDto changeStatusWrokGroup(WorkingGroupDto workingGroupDto,String staus){
        if(workingGroupDto!= null && !StringUtil.isEmpty(staus)){
            workingGroupDto.setStatus(staus);
        }
       return workingGroupDto;
    }
    private AppPremisesRoutingHistoryDto getAppPremisesRoutingHistory(String appNo, String appStatus,
                                                                         String stageId,String subStageId,String wrkGrpId, String internalRemarks,String externalRemarks,String processDecision,
                                                                      String roleId){
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(appNo);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setSubStage(subStageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setExternalRemarks(externalRemarks);
        appPremisesRoutingHistoryDto.setProcessDecision(processDecision);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setWrkGrpId(wrkGrpId);
        appPremisesRoutingHistoryDto.setRoleId(roleId);
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return appPremisesRoutingHistoryDto;
    }
//    private int remainDays(TaskDto taskDto){
//       int result = 0;
//       //todo: wait count kpi
//      // String  resultStr = DurationFormatUtils.formatPeriod(taskDto.getDateAssigned().getTime(),taskDto.getSlaDateCompleted().getTime(), "d");
//     // log.debug(StringUtil.changeForLog("The resultStr is -->:")+resultStr);
//      return  result;
//    }


    private TaskDto completedTask(TaskDto taskDto){
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setSlaDateCompleted(new Date());
        //taskDto.setSlaRemainInDays(remainDays(taskDto));
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return taskDto;
    }
    private ApplicationDto updateApplicaiton(ApplicationDto applicationDto,String appStatus){
        applicationDto.setStatus(appStatus);
        return applicationViewService.updateApplicaiton(applicationDto);
    }

    private List<String> getUserIds(List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos){
        Set<String> set = IaisCommonUtils.genNewHashSet();
        if(!IaisCommonUtils.isEmpty(appPremisesRoutingHistoryDtos)){
            for(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto :appPremisesRoutingHistoryDtos ){
                set.add(appPremisesRoutingHistoryDto.getActionby());
            }
        }
        return  new ArrayList(set);


    }

    /************************/


    private void sendAppealApproved(ApplicationDto applicationDto,String licenseeId){

        String applicationType = applicationDto.getApplicationType();
        if(ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)){
            Map<String,Object> map=IaisCommonUtils.genNewHashMap();
            List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(licenseeId);
            MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate("5B9EADD2-F27D-EA11-BE82-000C29F371DC").getEntity();
            map.put("applicationNumber",applicationDto.getApplicationNo());

        }

    }

    private void sendRFCRejectEmail(String licenseeId,String serviceId){
        String subject = "reassign reject";
        String mesContext = "reassign email";
        EmailDto emailDto = new EmailDto();
        emailDto.setContent(mesContext);
        emailDto.setSubject(subject);
        emailDto.setSender(mailSender);
        emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
        emailDto.setClientQueryCode(licenseeId);
        //send email
        emailClient.sendNotification(emailDto).getEntity();
        HashMap<String, String> maskParams = IaisCommonUtils.genNewHashMap();
        //send message
        sendMessage(subject,licenseeId,mesContext,maskParams,serviceId,null);
    }


    private void sendMessage(String subject, String licenseeId, String templateMessageByContent, HashMap<String, String> maskParams, String serviceId,String messageType){
        if(StringUtil.isEmpty(messageType)){
            messageType = MessageConstants.MESSAGE_TYPE_NOTIFICATION;
        }
        HcsaServiceDto serviceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
        InterMessageDto interMessageDto = new InterMessageDto();
        interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY);
        interMessageDto.setSubject(subject);
        interMessageDto.setMessageType(messageType);
        String refNo = inboxMsgService.getMessageNo();
        interMessageDto.setRefNo(refNo);
        if(serviceDto != null){
            interMessageDto.setService_id(serviceDto.getSvcCode()+"@");
        }
        interMessageDto.setUserId(licenseeId);
        interMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        interMessageDto.setMsgContent(templateMessageByContent);
        interMessageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        interMessageDto.setMaskParams(maskParams);
        inboxMsgService.saveInterMessage(interMessageDto);
    }


    private  void  sendAppealReject(String licenseeId, ApplicationDto applicationDto) throws IOException, TemplateException {

        String applicationType = applicationDto.getApplicationType();
        String applicationNo = applicationDto.getApplicationNo();
        if(ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)){
            Map<String,Object> map=new HashMap<>();
            MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate("3BA1C87A-5F7D-EA11-BE82-000C29F371DC").getEntity();
            if(msgTemplateDto!=null){
                map.put("applicationNumber",applicationNo);
                String mesContext = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), map);
                EmailDto emailDto = new EmailDto();
                emailDto.setContent(mesContext);
                emailDto.setSender(mailSender);
                emailDto.setSubject("MOH IAIS – Appeal, "+applicationNo+" , is Rejected");
                emailDto.setClientQueryCode(applicationNo);
                emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
                emailClient.sendNotification(emailDto).getEntity();
            }
        }
    }

    private void initData(BaseProcessClass bpc){
        //get the task
        String  taskId = ParamUtil.getMaskedString(bpc.request,"taskId");
        AuditTrailHelper.auditFunction("hcsa-licence", "hcsa licence");
        TaskDto taskDto = taskService.getTaskById(taskId);
        ParamUtil.setSessionAttr(bpc.request,"taskDto", taskDto);
        String roleId = "";
        //set internal marks fill back
        String correlationId = "";
        if(taskDto != null){
            correlationId = taskDto.getRefNo();
            roleId = taskDto.getRoleId();
        }else{
            throw new IaisRuntimeException("The Task Id  is Error !!!");
        }
        log.debug(StringUtil.changeForLog("the do prepareData get the NewAppPremisesCorrelationDto"));
        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationViewService.getLastAppPremisesCorrelationDtoById(correlationId);
        appPremisesCorrelationDto.setOldCorrelationId(correlationId);
        String newCorrelationId = appPremisesCorrelationDto.getId();
        ApplicationViewDto applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(newCorrelationId);
        applicationViewDto.setNewAppPremisesCorrelationDto(appPremisesCorrelationDto);
        ParamUtil.setSessionAttr(bpc.request,"applicationViewDto", applicationViewDto);
        //set recommendation dropdown value
        setRecommendationDropdownValue(bpc.request,applicationViewDto);
        //set verified dropdown value
        setVerifiedDropdownValue(bpc.request,applicationViewDto,taskDto);

        //check broadcast role id
        AppPremisesRoutingHistoryDto userHistory = appPremisesRoutingHistoryService.getAppHistoryByAppNoAndActionBy(applicationViewDto.getApplicationDto().getApplicationNo(), taskDto.getUserId());
        String currentRoleId = "";
        if(userHistory != null){
            currentRoleId = userHistory.getRoleId();
        }
        boolean broadcastAsoPso = false;
        boolean broadcastOther = false;
        boolean broadcastAso = false;
        String status = applicationViewDto.getApplicationDto().getStatus();
        //DMS
        if(ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(status)){
            ParamUtil.setSessionAttr(bpc.request,"isDMS","isDMS");
        }
        if(ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(status)){
            if(RoleConsts.USER_ROLE_PSO.equals(currentRoleId) || RoleConsts.USER_ROLE_ASO.equals(currentRoleId)){
                broadcastAsoPso = true;
                if(RoleConsts.USER_ROLE_ASO.equals(currentRoleId)){
                    broadcastAso = true;
                }
            }else{
                broadcastOther = true;
            }
        }
        ParamUtil.setSessionAttr(bpc.request,"broadcastAsoPso",broadcastAsoPso);
        ParamUtil.setSessionAttr(bpc.request,"broadcastAso",broadcastAso);

        boolean isRequestForChange = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationViewDto.getApplicationDto().getApplicationType());
        AppPremisesRecommendationDto appPremisesRecommendationDto = applicationViewDto.getAppPremisesRecommendationDto();
        if(appPremisesRecommendationDto != null){
            Integer recomInNumber = appPremisesRecommendationDto.getRecomInNumber();
            String chronoUnit = appPremisesRecommendationDto.getChronoUnit();
            String codeDesc = "";
            String recommendationOnlyShow = "";
            if(recomInNumber == null || recomInNumber == 0){
                recommendationOnlyShow = "Reject";
            }else{
                recommendationOnlyShow = getRecommendationOnlyShowStr(recomInNumber);
                if(isRequestForChange){
                    recommendationOnlyShow = "Approve";
                }
            }
            //PSO 0062307
            boolean needFillingBack = (RoleConsts.USER_ROLE_INSPECTIOR.equals(roleId) || RoleConsts.USER_ROLE_PSO.equals(roleId) || RoleConsts.USER_ROLE_ASO.equals(roleId) || broadcastAsoPso) && !ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(status);
            if(needFillingBack){
                //pso back fill
                checkRecommendationDropdownValue(recomInNumber,chronoUnit,codeDesc,applicationViewDto,bpc);
            }
            Date recomInDate = appPremisesRecommendationDto.getRecomInDate();
            String recomInDateOnlyShow = "-";
            if(recomInDate != null){
                recomInDateOnlyShow = Formatter.formatDateTime(recomInDate,Formatter.DATE);
            }
            ParamUtil.setRequestAttr(bpc.request, "recomInDateOnlyShow",recomInDateOnlyShow);
            if(RoleConsts.USER_ROLE_AO1.equals(roleId) || RoleConsts.USER_ROLE_AO2.equals(roleId) || RoleConsts.USER_ROLE_AO3.equals(roleId) || broadcastOther){
                ParamUtil.setRequestAttr(bpc.request, "recommendationOnlyShow",recommendationOnlyShow);
            }
        }

        //Licence Start Date back fill
        //AppPremisesRecommendationDto appPremisesRecommendationDto = applicationViewDto.getAppPremisesRecommendationDto();
        if(appPremisesRecommendationDto != null && appPremisesRecommendationDto.getRecomInDate() != null){
            Date recomInDate = appPremisesRecommendationDto.getRecomInDate();
            String date = Formatter.formatDateTime(recomInDate,Formatter.DATE);
            ParamUtil.setRequestAttr(bpc.request,"date",date);
        }

        //appeal
        setAppealTypeValues(bpc.request,applicationViewDto,roleId);

        //check route back review
        setRouteBackReview(bpc.request,applicationViewDto,roleId,taskDto,status);
    }

    private String getRecommendationOnlyShowStr (Integer recomInNumber){
        if(recomInNumber == null){
            return "-";
        }
        String recommendationOnlyShow = "-";
        if(recomInNumber >= 12){
            if( recomInNumber % 12 == 0){
                if(recomInNumber / 12 == 1){
                    recommendationOnlyShow = "1 Year";
                }else {
                    recommendationOnlyShow = recomInNumber / 12 + " Year(s)";
                }
            }else {
                if(recomInNumber / 12 == 1) {
                    if(recomInNumber % 12 == 1){
                        recommendationOnlyShow = 1 + " Year " + 1 + " Month";
                    }else {
                        recommendationOnlyShow = 1 + " Year " + recomInNumber % 12 + " Month(s)";
                    }

                }else {
                    if(recomInNumber % 12 == 1){
                        recommendationOnlyShow = recomInNumber / 12 + " Year(s) " + 1 + " Month";
                    }else {
                        recommendationOnlyShow = recomInNumber / 12 + " Year(s) " + recomInNumber % 12 + " Month(s)";
                    }
                }
            }
        }else {
            if( recomInNumber == 1){
                recommendationOnlyShow = recomInNumber + " Month";
            }else {
                recommendationOnlyShow = recomInNumber + " Month(s)";
            }
        }
        return recommendationOnlyShow;
    }

    public void setSelectionValue(HttpServletRequest request, ApplicationViewDto applicationViewDto, TaskDto taskDto){
        //set normal processingDecision value
        setNormalProcessingDecisionDropdownValue(request,applicationViewDto,taskDto);
        //set reply processingDecision value
        setReplyProcessingDecisionDropdownValue(request,applicationViewDto);
        //set DMS processingDecision value
        setDmsProcessingDecisionDropdownValue(request);
        //set route back dropdown value
        setRouteBackDropdownValue(request,applicationViewDto);
        //set recommendation dropdown value
//        setRecommendationDropdownValue(request,applicationViewDto);
        //set recommendation other dropdown value
        setRecommendationOtherDropdownValue(request);
        //set appeal recommendation dropdown value
        setAppealRecommendationDropdownValue(request,applicationViewDto);
    }

    private void setRouteBackReview(HttpServletRequest request, ApplicationViewDto applicationViewDto, String roleId, TaskDto taskDto, String status){
        //init session
        ParamUtil.setSessionAttr(request,"routeBackReview",null);
        if(!(RoleConsts.USER_ROLE_PSO.equals(roleId) || RoleConsts.USER_ROLE_ASO.equals(roleId))){
            if(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02.equals(status) || ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01.equals(status)){
                ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
                ApplicationGroupDto applicationGroupDto = applicationGroupService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
                List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtoList = applicationViewService.getStage(applicationDto.getServiceId(),
                        taskDto.getTaskKey(),applicationDto.getApplicationType(),applicationGroupDto.getIsPreInspection());
                if(hcsaSvcRoutingStageDtoList != null & hcsaSvcRoutingStageDtoList.size() > 0){
                    ParamUtil.setSessionAttr(request,"routeBackReview","canRouteBackReview");
                }
            }
        }
    }

    private void setAppealTypeValues(HttpServletRequest request, ApplicationViewDto applicationViewDto, String roleId){
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        boolean isOtherAppealType = false;
        boolean isChangePeriodAppealType = false;
        boolean isLateFeeAppealType = false;
        String applicationType = applicationDto.getApplicationType();
        AppPremisesRecommendationDto appPremisesRecommendationDto = applicationViewDto.getAppPremisesRecommendationDto();
        if(ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)) {
            //get appeal type
            String appId = applicationDto.getId();
            AppPremiseMiscDto premiseMiscDto = cessationClient.getAppPremiseMiscDtoByAppId(appId).getEntity();
            if(premiseMiscDto != null){
                String appealNo = "";
                String reason = premiseMiscDto.getReason();
                isOtherAppealType = true;
                if (ApplicationConsts.APPEAL_REASON_LICENCE_CHANGE_PERIOD.equals(reason)) {
                    isChangePeriodAppealType = true;
                    isOtherAppealType = false;
                } else if (ApplicationConsts.APPEAL_REASON_APPLICATION_LATE_RENEW_FEE.equals(reason)) {
                    isLateFeeAppealType = true;
                    isOtherAppealType = false;
                } else if (ApplicationConsts.APPEAL_REASON_APPLICATION_ADD_CGO.equals(reason)) {
                    String serviceId = applicationViewDto.getApplicationDto().getServiceId();
                    String serviceName = HcsaServiceCacheHelper.getServiceById(serviceId).getSvcName();
                    AppSvcCgoDto appSvcCgoDto = applicationClient.getApplicationCgoByAppId(appId,ApplicationConsts.PERSONNEL_PSN_TYPE_CGO).getEntity();
                    appSvcCgoDto.setAssignSelect("newOfficer");
                    List<AppSvcCgoDto> appSvcCgoDtoList = IaisCommonUtils.genNewArrayList();
                    appSvcCgoDtoList.add(appSvcCgoDto);
                    SelectOption sp0 = new SelectOption("-1", "Please Select");
                    List<SelectOption> cgoSelectList = IaisCommonUtils.genNewArrayList();
                    cgoSelectList.add(sp0);
                    SelectOption sp1 = new SelectOption("newOfficer", "I'd like to add a new personnel");
                    cgoSelectList.add(sp1);
                    List<SelectOption> idTypeSelOp = getIdTypeSelOp();
                    if (serviceName != null) {
                        HcsaServiceDto serviceByServiceName = HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
                        List<SelectOption> list = genSpecialtySelectList(serviceByServiceName.getSvcCode());
                        ParamUtil.setSessionAttr(request, "SpecialtySelectList", (Serializable) list);
                    }
                    ParamUtil.setSessionAttr(request,"CgoMandatoryCount",1);
                    ParamUtil.setSessionAttr(request,"GovernanceOfficersList",(Serializable) appSvcCgoDtoList);
                    ParamUtil.setSessionAttr(request, "CgoSelectList", (Serializable) cgoSelectList);
                    ParamUtil.setSessionAttr(request, "IdTypeSelect", (Serializable) idTypeSelOp);
                }
                //file
                AppPremisesSpecialDocDto appealSpecialDocDto = fillUpCheckListGetAppClient.getAppPremisesSpecialDocByPremId(premiseMiscDto.getAppPremCorreId()).getEntity();
                if(appealSpecialDocDto != null){
                    ParamUtil.setSessionAttr(request, "appealSpecialDocDto",appealSpecialDocDto);
                }
                String oldAppId = premiseMiscDto.getRelateRecId();
                String type = "";
                ApplicationDto oldApplication = applicationClient.getApplicationById(oldAppId).getEntity();
                if(oldApplication != null){
                    List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = appPremisesCorrClient.getAppPremisesCorrelationsByAppId(oldApplication.getId()).getEntity();
                    if(appPremisesCorrelationDtos != null && appPremisesCorrelationDtos.size()>0){
                        AppPremisesCorrelationDto appPremisesCorrelationDto = appPremisesCorrelationDtos.get(0);
                        AppInsRepDto appInsRepDto = appPremisesCorrClient.appGrpPremises(appPremisesCorrelationDto.getId()).getEntity();
                        if(appInsRepDto != null){
                            String hciName = appInsRepDto.getHciName();
                            List<String> hciNames = IaisCommonUtils.genNewArrayList();
                            if(!StringUtil.isEmpty(hciName)){
                                hciNames.add(hciName);
                                ParamUtil.setSessionAttr(request, "hciNames", (Serializable) hciNames);
                            }
                        }
                    }
                    appealNo = oldApplication.getApplicationNo();
                    type = "application";
                    ParamUtil.setSessionAttr(request, "oldApplicationNo", oldApplication.getApplicationNo());
                    ParamUtil.setSessionAttr(request, "appealingFor", oldApplication.getApplicationNo());
                }
                String appealType = premiseMiscDto.getAppealType();
                if(ApplicationConsts.APPEAL_TYPE_LICENCE.equals(appealType)){
                    LicenceDto licenceDto = licenceService.getLicenceDto(premiseMiscDto.getRelateRecId());
                    appealNo = licenceDto.getLicenceNo();
                    type = "licence";
                }
                ParamUtil.setSessionAttr(request, "appealNo", appealNo);
                ParamUtil.setSessionAttr(request, "appealingFor", appealNo);
                ParamUtil.setSessionAttr(request, "premiseMiscDto", premiseMiscDto);
                ParamUtil.setSessionAttr(request, "type", type);
            }
            //first ASO have no recommendation
            if(appPremisesRecommendationDto != null){
                //set filling back
                String recomDecision = appPremisesRecommendationDto.getRecomDecision();
                Integer recomInNumber = appPremisesRecommendationDto.getRecomInNumber();
                String chronoUnit = appPremisesRecommendationDto.getChronoUnit();
                boolean isAppealApprove = "approve".equals(recomDecision);
                boolean isAppealReject = "reject".equals(recomDecision);
                //pso
                if(RoleConsts.USER_ROLE_PSO.equals(roleId) || RoleConsts.USER_ROLE_ASO.equals(roleId)){
                    if(isAppealApprove){
                        recomDecision = "appealApprove";
                    }else if(isAppealReject){
                        recomDecision = "appealReject";
                    }
                    ParamUtil.setRequestAttr(request,"selectAppealRecommendationValue",recomDecision);
                    if(isLateFeeAppealType && isAppealApprove){
                        String returnFee = appPremisesRecommendationDto.getRemarks();
                        ParamUtil.setRequestAttr(request,"returnFee",returnFee);
                    }else if(isChangePeriodAppealType && isAppealApprove){
                        ParamUtil.setRequestAttr(request,"otherChrono",chronoUnit);
                        ParamUtil.setRequestAttr(request,"otherNumber",recomInNumber);
                    }
                }else if(RoleConsts.USER_ROLE_AO1.equals(roleId) || RoleConsts.USER_ROLE_AO2.equals(roleId) || RoleConsts.USER_ROLE_AO3.equals(roleId)){
                    if(isAppealApprove){
                        recomDecision = "Approve";
                    }else if(isAppealReject){
                        recomDecision = "Reject";
                    }
                    ParamUtil.setSessionAttr(request,"appealRecommendationValueOnlyShow",recomDecision);
                    if(isLateFeeAppealType && isAppealApprove){
                        String returnFee = appPremisesRecommendationDto.getRemarks();
                        ParamUtil.setSessionAttr(request,"returnFeeOnlyShow",returnFee);
                    }else if(isChangePeriodAppealType && isAppealApprove){
                        String codeDesc = "";
                        codeDesc = MasterCodeUtil.getCodeDesc(chronoUnit);
                        String appealRecommendationOtherOnlyShow = recomInNumber + " " + codeDesc;
                        ParamUtil.setSessionAttr(request,"appealRecommendationOtherOnlyShow",appealRecommendationOtherOnlyShow);
                    }
                }
            }
        }
        ParamUtil.setSessionAttr(request,"isOtherAppealType",isOtherAppealType);
        ParamUtil.setSessionAttr(request,"isChangePeriodAppealType",isChangePeriodAppealType);
        ParamUtil.setSessionAttr(request,"isLateFeeAppealType",isLateFeeAppealType);
    }

    private List<SelectOption> genSpecialtySelectList(String svcCode){
        List<SelectOption> specialtySelectList = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(svcCode)){
            if(AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(svcCode)){
                specialtySelectList = IaisCommonUtils.genNewArrayList();
                SelectOption ssl1 = new SelectOption("-1", "Please select");
                SelectOption ssl2 = new SelectOption("Pathology", "Pathology");
                SelectOption ssl3 = new SelectOption("Haematology", "Haematology");
                SelectOption ssl4 = new SelectOption("other", "Others");
                specialtySelectList.add(ssl1);
                specialtySelectList.add(ssl2);
                specialtySelectList.add(ssl3);
                specialtySelectList.add(ssl4);
            }else if(AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(svcCode)){
                specialtySelectList = IaisCommonUtils.genNewArrayList();
                SelectOption ssl1 = new SelectOption("-1", "Please select");
                SelectOption ssl2 = new SelectOption("Diagnostic Radiology", "Diagnostic Radiology");
                SelectOption ssl3 = new SelectOption("Nuclear Medicine", "Nuclear Medicine");
                SelectOption ssl4 = new SelectOption("other", "Others");
                specialtySelectList.add(ssl1);
                specialtySelectList.add(ssl2);
                specialtySelectList.add(ssl3);
                specialtySelectList.add(ssl4);
            }
        }
        return specialtySelectList;
    }

    private void setAppealRecommendationDropdownValue(HttpServletRequest request, ApplicationViewDto applicationViewDto){
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String applicationType = applicationDto.getApplicationType();
        if(ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)){
            //appealRecommendationValues
            List<SelectOption> appealRecommendationValues = IaisCommonUtils.genNewArrayList();
            appealRecommendationValues.add(new SelectOption("appealApprove", "Approve"));
            appealRecommendationValues.add(new SelectOption("appealReject", "Reject"));
            ParamUtil.setSessionAttr(request, "appealRecommendationValues", (Serializable)appealRecommendationValues);
        }
    }

    public void setNormalProcessingDecisionDropdownValue(HttpServletRequest request, ApplicationViewDto applicationViewDto, TaskDto taskDto){
        List<SelectOption> nextStageList = IaisCommonUtils.genNewArrayList();
        nextStageList.add(new SelectOption("", "Please Select"));
        if(RoleConsts.USER_ROLE_AO1.equals(taskDto.getRoleId()) || RoleConsts.USER_ROLE_AO2.equals(taskDto.getRoleId())){
//            nextStageList.add(new SelectOption("VERIFIED", "Support"));
            nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_VERIFIED, "Support"));
        }else{
            //62875
            //role is ao3 && status is 'Pending AO3 Approval'  have no verified
            if(!(RoleConsts.USER_ROLE_AO3.equals(taskDto.getRoleId()) && ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(applicationViewDto.getApplicationDto().getStatus()))){
                if (!ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(applicationViewDto.getApplicationDto().getApplicationType())) {
                    nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_VERIFIED, "Verified"));
                }
            }
        }
        if((ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION_REPLY.equals(applicationViewDto.getApplicationDto().getStatus())
                || ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(applicationViewDto.getApplicationDto().getStatus()))
                && RoleConsts.USER_ROLE_ASO.equals(taskDto.getRoleId())){

        }else{
            nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_ROLLBACK, "Internal Route Back"));
        }
        //62761
        Integer rfiCount =  applicationService.getAppBYGroupIdAndStatus(applicationViewDto.getApplicationDto().getAppGrpId(),
                ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION);
        log.info(StringUtil.changeForLog("The rfiCount is -->:"+rfiCount));
        if(!(RoleConsts.USER_ROLE_AO1.equals(taskDto.getRoleId()) || RoleConsts.USER_ROLE_AO2.equals(taskDto.getRoleId()) || RoleConsts.USER_ROLE_AO3.equals(taskDto.getRoleId()))){
            if(rfiCount==0){
                if (!ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(applicationViewDto.getApplicationDto().getApplicationType())){
                    nextStageList.add(new SelectOption("PROCRFI", "Request For Information"));
                }
            }
        }

        if (RoleConsts.USER_ROLE_ASO.equals(taskDto.getRoleId())
                && ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(applicationViewDto.getApplicationDto().getStatus())
                && ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(applicationViewDto.getApplicationDto().getApplicationType())){
            nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_PENDING_APPROVAL,"Approve"));
            nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_REJECT, "Reject"));
        }

        //62875
        if(RoleConsts.USER_ROLE_AO3.equals(taskDto.getRoleId()) && ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(applicationViewDto.getApplicationDto().getStatus())){
            nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_PENDING_APPROVAL,"Approve"));
            if(ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationViewDto.getApplicationDto().getApplicationType())){
                nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_REJECT, "Reject"));
            }else{
                nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_BROADCAST_QUERY,"Broadcast"));
                nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_ROUTE_TO_DMS,"Trigger to DMS"));
            }
        }

        ParamUtil.setSessionAttr(request, "nextStages", (Serializable)nextStageList);
    }

    public void setReplyProcessingDecisionDropdownValue(HttpServletRequest request, ApplicationViewDto applicationViewDto){
        List<SelectOption> nextStageReplyList = IaisCommonUtils.genNewArrayList();
        if(!ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(applicationViewDto.getApplicationDto().getStatus())){
            nextStageReplyList.add(new SelectOption("", "Please Select"));
        }
        nextStageReplyList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_REPLY, "Give Clarification"));
        ParamUtil.setSessionAttr(request, "nextStageReply", (Serializable)nextStageReplyList);
    }

    public void setDmsProcessingDecisionDropdownValue(HttpServletRequest request){
        List<SelectOption> decisionValues = IaisCommonUtils.genNewArrayList();
        decisionValues.add(new SelectOption("decisionApproval", "Approve"));
        decisionValues.add(new SelectOption("decisionReject", "Reject"));
        ParamUtil.setSessionAttr(request, "decisionValues", (Serializable)decisionValues);
    }

    private List<SelectOption> getIdTypeSelOp(){
        List<SelectOption> idTypeSelectList = IaisCommonUtils.genNewArrayList();
        SelectOption idType0 = new SelectOption("-1", "Please Select");
        idTypeSelectList.add(idType0);
        SelectOption idType1 = new SelectOption("NRIC", "NRIC");
        idTypeSelectList.add(idType1);
        SelectOption idType2 = new SelectOption("FIN", "FIN");
        idTypeSelectList.add(idType2);
        return idTypeSelectList;
    }

    public void setRouteBackDropdownValue(HttpServletRequest request, ApplicationViewDto applicationViewDto){
        //   rollback
        log.debug(StringUtil.changeForLog("the do prepareData get the rollBackMap"));
        Map<String,String> rollBackMap = IaisCommonUtils.genNewHashMap();
        List<SelectOption> rollBackStage = IaisCommonUtils.genNewArrayList();
        List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtoList = applicationViewDto.getRollBackHistroyList();
        if(!IaisCommonUtils.isEmpty(appPremisesRoutingHistoryDtoList)){
            for (AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto:appPremisesRoutingHistoryDtoList) {
//                String displayName = applicationViewService.getStageById(appPremisesRoutingHistoryDto.getStageId()).getStageName();
                String displayName = appPremisesRoutingHistoryDto.getRoleId();
                String userId = appPremisesRoutingHistoryDto.getActionby();
                String wrkGrpId = appPremisesRoutingHistoryDto.getWrkGrpId();
                OrgUserDto user = applicationViewService.getUserById(userId);
                String actionBy = user.getDisplayName();
                rollBackMap.put(actionBy+" ("+displayName+")",appPremisesRoutingHistoryDto.getStageId()+","+wrkGrpId+","+userId+","+appPremisesRoutingHistoryDto.getRoleId());
                String maskRollBackValue = MaskUtil.maskValue("rollBack",appPremisesRoutingHistoryDto.getStageId()+","+wrkGrpId+","+userId+","+appPremisesRoutingHistoryDto.getRoleId());
                SelectOption selectOption = new SelectOption(maskRollBackValue,actionBy+" ("+displayName+")");
                rollBackStage.add(selectOption);
            }
        }else{
            log.debug(StringUtil.changeForLog("the do prepareData do not have the rollback history"));
        }
        applicationViewDto.setRollBack(rollBackMap);
        ParamUtil.setSessionAttr(request, "routeBackValues", (Serializable)rollBackStage);
    }

    public void setVerifiedDropdownValue(HttpServletRequest request, ApplicationViewDto applicationViewDto, TaskDto taskDto){
        //get routing stage dropdown send to page.
        log.debug(StringUtil.changeForLog("the do prepareData get the hcsaSvcRoutingStageDtoList"));
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        ApplicationGroupDto applicationGroupDto = applicationGroupService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtoList = applicationViewService.getStage(applicationDto.getServiceId(),
                taskDto.getTaskKey(),applicationViewDto.getApplicationDto().getApplicationType(),applicationGroupDto.getIsPreInspection());
        List<SelectOption> routingStage = IaisCommonUtils.genNewArrayList();
        if(hcsaSvcRoutingStageDtoList!=null){
            if(hcsaSvcRoutingStageDtoList.size()>0){
                for (HcsaSvcRoutingStageDto hcsaSvcRoutingStage:hcsaSvcRoutingStageDtoList) {
                    routingStage.add(new SelectOption(hcsaSvcRoutingStage.getStageCode(),hcsaSvcRoutingStage.getStageName()));
                    if(hcsaSvcRoutingStage.isRecommend()){
                        ParamUtil.setRequestAttr(request,"selectVerified",hcsaSvcRoutingStage.getStageCode());
                        ParamUtil.setSessionAttr(request,"RecommendValue",hcsaSvcRoutingStage.getStageCode());
                    }
                }
            }else{
                log.debug(StringUtil.changeForLog("the do prepareData add the Approve"));
                //if  this is the last stage
                routingStage.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_PENDING_APPROVAL,
                        "Approve"));
            }
        }
        if(ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(applicationViewDto.getApplicationDto().getStatus())){
            routingStage.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_BROADCAST_REPLY,"Broadcast Reply For Internal"));
        }
        //set verified values
        applicationViewDto.setVerified(routingStage);
        ParamUtil.setSessionAttr(request, "verifiedValues", (Serializable)routingStage);
    }

    public void setRecommendationDropdownValue(HttpServletRequest request, ApplicationViewDto applicationViewDto){
        //set recommendation dropdown
        ParamUtil.setSessionAttr(request, "recommendationDropdown", (Serializable)getRecommendationDropdown(applicationViewDto,request));
    }

    public void setRecommendationOtherDropdownValue(HttpServletRequest request){
        //set recommendation other dropdown
        ParamUtil.setSessionAttr(request, "recommendationOtherDropdown", (Serializable)getRecommendationOtherDropdown());
    }

    //send email helper
    private String sendEmailHelper(Map<String ,Object> tempMap,String msgTemplateId,String subject,String licenseeId,String clientQueryCode){
        MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(msgTemplateId).getEntity();
        if(tempMap == null || tempMap.isEmpty() || msgTemplateDto == null
                || StringUtil.isEmpty(msgTemplateId)
                || StringUtil.isEmpty(subject)
                || StringUtil.isEmpty(licenseeId)
                || StringUtil.isEmpty(clientQueryCode)){
            return null;
        }
        String mesContext = null;
        try {
            mesContext = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), tempMap);
        } catch (IOException | TemplateException e) {
            log.error(e.getMessage(),e);
        }
        EmailDto emailDto = new EmailDto();
        emailDto.setContent(mesContext);
        emailDto.setSubject(" " + msgTemplateDto.getTemplateName() + " " + subject);
        emailDto.setSender(mailSender);
        emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
        emailDto.setClientQueryCode(clientQueryCode);
        //send
        emailClient.sendNotification(emailDto).getEntity();

        return mesContext;
    }
}
