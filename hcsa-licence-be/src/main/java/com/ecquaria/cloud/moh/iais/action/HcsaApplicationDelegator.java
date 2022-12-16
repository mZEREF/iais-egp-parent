package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.job.executor.util.SpringHelper;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.acra.AcraConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppLastInsGroup;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionReportConstants;
import com.ecquaria.cloud.moh.iais.common.constant.intranet.user.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesUpdateEmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppReturnFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.PremCheckItem;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessment;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessmentConfig;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppIntranetDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSubSvcRelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesSelfDeclChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessHciDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.EventBusLicenceGroupDtos;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.SuperLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskScoreDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdCheckListShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFDtosDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionPreTaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.mask.MaskAttackException;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MessageTemplateUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.utils.TaskUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.AjaxResDto;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.TaskHistoryDto;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.BeSelfChecklistHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.helper.SelectHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.ApplicationGroupService;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.BroadcastService;
import com.ecquaria.cloud.moh.iais.service.CessationBeService;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.GiroDeductionBeService;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.InspectionPreTaskService;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import com.ecquaria.cloud.moh.iais.service.LicenseeService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremSubSvcBeClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.InsRepClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.util.LicenceUtil;
import com.ecquaria.cloud.moh.iais.validation.HcsaApplicationProcessUploadFileValidate;
import com.ecquaria.cloud.moh.iais.validation.HcsaApplicationViewValidate;
import com.ecquaria.cloud.moh.iais.validation.InspectionCheckListItemValidate;
import com.ecquaria.cloudfeign.FeignException;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;
import sop.util.CopyUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * HcsaApplicationDelegator
 *
 * @author suocheng
 * @date 10/17/2019
 */
@Delegator("hcsaApplicationDelegator")
@Slf4j
public class HcsaApplicationDelegator {
    private static final String STR_TASK_DTO            = "taskDto";
    private static final String STR_FIELD               = "field";
    private static final String STR_LR_SELECT           = "lrSelect";
    private static final String EMPTY_OPT_DESC          = "Please Select";
    private static final String STR_MAIL_CONTENT        = "mailContent";
    private static final String STR_APP_PREM_EMAIL_DTO  = "appPremisesUpdateEmailDto";
    private static final String STR_DO_PROCESS          = "doProcess";
    private static final String STR_CRUD_ACTION_ADD     = "crud_action_additional";
    private static final String STR_CRUD_ACTION_TYPE    = "crud_action_type";
    private static final String STR_PROCESSING          = "processing";
    private static final String STR_HAS_ASO_EMAIL_DOC   = "hasAsoEmailDoc";
    private static final String STR_PROCAP              = "PROCAP";
    private static final String STR_PROCREJ             = "PROCREJ";
    private static final String STR_APPLICATION_DATE_U  = "ApplicationDate";
    private static final String STR_APPLICATION_DATE_L  = "applicationDate";
    private static final String STR_MOH_AGEN_NAM_GRP    = "MOH_AGENCY_NAM_GROUP";
    private static final String STR_EMAIL_SUBJECT       = "emailSubject : ";
    private static final String STR_INS_REP_DTO         = "insRepDto";
    private static final String STR_APP_TYPE            = "appType";
    private static final String[] ALPHABET_ARRAY_PROTOTYPE = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
            "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

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
    private OrganizationClient organizationClient;

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
    private AppInspectionStatusClient appInspectionStatusClient;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private CessationBeService cessationBeService;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;

    @Autowired
    private AppPremisesCorrClient appPremisesCorrClient;

    @Autowired
    private InspectionPreTaskService inspectionPreTaskService;

    @Autowired
    private AppPremSubSvcBeClient appPremSubSvcBeClient;

    @Autowired
    private AppCommService appCommService;

    @Value("${iais.system.one.address}")
    private String systemAddressOne;

    @Value("${iais.system.two.address}")
    private String systemAddressTwo;

    @Value("${iais.system.phone.number}")
    private String systemPhoneNumber;
    @Autowired
    InspEmailService inspEmailService;

    @Autowired
    private InsepctionNcCheckListService insepctionNcCheckListService;

    @Autowired
    private GiroDeductionBeService giroDeductionBeService;

    @Autowired
    private NotificationHelper notificationHelper;
    @Autowired
    private FillupChklistService fillupChklistService;
    @Autowired
    private VehicleCommonController vehicleCommonController;
    @Autowired
    private InspectReviseNcEmailDelegator inspectReviseNcEmailDelegator;
    @Autowired
    private InspectionNcCheckListDelegator inspectionNcCheckListDelegator;
    @Autowired
    private InsReportDelegator insReportDelegator;
    @Autowired
    private InspectionService inspectionService;
    @Autowired
    private InsRepClient insRepClient;
    private static final String[] reasonArr = new String[]{ApplicationConsts.CESSATION_REASON_NOT_PROFITABLE, ApplicationConsts.CESSATION_REASON_REDUCE_WORKLOA, ApplicationConsts.CESSATION_REASON_OTHER};
    private static final String[] patientsArr = new String[]{ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_HCI, ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_PRO, ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_OTHER};

    private static final List<String> ROLE = Arrays.asList(RoleConsts.USER_ROLE_ASO,RoleConsts.USER_ROLE_PSO,RoleConsts.USER_ROLE_INSPECTIOR,RoleConsts.USER_ROLE_AO1,RoleConsts.USER_ROLE_AO2,RoleConsts.USER_ROLE_AO3);


    @PostMapping(value = "/check-ao")
    public @ResponseBody
    AjaxResDto checkAo(HttpServletRequest request) {
        log.info(StringUtil.changeForLog("the do checkAo start ...."));
        Map<String,String> roleStage = IaisCommonUtils.genNewHashMap();
        String verified = ParamUtil.getString(request, "verified");
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(request, STR_TASK_DTO);

        roleStage.put(RoleConsts.USER_ROLE_ASO,HcsaConsts.ROUTING_STAGE_ASO);
        roleStage.put(RoleConsts.USER_ROLE_AO1,HcsaConsts.ROUTING_STAGE_AO1);
        roleStage.put(RoleConsts.USER_ROLE_AO2,HcsaConsts.ROUTING_STAGE_AO2);
        roleStage.put(InspectionConstants.PROCESS_DECI_ROTE_EMAIL_AO1_REVIEW, HcsaConsts.ROUTING_STAGE_INS);
        roleStage.put(RoleConsts.USER_ROLE_AO3,HcsaConsts.ROUTING_STAGE_AO3);
        if(taskDto.getRoleId().equals(verified)){
            roleStage.put(RoleConsts.USER_ROLE_PSO,HcsaConsts.ROUTING_STAGE_PSO);
            roleStage.put(RoleConsts.USER_ROLE_INSPECTIOR,HcsaConsts.ROUTING_STAGE_INS);
            roleStage.put(RoleConsts.USER_ROLE_INSPECTION_LEAD,HcsaConsts.ROUTING_STAGE_INS);
        }

        int order=1;
        AjaxResDto ajaxResDto = new AjaxResDto();
        log.info(StringUtil.changeForLog("verified is -->:"+verified));
        switch (verified){
            case InspectionConstants.PROCESS_DECI_ROTE_EMAIL_AO1_REVIEW : order=2;break;
            case RoleConsts.USER_ROLE_INSPECTION_LEAD : order=3;break;
            default:
        }
        Map<String,String> error = IaisCommonUtils.genNewHashMap();
        if(StringUtil.isEmpty(verified)){
            String msgGenError006 = MessageUtil.replaceMessage("GENERAL_ERR0006","Assign To",STR_FIELD);
            error.put("verifiedError",msgGenError006);
        }
        extractedCheckAo(request, roleStage, verified, taskDto, order, ajaxResDto, error);
        if(!error.isEmpty()){
            ajaxResDto.setResCode(AppConsts.AJAX_RES_CODE_VALIDATE_ERROR);
            ajaxResDto.setResultJson(MessageUtil.getMessageDesc(error.get("verifiedError")));
        }
        log.info(StringUtil.changeForLog("the do checkAo end ...."));
        return ajaxResDto;
    }

    private void extractedCheckAo(HttpServletRequest request, Map<String, String> roleStage, String verified, TaskDto taskDto, int order, AjaxResDto ajaxResDto, Map<String, String> error) {
        if (error.isEmpty()) {
            String stageId = roleStage.get(verified);
            log.info(StringUtil.changeForLog("stageId is -->:"+stageId));
            if(StringUtil.isNotEmpty(stageId)){
                ajaxResDto.setResCode(AppConsts.AJAX_RES_CODE_SUCCESS);
                Map<String, String> chargesTypeAttr = IaisCommonUtils.genNewHashMap();
                String firstOption="By System";
                if(taskDto.getRoleId().equals(verified)){
                    chargesTypeAttr.put("name", STR_LR_SELECT);
                    chargesTypeAttr.put("id", STR_LR_SELECT);
                    firstOption=EMPTY_OPT_DESC;
                }else {
                    chargesTypeAttr.put("name", "aoSelect");
                    chargesTypeAttr.put("id", "aoSelect");
                }

                List<String> checkedVals = IaisCommonUtils.genNewArrayList();
                String aoSelect = (String) ParamUtil.getSessionAttr(request, "aoSelect");
                if(!StringUtil.isEmpty(aoSelect)){
                    checkedVals.add(aoSelect);
                }
                ParamUtil.setSessionAttr(request,"aoSelect",null);
                log.info(StringUtil.changeForLog("aoSelect is -->:"+aoSelect));
                String lrSelect = ParamUtil.getRequestString(request, STR_LR_SELECT);
                if(!StringUtil.isEmpty(lrSelect)) {
                    checkedVals.add(lrSelect);
                }
                String chargeTypeSelHtml = SelectHelper.genMutilSelectOpHtml(chargesTypeAttr, getAoSelect(request,stageId, order),
                        firstOption, checkedVals, false,true);

                String aoSelectError = (String) ParamUtil.getSessionAttr(request, "aoSelectError");
                if(!StringUtil.isEmpty(aoSelectError)){
                    aoSelectError = MessageUtil.getMessageDesc(aoSelectError);
                    chargeTypeSelHtml = chargeTypeSelHtml +aoSelectError;
                }
                ParamUtil.setSessionAttr(request,"aoSelectError",null);
                log.info(StringUtil.changeForLog("aoSelectError is -->:"+aoSelectError));
                chargeTypeSelHtml = chargeTypeSelHtml +" </span>";
                ajaxResDto.setResultJson(chargeTypeSelHtml);
            }else{
                ajaxResDto.setResCode(AppConsts.AJAX_RES_CODE_ERROR);
            }
        }
    }


    public void saveDraftEmail(HttpServletRequest request) {
        log.info(StringUtil.changeForLog("the do saveDraftEmail start ...."));

        String mailContent = ParamUtil.getString(request, "mailContent");
        AppPremisesUpdateEmailDto emailDto= (AppPremisesUpdateEmailDto) ParamUtil.getSessionAttr(request,"appPremisesUpdateEmailDto");

        emailDto.setMailContent(mailContent);
        appPremisesCorrClient.saveEmailDraft(emailDto);
        ParamUtil.setSessionAttr(request,"appPremisesUpdateEmailDto",emailDto);

        log.info(StringUtil.changeForLog("the do saveDraftEmail end ...."));
    }


    private List<SelectOption> getAoSelect(HttpServletRequest request, String stageId,int order) {
        log.info(StringUtil.changeForLog("the getAoSelect start ...."));
        List<SelectOption> result = IaisCommonUtils.genNewArrayList();
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(request, "applicationViewDto");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
        applicationDtos.add(applicationDto);
        List<HcsaSvcStageWorkingGroupDto>  hcsaSvcStageWorkingGroupDtos = taskService.generateHcsaSvcStageWorkingGroupDtos(applicationDtos,stageId);
        if (HcsaConsts.ROUTING_STAGE_INS.equals(stageId)||HcsaConsts.ROUTING_STAGE_INP.equals(stageId)) {
            hcsaSvcStageWorkingGroupDtos.forEach(h -> h.setOrder(order));
        }
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(request, STR_TASK_DTO);
        List<TaskDto> taskDtos=taskService.getTaskByUrlAndRefNo(taskDto.getRefNo(),taskDto.getProcessUrl());
        hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
        if(IaisCommonUtils.isNotEmpty(hcsaSvcStageWorkingGroupDtos)){
            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = hcsaSvcStageWorkingGroupDtos.get(0);
            String workGroupId = hcsaSvcStageWorkingGroupDto.getGroupId();
            List<OrgUserDto> orgUserDtos = taskService.getUsersByWorkGroupIdExceptLeader(workGroupId,AppConsts.COMMON_STATUS_ACTIVE);
            extractedGetAoSelect(result, taskDtos, workGroupId, orgUserDtos);
        }

        log.info(StringUtil.changeForLog("the getAoSelect end ...."));
        return result;
    }

