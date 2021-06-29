package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.api.config.GatewayConfig;
import com.ecquaria.cloud.moh.iais.api.config.GatewayConstants;
import com.ecquaria.cloud.moh.iais.api.config.GatewayStripeConfig;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSvcPersonAndExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGroupMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremEventPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationSubDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.OperationHoursReloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RenewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessHciDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppSpecifiedLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgGiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.NewApplicationConstant;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.dto.AppSelectSvcDto;
import com.ecquaria.cloud.moh.iais.dto.ApplicationValidateDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.PmtReturnUrlDto;
import com.ecquaria.cloud.moh.iais.dto.ServiceStepDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.rfcutil.EqRequestForChangeSubmitResultChange;
import com.ecquaria.cloud.moh.iais.rfcutil.PageDataCopyUtil;
import com.ecquaria.cloud.moh.iais.rfi.exc.RfiLoadingExc;
import com.ecquaria.cloud.moh.iais.rfi.impl.RfiLoadingCheckImplForRenew;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.CessationFeService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.StepColorService;
import com.ecquaria.cloud.moh.iais.service.WithOutRenewalService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FeMessageClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigFeClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.impl.ServiceInfoChangeEffectPersonForRFC;
import com.ecquaria.cloud.moh.iais.utils.DealSessionUtil;
import com.ecquaria.cloud.moh.iais.utils.SingeFileUtil;
import com.ecquaria.cloud.moh.iais.validate.declarationsValidate.DeclarationsUtil;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;
import sop.util.CopyUtil;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * egator
 *
 * @author suocheng
 * @date 9/23/2019
 */
@Delegator("newApplicationDelegator")
@Slf4j
public class NewApplicationDelegator {
    public static final String ERRORMAP_PREMISES = "errorMap_premises";
    public static final String PREMISESTYPE = "premisesType";
    public static final String HCSASERVICEDTO = "hcsaServiceDto";
    public static final String CURRENTSERVICEID = "currentServiceId";
    public static final String CURRENTSVCCODE = "currentSvcCode";
    public static final String APPSUBMISSIONDTO = "AppSubmissionDto";
    public static final String OLDAPPSUBMISSIONDTO = "oldAppSubmissionDto";
    public static final String COMMONHCSASVCDOCCONFIGDTO = "commonHcsaSvcDocConfigDto";
    public static final String PREMHCSASVCDOCCONFIGDTO = "premHcsaSvcDocConfigDto";
    public static final String RELOADAPPGRPPRIMARYDOCMAP = "reloadAppGrpPrimaryDocMap";
    public static final String APPGRPPRIMARYDOCERRMSGMAP = "appGrpPrimaryDocErrMsgMap";
    public static final String PRIMARY_DOC_CONFIG = "primaryDocConfig";
    public static final String SVC_DOC_CONFIG = "svcDocConfig";

    public static final String REQUESTINFORMATIONCONFIG = "requestInformationConfig";
    public static final String ACKSTATUS = "AckStatus";
    public static final String ACKMESSAGE = "AckMessage";
    public static final String SERVICEALLPSNCONFIGMAP = "ServiceAllPsnConfigMap";
    public static final String FIRESTOPTION = "Please Select";
    public static final String LICAPPGRPPREMISESDTOMAP = "LicAppGrpPremisesDtoMap";
    public static final String PERSONSELECTMAP = "PersonSelectMap";
    public static final String LICPERSONSELECTMAP = "LicPersonSelectMap";

    public static final String DRAFTCONFIG = "DraftConfig";
    private static final String GROUPLICENCECONFIG = "GroupLicenceConfig";
    private static final String RFI_REPLY_SVC_DTO = "rfiReplySvcDto";
    private static final String ASSESSMENTCONFIG = "AssessMentConfig";

    //page name
    public static final String APPLICATION_PAGE_NAME_LICENSEE = "APPPN00";
    public static final String APPLICATION_PAGE_NAME_PREMISES = "APPPN01";
    public static final String APPLICATION_PAGE_NAME_PRIMARY = "APPPN02";
    public static final String APPLICATION_SVC_PAGE_NAME_LABORATORY = "APPSPN01";
    public static final String APPLICATION_SVC_PAGE_NAME_GOVERNANCE_OFFICERS = "APPSPN02";
    public static final String APPLICATION_SVC_PAGE_NAME_DISCIPLINE_ALLOCATION = "APPSPN03";
    public static final String APPLICATION_SVC_PAGE_NAME_PRINCIPAL_OFFICERS = "APPSPN04";
    public static final String APPLICATION_SVC_PAGE_NAME_DEPUTY_PRINCIPAL_OFFICERS = "APPSPN05";
    public static final String APPLICATION_SVC_PAGE_NAME_DOCUMENT = "APPSPN06";
    public static final String APPLICATION_SVC_PAGE_NAME_SERVICE_PERSONNEL = "APPSPN07";
    public static final String APPLICATION_SVC_PAGE_NAME_MEDALERT_PERSON = "APPSPN08";
    public static final String SELECT_DRAFT_NO = "selectDraftNo";
    //isClickEdit
    public static final String IS_EDIT = "isEdit";

    public static final String LICENSEE_MAP = "LICENSEE_MAP";
    public static final String LICENSEE_OPTIONS = "LICENSEE_OPTIONS";

    public static final String CURR_ORG_USER_ACCOUNT = "currOrgUserAccount";
    @Autowired
    private ServiceConfigService serviceConfigService;
    @Autowired
    private HcsaConfigFeClient hcsaConfigFeClient;
    @Autowired
    private AppSubmissionService appSubmissionService;
    @Autowired
    private RequestForChangeService requestForChangeService;
    @Autowired
    private CessationFeService cessationFeService;
    @Autowired
    private RfiLoadingCheckImplForRenew rfiLoadingCheckImplForRenew;
    @Autowired
    private LicenceClient licenceClient;
    @Autowired
    private GenerateIdClient generateIdClient;
    @Autowired
    private CessationClient cessationClient;
    @Autowired
    private WithOutRenewalService withOutRenewalService;
    @Autowired
    private RfiLoadingExc  rfiLoadingExc;
    @Autowired
    private ApplicationFeClient applicationFeClient;
    @Autowired
    private EventBusHelper eventBusHelper;
    @Autowired
    private StepColorService stepColorService;
    @Autowired
    private FeMessageClient feMessageClient;
    @Autowired
    private ServiceInfoChangeEffectPersonForRFC serviceInfoChangeEffectPersonForRFC;
    @Autowired
    private FeEicGatewayClient feEicGatewayClient;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private DealSessionUtil dealSessionUtil;
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Value("${iais.email.sender}")
    private String mailSender;
    @Autowired
    private HcsaAppClient hcsaAppClient;
    @Value("${iais.current.domain}")
    private String currentDomain;
    @Autowired
    private Environment env;

    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.info(StringUtil.changeForLog("the do Start start ...."));
        HcsaServiceCacheHelper.flushServiceMapping();
        appSubmissionService.clearSession(bpc.request);
        //fro draft loading
        String draftNo = ParamUtil.getMaskedString(bpc.request, "DraftNumber");
        //for rfi loading
        String appNo = ParamUtil.getMaskedString(bpc.request, "appNo");
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_NEW_APPLICATION, AuditTrailConsts.FUNCTION_NEW_APPLICATION);
        // rfc or renew
        requestForChangeOrRenewLoading(bpc);
        //renewLicence(bpc);
        requestForInformationLoading(bpc, appNo);
        //for loading the draft by appId
        loadingDraft(bpc, draftNo);
        //load new application info
        loadingNewAppInfo(bpc);
        //for loading Service Config
        boolean flag = loadingServiceConfig(bpc);
        log.info(StringUtil.changeForLog("The loadingServiceConfig -->:" + flag));
        if (flag) {
            //init session and data reomve function to DealSessionUtil
            dealSessionUtil.initSession(bpc);
        }
        bpc.request.getSession().setAttribute("RFC_ERR004",MessageUtil.getMessageDesc("RFC_ERR004"));
        /*    initOldSession(bpc);*/
        log.info(StringUtil.changeForLog("the do Start end ...."));
    }

    /**
     * StartStep: Prepare
     *
     * @param bpc
     * @throws
     */
    public void prepare(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do prepare start ...."));
        //String action = ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        String action = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if (StringUtil.isEmpty(action)) {
            action = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
            if (StringUtil.isEmpty(action) || "validation".equals(action)) {
                //first
                action = "licensee";
            }
        }
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, action);
        log.info(StringUtil.changeForLog("the do prepare end ...."));
    }

    /**
     * Process: MohNewApplication
     * Step: PrepareSubLicensee
     *
     *  Prepare licensee detail
     *
     * @param bpc
     */
    public void prepareSubLicensee(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("..... Prepare Sub Licensee...."));
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<SubLicenseeDto> subLicenseeDtoList = licenceClient.getIndividualSubLicensees(loginContext.getOrgId()).getEntity();
        Map<String, SubLicenseeDto> licenseeMap = NewApplicationHelper.genSubLicessMap(subLicenseeDtoList);
        bpc.request.getSession().setAttribute(LICENSEE_MAP, licenseeMap);
        bpc.request.setAttribute(LICENSEE_OPTIONS, NewApplicationHelper.genSubLicessOption(subLicenseeDtoList));

        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        SubLicenseeDto subLicenseeDto = appSubmissionDto.getSubLicenseeDto();
        if (subLicenseeDto == null) {
            subLicenseeDto = new SubLicenseeDto();
            appSubmissionDto.setSubLicenseeDto(subLicenseeDto);
        }
        if (StringUtil.isEmpty(subLicenseeDto.getAssignSelect())) {
            subLicenseeDto.setAssignSelect(NewApplicationHelper.getAssignSelect(licenseeMap.keySet(),
                    subLicenseeDto.getIdType(), subLicenseeDto.getIdNumber()));
        }
        if (StringUtil.isEmpty(subLicenseeDto.getUenNo())) {
            subLicenseeDto.setUenNo(loginContext.getUenNo());
        }
        bpc.request.setAttribute("subLicenseeDto", appSubmissionService.getLicenseeById(loginContext.getLicenseeId(), loginContext.getUenNo()));
    }

    /**
     * Process: MohNewApplication
     * Step: PrepareSubLicensee
     *
     *  Do licensee detail
     *
     * @param bpc
     */
    public void doSubLicensee(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("------doSubLicensee-------"));
        String action = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        if ("back".equals(action) || RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "jump");
            return;
        }
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String isEdit = ParamUtil.getString(bpc.request, IS_EDIT);
        boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
        boolean isGetDataFromPage = NewApplicationHelper.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_PREMISES_INFORMATION, isEdit, isRfi);
        log.info(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if (isGetDataFromPage) {
            SubLicenseeDto subLicenseeDto = getSubLicenseeDtoFromPage(bpc.request);
            appSubmissionDto.setSubLicenseeDto(subLicenseeDto);
            if (appSubmissionDto.isNeedEditController()) {
                Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
                clickEditPages.add(APPLICATION_PAGE_NAME_LICENSEE);
                appSubmissionDto.setClickEditPage(clickEditPages);
                AppEditSelectDto appEditSelectDto = appSubmissionDto.getChangeSelectDto();
                appEditSelectDto.setLicenseeEdit(true);
            }
            appSubmissionService.validateSubLicenseeDto(errorMap, subLicenseeDto, bpc.request);
        }

        String actionAdditional = ParamUtil.getString(bpc.request, "crud_action_additional");
//        if (!"saveDraft".equals(action)) {
//
//        }
        //errorMap = requestForChangeService.doValidatePremiss(appSubmissionDto, oldAppSubmissionDto, premisesHciList, keywords, isRfi);
//        String crud_action_type_continue = bpc.request.getParameter("crud_action_type_continue");
//        String crud_action_type = bpc.request.getParameter("crud_action_type");
//        bpc.request.setAttribute("continueStep", crud_action_type);
//        bpc.request.setAttribute("crudActionTypeContinue", crud_action_additional);
//        if ("continue".equals(crud_action_type_continue)) {
//            errorMap.remove("hciNameUsed");
//        }
//        String string = errorMap.get("hciNameUsed");
//        if (string != null) {
//            bpc.request.setAttribute("hciNameUsed", "hciNameUsed");
//        }
        if (errorMap.size() > 0) {
            //set audit
            NewApplicationHelper.setAudiErrMap(isRfi, appSubmissionDto.getAppType(), errorMap, appSubmissionDto.getRfiAppNo(),
                    appSubmissionDto.getLicenceNo());
//            String hciNameUsed = errorMap.get("hciNameUsed");
//            if (!StringUtil.isEmpty(hciNameUsed)) {
//                ParamUtil.setRequestAttr(bpc.request, "newAppPopUpMsg", hciNameUsed);
//            }
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "licensee");
            bpc.request.setAttribute("errormapIs", "error");
            HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
            coMap.put("licensee", "");
            //coMap.put("serviceConfig", sB.toString());
            bpc.request.getSession().setAttribute("coMap", coMap);
        } else {
            HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
            coMap.put("licensee", "licensee");
            //coMap.put("serviceConfig", sB.toString());
            bpc.request.getSession().setAttribute("coMap", coMap);
            if ("rfcSaveDraft".equals(actionAdditional)) {
                try {
                    doSaveDraft(bpc);
                } catch (IOException e) {
                    log.error("error", e);
                }
            }
        }
    }

    private SubLicenseeDto getSubLicenseeDtoFromPage(HttpServletRequest request) {
        String assignSelect = ParamUtil.getString(request, "assignSelect");
        String licenseeType = ParamUtil.getString(request, "licenseeType");
        SubLicenseeDto dto = null;
        // Check licensee type
        if (OrganizationConstants.LICENSEE_SUB_TYPE_COMPANY.equals(licenseeType)) {
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
            dto = appSubmissionService.getLicenseeById(loginContext.getLicenseeId(), loginContext.getUenNo());
            if (dto == null) {
                dto = new SubLicenseeDto();
                dto.setAssignSelect(assignSelect);
                dto.setLicenseeType(licenseeType);
            }
        } else {
            String idType = ParamUtil.getString(request, "idType");
            String idNumber = ParamUtil.getString(request, "idNumber");
            String licenseeName = ParamUtil.getString(request, "licenseeName");
            String postalCode = ParamUtil.getString(request, "postalCode");
            String addrType = ParamUtil.getString(request, "addrType");
            String blkNo = ParamUtil.getString(request, "blkNo");
            String floorNo = ParamUtil.getString(request, "floorNo");
            String unitNo = ParamUtil.getString(request, "unitNo");
            String streetName = ParamUtil.getString(request, "streetName");
            String buildingName = ParamUtil.getString(request, "buildingName");
            String telephoneNo = ParamUtil.getString(request, "telephoneNo");
            String emailAddr = ParamUtil.getString(request, "emailAddr");

            dto = new SubLicenseeDto();
            dto.setAssignSelect(assignSelect);
            dto.setLicenseeType(licenseeType);
            dto.setIdType(idType);
            dto.setIdNumber(idNumber);
            dto.setLicenseeName(licenseeName);
            dto.setPostalCode(postalCode);
            dto.setAddrType(addrType);
            dto.setBlkNo(blkNo);
            dto.setFloorNo(floorNo);
            dto.setUnitNo(unitNo);
            dto.setStreetName(streetName);
            dto.setBuildingName(buildingName);
            dto.setTelephoneNo(telephoneNo);
            dto.setEmailAddr(emailAddr);
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
            if (loginContext != null) {
                dto.setOrgId(loginContext.getOrgId());
                dto.setUenNo(loginContext.getUenNo());
            }
        }
        return dto;
    }

    /**
     * StartStep: PreparePremises
     *
     * @param bpc
     * @throws
     */
    public void preparePremises(BaseProcessClass bpc) {
        Object attribute1 = bpc.request.getAttribute(RfcConst.SWITCH);
        log.info(StringUtil.changeForLog("the do preparePremises start ...."));
        NewApplicationHelper.setTimeList(bpc.request);
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        //get svcCode to get svcId
        List<HcsaServiceDto> hcsaServiceDtoList = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST);
        List<HcsaServiceDto> rfiHcsaService = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request, "rfiHcsaService");
        List<String> svcIds = IaisCommonUtils.genNewArrayList();
        if (rfiHcsaService != null) {
            rfiHcsaService.forEach(v -> svcIds.add(v.getId()));
        } else {
            if (hcsaServiceDtoList != null) {
                hcsaServiceDtoList.forEach(item -> svcIds.add(item.getId()));
            }
        }

        String licenseeId = appSubmissionDto.getLicenseeId();
        log.info(StringUtil.changeForLog("The preparePremises licenseeId is -->:" + licenseeId));
        //PremisesKey,
        Map<String, AppGrpPremisesDto> licAppGrpPremisesDtoMap = null;
        if (!StringUtil.isEmpty(licenseeId)) {
            licAppGrpPremisesDtoMap = serviceConfigService.getAppGrpPremisesDtoByLoginId(licenseeId);
            log.info("----------- licAppGrpPremisesDtoMap ----->"+ licAppGrpPremisesDtoMap.size());
            String appType = appSubmissionDto.getAppType();
            if (licAppGrpPremisesDtoMap != null) {
                //remove premise info when pending premises hci same
//                List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST);
                List<String> pendAndLicPremHci = appSubmissionService.getHciFromPendAppAndLic(licenseeId, hcsaServiceDtoList);
                if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {

                } else if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)) {
                    List<String> currPremHci = IaisCommonUtils.genNewArrayList();
                    AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, OLDAPPSUBMISSIONDTO);
                    if (oldAppSubmissionDto != null) {
                        List<AppGrpPremisesDto> appGrpPremisesDtos = oldAppSubmissionDto.getAppGrpPremisesDtoList();
                        if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
                            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                                currPremHci.add(NewApplicationHelper.getPremHci(appGrpPremisesDto));
                            }
                        }
                    }
                    pendAndLicPremHci.removeAll(currPremHci);
                }
                Map<String, AppGrpPremisesDto> newLicAppGrpPremisesDtoMap = IaisCommonUtils.genNewHashMap();
                licAppGrpPremisesDtoMap.forEach((k, v) -> {
                    List<String> premisesHciList = NewApplicationHelper.genPremisesHciList(v);
                    boolean pendPremOrExistLic = false;
                    for(String premisesHci:premisesHciList){
                        if(pendAndLicPremHci.contains(premisesHci)){
                            pendPremOrExistLic = true;
                           break;
                        }
                    }
                    if(!pendPremOrExistLic){
                        newLicAppGrpPremisesDtoMap.put(k, v);
                    }
                });
                //
                if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)) {
                    List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
                    if(!IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
                        for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                            // rfc for rfi (choose move  a new address will join this address (if nothing this dropdown is empty))
                            String premisesSelect = appGrpPremisesDto.getPremisesSelect();
                            if("newPremise".equals(premisesSelect)){
                                continue;
                            }
                            newLicAppGrpPremisesDtoMap.put(appGrpPremisesDto.getPremisesSelect(),appGrpPremisesDto);
                        }
                    }
                }
                licAppGrpPremisesDtoMap = newLicAppGrpPremisesDtoMap;
                log.info("-------newLicAppGrpPremisesDtoMap----> "+newLicAppGrpPremisesDtoMap.size());
            }
        } else {
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
            licenseeId = loginContext.getLicenseeId();
        }

        //premise select
        NewApplicationHelper.setPremSelect(bpc.request, licAppGrpPremisesDtoMap);
        ParamUtil.setSessionAttr(bpc.request, LICAPPGRPPREMISESDTOMAP, (Serializable) licAppGrpPremisesDtoMap);
        //addressType
        //NewApplicationHelper.setPremAddressSelect(bpc.request);

        //get premises type
        if (!IaisCommonUtils.isEmpty(svcIds)) {
            log.info(StringUtil.changeForLog("svcId not null"));
            log.debug(StringUtil.changeForLog("svc id List :"+JsonUtil.parseToJson(svcIds)));
            Set<String> premisesType = serviceConfigService.getAppGrpPremisesTypeBySvcId(svcIds);
            boolean readOnlyPrem = false;
            if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
                List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
                for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                    if (!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo()) || !StringUtil.isEmpty(appSvcRelatedInfoDto.getAlignLicenceNo())) {
                        readOnlyPrem = true;
                        break;
                    }
                }
                if (readOnlyPrem) {
                    premisesType = IaisCommonUtils.genNewHashSet();
                    AppGrpPremisesDto appGrpPremisesDto = appSubmissionDto.getAppGrpPremisesDtoList().get(0);
                    premisesType.add(appGrpPremisesDto.getPremisesType());
                }
            }

            ParamUtil.setSessionAttr(bpc.request, PREMISESTYPE, (Serializable) premisesType);
        } else {
            log.debug(StringUtil.changeForLog("do not have select the services"));
        }

        //addressType
        /*List<SelectOption> addrTypeOpt = new ArrayList<>();
        SelectOption addrTypeSp = new SelectOption("", FIRESTOPTION);
        addrTypeOpt.add(addrTypeSp);
        addrTypeOpt.addAll(MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ADDRESS_TYPE));
        ParamUtil.setRequestAttr(bpc.request, "addressType", addrTypeOpt);*/
        //reload dateTime
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        if (!IaisCommonUtils.isEmpty(appGrpPremisesDtoList)) {
            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                appGrpPremisesDto = NewApplicationHelper.setWrkTime(appGrpPremisesDto);
            }
        }
        appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtoList);
        //
        int baseSvcCount = 0;
        if (hcsaServiceDtoList != null) {
            for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtoList) {
                if (ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equalsIgnoreCase(hcsaServiceDto.getSvcType())) {
                    baseSvcCount++;
                }
            }
        }
        if (baseSvcCount > 1) {
            ParamUtil.setRequestAttr(bpc.request, "multiBase", AppConsts.TRUE);
        } else {
            ParamUtil.setRequestAttr(bpc.request, "multiBase", AppConsts.FALSE);
        }
        //when rfc/renew check is select existing premises
        String appType = appSubmissionDto.getAppType();
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)) {
            AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, OLDAPPSUBMISSIONDTO);
            if (appSubmissionDto.getAppGrpPremisesDtoList().size() == oldAppSubmissionDto.getAppGrpPremisesDtoList().size()) {
                int length = appSubmissionDto.getAppGrpPremisesDtoList().size();
                for (int i = 0; i < length; i++) {
                    AppGrpPremisesDto appGrpPremisesDto = appSubmissionDto.getAppGrpPremisesDtoList().get(i);
                    AppGrpPremisesDto oldAppGrpPremisesDto = oldAppSubmissionDto.getAppGrpPremisesDtoList().get(i);
                    if (appGrpPremisesDto != null && oldAppGrpPremisesDto != null) {
                        String premSel = appGrpPremisesDto.getPremisesSelect();
                        String oldPremSel = oldAppGrpPremisesDto.getPremisesSelect();
                        if (oldPremSel.equals(premSel) || "-1".equals(premSel)) {
                            ParamUtil.setRequestAttr(bpc.request, "PageCanEdit", AppConsts.TRUE);
                        }
                    }
                }
            }

            List<AppGrpPremisesDto> appGrpPremisesDtoList1 = appSubmissionDto.getAppGrpPremisesDtoList();
            String licenceNo = appSubmissionDto.getLicenceNo();
            for (int i = 0; i < appGrpPremisesDtoList1.size(); i++) {
                String hciCode = appGrpPremisesDtoList1.get(i).getHciCode();
                String oldHciCode = appGrpPremisesDtoList1.get(i).getOldHciCode();
                if(!StringUtil.isEmpty(oldHciCode)&&!oldHciCode.equals(hciCode)){
                    hciCode=oldHciCode;
                }
                List<LicenceDto> licenceDtoByHciCode = requestForChangeService.getLicenceDtoByHciCode(hciCode, licenseeId);
                for (LicenceDto licenceDto : licenceDtoByHciCode) {
                    if (licenceDto.getLicenceNo().equals(licenceNo)) {
                        licenceDtoByHciCode.remove(licenceDto);
                        break;
                    }
                }
                appGrpPremisesDtoList1.get(i).setLicenceDtos(licenceDtoByHciCode);
                bpc.request.getSession().setAttribute("selectLicence" + i, licenceDtoByHciCode);
            }
        }
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        List<SelectOption> weeklyOpList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DAY_NAMES);
        ParamUtil.setRequestAttr(bpc.request,"weeklyOpList",weeklyOpList);
        List<SelectOption> phOpList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_PUBLIC_HOLIDAY);
        ParamUtil.setRequestAttr(bpc.request,"phOpList",phOpList);
        ParamUtil.setRequestAttr(bpc.request,"weeklyCount",systemParamConfig.getWeeklyCount());
        ParamUtil.setRequestAttr(bpc.request,"phCount",systemParamConfig.getPhCount());
        ParamUtil.setRequestAttr(bpc.request,"eventCount",systemParamConfig.getEventCount());
        ParamUtil.setRequestAttr(bpc.request,"postalCodeAckMsg",MessageUtil.getMessageDesc("NEW_ACK016"));
        //single premises service
        ParamUtil.setRequestAttr(bpc.request,"isMultiPremService",NewApplicationHelper.isMultiPremService(hcsaServiceDtoList));
        log.info(StringUtil.changeForLog("the do preparePremises end ...."));
        //
        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())||
                ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())){
            AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, OLDAPPSUBMISSIONDTO);
            //now no group licence
            if(oldAppSubmissionDto!=null){
                List<AppGrpPremisesDto> oldAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
                if(appGrpPremisesDtoList!=null&&!appGrpPremisesDtoList.isEmpty()&&oldAppGrpPremisesDtoList!=null&&!oldAppGrpPremisesDtoList.isEmpty()){
                    Object attribute = bpc.request.getSession().getAttribute(REQUESTINFORMATIONCONFIG);
                    if(attribute!=null){
                        for(AppGrpPremisesDto v : appGrpPremisesDtoList){
                            String hciCode = v.getHciCode();
                            String oldHciCode = v.getOldHciCode();
                            if(hciCode!=null&&oldHciCode!=null){
                                boolean equals = hciCode.equals(oldHciCode);
                                v.setExistingData(AppConsts.NO);
                                bpc.request.getSession().setAttribute("rfc_eqHciCode",String.valueOf(equals));
                            }else if(hciCode==null){
                                v.setExistingData(AppConsts.NO);
                                bpc.request.getSession().setAttribute("rfc_eqHciCode","true");
                            }
                        }
                    }else {
                        boolean eqHciCode = EqRequestForChangeSubmitResultChange.eqHciCode(appGrpPremisesDtoList.get(0), oldAppGrpPremisesDtoList.get(0));

                        appGrpPremisesDtoList.get(0).setExistingData(AppConsts.NO);
                        bpc.request.getSession().setAttribute("rfc_eqHciCode",String.valueOf(eqHciCode));
                    }

                }
            }
        }
    }

    /**
     * StartStep: PrepareDocuments
     *
     * @param bpc
     * @throws
     * @throwsdo
     */
    public void prepareDocuments(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do prepareDocuments start ...."));
        Object attribute1 = bpc.request.getAttribute(RfcConst.SWITCH);
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        List<HcsaSvcDocConfigDto> hcsaSvcDocDtos;
        boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
        AppSubmissionDto oldAppSubDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,OLDAPPSUBMISSIONDTO);
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = IaisCommonUtils.genNewArrayList();
        if(oldAppSubDto != null){
            appGrpPrimaryDocDtos = oldAppSubDto.getAppGrpPrimaryDocDtos();
        }
        if(isRfi && appGrpPrimaryDocDtos != null && appGrpPrimaryDocDtos.size() > 0){
            hcsaSvcDocDtos = serviceConfigService.getPrimaryDocConfigById(appGrpPrimaryDocDtos.get(0).getSvcComDocId());
        }else{
            hcsaSvcDocDtos = serviceConfigService.getAllHcsaSvcDocs(null);
        }
        ParamUtil.setSessionAttr(bpc.request,PRIMARY_DOC_CONFIG, (Serializable) hcsaSvcDocDtos);

        //reload page
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = appSubmissionDto.getAppGrpPrimaryDocDtos();
        Map<String, List<AppGrpPrimaryDocDto>> reloadDocMap = IaisCommonUtils.genNewHashMap();
        if(!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtoList)){
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDocDtoList){
                if(StringUtil.isEmpty(appGrpPrimaryDocDto.getFileRepoId()) && StringUtil.isEmpty(appGrpPrimaryDocDto.getDocName())){
                    continue;
                }
                String reloadDocMapKey;
                if(StringUtil.isEmpty(appGrpPrimaryDocDto.getPremisessName())){
                    reloadDocMapKey = appGrpPrimaryDocDto.getSvcComDocId();
                }else{
                    reloadDocMapKey = appGrpPrimaryDocDto.getPremisessName() + appGrpPrimaryDocDto.getSvcComDocId();
                }

                List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos1 = reloadDocMap.get(reloadDocMapKey);
                if(IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos1)){
                    appGrpPrimaryDocDtos1 = IaisCommonUtils.genNewArrayList();
                }
                appGrpPrimaryDocDtos1.add(appGrpPrimaryDocDto);
                reloadDocMap.put(reloadDocMapKey,appGrpPrimaryDocDtos1);
            }
            //do sort

            reloadDocMap.forEach((k,v)->{
                Collections.sort(v,(s1,s2)->(
                    s1.getSeqNum().compareTo(s2.getSeqNum())
                    ));
            });
        }
        ParamUtil.setSessionAttr(bpc.request,"docReloadMap", (Serializable) reloadDocMap);

        int sysFileSize = systemParamConfig.getUploadFileLimit();
        ParamUtil.setRequestAttr(bpc.request, "sysFileSize", sysFileSize);
        String sysFileType = systemParamConfig.getUploadFileType();
        String[] sysFileTypeArr = FileUtils.fileTypeToArray(sysFileType);
        StringBuilder fileTypeStr = new StringBuilder();
        if (sysFileTypeArr != null) {
            int i = 0;
            int fileTypeLength = sysFileTypeArr.length;
            for (String fileType : sysFileTypeArr) {
                fileTypeStr.append(' ').append(fileType);
                if (fileTypeLength > 1 && i < fileTypeLength - 2) {
                    fileTypeStr.append(',');
                }
                if (fileTypeLength > 1 && i == sysFileTypeArr.length - 2) {
                    fileTypeStr.append(" and");
                }
                if (i == fileTypeLength - 1) {
                    fileTypeStr.append('.');
                }
                i++;
            }
        }
        ParamUtil.setRequestAttr(bpc.request, "sysFileType", fileTypeStr.toString());
        log.info(StringUtil.changeForLog("the do prepareDocuments end ...."));
    }

    /**
     * StartStep: PrepareForms
     *
     * @param bpc
     * @throws
     */
    public void prepareForms(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do prepareForms start ...."));
        Object attribute = bpc.request.getAttribute(RfcConst.SWITCH);
        log.info(StringUtil.changeForLog("the do prepareForms end ...."));
    }

    /**
     * StartStep: PreparePreview
     *
     * @param bpc
     * @throws
     */
    public void preparePreview(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do preparePreview start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST);
        //todo:wait task complete remove this
        boolean ableGrpLic = true;
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                if (!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo()) || !StringUtil.isEmpty(appSvcRelatedInfoDto.getAlignLicenceNo())) {
                    ableGrpLic = false;
                    break;
                }
            }
        }

        if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos) && !IaisCommonUtils.isEmpty(hcsaServiceDtos) && ableGrpLic) {
            int premCount = appGrpPremisesDtos.size();
            int svcCount = hcsaServiceDtos.size();
            log.info(StringUtil.changeForLog("premises count:" + premCount + " ,service count:" + svcCount));
            if (premCount > 1 && svcCount >= 1) {
                //multi prem one svc
                ParamUtil.setRequestAttr(bpc.request, GROUPLICENCECONFIG, "test");
            }
        }
        boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
        List<HcsaSvcDocConfigDto> primaryDocConfig = (List<HcsaSvcDocConfigDto>) ParamUtil.getSessionAttr(bpc.request,PRIMARY_DOC_CONFIG);
        if(IaisCommonUtils.isEmpty(primaryDocConfig)){
            if(isRfi && appGrpPrimaryDocDtos != null && appGrpPrimaryDocDtos.size() > 0){
                primaryDocConfig = serviceConfigService.getPrimaryDocConfigById(appGrpPrimaryDocDtos.get(0).getSvcComDocId());
            }else{
                primaryDocConfig = serviceConfigService.getAllHcsaSvcDocs(null);
            }
            ParamUtil.setSessionAttr(bpc.request,PRIMARY_DOC_CONFIG, (Serializable) primaryDocConfig);
        }

        Map<String,List<AppGrpPrimaryDocDto>> reloadPrimaryDocMap = NewApplicationHelper.genPrimaryDocReloadMap(primaryDocConfig,appSubmissionDto.getAppGrpPremisesDtoList(),appGrpPrimaryDocDtos);
        /*if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())){
            reloadPrimaryDocMap.forEach((k,v)->{
                if(v != null && v.size() > 1){
                    Collections.sort(v,(s1,s2)->s1.getSeqNum().compareTo(s2.getSeqNum()));
                }
            });
        }*/
        appSubmissionDto.setMultipleGrpPrimaryDoc(reloadPrimaryDocMap);
        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())){
            if(!NewApplicationHelper.checkIsRfi(bpc.request)){
                AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.OLDAPPSUBMISSIONDTO);
                List<AppGrpPremisesDto> oldAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
                List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
                if(oldAppGrpPremisesDtoList!=null&& appGrpPremisesDtoList!=null){
                    for (int i = 0; i < appGrpPremisesDtoList.size(); i++) {
                        boolean eqHciNameChange = EqRequestForChangeSubmitResultChange.eqHciNameChange(appGrpPremisesDtoList.get(i), oldAppGrpPremisesDtoList.get(i));
                        if(eqHciNameChange){
                            bpc.request.setAttribute("RFC_eqHciNameChange","RFC_eqHciNameChange");
                        }
                    }
                }
            }else {
                AppDeclarationMessageDto appDeclarationMessageDto = appSubmissionDto.getAppDeclarationMessageDto();
                if(appSubmissionDto.getAppGrpNo()!=null&&appSubmissionDto.getAppGrpNo().startsWith("AR")){
                    if(appDeclarationMessageDto!=null){
                        RenewDto renewDto=new RenewDto();
                        List<AppSubmissionDto> appSubmissionDtos=new ArrayList<>(1);
                        AppSubmissionDto renewAppsub=new AppSubmissionDto();
                        renewAppsub.setAppDeclarationMessageDto(appDeclarationMessageDto);
                        renewAppsub.setAppDeclarationDocDtos(appSubmissionDto.getAppDeclarationDocDtos());
                        renewAppsub.setAppType(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
                        appSubmissionDtos.add(renewAppsub);
                        renewDto.setAppSubmissionDtos(appSubmissionDtos);
                        bpc.request.setAttribute("renewDto",renewDto);
                        bpc.request.setAttribute("renew_rfc_show","Y");
                    }
                }else {
                    if(appDeclarationMessageDto!=null){
                        bpc.request.setAttribute("RFC_eqHciNameChange","RFC_eqHciNameChange");
                    }
                }
            }
        }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())){
            if(NewApplicationHelper.checkIsRfi(bpc.request)){
                AppDeclarationMessageDto appDeclarationMessageDto = appSubmissionDto.getAppDeclarationMessageDto();
                if(appDeclarationMessageDto!=null){
                    RenewDto renewDto=new RenewDto();
                    List<AppSubmissionDto> list=new ArrayList<>(1);
                    list.add(appSubmissionDto);
                    renewDto.setAppSubmissionDtos(list);
                    bpc.request.setAttribute("renewDto",renewDto);
                }
            }
        }

        // init uploaded File
        appSubmissionService.initDeclarationFiles(appSubmissionDto.getAppDeclarationDocDtos(), appSubmissionDto.getAppType(), bpc.request);
        if (NewApplicationHelper.checkIsRfi(bpc.request)) {
            ParamUtil.setSessionAttr(bpc.request, "viewPrint", "Y");
        } else {
            ParamUtil.setSessionAttr(bpc.request, "viewPrint", null);
        }
        ParamUtil.setSessionAttr(bpc.request,APPSUBMISSIONDTO,appSubmissionDto);

        ParamUtil.setRequestAttr(bpc.request,"isCharity",NewApplicationHelper.isCharity(bpc.request));

        log.info(StringUtil.changeForLog("the do preparePreview end ...."));
    }


    /**
     * StartStep: PreparePayment
     *
     * @param bpc
     * @throws
     */
    public void preparePayment(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do preparePayment start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        List<AppSubmissionDto> appSubmissionDtos = (List<AppSubmissionDto>) bpc.request.getSession().getAttribute("appSubmissionDtos");
        HashMap<String, String> coMap  = bpc.request.getSession().getAttribute("coMap")==null ?
                null : (HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");

        String paymentMethod ;

        String serviceConfig = (String) bpc.request.getSession().getAttribute("serviceConfig");

        stepColorService.setStepColor(coMap,serviceConfig,appSubmissionDto);

        //get transfer info
        AppSubmissionDto tranferSub = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, "app-rfc-tranfer");
        if (tranferSub != null) {
            if (appSubmissionDtos == null) {
                appSubmissionDtos = new ArrayList<>(1);
                List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = tranferSub.getAppSvcRelatedInfoDtoList();
                if (appSvcRelatedInfoDtoList != null) {
                    for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList) {
                        appSvcRelatedInfoDto.setGroupNo(tranferSub.getAppGrpNo());
                    }
                }
                appSubmissionDtos.add(tranferSub);
            }
            String transferFlag = appSubmissionDto.getTransferFlag();
            appSubmissionDto = tranferSub;
            appSubmissionDto.setTransferFlag(transferFlag);
            //reload transfer payment method
            paymentMethod = tranferSub.getPaymentMethod();
        }else{
            //reload new/rfc payment method
            paymentMethod = appSubmissionDto.getPaymentMethod();
        }
        Double total = 0.0;
        if (!ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType()) && !ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
            total += appSubmissionDto.getAmount();
        }
        if (appSubmissionDtos != null && !appSubmissionDtos.isEmpty()) {
            for (AppSubmissionDto appSubmissionDto1 : appSubmissionDtos) {
                Double amount = appSubmissionDto1.getAmount();
                total = total + amount;
                String amountStr = Formatter.formatterMoney(appSubmissionDto1.getAmount());
                appSubmissionDto1.setAmountStr(amountStr);
                appSubmissionDto1.setServiceName(appSubmissionDto1.getAppSvcRelatedInfoDtoList().get(0).getServiceName());
            }

        }
        appSubmissionDto.setAmount(total);
        if (!StringUtil.isEmpty(appSubmissionDto.getAmount())) {
            String amountStr = Formatter.formatterMoney(appSubmissionDto.getAmount());
            log.info(StringUtil.changeForLog("The amountStr is -->:" + amountStr));
            appSubmissionDto.setAmountStr(amountStr);
        }
        if (appSubmissionDtos != null) {
            bpc.request.getSession().setAttribute("appSubmissionDtos", appSubmissionDtos);
        }
        if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
            Set<String> premTypes = IaisCommonUtils.genNewHashSet();
            List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
            if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
                for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                    premTypes.add(appGrpPremisesDto.getPremisesType());
                }
                if (premTypes.size() == 1 && premTypes.contains(ApplicationConsts.PREMISES_TYPE_OFF_SITE)) {
                    boolean flag = true;
                    ParamUtil.setRequestAttr(bpc.request, "onlyOffsite", flag);
                }
            }
        }
        String flag = bpc.request.getParameter("flag");
        if(!StringUtil.isEmpty(flag)){
            appSubmissionDto.setTransferFlag(flag);
        }
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        bpc.request.setAttribute("flag",appSubmissionDto.getTransferFlag());
        bpc.request.setAttribute("transfer",appSubmissionDto.getTransferFlag());
        ParamUtil.setRequestAttr(bpc.request,"IsCharity",NewApplicationHelper.isCharity(bpc.request));
        boolean isGiroAcc = false;
        List<OrgGiroAccountInfoDto> orgGiroAccountInfoDtos = appSubmissionService.getOrgGiroAccDtosByLicenseeId(NewApplicationHelper.getLicenseeId(bpc.request));
        if(!IaisCommonUtils.isEmpty(orgGiroAccountInfoDtos)){
            isGiroAcc = true;
            List<SelectOption> giroAccSel = NewApplicationHelper.genGiroAccSel(orgGiroAccountInfoDtos);
            ParamUtil.setRequestAttr(bpc.request, "giroAccSel", giroAccSel);
        }
        ParamUtil.setRequestAttr(bpc.request,"IsGiroAcc",isGiroAcc);
        ParamUtil.setRequestAttr(bpc.request,NewApplicationConstant.ATTR_RELOAD_PAYMENT_METHOD,paymentMethod);
        log.info(StringUtil.changeForLog("the do preparePayment end ...."));
    }

    /**
     * StartStep: DoPremises
     *
     * @param bpc
     * @throws
     */
    public void doPremises(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do doPremises start ...."));

        //gen dto
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);

