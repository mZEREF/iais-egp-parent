package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.helper.ConfigHelper;
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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcBusinessDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationSubDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.OperationHoursReloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.PersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RenewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessHciDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppSpecifiedLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.NewApplicationConstant;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.dto.AppSelectSvcDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.PmtReturnUrlDto;
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
import com.ecquaria.cloud.moh.iais.rfi.impl.RfiLoadingCheckImplForRenew;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.StepColorService;
import com.ecquaria.cloud.moh.iais.service.WithOutRenewalService;
import com.ecquaria.cloud.moh.iais.service.client.AppGrpPaymentClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigFeClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.impl.ServiceInfoChangeEffectPersonForRFC;
import com.ecquaria.cloud.moh.iais.utils.DealSessionUtil;
import com.ecquaria.cloud.moh.iais.utils.SingeFileUtil;
import com.ecquaria.cloud.moh.iais.validate.declarationsValidate.DeclarationsUtil;
import com.ecquaria.sz.commons.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.servlet.webflow.HttpHandler;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    public static final String ALL_SVC_NAMES = "allSvcNames";

    public static final String REQUESTINFORMATIONCONFIG = "requestInformationConfig";
    public static final String ACKSTATUS = "AckStatus";
    public static final String ACKMESSAGE = "AckMessage";
    public static final String SERVICEALLPSNCONFIGMAP = "ServiceAllPsnConfigMap";
    public static final String FIRESTOPTION = "Please Select";
    public static final String LIC_PREMISES_MAP = "LicAppGrpPremisesDtoMap";
    public static final String APP_PREMISES_MAP = "AppGrpPremisesDtoMap";
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
    public static final String DRAFT_NUMBER = "DraftNumber";
    //isClickEdit
    public static final String IS_EDIT = "isEdit";

    public static final String LICENSEE_MAP = "LICENSEE_MAP";
    public static final String LICENSEE_OPTIONS = "LICENSEE_OPTIONS";

    public static final String CURR_ORG_USER_ACCOUNT = "currOrgUserAccount";

    public static final String APP_SUBMISSIONS = "appSubmissionDtos";
    public static final String ACK_APP_SUBMISSIONS = "ackPageAppSubmissionDto";
    public static final String RFC_APP_GRP_PREMISES_DTO_LIST = "applicationAppGrpPremisesDtoList";

    @Autowired
    private ServiceConfigService serviceConfigService;
    @Autowired
    private HcsaConfigFeClient hcsaConfigFeClient;
    @Autowired
    private AppSubmissionService appSubmissionService;
    @Autowired
    private RequestForChangeService requestForChangeService;

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
    private ApplicationFeClient applicationFeClient;
    @Autowired
    private EventBusHelper eventBusHelper;
    @Autowired
    private StepColorService stepColorService;

    @Autowired
    private ServiceInfoChangeEffectPersonForRFC serviceInfoChangeEffectPersonForRFC;

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

    @Autowired
    private HcsaAppClient hcsaAppClient;
    @Value("${iais.current.domain}")
    private String currentDomain;
    @Autowired
    private Environment env;
    @Autowired
    AppGrpPaymentClient appGrpPaymentClient;
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
        String draftNo = ParamUtil.getMaskedString(bpc.request, DRAFT_NUMBER);
        //for rfi loading
        String appNo = ParamUtil.getMaskedString(bpc.request, "appNo");
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_NEW_APPLICATION, AuditTrailConsts.FUNCTION_NEW_APPLICATION);
        log.info(StringUtil.changeForLog("DraftNumber: + " + draftNo + " ----- AppNo: " + appNo));
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
        // app type and licence id
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,
                NewApplicationDelegator.APPSUBMISSIONDTO);
        if (appSubmissionDto != null && appSubmissionDto.getAppSvcRelatedInfoDtoList() != null) {
            for (AppSvcRelatedInfoDto dto : appSubmissionDto.getAppSvcRelatedInfoDtoList()) {
                if (StringUtil.isEmpty(dto.getApplicationType())) {
                    dto.setApplicationType(appSubmissionDto.getAppType());
                }
                if (StringUtil.isEmpty(dto.getLicenceId())) {
                    dto.setLicenceId(appSubmissionDto.getLicenceId());
                }
            }
        }
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
        // init sub licensee
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        SubLicenseeDto subLicenseeDto = appSubmissionDto.getSubLicenseeDto();
        if (subLicenseeDto == null) {
            subLicenseeDto = new SubLicenseeDto();
            appSubmissionDto.setSubLicenseeDto(subLicenseeDto);
        }
        SubLicenseeDto orgLicensee = appSubmissionService.getSubLicenseeByLicenseeId(loginContext.getLicenseeId(),
                loginContext.getUenNo());
        orgLicensee.setClaimUenNo(subLicenseeDto.getClaimUenNo());
        orgLicensee.setClaimCompanyName(subLicenseeDto.getClaimCompanyName());
        if (OrganizationConstants.LICENSEE_SUB_TYPE_COMPANY.equals(subLicenseeDto.getLicenseeType())
                || OrganizationConstants.LICENSEE_SUB_TYPE_SOLO.equals(orgLicensee.getLicenseeType())) {
            subLicenseeDto = (SubLicenseeDto) CopyUtil.copyMutableObject(orgLicensee);
            appSubmissionDto.setSubLicenseeDto(subLicenseeDto);
        }
        // init option, map and some fields
        if (!OrganizationConstants.LICENSEE_SUB_TYPE_SOLO.equals(subLicenseeDto.getLicenseeType())) {
            Map<String, SubLicenseeDto> licenseeMap = (Map<String, SubLicenseeDto>) bpc.request.getSession().getAttribute(LICENSEE_MAP);
            if (licenseeMap == null) {
                List<SubLicenseeDto> subLicenseeDtoList = licenceClient.getIndividualSubLicensees(loginContext.getOrgId()).getEntity();
                licenseeMap = NewApplicationHelper.genSubLicessMap(subLicenseeDtoList);
                bpc.request.getSession().setAttribute(LICENSEE_MAP, licenseeMap);
            }
            bpc.request.setAttribute(LICENSEE_OPTIONS, NewApplicationHelper.genSubLicessOption(licenseeMap));
            String assignSelect = subLicenseeDto.getAssignSelect();
            if ((StringUtil.isEmpty(assignSelect) || IaisEGPConstant.ASSIGN_SELECT_ADD_NEW.equals(assignSelect))
                    && OrganizationConstants.LICENSEE_SUB_TYPE_INDIVIDUAL.equals(subLicenseeDto.getLicenseeType())) {
                String assigned = NewApplicationHelper.getAssignSelect(licenseeMap.keySet(), subLicenseeDto.getNationality(),
                        subLicenseeDto.getIdType(), subLicenseeDto.getIdNumber());
                if (StringUtil.isEmpty(assignSelect) || !"-1".equals(assigned) && !IaisEGPConstant.ASSIGN_SELECT_ADD_NEW.equals(
                        assigned)) {
                    subLicenseeDto.setAssignSelect(assigned);
                }
            }
        }
        if (StringUtil.isEmpty(subLicenseeDto.getUenNo())) {
            subLicenseeDto.setUenNo(loginContext.getUenNo());
        }
        bpc.request.setAttribute("subLicenseeDto", orgLicensee);
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
        boolean isGetDataFromPage = NewApplicationHelper.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_LICENSEE, isEdit, isRfi);
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
                appEditSelectDto.setLicenseeEdit(NewApplicationHelper.canLicenseeEdit(appSubmissionDto, isRfi));
            }
            String actionValue = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
            if (!"saveDraft".equals(actionValue)) {
                appSubmissionService.validateSubLicenseeDto(errorMap, subLicenseeDto, bpc.request);
            }
        }

        if (!errorMap.isEmpty()) {
            //set audit
            NewApplicationHelper.setAudiErrMap(isRfi, appSubmissionDto.getAppType(), errorMap, appSubmissionDto.getRfiAppNo(),
                    appSubmissionDto.getLicenceNo());
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "licensee");
            bpc.request.setAttribute("errormapIs", "error");
            HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute(NewApplicationConstant.CO_MAP);
            coMap.put("licensee", "");
            bpc.request.getSession().setAttribute(NewApplicationConstant.CO_MAP, coMap);
        } else {
            HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute(NewApplicationConstant.CO_MAP);
            coMap.put("licensee", "licensee");
            bpc.request.getSession().setAttribute(NewApplicationConstant.CO_MAP, coMap);
            String actionAdditional = ParamUtil.getString(bpc.request, "crud_action_additional");
            if ("rfcSaveDraft".equals(actionAdditional)) {
                try {
                    doSaveDraft(bpc);
                } catch (IOException e) {
                    log.error("error", e);
                }
            }
        }
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
    }

    private SubLicenseeDto getSubLicenseeDtoFromPage(HttpServletRequest request) {
        String licenseeType = ParamUtil.getString(request, "licenseeType");
        SubLicenseeDto dto = null;
        // Check licensee type
        if (OrganizationConstants.LICENSEE_SUB_TYPE_COMPANY.equals(licenseeType)
                || OrganizationConstants.LICENSEE_SUB_TYPE_SOLO.equals(licenseeType)) {
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
            dto = appSubmissionService.getSubLicenseeByLicenseeId(loginContext.getLicenseeId(), loginContext.getUenNo());
            if (dto == null) {
                dto = new SubLicenseeDto();
            }
            dto.setLicenseeType(licenseeType);
        } else {
            String assignSelect = ParamUtil.getString(request, "assignSelect");
            dto = getSubLicenseeDtoDetailFromPage(request);
            AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
            if (!ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
                SubLicenseeDto old = appSubmissionDto.getSubLicenseeDto();
                if (old != null) {
                    // RFC/Renew can't edit these fields
                    dto.setIdType(old.getIdType());
                    dto.setIdNumber(StringUtil.toUpperCase(old.getIdNumber()));
                    dto.setLicenseeName(old.getLicenseeName());
                    dto.setLicenseeType(old.getLicenseeType());
                    dto.setNationality(old.getNationality());
                }
                if (OrganizationConstants.LICENSEE_SUB_TYPE_INDIVIDUAL.equals(dto.getLicenseeType())) {
                    dto.setAssignSelect(NewApplicationHelper.getPersonKey(dto.getNationality(), dto.getIdType(), dto.getIdNumber()));
                } else {
                    dto.setAssignSelect(NewApplicationConstant.NEW_PSN);
                }
            } else {
                dto.setAssignSelect(assignSelect);
                dto.setLicenseeType(licenseeType);
            }
            if (OrganizationConstants.LICENSEE_SUB_TYPE_INDIVIDUAL.equals(dto.getLicenseeType())) {
                Map<String, SubLicenseeDto> licenseeMap = (Map<String, SubLicenseeDto>) request.getSession().getAttribute(
                        LICENSEE_MAP);
                Optional<SubLicenseeDto> optional = Optional.ofNullable(licenseeMap).map(map -> map.get(assignSelect));
                if (optional.isPresent()) {
                    if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
                        MiscUtil.transferEntityDto(optional.get(), SubLicenseeDto.class, null, dto);
                        dto.setAssignSelect(assignSelect);
                        dto.setLicenseeType(OrganizationConstants.LICENSEE_SUB_TYPE_INDIVIDUAL);
                    } else {
                        SubLicenseeDto selectedDto = optional.get();
                        dto.setIdType(selectedDto.getIdType());
                        dto.setIdNumber(StringUtil.toUpperCase(selectedDto.getIdNumber()));
                        dto.setLicenseeName(selectedDto.getLicenseeName());
                    }
                }
            }
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
            if (loginContext != null) {
                dto.setOrgId(loginContext.getOrgId());
                dto.setUenNo(loginContext.getUenNo());
            }
        }
        if (StringUtil.isEmpty(licenseeType)) {
            licenseeType = dto.getLicenseeType();
        }
        if (OrganizationConstants.LICENSEE_SUB_TYPE_INDIVIDUAL.equals(licenseeType)
                || OrganizationConstants.LICENSEE_SUB_TYPE_SOLO.equals(licenseeType)) {
            String claimUenNo = ParamUtil.getString(request, "claimUenNo");
            String claimCompanyName = ParamUtil.getString(request, "claimCompanyName");
            dto.setClaimUenNo(claimUenNo);
            dto.setClaimCompanyName(claimCompanyName);
        }

        return dto;
    }

    // This is a common method, only do that get data from page
    public static SubLicenseeDto getSubLicenseeDtoDetailFromPage(HttpServletRequest request) {
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
        String nationality = ParamUtil.getString(request, "nationality");

        SubLicenseeDto dto = new SubLicenseeDto();
        dto.setIdType(idType);
        dto.setIdNumber(StringUtil.toUpperCase(idNumber));
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
        dto.setNationality(nationality);
        return dto;
    }

    /**
     * StartStep: PreparePremises
     *
     * @param bpc
     * @throws
     */
    public void preparePremises(BaseProcessClass bpc) {
        // Object attribute1 = bpc.request.getAttribute(RfcConst.SWITCH);
        log.info(StringUtil.changeForLog("the do preparePremises start ...."));
        NewApplicationHelper.setTimeList(bpc.request);
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        // boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
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
        if (StringUtil.isEmpty(licenseeId)) {
            licenseeId = NewApplicationHelper.getLicenseeId(bpc.request);
        }
        log.info(StringUtil.changeForLog("The preparePremises licenseeId is -->:" + licenseeId));

        //premise select select options
        NewApplicationHelper.setPremSelect(bpc.request);

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

        //reload dateTime
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        if (!IaisCommonUtils.isEmpty(appGrpPremisesDtoList)) {
            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                NewApplicationHelper.setWrkTime(appGrpPremisesDto);
                NewApplicationHelper.setOldHciCode(appGrpPremisesDto);
            }
        }
        appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtoList);
        //
        int baseSvcCount = 0;
        if (hcsaServiceDtoList != null) {
            for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtoList) {
                if (ApplicationConsts.SERVICE_TYPE_BASE.equalsIgnoreCase(hcsaServiceDto.getSvcType())) {
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
            AppSubmissionDto oldAppSubmissionDto = NewApplicationHelper.getOldAppSubmissionDto(bpc.request);
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

            setSelectLicence(appSubmissionDto,bpc.request);
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
        /*if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())||
                ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())){
            AppSubmissionDto oldAppSubmissionDto = NewApplicationHelper.getOldAppSubmissionDto(bpc.request);
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
                                v.setExistingData(equals ? AppConsts.NO : AppConsts.YES);
                                //bpc.request.getSession().setAttribute("rfc_eqHciCode",String.valueOf(equals));
                            }else if(hciCode==null){
                                v.setExistingData(AppConsts.NO);
                                //bpc.request.getSession().setAttribute("rfc_eqHciCode","true");
                            }
                        }
                    }else {
                        boolean eqHciCode = EqRequestForChangeSubmitResultChange.eqHciCode(appGrpPremisesDtoList.get(0), oldAppGrpPremisesDtoList.get(0));
                        appGrpPremisesDtoList.get(0).setExistingData(AppConsts.NO);
                        //bpc.request.getSession().setAttribute("rfc_eqHciCode",String.valueOf(eqHciCode));
                    }
                }
            }
        }*/
    }

    public void setSelectLicence(AppSubmissionDto appSubmissionDto,HttpServletRequest request) {
        List<AppGrpPremisesDto> appGrpPremisesDtoList1 = appSubmissionDto.getAppGrpPremisesDtoList();
        String licenceNo = appSubmissionDto.getLicenceNo();
        String licenseeId = appSubmissionDto.getLicenseeId();
        for (int i = 0; i < appGrpPremisesDtoList1.size(); i++) {
            List<LicenceDto> licenceDtoByHciCode = requestForChangeService.getLicenceDtoByHciCode(licenseeId,
                    appGrpPremisesDtoList1.get(i), licenceNo);
            appGrpPremisesDtoList1.get(i).setLicenceDtos(licenceDtoByHciCode);
            request.getSession().setAttribute("selectLicence" + i, licenceDtoByHciCode);
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
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        List<HcsaSvcDocConfigDto> hcsaSvcDocDtos;
        boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
        AppSubmissionDto oldAppSubDto = NewApplicationHelper.getOldAppSubmissionDto(bpc.request);
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
            reloadDocMap.forEach((k,v)->Collections.sort(v, Comparator.comparing(AppGrpPrimaryDocDto::getSeqNum)));
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
        if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())
                || ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
            requestForChangeService.svcDocToPresmise(appSubmissionDto);
        }
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
        appSubmissionDto.setMultipleGrpPrimaryDoc(reloadPrimaryDocMap);
        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())){
            if (!NewApplicationHelper.checkIsRfi(bpc.request)) {
                // 113164
                AppSubmissionDto oldAppSubmissionDto = NewApplicationHelper.getOldAppSubmissionDto(bpc.request);
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
            } else {
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
        List<AppSubmissionDto> appSubmissionDtos = (List<AppSubmissionDto>) bpc.request.getSession().getAttribute(APP_SUBMISSIONS);
        HashMap<String, String> coMap  = bpc.request.getSession().getAttribute(NewApplicationConstant.CO_MAP)==null ?
                null : (HashMap<String, String>) bpc.request.getSession().getAttribute(NewApplicationConstant.CO_MAP);

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
                if (amount != null) {
                    total = total + amount;
                }
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
            bpc.request.getSession().setAttribute(APP_SUBMISSIONS, appSubmissionDtos);
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
        List<SelectOption> giroAccSel = NewApplicationHelper.getGiroAccOptions(appSubmissionDtos, appSubmissionDto);
        if (!IaisCommonUtils.isEmpty(giroAccSel)) {
            isGiroAcc = true;
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
        String crud_action_value = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        log.info(StringUtil.changeForLog("##### Action Value: " + crud_action_value));
        if ("undo".equals(crud_action_value)) {
            // Undo All Changes
            NewApplicationHelper.clearPremisesMap(bpc.request);
            return;
        }
        //gen dto
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);

        String isEdit = ParamUtil.getString(bpc.request, IS_EDIT);
        boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
        boolean isGetDataFromPage = NewApplicationHelper.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_PREMISES_INFORMATION, isEdit, isRfi);
        log.info(StringUtil.changeForLog("isGetDataFromPage:" + isGetDataFromPage));
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
        String crud_action_additional = ParamUtil.getString(bpc.request, "crud_action_additional");
        if (!"saveDraft".equals(crud_action_value)) {
            boolean isNeedShowValidation = !"back".equals(crud_action_value);
            List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST);
            List<String> premisesHciList = appSubmissionService.getHciFromPendAppAndLic(appSubmissionDto.getLicenseeId(), hcsaServiceDtos);
            ParamUtil.setSessionAttr(bpc.request, NewApplicationConstant.PREMISES_HCI_LIST, (Serializable) premisesHciList);
            AppSubmissionDto oldAppSubmissionDto = NewApplicationHelper.getOldAppSubmissionDto(bpc.request);

            String actionType = bpc.request.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);

            Map<String, String> errorMap = requestForChangeService.doValidatePremiss(appSubmissionDto, oldAppSubmissionDto,
                    premisesHciList, isRfi, true);

            String crud_action_type_continue = bpc.request.getParameter("crud_action_type_continue");
            bpc.request.setAttribute("continueStep", actionType);
            bpc.request.setAttribute("crudActionTypeContinue", crud_action_additional);
            if ("continue".equals(crud_action_type_continue)) {
                errorMap.remove("hciNameUsed");
            }
            String hciNameUsed = errorMap.get("hciNameUsed");
            if (errorMap.size() == 1 && hciNameUsed != null) {
                ParamUtil.setRequestAttr(bpc.request, "hciNameUsed", "hciNameUsed");
                ParamUtil.setRequestAttr(bpc.request, "newAppPopUpMsg", hciNameUsed);
            }
            // check result
            HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute(NewApplicationConstant.CO_MAP);
            if (errorMap.size() > 0) {
                if (isNeedShowValidation) {
                    //set audit
                    NewApplicationHelper.setAudiErrMap(isRfi,appSubmissionDto.getAppType(),errorMap,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "premises");
                    bpc.request.setAttribute("errormapIs", "error");
                }
                coMap.put("premises", "");
            } else {
                coMap.put("premises", "premises");
                if ("rfcSaveDraft".equals(crud_action_additional)) {
                    try {
                        doSaveDraft(bpc);
                    } catch (IOException e) {
                        log.error("error", e);
                    }
                }
            }
            // coMap.put("serviceConfig", sB.toString());
            bpc.request.getSession().setAttribute(NewApplicationConstant.CO_MAP, coMap);
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
        List<AppGrpPrimaryDocDto> newAppGrpPrimaryDocDtoList = IaisCommonUtils.genNewArrayList();
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        AppSubmissionDto oldSubmissionDto = NewApplicationHelper.getOldAppSubmissionDto(bpc.request);
        if (oldSubmissionDto == null) {
            oldSubmissionDto = new AppSubmissionDto();
        }
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

        boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
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
        }
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionService.documentValid(bpc.request, errorMap,true);
        doIsCommom(bpc.request, errorMap);
        saveFileAndSetFileId(appGrpPrimaryDocDtos,saveFileMap);
        appSubmissionDto.setAppGrpPrimaryDocDtos(appGrpPrimaryDocDtos);
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        // check CO Map
        HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute(NewApplicationConstant.CO_MAP);
        if (errorMap.isEmpty()) {
            coMap.put(NewApplicationConstant.SECTION_DOCUMENT, "document");
        } else {
            coMap.put(NewApplicationConstant.SECTION_DOCUMENT, "");
        }
        bpc.request.getSession().setAttribute(NewApplicationConstant.CO_MAP, coMap);
        String actionValue = ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        if (errorMap.size() > 0 && !"back".equals(actionValue)) {
            //set audit
            bpc.request.setAttribute("errormapIs", "error");
            NewApplicationHelper.setAudiErrMap(isRfi,appSubmissionDto.getAppType(),errorMap,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setSessionAttr(bpc.request, APPGRPPRIMARYDOCERRMSGMAP, (Serializable) errorMap);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "documents");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, "documents");
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
        boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        boolean needNewDeclaration = false;
        if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
            if (!StringUtil.isEmpty(isGroupLic) && AppConsts.YES.equals(isGroupLic)) {
                appSubmissionDto.setGroupLic(true);
            } else {
                appSubmissionDto.setGroupLic(false);
            }
            needNewDeclaration = !isRfi;
        } else if (!isRfi && ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
            AppSubmissionDto oldAppSubmissionDto = NewApplicationHelper.getOldAppSubmissionDto(bpc.request);
            List<AppGrpPremisesDto> oldAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
            List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            if (oldAppGrpPremisesDtoList != null && appGrpPremisesDtoList != null) {
                for (int i = 0; i < appGrpPremisesDtoList.size(); i++) {
                    boolean eqHciNameChange = EqRequestForChangeSubmitResultChange.eqHciNameChange(appGrpPremisesDtoList.get(i),
                            oldAppGrpPremisesDtoList.get(i));
                    if (eqHciNameChange) {
                        needNewDeclaration = true;
                        break;
                    }
                }
            }
            String verifyInfoCheckbox = ParamUtil.getString(bpc.request, "verifyInfoCheckbox");
            appSubmissionDto.setUserAgreement(AppConsts.YES.equals(verifyInfoCheckbox));
        }
        if (needNewDeclaration) {
            // declaration
            appSubmissionDto.setAppDeclarationMessageDto(appSubmissionService.getAppDeclarationMessageDto(bpc.request, appSubmissionDto.getAppType()));
            DeclarationsUtil.declarationsValidate(errorMap, appSubmissionDto.getAppDeclarationMessageDto(),
                    appSubmissionDto.getAppType());
            // uploaded files
            appSubmissionDto.setAppDeclarationDocDtos(appSubmissionService.getDeclarationFiles(appSubmissionDto.getAppType(), bpc.request));
            String preQuesKindly = appSubmissionDto.getAppDeclarationMessageDto().getPreliminaryQuestionKindly();
            // validation
            appSubmissionService.validateDeclarationDoc(errorMap, appSubmissionService.getFileAppendId(appSubmissionDto.getAppType()),
                    preQuesKindly == null ? false : "0".equals(preQuesKindly), bpc.request);
        }

        if (!isRfi && ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
            String effectiveDateStr = ParamUtil.getString(bpc.request, "rfcEffectiveDate");
            appSubmissionDto.setEffectiveDateStr(effectiveDateStr);
            if (!StringUtil.isEmpty(effectiveDateStr)) {
                if (validateEffectiveDate("rfcEffectiveDate", effectiveDateStr, errorMap)) {
                    Date effDate = DateUtil.parseDate(effectiveDateStr, Formatter.DATE);
                    appSubmissionDto.setEffectiveDate(effDate);
                }
            } else {
                appSubmissionDto.setEffectiveDate(null);
            }
            effectiveDateStr = ParamUtil.getString(bpc.request, "effectiveDt");
            if (!StringUtil.isEmpty(effectiveDateStr)) {
                validateEffectiveDate("effectiveDt", effectiveDateStr, errorMap);
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

    private boolean validateEffectiveDate(String field, String effectiveDateStr, Map<String, String> errorMap) {
        boolean isValid = true;
        if (!CommonValidator.isDate(effectiveDateStr)) {
            errorMap.put(field, MessageUtil.getMessageDesc("GENERAL_ERR0033"));
            isValid = false;
        } else {
            int configDateSize = systemParamConfig.getRfcPeriodEffdate();
            LocalDate effectiveDate = LocalDate.parse(effectiveDateStr, DateTimeFormatter.ofPattern(Formatter.DATE));
            LocalDate today = LocalDate.now();
            LocalDate configDate = LocalDate.now().plusDays(configDateSize);
            if (!effectiveDate.isAfter(today)) {
                errorMap.put(field, MessageUtil.getMessageDesc("RFC_ERR012"));
                isValid = false;
            } else if (effectiveDate.isAfter(configDate)) {
                String errorMsg = MessageUtil.getMessageDesc("RFC_ERR008");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Formatter.DATE);
                errorMsg = errorMsg.replace("{date}", configDate.format(dtf));
                errorMap.put(field, errorMsg);
                isValid = false;
            }
        }
        return isValid;
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
        List<AppSubmissionDto> ackSubmissionDtos = (List<AppSubmissionDto>) bpc.request.getSession().getAttribute(ACK_APP_SUBMISSIONS);
        String switch2 = "loading";
        String pmtMethod = appSubmissionDto.getPaymentMethod();
        if (StringUtil.isEmpty(pmtMethod)) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "payment");
            return;
        }
        if (!StringUtil.isEmpty(pmtMethod) && ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(pmtMethod)) {
            switch2 = "ack";
            String txnDt = Formatter.formatDate(new Date());
            ParamUtil.setSessionAttr(bpc.request, "txnDt", txnDt);
        }
        String result = ParamUtil.getMaskedString(bpc.request, "result");
        String pmtRefNo = ParamUtil.getMaskedString(bpc.request, "reqRefNo");
        log.info(StringUtil.changeForLog("Payment result: " + result + "; reqRefNo: " + pmtRefNo
                + "; Draft No.: " + appSubmissionDto.getDraftNo() + "; App Group No: " + appSubmissionDto.getAppGrpNo()));
        if (!StringUtil.isEmpty(result)) {
            if ("success".equals(result) && !StringUtil.isEmpty(pmtRefNo)) {
                log.debug(StringUtil.changeForLog("online payment success ..."));
                try {
                    if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
                        List<AppSubmissionDto> appSubmissionDtos1 = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request,
                                APP_SUBMISSIONS);
                        if (appSubmissionDtos1 == null || appSubmissionDtos1.size() == 0) {
                            appSubmissionDtos1 = IaisCommonUtils.genNewArrayList();
                            appSubmissionDtos1.add(appSubmissionDto);
                        }
                        requestForChangeService.sendRfcSubmittedEmail(appSubmissionDtos1, appSubmissionDto.getPaymentMethod());
                    }
                } catch (Exception e) {
                    log.info(StringUtil.changeForLog(e.getMessage()), e);
                }

                List<String> appGrpIds = IaisCommonUtils.genNewArrayList();
                if (ackSubmissionDtos != null) {
                    for (AppSubmissionDto appSubmissionDto1 : ackSubmissionDtos) {
                        ApplicationGroupDto appGrp = new ApplicationGroupDto();
                        appGrp.setId(appSubmissionDto1.getAppGrpId());
                        appGrp.setPmtRefNo(pmtRefNo);
                        appGrp.setGroupNo(appSubmissionDto1.getAppGrpNo());
                        appGrp.setAutoRfc(appSubmissionDto1.isAutoRfc());
                        Double amount = appSubmissionDto1.getAmount();
                        if (amount != null && !MiscUtil.doubleEquals(0.0, amount)) {
                            appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                            appGrp.setPayMethod(appSubmissionDto.getPaymentMethod());
                        } else {
                            appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
                            appGrp.setPayMethod(appSubmissionDto.getPaymentMethod());
                        }
                        log.info(StringUtil.changeForLog("Payment response data is " + JsonUtil.parseToJson(appGrp)));
                        applicationFeClient.updatePaymentByAppGrp(appGrp);
                        appGrpIds.add(appSubmissionDto1.getAppGrpId());
                    }
                }
                String txnDt = ParamUtil.getMaskedString(bpc.request, "txnDt");
                String txnRefNo = ParamUtil.getMaskedString(bpc.request, "txnRefNo");
                ParamUtil.setSessionAttr(bpc.request, "txnDt", txnDt);
                ParamUtil.setSessionAttr(bpc.request, "txnRefNo", txnRefNo);
                switch2 = "ack";

                //update status for transfor payment
                String appGrpId = appSubmissionDto.getAppGrpId();
                if (!appGrpIds.contains(appGrpId)) {
                    ApplicationGroupDto appGrp = new ApplicationGroupDto();
                    appGrp.setId(appGrpId);
                    appGrp.setPmtRefNo(pmtRefNo);
                    appGrp.setPaymentDt(new Date());
                    appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                    appGrp.setPayMethod(appSubmissionDto.getPaymentMethod());
                    serviceConfigService.updatePaymentStatus(appGrp);
                }
                //send email
                if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
                    try {
                        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,
                                AppConsts.SESSION_ATTR_LOGIN_USER);
                        appSubmissionService.sendEmailAndSMSAndMessage(appSubmissionDto, loginContext.getUserName());
                    } catch (Exception e) {
                        log.error(StringUtil.changeForLog("send email error ...."));
                    }
                }
            } else {
                appSubmissionService.updateDraftStatus(appSubmissionDto.getDraftNo(),AppConsts.COMMON_STATUS_ACTIVE);
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
        } else {
            PaymentRequestDto paymentRequestDtoSuss=null;
            List<PaymentRequestDto> paymentRequestDtos= appGrpPaymentClient.getPaymentRequestDtoByReqRefNoLike(AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY, appSubmissionDto.getAppGrpNo()).getEntity();
            if(paymentRequestDtos!=null){
                for (PaymentRequestDto paymentRequestDto:paymentRequestDtos
                ) {
                    if("success".equals(paymentRequestDto.getStatus())){
                        paymentRequestDtoSuss=paymentRequestDto;
                        break;
                    }
                }
            }

            PaymentDto paymentDto= appGrpPaymentClient.getPaymentDtoByReqRefNo(
                    AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY, appSubmissionDto.getAppGrpNo()).getEntity();
            if(paymentRequestDtoSuss!=null){
                PaymentDto paymentDtoSuss= appGrpPaymentClient.getPaymentDtoByReqRefNo(
                        AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY, paymentRequestDtoSuss.getReqRefNo()
                        ).getEntity();
                if(paymentDtoSuss!=null){
                    paymentDto=paymentDtoSuss;
                }
            }
            if(paymentDto!=null&&"success".equals(paymentDto.getPmtStatus())){

                pmtRefNo=paymentDto.getReqRefNo();

                log.debug(StringUtil.changeForLog("online payment success ..."));
                try {
                    if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
                        List<AppSubmissionDto> appSubmissionDtos1 = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request,
                                APP_SUBMISSIONS);
                        if (appSubmissionDtos1 == null || appSubmissionDtos1.size() == 0) {
                            appSubmissionDtos1 = IaisCommonUtils.genNewArrayList();
                            appSubmissionDtos1.add(appSubmissionDto);
                        }
                        requestForChangeService.sendRfcSubmittedEmail(appSubmissionDtos1, appSubmissionDto.getPaymentMethod());
                    }
                } catch (Exception e) {
                    log.info(StringUtil.changeForLog(e.getMessage()), e);
                }

                List<String> appGrpIds = IaisCommonUtils.genNewArrayList();
                if (ackSubmissionDtos != null) {
                    for (AppSubmissionDto appSubmissionDto1 : ackSubmissionDtos) {
                        ApplicationGroupDto appGrp = new ApplicationGroupDto();
                        appGrp.setId(appSubmissionDto1.getAppGrpId());
                        appGrp.setPmtRefNo(pmtRefNo);
                        appGrp.setGroupNo(appSubmissionDto1.getAppGrpNo());
                        appGrp.setAutoRfc(appSubmissionDto1.isAutoRfc());
                        Double amount = appSubmissionDto1.getAmount();
                        if (amount != null && !MiscUtil.doubleEquals(0.0, amount)) {
                            appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                            appGrp.setPayMethod(appSubmissionDto.getPaymentMethod());
                        } else {
                            appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
                            appGrp.setPayMethod(appSubmissionDto.getPaymentMethod());
                        }
                        log.info(StringUtil.changeForLog("Payment response data is " + JsonUtil.parseToJson(appGrp)));
                        applicationFeClient.updatePaymentByAppGrp(appGrp);
                        appGrpIds.add(appSubmissionDto1.getAppGrpId());
                    }
                }
                String txnDt = ParamUtil.getMaskedString(bpc.request, "txnDt");
                String txnRefNo = ParamUtil.getMaskedString(bpc.request, "txnRefNo");
                ParamUtil.setSessionAttr(bpc.request, "txnDt", txnDt);
                ParamUtil.setSessionAttr(bpc.request, "txnRefNo", txnRefNo);
                switch2 = "ack";

                //update status for transfor payment
                String appGrpId = appSubmissionDto.getAppGrpId();
                if (!appGrpIds.contains(appGrpId)) {
                    ApplicationGroupDto appGrp = new ApplicationGroupDto();
                    appGrp.setId(appGrpId);
                    appGrp.setPmtRefNo(pmtRefNo);
                    appGrp.setPaymentDt(new Date());
                    appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                    appGrp.setPayMethod(appSubmissionDto.getPaymentMethod());
                    serviceConfigService.updatePaymentStatus(appGrp);
                }
                //send email
                if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
                    try {
                        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,
                                AppConsts.SESSION_ATTR_LOGIN_USER);
                        appSubmissionService.sendEmailAndSMSAndMessage(appSubmissionDto, loginContext.getUserName());
                    } catch (Exception e) {
                        log.error(StringUtil.changeForLog("send email error ...."));
                    }
                }
            }else {
                appSubmissionService.updateDraftStatus(appSubmissionDto.getDraftNo(),AppConsts.COMMON_STATUS_ACTIVE);
                log.debug(StringUtil.changeForLog("result is empty"));
                //appSubmissionService.updateDraftStatus(appSubmissionDto.getDraftNo(),AppConsts.COMMON_STATUS_ACTIVE);
                switch2 = "loading";
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE,"payment");
            }
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
                IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
            } else if ("ChooseSvc".equals(action)) {
                StringBuilder url = new StringBuilder();
                url.append("https://").append(bpc.request.getServerName()).append("/hcsa-licence-web/eservice/INTERNET/MohServiceFeMenu");
                String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
                IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
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
        HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute(NewApplicationConstant.CO_MAP);

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
        appSubmissionDto = appSubmissionService.doSaveDraft(appSubmissionDto);
        if("exitSaveDraft".equals(crud_action_additional)){
            jumpYeMian(bpc.request, bpc.response);
            return;
        }
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        bpc.request.setAttribute("saveDraftSuccess", "success");
        log.info(StringUtil.changeForLog("the do doSaveDraft end ...."));
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
        IaisEGPHelper.redirectUrl(response, tokenUrl);
    }

    private void sendURL(HttpServletRequest request, HttpServletResponse response, String url) {
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url, request);
        try {
            IaisEGPHelper.redirectUrl(response, tokenUrl);
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
                    String cess_ack002 = MessageUtil.getMessageDesc("CESS_ACK002");
                    ParamUtil.setSessionAttr(bpc.request,"cess_ack002",cess_ack002);
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
                    String hciAddress = MiscUtil.getAddressForApp(blkNo, streetName, buildingName, floorNo, unitNo, postalCode,appGrpPremisesEntityDto.getAppPremisesOperationalUnitDtos());
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
                        svcRelatedInfoView(appSubmissionDto,bpc.request,applicationDto.getServiceId(), appNo);
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
                filtrationAppGrpPremisesDtos(applicationDto,appSubmissionDto,newPremisesDtos);
                appSubmissionDto.setAppGrpPremisesDtoList(newPremisesDtos);
                List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = filterPrimaryDocs(appSubmissionDto.getAppGrpPrimaryDocDtos(),
                        newPremisesDtos);
                String svcId = applicationDto.getServiceId();
                if (!StringUtil.isEmpty(svcId) && !StringUtil.isEmpty(applicationDto.getApplicationNo())) {
                    List<AppSvcRelatedInfoDto> newSvcRelatedInfoDtos = IaisCommonUtils.genNewArrayList();
                    //set doc name
                    List<HcsaSvcDocConfigDto> primaryDocConfig = null;
                    if (appGrpPrimaryDocDtos != null && appGrpPrimaryDocDtos.size() > 0) {
                        primaryDocConfig = serviceConfigService.getPrimaryDocConfigById(appGrpPrimaryDocDtos.get(0).getSvcComDocId());
                    }
                    Optional<AppSvcRelatedInfoDto> optional = appSubmissionDto.getAppSvcRelatedInfoDtoList().stream()
                            .filter(dto -> applicationDto.getApplicationNo().equals(dto.getAppNo()))
                            .findAny();
                    if (!optional.isPresent()) {
                        optional = appSubmissionDto.getAppSvcRelatedInfoDtoList().stream()
                                .filter(dto -> svcId.equals(dto.getServiceId()))
                                .findAny();
                    }
                    if (optional.isPresent()) {
                        AppSvcRelatedInfoDto appSvcRelatedInfoDto = optional.get();
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

    private List<AppGrpPrimaryDocDto> filterPrimaryDocs(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos,
            List<AppGrpPremisesDto> newPremisesDtos) {
        if (appGrpPrimaryDocDtos == null || newPremisesDtos == null) {
            return appGrpPrimaryDocDtos;
        }
        return appGrpPrimaryDocDtos.stream()
                .filter(dto -> dto.getAppGrpPremId() == null ||
                        newPremisesDtos.stream().anyMatch(premise -> dto.getAppGrpPremId().equals(premise.getId())))
                .collect(Collectors.toList());
    }

    private void filtrationAppGrpPremisesDtos( ApplicationDto applicationDto,AppSubmissionDto appSubmissionDto, List<AppGrpPremisesDto> newPremisesDtos){
        AppGrpPremisesEntityDto rfiPremises = appSubmissionService.getPremisesByAppNo(applicationDto.getApplicationNo());
        if (rfiPremises != null) {
            boolean addPremisesSuccess = false;
            for(AppGrpPremisesDto appGrpPremisesDto : appSubmissionDto.getAppGrpPremisesDtoList()){
                if(appGrpPremisesDto.getId().equalsIgnoreCase(rfiPremises.getId())){
                    addPremisesSuccess = true;
                    addAppGrpPremisesDto(appGrpPremisesDto,newPremisesDtos);
                    break;
                }
            }
            if (!addPremisesSuccess) {
                String premKey = NewApplicationHelper.getPremisesKey(rfiPremises);
                for (AppGrpPremisesDto appGrpPremisesDto : appSubmissionDto.getAppGrpPremisesDtoList()) {
                    if (premKey.equals(NewApplicationHelper.getPremisesKey(appGrpPremisesDto))) {
                        addAppGrpPremisesDto(appGrpPremisesDto, newPremisesDtos);
                        break;
                    }
                }
            }
        }
    }

    private void addAppGrpPremisesDto(AppGrpPremisesDto appGrpPremisesDto,List<AppGrpPremisesDto> newPremisesDtos){
        NewApplicationHelper.setWrkTime(appGrpPremisesDto);
        List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodDtos = appGrpPremisesDto.getAppPremPhOpenPeriodList();
        //set ph name
        NewApplicationHelper.setPhName(appPremPhOpenPeriodDtos);
        newPremisesDtos.add(appGrpPremisesDto);
    }


    private void svcRelatedInfoView(AppSubmissionDto appSubmissionDto, HttpServletRequest request, String serviceId, String appNo) {
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfoDtoByServiceId(appSubmissionDto.getAppSvcRelatedInfoDtoList(),
                serviceId, appNo);
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
                    if(IaisCommonUtils.isNotEmpty(appSvcLaboratoryDisciplinesDtoList)){
                        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoListExclude  = getAppSvcLaboratoryDisciplinesDtosByPremises(appSvcLaboratoryDisciplinesDtoList,premises);
                        if( IaisCommonUtils.isNotEmpty(appSvcLaboratoryDisciplinesDtoListExclude)){
                            for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvcLaboratoryDisciplinesDtoListExclude){
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
                            appSvcRelatedInfoDto.setAppSvcLaboratoryDisciplinesDtoList(appSvcLaboratoryDisciplinesDtoListExclude);
                        }
                    }
                }
            }
            ParamUtil.setRequestAttr(request, HCSASERVICEDTO, hcsaServiceDto);
            ParamUtil.setSessionAttr(request, "currentPreviewSvcInfo", appSvcRelatedInfoDto);
    }
    private   List<AppSvcLaboratoryDisciplinesDto> getAppSvcLaboratoryDisciplinesDtosByPremises( List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList,String premises){
        List<AppSvcLaboratoryDisciplinesDto> appSvcDisciplineAllocationDtos = IaisCommonUtils.genNewArrayList();
        if(IaisCommonUtils.isNotEmpty(appSvcLaboratoryDisciplinesDtoList)){
            for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvcLaboratoryDisciplinesDtoList){
                if(appSvcLaboratoryDisciplinesDto.getPremiseVal().equals(premises)){
                   appSvcDisciplineAllocationDtos.add(appSvcLaboratoryDisciplinesDto);
                }
            }
        }
        return appSvcDisciplineAllocationDtos;
    }

    private AppSvcRelatedInfoDto getAppSvcRelatedInfoDtoByServiceId(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos, String serviceId,
            String appNo) {
        Optional<AppSvcRelatedInfoDto> optional = appSvcRelatedInfoDtos.stream()
                .filter(dto -> Objects.equals(serviceId, dto.getServiceId())
                        && Objects.equals(appNo, dto.getAppNo()))
                .findAny();
        if (!optional.isPresent()) {
            optional = appSvcRelatedInfoDtos.stream()
                    .filter(dto -> Objects.equals(appNo, dto.getAppNo()))
                    .findAny();
        }
        if (!optional.isPresent()) {
            optional = appSvcRelatedInfoDtos.stream()
                    .filter(dto -> Objects.equals(serviceId, dto.getServiceId()))
                    .findAny();
        }
        return optional.orElseGet(() -> appSvcRelatedInfoDtos.get(0));
    }

    private List<AppSvcRelatedInfoDto> getOtherAppSvcRelatedInfoDtos(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos,
            String serviceId, String appNo) {
        return appSvcRelatedInfoDtos.stream()
                .filter(dto -> Objects.equals(serviceId, dto.getServiceId())
                        && !Objects.equals(appNo, dto.getAppNo()))
                .collect(Collectors.toList());
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
        //oldAppSubmissionDtos
//        List<AppSubmissionDto> appSubmissionDtoByGroupNo = appSubmissionService.getAppSubmissionDtoByGroupNo(appGrpNo);
        AppSubmissionDto oldAppSubmissionDto = NewApplicationHelper.getOldAppSubmissionDto(bpc.request);
        StringBuilder stringBuilder = new StringBuilder(10);
        stringBuilder.append(appSubmissionDto);
        String str=stringBuilder.toString();
        log.info(StringUtil.changeForLog("appSubmissionDto:" + str));
        stringBuilder.setLength(0);
        stringBuilder.append(oldAppSubmissionDto);
        str=stringBuilder.toString();
        log.info(StringUtil.changeForLog("oldAppSubmissionDto:" + str));
        Map<String, String> doComChangeMap = doComChange(appSubmissionDto, oldAppSubmissionDto);
        if (!doComChangeMap.isEmpty()) {
            initAction("preview", doComChangeMap, appSubmissionDto, bpc.request);
            return;
        }
        log.info("doComChange is ok ...");
        Map<String, String> map = appSubmissionService.doPreviewAndSumbit(bpc);
        if (!map.isEmpty()) {
            initAction("preview", map, appSubmissionDto, bpc.request);
            return;
        }
        // check whether it has been withdrew or not
        String appId = getCurrentRfiAppId(oldAppSubmissionDto);
        List<AppPremiseMiscDto> appPremiseMiscs = appSubmissionService.getActiveWithdrawAppPremiseMiscsByApp(appId);
        if (IaisCommonUtils.isNotEmpty(appPremiseMiscs)) {
            // RFI_ERR002: There is a withdrawal for this application.
            ParamUtil.setRequestAttr(bpc.request, "showRfiWithdrawal", AppConsts.YES);
            initAction("preview", null, appSubmissionDto, bpc.request);
            return;
        }
        //sync person data
//        Map<String,AppSvcPersonAndExtDto> personMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.PERSONSELECTMAP);
//        NewApplicationHelper.syncPsnData(appSubmissionDto,personMap);

        //add other premise
        AppSubmissionDto beforeRemoveAppSubmissionDto = appSubmissionService.getAppSubmissionDtoByAppNo(appNo);
        List<AppGrpPremisesDto> beforeRemoveAppGrpPremisesDtoList = beforeRemoveAppSubmissionDto.getAppGrpPremisesDtoList();
        AppGrpPremisesDto currentAppGrpPremisesDto = appSubmissionDto.getAppGrpPremisesDtoList().get(0);
        String currentPremisesIndexNo = currentAppGrpPremisesDto.getPremisesIndexNo();
        if (IaisCommonUtils.isNotEmpty(beforeRemoveAppGrpPremisesDtoList) && beforeRemoveAppGrpPremisesDtoList.size() > 1){
            oldAppSubmissionDto.setAppGrpPremisesDtoList((List<AppGrpPremisesDto>) CopyUtil.copyMutableObjectList(beforeRemoveAppGrpPremisesDtoList));
            List<AppGrpPremisesDto> newAppGrpPremisesDtos = (List<AppGrpPremisesDto>) CopyUtil.copyMutableObjectList(beforeRemoveAppGrpPremisesDtoList);
            for (int i = newAppGrpPremisesDtos.size()-1; i>=0; i--){
                if (StringUtil.isNotEmpty(currentPremisesIndexNo) &&
                        currentPremisesIndexNo.equals(newAppGrpPremisesDtos.get(i).getPremisesIndexNo())){
                    currentAppGrpPremisesDto.setId(currentPremisesIndexNo);
                    newAppGrpPremisesDtos.set(i, currentAppGrpPremisesDto);
                }else {
                    NewApplicationHelper.setWrkTime(newAppGrpPremisesDtos.get(i));
                }
            }
            appSubmissionDto.setAppGrpPremisesDtoList(newAppGrpPremisesDtos);
            //add other premise doc
            List<AppGrpPrimaryDocDto> oldPrimaryDocDtoList = beforeRemoveAppSubmissionDto.getAppGrpPrimaryDocDtos();
            if (IaisCommonUtils.isNotEmpty(oldPrimaryDocDtoList)){
                for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : oldPrimaryDocDtoList){
                    if (StringUtil.isNotEmpty(appGrpPrimaryDocDto.getAppGrpPremId()) && !appGrpPrimaryDocDto.getAppGrpPremId().equals(currentPremisesIndexNo)){
                        appSubmissionDto.getAppGrpPrimaryDocDtos().add(appGrpPrimaryDocDto);
                    }
                }
            }
            //add other svcInfo person,doc,disciplines
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSubmissionDto.getAppSvcRelatedInfoDtoList()){
                resetRelatedInfoRFI(appSvcRelatedInfoDto, beforeRemoveAppSubmissionDto, appSubmissionDto.getAppGrpPremisesDtoList());
            }
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : oldAppSubmissionDto.getAppSvcRelatedInfoDtoList()){
                resetRelatedInfoRFI(appSvcRelatedInfoDto, beforeRemoveAppSubmissionDto, oldAppSubmissionDto.getAppGrpPremisesDtoList());
            }
        }
        // refresh premiseType premiseAddress
        AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto();
        if (appEditSelectDto != null && appEditSelectDto.isPremisesEdit() && StringUtil.isNotEmpty(currentPremisesIndexNo)){
            String premisesType = currentAppGrpPremisesDto.getPremisesType();
            String address = currentAppGrpPremisesDto.getAddress();

            for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appSubmissionDto.getAppGrpPrimaryDocDtos()){
                if (currentPremisesIndexNo.equals(appGrpPrimaryDocDto.getAppGrpPremId())){
                    appGrpPrimaryDocDto.setPremisessType(premisesType);
                }
            }

            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSubmissionDto.getAppSvcRelatedInfoDtoList()){
                for (AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList()){
                    if (currentPremisesIndexNo.equals(appSvcLaboratoryDisciplinesDto.getPremiseVal())){
                        appSvcLaboratoryDisciplinesDto.setPremiseType(premisesType);
                        appSvcLaboratoryDisciplinesDto.setPremiseGetAddress(address);
                    }
                }

                for (AppSvcBusinessDto appSvcBusinessDto : appSvcRelatedInfoDto.getAppSvcBusinessDtoList()){
                    if (currentPremisesIndexNo.equals(appSvcBusinessDto.getPremIndexNo())){
                        appSvcBusinessDto.setPremType(premisesType);
                        appSvcBusinessDto.setPremAddress(address);
                    }
                }
            }
        }
        NewApplicationHelper.reSetAdditionalFields(appSubmissionDto, oldAppSubmissionDto);
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
        appSubmissionRequestInformationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        //update message statusdo
        //appSubmissionService.updateMsgStatus(msgId, MessageConstants.MESSAGE_STATUS_RESPONSE);
        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType()) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())){
            appSubmissionDto= appSubmissionService.submitRequestRfcRenewInformation(appSubmissionRequestInformationDto, bpc.process);
        }else {
            appSubmissionDto = appSubmissionService.submitRequestInformation(appSubmissionRequestInformationDto, bpc.process);
        }
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
            List<AppSubmissionDto> appSubmissionDtos = new ArrayList<>(1);
            appSubmissionDto.setAmountStr("N/A");
            appSubmissionDtos.add(appSubmissionDto);
            ParamUtil.setSessionAttr(bpc.request, ACK_APP_SUBMISSIONS, (Serializable) appSubmissionDtos);
        }
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "Y");
        ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, "The request for information save success");
        log.info(StringUtil.changeForLog("the do doRequestInformationSubmit end ...."));
    }

    private void initAction(String action, Map<String, String> errorMap, AppSubmissionDto appSubmissionDto,
            HttpServletRequest request) {
        if (IaisCommonUtils.isNotEmpty(errorMap)) {
            boolean isRfi = NewApplicationHelper.checkIsRfi(request);
            if (isRfi) {
                NewApplicationHelper.setAudiErrMap(isRfi, appSubmissionDto.getAppType(), errorMap, appSubmissionDto.getRfiAppNo(),
                        appSubmissionDto.getLicenceNo());
            }
        }
        ParamUtil.setRequestAttr(request, "Msg", errorMap);
        ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, action);
        ParamUtil.setRequestAttr(request, "isrfiSuccess", "N");
    }

    private String getCurrentRfiAppId(AppSubmissionDto oldAppSubmissionDto) {
        if (oldAppSubmissionDto == null || IaisCommonUtils.isEmpty(oldAppSubmissionDto.getAppSvcRelatedInfoDtoList())) {
            return null;
        }
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : oldAppSubmissionDto.getAppSvcRelatedInfoDtoList()) {
            if (Objects.equals(oldAppSubmissionDto.getRfiAppNo(), appSvcRelatedInfoDto.getAppNo())) {
                return appSvcRelatedInfoDto.getAppId();
            }
        }
        return null;
    }

    private void resetRelatedInfoRFI(AppSvcRelatedInfoDto appSvcRelatedInfoDto, AppSubmissionDto appSubmissionDto, List<AppGrpPremisesDto> appGrpPremisesDtoList) {
        if (!ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
            return;
        }
        if (appSvcRelatedInfoDto == null) {
            return;
        }
        List<AppSvcRelatedInfoDto> otherList = getOtherAppSvcRelatedInfoDtos(appSubmissionDto.getAppSvcRelatedInfoDtoList(),
                appSvcRelatedInfoDto.getServiceId(), appSvcRelatedInfoDto.getAppNo());
        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemesByServiceId = serviceConfigService.getHcsaServiceStepSchemesByServiceId(appSvcRelatedInfoDto.getServiceId());
        appSvcRelatedInfoDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemesByServiceId);
        if (otherList != null && !otherList.isEmpty()) {
            otherList.forEach(dto -> {
                List<AppSvcPersonnelDto> appSvcPersonnelDtoList = appSvcRelatedInfoDto.getAppSvcSectionLeaderList();
                List<AppSvcPersonnelDto> otherAppSvcPersonnelDtoList = dto.getAppSvcSectionLeaderList();
                if (appSvcPersonnelDtoList != null && otherAppSvcPersonnelDtoList != null) {
                    for (AppSvcPersonnelDto otherAppSvcPersonnelDto : otherAppSvcPersonnelDtoList){
                        if (!isContainAppSvcPersonnelDto(appSvcPersonnelDtoList, otherAppSvcPersonnelDto)){
                            appSvcPersonnelDtoList.add(otherAppSvcPersonnelDto);
                        }
                    }
                }

                List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
                if (appSvcLaboratoryDisciplinesDtoList != null && dto.getAppSvcLaboratoryDisciplinesDtoList() != null) {
                    appSvcLaboratoryDisciplinesDtoList.addAll(dto.getAppSvcLaboratoryDisciplinesDtoList());
                    appSvcRelatedInfoDto.setAppSvcLaboratoryDisciplinesDtoList(appSvcLaboratoryDisciplinesDtoList);
                }
                List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
                List<AppSvcDisciplineAllocationDto> otherAppSvcDisciplineAllocationDtoList = dto.getAppSvcDisciplineAllocationDtoList();
                if (appSvcDisciplineAllocationDtoList != null && otherAppSvcDisciplineAllocationDtoList != null) {
                    setAppSvcDisciplineAllocationDtoSlIndex(appSvcPersonnelDtoList, otherAppSvcDisciplineAllocationDtoList);
                    appSvcDisciplineAllocationDtoList.addAll(otherAppSvcDisciplineAllocationDtoList);
                    appSvcRelatedInfoDto.setAppSvcDisciplineAllocationDtoList(appSvcDisciplineAllocationDtoList);
                }

                List<AppSvcDocDto> appSvcDocDtoLit = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                List<AppSvcDocDto> otherAppSvcDocDtoLit = dto.getAppSvcDocDtoLit();
                if (otherAppSvcDocDtoLit != null && appSvcDocDtoLit != null) {
                    otherAppSvcDocDtoLit.forEach(doc -> {
                        if (doc.getAppSvcPersonId() != null || doc.getAppGrpPersonId() != null) {
                            doc.setPsnIndexNo(getNewPsnIndexNo(dto.getPersonnels(), appSvcRelatedInfoDto.getPersonnels(), doc.getPsnIndexNo()));
                            appSvcDocDtoLit.add(doc);
                        }
                    });
                    appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtoLit);
                }
                // check personnels
                List<PersonnelDto> personnels = appSvcRelatedInfoDto.getPersonnels();
                if (personnels == null) {
                    personnels = IaisCommonUtils.genNewArrayList();
                }
                if (dto.getPersonnels() != null) {
                    personnels.addAll(dto.getPersonnels());
                }
                appSvcRelatedInfoDto.setPersonnels(personnels);
                //set Laboratory Disciplines Info
                String currentSvcId = dto.getServiceId();
                List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = null;
                if (!StringUtil.isEmpty(currentSvcId)) {
                    hcsaSvcSubtypeOrSubsumedDtos = serviceConfigService.loadLaboratoryDisciplines(currentSvcId);
                }
                if (!IaisCommonUtils.isEmpty(hcsaSvcSubtypeOrSubsumedDtos)) {
                    NewApplicationHelper.setLaboratoryDisciplinesInfo(appGrpPremisesDtoList, appSvcRelatedInfoDto, hcsaSvcSubtypeOrSubsumedDtos);
                }
            });
        }
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
        AppSubmissionDto oldAppSubmissionDto = NewApplicationHelper.getOldAppSubmissionDto(bpc.request);
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        List<AppGrpPremisesDto> oldAppSubmissionDtoAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
        boolean grpPremiseChange = false;
        if (appGrpPremisesDtoList.equals(oldAppSubmissionDtoAppGrpPremisesDtoList)) {
            grpPremiseChange = true;
        }
        if (!grpPremiseChange) {
            for (int i = 0; i < appGrpPremisesDtoList.size(); i++) {
                // all latest licence under the current hci code and licensee, except the current original licence.
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
        boolean isAutoRfc = EqRequestForChangeSubmitResultChange.compareNotChangePersonnel(appSubmissionDto, oldAppSubmissionDto);
        if (isAutoRfc) {
            boolean changeHciName = EqRequestForChangeSubmitResultChange.isChangeHciName(appSubmissionDto.getAppGrpPremisesDtoList(),
                    oldAppSubmissionDto.getAppGrpPremisesDtoList());
            boolean changeInLocation = !EqRequestForChangeSubmitResultChange.compareLocation(appSubmissionDto.getAppGrpPremisesDtoList(),
                    oldAppSubmissionDto.getAppGrpPremisesDtoList());
            boolean eqAddFloorNo = EqRequestForChangeSubmitResultChange.isChangeFloorUnit(appSubmissionDto, oldAppSubmissionDto);
            log.info(StringUtil.changeForLog("changeHciName: " + changeHciName + " - changeInLocation: " + changeInLocation + " - " +
                    "eqAddFloorNo: " + eqAddFloorNo));
            isAutoRfc = !changeInLocation && !eqAddFloorNo && !changeHciName;
        }
        log.info(StringUtil.changeForLog("isAutoRfc: " + isAutoRfc));
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

    /**
         * StartStep: doRequestForChangeSubmit
     *
     * @param bpc
     *
     */
    public void doRequestForChangeSubmit(BaseProcessClass bpc) throws Exception {
        //validate reject  apst050
        log.info(StringUtil.changeForLog("the do doRequestForChangeSubmit start ...."));
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "preview");
        ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "N");
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        AppSubmissionDto oldAppSubmissionDto = NewApplicationHelper.getOldAppSubmissionDto(bpc.request);
        log.info(StringUtil.changeForLog("The original licence No.: " + appSubmissionDto.getLicenceNo()));
        HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute(NewApplicationConstant.CO_MAP);

        String serviceConfig = (String) bpc.request.getSession().getAttribute("serviceConfig");

        stepColorService.setStepColor(coMap,serviceConfig,appSubmissionDto);

        Integer maxFileIndex = (Integer) ParamUtil.getSessionAttr(bpc.request,HcsaFileAjaxController.GLOBAL_MAX_INDEX_SESSION_ATTR);
        if(maxFileIndex == null){
            maxFileIndex = 0;
        }
        appSubmissionDto.setMaxFileIndex(maxFileIndex);
        // validate the submission data
        Map<String, String> map = appSubmissionService.doPreviewAndSumbit(bpc);
        boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        List<AppGrpPremisesDto> oldAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
        if (map.isEmpty() && !isRfi) {
            boolean changeHciName = false;
            if (oldAppGrpPremisesDtoList != null && appGrpPremisesDtoList != null
                    && !appGrpPremisesDtoList.isEmpty() && !oldAppGrpPremisesDtoList.isEmpty()) {
                changeHciName = EqRequestForChangeSubmitResultChange.eqHciNameChange(appGrpPremisesDtoList.get(0),
                        oldAppGrpPremisesDtoList.get(0));
            }
            if (changeHciName) {
                AppDeclarationMessageDto appDeclarationMessageDto = appSubmissionDto.getAppDeclarationMessageDto();
                DeclarationsUtil.declarationsValidate(map, appDeclarationMessageDto, appSubmissionDto.getAppType());
                String preQuesKindly = appDeclarationMessageDto == null ? null : appDeclarationMessageDto.getPreliminaryQuestionKindly();
                appSubmissionService.validateDeclarationDoc(map, appSubmissionService.getFileAppendId(appSubmissionDto.getAppType()),
                        preQuesKindly == null ? false : "0".equals(preQuesKindly), bpc.request);
            }
        }
        if (!map.isEmpty()) {
            //set audit
            log.warn(StringUtil.changeForLog("Error Message: " + map));
            ParamUtil.setRequestAttr(bpc.request, "Msg", map);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(map));
            return;
        }
        String licenceId = appSubmissionDto.getLicenceId();
        LicenceDto licenceById = requestForChangeService.getLicenceById(licenceId);
        /**
         * when use save it as draft in the previous, and the licence has been updated via other licence,
         * the licence will not be valid any more, so when use do the it from the old draft,
         * the licence will be null.
         */
        if (licenceById == null) {
            log.warn(StringUtil.changeForLog("Invalid selected Licence - " + licenceId));
            bpc.request.setAttribute(RfcConst.INVALID_LIC, MessageUtil.getMessageDesc("RFC_ERR023"));
            return;
        }
        Set<String> premiseTypes = null;
        if (appGrpPremisesDtoList != null) {
            premiseTypes = appGrpPremisesDtoList.stream().map(AppGrpPremisesDto::getPremisesType).collect(Collectors.toSet());
        }
        map = NewApplicationHelper.validateLicences(licenceById, premiseTypes, null);
        if (map != null && !map.isEmpty()) {
            NewApplicationHelper.setErrorRequest(map, false, bpc.request);
            return;
        }
        requestForChangeService.setRelatedInfoBaseServiceId(appSubmissionDto);
        String baseServiceId = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getBaseServiceId();
        if(StringUtil.isEmpty(baseServiceId)){
            bpc.request.setAttribute(RfcConst.SERVICE_CONFIG_CHANGE, MessageUtil.getMessageDesc("RFC_ERR020"));
            return;
        }
        // change edit
        AppEditSelectDto appEditSelectDto = EqRequestForChangeSubmitResultChange.rfcChangeModuleEvaluationDto(appSubmissionDto,oldAppSubmissionDto);
        boolean isAutoRfc = appEditSelectDto.isAutoRfc();
        // reSet: isNeedNewLicNo and self assessment flag
        NewApplicationHelper.reSetAdditionalFields(appSubmissionDto, oldAppSubmissionDto, appEditSelectDto);
        appSubmissionDto.setChangeSelectDto(appEditSelectDto);
        log.info(StringUtil.changeForLog(appSubmissionDto.getLicenceNo() + " - App Edit Select Dto: "
                + JsonUtil.parseToJson(appEditSelectDto)));
        // the declaration only for HCI name changed
        if(!appEditSelectDto.isChangeHciName()){
            appSubmissionDto.setAppDeclarationMessageDto(null);
            appSubmissionDto.setAppDeclarationDocDtos(null);
        }
        boolean isCharity = NewApplicationHelper.isCharity(bpc.request);
        FeeDto feeDto = appSubmissionService.getGroupAmendAmount(getAmendmentFeeDto(appEditSelectDto, isCharity));
        double amount = feeDto.getTotal();
        double currentAmount = amount;
        if (licenceById.getMigrated() == 1 && IaisEGPHelper.isActiveMigrated()) {
            currentAmount = 0.0;
        }
        log.info(StringUtil.changeForLog("the current amount is -->:" + currentAmount));
        appSubmissionDto.setAmount(currentAmount);

        String appGroupNo = null;
        String autoGroupNo = null;
        String draftNo = Optional.ofNullable(appSubmissionDto.getDraftNo())
                .orElseGet(() -> appSubmissionService.getDraftNo(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE));
        log.info(StringUtil.changeForLog("the draft is -->:" + draftNo));
        if (isAutoRfc) {
            autoGroupNo = getRfcGroupNo(autoGroupNo);
            appSubmissionDto.setAppGrpNo(autoGroupNo);
        } else {
            appGroupNo = getRfcGroupNo(appGroupNo);
            appSubmissionDto.setAppGrpNo(appGroupNo);
        }
        appSubmissionDto.setDraftNo(draftNo);

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
        // set status
        appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_PENDING_PAYMENT);
        if (MiscUtil.doubleEquals(0.0, currentAmount)) {
            appSubmissionDto.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_NOT_PAYMENT);
        }
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        requestForChangeService.premisesDocToSvcDoc(appSubmissionDto);
        requestForChangeService.premisesDocToSvcDoc(oldAppSubmissionDto);
        appSubmissionDto.setGetAppInfoFromDto(true);
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        appSubmissionDto.setAuditTrailDto(auditTrailDto);
        Map<AppSubmissionDto, List<String>> errorListMap = IaisCommonUtils.genNewHashMap();
        List<AppSubmissionDto> appSubmissionDtoList = IaisCommonUtils.genNewArrayList();
        List<AppSubmissionDto> autoSaveAppsubmission = IaisCommonUtils.genNewArrayList();
        List<AppSubmissionDto> notAutoSaveAppsubmission = IaisCommonUtils.genNewArrayList();
        AppSubmissionDto autoAppSubmissionDto = null;
        AppEditSelectDto autoChangeSelectDto = null;
        if (appEditSelectDto.isLicenseeEdit() || appEditSelectDto.isPremisesEdit() || appEditSelectDto.isDocEdit() || appEditSelectDto.isServiceEdit()) {
            // add the current dto to the group
            if (isAutoRfc) {
                autoSaveAppsubmission.add(appSubmissionDto);
            } else {
                notAutoSaveAppsubmission.add(appSubmissionDto);
            }
        }
        // init auto app submission
        if (!isAutoRfc && (appEditSelectDto.isLicenseeEdit() || appEditSelectDto.isDocEdit())) {
            autoAppSubmissionDto = (AppSubmissionDto) CopyUtil.copyMutableObject(appSubmissionDto);
            autoAppSubmissionDto.setAmount(0.0);
            autoChangeSelectDto = new AppEditSelectDto();
        }

        boolean rfcSplitFlag = ConfigHelper.getBoolean("halp.rfc.split.flag", false);
        log.info(StringUtil.changeForLog("##### halp rfc split flag: " + rfcSplitFlag));
        // check the premises step is auto or not
        int isAutoPremises = -1;
        // check app submissions affected by premises+
        if (appEditSelectDto.isPremisesEdit()) {
            AppEditSelectDto changeSelectDto = new AppEditSelectDto();
            changeSelectDto.setPremisesEdit(true);
            changeSelectDto.setPremisesListEdit(true);
            changeSelectDto.setChangeHciName(appEditSelectDto.isChangeHciName());
            changeSelectDto.setChangeInLocation(appEditSelectDto.isChangeInLocation());
            changeSelectDto.setChangeAddFloorUnit(appEditSelectDto.isChangeAddFloorUnit());
            String groupNo;
            if (changeSelectDto.isAutoRfc()) {
                autoGroupNo = getRfcGroupNo(autoGroupNo);
                groupNo = autoGroupNo;
            } else {
                appGroupNo = getRfcGroupNo(appGroupNo);
                groupNo = appGroupNo;
            }
            // reSet amount
            double otherAmount = 0.0D;
            FeeDto premiseFee = appSubmissionService.getGroupAmendAmount(getAmendmentFeeDto(changeSelectDto, isCharity));
            if (premiseFee != null && premiseFee.getTotal() != null) {
                otherAmount = premiseFee.getTotal();
            }
            log.info(StringUtil.changeForLog("The premise changed amount: " + otherAmount));
            List<AppSubmissionDto> appSubmissionDtos;
            if (rfcSplitFlag) {
                HcsaServiceDto serviceDto = HcsaServiceCacheHelper.getServiceByServiceName(appSubmissionDto.getServiceName());
                boolean checkSpec = ApplicationConsts.SERVICE_TYPE_BASE.equals(serviceDto.getSvcType());
                appSubmissionDtos = requestForChangeService.getAlginAppSubmissionDtos(appSubmissionDto.getLicenceId(), checkSpec);
                if (IaisCommonUtils.isNotEmpty(appSubmissionDtos)) {
                    StreamSupport.stream(appSubmissionDtos.spliterator(), appSubmissionDtos.size() >= RfcConst.DFT_MIN_PARALLEL_SIZE)
                            .forEach(dto -> NewApplicationHelper.reSetPremeses(dto, appGrpPremisesDtoList));
                    boolean isValid = checkAffectedAppSubmissions(appSubmissionDtos, otherAmount, draftNo, groupNo, changeSelectDto,
                            NewApplicationConstant.SECTION_PREMISES, bpc.request);
                    if (!isValid) {
                        return;
                    }
                }
            } else {
                appSubmissionDtos = IaisCommonUtils.genNewArrayList();
                boolean isValid = checkAffectedAppSubmissions(appGrpPremisesDtoList, otherAmount, draftNo, groupNo, changeSelectDto,
                        appSubmissionDtos, bpc.request);
                if (!isValid) {
                    return;
                }
            }
            // add the premises affected list to the group
            if (changeSelectDto.isAutoRfc()) {
                NewApplicationHelper.addToAuto(appSubmissionDtos, autoSaveAppsubmission);
                // re-set change edit select dto
                isAutoPremises = 1;
            } else {
                NewApplicationHelper.addToNonAuto(appSubmissionDtos, notAutoSaveAppsubmission);
                // split out the auto parts
                isAutoPremises = 0;
            }
            // for spliting
            if (changeSelectDto.isAutoRfc() && !isAutoRfc) {
                if (autoAppSubmissionDto == null) {
                    autoAppSubmissionDto = (AppSubmissionDto) CopyUtil.copyMutableObject(appSubmissionDto);
                    autoAppSubmissionDto.setAmount(0.0);
                    autoChangeSelectDto = new AppEditSelectDto();
                }
                autoChangeSelectDto.setPremisesEdit(true);
                appEditSelectDto.setPremisesEdit(false);
                appEditSelectDto.setPremisesListEdit(false);
            }
        }
        log.info(StringUtil.changeForLog("isAutoPremises: " + isAutoPremises));

        boolean addClaimed = false;
        // check app submissions affected by sub licensee
        if (appEditSelectDto.isLicenseeEdit()) {
            if (!rfcSplitFlag) {
                autoGroupNo = getRfcGroupNo(autoGroupNo);
                String groupNo = autoGroupNo;
                SubLicenseeDto oldSublicenseeDto = oldAppSubmissionDto.getSubLicenseeDto();
                List<AppSubmissionDto> licenseeAffectedList = licenceClient.getAppSubmissionDtosBySubLicensee(
                        oldSublicenseeDto).getEntity();
                if (licenseeAffectedList == null) {
                    licenseeAffectedList = IaisCommonUtils.genNewArrayList(0);
                }
                // remove the current app submission
                for (Iterator<AppSubmissionDto> it = licenseeAffectedList.iterator(); it.hasNext(); ) {
                    AppSubmissionDto dto = it.next();
                    if (licenceId.equals(dto.getLicenceId())) {
                        it.remove();
                        break;
                    }
                }
                AppEditSelectDto changeSelectDto = new AppEditSelectDto();
                changeSelectDto.setLicenseeEdit(true);
                StreamSupport.stream(licenseeAffectedList.spliterator(),
                        licenseeAffectedList.size() >= RfcConst.DFT_MIN_PARALLEL_SIZE)
                        .forEach(dto -> dto.setSubLicenseeDto(
                                MiscUtil.transferEntityDto(appSubmissionDto.getSubLicenseeDto(), SubLicenseeDto.class)));
                boolean isValid = checkAffectedAppSubmissions(licenseeAffectedList, 0.0D, draftNo, groupNo, changeSelectDto,
                        NewApplicationConstant.SECTION_LICENSEE, bpc.request);
                if (!isValid) {
                    return;
                }
                NewApplicationHelper.addToAuto(licenseeAffectedList, autoSaveAppsubmission);
            }

            // re-set change edit select dto
            if (autoAppSubmissionDto != null) {
                autoChangeSelectDto.setLicenseeEdit(true);
                appEditSelectDto.setLicenseeEdit(false);
            }
        } else if (rfcSplitFlag) {
            SubLicenseeDto subLicenseeDto = appSubmissionDto.getSubLicenseeDto();
            addClaimed = StringUtil.isNotEmpty(subLicenseeDto.getClaimUenNo())
                    || StringUtil.isNotEmpty(subLicenseeDto.getClaimCompanyName());
        }
        log.info(StringUtil.changeForLog("##### Only Add claimed: " + addClaimed));
        // Primary Doc
        // re-set change edit select dto
        if (appEditSelectDto.isDocEdit() && autoAppSubmissionDto != null) {
            appEditSelectDto.setDocEdit(false);
            autoChangeSelectDto.setDocEdit(true);
        }
        // check app submissions affected by personnel (service info)
        if (appEditSelectDto.isServiceEdit()) {
            String licenseeId = NewApplicationHelper.getLicenseeId(bpc.request);
            List<AppSubmissionDto> personAppSubmissionList = serviceInfoChangeEffectPersonForRFC.personContact(licenseeId,
                    appSubmissionDto, oldAppSubmissionDto, rfcSplitFlag ? 0 : 1);
            if (personAppSubmissionList != null && !personAppSubmissionList.isEmpty()) {
                autoGroupNo = getRfcGroupNo(autoGroupNo);
                boolean isValid = checkAffectedAppSubmissions(personAppSubmissionList, 0.0D, draftNo, autoGroupNo,
                        null, NewApplicationConstant.SECTION_SVCINFO, bpc.request);
                if (!isValid) {
                    return;
                }
                NewApplicationHelper.addToAuto(personAppSubmissionList, autoSaveAppsubmission);
            }

            // re-set current auto dto
            List<String> changeList = appSubmissionDto.getChangeSelectDto().getPersonnelEditList();
            List<String> stepList = appSubmissionDto.getAppEditSelectDto().getPersonnelEditList();
            log.info(StringUtil.changeForLog("StepList: " + stepList + " - ChangeList: " + changeList));
            if (!isAutoRfc && autoAppSubmissionDto == null && !IaisCommonUtils.isEmpty(changeList)) {
                autoAppSubmissionDto = (AppSubmissionDto) CopyUtil.copyMutableObject(appSubmissionDto);
                autoAppSubmissionDto.setAmount(0.0);
                autoChangeSelectDto = new AppEditSelectDto();
            }
            if (autoAppSubmissionDto != null) {
                autoChangeSelectDto.setServiceEdit(true);
                autoAppSubmissionDto.setAppSvcRelatedInfoDtoList(
                        serviceInfoChangeEffectPersonForRFC.generateDtosForAutoFields(autoAppSubmissionDto, oldAppSubmissionDto,
                                changeList, stepList));
                // re-set change edit select dto
                if (!appEditSelectDto.isChangeBusinessName() && !appEditSelectDto.isChangeVehicle()
                        && !appEditSelectDto.isChangePersonnel() && !appEditSelectDto.isChangeSectionLeader()) {
                    appEditSelectDto.setServiceEdit(false);
                }
            }
        }

        // synchronize data
        if (!autoSaveAppsubmission.isEmpty() && !notAutoSaveAppsubmission.isEmpty()) {
            StreamSupport.stream(notAutoSaveAppsubmission.spliterator(),
                    notAutoSaveAppsubmission.size() >= RfcConst.DFT_MIN_PARALLEL_SIZE)
                    .forEach(targetDto -> {
                        Optional<AppSubmissionDto> optional = autoSaveAppsubmission.stream()
                                .filter(source -> Objects.equals(targetDto.getLicenceId(), source.getLicenceId()))
                                .findAny();
                        if (optional.isPresent()) {
                            NewApplicationHelper.reSetNonAutoDataByAppEditSelectDto(targetDto, optional.get());
                        }
                    });
        }

        // re-set autoAppSubmissionDto
        if (autoAppSubmissionDto != null) {
            if (0 == isAutoPremises) {
                autoAppSubmissionDto.setAppGrpPremisesDtoList(oldAppGrpPremisesDtoList);
            }
            autoGroupNo = getRfcGroupNo(autoGroupNo);
            NewApplicationHelper.reSetAdditionalFields(autoAppSubmissionDto, autoChangeSelectDto, autoGroupNo);
            autoAppSubmissionDto.setChangeSelectDto(autoChangeSelectDto);
            autoSaveAppsubmission.add(0, autoAppSubmissionDto);
        }
        String autoGroupStatus = ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED;
        if (autoSaveAppsubmission.isEmpty() && notAutoSaveAppsubmission.isEmpty() && addClaimed) {
            autoGroupStatus = ApplicationConsts.APPLICATION_GROUP_STATUS_ADDITIONAL_CLAIM;
            appSubmissionDto.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED);
            NewApplicationHelper.reSetAdditionalFields(appSubmissionDto, false, true, appSubmissionDto.getAppGrpNo());
            autoSaveAppsubmission.add(appSubmissionDto);
        }
        // check whether the data has been changed or not
        if (autoSaveAppsubmission.isEmpty() && notAutoSaveAppsubmission.isEmpty()) {
            bpc.request.setAttribute("RFC_ERROR_NO_CHANGE",MessageUtil.getMessageDesc("RFC_ERR010"));
            return;
        }
        log.info(StringUtil.changeForLog("the appGroupNo --> Not-auto: " + appGroupNo + " - Auto:" + autoGroupNo));
        AppDeclarationMessageDto appDeclarationMessageDto = !appEditSelectDto.isChangeHciName() ? null :
                appSubmissionDto.getAppDeclarationMessageDto();
        List<AppDeclarationDocDto> appDeclarationDocDtos = !appEditSelectDto.isChangeHciName() ? null :
                appSubmissionDto.getAppDeclarationDocDtos();
        Date effectiveDate = appDeclarationMessageDto != null && appDeclarationMessageDto.getEffectiveDt() != null ?
                appDeclarationMessageDto.getEffectiveDt() : appSubmissionDto.getEffectiveDate();
        String effectiveDateStr = Formatter.formatDate(effectiveDate);
        log.info(StringUtil.changeForLog("effectiveDate: " + effectiveDateStr));
        log.info(StringUtil.changeForLog("------ Save Data Start ------"));
        String auto = generateIdClient.getSeqId().getEntity();
        String notAuto = generateIdClient.getSeqId().getEntity();
        AppSubmissionListDto autoAppSubmissionListDto = new AppSubmissionListDto();
        AppSubmissionListDto notAutoAppSubmissionListDto = new AppSubmissionListDto();
        Long autoTime = System.currentTimeMillis();
        Long notAutoTime = System.currentTimeMillis();
        autoAppSubmissionListDto.setEventRefNo(autoTime.toString());
        notAutoAppSubmissionListDto.setEventRefNo(notAutoTime.toString());
        autoAppSubmissionListDto.setAuditTrailDto(auditTrailDto);
        notAutoAppSubmissionListDto.setAuditTrailDto(auditTrailDto);
        List<AppSubmissionDto> ackPageAppSubmissionDto=new ArrayList<>(2);
        List<String> svcNameSet = new ArrayList<>();
        String notAutoGroupId = null;
        String autoGroupId = null;
        if (!notAutoSaveAppsubmission.isEmpty()) {
            // save submission (notAUto data)
            String appGrpStatus = autoSaveAppsubmission.isEmpty() ? ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED : ApplicationConsts.APPLICATION_GROUP_STATUS_PENDING_AUTO;
            notAutoSaveAppsubmission.parallelStream().forEach(dto -> {
                dto.setEffectiveDateStr(effectiveDateStr);
                dto.setEffectiveDate(effectiveDate);
                dto.setAppDeclarationMessageDto(appDeclarationMessageDto);
                dto.setAppDeclarationDocDtos(appDeclarationDocDtos);
                dto.setAppGrpStatus(appGrpStatus);
            });
            // save application, group, declaration
            List<AppSubmissionDto> appSubmissionDtos1 = requestForChangeService.saveAppsForRequestForGoupAndAppChangeByList(notAutoSaveAppsubmission);
            notAutoGroupId = appSubmissionDtos1.get(0).getAppGrpId();
            // save other data via event bus
            notAutoAppSubmissionListDto.setAppSubmissionDtos(appSubmissionDtos1);
            eventBusHelper.submitAsyncRequest(notAutoAppSubmissionListDto, notAuto, EventBusConsts.SERVICE_NAME_APPSUBMIT,
                    EventBusConsts.OPERATION_REQUEST_INFORMATION_SUBMIT, notAutoTime.toString(),
                    "RFC Non-Auto Approve Submit", notAutoGroupId);
            appSubmissionDtos1.get(0).getAppSvcRelatedInfoDtoList().get(0).setGroupNo(appSubmissionDtos1.get(0).getAppGrpNo());
            Double ackPageAmount = 0.0;
            for (AppSubmissionDto appSubmissionDto1 : notAutoSaveAppsubmission) {
                Double amount1 = appSubmissionDto1.getAmount();
                ackPageAmount = ackPageAmount + amount1;
                String s = Formatter.formatterMoney(amount1);
                appSubmissionDto1.setAmountStr(s);
                svcNameSet.add(appSubmissionDto1.getServiceName());
            }
            AppSubmissionDto o1 = (AppSubmissionDto) CopyUtil.copyMutableObject(appSubmissionDtos1.get(0));
            o1.setAmount(ackPageAmount);
            String s = Formatter.formatterMoney(ackPageAmount);
            o1.setAmountStr(s);
            ackPageAppSubmissionDto.add(o1);
            appSubmissionDtoList.addAll(appSubmissionDtos1);
            appSubmissionDto.setAppGrpId(notAutoGroupId);
        }
        final String newAutoGrpStatus = autoGroupStatus;
        if (!autoSaveAppsubmission.isEmpty()) {
            // save submission (auto data)
            autoSaveAppsubmission.parallelStream().forEach(dto -> {
                dto.setEffectiveDateStr(effectiveDateStr);
                dto.setEffectiveDate(effectiveDate);
                dto.setAppDeclarationMessageDto(appDeclarationMessageDto);
                dto.setAppDeclarationDocDtos(appDeclarationDocDtos);
                dto.setAppGrpStatus(newAutoGrpStatus);
            });
            // save application, group, declaration
            List<AppSubmissionDto> appSubmissionDtos1 = requestForChangeService.saveAppsForRequestForGoupAndAppChangeByList(autoSaveAppsubmission);
            autoGroupId = appSubmissionDtos1.get(0).getAppGrpId();
            // save other data via event bus
            autoAppSubmissionListDto.setAppSubmissionDtos(appSubmissionDtos1);
            eventBusHelper.submitAsyncRequest(autoAppSubmissionListDto, auto, EventBusConsts.SERVICE_NAME_APPSUBMIT,
                    EventBusConsts.OPERATION_REQUEST_INFORMATION_SUBMIT, autoTime.toString(),
                    "RFC Auto Approve Submit", autoGroupId);
            appSubmissionDtos1.get(0).getAppSvcRelatedInfoDtoList().get(0).setGroupNo(appSubmissionDtos1.get(0).getAppGrpNo());
            double t = 0.0;
            for (AppSubmissionDto appSubmissionDto1 : autoSaveAppsubmission) {
                t=t+appSubmissionDto1.getAmount();
                String s = Formatter.formatterMoney(appSubmissionDto1.getAmount());
                appSubmissionDto1.setAmountStr(s);
                svcNameSet.add(appSubmissionDto1.getServiceName());
            }
            AppSubmissionDto o1 = (AppSubmissionDto)CopyUtil.copyMutableObject(appSubmissionDtos1.get(0));
            o1.setAmount(t);
            String s = Formatter.formatterMoney(t);
            o1.setAmountStr(s);
            ackPageAppSubmissionDto.add(o1);
            appSubmissionDtoList.addAll(appSubmissionDtos1);
            if (StringUtil.isEmpty(notAutoGroupId)) {
                appSubmissionDto.setAppGrpId(autoGroupId);
            }
        }
        // app group misc
        appSubmissionService.saveAutoRFCLinkAppGroupMisc(notAutoGroupId,autoGroupId);
        log.info(StringUtil.changeForLog("------ Save Data End ------"));
        bpc.request.getSession().setAttribute(APP_SUBMISSIONS, appSubmissionDtoList);
        bpc.request.getSession().setAttribute(ACK_APP_SUBMISSIONS, ackPageAppSubmissionDto);
        bpc.request.getSession().setAttribute(ALL_SVC_NAMES, svcNameSet);

        appSubmissionService.doSaveDraft(appSubmissionDto);
        List<String> licenceIds = appSubmissionDtoList.parallelStream()
                .map(AppSubmissionDto::getLicenceId)
                .collect(Collectors.toList());
        appSubmissionService.updateDrafts(NewApplicationHelper.getLicenseeId(bpc.request), licenceIds, draftNo);
        ParamUtil.setSessionAttr(bpc.request,APPSUBMISSIONDTO,appSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "payment");
        log.info(StringUtil.changeForLog("the do doRequestForChangeSubmit start ...."));
    }

    private String getRfcGroupNo(String groupNo) {
        if (groupNo == null) {
            groupNo = appSubmissionService.getGroupNo(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        }
        return groupNo;
    }

    private boolean checkAffectedAppSubmissions(List<AppGrpPremisesDto> appGrpPremisesDtoList, double amount, String draftNo,
            String appGroupNo, AppEditSelectDto appEditSelectDto, List<AppSubmissionDto> appSubmissionDtos,
            HttpServletRequest request) throws Exception {
        if (appGrpPremisesDtoList == null) {
            return true;
        }
        int size = appGrpPremisesDtoList.size();
        for (int i = 0; i < size; i++) {
            AppGrpPremisesDto premisesDto = appGrpPremisesDtoList.get(i);
            // all latest licence under the current hci code and licensee
            List<LicenceDto> licenceDtos = (List<LicenceDto>) request.getSession().getAttribute("selectLicence" + i);
            if (licenceDtos == null) {
                licenceDtos = premisesDto.getLicenceDtos();
            }
            if (licenceDtos == null) {
                continue;
            }
            Map<String, String> errorMap = requestForChangeService.checkAffectedAppSubmissions(licenceDtos, premisesDto,
                    amount, draftNo, appGroupNo, appEditSelectDto, appSubmissionDtos);
            if (!errorMap.isEmpty()) {
                NewApplicationHelper.setErrorRequest(errorMap, false, request);
                return false;
            }
        }
        return true;
    }

    private boolean checkAffectedAppSubmissions(List<AppSubmissionDto> appSubmissionDtos, double amount, String draftNo,
            String appGroupNo, AppEditSelectDto appEditSelectDto, String type, HttpServletRequest request) {
        if (appSubmissionDtos == null || appSubmissionDtos.isEmpty()) {
            return true;
        }
        log.info(StringUtil.changeForLog("##### Affected Size: " + appSubmissionDtos.size()));
        boolean parallel = true;//appSubmissionDtos.size() >= RfcConst.DFT_MIN_PARALLEL_SIZE;
        Map<AppSubmissionDto, List<String>> errorListMap = StreamSupport.stream(appSubmissionDtos.spliterator(), parallel)
                .collect(Collectors.toMap(Function.identity(), dto -> appSubmissionService.doPreviewSubmitValidate(null, dto, false)));
        String errorMsg = NewApplicationHelper.getErrorMsg(errorListMap);
        if (StringUtil.isNotEmpty(errorMsg)) {
            ParamUtil.setRequestAttr(request, RfcConst.SHOW_OTHER_ERROR, errorMsg);
            return false;
        }
        Map<String, String> map = NewApplicationHelper.validateLicences(appSubmissionDtos, type);
        if (map != null && !map.isEmpty()) {
            log.info(StringUtil.changeForLog("##### checkAffectedAppSubmissions: " + map));
            NewApplicationHelper.setErrorRequest(map, false, request);
            return false;
        }
        StreamSupport.stream(appSubmissionDtos.spliterator(), parallel)
                .forEach(dto -> requestForChangeService.checkAffectedAppSubmissions(dto, null, amount, draftNo, appGroupNo,
                        appEditSelectDto, null));
        return true;
    }

    public void reSubmit(BaseProcessClass bpc) throws Exception {
        log.info(StringUtil.changeForLog("do reSubmit start ..."));
        String draftNo = ParamUtil.getMaskedString(bpc.request,"draftNo");
        StringBuilder url = new StringBuilder();
        url.append("https://").append(bpc.request.getServerName())
                .append("/hcsa-licence-web/eservice/INTERNET/MohNewApplication?DraftNumber=")
                .append(MaskUtil.maskValue(DRAFT_NUMBER, draftNo));
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);

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
            if(!MiscUtil.doubleEquals(0.0, totalAmount) && StringUtil.isEmpty(noNeedPayment)){
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
            if (!StringUtil.isEmpty(noNeedPayment)) {
                ParamUtil.setSessionAttr(bpc.request, "txnRefNo", "");
                try {
                    if (appSubmissionDto.getAppType().equals(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE)) {
                        List<AppSubmissionDto> appSubmissionDtos = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request,
                                APP_SUBMISSIONS);
                        if (appSubmissionDtos == null || appSubmissionDtos.size() == 0) {
                            appSubmissionDtos = IaisCommonUtils.genNewArrayList();
                            appSubmissionDtos.add(appSubmissionDto);
                        }
                        requestForChangeService.sendRfcSubmittedEmail(appSubmissionDtos, null);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        } else {
            appSubmissionDto.setId(null);
            appSubmissionDto.setAppGrpId(null);
            appSubmissionDto.setAppGrpNo(null);
            if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())
                    || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
                requestForChangeService.svcDocToPresmise(appSubmissionDto);
                AppSubmissionDto oldAppSubmissionDto = NewApplicationHelper.getOldAppSubmissionDto(bpc.request);
                if (oldAppSubmissionDto != null) {
                    requestForChangeService.svcDocToPresmise(oldAppSubmissionDto);
                    NewApplicationHelper.setOldAppSubmissionDto(oldAppSubmissionDto, bpc.request);
                }
            }
            NewApplicationHelper.clearPremisesMap(bpc.request);
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
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
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

    /**
     * StartStep: doSubmit
     *
     * @param bpc
     * @throws
     */
    public void doSubmit(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("the do doSubmit start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        // validate all data
        Map<String, String> map = appSubmissionService.doPreviewAndSumbit(bpc);
        if (!map.isEmpty()) {
            //set audit
            ParamUtil.setRequestAttr(bpc.request, "Msg", map);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "preview");
            HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute(NewApplicationConstant.CO_MAP);
            coMap.put("previewli", "");
            bpc.request.getSession().setAttribute(NewApplicationConstant.CO_MAP, coMap);
            return;
        } else {
            HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute(NewApplicationConstant.CO_MAP);
            coMap.put("previewli", "previewli");
            bpc.request.getSession().setAttribute(NewApplicationConstant.CO_MAP, coMap);
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
        appEditSelectDto.setLicenseeEdit(NewApplicationHelper.canLicenseeEdit(appSubmissionDto, false));
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
        HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute(NewApplicationConstant.CO_MAP);
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
        NewApplicationHelper.reSetAdditionalFields(appSubmissionDto, true, false, appGroupNo);
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
        } else if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
            // 72106
            String action = ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
            if (!"licensee".equals(action) && !"premises".equals(action) && !"jump".equals(action)) {
                AppGrpPremisesDto premisse = appSubmissionDto.getAppGrpPremisesDtoList() != null
                        && appSubmissionDto.getAppGrpPremisesDtoList().size() > 0 ?
                        appSubmissionDto.getAppGrpPremisesDtoList().get(0) : null;
                if (premisse == null || !premisse.isFilled()) {
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "premises");
                }
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
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String appGrpId = appSubmissionDto.getAppGrpId();
        log.info(StringUtil.changeForLog("The AppGrpNo: " + appSubmissionDto.getAppGrpNo() + "; payment method: "
                + appSubmissionDto.getPaymentMethod() + "; the amount: " + appSubmissionDto.getAmount()
                + " - " + appSubmissionDto.getAppGrpId()));
        List<String> ids = new ArrayList<>();
        //68099
        List<AppSubmissionDto> ackPageAppSubmissionDto = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request, ACK_APP_SUBMISSIONS);
        if(!IaisCommonUtils.isEmpty(ackPageAppSubmissionDto)){
            for(AppSubmissionDto appSubmissionDto1:ackPageAppSubmissionDto){
                if(!MiscUtil.doubleEquals(appSubmissionDto1.getAmount(), 0.0)){
                    appSubmissionDto1.setPaymentMethod(payMethod);
                } else {
                    log.info(StringUtil.changeForLog("--- " + appSubmissionDto1.getAppGrpNo() + " : " + appSubmissionDto1.getAppGrpId() + " ---"));
                    ids.add(appSubmissionDto1.getAppGrpId());
                    ApplicationGroupDto appGrp = new ApplicationGroupDto();
                    appGrp.setId(appSubmissionDto1.getAppGrpId());
                    appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
                    appGrp.setPayMethod(payMethod);
                    serviceConfigService.updatePaymentStatus(appGrp);
                }
            }
            ParamUtil.setSessionAttr(bpc.request,ACK_APP_SUBMISSIONS, (Serializable) ackPageAppSubmissionDto);
        }
        Double totalAmount = appSubmissionDto.getAmount();
        if (MiscUtil.doubleEquals(totalAmount, 0.0)) {
            if (StringUtil.isNotEmpty(appGrpId) && !ids.contains(appGrpId)) {
                ApplicationGroupDto appGrp = new ApplicationGroupDto();
                appGrp.setId(appGrpId);
                appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
                appGrp.setPayMethod(payMethod);
                serviceConfigService.updatePaymentStatus(appGrp);
                if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
                    LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
                    appSubmissionService.sendEmailAndSMSAndMessage(appSubmissionDto, loginContext.getUserName());
                }
            }

            StringBuilder url = new StringBuilder();
            url.append("https://")
                    .append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTERNET/MohNewApplication/PrepareAckPage");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
            return;
        }
        appSubmissionDto.setPaymentMethod(payMethod);
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        if (ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT.equals(payMethod)
                || ApplicationConsts.PAYMENT_METHOD_NAME_NETS.equals(payMethod)
                || ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW.equals(payMethod)) {
            //send email
            try {
                //inspectionDateSendNewApplicationPaymentOnlineEmail(appSubmissionDto, bpc);
            } catch (Exception e) {
                log.error(StringUtil.changeForLog("send email error ...." + e.getMessage()), e);
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
                IaisEGPHelper.redirectUrl(bpc.response, url);
                //ParamUtil.setRequestAttr(bpc.request, "jumpHtml", html);
            } catch (Exception e) {
                log.info(StringUtil.changeForLog(e.getMessage()), e);
            }
        } else if (ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod)) {
            //send email
            try {
                //sendNewApplicationPaymentGIROEmail(appSubmissionDto, bpc);
                if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
                    LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,
                            AppConsts.SESSION_ATTR_LOGIN_USER);
                    appSubmissionService.sendEmailAndSMSAndMessage(appSubmissionDto, loginContext.getUserName());
                }
                if(ackPageAppSubmissionDto==null){
                    ackPageAppSubmissionDto=IaisCommonUtils.genNewArrayList();
                    ackPageAppSubmissionDto.add(appSubmissionDto);
                }
                if ( ackPageAppSubmissionDto.get(0).getAppType().equals(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE)) {
                    requestForChangeService.sendRfcSubmittedEmail(ackPageAppSubmissionDto, ackPageAppSubmissionDto.get(0).getPaymentMethod());
                }
            } catch (Exception e) {
                log.error(StringUtil.changeForLog("send email error ...." + e.getMessage()), e);
            }
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
            // change
            StringBuilder url = new StringBuilder();
            url.append("https://")
                    .append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTERNET/MohNewApplication/PrepareAckPage");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
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
        List<AppSubmissionDto> ackPageAppSubmissionDto =(List<AppSubmissionDto>)ParamUtil.getSessionAttr(bpc.request, ACK_APP_SUBMISSIONS);
        String draftNo = "";
        if(appSubmissionDto != null) {
            draftNo = appSubmissionDto.getDraftNo();
            if (ackPageAppSubmissionDto == null) {
                List<AppSubmissionDto> ackPageAppSubmission = new ArrayList<>(1);
                ackPageAppSubmission.add(appSubmissionDto);
                bpc.request.getSession().setAttribute(ACK_APP_SUBMISSIONS, ackPageAppSubmission);
            }
        }
        if (draftNo != null) {
            applicationFeClient.deleteDraftByNo(draftNo);
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
            String txnDt = Formatter.formatDate(new Date());
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
            AppSubmissionDto oldAppSubmissionDto = NewApplicationHelper.getOldAppSubmissionDto(bpc.request);
            AppSubmissionDto oldDto = (AppSubmissionDto) CopyUtil.copyMutableObject(oldAppSubmissionDto);
            AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
            AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto();
            if (appEditSelectDto.isLicenseeEdit()) {
                appSubmissionDto.setSubLicenseeDto(oldDto.getSubLicenseeDto());
            }
            if (appEditSelectDto.isPremisesEdit()) {
                appSubmissionDto.setAppGrpPremisesDtoList(oldDto.getAppGrpPremisesDtoList());
            }
            if (appEditSelectDto.isDocEdit()) {
                appSubmissionDto.setAppGrpPrimaryDocDtos(oldDto.getAppGrpPrimaryDocDtos());
            }
            if (appEditSelectDto.isServiceEdit()) {
                appSubmissionDto.setAppSvcRelatedInfoDtoList(oldDto.getAppSvcRelatedInfoDtoList());
            }
            ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);
        }

        log.info(StringUtil.changeForLog("the do prepareJump end ...."));
    }

    private Map<String, String> doComChange(AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto) {
        Map<String, String> result = IaisCommonUtils.genNewHashMap();
        AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto();

        if (appEditSelectDto != null) {
            if (!appEditSelectDto.isPremisesEdit()) {
                List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
                for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                    appGrpPremisesDto.setLicenceDtos(null);
                }
                boolean eqGrpPremises = EqRequestForChangeSubmitResultChange.isChangeGrpPremises(appSubmissionDto.getAppGrpPremisesDtoList(), oldAppSubmissionDto.getAppGrpPremisesDtoList());
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

    private AmendmentFeeDto getAmendmentFeeDto(AppEditSelectDto appEditSelectDto, boolean isCharity) {
        return getAmendmentFeeDto(appEditSelectDto.isChangeHciName(),
                appEditSelectDto.isChangeInLocation() || appEditSelectDto.isChangeAddFloorUnit(),
                appEditSelectDto.isChangeVehicle(), isCharity, appEditSelectDto.isChangeBusinessName());
    }

    private AmendmentFeeDto getAmendmentFeeDto(boolean changeHciName, boolean changeLocation, boolean changeVehicles,
            boolean isCharity, boolean changeBusiness) {
        AmendmentFeeDto amendmentFeeDto = new AmendmentFeeDto();
        amendmentFeeDto.setChangeInLicensee(Boolean.FALSE);
        amendmentFeeDto.setChangeInHCIName(changeHciName);
        amendmentFeeDto.setChangeInLocation(changeLocation);
        if (changeVehicles) {
            amendmentFeeDto.setChangeInHCIName(Boolean.TRUE);
        }
        amendmentFeeDto.setIsCharity(isCharity);
        amendmentFeeDto.setChangeBusinessName(changeBusiness);
        return amendmentFeeDto;
    }

    private AppSvcRelatedInfoDto getAppSvcRelatedInfoDto(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos) {
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
            return appSvcRelatedInfoDtos.get(0);
        }
        return new AppSvcRelatedInfoDto();
    }

    /**
     * @description: get data from page
     * @author: zixian
     * @date: 11/6/2019 5:05 PM
     * @param: request
     * @return: AppGrpPremisesDto
     */
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

        boolean isRfi = NewApplicationHelper.checkIsRfi(request);
        List<AppGrpPremisesDto> appGrpPremisesDtoList = IaisCommonUtils.genNewArrayList();
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
        String[] siteEmail = ParamUtil.getStrings(request, "onSiteEmail");
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
        String[] conEmail = ParamUtil.getStrings(request, "conveyanceEmail");
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
        String[] offSiteEmail = ParamUtil.getStrings(request, "offSiteEmail");
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
            if (StringUtil.isEmpty(premIndexNo)) {
                log.info(StringUtil.changeForLog("New premise index"));
                premIndexNo = UUID.randomUUID().toString();
            }
            String appType = appSubmissionDto.getAppType();
            boolean newApp = !isRfi && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType);
            if (newApp) {
                if (!StringUtil.isEmpty(premisesSel) && !premisesSel.equals("-1")
                        && !premisesSel.equals(ApplicationConsts.NEW_PREMISES) && AppConsts.YES.equals(chooseExistData[i])) {
                    AppGrpPremisesDto licPremise = NewApplicationHelper.getPremisesFromMap(premisesSel, request);
                    if (licPremise != null) {
                        appGrpPremisesDto = (AppGrpPremisesDto) CopyUtil.copyMutableObject(licPremise);
                    } else {
                        log.info(StringUtil.changeForLog("can not found this existing premises data ...."));
                    }
                    if (appGrpPremisesDto != null) {
                        //get value for jsp page
                        appGrpPremisesDto.setExistingData(chooseExistData[i]);
                        NewApplicationHelper.setPremise(appGrpPremisesDto, premIndexNo, appSubmissionDto);
                        appGrpPremisesDtoList.add(appGrpPremisesDto);
                    }
                    continue;
                }
            } else if (AppConsts.YES.equals(chooseExistData[i])) {
                appGrpPremisesDto = NewApplicationHelper.getPremisesFromMap(premisesSel, request);
                if (appGrpPremisesDto != null) {
                    if (AppConsts.TRUE.equals(rfiCanEdit[i])) {
                        appGrpPremisesDto.setRfiCanEdit(true);
                    } else {
                        appGrpPremisesDto.setRfiCanEdit(false);
                    }
                    if (!AppConsts.YES.equals(isParyEdit[i])) {
                        appGrpPremisesDto.setExistingData(chooseExistData[i]);
                        NewApplicationHelper.setPremise(appGrpPremisesDto, premIndexNo, appSubmissionDto);
                        appGrpPremisesDtoList.add(appGrpPremisesDto);
                        continue;
                    } else {
                        log.info(StringUtil.changeForLog("Get data from page for " + StringUtil.clarify(premisesSel)));
                    }
                } else {
                    log.warn(StringUtil.changeForLog("##### warn Data: " + premIndexNo));
                    appGrpPremisesDto = new AppGrpPremisesDto();
                }
            }
            appGrpPremisesDto.setExistingData(chooseExistData[i]);
            NewApplicationHelper.setPremise(appGrpPremisesDto, premIndexNo, appSubmissionDto);
            // set premise type
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
                appGrpPremisesDto.setEasMtsPubEmail(siteEmail[i]);
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
                        weeklyDto.setStartFromHH(null);
                        weeklyDto.setStartFromMM(null);
                        weeklyDto.setEndToHH(null);
                        weeklyDto.setEndToMM(null);
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
                        phDto.setStartFromHH(null);
                        phDto.setStartFromMM(null);
                        phDto.setEndToHH(null);
                        phDto.setEndToMM(null);
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
                addFloorNoAndUnitNo(premValue[i], "onSiteFloorNo", "onSiteUnitNo", opLength,
                        appPremisesOperationalUnitDtos, request);
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
                appGrpPremisesDto.setConveyanceEmail(conEmail[i]);
                appGrpPremisesDto.setEasMtsPubEmail(conEmail[i]);
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
                        weeklyDto.setStartFromHH(null);
                        weeklyDto.setStartFromMM(null);
                        weeklyDto.setEndToHH(null);
                        weeklyDto.setEndToMM(null);
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
                        phDto.setStartFromHH(null);
                        phDto.setStartFromMM(null);
                        phDto.setEndToHH(null);
                        phDto.setEndToMM(null);
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
                addFloorNoAndUnitNo(premValue[i], "conveyanceFloorNo", "conveyanceUnitNo", opLength,
                        appPremisesOperationalUnitDtos, request);
            } else if (ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesType[i])) {
                appGrpPremisesDto.setOffSiteHciName(offSiteHciName[i]);
                appGrpPremisesDto.setPremisesSelect(offSitePremisesSelect[i]);
                appGrpPremisesDto.setOffSitePostalCode(offSitePostalCode[i]);
                appGrpPremisesDto.setOffSiteBlockNo(offSiteBlkNo[i]);
                appGrpPremisesDto.setOffSiteStreetName(offSiteStreetName[i]);
                appGrpPremisesDto.setOffSiteFloorNo(offSiteFloorNo[i]);
                appGrpPremisesDto.setOffSiteUnitNo(offSiteUnitNo[i]);
                appGrpPremisesDto.setOffSiteBuildingName(offSiteBuildingName[i]);
                appGrpPremisesDto.setOffSiteEmail(offSiteEmail[i]);
                appGrpPremisesDto.setEasMtsPubEmail(offSiteEmail[i]);
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
                        weeklyDto.setStartFromHH(null);
                        weeklyDto.setStartFromMM(null);
                        weeklyDto.setEndToHH(null);
                        weeklyDto.setEndToMM(null);
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
                        phDto.setStartFromHH(null);
                        phDto.setStartFromMM(null);
                        phDto.setEndToHH(null);
                        phDto.setEndToMM(null);
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
                addFloorNoAndUnitNo(premValue[i], "offSiteFloorNo", "offSiteUnitNo", opLength,
                        appPremisesOperationalUnitDtos, request);
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
                addFloorNoAndUnitNo(premValue[i], "easMtsFloorNo", "easMtsUnitNo", opLength,
                        appPremisesOperationalUnitDtos, request);
            }
            //appGrpPremisesDto.setAppPremPhOpenPeriodList(appPremPhOpenPeriods);
            appGrpPremisesDto.setAppPremisesOperationalUnitDtos(appPremisesOperationalUnitDtos);
            appGrpPremisesDto.setWeeklyDtoList(weeklyDtoList);
            appGrpPremisesDto.setPhDtoList(phDtoList);
            appGrpPremisesDto.setEventDtoList(eventList);
            appGrpPremisesDtoList.add(appGrpPremisesDto);
        }
        /*if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType()) ||
                ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
            //set premises edit status
            NewApplicationHelper.setPremEditStatus(appGrpPremisesDtoList, getAppGrpPremisesDtos(appSubmissionDto.getOldAppSubmissionDto()));
        }*/
        return appGrpPremisesDtoList;
    }

    private static void addFloorNoAndUnitNo(String prefix, String floorNoName, String unitNoNmae, int opLength,
            List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos, HttpServletRequest request) {
        log.info(StringUtil.changeForLog("The length of additional floor and unit: " + opLength));
        if (opLength <= 0) {
            return;
        }
        for (int j = 0; j < opLength; j++) {
            String opFloorNo = ParamUtil.getString(request, prefix + floorNoName + j);
            String opUnitNo = ParamUtil.getString(request, prefix + unitNoNmae + j);
            if (StringUtil.isEmpty(opFloorNo) && StringUtil.isEmpty(opUnitNo)) {
                continue;
            }
            AppPremisesOperationalUnitDto operationalUnitDto = new AppPremisesOperationalUnitDto();
            operationalUnitDto.setFloorNo(opFloorNo);
            operationalUnitDto.setUnitNo(opUnitNo);
            operationalUnitDto.setSeqNum(j);
            appPremisesOperationalUnitDtos.add(operationalUnitDto);
        }
    }

    private void loadingDraft(BaseProcessClass bpc, String draftNo) {
        log.info(StringUtil.changeForLog("the do loadingDraft start ...."));
        Object draftNumber = bpc.request.getSession().getAttribute(DRAFT_NUMBER);
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
                if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())
                        || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
                    requestForChangeService.svcDocToPresmise(appSubmissionDto);
                    requestForChangeService.svcDocToPresmise(appSubmissionDto.getOldAppSubmissionDto());
                }
                //set max file index into session
                NewApplicationHelper.reSetMaxFileIndex(appSubmissionDto.getMaxFileIndex());

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
                    bpc.getSession().setAttribute(NewApplicationConstant.CO_MAP, coMap);
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
            if (RfcConst.EDIT_LICENSEE.equals(currentEdit)) {
                appEditSelectDto.setLicenseeEdit(true);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "licensee");
            } else if (RfcConst.EDIT_PREMISES.equals(currentEdit)) {
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
            HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute(NewApplicationConstant.CO_MAP);
            coMap.put("licensee", "licensee");
            coMap.put("premises", "premises");
            coMap.put("document", "document");
            coMap.put("information", "information");
            coMap.put("previewli", "previewli");
            bpc.request.getSession().setAttribute(NewApplicationConstant.CO_MAP, coMap);
        }
        log.info(StringUtil.changeForLog("the do requestForChangeLoading end ...."));
    }

    private void requestForInformationLoading(BaseProcessClass bpc, String appNo) {
        log.info(StringUtil.changeForLog("the do requestForInformationLoading start ...."));
        String msgId = (String) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        //msgId = "415199C2-4AAA-42BF-B068-9B019BF1ED1C";
        log.info(StringUtil.changeForLog("MsgId: " + msgId));
        if (!StringUtil.isEmpty(appNo) && !StringUtil.isEmpty(msgId)) {
//            appNo = "AN210511010651A-01";
            AppSubmissionDto appSubmissionDto = appSubmissionService.getAppSubmissionDtoByAppNo(appNo);
            if (appSubmissionDto != null) {
                appSubmissionDto.setAmountStr("N/A");
                // rfiLoadingCheckImplForRenew.checkPremiseInfo(bpc.request,appSubmissionDto,appNo);
                if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType()) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
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
                svcRelatedInfoRFI(appSubmissionDto, appNo);
                //set max file index into session
                NewApplicationHelper.reSetMaxFileIndex(appSubmissionDto.getMaxFileIndex());

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
                    LicenceDto licenceById = requestForChangeService.getLicenceById(licenceId);
                    if (licenceById != null) {
                        appSubmissionDto.setLicenceNo(licenceById.getLicenceNo());
                    } else {
                        log.warn(StringUtil.changeForLog("##### No Active Licence for this ID: " + licenceId));
                    }
                }
                ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
                //ParamUtil.setSessionAttr(bpc.request,OLDAPPSUBMISSIONDTO,oldAppSubmissionDto);
                HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute(NewApplicationConstant.CO_MAP);
                coMap.put("premises", "premises");
                coMap.put("document", "document");
                coMap.put("information", "information");
                coMap.put("previewli", "previewli");
                bpc.request.getSession().setAttribute(NewApplicationConstant.CO_MAP, coMap);
                //control premises edit
                List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
                AppGrpPremisesEntityDto rfiPremises = appSubmissionService.getPremisesByAppNo(appNo);
                String rfiPremHci = NewApplicationHelper.getPremisesKey(rfiPremises);
                if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
                    for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                        appGrpPremisesDto.setFromDB(true);
                        appGrpPremisesDto.setExistingData(AppConsts.NO);
                        String premHci = NewApplicationHelper.getPremisesKey(appGrpPremisesDto);
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
                //filter other premise info
                if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType()) && appGrpPremisesDtos.size() > 1){
                    ApplicationDto applicationDto = applicationFeClient.getApplicationDtoByAppNo(appNo).getEntity();
                    List<AppGrpPremisesDto> newAppGrpPremisesDtoList = IaisCommonUtils.genNewArrayList();
                    filtrationAppGrpPremisesDtos(applicationDto, appSubmissionDto, newAppGrpPremisesDtoList);
                    appSubmissionDto.setAppGrpPremisesDtoList(newAppGrpPremisesDtoList);
                    appSubmissionDto.setAppGrpPrimaryDocDtos(filterPrimaryDocs(appSubmissionDto.getAppGrpPrimaryDocDtos(), newAppGrpPremisesDtoList));
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

    private void svcRelatedInfoRFI(AppSubmissionDto appSubmissionDto, String appNo) {
        if (!ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
            return;
        }
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfoDtoByServiceId(appSubmissionDto.getAppSvcRelatedInfoDtoList(),
                null, appNo);
        if (appSvcRelatedInfoDto == null) {
            return;
        }
        String serviceId = appSvcRelatedInfoDto.getServiceId();
        List<AppSvcRelatedInfoDto> otherList = getOtherAppSvcRelatedInfoDtos(appSubmissionDto.getAppSvcRelatedInfoDtoList(),
                serviceId, appNo);
        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemesByServiceId = serviceConfigService.getHcsaServiceStepSchemesByServiceId(appSvcRelatedInfoDto.getServiceId());
        appSvcRelatedInfoDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemesByServiceId);
        if (otherList != null && !otherList.isEmpty()) {
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            appSvcRelatedInfoDtos.removeAll(otherList);
            clearOtherServiceSvcRelate(appSvcRelatedInfoDtos);
            appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtos);
        }
    }

    private void clearOtherServiceSvcRelate(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos) {
        List<String> serviceIdList = IaisCommonUtils.genNewArrayList();
        for (int i = appSvcRelatedInfoDtos.size() - 1; i>=0; i--){
            if (serviceIdList.contains(appSvcRelatedInfoDtos.get(i).getServiceId())){
                appSvcRelatedInfoDtos.remove(i);
            }else {
                serviceIdList.add(appSvcRelatedInfoDtos.get(i).getServiceId());
            }
        }
    }

    private void setAppSvcDisciplineAllocationDtoSlIndex(List<AppSvcPersonnelDto> appSvcPersonnelDtoList, List<AppSvcDisciplineAllocationDto> otherAppSvcDisciplineAllocationDtoList) {
        if (IaisCommonUtils.isEmpty(appSvcPersonnelDtoList) || IaisCommonUtils.isEmpty(otherAppSvcDisciplineAllocationDtoList)){
            return;
        }
        for (AppSvcDisciplineAllocationDto otherAppSvcDisciplineAllocationDto : otherAppSvcDisciplineAllocationDtoList) {
            for (AppSvcPersonnelDto appSvcPersonnelDto : appSvcPersonnelDtoList){
                if (StringUtil.isNotEmpty(otherAppSvcDisciplineAllocationDto.getSectionLeaderName()) &&
                        otherAppSvcDisciplineAllocationDto.getSectionLeaderName().equals(appSvcPersonnelDto.getName())) {
                    otherAppSvcDisciplineAllocationDto.setSlIndex(appSvcPersonnelDto.getIndexNo());
                }
            }
        }
    }

    private boolean isContainAppSvcPersonnelDto(List<AppSvcPersonnelDto> appSvcPersonnelDtoList, AppSvcPersonnelDto otherAppSvcPersonnelDto) {
        if (IaisCommonUtils.isEmpty(appSvcPersonnelDtoList) || otherAppSvcPersonnelDto == null){
            return false;
        }
        boolean isContain = false;
        for (AppSvcPersonnelDto appSvcPersonnelDto : appSvcPersonnelDtoList){
            if (StringUtil.isNotEmpty(otherAppSvcPersonnelDto.getName()) &&
                    otherAppSvcPersonnelDto.getName().equals(appSvcPersonnelDto.getName())) {
                isContain = true;
                break;
            }
        }
        return isContain;
    }

    private String getNewPsnIndexNo(List<PersonnelDto> srcPersonnels, List<PersonnelDto> tarPersonnels, String srcPsnIndexNo) {
        String psnIndexNo = srcPsnIndexNo;
        if (srcPersonnels == null || tarPersonnels == null) {
            return psnIndexNo;
        }
        Optional<PersonnelDto> any = srcPersonnels.stream()
                .filter(dto -> Objects.equals(srcPsnIndexNo, dto.getPsnIndexNo()))
                .findAny();
        if (any.isPresent()) {
            PersonnelDto p = any.get();
            psnIndexNo = tarPersonnels.stream()
                    .filter(dto -> Objects.equals(p.getPersonType(), dto.getPersonType())
                            && Objects.equals(p.getIdType(), dto.getIdType())
                            && Objects.equals(p.getIdNo(), dto.getIdNo())
                            && Objects.equals(p.getName(), dto.getName()))
                    .findAny()
                    .map(PersonnelDto::getPsnIndexNo)
                    .orElse(srcPsnIndexNo);
        }
        return psnIndexNo;
    }

    private boolean loadingServiceConfig(BaseProcessClass bpc) {
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
        return NewApplicationHelper.getAppSubmissionDto(request);
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
                    if(MessageDigest.isEqual(md5Code.getBytes(StandardCharsets.UTF_8),
                            appGrpPrimaryDocDto.getMd5Code().getBytes(StandardCharsets.UTF_8))){
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
                    appGrpPrimaryDocDto = (AppGrpPrimaryDocDto) CopyUtil.copyMutableObject(appGrpPrimaryDocDto1);
                    break;
                }
            }
        }
        return appGrpPrimaryDocDto;
    }


    private void genPrimaryDoc(Map<String, File> fileMap, String docKey, HcsaSvcDocConfigDto hcsaSvcDocConfigDto,
            Map<String, File> saveFileMap, List<AppGrpPrimaryDocDto> currDocDtoList,
            List<AppGrpPrimaryDocDto> newAppGrpPrimaryDocDtoList,
            String premVal, String premType, boolean isRfi, List<AppGrpPrimaryDocDto> oldPrimaryDotList, String appGrpId, String appNo,
            String appType, String dupForPrem) {
        if (fileMap != null) {
            Set<Integer> nums = IaisCommonUtils.genNewHashSet();
            for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                String k = entry.getKey();
                File v = entry.getValue();
                int index = k.indexOf(docKey);
                String seqNumStr = k.substring(index + docKey.length());
                int seqNum = -1;
                try {
                    seqNum = Integer.parseInt(seqNumStr);
                } catch (Exception e) {
                    log.error(StringUtil.changeForLog("doc seq num can not parse to int"));
                }
                while (!nums.add(seqNum)) {
                    seqNum++;
                }
                AppGrpPrimaryDocDto primaryDocDto = new AppGrpPrimaryDocDto();
                if (v != null) {
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
                    primaryDocDto.setVersion(
                            getAppGrpPrimaryDocVersion(hcsaSvcDocConfigDto.getId(), oldPrimaryDotList, isRfi, md5Code, appGrpId, appNo,
                                    appType, seqNum, dupForPrem));
                    saveFileMap.put(premVal + hcsaSvcDocConfigDto.getId() + seqNum, v);
                } else {
                    primaryDocDto = getAppGrpPrimaryDocByConfigIdAndSeqNum(currDocDtoList, hcsaSvcDocConfigDto.getId(), seqNum,
                            premVal, premType);
                }
                //the data is retrieved from the DTO a second time
                fileMap.put(k, null);
                if (primaryDocDto != null) {
                    newAppGrpPrimaryDocDtoList.add(primaryDocDto);
                }
            }
        }
    }

    private void saveFileAndSetFileId(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList,Map<String,File> saveFileMap) {
        Map<String, File> passValidateFileMap = IaisCommonUtils.genNewLinkedHashMap();
        for (AppGrpPrimaryDocDto primaryDocDto : appGrpPrimaryDocDtoList) {
            if (primaryDocDto.isPassValidate()) {
                String premIndexNo = primaryDocDto.getPremisessName();
                if (StringUtil.isEmpty(premIndexNo)) {
                    premIndexNo = "";
                }
                String fileMapKey = premIndexNo + primaryDocDto.getSvcComDocId() + primaryDocDto.getSeqNum();
                File file = saveFileMap.get(fileMapKey);
                if (file != null) {
                    passValidateFileMap.put(fileMapKey, file);
                }
            }
        }
        log.info(StringUtil.changeForLog("Primary Doc size: " + appGrpPrimaryDocDtoList.size()));
        if (saveFileMap.size() != appGrpPrimaryDocDtoList.size()) {
            log.info(StringUtil.changeForLog("Save file size: " + saveFileMap.size()));
        }
        if (passValidateFileMap.size() > 0) {
            List<File> fileList = new ArrayList<>(passValidateFileMap.values());
            List<String> fileRepoIdList = appSubmissionService.saveFileList(fileList);
            int fileRepo = fileRepoIdList.size();
            log.info(StringUtil.changeForLog("File Repo size: " + fileRepo));
            int i = 0;
            for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtoList) {
                String premIndexNo = appGrpPrimaryDocDto.getPremisessName();
                if (StringUtil.isEmpty(premIndexNo)) {
                    premIndexNo = "";
                }
                String saveFileMapKey = premIndexNo + appGrpPrimaryDocDto.getSvcComDocId() + appGrpPrimaryDocDto.getSeqNum();
                File file = passValidateFileMap.get(saveFileMapKey);
                if (file != null) {
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


