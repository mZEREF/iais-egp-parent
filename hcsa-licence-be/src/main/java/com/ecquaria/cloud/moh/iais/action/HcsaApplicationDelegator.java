package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
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
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.risk.RiskConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppReturnFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSupDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppIntranetDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessHciDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppSpecifiedLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskScoreDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
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
import com.ecquaria.cloud.moh.iais.common.mask.MaskAttackException;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.utils.TaskUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.AjaxResDto;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.TaskHistoryDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.helper.SelectHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.method.RfiCanCheck;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.ApplicationGroupService;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.BroadcastService;
import com.ecquaria.cloud.moh.iais.service.CessationBeService;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import com.ecquaria.cloud.moh.iais.service.LicenseeService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.validation.HcsaApplicationProcessUploadFileValidate;
import com.ecquaria.cloud.moh.iais.validation.HcsaApplicationViewValidate;
import com.ecquaria.cloudfeign.FeignException;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
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

    @Value("${iais.email.sender}")
    private String mailSender;

    @Value("${iais.system.one.address}")
    private String systemAddressOne;


    @Autowired
    private InsepctionNcCheckListService insepctionNcCheckListService;
    @Autowired
    private RfiCanCheck rfiCanCheck;
    @Autowired
    private NotificationHelper notificationHelper;
    @Autowired
    private FillupChklistService fillupChklistService;
    @Autowired
    private VehicleCommonController vehicleCommonController;
    private static final String[] reasonArr = new String[]{ApplicationConsts.CESSATION_REASON_NOT_PROFITABLE, ApplicationConsts.CESSATION_REASON_REDUCE_WORKLOA, ApplicationConsts.CESSATION_REASON_OTHER};
    private static final String[] patientsArr = new String[]{ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_HCI, ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_PRO, ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_OTHER};


    @PostMapping(value = "/check-ao")
    public @ResponseBody
    AjaxResDto checkAo(HttpServletRequest request) {
        log.info(StringUtil.changeForLog("the do checkAo start ...."));
        Map<String,String> roleStage = IaisCommonUtils.genNewHashMap();
        roleStage.put(RoleConsts.USER_ROLE_AO1,HcsaConsts.ROUTING_STAGE_AO1);
        roleStage.put(RoleConsts.USER_ROLE_AO2,HcsaConsts.ROUTING_STAGE_AO2);
        roleStage.put(RoleConsts.USER_ROLE_AO3,HcsaConsts.ROUTING_STAGE_AO3);
        AjaxResDto ajaxResDto = new AjaxResDto();
        String verified = ParamUtil.getString(request, "verified");
        log.info(StringUtil.changeForLog("verified is -->:"+verified));

        Map<String,String> error = IaisCommonUtils.genNewHashMap();
        if(StringUtil.isEmpty(verified)){
            String msgGenError006 = MessageUtil.replaceMessage("GENERAL_ERR0006","Assign To","field");
            error.put("verifiedError",msgGenError006);
        }
        if (error.isEmpty()) {
            String stageId = roleStage.get(verified);
            log.info(StringUtil.changeForLog("stageId is -->:"+stageId));
            if(StringUtil.isNotEmpty(stageId)){
                ajaxResDto.setResCode(AppConsts.AJAX_RES_CODE_SUCCESS);
                Map<String, String> chargesTypeAttr = IaisCommonUtils.genNewHashMap();
                chargesTypeAttr.put("name", "aoSelect");
                chargesTypeAttr.put("id", "aoSelect");

                List<String> checkedVals = IaisCommonUtils.genNewArrayList();
                String aoSelect = (String) ParamUtil.getSessionAttr(request, "aoSelect");
                if(!StringUtil.isEmpty(aoSelect)){
                    checkedVals.add(aoSelect);
                }
                ParamUtil.setSessionAttr(request,"aoSelect",null);
                log.info(StringUtil.changeForLog("aoSelect is -->:"+aoSelect));
                String chargeTypeSelHtml = SelectHelper.genMutilSelectOpHtml(chargesTypeAttr, getSelect(verified,request,stageId),
                        "Please Select", checkedVals, false,true);

                String aoSelectError = (String) ParamUtil.getSessionAttr(request, "aoSelectError");
                chargeTypeSelHtml = chargeTypeSelHtml + "<span  class=\"error-msg\" name=\"iaisErrorMsg\" id=\"error_aoSelect\">";
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
        if(!error.isEmpty()){
            ajaxResDto.setResCode(AppConsts.AJAX_RES_CODE_VALIDATE_ERROR);
            ajaxResDto.setResultJson(MessageUtil.getMessageDesc(error.get("verifiedError")));
        }
        log.info(StringUtil.changeForLog("the do checkAo end ...."));
        return ajaxResDto;
    }

    private List<SelectOption> getSelect(String verified,HttpServletRequest request,String stageId){
        log.info(StringUtil.changeForLog("the getSelect start ...."));
        List<SelectOption> result = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(verified)){
            ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(request, "applicationViewDto");
            ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
            List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
            applicationDtos.add(applicationDto);
            List<HcsaSvcStageWorkingGroupDto>  hcsaSvcStageWorkingGroupDtos = taskService.generateHcsaSvcStageWorkingGroupDtos(applicationDtos,stageId);
            hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
            if(IaisCommonUtils.isNotEmpty(hcsaSvcStageWorkingGroupDtos)){
                HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = hcsaSvcStageWorkingGroupDtos.get(0);
                String workGroupId = hcsaSvcStageWorkingGroupDto.getGroupId();
                List<OrgUserDto> orgUserDtos = taskService.getUsersByWorkGroupIdExceptLeader(workGroupId,AppConsts.COMMON_STATUS_ACTIVE);
                if(IaisCommonUtils.isNotEmpty(orgUserDtos)){
                   for(OrgUserDto orgUserDto : orgUserDtos){
                       result.add(new SelectOption(workGroupId + AppConsts.DFT_DELIMITER +orgUserDto.getId(),orgUserDto.getDisplayName()));
                   }
                }
            }
        }else {
            log.info(StringUtil.changeForLog("The verified is null"));
        }

        log.info(StringUtil.changeForLog("the getSelect end ...."));
        return result;
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
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        if (applicationDto != null) {
            if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationDto.getApplicationType())|| ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationDto.getApplicationType())) {
                //RFC
                String applicationNo = applicationDto.getApplicationNo();
                List<AppEditSelectDto> appEditSelectDtosByAppIds = rfiCanCheck.getAppEditSelectDtoSForRfi(applicationNo);
                if (!appEditSelectDtosByAppIds.isEmpty()) {
                    applicationViewDto.setAppEditSelectDto(appEditSelectDtosByAppIds.get(0));
                }
            } else if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationDto.getApplicationType())) {
                if (!StringUtil.isEmpty(applicationDto.getId())) {
                    List<AppEditSelectDto> appEditSelectDtos = applicationService.getAppEditSelectDtos(applicationDto.getId(), ApplicationConsts.APPLICATION_EDIT_TYPE_NEW);
                    if (!IaisCommonUtils.isEmpty(appEditSelectDtos)) {
                        applicationViewDto.setAppEditSelectDto(appEditSelectDtos.get(0));
                    } else {
                        AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
                        appEditSelectDto.setLicenseeEdit(true);
                        appEditSelectDto.setPremisesEdit(true);
                        appEditSelectDto.setDocEdit(true);
                        appEditSelectDto.setServiceEdit(true);
                        appEditSelectDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                        applicationViewDto.setAppEditSelectDto(appEditSelectDto);
                    }
                }

            } else {
                AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
                appEditSelectDto.setPremisesEdit(true);
                appEditSelectDto.setDocEdit(true);
                appEditSelectDto.setMedAlertEdit(true);
                appEditSelectDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                appEditSelectDto.setServiceEdit(true);
                appEditSelectDto.setPoEdit(true);
                appEditSelectDto.setDpoEdit(true);
                applicationViewDto.setAppEditSelectDto(appEditSelectDto);
            }
        }
        bpc.request.getSession().removeAttribute("appEditSelectDto");
        bpc.request.getSession().removeAttribute("pageAppEditSelectDto");
        String roleId = taskDto.getRoleId();
        log.debug(StringUtil.changeForLog("the do prepareData get the appPremisesRecommendationDto"));
        log.debug(StringUtil.changeForLog("the do prepareData roleId -->:" + roleId));

        //set selection value
        setSelectionValue(bpc.request, applicationViewDto, taskDto);
        //check inspection
        checkShowInspection(bpc, applicationViewDto, taskDto, applicationViewDto.getNewAppPremisesCorrelationDto().getOldCorrelationId());
        //set recommendation show name
        checkRecommendationShowName(bpc, applicationViewDto);
        //set verified dropdown value, 114456 from initData(bpc) function
        setVerifiedDropdownValue(bpc.request, applicationViewDto, taskDto);
        //set session
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
        log.debug(StringUtil.changeForLog("the do prepareData end ...."));
    }


    private void dealWithDoc(ApplicationViewDto applicationViewDto){
        List<AppSupDocDto> appSupDocDtoList = applicationViewDto.getAppSupDocDtoList();
        if(appSupDocDtoList!=null){
            for(AppSupDocDto v : appSupDocDtoList){

            }
        }
    }
    /**
     * StartStep: chooseStage
     *
     * @param bpc
     * @throws
     */
    public void chooseStage(BaseProcessClass bpc) throws Exception {
        log.debug(StringUtil.changeForLog("the do chooseStage start ...."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        //do upload file
        String doDocument = ParamUtil.getString(bpc.request, "uploadFile");
        String interalFileId = ParamUtil.getString(bpc.request, "interalFileId");
        if (!StringUtil.isEmpty(interalFileId) || "Y".equals(doDocument)) {
            ParamUtil.setRequestAttr(bpc.request, "crud_action_type", "PREPARE");
            ParamUtil.setRequestAttr(bpc.request, "doDocument", "Y");
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
        validateCanApprove(approveSelect, applicationViewDto, errorMap);
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
                boolean isBroadcastAsoPso = (boolean) ParamUtil.getSessionAttr(bpc.request, "broadcastAsoPso");
                isAsoPso = isAsoPso || isBroadcastAsoPso;
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

            //request for information
            if (ApplicationConsts.PROCESSING_DECISION_REQUEST_FOR_INFORMATION.equals(stage)) {
                nextStage = stage;
            }

            String reply = ParamUtil.getString(bpc.request, "nextStageReplys");
            if (!StringUtil.isEmpty(reply)) {
                nextStage = reply;
            }

            //DMS to end
            if (isDMS) {
                if (isRejectDMS) {
                    nextStage = "PROCREJ";
                } else {
                    nextStage = "PROCAP";
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

            log.debug(StringUtil.changeForLog("the nextStage is -->:" + nextStage));
            ParamUtil.setRequestAttr(bpc.request, "crud_action_type", nextStage);

            if (ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(applicationViewDto.getApplicationDto().getStatus())
                    || ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING.equals(applicationViewDto.getApplicationDto().getStatus())) {
                String[] fastTracking = ParamUtil.getStrings(bpc.request, "fastTracking");
                if (fastTracking != null) {
                    applicationViewDto.getApplicationDto().setFastTracking(true);
                }
            }
            log.debug(StringUtil.changeForLog("the do chooseStage end ...."));
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

    private void validateCanApprove(String approveSelect, ApplicationViewDto applicationViewDto, Map<String, String> errMap) {
        log.info(StringUtil.changeForLog("The validateCanApprove start ..."));
        log.info(StringUtil.changeForLog("The approveSelect is -->:" + approveSelect));
        if (!StringUtil.isEmpty(approveSelect) && ApplicationConsts.PROCESSING_DECISION_PENDING_APPROVAL.equals(approveSelect)) {
            ApplicationDto rfiApplicationDto = applicationService.getApplicationDtoByGroupIdAndStatus(applicationViewDto.getApplicationDto().getAppGrpId(), ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION);
            if (rfiApplicationDto != null) {
                List<AppEditSelectDto> appEditSelectDtos = applicationService.getAppEditSelectDtos(rfiApplicationDto.getId(), ApplicationConsts.APPLICATION_EDIT_TYPE_RFI);
                if (!IaisCommonUtils.isEmpty(appEditSelectDtos)) {
                    AppEditSelectDto appEditSelectDto = appEditSelectDtos.get(0);
                    log.info(StringUtil.changeForLog("The appEditSelectDto id is  -->:" + appEditSelectDto.getId()));
                    if (appEditSelectDto.isPremisesEdit()) {
                        errMap.put("nextStage", "You can not submit now, because there is request for information pending now.");
                    }
                } else {
                    log.debug(StringUtil.changeForLog("There is the Data error for this Application id -->:" + rfiApplicationDto.getId()));
                }
            } else {
                log.info(StringUtil.changeForLog("This applicationGroup do not have the rfi -->:" + applicationViewDto.getApplicationGroupDto().getGroupNo()));
            }
        }
        log.info(StringUtil.changeForLog("The validateCanApprove end ..."));
    }

    /**
     * StartStep: chooseAckValue
     */

    public void chooseAckValue(BaseProcessClass bpc) throws Exception {
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
        String roleId = taskDto.getRoleId();
        String successInfo = "LOLEV_ACK018";
        String nextStage = ParamUtil.getString(bpc.request, "nextStage");
        String nextStageReplys = ParamUtil.getString(bpc.request, "nextStageReplys");
        String verified = "";
        String rollBack = "";
        if (ApplicationConsts.PROCESSING_DECISION_VERIFIED.equals(nextStage)) {
            verified = ParamUtil.getString(bpc.request, "verified");
        } else if (ApplicationConsts.PROCESSING_DECISION_ROLLBACK.equals(nextStage)) {
            rollBack = ParamUtil.getMaskedString(bpc.request, "rollBack");
        }
        String decisionValue = ParamUtil.getString(bpc.request, "decisionValues");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        if(!ApplicationConsts.PROCESSING_DECISION_ROLLBACK.equals(nextStage) && applicationViewDto.isShowTcu() && applicationViewDto.isEditTcu()){
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
            } else if (RoleConsts.USER_ROLE_AO3.equals(roleId) && ApplicationConsts.PROCESSING_DECISION_ROUTE_TO_DMS.equals(nextStage)) {
                //AO3 DMS
                successInfo = "LOLEV_ACK024";
            }
        }
        //give clarification
        if (StringUtil.isEmpty(nextStage) && ApplicationConsts.PROCESSING_DECISION_REPLY.equals(nextStageReplys) && !"isDMS".equals(isDMS)) {
            successInfo = "LOLEV_ACK028";
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
        if (application != null) {
            String appNo = application.getApplicationNo();
            log.info(StringUtil.changeForLog("The appNo is -->:" + appNo));
            //HcsaConsts.ROUTING_STAGE_INS
            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = appPremisesRoutingHistoryService.
                    getAppPremisesRoutingHistoryForCurrentStage(appNo, "14848A70-820B-EA11-BE7D-000C29F371DC", RoleConsts.USER_ROLE_INSPECTIOR, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT);
            if (appPremisesRoutingHistoryDto == null) {
                appPremisesRoutingHistoryDto = appPremisesRoutingHistoryService.
                        getAppPremisesRoutingHistoryForCurrentStage(appNo, HcsaConsts.ROUTING_STAGE_ASO, RoleConsts.USER_ROLE_ASO, ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
            }
            if (appPremisesRoutingHistoryDto != null) {
                rollBack(bpc, appPremisesRoutingHistoryDto.getStageId(), ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS,
                        appPremisesRoutingHistoryDto.getRoleId(), appPremisesRoutingHistoryDto.getWrkGrpId(), appPremisesRoutingHistoryDto.getActionby());
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
            } else if (HcsaConsts.ROUTING_STAGE_INS.equals(stageId)) {
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
            broadcastOrganizationDto = broadcastService.svaeBroadcastOrganization(broadcastOrganizationDto, null, submissionId);
            broadcastApplicationDto = broadcastService.svaeBroadcastApplicationDto(broadcastApplicationDto, null, submissionId);
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
        String applicationNo = applicationViewDto.getApplicationDto().getApplicationNo();
        Date date = new Date();
        String appDate = Formatter.formatDateTime(date, "dd/MM/yyyy");
        //new application send email
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String MohName = AppConsts.MOH_AGENCY_NAME;
        String applicationType = applicationDto.getApplicationType();
        String applicationTypeShow = MasterCodeUtil.getCodeDesc(applicationType);
        HcsaServiceDto svcDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(applicationDto.getServiceId()).getEntity();
        List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
        if (svcDto != null) {
            svcCodeList.add(svcDto.getSvcCode());
        }
        if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationType)) {
            newAppSendNotification(applicationTypeShow, applicationNo, appDate, MohName, applicationDto, svcCodeList);
        } else if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationType)) {
            renewalSendNotification(applicationTypeShow, applicationNo, appDate, MohName, applicationDto, svcCodeList);
        } else if (ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)) {
            //send email Appeal - Send SMS to licensee when appeal application is reject
            try {
                sendAppealReject(bpc, applicationDto, MohName);
            } catch (Exception e) {
                log.error(e.getMessage() + "error", e);
            }
        } else if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType)) {
            if(StringUtil.isEmpty(applicationViewDto.getApplicationGroupDto().getNewLicenseeId())){
                rfcRejectedSendNotification( applicationNo, applicationViewDto.getApplicationGroupDto(), applicationDto, svcCodeList);
            }else {
                rfcLicenseeRejectedSendNotification( applicationNo, applicationViewDto.getApplicationGroupDto(), applicationDto, svcCodeList);
            }
        }

    }
    private void rfcRejectedSendNotification( String applicationNo, ApplicationGroupDto applicationGroupDto, ApplicationDto applicationDto, List<String> svcCodeList) {

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
            emailMap.put("ApplicationDate", Formatter.formatDate(new Date()));
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

    private void rfcLicenseeRejectedSendNotification( String applicationNo, ApplicationGroupDto applicationGroupDto, ApplicationDto applicationDto, List<String> svcCodeList) {

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
            emailMap.put("ApplicationDate", Formatter.formatDate(new Date()));
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

    private void newAppSendNotification(String applicationTypeShow, String applicationNo, String appDate, String MohName, ApplicationDto applicationDto, List<String> svcCodeList) {
        log.info(StringUtil.changeForLog("send new application notification start"));
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

    private void renewalSendNotification(String applicationTypeShow, String applicationNo, String appDate, String MohName, ApplicationDto applicationDto, List<String> svcCodeList) {
        log.info(StringUtil.changeForLog("send renewal application notification start"));
        //send email
        ApplicationGroupDto applicationGroupDto = applicationGroupService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
        if (applicationGroupDto != null) {
            String groupLicenseeId = applicationGroupDto.getLicenseeId();
            log.info(StringUtil.changeForLog("send renewal application notification groupLicenseeId : " + groupLicenseeId));
//            LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(groupLicenseeId).getEntity();
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
        routingTask(bpc, null, ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION, null);
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String applicationNo = applicationDto.getApplicationNo();
        String licenseeId = applicationViewDto.getApplicationGroupDto().getLicenseeId();
        LicenseeDto licenseeDto = licenseeService.getLicenseeDtoById(licenseeId);
        String externalRemarks = ParamUtil.getString(bpc.request, "comments");
        String applicationType = applicationDto.getApplicationType();
        String serviceId = applicationDto.getServiceId();
        String appId = applicationDto.getId();
        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(appId).getEntity();
        try {
            if (ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)) {
                AppPremiseMiscDto premiseMiscDto = (AppPremiseMiscDto) ParamUtil.getSessionAttr(bpc.request, "premiseMiscDto");
                if (premiseMiscDto != null) {
                    String appealingFor = premiseMiscDto.getRelateRecId();
                    HashMap<String, String> maskParams = IaisCommonUtils.genNewHashMap();
                    maskParams.put("appealingFor", appealingFor);
                    String appealType = (String) ParamUtil.getSessionAttr(bpc.request, "type");
/*
                    sendAppealMessage(appealingFor,licenseeId,maskParams,serviceId,appealType,applicationDto.getApplicationNo());
*/
                    String url = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_CALL_BACK_URL_Appeal + appealingFor + "&type=" + appealType;
                    applicationService.appealRfiAndEmail(applicationViewDto, applicationDto, maskParams, url,externalRemarks);
                }
            } else if (ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationType)) {
                HashMap<String, String> maskParams = IaisCommonUtils.genNewHashMap();
                String appGrpPremId = "";
                if (appPremisesCorrelationDto != null) {
                    appGrpPremId = appPremisesCorrelationDto.getAppGrpPremId();
                    maskParams.put("premiseId", appGrpPremId);
                }
                maskParams.put("appId", appId);
//                sendCessationMessage(appId,appGrpPremId,applicationNo,licenseeId,maskParams,serviceId);
                String url = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + InboxConst.URL_LICENCE_WEB_MODULE + "MohCessationApplication?appId=" + appId + "&premiseId=" + appGrpPremId;
                applicationService.appealRfiAndEmail(applicationViewDto, applicationDto, maskParams, url,externalRemarks);
            } else if (ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(applicationType)) {
                HashMap<String, String> maskParams = IaisCommonUtils.genNewHashMap();
                maskParams.put("rfiWithdrawAppNo", applicationNo);
                String url = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + InboxConst.URL_LICENCE_WEB_MODULE + "MohWithdrawalApplication?rfiWithdrawAppNo=" + applicationNo;
                applicationService.appealRfiAndEmail(applicationViewDto, applicationDto, maskParams, url,externalRemarks);
            }
            applicationService.applicationRfiAndEmail(applicationViewDto, applicationDto, loginContext, externalRemarks);
        } catch (Exception e) {
            log.debug(StringUtil.changeForLog("send application RfiAndEmail error"));
            log.error(e.getMessage(), e);
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

    private void sendAppealMessage(String appealingFor, String licenseeId, HashMap<String, String> maskParams, String serviceId, String appealType, String appNo) {
        if (StringUtil.isEmpty(appealingFor)
                || StringUtil.isEmpty(licenseeId)
                || StringUtil.isEmpty(serviceId)
                || StringUtil.isEmpty(appealType)
                || StringUtil.isEmpty(appNo)) {
            return;
        }
        String url = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_CALL_BACK_URL_Appeal + appealingFor + "&type=" + appealType;
        String subject = MessageConstants.MESSAGE_SUBJECT_REQUEST_FOR_INFORMATION + appNo;
        String mesContext = "Please click the link <a href='" + url + "'>here</a> for submission.";
        //send message
        sendMessage(subject, licenseeId, mesContext, maskParams, serviceId, MessageConstants.MESSAGE_TYPE_ACTION_REQUIRED);
    }

    private void sendCessationMessage(String appId, String appGrpPremId, String appNo, String licenseeId, HashMap<String, String> maskParams, String serviceId) {
        if (StringUtil.isEmpty(appId)
                || StringUtil.isEmpty(licenseeId)
                || StringUtil.isEmpty(serviceId)
                || StringUtil.isEmpty(appNo)
                || StringUtil.isEmpty(appGrpPremId)) {
            return;
        }
        String url = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + InboxConst.URL_LICENCE_WEB_MODULE + "MohCessationApplication?appId=" + appId + "&premiseId=" + appGrpPremId;
        String subject = MessageConstants.MESSAGE_SUBJECT_REQUEST_FOR_INFORMATION + appNo;
        String mesContext = "Please click the link <a href='" + url + "'>here</a> for submission.";
        sendMessage(subject, licenseeId, mesContext, maskParams, serviceId, MessageConstants.MESSAGE_TYPE_ACTION_REQUIRED);
    }

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
                getAppPremisesRoutingHistoryForCurrentStage(applicationViewDto.getApplicationDto().getApplicationNo(), HcsaConsts.ROUTING_STAGE_INS);
        if (appPremisesRoutingHistoryDto == null || ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(status)) {
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

    public void sendRouteBackEmail(String licenseeId, ApplicationViewDto applicationViewDto) throws Exception {
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String applicationType = applicationDto.getApplicationType();
        if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationType)) {
            String mesContext = "Internal route back context";
            EmailDto emailDto = new EmailDto();
            emailDto.setContent(mesContext);
            emailDto.setSubject("MOH IAIS – Internal Clarification for New Application");
            emailDto.setSender(mailSender);
            emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
            emailDto.setClientQueryCode(applicationViewDto.getApplicationDto().getAppGrpId());
            //send
            emailClient.sendNotification(emailDto).getEntity();
        } else if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationType)) {
            String mesContext = "Internal route back context";
            EmailDto emailDto = new EmailDto();
            emailDto.setContent(mesContext);
            emailDto.setSubject("MOH IAIS – Internal Clarification for Renew Application");
            emailDto.setSender(mailSender);
            emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
            emailDto.setClientQueryCode(applicationViewDto.getApplicationDto().getAppGrpId());
            //send
            emailClient.sendNotification(emailDto).getEntity();
        } else if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType)) {
            String mesContext = "Internal route back context";
            EmailDto emailDto = new EmailDto();
            emailDto.setContent(mesContext);
            emailDto.setSubject("MOH IAIS – Internal Clarification for Request for Change");
            emailDto.setSender(mailSender);
            emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
            emailDto.setClientQueryCode(applicationViewDto.getApplicationDto().getAppGrpId());
            //send
            emailClient.sendNotification(emailDto).getEntity();
        } else if (ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)) {
            String mesContext = "Internal route back context";
            EmailDto emailDto = new EmailDto();
            emailDto.setContent(mesContext);
            emailDto.setSubject("MOH IAIS – Internal Clarification for Appeal Application");
            emailDto.setSender(mailSender);
            emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
            emailDto.setClientQueryCode(applicationViewDto.getApplicationDto().getAppGrpId());
            //send
            emailClient.sendNotification(emailDto).getEntity();
        } else if (ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationType)) {
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
        String licenseeId = applicationViewDto.getApplicationGroupDto().getLicenseeId();
        if (!StringUtil.isEmpty(nextStageReplys) && StringUtil.isEmpty(processDecision)) {
            processDecision = nextStageReplys;
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
        //update application status
        broadcastApplicationDto.setRollBackApplicationDto((ApplicationDto) CopyUtil.copyMutableObject(applicationDto));
        String oldStatus = applicationDto.getStatus();
        applicationDto.setStatus(appStatus);
        //cessation
        if (ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(appStatus) && ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationType)) {
            boolean grpLic = applicationDto.isGrpLic();
            String applicationNo = applicationDto.getApplicationNo();
            String applicationDtoId = applicationDto.getId();
            AppGrpPremisesDto appGrpPremisesDto = cessationClient.getAppGrpPremisesDtoByAppId(applicationDtoId).getEntity();
            String hciCode = appGrpPremisesDto.getHciCode();
            String originLicenceId = applicationDto.getOriginLicenceId();
            List<String> specLicIds = hcsaLicenceClient.getActSpecIdByActBaseId(originLicenceId).getEntity();
            List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
            if (grpLic) {
                applicationDto.setGroupLicenceFlag(ApplicationConsts.GROUP_LICENCE_FLAG_CESSATION_NEED);
                applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_CESSATION_NEED_LICENCE);
                applicationDto.setNeedNewLicNo(true);
                applicationDtos.add(applicationDto);
                if (!IaisCommonUtils.isEmpty(specLicIds)) {
                    List<ApplicationDto> specApplicationDtos = cessationClient.getAppsByLicId(specLicIds.get(0)).getEntity();
                    if (!IaisCommonUtils.isEmpty(specApplicationDtos)) {
                        for (ApplicationDto dto : specApplicationDtos) {
                            String id = dto.getId();
                            String baseApplicationNo = dto.getBaseApplicationNo();
                            AppGrpPremisesDto appGrpPremisesSpecDto = cessationClient.getAppGrpPremisesDtoByAppId(id).getEntity();
                            String hciCode1 = appGrpPremisesSpecDto.getHciCode();
                            if (hciCode.equals(hciCode1) && applicationNo.equals(baseApplicationNo)) {
                                dto.setGroupLicenceFlag(ApplicationConsts.GROUP_LICENCE_FLAG_CESSATION_NEED);
                                dto.setStatus(ApplicationConsts.APPLICATION_STATUS_CESSATION_NEED_LICENCE);
                                dto.setNeedNewLicNo(true);
                                applicationDtos.add(dto);
                            }
                        }
                    }
                }
                applicationClient.updateCessationApplications(applicationDtos);
            } else {
                applicationDto.setGroupLicenceFlag(ApplicationConsts.GROUP_LICENCE_FLAG_CESSATION_NOT);
                applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_CESSATION_NEED_LICENCE);
                applicationDto.setNeedNewLicNo(false);
                applicationDtos.add(applicationDto);
                if (!IaisCommonUtils.isEmpty(specLicIds)) {
                    List<ApplicationDto> specApplicationDtos = cessationClient.getAppsByLicId(specLicIds.get(0)).getEntity();
                    if (!IaisCommonUtils.isEmpty(specApplicationDtos)) {
                        for (ApplicationDto dto : specApplicationDtos) {
                            String baseApplicationNo = dto.getBaseApplicationNo();
                            if (applicationNo.equals(baseApplicationNo)) {
                                dto.setGroupLicenceFlag(ApplicationConsts.GROUP_LICENCE_FLAG_CESSATION_NOT);
                                dto.setStatus(ApplicationConsts.APPLICATION_STATUS_CESSATION_NEED_LICENCE);
                                dto.setNeedNewLicNo(false);
                                applicationDtos.add(dto);
                            }
                        }
                    }
                    applicationClient.updateCessationApplications(applicationDtos);
                }
            }
        }

        if (ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(appStatus) && ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(applicationType)) {
            applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED);
        }
        if (ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(appStatus) && ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationType)) {
            String originLicenceId = applicationDto.getOriginLicenceId();
            String applicationNo = applicationDto.getApplicationNo();
            String applicationDtoId = applicationDto.getId();
            AppGrpPremisesDto appGrpPremisesDto = cessationClient.getAppGrpPremisesDtoByAppId(applicationDtoId).getEntity();
            String hciCode = appGrpPremisesDto.getHciCode();
            List<String> specLicIds = hcsaLicenceClient.getActSpecIdByActBaseId(originLicenceId).getEntity();
            if (!IaisCommonUtils.isEmpty(specLicIds)) {
                List<ApplicationDto> specApplicationDtos = cessationClient.getAppsByLicId(specLicIds.get(0)).getEntity();
                if (!IaisCommonUtils.isEmpty(specApplicationDtos)) {
                    for (ApplicationDto dto : specApplicationDtos) {
                        String id = dto.getId();
                        String baseApplicationNo = dto.getBaseApplicationNo();
                        AppGrpPremisesDto appGrpPremisesSpecDto = cessationClient.getAppGrpPremisesDtoByAppId(id).getEntity();
                        String hciCode1 = appGrpPremisesSpecDto.getHciCode();
                        if (hciCode.equals(hciCode1) && applicationNo.equals(baseApplicationNo)) {
                            dto.setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
                        }
                    }
                }
                applicationClient.updateCessationApplications(specApplicationDtos);
            }
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
           /* else if (ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(appStatus) || ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02.equals(appStatus)) {
                if (applicationDto.isFastTracking()) {
                    TaskDto newTaskDto = taskService.getRoutingTask(applicationDto, stageId, roleId, newCorrelationId);
                    newTaskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    broadcastOrganizationDto.setCreateTask(newTaskDto);
                }
                String appGrpId = applicationDto.getAppGrpId();
                List<ApplicationDto> applicationDtoList = applicationService.getApplicaitonsByAppGroupId(appGrpId);
                applicationDtoList = removeFastTrackingAndTransfer(applicationDtoList);
                List<ApplicationDto> saveApplicationDtoList = IaisCommonUtils.genNewArrayList();
                CopyUtil.copyMutableObjectList(applicationDtoList, saveApplicationDtoList);
                saveApplicationDtoList = removeCurrentApplicationDto(saveApplicationDtoList, applicationDto.getId());
                boolean flag = taskService.checkCompleteTaskByApplicationNo(saveApplicationDtoList, newCorrelationId);
//                boolean isAllSubmit = applicationService.isOtherApplicaitonSubmit(applicationDtoList,applicationDto.getApplicationNo(),
//                        ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02);
                if (flag) {
                    //update current application status in db search result
                    updateCurrentApplicationStatus(applicationDtoList, applicationDto.getId(), appStatus);
                    List<ApplicationDto> ao2AppList = getStatusAppList(applicationDtoList, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02);
                    List<ApplicationDto> ao3AppList = getStatusAppList(applicationDtoList, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03);
                    List<ApplicationDto> creatTaskApplicationList = ao2AppList;
                    //routingTask(bpc,HcsaConsts.ROUTING_STAGE_AO2,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02,RoleConsts.USER_ROLE_AO2);
                    if (IaisCommonUtils.isEmpty(ao2AppList) && !IaisCommonUtils.isEmpty(ao3AppList)) {
                        creatTaskApplicationList = ao3AppList;
                    } else {
                        stageId = HcsaConsts.ROUTING_STAGE_AO2;
                        roleId = RoleConsts.USER_ROLE_AO2;
                    }
                    if (!IaisCommonUtils.isEmpty(creatTaskApplicationList)) {
                        // send the task to Ao2  or Ao3
                        TaskHistoryDto taskHistoryDto = taskService.getRoutingTaskOneUserForSubmisison(creatTaskApplicationList,
                                stageId, roleId, IaisEGPHelper.getCurrentAuditTrailDto(), taskDto.getRoleId(), taskDto.getWkGrpId());
                        List<TaskDto> taskDtos = taskHistoryDto.getTaskDtoList();
                        List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = taskHistoryDto.getAppPremisesRoutingHistoryDtos();
                        if (!applicationDto.isFastTracking()) {
                            broadcastOrganizationDto.setOneSubmitTaskList(taskDtos);
                        }
                        broadcastApplicationDto.setOneSubmitTaskHistoryList(appPremisesRoutingHistoryDtos);
                    }
                }
            }*/
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
            } else {
                String verified = ParamUtil.getRequestString(bpc.request, "verified");
                String aoWorkGroupId = null;
                String aoUserId = null;
                if(RoleConsts.USER_ROLE_AO1.equals(verified) || RoleConsts.USER_ROLE_AO2.equals(verified) || RoleConsts.USER_ROLE_AO3.equals(verified)){
                    String aoSelect = ParamUtil.getRequestString(bpc.request, "aoSelect");
                    log.info(StringUtil.changeForLog("The aoSelect is -->:"+aoSelect));
                    if(StringUtil.isNotEmpty(aoSelect)){
                        String[] aoSelects =  aoSelect.split(AppConsts.DFT_DELIMITER);
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
                        }
                        applicationGroupDto.setAo3ApprovedDt(new Date());
                        applicationGroupDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                        broadcastApplicationDto.setApplicationGroupDto(applicationGroupDto);

                        if (needUpdateGroupStatus) {
                            //get and set return fee
                            saveApplicationDtoList = hcsaConfigClient.returnFee(saveApplicationDtoList).getEntity();
                            //save return fee
                            saveRejectReturnFee(saveApplicationDtoList, broadcastApplicationDto);
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
                boolean isCharity = false;
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
                                    List<AppPremisesCorrelationDto> oldAppPremisesCorrelationDtos=applicationClient.getAppPremisesCorrelationsByAppId(oldAppId).getEntity();
                                    String corrId=oldAppPremisesCorrelationDtos.get(0).getId();
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
                            msgInfoMap.put("ApplicationDate", Formatter.formatDateTime(new Date()));
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
        String applicationNo = applicationDto.getApplicationNo();
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
            if (ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(applicationDto.getStatus()) && !ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(applicationDto.getApplicationType()) && !ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationDto.getApplicationType())) {
                AppReturnFeeDto appReturnFeeDto = new AppReturnFeeDto();
                Double returnFee = applicationDto.getReturnFee();
                if (returnFee == null || MiscUtil.doubleEquals(returnFee, 0d)) {
                    continue;
                }
                appReturnFeeDto.setStatus("paying");
                appReturnFeeDto.setTriggerCount(0);
                appReturnFeeDto.setApplicationNo(applicationDto.getApplicationNo());
                appReturnFeeDto.setReturnAmount(returnFee);
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
            boolean isCharity = false;
            if (AcraConsts.ENTITY_TYPE_CHARITIES.equals(entityType)) {
                isCharity = true;
            } else {
                isCharity = false;
            }
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
        String refNo = taskDto.getRefNo();
        String subStageId = null;
        broadcastOrganizationDto.setRollBackComplateTask((TaskDto) CopyUtil.copyMutableObject(taskDto));
        taskDto = completedTask(taskDto);
        broadcastOrganizationDto.setComplateTask(taskDto);
        String processDecision = ParamUtil.getString(bpc.request, "nextStage");
        String nextStageReplys = ParamUtil.getString(bpc.request, "nextStageReplys");
        if (!StringUtil.isEmpty(nextStageReplys) && StringUtil.isEmpty(processDecision)) {
            processDecision = nextStageReplys;
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
            if (RoleConsts.USER_ROLE_AO1.equals(roleId)) {
                TaskUrl = TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT_REVIEW_AO1;
            } else if ((!RoleConsts.USER_ROLE_AO2.equals(roleId)) &&
                    (!RoleConsts.USER_ROLE_AO3.equals(roleId)) &&
                    (!RoleConsts.USER_ROLE_ASO.equals(roleId)) &&
                    (!RoleConsts.USER_ROLE_PSO.equals(roleId))
            ) {
                TaskUrl = TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT;
            }
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
                taskDto.getWkGrpId(), null, null, null, roleId);
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


    private void sendAppealApproved(ApplicationDto applicationDto, String licenseeId) {

        String applicationType = applicationDto.getApplicationType();
        if (ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)) {
            Map<String, Object> map = IaisCommonUtils.genNewHashMap();
            List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(licenseeId);
            MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate("5B9EADD2-F27D-EA11-BE82-000C29F371DC").getEntity();
            map.put("applicationNumber", applicationDto.getApplicationNo());

        }

    }

    private void sendRFCRejectEmail(String licenseeId, String serviceId) {
        String subject = "Request for change reject";
        String mesContext = "Request for change email";
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
        sendMessage(subject, licenseeId, mesContext, maskParams, serviceId, null);
    }


    private void sendMessage(String subject, String licenseeId, String templateMessageByContent, HashMap<String, String> maskParams, String serviceId, String messageType) {
        if (StringUtil.isEmpty(messageType)) {
            messageType = MessageConstants.MESSAGE_TYPE_NOTIFICATION;
        }
        HcsaServiceDto serviceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
        InterMessageDto interMessageDto = new InterMessageDto();
        interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY);
        interMessageDto.setSubject(subject);
        interMessageDto.setMessageType(messageType);
        String refNo = inboxMsgService.getMessageNo();
        interMessageDto.setRefNo(refNo);
        if (serviceDto != null) {
            interMessageDto.setService_id(serviceDto.getSvcCode() + "@");
        }
        interMessageDto.setUserId(licenseeId);
        interMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        interMessageDto.setMsgContent(templateMessageByContent);
        interMessageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        interMessageDto.setMaskParams(maskParams);
        inboxMsgService.saveInterMessage(interMessageDto);
    }


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
        templateContent.put("ApplicationDate", Formatter.formatDateTime(new Date(), "dd/MM/yyyy"));
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
        HcsaServiceDto svcDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(applicationDto.getServiceId()).getEntity();
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
        String roleId = "";
        //set internal marks fill back
        String correlationId = "";
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
            if (RoleConsts.USER_ROLE_INSPECTION_LEAD.equals(roleId) || RoleConsts.USER_ROLE_AO1.equals(roleId) || RoleConsts.USER_ROLE_AO2.equals(roleId) || RoleConsts.USER_ROLE_AO3.equals(roleId) || broadcastOther) {
                ParamUtil.setRequestAttr(bpc.request, "recommendationOnlyShow", recommendationOnlyShow);
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
                boolean recomTypeLastIns = appLastInsGroup.isRecomTypeLastIns();
                flag = recomTypeLastIns;
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
            AppGrpPremisesEntityDto appGrpPremisesEntityDto = applicationClient.getPremisesByAppNo(applicationDto.getApplicationNo()).getEntity();
            String blkNo = appGrpPremisesEntityDto.getBlkNo();
            String premisesId = appGrpPremisesEntityDto.getId();
            String streetName = appGrpPremisesEntityDto.getStreetName();
            String buildingName = appGrpPremisesEntityDto.getBuildingName();
            String floorNo = appGrpPremisesEntityDto.getFloorNo();
            String unitNo = appGrpPremisesEntityDto.getUnitNo();
            String postalCode = appGrpPremisesEntityDto.getPostalCode();
            String hciAddress = MiscUtil.getAddressForApp(blkNo, streetName, buildingName, floorNo, unitNo, postalCode,appGrpPremisesEntityDto.getAppPremisesOperationalUnitDtos());
            AppCessHciDto appCessHciDto = new AppCessHciDto();
            String hciName = appGrpPremisesEntityDto.getHciName();
            String hciCode = appGrpPremisesEntityDto.getHciCode();
            appCessHciDto.setHciCode(hciCode);
            appCessHciDto.setHciName(hciName);
            appCessHciDto.setPremiseId(premisesId);
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
                String applicationNo = applicationDto.getApplicationNo();
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
                }
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
        setReplyProcessingDecisionDropdownValue(request, applicationViewDto);
        //set DMS processingDecision value
        setDmsProcessingDecisionDropdownValue(request);
        //set route back dropdown value
        setRouteBackDropdownValue(request, applicationViewDto);
        //set recommendation dropdown value
//        setRecommendationDropdownValue(request,applicationViewDto);
        //set recommendation other dropdown value
        setRecommendationOtherDropdownValue(request);
        //set appeal recommendation dropdown value
        setAppealRecommendationDropdownValue(request, applicationViewDto);
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
                if (hcsaSvcRoutingStageDtoList != null & hcsaSvcRoutingStageDtoList.size() > 0) {
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
                    appSvcCgoDto.setAssignSelect("newOfficer");
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
                } else if (isAO) {
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
            if (rfiCount == 0) {
                nextStageList.add(new SelectOption("PROCRFI", "Request For Information"));
            }
        }

        //62875
        if (!isBeCessationFlow
                && (hasRollBackHistoryList && RoleConsts.USER_ROLE_AO3.equals(taskRole) && ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(applicationStatus))) {
            nextStageList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_PENDING_APPROVAL, "Approve"));
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
        ParamUtil.setSessionAttr(request, "finalStage", finalStage);
        ParamUtil.setRequestAttr(request, "hasRollBackHistoryList", hasRollBackHistoryList);
        ParamUtil.setSessionAttr(request, "nextStages", (Serializable) nextStageList);
        log.info(StringUtil.changeForLog("The setNormalProcessingDecisionDropdownValue end ... "));
    }

    public void setReplyProcessingDecisionDropdownValue(HttpServletRequest request, ApplicationViewDto applicationViewDto) {
        List<SelectOption> nextStageReplyList = IaisCommonUtils.genNewArrayList();
        if (!ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(applicationViewDto.getApplicationDto().getStatus())) {
            nextStageReplyList.add(new SelectOption("", "Please Select"));
        }
        nextStageReplyList.add(new SelectOption(ApplicationConsts.PROCESSING_DECISION_REPLY, "Give Clarification"));
        ParamUtil.setSessionAttr(request, "nextStageReply", (Serializable) nextStageReplyList);
    }

    public void setDmsProcessingDecisionDropdownValue(HttpServletRequest request) {
        List<SelectOption> decisionValues = IaisCommonUtils.genNewArrayList();
        decisionValues.add(new SelectOption("decisionApproval", "Approve"));
        decisionValues.add(new SelectOption("decisionReject", "Reject"));
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

    public void setRouteBackDropdownValue(HttpServletRequest request, ApplicationViewDto applicationViewDto) {
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
                if(user != null) {
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
        ParamUtil.setSessionAttr(request, "routeBackValues", (Serializable) rollBackStage);
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
            if (hcsaSvcRoutingStageDtoList.size() > 0) {
                flag = false;
            } else {
                flag = true;
            }
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
            }
        }
        if (ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS.equals(applicationViewDto.getApplicationDto().getStatus())) {
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

    //send email helper
    private String sendEmailHelper(Map<String, Object> tempMap, String msgTemplateId, String subject, String licenseeId, String clientQueryCode) {
        MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(msgTemplateId).getEntity();
        if (tempMap == null || tempMap.isEmpty() || msgTemplateDto == null
                || StringUtil.isEmpty(msgTemplateId)
                || StringUtil.isEmpty(subject)
                || StringUtil.isEmpty(licenseeId)
                || StringUtil.isEmpty(clientQueryCode)) {
            return null;
        }
        String mesContext = null;
        try {
            mesContext = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), tempMap);
        } catch (IOException | TemplateException e) {
            log.error(e.getMessage(), e);
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