//        String action = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);

//        if ("back".equals(action) || RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)) {
//            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "jump");
//            return;
//        }

        String isEdit = ParamUtil.getString(bpc.request, IS_EDIT);
        boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
        boolean isGetDataFromPage = NewApplicationHelper.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_PREMISES_INFORMATION, isEdit, isRfi);
        log.info(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
//        if(isGetDataFromPage && !appSubmissionDto.isOnlySpecifiedSvc() ){
        if (isGetDataFromPage) {
            List<AppGrpPremisesDto> oldAppGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            List<AppGrpPremisesDto> appGrpPremisesDtoList = genAppGrpPremisesDtoList(bpc.request);
            if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())
                    ||ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())){
                for(int i=0 ;i < oldAppGrpPremisesDtoList.size();i++){
                    appGrpPremisesDtoList.get(i).setOldHciCode(oldAppGrpPremisesDtoList.get(i).getOldHciCode());
                }
            }

            appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtoList);
            if (appSubmissionDto.isNeedEditController()) {
                Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
                clickEditPages.add(APPLICATION_PAGE_NAME_PREMISES);
                appSubmissionDto.setClickEditPage(clickEditPages);
                AppEditSelectDto appEditSelectDto = appSubmissionDto.getChangeSelectDto();
                appEditSelectDto.setPremisesEdit(true);
                appSubmissionDto.setChangeSelectDto(appEditSelectDto);
            }
            if(!isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())){
                //65718
                NewApplicationHelper.removePremiseEmptyAlignInfo(appSubmissionDto);
                //ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
            }
            //update address
            NewApplicationHelper.updatePremisesAddress(appSubmissionDto);
            ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        }
        String crud_action_value = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        String crud_action_additional = ParamUtil.getString(bpc.request, "crud_action_additional");
        if (!"saveDraft".equals(crud_action_value)) {
            String keywords = MasterCodeUtil.getCodeDesc("MS001");

            Map<String, List<HcsaSvcPersonnelDto>> allSvcAllPsnConfig = appSubmissionService.getAllSvcAllPsnConfig(bpc.request);
            List<AppSvcRelatedInfoDto> dto = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            StringBuilder sB = new StringBuilder(10);

            for (int i = 0; i < dto.size(); i++) {
                String serviceId = dto.get(i).getServiceId();
                List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = serviceConfigService.getHcsaServiceStepSchemesByServiceId(serviceId);
                ServiceStepDto serviceStepDto = new ServiceStepDto();
                serviceStepDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemeDtos);
                List<HcsaSvcPersonnelDto> currentSvcAllPsnConfig = serviceConfigService.getSvcAllPsnConfig(hcsaServiceStepSchemeDtos, serviceId);
                appSubmissionService.doCheckBox(bpc, sB, allSvcAllPsnConfig, currentSvcAllPsnConfig, dto.get(i),systemParamConfig.getUploadFileLimit(),systemParamConfig.getUploadFileType(),appSubmissionDto.getAppGrpPremisesDtoList());
            }
            bpc.request.getSession().setAttribute("serviceConfig", sB.toString());
            List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST);
            List<String> premisesHciList = appSubmissionService.getHciFromPendAppAndLic(appSubmissionDto.getLicenseeId(), hcsaServiceDtos);
            ParamUtil.setSessionAttr(bpc.request, NewApplicationConstant.PREMISES_HCI_LIST, (Serializable) premisesHciList);
            AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.OLDAPPSUBMISSIONDTO);

            String actionType = bpc.request.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
            Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
            // not click back button
            if (!"licensee".equals(actionType)) {
                errorMap = requestForChangeService.doValidatePremiss(appSubmissionDto, oldAppSubmissionDto, premisesHciList, keywords, isRfi);
            }
            String crud_action_type_continue = bpc.request.getParameter("crud_action_type_continue");
            bpc.request.setAttribute("continueStep", actionType);
            bpc.request.setAttribute("crudActionTypeContinue", crud_action_additional);
            if ("continue".equals(crud_action_type_continue)) {
                errorMap.remove("hciNameUsed");
            }
            String string = errorMap.get("hciNameUsed");
            if (string != null) {
                bpc.request.setAttribute("hciNameUsed", "hciNameUsed");
            }
            if (errorMap.size() > 0) {
                //set audit
                NewApplicationHelper.setAudiErrMap(isRfi,appSubmissionDto.getAppType(),errorMap,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
                String hciNameUsed = errorMap.get("hciNameUsed");
                if (!StringUtil.isEmpty(hciNameUsed)) {
                    ParamUtil.setRequestAttr(bpc.request, "newAppPopUpMsg", hciNameUsed);
                }
                ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "premises");
                bpc.request.setAttribute("errormapIs", "error");
                HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
                coMap.put("premises", "");
                coMap.put("serviceConfig", sB.toString());
                bpc.request.getSession().setAttribute("coMap", coMap);
            } else {
                HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
                coMap.put("premises", "premises");
                coMap.put("serviceConfig", sB.toString());
                bpc.request.getSession().setAttribute("coMap", coMap);
                if ("rfcSaveDraft".equals(crud_action_additional)) {
                    try {
                        doSaveDraft(bpc);
                    } catch (IOException e) {
                        log.error("error", e);
                    }
                }
            }
        }
        log.info(StringUtil.changeForLog("the do doPremises end ...."));
    }

    /**
     * StartStep: DoDocument
     *
     * @param bpc
     * @throws
     */
    public void doDocument(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("the do doDocument start ...."));
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String crudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
        String crudActionValue = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_VALUE);
        String crud_action_additional = mulReq.getParameter("crud_action_additional");

        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE, crudActionValue);
        ParamUtil.setRequestAttr(bpc.request, crud_action_additional, crud_action_additional);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        Map<String, CommonsMultipartFile> commonsMultipartFileMap = IaisCommonUtils.genNewHashMap();
        List<AppGrpPrimaryDocDto> newAppGrpPrimaryDocDtoList = IaisCommonUtils.genNewArrayList();
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        AppSubmissionDto oldSubmissionDto = NewApplicationHelper.getOldSubmissionDto(bpc.request,appSubmissionDto.getAppType());
        List<AppGrpPrimaryDocDto> oldPrimaryDocDtoList = oldSubmissionDto.getAppGrpPrimaryDocDtos();
        String action = ParamUtil.getRequestString(bpc.request, "crud_action_value");
        String appType = appSubmissionDto.getAppType();
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)) {
            if (RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)) {
                //clear
                ParamUtil.setSessionAttr(bpc.request, RELOADAPPGRPPRIMARYDOCMAP, null);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "jump");
                return;
            }
        }

        Object requestInformationConfig = ParamUtil.getSessionAttr(bpc.request, REQUESTINFORMATIONCONFIG);
        boolean isRfi = false;
        if (requestInformationConfig != null) {
            isRfi = true;
        }
        String isEdit = ParamUtil.getString(mulReq, IS_EDIT);
        boolean isGetDataFromPage = NewApplicationHelper.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SUPPORTING_DOCUMENT, isEdit, isRfi);

        //premIndexNo+configId+seqnum
        Map<String,File> saveFileMap = IaisCommonUtils.genNewHashMap();
        if (isGetDataFromPage) {
            List<AppGrpPremisesDto> appGrpPremisesList = appSubmissionDto.getAppGrpPremisesDtoList();
            if (appSubmissionDto.isNeedEditController()) {
                Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
                clickEditPages.add(NewApplicationDelegator.APPLICATION_PAGE_NAME_PRIMARY);
                appSubmissionDto.setClickEditPage(clickEditPages);
                AppEditSelectDto appEditSelectDto = appSubmissionDto.getChangeSelectDto();
                appEditSelectDto.setDpoEdit(true);
                appSubmissionDto.setChangeSelectDto(appEditSelectDto);
            }
            List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos = (List<HcsaSvcDocConfigDto>) ParamUtil.getSessionAttr(bpc.request,PRIMARY_DOC_CONFIG);
            List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
            for(int i =0;i<hcsaSvcDocConfigDtos.size();i++){
                HcsaSvcDocConfigDto hcsaSvcDocConfigDto = hcsaSvcDocConfigDtos.get(i);
                String dupForPrem = hcsaSvcDocConfigDto.getDupForPrem();
                if("0".equals(dupForPrem)){
                    String docKey = i+"primaryDoc";
                    Map<String, File> fileMap = (Map<String, File>) ParamUtil.getSessionAttr(mulReq,HcsaFileAjaxController.SEESION_FILES_MAP_AJAX+docKey);
                    genPrimaryDoc(fileMap,docKey,hcsaSvcDocConfigDto,saveFileMap,appGrpPrimaryDocDtos,newAppGrpPrimaryDocDtoList,"","",isRfi,oldPrimaryDocDtoList,oldSubmissionDto.getAppGrpId(),oldSubmissionDto.getRfiAppNo(),appSubmissionDto.getAppType(),dupForPrem);
                    ParamUtil.setSessionAttr(bpc.request,HcsaFileAjaxController.SEESION_FILES_MAP_AJAX+docKey, (Serializable) fileMap);
                }else if("1".equals(dupForPrem)){
                    for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesList){
                        String docKey = i+"primaryDoc" + appGrpPremisesDto.getPremisesIndexNo();
                        Map<String, File> fileMap = (Map<String, File>) ParamUtil.getSessionAttr(mulReq,HcsaFileAjaxController.SEESION_FILES_MAP_AJAX+docKey);
                        genPrimaryDoc(fileMap,docKey,hcsaSvcDocConfigDto,saveFileMap,appGrpPrimaryDocDtos,newAppGrpPrimaryDocDtoList,appGrpPremisesDto.getPremisesIndexNo(),appGrpPremisesDto.getPremisesType(),isRfi,oldPrimaryDocDtoList,oldSubmissionDto.getAppGrpId(),oldSubmissionDto.getRfiAppNo(),appSubmissionDto.getAppType(),dupForPrem);
                        ParamUtil.setSessionAttr(bpc.request,HcsaFileAjaxController.SEESION_FILES_MAP_AJAX+docKey, (Serializable) fileMap);
                    }
                }
            }
            //set value into AppSubmissionDto
            appSubmissionDto.setAppGrpPrimaryDocDtos(newAppGrpPrimaryDocDtoList);
            ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        }

        String crud_action_values = ParamUtil.getRequestString(bpc.request, "crud_action_value");
        if ("next".equals(crud_action_values)) {
            List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionService.documentValid(bpc.request, errorMap,true);
            doIsCommom(bpc.request, errorMap);
            HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
            if (errorMap.isEmpty()) {
                coMap.put("document", "document");
            } else {
                coMap.put("document", "");
            }
            saveFileAndSetFileId(appGrpPrimaryDocDtos,saveFileMap);
            appSubmissionDto.setAppGrpPrimaryDocDtos(appGrpPrimaryDocDtos);
            ParamUtil.setSessionAttr(bpc.request,APPSUBMISSIONDTO,appSubmissionDto);

            bpc.request.getSession().setAttribute("coMap", coMap);
        }else{
            List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionService.documentValid(bpc.request, errorMap,true);
            doIsCommom(bpc.request, errorMap);
            saveFileAndSetFileId(appGrpPrimaryDocDtos,saveFileMap);
            appSubmissionDto.setAppGrpPrimaryDocDtos(appGrpPrimaryDocDtos);
            ParamUtil.setSessionAttr(bpc.request,APPSUBMISSIONDTO,appSubmissionDto);
            errorMap = IaisCommonUtils.genNewHashMap();
        }
        if (errorMap.size() > 0) {
            //set audit
            bpc.request.setAttribute("errormapIs", "error");
            NewApplicationHelper.setAudiErrMap(isRfi,appSubmissionDto.getAppType(),errorMap,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setSessionAttr(bpc.request, APPGRPPRIMARYDOCERRMSGMAP, (Serializable) errorMap);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "documents");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, "documents");
            return;
        }

        log.info(StringUtil.changeForLog("the do doDocument end ...."));
    }

    private void doIsCommom(HttpServletRequest request, Map<String, String> errorMap) {

        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = appSubmissionDto.getAppGrpPrimaryDocDtos();
        String err006 = MessageUtil.replaceMessage("GENERAL_ERR0006", "Document", "field");
        List<HcsaSvcDocConfigDto> commonHcsaSvcDocConfigList = (List<HcsaSvcDocConfigDto>) request.getSession().getAttribute(PRIMARY_DOC_CONFIG);
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        if(!IaisCommonUtils.isEmpty(commonHcsaSvcDocConfigList) && !IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
            int i = 0;
            String suffix = "Error";
            for (HcsaSvcDocConfigDto comm : commonHcsaSvcDocConfigList) {
                String errKey = i+"primaryDoc";
                Boolean isMandatory = comm.getIsMandatory();
                String dupForPrem = comm.getDupForPrem();
                String configId = comm.getId();
                i++;
                if(!isMandatory){
                    continue;
                }
                if(IaisCommonUtils.isEmpty(appGrpPrimaryDocDtoList)){
                    appGrpPrimaryDocDtoList = IaisCommonUtils.genNewArrayList();
                }
                if("0".equals(dupForPrem)){
                    AppGrpPrimaryDocDto appGrpPrimaryDocDto =getAppGrpPrimaryDocByConfigIdAndPremIndex(appGrpPrimaryDocDtoList,configId,"");
                    if(appGrpPrimaryDocDto == null){
                        errorMap.put(errKey+suffix, err006);
                    }
                }else if("1".equals(dupForPrem)){
                    for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                        String premIndex = appGrpPremisesDto.getPremisesIndexNo();
                        String currErrKey = errKey + premIndex +suffix;
                        AppGrpPrimaryDocDto appGrpPrimaryDocDto =getAppGrpPrimaryDocByConfigIdAndPremIndex(appGrpPrimaryDocDtoList,configId,premIndex);
                        if(appGrpPrimaryDocDto == null){
                            errorMap.put(currErrKey, err006);
                        }
                    }
                }
            }
        }
    }

    /**
     * StartStep: doForms
     *
     * @param bpc
     * @throws
     */
    public void doForms(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do doForms start ...."));

        log.info(StringUtil.changeForLog("the do doForms end ...."));
    }

    /**
     * StartStep: doPreview
     *
     * @param bpc
     * @throws
     */
    public void doPreview(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do doPreview start ...."));
        String action = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String isGroupLic = ParamUtil.getString(bpc.request, "isGroupLic");
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
//        if(!appSubmissionDto.isOnlySpecifiedSvc() && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())){
        if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
            if (!StringUtil.isEmpty(isGroupLic) && AppConsts.YES.equals(isGroupLic)) {
                appSubmissionDto.setGroupLic(true);
            } else {
                appSubmissionDto.setGroupLic(false);
            }
            if (!NewApplicationHelper.checkIsRfi(bpc.request)) {
                // declaration
                appSubmissionDto.setAppDeclarationMessageDto(appSubmissionService.getAppDeclarationMessageDto(bpc.request, appSubmissionDto.getAppType()));
                DeclarationsUtil.declarationsValidate(errorMap, appSubmissionDto.getAppDeclarationMessageDto(),
                        appSubmissionDto.getAppType());
                // uploaded files
                appSubmissionDto.setAppDeclarationDocDtos(appSubmissionService.getDeclarationFiles(appSubmissionDto.getAppType(), bpc.request));
                String preQuesKindly = appSubmissionDto.getAppDeclarationMessageDto().getPreliminaryQuestionKindly();
                appSubmissionService.validateDeclarationDoc(errorMap, appSubmissionService.getFileAppendId(appSubmissionDto.getAppType()),
                        preQuesKindly ==null ? false : "0".equals(preQuesKindly), bpc.request);
            }
        }else if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())){
            if(!NewApplicationHelper.checkIsRfi(bpc.request)){
                AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.OLDAPPSUBMISSIONDTO);
                List<AppGrpPremisesDto> oldAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
                List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
                if(oldAppGrpPremisesDtoList!=null&& appGrpPremisesDtoList!=null){
                    for (int i = 0; i < appGrpPremisesDtoList.size(); i++) {
                        boolean eqHciNameChange = EqRequestForChangeSubmitResultChange.eqHciNameChange(appGrpPremisesDtoList.get(i), oldAppGrpPremisesDtoList.get(i));
                        if(eqHciNameChange){
                            AppDeclarationMessageDto appDeclarationMessageDto = appSubmissionService.getAppDeclarationMessageDto(bpc.request,ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
                            appSubmissionDto.setAppDeclarationMessageDto(appDeclarationMessageDto);
                            appSubmissionDto.setAppDeclarationDocDtos(appSubmissionService.getDeclarationFiles(appSubmissionDto.getAppType(), bpc.request));
                        }
                    }
                }
            }
        }

        String userAgreement = ParamUtil.getString(bpc.request, "verifyInfoCheckbox");
        Object requestInformationConfig = ParamUtil.getSessionAttr(bpc.request, REQUESTINFORMATIONCONFIG);
        if (requestInformationConfig == null && ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
            String effectiveDateStr = ParamUtil.getString(bpc.request, "rfcEffectiveDate");
            if (!StringUtil.isEmpty(effectiveDateStr)) {
                appSubmissionDto.setEffectiveDateStr(effectiveDateStr);
                int configDateSize = systemParamConfig.getRfcPeriodEffdate();
                LocalDate effectiveDate = LocalDate.parse(effectiveDateStr, DateTimeFormatter.ofPattern(Formatter.DATE));
                LocalDate today = LocalDate.now();
                LocalDate configDate = LocalDate.now().plusDays(configDateSize);
                if (effectiveDate.isBefore(today)) {
                    errorMap.put("rfcEffectiveDate", "RFC_ERR012");
                } else if (effectiveDate.isAfter(configDate)) {
                    String errorMsg = MessageUtil.getMessageDesc("RFC_ERR008");
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Formatter.DATE);
                    errorMsg = errorMsg.replace("{date}", configDate.format(dtf));
                    errorMap.put("rfcEffectiveDate", errorMsg);
                } else if (today.isEqual(effectiveDate)) {
                    errorMap.put("rfcEffectiveDate", "RFC_ERR012");
                }
                String rfcEffectiveDateErr = errorMap.get("rfcEffectiveDate");
                if (StringUtil.isEmpty(rfcEffectiveDateErr)) {
                    Date effDate = DateUtil.parseDate(effectiveDateStr, Formatter.DATE);
                    appSubmissionDto.setEffectiveDate(effDate);
                }
            }
        }
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        if ("doSubmit".equals(action) && !errorMap.isEmpty()) {
            NewApplicationHelper.setAudiErrMap(NewApplicationHelper.checkIsRfi(bpc.request), appSubmissionDto.getAppType(), errorMap,
                    appSubmissionDto.getRfiAppNo(), appSubmissionDto.getLicenceNo());
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "preview");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, "test");
        }
        log.info(StringUtil.changeForLog("the do doPreview end ...."));
    }

    /**
     * StartStep: doPreview
     *
     * @param bpc
     * @throws
     */
    public void doPayment(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do doPayment start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        //for relation Licences when RFC the premises.
        List<AppSubmissionDto> appSubmissionDtos = (List<AppSubmissionDto>) bpc.request.getSession().getAttribute("ackPageAppSubmissionDto");
        String switch2 = "loading";
        String pmtMethod = appSubmissionDto.getPaymentMethod();
        if (StringUtil.isEmpty(pmtMethod)) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "payment");
            return;
        }
        if (!StringUtil.isEmpty(pmtMethod) && ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(pmtMethod)) {
            switch2 = "ack";
            String txnDt = DateUtil.formatDate(new Date(), "dd/MM/yyyy");
            ParamUtil.setSessionAttr(bpc.request, "txnDt", txnDt);
        }
        String result = ParamUtil.getMaskedString(bpc.request, "result");
        String pmtRefNo = ParamUtil.getMaskedString(bpc.request, "reqRefNo");
        appSubmissionService.updateDraftStatus(appSubmissionDto.getDraftNo(),AppConsts.COMMON_STATUS_ACTIVE);
        if (!StringUtil.isEmpty(result)) {
            log.info(StringUtil.changeForLog("payment result:" + result));
            if ("success".equals(result) && !StringUtil.isEmpty(pmtRefNo)) {
                log.debug(StringUtil.changeForLog("online payment success ..."));
                try {
                    if(appSubmissionDto.getAppType().equals(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE)){
                        List<AppSubmissionDto> appSubmissionDtos1 = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request, "appSubmissionDtos");
                        if(appSubmissionDtos1==null||appSubmissionDtos1.size()==0){
                            appSubmissionDtos1=IaisCommonUtils.genNewArrayList();
                            appSubmissionDtos1.add(appSubmissionDto);
                        }
                        requestForChangeService.sendRfcSubmittedEmail(appSubmissionDtos1, appSubmissionDto.getPaymentMethod());
                    }
                } catch (Exception e) {
                    log.info(e.getMessage(), e);
                }

                if (appSubmissionDtos != null) {
                    for (AppSubmissionDto appSubmissionDto1 : appSubmissionDtos) {
                        Double amount = appSubmissionDto1.getAmount();
                        String grpId = appSubmissionDto1.getAppGrpId();
                        boolean autoRfc = appSubmissionDto1.isAutoRfc();
                        List<ApplicationDto> entity = applicationFeClient.getApplicationsByGroupNo(appSubmissionDto1.getAppGrpNo()).getEntity();
                        if(entity!=null && !entity.isEmpty()){
                            for(ApplicationDto applicationDto : entity){
                                if(autoRfc){
                                    applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
                                }else {
                                    applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
                                }
                            }
                            applicationFeClient.updateApplicationList(entity);
                        }

                        ApplicationGroupDto appGrp = new ApplicationGroupDto();
                        appGrp.setId(grpId);
                        appGrp.setPmtRefNo(pmtRefNo);
                        if (0.0 != amount) {
                            appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                            appGrp.setPayMethod(appSubmissionDto.getPaymentMethod());
                            serviceConfigService.updatePaymentStatus(appGrp);
                        }else {
                            appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
                            appGrp.setPayMethod(appSubmissionDto.getPaymentMethod());
                            serviceConfigService.updatePaymentStatus(appGrp);
                        }
                    }
                }
                String txnDt = ParamUtil.getMaskedString(bpc.request, "txnDt");
                String txnRefNo = ParamUtil.getMaskedString(bpc.request, "txnRefNo");
                ParamUtil.setSessionAttr(bpc.request, "txnDt", txnDt);
                ParamUtil.setSessionAttr(bpc.request, "txnRefNo", txnRefNo);
                switch2 = "ack";
                //update status
                String appGrpId = appSubmissionDto.getAppGrpId();
                ApplicationGroupDto appGrp = new ApplicationGroupDto();
                appGrp.setId(appGrpId);
                appGrp.setPmtRefNo(pmtRefNo);
                appGrp.setPaymentDt(new Date());
                appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                appGrp.setPayMethod(appSubmissionDto.getPaymentMethod());
                serviceConfigService.updatePaymentStatus(appGrp);
                //send email
                try {
//                    sendNewApplicationPaymentOnlineSuccesedEmail(appSubmissionDto, pmtMethod, pmtRefNo);
                    //requestForChangeService.sendEmail(appSubmissionDto.getAppGrpId(),null,appSubmissionDto.getApplicationDtos().get(0).getApplicationNo(),null,null,appSubmissionDto.getAmount(),null,null,appSubmissionDto.getLicenseeId(),"RfcAndOnPay",null);
                    LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
                    appSubmissionService.sendEmailAndSMSAndMessage(appSubmissionDto,loginContext.getUserName());
                } catch (Exception e) {
                    log.error(StringUtil.changeForLog("send email error ...."));
                }
            }else{
                if(!"cancelled".equals(result)){
                    Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
                    errorMap.put("pay",MessageUtil.getMessageDesc("NEW_ERR0024"));
                    NewApplicationHelper.setAudiErrMap(NewApplicationHelper.checkIsRfi(bpc.request),appSubmissionDto.getAppType(),errorMap,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
                    ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));

                }
                //appSubmissionService.updateDraftStatus(appSubmissionDto.getDraftNo(),AppConsts.COMMON_STATUS_ACTIVE);
                switch2 = "loading";
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE,"payment");
            }
        }else{
            log.debug(StringUtil.changeForLog("result is empty"));
            //appSubmissionService.updateDraftStatus(appSubmissionDto.getDraftNo(),AppConsts.COMMON_STATUS_ACTIVE);
            switch2 = "loading";
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE,"payment");
        }

        if ("ack".equals(switch2)) {
            ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, "payment success !!!");
        }
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE, switch2);
        log.info(StringUtil.changeForLog("the do doPayment end ...."));
    }

    /**
     * StartStep: preInvoke
     *
     * @param bpc
     * @throws
     */
    public void preInvoke(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("the do preInvoke start ...."));
        String action = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        if (!StringUtil.isEmpty(action)) {
            if ("MohAppPremSelfDecl".equals(action)) {
//                ParamUtil.setSessionAttr(bpc.request, NewApplicationConstant.SESSION_PARAM_APPLICATION_GROUP_ID, appSubmissionDto.getAppGrpId());
//                ParamUtil.setSessionAttr(bpc.request,NewApplicationConstant.SESSION_SELF_DECL_ACTION,"new");
            } else if ("DashBoard".equals(action)) {
                StringBuilder url = new StringBuilder();
                url.append("https://").append(bpc.request.getServerName()).append("/main-web/eservice/INTERNET/MohInternetInbox");
                String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
                bpc.response.sendRedirect(tokenUrl);
            } else if ("ChooseSvc".equals(action)) {
                StringBuilder url = new StringBuilder();
                url.append("https://").append(bpc.request.getServerName()).append("/hcsa-licence-web/eservice/INTERNET/MohServiceFeMenu");
                String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
                bpc.response.sendRedirect(tokenUrl);
            }
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, action);
        }

        log.info(StringUtil.changeForLog("the do preInvoke start ...."));
    }

    /**
     * StartStep: doSaveDraft
     *
     * @param bpc
     * @throws
     */
    public void doSaveDraft(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("the do doSaveDraft start ...."));
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String crud_action_additional;
        if (mulReq != null) {
            crud_action_additional = mulReq.getParameter("crud_action_additional");
        } else {
            crud_action_additional = bpc.request.getParameter("crud_action_additional");
        }
        if ("jumpPage".equals(crud_action_additional)) {
            jumpYeMian(bpc.request, bpc.response);
            return;
        }
        HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");

        String serviceConfig = (String) bpc.request.getSession().getAttribute("serviceConfig");

        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        if (StringUtil.isEmpty(appSubmissionDto.getDraftNo())) {
            String draftNo = appSubmissionService.getDraftNo(appSubmissionDto.getAppType());
            log.info(StringUtil.changeForLog("the draftNo -->:" + draftNo));
            appSubmissionDto.setDraftNo(draftNo);
        } else {
            appSubmissionDto.setOldDraftNo(null);
        }
        String oldDraftNo = (String) bpc.request.getSession().getAttribute(SELECT_DRAFT_NO);
        bpc.request.getSession().removeAttribute(SELECT_DRAFT_NO);
        appSubmissionDto.setOldDraftNo(oldDraftNo);

        stepColorService.setStepColor(coMap,serviceConfig,appSubmissionDto);
        Integer maxFileIndex = (Integer) ParamUtil.getSessionAttr(bpc.request,HcsaFileAjaxController.GLOBAL_MAX_INDEX_SESSION_ATTR);
        if(maxFileIndex == null){
            maxFileIndex = 0;
        }
        appSubmissionDto.setMaxFileIndex(maxFileIndex);
        //set psn dropdown
        setPsnDroTo(appSubmissionDto, bpc);
        preDataDeclaration(bpc.request,appSubmissionDto);
        appSubmissionDto = appSubmissionService.doSaveDraft(appSubmissionDto);
        if("exitSaveDraft".equals(crud_action_additional)){
            jumpYeMian(bpc.request, bpc.response);
            return;
        }
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        bpc.request.setAttribute("saveDraftSuccess", "success");
        log.info(StringUtil.changeForLog("the do doSaveDraft end ...."));
    }
    private void preDataDeclaration(HttpServletRequest request,AppSubmissionDto appSubmissionDto){
        if(appSubmissionDto==null){
            return;
        }
        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())){
            AppDeclarationMessageDto appDeclarationMessageDto = appSubmissionService.getAppDeclarationMessageDto(request, ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
            List<AppDeclarationDocDto> declarationFiles = appSubmissionService.getDeclarationFiles(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE, request);
            if((declarationFiles==null || declarationFiles .isEmpty()) && isEmptyData(appDeclarationMessageDto,ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE)){

            }else {
                appSubmissionDto.setAppDeclarationMessageDto(appDeclarationMessageDto);
                appSubmissionDto.setAppDeclarationDocDtos(declarationFiles);
            }
        }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())){

        }else if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())){
            AppDeclarationMessageDto appDeclarationMessageDto = appSubmissionService.getAppDeclarationMessageDto(request, ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
            List<AppDeclarationDocDto> declarationFiles = appSubmissionService.getDeclarationFiles(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION, request);
            appSubmissionService.getAppDeclarationMessageDto(request,ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
            appSubmissionDto.setAppDeclarationMessageDto(appDeclarationMessageDto);
            appSubmissionDto.setAppDeclarationDocDtos(declarationFiles);
        }
    }
    /*NewApplicationDelegator
    * */
    private boolean isEmptyData( AppDeclarationMessageDto appDeclarationMessageDto ,String apptye){
        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(apptye)){
            String preliminaryQuestionKindly = appDeclarationMessageDto.getPreliminaryQuestionKindly();
            String preliminaryQuestionItem1 = appDeclarationMessageDto.getPreliminaryQuestionItem1();
            String preliminaryQuestiontem2 = appDeclarationMessageDto.getPreliminaryQuestiontem2();
            Date effectiveDt = appDeclarationMessageDto.getEffectiveDt();
            if(preliminaryQuestionKindly==null&&preliminaryQuestionItem1==null&&preliminaryQuestiontem2==null&&effectiveDt==null){
                return true;
            }else {
                return false;
            }
        }

        return true;
    }

    public void jumpYeMian(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder url = new StringBuilder(10);
        url.append("https://").append(request.getServerName()).append("/main-web/eservice/INTERNET/MohInternetInbox");
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), request);
        response.sendRedirect(tokenUrl);
    }

    private void sendURL(HttpServletRequest request, HttpServletResponse response, String url) {
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url, request);
        try {
            response.sendRedirect(tokenUrl);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void inboxToPreview(BaseProcessClass bpc) throws Exception {
        // clear session
        appSubmissionService.clearSession(bpc.request);
        // View and Print
        ParamUtil.setSessionAttr(bpc.request, "viewPrint","Y");
        String appNo = ParamUtil.getMaskedString(bpc.request, "appNo");
        if (!StringUtil.isEmpty(appNo)) {
            ApplicationDto applicationDto = applicationFeClient.getApplicationDtoByAppNo(appNo).getEntity();
            if(applicationDto != null) {
                if (ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION.equals(applicationDto.getStatus())) {
                    MsgTemplateDto autoEntity = generateIdClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_APP_RFI_MSG).getEntity();
                    Map<String,Object> subjectMap = IaisCommonUtils.genNewHashMap();
                    subjectMap.put("ApplicationType",MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
                    subjectMap.put("ApplicationNumber",StringUtil.viewHtml(appNo));
                    String msgSubject = MsgUtil.getTemplateMessageByContent(autoEntity.getTemplateName(),subjectMap);
                    InterMessageDto interMessageBySubjectLike = appSubmissionService.getInterMessageBySubjectLike(msgSubject.trim(), MessageConstants.MESSAGE_STATUS_RESPONSE);
                    if (interMessageBySubjectLike.getId() != null) {
                        List<AppEditSelectDto> entity = applicationFeClient.getAppEditSelectDtos(applicationDto.getId(), ApplicationConsts.APPLICATION_EDIT_TYPE_RFI).getEntity();
                        String url = "";
                        String s = MaskUtil.maskValue("appNo", applicationDto.getApplicationNo());
                        if (!entity.isEmpty()) {
                            boolean rfcFlag = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationDto.getApplicationType());
                            boolean premisesListEdit = entity.get(0).isPremisesListEdit();
                            if (rfcFlag && premisesListEdit) {
                                url = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_CALL_BACK_URL_PREMISES_LIST + s;
                                sendURL(bpc.request, bpc.response, url);
                                bpc.request.getSession().setAttribute(AppConsts.SESSION_INTER_INBOX_MESSAGE_ID, interMessageBySubjectLike.getId());
                                return;
                            }
                        }

                        url = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_CALL_BACK_URL_NEWAPPLICATION + s;
                        sendURL(bpc.request, bpc.response, url);
                        bpc.request.getSession().setAttribute(AppConsts.SESSION_INTER_INBOX_MESSAGE_ID, interMessageBySubjectLike.getId());
                        return;
                    }
                }
                /**
                 * cessation
                 */

                if (ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationDto.getApplicationType())) {
                    AppCessLicDto appCessLicDto = new AppCessLicDto();
                    String originLicenceId = applicationDto.getOriginLicenceId();
                    LicenceDto licenceDto = licenceClient.getLicDtoById(originLicenceId).getEntity();
                    String svcName = licenceDto.getSvcName();
                    String licenceNo = licenceDto.getLicenceNo();
                    appCessLicDto.setLicenceNo(licenceNo);
                    appCessLicDto.setSvcName(svcName);
                    AppCessMiscDto appCessMiscDto = cessationClient.getAppMiscDtoByAppId(applicationDto.getId()).getEntity();
                    AppGrpPremisesEntityDto appGrpPremisesEntityDto = applicationFeClient.getPremisesByAppNo(applicationDto.getApplicationNo()).getEntity();
                    List<AppDeclarationMessageDto> appDeclarationMessageDtoList = applicationFeClient.getAppDeclarationMessageDto(appGrpPremisesEntityDto.getAppGrpId()).getEntity();
                    List<AppDeclarationDocDto> appDeclarationDocDtoList = applicationFeClient.getAppDeclarationDocDto(appGrpPremisesEntityDto.getAppGrpId()).getEntity();
                    if (appDeclarationMessageDtoList != null && appDeclarationMessageDtoList.size() > 0){
                        appCessLicDto.setAppDeclarationMessageDto(appDeclarationMessageDtoList.get(0));
                        appCessLicDto.setAppDeclarationDocDtos(appDeclarationDocDtoList);
                    }
                    //appSubmissionService.initDeclarationFiles(appDeclarationDocDtoList,ApplicationConsts.APPLICATION_TYPE_CESSATION,bpc.request);
                    String blkNo = appGrpPremisesEntityDto.getBlkNo();
                    String premisesId = appGrpPremisesEntityDto.getId();
                    String streetName = appGrpPremisesEntityDto.getStreetName();
                    String buildingName = appGrpPremisesEntityDto.getBuildingName();
                    String floorNo = appGrpPremisesEntityDto.getFloorNo();
                    String unitNo = appGrpPremisesEntityDto.getUnitNo();
                    String postalCode = appGrpPremisesEntityDto.getPostalCode();
                    String hciAddress = MiscUtil.getAddress(blkNo, streetName, buildingName, floorNo, unitNo, postalCode);
                    AppCessHciDto appCessHciDto = new AppCessHciDto();
                    String hciName = appGrpPremisesEntityDto.getHciName();
                    String hciCode = appGrpPremisesEntityDto.getHciCode();
                    appCessHciDto.setHciCode(hciCode);
                    appCessHciDto.setHciName(hciName);
                    appCessHciDto.setPremiseId(premisesId);
                    appCessHciDto.setHciAddress(hciAddress);
                    Date effectiveDate = appCessMiscDto.getEffectiveDate();
                    String reason = appCessMiscDto.getReason();
                    String otherReason = appCessMiscDto.getOtherReason();
                    String patTransType = appCessMiscDto.getPatTransType();
                    String patTransTo = appCessMiscDto.getPatTransTo();
                    Boolean patNeedTrans = appCessMiscDto.getPatNeedTrans();
                    String mobileNo = appCessMiscDto.getMobileNo();
                    String emailAddress = appCessMiscDto.getEmailAddress();
                    appCessHciDto.setPatientSelect(patTransType);
                    appCessHciDto.setReason(reason);
                    appCessHciDto.setOtherReason(otherReason);
                    appCessHciDto.setEffectiveDate(effectiveDate);
                    appCessHciDto.setTransferDetail(appCessMiscDto.getTransferDetail());
                    appCessHciDto.setTransferredWhere(appCessMiscDto.getTransferredWhere());
                    if(patNeedTrans){
                        appCessHciDto.setPatNeedTrans(Boolean.TRUE);
//                        if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_HCI.equals(patTransType) && !StringUtil.isEmpty(patTransTo)) {
//                            appCessHciDto.setPatHciName(patTransTo);
//                            appCessHciDto.setPatNeedTrans(Boolean.TRUE);
//                            PremisesDto premisesDto = cessationFeService.getPremiseByHciCodeName(patTransTo);
//                            String hciAddressPat = premisesDto.getHciAddress();
//                            String hciNamePat = premisesDto.getHciName();
//                            String hciCodePat= premisesDto.getHciCode();
//                            appCessHciDto.setHciNamePat(hciNamePat);
//                            appCessHciDto.setHciAddressPat(hciAddressPat);
//                            appCessHciDto.setHciCodePat(hciCodePat);
//                        } else if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_PRO.equals(patTransType) && !StringUtil.isEmpty(patTransTo)) {
//                            appCessHciDto.setPatRegNo(patTransTo);
//                            appCessHciDto.setPatNeedTrans(Boolean.TRUE);
//                        } else if (ApplicationConsts.CESSATION_PATIENT_TRANSFERRED_TO_OTHER.equals(patTransType) && !StringUtil.isEmpty(patTransTo)) {
//                            appCessHciDto.setPatOthers(patTransTo);
//                            appCessHciDto.setMobileNo(mobileNo);
//                            appCessHciDto.setEmailAddress(emailAddress);
//                            appCessHciDto.setPatNeedTrans(Boolean.TRUE);
//                        }
                    }else {
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
                    if(!IaisCommonUtils.isEmpty(specApps)){
                        List<AppSpecifiedLicDto> appSpecifiedLicDtos = IaisCommonUtils.genNewArrayList();
                        for(ApplicationDto specApp : specApps){
                            String specId = specApp.getOriginLicenceId();
                            LicenceDto specLicenceDto = licenceClient.getLicDtoById(specId).getEntity();
                            if (specLicenceDto != null) {
                                AppSpecifiedLicDto appSpecifiedLicDto = new AppSpecifiedLicDto();
                                LicenceDto baseLic = licenceClient.getLicDtoById(originLicenceId).getEntity();
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
                        ParamUtil.setRequestAttr(bpc.request, "specLicInfo", appSpecifiedLicDtos);
                        ParamUtil.setSessionAttr(bpc.request, "specLicInfoPrint", (Serializable) appSpecifiedLicDtos);
                    }
                    List<SelectOption> reasonOption = NewApplicationHelper.getReasonOption();
                    List<SelectOption> patientsOption = NewApplicationHelper.getPatientsOption();
                    ParamUtil.setRequestAttr(bpc.request, "reasonOption", reasonOption);
                    ParamUtil.setRequestAttr(bpc.request, "patientsOption", patientsOption);
                    ParamUtil.setRequestAttr(bpc.request, "applicationDto", applicationDto);
                    List<AppCessLicDto> appCessLicDtos = IaisCommonUtils.genNewArrayList();
                    appCessLicDtos.add(appCessLicDto);
                    ParamUtil.setRequestAttr(bpc.request, "confirmDtos", appCessLicDtos);
                    ParamUtil.setRequestAttr(bpc.request, "printFlag","Y");

                    ParamUtil.setSessionAttr(bpc.request, "appCessationDtos", (Serializable)appCessLicDtos);
                    ParamUtil.setSessionAttr(bpc.request, "reasonOptionPrint", (Serializable) reasonOption);
                    ParamUtil.setSessionAttr(bpc.request, "patientsOptionPrint", (Serializable) patientsOption);
                    ParamUtil.setRequestAttr(bpc.request, "applicationDtoPrint", applicationDto);
                    ParamUtil.setSessionAttr(bpc.request, "confirmPrintDtos", (Serializable) appCessLicDtos);

                    ParamUtil.setSessionAttr(bpc.request,"declaration_page_is","cessation");
                    return;
                }
                AppSubmissionDto appSubmissionDto = appSubmissionService.getAppSubmissionDto(appNo);
                if (appSubmissionDto != null) {
                    /**
                     * preview
                     */
                    if (!IaisCommonUtils.isEmpty(appSubmissionDto.getAppSvcRelatedInfoDtoList())){
                        svcRelatedInfoView(appSubmissionDto,bpc.request,applicationDto.getServiceId());
                        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())
                                ||ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())
                                || ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(appSubmissionDto.getAppType())){
                            requestForChangeService.svcDocToPresmise(appSubmissionDto);
                        }
                    }
                    if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())){
                        AppDeclarationMessageDto appDeclarationMessageDto = appSubmissionDto.getAppDeclarationMessageDto();
                        if(appDeclarationMessageDto!=null){
                            bpc.request.setAttribute("RFC_eqHciNameChange","RFC_eqHciNameChange");
                        }
                        if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equalsIgnoreCase(appSubmissionDto.getAppGroupAppType())){
                            bpc.request.setAttribute("group_renewal_app_rfc","1");
                        }
                    }
                    if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())){
                        AppDeclarationMessageDto appDeclarationMessageDto = appSubmissionDto.getAppDeclarationMessageDto();
                        if(appDeclarationMessageDto!=null){
                            RenewDto renewDto=new RenewDto();
                            renewDto.setAppSubmissionDtos(Collections.singletonList(appSubmissionDto));
                            bpc.request.setAttribute("renewDto",renewDto);
                            appSubmissionService.initDeclarationFiles(appSubmissionDto.getAppDeclarationDocDtos(),
                                    appSubmissionDto.getAppType(), bpc.request);
                            bpc.request.getSession().setAttribute("isSingle","Y");
                        }else {
                            bpc.request.getSession().setAttribute("isSingle","N");
                        }
                    }
                    if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())
                            || ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
                        appSubmissionService.initDeclarationFiles(appSubmissionDto.getAppDeclarationDocDtos(),
                                appSubmissionDto.getAppType(), bpc.request);
                    }
                    premiseView(appSubmissionDto, applicationDto, bpc.request);
                }
                ParamUtil.setRequestAttr(bpc.request, "cessationForm", "Application Details");
                ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
            }
        }
    }

    private void premiseView(AppSubmissionDto appSubmissionDto,ApplicationDto applicationDto,HttpServletRequest request) throws CloneNotSupportedException {
        if (appSubmissionDto == null || applicationDto == null) {
            return;
        }
        if (!IaisCommonUtils.isEmpty(appSubmissionDto.getAppGrpPremisesDtoList())) {
            if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationDto.getApplicationType())
                    || ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationDto.getApplicationType())
                    || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationDto.getApplicationType())
                    || ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(applicationDto.getApplicationType())) {
                log.info(StringUtil.changeForLog("InboxToView AppNo -->" + applicationDto.getApplicationNo()));
                List<AppGrpPremisesDto> newPremisesDtos = IaisCommonUtils.genNewArrayList();
                AppGrpPremisesEntityDto rfiPremises = appSubmissionService.getPremisesByAppNo(applicationDto.getApplicationNo());
                if (rfiPremises != null) {
                    String premHci = IaisCommonUtils.genPremisesKey(rfiPremises.getPostalCode(), rfiPremises.getBlkNo(), rfiPremises.getFloorNo(), rfiPremises.getUnitNo());
                    if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(rfiPremises.getPremisesType())) {
                        premHci = rfiPremises.getHciName() + premHci;
                    } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(rfiPremises.getPremisesType())) {
                        premHci = rfiPremises.getVehicleNo() + premHci;
                    }
                    for (AppGrpPremisesDto appGrpPremisesDto : appSubmissionDto.getAppGrpPremisesDtoList()) {
                        if (premHci.equals(NewApplicationHelper.getPremHci(appGrpPremisesDto))) {
                            NewApplicationHelper.setWrkTime(appGrpPremisesDto);
                            List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodDtos = appGrpPremisesDto.getAppPremPhOpenPeriodList();
                            //set ph name
                            NewApplicationHelper.setPhName(appPremPhOpenPeriodDtos);
                            newPremisesDtos.add(appGrpPremisesDto);
                            break;
                        }
                    }
                }
                appSubmissionDto.setAppGrpPremisesDtoList(newPremisesDtos);
                String svcId = applicationDto.getServiceId();
                List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
                if (!StringUtil.isEmpty(svcId)) {
                    List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
                    List<AppSvcRelatedInfoDto> newSvcRelatedInfoDtos = IaisCommonUtils.genNewArrayList();
                    //set doc name
                    List<HcsaSvcDocConfigDto> primaryDocConfig = null;
                    if (appGrpPrimaryDocDtos != null && appGrpPrimaryDocDtos.size() > 0) {
                        primaryDocConfig = serviceConfigService.getPrimaryDocConfigById(appGrpPrimaryDocDtos.get(0).getSvcComDocId());
                    }

                    for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                        if (svcId.equals(appSvcRelatedInfoDto.getServiceId())) {
                            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(svcId);
                            appSvcRelatedInfoDto.setServiceCode(hcsaServiceDto.getSvcCode());
                            appSvcRelatedInfoDto.setServiceName(hcsaServiceDto.getSvcName());
                            appSvcRelatedInfoDto.setServiceType(hcsaServiceDto.getSvcType());
                            List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                            List<HcsaSvcDocConfigDto> svcDocConfig = serviceConfigService.getAllHcsaSvcDocs(svcId);
                            NewApplicationHelper.setDocInfo(appGrpPrimaryDocDtos, appSvcDocDtos, primaryDocConfig, svcDocConfig);
                            ParamUtil.setSessionAttr(request,NewApplicationDelegator.SVC_DOC_CONFIG, (Serializable) svcDocConfig);
                            //set dupForPsn attr
                            NewApplicationHelper.setDupForPersonAttr(request,appSvcRelatedInfoDto);
                            //svc doc add align for dup for prem
                            NewApplicationHelper.addPremAlignForSvcDoc(svcDocConfig,appSvcDocDtos,newPremisesDtos);
                            appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtos);
                            //set svc doc title
                            Map<String,List<AppSvcDocDto>> reloadSvcDocMap = NewApplicationHelper.genSvcDocReloadMap(svcDocConfig,newPremisesDtos,appSvcRelatedInfoDto);
                            appSvcRelatedInfoDto.setMultipleSvcDoc(reloadSvcDocMap);
                            appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtos);
                            newSvcRelatedInfoDtos.add(appSvcRelatedInfoDto);
                            break;
                        }
                    }
                    NewApplicationHelper.addPremAlignForPrimaryDoc(primaryDocConfig,appGrpPrimaryDocDtos,newPremisesDtos);
                    //set primary doc title
                    Map<String,List<AppGrpPrimaryDocDto>> reloadPrimaryDocMap = NewApplicationHelper.genPrimaryDocReloadMap(primaryDocConfig,newPremisesDtos,appGrpPrimaryDocDtos);
                    appSubmissionDto.setMultipleGrpPrimaryDoc(reloadPrimaryDocMap);
                    ParamUtil.setSessionAttr(request,NewApplicationDelegator.PRIMARY_DOC_CONFIG, (Serializable) primaryDocConfig);
                    List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = serviceConfigService.loadLaboratoryDisciplines(svcId);
                    NewApplicationHelper.setLaboratoryDisciplinesInfo(appSubmissionDto, hcsaSvcSubtypeOrSubsumedDtos);
                    appSubmissionDto.setAppSvcRelatedInfoDtoList(newSvcRelatedInfoDtos);
                }
                //set DisciplineAllocationMap
                Map<String, List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap = appSubmissionService.getDisciplineAllocationDtoList(appSubmissionDto, svcId);
                ParamUtil.setRequestAttr(request, "reloadDisciplineAllocationMap", (Serializable) reloadDisciplineAllocationMap);
            }
            for (AppGrpPremisesDto appGrpPremisesDto : appSubmissionDto.getAppGrpPremisesDtoList()) {
                NewApplicationHelper.setWrkTime(appGrpPremisesDto);
            }
        }
    }

    private void svcRelatedInfoView(AppSubmissionDto appSubmissionDto,HttpServletRequest request,String serviceId){
            AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfoDtoByServiceId(appSubmissionDto.getAppSvcRelatedInfoDtoList(),serviceId);
            List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemesByServiceId = serviceConfigService.getHcsaServiceStepSchemesByServiceId(appSvcRelatedInfoDto.getServiceId());
            appSvcRelatedInfoDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemesByServiceId);
            String svcId = appSvcRelatedInfoDto.getServiceId();
            HcsaServiceDto hcsaServiceDto = serviceConfigService.getHcsaServiceDtoById(svcId);
            List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            if(appGrpPremisesDtoList!=null){
                for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList){
                    List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodList = appGrpPremisesDto.getAppPremPhOpenPeriodList();
                    if(appPremPhOpenPeriodList!=null){
                        for(AppPremPhOpenPeriodDto appPremPhOpenPeriodDto : appPremPhOpenPeriodList){
                            appPremPhOpenPeriodDto.setDayName(MasterCodeUtil.getCodeDesc(appPremPhOpenPeriodDto.getPhDate()));
                        }
                    }
                    String premises = appGrpPremisesDto.getPremisesIndexNo();
                    List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
                    List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
                    if(appSvcLaboratoryDisciplinesDtoList != null && appSvcLaboratoryDisciplinesDtoList.size() >0){
                        appSvcLaboratoryDisciplinesDtoList.removeIf(appSvcLaboratoryDisciplinesDto -> !appSvcLaboratoryDisciplinesDto.getPremiseVal().equals(premises));
                        for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvcLaboratoryDisciplinesDtoList){
                            List<AppSvcChckListDto> appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                            if(appSvcChckListDtoList!=null){
                                List<AppSvcChckListDto> entity = hcsaConfigFeClient.getAppSvcChckListDto(appSvcChckListDtoList).getEntity();
                                if(appSvcDisciplineAllocationDtoList!=null){
                                    for(AppSvcChckListDto appSvcChckListDto : entity){
                                        for(AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto : appSvcDisciplineAllocationDtoList){
                                            String chkLstConfId = appSvcDisciplineAllocationDto.getChkLstConfId();
                                            if(appSvcChckListDto.getChkLstConfId().equals(chkLstConfId)){
                                                appSvcDisciplineAllocationDto.setChkLstName(appSvcChckListDto.getChkName());
                                                break;
                                            }
                                        }
                                    }
                                }
                                appSvcLaboratoryDisciplinesDto.setAppSvcChckListDtoList(entity);
                            }
                        }
                        appSvcRelatedInfoDto.setAppSvcLaboratoryDisciplinesDtoList(appSvcLaboratoryDisciplinesDtoList);
                    }
                }
            }
            ParamUtil.setRequestAttr(request, HCSASERVICEDTO, hcsaServiceDto);
            ParamUtil.setSessionAttr(request, "currentPreviewSvcInfo", appSvcRelatedInfoDto);
    }

    private AppSvcRelatedInfoDto getAppSvcRelatedInfoDtoByServiceId( List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos,String serviceId){
        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos){
            if(serviceId.equalsIgnoreCase(appSvcRelatedInfoDto.getServiceId())){
                return appSvcRelatedInfoDto;
            }
        }
        return appSvcRelatedInfoDtos.get(0);
    }

    /**
     * StartStep: doReDquestInformationSubmit
     * prepare
     *
     * @param bpc
     * @throws
     */
    public void doRequestInformationSubmit(BaseProcessClass bpc) throws Exception {
        log.info(StringUtil.changeForLog("the do doRequestInformationSubmit start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        AppSubmissionRequestInformationDto appSubmissionRequestInformationDto = new AppSubmissionRequestInformationDto();
        String appGrpNo = appSubmissionDto.getAppGrpNo();
        List<ApplicationDto> entity = applicationFeClient.getApplicationsByGroupNo(appGrpNo).getEntity();
        String appNo="";
        for(ApplicationDto applicationDto : entity){
            if((ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION.equals(applicationDto.getStatus()))){
                    appNo=applicationDto.getApplicationNo();
                    break;
            }
        }
        Integer maxFileIndex = (Integer) ParamUtil.getSessionAttr(bpc.request,HcsaFileAjaxController.GLOBAL_MAX_INDEX_SESSION_ATTR);
        if(maxFileIndex == null){
            maxFileIndex = 0;
        }
        appSubmissionDto.setMaxFileIndex(maxFileIndex);
        List<AppPremisesRoutingHistoryDto> hisList;
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        String gatewayUrl = env.getProperty("iais.inter.gateway.url");
        Map<String, Object> params = IaisCommonUtils.genNewHashMap(1);
        params.put("appNo", appNo);
        hisList = IaisEGPHelper.callEicGatewayWithParamForList(gatewayUrl + "/v1/app-routing-history", HttpMethod.GET, params,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization(), AppPremisesRoutingHistoryDto.class).getEntity();
        if(hisList!=null){
            for(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto : hisList){
                if(ApplicationConsts.PROCESSING_DECISION_REQUEST_FOR_INFORMATION.equals(appPremisesRoutingHistoryDto.getProcessDecision())
                        || InspectionConstants.PROCESS_DECI_REQUEST_FOR_INFORMATION.equals(appPremisesRoutingHistoryDto.getProcessDecision())){
                    if(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(appPremisesRoutingHistoryDto.getAppStatus())){
                        appSubmissionRequestInformationDto.setRfiStatus(ApplicationConsts.PENDING_ASO_REPLY);
                    }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING.equals(appPremisesRoutingHistoryDto.getAppStatus())){
                        appSubmissionRequestInformationDto.setRfiStatus(ApplicationConsts.PENDING_PSO_REPLY);
                    }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS.equals(appPremisesRoutingHistoryDto.getAppStatus())){
                        appSubmissionRequestInformationDto.setRfiStatus(ApplicationConsts.PENDING_INP_REPLY);
                    }
                }
            }
        }
        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.OLDAPPSUBMISSIONDTO);
        //oldAppSubmissionDtos