    private void extractedGetAoSelect(List<SelectOption> result, List<TaskDto> taskDtos, String workGroupId, List<OrgUserDto> orgUserDtos) {
        if(IaisCommonUtils.isNotEmpty(orgUserDtos)){
            for(OrgUserDto orgUserDto : orgUserDtos){
                boolean hasTask=false;
                for (TaskDto task: taskDtos
                ) {
                    if(task.getUserId().equals(orgUserDto.getId())){
                        hasTask=true;
                    }
                }
                if(!hasTask){
                    result.add(new SelectOption(workGroupId + "_" + orgUserDto.getId(),orgUserDto.getDisplayName()));
                }
            }
        }
    }
    /**
     * StartStep: doStart
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc) throws IOException {
        log.debug(StringUtil.changeForLog("the do cleanSession start ...."));
        ParamUtil.setSessionAttr(bpc.request, "taskDto", null);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", null);
        ParamUtil.setSessionAttr(bpc.request, "isSaveRfiSelect", null);
        ParamUtil.setSessionAttr(bpc.request, "rfiAppEditSelectDto", null);
        ParamUtil.setSessionAttr(bpc.request, "nextStages", null);
        ParamUtil.setSessionAttr(bpc.request, "nextStageReply", null);
        ParamUtil.setSessionAttr(bpc.request, "premiseMiscDto", null);
        ParamUtil.setSessionAttr(bpc.request, "oldApplicationNo", null);
        ParamUtil.setSessionAttr(bpc.request, "feAppealSpecialDocDto", null);
        ParamUtil.setSessionAttr(bpc.request, "Ao1Ao2Approve", "N");
        ParamUtil.setSessionAttr(bpc.request, "isChooseInspection", Boolean.FALSE);
        ParamUtil.setSessionAttr(bpc.request, "chooseInspectionChecked", "N");
        ParamUtil.setSessionAttr(bpc.request, "AppLastInsGroup", null);
        ParamUtil.setSessionAttr(bpc.request, "appealRecommendationValueOnlyShow", "");
        ParamUtil.setSessionAttr(bpc.request, "isDMS", null);
        ParamUtil.setSessionAttr(bpc.request, "finalStage", Boolean.FALSE);
        ParamUtil.setSessionAttr(bpc.request,"aoSelect",null);
        ParamUtil.setSessionAttr(bpc.request, "doCheckList", null);
        ParamUtil.setSessionAttr(bpc.request,"recommendationOnlyShow",null);
        ParamUtil.setSessionAttr(bpc.request, "appPremisesRecommendationDtoEdit", null);
        ParamUtil.setSessionAttr(bpc.request, "finish_ahoc_check_list", null);
        ParamUtil.setSessionAttr(bpc.request,"userOnlyTypeRecommendationDto",null);

        vehicleCommonController.clearVehicleInformationSession(bpc.request);
        ParamUtil.setSessionAttr(bpc.request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DECIDE,null);
        SearchParam searchParamGroup = (SearchParam) ParamUtil.getSessionAttr(bpc.request, "backendinboxSearchParam");
        ParamUtil.setSessionAttr(bpc.request, "backSearchParamFromHcsaApplication", searchParamGroup);

        log.debug(StringUtil.changeForLog("the do cleanSession end ...."));

        initData(bpc);
    }

    /**
     * StartStep: prepareData
     *
     * @param bpc
     * @throws
     */
    public void prepareData(BaseProcessClass bpc) throws Exception {
        log.debug(StringUtil.changeForLog("the do prepareData start ..."));

        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
        String doViewEmail = ParamUtil.getString(bpc.request, "perViewEmail");
        if ( "Y".equals(doViewEmail)) {
            ParamUtil.setRequestAttr(bpc.request,"doProcess","Y");
        }

        String correlationId;
        if (taskDto != null) {
            correlationId = taskDto.getRefNo();
        } else {
            throw new IaisRuntimeException("The Task Id  is Error !!!");
        }
        log.debug(StringUtil.changeForLog("the do prepareData get the NewAppPremisesCorrelationDto"));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        if (applicationViewDto == null) {
            AppPremisesCorrelationDto appPremisesCorrelationDto = applicationViewService.getLastAppPremisesCorrelationDtoById(correlationId);
            appPremisesCorrelationDto.setOldCorrelationId(correlationId);
            String newCorrelationId = appPremisesCorrelationDto.getId();
            applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(newCorrelationId,taskDto.getRoleId());
            applicationViewDto.setNewAppPremisesCorrelationDto(appPremisesCorrelationDto);
            //set can tcu date
            setShowAndEditTcuDate(bpc.request,applicationViewDto,taskDto);
        }
        log.debug(StringUtil.changeForLog("the do prepareData get the appEditSelectDto"));
        bpc.request.getSession().removeAttribute("appEditSelectDto");
        bpc.request.getSession().removeAttribute("pageAppEditSelectDto");
        String roleId = taskDto.getRoleId();
        log.debug(StringUtil.changeForLog("the do prepareData get the appPremisesRecommendationDto"));
        log.debug(StringUtil.changeForLog("the do prepareData roleId -->:" + roleId));

        //set selection value
        setSelectionValue(bpc.request, applicationViewDto, taskDto);
        //check inspection
        checkShowInspection(bpc, applicationViewDto, taskDto, taskDto.getRefNo());
        //set recommendation show name
        checkRecommendationShowName(bpc, applicationViewDto);
        //set verified dropdown value, 114456 from initData(bpc) function
        setVerifiedDropdownValue(bpc.request, applicationViewDto, taskDto);
        //set session
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
        // for edit application
        checkForEditingApplication(bpc.request);

        Map<String, String> maxCountMap = IaisCommonUtils.genNewHashMap(2);
        maxCountMap.put("maxlength", Formatter.formatNumber(50, "#,##0"));
        maxCountMap.put("field", "Document");
        ParamUtil.setRequestAttr(bpc.request, "maxCountMap", maxCountMap);
        boolean hasEmailAttaDoc=false;
        if(IaisCommonUtils.isNotEmpty(applicationViewDto.getAppIntranetDocDtoList())){
            for (AppIntranetDocDto doc:applicationViewDto.getAppIntranetDocDtoList()
            ) {
                if(doc.getAppDocType().equals(ApplicationConsts.APP_DOC_TYPE_EMAIL_ATTACHMENT)){
                    hasEmailAttaDoc=true;
                }
            }
        }
        if(hasEmailAttaDoc){
            ParamUtil.setRequestAttr(bpc.request, "hasEmailAttaDoc", hasEmailAttaDoc);
        }
        List<AppPremisesSelfDeclChklDto> appPremisesSelfDeclChklDtos = applicationClient.getAppPremisesSelfDeclByCorrelationId(correlationId).getEntity();
        if(IaisCommonUtils.isNotEmpty(appPremisesSelfDeclChklDtos)){
            ParamUtil.setSessionAttr(bpc.request,"selfDeclChklShow",Boolean.TRUE);
        }else {
            ParamUtil.setSessionAttr(bpc.request,"selfDeclChklShow",Boolean.FALSE);

        }
        List<AppPremSubSvcRelDto> specialServiceList=applicationViewDto.getAppPremSpecialSubSvcRelDtoList();
        if (IaisCommonUtils.isNotEmpty(specialServiceList)){
            ParamUtil.setRequestAttr(bpc.request, "addSpecialServiceList", specialServiceList.stream()
                    .filter(dto->ApplicationConsts.RECORD_ACTION_CODE_ADD.equals(dto.getActCode()))
                    .collect(Collectors.toList()));
            ParamUtil.setRequestAttr(bpc.request, "removeSpecialServiceList", specialServiceList.stream()
                    .filter(dto->ApplicationConsts.RECORD_ACTION_CODE_REMOVE.equals(dto.getActCode()))
                    .collect(Collectors.toList()));
        }
        List<AppPremSubSvcRelDto> otherServiceList=applicationViewDto.getAppPremOthersSubSvcRelDtoList();
        if (IaisCommonUtils.isNotEmpty(otherServiceList)){
            ParamUtil.setRequestAttr(bpc.request, "addOtherServiceList", otherServiceList.stream()
                    .filter(dto->ApplicationConsts.RECORD_ACTION_CODE_ADD.equals(dto.getActCode()))
                    .collect(Collectors.toList()));
            ParamUtil.setRequestAttr(bpc.request, "removeOtherServiceList", otherServiceList.stream()
                    .filter(dto->ApplicationConsts.RECORD_ACTION_CODE_REMOVE.equals(dto.getActCode()))
                    .collect(Collectors.toList()));
        }
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSP_USE_ONLY).getEntity();
        if(appPremisesRecommendationDto==null){
            appPremisesRecommendationDto=new AppPremisesRecommendationDto();
            appPremisesRecommendationDto.setAppPremCorreId(correlationId);
            appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSP_USE_ONLY);
        }
        ParamUtil.setSessionAttr(bpc.request,"userOnlyTypeRecommendationDto",appPremisesRecommendationDto);

        log.debug(StringUtil.changeForLog("the do prepareData end ...."));
    }

    private void checkForEditingApplication(HttpServletRequest request) {
        // check from editing application
        String appError = ParamUtil.getString(request, HcsaAppConst.ERROR_APP);
        if (StringUtil.isNotEmpty(appError)) {
            ParamUtil.setRequestAttr(request, HcsaAppConst.ERROR_APP, StringUtil.clarify(appError));
        }
        ParamUtil.setRequestAttr(request, HcsaAppConst.SHOW_EDIT_BTN, SpringHelper.getBean(ApplicationDelegator.class).checkData(HcsaAppConst.CHECKED_BTN_SHOW, request));
    }

    /**
     * StartStep: chooseStage
     *
     * @param bpc
     * @throws
     */
    public void chooseStage(BaseProcessClass bpc) throws Exception {
        log.info(StringUtil.changeForLog("the do chooseStage start ...."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        //do upload file
        String doDocument = ParamUtil.getString(bpc.request, "uploadFile");
        String interalFileId = ParamUtil.getString(bpc.request, "interalFileId");
        String actionAdditional = ParamUtil.getRequestString(bpc.request, "crud_action_additional");
        if (!StringUtil.isEmpty(actionAdditional)) {
            if("editChecklist".equals(actionAdditional)){
                ParamUtil.setRequestAttr(bpc.request, "crud_action_type", actionAdditional);
                return;
            }
            if("editInspectorReport".equals(actionAdditional)){
                ParamUtil.setRequestAttr(bpc.request, "crud_action_type", actionAdditional);
                return;
            }
            if("processing".equals(actionAdditional)){
                ParamUtil.setRequestAttr(bpc.request, "crud_action_type", "PREPARE");
                ParamUtil.setRequestAttr(bpc.request,"doProcess","Y");
                return;
            }
        }
        if(IaisCommonUtils.isNotEmpty(applicationViewDto.getAppIntranetDocDtoList())){
            boolean hasAsoEmailDoc=false;
            for (AppIntranetDocDto docDto:applicationViewDto.getAppIntranetDocDtoList()
            ) {
                if(docDto.getAppDocType().equals(ApplicationConsts.APP_DOC_TYPE_EMAIL_ATTACHMENT)){
                    hasAsoEmailDoc=true;
                }
            }
            if(hasAsoEmailDoc){
                ParamUtil.setRequestAttr(bpc.request, "hasAsoEmailDoc", "Y");
            }else {
                ParamUtil.setRequestAttr(bpc.request, "hasAsoEmailDoc", "N");

            }

        }
        if (!StringUtil.isEmpty(interalFileId) || "Y".equals(doDocument)) {
            ParamUtil.setRequestAttr(bpc.request, "crud_action_type", "PREPARE");
            ParamUtil.setRequestAttr(bpc.request, "doDocument", "Y");
            return;
        }
        if(applicationViewDto.getApplicationDto().getStatus().equals(ApplicationConsts.APPLICATION_STATUS_ASO_EMAIL_PENDING)){
            int contentSize = ParamUtil.getInt(bpc.request, SystemAdminBaseConstants.TEMPLATE_CONTENT_SIZE);
            ParamUtil.setSessionAttr(bpc.request, "confirm_err_msg", MessageUtil.replaceMessage("EMM_ERR005","8000","num"));

            if (contentSize > 8000) {
                ParamUtil.setRequestAttr(bpc.request,"doValidEmail","Y");
                ParamUtil.setRequestAttr(bpc.request, "crud_action_type", "PREPARE");
                ParamUtil.setRequestAttr(bpc.request,"doProcess","Y");
                String mailContent = ParamUtil.getString(bpc.request, "mailContent");
                AppPremisesUpdateEmailDto emailDto= (AppPremisesUpdateEmailDto) ParamUtil.getSessionAttr(bpc.request,"appPremisesUpdateEmailDto");
                emailDto.setMailContent(mailContent);
                ParamUtil.setSessionAttr(bpc.request,"appPremisesUpdateEmailDto",emailDto);
                return;
            }

        }
        String doSaveDraftEmail = ParamUtil.getString(bpc.request, "saveDraftEmail");
        if ( "Y".equals(doSaveDraftEmail)) {
            saveDraftEmail(bpc.request);
            ParamUtil.setRequestAttr(bpc.request, "crud_action_type", "PREPARE");
            ParamUtil.setRequestAttr(bpc.request,"doProcess","Y");
            ParamUtil.setRequestAttr(bpc.request,"doSaveDraftEmail","Y");

            return;
        }
        String doViewEmail = ParamUtil.getString(bpc.request, "perViewEmail");
        if ( "Y".equals(doViewEmail)) {
            String mailContent = ParamUtil.getString(bpc.request, "mailContent");
            AppPremisesUpdateEmailDto emailDto= (AppPremisesUpdateEmailDto) ParamUtil.getSessionAttr(bpc.request,"appPremisesUpdateEmailDto");
            emailDto.setMailContent(mailContent);
            ParamUtil.setSessionAttr(bpc.request,"appPremisesUpdateEmailDto",emailDto);
            ParamUtil.setRequestAttr(bpc.request, "crud_action_type", "PREVIEW");

            return;
        }
        //setTcu data
        setTcuDate(bpc.request,applicationViewDto);
        //appeal
        String applicationType = applicationViewDto.getApplicationDto().getApplicationType();
        boolean isAppealType = ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType);
        boolean isWithdrawal = ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(applicationType);
        boolean isCessation = ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationType);
        boolean isChangePeriodAppealType = false;
        boolean isLateFeeAppealType = false;
        if (isAppealType) {
            isChangePeriodAppealType = (boolean) ParamUtil.getSessionAttr(bpc.request, "isChangePeriodAppealType");
            isLateFeeAppealType = (boolean) ParamUtil.getSessionAttr(bpc.request, "isLateFeeAppealType");
        }
        //validate
        HcsaApplicationViewValidate hcsaApplicationViewValidate = new HcsaApplicationViewValidate();
        Map<String, String> errorMap = hcsaApplicationViewValidate.validate(bpc.request);
        //do not have the rfi applicaiton can approve.
        String approveSelect = ParamUtil.getString(bpc.request, "nextStage");
        String nextStageReplys = ParamUtil.getString(bpc.request, "nextStageReplys");
        applicationService.validateCanApprove(approveSelect, applicationViewDto, errorMap);
        if (!errorMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(applicationViewDto.getApplicationDto(), errorMap);
            String doProcess = "Y";
            ParamUtil.setRequestAttr(bpc.request, "doProcess", doProcess);
            ParamUtil.setRequestAttr(bpc.request, "crud_action_type", "PREPARE");
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
        } else {
            TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
            String roleId = taskDto.getRoleId();
            String status = applicationViewDto.getApplicationDto().getStatus();
            boolean isAsoPso = RoleConsts.USER_ROLE_ASO.equals(roleId) || RoleConsts.USER_ROLE_PSO.equals(roleId);
            if (RoleConsts.USER_ROLE_BROADCAST.equals(roleId)) {
                isAsoPso = (boolean) ParamUtil.getSessionAttr(bpc.request, "broadcastAsoPso");
            }
//            String appPremCorreId=taskDto.getRefNo();
            String appPremCorreId = applicationViewDto.getNewAppPremisesCorrelationDto().getId();
            //save recommendation
            String recommendationStr = ParamUtil.getString(bpc.request, "recommendation");
            boolean isDMS = ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(applicationViewDto.getApplicationDto().getStatus());
            String decisionValues = ParamUtil.getString(bpc.request, "decisionValues");
            boolean isRejectDMS = "decisionReject".equals(decisionValues);
            boolean isRequestForChange = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType);
            if (isDMS) {
                if (isRejectDMS) {
                    recommendationStr = "reject";
                }
                if (isRequestForChange) {
                    if ("decisionApproval".equals(decisionValues)) {
                        recommendationStr = "decisionApproval";
                    }
                }
            } else if ((isAppealType || isWithdrawal || isCessation) && isAsoPso) {
                String appealRecommendationValues = ParamUtil.getString(bpc.request, "appealRecommendationValues");
                if ("appealReject".equals(appealRecommendationValues)) {
                    recommendationStr = "reject";
                } else {
                    recommendationStr = "other";
                }
                if(isAppealType && StringUtil.isEmpty(appealRecommendationValues)){
                    recommendationStr = "";
                }
            }
            boolean isFinalStage = (boolean) ParamUtil.getSessionAttr(bpc.request, "finalStage");
            boolean isCessationOrWithdrawalFinalStage = (isCessation || isWithdrawal) && isFinalStage;
            // cessation and withdrawal fina
            if (isCessationOrWithdrawalFinalStage) {
                if (ApplicationConsts.PROCESSING_DECISION_PENDING_APPROVAL.equals(approveSelect)) {
                    recommendationStr = "approve";
                } else if (ApplicationConsts.PROCESSING_DECISION_REJECT.equals(approveSelect)) {
                    recommendationStr = "reject";
                }
            }

            String dateStr = ParamUtil.getDate(bpc.request, "tuc");
            String dateTimeShow = ParamUtil.getString(bpc.request, "dateTimeShow");
            boolean saveRecommenFlag = true;
            if (StringUtil.isEmpty(recommendationStr)) {
                //PSO route back to ASO,PSO clear recommendation or licence start date
                if (ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING.equals(status) && RoleConsts.USER_ROLE_PSO.equals(roleId) && ApplicationConsts.PROCESSING_DECISION_ROLLBACK.equals(approveSelect)) {
                    AppPremisesRecommendationDto clearRecommendationDto = getClearRecommendationDto(appPremCorreId, dateStr, dateTimeShow);
                    insRepService.updateRecommendation(clearRecommendationDto);
                }
                //PSO route back to ASO,ASO clear recommendation or licence start date
                if (ApplicationConsts.APPLICATION_STATUS_PSO_ROUTE_BACK.equals(status) && RoleConsts.USER_ROLE_ASO.equals(roleId) && ApplicationConsts.PROCESSING_DECISION_REPLY.equals(nextStageReplys)) {
                    AppPremisesRecommendationDto clearRecommendationDto = getClearRecommendationDto(appPremCorreId, dateStr, dateTimeShow);
                    insRepService.updateRecommendation(clearRecommendationDto);
                }
                //ASO route to PSO
                if (ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(status) && RoleConsts.USER_ROLE_ASO.equals(roleId) && ApplicationConsts.PROCESSING_DECISION_VERIFIED.equals(approveSelect)) {
                    AppPremisesRecommendationDto clearRecommendationDto = getClearRecommendationDto(appPremCorreId, dateStr, dateTimeShow);
                    insRepService.updateRecommendation(clearRecommendationDto);
                }
            } else if(!ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION.equals(status)) {
                if (("reject").equals(recommendationStr)) {
                    AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
                    appPremisesRecommendationDto.setRecomDecision(InspectionReportConstants.REJECTED);
                    appPremisesRecommendationDto.setAppPremCorreId(appPremCorreId);
                    appPremisesRecommendationDto.setRecomInNumber(0);
                    appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT);
                    if (isAppealType || isWithdrawal || isCessation || isRequestForChange) {
                        //                    appPremisesRecommendationDto.setRecomDecision("reject");
                        appPremisesRecommendationDto.setRecomDecision(InspectionReportConstants.RFC_REJECTED);
                    }
                    appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    //save date
                    if (!StringUtil.isEmpty(dateStr)) {
                        Date date = Formatter.parseDate(dateStr);
                        appPremisesRecommendationDto.setRecomInDate(date);
                    } else if (!StringUtil.isEmpty(dateTimeShow) && !"-".equals(dateTimeShow)) {
                        Date date = Formatter.parseDate(dateTimeShow);
                        appPremisesRecommendationDto.setRecomInDate(date);
                    }
                    insRepService.updateRecommendation(appPremisesRecommendationDto);
                } else {
                    AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
                    appPremisesRecommendationDto.setAppPremCorreId(appPremCorreId);
                    appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT);
                    appPremisesRecommendationDto.setRecomDecision(InspectionReportConstants.APPROVED);
                    if (isAppealType || isWithdrawal || isCessation || isRequestForChange) {
                        //                    appPremisesRecommendationDto.setRecomDecision("approve");
                        appPremisesRecommendationDto.setRecomDecision(InspectionReportConstants.RFC_APPROVED);
                    }
                    if ("other".equals(recommendationStr)) {
                        if (!ApplicationConsts.PROCESSING_DECISION_ROLLBACK.equals(approveSelect)) {
                            if ((isAppealType && isChangePeriodAppealType) || !isAppealType) {
                                String number = ParamUtil.getString(bpc.request, "number");
                                if (!StringUtil.isEmpty(number)) {
                                    String chrono = ParamUtil.getString(bpc.request, "chrono");
                                    appPremisesRecommendationDto.setRecomInNumber(Integer.valueOf(number));
                                    appPremisesRecommendationDto.setChronoUnit(chrono);
                                } else {
                                    saveRecommenFlag = false;
                                }
                            }
                        } else {
                            saveRecommenFlag = false;
                        }
                        if (isAppealType) {
                            //                        appPremisesRecommendationDto.setRecomDecision("approve");
                            appPremisesRecommendationDto.setRecomDecision(InspectionReportConstants.RFC_APPROVED);
                            if (isLateFeeAppealType) {
                                String returnFee = ParamUtil.getString(bpc.request, "returnFee");
                                appPremisesRecommendationDto.setRemarks(returnFee);
                            }
                        }
                    } else {
                        if (isRequestForChange || isCessationOrWithdrawalFinalStage) {
                            recommendationStr = "6 DTPE002";
                        }
                        String[] strs = recommendationStr.split("\\s+");
                        appPremisesRecommendationDto.setRecomInNumber(Integer.valueOf(strs[0]));
                        appPremisesRecommendationDto.setChronoUnit(strs[1]);
                    }
                    //save date
                    if (!StringUtil.isEmpty(dateStr)) {
                        Date date = Formatter.parseDate(dateStr);
                        appPremisesRecommendationDto.setRecomInDate(date);
                    } else if (!StringUtil.isEmpty(dateTimeShow) && !"-".equals(dateTimeShow)) {
                        Date date = Formatter.parseDate(dateTimeShow);
                        appPremisesRecommendationDto.setRecomInDate(date);
                    }
                    if (saveRecommenFlag) {
                        insRepService.updateRecommendation(appPremisesRecommendationDto);
                    }
                }
            }
            String verified = ParamUtil.getString(bpc.request, "verified");
            String rollBack = ParamUtil.getMaskedString(bpc.request, "rollBack");
            String rollBackCr = ParamUtil.getString(bpc.request, "rollBackCr");
            String nextStage = null;

            boolean chooseInspection = (boolean) ParamUtil.getSessionAttr(bpc.request, "isChooseInspection");
            boolean needSaveInspection = (ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING.equals(status) || ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(status)) && chooseInspection;
            if (needSaveInspection) {
                String[] chooseInspections = ParamUtil.getStrings(bpc.request, "chooseInspection");
                AppLastInsGroup appLastInsGroup = (AppLastInsGroup) ParamUtil.getSessionAttr(bpc.request, "AppLastInsGroup");
                if (appLastInsGroup != null) {
                    if (chooseInspections != null) {
                        applicationClient.saveAppPremisesRecommendationDtoForLastInsp(appLastInsGroup.getAppId(), appLastInsGroup.getOldAppId());
                        if (RoleConsts.USER_ROLE_AO1.equals(verified) || RoleConsts.USER_ROLE_AO2.equals(verified) || RoleConsts.USER_ROLE_AO3.equals(verified)) {
                            applicationClient.saveLastInsForSixMonthToRenew(appLastInsGroup.getAppId(), appLastInsGroup.getOldAppId());
                        }
                    } else {
                        applicationClient.deleteAppPremisesRecommendationDtoForLastInsp(appPremCorreId);
                        ParamUtil.setSessionAttr(bpc.request, "chooseInspectionChecked", "N");
                    }
                }
            }

            //62875
            String stage = ParamUtil.getString(bpc.request, "nextStage");

            if (RoleConsts.USER_ROLE_AO3.equals(taskDto.getRoleId()) &&
                    ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(applicationViewDto.getApplicationDto().getStatus())) {
                if (!StringUtil.isEmpty(stage)) {
                    nextStage = stage;
                }
            }
            if(ApplicationConsts.PROCESSING_DECISION_BROADCAST_QUERY.equals(stage)){
                nextStage = stage;
            }
            if (!StringUtil.isEmpty(rollBack) && ApplicationConsts.PROCESSING_DECISION_ROLLBACK.equals(stage)) {
                nextStage = "PROCRB";
            } else if (!StringUtil.isEmpty(verified) && ApplicationConsts.PROCESSING_DECISION_VERIFIED.equals(stage)) {
                nextStage = verified;
            }
            if (!StringUtil.isEmpty(rollBackCr) && ApplicationConsts.PROCESSING_DECISION_ROLLBACK_CR.equals(stage)) {
                nextStage = stage;
            }
            if(ApplicationConsts.PROCESSING_DECISION_ASO_SEND_EMAIL.equals(stage)){
                if(ApplicationConsts.APPLICATION_STATUS_ASO_EMAIL_PENDING.equals(applicationViewDto.getApplicationDto().getStatus())){
                    nextStage = stage;
                }else {
                    nextStage = "PROCAP";
                }
            }
            if(ApplicationConsts.PROCESSING_DECISION_ROUTE_LATERALLY.equals(stage)){
                nextStage = taskDto.getRoleId();
            }
            //request for information
            if (ApplicationConsts.PROCESSING_DECISION_REQUEST_FOR_INFORMATION.equals(stage)) {
                nextStage = stage;
                Map<String, String> map = applicationService.checkApplicationByAppGrpNo(
                        applicationViewDto.getApplicationGroupDto().getGroupNo());
                String canEdit = map.get(HcsaAppConst.CAN_RFI);
                if (AppConsts.NO.equals(canEdit)) {
                    String appError = map.get(HcsaAppConst.ERROR_APP);
                    if (StringUtil.isNotEmpty(appError)) {
                        ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.ERROR_APP, appError);
                        nextStage = "PREPARE";
                    }
                } else {
                    String appNo = applicationViewDto.getApplicationDto().getApplicationNo();
                    String appStatus = map.get(appNo);
                    if (StringUtil.isNotEmpty(appStatus) && IaisCommonUtils.getNonDoRFIStatus().contains(appStatus)) {
                        ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.ERROR_APP, MessageUtil.replaceMessage("GENERAL_ERR0061",
                                "edited", "action"));
                        nextStage = "PREPARE";
                    }
                }
            }

            String reply = ParamUtil.getString(bpc.request, "nextStageReplys");
            if (!StringUtil.isEmpty(reply)) {
                nextStage = reply;
                if (ApplicationConsts.PROCESSING_DECISION_REQUEST_FOR_INFORMATION.equals(reply)) {
                    nextStage = reply;
                    Map<String, String> map = applicationService.checkApplicationByAppGrpNo(
                            applicationViewDto.getApplicationGroupDto().getGroupNo());
                    String canEdit = map.get(HcsaAppConst.CAN_RFI);
                    if (AppConsts.NO.equals(canEdit)) {
                        String appError = map.get(HcsaAppConst.ERROR_APP);
                        if (StringUtil.isNotEmpty(appError)) {
                            ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.ERROR_APP, appError);
                            nextStage = "PREPARE";
                        }
                    } else {
                        String appNo = applicationViewDto.getApplicationDto().getApplicationNo();
                        String appStatus = map.get(appNo);
                        if (StringUtil.isNotEmpty(appStatus) && IaisCommonUtils.getNonDoRFIStatus().contains(appStatus)) {
                            ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.ERROR_APP, MessageUtil.replaceMessage("GENERAL_ERR0061",
                                    "edited", "action"));
                            nextStage = "PREPARE";
                        }
                    }
                }
            }

            //DMS to end
            if (isDMS) {
                if (isRejectDMS) {
                    nextStage = "PROCREJ";
                } else {
                    nextStage = "PROCAP";
                }
                if(ApplicationConsts.PROCESSING_DECISION_ROUTE_LATERALLY.equals(decisionValues)){
                    if(ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(applicationViewDto.getApplicationDto().getStatus())){
                        nextStage = ApplicationConsts.PROCESSING_DECISION_ROUTE_TO_DMS;
                    }
                }
            }

            //withdrawal and cessation final stage
            if (isFinalStage && (isCessation || isWithdrawal)) {
                if (ApplicationConsts.PROCESSING_DECISION_PENDING_APPROVAL.equals(approveSelect)) {
                    nextStage = "PROCAP";
                } else if (ApplicationConsts.PROCESSING_DECISION_REJECT.equals(approveSelect)) {
                    nextStage = "PROCREJ";
                }
            }

            //114496
            String ao1Ao2Approve = (String) ParamUtil.getSessionAttr(bpc.request, "Ao1Ao2Approve");
            if (ApplicationConsts.PROCESSING_DECISION_PENDING_APPROVAL.equals(approveSelect) && "Y".equals(ao1Ao2Approve)) {
                nextStage = approveSelect;
            }

            log.info(StringUtil.changeForLog("the nextStage is -->:" + nextStage));
            ParamUtil.setRequestAttr(bpc.request, "crud_action_type", nextStage);

            if (ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(applicationViewDto.getApplicationDto().getStatus())
                    || ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING.equals(applicationViewDto.getApplicationDto().getStatus())) {
                String[] fastTracking = ParamUtil.getStrings(bpc.request, "fastTracking");
                if (fastTracking != null) {
                    applicationViewDto.getApplicationDto().setFastTracking(true);
                }
            }
            log.info(StringUtil.changeForLog("the do chooseStage end ...."));
        }
        String easMtsUseOnly = ParamUtil.getString(bpc.request, "easMtsUseOnly");
        if(StringUtil.isNotEmpty(easMtsUseOnly)){
            AppPremisesRecommendationDto appPremisesRecommendationDto= (AppPremisesRecommendationDto) ParamUtil.getSessionAttr(bpc.request, "userOnlyTypeRecommendationDto");
            appPremisesRecommendationDto.setRecomDecision(easMtsUseOnly);
            ParamUtil.setSessionAttr(bpc.request, "userOnlyTypeRecommendationDto",appPremisesRecommendationDto);
        }
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
    }

    private void setTcuDate(HttpServletRequest request, ApplicationViewDto applicationViewDto){
        if(applicationViewDto.isShowTcu() && applicationViewDto.isEditTcu()){
            String tcuflag = ParamUtil.getString(request,"tcuType");
            if(!StringUtil.isEmpty(tcuflag)){
                applicationViewDto.setTcuFlag(true);
                applicationViewDto.setTuc(ParamUtil.getString(request,"tucDate"));
            }else{
                applicationViewDto.setTcuFlag(false);
                applicationViewDto.setTuc(null);
            }
        }
    }

    /**
     * StartStep: chooseAckValue
     */

    public void chooseAckValue(BaseProcessClass bpc) {
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
        String roleId = taskDto.getRoleId();
        String successInfo = "LOLEV_ACK018";
        String nextStage = ParamUtil.getString(bpc.request, "nextStage");
        String nextStageReplys = ParamUtil.getString(bpc.request, "nextStageReplys");
        String verified = "";
        String rollBack = "";
        String rollBackCr = "";
        if (ApplicationConsts.PROCESSING_DECISION_VERIFIED.equals(nextStage)) {
            verified = ParamUtil.getString(bpc.request, "verified");
        } else if (ApplicationConsts.PROCESSING_DECISION_ROLLBACK.equals(nextStage)) {
            rollBack = ParamUtil.getMaskedString(bpc.request, "rollBack");
        }else if (ApplicationConsts.PROCESSING_DECISION_ROLLBACK_CR.equals(nextStage)) {
            rollBackCr = ParamUtil.getString(bpc.request, "rollBackCr");
        }
        String decisionValue = ParamUtil.getString(bpc.request, "decisionValues");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        if(!ApplicationConsts.PROCESSING_DECISION_ROLLBACK_CR.equals(nextStage) &&!ApplicationConsts.PROCESSING_DECISION_ROLLBACK.equals(nextStage) && applicationViewDto.isShowTcu() && applicationViewDto.isEditTcu()){
            insepctionNcCheckListService.saveTcuDate(applicationViewDto.getAppPremisesCorrelationId(),applicationViewDto.getTuc());
        }
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        boolean isWithdrawal = ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(applicationDto.getApplicationType());
        boolean isCessation = ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationDto.getApplicationType());
        boolean isFinalStage = (boolean) ParamUtil.getSessionAttr(bpc.request, "finalStage");
        String status = applicationDto.getStatus();
        //cessation and withdrawal final stage
        boolean isCessationOrWithdrawalFinalStage = (isCessation || isWithdrawal) && isFinalStage;
        String crud_action_type = (String) ParamUtil.getRequestAttr(bpc.request, "crud_action_type");
        //isDMS
        String isDMS = (String) ParamUtil.getSessionAttr(bpc.request, "isDMS");
        //replay "PROCREP"
        if ("isDMS".equals(isDMS)) {
            if ("decisionApproval".equals(decisionValue)) {
                //DMS APPROVAL
                successInfo = "LOLEV_ACK026";
            } else if ("decisionReject".equals(decisionValue)) {
                //DMS REJECT
                successInfo = "LOLEV_ACK027";
            } else if (ApplicationConsts.PROCESSING_DECISION_ASO_SEND_EMAIL.equals(decisionValue)) {
                //ASO Email
                successInfo = "LOLEV_ACK055";
            } else if (ApplicationConsts.PROCESSING_DECISION_ROUTE_LATERALLY.equals(decisionValue)) {
                //ASO Email
                successInfo = "LOLEV_ACK057";
            } else {
                successInfo = "LOLEV_ACK028";
            }
        } else {
            //ASO PSO
            if ((RoleConsts.USER_ROLE_ASO.equals(roleId) || RoleConsts.USER_ROLE_PSO.equals(roleId)) && !isCessationOrWithdrawalFinalStage) {
                successInfo = "LOLEV_ACK018";
            }
            //ao3 approve and reject
            if ((RoleConsts.USER_ROLE_AO3.equals(roleId) || isCessationOrWithdrawalFinalStage) && ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(status)) {
                //AO3 APPROVAL
                successInfo = "LOLEV_ACK020";
            } else if ((RoleConsts.USER_ROLE_AO3.equals(roleId) || isCessationOrWithdrawalFinalStage) && ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(status)) {
                //AO3 REJECT
                successInfo = "LOLEV_ACK022";
            }
            //verified
            if (!StringUtil.isEmpty(verified)) {
                //AO1 -> AO2
                if (RoleConsts.USER_ROLE_AO1.equals(roleId) && RoleConsts.USER_ROLE_AO2.equals(verified)) {
                    successInfo = "LOLEV_ACK009";
                } else if (RoleConsts.USER_ROLE_AO2.equals(roleId) && RoleConsts.USER_ROLE_AO3.equals(verified)) {
                    //AO2 -> AO3
                    successInfo = "LOLEV_ACK013";
                } else if (ApplicationConsts.PROCESSING_DECISION_PENDING_APPROVAL.equals(verified)) {
                    successInfo = "LOLEV_ACK020";
                } else if (ApplicationConsts.PROCESSING_DECISION_REJECT.equals(verified)) {
                    successInfo = "LOLEV_ACK022";
                }
            } else if (!StringUtil.isEmpty(rollBack)) {
                //roll back
                successInfo = "LOLEV_ACK002";
                if (RoleConsts.USER_ROLE_AO1.equals(roleId)) {
                    //AO1
                    successInfo = "LOLEV_ACK002";
                } else if (RoleConsts.USER_ROLE_AO2.equals(roleId)) {
                    //AO2
                    successInfo = "LOLEV_ACK014";
                } else if ((RoleConsts.USER_ROLE_AO3.equals(roleId))) {
                    //AO3
                    successInfo = "LOLEV_ACK025";
                }
            }else if (!StringUtil.isEmpty(rollBackCr)) {
                //roll back
                successInfo = "INSPE_ACK002";

            } else if (RoleConsts.USER_ROLE_AO3.equals(roleId) && ApplicationConsts.PROCESSING_DECISION_ROUTE_TO_DMS.equals(nextStage)) {
                //AO3 DMS
                successInfo = "LOLEV_ACK024";
            }
        }
        //give clarification
        if (StringUtil.isEmpty(nextStage) && ApplicationConsts.PROCESSING_DECISION_REPLY.equals(nextStageReplys) && !"isDMS".equals(isDMS)) {
            successInfo = "LOLEV_ACK028";
        }
        //CR 2022-23
        if (StringUtil.isEmpty(nextStage) && ApplicationConsts.PROCESSING_DECISION_ROUTE_LATERALLY.equals(nextStageReplys) && !"isDMS".equals(isDMS)) {
            successInfo = "LOLEV_ACK057";
        }
        //request for information CR22
        if (StringUtil.isEmpty(nextStage) && ApplicationConsts.PROCESSING_DECISION_REQUEST_FOR_INFORMATION.equals(nextStageReplys) && !"isDMS".equals(isDMS)) {
            successInfo = MessageUtil.dateIntoMessage("RFI_ACK001");
            ParamUtil.setRequestAttr(bpc.request, "rfiSuccessInfo", "Y");
        }

        //request for information
        if (ApplicationConsts.PROCESSING_DECISION_REQUEST_FOR_INFORMATION.equals(nextStage)) {
            successInfo = MessageUtil.dateIntoMessage("RFI_ACK001");
            ParamUtil.setRequestAttr(bpc.request, "rfiSuccessInfo", "Y");
        }

        if (ApplicationConsts.PROCESSING_DECISION_PENDING_APPROVAL.equals(nextStage)) {
            successInfo = "LOLEV_ACK020";
        }
        if (ApplicationConsts.PROCESSING_DECISION_REJECT.equals(nextStage)) {
            successInfo = "LOLEV_ACK022";
        }
        //be cessation flow
        if (isCessation) {
            List<AppPremisesRoutingHistoryDto> rollBackHistroyList = applicationClient.getHistoryByAppNoAndDecision(applicationDto.getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_CESSATION_BE_DECISION).getEntity();
            if (!IaisCommonUtils.isEmpty(rollBackHistroyList) && rollBackHistroyList.size() < 2) {
                if (ApplicationConsts.PROCESSING_DECISION_REJECT.equals(nextStage)) {
                    successInfo = "LOLEV_ACK048";
                }
            }
        }
        //withdrawal aso approve or reject flow
        if (isWithdrawal && isFinalStage) {
            if (ApplicationConsts.PROCESSING_DECISION_REJECT.equals(nextStage)) {
                successInfo = "LOLEV_ACK049";
            } else if (ApplicationConsts.PROCESSING_DECISION_PENDING_APPROVAL.equals(nextStage)) {
                successInfo = "LOLEV_ACK050";
            }
        }

        //114496
        String ao1Ao2Approve = (String) ParamUtil.getSessionAttr(bpc.request, "Ao1Ao2Approve");
        if (ApplicationConsts.PROCESSING_DECISION_PENDING_APPROVAL.equals(nextStage) && "Y".equals(ao1Ao2Approve)) {
            successInfo = "LOLEV_ACK020";
        }
        if (ApplicationConsts.PROCESSING_DECISION_ASO_SEND_EMAIL.equals(nextStage) ) {
            if(RoleConsts.USER_ROLE_ASO.equals(roleId)){
                successInfo = "LOLEV_ACK054";
            }else {
                successInfo = "LOLEV_ACK055";
            }
        }
        if (ApplicationConsts.PROCESSING_DECISION_ROUTE_LATERALLY.equals(nextStage) ) {
            successInfo = "LOLEV_ACK057";
        }
        ParamUtil.setRequestAttr(bpc.request, "successInfo", successInfo);
    }

    /**
     * StartStep: rontingTaskToPSO
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToPSO(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException, IOException, TemplateException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToPSO start ...."));
        routingTask(bpc, HcsaConsts.ROUTING_STAGE_PSO, ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING, RoleConsts.USER_ROLE_PSO);
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
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
        //base flow
        if(!StringUtil.isEmpty(applicationDto.getBaseServiceId())) {
            hcsaSvcStageWorkingGroupDto.setServiceId(applicationDto.getBaseServiceId());
        } else {
            hcsaSvcStageWorkingGroupDto.setServiceId(applicationDto.getServiceId());
        }
        hcsaSvcStageWorkingGroupDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
        hcsaSvcStageWorkingGroupDto.setOrder(1);
        hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
        HcsaSvcStageWorkingGroupDto dto = hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity();
        if ("round".equals(dto.getSchemeType())) {
            routingTask(bpc, HcsaConsts.ROUTING_STAGE_INS, ApplicationConsts.APPLICATION_STATUS_PENDING_APPOINTMENT_SCHEDULING, RoleConsts.USER_ROLE_INSPECTIOR);
        } else {
            routingTask(bpc, HcsaConsts.ROUTING_STAGE_INS, ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT, RoleConsts.USER_ROLE_INSPECTIOR);
        }

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
        routingTask(bpc, HcsaConsts.ROUTING_STAGE_ASO, ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING, RoleConsts.USER_ROLE_ASO);
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
        routingTask(bpc, HcsaConsts.ROUTING_STAGE_AO1, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01, RoleConsts.USER_ROLE_AO1);
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
        routingTask(bpc, HcsaConsts.ROUTING_STAGE_AO2, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02, RoleConsts.USER_ROLE_AO2);
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
        routingTask(bpc, HcsaConsts.ROUTING_STAGE_AO3, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03, RoleConsts.USER_ROLE_AO3);
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
        routingTask(bpc, null, ApplicationConsts.APPLICATION_STATUS_APPROVED, null);
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
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        ApplicationDto application = applicationViewDto.getApplicationDto();
        String decisionValues = ParamUtil.getString(bpc.request, "decisionValues");
        if (application != null) {
            String appNo = application.getApplicationNo();
            log.info(StringUtil.changeForLog("The appNo is -->:" + appNo));
            TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
            List<AppPremisesRoutingHistoryDto> activeHistory = appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryDtosByCorrId(taskDto.getRefNo());
            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = activeHistory
                    .stream()
                    .filter(it->appNo.equals(it.getApplicationNo())
                            &&HcsaConsts.ROUTING_STAGE_INS.equals(it.getStageId())
                            &&RoleConsts.USER_ROLE_INSPECTIOR.equals(it.getRoleId())
                            &&ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT.equals(it.getAppStatus()))
                    .findFirst().orElse(null);
            if (appPremisesRoutingHistoryDto == null) {
                appPremisesRoutingHistoryDto = activeHistory
                        .stream()
                        .filter(it->appNo.equals(it.getApplicationNo())
                                &&HcsaConsts.ROUTING_STAGE_PSO.equals(it.getStageId())
                                &&RoleConsts.USER_ROLE_PSO.equals(it.getRoleId())
                                &&ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING.equals(it.getAppStatus()))
                        .findFirst().orElse(null);
            }
            if (appPremisesRoutingHistoryDto == null) {
                appPremisesRoutingHistoryDto = activeHistory
                        .stream()
                        .filter(it->appNo.equals(it.getApplicationNo())
                                &&HcsaConsts.ROUTING_STAGE_ASO.equals(it.getStageId())
                                &&RoleConsts.USER_ROLE_ASO.equals(it.getRoleId())
                                &&ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(it.getAppStatus()))
                        .findFirst().orElse(null);
            }
            if (appPremisesRoutingHistoryDto != null) {
                String workGroupId=appPremisesRoutingHistoryDto.getWrkGrpId();
                String userId=appPremisesRoutingHistoryDto.getActionby();
                String stageId=appPremisesRoutingHistoryDto.getStageId();
                String roleId=appPremisesRoutingHistoryDto.getRoleId();
                if (ApplicationConsts.PROCESSING_DECISION_ROUTE_LATERALLY.equals(decisionValues)) {
                    String aoSelect = ParamUtil.getRequestString(bpc.request, "lrSelect");
                    log.info(StringUtil.changeForLog("The aoSelect is -->:"+aoSelect));
                    if(StringUtil.isNotEmpty(aoSelect)){
                        String[] aoSelects =  aoSelect.split("_");
                        workGroupId = aoSelects[0];
                        userId = aoSelects[1];
                        stageId=HcsaConsts.ROUTING_STAGE_ASO;
                        roleId=RoleConsts.USER_ROLE_ASO;
                    }
                }
                rollBack(bpc, stageId, ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS,
                        roleId, workGroupId, userId);
            } else {
                log.debug(StringUtil.changeForLog("can not get the appPremisesRoutingHistoryDto ..."));
            }
        } else {
            log.debug(StringUtil.changeForLog("do not have the applicaiton"));
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
        String str = ParamUtil.getMaskedString(bpc.request, "rollBack");
        log.info(StringUtil.changeForLog(str));
        String[] result = str.split(",");
        String stageId = result[0];
        String wrkGpId = result[1];
        String userId = result[2];
        String roleId = result[3];

        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
        String userRoleId = taskDto.getRoleId();
        //else status
        String routeBackStatus = ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_AO;
        //status by
        if (RoleConsts.USER_ROLE_AO1.equals(userRoleId) || RoleConsts.USER_ROLE_AO2.equals(userRoleId) || RoleConsts.USER_ROLE_AO3.equals(userRoleId)) {
            if (HcsaConsts.ROUTING_STAGE_ASO.equals(stageId)) {
                routeBackStatus = ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_ASO;
            } else if (HcsaConsts.ROUTING_STAGE_PSO.equals(stageId)) {
                routeBackStatus = ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_PSO;
            } else if (HcsaConsts.ROUTING_STAGE_INS.equals(stageId) && RoleConsts.USER_ROLE_INSPECTIOR.equals(roleId)) {
                routeBackStatus = ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_INSPECTOR;
            }
        } else if (RoleConsts.USER_ROLE_PSO.equals(userRoleId)) {
            routeBackStatus = ApplicationConsts.APPLICATION_STATUS_PSO_ROUTE_BACK;
        } else if (RoleConsts.USER_ROLE_INSPECTIOR.equals(userRoleId)) {
            routeBackStatus = ApplicationConsts.APPLICATION_STATUS_INSPECTOR_ROUTE_BACK;
        }

        //do roll back
        if (HcsaConsts.ROUTING_STAGE_ASO.equals(stageId)) {
            rollBack(bpc, HcsaConsts.ROUTING_STAGE_ASO, routeBackStatus, RoleConsts.USER_ROLE_ASO, wrkGpId, userId);
        } else if (HcsaConsts.ROUTING_STAGE_PSO.equals(stageId)) {
            rollBack(bpc, HcsaConsts.ROUTING_STAGE_PSO, routeBackStatus, RoleConsts.USER_ROLE_PSO, wrkGpId, userId);
        } else if (HcsaConsts.ROUTING_STAGE_INS.equals(stageId)) {
            if (RoleConsts.USER_ROLE_AO1.equals(roleId)) {
                applicationService.rollBackInspAo1InspLead(bpc, roleId, routeBackStatus, wrkGpId, userId);
            } else if (RoleConsts.USER_ROLE_INSPECTION_LEAD.equals(roleId)){
                applicationService.rollBackInspAo1InspLead(bpc, roleId, routeBackStatus, wrkGpId, userId);
            } else {
                rollBack(bpc, HcsaConsts.ROUTING_STAGE_INS, routeBackStatus, RoleConsts.USER_ROLE_INSPECTIOR, wrkGpId, userId);
            }
        } else if (HcsaConsts.ROUTING_STAGE_AO1.equals(stageId)) {
            rollBack(bpc, HcsaConsts.ROUTING_STAGE_AO1, routeBackStatus, RoleConsts.USER_ROLE_AO1, wrkGpId, userId);
        } else if (HcsaConsts.ROUTING_STAGE_AO2.equals(stageId)) {
            rollBack(bpc, HcsaConsts.ROUTING_STAGE_AO2, routeBackStatus, RoleConsts.USER_ROLE_AO2, wrkGpId, userId);
        } else if (HcsaConsts.ROUTING_STAGE_AO3.equals(stageId)) {
            rollBack(bpc, HcsaConsts.ROUTING_STAGE_AO3, routeBackStatus, RoleConsts.USER_ROLE_AO3, wrkGpId, userId);
        }
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        String internalRemarks = ParamUtil.getString(bpc.request, "internalRemarks");
        //send internal route back email
        String licenseeId = applicationViewDto.getApplicationGroupDto().getLicenseeId();
        try {
            applicationService.sendRfcClarificationEmail(licenseeId, applicationViewDto, internalRemarks, null, userId);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.debug(StringUtil.changeForLog("the do routeBack end ...."));
    }

    /**
     * StartStep: approveWithdrawal
     *
     * @param bpc
     * @throws
     */
    public void approveWithdrawal(BaseProcessClass bpc) {

    }

    /**
     * StartStep: rollBackCr
     *
     * @param bpc
     * @throws
     */
    public void rollBackCr(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do rollBack start ...."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        Map<String, String> rollBackValueMap = (Map<String, String>) ParamUtil.getSessionAttr(bpc.request, "rollBackValueMap");
        String str = rollBackValueMap.get(ParamUtil.getString(bpc.request, "rollBackCr"));
        log.info(StringUtil.changeForLog(str));
        String[] result = str.split(",");
        String stageId = result[0];
        String wrkGpId = result[1];
        String userId = result[2];
        String roleId = result[3];
        String historyId = result[4];
        OrgUserDto user = applicationViewService.getUserById(userId);
        userId=user.getId();
        //do roll back
        if (HcsaConsts.ROUTING_STAGE_ASO.equals(stageId)) {
            inspectionService.rollBackInspectionRecord(applicationViewDto.getAppPremisesCorrelationId(), applicationViewDto.getApplicationDto());
            rollBackTask(bpc, historyId, HcsaConsts.ROUTING_STAGE_ASO, RoleConsts.USER_ROLE_ASO, wrkGpId, userId);
        } else if (HcsaConsts.ROUTING_STAGE_PSO.equals(stageId)) {
            inspectionService.rollBackInspectionRecord(applicationViewDto.getAppPremisesCorrelationId(), applicationViewDto.getApplicationDto());
            rollBackTask(bpc,historyId, HcsaConsts.ROUTING_STAGE_PSO,  RoleConsts.USER_ROLE_PSO, wrkGpId, userId);
        } else if (HcsaConsts.ROUTING_STAGE_INS.equals(stageId)) {
            applicationService.rollBackInsp(bpc, stageId,roleId,  wrkGpId, userId, historyId,ParamUtil.getString(bpc.request, "internalRemarks"));

        } else if (HcsaConsts.ROUTING_STAGE_AO1.equals(stageId)) {
            rollBackTask(bpc,historyId, HcsaConsts.ROUTING_STAGE_AO1, RoleConsts.USER_ROLE_AO1, wrkGpId, userId);
        } else if (HcsaConsts.ROUTING_STAGE_AO2.equals(stageId)) {
            rollBackTask(bpc,historyId, HcsaConsts.ROUTING_STAGE_AO2,  RoleConsts.USER_ROLE_AO2, wrkGpId, userId);
        } else if (HcsaConsts.ROUTING_STAGE_AO3.equals(stageId)) {
            rollBackTask(bpc,historyId, HcsaConsts.ROUTING_STAGE_AO3,  RoleConsts.USER_ROLE_AO3, wrkGpId, userId);
        }
        log.debug(StringUtil.changeForLog("the do rollBack end ...."));
    }

    /**
     * StartStep: asoEmail
     *
     * @param bpc
     * @throws
     */
    public void asoEmail(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do asoSendEmail start ...."));
        HttpServletRequest request=bpc.request;
        String mailContent = ParamUtil.getString(request, STR_MAIL_CONTENT);
        AppPremisesUpdateEmailDto emailDto= (AppPremisesUpdateEmailDto) ParamUtil.getSessionAttr(request,STR_APP_PREM_EMAIL_DTO);
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        emailDto.setMailContent(mailContent);

        appPremisesCorrClient.saveEmailDraft(emailDto);

        applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED);
        applicationService.updateBEApplicaiton(applicationDto);
        boolean allAsoSendEmail=true;
        if(!applicationDto.isFastTracking()){
            List<ApplicationDto> applicationDtoList=applicationService.getApplicaitonsByAppGroupId(applicationDto.getAppGrpId());
            for (ApplicationDto app:applicationDtoList
            ) {
                if(!ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED.equals(app.getStatus())){
                    allAsoSendEmail=false;
                }
            }
        }

        List<AppPremiseMiscDto> appPremiseMiscDtoList= cessationClient.getAppPremiseMiscDtoListByAppId( applicationDto.getId()).getEntity();
        String eventRefNum=null;
        if(IaisCommonUtils.isNotEmpty(appPremiseMiscDtoList)){
            for (AppPremiseMiscDto misc:appPremiseMiscDtoList) {
                if(ApplicationConsts.PROCESSING_DECISION_ASO_SEND_EMAIL.equals(misc.getAppealType())){
                    eventRefNum=misc.getOtherReason();
                    break;
                }
            }
        }
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, STR_TASK_DTO);
        if(allAsoSendEmail){
            if(!StringUtil.isEmpty(eventRefNum)){
                EventBusLicenceGroupDtos eventBusLicenceGroupDtos=licenceService.getEventBusLicenceGroupDtosByRefNo(eventRefNum);
                licenceService.createFESuperLicDto(eventBusLicenceGroupDtos,eventRefNum);
                for (LicenceGroupDto item:eventBusLicenceGroupDtos.getLicenceGroupDtos()) {
                    for (SuperLicDto superLicDto : item.getSuperLicDtos()) {
                        licenceService.sendNotification(superLicDto);
                    }
                }
            }
            List<ApplicationDto> applicationDtoList=applicationService.getApplicaitonsByAppGroupId(applicationDto.getAppGrpId());

            for (ApplicationDto appDto:applicationDtoList
            ) {
                AppPremisesCorrelationDto appPremisesCorrelationDto=applicationClient.getAppPremisesCorrelationDtosByAppId(appDto.getId()).getEntity();
                AppPremisesUpdateEmailDto sendEmailDto=appPremisesCorrClient.retrieveEmailDraft(appPremisesCorrelationDto.getId(),ApplicationConsts.PROCESSING_DECISION_ASO_SEND_EMAIL).getEntity();
                if(sendEmailDto!=null){
                    sendAsoApproveEmail(appDto,sendEmailDto,appPremisesCorrelationDto.getId());
                }
            }

        }
        inspEmailService.completedTask(taskDto);

    }
    private void sendAsoApproveEmail(ApplicationDto applicationDto,AppPremisesUpdateEmailDto emailDto,String refNo){
        String mailContent=emailDto.getMailContent();
        String subject=emailDto.getSubject();
        String applicationNo =applicationDto.getApplicationNo();

        Map<String,Object> map=IaisCommonUtils.genNewHashMap();
        if(StringUtil.isEmpty(mailContent)){
            mailContent="-";
        }
        mailContent = mailContent.replaceAll("\\$\\{","{");
        map.put("msgContent",mailContent);
        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_APPROVED_ID);
        emailParam.setTemplateContent(map);
        emailParam.setQueryCode(applicationNo);
        emailParam.setReqRefNum(applicationNo);
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
        emailParam.setRefId(applicationNo);
        emailParam.setSubject(subject);

        List<AppIntranetDocDto> appIntranetDocDtoList = fillUpCheckListGetAppClient.getAppIntranetDocListByPremIdAndStatus(refNo, AppConsts.COMMON_STATUS_ACTIVE).getEntity();

        if(IaisCommonUtils.isNotEmpty(appIntranetDocDtoList)){
            Map<String, byte[]> attachments =IaisCommonUtils.genNewHashMap();
            boolean hasAtta=false;
            for (AppIntranetDocDto appIntranetDocDto:appIntranetDocDtoList
            ) {
                if(appIntranetDocDto!=null&&appIntranetDocDto.getFileRepoId()!=null&&appIntranetDocDto.getAppDocType().equals(ApplicationConsts.APP_DOC_TYPE_EMAIL_ATTACHMENT)){
                    attachments.put(appIntranetDocDto.getDocName(),fileRepoClient.getFileFormDataBase(appIntranetDocDto.getFileRepoId()).getEntity());
                    hasAtta=true;
                }
            }
            if(hasAtta){
                emailParam.setAttachments(attachments);
            }
        }
        //send email
        log.info(StringUtil.changeForLog("send new application email"));
        notificationHelper.sendNotification(emailParam);
        log.info(StringUtil.changeForLog("send new application email end"));
        //send sms
        EmailParam smsParam = new EmailParam();
        smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_APPROVED_SMS_ID);
        smsParam.setSubject(subject);
        smsParam.setQueryCode(applicationNo);
        smsParam.setReqRefNum(applicationNo);
        smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
        smsParam.setRefId(applicationNo);
        log.info(StringUtil.changeForLog("send new application sms"));
        notificationHelper.sendNotification(smsParam);
        log.info(StringUtil.changeForLog("send new application sms end"));
        //send message
        EmailParam messageParam = new EmailParam();
        messageParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_APPROVED_MESSAGE_ID);
        messageParam.setTemplateContent(map);
        messageParam.setQueryCode(applicationNo);
        messageParam.setReqRefNum(applicationNo);
        messageParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        messageParam.setRefId(applicationNo);
        messageParam.setSubject(subject);
        HcsaServiceDto svcDto = HcsaServiceCacheHelper.getServiceById(applicationDto.getServiceId());
        messageParam.setServiceTypes(svcDto.getSvcCode()+"@");
        log.info(StringUtil.changeForLog("send new application message"));
        notificationHelper.sendNotification(messageParam);
        log.info(StringUtil.changeForLog("send new application message end"));
        log.debug(StringUtil.changeForLog("the do asoSendEmail end ...."));
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



    private void addNewUserToWorkGroup(List<UserGroupCorrelationDto> userGroupCorrelationDtoList,List<String> userIds){
        log.info(StringUtil.changeForLog("The addNewUserToWorkGroup start ..."));
        List<String> oldUserIdList = IaisCommonUtils.genNewArrayList();
        for (UserGroupCorrelationDto userGroupCorrelationDto : userGroupCorrelationDtoList){
            oldUserIdList.add(userGroupCorrelationDto.getUserId());
        }
        if(IaisCommonUtils.isNotEmpty(userIds)){
            for (String userId : userIds) {
                if(!oldUserIdList.contains(userId)){
                    log.info(StringUtil.changeForLog("The addNewUserToWorkGroup new  add userId -->:"+userId));
                    UserGroupCorrelationDto userGroupCorrelationDto = new UserGroupCorrelationDto();
                    userGroupCorrelationDto.setUserId(userId);
                    userGroupCorrelationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    userGroupCorrelationDto.setIsLeadForGroup(Integer.valueOf(AppConsts.NO));
                    userGroupCorrelationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    userGroupCorrelationDtoList.add(userGroupCorrelationDto);
                }
            }
        }
        log.info(StringUtil.changeForLog("The addNewUserToWorkGroup start ..."));
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
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = appPremisesRoutingHistoryService.
                getAppPremisesRoutingHistoryDtosByAppNo(applicationDto.getApplicationNo());
        List<String> userIds = getUserIds(appPremisesRoutingHistoryDtos);
        if (!IaisCommonUtils.isEmpty(userIds)) {
            BroadcastOrganizationDto broadcastOrganizationDto = broadcastService.getBroadcastOrganizationDto(
                    applicationDto.getApplicationNo(), AppConsts.DOMAIN_TEMPORARY);
            BroadcastApplicationDto broadcastApplicationDto = new BroadcastApplicationDto();
            //create workgroup
            WorkingGroupDto workingGroupDto = broadcastOrganizationDto.getWorkingGroupDto();
            broadcastOrganizationDto.setRollBackworkingGroupDto((WorkingGroupDto) CopyUtil.copyMutableObject(workingGroupDto));
            if (workingGroupDto == null) {
                workingGroupDto = new WorkingGroupDto();
                workingGroupDto.setGroupName(applicationDto.getApplicationNo());
                workingGroupDto.setGroupDomain(AppConsts.DOMAIN_TEMPORARY);
            }
            workingGroupDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            workingGroupDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            broadcastOrganizationDto.setWorkingGroupDto(workingGroupDto);

            //add this user to this workgroup
            List<UserGroupCorrelationDto> userGroupCorrelationDtoList = broadcastOrganizationDto.getUserGroupCorrelationDtoList();
            if (broadcastOrganizationDto.getWorkingGroupDto() != null && userGroupCorrelationDtoList != null && userGroupCorrelationDtoList.size() > 0) {
                List<UserGroupCorrelationDto> cloneUserGroupCorrelationDtos = IaisCommonUtils.genNewArrayList();
                CopyUtil.copyMutableObjectList(userGroupCorrelationDtoList, cloneUserGroupCorrelationDtos);
                broadcastOrganizationDto.setRollBackUserGroupCorrelationDtoList(cloneUserGroupCorrelationDtos);
                userGroupCorrelationDtoList = changeStatusUserGroupCorrelationDtos(userGroupCorrelationDtoList, AppConsts.COMMON_STATUS_ACTIVE);
                addNewUserToWorkGroup(userGroupCorrelationDtoList,userIds);
            } else {
                userGroupCorrelationDtoList = IaisCommonUtils.genNewArrayList();
                for (String id : userIds) {
                    UserGroupCorrelationDto userGroupCorrelationDto = new UserGroupCorrelationDto();
                    userGroupCorrelationDto.setUserId(id);
                    userGroupCorrelationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    userGroupCorrelationDto.setIsLeadForGroup(Integer.valueOf(AppConsts.NO));
                    userGroupCorrelationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    userGroupCorrelationDtoList.add(userGroupCorrelationDto);
                }
            }
            broadcastOrganizationDto.setUserGroupCorrelationDtoList(userGroupCorrelationDtoList);

            //complated this task and create the history
            TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
            taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            taskDto = completedTask(taskDto);
            broadcastOrganizationDto.setComplateTask(taskDto);
            String internalRemarks = ParamUtil.getString(bpc.request, "internalRemarks");
            String externalRemarks = ParamUtil.getString(bpc.request, "comments");
            String processDecision = ParamUtil.getString(bpc.request, "nextStage");
            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),
                    applicationDto.getStatus(), taskDto.getTaskKey(), null, taskDto.getWkGrpId(), internalRemarks, externalRemarks, processDecision, taskDto.getRoleId());
            broadcastApplicationDto.setComplateTaskHistory(appPremisesRoutingHistoryDto);
            broadcastApplicationDto.setRollBackApplicationDto((ApplicationDto) CopyUtil.copyMutableObject(applicationDto));
            //update application status
            applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST);
            applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            broadcastApplicationDto.setApplicationDto(applicationDto);
            //create the new task and create the history
            TaskDto taskDtoNew = TaskUtil.getTaskDto(applicationDto.getApplicationNo(), taskDto.getTaskKey(), TaskConsts.TASK_TYPE_MAIN_FLOW, taskDto.getRefNo(),TaskConsts.TASK_STATUS_PENDING, null,
                    null, null, null,0, TaskConsts.TASK_PROCESS_URL_MAIN_FLOW, RoleConsts.USER_ROLE_BROADCAST, IaisEGPHelper.getCurrentAuditTrailDto());
            broadcastOrganizationDto.setCreateTask(taskDtoNew);
            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew = getAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(),
                    taskDto.getTaskKey(), null, taskDto.getWkGrpId(), null, null, null, taskDto.getRoleId());
            broadcastApplicationDto.setNewTaskHistory(appPremisesRoutingHistoryDtoNew);
            //save the broadcast
            broadcastOrganizationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            broadcastApplicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            String submissionId = generateIdClient.getSeqId().getEntity();
            log.info(StringUtil.changeForLog(submissionId));
            String evenRefNum = String.valueOf(System.currentTimeMillis());
            broadcastOrganizationDto.setEventRefNo(evenRefNum);
            broadcastApplicationDto.setEventRefNo(evenRefNum);
            broadcastOrganizationDto = broadcastService.svaeBroadcastOrganization(broadcastOrganizationDto, bpc.process, submissionId);
            broadcastApplicationDto = broadcastService.svaeBroadcastApplicationDto(broadcastApplicationDto, bpc.process, submissionId);
            //0062460 update FE  application status.
            applicationService.updateFEApplicaiton(broadcastApplicationDto.getApplicationDto());
            String licenseeId = applicationViewDto.getApplicationGroupDto().getLicenseeId();
            log.info(StringUtil.changeForLog("The broadcast for licenseeId is -->:"+licenseeId));
            try {
                for(String userId : userIds){
                    applicationService.sendRfcClarificationEmail(licenseeId, applicationViewDto, internalRemarks, null, userId);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
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

    private void deleteTempWorkingGroup(ApplicationDto applicationDto,BaseProcessClass bpc){
        try{
            //delete workgroup
            BroadcastOrganizationDto broadcastOrganizationDto1 = broadcastService.getBroadcastOrganizationDto(
                    applicationDto.getApplicationNo(), AppConsts.DOMAIN_TEMPORARY);
            BroadcastOrganizationDto broadcastOrganizationDto = new BroadcastOrganizationDto();
            WorkingGroupDto workingGroupDto = broadcastOrganizationDto1.getWorkingGroupDto();
            broadcastOrganizationDto.setRollBackworkingGroupDto((WorkingGroupDto) CopyUtil.copyMutableObject(workingGroupDto));
            workingGroupDto = changeStatusWrokGroup(workingGroupDto, AppConsts.COMMON_STATUS_DELETED);
            if (workingGroupDto != null) {
                log.debug(StringUtil.changeForLog("temp workingGroup != null delete"));
                workingGroupDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            }
            broadcastOrganizationDto.setWorkingGroupDto(workingGroupDto);
            List<UserGroupCorrelationDto> userGroupCorrelationDtos = broadcastOrganizationDto1.getUserGroupCorrelationDtoList();
            List<UserGroupCorrelationDto> cloneUserGroupCorrelationDtos = IaisCommonUtils.genNewArrayList();
            CopyUtil.copyMutableObjectList(userGroupCorrelationDtos, cloneUserGroupCorrelationDtos);
            broadcastOrganizationDto.setRollBackUserGroupCorrelationDtoList(cloneUserGroupCorrelationDtos);
            userGroupCorrelationDtos = changeStatusUserGroupCorrelationDtos(userGroupCorrelationDtos, AppConsts.COMMON_STATUS_DELETED);
            broadcastOrganizationDto.setUserGroupCorrelationDtoList(userGroupCorrelationDtos);

            //save the broadcast
            broadcastOrganizationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            String evenRefNum = String.valueOf(System.currentTimeMillis());
            broadcastOrganizationDto.setEventRefNo(evenRefNum);
            String submissionId = generateIdClient.getSeqId().getEntity();
            log.debug(StringUtil.changeForLog(submissionId));
            broadcastService.svaeBroadcastOrganization(broadcastOrganizationDto, bpc.process, submissionId);
            log.debug(StringUtil.changeForLog("delete temp workingGroup end"));
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

    }


    /**
     * StartStep: replay
     *
     * @param bpc
     * @throws
     */
    public void replay(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException, IOException, TemplateException {
        log.debug(StringUtil.changeForLog("the do replay start ...."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
        String nextStatus = ApplicationConsts.APPLICATION_STATUS_REPLY;
        String getHistoryStatus = applicationViewDto.getApplicationDto().getStatus();

        log.info(StringUtil.changeForLog("----------- route back historyStatus : " + getHistoryStatus + "----------"));
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = appPremisesRoutingHistoryService.getSecondRouteBackHistoryByAppNo(
                applicationViewDto.getApplicationDto().getApplicationNo(), getHistoryStatus);
        String wrkGrpId = appPremisesRoutingHistoryDto.getWrkGrpId();
        String roleId = appPremisesRoutingHistoryDto.getRoleId();
        String stageId = appPremisesRoutingHistoryDto.getStageId();
        String userId = appPremisesRoutingHistoryDto.getActionby();
        if (ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(getHistoryStatus)) {
            nextStatus = appPremisesRoutingHistoryDto.getAppStatus();
            //delete template working group
            deleteTempWorkingGroup(applicationDto,bpc);
        } else if (ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(getHistoryStatus)) {
            nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03;
        }

        if (!ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(nextStatus) && HcsaConsts.ROUTING_STAGE_ASO.equals(stageId)) {
            nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING;
        } else if (!ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(nextStatus) && HcsaConsts.ROUTING_STAGE_PSO.equals(stageId)) {
            nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING;
        } else if (!ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(nextStatus) && HcsaConsts.ROUTING_STAGE_INS.equals(stageId)) {
            nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS;
        } else if (!ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(nextStatus) && HcsaConsts.ROUTING_STAGE_AO1.equals(stageId)) {
            nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01;
        } else if (!ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(nextStatus) && HcsaConsts.ROUTING_STAGE_AO2.equals(stageId)) {
            nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02;
        } else if (!ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(nextStatus) && HcsaConsts.ROUTING_STAGE_AO3.equals(stageId)) {
            nextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03;
        }

        String routeHistoryId = appPremisesRoutingHistoryDto.getId();
        AppPremisesRoutingHistoryExtDto historyExtDto = appPremisesRoutingHistoryClient.getAppPremisesRoutingHistoryExtByHistoryAndComponentName(routeHistoryId, ApplicationConsts.APPLICATION_ROUTE_BACK_REVIEW).getEntity();
        if (historyExtDto == null) {
            rollBack(bpc, stageId, nextStatus, roleId, wrkGrpId, userId);
        } else {
            String componentValue = historyExtDto.getComponentValue();
            if ("N".equals(componentValue)) {
                ApplicationGroupDto applicationGroupDto = applicationGroupService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
                List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtoList = applicationViewService.getStage(applicationDto.getServiceId(),
                        stageId, applicationDto.getApplicationType(), applicationGroupDto.getIsPreInspection());
                if (hcsaSvcRoutingStageDtoList != null) {
                    HcsaSvcRoutingStageDto nextStage = hcsaSvcRoutingStageDtoList.get(0);
                    String stageCode = nextStage.getStageCode();
                    String routeNextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02;
                    String nextStageId = HcsaConsts.ROUTING_STAGE_AO2;
                    if (RoleConsts.USER_ROLE_AO3.equals(stageCode)) {
                        nextStageId = HcsaConsts.ROUTING_STAGE_AO3;
                        routeNextStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03;
                    }
                    //update inspection status
                    if(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02.equals(routeNextStatus) || ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(routeNextStatus)){
                        applicationService.updateInspectionStatusByAppNo(applicationDto.getId(), InspectionConstants.INSPECTION_STATUS_PENDING_AO2_RESULT);
                    }
                    routingTask(bpc, nextStageId, routeNextStatus, stageCode);
                } else {
                    log.debug(StringUtil.changeForLog("RoutingStageDtoList is null"));
                }
            } else {
                rollBack(bpc, stageId, nextStatus, roleId, wrkGrpId, userId);
            }
        }
        /*String submissionId = generateIdClient.getSeqId().getEntity();
        BroadcastApplicationDto broadcastApplicationDto = new BroadcastApplicationDto();
        broadcastApplicationDto = broadcastService.setAppPremSubSvcDtoByAppView(broadcastApplicationDto, applicationViewDto, "", "");
        broadcastService.svaeBroadcastApplicationDto(broadcastApplicationDto, bpc.process, submissionId);*/
        if (IaisCommonUtils.isNotEmpty(applicationViewDto.getAppPremSpecialSubSvcRelDtoList())){
            appPremSubSvcBeClient.saveSubServiceDtoList(applicationViewDto.getAppPremSpecialSubSvcRelDtoList());
        }
        if (IaisCommonUtils.isNotEmpty(applicationViewDto.getAppPremOthersSubSvcRelDtoList())){
            appPremSubSvcBeClient.saveSubServiceDtoList(applicationViewDto.getAppPremOthersSubSvcRelDtoList());
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
        routingTask(bpc, null, ApplicationConsts.APPLICATION_STATUS_REJECTED, null);
        log.debug(StringUtil.changeForLog("the do reject end ...."));
    }

    private void rejectSendNotification(BaseProcessClass bpc, ApplicationViewDto applicationViewDto) {
        //send appeal email
        String applicationType = applicationViewDto.getApplicationDto().getApplicationType();
        if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationType)) {
            newAppSendNotification(applicationViewDto);
        } else if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationType)) {
            renewalSendNotification(applicationViewDto);
        } else if (ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)) {
            //send email Appeal - Send SMS to licensee when appeal application is reject
            try {
                sendAppealReject(bpc, applicationViewDto.getApplicationDto(), AppConsts.MOH_AGENCY_NAME);
            } catch (Exception e) {
                log.error(e.getMessage() + "error", e);
            }
        } else if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType)) {
            if(StringUtil.isEmpty(applicationViewDto.getApplicationGroupDto().getNewLicenseeId())){
                rfcRejectedSendNotification( applicationViewDto);
            }else {
                rfcLicenseeRejectedSendNotification( applicationViewDto);
            }
        }

    }
    private void rfcRejectedSendNotification( ApplicationViewDto applicationViewDto) {
        String applicationNo = applicationViewDto.getApplicationDto().getApplicationNo();
        //new application send email
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        ApplicationGroupDto applicationGroupDto =applicationViewDto.getApplicationGroupDto();
        HcsaServiceDto svcDto = HcsaServiceCacheHelper.getServiceById(applicationDto.getServiceId());
        List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
        if (svcDto != null) {
            svcCodeList.add(svcDto.getSvcCode());
        }
        Map<String, Object> rejectMap = IaisCommonUtils.genNewHashMap();
        rejectMap.put("applicationId", applicationNo);
        try {
            String applicantName = "";
            OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
            if (orgUserDto != null) {
                applicantName = orgUserDto.getDisplayName();
            }
            Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
            emailMap.put("ApplicantName", applicantName);
            emailMap.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE}).get(0).getText());
            emailMap.put("ApplicationNumber", applicationNo);
            emailMap.put("ApplicationDate", Formatter.formatDate(applicationGroupDto.getSubmitDt()));
            emailMap.put("email_address", systemParamConfig.getSystemAddressOne());
            emailMap.put("MOH_AGENCY_NAM_GROUP", "<b>" + AppConsts.MOH_AGENCY_NAM_GROUP + "</b>");
            emailMap.put("MOH_AGENCY_NAME", "<b>" + AppConsts.MOH_AGENCY_NAME + "</b>");
            EmailParam emailParam = new EmailParam();
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_004_REJECTED);
            emailParam.setTemplateContent(emailMap);
            emailParam.setQueryCode(applicationNo);
            emailParam.setReqRefNum(applicationNo);
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
            emailParam.setRefId(applicationNo);
            Map<String, Object> map = IaisCommonUtils.genNewHashMap();
            MsgTemplateDto rfiEmailTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_004_REJECTED).getEntity();
            map.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE}).get(0).getText());
            map.put("ApplicationNumber", applicationNo);
            String subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
            emailParam.setSubject(subject);
            //email
            notificationHelper.sendNotification(emailParam);
            //msg
            EmailParam msgParam = new EmailParam();
            msgParam.setQueryCode(applicationNo);
            msgParam.setReqRefNum(applicationNo);
            msgParam.setTemplateContent(emailMap);
            msgParam.setSubject(subject);
            msgParam.setSvcCodeList(svcCodeList);
            msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_004_REJECTED_MSG);
            msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
            msgParam.setRefId(applicationNo);
            notificationHelper.sendNotification(msgParam);
            //sms
            EmailParam smsParam = new EmailParam();
            smsParam.setQueryCode(applicationNo);
            smsParam.setReqRefNum(applicationNo);
            smsParam.setRefId(applicationNo);
            smsParam.setTemplateContent(emailMap);
            smsParam.setSubject(subject);
            smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_004_REJECTED_SMS);
            smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
            notificationHelper.sendNotification(smsParam);
        } catch (Exception e) {
            log.info("-----RFC Application - Send SMS to transferor when licensee transfer application is rejected. licenseeId is null---------");
        }
    }

    private void rfcLicenseeRejectedSendNotification( ApplicationViewDto applicationViewDto) {
        String applicationNo = applicationViewDto.getApplicationDto().getApplicationNo();
        Date date = applicationViewDto.getApplicationGroupDto().getSubmitDt();
        //new application send email
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        ApplicationGroupDto applicationGroupDto =applicationViewDto.getApplicationGroupDto();
        String applicationType = applicationViewDto.getApplicationDto().getApplicationType();
        HcsaServiceDto svcDto = HcsaServiceCacheHelper.getServiceById(applicationDto.getServiceId());
        List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
        if (svcDto != null) {
            svcCodeList.add(svcDto.getSvcCode());
        }
        Map<String, Object> rejectMap = IaisCommonUtils.genNewHashMap();
        rejectMap.put("applicationId", applicationNo);
        try {
            String applicantName = "";
            OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
            if (orgUserDto != null) {
                applicantName = orgUserDto.getDisplayName();
            }
            Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
            LicenseeDto existingLicensee= organizationClient.getLicenseeDtoById(applicationGroupDto.getLicenseeId()).getEntity();
            emailMap.put("ExistingLicensee", existingLicensee.getName());
            LicenseeDto transfereeLicensee= organizationClient.getLicenseeDtoById(applicationGroupDto.getNewLicenseeId()).getEntity();
            emailMap.put("TransfereeLicensee", transfereeLicensee.getName());
            emailMap.put("ApplicantName", applicantName);
            emailMap.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE}).get(0).getText());
            emailMap.put("ApplicationNumber", applicationNo);
            emailMap.put("ApplicationDate", Formatter.formatDate(applicationGroupDto.getSubmitDt()));
            emailMap.put("email_address", systemParamConfig.getSystemAddressOne());
            emailMap.put("MOH_AGENCY_NAM_GROUP", "<b>" + AppConsts.MOH_AGENCY_NAM_GROUP + "</b>");
            emailMap.put("MOH_AGENCY_NAME", "<b>" + AppConsts.MOH_AGENCY_NAME + "</b>");
            EmailParam emailParam = new EmailParam();
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_011_LICENSEE_REJECTED);
            emailParam.setTemplateContent(emailMap);
            emailParam.setQueryCode(applicationNo);
            emailParam.setReqRefNum(applicationNo);
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
            emailParam.setRefId(applicationNo);
            Map<String, Object> map = IaisCommonUtils.genNewHashMap();
            MsgTemplateDto rfiEmailTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_011_LICENSEE_REJECTED).getEntity();
            map.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE}).get(0).getText());
            map.put("ApplicationNumber", applicationNo);
            String subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
            emailParam.setSubject(subject);
            //email
            notificationHelper.sendNotification(emailParam);
            //msg
            EmailParam msgParam = new EmailParam();
            msgParam.setQueryCode(applicationNo);
            msgParam.setReqRefNum(applicationNo);
            msgParam.setTemplateContent(emailMap);
            msgParam.setSubject(subject);
            msgParam.setSvcCodeList(svcCodeList);
            msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_011_LICENSEE_REJECTED_MSG);
            msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
            msgParam.setRefId(applicationNo);
            notificationHelper.sendNotification(msgParam);
            //sms
            EmailParam smsParam = new EmailParam();
            smsParam.setQueryCode(applicationNo);
            smsParam.setReqRefNum(applicationNo);
            smsParam.setRefId(applicationNo);
            smsParam.setTemplateContent(emailMap);
            smsParam.setSubject(subject);
            smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_011_LICENSEE_REJECTED_SMS);
            smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
            notificationHelper.sendNotification(smsParam);
        } catch (Exception e) {
            log.info("-----RFC Application - Send SMS to transferor when licence transfer application is rejected. licenseeId is null---------");
        }
    }

    private void newAppSendNotification(ApplicationViewDto applicationViewDto) {
        log.info(StringUtil.changeForLog("send new application notification start"));
        String applicationNo = applicationViewDto.getApplicationDto().getApplicationNo();
        Date date = applicationViewDto.getApplicationGroupDto().getSubmitDt();
        String appDate = Formatter.formatDateTime(date, "dd/MM/yyyy");
        //new application send email
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String MohName = AppConsts.MOH_AGENCY_NAME;
        String applicationType = applicationViewDto.getApplicationDto().getApplicationType();
        String applicationTypeShow = MasterCodeUtil.getCodeDesc(applicationType);
        HcsaServiceDto svcDto = HcsaServiceCacheHelper.getServiceById(applicationDto.getServiceId());
        List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
        if (svcDto != null) {
            svcCodeList.add(svcDto.getSvcCode());
        }
        //send email
        ApplicationGroupDto applicationGroupDto = applicationGroupService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
        if (applicationGroupDto != null) {
            String groupLicenseeId = applicationGroupDto.getLicenseeId();
            OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
            log.info(StringUtil.changeForLog("send new application notification groupLicenseeId : " + groupLicenseeId));
            if (orgUserDto != null) {
                String applicantName = orgUserDto.getDisplayName();
                log.info(StringUtil.changeForLog("send new application notification applicantName : " + applicantName));
                Map<String, Object> map = IaisCommonUtils.genNewHashMap();
                map.put("ApplicantName", applicantName);
                map.put("applicationType", applicationTypeShow);
                map.put("applicationNumber", applicationNo);
                map.put("applicationDate", appDate);
                map.put("emailAddress", systemAddressOne);
                map.put("MOH_AGENCY_NAME", MohName);
                map.put("BusinessName", applicationViewDto.getHciName());
                map.put("LicenseeName",  applicationViewDto.getSubLicenseeDto().getLicenseeName());
                try {
//                    String subject = "MOH HALP - Your "+ applicationTypeShow + ", "+ applicationNo +" is rejected ";
                    Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
                    subMap.put("ApplicationType", applicationTypeShow);
                    subMap.put("ApplicationNumber", applicationNo);
                    String emailSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_REJECTED_ID, subMap);
                    String smsSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_REJECTED_SMS_ID, subMap);
                    String messageSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_REJECTED_MESSAGE_ID, subMap);
                    log.debug(StringUtil.changeForLog("emailSubject : " + emailSubject));
                    log.debug(StringUtil.changeForLog("smsSubject : " + smsSubject));
                    log.debug(StringUtil.changeForLog("messageSubject : " + messageSubject));
                    EmailParam emailParam = new EmailParam();
                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_REJECTED_ID);
                    emailParam.setTemplateContent(map);
                    emailParam.setQueryCode(applicationNo);
                    emailParam.setReqRefNum(applicationNo);
                    emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
                    emailParam.setRefId(applicationNo);
                    emailParam.setSubject(emailSubject);
                    //send email
                    log.info(StringUtil.changeForLog("send new application email"));
                    notificationHelper.sendNotification(emailParam);
                    //send sms
                    EmailParam smsParam = new EmailParam();
                    smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_REJECTED_SMS_ID);
                    smsParam.setSubject(smsSubject);
                    smsParam.setQueryCode(applicationNo);
                    smsParam.setReqRefNum(applicationNo);
                    smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
                    smsParam.setRefId(applicationNo);
                    log.info(StringUtil.changeForLog("send new application sms"));
                    notificationHelper.sendNotification(smsParam);
                    //send message
                    EmailParam messageParam = new EmailParam();
                    messageParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_REJECTED_MESSAGE_ID);
                    messageParam.setTemplateContent(map);
                    messageParam.setQueryCode(applicationNo);
                    messageParam.setReqRefNum(applicationNo);
                    messageParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                    messageParam.setRefId(applicationNo);
                    messageParam.setSubject(messageSubject);
                    messageParam.setSvcCodeList(svcCodeList);
                    log.info(StringUtil.changeForLog("send new application message"));
                    notificationHelper.sendNotification(messageParam);
                    log.info(StringUtil.changeForLog("send new application notification end"));
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    private void renewalSendNotification(ApplicationViewDto applicationViewDto) {
        log.info(StringUtil.changeForLog("send renewal application notification start"));
        String applicationNo = applicationViewDto.getApplicationDto().getApplicationNo();
        Date date = applicationViewDto.getApplicationGroupDto().getSubmitDt();
        String appDate = Formatter.formatDateTime(date, "dd/MM/yyyy");
        //new application send email
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String MohName = AppConsts.MOH_AGENCY_NAME;
        String applicationType = applicationViewDto.getApplicationDto().getApplicationType();
        String applicationTypeShow = MasterCodeUtil.getCodeDesc(applicationType);
        HcsaServiceDto svcDto = HcsaServiceCacheHelper.getServiceById(applicationDto.getServiceId());
        List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
        if (svcDto != null) {
            svcCodeList.add(svcDto.getSvcCode());
        }
        //send email
        ApplicationGroupDto applicationGroupDto = applicationGroupService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
        if (applicationGroupDto != null) {
            OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
            if (orgUserDto != null) {
                String applicantName = orgUserDto.getDisplayName();
                log.info(StringUtil.changeForLog("send renewal application notification applicantName : " + applicantName));
                Map<String, Object> map = IaisCommonUtils.genNewHashMap();
                map.put("ApplicantName", applicantName);
                map.put("ApplicationType", applicationTypeShow);
                map.put("ApplicationNumber", applicationNo);
                map.put("ApplicationDate", appDate);
                map.put("emailAddress", systemAddressOne);
                map.put("MOH_AGENCY_NAME", MohName);
                map.put("BusinessName", applicationViewDto.getHciName());
                map.put("LicenseeName",  applicationViewDto.getSubLicenseeDto().getLicenseeName());
                try {
//                    String subject = "MOH HALP - Your "+ applicationTypeShow + ", "+ applicationNo +" is rejected ";
                    Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
                    subMap.put("ApplicationType", applicationTypeShow);
                    subMap.put("ApplicationNumber", applicationNo);
                    String emailSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_REJECT, subMap);
                    String smsSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_REJECT_SMS, subMap);
                    String messageSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_REJECT_MESSAGE, subMap);
                    log.debug(StringUtil.changeForLog("emailSubject : " + emailSubject));
                    log.debug(StringUtil.changeForLog("smsSubject : " + smsSubject));
                    log.debug(StringUtil.changeForLog("messageSubject : " + messageSubject));
                    EmailParam emailParam = new EmailParam();
                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_REJECT);
                    emailParam.setTemplateContent(map);
                    emailParam.setQueryCode(applicationNo);
                    emailParam.setReqRefNum(applicationNo);
                    emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
                    emailParam.setRefId(applicationNo);
                    emailParam.setSubject(emailSubject);
                    //send email
                    log.info(StringUtil.changeForLog("send renewal application email"));
                    notificationHelper.sendNotification(emailParam);
                    log.info(StringUtil.changeForLog("send renewal application email end"));
                    //send sms
                    EmailParam smsParam = new EmailParam();
                    smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_REJECT_SMS);
                    smsParam.setSubject(smsSubject);
                    smsParam.setQueryCode(applicationNo);
                    smsParam.setReqRefNum(applicationNo);
                    smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
                    smsParam.setRefId(applicationNo);
                    log.info(StringUtil.changeForLog("send renewal application sms"));
                    notificationHelper.sendNotification(smsParam);
                    log.info(StringUtil.changeForLog("send renewal application sms end"));
                    //send message
                    EmailParam messageParam = new EmailParam();
                    messageParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_REJECT_MESSAGE);
                    messageParam.setTemplateContent(map);
                    messageParam.setQueryCode(applicationNo);
                    messageParam.setReqRefNum(applicationNo);
                    messageParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                    messageParam.setRefId(applicationNo);
                    messageParam.setSubject(messageSubject);
                    messageParam.setSvcCodeList(svcCodeList);
                    log.info(StringUtil.changeForLog("send renewal application message"));
                    notificationHelper.sendNotification(messageParam);
                    log.info(StringUtil.changeForLog("send renewal application notification end"));
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    private String getEmailSubject(String templateId, Map<String, Object> subMap) {
        String subject = "-";
        if (!StringUtil.isEmpty(templateId)) {
            MsgTemplateDto emailTemplateDto = msgTemplateClient.getMsgTemplate(templateId).getEntity();
            if (emailTemplateDto != null) {
                try {
                    if (!IaisCommonUtils.isEmpty(subMap)) {
                        subject = MsgUtil.getTemplateMessageByContent(emailTemplateDto.getTemplateName(), subMap);
                    } else {
                        subject = emailTemplateDto.getTemplateName();
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return subject;
    }

    /**
     * StartStep: requestForInformation
     *
     * @param bpc
     * @throws
     */
    public void requestForInformation(BaseProcessClass bpc) throws FeignException, CloneNotSupportedException, IOException, TemplateException {
        log.debug(StringUtil.changeForLog("the do requestForInformation start ...."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String applicationNo = applicationDto.getApplicationNo();
        String externalRemarks = ParamUtil.getString(bpc.request, "comments");
        String applicationType = applicationDto.getApplicationType();
        String appId = applicationDto.getId();
        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(appId).getEntity();
        String[] preInspRfiCheckStr = ParamUtil.getStrings(bpc.request,"preInspRfiCheck");

        List<String> preInspRfiCheck = IaisCommonUtils.genNewArrayList();
        if(preInspRfiCheckStr != null && preInspRfiCheckStr.length > 0){
            preInspRfiCheck.addAll(Arrays.asList(preInspRfiCheckStr));
            TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, STR_TASK_DTO);
            InspectionPreTaskDto inspectionPreTaskDto = new InspectionPreTaskDto();
            String internalRemarks = ParamUtil.getString(bpc.request, "internalRemarks");
            inspectionPreTaskDto.setReMarks(internalRemarks);
            inspectionPreTaskDto.setPreInspRfiCheck(preInspRfiCheck);
            inspectionPreTaskDto.setPreInspecComments(externalRemarks);
            List<SelfAssessment> selfAssessments = BeSelfChecklistHelper.receiveSelfAssessmentDataByCorrId(taskDto.getRefNo());
            List<PremCheckItem> premCheckItems=null;
            if(!IaisCommonUtils.isEmpty(selfAssessments)){
                //one refNo(appPremCorrId) --> one SelfAssessment
                List<SelfAssessmentConfig> selfAssessmentConfigs = selfAssessments.get(0).getSelfAssessmentConfig();
                if(!IaisCommonUtils.isEmpty(selfAssessmentConfigs)) {
                    for(SelfAssessmentConfig selfAssessmentConfig : selfAssessmentConfigs){
                        if(selfAssessmentConfig == null){
                            continue;
                        }
                        if(selfAssessmentConfig.isCommon()){
                            premCheckItems = selfAssessmentConfig.getQuestion();
                        }
                    }
                }
            }

            inspectionPreTaskService.routingBack(taskDto, inspectionPreTaskDto, loginContext, premCheckItems, applicationViewDto);
        }else {

            try {
                if (ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)) {
                    AppPremiseMiscDto premiseMiscDto = (AppPremiseMiscDto) ParamUtil.getSessionAttr(bpc.request, "premiseMiscDto");
                    if (premiseMiscDto != null) {
                        String appealingFor = premiseMiscDto.getRelateRecId();
                        HashMap<String, String> maskParams = IaisCommonUtils.genNewHashMap();
                        maskParams.put("appealingFor", appealingFor);
                        String appealType = (String) ParamUtil.getSessionAttr(bpc.request, "type");
                        String url = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_CALL_BACK_URL_APPEAL + appealingFor + "&type=" + appealType;
                        applicationService.appealRfiAndEmail(applicationViewDto, applicationDto, maskParams, url, externalRemarks);
                    }
                } else if (ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationType)) {
                    HashMap<String, String> maskParams = IaisCommonUtils.genNewHashMap();
                    String appGrpPremId = "";
                    if (appPremisesCorrelationDto != null) {
                        appGrpPremId = appPremisesCorrelationDto.getAppGrpPremId();
                        maskParams.put("premiseId", appGrpPremId);
                    }
                    maskParams.put("appId", appId);
                    String url = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + InboxConst.URL_LICENCE_WEB_MODULE + "MohCessationApplication?appId=" + appId + "&premiseId=" + appGrpPremId;
                    applicationService.appealRfiAndEmail(applicationViewDto, applicationDto, maskParams, url, externalRemarks);
                } else if (ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(applicationType)) {
                    HashMap<String, String> maskParams = IaisCommonUtils.genNewHashMap();
                    maskParams.put("rfiWithdrawAppNo", applicationNo);
                    String url = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + InboxConst.URL_LICENCE_WEB_MODULE + "MohWithdrawalApplication?rfiWithdrawAppNo=" + applicationNo;
                    applicationService.appealRfiAndEmail(applicationViewDto, applicationDto, maskParams, url, externalRemarks);
                }
                applicationService.applicationRfiAndEmail(applicationViewDto, applicationDto, loginContext, externalRemarks);
            } catch (Exception e) {
                log.debug(StringUtil.changeForLog("send application RfiAndEmail error"));
                log.error(e.getMessage(), e);
            }
            routingTask(bpc, null, ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION, null);
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
        String doDocument = ParamUtil.getString(bpc.request, "uploadFile");
        String interalFileId = ParamUtil.getMaskedString(bpc.request, "interalFileId");
        if (!StringUtil.isEmpty(interalFileId)) {
            fillUpCheckListGetAppClient.deleteAppIntranetDocsById(interalFileId);
        }

        if ("Y".equals(doDocument)) {
            HcsaApplicationProcessUploadFileValidate uploadFileValidate = new HcsaApplicationProcessUploadFileValidate();
            Map<String, String> errorMap = uploadFileValidate.validate(bpc.request);
            if (!errorMap.isEmpty()) {
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, "uploadFileValidate", "Y");
            } else {
                MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
                CommonsMultipartFile selectedFile = (CommonsMultipartFile) mulReq.getFile("selectedFile");
                AppIntranetDocDto appIntranetDocDto = new AppIntranetDocDto();
                if (selectedFile != null) {
                    //size
                    long size = selectedFile.getSize();
                    appIntranetDocDto.setDocSize(String.valueOf(size / 1024));
                    String originName = selectedFile.getOriginalFilename();
                    if (!StringUtil.isEmpty(originName)) {
                        log.info(StringUtil.changeForLog("HcsaApplicationAjaxController uploadInternalFile OriginalFilename ==== " + selectedFile.getOriginalFilename()));
                        //type
                        String[] fileSplit = originName.split("\\.");
                        String fileType = fileSplit[fileSplit.length - 1];
                        appIntranetDocDto.setDocType(fileType);
                        //name
                        String fileName = fileSplit[0];
                        appIntranetDocDto.setDocName(fileName);

                        String fileRemark = ParamUtil.getString(bpc.request, "fileRemark");
                        if (StringUtil.isEmpty(fileRemark)) {
                            fileRemark = fileName;
                        }
                        //set document
                        appIntranetDocDto.setDocDesc(fileRemark);
                        //status
                        appIntranetDocDto.setDocStatus(AppConsts.COMMON_STATUS_ACTIVE);
                        //APP_PREM_CORRE_ID
                        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
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
                    }
                }
                String id = fillUpCheckListGetAppClient.saveAppIntranetDocByAppIntranetDoc(appIntranetDocDto).getEntity();
                appIntranetDocDto.setId(id);
                //ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
            }
        }
        log.debug(StringUtil.changeForLog("the do doDocument end ...."));
    }


    //***************************************
    //private methods
    //*************************************





    private AppPremisesRecommendationDto getClearRecommendationDto(String appPremCorreId, String dateStr, String dateTimeShow) throws Exception {
        AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
        appPremisesRecommendationDto.setAppPremCorreId(appPremCorreId);
        appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT);
        appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        //save date
        if (!StringUtil.isEmpty(dateStr)) {
            Date date = Formatter.parseDate(dateStr);
            appPremisesRecommendationDto.setRecomInDate(date);
        } else if (!StringUtil.isEmpty(dateTimeShow) && !"-".equals(dateTimeShow)) {
            Date date = Formatter.parseDate(dateTimeShow);
            appPremisesRecommendationDto.setRecomInDate(date);
        }
        return appPremisesRecommendationDto;
    }

    private void checkRecommendationShowName(BaseProcessClass bpc, ApplicationViewDto applicationViewDto) {
        String appType = applicationViewDto.getApplicationDto().getApplicationType();
        String recommendationShowName = "Recommendation";
        if (ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(applicationViewDto.getApplicationDto().getStatus())) {
            recommendationShowName = "Licence Tenure Period";
        } else if (ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(appType)) {
            recommendationShowName = "Recommended Licence Period";
        }
        ParamUtil.setSessionAttr(bpc.request, "recommendationShowName", recommendationShowName);
    }

    private void checkShowInspection(BaseProcessClass bpc, ApplicationViewDto applicationViewDto, TaskDto taskDto, String correlationId) {
        String status = applicationViewDto.getApplicationDto().getStatus();
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = appPremisesRoutingHistoryService.
                getActiveAppPremisesRoutingHistoryForCurrentStage(applicationViewDto.getApplicationDto().getApplicationNo(), HcsaConsts.ROUTING_STAGE_INS);
        if (appPremisesRoutingHistoryDto == null || ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(status) || ApplicationConsts.PROCESSING_DECISION_ROLLBACK_CR.equals(appPremisesRoutingHistoryDto.getProcessDecision())) {
            ParamUtil.setRequestAttr(bpc.request, "isShowInspection", "N");
        } else {
            if (ApplicationConsts.APPLICATION_STATUS_INSPECTOR_ROUTE_BACK.equals(status)) {
                ParamUtil.setRequestAttr(bpc.request, "isShowInspection", "N");
            } else {
                ParamUtil.setRequestAttr(bpc.request, "isShowInspection", "Y");
                LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
                InspectionReportDto insRepDto = insRepService.getInsRepDto(taskDto, applicationViewDto, loginContext);
                InspectionReportDto inspectorAo = insRepService.getInspectorAo(taskDto, applicationViewDto);
                insRepDto.setInspectors(inspectorAo.getInspectors());
                insRepDto.setAppPremSpecialSubSvcRelDtoList(applicationViewDto.getAppPremSpecialSubSvcRelDtoList().stream()
                        .filter(dto->!ApplicationConsts.RECORD_ACTION_CODE_REMOVE.equals(dto.getActCode()))
                        .collect(Collectors.toList()));
                insRepDto.setSpecialServiceCheckList(fillupChklistService.getSpecialServiceCheckList(applicationViewDto));
                if(fillupChklistService.checklistNeedVehicleSeparation(applicationViewDto)){
                    ParamUtil.setSessionAttr(bpc.request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DECIDE,AppConsts.YES);
                }
                ParamUtil.setRequestAttr(bpc.request, "insRepDto", insRepDto);
                String appType = applicationViewDto.getApplicationDto().getApplicationType();
                ParamUtil.setRequestAttr(bpc.request, "appType", appType);
                initAoRecommendation(correlationId, bpc,appType);
            }
        }
    }

    private void initAoRecommendation(String correlationId, BaseProcessClass bpc, String appType) {
        AppPremisesRecommendationDto initRecommendationDto = vehicleCommonController.initAoRecommendation(correlationId, bpc,appType);
        AppPremisesRecommendationDto riskRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPCTION_RISK_LEVEL).getEntity();
        if (riskRecommendationDto != null) {
            String riskLevel = riskRecommendationDto.getRecomDecision();
            initRecommendationDto.setRiskLevel(riskLevel);
        }
        ParamUtil.setSessionAttr(bpc.request, "appPremisesRecommendationDto", initRecommendationDto);
    }


    private void checkRecommendationDropdownValue(Integer recomInNumber, String chronoUnit, String recomDecision, ApplicationViewDto applicationViewDto, BaseProcessClass bpc) {
        if(!ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationViewDto.getApplicationDto().getApplicationType())){
            if (StringUtil.isEmpty(recomInNumber)) {
                ParamUtil.setRequestAttr(bpc.request, "recommendationStr", "");
                return;
            } else if (recomInNumber == 0) {
                ParamUtil.setRequestAttr(bpc.request, "recommendationStr", "reject");
                return;
            }
        }else{
            if(InspectionReportConstants.RFC_APPROVED.equals(recomDecision)){
                ParamUtil.setRequestAttr(bpc.request, "recommendationStr", "approve");
                return;
            }else if(InspectionReportConstants.RFC_REJECTED.equals(recomDecision)){
                ParamUtil.setRequestAttr(bpc.request, "recommendationStr", "reject");
                return;
            }else{
                ParamUtil.setRequestAttr(bpc.request, "recommendationStr", "");
                return;
            }
        }
        HcsaServiceDto hcsaServiceDto = applicationViewService.getHcsaServiceDtoById(applicationViewDto.getApplicationDto().getServiceId());
        String svcCode = hcsaServiceDto.getSvcCode();
        RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
        riskAcceptiionDto.setScvCode(svcCode);
        List<RiskAcceptiionDto> listRiskAcceptiionDto = IaisCommonUtils.genNewArrayList();
        listRiskAcceptiionDto.add(riskAcceptiionDto);
        List<RiskResultDto> listRiskResultDto = hcsaConfigClient.getRiskResult(listRiskAcceptiionDto).getEntity();
        boolean flag = true;
        if (listRiskResultDto != null && !listRiskResultDto.isEmpty()) {
            for (RiskResultDto riskResultDto : listRiskResultDto) {
                String dateType = riskResultDto.getDateType();
                String count = String.valueOf(riskResultDto.getTimeCount());
                String recommTime = count + " " + dateType;
                if (recommTime.equals(recomInNumber + " " + chronoUnit)) {
                    ParamUtil.setRequestAttr(bpc.request, "recommendationStr", recommTime);
                    if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationViewDto.getApplicationDto().getApplicationType())) {
                        ParamUtil.setRequestAttr(bpc.request, "recommendationStr", "approve");
                    }
                    flag = false;
                }
            }
            if (flag) {
                ParamUtil.setRequestAttr(bpc.request, "recommendationStr", "other");
                ParamUtil.setRequestAttr(bpc.request, "otherNumber", recomInNumber);
                ParamUtil.setRequestAttr(bpc.request, "otherChrono", chronoUnit);
            }
        }
    }

    private List<SelectOption> getRecommendationOtherDropdown() {
        List<SelectOption> recommendationOtherSelectOption = IaisCommonUtils.genNewArrayList();
        recommendationOtherSelectOption.add(new SelectOption(RiskConsts.YEAR, "Year(s)"));
        recommendationOtherSelectOption.add(new SelectOption(RiskConsts.MONTH, "Month(s)"));
        return recommendationOtherSelectOption;
    }

    private List<SelectOption> getRecommendationDropdown(ApplicationViewDto applicationViewDto, HttpServletRequest request) {
        List<SelectOption> recommendationSelectOption = IaisCommonUtils.genNewArrayList();
        HcsaServiceDto hcsaServiceDto = applicationViewService.getHcsaServiceDtoById(applicationViewDto.getApplicationDto().getServiceId());
        String svcCode = hcsaServiceDto.getSvcCode();
        RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
        riskAcceptiionDto.setScvCode(svcCode);
        riskAcceptiionDto.setRiskScore(applicationViewDto.getApplicationDto().getRiskScore());
        List<RiskAcceptiionDto> listRiskAcceptiionDto = IaisCommonUtils.genNewArrayList();
        listRiskAcceptiionDto.add(riskAcceptiionDto);
        List<RiskResultDto> listRiskResultDto = hcsaConfigClient.getRiskResult(listRiskAcceptiionDto).getEntity();
        String applicationType = applicationViewDto.getApplicationDto().getApplicationType();
        //request for change application type recommendation only has 'reject' and 'approve'
        boolean isRequestForChange = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType);
        if (listRiskResultDto != null && !listRiskResultDto.isEmpty() && !isRequestForChange) {
            for (RiskResultDto riskResultDto : listRiskResultDto) {
                String dateType = riskResultDto.getDateType();
//                String codeDesc = MasterCodeUtil.getCodeDesc(dateType);
                String count = String.valueOf(riskResultDto.getTimeCount());
                //String recommTime = count + " " + codeDesc;
                recommendationSelectOption.add(new SelectOption(count + " " + dateType, riskResultDto.getLictureText()));
                if ("24".equals(count) && RiskConsts.MONTH.equals(dateType)) {
                    //recommendationStr
                    ParamUtil.setRequestAttr(request, "recommendationStr", count + " " + dateType);
                }
            }
        }
        if (!isRequestForChange) {
            recommendationSelectOption.add(new SelectOption("other", "Others"));
        }
        if (isRequestForChange) {
            recommendationSelectOption.add(new SelectOption("approve", "Approve"));
            ParamUtil.setSessionAttr(request, "isRequestForChange", "Y");
        }
        if (!ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(applicationViewDto.getApplicationDto().getStatus())) {
            recommendationSelectOption.add(new SelectOption("reject", "Reject"));
        }
        return recommendationSelectOption;
    }

    public void routingTask(BaseProcessClass bpc, String stageId, String appStatus, String roleId) throws FeignException, CloneNotSupportedException, IOException, TemplateException {
        log.info(StringUtil.changeForLog("The routingTask start ..."));
        //get the user for this applicationNo
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        ApplicationGroupDto appGroupDtoView = applicationViewDto.getApplicationGroupDto();
        AppPremisesCorrelationDto newAppPremisesCorrelationDto = applicationViewDto.getNewAppPremisesCorrelationDto();
        String newCorrelationId = newAppPremisesCorrelationDto.getId();
        BroadcastOrganizationDto broadcastOrganizationDto = new BroadcastOrganizationDto();
        BroadcastApplicationDto broadcastApplicationDto = new BroadcastApplicationDto();
        String internalRemarks = ParamUtil.getString(bpc.request, "internalRemarks");
        String externalRemarks = ParamUtil.getString(bpc.request, "comments");
        String processDecision = ParamUtil.getString(bpc.request, "nextStage");
        String nextStageReplys = ParamUtil.getString(bpc.request, "nextStageReplys");
        String decisionValues = ParamUtil.getString(bpc.request, "decisionValues");
        String easMtsUseOnly = ParamUtil.getString(bpc.request, "easMtsUseOnly");
        if(StringUtil.isNotEmpty(easMtsUseOnly)){
            AppPremisesRecommendationDto appPremisesRecommendationDto= (AppPremisesRecommendationDto) ParamUtil.getSessionAttr(bpc.request, "userOnlyTypeRecommendationDto");

            appPremisesRecommendationDto.setRecomDecision(easMtsUseOnly);
            appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            insRepClient.saveRecommendationData(appPremisesRecommendationDto);

        }

        String licenseeId = applicationViewDto.getApplicationGroupDto().getLicenseeId();
        if (!StringUtil.isEmpty(nextStageReplys) && StringUtil.isEmpty(processDecision)) {
            processDecision = nextStageReplys;
        }
        if (ApplicationConsts.PROCESSING_DECISION_ASO_SEND_EMAIL.equals(decisionValues)) {
            processDecision = ApplicationConsts.PROCESSING_DECISION_ASO_SEND_EMAIL;
        }
        log.info(StringUtil.changeForLog("The processDecision is -- >:" + processDecision));
        //judge the final status is Approve or Reject.
        AppPremisesRecommendationDto appPremisesRecommendationDto = applicationViewDto.getAppPremisesRecommendationDto();
        String applicationType = applicationDto.getApplicationType();
        boolean isDMS = ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(applicationViewDto.getApplicationDto().getStatus());
        boolean isRequestForChange = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType);
        boolean isRfcDMS = isRequestForChange && isDMS;
        if (appPremisesRecommendationDto != null && !isRfcDMS && ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(appStatus)) {
            if (!(ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType) || ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(applicationType) || ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationType))) {
                Integer recomInNumber = appPremisesRecommendationDto.getRecomInNumber();
                if (null != recomInNumber && recomInNumber == 0) {
                    appStatus = ApplicationConsts.APPLICATION_STATUS_REJECTED;
                }
            } else {
                String recomDecision = appPremisesRecommendationDto.getRecomDecision();
                if ("reject".equals(recomDecision) || InspectionReportConstants.RFC_REJECTED.equals(recomDecision) || InspectionReportConstants.REJECTED.equals(recomDecision)) {
                    appStatus = ApplicationConsts.APPLICATION_STATUS_REJECTED;
                }
            }
        }

        //set risk score
        setRiskScore(applicationDto,newCorrelationId);
        //send reject email
        if (ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(appStatus)) {
            try {
                rejectSendNotification(bpc, applicationViewDto);
            } catch (Exception e) {
                log.debug(StringUtil.changeForLog("send reject notification error"), e);
            }
            String originLicenceId = applicationDto.getOriginLicenceId();
            if (!StringUtil.isEmpty(originLicenceId)) {
                LicAppCorrelationDto licAppCorrelationDto = new LicAppCorrelationDto();
                licAppCorrelationDto.setLicenceId(originLicenceId);
                licAppCorrelationDto.setApplicationId(applicationDto.getId());
                licAppCorrelationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                hcsaLicenceClient.saveLicenceAppCorrelation(licAppCorrelationDto);
            }
        }
        if (ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(appStatus) || ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02.equals(appStatus) || ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01.equals(appStatus)) {
            List<String> roleIds = new ArrayList<>();
            roleIds.add(RoleConsts.USER_ROLE_INSPECTIOR);
            List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtoList = appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryDtosByAppNoAndRoleIds(applicationDto.getApplicationNo(), roleIds);
            if (appPremisesRoutingHistoryDtoList == null || appPremisesRoutingHistoryDtoList.size() == 0) {
                applicationDto.setSelfAssMtFlag(4);
            }
        }
        //if do finish audit ,no need to do post
        licenceService.changePostInsForTodoAudit(applicationViewDto);
        //appeal save return fee
        try {
            if (ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(appStatus)) {
                if (ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)) {
                    String returnFee = ParamUtil.getString(bpc.request, "returnFee");
                    if(StringUtil.isEmpty(returnFee)) {
                        returnFee = appPremisesRecommendationDto.getRemarks();
                    }
                    log.info(StringUtil.changeForLog("appeal return fee remarks in recommendation db : " + returnFee));
                    if (!StringUtil.isEmpty(returnFee)) {
                        String oldApplicationNo = (String) ParamUtil.getSessionAttr(bpc.request, "oldApplicationNo");
                        log.info(StringUtil.changeForLog("appeal return fee old application no : " + oldApplicationNo));
                        if (!StringUtil.isEmpty(oldApplicationNo)) {
                            log.info(StringUtil.changeForLog("save appeal return fee"));
                            AppReturnFeeDto appReturnFeeDto = new AppReturnFeeDto();
                            appReturnFeeDto.setApplicationNo(oldApplicationNo);
                            appReturnFeeDto.setReturnAmount(Double.parseDouble(returnFee));
                            appReturnFeeDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                            appReturnFeeDto.setReturnType(ApplicationConsts.APPLICATION_RETURN_FEE_TYPE_APPEAL);
                            appReturnFeeDto.setStatus("paying");
                            appReturnFeeDto.setTriggerCount(0);
                            List<AppReturnFeeDto> saveReturnFeeDtos = IaisCommonUtils.genNewArrayList();
                            saveReturnFeeDtos.add(appReturnFeeDto);
                            try {
                                doRefunds(saveReturnFeeDtos);
                            } catch (Exception e) {
                                log.info(e.getMessage(), e);
                            }
                            broadcastApplicationDto.setReturnFeeDtos(saveReturnFeeDtos);
                            broadcastApplicationDto.setRollBackReturnFeeDtos(saveReturnFeeDtos);
                        }
                    }
                } else if (ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(applicationType)) {
                    AppPremiseMiscDto premiseMiscDto = cessationClient.getAppPremiseMiscDtoByAppId(applicationDto.getId()).getEntity();
                    AppReturnFeeDto appReturnFeeDto = new AppReturnFeeDto();
                    String oldAppId = premiseMiscDto.getRelateRecId();
                    String appGrpId = applicationDto.getAppGrpId();
                    List<ApplicationDto> applicationDtos = applicationClient.getAppDtosByAppGrpId(appGrpId).getEntity();
                    for (ApplicationDto applicationDto1 : applicationDtos) {
                        if (applicationDto1.getApplicationNo().equals(applicationDto.getApplicationNo())) {
                            applicationDto1.setStatus(ApplicationConsts.APPLICATION_STATUS_REJECTED);
                        }
                    }
                    ApplicationDto oldApplication = applicationClient.getApplicationById(oldAppId).getEntity();
                    if (oldApplication != null && ApplicationConsts.APPLICATION_STATUS_DELETED.equals(oldApplication.getStatus())) {
                        oldApplication = applicationClient.getAppByNo(oldApplication.getApplicationNo()).getEntity();
                    }
                    if (oldApplication != null) {
                        ApplicationGroupDto oldAppGrpDto = applicationGroupService.getApplicationGroupDtoById(oldApplication.getAppGrpId());
                        LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(licenseeId).getEntity();
                        if (licenseeDto != null) {
                            LicenseeEntityDto licenseeEntityDto = licenseeDto.getLicenseeEntityDto();
                            if (licenseeEntityDto != null) {
                                String entityType = licenseeEntityDto.getEntityType();
                                if (AcraConsts.ENTITY_TYPE_CHARITIES.equals(entityType)) {
                                    oldApplication.setIsCharity(Boolean.TRUE);
                                } else {
                                    oldApplication.setIsCharity(Boolean.FALSE);
                                }
                            }
                        }
                        String oldAppNo = oldApplication.getApplicationNo();
                        oldApplication.setReturnType(ApplicationConsts.APPLICATION_RETURN_FEE_TYPE_WITHDRAW);
                        List<ApplicationDto> applicationDtosReturn = IaisCommonUtils.genNewArrayList();
                        oldApplication.setStatus(ApplicationConsts.APPLICATION_STATUS_REJECTED);
                        applicationDtosReturn.add(oldApplication);
                        if (!StringUtil.isEmpty(oldAppNo)&&!(oldAppGrpDto.getPayMethod().equals(ApplicationConsts.PAYMENT_METHOD_NAME_GIRO)&&oldAppGrpDto.getPmtStatus().equals(ApplicationConsts.PAYMENT_STATUS_PENDING_GIRO))) {
                            List<ApplicationDto> applicationReturnFeeDtos = hcsaConfigClient.returnFee(applicationDtosReturn).getEntity();
                            if (applicationReturnFeeDtos != null) {
                                Double returnFee = applicationReturnFeeDtos.get(0).getReturnFee();
                                if (returnFee != null && returnFee > 0d) {
                                    appReturnFeeDto.setApplicationNo(oldAppNo);
                                    appReturnFeeDto.setReturnAmount(returnFee);
                                    log.info(StringUtil.changeForLog("==========================returnFee" + returnFee));
                                    appReturnFeeDto.setReturnType(ApplicationConsts.APPLICATION_RETURN_FEE_TYPE_WITHDRAW);
                                    appReturnFeeDto.setStatus("paying");
                                    appReturnFeeDto.setTriggerCount(0);
                                    appReturnFeeDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                                    List<AppReturnFeeDto> saveReturnFeeDtos = IaisCommonUtils.genNewArrayList();

                                    saveReturnFeeDtos.add(appReturnFeeDto);
                                    broadcastApplicationDto.setReturnFeeDtos(saveReturnFeeDtos);
                                    broadcastApplicationDto.setRollBackReturnFeeDtos(saveReturnFeeDtos);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.debug(StringUtil.changeForLog("save return fee error"), e);
        }
        try {
            doRefunds(broadcastApplicationDto.getReturnFeeDtos());
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        //completed this task and create the history
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
        broadcastOrganizationDto.setRollBackComplateTask((TaskDto) CopyUtil.copyMutableObject(taskDto));
        taskDto = completedTask(taskDto);
        broadcastOrganizationDto.setComplateTask(taskDto);
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),
                applicationDto.getStatus(), taskDto.getTaskKey(), null, taskDto.getWkGrpId(), internalRemarks, externalRemarks, processDecision, taskDto.getRoleId());
        broadcastApplicationDto.setComplateTaskHistory(appPremisesRoutingHistoryDto);

        //aso email for appeal history
        if(applicationType.equals(ApplicationConsts.APPLICATION_TYPE_APPEAL)&&ApplicationConsts.PROCESSING_DECISION_ASO_SEND_EMAIL.equals(processDecision)){
            AppPremiseMiscDto appPremiseMiscDto=applicationViewDto.getPremiseMiscDto();
            if(appPremiseMiscDto.getReason().equals(ApplicationConsts.APPEAL_REASON_APPLICATION_ADD_CGO)
                    ||appPremiseMiscDto.getReason().equals(ApplicationConsts.APPEAL_REASON_APPLICATION_REJECTION)
                    ||appPremiseMiscDto.getReason().equals(ApplicationConsts.APPEAL_REASON_APPLICATION_CHANGE_HCI_NAME)){

                ApplicationDto entity = applicationClient.getApplicationById(appPremiseMiscDto.getRelateRecId()).getEntity();
                appPremisesRoutingHistoryDto.setApplicationNo(entity.getApplicationNo());
                appPremisesRoutingHistoryService.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto);

            }
            if(appPremiseMiscDto.getReason().equals(ApplicationConsts.APPEAL_REASON_LICENCE_CHANGE_PERIOD)){
                List<LicAppCorrelationDto> licAppCorrelationDtos = hcsaLicenceClient.getLicCorrBylicId(appPremiseMiscDto.getRelateRecId()).getEntity();
                if(!IaisCommonUtils.isEmpty(licAppCorrelationDtos)) {
                    log.debug(StringUtil.changeForLog("licAppCorrelationDtos is Not null=======> Licence Id = " + appPremiseMiscDto.getRelateRecId()));
                    for (LicAppCorrelationDto licAppCorrelationDto : licAppCorrelationDtos) {
                        String appId = licAppCorrelationDto.getApplicationId();
                        ApplicationDto oldAppDto=applicationClient.getApplicationById(appId).getEntity();
                        appPremisesRoutingHistoryDto.setApplicationNo(oldAppDto.getApplicationNo());
                        appPremisesRoutingHistoryService.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto);
                    }
                }
            }
            if(!appPremisesRoutingHistoryDto.getApplicationNo().equals(applicationDto.getApplicationNo())){
                appPremisesRoutingHistoryDto.setApplicationNo(applicationDto.getApplicationNo());
                broadcastApplicationDto.setComplateTaskHistory(appPremisesRoutingHistoryDto);
            }
        }
        //update application status
        broadcastApplicationDto.setRollBackApplicationDto((ApplicationDto) CopyUtil.copyMutableObject(applicationDto));
        String oldStatus = applicationDto.getStatus();
        applicationDto.setStatus(appStatus);
        //cessation
        if (ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(appStatus) && ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationType)) {
            boolean grpLic = applicationDto.isGrpLic();
            String applicationDtoId = applicationDto.getId();
            AppGrpPremisesDto appGrpPremisesDto = cessationClient.getAppGrpPremisesDtoByAppId(applicationDtoId).getEntity();
            List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
            if (grpLic) {
                applicationDto.setGroupLicenceFlag(ApplicationConsts.GROUP_LICENCE_FLAG_CESSATION_NEED);
                applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_CESSATION_NEED_LICENCE);
                applicationDto.setNeedNewLicNo(true);
                applicationDtos.add(applicationDto);

                applicationClient.updateCessationApplications(applicationDtos);
            } else {
                applicationDto.setGroupLicenceFlag(ApplicationConsts.GROUP_LICENCE_FLAG_CESSATION_NOT);
                applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_CESSATION_NEED_LICENCE);
                applicationDto.setNeedNewLicNo(false);
                applicationDtos.add(applicationDto);

            }
        }

        if (ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(appStatus) && ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(applicationType)) {
            applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED);
        }

        applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        if(ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(applicationType) && ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(appStatus)){
            applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED);
        }

        broadcastApplicationDto.setApplicationDto(applicationDto);
        // send the task
        if (!StringUtil.isEmpty(stageId)) {
            //For the BROADCAST Rely
            if (ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(oldStatus)) {
                AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto1 = appPremisesRoutingHistoryService.getAppPremisesRoutingHistoryForCurrentStage(
                        applicationDto.getApplicationNo(), stageId
                );
                log.debug(StringUtil.changeForLog("The appId is-->;" + applicationDto.getId()));
                log.debug(StringUtil.changeForLog("The stageId is-->;" + stageId));
                if (appPremisesRoutingHistoryDto1 != null) {
                    TaskDto newTaskDto = TaskUtil.getTaskDto(applicationDto.getApplicationNo(), stageId, TaskConsts.TASK_TYPE_MAIN_FLOW,
                            taskDto.getRefNo(),TaskConsts.TASK_STATUS_PENDING, appPremisesRoutingHistoryDto1.getWrkGrpId(),
                            appPremisesRoutingHistoryDto1.getActionby(), new Date(), null,0, TaskConsts.TASK_PROCESS_URL_MAIN_FLOW, roleId,
                            IaisEGPHelper.getCurrentAuditTrailDto());
                    broadcastOrganizationDto.setCreateTask(newTaskDto);
                    //delete workgroup
                    BroadcastOrganizationDto broadcastOrganizationDto1 = broadcastService.getBroadcastOrganizationDto(
                            applicationDto.getApplicationNo(), AppConsts.DOMAIN_TEMPORARY);

                    WorkingGroupDto workingGroupDto = broadcastOrganizationDto1.getWorkingGroupDto();
                    broadcastOrganizationDto.setRollBackworkingGroupDto((WorkingGroupDto) CopyUtil.copyMutableObject(workingGroupDto));
                    workingGroupDto = changeStatusWrokGroup(workingGroupDto, AppConsts.COMMON_STATUS_DELETED);
                    if (workingGroupDto != null) {
                        workingGroupDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    }
                    broadcastOrganizationDto.setWorkingGroupDto(workingGroupDto);
                    List<UserGroupCorrelationDto> userGroupCorrelationDtos = broadcastOrganizationDto1.getUserGroupCorrelationDtoList();
                    List<UserGroupCorrelationDto> cloneUserGroupCorrelationDtos = IaisCommonUtils.genNewArrayList();
                    CopyUtil.copyMutableObjectList(userGroupCorrelationDtos, cloneUserGroupCorrelationDtos);
                    broadcastOrganizationDto.setRollBackUserGroupCorrelationDtoList(cloneUserGroupCorrelationDtos);
                    userGroupCorrelationDtos = changeStatusUserGroupCorrelationDtos(userGroupCorrelationDtos, AppConsts.COMMON_STATUS_DELETED);
                    broadcastOrganizationDto.setUserGroupCorrelationDtoList(userGroupCorrelationDtos);
                } else {
                    throw new IaisRuntimeException("This getAppPremisesCorrelationId can not get the broadcast -- >:" + applicationViewDto.getAppPremisesCorrelationId());
                }
            }

            else if (ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT.equals(appStatus)
                    || ApplicationConsts.APPLICATION_STATUS_PENDING_APPOINTMENT_SCHEDULING.equals(appStatus)) {
                AppInspectionStatusDto appInspectionStatusDto = new AppInspectionStatusDto();
                appInspectionStatusDto.setAppPremCorreId(taskDto.getRefNo());
                appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                appInspectionStatusDto.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_APPOINTMENT_INSPECTION_DATE);
                broadcastApplicationDto.setAppInspectionStatusDto(appInspectionStatusDto);
                TaskDto newTaskDto = taskService.getRoutingTask(applicationDto, stageId, roleId, newCorrelationId);
                broadcastOrganizationDto.setCreateTask(newTaskDto);
                AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew = getAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), stageId, null,
                        taskDto.getWkGrpId(), null, null, externalRemarks, taskDto.getRoleId());
                broadcastApplicationDto.setNewTaskHistory(appPremisesRoutingHistoryDtoNew);
                //set inspector leads
                if (ApplicationConsts.APPLICATION_STATUS_PENDING_APPOINTMENT_SCHEDULING.equals(appStatus) && !StringUtil.isEmpty(newTaskDto.getWkGrpId())) {
                    setInspLeadsInRecommendation(newTaskDto, newTaskDto.getWkGrpId(), IaisEGPHelper.getCurrentAuditTrailDto());
                }
            }else if(ApplicationConsts.PROCESSING_DECISION_ROUTE_LATERALLY.equals(processDecision)){
                String aoWorkGroupId = null;
                String aoUserId = null;
                String lrSelect = ParamUtil.getRequestString(bpc.request, "lrSelect");
                log.info(StringUtil.changeForLog("The lrSelect is -->:"+lrSelect));
                if(StringUtil.isNotEmpty(lrSelect)){
                    String[] lrSelects =  lrSelect.split("_");
                    aoWorkGroupId = lrSelects[0];
                    aoUserId = lrSelects[1];
                }
                TaskDto newTaskDto = taskService.getRoutingTask(applicationDto, stageId, roleId, newCorrelationId,aoWorkGroupId,aoUserId);
                broadcastOrganizationDto.setCreateTask(newTaskDto);
                AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew = getAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), stageId, null,
                        taskDto.getWkGrpId(), null, null, externalRemarks, taskDto.getRoleId());
                broadcastApplicationDto.setNewTaskHistory(appPremisesRoutingHistoryDtoNew);
            }
            else {
                String verified = ParamUtil.getRequestString(bpc.request, "verified");
                String aoWorkGroupId = null;
                String aoUserId = null;
                if(RoleConsts.USER_ROLE_AO1.equals(verified) || RoleConsts.USER_ROLE_AO2.equals(verified) || RoleConsts.USER_ROLE_AO3.equals(verified)){
                    String aoSelect = ParamUtil.getRequestString(bpc.request, "aoSelect");
                    log.info(StringUtil.changeForLog("The aoSelect is -->:"+aoSelect));
                    if(StringUtil.isNotEmpty(aoSelect)){
                        String[] aoSelects =  aoSelect.split("_");
                        aoWorkGroupId = aoSelects[0];
                        aoUserId = aoSelects[1];
                    }
                }
                TaskDto newTaskDto = taskService.getRoutingTask(applicationDto, stageId, roleId, newCorrelationId,aoWorkGroupId,aoUserId);
                broadcastOrganizationDto.setCreateTask(newTaskDto);
                AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew = getAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), stageId, null,
                        taskDto.getWkGrpId(), null, null, externalRemarks, taskDto.getRoleId());
                broadcastApplicationDto.setNewTaskHistory(appPremisesRoutingHistoryDtoNew);
            }