//        List<AppSubmissionDto> appSubmissionDtoByGroupNo = appSubmissionService.getAppSubmissionDtoByGroupNo(appGrpNo);
        StringBuilder stringBuilder = new StringBuilder(10);
        stringBuilder.append(appSubmissionDto);
        String str=stringBuilder.toString();
        log.info(StringUtil.changeForLog("appSubmissionDto:" + str));
        stringBuilder.setLength(0);
        stringBuilder.append(oldAppSubmissionDto);
        str=stringBuilder.toString();
        log.info(StringUtil.changeForLog("oldAppSubmissionDto:" + str));
        Map<String, String> doComChangeMap = doComChange(appSubmissionDto, oldAppSubmissionDto);
        boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
        if (!doComChangeMap.isEmpty()) {
            //set audit
            NewApplicationHelper.setAudiErrMap(isRfi,appSubmissionDto.getAppType(),doComChangeMap,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
            ParamUtil.setRequestAttr(bpc.request, "Msg", doComChangeMap);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "preview");
            ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "N");
            return;
        }
        log.info("doComChange is ok ...");
        Map<String, String> map = appSubmissionService.doPreviewAndSumbit(bpc);
        if (!map.isEmpty()) {
            //set audit
            ParamUtil.setRequestAttr(bpc.request, "Msg", map);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "preview");
            ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "N");
            return;
        }
        //sync person data
//        Map<String,AppSvcPersonAndExtDto> personMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.PERSONSELECTMAP);
//        NewApplicationHelper.syncPsnData(appSubmissionDto,personMap);

        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        oldAppSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType()) || ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())){
            appSubmissionDto.setAppDeclarationDocDtos(oldAppSubmissionDto.getAppDeclarationDocDtos());
            appSubmissionDto.setAppDeclarationMessageDto(oldAppSubmissionDto.getAppDeclarationMessageDto());
            requestForChangeService.premisesDocToSvcDoc(appSubmissionDto);
        }
        //handler primary doc
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionService.handlerPrimaryDoc(appSubmissionDto.getAppGrpPremisesDtoList(),appSubmissionDto.getAppGrpPrimaryDocDtos());
        appSubmissionDto.setAppGrpPrimaryDocDtos(appGrpPrimaryDocDtos);

        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType()) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())){
            rfiLoadingCheckImplForRenew.beforeSubmitRfi(appSubmissionDto,appNo);
        }else {
            /*rfiLoadingExc.beforeSubmitRfi(appSubmissionDto,appNo);*/
        }
        String msgId = (String) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        appSubmissionDto.setRfiMsgId(msgId);
        appSubmissionRequestInformationDto.setAppSubmissionDto(appSubmissionDto);
        appSubmissionRequestInformationDto.setOldAppSubmissionDto(oldAppSubmissionDto);
        //update message statusdo
        //appSubmissionService.updateMsgStatus(msgId, MessageConstants.MESSAGE_STATUS_RESPONSE);
        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType()) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())){
            appSubmissionDto= appSubmissionService.submitRequestRfcRenewInformation(appSubmissionRequestInformationDto, bpc.process);
/*
            appSubmissionDto= applicationFeClient.saveRFCOrRenewRequestInformation(appSubmissionRequestInformationDto).getEntity();
*/
        }else {
            appSubmissionDto = appSubmissionService.submitRequestInformation(appSubmissionRequestInformationDto, bpc.process);
        }
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
            List<AppSubmissionDto> appSubmissionDtos = new ArrayList<>(1);
            appSubmissionDto.setAmountStr("N/A");
            appSubmissionDtos.add(appSubmissionDto);
            ParamUtil.setSessionAttr(bpc.request, "ackPageAppSubmissionDto", (Serializable) appSubmissionDtos);
        }
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "Y");
        ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, "The request for information save success");
        log.info(StringUtil.changeForLog("the do doRequestInformationSubmit end ...."));
    }

    public void doRenewSubmit(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do doRenewSubmit start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        List<ApplicationDto> applicationDtos = requestForChangeService.getAppByLicIdAndExcludeNew(appSubmissionDto.getLicenceId());
        String rfcErrOne = MessageUtil.getMessageDesc("RFC_ERR001");
        if (!applicationDtos.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "Y");
            ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, "error");
            ParamUtil.setRequestAttr(bpc.request, "content", rfcErrOne);
            return;
        }
        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, OLDAPPSUBMISSIONDTO);
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        List<AppGrpPremisesDto> oldAppSubmissionDtoAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
        boolean grpPremiseChange = false;
        if (appGrpPremisesDtoList.equals(oldAppSubmissionDtoAppGrpPremisesDtoList)) {
            grpPremiseChange = true;
        }
        if (!grpPremiseChange) {
            for (int i = 0; i < appGrpPremisesDtoList.size(); i++) {
                List<LicenceDto> attribute = (List<LicenceDto>) bpc.request.getSession().getAttribute("selectLicence" + i);
                if (attribute != null && !attribute.isEmpty()) {
                    for (LicenceDto licenceDto : attribute) {
                        List<ApplicationDto> appByLicIdAndExcludeNew =
                                requestForChangeService.getAppByLicIdAndExcludeNew(licenceDto.getId());
                        if (!IaisCommonUtils.isEmpty(appByLicIdAndExcludeNew)) {
                            ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "Y");
                            ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, "error");
                            ParamUtil.setRequestAttr(bpc.request, "content", rfcErrOne);
                            return;
                        }
                    }
                }
            }
        }
        boolean isAutoRfc = compareAndSendEmail(appSubmissionDto, oldAppSubmissionDto);
        appSubmissionDto.setAutoRfc(isAutoRfc);
        Map<String, String> map = appSubmissionService.doPreviewAndSumbit(bpc);
        if (!map.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "N");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "preview");
            return;
        }

        String draftNo = appSubmissionDto.getDraftNo();
        if (StringUtil.isEmpty(draftNo)) {
            draftNo = appSubmissionService.getDraftNo(appSubmissionDto.getAppType());
            appSubmissionDto.setDraftNo(draftNo);
        }
        //get appGroupNo
        String appGroupNo = appSubmissionService.getGroupNo(appSubmissionDto.getAppType());
        log.info(StringUtil.changeForLog("the appGroupNo is -->:" + appGroupNo));
        appSubmissionDto.setAppGrpNo(appGroupNo);
        //get Amount
        FeeDto feeDto = appSubmissionService.getGroupAmount(appSubmissionDto,NewApplicationHelper.isCharity(bpc.request));
        appSubmissionDto.setFeeInfoDtos(feeDto.getFeeInfoDtos());
        Double amount = feeDto.getTotal();
        log.info(StringUtil.changeForLog("the amount is -->:" + amount));
        appSubmissionDto.setAmount(amount);
        //judge is the preInspection
        PreOrPostInspectionResultDto preOrPostInspectionResultDto = appSubmissionService.judgeIsPreInspection(appSubmissionDto);
        if (preOrPostInspectionResultDto == null) {
            appSubmissionDto.setPreInspection(true);
            appSubmissionDto.setRequirement(true);
        } else {
            appSubmissionDto.setPreInspection(preOrPostInspectionResultDto.isPreInspection());
            appSubmissionDto.setRequirement(preOrPostInspectionResultDto.isRequirement());
        }

        //set Risk Score
        appSubmissionService.setRiskToDto(appSubmissionDto);

        appSubmissionDto = appSubmissionService.submitRenew(appSubmissionDto);
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);

        //back to renewal licence view page
        ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "N");
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "jump");
        ParamUtil.setRequestAttr(bpc.request, "jumpPmt", "Y");

        log.info(StringUtil.changeForLog("the do doRenewSubmit end ...."));
    }

    public static boolean eqAddFloorNo(AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto){
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        List<AppGrpPremisesDto> oldAppSubmissionDtoAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
        return EqRequestForChangeSubmitResultChange.eqAddFloorNo(appGrpPremisesDtoList,oldAppSubmissionDtoAppGrpPremisesDtoList);
    }
    /**
         * StartStep: doRequestForChangeSubmit
     *
     * @param bpc
     *
     */
    public void doRequestForChangeSubmit(BaseProcessClass bpc) throws Exception {
        //validate reject  apst050
        log.info(StringUtil.changeForLog("the do doRequestForChangeSubmit start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");

        String rfc_err020 = MessageUtil.getMessageDesc("RFC_ERR020");
        String serviceConfig = (String) bpc.request.getSession().getAttribute("serviceConfig");

        stepColorService.setStepColor(coMap,serviceConfig,appSubmissionDto);

        Integer maxFileIndex = (Integer) ParamUtil.getSessionAttr(bpc.request,HcsaFileAjaxController.GLOBAL_MAX_INDEX_SESSION_ATTR);
        if(maxFileIndex == null){
            maxFileIndex = 0;
        }
        appSubmissionDto.setMaxFileIndex(maxFileIndex);
        List<ApplicationDto> applicationDtos = requestForChangeService.getAppByLicIdAndExcludeNew(appSubmissionDto.getLicenceId());
        Map<String, AppSvcPersonAndExtDto> personMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.PERSONSELECTMAP);
        //todo chnage edit
        AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
        appEditSelectDto.setServiceEdit(false);
        appEditSelectDto.setPremisesEdit(true);
        appEditSelectDto.setPremisesListEdit(false);
        appEditSelectDto.setDocEdit(true);
        appEditSelectDto.setPoEdit(true);
        appSubmissionDto.setIsNeedNewLicNo(AppConsts.NO);
        Map<String, String> map = appSubmissionService.doPreviewAndSumbit(bpc);
        boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.OLDAPPSUBMISSIONDTO);
        List<AppGrpPremisesDto> oldAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        boolean oneOfHciName=false;
        if(oldAppGrpPremisesDtoList!=null&& appGrpPremisesDtoList!=null){
            for (int i = 0; i < appGrpPremisesDtoList.size(); i++) {
                boolean eqHciNameChange = EqRequestForChangeSubmitResultChange.eqHciNameChange(appGrpPremisesDtoList.get(i), oldAppGrpPremisesDtoList.get(i));
                if(eqHciNameChange){
                    AppDeclarationMessageDto appDeclarationMessageDto = appSubmissionService.getAppDeclarationMessageDto(bpc.request,ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
                    appSubmissionDto.setAppDeclarationMessageDto(appDeclarationMessageDto);
                    DeclarationsUtil.declarationsValidate(map,appDeclarationMessageDto,ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
                    appSubmissionDto.setAppDeclarationDocDtos(appSubmissionService.getDeclarationFiles(appSubmissionDto.getAppType(), bpc.request));
                    String preQuesKindly = appSubmissionDto.getAppDeclarationMessageDto().getPreliminaryQuestionKindly();
                    appSubmissionService.validateDeclarationDoc(map, appSubmissionService.getFileAppendId(appSubmissionDto.getAppType()),
                            preQuesKindly ==null ? false : "0".equals(preQuesKindly), bpc.request);
                    appSubmissionService.initDeclarationFiles(appSubmissionDto.getAppDeclarationDocDtos(),appSubmissionDto.getAppType(),bpc.request);
                    oneOfHciName=true;
                }
            }
        }
        if(!oneOfHciName){
            appSubmissionDto.setAppDeclarationMessageDto(null);
            appSubmissionDto.setAppDeclarationDocDtos(null);
        }
        if (!map.isEmpty()) {
            //set audit
            ParamUtil.setRequestAttr(bpc.request, "Msg", map);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "preview");
            ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "N");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(map));
            return;
        }
        String effectiveDateStr = appSubmissionDto.getEffectiveDateStr();
        Date effectiveDate = appSubmissionDto.getEffectiveDate();
        log.info(StringUtil.changeForLog("effectiveDate"+effectiveDate));

        Boolean otherOperation = requestForChangeService.isOtherOperation(appSubmissionDto.getLicenceId());
        if (!otherOperation) {
            //set audit
            Map<String,String> appealOrCesed = IaisCommonUtils.genNewHashMap();
            appealOrCesed.put("appealOrCesed",String.valueOf(otherOperation));
            NewApplicationHelper.setAudiErrMap(isRfi,appSubmissionDto.getAppType(),appealOrCesed,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
            bpc.request.setAttribute("rfcPendingApplication","errorRfcPendingApplication");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "preview");
            ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "N");
            return;
        }
        appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
        boolean isAutoRfc = compareAndSendEmail(appSubmissionDto, oldAppSubmissionDto);
        boolean eqAddFloorNo = eqAddFloorNo(appSubmissionDto, oldAppSubmissionDto);
        //is need to pay ?
        String appGroupNo = appSubmissionService.getGroupNo(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        boolean licHadSubmit = !IaisCommonUtils.isEmpty(applicationDtos);
        if (licHadSubmit) {
            //set audit
            Map<String,String> errMap2 = IaisCommonUtils.genNewHashMap();
            errMap2.put("licenceHadSubmit",String.valueOf(licHadSubmit));
            NewApplicationHelper.setAudiErrMap(isRfi,appSubmissionDto.getAppType(),errMap2,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
            bpc.request.setAttribute("rfcPendingApplication","errorRfcPendingApplication");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "preview");
            ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "N");
            return;
        }

        List<AppGrpPremisesDto> oldAppSubmissionDtoAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
        String licenceId = appSubmissionDto.getLicenceId();
        LicenceDto licenceById = requestForChangeService.getLicenceById(licenceId);
        if(licenceById.getSvcName()!=null){
            List<String> serviceIds=IaisCommonUtils.genNewArrayList();
            HcsaServiceDto activeHcsaServiceDtoByName = serviceConfigService.getActiveHcsaServiceDtoByName(licenceById.getSvcName());
            if(activeHcsaServiceDtoByName!=null){
                serviceIds.add(activeHcsaServiceDtoByName.getId());
                for(AppGrpPremisesDto appGrpPremisesDto : oldAppSubmissionDtoAppGrpPremisesDtoList){
                    String premisesType = appGrpPremisesDto.getPremisesType();
                    boolean b = requestForChangeService.serviceConfigIsChange(serviceIds, premisesType);
                    if(!b){
                        rfc_err020=rfc_err020.replace("{ServiceName}",licenceById.getSvcName());
                        bpc.request.setAttribute("SERVICE_CONFIG_CHANGE",rfc_err020);
                        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "preview");
                        ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "N");
                        return;
                    }
                }
            }
        }
        requestForChangeService.setRelatedInfoBaseServiceId(appSubmissionDto);
        String baseServiceId = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getBaseServiceId();
        if(StringUtil.isEmpty(baseServiceId)){
            rfc_err020=rfc_err020.replace("{ServiceName}",licenceById.getSvcName());
            bpc.request.setAttribute("SERVICE_CONFIG_CHANGE",rfc_err020);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "preview");
            ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "N");
            return;
        }

        boolean grpPremiseIsChange = false;
        boolean serviceIsChange;
        boolean docIsChange ;
        List<AppGrpPrimaryDocDto> dtoAppGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
        List<AppGrpPrimaryDocDto> oldAppGrpPrimaryDocDtos = oldAppSubmissionDto.getAppGrpPrimaryDocDtos();
        docIsChange = EqRequestForChangeSubmitResultChange.eqDocChange(dtoAppGrpPrimaryDocDtos, oldAppGrpPrimaryDocDtos);
        appEditSelectDto.setDocEdit(docIsChange);
        /*
        * 1.add if migrated -> hci name if no change to 2  ,if change to 0 (premises) first rfc or renew
        * 2. migrated ->premises -> is 0 all to -> 0 . can 2-> 0. but cannot 0->2
        *
        * */
        if (appGrpPremisesDtoList != null) {
            grpPremiseIsChange = EqRequestForChangeSubmitResultChange.eqGrpPremises(appGrpPremisesDtoList, oldAppSubmissionDtoAppGrpPremisesDtoList);
        }
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList();
        serviceIsChange = EqRequestForChangeSubmitResultChange.eqServiceChange(appSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList);
        log.info(StringUtil.changeForLog("serviceIsChange"+serviceIsChange));
        appEditSelectDto.setServiceEdit(serviceIsChange);
        appEditSelectDto.setPremisesEdit(grpPremiseIsChange);
        appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
        appSubmissionDto.setChangeSelectDto(appEditSelectDto);
        List<AppSubmissionDto> appSubmissionDtos = IaisCommonUtils.genNewArrayList();
        boolean isCharity = NewApplicationHelper.isCharity(bpc.request);
        AmendmentFeeDto amendmentFeeDto = getAmendmentFeeDto(appSubmissionDto, oldAppSubmissionDto,isCharity);
        if (!amendmentFeeDto.getChangeInHCIName() && !amendmentFeeDto.getChangeInLocation()) {
            List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
            if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
                for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                    appGrpPremisesDto.setNeedNewLicNo(Boolean.FALSE);
                }
            }
        }
        if(eqAddFloorNo){
            amendmentFeeDto.setChangeInLocation(Boolean.TRUE);
        }
        FeeDto feeDto = appSubmissionService.getGroupAmendAmount(amendmentFeeDto);
        double amount = feeDto.getTotal();
        log.info(StringUtil.changeForLog("the amount is -->:" + amount));
        appSubmissionDto.setAmount(amount);
        if(grpPremiseIsChange){
            if (appGrpPremisesDtoList != null) {
                int size = appGrpPremisesDtoList.size();
                for (int i = 0; i < size; i++) {
                    //Get the selected license from page to save
                    //eq change premises hci code a-> b
                    appGrpPremisesDtoList.get(i).setOldHciCode(oldAppSubmissionDtoAppGrpPremisesDtoList.get(i).getHciCode());
                    boolean eqHciNameChange = EqRequestForChangeSubmitResultChange.eqHciNameChange(appGrpPremisesDtoList.get(i), oldAppSubmissionDtoAppGrpPremisesDtoList.get(i));
                    if(eqHciNameChange){
                        appGrpPremisesDtoList.get(i).setHciNameChanged(0);
                    }
                    List<LicenceDto> attribute = (List<LicenceDto>) bpc.request.getSession().getAttribute("selectLicence" + i);
                    if (attribute != null) {
                        for (LicenceDto string : attribute) {
                            HcsaServiceDto activeHcsaServiceDtoByName = serviceConfigService.getActiveHcsaServiceDtoByName(string.getSvcName());
                            List<String> serviceIds=IaisCommonUtils.genNewArrayList();
                            serviceIds.add(activeHcsaServiceDtoByName.getId()   );
                            boolean configIsChange = requestForChangeService.serviceConfigIsChange(serviceIds, appGrpPremisesDtoList.get(i).getPremisesType());
                            if(!configIsChange){
                                rfc_err020=rfc_err020.replace("{ServiceName}",licenceById.getSvcName());
                                bpc.request.setAttribute("SERVICE_CONFIG_CHANGE",rfc_err020);
                                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "preview");
                                ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "N");
                                return;
                            }
                            AppSubmissionDto appSubmissionDtoByLicenceId = requestForChangeService.getAppSubmissionDtoByLicenceId(string.getId());
                            requestForChangeService.setRelatedInfoBaseServiceId(appSubmissionDtoByLicenceId);
                            String baseServiceId1 = appSubmissionDtoByLicenceId.getAppSvcRelatedInfoDtoList().get(0).getBaseServiceId();
                            if(StringUtil.isEmpty(baseServiceId1)){
                                rfc_err020=rfc_err020.replace("{ServiceName}",licenceById.getSvcName());
                                bpc.request.setAttribute("SERVICE_CONFIG_CHANGE",rfc_err020);
                                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "preview");
                                ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "N");
                                return;
                            }
                            Boolean changeOtherOperation = requestForChangeService.isOtherOperation(string.getId());
                            if (!changeOtherOperation) {
                                bpc.request.setAttribute("rfcPendingApplication","errorRfcPendingApplication");
                                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "preview");
                                ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "N");
                                return;
                            }
                            List<ApplicationDto> changeApplicationDtos = requestForChangeService.getAppByLicIdAndExcludeNew(string.getId());
                            if (!IaisCommonUtils.isEmpty(changeApplicationDtos)) {
                                bpc.request.setAttribute("rfcPendingApplication","errorRfcPendingApplication");
                                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "preview");
                                ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "N");
                                return;
                            }

                            appSubmissionDtoByLicenceId.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
                            appSubmissionService.transform(appSubmissionDtoByLicenceId, appSubmissionDto.getLicenseeId());
                            boolean groupLic = appSubmissionDtoByLicenceId.isGroupLic();
                            boolean equals = false;
                            String address = appSubmissionDto.getAppGrpPremisesDtoList().get(i).getAddress();
                            String premisesIndexNo;
                            int hciNameChange;
                            if (groupLic) {
                                amendmentFeeDto.setChangeInLocation(equals);
                                AppGrpPremisesDto appGrpPremisesDto = oldAppSubmissionDto.getAppGrpPremisesDtoList().get(i);
                                hciNameChange=appGrpPremisesDto.getHciNameChanged();
                                boolean eqHciNameChange1 = EqRequestForChangeSubmitResultChange.eqHciNameChange(appGrpPremisesDtoList.get(i), appGrpPremisesDto);
                                if(eqHciNameChange1){
                                    hciNameChange=0;
                                }
                                premisesIndexNo = appGrpPremisesDto.getPremisesIndexNo();
                                boolean b = EqRequestForChangeSubmitResultChange.compareHciName(appGrpPremisesDto, appSubmissionDto.getAppGrpPremisesDtoList().get(i));
                                amendmentFeeDto.setChangeInHCIName(!b);
                                String grpAddress = appGrpPremisesDto.getAddress();
                                equals = grpAddress.equals(address);
                                amendmentFeeDto.setChangeInLocation(!equals);
                                List<AppGrpPremisesDto> rfcAppGrpPremisesDtoList = appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList();
                                if (rfcAppGrpPremisesDtoList != null) {
                                    for (AppGrpPremisesDto appGrpPremisesDto1 : rfcAppGrpPremisesDtoList) {
                                        appGrpPremisesDto1.setGroupLicenceFlag(string.getId());
                                    }
                                }
                                appSubmissionDtoByLicenceId.setPartPremise(true);
                            } else {
                                String oldAddress = appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList().get(0).getAddress();
                                equals = oldAddress.equals(address);
                                boolean b = EqRequestForChangeSubmitResultChange.compareHciName(appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList().get(0), appSubmissionDto.getAppGrpPremisesDtoList().get(i));
                                amendmentFeeDto.setChangeInHCIName(!b);
                                amendmentFeeDto.setChangeInLocation(!equals);
                                premisesIndexNo = appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList().get(0).getPremisesIndexNo();
                                hciNameChange=appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList().get(0).getHciNameChanged();
                                boolean eqHciNameChange1 = EqRequestForChangeSubmitResultChange.eqHciNameChange(appGrpPremisesDtoList.get(i), appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList().get(0));
                                if(eqHciNameChange1){
                                    hciNameChange=0;
                                }
                            }

                            appSubmissionDtoByLicenceId.setGroupLic(groupLic);
                            appSubmissionDtoByLicenceId.setAmount(amount);
                            AppGrpPremisesDto appGrpPremisesDto = appSubmissionDto.getAppGrpPremisesDtoList().get(i);
                            List<AppGrpPremisesDto> appGrpPremisesDtos = new ArrayList<>(1);
                            AppGrpPremisesDto copyMutableObject = (AppGrpPremisesDto) CopyUtil.copyMutableObject(appGrpPremisesDto);
                            appGrpPremisesDtos.add(copyMutableObject);
                            if (groupLic) {
                                appGrpPremisesDtos.get(0).setGroupLicenceFlag(string.getId());
                            }
                            appSubmissionDtoByLicenceId.setAppGrpPremisesDtoList(appGrpPremisesDtos);
                            appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList().get(0).setPremisesIndexNo(premisesIndexNo);
                            appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList().get(0).setHciNameChanged(hciNameChange);
                            appSubmissionDtoByLicenceId.setAppGrpNo(appGroupNo);
                            PreOrPostInspectionResultDto preOrPostInspectionResultDto = appSubmissionService.judgeIsPreInspection(appSubmissionDtoByLicenceId);
                            if (preOrPostInspectionResultDto == null) {
                                appSubmissionDtoByLicenceId.setPreInspection(true);
                                appSubmissionDtoByLicenceId.setRequirement(true);
                            } else {
                                appSubmissionDtoByLicenceId.setPreInspection(preOrPostInspectionResultDto.isPreInspection());
                                appSubmissionDtoByLicenceId.setRequirement(preOrPostInspectionResultDto.isRequirement());
                            }
                            boolean eqAddFloorNo1 = eqAddFloorNo(appSubmissionDtoByLicenceId, oldAppSubmissionDto);
                            if (!amendmentFeeDto.getChangeInHCIName() && !amendmentFeeDto.getChangeInLocation() &&!eqAddFloorNo1) {
                                appSubmissionDtoByLicenceId.setIsNeedNewLicNo(AppConsts.NO);
                                for(AppGrpPremisesDto appGrpPremisesDto1 : appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList()){
                                    appGrpPremisesDto1.setNeedNewLicNo(Boolean.FALSE);
                                }
                            } else {
                                appSubmissionDtoByLicenceId.setIsNeedNewLicNo(AppConsts.YES);
                                for(AppGrpPremisesDto appGrpPremisesDto1 : appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList()){
                                    appGrpPremisesDto1.setNeedNewLicNo(Boolean.TRUE);
                                }
                            }
                            appSubmissionDtoByLicenceId.setAutoRfc(isAutoRfc);
                            appEditSelectDto.setPremisesEdit(true);
                            appSubmissionDtoByLicenceId.setAppEditSelectDto(appEditSelectDto);
                            appSubmissionDtoByLicenceId.setChangeSelectDto(appEditSelectDto);
                            appSubmissionDtoByLicenceId.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
                            appSubmissionDtoByLicenceId.setStatus(ApplicationConsts.APPLICATION_STATUS_REQUEST_FOR_CHANGE_SUBMIT);
                            String draftNo = appSubmissionDtoByLicenceId.getDraftNo();
                            if (StringUtil.isEmpty(draftNo)) {
                                appSubmissionService.setDraftNo(appSubmissionDtoByLicenceId);
                            }
                            if (0.0 == amount) {
                                appSubmissionDtoByLicenceId.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_PENDING_PAYMENT);
                                appSubmissionDtoByLicenceId.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_NOT_PAYMENT);
                            } else {
                                appSubmissionDtoByLicenceId.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_PENDING_PAYMENT);
                            }
                            appSubmissionDtoByLicenceId.setGetAppInfoFromDto(true);
                            RequestForChangeMenuDelegator.oldPremiseToNewPremise(appSubmissionDtoByLicenceId);
                            requestForChangeService.premisesDocToSvcDoc(appSubmissionDtoByLicenceId);
                            appSubmissionDtoByLicenceId.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                            appSubmissionDtoByLicenceId.setEffectiveDate(effectiveDate);
                            appSubmissionDtoByLicenceId.setEffectiveDateStr(effectiveDateStr);
                            NewApplicationHelper.syncPsnData(appSubmissionDtoByLicenceId, personMap);
                            appSubmissionDtos.add(appSubmissionDtoByLicenceId);
                        }
                    }


                }
            }
        }


        appSubmissionDto.setAutoRfc(isAutoRfc);
        String draftNo = appSubmissionDto.getDraftNo();
        if (StringUtil.isEmpty(draftNo)) {
            draftNo = appSubmissionService.getDraftNo(appSubmissionDto.getAppType());
            appSubmissionDto.setDraftNo(draftNo);
        }
        //appSubmissionDto =checkAndSetData(bpc.request, appSubmissionDto);
        //get appGroupNo
        log.info(StringUtil.changeForLog("the appGroupNo is -->:" + appGroupNo));
        appSubmissionDto.setAppGrpNo(appGroupNo);
        //judge is the preInspection
        PreOrPostInspectionResultDto preOrPostInspectionResultDto = appSubmissionService.judgeIsPreInspection(appSubmissionDto);
        if (preOrPostInspectionResultDto == null) {
            appSubmissionDto.setPreInspection(true);
            appSubmissionDto.setRequirement(true);
        } else {
            appSubmissionDto.setPreInspection(preOrPostInspectionResultDto.isPreInspection());
            appSubmissionDto.setRequirement(preOrPostInspectionResultDto.isRequirement());
        }
        //set Risk Score
        appSubmissionService.setRiskToDto(appSubmissionDto);
        if (0.0 == amount) {
            appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_PENDING_PAYMENT);
            appSubmissionDto.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_NOT_PAYMENT);

        } else {
            appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_PENDING_PAYMENT);
        }
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        String isrfiSuccess = "N";
        requestForChangeService.premisesDocToSvcDoc(appSubmissionDto);
        requestForChangeService.premisesDocToSvcDoc(oldAppSubmissionDto);
        appSubmissionDto.setPartPremise(appSubmissionDto.isGroupLic());
        appSubmissionDto.setGetAppInfoFromDto(true);
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        List<AppSubmissionDto> appSubmissionDtoList = IaisCommonUtils.genNewArrayList();
        List<AppSubmissionDto> autoSaveAppsubmission = IaisCommonUtils.genNewArrayList();
        List<AppSubmissionDto> notAutoSaveAppsubmission = IaisCommonUtils.genNewArrayList();
        if (grpPremiseIsChange || docIsChange) {
            appSubmissionDto.setOneLicDoRenew(true);
            if(appSubmissionDto.isGroupLic()){
                List<AppGrpPremisesDto> appGrpPremisesDtos = groupLicecePresmiseChange(appSubmissionDto.getAppGrpPremisesDtoList(), oldAppSubmissionDto.getAppGrpPremisesDtoList());
                if(appGrpPremisesDtos.size()!=appSubmissionDto.getAppGrpPremisesDtoList().size()){
                    appSubmissionDto.setPartPremise(true);
                }else {
                    appSubmissionDto.setPartPremise(false);
                }
                appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtos);
            }
            if (isAutoRfc) {
                appSubmissionDto.setIsNeedNewLicNo(AppConsts.NO);
                for(AppGrpPremisesDto appGrpPremisesDto :  appSubmissionDto.getAppGrpPremisesDtoList()){
                    appGrpPremisesDto.setNeedNewLicNo(Boolean.FALSE);
                }
                appSubmissionDtos.add(appSubmissionDto);
                autoSaveAppsubmission.addAll(appSubmissionDtos);
            } else {
                appSubmissionDto.setIsNeedNewLicNo(AppConsts.YES);
                for(AppGrpPremisesDto appGrpPremisesDto :  appSubmissionDto.getAppGrpPremisesDtoList()){
                    if(0.0==amount){
                        appGrpPremisesDto.setNeedNewLicNo(Boolean.FALSE);
                    }else {
                        appGrpPremisesDto.setNeedNewLicNo(Boolean.TRUE);
                    }

                }
                appSubmissionDtos.add(appSubmissionDto);
                notAutoSaveAppsubmission.addAll(appSubmissionDtos);
            }

        }
        ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", isrfiSuccess);
        if ("Y".equals(isrfiSuccess)) {
            AppSubmissionDto appSubmissionDto1 = getAppSubmissionDto(bpc.request);
            ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto1);
        }
        /* String submissionId1 = generateIdClient.getSeqId().getEntity();*/
        AppSubmissionListDto appSubmissionListDto1 = new AppSubmissionListDto();
        Long l1 = System.currentTimeMillis();
        appSubmissionListDto1.setEventRefNo(l1.toString());
        boolean appGrpMisc = false;
        if (serviceIsChange) {
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
            String licenseeId = loginContext.getLicenseeId();
            List<AppSubmissionDto> personAppSubmissionList = serviceInfoChangeEffectPersonForRFC.personContact(licenseeId, appSubmissionDto, oldAppSubmissionDto);
            //sync other application

            AppSubmissionDto personAppsubmit = getPersonAppsubmit(oldAppSubmissionDto, appSubmissionDto, bpc);

            personAppsubmit.setPartPremise(appSubmissionDto.isPartPremise());
            personAppsubmit.setOneLicDoRenew(true);
            // true is auto
            boolean autoRfc = personAppsubmit.isAutoRfc();
            if (!autoRfc) {
                if (!notAutoSaveAppsubmission.isEmpty()) {
                    AppSubmissionDto appSubmissionDto1 = notAutoSaveAppsubmission.get(notAutoSaveAppsubmission.size() - 1);
                    AppEditSelectDto appEditSelectDto1 = appSubmissionDto1.getAppEditSelectDto();
                    appEditSelectDto1.setPremisesListEdit(grpPremiseIsChange);
                    appEditSelectDto1.setDocEdit(docIsChange);
                    appSubmissionDto1.setAppEditSelectDto(appEditSelectDto1);
                    appSubmissionDto1.setAppSvcRelatedInfoDtoList(personAppsubmit.getAppSvcRelatedInfoDtoList());
                    appSubmissionDto1.setAppDeclarationDocDtos(personAppsubmit.getAppDeclarationDocDtos());
                    appSubmissionDto1.setAppDeclarationMessageDto(personAppsubmit.getAppDeclarationMessageDto());
                } else if(!autoSaveAppsubmission.isEmpty()){
                    AppSubmissionDto appSubmissionDto1 =autoSaveAppsubmission.get(autoSaveAppsubmission.size() - 1);
                    appSubmissionDto1.setAppSvcRelatedInfoDtoList(personAppsubmit.getAppSvcRelatedInfoDtoList());
                    appSubmissionDto1.setAutoRfc(false);
                    AppEditSelectDto appEditSelectDto1 = appSubmissionDto1.getAppEditSelectDto();
                    appEditSelectDto1.setPremisesListEdit(grpPremiseIsChange);
                    appEditSelectDto1.setDocEdit(docIsChange);
                    appSubmissionDto1.setAppEditSelectDto(appEditSelectDto1);
                    String groupNo = appSubmissionService.getGroupNo(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
                    appSubmissionDto1.setAppGrpNo(groupNo);
                    notAutoSaveAppsubmission.add(appSubmissionDto1);
                    autoSaveAppsubmission.remove(autoSaveAppsubmission.size() - 1);
                }else {
                    notAutoSaveAppsubmission.add(personAppsubmit);
                }
            } else {
                if (!autoSaveAppsubmission.isEmpty()) {
                    AppSubmissionDto appSubmissionDto1 = autoSaveAppsubmission.get(autoSaveAppsubmission.size() - 1);
                    appSubmissionDto1.setAppSvcRelatedInfoDtoList(personAppsubmit.getAppSvcRelatedInfoDtoList());
                } else if(!notAutoSaveAppsubmission.isEmpty()){
                    AppSubmissionDto appSubmissionDto1 = notAutoSaveAppsubmission.get(notAutoSaveAppsubmission.size() - 1);
                    AppEditSelectDto appEditSelectDto1 = appSubmissionDto1.getAppEditSelectDto();
                    appEditSelectDto1.setDocEdit(docIsChange);
                    appEditSelectDto1.setServiceEdit(serviceIsChange);
                    appSubmissionDto1.setAppEditSelectDto(appEditSelectDto1);
                    appSubmissionDto1.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_PENDING_PAYMENT);
                    appSubmissionDto1.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_NOT_PAYMENT);
                    appSubmissionDto1.setIsNeedNewLicNo(AppConsts.YES);
                    for(AppGrpPremisesDto appGrpPremisesDto : appSubmissionDto1.getAppGrpPremisesDtoList()){
                        appGrpPremisesDto.setNeedNewLicNo(Boolean.TRUE);
                    }
                    appSubmissionDto1.setAppSvcRelatedInfoDtoList(personAppsubmit.getAppSvcRelatedInfoDtoList());
                    appSubmissionDto1.setAppDeclarationMessageDto(personAppsubmit.getAppDeclarationMessageDto());
                    appSubmissionDto1.setAppDeclarationDocDtos(personAppsubmit.getAppDeclarationDocDtos());
                }else {
                    autoSaveAppsubmission.add(personAppsubmit);
                }
                if (!notAutoSaveAppsubmission.isEmpty()) {
                    appGrpMisc = true;
                }
            }
            autoSaveAppsubmission.addAll(personAppSubmissionList);

        }


        String auto = generateIdClient.getSeqId().getEntity();
        String notAuto = generateIdClient.getSeqId().getEntity();
        AppSubmissionListDto autoAppSubmissionListDto = new AppSubmissionListDto();
        AppSubmissionListDto notAutoAppSubmissionListDto = new AppSubmissionListDto();
        Long autoTime = System.currentTimeMillis();
        Long notAutoTime = System.currentTimeMillis();
        autoAppSubmissionListDto.setEventRefNo(autoTime.toString());
        notAutoAppSubmissionListDto.setEventRefNo(notAutoTime.toString());
        List<AppSubmissionDto> ackPageAppSubmissionDto=new ArrayList<>(2);
        if (!notAutoSaveAppsubmission.isEmpty()) {
            for (AppSubmissionDto appSubmissionDto1 : notAutoSaveAppsubmission) {
                NewApplicationHelper.syncPsnData(appSubmissionDto1, personMap);
            }
            //set missing data
            AuditTrailDto currentAuditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
            notAutoSaveAppsubmission.get(0).setAuditTrailDto(currentAuditTrailDto);
            for (AppSubmissionDto appSubmissionDto1 : notAutoSaveAppsubmission) {
                appSubmissionDto1.setEffectiveDateStr(effectiveDateStr);
                appSubmissionDto1.setEffectiveDate(effectiveDate);
                AppDeclarationMessageDto appDeclarationMessageDto = appSubmissionDto1.getAppDeclarationMessageDto();
                if(appDeclarationMessageDto!=null&&appDeclarationMessageDto.getEffectiveDt()!=null){
                    appSubmissionDto1.setEffectiveDate(appDeclarationMessageDto.getEffectiveDt());
                    appSubmissionDto1.setEffectiveDateStr(new SimpleDateFormat("dd/MM/yyyy").format(appDeclarationMessageDto.getEffectiveDt()));
                }
            }
            List<AppSubmissionDto> appSubmissionDtos1 = requestForChangeService.saveAppsForRequestForGoupAndAppChangeByList(notAutoSaveAppsubmission);
            for(AppSubmissionDto appSubmissionDto1 : appSubmissionDtos1){
                appSubmissionDto1.setEffectiveDateStr(effectiveDateStr);
                appSubmissionDto1.setEffectiveDate(effectiveDate);
                AppDeclarationMessageDto appDeclarationMessageDto = appSubmissionDto1.getAppDeclarationMessageDto();
                if(appDeclarationMessageDto!=null&&appDeclarationMessageDto.getEffectiveDt()!=null){
                    appSubmissionDto1.setEffectiveDate(appDeclarationMessageDto.getEffectiveDt());
                    appSubmissionDto1.setEffectiveDateStr(new SimpleDateFormat("dd/MM/yyyy").format(appDeclarationMessageDto.getEffectiveDt()));
                }
            }
            notAutoAppSubmissionListDto.setAppSubmissionDtos(appSubmissionDtos1);
            eventBusHelper.submitAsyncRequest(notAutoAppSubmissionListDto, notAuto, EventBusConsts.SERVICE_NAME_APPSUBMIT,
                    EventBusConsts.OPERATION_REQUEST_INFORMATION_SUBMIT, notAutoTime.toString(), bpc.process);
            appSubmissionDtos1.get(0).getAppSvcRelatedInfoDtoList().get(0).setGroupNo(appSubmissionDtos1.get(0).getAppGrpNo());
            Double ackPageAmount=0.0;
            for (AppSubmissionDto appSubmissionDto1 : notAutoSaveAppsubmission) {
                Double amount1 = appSubmissionDto1.getAmount();
                ackPageAmount=ackPageAmount+amount1;
                String s = Formatter.formatterMoney(amount1);
                appSubmissionDto1.setAmountStr(s);
            }
            for(AppSubmissionDto appSubmissionDto1 : appSubmissionDtos1){
                appSubmissionDto1.setAppGrpNo(appSubmissionDtos1.get(0).getAppGrpNo());
            }
            AppSubmissionDto o1 = (AppSubmissionDto)CopyUtil.copyMutableObject(appSubmissionDtos1.get(0));
            o1.setAmount(ackPageAmount);
            String s = Formatter.formatterMoney(ackPageAmount);
            o1.setAmountStr(s);
            ackPageAppSubmissionDto.add(o1);
            appSubmissionDtoList.addAll(appSubmissionDtos1);
            appSubmissionDto.setAppGrpId(appSubmissionDtos1.get(0).getAppGrpId());
        }
        if (!autoSaveAppsubmission.isEmpty()) {
            for (AppSubmissionDto appSubmissionDto1 : autoSaveAppsubmission) {
                NewApplicationHelper.syncPsnData(appSubmissionDto1, personMap);
                List<AppGrpPremisesDto> appGrpPremisesDtoList1 = appSubmissionDto1.getAppGrpPremisesDtoList();
                for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList1){
                    appGrpPremisesDto.setSelfAssMtFlag(4);
                }
                List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList1 = appSubmissionDto1.getAppSvcRelatedInfoDtoList();
                Iterator<AppSvcRelatedInfoDto> iterator = appSvcRelatedInfoDtoList1.iterator();
                while (iterator.hasNext()){
                    AppSvcRelatedInfoDto next = iterator.next();
                    List<AppSvcVehicleDto> appSvcVehicleDtoList = next.getAppSvcVehicleDtoList();
                    if(appSvcVehicleDtoList!=null&&!appSvcVehicleDtoList.isEmpty()){
                        for (AppSvcVehicleDto appSvcVehicleDto : appSvcVehicleDtoList) {
                            appSvcVehicleDto.setStatus(ApplicationConsts.VEHICLE_STATUS_APPROVE);
                        }
                    }
                }
            }
            //set missing data
            AuditTrailDto currentAuditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
            autoSaveAppsubmission.get(0).setAuditTrailDto(currentAuditTrailDto);
            for (AppSubmissionDto appSubmissionDto1 : autoSaveAppsubmission) {
                appSubmissionDto1.setEffectiveDateStr(effectiveDateStr);
                appSubmissionDto1.setEffectiveDate(effectiveDate);
            }
            List<AppSubmissionDto> appSubmissionDtos1 = requestForChangeService.saveAppsForRequestForGoupAndAppChangeByList(autoSaveAppsubmission);
            for(AppSubmissionDto appSubmissionDto1 : appSubmissionDtos1){
                appSubmissionDto1.setEffectiveDateStr(effectiveDateStr);
                appSubmissionDto1.setEffectiveDate(effectiveDate);
            }
            autoAppSubmissionListDto.setAppSubmissionDtos(appSubmissionDtos1);
            eventBusHelper.submitAsyncRequest(autoAppSubmissionListDto, auto, EventBusConsts.SERVICE_NAME_APPSUBMIT,
                    EventBusConsts.OPERATION_REQUEST_INFORMATION_SUBMIT, autoTime.toString(), bpc.process);
            appSubmissionDtos1.get(0).getAppSvcRelatedInfoDtoList().get(0).setGroupNo(appSubmissionDtos1.get(0).getAppGrpNo());
            double t = 0.0;
            if (appGrpMisc) {
                if (!appSubmissionDtoList.isEmpty()) {
                    String grpId = appSubmissionDtoList.get(0).getAppGrpId();
                    String grpId1 = appSubmissionDtos1.get(0).getAppGrpId();
                    AppGroupMiscDto appGroupMiscDto = new AppGroupMiscDto();
                    appGroupMiscDto.setAppGrpId(grpId);
                    appGroupMiscDto.setMiscValue(grpId1);
                    appGroupMiscDto.setMiscType(ApplicationConsts.APP_GROUP_MISC_TYPE_AMEND_GROUP_ID);
                    appGroupMiscDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    appSubmissionService.saveAppGrpMisc(appGroupMiscDto);
                }
            }
            for (AppSubmissionDto appSubmissionDto1 : autoSaveAppsubmission) {
                t=t+appSubmissionDto1.getAmount();
                String s = Formatter.formatterMoney(appSubmissionDto1.getAmount());
                appSubmissionDto1.setAmountStr(s);
                appSubmissionDto1.setAppGrpNo(appSubmissionDtos1.get(0).getAppGrpNo());
            }
            for(AppSubmissionDto appSubmissionDto1 : appSubmissionDtos1){
                appSubmissionDto1.setAppGrpNo(appSubmissionDtos1.get(0).getAppGrpNo());
            }
            AppSubmissionDto o1 = (AppSubmissionDto)CopyUtil.copyMutableObject(appSubmissionDtos1.get(0));
            o1.setAmount(t);
            String s = Formatter.formatterMoney(t);
            o1.setAmountStr(s);
            ackPageAppSubmissionDto.add(o1);
            appSubmissionDtoList.addAll(appSubmissionDtos1);
            appSubmissionDto.setAppGrpId(appSubmissionDtos1.get(0).getAppGrpId());
        }
        if(autoSaveAppsubmission.isEmpty()&&notAutoSaveAppsubmission.isEmpty()){
            bpc.request.setAttribute("RFC_ERROR_NO_CHANGE",MessageUtil.getMessageDesc("RFC_ERR014"));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "preview");
            requestForChangeService.svcDocToPresmise(appSubmissionDto);
            return;
        }
        bpc.request.getSession().setAttribute("appSubmissionDtos", appSubmissionDtoList);
        bpc.request.getSession().setAttribute("ackPageAppSubmissionDto",ackPageAppSubmissionDto);
        appSubmissionService.doSaveDraft(appSubmissionDto);
        ParamUtil.setSessionAttr(bpc.request,APPSUBMISSIONDTO,appSubmissionDto);
        log.info(StringUtil.changeForLog("the do doRequestForChangeSubmit start ...."));
    }

    public void reSubmit(BaseProcessClass bpc) throws Exception {
        log.info(StringUtil.changeForLog("do reSubmit start ..."));
        String draftNo = ParamUtil.getMaskedString(bpc.request,"draftNo");
        StringBuilder url = new StringBuilder();
        url.append("https://").append(bpc.request.getServerName())
                .append("/hcsa-licence-web/eservice/INTERNET/MohNewApplication?DraftNumber=")
                .append(MaskUtil.maskValue("DraftNumber",draftNo));
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        bpc.response.sendRedirect(tokenUrl);

        log.info(StringUtil.changeForLog("do reSubmit end ..."));
    }

    public void doPayValidate(BaseProcessClass bpc) throws Exception {
        log.info(StringUtil.changeForLog("do doPayValidate start ..."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        Double totalAmount = appSubmissionDto.getAmount();
        String payMethod = ParamUtil.getString(bpc.request, "payMethod");
        appSubmissionDto.setPaymentMethod(payMethod);
        String giroAccNum = "";
        if(!StringUtil.isEmpty(payMethod) && ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod)){
            giroAccNum = ParamUtil.getString(bpc.request, "giroAccount");
        }
        appSubmissionDto.setGiroAcctNum(giroAccNum);
        String noNeedPayment = bpc.request.getParameter("noNeedPayment");
        log.debug(StringUtil.changeForLog("payMethod:"+payMethod));
        log.debug(StringUtil.changeForLog("noNeedPayment:"+noNeedPayment));
        String action = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);
        if("next".equals(action)){
            if(0.0 != totalAmount && StringUtil.isEmpty(noNeedPayment)){
                Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
                if (StringUtil.isEmpty(payMethod)) {
                    errorMap.put("pay",MessageUtil.replaceMessage("GENERAL_ERR0006", "Payment Method", "field"));
                }else if(ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod) && StringUtil.isEmpty(giroAccNum)){
                    errorMap.put("pay",MessageUtil.replaceMessage("GENERAL_ERR0006", "Giro Account", "field"));
                }
                if(!errorMap.isEmpty()){
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "payment");
                    NewApplicationHelper.setAudiErrMap(NewApplicationHelper.checkIsRfi(bpc.request),appSubmissionDto.getAppType(),errorMap,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
                    ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
                }
            }
            if(!StringUtil.isEmpty(noNeedPayment)){
                ParamUtil.setSessionAttr(bpc.request,"txnRefNo","");
                try{
                    if(appSubmissionDto.getAppType().equals(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE)){
                        List<AppSubmissionDto> appSubmissionDtos =IaisCommonUtils.genNewArrayList();
                        appSubmissionDtos.add(appSubmissionDto);
                        requestForChangeService.sendRfcSubmittedEmail(appSubmissionDtos,null);

                    }
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        }else {
            appSubmissionDto.setId(null);
            appSubmissionDto.setAppGrpId(null);
            appSubmissionDto.setAppGrpNo(null);
            requestForChangeService.svcDocToPresmise(appSubmissionDto);
        }
        ParamUtil.setSessionAttr(bpc.request,APPSUBMISSIONDTO,appSubmissionDto);
        String tranSferFlag = appSubmissionDto.getTransferFlag();
        //back to tansfer page
        if(!"next".equals(action) && !StringUtil.isEmpty(tranSferFlag)){
            AppSubmissionDto tranferSub = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, "app-rfc-tranfer");
            if(tranferSub != null){
                tranferSub.setPaymentMethod(payMethod);
                ParamUtil.setSessionAttr(bpc.request,"app-rfc-tranfer",tranferSub);
            }
            StringBuilder url = new StringBuilder();
            url.append("https://").append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTERNET/MohRequestForChange/prepareTranfer");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
        }
        log.info(StringUtil.changeForLog("do doPayValidate end ..."));
    }

    private List<AppGrpPremisesDto> groupLicecePresmiseChange(List<AppGrpPremisesDto> appGrpPremisesDtos,List<AppGrpPremisesDto> oldAppGrpPremisesDtos){
        if(appGrpPremisesDtos==null || oldAppGrpPremisesDtos==null){
            return new ArrayList<>();
        }
        List<AppGrpPremisesDto> list=IaisCommonUtils.genNewArrayList();
        Set<AppGrpPremisesDto> set=IaisCommonUtils.genNewHashSet();
        for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos){
            String hciCode = appGrpPremisesDto.getHciCode();
            for(AppGrpPremisesDto appGrpPremisesDto1 : oldAppGrpPremisesDtos){
                if(hciCode.equals(appGrpPremisesDto1.getHciCode())){
                    AppGrpPremisesDto appGrpPremisesDto3 = PageDataCopyUtil.copyAppGrpPremisesDto(appGrpPremisesDto1);
                    AppGrpPremisesDto appGrpPremisesDto2 =  PageDataCopyUtil.copyAppGrpPremisesDto(appGrpPremisesDto);
                    if(!appGrpPremisesDto3.equals(appGrpPremisesDto2)){
                        set.add(appGrpPremisesDto);
                    }
                }
            }
        }
        list.addAll(set);
        return list;
    }

    

    private AppSubmissionDto getPersonAppsubmit(AppSubmissionDto oldAppSubmissionDto, AppSubmissionDto appSubmissionDto, BaseProcessClass bpc) throws Exception {
        AppSubmissionDto changePerson = (AppSubmissionDto) CopyUtil.copyMutableObject(oldAppSubmissionDto);
        boolean b = EqRequestForChangeSubmitResultChange.changePersonAuto(oldAppSubmissionDto, appSubmissionDto);
        changePerson.setAppSvcRelatedInfoDtoList(appSubmissionDto.getAppSvcRelatedInfoDtoList());
        changePerson.setAppDeclarationMessageDto(appSubmissionDto.getAppDeclarationMessageDto());
        changePerson.setAppDeclarationDocDtos(appSubmissionDto.getAppDeclarationDocDtos());
        changePerson.setAutoRfc(!b);
        String changePersonDraftNo = changePerson.getDraftNo();
        if (StringUtil.isEmpty(changePersonDraftNo)) {
            if (StringUtil.isEmpty(changePerson.getDraftNo())) {
                String draftNo = appSubmissionService.getDraftNo(changePerson.getAppType());
                changePerson.setDraftNo(draftNo);
            }
        }
        if (b) {
            changePerson.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_NOT_PAYMENT);
            appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
            appSubmissionDto.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_NOT_PAYMENT);
        } else {
            appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
            appSubmissionDto.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_NOT_PAYMENT);
            changePerson.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_NOT_PAYMENT);
        }
        changePerson.setAmount(appSubmissionDto.getAmount() == null ? new Double(0.0) : appSubmissionDto.getAmount());
        changePerson.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        changePerson.setStatus(ApplicationConsts.APPLICATION_STATUS_REQUEST_FOR_CHANGE_SUBMIT);
        changePerson.setIsNeedNewLicNo(AppConsts.NO);
        List<AppGrpPremisesDto> appGrpPremisesDtoList = changePerson.getAppGrpPremisesDtoList();
        for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList){
            appGrpPremisesDto.setNeedNewLicNo(Boolean.FALSE);
            if(!b){
                appGrpPremisesDto.setSelfAssMtFlag(4);
            }
        }
        changePerson.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        appSubmissionService.transform(changePerson, appSubmissionDto.getLicenseeId());
        PreOrPostInspectionResultDto preOrPostInspectionResultDto1 = appSubmissionService.judgeIsPreInspection(changePerson);
        if (preOrPostInspectionResultDto1 == null) {
            changePerson.setPreInspection(true);
            changePerson.setRequirement(true);
        } else {
            changePerson.setPreInspection(preOrPostInspectionResultDto1.isPreInspection());
            changePerson.setRequirement(preOrPostInspectionResultDto1.isRequirement());
        }
        appSubmissionService.setRiskToDto(changePerson);

        changePerson.setAppEditSelectDto(appSubmissionDto.getAppEditSelectDto());
        changePerson.setChangeSelectDto(appSubmissionDto.getChangeSelectDto());
        changePerson.setGetAppInfoFromDto(true);
        requestForChangeService.premisesDocToSvcDoc(changePerson);
        return changePerson;
    }


    /**
     * StartStep: doSubmit
     *
     * @param bpc
     * @throws
     */
    public void doSubmit(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("the do doSubmit start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        Map<String, String> map = appSubmissionService.doPreviewAndSumbit(bpc);
        if (!map.isEmpty()) {
            //set audit
            ParamUtil.setRequestAttr(bpc.request, "Msg", map);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "preview");
            HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
            coMap.put("previewli", "");
            bpc.request.getSession().setAttribute("coMap", coMap);
            return;
        } else {
            HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
            coMap.put("previewli", "previewli");
            bpc.request.getSession().setAttribute("coMap", coMap);
        }
        //sync person data
        Map<String, AppSvcPersonAndExtDto> personMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.PERSONSELECTMAP);
        NewApplicationHelper.syncPsnData(appSubmissionDto, personMap);

        String draftNo = appSubmissionDto.getDraftNo();
        if (StringUtil.isEmpty(draftNo)) {
            draftNo = appSubmissionService.getDraftNo(appSubmissionDto.getAppType());
            appSubmissionDto.setDraftNo(draftNo);
        }
        //get appGroupNo
        String appGroupNo = appSubmissionService.getGroupNo(appSubmissionDto.getAppType());
        log.info(StringUtil.changeForLog("the appGroupNo is -->:" + appGroupNo));
        appSubmissionDto.setAppGrpNo(appGroupNo);
        //clear appGrpId
        appSubmissionDto.setAppGrpId(null);
        //get Amount
        FeeDto feeDto = appSubmissionService.getNewAppAmount(appSubmissionDto,NewApplicationHelper.isCharity(bpc.request));
        appSubmissionDto.setFeeInfoDtos(feeDto.getFeeInfoDtos());
        Double amount = feeDto.getTotal();
        log.info(StringUtil.changeForLog("the amount is -->:" + amount));
        /*if(0.0==amount){
            appSubmissionDto.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
            appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
        }*/
        appSubmissionDto.setAmount(amount);
        //judge is the preInspection
        PreOrPostInspectionResultDto preOrPostInspectionResultDto = appSubmissionService.judgeIsPreInspection(appSubmissionDto);
        if (preOrPostInspectionResultDto == null) {
            appSubmissionDto.setPreInspection(true);
            appSubmissionDto.setRequirement(true);
        } else {
            appSubmissionDto.setPreInspection(preOrPostInspectionResultDto.isPreInspection());
            appSubmissionDto.setRequirement(preOrPostInspectionResultDto.isRequirement());
        }

        //set Risk Score
        appSubmissionService.setRiskToDto(appSubmissionDto);

        //set psn dropdown
        setPsnDroTo(appSubmissionDto, bpc);
        //rfi select control
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
        appEditSelectDto.setPremisesEdit(true);
        appEditSelectDto.setDocEdit(true);
        appEditSelectDto.setServiceEdit(true);
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
            if (!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo()) || !StringUtil.isEmpty(appSvcRelatedInfoDto.getAlignLicenceNo())) {
                appEditSelectDto.setPremisesEdit(false);
                break;
            }
        }
        appSubmissionDto.setChangeSelectDto(appEditSelectDto);
        HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
        List<String> strList = new ArrayList<>(5);
        coMap.forEach((k, v) -> {
            if (!StringUtil.isEmpty(v)) {
                strList.add(v);
            }
        });
        String serviceConfig = (String) bpc.request.getSession().getAttribute("serviceConfig");
        strList.add(serviceConfig);
        appSubmissionDto.setStepColor(strList);
        //judge is giro acc
        boolean isGiroAcc = appSubmissionService.isGiroAccount(appSubmissionDto.getLicenseeId());
        appSubmissionDto.setGiroAccount(isGiroAcc);
        //handler primary doc
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionService.handlerPrimaryDoc(appSubmissionDto.getAppGrpPremisesDtoList(),appSubmissionDto.getAppGrpPrimaryDocDtos());
        appSubmissionDto.setAppGrpPrimaryDocDtos(appGrpPrimaryDocDtos);
        Integer maxFileIndex = (Integer) ParamUtil.getSessionAttr(bpc.request,HcsaFileAjaxController.GLOBAL_MAX_INDEX_SESSION_ATTR);
        if(maxFileIndex == null){
            maxFileIndex = 0;
        }
        appSubmissionDto.setMaxFileIndex(maxFileIndex);
        appSubmissionDto = appSubmissionService.submit(appSubmissionDto, bpc.process);

        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);

        //get wrokgroup
        log.info(StringUtil.changeForLog("the do doSubmit end ...."));
    }

    /**
     * StartStep: ControlSwitch
     *
     * @param bpc
     * @throws
     */
    public void controlSwitch(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do controlSwitch start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String switch2 = "loading";
        String crudActionValue = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.ISVALID);
        if (StringUtil.isEmpty(crudActionValue)) {
            crudActionValue = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
            if (StringUtil.isEmpty(crudActionValue)) {
                crudActionValue = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
            }
        }
        Object requestInformationConfig = ParamUtil.getSessionAttr(bpc.request, REQUESTINFORMATIONCONFIG);
        if ((ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())
                || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) && requestInformationConfig == null) {
            String crud_action_additional = ParamUtil.getString(bpc.request, "crud_action_additional");
            if ("rfcSaveDraft".equals(crud_action_additional)) {
                crudActionValue = "saveDraft";
            }
        }
        if ("saveDraft".equals(crudActionValue) || "ack".equals(crudActionValue) || "payFailed".equals(crudActionValue)) {
            switch2 = crudActionValue;
        } else if ("doSubmit".equals(crudActionValue)) {
            if (requestInformationConfig == null) {
                switch2 = crudActionValue;
                if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
                    switch2 = "requstChange";
                } else if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
                    switch2 = "renew";
                }
            } else {
                switch2 = "information";
            }

        }
        ParamUtil.setRequestAttr(bpc.request, "Switch2", switch2);
        log.info(StringUtil.changeForLog("the do controlSwitch end ...."));

    }

    /**
     * StartStep: jumpbank
     *
     * @param bpc
     * @throws
     */
    public void jumpBank(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("the do jumpBank start ...."));
        String payMethod = ParamUtil.getString(bpc.request, "payMethod");
        String noNeedPayment = bpc.request.getParameter("noNeedPayment");
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);

        //68099
        List<AppSubmissionDto> ackPageAppSubmissionDto = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request, "ackPageAppSubmissionDto");
        if(!IaisCommonUtils.isEmpty(ackPageAppSubmissionDto)){
            for(AppSubmissionDto appSubmissionDto1:ackPageAppSubmissionDto){
                if(appSubmissionDto1.getAmount() != 0.0){
                    appSubmissionDto1.setPaymentMethod(payMethod);
                }
            }
            ParamUtil.setSessionAttr(bpc.request,"ackPageAppSubmissionDto", (Serializable) ackPageAppSubmissionDto);
        }
        Double totalAmount = appSubmissionDto.getAmount();
        if (totalAmount == 0.0) {
            if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())){
                String appGrpId = appSubmissionDto.getAppGrpId();
                ApplicationGroupDto appGrp = new ApplicationGroupDto();
                appGrp.setId(appGrpId);
                appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
                appGrp.setPayMethod(payMethod);
                serviceConfigService.updatePaymentStatus(appGrp);

                //
                LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
                appSubmissionService.sendEmailAndSMSAndMessage(appSubmissionDto,loginContext.getUserName());
            }

            StringBuilder url = new StringBuilder();
            url.append("https://")
                    .append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTERNET/MohNewApplication/PrepareAckPage");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
            return;
        }
        String a = appSubmissionDto.getPaymentMethod();
        appSubmissionDto.setPaymentMethod(payMethod);
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        if (ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT.equals(payMethod)
                || ApplicationConsts.PAYMENT_METHOD_NAME_NETS.equals(payMethod)
                || ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW.equals(payMethod)) {
            //send email
            try {
                //inspectionDateSendNewApplicationPaymentOnlineEmail(appSubmissionDto, bpc);
            } catch (Exception e) {
                log.error(StringUtil.changeForLog("send email error ...."));
            }
            String amount = String.valueOf(appSubmissionDto.getAmount());
            Map<String, String> fieldMap = new HashMap<String, String>();
            fieldMap.put(GatewayConstants.AMOUNT_KEY, amount);
            fieldMap.put(GatewayConstants.PYMT_DESCRIPTION_KEY, payMethod);
            fieldMap.put(GatewayConstants.SVCREF_NO, appSubmissionDto.getAppGrpNo()+"_"+System.currentTimeMillis());
            PmtReturnUrlDto pmtReturnUrlDto = new PmtReturnUrlDto();
            pmtReturnUrlDto.setCreditRetUrl(GatewayStripeConfig.return_url);
            pmtReturnUrlDto.setPayNowRetUrl(GatewayConfig.return_url);
            pmtReturnUrlDto.setNetsRetUrl(GatewayConfig.return_url);
            pmtReturnUrlDto.setOtherRetUrl(GatewayConfig.return_url);
            try {
                appSubmissionService.updateDraftStatus(appSubmissionDto.getDraftNo(),ApplicationConsts.DRAFT_STATUS_PENDING_PAYMENT);
                String url = NewApplicationHelper.genBankUrl(bpc.request,payMethod,fieldMap,pmtReturnUrlDto);
                bpc.response.sendRedirect(url);
                //ParamUtil.setRequestAttr(bpc.request, "jumpHtml", html);
            } catch (Exception e) {
                log.info(e.getMessage(), e);
            }
            return;
        } else if (ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod)) {
            //send email
            try {
                //sendNewApplicationPaymentGIROEmail(appSubmissionDto, bpc);
                LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
                appSubmissionService.sendEmailAndSMSAndMessage(appSubmissionDto,loginContext.getUserName());
                if(ackPageAppSubmissionDto==null){
                    ackPageAppSubmissionDto=IaisCommonUtils.genNewArrayList();
                    ackPageAppSubmissionDto.add(appSubmissionDto);
                }
                if ( ackPageAppSubmissionDto.get(0).getAppType().equals(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE)) {
                    requestForChangeService.sendRfcSubmittedEmail(ackPageAppSubmissionDto, ackPageAppSubmissionDto.get(0).getPaymentMethod());
                }
            } catch (Exception e) {
                log.error(StringUtil.changeForLog("send email error ...."));
            }
            String appGrpId = appSubmissionDto.getAppGrpId();
            ApplicationGroupDto appGrp = new ApplicationGroupDto();
            appGrp.setId(appGrpId);
            appGrp.setPmtStatus(serviceConfigService.giroPaymentXmlUpdateByGrpNo(appSubmissionDto).getPmtStatus());
            String giroTranNo = appSubmissionDto.getGiroTranNo();
            appGrp.setPmtRefNo(giroTranNo);
            appGrp.setPayMethod(payMethod);
            serviceConfigService.updateAppGrpPmtStatus(appGrp, appSubmissionDto.getGiroAcctNum());
            serviceConfigService.updatePaymentStatus(appGrp);
            /*List<ApplicationDto> entity = applicationFeClient.getApplicationsByGroupNo(appGrp.getGroupNo()).getEntity();
            if (entity!=null && !entity.isEmpty()) {
                for(ApplicationDto applicationDto : entity){
                    applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
                }
                applicationFeClient.saveApplicationDtos(entity);
            }*/
            ParamUtil.setRequestAttr(bpc.request, "PmtStatus", ApplicationConsts.PAYMENT_METHOD_NAME_GIRO);
            ParamUtil.setSessionAttr(bpc.request, "txnRefNo", giroTranNo);
            //todo change
            StringBuilder url = new StringBuilder();
            url.append("https://")
                    .append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTERNET/MohNewApplication/PrepareAckPage");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
            return;
        }
        log.info(StringUtil.changeForLog("the do jumpBank end ...."));
    }

    /**
     * StartStep: PrePareErrorAckPage
     *
     * @param bpc
     * @throws
     */
    public void prepareErrorAck(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do prepareErrorAck start ...."));

        log.info(StringUtil.changeForLog("the do prepareErrorAck end ...."));
    }

    public void doErrorAck(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do doErrorAck start ...."));


        log.info(StringUtil.changeForLog("the do doErrorAck end ...."));
    }

    /**
     * StartStep: PrepareAckPage
     *
     * @param bpc
     * @throws
     */
    public void prepareAckPage(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do prepareAckPage start ...."));
        String txnRefNo = (String) bpc.request.getSession().getAttribute("txnDt");
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        List<AppSubmissionDto> ackPageAppSubmissionDto =(List<AppSubmissionDto>)ParamUtil.getSessionAttr(bpc.request, "ackPageAppSubmissionDto");
        log.info(StringUtil.changeForLog("ackPageAppSubmissionDto {}"+ackPageAppSubmissionDto));
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenceId = "";
        String draftNo = "";
        if(appSubmissionDto != null){
            licenceId = appSubmissionDto.getLicenseeId();
            if(StringUtil.isEmpty(licenceId) && loginContext != null){
                licenceId = loginContext.getLicenseeId();
            }
            draftNo = appSubmissionDto.getDraftNo();
            if(ackPageAppSubmissionDto==null){
                List<AppSubmissionDto> ackPageAppSubmission=new ArrayList<>(1);
                ackPageAppSubmission.add(appSubmissionDto);
                bpc.request.getSession().setAttribute("ackPageAppSubmissionDto",ackPageAppSubmission);
            }else {
                for(AppSubmissionDto appSubmissionDto1 : ackPageAppSubmissionDto){
                    Double amount = appSubmissionDto1.getAmount();
                    boolean autoRfc = appSubmissionDto1.isAutoRfc();
                    if(0.0==amount && autoRfc){
                        String appGrpNo = appSubmissionDto1.getAppGrpNo();
                        List<ApplicationDto> entity = applicationFeClient.getApplicationsByGroupNo(appGrpNo).getEntity();
                        if(entity!=null &&!entity.isEmpty()){
                            for(ApplicationDto applicationDto : entity){
                                applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
                            }
                            String grpId = entity.get(0).getAppGrpId();
                            log.info(StringUtil.changeForLog("grpId {}"+grpId));
                            ApplicationGroupDto applicationGroupDto = applicationFeClient.getApplicationGroup(grpId).getEntity();
                            applicationGroupDto.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
                            log.info(StringUtil.changeForLog("applicationGroupDto {}"+JsonUtil.parseToJson(applicationGroupDto)));
                            applicationFeClient.saveApplicationDtos(entity);
                            applicationFeClient.updateAppGrpPmtStatus(applicationGroupDto);
                        }
                    }else if(0.0==amount && !autoRfc){
                        String appGrpNo = appSubmissionDto1.getAppGrpNo();
                        List<ApplicationDto> entity = applicationFeClient.getApplicationsByGroupNo(appGrpNo).getEntity();
                        if(entity!=null &&!entity.isEmpty()){
                            for(ApplicationDto applicationDto : entity){
                                applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
                            }
                            String grpId = entity.get(0).getAppGrpId();
                            ApplicationGroupDto applicationGroupDto = applicationFeClient.getApplicationGroup(grpId).getEntity();
                            applicationGroupDto.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
                            applicationFeClient.saveApplicationDtos(entity);
                            applicationFeClient.updateAppGrpPmtStatus(applicationGroupDto);
                        }

                    }
                }
            }
        }
        if(draftNo!=null){
            AppSubmissionDto draftAppSubmissionDto = serviceConfigService.getAppSubmissionDtoDraft(draftNo);
            if(draftAppSubmissionDto!=null){
                applicationFeClient.deleteDraftByNo(draftNo);
            }
        }
        if (!StringUtil.isEmpty(appSubmissionDto)&&!StringUtil.isEmpty(appSubmissionDto.getLicenceId())) {
            List<ApplicationSubDraftDto> entity = applicationFeClient.getDraftByLicAppId(appSubmissionDto.getLicenceId()).getEntity();
            for (ApplicationSubDraftDto applicationSubDraftDto : entity) {
                if(!applicationSubDraftDto.getDraftNo().equals(draftNo)){

                    applicationFeClient.deleteDraftByNo(applicationSubDraftDto.getDraftNo());
                }else {
                    if(AppConsts.COMMON_STATUS_ACTIVE.equals(applicationSubDraftDto.getStatus())){

                        applicationFeClient.deleteDraftByNo(applicationSubDraftDto.getDraftNo());
                    }
                }
            }
        }
        if (StringUtil.isEmpty(txnRefNo)) {
            String txnDt = DateUtil.formatDate(new Date(), "dd/MM/yyyy");
            ParamUtil.setSessionAttr(bpc.request, "txnDt", txnDt);
        }
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto) ParamUtil.getSessionAttr(bpc.request, "INTER_INBOX_USER_INFO");
        String licenseeId = null;
        if (interInboxUserDto != null) {
            licenseeId = interInboxUserDto.getLicenseeId();
        } else {
            log.debug(StringUtil.changeForLog("interInboxUserDto null"));
        }
        List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(licenseeId);
        String emailAddress = WithOutRenewalDelegator.emailAddressesToString(licenseeEmailAddrs);
        ParamUtil.setSessionAttr(bpc.request, "emailAddress", emailAddress);
        String ackStatus = (String) ParamUtil.getRequestAttr(bpc.request, ACKSTATUS);
        boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
        if(isRfi && "error".equals(ackStatus)){
            List<HcsaServiceDto> hcsaServiceDtoList = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request,RFI_REPLY_SVC_DTO);
            ParamUtil.setSessionAttr(bpc.request,AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) hcsaServiceDtoList);
        }
        log.info(StringUtil.changeForLog("the do prepareAckPage end ...."));
    }

    /**
     * StartStep: prepareJumpv
     *
     * @param bpc
     * @throws
     */
    public void prepareJump(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do prepareJump start ...."));
        String action = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        if (StringUtil.isEmpty(action)) {
            action = ParamUtil.getRequestString(bpc.request, "nextStep");
        }
        if (RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)) {
            AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.OLDAPPSUBMISSIONDTO);
            AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
            AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto();
            if (appEditSelectDto.isPremisesEdit()) {
                appSubmissionDto.setAppGrpPremisesDtoList(oldAppSubmissionDto.getAppGrpPremisesDtoList());
            }
            if (appEditSelectDto.isDocEdit()) {
                appSubmissionDto.setAppGrpPrimaryDocDtos(oldAppSubmissionDto.getAppGrpPrimaryDocDtos());
            }
            if (appEditSelectDto.isServiceEdit()) {
                appSubmissionDto.setAppSvcRelatedInfoDtoList(oldAppSubmissionDto.getAppSvcRelatedInfoDtoList());
            }
            ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);
        }

        log.info(StringUtil.changeForLog("the do prepareJump end ...."));
    }

    //=============================================================================
    //private method
    //=============================================================================

    private String getServiceCodeString(List<String> serviceCodeList) {
        StringBuilder serviceCodeString = new StringBuilder(10);
        for (String code : serviceCodeList) {
            serviceCodeString.append(code);
            serviceCodeString.append('@');
        }
        return serviceCodeString.toString();
    }

    private void sendMessageHelper(String subject, String messageType, String srcSystemId, String serviceId, String licenseeId, String templateMessageByContent, HashMap<String, String> maskParams) {
        InterMessageDto interMessageDto = new InterMessageDto();
        interMessageDto.setSrcSystemId(srcSystemId);
        interMessageDto.setSubject(subject);
        interMessageDto.setMessageType(messageType);
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        String refNo = feEicGatewayClient.getMessageNo(signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();
        interMessageDto.setRefNo(refNo);
        interMessageDto.setService_id(serviceId);
        interMessageDto.setUserId(licenseeId);
        interMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        interMessageDto.setMsgContent(templateMessageByContent);
        interMessageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        feMessageClient.createInboxMessage(interMessageDto);
    }

    //send email helper
    private String sendEmailHelper(Map<String, Object> tempMap, String msgTemplateId, String subject, String licenseeId, String clientQueryCode) {
        MsgTemplateDto msgTemplateDto = appSubmissionService.getMsgTemplateById(msgTemplateId);
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
        appSubmissionService.feSendEmail(emailDto);

        return mesContext;
    }

    private Map<String, String> doComChange(AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto) throws Exception {
        Map<String, String> result = IaisCommonUtils.genNewHashMap();
        AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto();

        if (appEditSelectDto != null) {
            if (!appEditSelectDto.isPremisesEdit()) {
                List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
                for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                    appGrpPremisesDto.setLicenceDtos(null);
                }
                boolean eqGrpPremises = EqRequestForChangeSubmitResultChange.eqGrpPremises(appSubmissionDto.getAppGrpPremisesDtoList(), oldAppSubmissionDto.getAppGrpPremisesDtoList());
                if (eqGrpPremises) {
                    log.info(StringUtil.changeForLog("appGrpPremisesDto" + JsonUtil.parseToJson(appSubmissionDto.getAppGrpPremisesDtoList())));
                    log.info(StringUtil.changeForLog("oldappGrpPremisesDto" + JsonUtil.parseToJson(oldAppSubmissionDto.getAppGrpPremisesDtoList())));
                    result.put("premiss",MessageUtil.replaceMessage("GENERAL_ERR0006","premiss","field"));
                }
            }
            List<AppGrpPrimaryDocDto> dtoAppGrpPrimaryDocDtos = oldAppSubmissionDto.getAppGrpPrimaryDocDtos();
            if (dtoAppGrpPrimaryDocDtos != null) {
                for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : dtoAppGrpPrimaryDocDtos) {
                    appGrpPrimaryDocDto.setPassValidate(true);
                }
            }
            List<AppGrpPrimaryDocDto> dtoAppGrpPrimaryDocDtos1 = appSubmissionDto.getAppGrpPrimaryDocDtos();
            if (dtoAppGrpPrimaryDocDtos1 != null) {
                for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : dtoAppGrpPrimaryDocDtos1) {
                    appGrpPrimaryDocDto.setPassValidate(true);
                }
            }
            if (!appEditSelectDto.isDocEdit()) {
                boolean b = EqRequestForChangeSubmitResultChange.eqDocChange(appSubmissionDto.getAppGrpPrimaryDocDtos(), oldAppSubmissionDto.getAppGrpPrimaryDocDtos());
                if (b) {
                    log.info(StringUtil.changeForLog("appGrpPrimaryDocDto" + JsonUtil.parseToJson(appSubmissionDto.getAppGrpPrimaryDocDtos())));
                    log.info(StringUtil.changeForLog("oldAppGrpPrimaryDocDto" + JsonUtil.parseToJson(oldAppSubmissionDto.getAppGrpPrimaryDocDtos())));
                    result.put("document",MessageUtil.replaceMessage("GENERAL_ERR0006","document","field"));
                }
            }

            if (!appEditSelectDto.isServiceEdit()) {
                boolean b = EqRequestForChangeSubmitResultChange.eqServiceChange(appSubmissionDto.getAppSvcRelatedInfoDtoList(), oldAppSubmissionDto.getAppSvcRelatedInfoDtoList());
                if (b) {
                    log.info(StringUtil.changeForLog("AppSvcRelatedInfoDtoList" + JsonUtil.parseToJson(appSubmissionDto.getAppSvcRelatedInfoDtoList())));
                    log.info(StringUtil.changeForLog("oldAppSvcRelatedInfoDtoList" + JsonUtil.parseToJson(oldAppSubmissionDto.getAppSvcRelatedInfoDtoList())));
                    result.put("serviceId",MessageUtil.replaceMessage("GENERAL_ERR0006","serviceId","field"));
                }
            }
        }
        return result;
    }

    private boolean compareAndSendEmail(AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto) {
        boolean isAuto = true;
        AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto();
        if (appEditSelectDto != null) {
            if (appEditSelectDto.isPremisesEdit()) {
                if (!appSubmissionDto.getAppGrpPremisesDtoList().equals(oldAppSubmissionDto.getAppGrpPremisesDtoList())) {
                    isAuto = compareHciName(appSubmissionDto.getAppGrpPremisesDtoList(), oldAppSubmissionDto.getAppGrpPremisesDtoList());
                    if (isAuto) {
                        isAuto = compareLocation(appSubmissionDto.getAppGrpPremisesDtoList(), oldAppSubmissionDto.getAppGrpPremisesDtoList());
                    }
                    //send eamil

                }
                boolean b = eqAddFloorNo(appSubmissionDto, oldAppSubmissionDto);
                if(b){
                    isAuto=false;
                }
            }

            if (appEditSelectDto.isMedAlertEdit()) {
                //todo:
            }
            //one svc
            AppSvcRelatedInfoDto appSvcRelatedInfoDtoList = getAppSvcRelatedInfoDto(appSubmissionDto.getAppSvcRelatedInfoDtoList());
            AppSvcRelatedInfoDto oldAppSvcRelatedInfoDtoList = getAppSvcRelatedInfoDto(oldAppSubmissionDto.getAppSvcRelatedInfoDtoList());
            if (appEditSelectDto.isPoEdit() || appEditSelectDto.isDpoEdit() || appEditSelectDto.isServiceEdit()) {
                //remove
                List<AppSvcPrincipalOfficersDto> newAppSvcPrincipalOfficersDto = appSvcRelatedInfoDtoList.getAppSvcPrincipalOfficersDtoList();
                List<AppSvcPrincipalOfficersDto> oldAppSvcPrincipalOfficersDto = oldAppSvcRelatedInfoDtoList.getAppSvcPrincipalOfficersDtoList();
                if (newAppSvcPrincipalOfficersDto.size() > oldAppSvcPrincipalOfficersDto.size()) {
                    isAuto = false;
                    //send eamil
                }
                //change
                List<String> newPoIdNos = IaisCommonUtils.genNewArrayList();
                List<String> oldPoIdNos = IaisCommonUtils.genNewArrayList();
                List<String> newDpoIdNos = IaisCommonUtils.genNewArrayList();
                List<String> olddDpoIdNos = IaisCommonUtils.genNewArrayList();

                for (AppSvcPrincipalOfficersDto item : newAppSvcPrincipalOfficersDto) {
                    if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(item.getPsnType())) {
                        newPoIdNos.add(item.getIdNo());
                    } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(item.getPsnType())) {
                        newDpoIdNos.add(item.getIdNo());
                    }
                }
                for (AppSvcPrincipalOfficersDto item : oldAppSvcPrincipalOfficersDto) {
                    if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(item.getPsnType())) {
                        oldPoIdNos.add(item.getIdNo());
                    } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(item.getPsnType())) {
                        olddDpoIdNos.add(item.getIdNo());
                    }
                }
                if (!newPoIdNos.equals(oldPoIdNos)) {
                    isAuto = false;
                }
                if (!newDpoIdNos.equals(olddDpoIdNos)) {
                    isAuto = false;
                }
            }

            if (appEditSelectDto.isServiceEdit()) {
                List<AppSvcPrincipalOfficersDto> newAppSvcCgoDto = appSvcRelatedInfoDtoList.getAppSvcCgoDtoList();
                List<AppSvcPrincipalOfficersDto> oldAppSvcCgoDto = oldAppSvcRelatedInfoDtoList.getAppSvcCgoDtoList();
                List<String> newCgoIdNos = IaisCommonUtils.genNewArrayList();
                List<String> oldCgoIdNos = IaisCommonUtils.genNewArrayList();
                if (!IaisCommonUtils.isEmpty(newAppSvcCgoDto) && !IaisCommonUtils.isEmpty(oldAppSvcCgoDto)) {
                    for (AppSvcPrincipalOfficersDto item : newAppSvcCgoDto) {
                        newCgoIdNos.add(item.getIdNo());
                    }
                    for (AppSvcPrincipalOfficersDto item : oldAppSvcCgoDto) {
                        oldCgoIdNos.add(item.getIdNo());
                    }
                    if (!newCgoIdNos.equals(oldCgoIdNos)) {
                        isAuto = false;
                    }

                }

                List<AppSvcLaboratoryDisciplinesDto> newDisciplinesDto = appSvcRelatedInfoDtoList.getAppSvcLaboratoryDisciplinesDtoList();
                List<AppSvcLaboratoryDisciplinesDto> oldDisciplinesDto = oldAppSvcRelatedInfoDtoList.getAppSvcLaboratoryDisciplinesDtoList();

                if (!IaisCommonUtils.isEmpty(newDisciplinesDto) && !IaisCommonUtils.isEmpty(oldDisciplinesDto)) {
                    for (AppSvcLaboratoryDisciplinesDto item : newDisciplinesDto) {
                        String hciName = item.getPremiseVal();
                        AppSvcLaboratoryDisciplinesDto oldAppSvcLaboratoryDisciplinesDto = getDisciplinesDto(oldDisciplinesDto, hciName);
                        List<AppSvcChckListDto> newCheckList = item.getAppSvcChckListDtoList();
                        List<AppSvcChckListDto> oldCheckList = oldAppSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                        if (!IaisCommonUtils.isEmpty(newCheckList) && !IaisCommonUtils.isEmpty(oldCheckList)) {
                            List<String> newCheckListIds = IaisCommonUtils.genNewArrayList();
                            List<String> oldCheckListIds = IaisCommonUtils.genNewArrayList();
                            for (AppSvcChckListDto checBox : oldCheckList) {
                                oldCheckListIds.add(checBox.getChkLstConfId());
                            }
                            for (AppSvcChckListDto checBox : newCheckList) {
                                if (!oldCheckListIds.contains(checBox.getChkLstConfId())) {
                                    isAuto = false;
                                    break;
                                }
                            }
                        }
                    }
                    if (!newDisciplinesDto.equals(oldDisciplinesDto)) {

                    }

                }


            }

            if (appEditSelectDto.isDocEdit()) {
            /*if(!appSubmissionDto.getAppGrpPrimaryDocDtos().equals(oldAppSubmissionDto.getAppGrpPrimaryDocDtos()) ||
                    !appSvcRelatedInfoDtoList.getAppSvcDocDtoLit().equals(oldAppSvcRelatedInfoDtoList.getAppSvcDocDtoLit())){
                //send eamil

            }*/

            }
        }

        return isAuto;
    }

    private AppSvcLaboratoryDisciplinesDto getDisciplinesDto(List<AppSvcLaboratoryDisciplinesDto> disciplinesDto, String hciName) {
        for (AppSvcLaboratoryDisciplinesDto iten : disciplinesDto) {
            if (hciName.equals(iten.getPremiseVal())) {
                return iten;
            }
        }
        return new AppSvcLaboratoryDisciplinesDto();
    }

    private boolean compareHciName(List<AppGrpPremisesDto> appGrpPremisesDtos, List<AppGrpPremisesDto> oldAppGrpPremisesDtos) {
        int length = appGrpPremisesDtos.size();
        int oldLength = oldAppGrpPremisesDtos.size();
        if (length == oldLength) {
            for (int i = 0; i < length; i++) {
                AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtos.get(0);
                AppGrpPremisesDto oldAppGrpPremisesDto = oldAppGrpPremisesDtos.get(0);
                if (!EqRequestForChangeSubmitResultChange.compareHciName(appGrpPremisesDto,oldAppGrpPremisesDto)) {
                    return false;
                }
            }
        }
        //is same
        return true;
    }

    private boolean compareLocation(List<AppGrpPremisesDto> appGrpPremisesDtos, List<AppGrpPremisesDto> oldAppGrpPremisesDtos) {
        int length = appGrpPremisesDtos.size();
        int oldLength = oldAppGrpPremisesDtos.size();
        if (length == oldLength) {
            for (int i = 0; i < length; i++) {
                AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtos.get(0);
                AppGrpPremisesDto oldAppGrpPremisesDto = oldAppGrpPremisesDtos.get(0);
                if (!appGrpPremisesDto.getAddress().equals(oldAppGrpPremisesDto.getAddress())) {
                    return false;
                }
            }
        }
        //is same
        return true;
    }

    private AmendmentFeeDto getAmendmentFeeDto(AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto,boolean isCharity) {
        AmendmentFeeDto amendmentFeeDto = new AmendmentFeeDto();
        boolean changeHciName = compareHciName(appSubmissionDto.getAppGrpPremisesDtoList(), oldAppSubmissionDto.getAppGrpPremisesDtoList());
        boolean changeLocation = compareLocation(appSubmissionDto.getAppGrpPremisesDtoList(), oldAppSubmissionDto.getAppGrpPremisesDtoList());
        boolean eqAppSvcVehicleDto = EqRequestForChangeSubmitResultChange.eqAppSvcVehicleDto(appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getAppSvcVehicleDtoList(), oldAppSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getAppSvcVehicleDtoList());
        amendmentFeeDto.setChangeInLicensee(Boolean.FALSE);
        amendmentFeeDto.setChangeInHCIName(!changeHciName);
        amendmentFeeDto.setChangeInLocation(!changeLocation);
        if(eqAppSvcVehicleDto){
            amendmentFeeDto.setChangeInHCIName(Boolean.TRUE);
        }
        amendmentFeeDto.setIsCharity(isCharity);
        return amendmentFeeDto;
    }

    private AppSvcRelatedInfoDto getAppSvcRelatedInfoDto(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos) {
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
            return appSvcRelatedInfoDtos.get(0);
        }
        return new AppSvcRelatedInfoDto();
    }

    /**
     * @param request
     * @return
     * @description: for the page validate call.
     */
    private ApplicationValidateDto getValueFromPage(HttpServletRequest request) {
        ApplicationValidateDto dto = new ApplicationValidateDto();
        String pageCon = request.getParameter("pageCon");
        chose(request, pageCon);

        return dto;
    }

    private void chose(HttpServletRequest request, String type) {
        if ("valPremiseList".equals(type)) {
            List<AppGrpPremisesDto> list = genAppGrpPremisesDtoList(request);
            ParamUtil.setRequestAttr(request, "valPremiseList", list);
        }
        if ("prinOffice".equals(type)) {
            List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDto =
                    (List<AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(request, "AppSvcPrincipalOfficersDto");
            ParamUtil.setRequestAttr(request, "prinOffice", appSvcPrincipalOfficersDto);
        }

    }

    /**
     * @description: get data from page
     * @author: zixian
     * @date: 11/6/2019 5:05 PM
     * @param: request
     * @return: AppGrpPremisesDto
     */
    //todo:move to NewApplicationHelper
    public static List<AppGrpPremisesDto> genAppGrpPremisesDtoList(HttpServletRequest request) {
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, APPSUBMISSIONDTO);
        boolean onlySpecifiedSvc = false;
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                if (!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo()) || !StringUtil.isEmpty(appSvcRelatedInfoDto.getAlignLicenceNo())) {
                    onlySpecifiedSvc = true;
                    break;
                }
            }
        }
        if (onlySpecifiedSvc) {
            return appSubmissionDto.getAppGrpPremisesDtoList();
        }

        List<AppGrpPremisesDto> appGrpPremisesDtoList = IaisCommonUtils.genNewArrayList();
        Object requestInformationConfig = ParamUtil.getSessionAttr(request, REQUESTINFORMATIONCONFIG);
        List<HcsaServiceDto> hcsaServiceDtoList = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(request, AppServicesConsts.HCSASERVICEDTOLIST);
        boolean isMultiPremService = NewApplicationHelper.isMultiPremService(hcsaServiceDtoList);
        int count = 0;
        String[] premisesType = ParamUtil.getStrings(request, "premType");
        String[] hciName = ParamUtil.getStrings(request, "onSiteHciName");
        if (premisesType != null) {
            count = premisesType.length;
        }
        if(!isMultiPremService){
            count = 1;
        }
        String[] premisesIndexNo = ParamUtil.getStrings(request, "premisesIndexNo");
        String[] rfiCanEdit = ParamUtil.getStrings(request,"rfiCanEdit");
        //onsite
        String[] premisesSelect = ParamUtil.getStrings(request, "onSiteSelect");
        String[] postalCode = ParamUtil.getStrings(request, "onSitePostalCode");
        String[] blkNo = ParamUtil.getStrings(request, "onSiteBlkNo");
        String[] streetName = ParamUtil.getStrings(request, "onSiteStreetName");
        String[] floorNo = ParamUtil.getStrings(request, "onSiteFloorNo");
        String[] unitNo = ParamUtil.getStrings(request, "onSiteUnitNo");
        String[] buildingName = ParamUtil.getStrings(request, "onSiteBuildingName");
        String[] siteAddressType = ParamUtil.getStrings(request, "onSiteAddressType");
        String[] offTelNo = ParamUtil.getStrings(request, "onSiteOffTelNo");
        String[] scdfRefNo = ParamUtil.getStrings(request, "onSiteScdfRefNo");
        String[] isOtherLic = ParamUtil.getStrings(request, "onSiteIsOtherLic");
        //conveyance
        String[] conveyanceHciName = ParamUtil.getStrings(request, "conveyanceHciName");
        String[] conPremisesSelect = ParamUtil.getStrings(request, "conveyanceSelect");
        String[] conVehicleNo = ParamUtil.getStrings(request, "conveyanceVehicleNo");
        String[] conPostalCode = ParamUtil.getStrings(request, "conveyancePostalCode");
        String[] conBlkNo = ParamUtil.getStrings(request, "conveyanceBlkNo");
        String[] conStreetName = ParamUtil.getStrings(request, "conveyanceStreetName");
        String[] conFloorNo = ParamUtil.getStrings(request, "conveyanceFloorNo");
        String[] conUnitNo = ParamUtil.getStrings(request, "conveyanceUnitNo");
        String[] conBuildingName = ParamUtil.getStrings(request, "conveyanceBuildingName");
        String[] conSiteAddressType = ParamUtil.getStrings(request, "conveyanceAddrType");
        //offSite
        String[] offSiteHciName = ParamUtil.getStrings(request, "offSiteHciName");
        String[] offSitePremisesSelect = ParamUtil.getStrings(request, "offSiteSelect");
        String[] offSitePostalCode = ParamUtil.getStrings(request, "offSitePostalCode");
        String[] offSiteBlkNo = ParamUtil.getStrings(request, "offSiteBlkNo");
        String[] offSiteStreetName = ParamUtil.getStrings(request, "offSiteStreetName");
        String[] offSiteFloorNo = ParamUtil.getStrings(request, "offSiteFloorNo");
        String[] offSiteUnitNo = ParamUtil.getStrings(request, "offSiteUnitNo");
        String[] offSiteBuildingName = ParamUtil.getStrings(request, "offSiteBuildingName");
        String[] offSiteSiteAddressType = ParamUtil.getStrings(request, "offSiteAddrType");

        String[] easMtsPremisesSelect = ParamUtil.getStrings(request, "easMtsSelect");
        //every prem's ph length
        String[] phLengths = ParamUtil.getStrings(request, "phLength");
        String[] premValue = ParamUtil.getStrings(request, "premValue");
        String[] isParyEdit = ParamUtil.getStrings(request, "isPartEdit");
        String[] chooseExistData = ParamUtil.getStrings(request, "chooseExistData");
        String[] opLengths = ParamUtil.getStrings(request,"opLength");
        String[] retrieveflag = ParamUtil.getStrings(request,"retrieveflag");
        String[] weeklyLengths = ParamUtil.getStrings(request,"weeklyLength");
        String[] eventLengths = ParamUtil.getStrings(request,"eventLength");
        Map<String, AppGrpPremisesDto> licAppGrpPremisesDtoMap = (Map<String, AppGrpPremisesDto>) ParamUtil.getSessionAttr(request, LICAPPGRPPREMISESDTOMAP);
        for (int i = 0; i < count; i++) {
            AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
            String premisesSel = "";
            if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesType[i])) {
                premisesSel = premisesSelect[i];
            } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesType[i])) {
                premisesSel = conPremisesSelect[i];
            } else if (ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesType[i])) {
                premisesSel = offSitePremisesSelect[i];
            } else if (ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(premisesType[i])){
                premisesSel = easMtsPremisesSelect[i];
            }
            String premIndexNo = "";
            try {
                premIndexNo = premisesIndexNo[i];
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            String appType = appSubmissionDto.getAppType();
            boolean newApp = requestInformationConfig == null && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType);
            if (newApp) {
                if (!StringUtil.isEmpty(premisesSel) && !premisesSel.equals("-1") && !premisesSel.equals(ApplicationConsts.NEW_PREMISES)) {
                    AppGrpPremisesDto licPremise = licAppGrpPremisesDtoMap.get(premisesSel);
                    if(licPremise != null){
                        try {
                            appGrpPremisesDto = (AppGrpPremisesDto) CopyUtil.copyMutableObject(licPremise);
                        } catch (CloneNotSupportedException e) {
                            log.error(StringUtil.changeForLog(e.getMessage()),e);
                        }
                    }else{
                        log.info(StringUtil.changeForLog("can not found this existing premises data ...."));
                    }
//                    appGrpPremisesDto = licAppGrpPremisesDtoMap.get(premisesSel);
                    if (appGrpPremisesDto != null) {
                        //get value for jsp page
                        if (StringUtil.isEmpty(premisesIndexNo[i])) {
                            appGrpPremisesDto.setPremisesIndexNo(UUID.randomUUID().toString());
                        } else {
                            appGrpPremisesDto.setPremisesIndexNo(premisesIndexNo[i]);
                        }
                        appGrpPremisesDtoList.add(appGrpPremisesDto);
                    }
                    continue;
                }
            } else if (!StringUtil.isEmpty(isParyEdit[i])) {
                if (!AppConsts.YES.equals(isParyEdit[i])) {
                    List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
                    for (AppGrpPremisesDto prem : appGrpPremisesDtos) {
                        if (prem.getPremisesIndexNo().equals(premIndexNo)) {
                            appGrpPremisesDto = prem;
                            break;
                        }
                    }
                    appGrpPremisesDtoList.add(appGrpPremisesDto);
                    continue;
                }
                if (AppConsts.YES.equals(chooseExistData[i])) {
                    appGrpPremisesDto = licAppGrpPremisesDtoMap.get(premisesSel);
                    //get value for jsp page
                    if (StringUtil.isEmpty(premisesIndexNo[i])) {
                        appGrpPremisesDto.setPremisesIndexNo(UUID.randomUUID().toString());
                    } else {
                        appGrpPremisesDto.setPremisesIndexNo(premisesIndexNo[i]);
                    }
                    appGrpPremisesDtoList.add(appGrpPremisesDto);
                    continue;
                }
                //set hciCode
                List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
                AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) request.getSession().getAttribute("oldAppSubmissionDto");
                Object attribute = request.getSession().getAttribute(REQUESTINFORMATIONCONFIG);
                for (AppGrpPremisesDto premDto : appGrpPremisesDtos) {
                    if (!StringUtil.isEmpty(premisesIndexNo[i]) && premisesIndexNo[i].equals(premDto.getPremisesIndexNo())) {
                        String hciCode="";
                        if(attribute!=null){
                            String hciCode1 = premDto.getHciCode();
                            String oldHciCode = premDto.getOldHciCode();
                            if(hciCode1!=null&&oldHciCode!=null){
                                if(hciCode1.equals(oldHciCode)){
                                    hciCode=hciCode1;
                                }
                            }else if(hciCode1==null) {

                            }

                        }else {
                            if(oldAppSubmissionDto!=null){
                                boolean eqHciCode = EqRequestForChangeSubmitResultChange.eqHciCode(appGrpPremisesDto, oldAppSubmissionDto.getAppGrpPremisesDtoList().get(0));
                                if(eqHciCode){
                                    hciCode=  oldAppSubmissionDto.getAppGrpPremisesDtoList().get(0).getHciCode();
                                }
                            }
                        }
                        appGrpPremisesDto.setHciCode(premDto.getHciCode());
                        if(!StringUtil.isEmpty(hciCode)){
                            appGrpPremisesDto.setHciCode(hciCode);
                        }
                        appGrpPremisesDto.setHciNameChanged(premDto.getHciNameChanged());
                        appGrpPremisesDto.setLicenceDtos(premDto.getLicenceDtos());
                        break;
                    }
                }
            }

            //get value for session , this is the subtype's checkbox
            if (StringUtil.isEmpty(premisesIndexNo[i])) {
                appGrpPremisesDto.setPremisesIndexNo(UUID.randomUUID().toString());
            } else {
                appGrpPremisesDto.setPremisesIndexNo(premisesIndexNo[i]);
            }

            appGrpPremisesDto.setPremisesType(premisesType[i]);
            //List<AppPremPhOpenPeriodDto> appPremPhOpenPeriods = IaisCommonUtils.genNewArrayList();
            List<OperationHoursReloadDto> weeklyDtoList = IaisCommonUtils.genNewArrayList();
            List<OperationHoursReloadDto> phDtoList = IaisCommonUtils.genNewArrayList();
            List<AppPremEventPeriodDto> eventList = IaisCommonUtils.genNewArrayList();
            /*int length = 0;
            try {
                length = Integer.parseInt(phLength[i]);
            } catch (Exception e) {
                log.error(StringUtil.changeForLog("length can not parse to int"));
            }*/
            int opLength = 0;
            try{
                opLength = Integer.parseInt(opLengths[i]);
            }catch(Exception e){
                log.error(StringUtil.changeForLog("operation length can not parse to int"));
            }
            int weeklyLength = 0;
            try{
                weeklyLength = Integer.parseInt(weeklyLengths[i]);
            }catch(Exception e){
                log.error(StringUtil.changeForLog("weekly length can not parse to int"));
            }
            int phLength = 0;
            try{
                phLength = Integer.parseInt(phLengths[i]);
            }catch(Exception e){
                log.error(StringUtil.changeForLog("ph length can not parse to int"));
            }
            int eventLength = 0;
            try{
                eventLength = Integer.parseInt(eventLengths[i]);
            }catch(Exception e){
                log.error(StringUtil.changeForLog("event length can not parse to int"));
            }
            if(AppConsts.TRUE.equals(rfiCanEdit[i])){
                appGrpPremisesDto.setRfiCanEdit(true);
            }else{
                appGrpPremisesDto.setRfiCanEdit(false);
            }
            if(AppConsts.YES.equals(retrieveflag[i])){
                appGrpPremisesDto.setClickRetrieve(true);
            }else{
                appGrpPremisesDto.setClickRetrieve(false);
            }
            List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos = IaisCommonUtils.genNewArrayList();
            if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesType[i])) {
                String premVal = premValue[i];
                String fireSafetyCertIssuedDateStr = ParamUtil.getString(request, premVal+"onSiteFireSafetyCertIssuedDate");
                appGrpPremisesDto.setPremisesSelect(premisesSelect[i]);
                appGrpPremisesDto.setHciName(hciName[i]);
                appGrpPremisesDto.setPostalCode(postalCode[i]);
                appGrpPremisesDto.setBlkNo(blkNo[i]);
                appGrpPremisesDto.setStreetName(streetName[i]);
                appGrpPremisesDto.setFloorNo(floorNo[i]);
                appGrpPremisesDto.setUnitNo(unitNo[i]);
                appGrpPremisesDto.setBuildingName(buildingName[i]);
                appGrpPremisesDto.setScdfRefNo(scdfRefNo[i]);
                appGrpPremisesDto.setAddrType(siteAddressType[i]);
                appGrpPremisesDto.setOffTelNo(offTelNo[i]);
                Date fireSafetyCertIssuedDateDate = DateUtil.parseDate(fireSafetyCertIssuedDateStr, Formatter.DATE);
                appGrpPremisesDto.setCertIssuedDt(fireSafetyCertIssuedDateDate);
                String certIssuedDtStr = Formatter.formatDate(fireSafetyCertIssuedDateDate);
                appGrpPremisesDto.setCertIssuedDtStr(certIssuedDtStr);
                if (AppConsts.YES.equals(isOtherLic[i])) {
                    appGrpPremisesDto.setLocateWithOthers(AppConsts.YES);
                } else if (AppConsts.NO.equals(isOtherLic[i])) {
                    appGrpPremisesDto.setLocateWithOthers(AppConsts.NO);
                }

                //weekly
                for(int j = 0;j<weeklyLength;j++){
                    OperationHoursReloadDto weeklyDto = new OperationHoursReloadDto();
                    String[] weeklyVal = ParamUtil.getStrings(request,genPageName(premVal,"onSiteWeekly",j));
                    String allDay = ParamUtil.getString(request,genPageName(premVal,"onSiteWeeklyAllDay",j));
                    //reload
                    String weeklySelect = ParamUtil.StringsToString(weeklyVal);
                    weeklyDto.setSelectVal(weeklySelect);
                    if(weeklyVal != null){
                        List<String> selectValList = Arrays.asList(weeklyVal);
                        weeklyDto.setSelectValList(selectValList);
                    }
                    if(AppConsts.TRUE.equals(allDay)){
                        weeklyDto.setSelectAllDay(true);
                        //todo:generate star,end

                    }else{
                        String weeklyStartHH = ParamUtil.getString(request,genPageName(premVal,"onSiteWeeklyStartHH",j));
                        String weeklyStartMM = ParamUtil.getString(request,genPageName(premVal,"onSiteWeeklyStartMM",j));
                        String weeklyEndHH = ParamUtil.getString(request,genPageName(premVal,"onSiteWeeklyEndHH",j));
                        String weeklyEndMM = ParamUtil.getString(request,genPageName(premVal,"onSiteWeeklyEndMM",j));
                        weeklyDto.setStartFromHH(weeklyStartHH);
                        weeklyDto.setStartFromMM(weeklyStartMM);
                        weeklyDto.setEndToHH(weeklyEndHH);
                        weeklyDto.setEndToMM(weeklyEndMM);
                    }
                    weeklyDtoList.add(weeklyDto);
                }
                //ph
                for(int j = 0;j < phLength;j++){
                    OperationHoursReloadDto phDto = new OperationHoursReloadDto();
                    String[] phVal = ParamUtil.getStrings(request,genPageName(premVal,"onSitePubHoliday",j));
                    String allDay = ParamUtil.getString(request,genPageName(premVal,"onSitePhAllDay",j));
                    //reload
                    String phSelect = ParamUtil.StringsToString(phVal);
                    phDto.setSelectVal(phSelect);
                    if(phSelect != null){
                        List<String> selectValList = Arrays.asList(phVal);
                        phDto.setSelectValList(selectValList);
                    }
                    if(AppConsts.TRUE.equals(allDay)){
                        phDto.setSelectAllDay(true);
                        phDtoList.add(phDto);
                    }else{
                        String phStartHH = ParamUtil.getString(request,genPageName(premVal,"onSitePhStartHH",j));
                        String phStartMM = ParamUtil.getString(request,genPageName(premVal,"onSitePhStartMM",j));
                        String phEndHH = ParamUtil.getString(request,genPageName(premVal,"onSitePhEndHH",j));
                        String phEndMM = ParamUtil.getString(request,genPageName(premVal,"onSitePhEndMM",j));
                        phDto.setStartFromHH(phStartHH);
                        phDto.setStartFromMM(phStartMM);
                        phDto.setEndToHH(phEndHH);
                        phDto.setEndToMM(phEndMM);
                        if(phLength >1 || !StringUtil.isEmpty(phSelect) || !StringUtil.isEmpty(phStartHH) || !StringUtil.isEmpty(phStartMM) || !StringUtil.isEmpty(phEndHH)|| !StringUtil.isEmpty(phEndMM)){
                            phDtoList.add(phDto);
                        }
                    }

                }
                //event
                for(int j = 0;j < eventLength;j++){
                    AppPremEventPeriodDto appPremEventPeriodDto = new AppPremEventPeriodDto();
                    String eventName = ParamUtil.getString(request,genPageName(premVal,"onSiteEvent",j));
                    String eventStartStr = ParamUtil.getString(request,genPageName(premVal,"onSiteEventStart",j));
                    Date eventStart = DateUtil.parseDate(eventStartStr, Formatter.DATE);
                    String eventEndStr = ParamUtil.getString(request,genPageName(premVal,"onSiteEventEnd",j));
                    Date eventEnd = DateUtil.parseDate(eventEndStr, Formatter.DATE);
                    appPremEventPeriodDto.setEventName(eventName);
                    appPremEventPeriodDto.setStartDate(eventStart);
                    appPremEventPeriodDto.setStartDateStr(eventStartStr);
                    appPremEventPeriodDto.setEndDate(eventEnd);
                    appPremEventPeriodDto.setEndDateStr(eventEndStr);
                    if(eventLength >1 || !StringUtil.isEmpty(eventName) || !StringUtil.isEmpty(eventStartStr) || !StringUtil.isEmpty(eventEndStr)){
                        eventList.add(appPremEventPeriodDto);
                    }
                }


                if(opLength > 0){
                    for(int j=0;j<opLength;j++){
                        AppPremisesOperationalUnitDto operationalUnitDto = new AppPremisesOperationalUnitDto();
                        String opFloorNoName = premValue[i] + "onSiteFloorNo" + j;
                        String opUnitNoName = premValue[i] + "onSiteUnitNo" + j;

                        String opFloorNo = ParamUtil.getString(request,opFloorNoName);
                        String opUnitNo = ParamUtil.getString(request,opUnitNoName);
                        operationalUnitDto.setFloorNo(opFloorNo);
                        operationalUnitDto.setUnitNo(opUnitNo);
                        appPremisesOperationalUnitDtos.add(operationalUnitDto);
                    }
                }

            } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesType[i])) {

                appGrpPremisesDto.setConveyanceHciName(conveyanceHciName[i]);
                appGrpPremisesDto.setPremisesSelect(conPremisesSelect[i]);
                appGrpPremisesDto.setConveyanceVehicleNo(conVehicleNo[i]);
                appGrpPremisesDto.setConveyancePostalCode(conPostalCode[i]);
                appGrpPremisesDto.setConveyanceBlockNo(conBlkNo[i]);
                appGrpPremisesDto.setConveyanceStreetName(conStreetName[i]);
                appGrpPremisesDto.setConveyanceFloorNo(conFloorNo[i]);
                appGrpPremisesDto.setConveyanceUnitNo(conUnitNo[i]);
                appGrpPremisesDto.setConveyanceBuildingName(conBuildingName[i]);
                appGrpPremisesDto.setConveyanceAddressType(conSiteAddressType[i]);

                //weekly
                String premVal = premValue[i];
                for(int j = 0;j<weeklyLength;j++){
                    OperationHoursReloadDto weeklyDto = new OperationHoursReloadDto();
                    String[] weeklyVal = ParamUtil.getStrings(request,genPageName(premVal,"conveyanceWeekly",j));
                    String allDay = ParamUtil.getString(request,genPageName(premVal,"conveyanceWeeklyAllDay",j));
                    //reload
                    String weeklySelect = ParamUtil.StringsToString(weeklyVal);
                    weeklyDto.setSelectVal(weeklySelect);
                    if(weeklyVal != null){
                        List<String> selectValList = Arrays.asList(weeklyVal);
                        weeklyDto.setSelectValList(selectValList);
                    }
                    if(AppConsts.TRUE.equals(allDay)){
                        weeklyDto.setSelectAllDay(true);
                    }else{
                        String weeklyStartHH = ParamUtil.getString(request,genPageName(premVal,"conveyanceWeeklyStartHH",j));
                        String weeklyStartMM = ParamUtil.getString(request,genPageName(premVal,"conveyanceWeeklyStartMM",j));
                        String weeklyEndHH = ParamUtil.getString(request,genPageName(premVal,"conveyanceWeeklyEndHH",j));
                        String weeklyEndMM = ParamUtil.getString(request,genPageName(premVal,"conveyanceWeeklyEndMM",j));
                        weeklyDto.setStartFromHH(weeklyStartHH);
                        weeklyDto.setStartFromMM(weeklyStartMM);
                        weeklyDto.setEndToHH(weeklyEndHH);
                        weeklyDto.setEndToMM(weeklyEndMM);
                    }
                    weeklyDtoList.add(weeklyDto);
                }
                //ph
                for(int j = 0;j < phLength;j++){
                    OperationHoursReloadDto phDto = new OperationHoursReloadDto();
                    String[] phVal = ParamUtil.getStrings(request,genPageName(premVal,"conveyancePubHoliday",j));
                    String allDay = ParamUtil.getString(request,genPageName(premVal,"conveyancePhAllDay",j));
                    //reload
                    String phSelect = ParamUtil.StringsToString(phVal);
                    phDto.setSelectVal(phSelect);
                    if(phSelect != null){
                        List<String> selectValList = Arrays.asList(phVal);
                        phDto.setSelectValList(selectValList);
                    }
                    if(AppConsts.TRUE.equals(allDay)){
                        phDto.setSelectAllDay(true);
                        phDtoList.add(phDto);
                    }else{
                        String phStartHH = ParamUtil.getString(request,genPageName(premVal,"conveyancePhStartHH",j));
                        String phStartMM = ParamUtil.getString(request,genPageName(premVal,"conveyancePhStartMM",j));
                        String phEndHH = ParamUtil.getString(request,genPageName(premVal,"conveyancePhEndHH",j));
                        String phEndMM = ParamUtil.getString(request,genPageName(premVal,"conveyancePhEndMM",j));
                        phDto.setStartFromHH(phStartHH);
                        phDto.setStartFromMM(phStartMM);
                        phDto.setEndToHH(phEndHH);
                        phDto.setEndToMM(phEndMM);
                        if(phLength >1 || !StringUtil.isEmpty(phSelect) || !StringUtil.isEmpty(phStartHH) || !StringUtil.isEmpty(phStartMM) || !StringUtil.isEmpty(phEndHH)|| !StringUtil.isEmpty(phEndMM)){
                            phDtoList.add(phDto);
                        }
                    }

                }
                //event
                for(int j = 0;j < eventLength;j++){
                    AppPremEventPeriodDto appPremEventPeriodDto = new AppPremEventPeriodDto();
                    String eventName = ParamUtil.getString(request,genPageName(premVal,"conveyanceEvent",j));
                    String eventStartStr = ParamUtil.getString(request,genPageName(premVal,"conveyanceEventStart",j));
                    Date eventStart = DateUtil.parseDate(eventStartStr, Formatter.DATE);
                    String eventEndStr = ParamUtil.getString(request,genPageName(premVal,"conveyanceEventEnd",j));
                    Date eventEnd = DateUtil.parseDate(eventEndStr, Formatter.DATE);
                    appPremEventPeriodDto.setEventName(eventName);
                    appPremEventPeriodDto.setStartDate(eventStart);
                    appPremEventPeriodDto.setStartDateStr(eventStartStr);
                    appPremEventPeriodDto.setEndDate(eventEnd);
                    appPremEventPeriodDto.setEndDateStr(eventEndStr);
                    if(eventLength >1 || !StringUtil.isEmpty(eventName) || !StringUtil.isEmpty(eventStartStr) || !StringUtil.isEmpty(eventEndStr)){
                        eventList.add(appPremEventPeriodDto);
                    }
                }
                if(opLength > 0){
                    for(int j=0;j<opLength;j++){
                        AppPremisesOperationalUnitDto operationalUnitDto = new AppPremisesOperationalUnitDto();
                        String opFloorNoName = premValue[i] + "conveyanceFloorNo" + j;
                        String opUnitNoName = premValue[i] + "conveyanceUnitNo" + j;

                        String opFloorNo = ParamUtil.getString(request,opFloorNoName);
                        String opUnitNo = ParamUtil.getString(request,opUnitNoName);
                        operationalUnitDto.setFloorNo(opFloorNo);
                        operationalUnitDto.setUnitNo(opUnitNo);
                        appPremisesOperationalUnitDtos.add(operationalUnitDto);
                    }
                }
            } else if (ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesType[i])) {
                appGrpPremisesDto.setOffSiteHciName(offSiteHciName[i]);
                appGrpPremisesDto.setPremisesSelect(offSitePremisesSelect[i]);
                appGrpPremisesDto.setOffSitePostalCode(offSitePostalCode[i]);
                appGrpPremisesDto.setOffSiteBlockNo(offSiteBlkNo[i]);
                appGrpPremisesDto.setOffSiteStreetName(offSiteStreetName[i]);
                appGrpPremisesDto.setOffSiteFloorNo(offSiteFloorNo[i]);
                appGrpPremisesDto.setOffSiteUnitNo(offSiteUnitNo[i]);
                appGrpPremisesDto.setOffSiteBuildingName(offSiteBuildingName[i]);
                appGrpPremisesDto.setOffSiteAddressType(offSiteSiteAddressType[i]);
                //weekly
                String premVal = premValue[i];
                for(int j = 0;j<weeklyLength;j++){
                    OperationHoursReloadDto weeklyDto = new OperationHoursReloadDto();
                    String[] weeklyVal = ParamUtil.getStrings(request,genPageName(premVal,"offSiteWeekly",j));
                    String allDay = ParamUtil.getString(request,genPageName(premVal,"offSiteWeeklyAllDay",j));
                    //reload
                    String weeklySelect = ParamUtil.StringsToString(weeklyVal);
                    weeklyDto.setSelectVal(weeklySelect);
                    if(weeklyVal != null){
                        List<String> selectValList = Arrays.asList(weeklyVal);
                        weeklyDto.setSelectValList(selectValList);
                    }
                    if(AppConsts.TRUE.equals(allDay)){
                        weeklyDto.setSelectAllDay(true);
                    }else{
                        String weeklyStartHH = ParamUtil.getString(request,genPageName(premVal,"offSiteWeeklyStartHH",j));
                        String weeklyStartMM = ParamUtil.getString(request,genPageName(premVal,"offSiteWeeklyStartMM",j));
                        String weeklyEndHH = ParamUtil.getString(request,genPageName(premVal,"offSiteWeeklyEndHH",j));
                        String weeklyEndMM = ParamUtil.getString(request,genPageName(premVal,"offSiteWeeklyEndMM",j));
                        weeklyDto.setStartFromHH(weeklyStartHH);
                        weeklyDto.setStartFromMM(weeklyStartMM);
                        weeklyDto.setEndToHH(weeklyEndHH);
                        weeklyDto.setEndToMM(weeklyEndMM);
                    }
                    weeklyDtoList.add(weeklyDto);
                }
                //ph
                for(int j = 0;j < phLength;j++){
                    OperationHoursReloadDto phDto = new OperationHoursReloadDto();
                    String[] phVal = ParamUtil.getStrings(request,genPageName(premVal,"offSitePubHoliday",j));
                    String allDay = ParamUtil.getString(request,genPageName(premVal,"offSitePhAllDay",j));
                    //reload
                    String phSelect = ParamUtil.StringsToString(phVal);
                    phDto.setSelectVal(phSelect);
                    if(phSelect != null){
                        List<String> selectValList = Arrays.asList(phVal);
                        phDto.setSelectValList(selectValList);
                    }
                    if(AppConsts.TRUE.equals(allDay)){
                        phDto.setSelectAllDay(true);
                        phDtoList.add(phDto);
                    }else{
                        String phStartHH = ParamUtil.getString(request,genPageName(premVal,"offSitePhStartHH",j));
                        String phStartMM = ParamUtil.getString(request,genPageName(premVal,"offSitePhStartMM",j));
                        String phEndHH = ParamUtil.getString(request,genPageName(premVal,"offSitePhEndHH",j));
                        String phEndMM = ParamUtil.getString(request,genPageName(premVal,"offSitePhEndMM",j));
                        phDto.setStartFromHH(phStartHH);
                        phDto.setStartFromMM(phStartMM);
                        phDto.setEndToHH(phEndHH);
                        phDto.setEndToMM(phEndMM);
                        if(phLength >1 || !StringUtil.isEmpty(phSelect) || !StringUtil.isEmpty(phStartHH) || !StringUtil.isEmpty(phStartMM) || !StringUtil.isEmpty(phEndHH)|| !StringUtil.isEmpty(phEndMM)){
                            phDtoList.add(phDto);
                        }
                    }

                }
                //event
                for(int j = 0;j < eventLength;j++){
                    AppPremEventPeriodDto appPremEventPeriodDto = new AppPremEventPeriodDto();
                    String eventName = ParamUtil.getString(request,genPageName(premVal,"offSiteEvent",j));
                    String eventStartStr = ParamUtil.getString(request,genPageName(premVal,"offSiteEventStart",j));
                    Date eventStart = DateUtil.parseDate(eventStartStr, Formatter.DATE);
                    String eventEndStr = ParamUtil.getString(request,genPageName(premVal,"offSiteEventEnd",j));
                    Date eventEnd = DateUtil.parseDate(eventEndStr, Formatter.DATE);
                    appPremEventPeriodDto.setEventName(eventName);
                    appPremEventPeriodDto.setStartDate(eventStart);
                    appPremEventPeriodDto.setStartDateStr(eventStartStr);
                    appPremEventPeriodDto.setEndDate(eventEnd);
                    appPremEventPeriodDto.setEndDateStr(eventEndStr);
                    if(eventLength >1 || !StringUtil.isEmpty(eventName) || !StringUtil.isEmpty(eventStartStr) || !StringUtil.isEmpty(eventEndStr)){
                        eventList.add(appPremEventPeriodDto);
                    }
                }
                if(opLength > 0){
                    for(int j=0;j<opLength;j++){
                        AppPremisesOperationalUnitDto operationalUnitDto = new AppPremisesOperationalUnitDto();
                        String opFloorNoName = premValue[i] + "offSiteFloorNo" + j;
                        String opUnitNoName = premValue[i] + "offSiteUnitNo" + j;

                        String opFloorNo = ParamUtil.getString(request,opFloorNoName);
                        String opUnitNo = ParamUtil.getString(request,opUnitNoName);
                        operationalUnitDto.setFloorNo(opFloorNo);
                        operationalUnitDto.setUnitNo(opUnitNo);
                        appPremisesOperationalUnitDtos.add(operationalUnitDto);
                    }
                }
            }else if(ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(premisesType[i])){
                String easMtsHciName = ParamUtil.getString(request, "easMtsHciName");
                String easMtsPostalCode = ParamUtil.getString(request, "easMtsPostalCode");
                String easMtsBlkNo = ParamUtil.getString(request, "easMtsBlkNo");
                String easMtsStreetName = ParamUtil.getString(request, "easMtsStreetName");
                String easMtsFloorNo = ParamUtil.getString(request, "easMtsFloorNo");
                String easMtsUnitNo = ParamUtil.getString(request, "easMtsUnitNo");
                String easMtsBuildingName = ParamUtil.getString(request, "easMtsBuildingName");
                String easMtsAddressType = ParamUtil.getString(request, "easMtsAddrType");
                String easMtsUseOnly = ParamUtil.getString(request, "easMtsUseOnlyVal");
                String easMtsPubEmail = ParamUtil.getString(request, "easMtsPubEmail");
                String easMtsPubHotline = ParamUtil.getString(request, "easMtsPubHotline");
                appGrpPremisesDto.setEasMtsHciName(easMtsHciName);
                appGrpPremisesDto.setPremisesSelect(easMtsPremisesSelect[i]);
                appGrpPremisesDto.setEasMtsPostalCode(easMtsPostalCode);
                appGrpPremisesDto.setEasMtsAddressType(easMtsAddressType);
                appGrpPremisesDto.setEasMtsBlockNo(easMtsBlkNo);
                appGrpPremisesDto.setEasMtsFloorNo(easMtsFloorNo);
                appGrpPremisesDto.setEasMtsUnitNo(easMtsUnitNo);
                appGrpPremisesDto.setEasMtsStreetName(easMtsStreetName);
                appGrpPremisesDto.setEasMtsBuildingName(easMtsBuildingName);
                appGrpPremisesDto.setEasMtsUseOnly(easMtsUseOnly);
                appGrpPremisesDto.setEasMtsPubEmail(easMtsPubEmail);
                appGrpPremisesDto.setEasMtsPubHotline(easMtsPubHotline);
                if(opLength > 0){
                    for(int j=0;j<opLength;j++){
                        AppPremisesOperationalUnitDto operationalUnitDto = new AppPremisesOperationalUnitDto();
                        String opFloorNoName = premValue[i] + "easMtsFloorNo" + j;
                        String opUnitNoName = premValue[i] + "easMtsUnitNo" + j;

                        String opFloorNo = ParamUtil.getString(request,opFloorNoName);
                        String opUnitNo = ParamUtil.getString(request,opUnitNoName);
                        operationalUnitDto.setFloorNo(opFloorNo);
                        operationalUnitDto.setUnitNo(opUnitNo);
                        appPremisesOperationalUnitDtos.add(operationalUnitDto);
                    }
                }

            }
            //appGrpPremisesDto.setAppPremPhOpenPeriodList(appPremPhOpenPeriods);
            appGrpPremisesDto.setAppPremisesOperationalUnitDtos(appPremisesOperationalUnitDtos);
            appGrpPremisesDto.setWeeklyDtoList(weeklyDtoList);
            appGrpPremisesDto.setPhDtoList(phDtoList);
            appGrpPremisesDto.setEventDtoList(eventList);
            appGrpPremisesDtoList.add(appGrpPremisesDto);
        }
        if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType()) ||
                ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
            //set premises edit status
            NewApplicationHelper.setPremEditStatus(appGrpPremisesDtoList, getAppGrpPremisesDtos(appSubmissionDto.getOldAppSubmissionDto()));
        }
        return appGrpPremisesDtoList;
    }



    private void loadingDraft(BaseProcessClass bpc, String draftNo) {
        log.info(StringUtil.changeForLog("the do loadingDraft start ...."));
        Object draftNumber = bpc.request.getSession().getAttribute("DraftNumber");
        if (draftNumber != null) {
            draftNo = (String) draftNumber;
        }
        //draftNo = "DN191118000001";
        if (!StringUtil.isEmpty(draftNo)) {
            log.info(StringUtil.changeForLog("draftNo is not empty"));
            AppSubmissionDto appSubmissionDto = serviceConfigService.getAppSubmissionDtoDraft(draftNo);
            if (appSubmissionDto != null) {
                if (IaisCommonUtils.isEmpty(appSubmissionDto.getAppSvcRelatedInfoDtoList())) {
                    log.info(StringUtil.changeForLog("appSvcRelatedInfoDtoList is empty"));
                }
                if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())||ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())){
                    requestForChangeService.svcDocToPresmise(appSubmissionDto);
                }
                //set max file index into session
                Integer maxFileIndex = appSubmissionDto.getMaxFileIndex();
                if(maxFileIndex == null){
                    maxFileIndex = 0;
                }else{
                    maxFileIndex ++;
                }
                ParamUtil.setSessionAttr(bpc.request,HcsaFileAjaxController.GLOBAL_MAX_INDEX_SESSION_ATTR,maxFileIndex);
                List<String> stepColor = appSubmissionDto.getStepColor();
                if (stepColor != null) {
                    HashMap<String, String> coMap = new HashMap<>(4);
                    coMap.put("premises", "");
                    coMap.put("document", "");
                    coMap.put("information", "");
                    coMap.put("previewli", "");
                    if (!stepColor.isEmpty()) {
                        for (String str : stepColor) {
                            if ("premises".equals(str)) {
                                coMap.put("premises", str);
                            } else if ("document".equals(str)) {
                                coMap.put("document", str);
                            } else if ("information".equals(str)) {
                                coMap.put("information", str);
                            } else if ("previewli".equals(str)) {
                                coMap.put("previewli", str);
                            } else {
                                bpc.request.getSession().setAttribute("serviceConfig", str);
                            }
                        }
                    }
                    bpc.getSession().setAttribute("coMap", coMap);
                }
                if (appSubmissionDto.getAppGrpPremisesDtoList() != null && appSubmissionDto.getAppGrpPremisesDtoList().size() > 0) {
                    ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
                } else {
                    ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, null);
                }
                ParamUtil.setSessionAttr(bpc.request, DRAFTCONFIG, "test");
            }
            bpc.request.getSession().setAttribute(SELECT_DRAFT_NO, null);
        }
        if (draftNumber != null) {

            ParamUtil.setSessionAttr(bpc.request, DRAFTCONFIG, null);
            String entryType = ParamUtil.getString(bpc.request,"entryType");
            if(!StringUtil.isEmpty(entryType) && entryType.equals("assessment")){
                ParamUtil.setSessionAttr(bpc.request,ASSESSMENTCONFIG,"test");
            }
        }
        log.info(StringUtil.changeForLog("the do loadingDraft end ...."));
    }

    private void requestForChangeOrRenewLoading(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do requestForChangeLoading start ...."));
        String appType = (String) ParamUtil.getRequestAttr(bpc.request, "appType");
        String currentEdit = (String) ParamUtil.getRequestAttr(bpc.request, RfcConst.RFC_CURRENT_EDIT);
        boolean canDoEdit = (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType));
        if(!canDoEdit || StringUtil.isEmpty(currentEdit)){
            return;
        }

        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getRequestAttr(bpc.request, RfcConst.APPSUBMISSIONDTORFCATTR);
        if (canDoEdit && appSubmissionDto != null) {
            AuditTrailHelper.setAuditTrailInfoByAppType(appType);
            ParamUtil.setSessionAttr(bpc.request, "hasDetail", "Y");
            ParamUtil.setSessionAttr(bpc.request, "isSingle", "Y");
            AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
            if (RfcConst.EDIT_PREMISES.equals(currentEdit)) {
                appEditSelectDto.setPremisesEdit(true);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "premises");
            } else if (RfcConst.EDIT_PRIMARY_DOC.equals(currentEdit)) {
                appEditSelectDto.setDocEdit(true);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "documents");
            } else if (RfcConst.EDIT_SERVICE.equals(currentEdit)) {
                appEditSelectDto.setServiceEdit(true);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "serviceForms");
            }
            appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
            appSubmissionDto.setNeedEditController(true);
            ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
            HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
            coMap.put("premises", "premises");
            coMap.put("document", "document");
            coMap.put("information", "information");
            coMap.put("previewli", "previewli");
            bpc.request.getSession().setAttribute("coMap", coMap);
        }
        log.info(StringUtil.changeForLog("the do requestForChangeLoading end ...."));
    }

    private void renewLicence(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do renewLicence start ...."));
        String licenceId = ParamUtil.getString(bpc.request, "licenceId");
        String type = ParamUtil.getString(bpc.request, "type");
        log.info(StringUtil.changeForLog("The type is -->:" + type));
        if (!StringUtil.isEmpty(licenceId) && ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(type)) {
            AppSubmissionDto appSubmissionDto = appSubmissionService.getAppSubmissionDtoByLicenceId(licenceId);
            if (appSubmissionDto != null) {
                appSubmissionDto.setNeedEditController(true);
                AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
                appEditSelectDto.setPremisesEdit(true);
                appEditSelectDto.setDocEdit(true);
                appEditSelectDto.setPoEdit(true);
                appEditSelectDto.setDocEdit(true);
                appEditSelectDto.setServiceEdit(true);
                appEditSelectDto.setMedAlertEdit(true);
                appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
                appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
            }
            ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        }
        log.info(StringUtil.changeForLog("the do renewLicence end ...."));
    }

    private void requestForInformationLoading(BaseProcessClass bpc, String appNo) throws CloneNotSupportedException {
        log.info(StringUtil.changeForLog("the do requestForInformationLoading start ...."));
        String msgId = (String) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        //msgId = "415199C2-4AAA-42BF-B068-9B019BF1ED1C";
        if (!StringUtil.isEmpty(appNo) && !StringUtil.isEmpty(msgId)) {
//            appNo = "AN210511010651A-01";
            AppSubmissionDto appSubmissionDto = appSubmissionService.getAppSubmissionDtoByAppNo(appNo);
            if (appSubmissionDto != null) {
                if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType()) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
                    rfiLoadingCheckImplForRenew.checkPremiseInfo(appSubmissionDto,appNo);
                    requestForChangeService.svcDocToPresmise(appSubmissionDto);
                }else {
                  /*  rfiLoadingExc.checkPremiseInfo(appSubmissionDto,appNo);*/
                }
            }
            InterMessageDto interMessageDto = appSubmissionService.getInterMessageById(msgId);
            if (MessageConstants.MESSAGE_STATUS_RESPONSE.equals(interMessageDto.getStatus())) {
                appSubmissionDto = null;
            }

            if (appSubmissionDto != null) {
                //set max file index into session
                Integer maxFileIndex = appSubmissionDto.getMaxFileIndex();
                if(maxFileIndex == null){
                    maxFileIndex = 0;
                }else{
                    maxFileIndex ++;
                }
                ParamUtil.setSessionAttr(bpc.request,HcsaFileAjaxController.GLOBAL_MAX_INDEX_SESSION_ATTR,maxFileIndex);

                appSubmissionDto.setRfiAppNo(appNo);
                //clear svcDoc id
                List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
                if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
                    for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                        List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                        if (!IaisCommonUtils.isEmpty(appSvcDocDtos)) {
                            for (AppSvcDocDto appSvcDocDto : appSvcDocDtos) {
                                appSvcDocDto.setId(null);
                            }
                            appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtos);
                        }
                    }
                    appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtos);
                }
                String appType = appSubmissionDto.getAppType();
                boolean isRenewalOrRfc = ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType) || ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType);
                appSubmissionDto.setNeedEditController(true);
                AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto();
                if (appEditSelectDto == null) {
                    appEditSelectDto = new AppEditSelectDto();
                    appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
                }
                if (isRenewalOrRfc) {
                    // set the required information
                    String licenceId = appSubmissionDto.getLicenceId();
                    appSubmissionDto.setLicenceNo(withOutRenewalService.getLicenceNumberByLicenceId(licenceId));
                }
                ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
                //ParamUtil.setSessionAttr(bpc.request,OLDAPPSUBMISSIONDTO,oldAppSubmissionDto);
                HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
                coMap.put("premises", "premises");
                coMap.put("document", "document");
                coMap.put("information", "information");
                coMap.put("previewli", "previewli");
                bpc.request.getSession().setAttribute("coMap", coMap);
                //control premises edit
                List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
                AppGrpPremisesEntityDto rfiPremises = appSubmissionService.getPremisesByAppNo(appNo);
                String rfiPremHci = IaisCommonUtils.genPremisesKey(rfiPremises.getPostalCode(),rfiPremises.getBlkNo(),rfiPremises.getFloorNo(),rfiPremises.getUnitNo());
                if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(rfiPremises.getPremisesType())){
                    rfiPremHci = rfiPremises.getHciName()+rfiPremHci;
                }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(rfiPremises.getPremisesType())){
                    rfiPremHci = rfiPremises.getHciName() + rfiPremises.getVehicleNo()+rfiPremHci;
                }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(rfiPremises.getPremisesType())){
                    rfiPremHci = rfiPremises.getHciName() + rfiPremHci;
                }else if(ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(rfiPremises.getPremisesType())){
                    rfiPremHci = rfiPremises.getHciName() + rfiPremHci;
                }
                if(!IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
                    for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                        String premHci = NewApplicationHelper.getPremHci(appGrpPremisesDto);
                        if(rfiPremHci.equals(premHci)){
                            appGrpPremisesDto.setRfiCanEdit(true);
                        }
                        //clear ph id
                        List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodDtos = appGrpPremisesDto.getAppPremPhOpenPeriodList();
                        if(!IaisCommonUtils.isEmpty(appPremPhOpenPeriodDtos)){
                            for(AppPremPhOpenPeriodDto phDto:appPremPhOpenPeriodDtos){
                                phDto.setId(null);
                                phDto.setPremId(null);
                            }
                            appGrpPremisesDto.setAppPremPhOpenPeriodList(appPremPhOpenPeriodDtos);
                        }
                        //clear operation premId
                        List<AppPremisesOperationalUnitDto> operationalUnitDtos = appGrpPremisesDto.getAppPremisesOperationalUnitDtos();
                        if(!IaisCommonUtils.isEmpty(operationalUnitDtos)){
                            for(AppPremisesOperationalUnitDto operationalUnitDto:operationalUnitDtos){
                                operationalUnitDto.setId(null);
                                operationalUnitDto.setPremisesId(null);
                            }
                        }
                    }
                    appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtos);
                }
                ParamUtil.setSessionAttr(bpc.request,APPSUBMISSIONDTO,appSubmissionDto);
            } else {
                ApplicationDto applicationDto = appSubmissionService.getMaxVersionApp(appNo);
                if (applicationDto != null) {
                    HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(applicationDto.getServiceId());
                    if (hcsaServiceDto != null) {
                        List<HcsaServiceDto> hcsaServiceDtoList = IaisCommonUtils.genNewArrayList();
                        hcsaServiceDtoList.add(hcsaServiceDto);
                        ParamUtil.setSessionAttr(bpc.request, RFI_REPLY_SVC_DTO, (Serializable) hcsaServiceDtoList);
                    }
                    bpc.request.setAttribute("APPLICATION_TYPE",applicationDto.getApplicationType());
                    String errMsg = MessageUtil.getMessageDesc("INBOX_ERR001");
                    jumpToAckPage(bpc, NewApplicationConstant.ACK_STATUS_ERROR, errMsg);
                }
            }
            ParamUtil.setSessionAttr(bpc.request, REQUESTINFORMATIONCONFIG, "test");
        }
        log.info(StringUtil.changeForLog("the do requestForInformationLoading end ...."));
    }

    private boolean loadingServiceConfig(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.info(StringUtil.changeForLog("the do loadingServiceConfig start ...."));
        //loading the service
        List<String> serviceConfigIds = IaisCommonUtils.genNewArrayList();
        List<String> names = IaisCommonUtils.genNewArrayList();
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        if (appSubmissionDto != null) {
            // from draft,rfi
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtoList)) {
                for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList) {
                    if (!StringUtil.isEmpty(appSvcRelatedInfoDto.getServiceId())) {
                        serviceConfigIds.add(appSvcRelatedInfoDto.getServiceId());
                    }
                    //if get the data from licence, only have the serviceName
                    if (!StringUtil.isEmpty(appSvcRelatedInfoDto.getServiceName())) {
                        names.add(appSvcRelatedInfoDto.getServiceName());
                    }

                }
            }
        } else {
            List<String> licenceIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, "licence");
            List<String> baseServiceIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, "baseServiceChecked");
            List<String> specifiedServiceIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, "specifiedServiceChecked");
            if (IaisCommonUtils.isEmpty(licenceIds)) {
                if (!IaisCommonUtils.isEmpty(baseServiceIds)) {
                    serviceConfigIds.addAll(baseServiceIds);
                }
                if (!IaisCommonUtils.isEmpty(specifiedServiceIds)) {
                    serviceConfigIds.addAll(specifiedServiceIds);
                }
            }
        }

        if (IaisCommonUtils.isEmpty(serviceConfigIds) && IaisCommonUtils.isEmpty(names)) {
            log.info(StringUtil.changeForLog("service id is empty"));
            String errMsg = "you have encountered some problems, please contact the administrator !!!";
            jumpToAckPage(bpc, NewApplicationConstant.ACK_STATUS_ERROR, errMsg);
            return false;
        }

        List<HcsaServiceDto> hcsaServiceDtoList = null;
        if (!serviceConfigIds.isEmpty()) {
            hcsaServiceDtoList = serviceConfigService.getHcsaServiceDtosById(serviceConfigIds);
        } else if (!names.isEmpty()) {
            hcsaServiceDtoList = serviceConfigService.getActiveHcsaSvcByNames(names);
        }
        if (hcsaServiceDtoList != null) {
            hcsaServiceDtoList = NewApplicationHelper.sortHcsaServiceDto(hcsaServiceDtoList);
        }
        ParamUtil.setSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) hcsaServiceDtoList);
        log.info(StringUtil.changeForLog("the do loadingServiceConfig end ...."));
        return true;
    }

    private static AppSubmissionDto getAppSubmissionDto(HttpServletRequest request) {
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, APPSUBMISSIONDTO);
        if (appSubmissionDto == null) {
            log.info(StringUtil.changeForLog("appSubmissionDto is empty "));
            appSubmissionDto = new AppSubmissionDto();
            ParamUtil.setSessionAttr(request, APPSUBMISSIONDTO, appSubmissionDto);
        }
        return appSubmissionDto;
    }

    private void loadingNewAppInfo(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do loadingSpecifiedInfo start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = (List<AppSvcRelatedInfoDto>) ParamUtil.getSessionAttr(bpc.request, ServiceMenuDelegator.APP_SVC_RELATED_INFO_LIST);
        AppSelectSvcDto appSelectSvcDto = (AppSelectSvcDto) ParamUtil.getSessionAttr(bpc.request, ServiceMenuDelegator.APP_SELECT_SERVICE);
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos) && appSubmissionDto == null && appSelectSvcDto != null) {
            String entryType = ParamUtil.getString(bpc.request,"entryType");
            if(!StringUtil.isEmpty(entryType) && entryType.equals("assessment")){
                ParamUtil.setSessionAttr(bpc.request,ASSESSMENTCONFIG,"test");
            }
            appSubmissionDto = new AppSubmissionDto();
            appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
            appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtos);
            String premisesId = "";
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                String premId = appSvcRelatedInfoDto.getLicPremisesId();
                if (!StringUtil.isEmpty(premId) && !"-1".equals(premId)) {
                    premisesId = premId;
                    break;
                }
            }
            if (!StringUtil.isEmpty(premisesId)) {
                List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionService.getLicPremisesInfo(premisesId);
                /*for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                    appGrpPremisesDto.setPremisesSelect(NewApplicationConstant.NEW_PREMISES);
                }*/
                appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtos);
            } else {
                List<AppGrpPremisesDto> appGrpPremisesDtos = IaisCommonUtils.genNewArrayList();
                AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
                appGrpPremisesDtos.add(appGrpPremisesDto);
                appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtos);
            }
            ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
            ParamUtil.setSessionAttr(bpc.request,HcsaFileAjaxController.GLOBAL_MAX_INDEX_SESSION_ATTR,0);
        }
        log.info(StringUtil.changeForLog("the do loadingSpecifiedInfo start ...."));
    }

    private static List<AppGrpPremisesDto> getAppGrpPremisesDtos(AppSubmissionDto appSubmissionDto) {
        List<AppGrpPremisesDto> appGrpPremisesDtos = IaisCommonUtils.genNewArrayList();
        if (appSubmissionDto != null) {
            appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        }
        return appGrpPremisesDtos;
    }

    private static void jumpToAckPage(BaseProcessClass bpc, String ackStatus, String errorMsg) {
        String actionType = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if (StringUtil.isEmpty(actionType)) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "errorAck");
            if (NewApplicationConstant.ACK_STATUS_ERROR.equals(ackStatus)) {
                ParamUtil.setRequestAttr(bpc.request, ACKSTATUS, "error");
                ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, errorMsg);
            }
        }
    }

    private void setPsnDroTo(AppSubmissionDto appSubmissionDto, BaseProcessClass bpc) {
        Map<String, AppSvcPrincipalOfficersDto> personMap = (Map<String, AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.PERSONSELECTMAP);
        String personMapStr = JsonUtil.parseToJson(personMap);
        appSubmissionDto.setDropDownPsnMapStr(personMapStr);
    }

    private Integer getAppGrpPrimaryDocVersion(String configDocId,List<AppGrpPrimaryDocDto> oldDocs,boolean isRfi,String md5Code,String appGrpId,String appNo,String appType,int seqNum,String dupForPrem){
        log.info(StringUtil.changeForLog("AppGrpPrimaryDocVersion start..."));
        Integer version = 1;
        if(StringUtil.isEmpty(configDocId) || IaisCommonUtils.isEmpty(oldDocs) || StringUtil.isEmpty(md5Code)){
            return version;
        }
        log.info(StringUtil.changeForLog("isRfi:"+isRfi));
        log.info(StringUtil.changeForLog("appType:"+appType));
        log.info(StringUtil.changeForLog("seqNum:"+seqNum));
        if(isRfi){
            log.info(StringUtil.changeForLog("rfi appNo:"+appNo));
            boolean canFound = false;
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:oldDocs){
                Integer oldVersion = appGrpPrimaryDocDto.getVersion();
                if(configDocId.equals(appGrpPrimaryDocDto.getSvcComDocId()) && seqNum == appGrpPrimaryDocDto.getSeqNum()){
                    canFound = true;
                    if(md5Code.equals(appGrpPrimaryDocDto.getMd5Code())){
                        if(!StringUtil.isEmpty(oldVersion)){
                            version = oldVersion;
                        }
                    }else{
                        version = getVersion(appGrpId,configDocId,appNo,appType,dupForPrem,seqNum);
                    }
                    break;
                }
            }
            if(!canFound){
                //last doc is null new rfi not use app no
                version = getVersion(appGrpId,configDocId,appNo,appType,dupForPrem,seqNum);
            }
        }
        log.info(StringUtil.changeForLog("AppGrpPrimaryDocVersion end..."));
        return version;
    }

    private Integer getVersion(String appGrpId,String configDocId,String appNo,String appType,String dupForPrem,int seqNum){
        Integer version = 1;

        //common doc
        if("0".equals(dupForPrem)){
            if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){
                AppGrpPrimaryDocDto maxVersionDocDto = appSubmissionService.getMaxVersionPrimaryComDoc(appGrpId,configDocId,String.valueOf(seqNum));
                Integer maxVersion = maxVersionDocDto.getVersion();
                String fileRepoId = maxVersionDocDto.getFileRepoId();
                if(!StringUtil.isEmpty(maxVersion) &&  !StringUtil.isEmpty(fileRepoId)){
                    version = maxVersionDocDto.getVersion() + 1;
                }
            }else{
                AppSvcDocDto maxVersionDocDto = appSubmissionService.getMaxVersionSvcComDoc(appGrpId,configDocId,String.valueOf(seqNum));
                if(!StringUtil.isEmpty(maxVersionDocDto.getVersion()) && !StringUtil.isEmpty(maxVersionDocDto.getFileRepoId())){
                    version = maxVersionDocDto.getVersion() + 1;
                }
            }

        }else if("1".equals(dupForPrem)){
            if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){
                AppGrpPrimaryDocDto maxVersionDocDto = appSubmissionService.getMaxVersionPrimarySpecDoc(appGrpId,configDocId,appNo,String.valueOf(seqNum));
                if(!StringUtil.isEmpty(maxVersionDocDto.getVersion()) && !StringUtil.isEmpty(maxVersionDocDto.getFileRepoId())){
                    version = maxVersionDocDto.getVersion() + 1;
                }
            }else{
                AppSvcDocDto searchDto = new AppSvcDocDto();
                searchDto.setAppGrpId(appGrpId);
                searchDto.setSvcDocId(configDocId);
                searchDto.setSeqNum(seqNum);
                AppSvcDocDto maxVersionDocDto = appSubmissionService.getMaxVersionSvcSpecDoc(searchDto,appNo);
                if(!StringUtil.isEmpty(maxVersionDocDto.getVersion()) && !StringUtil.isEmpty(maxVersionDocDto.getFileRepoId())){
                    version = maxVersionDocDto.getVersion() + 1;
                }
            }
        }
        return version;
    }

    private static String  genPageName(Object prefix,String name,Object suffix){
        return prefix + name + suffix;
    }

    private static AppGrpPrimaryDocDto getAppGrpPrimaryDocByConfigIdAndSeqNum(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos,String configId,int seqNum,String premVal,String premType){
        log.debug(StringUtil.changeForLog("getAppGrpPrimaryDocByConfigIdAndSeqNum start..."));
        AppGrpPrimaryDocDto appGrpPrimaryDocDto = null;
        if(!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos)){
            log.debug("configId is {}", configId);
            log.debug("seqNum is {}", seqNum);
            log.debug("premIndex is {}", premVal);
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto1:appGrpPrimaryDocDtos){
                String currPremVal = "";
                String currPremType = "";
                if(!StringUtil.isEmpty(appGrpPrimaryDocDto1.getPremisessName())){
                    currPremVal = appGrpPrimaryDocDto1.getPremisessName();
                }
                if(!StringUtil.isEmpty(appGrpPrimaryDocDto1.getPremisessType())){
                    currPremType = appGrpPrimaryDocDto1.getPremisessType();
                }
                if(!StringUtil.isEmpty(appGrpPrimaryDocDto1.getFileRepoId())
                        && configId.equals(appGrpPrimaryDocDto1.getSvcComDocId())
                        && seqNum == appGrpPrimaryDocDto1.getSeqNum()
                        && premVal.equals(currPremVal)
                        && premType.equals(currPremType)){
                    try {
                        appGrpPrimaryDocDto = (AppGrpPrimaryDocDto) CopyUtil.copyMutableObject(appGrpPrimaryDocDto1);
                    } catch (CloneNotSupportedException e) {
                        log.error(StringUtil.changeForLog("copy appGrpPrimaryDocDto error !!!"));
                    }
                    break;
                }
            }
        }
        return appGrpPrimaryDocDto;
    }


    private void genPrimaryDoc(Map<String, File> fileMap,String docKey,HcsaSvcDocConfigDto hcsaSvcDocConfigDto,
                                      Map<String,File> saveFileMap,List<AppGrpPrimaryDocDto> currDocDtoList,List<AppGrpPrimaryDocDto> newAppGrpPrimaryDocDtoList,
                                      String premVal,String premType,boolean isRfi,List<AppGrpPrimaryDocDto> oldPrimaryDotList,String appGrpId,String appNo,String appType,String dupForPrem){
        if(fileMap != null){
            fileMap.forEach((k,v)->{
                int index = k.indexOf(docKey);
                String seqNumStr = k.substring(index+docKey.length());
                int seqNum = -1;
                try{
                    seqNum = Integer.parseInt(seqNumStr);
                }catch (Exception e){
                    log.error(StringUtil.changeForLog("doc seq num can not parse to int"));
                }
                AppGrpPrimaryDocDto primaryDocDto = new AppGrpPrimaryDocDto();
                if(v != null){
                    primaryDocDto.setSvcComDocId(hcsaSvcDocConfigDto.getId());
                    primaryDocDto.setSvcComDocName(hcsaSvcDocConfigDto.getDocTitle());
                    primaryDocDto.setDocName(v.getName());
                    primaryDocDto.setRealDocSize(v.length());
                    long size = v.length() / 1024;
                    primaryDocDto.setDocSize(Integer.valueOf(String.valueOf(size)));
                    String md5Code = SingeFileUtil.getInstance().getFileMd5(v);
                    primaryDocDto.setMd5Code(md5Code);
                    //if  common ==> set null
                    primaryDocDto.setPremisessName(premVal);
                    primaryDocDto.setPremisessType(premType);
                    primaryDocDto.setSeqNum(seqNum);
                    primaryDocDto.setVersion(getAppGrpPrimaryDocVersion(hcsaSvcDocConfigDto.getId(),oldPrimaryDotList,isRfi,md5Code,appGrpId,appNo,appType,seqNum,dupForPrem));
                    saveFileMap.put(premVal+hcsaSvcDocConfigDto.getId()+seqNum,v);
                }else{
                    primaryDocDto = getAppGrpPrimaryDocByConfigIdAndSeqNum(currDocDtoList,hcsaSvcDocConfigDto.getId(),seqNum,premVal,premType);
                }
                //the data is retrieved from the DTO a second time
                fileMap.put(k,null);
                if(primaryDocDto != null){
                    newAppGrpPrimaryDocDtoList.add(primaryDocDto);
                }
            });
        }
    }
    private void saveFileAndSetFileId(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList,Map<String,File> saveFileMap){
        Map<String,File> passValidateFileMap = IaisCommonUtils.genNewHashMap();
        for (AppGrpPrimaryDocDto primaryDocDto : appGrpPrimaryDocDtoList) {
            if(primaryDocDto.isPassValidate()){
                String premIndex = "";
                if(!StringUtil.isEmpty(primaryDocDto.getPremisessName())){
                    premIndex = primaryDocDto.getPremisessName();
                }
                String fileMapKey = premIndex + primaryDocDto.getSvcComDocId() + primaryDocDto.getSeqNum();
                File file = saveFileMap.get(fileMapKey);
                if(file != null){
                    passValidateFileMap.put(fileMapKey,file);
                }
            }
        }
        if(passValidateFileMap.size() > 0){
            List<File> fileList = new ArrayList<>(passValidateFileMap.values());
            List<String> fileRepoIdList = appSubmissionService.saveFileList(fileList);
            int i = 0;
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDocDtoList){
                String premIndexNo = appGrpPrimaryDocDto.getPremisessName();
                if(StringUtil.isEmpty(premIndexNo)){
                    premIndexNo = "";
                }
                String saveFileMapKey = premIndexNo+appGrpPrimaryDocDto.getSvcComDocId()+appGrpPrimaryDocDto.getSeqNum();
                File file = saveFileMap.get(saveFileMapKey);
                if(file != null){
                    appGrpPrimaryDocDto.setFileRepoId(fileRepoIdList.get(i));
                    i++;
                }
            }
        }

    }

    private AppGrpPrimaryDocDto getAppGrpPrimaryDocByConfigIdAndPremIndex(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos,String config,String premIndex){
        AppGrpPrimaryDocDto appGrpPrimaryDocDto = null;
        if(!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos)){
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto1:appGrpPrimaryDocDtos){
                String currPremVal = appGrpPrimaryDocDto1.getPremisessName();
                if(StringUtil.isEmpty(currPremVal)){
                    currPremVal = "";
                }
                if(config.equals(appGrpPrimaryDocDto1.getSvcComDocId()) && premIndex.equals(currPremVal)){
                    appGrpPrimaryDocDto = appGrpPrimaryDocDto1;
                    break;
                }
            }
        }
        return appGrpPrimaryDocDto;
    }
}