//            //add history for next stage start
//            if (!((ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(appStatus) || ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02.equals(appStatus))
//                    && !applicationDto.isFastTracking())
//                    && !ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT.equals(appStatus)) {
//                AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew = getAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), stageId, null,
//                        taskDto.getWkGrpId(), null, null, externalRemarks, taskDto.getRoleId());
//                broadcastApplicationDto.setNewTaskHistory(appPremisesRoutingHistoryDtoNew);
//            }
        } else {
            //0065354
            if (!ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION.equals(appStatus)) {
                List<ApplicationDto> applicationDtoList = applicationService.getApplicaitonsByAppGroupId(applicationDto.getAppGrpId());
                List<ApplicationDto> saveApplicationDtoList = IaisCommonUtils.genNewArrayList();
                CopyUtil.copyMutableObjectList(applicationDtoList, saveApplicationDtoList);
                applicationDtoList = removeFastTrackingAndTransfer(applicationDtoList);
                String ao1Ao2Approve = (String) ParamUtil.getSessionAttr(bpc.request, "Ao1Ao2Approve");
                if(ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(appStatus)){
                    List<ApplicationDto> applicationDtos=IaisCommonUtils.genNewArrayList();
                    applicationDtos.add(applicationDto);
                    //get and set return fee
                    applicationDtos = hcsaConfigClient.returnFee(applicationDtos).getEntity();
                    //save return fee
                    saveRejectReturnFee(applicationDtos, broadcastApplicationDto);
                }
                boolean isAo1Ao2Approve = "Y".equals(ao1Ao2Approve);
                boolean isAllSubmit = applicationService.isOtherApplicaitonSubmit(applicationDtoList, applicationDto.getApplicationNo(),
                        ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02);
                if (isAllSubmit || applicationDto.isFastTracking() || isAo1Ao2Approve) {
                    if (isAo1Ao2Approve && !applicationDto.isFastTracking()) {
                        doAo1Ao2Approve(broadcastOrganizationDto, broadcastApplicationDto, applicationDto, taskDto, newCorrelationId);
                    }
                    boolean needUpdateGroupStatus = applicationService.isOtherApplicaitonSubmit(applicationDtoList, applicationDto.getApplicationNo(),
                            ApplicationConsts.APPLICATION_STATUS_APPROVED, ApplicationConsts.APPLICATION_STATUS_REJECTED);
                    if (needUpdateGroupStatus || applicationDto.isFastTracking()) {
                        //update application Group status
                        ApplicationGroupDto applicationGroupDto = applicationGroupService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
                        broadcastApplicationDto.setRollBackApplicationGroupDto((ApplicationGroupDto) CopyUtil.copyMutableObject(applicationGroupDto));
                        //update current application status in db search result
                        updateCurrentApplicationStatus(saveApplicationDtoList, applicationDto.getId(), appStatus, licenseeId);
                        //set app group status
                        boolean appStatusIsAllRejected = checkAllStatus(saveApplicationDtoList, ApplicationConsts.APPLICATION_STATUS_REJECTED);
                        if (appStatusIsAllRejected) {
                            applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_REJECT);
                        } else {
                            applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_APPROVED);
                            List<ApplicationGroupDto> applicationGroupDtoList=IaisCommonUtils.genNewArrayList();
                            applicationGroupDtoList.add(applicationGroupDto);
                            giroDeductionBeService.syncFeApplicationGroupStatus(applicationGroupDtoList);
                        }
                        applicationGroupDto.setAo3ApprovedDt(new Date());
                        applicationGroupDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                        broadcastApplicationDto.setApplicationGroupDto(applicationGroupDto);

                        if (needUpdateGroupStatus) {
                            //clearApprovedHclCodeByExistRejectApp
                            applicationViewService.clearApprovedHclCodeByExistRejectApp(saveApplicationDtoList, applicationGroupDto.getAppType(), broadcastApplicationDto.getApplicationDto());
                        }
                    }
                }
            } else {
                log.info(StringUtil.changeForLog("This RFI  this application -->:" + applicationDto.getApplicationNo()));
            }
            //cessation
            if(ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationType)){
                String appGrpId = applicationDto.getAppGrpId();
                List<ApplicationDto> applicationDtoList = applicationService.getApplicaitonsByAppGroupId(appGrpId);
                List<ApplicationGroupDto> applicationGroupDtos = IaisCommonUtils.genNewArrayList();
                Set<String> statusList = IaisCommonUtils.genNewHashSet();
                for(ApplicationDto  applicationDto1 : applicationDtoList){
                    String status = applicationDto1.getStatus();
                    statusList.add(status);
                }
                if(statusList.size()==1&&statusList.contains(ApplicationConsts.APPLICATION_STATUS_CESSATION_NEED_LICENCE)){
                    ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(appGrpId).getEntity();
                    applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_LICENCE_GENERATED);
                    applicationGroupDtos.add(applicationGroupDto);
                }
                //spec
                List<ApplicationDto> specApp = cessationClient.getAppByBaseAppNo(applicationDto.getApplicationNo()).getEntity();
                if(!IaisCommonUtils.isEmpty(specApp)){
                    String appGrpIdSpec = specApp.get(0).getAppGrpId();
                    List<ApplicationDto> applicationDtoListSpec = applicationService.getApplicaitonsByAppGroupId(appGrpIdSpec);
                    Set<String> statusListSpec = IaisCommonUtils.genNewHashSet();
                    for(ApplicationDto  applicationDto1 : applicationDtoListSpec){
                        String status = applicationDto1.getStatus();
                        statusListSpec.add(status);
                    }
                    if(statusListSpec.size()==1&&statusListSpec.contains(ApplicationConsts.APPLICATION_STATUS_CESSATION_NEED_LICENCE)){
                        ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(appGrpIdSpec).getEntity();
                        applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_LICENCE_GENERATED);
                        applicationGroupDtos.add(applicationGroupDto);
                    }
                }
                if(!IaisCommonUtils.isEmpty(applicationGroupDtos)){
                    applicationClient.updateApplications(applicationGroupDtos);

                }
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
        broadcastOrganizationDto = broadcastService.svaeBroadcastOrganization(broadcastOrganizationDto, bpc.process, submissionId);
        if(!StringUtil.isEmpty(stageId)){
            if(HcsaConsts.ROUTING_STAGE_AO1.equals(stageId) ||
                    HcsaConsts.ROUTING_STAGE_AO2.equals(stageId) ||
                    HcsaConsts.ROUTING_STAGE_AO3.equals(stageId)){
                //close submit pref insp date
                broadcastApplicationDto.getApplicationDto().setHasSubmitPrefDate(1);
            }
        }
        //if Giro payment fail
        if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationType) ||
                ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationType) ||
                ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType)
        ) {
            if (ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(appStatus)) {
                if (ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_FAIL.equals(appGroupDtoView.getPmtStatus()) ||
                        ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_FAIL_REMIND_OK.equals(appGroupDtoView.getPmtStatus()) ||
                        ApplicationConsts.PAYMENT_STATUS_PENDING_GIRO.equals(appGroupDtoView.getPmtStatus())) {
                    broadcastApplicationDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_GIRO_PAYMENT_FAIL);
                } else if (ApplicationConsts.PAYMENT_STATUS_GIRO_RETRIGGER.equals(appGroupDtoView.getPmtStatus())) {
                    broadcastApplicationDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_PAYMENT_RESUBMIT);
                }
            }
        } else if (ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(applicationType)) {
            if (ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(appStatus)) {
                broadcastApplicationDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED);
            }
        }
        //set appSvcVehicleDto
        broadcastApplicationDto = broadcastService.setAppSvcVehicleDtoByAppView(broadcastApplicationDto, applicationViewDto, appStatus, applicationType);
        broadcastApplicationDto = broadcastService.setAppPremSubSvcDtoByAppView(broadcastApplicationDto, applicationViewDto, appStatus, applicationType);
        broadcastApplicationDto = broadcastService.svaeBroadcastApplicationDto(broadcastApplicationDto, bpc.process, submissionId);
        //when reject, app type is renew rfc, save licence app correlation
        broadcastService.saveEventBeLicenseDto(appStatus, applicationDto, submissionId, evenRefNum, bpc.process);
        //0062460 update FE  application status.
        applicationService.updateFEApplicaiton(broadcastApplicationDto.getApplicationDto());
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);

        //appeal save return fee


        ApplicationDto withdrawApplicationDto = broadcastApplicationDto.getApplicationDto();
        if (withdrawApplicationDto != null) {
            /**
             * Send Withdrawal Application Email
             14      */
            if (ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(withdrawApplicationDto.getApplicationType())) {
                String applicantName = "";
                String serviceId = applicationViewDto.getApplicationDto().getServiceId();
                AppPremiseMiscDto premiseMiscDto = cessationClient.getAppPremiseMiscDtoByAppId(applicationDto.getId()).getEntity();
                if (premiseMiscDto != null) {
                    String oldAppId = premiseMiscDto.getRelateRecId();
                    if (!StringUtil.isEmpty(oldAppId)) {
                        ApplicationDto oldApplication = applicationClient.getApplicationById(oldAppId).getEntity();
                        String applicationNo = oldApplication.getApplicationNo();
                        String applicationType1 = oldApplication.getApplicationType();
                        ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(oldApplication.getAppGrpId()).getEntity();
                        OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
                        if (orgUserDto != null) {
                            applicantName = orgUserDto.getDisplayName();
                        }
                        if (ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(withdrawApplicationDto.getStatus())) {
                            try {
                                if(!oldApplication.getStatus().equals(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION)){
                                    //WITHDRAWAL To restore the old task
                                    AppPremisesCorrelationDto appPremisesCorrelationDto=applicationClient.getAppPremCorrByAppNo(applicationNo).getEntity();
                                    String corrId=appPremisesCorrelationDto.getId();
                                    List<TaskDto> taskDtos = organizationClient.getTasksByRefNo(corrId).getEntity();
                                    TaskDto oldTaskDto=taskDtos.get(0);
                                    oldTaskDto.setTaskStatus(TaskConsts.TASK_STATUS_READ);
                                    oldTaskDto.setId(null);
                                    taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                                    List<TaskDto> newTaskDto=IaisCommonUtils.genNewArrayList();
                                    newTaskDto.add(oldTaskDto);
                                    taskService.createTasks(newTaskDto);
                                }
                            }catch (Exception e){
                                log.error(e.getMessage(),e);
                            }
                            Map<String, Object> msgInfoMap = IaisCommonUtils.genNewHashMap();
                            msgInfoMap.put("ApplicationNumber", applicationNo);
                            msgInfoMap.put("ApplicationType", MasterCodeUtil.getCodeDesc(applicationType1));
                            msgInfoMap.put("Applicant", applicantName);
                            msgInfoMap.put("ApplicationDate", Formatter.formatDateTime(applicationGroupDto.getSubmitDt()));
                            msgInfoMap.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);
                            sendEmail(MsgTemplateConstants.MSG_TEMPLATE_WITHDRAWAL_APP_REJECT_EMAIL, msgInfoMap, oldApplication);
                            sendInboxMessage(oldApplication, serviceId, msgInfoMap, MsgTemplateConstants.MSG_TEMPLATE_WITHDRAWAL_APP_REJECT_MESSAGE);
                            sendSMS(oldApplication, MsgTemplateConstants.MSG_TEMPLATE_WITHDRAWAL_APP_REJECT_SMS, msgInfoMap);
                        }
                    }
                }
            }
        }

        log.info(StringUtil.changeForLog("The routingTask end ..."));
    }

    private void setRiskScore(ApplicationDto applicationDto,String newCorrelationId){
        log.debug(StringUtil.changeForLog("correlationId : " + newCorrelationId));
        try {
            if(applicationDto != null && !StringUtil.isEmpty(newCorrelationId)){
                AppPremisesRecommendationDto appPreRecommentdationDtoInspectionDate =insepctionNcCheckListService.getAppRecomDtoByAppCorrId(newCorrelationId,InspectionConstants.RECOM_TYPE_INSEPCTION_DATE);
                if(appPreRecommentdationDtoInspectionDate != null){
                    HcsaRiskScoreDto hcsaRiskScoreDto = new HcsaRiskScoreDto();
                    hcsaRiskScoreDto.setAppType(applicationDto.getApplicationType());
                    hcsaRiskScoreDto.setLicId(applicationDto.getOriginLicenceId());
                    List<ApplicationDto> applicationDtos = new ArrayList<>(1);
                    applicationDtos.add(applicationDto);
                    hcsaRiskScoreDto.setApplicationDtos(applicationDtos);
                    hcsaRiskScoreDto.setServiceId(applicationDto.getServiceId());
                    HcsaRiskScoreDto entity = hcsaConfigClient.getHcsaRiskScoreDtoByHcsaRiskScoreDto(hcsaRiskScoreDto).getEntity();
                    String riskLevel = entity.getRiskLevel();
                    log.debug(StringUtil.changeForLog("riskLevel : " + riskLevel));
                    applicationDto.setRiskLevel(riskLevel);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    private void doRefunds(List<AppReturnFeeDto> saveReturnFeeDtos) {
        if(saveReturnFeeDtos!=null&&!saveReturnFeeDtos.isEmpty()){
            List<AppReturnFeeDto> saveReturnFeeDtosStripe=IaisCommonUtils.genNewArrayList();
            for (AppReturnFeeDto appreturn:saveReturnFeeDtos
            ) {
                ApplicationDto applicationDto=applicationClient.getAppByNo(appreturn.getApplicationNo()).getEntity();
                ApplicationGroupDto applicationGroupDto=applicationClient.getAppById(applicationDto.getAppGrpId()).getEntity();
                if(applicationGroupDto.getPayMethod()!=null&&applicationGroupDto.getPayMethod().equals(ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT)){
                    appreturn.setSysClientId(AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY);
                    saveReturnFeeDtosStripe.add(appreturn);
                }
            }
            List<PaymentRequestDto> paymentRequestDtos= applicationService.eicFeStripeRefund(saveReturnFeeDtosStripe);
            for (PaymentRequestDto refund : paymentRequestDtos
            ) {
                for (AppReturnFeeDto appreturn : saveReturnFeeDtos
                ) {
                    if (appreturn.getApplicationNo().equals(refund.getReqRefNo())) {
                        appreturn.setStatus(refund.getStatus());
                    }
                }
            }
        }

    }

    private boolean checkAllStatus(List<ApplicationDto> applicationDtoList, String status) {
        boolean flag = false;
        if (!IaisCommonUtils.isEmpty(applicationDtoList) && !StringUtil.isEmpty(status)) {
            int index = 0;
            for (ApplicationDto applicationDto : applicationDtoList) {
                if (status.equals(applicationDto.getStatus())) {
                    index++;
                }
            }
            if (index == applicationDtoList.size()) {
                flag = true;
            }
        }

        return flag;
    }

    private List<ApplicationDto> removeCurrentApplicationDto(List<ApplicationDto> applicationDtoList, String currentId) {
        List<ApplicationDto> result = null;
        if (!IaisCommonUtils.isEmpty(applicationDtoList) && !StringUtil.isEmpty(currentId)) {
            result = IaisCommonUtils.genNewArrayList();
            for (ApplicationDto applicationDto : applicationDtoList) {
                if (currentId.equals(applicationDto.getId())) {
                    continue;
                }
                result.add(applicationDto);
            }
        }
        return result;
    }

    private void doAo1Ao2Approve(BroadcastOrganizationDto broadcastOrganizationDto, BroadcastApplicationDto broadcastApplicationDto, ApplicationDto applicationDto, TaskDto taskDto, String newCorrelationId) throws FeignException {
        String appGrpId = applicationDto.getAppGrpId();
        String status = applicationDto.getStatus();
        String appId = applicationDto.getId();
        List<ApplicationDto> applicationDtoList = applicationService.getApplicaitonsByAppGroupId(appGrpId);
        applicationDtoList = removeFastTrackingAndTransfer(applicationDtoList);
        applicationDtoList = removeCurrentApplicationDto(applicationDtoList, appId);
        if (IaisCommonUtils.isEmpty(applicationDtoList)) {
            return;
        } else {
            boolean flag = taskService.checkCompleteTaskByApplicationNo(applicationDtoList, newCorrelationId);
            if (flag) {
                String stageId = HcsaConsts.ROUTING_STAGE_AO3;
                String roleId = RoleConsts.USER_ROLE_AO3;
                updateCurrentApplicationStatus(applicationDtoList, appId, status);
                List<ApplicationDto> ao2AppList = getStatusAppList(applicationDtoList, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02);
                if (IaisCommonUtils.isEmpty(ao2AppList)) {
                    log.info(StringUtil.changeForLog("ao2AppList is null"));
                } else {
                    log.info(StringUtil.changeForLog("ao2AppList size() not null "));
                }
                List<ApplicationDto> ao3AppList = getStatusAppList(applicationDtoList, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03);
                if (IaisCommonUtils.isEmpty(ao3AppList)) {
                    log.info(StringUtil.changeForLog("ao3AppList is null"));
                } else {
                    log.info(StringUtil.changeForLog("ao3AppList size() not null "));
                }
                List<ApplicationDto> creatTaskApplicationList = ao2AppList;
                if (IaisCommonUtils.isEmpty(ao2AppList) && !IaisCommonUtils.isEmpty(ao3AppList)) {
                    creatTaskApplicationList = ao3AppList;
                } else {
                    stageId = HcsaConsts.ROUTING_STAGE_AO2;
                    roleId = RoleConsts.USER_ROLE_AO2;
                }
                if (IaisCommonUtils.isEmpty(creatTaskApplicationList)) {
                    return;
                }
                log.info(StringUtil.changeForLog("stageId : " + stageId));
                log.info(StringUtil.changeForLog("roleId : " + roleId));
                // send the task to Ao2  or Ao3
                TaskHistoryDto taskHistoryDto = taskService.getRoutingTaskOneUserForSubmisison(creatTaskApplicationList,
                        stageId, roleId, IaisEGPHelper.getCurrentAuditTrailDto(), taskDto.getRoleId(), taskDto.getWkGrpId());
                List<TaskDto> taskDtos = taskHistoryDto.getTaskDtoList();
                List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = taskHistoryDto.getAppPremisesRoutingHistoryDtos();
                broadcastOrganizationDto.setOneSubmitTaskList(taskDtos);
                broadcastApplicationDto.setOneSubmitTaskHistoryList(appPremisesRoutingHistoryDtos);
            }
        }
    }

    private List<ApplicationDto> getStatusAppList(List<ApplicationDto> applicationDtos, String status) {
        if (IaisCommonUtils.isEmpty(applicationDtos) || StringUtil.isEmpty(status)) {
            return null;
        }
        List<ApplicationDto> applicationDtoList = null;
        for (ApplicationDto applicationDto : applicationDtos) {
            if (status.equals(applicationDto.getStatus())) {
                if (applicationDtoList == null) {
                    applicationDtoList = IaisCommonUtils.genNewArrayList();
                    applicationDtoList.add(applicationDto);
                } else {
                    applicationDtoList.add(applicationDto);
                }
            }
        }

        return applicationDtoList;
    }

    private void setInspLeadsInRecommendation(TaskDto taskDto, String workGroupId, AuditTrailDto auditTrailDto) {
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(taskDto.getRefNo(), InspectionConstants.RECOM_TYPE_INSPECTION_LEAD).getEntity();
        if (appPremisesRecommendationDto == null) {
            WorkingGroupDto workingGroupDto = organizationClient.getWrkGrpById(workGroupId).getEntity();
            String workGroupName = workingGroupDto.getGroupName();
            if (!StringUtil.isEmpty(workGroupName) && RoleConsts.USER_ROLE_INSPECTIOR.equals(taskDto.getRoleId())) {
                List<String> leadIds = organizationClient.getInspectionLead(workGroupId).getEntity();
                String nameStr = "";
                for (String id : leadIds) {
                    OrgUserDto oDto = organizationClient.retrieveOrgUserAccountById(id).getEntity();
                    if (id.equals(oDto.getId())) {
                        if (StringUtil.isEmpty(nameStr)) {
                            nameStr = oDto.getDisplayName();
                        } else {
                            nameStr = nameStr + "," + oDto.getDisplayName();
                        }
                    }
                }
                appPremisesRecommendationDto = new AppPremisesRecommendationDto();
                appPremisesRecommendationDto.setAppPremCorreId(taskDto.getRefNo());
                appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                appPremisesRecommendationDto.setVersion(1);
                appPremisesRecommendationDto.setRecomInDate(null);
                appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSPECTION_LEAD);
                appPremisesRecommendationDto.setRecomDecision(nameStr);
                appPremisesRecommendationDto.setAuditTrailDto(auditTrailDto);
                fillUpCheckListGetAppClient.saveAppRecom(appPremisesRecommendationDto);
            }
        }
    }

    private void updateFeApplications(List<ApplicationDto> applications) {
        try {
            applicationService.updateFEApplicaitons(applications);
        } catch (Exception e) {
            log.debug(StringUtil.changeForLog("update fe applications error"));
        }
    }

    public void sendInboxMessage(ApplicationDto applicationDto, String serviceId, Map<String, Object> map, String messageTemplateId) throws IOException, TemplateException {
        EmailParam messageParam = new EmailParam();
        HcsaServiceDto serviceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
        List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
        if (serviceDto != null) {
            svcCodeList.add(serviceDto.getSvcCode());
        }
        MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(messageTemplateId).getEntity();
        Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
        subMap.put("ApplicationNumber", applicationDto.getApplicationNo());
        subMap.put("ApplicationType", MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
        String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), subMap);
        messageParam.setTemplateId(messageTemplateId);
        messageParam.setQueryCode(applicationDto.getApplicationNo());
        messageParam.setTemplateContent(map);
        messageParam.setReqRefNum(applicationDto.getApplicationNo());
        messageParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        messageParam.setRefId(applicationDto.getApplicationNo());
        messageParam.setSubject(subject);
        messageParam.setSvcCodeList(svcCodeList);
        log.info(StringUtil.changeForLog("send withdraw Application approve message"));
        notificationHelper.sendNotification(messageParam);
    }

    private void saveRejectReturnFee(List<ApplicationDto> applicationDtos, BroadcastApplicationDto broadcastApplicationDto) {
        List<AppReturnFeeDto> saveReturnFeeDtos = IaisCommonUtils.genNewArrayList();
        //save return fee
        for (ApplicationDto applicationDto : applicationDtos) {
            if ( !ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(applicationDto.getApplicationType()) && !ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationDto.getApplicationType())) {
                AppReturnFeeDto appReturnFeeDto = new AppReturnFeeDto();
                Double returnFee = applicationDto.getReturnFee();
                appReturnFeeDto.setStatus("paying");
                if (returnFee == null || MiscUtil.doubleEquals(returnFee, 0.0d)) {
                    appReturnFeeDto.setReturnAmount(0.0d);
                    appReturnFeeDto.setStatus("success");
                }else {
                    appReturnFeeDto.setReturnAmount(returnFee);
                }
                appReturnFeeDto.setTriggerCount(0);
                appReturnFeeDto.setApplicationNo(applicationDto.getApplicationNo());
                appReturnFeeDto.setReturnType(ApplicationConsts.APPLICATION_RETURN_FEE_REJECT);
                saveReturnFeeDtos.add(appReturnFeeDto);
//                applicationService.saveAppReturnFee(appReturnFeeDto);
            }
        }
        try {
            doRefunds(saveReturnFeeDtos);
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        if (!IaisCommonUtils.isEmpty(saveReturnFeeDtos)) {
            broadcastApplicationDto.setReturnFeeDtos(saveReturnFeeDtos);
            broadcastApplicationDto.setRollBackReturnFeeDtos(saveReturnFeeDtos);
        }
    }

    public void sendEmail(String msgId, Map<String, Object> msgInfoMap, ApplicationDto applicationDto) throws IOException, TemplateException {
        log.info(StringUtil.changeForLog("***************** send withdraw application Email  *****************"));
        MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(msgId).getEntity();
        EmailParam emailParam = new EmailParam();
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        map.put("ApplicationType", MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
        map.put("ApplicationNumber", applicationDto.getApplicationNo());
        String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), map);
        emailParam.setTemplateContent(msgInfoMap);
        emailParam.setTemplateId(msgId);
        emailParam.setReqRefNum(applicationDto.getApplicationNo());
        emailParam.setQueryCode(applicationDto.getApplicationNo());
        emailParam.setRefId(applicationDto.getApplicationNo());
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
        emailParam.setSubject(subject);
        log.info(StringUtil.changeForLog("***************** send withdraw application Email  end*****************"));
        notificationHelper.sendNotification(emailParam);
    }

    public void sendSMS(ApplicationDto applicationDto, String msgId, Map<String, Object> msgInfoMap) throws IOException, TemplateException {
        log.info(StringUtil.changeForLog("***************** send withdraw application sms  *****************"));
        EmailParam emailParam = new EmailParam();
        MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(msgId).getEntity();
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        map.put("ApplicationType", MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
        map.put("ApplicationNumber", applicationDto.getApplicationNo());
        String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), map);
        emailParam.setTemplateId(msgId);
        emailParam.setTemplateContent(msgInfoMap);
        emailParam.setQueryCode(applicationDto.getApplicationNo());
        emailParam.setReqRefNum(applicationDto.getApplicationNo());
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
        emailParam.setRefId(applicationDto.getApplicationNo());
        emailParam.setSubject(subject);
        notificationHelper.sendNotification(emailParam);
    }

    private List<ApplicationDto> removeFastTrackingAndTransfer(List<ApplicationDto> applicationDtos) {
        List<ApplicationDto> result = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(applicationDtos)) {
            for (ApplicationDto applicationDto : applicationDtos) {
                if (ApplicationConsts.APPLICATION_STATUS_TRANSFER_ORIGIN.equals(applicationDto.getStatus())) {
                    continue;
                }
                if (!applicationDto.isFastTracking()) {
                    result.add(applicationDto);
                }
            }
        }
        return result;
    }

    private void updateCurrentApplicationStatus(List<ApplicationDto> applicationDtos, String applicationId, String status, String licenseeId) {
        if (!IaisCommonUtils.isEmpty(applicationDtos) && !StringUtil.isEmpty(applicationId)) {
            String entityType = "";
            LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(licenseeId).getEntity();
            if (licenseeDto != null) {
                LicenseeEntityDto licenseeEntityDto = licenseeDto.getLicenseeEntityDto();
                if (licenseeEntityDto != null) {
                    entityType = licenseeEntityDto.getEntityType();
                }
            }
            boolean isCharity = AcraConsts.ENTITY_TYPE_CHARITIES.equals(entityType);
            for (ApplicationDto applicationDto : applicationDtos) {
                log.debug(StringUtil.changeForLog("set reject return fee , app no : " + applicationDto.getApplicationNo()));
                applicationDto.setIsCharity(isCharity);
                applicationDto.setReturnType(ApplicationConsts.APPLICATION_RETURN_FEE_REJECT);
                if (applicationId.equals(applicationDto.getId())) {
                    applicationDto.setStatus(status);
                }
                log.debug(StringUtil.changeForLog("set reject return fee , charity : " + applicationDto.getIsCharity()));
            }
        }
    }

    private void updateCurrentApplicationStatus(List<ApplicationDto> applicationDtos, String applicationId, String status) {
        if (!IaisCommonUtils.isEmpty(applicationDtos) && !StringUtil.isEmpty(applicationId)) {
            for (ApplicationDto applicationDto : applicationDtos) {
                if (applicationId.equals(applicationDto.getId())) {
                    applicationDto.setStatus(status);
                }
            }
        }
    }

    private ApplicationDto getCurrentApplicationDto(List<ApplicationDto> applicationDtos, String applicationId) {
        ApplicationDto applicationDto = null;
        if (IaisCommonUtils.isEmpty(applicationDtos) || StringUtil.isEmpty(applicationId)) {
            return null;
        }
        for (ApplicationDto app : applicationDtos) {
            if (applicationId.equals(app.getId())) {
                applicationDto = app;
                break;
            }
        }
        return applicationDto;
    }

    private void rollBack(BaseProcessClass bpc, String stageId, String appStatus, String roleId, String wrkGpId, String userId) throws CloneNotSupportedException {
        //get the user for this applicationNo
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String internalRemarks = ParamUtil.getString(bpc.request, "internalRemarks");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        BroadcastOrganizationDto broadcastOrganizationDto = new BroadcastOrganizationDto();
        BroadcastApplicationDto broadcastApplicationDto = new BroadcastApplicationDto();
        //complated this task and create the history
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
        String subStageId = null;
        broadcastOrganizationDto.setRollBackComplateTask((TaskDto) CopyUtil.copyMutableObject(taskDto));
        taskDto = completedTask(taskDto);
        broadcastOrganizationDto.setComplateTask(taskDto);
        String processDecision = ParamUtil.getString(bpc.request, "nextStage");
        String nextStageReplys = ParamUtil.getString(bpc.request, "nextStageReplys");
        if (!StringUtil.isEmpty(nextStageReplys) && StringUtil.isEmpty(processDecision)) {
            processDecision = nextStageReplys;
        }
        if(appStatus.equals(ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS)){
            String decisionValues = ParamUtil.getString(bpc.request, "decisionValues");
            if(StringUtil.isNotEmpty(decisionValues)&&ApplicationConsts.PROCESSING_DECISION_ROUTE_LATERALLY.equals(decisionValues)){
                processDecision=ApplicationConsts.PROCESSING_DECISION_ROUTE_LATERALLY;
            }
        }
        String routeBackReview = (String) ParamUtil.getSessionAttr(bpc.request, "routeBackReview");
        if ("canRouteBackReview".equals(routeBackReview)) {
            AppPremisesRoutingHistoryExtDto appPremisesRoutingHistoryExtDto = new AppPremisesRoutingHistoryExtDto();
            appPremisesRoutingHistoryExtDto.setComponentName(ApplicationConsts.APPLICATION_ROUTE_BACK_REVIEW);
            String[] routeBackReviews = ParamUtil.getStrings(bpc.request, "routeBackReview");
            if (routeBackReviews != null) {
                appPremisesRoutingHistoryExtDto.setComponentValue("Y");
            } else {
                appPremisesRoutingHistoryExtDto.setComponentValue("N");
                //route back and route task processing
                processDecision = ApplicationConsts.PROCESSING_DECISION_ROUTE_BACK_AND_ROUTE_TASK;
            }
            broadcastApplicationDto.setNewTaskHistoryExt(appPremisesRoutingHistoryExtDto);
        }

        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),
                applicationDto.getStatus(), taskDto.getTaskKey(), null, taskDto.getWkGrpId(), internalRemarks, null, processDecision, taskDto.getRoleId());
        broadcastApplicationDto.setComplateTaskHistory(appPremisesRoutingHistoryDto);
        //update application status
        broadcastApplicationDto.setRollBackApplicationDto((ApplicationDto) CopyUtil.copyMutableObject(applicationDto));
        applicationDto.setStatus(appStatus);
        applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        broadcastApplicationDto.setApplicationDto(applicationDto);
        String taskType = TaskConsts.TASK_TYPE_MAIN_FLOW;
        String TaskUrl = TaskConsts.TASK_PROCESS_URL_MAIN_FLOW;
        if (HcsaConsts.ROUTING_STAGE_INS.equals(stageId) && !ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS.equals(appStatus)) {
            taskType = TaskConsts.TASK_TYPE_INSPECTION;
            subStageId = HcsaConsts.ROUTING_STAGE_POT;
            //update inspector status
            updateInspectionStatus(applicationViewDto.getAppPremisesCorrelationId(), InspectionConstants.INSPECTION_STATUS_PENDING_PREPARE_REPORT);
        }
        //reply inspector
        if (ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS.equals(appStatus)) {
            List<TaskDto> taskDtos = organizationClient.getTaskByAppNoStatus(applicationDto.getApplicationNo(), TaskConsts.TASK_STATUS_COMPLETED, TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION).getEntity();
            taskType = taskDtos.get(0).getTaskType();
            TaskUrl = TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION;
            subStageId = HcsaConsts.ROUTING_STAGE_PRE;
            //update inspector status
            updateInspectionStatus(applicationViewDto.getAppPremisesCorrelationId(), InspectionConstants.INSPECTION_STATUS_PENDING_PRE);
        }
        //be cessation flow
        if (ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationDto.getApplicationType())) {
            List<AppPremisesRoutingHistoryDto> rollBackHistroyList = applicationClient.getHistoryByAppNoAndDecision(applicationDto.getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_CESSATION_BE_DECISION).getEntity();
            if (!IaisCommonUtils.isEmpty(rollBackHistroyList) && rollBackHistroyList.size() < 2) {
                TaskUrl = TaskConsts.TASK_PROCESS_URL_RESCHEDULING_CESSATION_RFI;
            }
        }
        //DMS go to main flow
        if (ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(appStatus)) {
            taskType = TaskConsts.TASK_TYPE_MAIN_FLOW;
            TaskUrl = TaskConsts.TASK_PROCESS_URL_MAIN_FLOW;
        }
        TaskDto newTaskDto = TaskUtil.getTaskDto(applicationDto.getApplicationNo(), stageId, taskType,
                taskDto.getRefNo(), TaskConsts.TASK_STATUS_PENDING,wrkGpId, userId, new Date(), null,0, TaskUrl, roleId,
                IaisEGPHelper.getCurrentAuditTrailDto());
        broadcastOrganizationDto.setCreateTask(newTaskDto);

        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew = getAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), stageId, subStageId,
                taskDto.getWkGrpId(), null, null, null, taskDto.getRoleId());
        broadcastApplicationDto.setNewTaskHistory(appPremisesRoutingHistoryDtoNew);

        //save the broadcast
        //set vehicle No
        broadcastApplicationDto = broadcastService.replySetVehicleByRole(taskDto, applicationViewDto, broadcastApplicationDto);
        broadcastOrganizationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        broadcastApplicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        String evenRefNum = String.valueOf(System.currentTimeMillis());
        broadcastOrganizationDto.setEventRefNo(evenRefNum);
        broadcastApplicationDto.setEventRefNo(evenRefNum);
        String submissionId = generateIdClient.getSeqId().getEntity();
        log.info(StringUtil.changeForLog(submissionId));
        broadcastOrganizationDto = broadcastService.svaeBroadcastOrganization(broadcastOrganizationDto, bpc.process, submissionId);
        broadcastApplicationDto = broadcastService.svaeBroadcastApplicationDto(broadcastApplicationDto, bpc.process, submissionId);

        //0062460 update FE  application status.
        applicationService.updateFEApplicaiton(broadcastApplicationDto.getApplicationDto());
    }

    private void rollBackTask(BaseProcessClass bpc, String historyId,String stageId,  String roleId, String wrkGpId, String userId) throws CloneNotSupportedException {
        //get the user for this applicationNo
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        String taskType = TaskConsts.TASK_TYPE_MAIN_FLOW;
        String TaskUrl = TaskConsts.TASK_PROCESS_URL_MAIN_FLOW;
        String appStatus;

        //status by
        switch (roleId){
            case RoleConsts.USER_ROLE_AO1:appStatus =ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01;break;
            case RoleConsts.USER_ROLE_AO2:appStatus =ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02;break;
            case RoleConsts.USER_ROLE_AO3:appStatus =ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03;break;
            case RoleConsts.USER_ROLE_PSO:appStatus =ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING;break;
            case RoleConsts.USER_ROLE_ASO:appStatus =ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING;break;
            default:appStatus= applicationViewDto.getApplicationDto().getStatus();
        }

        String internalRemarks = ParamUtil.getString(bpc.request, "internalRemarks");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        BroadcastOrganizationDto broadcastOrganizationDto = new BroadcastOrganizationDto();
        BroadcastApplicationDto broadcastApplicationDto = new BroadcastApplicationDto();

        //complated this task and create the history
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
        String subStageId = null;
        broadcastOrganizationDto.setRollBackComplateTask((TaskDto) CopyUtil.copyMutableObject(taskDto));
        taskDto = completedTask(taskDto);
        broadcastOrganizationDto.setComplateTask(taskDto);
        String processDecision = ParamUtil.getString(bpc.request, "nextStage");
        String nextStageReplys = ParamUtil.getString(bpc.request, "nextStageReplys");
        if (!StringUtil.isEmpty(nextStageReplys) && StringUtil.isEmpty(processDecision)) {
            processDecision = nextStageReplys;
        }

        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),
                applicationDto.getStatus(), taskDto.getTaskKey(), null, taskDto.getWkGrpId(), internalRemarks, null, processDecision, taskDto.getRoleId());
        broadcastApplicationDto.setComplateTaskHistory(appPremisesRoutingHistoryDto);
        //update application status
        broadcastApplicationDto.setRollBackApplicationDto((ApplicationDto) CopyUtil.copyMutableObject(applicationDto));
        applicationDto.setStatus(appStatus);
        applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        broadcastApplicationDto.setApplicationDto(applicationDto);


        //be cessation flow
        if (ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationDto.getApplicationType())) {
            List<AppPremisesRoutingHistoryDto> rollBackHistroyList = applicationClient.getHistoryByAppNoAndDecision(applicationDto.getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_CESSATION_BE_DECISION).getEntity();
            if (!IaisCommonUtils.isEmpty(rollBackHistroyList) && rollBackHistroyList.size() < 2) {
                TaskUrl = TaskConsts.TASK_PROCESS_URL_RESCHEDULING_CESSATION_RFI;
            }
        }

        TaskDto newTaskDto = TaskUtil.getTaskDto(applicationDto.getApplicationNo(), stageId, taskType,
                taskDto.getRefNo(), TaskConsts.TASK_STATUS_PENDING,wrkGpId, userId, new Date(), null,0, TaskUrl, roleId,
                IaisEGPHelper.getCurrentAuditTrailDto());
        broadcastOrganizationDto.setCreateTask(newTaskDto);

        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew = getAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), stageId, subStageId,
                taskDto.getWkGrpId(), null, null, null, taskDto.getRoleId());
        broadcastApplicationDto.setNewTaskHistory(appPremisesRoutingHistoryDtoNew);

        //save the broadcast
        //set vehicle No
        broadcastApplicationDto = broadcastService.replySetVehicleByRole(taskDto, applicationViewDto, broadcastApplicationDto);
        broadcastApplicationDto = broadcastService.replySetSubSvcByRole(taskDto, applicationViewDto, broadcastApplicationDto);
        broadcastOrganizationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        broadcastApplicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        String evenRefNum = String.valueOf(System.currentTimeMillis());
        broadcastOrganizationDto.setEventRefNo(evenRefNum);
        broadcastApplicationDto.setEventRefNo(evenRefNum);
        String submissionId = generateIdClient.getSeqId().getEntity();
        inspectionService.saveRollBackExtInfo(broadcastApplicationDto,taskDto.getRefNo(),taskDto.getId(),historyId,stageId,wrkGpId,userId,roleId);
        log.info(StringUtil.changeForLog(submissionId));
        broadcastOrganizationDto = broadcastService.svaeBroadcastOrganization(broadcastOrganizationDto, bpc.process, submissionId);
        broadcastApplicationDto = broadcastService.svaeBroadcastApplicationDto(broadcastApplicationDto, bpc.process, submissionId);
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

    private List<UserGroupCorrelationDto> changeStatusUserGroupCorrelationDtos(List<UserGroupCorrelationDto> userGroupCorrelationDtos, String status) {
        List<UserGroupCorrelationDto> result = IaisCommonUtils.genNewArrayList();
        if (userGroupCorrelationDtos != null && userGroupCorrelationDtos.size() > 0) {
            for (UserGroupCorrelationDto userGroupCorrelationDto : userGroupCorrelationDtos) {
                userGroupCorrelationDto.setStatus(status);
                userGroupCorrelationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                result.add(userGroupCorrelationDto);
            }
        }
        return result;
    }

    private WorkingGroupDto changeStatusWrokGroup(WorkingGroupDto workingGroupDto, String staus) {
        if (workingGroupDto != null && !StringUtil.isEmpty(staus)) {
            workingGroupDto.setStatus(staus);
        }
        return workingGroupDto;
    }

    private AppPremisesRoutingHistoryDto getAppPremisesRoutingHistory(String appNo, String appStatus,
                                                                      String stageId, String subStageId, String wrkGrpId, String internalRemarks, String externalRemarks, String processDecision,
                                                                      String roleId) {
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


    private TaskDto completedTask(TaskDto taskDto) {
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setSlaDateCompleted(new Date());
        //taskDto.setSlaRemainInDays(remainDays(taskDto));
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return taskDto;
    }

    private ApplicationDto updateApplicaiton(ApplicationDto applicationDto, String appStatus) {
        applicationDto.setStatus(appStatus);
        return applicationViewService.updateApplicaiton(applicationDto);
    }

    private List<String> getUserIds(List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos) {
        Set<String> set = IaisCommonUtils.genNewHashSet();
        if (!IaisCommonUtils.isEmpty(appPremisesRoutingHistoryDtos)) {
            for (AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto : appPremisesRoutingHistoryDtos) {
                set.add(appPremisesRoutingHistoryDto.getActionby());
            }
        }
        return new ArrayList(set);


    }

    /************************/









    private void sendAppealReject(BaseProcessClass bpc, ApplicationDto applicationDto, String MohName) throws IOException, TemplateException {
        log.info("start send email sms and msg");
        log.info(StringUtil.changeForLog("appNo: " + applicationDto.getApplicationNo()));
        String applicantName = "";
        Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();
        ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(applicationDto.getAppGrpId()).getEntity();
        OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
        String appealNo = (String) ParamUtil.getSessionAttr(bpc.request, "appealNo");
        if (orgUserDto != null) {
            applicantName = orgUserDto.getDisplayName();
        }
        List<AppPremiseMiscDto> premiseMiscDtoList = cessationClient.getAppPremiseMiscDtoListByAppId(applicationDto.getId()).getEntity();
        String appealType = "Licence";
        if (premiseMiscDtoList != null) {
            AppPremiseMiscDto premiseMiscDto = premiseMiscDtoList.get(0);
            String oldAppId = premiseMiscDto.getRelateRecId();
            ApplicationDto oldApplication = applicationClient.getApplicationById(oldAppId).getEntity();
            if (oldApplication != null) {
                appealType = MasterCodeUtil.getCodeDesc(oldApplication.getApplicationType());
            }
            LicenceDto licenceDto = hcsaLicenceClient.getLicDtoById(oldAppId).getEntity();
            if(licenceDto!=null){
                appealNo = licenceDto.getLicenceNo();
            }
        }

        templateContent.put("ApplicantName", applicantName);
        templateContent.put("ApplicationType", appealType);
        templateContent.put("ApplicationNo", appealNo);
        templateContent.put("ApplicationDate", Formatter.formatDateTime(applicationGroupDto.getSubmitDt(), "dd/MM/yyyy"));
        templateContent.put("MOH_AGENCY_NAME", MohName);
        templateContent.put("emailAddress", systemParamConfig.getSystemAddressOne());

        MsgTemplateDto emailTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_APPEAL_REJECT_EMAIL).getEntity();
        MsgTemplateDto smsTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_APPEAL_REJECT_SMS).getEntity();
        MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_APPEAL_REJECT_MSG).getEntity();
        Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
        subMap.put("ApplicationType", appealType);
        subMap.put("ApplicationNo", appealNo);
        String emailSubject = MsgUtil.getTemplateMessageByContent(emailTemplateDto.getTemplateName(), subMap);
        String smsSubject = MsgUtil.getTemplateMessageByContent(smsTemplateDto.getTemplateName(), subMap);
        String msgSubject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), subMap);

        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_APPEAL_REJECT_EMAIL);
        emailParam.setTemplateContent(templateContent);
        emailParam.setSubject(emailSubject);
        emailParam.setQueryCode(applicationDto.getApplicationNo());
        emailParam.setReqRefNum(applicationDto.getApplicationNo());
        emailParam.setRefId(applicationDto.getApplicationNo());
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);

        notificationHelper.sendNotification(emailParam);
        EmailParam smsParam = new EmailParam();
        smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_APPEAL_REJECT_SMS);
        smsParam.setSubject(smsSubject);
        smsParam.setTemplateContent(templateContent);
        smsParam.setQueryCode(applicationDto.getApplicationNo());
        smsParam.setReqRefNum(applicationDto.getApplicationNo());
        smsParam.setRefId(applicationDto.getApplicationNo());
        smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
        notificationHelper.sendNotification(smsParam);

        EmailParam msgParam = new EmailParam();
        msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_APPEAL_REJECT_MSG);
        msgParam.setTemplateContent(templateContent);
        msgParam.setSubject(msgSubject);
        msgParam.setQueryCode(applicationDto.getApplicationNo());
        msgParam.setReqRefNum(applicationDto.getApplicationNo());
        List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
        HcsaServiceDto svcDto = HcsaServiceCacheHelper.getServiceById(applicationDto.getServiceId());
        svcCodeList.add(svcDto.getSvcCode());
        msgParam.setSvcCodeList(svcCodeList);
        msgParam.setRefId(applicationDto.getApplicationNo());
        msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        notificationHelper.sendNotification(msgParam);
        log.info("end send email sms and msg");
    }

    private void initData(BaseProcessClass bpc) throws IOException {
        //get the task
        String taskId = "";
        try {
            taskId = ParamUtil.getMaskedString(bpc.request, "taskId");
        } catch (MaskAttackException e) {
            log.error(e.getMessage(), e);
            IaisEGPHelper.redirectUrl(bpc.response, "https://" + bpc.request.getServerName() + "/hcsa-licence-web/CsrfErrorPage.jsp");
        }

        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_LOAD_LEVELING, AuditTrailConsts.FUNCTION_APPLICATION_MAIN_FLOW);
        TaskDto taskDto = taskService.getTaskById(taskId);
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
        String roleId;
        //set internal marks fill back
        String correlationId;
        if (taskDto != null) {
            correlationId = taskDto.getRefNo();
            roleId = taskDto.getRoleId();
        } else {
            throw new IaisRuntimeException("The Task Id  is Error !!!");
        }
        log.debug(StringUtil.changeForLog("the do prepareData get the NewAppPremisesCorrelationDto"));
        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationViewService.getLastAppPremisesCorrelationDtoById(correlationId);
        appPremisesCorrelationDto.setOldCorrelationId(correlationId);
        String newCorrelationId = appPremisesCorrelationDto.getId();
        ApplicationViewDto applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(newCorrelationId,taskDto.getRoleId());
        ParamUtil.setSessionAttr(bpc.request, HcsaLicenceBeConstant.APP_SPECIAL_FLAG,
                applicationService.getSubSvcFlagToShowOrEdit(taskDto,applicationViewDto));
        ParamUtil.setSessionAttr(bpc.request, HcsaLicenceBeConstant.APP_OTHER_FLAG,
                applicationService.getSubSvcFlagToShowOrEdit(taskDto,applicationViewDto));
        initApplicationViewDtoSubSvc(applicationViewDto);
        applicationViewDto.setNewAppPremisesCorrelationDto(appPremisesCorrelationDto);

        //set can tcu date
        setShowAndEditTcuDate(bpc.request,applicationViewDto,taskDto);
        //filter vehicle
        vehicleCommonController.setVehicleInformation(bpc.request,taskDto,applicationViewDto);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
        //set recommendation dropdown value
        setRecommendationDropdownValue(bpc.request, applicationViewDto);

        //check broadcast role id
        AppPremisesRoutingHistoryDto userHistory = appPremisesRoutingHistoryService.getAppHistoryByAppNoAndActionBy(applicationViewDto.getApplicationDto().getApplicationNo(), taskDto.getUserId());
        String currentRoleId = "";
        if (userHistory != null) {
            currentRoleId = userHistory.getRoleId();
        }
        boolean broadcastAsoPso = false;
        boolean broadcastOther = false;
        boolean broadcastAso = false;
        String status = applicationViewDto.getApplicationDto().getStatus();
        //DMS
        if (ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(status)) {
            ParamUtil.setSessionAttr(bpc.request, "isDMS", "isDMS");
        }
        if (ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(status)) {
            if (RoleConsts.USER_ROLE_PSO.equals(currentRoleId) || RoleConsts.USER_ROLE_ASO.equals(currentRoleId)) {
                broadcastAsoPso = true;
                if (RoleConsts.USER_ROLE_ASO.equals(currentRoleId)) {
                    broadcastAso = true;
                }
            } else {
                broadcastOther = true;
            }
        }
        ParamUtil.setSessionAttr(bpc.request, "broadcastAsoPso", broadcastAsoPso);
        ParamUtil.setSessionAttr(bpc.request, "broadcastAso", broadcastAso);

        boolean isRequestForChange = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationViewDto.getApplicationDto().getApplicationType());
        AppPremisesRecommendationDto appPremisesRecommendationDto = applicationViewDto.getAppPremisesRecommendationDto();
        if (appPremisesRecommendationDto != null) {
            Integer recomInNumber = appPremisesRecommendationDto.getRecomInNumber();
            String chronoUnit = appPremisesRecommendationDto.getChronoUnit();
            String codeDesc = "";
            String recommendationOnlyShow;
            String recomDecision = appPremisesRecommendationDto.getRecomDecision();
            if (recomInNumber == null || recomInNumber == 0) {
                recommendationOnlyShow = "Reject";
            } else {
                recommendationOnlyShow = getRecommendationOnlyShowStr(recomInNumber, chronoUnit);
            }
            if (isRequestForChange) {
                if(InspectionReportConstants.RFC_APPROVED.equals(recomDecision)){
                    recommendationOnlyShow = "Approve";
                }else if(InspectionReportConstants.RFC_REJECTED.equals(recomDecision)){
                    recommendationOnlyShow = "Reject";
                }
            }
            //PSO 0062307
            boolean needFillingBack = (RoleConsts.USER_ROLE_INSPECTIOR.equals(roleId) || RoleConsts.USER_ROLE_PSO.equals(roleId) || RoleConsts.USER_ROLE_ASO.equals(roleId) || broadcastAsoPso) && !ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(status);
            if (needFillingBack) {
                //pso back fill
                checkRecommendationDropdownValue(recomInNumber, chronoUnit, recomDecision, applicationViewDto, bpc);
            }
            Date recomInDate = appPremisesRecommendationDto.getRecomInDate();
            String recomInDateOnlyShow = "-";
            if (recomInDate != null) {
                recomInDateOnlyShow = Formatter.formatDateTime(recomInDate, Formatter.DATE);
            }
            ParamUtil.setRequestAttr(bpc.request, "recomInDateOnlyShow", recomInDateOnlyShow);
            if (RoleConsts.USER_ROLE_INSPECTION_LEAD.equals(roleId) || RoleConsts.USER_ROLE_INSPECTIOR.equals(roleId) || RoleConsts.USER_ROLE_AO1.equals(roleId) || RoleConsts.USER_ROLE_AO2.equals(roleId) || RoleConsts.USER_ROLE_AO3.equals(roleId) || broadcastOther) {
                ParamUtil.setSessionAttr(bpc.request, "recommendationOnlyShow", recommendationOnlyShow);
            }
        }

        //Licence Start Date back fill
        //AppPremisesRecommendationDto appPremisesRecommendationDto = applicationViewDto.getAppPremisesRecommendationDto();
        if (appPremisesRecommendationDto != null && appPremisesRecommendationDto.getRecomInDate() != null) {
            Date recomInDate = appPremisesRecommendationDto.getRecomInDate();
            String date = Formatter.formatDateTime(recomInDate, Formatter.DATE);
            ParamUtil.setRequestAttr(bpc.request, "date", date);
        }

        //appeal
        setAppealTypeValues(bpc.request, applicationViewDto, roleId, taskDto.getTaskKey());

        //check route back review
        setRouteBackReview(bpc.request, applicationViewDto, roleId, taskDto, status);

        //cessation
        setCessation(bpc.request, applicationViewDto, correlationId);
        //set choose inspection
        setChooseInspectionValue(bpc.request, applicationViewDto);

        //set Aso Email
        setAsoEmail(bpc.request,applicationViewDto);
    }

    private void initApplicationViewDtoSubSvc(ApplicationViewDto applicationViewDto) {
        List<AppPremSubSvcRelDto> appPremSpecialSubSvcRelDtoList = applicationViewDto.getAppPremSpecialSubSvcRelDtoList();
        if (IaisCommonUtils.isNotEmpty(appPremSpecialSubSvcRelDtoList)){
            for (AppPremSubSvcRelDto appPremSubSvcRelDto : appPremSpecialSubSvcRelDtoList) {
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(appPremSubSvcRelDto.getSvcId());
                if (hcsaServiceDto!=null){
                    appPremSubSvcRelDto.setSvcConfigDto(hcsaServiceDto);
                }
            }
        }
        List<AppPremSubSvcRelDto> appPremOthersSubSvcRelDtoList = applicationViewDto.getAppPremOthersSubSvcRelDtoList();
        if (IaisCommonUtils.isNotEmpty(appPremOthersSubSvcRelDtoList)){
            for (AppPremSubSvcRelDto appPremSubSvcRelDto : appPremOthersSubSvcRelDtoList) {
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(appPremSubSvcRelDto.getSvcId());
                if (hcsaServiceDto!=null){
                    appPremSubSvcRelDto.setSvcConfigDto(hcsaServiceDto);
                }
            }
        }
    }

    private void setShowAndEditTcuDate(HttpServletRequest request,ApplicationViewDto applicationViewDto,TaskDto taskDto){
        String appType = applicationViewDto.getApplicationDto() == null ? "" : applicationViewDto.getApplicationDto().getApplicationType();
        if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equalsIgnoreCase( appType) ||
                ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equalsIgnoreCase( appType) ||
                ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equalsIgnoreCase( appType)){
            LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
            if(!RoleConsts.USER_ROLE_INSPECTION_LEAD.equalsIgnoreCase(loginContext.getCurRoleId())){

                if(RoleConsts.USER_ROLE_BROADCAST.equalsIgnoreCase(loginContext.getCurRoleId())){
                    if(HcsaConsts.ROUTING_STAGE_ASO.equalsIgnoreCase(taskDto.getTaskKey()) || HcsaConsts.ROUTING_STAGE_PSO.equalsIgnoreCase(taskDto.getTaskKey())){
                        applicationViewDto.setShowTcu(true);
                        applicationViewDto.setEditTcu(true);
                        return;
                    }
                }

                if(applicationViewDto.getApplicationDto() != null && fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(applicationViewDto.getApplicationDto().getAppPremisesCorrelationId(),InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity()!= null){
                    if(RoleConsts.USER_ROLE_AO1.equalsIgnoreCase(loginContext.getCurRoleId()) || RoleConsts.USER_ROLE_AO2.equalsIgnoreCase(loginContext.getCurRoleId()) ||  RoleConsts.USER_ROLE_AO3.equalsIgnoreCase(loginContext.getCurRoleId())){
                        return;
                    }
                    if( RoleConsts.USER_ROLE_INSPECTIOR.equalsIgnoreCase(loginContext.getCurRoleId())){
                        applicationViewDto.setShowTcu(true);
                        applicationViewDto.setEditTcu(true);
                        return;
                    }
                }
                applicationViewDto.setShowTcu(true);
                applicationViewDto.setEditTcu(RoleConsts.USER_ROLE_PSO.equalsIgnoreCase(loginContext.getCurRoleId())|| RoleConsts.USER_ROLE_ASO.equalsIgnoreCase(loginContext.getCurRoleId()));
            }
        }
    }
    private void setChooseInspectionValue(HttpServletRequest request, ApplicationViewDto applicationViewDto) {
        String kpiInfo = MessageUtil.getMessageDesc("LOLEV_ACK051");
        ParamUtil.setSessionAttr(request, "kpiInfo", kpiInfo);
        boolean chooseInspection = chooseInspection(applicationViewDto, request);
        ParamUtil.setSessionAttr(request, "isChooseInspection", chooseInspection);
//        ParamUtil.setSessionAttr(request,"isChooseInspection",true);
    }

    private boolean chooseInspection(ApplicationViewDto applicationViewDto, HttpServletRequest request) {
        boolean flag = false;
        if (applicationViewDto != null) {
            ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
            String originLicenceId = applicationDto.getOriginLicenceId();
            if (!StringUtil.isEmpty(originLicenceId)) {
                AppLastInsGroup appLastInsGroup = applicationClient.getAppLastInsGroup(applicationViewDto).getEntity();
                flag = appLastInsGroup.isRecomTypeLastIns();
                boolean saveRecomTypeForLastIns = appLastInsGroup.isSaveRecomTypeForLastIns();
                if (saveRecomTypeForLastIns) {
                    ParamUtil.setSessionAttr(request, "chooseInspectionChecked", "Y");
                    List<SelectOption> routingStage = (List<SelectOption>) ParamUtil.getSessionAttr(request, "verifiedValues");
                    removeStage(routingStage, RoleConsts.PROCESS_TYPE_INS);
                    applicationViewDto.setVerified(routingStage);
                    ParamUtil.setSessionAttr(request, "verifiedValues", (Serializable) routingStage);
                }
                ParamUtil.setSessionAttr(request, "AppLastInsGroup", appLastInsGroup);
            }
        }
        return flag;
    }

    private void removeStage(List<SelectOption> routingStage, String value) {
        if (IaisCommonUtils.isEmpty(routingStage) || StringUtil.isEmpty(value)) {
            return;
        }
        int index = -1;
        for (int i = 0; i <= routingStage.size(); i++) {
            if (value.equals(routingStage.get(i).getValue())) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            routingStage.remove(index);
        }
    }

    private void setCessation(HttpServletRequest request, ApplicationViewDto applicationViewDto, String correlationId) {
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String applicationType = applicationDto.getApplicationType();
        if (ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationType)) {
            AppCessLicDto appCessLicDto = new AppCessLicDto();
            String originLicenceId = applicationDto.getOriginLicenceId();
            LicenceDto licenceDto = hcsaLicenceClient.getLicDtoById(originLicenceId).getEntity();
            String svcName = licenceDto.getSvcName();
            String licenceNo = licenceDto.getLicenceNo();
            appCessLicDto.setLicenceNo(licenceNo);
            appCessLicDto.setSvcName(svcName);
            AppPremiseMiscDto appPremiseMiscDto = cessationClient.getAppPremiseMiscDtoByAppId(applicationDto.getId()).getEntity();
            AppCessMiscDto appCessMiscDto = MiscUtil.transferEntityDto(appPremiseMiscDto, AppCessMiscDto.class);
            AppGrpPremisesDto appGrpPremisesDto =
                    appCommService.getActivePremisesByAppNo(applicationDto.getApplicationNo());
            String hciAddress = IaisCommonUtils.getAddress(appGrpPremisesDto);
            AppCessHciDto appCessHciDto = new AppCessHciDto();
            String hciName = appGrpPremisesDto.getHciName();
            String hciCode = appGrpPremisesDto.getHciCode();
            appCessHciDto.setHciCode(hciCode);
            appCessHciDto.setHciName(hciName);
            appCessHciDto.setPremiseId(appGrpPremisesDto.getId());
            appCessHciDto.setHciAddress(hciAddress);
            if (appCessMiscDto != null) {
                Date effectiveDate = appCessMiscDto.getEffectiveDate();
                String reason = appCessMiscDto.getReason();
                String otherReason = appCessMiscDto.getOtherReason();
                String patTransType = appCessMiscDto.getPatTransType();
                String patTransTo = appCessMiscDto.getPatTransTo();
                String mobileNo = appCessMiscDto.getMobileNo();
                String emailAddress = appCessMiscDto.getEmailAddress();
                appCessHciDto.setPatientSelect(patTransType);
                appCessHciDto.setReason(reason);
                appCessHciDto.setOtherReason(otherReason);
                appCessHciDto.setEffectiveDate(effectiveDate);
                appCessHciDto.setTransferredWhere(appCessMiscDto.getTransferredWhere());
                appCessHciDto.setTransferDetail(appCessMiscDto.getTransferDetail());
                Map<String, String> fieldMap = IaisCommonUtils.genNewHashMap();
                MiscUtil.transferEntityDto(appCessMiscDto, AppCessHciDto.class, fieldMap, appCessHciDto);
                Boolean patNeedTrans = appCessMiscDto.getPatNeedTrans();
                if (patNeedTrans) {
                    appCessHciDto.setPatientSelect(patTransType);
                    if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_HCI.equals(patTransType) && !StringUtil.isEmpty(patTransTo)) {
                        appCessHciDto.setPatHciName(patTransTo);
                        appCessHciDto.setPatNeedTrans(Boolean.TRUE);
                        PremisesDto premisesDto = cessationBeService.getPremiseByHciCodeName(patTransTo);
                        String hciAddressPat = premisesDto.getHciAddress();
                        String hciNamePat = premisesDto.getHciName();
                        String hciCodePat = premisesDto.getHciCode();
                        appCessHciDto.setHciNamePat(hciNamePat);
                        appCessHciDto.setHciCodePat(hciCodePat);
                        appCessHciDto.setHciAddressPat(hciAddressPat);
                    }
                    if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_PRO.equals(patTransType) && !StringUtil.isEmpty(patTransTo)) {
                        appCessHciDto.setPatRegNo(patTransTo);
                        appCessHciDto.setPatNeedTrans(Boolean.TRUE);
                    }
                    if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_OTHER.equals(patTransType) && !StringUtil.isEmpty(patTransTo)) {
                        appCessHciDto.setPatOthers(patTransTo);
                        appCessHciDto.setPatNeedTrans(Boolean.TRUE);
                        appCessHciDto.setMobileNo(mobileNo);
                        appCessHciDto.setEmailAddress(emailAddress);
                    }
                } else {
                    String remarks = appCessMiscDto.getPatNoReason();
                    appCessHciDto.setPatNoRemarks(remarks);
                    appCessHciDto.setPatNoConfirm("no");
                    appCessHciDto.setPatNeedTrans(Boolean.FALSE);
                }
                List<AppCessHciDto> appCessHciDtos = IaisCommonUtils.genNewArrayList();
                appCessHciDtos.add(appCessHciDto);
                appCessLicDto.setAppCessHciDtos(appCessHciDtos);
                //spec
                /*String applicationNo = applicationDto.getApplicationNo();
                List<ApplicationDto> specApps = cessationClient.getAppByBaseAppNo(applicationNo).getEntity();
                List<AppSpecifiedLicDto> appSpecifiedLicDtos = IaisCommonUtils.genNewArrayList();
                if (!IaisCommonUtils.isEmpty(specApps)) {
                    for(ApplicationDto specApp :specApps ){
                        String specId = specApp.getOriginLicenceId();
                        LicenceDto specLicenceDto = hcsaLicenceClient.getLicDtoById(specId).getEntity();
                        if (specLicenceDto != null) {
                            AppSpecifiedLicDto appSpecifiedLicDto = new AppSpecifiedLicDto();
                            LicenceDto baseLic = hcsaLicenceClient.getLicDtoById(originLicenceId).getEntity();
                            String specLicenceNo = specLicenceDto.getLicenceNo();
                            String licenceDtoId = specLicenceDto.getId();
                            String specSvcName = specLicenceDto.getSvcName();
                            appSpecifiedLicDto.setBaseLicNo(baseLic.getLicenceNo());
                            appSpecifiedLicDto.setBaseSvcName(baseLic.getSvcName());
                            appSpecifiedLicDto.setSpecLicNo(specLicenceNo);
                            appSpecifiedLicDto.setSpecSvcName(specSvcName);
                            appSpecifiedLicDto.setSpecLicId(licenceDtoId);
                            appSpecifiedLicDtos.add(appSpecifiedLicDto);
                        }
                    }
                    ParamUtil.setSessionAttr(request, "specLicInfo", (Serializable) appSpecifiedLicDtos);
                }*/
                ParamUtil.setSessionAttr(request, "confirmDto", appCessLicDto);
                ParamUtil.setSessionAttr(request, "reasonOption", (Serializable) getReasonOption());
                ParamUtil.setSessionAttr(request, "patientsOption", (Serializable) getPatientsOption());

            }

        }
    }

    private List<SelectOption> getReasonOption() {
        List<SelectOption> selectOptions = MasterCodeUtil.retrieveOptionsByCodes(reasonArr);
        return selectOptions;
    }

    private List<SelectOption> getPatientsOption() {
        List<SelectOption> selectOptions = MasterCodeUtil.retrieveOptionsByCodes(patientsArr);
        return selectOptions;
    }

    private String getRecommendationOnlyShowStr(Integer recomInNumber, String chronoUnit) {
        String recommendationOnlyShow = "-";
        if (recomInNumber == null) {
            return "-";
        } else if (RiskConsts.YEAR.equals(chronoUnit)) {
            recommendationOnlyShow = recomInNumber + " Year(s)";
        } else if (RiskConsts.MONTH.equals(chronoUnit)) {
            if (recomInNumber >= 12) {
                if (recomInNumber % 12 == 0) {
                    if (recomInNumber / 12 == 1) {
                        recommendationOnlyShow = "1 Year";
                    } else {
                        recommendationOnlyShow = recomInNumber / 12 + " Year(s)";
                    }
                } else {
                    if (recomInNumber / 12 == 1) {
                        if (recomInNumber % 12 == 1) {
                            recommendationOnlyShow = 1 + " Year " + 1 + " Month";
                        } else {
                            recommendationOnlyShow = 1 + " Year " + recomInNumber % 12 + " Month(s)";
                        }

                    } else {
                        if (recomInNumber % 12 == 1) {
                            recommendationOnlyShow = recomInNumber / 12 + " Year(s) " + 1 + " Month";
                        } else {
                            recommendationOnlyShow = recomInNumber / 12 + " Year(s) " + recomInNumber % 12 + " Month(s)";
                        }
                    }
                }
            } else {
                if (recomInNumber == 1) {
                    recommendationOnlyShow = recomInNumber + " Month";
                } else if (recomInNumber == 0) {
                    recommendationOnlyShow = "Reject";
                } else {
                    recommendationOnlyShow = recomInNumber + " Month(s)";
                }
            }
        }
        return recommendationOnlyShow;
    }

    public void setSelectionValue(HttpServletRequest request, ApplicationViewDto applicationViewDto, TaskDto taskDto) {
        //set normal processingDecision value
        setNormalProcessingDecisionDropdownValue(request, applicationViewDto, taskDto);
        //set reply processingDecision value
        setReplyProcessingDecisionDropdownValue(request, applicationViewDto, taskDto);
        //set DMS processingDecision value
        setDmsProcessingDecisionDropdownValue(request,taskDto);
        //set route back dropdown value
        setRouteBackDropdownValue(request, applicationViewDto,taskDto);
        //set roll back dropdown value
        setRollBackDropdownValue(request, applicationViewDto,taskDto);
        //set recommendation dropdown value
//        setRecommendationDropdownValue(request,applicationViewDto);
        //set recommendation other dropdown value
        setRecommendationOtherDropdownValue(request);
        //set appeal recommendation dropdown value
        setAppealRecommendationDropdownValue(request, applicationViewDto);
    }

    public void setAsoEmail(HttpServletRequest request, ApplicationViewDto applicationViewDto) {
        ApplicationDto applicationDto =applicationViewDto.getApplicationDto();

        if(applicationDto.getStatus().equals(ApplicationConsts.APPLICATION_STATUS_ASO_EMAIL_PENDING)){
            ParamUtil.setSessionAttr(request, "confirm_err_msg", MessageUtil.replaceMessage("EMM_ERR005","8000","num"));
            if(IaisCommonUtils.isNotEmpty(applicationViewDto.getAppIntranetDocDtoList())){
                boolean hasAsoEmailDoc=false;
                for (AppIntranetDocDto docDto:applicationViewDto.getAppIntranetDocDtoList()
                ) {
                    if(docDto.getAppDocType().equals(ApplicationConsts.APP_DOC_TYPE_EMAIL_ATTACHMENT)){
                        hasAsoEmailDoc=true;
                    }
                }
                if(hasAsoEmailDoc){
                    ParamUtil.setRequestAttr(request, "hasAsoEmailDoc", "Y");
                }else {
                    ParamUtil.setRequestAttr(request, "hasAsoEmailDoc", "N");

                }
            }
            AppPremisesUpdateEmailDto emailDto=appPremisesCorrClient.retrieveEmailDraft(applicationViewDto.getAppPremisesCorrelationId(),ApplicationConsts.PROCESSING_DECISION_ASO_SEND_EMAIL).getEntity();
            if(emailDto==null){


                String applicationNo = applicationDto.getApplicationNo();
                log.debug(StringUtil.changeForLog("send approve email --- get app by applicationNo : " + applicationNo));
                String applicationType = applicationDto.getApplicationType();
                String appGrpId = applicationDto.getAppGrpId();
                ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(appGrpId).getEntity();

                String applicationTypeShow = MasterCodeUtil.getCodeDesc(applicationType);
                log.info(StringUtil.changeForLog("send notification applicationType : " + applicationTypeShow));
                OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();

                if( orgUserDto != null) {
                    EmailParam emailParam = null;

                    if (applicationDto.getApplicationType().equals(ApplicationConsts.APPLICATION_TYPE_APPEAL)) {
                        emailParam = sendAppealAppApproveNotification(applicationViewDto);
                    } else {
                        LicAppCorrelationDto licAppCorrelationDto = hcsaLicenceClient.getOneLicAppCorrelationByApplicationId(applicationDto.getId()).getEntity();
                        LicenceDto licenceDto = hcsaLicenceClient.getLicDtoById(licAppCorrelationDto.getLicenceId()).getEntity();
                        String licenceNo = licenceDto.getLicenceNo();
                        String applicantName = orgUserDto.getDisplayName();
                        log.info(StringUtil.changeForLog("send notification applicantName : " + applicantName));
                        AppPremisesRecommendationDto inspectionRecommendation = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(applicationViewDto.getAppPremisesCorrelationId(), InspectionConstants.RECOM_TYPE_INSPCTION_FOLLOW_UP_ACTION).getEntity();
                        String appDate = Formatter.formatDateTime(applicationGroupDto.getSubmitDt(), AppConsts.DEFAULT_DATE_FORMAT);

                        if (applicationDto.getApplicationType().equals(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION)) {
                            LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(licenceDto.getLicenseeId()).getEntity();
                            String organizationId = licenseeDto.getOrganizationId();
                            OrganizationDto organizationDto = organizationClient.getOrganizationById(organizationId).getEntity();
                            emailParam = sendNewAppApproveNotification(applicantName, licenceNo, organizationDto, inspectionRecommendation, appDate, applicationViewDto);
                        }
                        if (applicationDto.getApplicationType().equals(ApplicationConsts.APPLICATION_TYPE_RENEWAL)) {
                            emailParam = sendRenewalAppApproveNotification(applicantName, applicationTypeShow, applicationNo, licenceNo, inspectionRecommendation, appDate);
                        }
                        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType)) {
                            if (applicationGroupDto.getNewLicenseeId() == null) {
                                emailParam = sendRfcApproveNotification(applicantName, applicationTypeShow, applicationNo, appDate);
                            }
                            //transfee
                            if (applicationGroupDto.getNewLicenseeId() != null) {
                                emailParam = sendRfcApproveLicenseeEmail(applicationGroupDto, applicationDto, licenceNo);
                            }
                        }
                    }

                    if (emailParam != null) {
                        String mesContext = null;
                        MsgTemplateDto msgTemplateDto = generateIdClient.getMsgTemplate(emailParam.getTemplateId()).getEntity();
                        Map<String, Object> templateContent = emailParam.getTemplateContent();
                        String subject = emailParam.getSubject();
                        try {
                            mesContext = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), templateContent);
                        } catch (IOException | TemplateException e) {
                            log.error(e.getMessage(), e);
                            throw new IaisRuntimeException(e);
                        }
                        //replace num
                        mesContext = MessageTemplateUtil.replaceNum(mesContext);
                        emailDto = new AppPremisesUpdateEmailDto();
                        emailDto.setAppPremCorrId(applicationViewDto.getAppPremisesCorrelationId());
                        emailDto.setEmailType(ApplicationConsts.PROCESSING_DECISION_ASO_SEND_EMAIL);
                        emailDto.setSubject(subject);
                        emailDto.setMailContent(mesContext);
                    }
                }
            }

            ParamUtil.setSessionAttr(request,"appPremisesUpdateEmailDto",emailDto);
        }


    }



    private void setRouteBackReview(HttpServletRequest request, ApplicationViewDto applicationViewDto, String roleId, TaskDto taskDto, String status) {
        //init session
        ParamUtil.setSessionAttr(request, "routeBackReview", null);
        if (!(RoleConsts.USER_ROLE_PSO.equals(roleId) || RoleConsts.USER_ROLE_ASO.equals(roleId))) {
            if (ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02.equals(status) || ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01.equals(status)) {
                ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
                ApplicationGroupDto applicationGroupDto = applicationGroupService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
                List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtoList = applicationViewService.getStage(applicationDto.getServiceId(),
                        taskDto.getTaskKey(), applicationDto.getApplicationType(), applicationGroupDto.getIsPreInspection());
                if (hcsaSvcRoutingStageDtoList != null && hcsaSvcRoutingStageDtoList.size() > 0) {
                    ParamUtil.setSessionAttr(request, "routeBackReview", "canRouteBackReview");
                }
            }
        }
    }

    private void setAppealTypeValues(HttpServletRequest request, ApplicationViewDto applicationViewDto, String roleId, String taskKey) {
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        boolean isOtherAppealType = false;
        boolean isChangePeriodAppealType = false;
        boolean isLateFeeAppealType = false;
        String applicationType = applicationDto.getApplicationType();
        AppPremisesRecommendationDto appPremisesRecommendationDto = applicationViewDto.getAppPremisesRecommendationDto();
        if (ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType) || ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(applicationType) || ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationType)) {
            ParamUtil.setSessionAttr(request, "isAppeal", "Y");
            //get appeal type
            String appId = applicationDto.getId();
//            AppPremiseMiscDto premiseMiscDto = cessationClient.getAppPremiseMiscDtoByAppId(appId).getEntity();
            List<AppPremiseMiscDto> premiseMiscDtoList = cessationClient.getAppPremiseMiscDtoListByAppId(appId).getEntity();
            if (premiseMiscDtoList != null && ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)) {
                AppPremiseMiscDto premiseMiscDto = premiseMiscDtoList.get(0);
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
                    AppSvcPrincipalOfficersDto appSvcCgoDto = applicationClient.getApplicationCgoByAppId(appId, ApplicationConsts.PERSONNEL_PSN_TYPE_CGO).getEntity();
                    if(appSvcCgoDto==null){
                        log.error("CGO info lost!!");
                        return;
                    }
                    appSvcCgoDto.setAssignSelect("newOfficer");
                    appSvcCgoDto.setSpecialtyGetDateStr(ApplicationHelper.handleDateString(appSvcCgoDto.getSpecialtyGetDate(), null));
                    appSvcCgoDto.setCurrRegiDateStr(ApplicationHelper.handleDateString(appSvcCgoDto.getCurrRegiDate(), null));
                    appSvcCgoDto.setPraCerEndDateStr(ApplicationHelper.handleDateString(appSvcCgoDto.getPraCerEndDate(), null));
                    List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = IaisCommonUtils.genNewArrayList();
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
                    ParamUtil.setSessionAttr(request, "CgoMandatoryCount", 1);
                    ParamUtil.setSessionAttr(request, "GovernanceOfficersList", (Serializable) appSvcCgoDtoList);
                    ParamUtil.setSessionAttr(request, "CgoSelectList", (Serializable) cgoSelectList);
                    ParamUtil.setSessionAttr(request, "IdTypeSelect", (Serializable) idTypeSelOp);
                }
                //file
                List<AppPremisesSpecialDocDto> appealSpecialDocDto = fillUpCheckListGetAppClient.getAppPremisesSpecialDocByPremId(premiseMiscDto.getAppPremCorreId()).getEntity();
                if (appealSpecialDocDto != null) {
                    Collections.sort(appealSpecialDocDto,(s1,s2)->(s1.getIndex().compareTo(s2.getIndex())));
                    request.getSession().setAttribute("feAppealSpecialDocDto", appealSpecialDocDto);

                }
                String oldAppId = premiseMiscDto.getRelateRecId();
                String type = "";
                ApplicationDto oldApplication = applicationClient.getApplicationById(oldAppId).getEntity();
                if (oldApplication != null) {
                    List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationClient.getAppPremisesCorrelationsByAppId(oldApplication.getId()).getEntity();
                    if (appPremisesCorrelationDtos != null && appPremisesCorrelationDtos.size() > 0) {
                        AppPremisesCorrelationDto appPremisesCorrelationDto = appPremisesCorrelationDtos.get(0);
                        AppInsRepDto appInsRepDto = applicationClient.appGrpPremises(appPremisesCorrelationDto.getId()).getEntity();
                        if (appInsRepDto != null) {
                            String hciName = appInsRepDto.getHciName();
                            List<String> hciNames = IaisCommonUtils.genNewArrayList();
                            if (!StringUtil.isEmpty(hciName)) {
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
                if (ApplicationConsts.APPEAL_TYPE_LICENCE.equals(appealType)) {
                    LicenceDto licenceDto = licenceService.getLicDtoById(premiseMiscDto.getRelateRecId());
                    if (licenceDto != null) {
                        appealNo = licenceDto.getLicenceNo();
                        type = "licence";
                    }
                }
                ParamUtil.setSessionAttr(request, "appealNo", appealNo);
                ParamUtil.setSessionAttr(request, "appealingFor", appealNo);
                ParamUtil.setSessionAttr(request, "premiseMiscDto", premiseMiscDto);
                ParamUtil.setSessionAttr(request, "type", type);
            }
            //first ASO have no recommendation
            if (appPremisesRecommendationDto != null) {
                //set filling back
                String recomDecision = appPremisesRecommendationDto.getRecomDecision();
                Integer recomInNumber = appPremisesRecommendationDto.getRecomInNumber();
                String chronoUnit = appPremisesRecommendationDto.getChronoUnit();
//                boolean isAppealApprove = "approve".equals(recomDecision);
                boolean isAppealApprove = (InspectionReportConstants.APPROVED.equals(recomDecision)) || "approve".equals(recomDecision) || InspectionReportConstants.RFC_APPROVED.equals(recomDecision);
                boolean isAppealReject = (InspectionReportConstants.REJECTED.equals(recomDecision)) || "reject".equals(recomDecision) || InspectionReportConstants.RFC_REJECTED.equals(recomDecision);
                boolean isAso = HcsaConsts.ROUTING_STAGE_ASO.equals(taskKey) || RoleConsts.USER_ROLE_ASO.equals(roleId);
                boolean isPso = HcsaConsts.ROUTING_STAGE_PSO.equals(taskKey) || RoleConsts.USER_ROLE_PSO.equals(roleId);
                boolean isAO = RoleConsts.USER_ROLE_AO1.equals(roleId) || RoleConsts.USER_ROLE_AO2.equals(roleId) || RoleConsts.USER_ROLE_AO3.equals(roleId);
                boolean isBroadcast = RoleConsts.USER_ROLE_BROADCAST.equals(roleId) ;
                //pso
                if (isPso || isAso) {
                    if (isAppealApprove) {
                        recomDecision = "appealApprove";
                    } else if (isAppealReject) {
                        recomDecision = "appealReject";
                    }
                    ParamUtil.setRequestAttr(request, "selectAppealRecommendationValue", recomDecision);
                    if (isLateFeeAppealType && isAppealApprove) {
                        String returnFee = appPremisesRecommendationDto.getRemarks();
                        ParamUtil.setRequestAttr(request, "returnFee", returnFee);
                    } else if (isChangePeriodAppealType && isAppealApprove) {
                        ParamUtil.setRequestAttr(request, "otherChrono", chronoUnit);
                        ParamUtil.setRequestAttr(request, "otherNumber", recomInNumber);
                    }
                } else if (isAO || isBroadcast) {
                    if (isAppealApprove) {
                        recomDecision = "Approve";
                    } else if (isAppealReject) {
                        recomDecision = "Reject";
                    }
                    ParamUtil.setSessionAttr(request, "appealRecommendationValueOnlyShow", recomDecision);
                    if (isLateFeeAppealType && isAppealApprove) {
                        String returnFee = appPremisesRecommendationDto.getRemarks();
                        ParamUtil.setSessionAttr(request, "returnFeeOnlyShow", returnFee);
                    } else if (isChangePeriodAppealType && isAppealApprove) {
                        String appealRecommendationOtherOnlyShow = getRecommendationOnlyShowStr(recomInNumber, chronoUnit);
                        ParamUtil.setSessionAttr(request, "appealRecommendationOtherOnlyShow", appealRecommendationOtherOnlyShow);
                    }
                }
            }
        }
        ParamUtil.setSessionAttr(request, "isOtherAppealType", isOtherAppealType);
        ParamUtil.setSessionAttr(request, "isChangePeriodAppealType", isChangePeriodAppealType);
        ParamUtil.setSessionAttr(request, "isLateFeeAppealType", isLateFeeAppealType);
    }

    private List<SelectOption> genSpecialtySelectList(String svcCode) {
        List<SelectOption> specialtySelectList = IaisCommonUtils.genNewArrayList();
        if (!StringUtil.isEmpty(svcCode)) {
            if (AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(svcCode)) {
                specialtySelectList = IaisCommonUtils.genNewArrayList();
                SelectOption ssl1 = new SelectOption("-1", "Please select");
                SelectOption ssl2 = new SelectOption("Pathology", "Pathology");
                SelectOption ssl3 = new SelectOption("Haematology", "Haematology");
                SelectOption ssl4 = new SelectOption("other", "Others");
                specialtySelectList.add(ssl1);
                specialtySelectList.add(ssl2);
                specialtySelectList.add(ssl3);
                specialtySelectList.add(ssl4);
            } else if (AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(svcCode)) {
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

    private void setAppealRecommendationDropdownValue(HttpServletRequest request, ApplicationViewDto applicationViewDto) {
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String applicationType = applicationDto.getApplicationType();
        //add withdrawal cessation
        if (ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType) || ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(applicationType) || ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationType)) {
            //appealRecommendationValues
            List<SelectOption> appealRecommendationValues = IaisCommonUtils.genNewArrayList();
            appealRecommendationValues.add(new SelectOption("appealApprove", "Approve"));
            appealRecommendationValues.add(new SelectOption("appealReject", "Reject"));
            ParamUtil.setSessionAttr(request, "appealRecommendationValues", (Serializable) appealRecommendationValues);
        }
    }

    public void setNormalProcessingDecisionDropdownValue(HttpServletRequest request, ApplicationViewDto applicationViewDto, TaskDto taskDto) {
        log.info(StringUtil.changeForLog("The setNormalProcessingDecisionDropdownValue start ... "));
        List<SelectOption> nextStageList = IaisCommonUtils.genNewArrayList();
        String applicationType = applicationViewDto.getApplicationDto().getApplicationType();
        String applicationNo = applicationViewDto.getApplicationDto().getApplicationNo();
        String applicationStatus = applicationViewDto.getApplicationDto().getStatus();
        String applicationGroupId = applicationViewDto.getApplicationDto().getAppGrpId();
        String taskRole = taskDto.getRoleId();
        log.info(StringUtil.changeForLog("The setNormalProcessingDecisionDropdownValue applicationGroupId is -->: "+applicationGroupId));
        log.info(StringUtil.changeForLog("The setNormalProcessingDecisionDropdownValue applicationNo is -->: "+applicationNo));
        log.info(StringUtil.changeForLog("The setNormalProcessingDecisionDropdownValue applicationType is -->: "+applicationType));
        log.info(StringUtil.changeForLog("The setNormalProcessingDecisionDropdownValue applicationStatus is -->: "+applicationStatus));
        log.info(StringUtil.changeForLog("The setNormalProcessingDecisionDropdownValue taskRole is -->: "+taskRole));
        List<AppPremisesRoutingHistoryDto> rollBackHistroyList = applicationViewDto.getRollBackHistroyList();
        boolean hasRollBackHistoryList = rollBackHistroyList != null && rollBackHistroyList.size() > 0;
        boolean isCessationOrWithdrawal = ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(applicationType)
                || ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationType);
        boolean finalStage = isFinalStage(taskDto, applicationViewDto);
        boolean routeBackFlag = true;
        //if be cessation flow
        boolean isBeCessationFlow = false;
        if (ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationType)) {
            List<AppPremisesRoutingHistoryDto> temp = applicationClient.getHistoryByAppNoAndDecision(applicationNo,
                    ApplicationConsts.APPLICATION_STATUS_CESSATION_BE_DECISION).getEntity();
            if (!IaisCommonUtils.isEmpty(temp) && temp.size() < 2) {
                isBeCessationFlow = true;
            }
        }
        if (!isBeCessationFlow){
            if (RoleConsts.USER_ROLE_AO1.equals(taskRole) || RoleConsts.USER_ROLE_AO2.equals(taskRole)) {
                nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_BROADCAST_QUERY, "Broadcast"));
            }
        }
        nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_ROUTE_LATERALLY, "Route Laterally"));

        if(!finalStage){
            if (RoleConsts.USER_ROLE_AO1.equals(taskRole) || RoleConsts.USER_ROLE_AO2.equals(taskRole)) {
                nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_VERIFIED, "Support"));
            } else {
                //62875
                //role is ao3 && status is 'Pending AO3 Approval'  have no verified
                if (!(RoleConsts.USER_ROLE_AO3.equals(taskRole)
                        && ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(applicationStatus))) {
                    nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_VERIFIED, "Verified"));
                }
            }
        }

        List<String> status = new ArrayList<>(3);
        status.add(ApplicationConsts.PENDING_ASO_REPLY);
        status.add(ApplicationConsts.PENDING_PSO_REPLY);
        status.add(ApplicationConsts.PENDING_INP_REPLY);
        //62761
        Integer rfiCount = applicationService.getAppBYGroupIdAndStatus(applicationGroupId,
                ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION);
        log.info(StringUtil.changeForLog("The rfiCount is -->:" + rfiCount));
        if (!(RoleConsts.USER_ROLE_AO1.equals(taskRole) || RoleConsts.USER_ROLE_AO2.equals(taskRole)
                || RoleConsts.USER_ROLE_AO3.equals(taskRole))) {
            Map<String, String> map = applicationService.checkApplicationByAppGrpNo(
                    applicationViewDto.getApplicationGroupDto().getGroupNo());
            String canEdit = map.get(HcsaAppConst.CAN_RFI);
            if (AppConsts.YES.equals(canEdit) && rfiCount == 0) {
                nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_REQUEST_FOR_INFORMATION,
                        "Request For Information"));
            }
        }
        boolean isRejected=recommendationIsRejected(applicationViewDto);
        //62875
        if (!isBeCessationFlow
                && (hasRollBackHistoryList && RoleConsts.USER_ROLE_AO3.equals(taskRole) && ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(applicationStatus))) {
            nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_PENDING_APPROVAL, "Approve"));
            if(!isRejected){
                if(applicationType.equals(ApplicationConsts.APPLICATION_TYPE_RENEWAL)||
                        applicationType.equals(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION)||
                        applicationType.equals(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE)){
                    nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_ASO_SEND_EMAIL, "Approve (ASO Email)"));

                }else if(applicationType.equals(ApplicationConsts.APPLICATION_TYPE_APPEAL)){
                    AppPremiseMiscDto appPremiseMiscDto=applicationViewDto.getPremiseMiscDto();
                    if(appPremiseMiscDto.getReason().equals(ApplicationConsts.APPEAL_REASON_APPLICATION_ADD_CGO)
                            ||appPremiseMiscDto.getReason().equals(ApplicationConsts.APPEAL_REASON_APPLICATION_REJECTION)
                            ||appPremiseMiscDto.getReason().equals(ApplicationConsts.APPEAL_REASON_APPLICATION_CHANGE_HCI_NAME)
                            ||appPremiseMiscDto.getReason().equals(ApplicationConsts.APPEAL_REASON_LICENCE_CHANGE_PERIOD)){
                        nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_ASO_SEND_EMAIL, "Approve (ASO Email)"));
                    }
                }
            }

            nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_BROADCAST_QUERY, "Broadcast"));
            if ((status.contains(applicationStatus) || ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(applicationStatus))
                    && RoleConsts.USER_ROLE_ASO.equals(taskRole)) {

            } else {
                if (hasRollBackHistoryList) {
                    routeBackFlag = false;
                    nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_ROLLBACK, "Internal Route Back"));
                }
            }
            nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_ROUTE_TO_DMS, "Trigger to DMS"));
        }
        //if final stage
        if ((finalStage && isCessationOrWithdrawal && !hasRollBackHistoryList) || isBeCessationFlow) {
            nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_PENDING_APPROVAL, "Approve"));
            nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_REJECT, "Reject"));
            if (isBeCessationFlow) {
                if (hasRollBackHistoryList && RoleConsts.USER_ROLE_AO3.equals(taskRole) && ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(applicationViewDto.getApplicationDto().getStatus())) {
                    nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_ROUTE_TO_DMS, "Trigger to DMS"));
                }
                finalStage = true;
            }
        }
        if ((status.contains(applicationStatus)
                || ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(applicationStatus))
                && RoleConsts.USER_ROLE_ASO.equals(taskRole)) {

        } else {
            if (hasRollBackHistoryList && routeBackFlag) {
                nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_ROLLBACK, "Internal Route Back"));
            }
        }
        if (ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(applicationStatus)
                && RoleConsts.USER_ROLE_ASO.equals(taskRole)) {

        } else {
            if (hasRollBackHistoryList &&  !ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationType)) {
                nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_ROLLBACK_CR, MasterCodeUtil.getCodeDesc(InspectionConstants.PROCESS_DECI_ROLL_BACK)));
            }
        }
        ParamUtil.setSessionAttr(request, "finalStage", finalStage);
        ParamUtil.setRequestAttr(request, "hasRollBackHistoryList", hasRollBackHistoryList);
        ParamUtil.setSessionAttr(request, "nextStages", (Serializable) nextStageList);
        log.info(StringUtil.changeForLog("The setNormalProcessingDecisionDropdownValue end ... "));
    }

    public void setReplyProcessingDecisionDropdownValue(HttpServletRequest request, ApplicationViewDto applicationViewDto, TaskDto taskDto) {
        List<SelectOption> nextStageReplyList = IaisCommonUtils.genNewArrayList();
        if (!ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(applicationViewDto.getApplicationDto().getStatus())) {
            nextStageReplyList.add(new SelectOption("", "Please Select"));
        }
        nextStageReplyList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_REPLY, "Give Clarification"));
        nextStageReplyList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_ROUTE_LATERALLY, "Route Laterally"));
        String applicationGroupId = applicationViewDto.getApplicationDto().getAppGrpId();
        Integer rfiCount = applicationService.getAppBYGroupIdAndStatus(applicationGroupId,
                ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION);
        log.info(StringUtil.changeForLog("The rfiCount is -->:" + rfiCount));

        if (! RoleConsts.USER_ROLE_AO2.equals(taskDto.getRoleId())) {
            Map<String, String> map = applicationService.checkApplicationByAppGrpNo(
                    applicationViewDto.getApplicationGroupDto().getGroupNo());
            String canEdit = map.get(HcsaAppConst.CAN_RFI);
            if (AppConsts.YES.equals(canEdit) && rfiCount == 0) {
                nextStageReplyList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_REQUEST_FOR_INFORMATION,
                        "Request For Information"));
            }
        }
        ParamUtil.setSessionAttr(request, "nextStageReply", (Serializable) nextStageReplyList);
    }

    public void setDmsProcessingDecisionDropdownValue(HttpServletRequest request,TaskDto taskDto) {
        List<SelectOption> decisionValues = IaisCommonUtils.genNewArrayList();
        decisionValues.add(new SelectOption("decisionApproval", "Approve"));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(request, "applicationViewDto");
        String applicationType=applicationViewDto.getApplicationDto().getApplicationType();
        boolean isRejected=recommendationIsRejected(applicationViewDto);
        if(!isRejected){
            if(applicationType.equals(ApplicationConsts.APPLICATION_TYPE_RENEWAL)||
                    applicationType.equals(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION)||
                    applicationType.equals(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE)){
                decisionValues.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_ASO_SEND_EMAIL, "Approve (ASO Email)"));
            }else if(applicationType.equals(ApplicationConsts.APPLICATION_TYPE_APPEAL)){
                AppPremiseMiscDto appPremiseMiscDto=applicationViewDto.getPremiseMiscDto();
                if(appPremiseMiscDto.getReason().equals(ApplicationConsts.APPEAL_REASON_APPLICATION_ADD_CGO)
                        ||appPremiseMiscDto.getReason().equals(ApplicationConsts.APPEAL_REASON_APPLICATION_REJECTION)
                        ||appPremiseMiscDto.getReason().equals(ApplicationConsts.APPEAL_REASON_APPLICATION_CHANGE_HCI_NAME)
                        ||appPremiseMiscDto.getReason().equals(ApplicationConsts.APPEAL_REASON_LICENCE_CHANGE_PERIOD)){
                    decisionValues.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_ASO_SEND_EMAIL, "Approve (ASO Email)"));
                }
            }
        }
        decisionValues.add(new SelectOption("decisionReject", "Reject"));
        decisionValues.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_ROUTE_LATERALLY,"Route Laterally"));
        ParamUtil.setSessionAttr(request, "decisionValues", (Serializable) decisionValues);
    }

    private List<SelectOption> getIdTypeSelOp() {
        List<SelectOption> idTypeSelectList = IaisCommonUtils.genNewArrayList();
        SelectOption idType0 = new SelectOption("-1", "Please Select");
        idTypeSelectList.add(idType0);
        SelectOption idType1 = new SelectOption("NRIC", "NRIC");
        idTypeSelectList.add(idType1);
        SelectOption idType2 = new SelectOption("FIN", "FIN");
        idTypeSelectList.add(idType2);
        return idTypeSelectList;
    }

    public void setRouteBackDropdownValue(HttpServletRequest request, ApplicationViewDto applicationViewDto, TaskDto taskDto) {
        //   rollback
        log.debug(StringUtil.changeForLog("the do prepareData get the rollBackMap"));
        Map<String, String> rollBackMap = IaisCommonUtils.genNewHashMap();
        List<SelectOption> rollBackStage = IaisCommonUtils.genNewArrayList();
        List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtoList = applicationViewDto.getRollBackHistroyList();
        if (!IaisCommonUtils.isEmpty(appPremisesRoutingHistoryDtoList)) {
            for (AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto : appPremisesRoutingHistoryDtoList) {
//                String displayName = applicationViewService.getStageById(appPremisesRoutingHistoryDto.getStageId()).getStageName();
                String displayName = appPremisesRoutingHistoryDto.getRoleId();
                String userId = appPremisesRoutingHistoryDto.getActionby();
                String wrkGrpId = appPremisesRoutingHistoryDto.getWrkGrpId();
                OrgUserDto user = applicationViewService.getUserById(userId);
                if(user != null&&ROLE.indexOf(taskDto.getRoleId())>ROLE.indexOf(displayName)) {
                    String actionBy = user.getDisplayName();
                    rollBackMap.put(actionBy + " (" + displayName + ")", appPremisesRoutingHistoryDto.getStageId() + "," + wrkGrpId + "," + userId + "," + appPremisesRoutingHistoryDto.getRoleId());
                    String maskRollBackValue = MaskUtil.maskValue("rollBack", appPremisesRoutingHistoryDto.getStageId() + "," + wrkGrpId + "," + userId + "," + appPremisesRoutingHistoryDto.getRoleId());
                    SelectOption selectOption = new SelectOption(maskRollBackValue, actionBy + " (" + displayName + ")");
                    rollBackStage.add(selectOption);
                }
            }
        } else {
            log.debug(StringUtil.changeForLog("the do prepareData do not have the rollback history"));
        }
        applicationViewDto.setRollBack(rollBackMap);
        rollBackStage.sort(new Comparator<SelectOption>() {
            @Override
            public int compare(SelectOption o1, SelectOption o2) {
                String displayName = o1.getText();
                String displayName2 = o2.getText();
                String role1=displayName.substring(displayName.lastIndexOf('('),displayName.lastIndexOf(')'));
                String role2=displayName2.substring(displayName2.lastIndexOf('('),displayName2.lastIndexOf(')'));
                int diff = ROLE.indexOf(role1) - ROLE.indexOf(role2);
                if (diff > 0) {
                    return 1;
                } else if (diff < 0) {
                    return -1;
                }
                return 0;
            }
        });
        ParamUtil.setSessionAttr(request, "routeBackValues", (Serializable) rollBackStage);
    }

    public void setRollBackDropdownValue(HttpServletRequest request, ApplicationViewDto applicationViewDto, TaskDto taskDto) {
        //   rollback
        log.debug(StringUtil.changeForLog("the do prepareData get the rollBackMap"));
        Map<String, String> rollBackMap = IaisCommonUtils.genNewHashMap();
        Map<String, String> rollBackValueMap = IaisCommonUtils.genNewHashMap();
        List<SelectOption> rollBackStage = IaisCommonUtils.genNewArrayList();
        List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtoList = applicationViewDto.getRollBackHistroyList();
        if (!IaisCommonUtils.isEmpty(appPremisesRoutingHistoryDtoList)) {
            int i = 0;
            for (AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto : appPremisesRoutingHistoryDtoList) {
//                String displayName = applicationViewService.getStageById(appPremisesRoutingHistoryDto.getStageId()).getStageName();
                String displayName = appPremisesRoutingHistoryDto.getRoleId();
                String userId = appPremisesRoutingHistoryDto.getActionby();
                String wrkGrpId = appPremisesRoutingHistoryDto.getWrkGrpId();
                OrgUserDto user = applicationViewService.getUserById(userId);
                if(user != null&&ROLE.indexOf(taskDto.getRoleId())>ROLE.indexOf(displayName)) {
                    String actionBy = user.getDisplayName();
                    rollBackMap.put(actionBy + " (" + displayName + ")", appPremisesRoutingHistoryDto.getStageId() + "," + wrkGrpId + "," + userId + "," + appPremisesRoutingHistoryDto.getRoleId() + "," + appPremisesRoutingHistoryDto.getId());
                    rollBackValueMap.put(String.valueOf(i), appPremisesRoutingHistoryDto.getStageId() + "," + wrkGrpId + "," + userId + "," + appPremisesRoutingHistoryDto.getRoleId() + "," + appPremisesRoutingHistoryDto.getId());
                    SelectOption selectOption = new SelectOption(String.valueOf(i), actionBy + " (" + displayName + ")");
                    rollBackStage.add(selectOption);
                    i++;
                }
            }
        } else {
            log.debug(StringUtil.changeForLog("the do prepareData do not have the rollback history"));
        }
        rollBackStage.sort(new Comparator<SelectOption>() {
            @Override
            public int compare(SelectOption o1, SelectOption o2) {
                String displayName = o1.getText();
                String displayName2 = o2.getText();
                String role1=displayName.substring(displayName.lastIndexOf('('),displayName.lastIndexOf(')'));
                String role2=displayName2.substring(displayName2.lastIndexOf('('),displayName2.lastIndexOf(')'));
                int diff = ROLE.indexOf(role1) - ROLE.indexOf(role2);
                if (diff > 0) {
                    return 1;
                } else if (diff < 0) {
                    return -1;
                }
                return 0;
            }
        });
        applicationViewDto.setRollBack(rollBackMap);
        ParamUtil.setSessionAttr(request, "rollBackValues", (Serializable) rollBackStage);
        ParamUtil.setSessionAttr(request, "rollBackValueMap", (Serializable) rollBackValueMap);
    }

    private boolean isFinalStage(TaskDto taskDto, ApplicationViewDto applicationViewDto) {
        boolean flag = true;
        if (taskDto == null || applicationViewDto == null) {
            return flag;
        }
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        ApplicationGroupDto applicationGroupDto = applicationGroupService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtoList = applicationViewService.getStage(applicationDto.getRoutingServiceId(),
                taskDto.getTaskKey(), applicationViewDto.getApplicationDto().getApplicationType(), applicationGroupDto.getIsPreInspection());
        if (hcsaSvcRoutingStageDtoList != null) {
            flag = hcsaSvcRoutingStageDtoList.size() <= 0;
        }

        return flag;
    }

    public void setVerifiedDropdownValue(HttpServletRequest request, ApplicationViewDto applicationViewDto, TaskDto taskDto) {
        //get routing stage dropdown send to page.
        log.debug(StringUtil.changeForLog("the do prepareData get the hcsaSvcRoutingStageDtoList"));
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        //get flow service Id
        String serviceId;
        if(!StringUtil.isEmpty(applicationDto.getBaseServiceId())) {
            serviceId = applicationDto.getBaseServiceId();
        } else {
            serviceId = applicationDto.getServiceId();
        }
        ApplicationGroupDto applicationGroupDto = applicationGroupService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtoList = applicationViewService.getStage(serviceId,
                taskDto.getTaskKey(), applicationViewDto.getApplicationDto().getApplicationType(), applicationGroupDto.getIsPreInspection());
        String appStatus = applicationDto.getStatus();
        String applicationType = applicationDto.getApplicationType();

        List<SelectOption> routingStage = IaisCommonUtils.genNewArrayList();
        if (hcsaSvcRoutingStageDtoList != null) {

            if (hcsaSvcRoutingStageDtoList.size() > 0) {
                for (HcsaSvcRoutingStageDto hcsaSvcRoutingStage : hcsaSvcRoutingStageDtoList) {
                    routingStage.add(new SelectOption(hcsaSvcRoutingStage.getStageCode(), hcsaSvcRoutingStage.getStageName()));
                    if (hcsaSvcRoutingStage.isRecommend()) {
                        ParamUtil.setRequestAttr(request,
                                "selectVerified",ParamUtil.getString(request, "verified")==null
                                        ? hcsaSvcRoutingStage.getStageCode()
                                        :ParamUtil.getString(request, "verified"));
                        ParamUtil.setSessionAttr(request, "RecommendValue", hcsaSvcRoutingStage.getStageCode());
                    }
                }
                if (appStatus.equals(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01) || appStatus.equals(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02)) {
                    HcsaSvcRoutingStageDto canApproveStageDto = getCanApproveStageDto(applicationType, appStatus, serviceId);
                    boolean canApprove = checkCanApproveStage(canApproveStageDto);
                    if (canApprove) {
                        //114496
                        List<SelectOption> nextStageList = (List<SelectOption>)ParamUtil.getSessionAttr(request, "nextStages");
                        if(!IaisCommonUtils.isEmpty(nextStageList)) {
                            boolean noContainsFlag = applicationViewService.noContainsFlagByStageList(nextStageList);
                            if(noContainsFlag) {
                                nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_PENDING_APPROVAL,
                                        "Approve"));
                                boolean isRejected=recommendationIsRejected(applicationViewDto);
                                if(!isRejected){
                                    if(applicationType.equals(ApplicationConsts.APPLICATION_TYPE_RENEWAL)||
                                            applicationType.equals(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION)||
                                            applicationType.equals(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE)){
                                        nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_ASO_SEND_EMAIL, "Approve (ASO Email)"));

                                    }else if(applicationType.equals(ApplicationConsts.APPLICATION_TYPE_APPEAL)){
                                        AppPremiseMiscDto appPremiseMiscDto=applicationViewDto.getPremiseMiscDto();
                                        if(appPremiseMiscDto.getReason().equals(ApplicationConsts.APPEAL_REASON_APPLICATION_ADD_CGO)
                                                ||appPremiseMiscDto.getReason().equals(ApplicationConsts.APPEAL_REASON_APPLICATION_REJECTION)
                                                ||appPremiseMiscDto.getReason().equals(ApplicationConsts.APPEAL_REASON_APPLICATION_CHANGE_HCI_NAME)
                                                ||appPremiseMiscDto.getReason().equals(ApplicationConsts.APPEAL_REASON_LICENCE_CHANGE_PERIOD)){
                                            nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_ASO_SEND_EMAIL, "Approve (ASO Email)"));
                                        }
                                    }
                                }
                                ParamUtil.setSessionAttr(request, "nextStages", (Serializable) nextStageList);
                                ParamUtil.setSessionAttr(request, "Ao1Ao2Approve", "Y");
                            }
                        } else {
                            ParamUtil.setSessionAttr(request, "Ao1Ao2Approve", "N");
                        }
                    }
                }
            } else {
                log.debug(StringUtil.changeForLog("the do prepareData add the Approve"));
                //if  this is the last stage
                routingStage.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_PENDING_APPROVAL,
                        "Approve"));
                if(applicationType.equals(ApplicationConsts.APPLICATION_TYPE_RENEWAL)||
                        applicationType.equals(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION)||
                        applicationType.equals(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE)){
                    routingStage.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_ASO_SEND_EMAIL, "Approve (ASO Email)"));

                }else if(applicationType.equals(ApplicationConsts.APPLICATION_TYPE_APPEAL)){
                    AppPremiseMiscDto appPremiseMiscDto=applicationViewDto.getPremiseMiscDto();
                    if(appPremiseMiscDto.getReason().equals(ApplicationConsts.APPEAL_REASON_APPLICATION_ADD_CGO)
                            ||appPremiseMiscDto.getReason().equals(ApplicationConsts.APPEAL_REASON_APPLICATION_REJECTION)
                            ||appPremiseMiscDto.getReason().equals(ApplicationConsts.APPEAL_REASON_APPLICATION_CHANGE_HCI_NAME)
                            ||appPremiseMiscDto.getReason().equals(ApplicationConsts.APPEAL_REASON_LICENCE_CHANGE_PERIOD)){
                        routingStage.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_ASO_SEND_EMAIL, "Approve (ASO Email)"));
                    }
                }
            }
        }
        if (ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(applicationViewDto.getApplicationDto().getStatus())) {
            routingStage.clear();
            routingStage.add(new SelectOption(RoleConsts.USER_ROLE_ASO, RoleConsts.USER_ROLE_ADMIN_OFFICER));
        }
        if(ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST.equals(applicationViewDto.getApplicationDto().getStatus())){
            routingStage.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_BROADCAST_REPLY, "Broadcast Reply For Internal"));
        }
        //set verified values
        applicationViewDto.setVerified(routingStage);
        ParamUtil.setSessionAttr(request, "verifiedValues", (Serializable) routingStage);
    }

    public void setRecommendationDropdownValue(HttpServletRequest request, ApplicationViewDto applicationViewDto) {
        //set recommendation dropdown
        ParamUtil.setSessionAttr(request, "recommendationDropdown", (Serializable) getRecommendationDropdown(applicationViewDto, request));
    }

    private Boolean recommendationIsRejected(ApplicationViewDto applicationViewDto){
        String applicationType=applicationViewDto.getApplicationDto().getApplicationType();
        AppPremisesRecommendationDto appPremisesRecommendationDto = applicationViewDto.getAppPremisesRecommendationDto();
        if (appPremisesRecommendationDto != null  ) {
            if (!(ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType) || ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(applicationType) || ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationType))) {
                Integer recomInNumber = appPremisesRecommendationDto.getRecomInNumber();
                return null != recomInNumber && recomInNumber == 0;
            } else {
                String recomDecision = appPremisesRecommendationDto.getRecomDecision();
                return "reject".equals(recomDecision) || InspectionReportConstants.RFC_REJECTED.equals(recomDecision) || InspectionReportConstants.REJECTED.equals(recomDecision);
            }
        }
        return Boolean.FALSE;
    }

    public void setRecommendationOtherDropdownValue(HttpServletRequest request) {
        //set recommendation other dropdown
        ParamUtil.setSessionAttr(request, "recommendationOtherDropdown", (Serializable) getRecommendationOtherDropdown());
    }

    private HcsaSvcRoutingStageDto getCanApproveStageDto(String appType, String appStatus, String serviceId) {
        if (StringUtil.isEmpty(appType) || StringUtil.isEmpty(appStatus) || StringUtil.isEmpty(serviceId)) {
            return null;
        }
        String stageId = HcsaConsts.ROUTING_STAGE_AO1;
        if (appStatus.equals(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01)) {
            stageId = HcsaConsts.ROUTING_STAGE_AO1;
        } else if (appStatus.equals(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02)) {
            stageId = HcsaConsts.ROUTING_STAGE_AO2;
        }
        HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto = new HcsaSvcRoutingStageDto();
        hcsaSvcRoutingStageDto.setStageId(stageId);
        hcsaSvcRoutingStageDto.setServiceId(serviceId);
        hcsaSvcRoutingStageDto.setAppType(appType);
        HcsaSvcRoutingStageDto result = hcsaConfigClient.getHcsaSvcRoutingStageDto(hcsaSvcRoutingStageDto).getEntity();
        return result;
    }

    private boolean checkCanApproveStage(HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto) {
        if (hcsaSvcRoutingStageDto == null) {
            return false;
        }
        boolean flag = false;
        String canApprove = hcsaSvcRoutingStageDto.getCanApprove();
        if ("1".equals(canApprove)) {
            flag = true;
        }
        return flag;
    }



    private EmailParam sendNewAppApproveNotification(String applicantName,
                                                     String licenceNo,
                                                     OrganizationDto organizationDto,
                                                     AppPremisesRecommendationDto inspectionRecommendation ,String appDate ,ApplicationViewDto applicationViewDto
                                                     ){
        String MohName = AppConsts.MOH_AGENCY_NAME;
        String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
        String corpPassUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + "/main-web/eservice/INTERNET/FE_Landing";
        ApplicationDto applicationDto =applicationViewDto.getApplicationDto();
        HcsaServiceDto baseServiceDto = HcsaServiceCacheHelper.getServiceById(applicationDto.getServiceId());
        String applicationNo=applicationDto.getApplicationNo();
        String applicationTypeShow = MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType());

        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        map.put("ApplicantName", applicantName);
        map.put("ApplicationType", applicationTypeShow);
        map.put("ApplicationNumber", applicationNo);
        map.put("applicationDate", appDate);
        map.put("licenceNumber", licenceNo);
        map.put("svcNameMOSD", baseServiceDto.getSvcName()+"("+applicationViewDto.getHciAddress()+")");
        map.put("BusinessName", applicationViewDto.getHciName());
        map.put("LicenseeName",  applicationViewDto.getSubLicenseeDto().getLicenseeName());
        map.put("isSpecial", "N");
        List<AppPremSubSvcRelDto> appPremSubSvcRelDtos = appPremSubSvcBeClient.getAppPremSubSvcRelDtoListByCorrIdAndType(
                        applicationViewDto.getAppPremisesCorrelationId(), HcsaConsts.SERVICE_TYPE_SPECIFIED)
                .getEntity();
        if (!IaisCommonUtils.isEmpty(appPremSubSvcRelDtos)) {
            int i=0;
            StringBuilder svcNameLicNo = new StringBuilder();
            for (AppPremSubSvcRelDto specSvc : appPremSubSvcRelDtos) {
                HcsaServiceDto specServiceDto = HcsaServiceCacheHelper.getServiceById(specSvc.getSvcId());
                String svcName1 = specServiceDto.getSvcName();
                String index=ALPHABET_ARRAY_PROTOTYPE[i++];
                svcNameLicNo.append("<p>").append(index).append(")&nbsp;&nbsp;").append(svcName1).append("</p>");

            }
            map.put("isSpecial", "Y");
            map.put("ss1ss2", svcNameLicNo.toString());

        }
        map.put("isCorpPass", "N");

        if(inspectionRecommendation != null){
            map.put("inInspection", "Y");
            map.put("inspectionText", inspectionRecommendation.getRemarks());
        }else {
            map.put("inInspection", "N");
        }
        if(organizationDto != null){
            if(StringUtil.isEmpty(organizationDto.getUenNo())){
                map.put("isCorpPass", "Y");
                map.put("corpPassLink", corpPassUrl);
            }
        }
        map.put("systemLink", loginUrl);

        map.put("createHyperlink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_CREATE_LINK));
        map.put("regulationLink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_REGULATIONS_LINK));
        map.put("link", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_LINK));
        map.put("scdfLink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_SCDF_LINK));
        map.put("momLink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_MOM_LINK));

        map.put("phoneNumber", systemPhoneNumber);
        map.put("emailAddress1", systemAddressOne);
        map.put("emailAddress2", systemAddressTwo);
        map.put("MOH_AGENCY_NAME", MohName);

        Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
        subMap.put("ApplicationType", applicationTypeShow);
        subMap.put("ApplicationNumber", applicationNo);
        String emailSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_APPROVED_ID,subMap);

        log.debug(StringUtil.changeForLog("emailSubject : " + emailSubject));

        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_APPROVED_ID);
        emailParam.setTemplateContent(map);
        emailParam.setQueryCode(applicationNo);
        emailParam.setReqRefNum(applicationNo);
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
        emailParam.setRefId(applicationNo);
        emailParam.setSubject(emailSubject);
        //send email
        return emailParam;
    }

    private EmailParam sendRenewalAppApproveNotification(String applicantName,
                                                   String applicationTypeShow,
                                                   String applicationNo,
                                                   String licenceNo,
                                                   AppPremisesRecommendationDto inspectionRecommendation,String appDate){
        String MohName = AppConsts.MOH_AGENCY_NAME;
        String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        map.put("ApplicantName", applicantName);
        map.put("ApplicationType", applicationTypeShow);
        map.put("ApplicationNumber", applicationNo);
        map.put("applicationDate", appDate);
        map.put("licenceNumber", licenceNo);
        map.put("isSpecial", "N");
        if(inspectionRecommendation != null){
            map.put("inInspection", "Y");
            map.put("inspectionText", inspectionRecommendation.getRemarks());
        }else {
            map.put("inInspection", "N");
        }
        map.put("systemLink", loginUrl);

        map.put("createHyperlink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_CREATE_LINK));
        map.put("regulationLink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_REGULATIONS_LINK));
        map.put("link", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_LINK));
        map.put("scdfLink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_SCDF_LINK));
        map.put("momLink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_MOM_LINK));
        map.put("irasLink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_IRAS_LINK));

        map.put("phoneNumber", systemPhoneNumber);
        map.put("emailAddress1", systemAddressOne);
        map.put("emailAddress2", systemAddressTwo);
        map.put("MOH_AGENCY_NAME", MohName);
        //            String subject = "MOH HALP - Your "+ applicationTypeShow + ", "+ applicationNo +" is approved ";
        Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
        subMap.put("ApplicationType", applicationTypeShow);
        subMap.put("ApplicationNumber", applicationNo);
        String emailSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_APPROVE,subMap);
        log.debug(StringUtil.changeForLog("emailSubject : " + emailSubject));
        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_APPROVE);
        emailParam.setTemplateContent(map);
        emailParam.setQueryCode(applicationNo);
        emailParam.setReqRefNum(applicationNo);
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
        emailParam.setRefId(applicationNo);
        emailParam.setSubject(emailSubject);
        //send email

        return emailParam;
    }

    private EmailParam sendRfcApproveLicenseeEmail(ApplicationGroupDto applicationGroupDto, ApplicationDto applicationDto, String licenceNo) {
        String loginUrl = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
        Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
        LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(applicationGroupDto.getLicenseeId()).getEntity();
        LicenseeDto newLicenseeDto = organizationClient.getLicenseeDtoById(applicationGroupDto.getNewLicenseeId()).getEntity();
        String applicantName = licenseeDto.getName();
        emailMap.put("name_transferee", newLicenseeDto.getName());
        emailMap.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationDto.getApplicationType()}).get(0).getText());
        emailMap.put("ApplicationNumber", applicationDto.getApplicationNo());
        emailMap.put("ApplicationDate", Formatter.formatDate(applicationGroupDto.getSubmitDt()));
        emailMap.put("ExistingLicensee", applicantName);
        emailMap.put("transferee_licensee", newLicenseeDto.getName());
        emailMap.put("LicenceNumber", licenceNo);
        emailMap.put("specialText", "");
        //emailMap.put("Hypelink", loginUrl);
        emailMap.put("isSpecial", "N");
        emailMap.put("createHyperlink", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_CREATE_LINK));
        emailMap.put("HCSA_Regulations", MasterCodeUtil.getCodeDesc(AppConsts.MOH_RELATED_REGULATIONS_LINK));
        emailMap.put("Number", systemPhoneNumber);
        emailMap.put("email_1", systemAddressOne);
        emailMap.put("Email_2", systemAddressTwo);
        emailMap.put("systemLink", loginUrl);
        emailMap.put("MOH_AGENCY_NAM_GROUP","<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"</b>");
        emailMap.put("MOH_AGENCY_NAME", "<b>"+AppConsts.MOH_AGENCY_NAME+"</b>");
        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateContent(emailMap);
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        map.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationDto.getApplicationType()}).get(0).getText());
        map.put("ApplicationNumber", applicationDto.getApplicationNo());
        String subject =  getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_007_LICENSEE_APPROVED, map);
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_007_LICENSEE_APPROVED);
        emailParam.setQueryCode(applicationDto.getApplicationNo());
        emailParam.setReqRefNum(applicationDto.getApplicationNo());
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
        emailParam.setRefId(applicationDto.getApplicationNo());
        emailParam.setSubject(subject);
        return emailParam;
    }

    private EmailParam sendRfcApproveNotification(String applicantName, String applicationTypeShow, String applicationNo,String appDate) {
        String loginUrl = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
        Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
        emailMap.put("change", "true");
        emailMap.put("ApplicantName", applicantName);
        emailMap.put("ApplicationType", applicationTypeShow);
        emailMap.put("ApplicationNumber", applicationNo);
        emailMap.put("ApplicationDate", appDate);
        emailMap.put("systemLink", loginUrl);
        emailMap.put("MOH_AGENCY_NAM_GROUP","<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"</b>");
        emailMap.put("MOH_AGENCY_NAME", "<b>"+AppConsts.MOH_AGENCY_NAME+"</b>");
        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateContent(emailMap);
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        map.put("ApplicationType", applicationTypeShow);
        map.put("ApplicationNumber", applicationNo);
        String subject =  getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_003_APPROVED_PAYMENT, map);
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_003_APPROVED_PAYMENT);
        emailParam.setQueryCode(applicationNo);
        emailParam.setReqRefNum(applicationNo);
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
        emailParam.setRefId(applicationNo);
        emailParam.setSubject(subject);
        return emailParam;
    }


    private EmailParam sendAppealAppApproveNotification(ApplicationViewDto applicationViewDto ){
        ApplicationDto applicationDto=applicationViewDto.getApplicationDto();
        LicenceDto licenceDto = null;

        AppPremisesRecommendationDto appPremisesRecommendationDto=applicationViewDto.getAppPremisesRecommendationDto();
        AppPremiseMiscDto appPremiseMiscDto=applicationViewDto.getPremiseMiscDto();
        String reason=appPremiseMiscDto.getReason();
        String appealType = appPremiseMiscDto.getAppealType();
        if (ApplicationConsts.APPEAL_TYPE_LICENCE.equals(appealType)) {
            licenceDto = licenceService.getLicDtoById(appPremiseMiscDto.getRelateRecId());
        }
        String paymentMethodName = "";
        String paymentMode = "";
        log.info("start send email sms and msg");
        String relateRecId = appPremiseMiscDto.getRelateRecId();
        ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(applicationDto.getAppGrpId()).getEntity();

        String appType = "Licence";
        String subNo="";
        String oldAppId = appPremiseMiscDto.getRelateRecId();
        ApplicationDto oldApplication = applicationClient.getApplicationById(oldAppId).getEntity();
        if(oldApplication!=null){
            String applicationType = oldApplication.getApplicationType();
            if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationType)){
                //new
                appType ="New Licence Application";
            }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationType)){
                //renew
                appType ="Renewal";
            } else if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType)){
                //RFC
                appType ="Request For Change";
            }
            subNo=oldApplication.getApplicationNo();
        }else {

            if(licenceDto!=null){
                subNo= licenceDto.getLicenceNo();
            }
        }
        if(StringUtil.isEmpty(appType)){
            appType = "Licence";
        }


        if(ApplicationConsts.APPEAL_REASON_APPLICATION_REJECTION.equals(reason)){

        }else if(ApplicationConsts.APPEAL_REASON_APPLICATION_LATE_RENEW_FEE.equals(reason)){
            //return fee
            ApplicationDto entity = applicationClient.getApplicationById(relateRecId).getEntity();
            ApplicationGroupDto entity1 = applicationClient.getAppById(entity.getAppGrpId()).getEntity();
            if (ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT .equals(entity1.getPayMethod())) {
                paymentMethodName = "onlinePayment";
                paymentMode = MasterCodeUtil.getCodeDesc(ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT);
            }else if(ApplicationConsts.PAYMENT_METHOD_NAME_NETS.equals(entity1.getPayMethod())){
                paymentMethodName = "onlinePayment";
                paymentMode = ApplicationConsts.PAYMENT_METHOD_NAME_NETS;
            }
            else if (ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(entity1.getPayMethod())) {
                paymentMethodName = MasterCodeUtil.getCodeDesc(ApplicationConsts.PAYMENT_METHOD_NAME_GIRO);
                paymentMode = ApplicationConsts.PAYMENT_METHOD_NAME_GIRO;
            }
        }else if(ApplicationConsts.APPEAL_REASON_APPLICATION_ADD_CGO.equals(reason)){

        }else if(ApplicationConsts.APPEAL_REASON_APPLICATION_CHANGE_HCI_NAME.equals(reason)){

        }else if(ApplicationConsts.APPEAL_REASON_OTHER.equals(reason)){
            //ohter
            paymentMethodName = "other";
        }else if(ApplicationConsts.APPEAL_REASON_LICENCE_CHANGE_PERIOD.equals(reason)){
            //licence
            paymentMethodName = "applicable";
        }else{
            paymentMethodName = "other";
        }
        log.info(StringUtil.changeForLog("paymentMethodName :" +paymentMethodName));
        Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();
        OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
        templateContent.put("ApplicantName", orgUserDto.getDisplayName());
        templateContent.put("ApplicationType",  appType);
        templateContent.put("ApplicationNo", subNo);
        templateContent.put("ApplicationDate", Formatter.formatDateTime(applicationGroupDto.getSubmitDt(),"dd/MM/yyyy"));
        String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
        templateContent.put("newSystem", loginUrl);

        templateContent.put("emailAddress", systemParamConfig.getSystemAddressOne());
        templateContent.put("paymentMethod", paymentMethodName);
        if("onlinePayment".equals(paymentMethodName)){
            if(!StringUtil.isEmpty(oldApplication.getApplicationNo())){
                AppReturnFeeDto appReturnFeeDto = applicationClient.getReturnFeeByAppNo(oldApplication.getApplicationNo(),ApplicationConsts.APPLICATION_RETURN_FEE_TYPE_APPEAL).getEntity();
                templateContent.put("returnAmount", Formatter.formatterMoney(appReturnFeeDto.getReturnAmount()));
            }else{
                templateContent.put("returnAmount", "$0");
            }
            templateContent.put("paymentMode", paymentMode);
            templateContent.put("adminFee", "$100");

        } else if("GIRO".equals(paymentMethodName)){
            if(!StringUtil.isEmpty(oldApplication.getApplicationNo())){
                AppReturnFeeDto appReturnFeeDto = applicationClient.getReturnFeeByAppNo(oldApplication.getApplicationNo(),ApplicationConsts.APPLICATION_RETURN_FEE_TYPE_APPEAL).getEntity();
                templateContent.put("returnAmount", Formatter.formatterMoney(appReturnFeeDto.getReturnAmount()));
            }else{
                templateContent.put("returnAmount", "$0");
            }
            templateContent.put("adminFee", "$100");
        }else if("applicable".equals(paymentMethodName)){
            Date expiryDate;
            try {
                Date startDate = licenceDto.getStartDate();
                expiryDate = LicenceUtil.getExpiryDate(startDate,appPremisesRecommendationDto);
            }catch (Exception e){
                expiryDate = new Date();
            }
            templateContent.put("serviceName", licenceDto.getSvcName());
            templateContent.put("licenceNo", licenceDto.getLicenceNo());
            templateContent.put("licenceEndDate", Formatter.formatDate(appPremisesRecommendationDto.getRecomInDate()));
            templateContent.put("newEndDate", Formatter.formatDate(expiryDate));
        }

        log.info(StringUtil.changeForLog("templateContent :" +JsonUtil.parseToJson(templateContent)));

        MsgTemplateDto emailTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_APPEAL_APPROVE_EMAIL).getEntity();
        Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
        subMap.put("ApplicationType", appType);
        subMap.put("ApplicationNo", subNo);
        String emailSubject = null;
        try {
            emailSubject = MsgUtil.getTemplateMessageByContent(emailTemplateDto.getTemplateName(),subMap);
        } catch (IOException | TemplateException e) {
            log.error(e.getMessage(),e);
        }

        //send email
        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_APPEAL_APPROVE_EMAIL);
        emailParam.setTemplateContent(templateContent);
        emailParam.setSubject(emailSubject);
        emailParam.setQueryCode(applicationDto.getApplicationNo());
        emailParam.setReqRefNum(applicationDto.getApplicationNo());
        emailParam.setRefId(applicationDto.getApplicationNo());
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
        return emailParam;
    }

    public void preCheckList(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String doCheckList= (String) ParamUtil.getSessionAttr(request,"doCheckList");
        if(!IaisEGPConstant.YES.equals(doCheckList)){
            TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, STR_TASK_DTO);
            inspectReviseNcEmailDelegator.setCheckDataHaveFinished(request,taskDto);
            inspectReviseNcEmailDelegator.setSelectionsForDDMMAndAuditRiskSelect(request);
            inspectReviseNcEmailDelegator.preCheckList(bpc);
            ParamUtil.setSessionAttr(request, "doCheckList", IaisEGPConstant.YES);
            AdCheckListShowDto adCheckListShowDto = (AdCheckListShowDto) ParamUtil.getSessionAttr(request,"adchklDto");
            if(adCheckListShowDto!=null&&IaisCommonUtils.isNotEmpty(adCheckListShowDto.getAdItemList())){
                ParamUtil.setSessionAttr(request, "finish_ahoc_check_list", "1");
            }
        }

    }

    public void checkListNext(BaseProcessClass bpc)  {
        inspectReviseNcEmailDelegator.checkListNext(bpc);
        String actionAdditional = ParamUtil.getString(bpc.request, STR_CRUD_ACTION_ADD);
        if(STR_PROCESSING.equals(actionAdditional)){
            ParamUtil.setRequestAttr(bpc.request, STR_CRUD_ACTION_TYPE, actionAdditional);
        }
        if("editInspectorReport".equals(actionAdditional)){
            ParamUtil.setRequestAttr(bpc.request, STR_CRUD_ACTION_TYPE, actionAdditional);
        }

    }

    public void doCheckList(BaseProcessClass bpc){
        log.info("=======>>>>>doCheckList>>>>>>>>>>>>>>>>doCheckList");
        HttpServletRequest request = bpc.request;
        inspectReviseNcEmailDelegator.setCheckListData(request);
        InspectionFDtosDto serListDto = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,"serListDto");
        InspectionCheckListItemValidate inspectionCheckListItemValidate = new InspectionCheckListItemValidate();
        Map errMap = inspectionCheckListItemValidate.validate(request);
        if (!errMap.isEmpty()) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            serListDto.setCheckListTab("chkList");
            ParamUtil.setSessionAttr(request, "serListDto", serListDto);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
        } else {
            serListDto.setCheckListTab("chkList");
            ParamUtil.setSessionAttr(request, "serListDto", serListDto);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        }
        inspectReviseNcEmailDelegator.setChangeTabForChecklist(request);
        String doSubmitAction = ParamUtil.getString(request,"doSubmitAction");
        AdCheckListShowDto adCheckListShowDto = (AdCheckListShowDto) ParamUtil.getSessionAttr(request,"adchklDto");
        if(adCheckListShowDto!=null&&IaisCommonUtils.isNotEmpty(adCheckListShowDto.getAdItemList())){
            ParamUtil.setSessionAttr(request, "finish_ahoc_check_list", "1");
        }
        if("next".equals(doSubmitAction)){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
        }
    }

    public void preViewCheckList(BaseProcessClass bpc) throws IOException{


        inspectReviseNcEmailDelegator.preViewCheckList(bpc);

    }

    public void inspectionReportPre(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionReportPre start ...."));
        HttpServletRequest request = bpc.request;
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, STR_TASK_DTO);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);

        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        String applicationType = applicationViewDto.getApplicationDto().getApplicationType();
        String correlationId=applicationViewDto.getAppPremisesCorrelationId();
        String appStatus=applicationViewDto.getApplicationDto().getStatus();
        ParamUtil.setSessionAttr(bpc.request, STR_APP_TYPE, applicationType);
        InspectionReportDto insRepDto = (InspectionReportDto) ParamUtil.getSessionAttr(request, STR_INS_REP_DTO);
        if (insRepDto == null) {
            insRepDto = insRepService.getInsRepDto(taskDto, applicationViewDto, loginContext);
            InspectionReportDto inspectorUser = insRepService.getInspectorUser(taskDto, loginContext);
            insRepDto.setInspectors(inspectorUser.getInspectors());
        }
        AppPremisesRecommendationDto appPremisesRecommendationDto =  insRepService.initRecommendation(correlationId, applicationViewDto);

        String recommendation = appPremisesRecommendationDto.getRecommendation();

        AppPremisesRecommendationDto accRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPECTYPE).getEntity();
        if (accRecommendationDto != null) {
            String recomDecision = accRecommendationDto.getRecomDecision();
            if (ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT.equals(appStatus)&&InspectionConstants.PROCESS_DECI_ACCEPTS_RECTIFICATION_CONDITION.equals(recomDecision)) {
                appPremisesRecommendationDto.setRecommendation(InspectionReportConstants.APPROVEDLTC);
            }
        }

        String riskLevelForSave = appPremisesRecommendationDto.getRiskLevel();
        List<SelectOption> riskOption = insRepService.getRiskOption(applicationViewDto);
        List<SelectOption> chronoOption = insRepService.getChronoOption();
        List<SelectOption> recommendationOption = insRepService.getRecommendationOption(applicationType);
        String infoClassTop = "active";
        String infoClassBelow = "tab-pane active";
        String reportClassBelow = "tab-pane";
        String kpiInfo = MessageUtil.getMessageDesc("LOLEV_ACK051");
        ParamUtil.setSessionAttr(request, "kpiInfo", kpiInfo);
        ParamUtil.setRequestAttr(request, "appPremisesRecommendationDto", appPremisesRecommendationDto);
        ParamUtil.setSessionAttr(request, STR_APP_TYPE, null);
        ParamUtil.setSessionAttr(request, "infoClassTop", infoClassTop);
        ParamUtil.setSessionAttr(request, "reportClassTop", null);
        ParamUtil.setSessionAttr(request, "processClassTop", null);
        ParamUtil.setSessionAttr(request, "infoClassBelow", infoClassBelow);
        ParamUtil.setSessionAttr(request, "reportClassBelow", reportClassBelow);
        ParamUtil.setSessionAttr(request, "processClassBelow", "tab-pane");
        ParamUtil.setSessionAttr(request, "recommendationOption", (Serializable) recommendationOption);
        ParamUtil.setSessionAttr(request, "chronoOption", (Serializable) chronoOption);
        ParamUtil.setSessionAttr(request, "riskOption", (Serializable) riskOption);
        ParamUtil.setSessionAttr(request, STR_INS_REP_DTO, insRepDto);
        ParamUtil.setSessionAttr(request, "applicationViewDto", applicationViewDto);
        ParamUtil.setSessionAttr(request, "riskLevelForSave", riskLevelForSave);

    }

    public void inspectorReportSave(BaseProcessClass bpc) throws Exception {
        log.debug(StringUtil.changeForLog("the inspectorReportSave start ...."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");

        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        Date recomLiceStartDate = applicationViewDto.getRecomLiceStartDate();
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String applicationType = applicationDto.getApplicationType();
        List<String> appTypes = IaisCommonUtils.genNewArrayList();
        appTypes.add(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        appTypes.add(ApplicationConsts.APPLICATION_TYPE_APPEAL);
        appTypes.add(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL);
        appTypes.add(ApplicationConsts.APPLICATION_TYPE_CESSATION);
        AppPremisesRecommendationDto appPremisesRecommendationDto = insReportDelegator.prepareRecommendation(bpc,applicationType,appTypes);
        ParamUtil.setSessionAttr(bpc.request, "appPremisesRecommendationDtoEdit", appPremisesRecommendationDto);
        if (ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(applicationType)) {
            appPremisesRecommendationDto.setRecommendation("Audit");
        }else if (ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(applicationType)) {
            appPremisesRecommendationDto.setRecommendation("Post");
        }
        String actionAdditional = ParamUtil.getString(bpc.request, STR_CRUD_ACTION_ADD);
        if (!StringUtil.isEmpty(actionAdditional)) {
            if(STR_PROCESSING.equals(actionAdditional)){
                ParamUtil.setRequestAttr(bpc.request, STR_CRUD_ACTION_TYPE, actionAdditional);
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
                return;
            }
            if("editChecklist".equals(actionAdditional)){
                ParamUtil.setRequestAttr(bpc.request, STR_CRUD_ACTION_TYPE, actionAdditional);
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
                return;
            }
        }
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();

        ValidationResult validationResult = WebValidationHelper.validateProperty(appPremisesRecommendationDto, "save");
        if(appTypes.contains(applicationType)){
            String recommendationRfc = ParamUtil.getRequestString(bpc.request, "recommendationRfc");
            if(StringUtil.isEmpty(recommendationRfc)){
                String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006","Recommendation", STR_FIELD);
                errorMap.put("recommendationRfc", errMsg);
            }
        }
        validationResult.retrieveAll().remove("processingDecision");
        Map<String, String> stringStringMap = validationResult.retrieveAll();
        if (IaisCommonUtils.isNotEmpty(stringStringMap)) {
            errorMap.putAll(stringStringMap);
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            WebValidationHelper.saveAuditTrailForNoUseResult(applicationDto,errorMap);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);
            String reportClassTop = "active";
            String infoClassBelow = "tab-pane";
            String reportClassBelow = "tab-pane active";
            ParamUtil.setSessionAttr(bpc.request, "infoClassTop", null);
            ParamUtil.setSessionAttr(bpc.request, "reportClassTop", reportClassTop);
            ParamUtil.setSessionAttr(bpc.request, "processClassTop", null);
            ParamUtil.setSessionAttr(bpc.request, "infoClassBelow", infoClassBelow);
            ParamUtil.setSessionAttr(bpc.request, "reportClassBelow", reportClassBelow);
            ParamUtil.setSessionAttr(bpc.request, "processClassBelow", "tab-pane");
            return;
        }
        List<AppPremisesRecommendationDto> appPremisesRecommendationDtoList = insReportDelegator.prepareForSave(bpc, appPremisesCorrelationId, recomLiceStartDate, applicationType);
        insRepService.saveRecommendations(appPremisesRecommendationDtoList);
        String recommendationOnlyShow;
        boolean isRequestForChange = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationViewDto.getApplicationDto().getApplicationType());
        Integer recomInNumber = appPremisesRecommendationDto.getRecomInNumber();
        String chronoUnit = appPremisesRecommendationDto.getChronoUnit();
        String recomDecision = appPremisesRecommendationDto.getRecomDecision();
        if (recomInNumber == null || recomInNumber == 0) {
            recommendationOnlyShow = "Reject";
        } else {
            recommendationOnlyShow = getRecommendationOnlyShowStr(recomInNumber, chronoUnit);
        }
        if (isRequestForChange) {
            if(InspectionReportConstants.RFC_APPROVED.equals(recomDecision)){
                recommendationOnlyShow = "Approve";
            }else if(InspectionReportConstants.RFC_REJECTED.equals(recomDecision)){
                recommendationOnlyShow = "Reject";
            }
        }
        ParamUtil.setSessionAttr(bpc.request,"recommendationOnlyShow",recommendationOnlyShow);
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.FALSE);


    }


    public void routeLaterally(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, STR_TASK_DTO);
        String internalRemarks = ParamUtil.getString(bpc.request, "internalRemarks");

        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        String lrSelect = ParamUtil.getRequestString(bpc.request, "lrSelect");
        log.info(StringUtil.changeForLog("The lrSelect is -->:"+lrSelect));
        if(StringUtil.isNotEmpty(lrSelect)){
            String[] lrSelects =  lrSelect.split("_");
            String aoWorkGroupId = lrSelects[0];
            String aoUserId = lrSelects[1];
            inspEmailService.completedTask(taskDto);
            List<TaskDto> taskDtos = IaisCommonUtils.genNewArrayList();
            taskDto.setUserId(aoUserId);
            taskDto.setDateAssigned(new Date());
            taskDto.setId(null);
            taskDto.setWkGrpId(aoWorkGroupId);
            taskDto.setSlaDateCompleted(null);
            taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
            taskDtos.add(taskDto);
            taskService.createTasks(taskDtos);
            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = getAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(),
                    applicationViewDto.getApplicationDto().getStatus(), taskDto.getTaskKey(), null, taskDto.getWkGrpId(), internalRemarks, null, ApplicationConsts.PROCESSING_DECISION_ROUTE_LATERALLY, taskDto.getRoleId());
            appPremisesRoutingHistoryDto.setActionby(aoUserId);
            appPremisesRoutingHistoryService.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto);
        }
    }

    public void listAhocs(BaseProcessClass bpc){
        inspectionNcCheckListDelegator.listAhocs(bpc);
    }

    public void addAhocs(BaseProcessClass bpc){
        inspectionNcCheckListDelegator.addAhocs(bpc);

    }
    public void saveAhocs(BaseProcessClass bpc){

        inspectionNcCheckListDelegator.saveAhocs(bpc);
        ParamUtil.setSessionAttr(bpc.request, "doCheckList", null);
    }
}
