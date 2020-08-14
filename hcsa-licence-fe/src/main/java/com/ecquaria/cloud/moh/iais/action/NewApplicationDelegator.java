package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.api.config.GatewayConfig;
import com.ecquaria.cloud.moh.iais.api.config.GatewayConstants;
import com.ecquaria.cloud.moh.iais.api.services.GatewayAPI;
import com.ecquaria.cloud.moh.iais.common.base.FileType;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSvcPersonAndExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGroupMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.common.validation.VehNoValidator;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.NewApplicationConstant;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.dto.AppSelectSvcDto;
import com.ecquaria.cloud.moh.iais.dto.ApplicationValidateDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.ServiceStepDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.WithOutRenewalService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FeMessageClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import com.ecquaria.sz.commons.util.FileUtil;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;
import sop.util.CopyUtil;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * egator
 *
 * @author suocheng
 * @date 9/23/2019
 */
@Delegator("newApplicationDelegator")
@Slf4j //NOSONAR
public class NewApplicationDelegator {
    private static final String ERRORMAP_PREMISES = "errorMap_premises";
    public static final String PREMISESTYPE = "premisesType";
    private static final String HCSASERVICEDTO = "hcsaServiceDto";
    public static final String CURRENTSERVICEID = "currentServiceId";
    public static final String CURRENTSVCCODE = "currentSvcCode";
    public static final String APPSUBMISSIONDTO = "AppSubmissionDto";
    public static final String OLDAPPSUBMISSIONDTO = "oldAppSubmissionDto";
    public static final String COMMONHCSASVCDOCCONFIGDTO = "commonHcsaSvcDocConfigDto";
    public static final String PREMHCSASVCDOCCONFIGDTO = "premHcsaSvcDocConfigDto";
    public static final String RELOADAPPGRPPRIMARYDOCMAP = "reloadAppGrpPrimaryDocMap";
    public static final String  APPGRPPRIMARYDOCERRMSGMAP = "appGrpPrimaryDocErrMsgMap";

    public static final String REQUESTINFORMATIONCONFIG  = "requestInformationConfig";
    public static final String ACKSTATUS = "AckStatus";
    public static final String ACKMESSAGE = "AckMessage";
    public static final String SERVICEALLPSNCONFIGMAP = "ServiceAllPsnConfigMap";
    public static final String FIRESTOPTION = "Please Select";
    public static final String LICAPPGRPPREMISESDTOMAP = "LicAppGrpPremisesDtoMap";
    public static final String PERSONSELECTMAP = "PersonSelectMap";
    public static final String LICPERSONSELECTMAP = "LicPersonSelectMap";

    private static final String DRAFTCONFIG = "DraftConfig";
    private static final String GROUPLICENCECONFIG = "GroupLicenceConfig";


    //page name
    public static final String APPLICATION_PAGE_NAME_PREMISES                       = "APPPN01";
    public static final String APPLICATION_PAGE_NAME_PRIMARY                        = "APPPN02";
    public static final String APPLICATION_SVC_PAGE_NAME_LABORATORY                 = "APPSPN01";
    public static final String APPLICATION_SVC_PAGE_NAME_GOVERNANCE_OFFICERS        = "APPSPN02";
    public static final String APPLICATION_SVC_PAGE_NAME_DISCIPLINE_ALLOCATION      = "APPSPN03";
    public static final String APPLICATION_SVC_PAGE_NAME_PRINCIPAL_OFFICERS         = "APPSPN04";
    public static final String APPLICATION_SVC_PAGE_NAME_DEPUTY_PRINCIPAL_OFFICERS  = "APPSPN05";
    public static final String APPLICATION_SVC_PAGE_NAME_DOCUMENT                   = "APPSPN06";
    public static final String APPLICATION_SVC_PAGE_NAME_SERVICE_PERSONNEL          = "APPSPN07";
    public static final String APPLICATION_SVC_PAGE_NAME_MEDALERT_PERSON            = "APPSPN08";
    public static final String SELECT_DRAFT_NO                                      ="selectDraftNo";
    //isClickEdit
    public static final String IS_EDIT = "isEdit";

    @Autowired
    private ServiceConfigService serviceConfigService;

    @Autowired
    private AppSubmissionService appSubmissionService;
    @Autowired
    private RequestForChangeService requestForChangeService;

    @Autowired
    private WithOutRenewalService withOutRenewalService;

    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private EventBusHelper eventBusHelper;
    @Autowired
    private GenerateIdClient generateIdClient;
    @Autowired
    private FeMessageClient feMessageClient;
    @Autowired
    private FeEicGatewayClient feEicGatewayClient;
    @Autowired
    private SystemParamConfig systemParamConfig;

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

    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.info(StringUtil.changeForLog("the do Start start ...."));
        //fro draft loading
        String draftNo = ParamUtil.getMaskedString(bpc.request, "DraftNumber");
        //for rfi loading
        String appNo = ParamUtil.getMaskedString(bpc.request,"appNo");
        AuditTrailHelper.auditFunction("hcsa-application", "hcsa application");
        //clear Session
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, null);
        //Primary Documents
        ParamUtil.setSessionAttr(bpc.request, COMMONHCSASVCDOCCONFIGDTO, null);
        ParamUtil.setSessionAttr(bpc.request, PREMHCSASVCDOCCONFIGDTO, null);
        ParamUtil.setSessionAttr(bpc.request, RELOADAPPGRPPRIMARYDOCMAP, null);
        ParamUtil.setSessionAttr(bpc.request,DRAFTCONFIG,null);
        Map<String,AppSvcPrincipalOfficersDto> psnMap = IaisCommonUtils.genNewHashMap();
        ParamUtil.setSessionAttr(bpc.request, PERSONSELECTMAP, (Serializable) psnMap);
        ParamUtil.setSessionAttr(bpc.request,AppServicesConsts.HCSASERVICEDTOLIST, null);
        removeSession(bpc);
        ParamUtil.setSessionAttr(bpc.request,NewApplicationConstant.PREMISES_HCI_LIST, null);
        ParamUtil.setSessionAttr(bpc.request,LICPERSONSELECTMAP, null);
        HashMap<String,String> coMap=new HashMap<>(4);
        coMap.put("premises","");
        coMap.put("document","");
        coMap.put("information","");
        coMap.put("previewli","");
        bpc.request.getSession().setAttribute("coMap",coMap);
        //request For Information Loading
        ParamUtil.setSessionAttr(bpc.request,REQUESTINFORMATIONCONFIG,null);

        requestForChangeOrRenewLoading(bpc);
        //renewLicence(bpc);
        requestForInformationLoading(bpc,appNo);
        //for loading the draft by appId
        loadingDraft(bpc,draftNo);
        //load new application info
        loadingNewAppInfo(bpc);
        //for loading Service Config
        boolean flag = loadingServiceConfig(bpc);
        log.info(StringUtil.changeForLog("The loadingServiceConfig -->:"+flag));
        if(flag){
            //init session and data
            initSession(bpc);
        }
    /*    initOldSession(bpc);*/
        log.info(StringUtil.changeForLog("the do Start end ...."));
    }

    private void removeSession(BaseProcessClass bpc){
        bpc.request.getSession().removeAttribute("oldSubmitAppSubmissionDto");
        bpc.request.getSession().removeAttribute("submitAppSubmissionDto");
        bpc.request.getSession().removeAttribute("appSubmissionDtos");
        bpc.request.getSession().removeAttribute("rfiHcsaService");
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
                action = "premises";
            }
        }
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, action);
        log.info(StringUtil.changeForLog("the do prepare end ...."));
    }

    /**
     * StartStep: PreparePremises
     *
     * @param bpc
     * @throws
     */
    public void preparePremises(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do preparePremises start ...."));
        NewApplicationHelper.setTimeList(bpc.request);
        List<SelectOption> publicHolidayList = serviceConfigService.getPubHolidaySelect();
        ParamUtil.setSessionAttr(bpc.request, "publicHolidaySelect", (Serializable) publicHolidayList);
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        //get svcCode to get svcId
        List<HcsaServiceDto> hcsaServiceDtoList = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST);
        List<HcsaServiceDto>    rfiHcsaService=  (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request,"rfiHcsaService");
        List<String> svcIds = IaisCommonUtils.genNewArrayList();
        if(rfiHcsaService!=null){
            rfiHcsaService.forEach(v->svcIds.add(v.getId()));
        }else {
            if (hcsaServiceDtoList != null) {
                hcsaServiceDtoList.forEach(item -> svcIds.add(item.getId()));
            }
        }

        String licenseeId = appSubmissionDto.getLicenseeId();
        log.info(StringUtil.changeForLog("The preparePremises licenseeId is -->:"+licenseeId));
        //PremisesKey,
        Map<String,AppGrpPremisesDto> licAppGrpPremisesDtoMap = null;
        if(!StringUtil.isEmpty(licenseeId)){
            licAppGrpPremisesDtoMap = serviceConfigService.getAppGrpPremisesDtoByLoginId(licenseeId);
            String appType = appSubmissionDto.getAppType();
            if(licAppGrpPremisesDtoMap != null){
                //remove premise info when pending premises hci same
                List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request,AppServicesConsts.HCSASERVICEDTOLIST);
                List<String> pendAndLicPremHci = appSubmissionService.getHciFromPendAppAndLic(licenseeId,hcsaServiceDtos);
                if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){

                } else if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){
                    List<String> currPremHci = IaisCommonUtils.genNewArrayList();
                    List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
                    if(!IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
                        for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                            currPremHci.add(NewApplicationHelper.genPremHci(appGrpPremisesDto));
                        }
                    }
                    pendAndLicPremHci.removeAll(currPremHci);
                }
                Map<String,AppGrpPremisesDto> newLicAppGrpPremisesDtoMap = IaisCommonUtils.genNewHashMap();
                licAppGrpPremisesDtoMap.forEach((k,v)->{
                    String premisesKey = NewApplicationHelper.getPremKey(v);
                    String premisesHci = "";
                    if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(v.getPremisesType())){
                        premisesHci = v.getHciName()+premisesKey;
                    }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(v.getPremisesType())){
                        premisesHci = v.getConveyanceVehicleNo()+premisesKey;
                    }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(v.getPremisesType())){
                        premisesHci = premisesKey;
                    }
                    if(!pendAndLicPremHci.contains(premisesHci)){
                        newLicAppGrpPremisesDtoMap.put(k,v);
                    }
                });
                licAppGrpPremisesDtoMap = newLicAppGrpPremisesDtoMap;
            }
        }else {
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
            licenseeId = loginContext.getLicenseeId();
        }

        //premise select
        NewApplicationHelper.setPremSelect(bpc.request,licAppGrpPremisesDtoMap);
        ParamUtil.setSessionAttr(bpc.request, LICAPPGRPPREMISESDTOMAP, (Serializable) licAppGrpPremisesDtoMap);
        //addressType
        NewApplicationHelper.setPremAddressSelect(bpc.request);

        //get premises type
        if (!IaisCommonUtils.isEmpty(svcIds)) {
            log.info(StringUtil.changeForLog("svcId not null"));
            Set<String> premisesType  = IaisCommonUtils.genNewHashSet();
            premisesType = serviceConfigService.getAppGrpPremisesTypeBySvcId(svcIds);
            boolean readOnlyPrem = false;
            if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())){
                List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                    if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo()) || !StringUtil.isEmpty(appSvcRelatedInfoDto.getAlignLicenceNo())){
                        readOnlyPrem = true;
                        break;
                    }
                }
                if(readOnlyPrem){
                    premisesType = IaisCommonUtils.genNewHashSet();
                    premisesType.add(ApplicationConsts.PREMISES_TYPE_ON_SITE);
                }
            }

            ParamUtil.setSessionAttr(bpc.request, PREMISESTYPE, (Serializable) premisesType);
        }else{
            log.error(StringUtil.changeForLog("do not have select the services"));
        }

        //addressType
        List<SelectOption> addrTypeOpt = new ArrayList<>();
        SelectOption addrTypeSp = new SelectOption("",FIRESTOPTION);
        addrTypeOpt.add(addrTypeSp);
        addrTypeOpt.addAll(MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ADDRESS_TYPE));
        ParamUtil.setRequestAttr(bpc.request,"addressType",addrTypeOpt);
        //reload dateTime
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        if(!IaisCommonUtils.isEmpty(appGrpPremisesDtoList)){
            for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtoList){
                appGrpPremisesDto = NewApplicationHelper.setWrkTime(appGrpPremisesDto);
            }
        }
        appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtoList);
        //
        int baseSvcCount = 0;
        if(hcsaServiceDtoList!=null){
            for(HcsaServiceDto hcsaServiceDto:hcsaServiceDtoList){
                if(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equalsIgnoreCase(hcsaServiceDto.getSvcType())) {
                    baseSvcCount++;
                }
            }
        }
        if(baseSvcCount>1 ){
            ParamUtil.setRequestAttr(bpc.request, "multiBase", AppConsts.TRUE);
        }else{
            ParamUtil.setRequestAttr(bpc.request, "multiBase", AppConsts.FALSE);
        }
        //when rfc/renew check is select existing premises
        String appType = appSubmissionDto.getAppType();
        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){
            AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,OLDAPPSUBMISSIONDTO);
            if(appSubmissionDto.getAppGrpPremisesDtoList().size() == oldAppSubmissionDto.getAppGrpPremisesDtoList().size()){
                int length = appSubmissionDto.getAppGrpPremisesDtoList().size();
                for(int i=0;i<length;i++){
                    AppGrpPremisesDto appGrpPremisesDto = appSubmissionDto.getAppGrpPremisesDtoList().get(i);
                    AppGrpPremisesDto oldAppGrpPremisesDto = oldAppSubmissionDto.getAppGrpPremisesDtoList().get(i);
                    if(appGrpPremisesDto != null && oldAppGrpPremisesDto != null){
                        String premSel = appGrpPremisesDto.getPremisesSelect();
                        String oldPremSel = oldAppGrpPremisesDto.getPremisesSelect();
                        if(oldPremSel.equals(premSel) || "-1".equals(premSel)){
                            ParamUtil.setRequestAttr(bpc.request,"PageCanEdit",AppConsts.TRUE);
                        }
                    }
                }
            }

            List<AppGrpPremisesDto> appGrpPremisesDtoList1 = appSubmissionDto.getAppGrpPremisesDtoList();
            String licenceNo = appSubmissionDto.getLicenceNo();

            for(int i=0;i<appGrpPremisesDtoList1.size();i++  ){
                String hciCode = appGrpPremisesDtoList1.get(i).getHciCode();
                List<LicenceDto> licenceDtoByHciCode = requestForChangeService.getLicenceDtoByHciCode(hciCode,licenseeId);
                for(LicenceDto licenceDto : licenceDtoByHciCode){
                    if(licenceDto.getLicenceNo().equals(licenceNo)){
                        licenceDtoByHciCode.remove(licenceDto);
                        break;
                    }
                }
                appGrpPremisesDtoList1.get(i).setLicenceDtos(licenceDtoByHciCode);
                bpc.request.getSession().setAttribute("selectLicence"+i,licenceDtoByHciCode);
            }

        }
        ParamUtil.setSessionAttr(bpc.request,APPSUBMISSIONDTO,appSubmissionDto);
        log.info(StringUtil.changeForLog("the do preparePremises end ...."));
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

        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        List<HcsaSvcDocConfigDto> hcsaSvcDocDtos = serviceConfigService.getAllHcsaSvcDocs(null);
        if (hcsaSvcDocDtos != null) {
            List<HcsaSvcDocConfigDto> commonHcsaSvcDocConfigDto = IaisCommonUtils.genNewArrayList();
            List<HcsaSvcDocConfigDto> premHcsaSvcDocConfigDto = IaisCommonUtils.genNewArrayList();
            for(HcsaSvcDocConfigDto hcsaSvcDocConfigDto:hcsaSvcDocDtos){
                if("0".equals(hcsaSvcDocConfigDto.getDupForPrem())){
                    commonHcsaSvcDocConfigDto.add(hcsaSvcDocConfigDto);
                }else if("1".equals(hcsaSvcDocConfigDto.getDupForPrem())){
                    premHcsaSvcDocConfigDto.add(hcsaSvcDocConfigDto);
                }
            }
            ParamUtil.setSessionAttr(bpc.request, COMMONHCSASVCDOCCONFIGDTO, (Serializable) commonHcsaSvcDocConfigDto);
            ParamUtil.setSessionAttr(bpc.request, PREMHCSASVCDOCCONFIGDTO, (Serializable) premHcsaSvcDocConfigDto);
        }

        //reload page
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = appSubmissionDto.getAppGrpPrimaryDocDtos();
        if(appGrpPrimaryDocDtoList != null && appGrpPrimaryDocDtoList.size()>0){
            Map<String,AppGrpPrimaryDocDto> reloadDocMap = IaisCommonUtils.genNewHashMap();
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDocDtoList){
                reloadDocMap.put(appGrpPrimaryDocDto.getPrimaryDocReloadName(), appGrpPrimaryDocDto);
            }
            ParamUtil.setSessionAttr(bpc.request, RELOADAPPGRPPRIMARYDOCMAP, (Serializable) reloadDocMap);
        }else{
            ParamUtil.setSessionAttr(bpc.request, RELOADAPPGRPPRIMARYDOCMAP, (Serializable) new HashMap());
        }
        int sysFileSize = systemParamConfig.getUploadFileLimit();
        ParamUtil.setRequestAttr(bpc.request,"sysFileSize",sysFileSize);
        String sysFileType = systemParamConfig.getUploadFileType();
        String[] sysFileTypeArr = FileUtils.fileTypeToArray(sysFileType);
        StringBuffer fileTypeStr = new StringBuffer();
        if(sysFileTypeArr != null){
            int i = 0;
            int fileTypeLength = sysFileTypeArr.length;
            for(String fileType:sysFileTypeArr){
                fileTypeStr.append(" ").append(fileType);
                if(fileTypeLength > 1 && i < fileTypeLength-2){
                    fileTypeStr.append(",");
                }
                if(fileTypeLength > 1 && i == sysFileTypeArr.length-2){
                    fileTypeStr.append(" and");
                }
                if(i == fileTypeLength-1){
                    fileTypeStr.append(".");
                }
                i++;
            }
        }
        ParamUtil.setRequestAttr(bpc.request,"sysFileType",fileTypeStr.toString());
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
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo() ) || !StringUtil.isEmpty(appSvcRelatedInfoDto.getAlignLicenceNo())){
                    ableGrpLic = false;
                    break;
                }
            }
        }

        if(!IaisCommonUtils.isEmpty(appGrpPremisesDtos) && !IaisCommonUtils.isEmpty(hcsaServiceDtos) && ableGrpLic){
            int premCount = appGrpPremisesDtos.size();
            int svcCount = hcsaServiceDtos.size();
            log.info(StringUtil.changeForLog("premises count:"+premCount+" ,service count:"+svcCount));
            if(premCount >1 && svcCount >= 1){
                //multi prem one svc
                ParamUtil.setRequestAttr(bpc.request,GROUPLICENCECONFIG,"test");
            }
        }

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
        List<AppSubmissionDto> appSubmissionDtos=( List<AppSubmissionDto>)bpc.request.getSession().getAttribute("appSubmissionDtos");

        AppSubmissionDto tranferSub = (AppSubmissionDto)ParamUtil.getSessionAttr(bpc.request,"app-rfc-tranfer");
        if(tranferSub!=null){
            if(appSubmissionDtos==null){
                appSubmissionDtos=new ArrayList<>(1);
                List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = tranferSub.getAppSvcRelatedInfoDtoList();
                if(appSvcRelatedInfoDtoList!=null){
                    for(AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList){
                        appSvcRelatedInfoDto.setGroupNo(tranferSub.getAppGrpNo());
                    }
                }
                appSubmissionDtos.add(tranferSub);
            }
            appSubmissionDto=tranferSub;
        }
        Double total=0.0;
        if(!ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())&&!ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())){
            total+=appSubmissionDto.getAmount();
        }
        if(appSubmissionDtos!=null&&!appSubmissionDtos.isEmpty()){
            for(AppSubmissionDto appSubmissionDto1 : appSubmissionDtos){
                Double amount = appSubmissionDto1.getAmount();
                total=total+amount;
                String amountStr = Formatter.formatterMoney(appSubmissionDto1.getAmount());
                appSubmissionDto1.setAmountStr(amountStr);
                appSubmissionDto1.setServiceName(appSubmissionDto1.getAppSvcRelatedInfoDtoList().get(0).getServiceName());
            }

        }
        appSubmissionDto.setAmount(total);
        if(!StringUtil.isEmpty(appSubmissionDto.getAmount())){
            String amountStr = Formatter.formatterMoney(appSubmissionDto.getAmount());
            log.info(StringUtil.changeForLog("The amountStr is -->:"+amountStr));
            appSubmissionDto.setAmountStr(amountStr);
        }
        ParamUtil.setSessionAttr(bpc.request,APPSUBMISSIONDTO,appSubmissionDto);
        if(appSubmissionDtos!=null){
            bpc.request.getSession().setAttribute("appSubmissionDtos",appSubmissionDtos);
        }

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

        String action = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);

        if("back".equals(action)||RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "jump");
            return;
        }

        String isEdit = ParamUtil.getString(bpc.request, IS_EDIT);
        boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
        boolean isGetDataFromPage = NewApplicationHelper.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_PREMISES_INFORMATION, isEdit, isRfi);
        log.info(StringUtil.changeForLog("isGetDataFromPage:"+isGetDataFromPage));
//        if(isGetDataFromPage && !appSubmissionDto.isOnlySpecifiedSvc() ){
        if(isGetDataFromPage){
            List<AppGrpPremisesDto> appGrpPremisesDtoList = genAppGrpPremisesDtoList(bpc.request);
            appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtoList);
            if(appSubmissionDto.isNeedEditController()){
                Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null? IaisCommonUtils.genNewHashSet() :appSubmissionDto.getClickEditPage();
                clickEditPages.add(APPLICATION_PAGE_NAME_PREMISES);
                appSubmissionDto.setClickEditPage(clickEditPages);
                AppEditSelectDto appEditSelectDto = appSubmissionDto.getChangeSelectDto();
                appEditSelectDto.setPremisesEdit(true);
                appSubmissionDto.setChangeSelectDto(appEditSelectDto);
            }
            //65718
            //remove
            List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();

            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                    //laboratoryDisciplinesDto
                    List<AppSvcLaboratoryDisciplinesDto> laboratoryDisciplinesDtos = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
                    if(!IaisCommonUtils.isEmpty(laboratoryDisciplinesDtos)){
                        List<AppSvcLaboratoryDisciplinesDto> newLaboratoryDisciplinesDtos = IaisCommonUtils.genNewArrayList();
                        for(AppSvcLaboratoryDisciplinesDto laboratoryDisciplinesDto:laboratoryDisciplinesDtos){
                            for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtoList){
                                if(laboratoryDisciplinesDto.getPremiseVal().equals(appGrpPremisesDto.getPremisesIndexNo())){
                                    newLaboratoryDisciplinesDtos.add(laboratoryDisciplinesDto);
                                    break;
                                }
                            }
                        }
                        appSvcRelatedInfoDto.setAppSvcLaboratoryDisciplinesDtoList(newLaboratoryDisciplinesDtos);
                    }
                    //disciplineAllocation
                    List<AppSvcDisciplineAllocationDto> disciplineAllocationDtos = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
                    if(!IaisCommonUtils.isEmpty(disciplineAllocationDtos)){
                        List<AppSvcDisciplineAllocationDto> newDisciplineAllocations = IaisCommonUtils.genNewArrayList();
                        for(AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto:disciplineAllocationDtos){
                            for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtoList){
                                if(appSvcDisciplineAllocationDto.getPremiseVal().equals(appGrpPremisesDto.getPremisesIndexNo())){
                                    newDisciplineAllocations.add(appSvcDisciplineAllocationDto);
                                    break;
                                }
                            }
                        }
                        appSvcRelatedInfoDto.setAppSvcDisciplineAllocationDtoList(newDisciplineAllocations);
                    }
                }
            }
            ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        }
        String crud_action_value = ParamUtil.getString(bpc.request, "crud_action_value");
        String crud_action_additional = ParamUtil.getString(bpc.request, "crud_action_additional");
        if(!"saveDraft".equals(crud_action_value)){
            String keywords = MasterCodeUtil.getCodeDesc("MS001");

            Map<String, List<HcsaSvcPersonnelDto>> allSvcAllPsnConfig = getAllSvcAllPsnConfig(bpc.request);
            List<AppSvcRelatedInfoDto> dto = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            StringBuilder sB =new StringBuilder(10);

            for(int i=0;i< dto.size();i++ ){
                String serviceId = dto.get(i).getServiceId();
                List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = serviceConfigService.getHcsaServiceStepSchemesByServiceId(serviceId);
                ServiceStepDto serviceStepDto = new ServiceStepDto();
                serviceStepDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemeDtos);
                List<HcsaSvcPersonnelDto>  currentSvcAllPsnConfig= serviceConfigService.getSvcAllPsnConfig(hcsaServiceStepSchemeDtos, serviceId);
                doCheckBox(bpc,sB,allSvcAllPsnConfig,currentSvcAllPsnConfig, dto.get(i));
            }
            bpc.request.getSession().setAttribute("serviceConfig",sB.toString());
            List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request,AppServicesConsts.HCSASERVICEDTOLIST);
            List<String> premisesHciList = appSubmissionService.getHciFromPendAppAndLic(appSubmissionDto.getLicenseeId(),hcsaServiceDtos);
            ParamUtil.setSessionAttr(bpc.request,NewApplicationConstant.PREMISES_HCI_LIST, (Serializable) premisesHciList);
            AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.OLDAPPSUBMISSIONDTO);

            Map<String, String> errorMap= requestForChangeService.doValidatePremiss(appSubmissionDto,oldAppSubmissionDto,premisesHciList,keywords,isRfi);
            String crud_action_type_continue = bpc.request.getParameter("crud_action_type_continue");
            String crud_action_type = bpc.request.getParameter("crud_action_type");
            bpc.request.setAttribute("continueStep",crud_action_type);
            bpc.request.setAttribute("crudActionTypeContinue",crud_action_additional);
            if("continue".equals(crud_action_type_continue)){
                errorMap.remove("hciNameUsed");
            }
            String string = errorMap.get("hciNameUsed");
            if(string!=null){
                bpc.request.setAttribute("hciNameUsed","hciNameUsed");
            }
            if(errorMap.size()>0){
                String hciNameUsed = errorMap.get("hciNameUsed");
                if(!StringUtil.isEmpty(hciNameUsed)){
                    ParamUtil.setRequestAttr(bpc.request,"newAppPopUpMsg",hciNameUsed);
                }
                ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE,"premises");
                bpc.request.setAttribute("errormapIs","error");
                HashMap<String,String> coMap=(HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
                coMap.put("premises","");
                coMap.put("serviceConfig",sB.toString());
                bpc.request.getSession().setAttribute("coMap",coMap);
            }else {
                HashMap<String,String> coMap=(HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
                coMap.put("premises","premises");
                coMap.put("serviceConfig",sB.toString());
                bpc.request.getSession().setAttribute("coMap",coMap);
                if("rfcSaveDraft".equals(crud_action_additional)){
                    try {
                        doSaveDraft(bpc);
                    } catch (IOException e) {
                       log.error("error",e);
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
        ParamUtil.setRequestAttr(bpc.request,crud_action_additional,crud_action_additional);
        Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
        Map<String,CommonsMultipartFile> commonsMultipartFileMap = IaisCommonUtils.genNewHashMap();
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = IaisCommonUtils.genNewArrayList();
        AppGrpPrimaryDocDto appGrpPrimaryDocDto = null;
        CommonsMultipartFile file = null;
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);

        String action = ParamUtil.getRequestString(bpc.request, "crud_action_value");
        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())){
            if(RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)) {
                //clear
                ParamUtil.setSessionAttr(bpc.request,RELOADAPPGRPPRIMARYDOCMAP,null);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "jump");
                return;
            }
        }

        Object requestInformationConfig = ParamUtil.getSessionAttr(bpc.request,REQUESTINFORMATIONCONFIG);
        boolean isRfi = false;
        if(requestInformationConfig != null){
            isRfi = true;
        }
        String isEdit = ParamUtil.getString(mulReq, IS_EDIT);
        boolean isGetDataFromPage = NewApplicationHelper.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SUPPORTING_DOCUMENT, isEdit, isRfi);


//        if(isGetDataFromPage && !appSubmissionDto.isOnlySpecifiedSvc()){
        if(isGetDataFromPage){
            List<HcsaSvcDocConfigDto> commonHcsaSvcDocConfigList = (List<HcsaSvcDocConfigDto>) ParamUtil.getSessionAttr(bpc.request, COMMONHCSASVCDOCCONFIGDTO);
            List<HcsaSvcDocConfigDto> premHcsaSvcDocConfigList = (List<HcsaSvcDocConfigDto>) ParamUtil.getSessionAttr(bpc.request, PREMHCSASVCDOCCONFIGDTO);
            List<AppGrpPremisesDto> appGrpPremisesList = appSubmissionDto.getAppGrpPremisesDtoList();
            Map<String,AppGrpPrimaryDocDto> beforeReloadDocMap = (Map<String, AppGrpPrimaryDocDto>) ParamUtil.getSessionAttr(bpc.request, RELOADAPPGRPPRIMARYDOCMAP);
            if(appSubmissionDto.isNeedEditController()){
                Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
                clickEditPages.add(NewApplicationDelegator.APPLICATION_PAGE_NAME_PRIMARY);
                appSubmissionDto.setClickEditPage(clickEditPages);
                AppEditSelectDto appEditSelectDto = appSubmissionDto.getChangeSelectDto();
                appEditSelectDto.setDpoEdit(true);
                appSubmissionDto.setChangeSelectDto(appEditSelectDto);
            }


            for(HcsaSvcDocConfigDto comm:commonHcsaSvcDocConfigList){
                String name = "common"+comm.getId();
                file = (CommonsMultipartFile) mulReq.getFile(name);
                String delFlag = name+"flag";
                String delFlagValue =  mulReq.getParameter(delFlag);
                if(file != null && file.getSize() != 0){
                    if (!StringUtil.isEmpty(file.getOriginalFilename())) {
                        file.getFileItem().setFieldName("selectedFile");
                        appGrpPrimaryDocDto = new AppGrpPrimaryDocDto();
                        appGrpPrimaryDocDto.setSvcComDocId(comm.getId());
                        appGrpPrimaryDocDto.setSvcComDocName(comm.getDocTitle());
                        appGrpPrimaryDocDto.setDocName(file.getOriginalFilename());
                        appGrpPrimaryDocDto.setRealDocSize(file.getSize());
                        long size = file.getSize() / 1024;
                        appGrpPrimaryDocDto.setDocSize(Integer.valueOf(String.valueOf(size)));
                        String md5Code = FileUtil.genMd5FileChecksum(file.getBytes());
                        appGrpPrimaryDocDto.setMd5Code(md5Code);
                        //if  common ==> set null
                        appGrpPrimaryDocDto.setPremisessName("");
                        appGrpPrimaryDocDto.setPremisessType("");
                        commonsMultipartFileMap.put(comm.getId(), file);
                        appGrpPrimaryDocDtoList.add(appGrpPrimaryDocDto);

                    }
                }else if("N".equals(delFlagValue)){
                    AppGrpPrimaryDocDto beforeDto = beforeReloadDocMap.get(name);
                    if(beforeDto != null){
                        appGrpPrimaryDocDtoList.add(beforeDto);
                    }
                } else{
//                    if(comm.getIsMandatory()){
//                        errorMap.put(name, "UC_CHKLMD001_ERR001");
//                    }
                }
            }
            for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesList){
                for(HcsaSvcDocConfigDto prem : premHcsaSvcDocConfigList){

                    String premisesIndexNo = appGrpPremisesDto.getPremisesIndexNo();
                   /* if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){
                        premisesIndexNo = appGrpPremisesDto.getHciName();
                    }else  if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
                        premisesIndexNo = appGrpPremisesDto.getConveyanceVehicleNo();
                    }*/
                    String name = "prem"+prem.getId()+premisesIndexNo;
                    file = (CommonsMultipartFile) mulReq.getFile(name);
                    String delFlag = name+"flag";
                    String delFlagValue =  mulReq.getParameter(delFlag);
                    if(file != null && file.getSize() != 0){
                        if (!StringUtil.isEmpty(file.getOriginalFilename())) {
                            file.getFileItem().setFieldName("selectedFile");
                            appGrpPrimaryDocDto = new AppGrpPrimaryDocDto();
                            appGrpPrimaryDocDto.setSvcComDocId(prem.getId());
                            appGrpPrimaryDocDto.setSvcComDocName(prem.getDocTitle());
                            appGrpPrimaryDocDto.setDocName(file.getOriginalFilename());
                            appGrpPrimaryDocDto.setRealDocSize(file.getSize());
                            long size = file.getSize() / 1024;
                            appGrpPrimaryDocDto.setDocSize(Integer.valueOf(String.valueOf(size)));
                            String md5Code = FileUtil.genMd5FileChecksum(file.getBytes());
                            appGrpPrimaryDocDto.setMd5Code(md5Code);
                            appGrpPrimaryDocDto.setPremisessName(premisesIndexNo);
                            appGrpPrimaryDocDto.setPremisessType(appGrpPremisesDto.getPremisesType());
                            commonsMultipartFileMap.put(premisesIndexNo+prem.getId(), file);
                            appGrpPrimaryDocDtoList.add(appGrpPrimaryDocDto);
                        }
                    }else if("N".equals(delFlagValue)){
                        AppGrpPrimaryDocDto beforeDto = (AppGrpPrimaryDocDto) beforeReloadDocMap.get(name);
                        if(beforeDto != null){
                            appGrpPrimaryDocDtoList.add(beforeDto);
                        }
                    } else{
//                        if(prem.getIsMandatory()) {
//                            errorMap.put(name, "UC_CHKLMD001_ERR001");
//                        }
                    }
                }
            }
            //set value into AppSubmissionDto
            appSubmissionDto.setAppGrpPrimaryDocDtos(appGrpPrimaryDocDtoList);
            ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);


        }

        String crud_action_values = ParamUtil.getRequestString(bpc.request, "crud_action_value");
        if("next".equals(crud_action_values)){
            documentValid(bpc.request, errorMap);
            doIsCommom(bpc.request, errorMap);
            HashMap<String,String> coMap=(HashMap<String, String>)bpc.request.getSession().getAttribute("coMap");
            if(errorMap.isEmpty()){
                coMap.put("document","document");
            }else {
                coMap.put("document","");
            }

            bpc.request.getSession().setAttribute("coMap",coMap);
        }
        if(errorMap.size()>0){
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setSessionAttr(bpc.request, APPGRPPRIMARYDOCERRMSGMAP, (Serializable) errorMap);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "documents");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, "documents");
            return;
        }

        if( commonsMultipartFileMap!= null && commonsMultipartFileMap.size()>0){
            for(AppGrpPrimaryDocDto primaryDoc:appGrpPrimaryDocDtoList){
                String key = primaryDoc.getPremisessName()+primaryDoc.getSvcComDocId();
                CommonsMultipartFile commonsMultipartFile = commonsMultipartFileMap.get(key);
                if(commonsMultipartFile != null && commonsMultipartFile.getSize() != 0){
                    String fileRepoGuid = serviceConfigService.saveFileToRepo(commonsMultipartFile);
                    primaryDoc.setFileRepoId(fileRepoGuid);
                }
            }
        }

        log.info(StringUtil.changeForLog("the do doDocument end ...."));
    }

    private void doIsCommom(HttpServletRequest request, Map<String, String> errorMap) {

        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList  = appSubmissionDto.getAppGrpPrimaryDocDtos();

        List<HcsaSvcDocConfigDto> commonHcsaSvcDocConfigList = (List<HcsaSvcDocConfigDto>)   request.getSession().getAttribute(COMMONHCSASVCDOCCONFIGDTO);
        for(HcsaSvcDocConfigDto comm : commonHcsaSvcDocConfigList){
            String name = "common"+comm.getId();

            Boolean isMandatory = comm.getIsMandatory();
            if(isMandatory&&appGrpPrimaryDocDtoList==null||isMandatory&&appGrpPrimaryDocDtoList.isEmpty()){
                errorMap.put(name, MessageUtil.replaceMessage("GENERAL_ERR0006","Document","field"));
            }else if(isMandatory&&!appGrpPrimaryDocDtoList.isEmpty()){
                Boolean flag=Boolean.FALSE;
                for(AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtoList){
                    String svcComDocId = appGrpPrimaryDocDto.getSvcComDocId();
                    if(comm.getId().equals(svcComDocId)){
                        flag=Boolean.TRUE;
                        break;
                    }
                }
                if(!flag){
                    errorMap.put(name, MessageUtil.replaceMessage("GENERAL_ERR0006","Document","field"));
                }
            }
        }

        List<HcsaSvcDocConfigDto> premHcasDocConfigs = (List<HcsaSvcDocConfigDto>) ParamUtil.getSessionAttr(request,PREMHCSASVCDOCCONFIGDTO);
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        if(!IaisCommonUtils.isEmpty(premHcasDocConfigs) && !IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
            for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                String premIndexNo = appGrpPremisesDto.getPremisesIndexNo();
                for(HcsaSvcDocConfigDto premHcasDocConfig:premHcasDocConfigs){
                    String errName = "prem"+premHcasDocConfig.getId()+premIndexNo;
                    Boolean isMandatory = premHcasDocConfig.getIsMandatory();
                    if(isMandatory && IaisCommonUtils.isEmpty(appGrpPrimaryDocDtoList)){
                        errorMap.put(errName, MessageUtil.replaceMessage("GENERAL_ERR0006","Document","field"));
                    }else if(isMandatory && !IaisCommonUtils.isEmpty(appGrpPrimaryDocDtoList)){
                        boolean isEmpty = false;
                        for(AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtoList){
                            String configId = premHcasDocConfig.getId();
                            String docConfigId = appGrpPrimaryDocDto.getSvcComDocId();
                            if(configId.equals(docConfigId)){
                                isEmpty = true;
                                break;
                            }
                        }
                        if(!isEmpty){
                            errorMap.put(errName, MessageUtil.replaceMessage("GENERAL_ERR0006","Document","field"));
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
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String isGroupLic = ParamUtil.getString(bpc.request,"isGroupLic");
        Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
//        if(!appSubmissionDto.isOnlySpecifiedSvc() && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())){
        if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())){
            if(!StringUtil.isEmpty(isGroupLic) && AppConsts.YES.equals(isGroupLic)){
                appSubmissionDto.setGroupLic(true);
            }else{
                appSubmissionDto.setGroupLic(false);
            }
        }
        String userAgreement = ParamUtil.getString(bpc.request,"verifyInfoCheckbox");
        if(!StringUtil.isEmpty(userAgreement) && AppConsts.YES.equals(userAgreement)){
            appSubmissionDto.setUserAgreement(true);
        }else{
            appSubmissionDto.setUserAgreement(false);
        }
        Object requestInformationConfig = ParamUtil.getSessionAttr(bpc.request,REQUESTINFORMATIONCONFIG);
        if(requestInformationConfig == null&&ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType()) ){
            String effectiveDateStr = ParamUtil.getString(bpc.request,"rfcEffectiveDate");
            if(!StringUtil.isEmpty(effectiveDateStr)) {
                appSubmissionDto.setEffectiveDateStr(effectiveDateStr);
                String configDateSize = MasterCodeUtil.getCodeDesc("EFFDATE");
                LocalDate effectiveDate = LocalDate.parse(effectiveDateStr, DateTimeFormatter.ofPattern(Formatter.DATE));
                LocalDate today = LocalDate.now();
                LocalDate configDate = LocalDate.now().plusDays(Integer.parseInt(configDateSize));
                if(effectiveDate.isBefore(today)){
                    errorMap.put("rfcEffectiveDate","RFC_ERR012");
                }else if(effectiveDate.isAfter(configDate)){
                    String errorMsg = MessageUtil.getMessageDesc("RFC_ERR008");
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Formatter.DATE);
                    errorMsg = errorMsg.replace("<date>",configDate.format(dtf));
                    errorMap.put("rfcEffectiveDate",errorMsg);
                }else if(today.isEqual(effectiveDate)){
                    errorMap.put("rfcEffectiveDate","RFC_ERR012");
                }
                String rfcEffectiveDateErr = errorMap.get("rfcEffectiveDate");
                if(StringUtil.isEmpty(rfcEffectiveDateErr)){
                    Date effDate = DateUtil.parseDate(effectiveDateStr, Formatter.DATE);
                    appSubmissionDto.setEffectiveDate(effDate);
                }
            }
        }
        ParamUtil.setSessionAttr(bpc.request,APPSUBMISSIONDTO,appSubmissionDto);
        if(!errorMap.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request,"errorMsg",WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE,"preview");
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ISVALID,"test");
            return;
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
        List<AppSubmissionDto> appSubmissionDtos=(List<AppSubmissionDto>)bpc.request.getSession().getAttribute("otherAppSubmissionDtos");
        String switch2 = "loading";
        String pmtMethod = appSubmissionDto.getPaymentMethod();
        if(StringUtil.isEmpty(pmtMethod)){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "payment");
            return;
        }
        if(!StringUtil.isEmpty(pmtMethod) && ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(pmtMethod)){
            switch2 = "ack";
            String txnDt = DateUtil.formatDate(new Date(), "dd/MM/yyyy");
            ParamUtil.setSessionAttr(bpc.request,"txnDt",txnDt);
        }
        String result = ParamUtil.getMaskedString(bpc.request,"result");
        String pmtRefNo = ParamUtil.getMaskedString(bpc.request,"reqRefNo");
        if (!StringUtil.isEmpty(result)) {
            log.info(StringUtil.changeForLog("payment result:" + result));
            if ("success".equals(result) && !StringUtil.isEmpty(pmtRefNo)) {
                log.info("credit card payment success");
                if(appSubmissionDtos!=null){
                    for(AppSubmissionDto appSubmissionDto1 :appSubmissionDtos){
                        Double amount = appSubmissionDto1.getAmount();
                        if(0.0!=amount){
                            String grpId = appSubmissionDto1.getAppGrpId();
                            ApplicationGroupDto appGrp = new ApplicationGroupDto();
                            appGrp.setId(grpId);
                            appGrp.setPmtRefNo(pmtRefNo);
                            appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                            serviceConfigService.updatePaymentStatus(appGrp);
                        }
                    }
                }
                String txnDt = ParamUtil.getMaskedString(bpc.request,"txnDt");
                String txnRefNo = ParamUtil.getMaskedString(bpc.request,"txnRefNo");
                ParamUtil.setSessionAttr(bpc.request,"txnDt",txnDt);
                ParamUtil.setSessionAttr(bpc.request,"txnRefNo",txnRefNo);
                switch2 = "ack";
                //update status
                String appGrpId = appSubmissionDto.getAppGrpId();
                ApplicationGroupDto appGrp = new ApplicationGroupDto();
                appGrp.setId(appGrpId);
                appGrp.setPmtRefNo(pmtRefNo);
                appGrp.setPaymentDt(new Date());
                appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                serviceConfigService.updatePaymentStatus(appGrp);
                //send email
                try{
                    sendNewApplicationPaymentOnlineSuccesedEmail(appSubmissionDto,pmtMethod,pmtRefNo);
                    //requestForChangeService.sendEmail(appSubmissionDto.getAppGrpId(),null,appSubmissionDto.getApplicationDtos().get(0).getApplicationNo(),null,null,appSubmissionDto.getAmount(),null,null,appSubmissionDto.getLicenseeId(),"RfcAndOnPay",null);
                }catch(Exception e){
                    log.error(StringUtil.changeForLog("send email error ...."));
                }
            }
        }

        if("ack".equals(switch2)){
            //send email
            try{
                if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())){
                    requestForChangeService.sendRfcPaymentOnlineOrGIROSuccesedEmail(appSubmissionDto);
                }

            }catch(Exception e){
                log.error(StringUtil.changeForLog("send email error ...."));
            }
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
        String action = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        if(!StringUtil.isEmpty(action)){
            if("MohAppPremSelfDecl".equals(action)){
//                ParamUtil.setSessionAttr(bpc.request, NewApplicationConstant.SESSION_PARAM_APPLICATION_GROUP_ID, appSubmissionDto.getAppGrpId());
//                ParamUtil.setSessionAttr(bpc.request,NewApplicationConstant.SESSION_SELF_DECL_ACTION,"new");
            }else if("DashBoard".equals(action)){
                StringBuilder url = new StringBuilder();
                url.append("https://").append(bpc.request.getServerName()).append("/main-web/eservice/INTERNET/MohInternetInbox");
                String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
                bpc.response.sendRedirect(tokenUrl);
            }else if("ChooseSvc".equals(action)){
                StringBuilder url = new StringBuilder();
                url.append("https://").append(bpc.request.getServerName()).append("/hcsa-licence-web/eservice/INTERNET/MohServiceFeMenu");
                String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
                bpc.response.sendRedirect(tokenUrl);
            }
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,action);
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
        if(mulReq!=null){
            crud_action_additional = mulReq.getParameter("crud_action_additional");
        }else {
            crud_action_additional = bpc.request.getParameter("crud_action_additional");
        }
        if("jumpPage".equals(crud_action_additional)){
            jumpYeMian(bpc.request,bpc.response);
            return;
        }
        HashMap<String,String> coMap=(HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
        List<String> strList=new ArrayList<>(5);
        coMap.forEach((k,v)->{
            if(!StringUtil.isEmpty(v)){
                strList.add(v);
            }
        });
        String serviceConfig = (String)bpc.request.getSession().getAttribute("serviceConfig");
        strList.add(serviceConfig);
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        if(StringUtil.isEmpty(appSubmissionDto.getDraftNo())){
            String draftNo = appSubmissionService.getDraftNo(appSubmissionDto.getAppType());
            log.info(StringUtil.changeForLog("the draftNo -->:"+ draftNo) );
            appSubmissionDto.setDraftNo(draftNo);
        }else {
            appSubmissionDto.setOldDraftNo(null);
        }
        String oldDraftNo=(String)bpc.request.getSession().getAttribute(SELECT_DRAFT_NO);
        bpc.request.getSession().removeAttribute(SELECT_DRAFT_NO);
        appSubmissionDto.setOldDraftNo(oldDraftNo);
        appSubmissionDto.setStepColor(strList);
        //set psn dropdown
        setPsnDroTo(appSubmissionDto,bpc);
        appSubmissionDto = appSubmissionService.doSaveDraft(appSubmissionDto);
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        bpc.request.setAttribute("saveDraftSuccess","success");
        log.info(StringUtil.changeForLog("the do doSaveDraft end ...."));
    }

    public void jumpYeMian(HttpServletRequest request , HttpServletResponse response) throws IOException {
        StringBuilder url = new StringBuilder(10);
        url.append("https://").append(request.getServerName()).append("/main-web/eservice/INTERNET/MohInternetInbox");
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), request);
        response.sendRedirect(tokenUrl);
    }
    private void sendURL(HttpServletRequest request,HttpServletResponse response,String url){
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url,request);
        try {
            response.sendRedirect(tokenUrl);
            request.getSession().removeAttribute("orgUserDto");
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
    }
    public void inboxToPreview(BaseProcessClass bpc) throws Exception{
        ParamUtil.setSessionAttr(bpc.request,APPSUBMISSIONDTO,null);
        String appNo = ParamUtil.getMaskedString(bpc.request,"appNo");
        if(!StringUtil.isEmpty(appNo)) {
            ApplicationDto applicationDto = applicationClient.getApplicationDtoByAppNo(appNo).getEntity();
            if(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION.equals(applicationDto.getStatus())){
                List<AppEditSelectDto> entity = applicationClient.getAppEditSelectDtos(applicationDto.getApplicationNo(), ApplicationConsts.APPLICATION_EDIT_TYPE_RFI).getEntity();
                String url= HmacConstants.HTTPS +"://"+systemParamConfig.getInterServerName()+MessageConstants.MESSAGE_CALL_BACK_URL_NEWAPPLICATION+applicationDto.getApplicationNo();
                sendURL(bpc.request,bpc.response,url);
                return;
            }
            AppSubmissionDto appSubmissionDto = appSubmissionService.getAppSubmissionDto(appNo);
            if(appSubmissionDto != null && !IaisCommonUtils.isEmpty(appSubmissionDto.getAppGrpPremisesDtoList())){
                for(AppGrpPremisesDto appGrpPremisesDto:appSubmissionDto.getAppGrpPremisesDtoList()){
                    NewApplicationHelper.setWrkTime(appGrpPremisesDto);
                }
            }
            if(appSubmissionDto!=null){
                AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
                List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemesByServiceId = serviceConfigService.getHcsaServiceStepSchemesByServiceId(appSvcRelatedInfoDto.getServiceId());
                appSvcRelatedInfoDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemesByServiceId);
                String svcId = appSvcRelatedInfoDto.getServiceId();
                HcsaServiceDto hcsaServiceDto  = serviceConfigService.getHcsaServiceDtoById(svcId);
                ParamUtil.setRequestAttr(bpc.request, HCSASERVICEDTO, hcsaServiceDto);
                ParamUtil.setSessionAttr(bpc.request, "currentPreviewSvcInfo", appSvcRelatedInfoDto);
            }
            ParamUtil.setSessionAttr(bpc.request,APPSUBMISSIONDTO,appSubmissionDto);
        }
    }
    /**
     * StartStep: doReDquestInformationSubmit
     *prepare
     * @param bpc
     * @throws
     */
    public void doRequestInformationSubmit(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do doRequestInformationSubmit start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);

        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.OLDAPPSUBMISSIONDTO);
        String appGrpNo = appSubmissionDto.getAppGrpNo();
        //oldAppSubmissionDtos
//        List<AppSubmissionDto> appSubmissionDtoByGroupNo = appSubmissionService.getAppSubmissionDtoByGroupNo(appGrpNo);
        StringBuilder stringBuilder=new StringBuilder(10);
        stringBuilder.append(appSubmissionDto);
        log.info(StringUtil.changeForLog("appSubmissionDto:"+stringBuilder.toString()));
        stringBuilder.setLength(0);
        stringBuilder.append(oldAppSubmissionDto);
        log.info(StringUtil.changeForLog("oldAppSubmissionDto:"+stringBuilder.toString()));
        Map<String, String>   doComChangeMap= doComChange(appSubmissionDto,oldAppSubmissionDto);
        if(!doComChangeMap.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request,"Msg",doComChangeMap);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE,"preview");
            ParamUtil.setRequestAttr(bpc.request,"isrfiSuccess","N");
            return;
        }
        log.info("doComChange is ok ...");
        Map<String, String> map = doPreviewAndSumbit(bpc);
        if(!map.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request,"Msg",map);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE,"preview");
            ParamUtil.setRequestAttr(bpc.request,"isrfiSuccess","N");
            return;
        }
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        oldAppSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        AppSubmissionRequestInformationDto appSubmissionRequestInformationDto = new AppSubmissionRequestInformationDto();

        appSubmissionRequestInformationDto.setAppSubmissionDto(appSubmissionDto);
        appSubmissionRequestInformationDto.setOldAppSubmissionDto(oldAppSubmissionDto);
        //update message statusdo
        String msgId = (String) ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        appSubmissionService.updateMsgStatus(msgId,MessageConstants.MESSAGE_STATUS_RESPONSE);
       /* applicationClient.saveReqeustInformationSubmision(appSubmissionRequestInformationDto);*/
        appSubmissionDto = appSubmissionService.submitRequestInformation(appSubmissionRequestInformationDto, bpc.process);
        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())){
            List<AppSubmissionDto> appSubmissionDtos=new ArrayList<>(1);
            appSubmissionDto.setAmountStr("NA");
            appSubmissionDtos.add(appSubmissionDto);
            ParamUtil.setSessionAttr(bpc.request, "appSubmissionDtos", (Serializable) appSubmissionDtos);
        }
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request,"isrfiSuccess","Y");
        ParamUtil.setRequestAttr(bpc.request,ACKMESSAGE,"The request for information save success");
        log.info(StringUtil.changeForLog("the do doRequestInformationSubmit end ...."));
    }

    public void doRenewSubmit(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the do doRenewSubmit start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        List<ApplicationDto> applicationDtos = requestForChangeService.getAppByLicIdAndExcludeNew(appSubmissionDto.getLicenceId());
        if(!IaisCommonUtils.isEmpty(applicationDtos)){
            ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "Y");
            ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, "error");
            ParamUtil.setRequestAttr(bpc.request,"content",MessageUtil.getMessageDesc("ERRRFC001"));
            return ;
        }
        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, OLDAPPSUBMISSIONDTO);
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        List<AppGrpPremisesDto> oldAppSubmissionDtoAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
        boolean grpPremiseChange=false;
        if(appGrpPremisesDtoList.equals(oldAppSubmissionDtoAppGrpPremisesDtoList)){
            grpPremiseChange=true;
        }
        if(!grpPremiseChange){
            for(int i=0;i<appGrpPremisesDtoList.size();i++){
                List<LicenceDto> attribute =(List<LicenceDto>) bpc.request.getSession().getAttribute("selectLicence"+i);
                if(attribute!=null&&!attribute.isEmpty()){
                    for(LicenceDto licenceDto : attribute){
                        List<ApplicationDto> appByLicIdAndExcludeNew =
                                requestForChangeService.getAppByLicIdAndExcludeNew(licenceDto.getId());
                        if(!IaisCommonUtils.isEmpty(appByLicIdAndExcludeNew)){
                            ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "Y");
                            ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, "error");
                            ParamUtil.setRequestAttr(bpc.request,"content",MessageUtil.getMessageDesc("ERRRFC001"));
                            return ;
                        }
                    }
                }
            }
        }
        boolean isAutoRfc = compareAndSendEmail(appSubmissionDto, oldAppSubmissionDto);
        appSubmissionDto.setAutoRfc(isAutoRfc);
        Map<String, String> map = doPreviewAndSumbit(bpc);
        if(!map.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request,"isrfiSuccess","N");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE,"preview");
            return;
        }

        String draftNo = appSubmissionDto.getDraftNo();
        if(StringUtil.isEmpty(draftNo)){
            draftNo = appSubmissionService.getDraftNo(appSubmissionDto.getAppType());
            appSubmissionDto.setDraftNo(draftNo);
        }
        //get appGroupNo
        String appGroupNo = appSubmissionService.getGroupNo(appSubmissionDto.getAppType());
        log.info(StringUtil.changeForLog("the appGroupNo is -->:"+ appGroupNo) );
        appSubmissionDto.setAppGrpNo(appGroupNo);
        //get Amount
        FeeDto feeDto = appSubmissionService.getGroupAmount(appSubmissionDto);
        appSubmissionDto.setFeeInfoDtos(feeDto.getFeeInfoDtos());
        Double amount = feeDto.getTotal();
        log.info(StringUtil.changeForLog("the amount is -->:"+ amount) );
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
        ParamUtil.setRequestAttr(bpc.request,"isrfiSuccess","N");
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE,"jump");
        ParamUtil.setRequestAttr(bpc.request,"jumpPmt","Y");

        log.info(StringUtil.changeForLog("the do doRenewSubmit end ...."));
    }

    /**
     * StartStep: doRequestForChangeSubmit
     *
     * @param bpc
     * @throwsdoRequestInformationSubmit
     */
    public void doRequestForChangeSubmit(BaseProcessClass bpc) throws  Exception{
        //validate reject  apst050
        log.info(StringUtil.changeForLog("the do doRequestForChangeSubmit start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        List<ApplicationDto> applicationDtos = requestForChangeService.getAppByLicIdAndExcludeNew(appSubmissionDto.getLicenceId());
        //todo chnage edit
        AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
        appEditSelectDto.setServiceEdit(true);
        appEditSelectDto.setPremisesEdit(true);
        appEditSelectDto.setPremisesListEdit(false);
        appEditSelectDto.setDocEdit(true);
        appEditSelectDto.setPoEdit(true);
        appEditSelectDto.setDocEdit(false);
        appSubmissionDto.setAppEditSelectDto(appEditSelectDto);

        Map<String, String> map = doPreviewAndSumbit(bpc);
        if(!map.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request,"Msg",map);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE,"preview");
            ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "N");
            return;
        }
        String effectiveDateStr = appSubmissionDto.getEffectiveDateStr();
        Date effectiveDate = appSubmissionDto.getEffectiveDate();
        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, OLDAPPSUBMISSIONDTO);

        Boolean otherOperation = requestForChangeService.isOtherOperation(appSubmissionDto.getLicenceId());
        if(!otherOperation){
            ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "Y");
            ParamUtil.setRequestAttr(bpc.request,ACKSTATUS,"error");
            ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, MessageUtil.getMessageDesc("ERRRFC002"));
            return ;
        }
        boolean isAutoRfc = compareAndSendEmail(appSubmissionDto, oldAppSubmissionDto);
        //is need to pay ?
        String appGroupNo = appSubmissionService.getGroupNo(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        if(!IaisCommonUtils.isEmpty(applicationDtos)){
            ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "Y");
            ParamUtil.setRequestAttr(bpc.request,ACKSTATUS,"error");
            ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, MessageUtil.getMessageDesc("ERRRFC001"));
            return ;
        }

        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        List<AppGrpPremisesDto> oldAppSubmissionDtoAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
        boolean grpPremiseIsChange=false;
        boolean serviceIsChange=false;
        boolean docIsChange=false;
        List<AppGrpPrimaryDocDto> dtoAppGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
        List<AppGrpPrimaryDocDto> oldAppGrpPrimaryDocDtos = oldAppSubmissionDto.getAppGrpPrimaryDocDtos();
        docIsChange=eqDocChange(dtoAppGrpPrimaryDocDtos,oldAppGrpPrimaryDocDtos);
        appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
        appEditSelectDto.setDocEdit(docIsChange);
        if(appGrpPremisesDtoList!=null){
            for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList){
                appGrpPremisesDto.setLicenceDtos(null);
                if(StringUtil.isEmpty(appGrpPremisesDto.getOffTelNo())){
                    appGrpPremisesDto.setOffTelNo(null);
                }
                if(StringUtil.isEmpty(appGrpPremisesDto.getCertIssuedDtStr())){
                    appGrpPremisesDto.setCertIssuedDtStr(null);
                }
            }
        }
        grpPremiseIsChange = eqGrpPremises(appGrpPremisesDtoList, oldAppSubmissionDtoAppGrpPremisesDtoList);
        AppSubmissionDto n= (AppSubmissionDto)CopyUtil.copyMutableObject(appSubmissionDto);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList=n.getAppSvcRelatedInfoDtoList();
        AppSubmissionDto o =(AppSubmissionDto) CopyUtil.copyMutableObject(oldAppSubmissionDto);
        List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList= o.getAppSvcRelatedInfoDtoList();
        boolean eqServiceResult = eqServiceChange(appSvcRelatedInfoDtoList, oldAppSvcRelatedInfoDtoList);

        serviceIsChange=eqServiceResult;
        appEditSelectDto.setServiceEdit(serviceIsChange);
        appEditSelectDto.setPremisesEdit(grpPremiseIsChange);
        appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
        List<AppSubmissionDto> appSubmissionDtos=IaisCommonUtils.genNewArrayList();
        List<AppSubmissionDto> personAppSubmissionDtos=IaisCommonUtils.genNewArrayList();
        AmendmentFeeDto amendmentFeeDto = getAmendmentFeeDto(appSubmissionDto, oldAppSubmissionDto);
        if(!amendmentFeeDto.getChangeInHCIName() && !amendmentFeeDto.getChangeInLocation()){
            List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
            if(!IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
                for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                    appGrpPremisesDto.setNeedNewLicNo(Boolean.FALSE);
                }
            }
        }
        FeeDto  feeDto = appSubmissionService.getGroupAmendAmount(amendmentFeeDto);
        double amount = feeDto.getTotal();
        log.info(StringUtil.changeForLog("the amount is -->:"+ amount) );
        appSubmissionDto.setAmount(amount);
        if(appGrpPremisesDtoList!=null){
            int size = appGrpPremisesDtoList.size();
            for(int i=0;i<size;i++){
                //Get the selected license from page to save
                List<LicenceDto> attribute =(List<LicenceDto>)bpc.request.getSession().getAttribute("selectLicence" + i);
                if(attribute!=null){
                    for(LicenceDto string :attribute){
                        AppSubmissionDto appSubmissionDtoByLicenceId = requestForChangeService.getAppSubmissionDtoByLicenceId(string.getId());
                        appSubmissionDtoByLicenceId.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
                        appSubmissionService.transform(appSubmissionDtoByLicenceId,appSubmissionDto.getLicenseeId());
                        boolean groupLic = appSubmissionDtoByLicenceId.isGroupLic();
                        boolean equals=false;
                        String address = appSubmissionDto.getAppGrpPremisesDtoList().get(i).getAddress();
                        String premisesIndexNo="";
                            if(groupLic){
                                amendmentFeeDto.setChangeInLocation(equals);
                                AppGrpPremisesDto appGrpPremisesDto = oldAppSubmissionDto.getAppGrpPremisesDtoList().get(i);
                                 premisesIndexNo = appGrpPremisesDto.getPremisesIndexNo();
                                boolean b = compareHciName(appGrpPremisesDto, appSubmissionDto.getAppGrpPremisesDtoList().get(i));
                                amendmentFeeDto.setChangeInHCIName(!b);
                                String grpAddress = appGrpPremisesDto.getAddress();
                                equals = grpAddress.equals(address);
                                amendmentFeeDto.setChangeInLocation(!equals);
                                List<AppGrpPremisesDto> rfcAppGrpPremisesDtoList = appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList();
                                if(rfcAppGrpPremisesDtoList!=null){
                                    for(AppGrpPremisesDto appGrpPremisesDto1 : rfcAppGrpPremisesDtoList){
                                        appGrpPremisesDto1.setGroupLicenceFlag(string.getId());
                                    }
                                }

                            }else {
                                String oldAddress = appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList().get(0).getAddress();
                                equals = oldAddress.equals(address);
                                boolean b = compareHciName(appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList().get(0), appSubmissionDto.getAppGrpPremisesDtoList().get(i));
                                amendmentFeeDto.setChangeInHCIName(!b);
                                amendmentFeeDto.setChangeInLocation(!equals);
                                premisesIndexNo=appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList().get(0).getPremisesIndexNo();
                            }
                            if(equals){
                                List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList();
                                if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
                                    for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                                        appGrpPremisesDto.setNeedNewLicNo(Boolean.FALSE);
                                    }
                                }
                            }
                            appSubmissionDtoByLicenceId.setGroupLic(groupLic);
                            appSubmissionDtoByLicenceId.setAmount(amount);
                            AppGrpPremisesDto appGrpPremisesDto = appSubmissionDto.getAppGrpPremisesDtoList().get(i);
                            List<AppGrpPremisesDto> appGrpPremisesDtos=new ArrayList<>(1);
                            AppGrpPremisesDto copyMutableObject = (AppGrpPremisesDto)CopyUtil.copyMutableObject(appGrpPremisesDto);
                            appGrpPremisesDtos.add(copyMutableObject);
                            if(groupLic){
                                appGrpPremisesDtos.get(0).setGroupLicenceFlag(string.getId() );
                            }
                            appSubmissionDtoByLicenceId.setAppGrpPremisesDtoList(appGrpPremisesDtos);
                            appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList().get(0).setPremisesIndexNo(premisesIndexNo);
                            appSubmissionDtoByLicenceId.setAppGrpNo(appGroupNo);
                            appSubmissionDtoByLicenceId.setIsNeedNewLicNo(AppConsts.YES);
                            PreOrPostInspectionResultDto preOrPostInspectionResultDto = appSubmissionService.judgeIsPreInspection(appSubmissionDtoByLicenceId);
                            if (preOrPostInspectionResultDto == null) {
                                appSubmissionDtoByLicenceId.setPreInspection(true);
                                appSubmissionDtoByLicenceId.setRequirement(true);
                            } else {
                                appSubmissionDtoByLicenceId.setPreInspection(preOrPostInspectionResultDto.isPreInspection());
                                appSubmissionDtoByLicenceId.setRequirement(preOrPostInspectionResultDto.isRequirement());
                            }

                            appSubmissionDtoByLicenceId.setAutoRfc(isAutoRfc);

                            appEditSelectDto.setPremisesEdit(true);
                            appSubmissionDtoByLicenceId.setAppEditSelectDto(appEditSelectDto);
                            appSubmissionDtoByLicenceId.setChangeSelectDto(appEditSelectDto);
                            appSubmissionDtoByLicenceId.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
                            appSubmissionDtoByLicenceId.setStatus(ApplicationConsts.APPLICATION_STATUS_REQUEST_FOR_CHANGE_SUBMIT);
                            String draftNo = appSubmissionDtoByLicenceId.getDraftNo();
                            if(StringUtil.isEmpty(draftNo)){
                                appSubmissionService.setDraftNo(appSubmissionDtoByLicenceId);
                            }
                            if(0.0==amount){
                                appSubmissionDtoByLicenceId.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
                                appSubmissionDtoByLicenceId.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
                            }else {
                                appSubmissionDtoByLicenceId.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_PENDING_PAYMENT);
                            }
                            appSubmissionDtoByLicenceId.setGetAppInfoFromDto(true);
                            RequestForChangeMenuDelegator.oldPremiseToNewPremise(appSubmissionDtoByLicenceId);
                            requestForChangeService.premisesDocToSvcDoc(appSubmissionDtoByLicenceId);
                            appSubmissionDtoByLicenceId.setAuditTrailDto( IaisEGPHelper.getCurrentAuditTrailDto());
                            appSubmissionDtoByLicenceId.setEffectiveDate(effectiveDate);
                            appSubmissionDtoByLicenceId.setEffectiveDateStr(effectiveDateStr);
                            appSubmissionDtos.add(appSubmissionDtoByLicenceId);
                    }
                }
            }
        }

        appSubmissionDto.setAutoRfc(isAutoRfc);
        String draftNo = appSubmissionDto.getDraftNo();
        if(StringUtil.isEmpty(draftNo)){
            draftNo = appSubmissionService.getDraftNo(appSubmissionDto.getAppType());
            appSubmissionDto.setDraftNo(draftNo);
        }
        //appSubmissionDto =checkAndSetData(bpc.request, appSubmissionDto);
        //get appGroupNo
        log.info(StringUtil.changeForLog("the appGroupNo is -->:"+ appGroupNo) );
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
        if(0.0==amount){
            appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
            appSubmissionDto.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
        }else {
            appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_PENDING_PAYMENT);
        }
        appSubmissionDto.setIsNeedNewLicNo(AppConsts.NO);
   /*     appSubmissionDto = appSubmissionService.submitRequestChange(appSubmissionDto, bpc.process);*/
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        String isrfiSuccess = "N";
        requestForChangeService.premisesDocToSvcDoc(appSubmissionDto);
        appSubmissionDto.setPartPremise(appSubmissionDto.isGroupLic());
        appSubmissionDto.setGetAppInfoFromDto(true);
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos=IaisCommonUtils.genNewArrayList();
        List<AppSubmissionDto> appSubmissionDtoList=IaisCommonUtils.genNewArrayList();
        List<AppSubmissionDto> autoSaveAppsubmission=IaisCommonUtils.genNewArrayList();
        List<AppSubmissionDto> notAutoSaveAppsubmission=IaisCommonUtils.genNewArrayList();
        if(grpPremiseIsChange){
            appSubmissionDtos.add(appSubmissionDto);

            if(isAutoRfc){
                autoSaveAppsubmission.addAll(appSubmissionDtos);
            }else {
                notAutoSaveAppsubmission.addAll(appSubmissionDtos);
            }

        }
        ParamUtil.setRequestAttr(bpc.request,"isrfiSuccess",isrfiSuccess);
        if("Y".equals(isrfiSuccess)){
            AppSubmissionDto appSubmissionDto1 = getAppSubmissionDto(bpc.request);
            ParamUtil.setSessionAttr(bpc.request,APPSUBMISSIONDTO,appSubmissionDto1);
        }
       /* String submissionId1 = generateIdClient.getSeqId().getEntity();*/
        AppSubmissionListDto appSubmissionListDto1 =new AppSubmissionListDto();
        Long l1 = System.currentTimeMillis();
        appSubmissionListDto1.setEventRefNo(l1.toString());
        boolean appGrpMisc=false;
        if(serviceIsChange){
            List<AppSubmissionDto> personAppSubmissionList = personContact(bpc,appSubmissionDto, oldAppSubmissionDto);
            AppSubmissionDto personAppsubmit = getPersonAppsubmit(oldAppSubmissionDto, appSubmissionDto, bpc);
            personAppsubmit.setPartPremise(personAppsubmit.isGroupLic());
            requestForChangeService.premisesDocToSvcDoc(personAppsubmit);
            personAppSubmissionDtos.add(personAppsubmit);
            // true is auto
            boolean autoRfc = personAppsubmit.isAutoRfc();
            if(!autoRfc){
                if(!notAutoSaveAppsubmission.isEmpty()){
                    AppSubmissionDto appSubmissionDto1 = notAutoSaveAppsubmission.get(notAutoSaveAppsubmission.size() - 1);
                    appSubmissionDto1.setAppSvcRelatedInfoDtoList(personAppsubmit.getAppSvcRelatedInfoDtoList());
                }else {
                    notAutoSaveAppsubmission.add(personAppsubmit);
                }
            }else {
                if(!autoSaveAppsubmission.isEmpty()){
                    AppSubmissionDto appSubmissionDto1 = autoSaveAppsubmission.get(autoSaveAppsubmission.size() - 1);
                    appSubmissionDto1.setAppSvcRelatedInfoDtoList(personAppsubmit.getAppSvcRelatedInfoDtoList());
                }else {
                    autoSaveAppsubmission.add(personAppsubmit);
                }
                if(!notAutoSaveAppsubmission.isEmpty()){
                    appGrpMisc=true;
                }
            }
            autoSaveAppsubmission.addAll(personAppSubmissionList);

        }
        Map<String,AppSvcPersonAndExtDto> personMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.PERSONSELECTMAP);
        for(AppSubmissionDto appSubmissionDto1 : autoSaveAppsubmission){
            NewApplicationHelper.syncPsnData(appSubmissionDto1, personMap);
        }
        for(AppSubmissionDto appSubmissionDto1 : notAutoSaveAppsubmission){
            NewApplicationHelper.syncPsnData(appSubmissionDto1, personMap);
        }
        String auto = generateIdClient.getSeqId().getEntity();
        String notAuto = generateIdClient.getSeqId().getEntity();
        AppSubmissionListDto autoAppSubmissionListDto =new AppSubmissionListDto();
        AppSubmissionListDto notAutoAppSubmissionListDto =new AppSubmissionListDto();
        Long autoTime = System.currentTimeMillis();
        Long notAutoTime = System.currentTimeMillis();
        autoAppSubmissionListDto.setEventRefNo(autoTime.toString());
        notAutoAppSubmissionListDto.setEventRefNo(notAutoTime.toString());
        if(!notAutoSaveAppsubmission.isEmpty()){
            //set missing data
            for(AppSubmissionDto appSubmissionDto1:notAutoSaveAppsubmission){
                appSubmissionDto1.setEffectiveDateStr(effectiveDateStr);
                appSubmissionDto.setEffectiveDate(effectiveDate);
            }
            List<AppSubmissionDto> appSubmissionDtos1 = requestForChangeService.saveAppsForRequestForGoupAndAppChangeByList(notAutoSaveAppsubmission);
            notAutoAppSubmissionListDto.setAppSubmissionDtos(appSubmissionDtos1);
            eventBusHelper.submitAsyncRequest(notAutoAppSubmissionListDto,notAuto, EventBusConsts.SERVICE_NAME_APPSUBMIT,
                    EventBusConsts.OPERATION_REQUEST_INFORMATION_SUBMIT,notAutoTime.toString(),bpc.process);
            appSubmissionDtos1.get(0).getAppSvcRelatedInfoDtoList().get(0).setGroupNo(appSubmissionDtos1.get(0).getAppGrpNo());

            for(AppSubmissionDto appSubmissionDto1 : notAutoSaveAppsubmission){
                Double amount1 = appSubmissionDto1.getAmount();
                String s = Formatter.formatterMoney(amount1);
                appSubmissionDto1.setAmountStr(s);
            }

            appSubmissionDtoList.addAll(appSubmissionDtos1);
            appSubmissionDto.setAppGrpId(appSubmissionDtos1.get(0).getAppGrpId());
        }
        if(!autoSaveAppsubmission.isEmpty()){
            //set missing data
            for(AppSubmissionDto appSubmissionDto1:autoSaveAppsubmission){
                appSubmissionDto1.setEffectiveDateStr(effectiveDateStr);
                appSubmissionDto.setEffectiveDate(effectiveDate);
            }
            List<AppSubmissionDto> appSubmissionDtos1 = requestForChangeService.saveAppsForRequestForGoupAndAppChangeByList(autoSaveAppsubmission);
            autoAppSubmissionListDto.setAppSubmissionDtos(appSubmissionDtos1);
            eventBusHelper.submitAsyncRequest(autoAppSubmissionListDto,auto, EventBusConsts.SERVICE_NAME_APPSUBMIT,
                    EventBusConsts.OPERATION_REQUEST_INFORMATION_SUBMIT,autoTime.toString(),bpc.process);
            appSubmissionDtos1.get(0).getAppSvcRelatedInfoDtoList().get(0).setGroupNo(appSubmissionDtos1.get(0).getAppGrpNo());
            double t=0.0;
            for(AppSubmissionDto appSubmissionDto1 : autoSaveAppsubmission){
                t+=appSubmissionDto1.getAmount();
            }
            if(appGrpMisc){
                if(!appSubmissionDtoList.isEmpty()){
                    String grpId = appSubmissionDtoList.get(0).getAppGrpId();
                    String grpId1 = appSubmissionDtos1.get(0).getAppGrpId();
                    AppGroupMiscDto appGroupMiscDto=new AppGroupMiscDto();
                    appGroupMiscDto.setAppGrpId(grpId);
                    appGroupMiscDto.setMiscValue(grpId1);
                    appGroupMiscDto.setMiscType(ApplicationConsts.APP_GROUP_MISC_TYPE_AMEND_GROUP_ID);
                    appGroupMiscDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    appSubmissionService.saveAppGrpMisc(appGroupMiscDto);
                }
            }
            for(AppSubmissionDto appSubmissionDto1 :autoSaveAppsubmission){
                String s = Formatter.formatterMoney(appSubmissionDto1.getAmount());
                appSubmissionDto1.setAmountStr(s);
                appSubmissionDto1.setAppGrpNo(autoSaveAppsubmission.get(0).getAppGrpNo());
            }
            appSubmissionDtoList.addAll( appSubmissionDtos1);
            appSubmissionDto.setAppGrpId(appSubmissionDtos1.get(0).getAppGrpId());
        }

        bpc.request.getSession().setAttribute("appSubmissionDtos",appSubmissionDtoList);
        log.info(StringUtil.changeForLog("the do doRequestForChangeSubmit start ...."));
    }



    private boolean eqDocChange( List<AppGrpPrimaryDocDto> dtoAppGrpPrimaryDocDtos, List<AppGrpPrimaryDocDto> oldAppGrpPrimaryDocDtos){
        if(dtoAppGrpPrimaryDocDtos!=null&&oldAppGrpPrimaryDocDtos==null||dtoAppGrpPrimaryDocDtos==null&&oldAppGrpPrimaryDocDtos!=null){
            return true;
        }else if(dtoAppGrpPrimaryDocDtos!=null&&oldAppGrpPrimaryDocDtos!=null){
            if(dtoAppGrpPrimaryDocDtos.equals(oldAppGrpPrimaryDocDtos)){
                return true;
            }
        }

        return false;
    }

    private boolean eqServiceChange(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList, List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList) throws  Exception{
        List<AppSvcRelatedInfoDto> n=(List<AppSvcRelatedInfoDto>)CopyUtil.copyMutableObject(appSvcRelatedInfoDtoList);
        List<AppSvcRelatedInfoDto> o=(List<AppSvcRelatedInfoDto>)CopyUtil.copyMutableObject(oldAppSvcRelatedInfoDtoList);
        List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList = n.get(0).getAppSvcDisciplineAllocationDtoList();
        List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList1 = o.get(0).getAppSvcDisciplineAllocationDtoList();
        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = n.get(0).getHcsaServiceStepSchemeDtos();
        String deputyPoFlag = n.get(0).getDeputyPoFlag();
        o.get(0).setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemeDtos);
        o.get(0).setDeputyPoFlag(deputyPoFlag);
        if(appSvcDisciplineAllocationDtoList!=null&&appSvcDisciplineAllocationDtoList1!=null){
            for(AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto : appSvcDisciplineAllocationDtoList){
                String idNo = appSvcDisciplineAllocationDto.getIdNo();
                String premiseVal = appSvcDisciplineAllocationDto.getPremiseVal();
                String chkLstConfId = appSvcDisciplineAllocationDto.getChkLstConfId();
                String cgoSelName = appSvcDisciplineAllocationDto.getCgoSelName();
                String chkLstName = appSvcDisciplineAllocationDto.getChkLstName();
                for(AppSvcDisciplineAllocationDto allocationDto : appSvcDisciplineAllocationDtoList1){
                    String idNo1 = allocationDto.getIdNo();
                    String premiseVal1 = allocationDto.getPremiseVal();
                    String chkLstConfId1 = allocationDto.getChkLstConfId();
                    try {
                        if(idNo.equals(idNo1)&&premiseVal.equals(premiseVal1)&&chkLstConfId.equals(chkLstConfId1)){
                            allocationDto.setCgoSelName(cgoSelName);
                            allocationDto.setChkLstName(chkLstName);
                        }
                    }catch (NullPointerException e){
                        log.error("error " ,e);
                    }
                }
            }
        }
        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = n.get(0).getAppSvcPrincipalOfficersDtoList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcPrincipalOfficersDtoList = o.get(0).getAppSvcPrincipalOfficersDtoList();
        if(appSvcPrincipalOfficersDtoList!=null&&oldAppSvcPrincipalOfficersDtoList!=null){
            List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos = copyAppSvcPo(appSvcPrincipalOfficersDtoList);
            List<AppSvcPrincipalOfficersDto> oldAppSvcPrincipalOfficersDtos = copyAppSvcPo(oldAppSvcPrincipalOfficersDtoList);
            n.get(0).setAppSvcPrincipalOfficersDtoList(appSvcPrincipalOfficersDtos);
            o.get(0).setAppSvcPrincipalOfficersDtoList(oldAppSvcPrincipalOfficersDtos);
        }
        List<AppSvcCgoDto> appSvcCgoDtoList = n.get(0).getAppSvcCgoDtoList();
        List<AppSvcCgoDto> oldAppSvcCgoDtoList = o.get(0).getAppSvcCgoDtoList();
        if(appSvcCgoDtoList!=null&&oldAppSvcCgoDtoList!=null){
            List<AppSvcCgoDto> appSvcCgoDtos = copyAppSvcCgo(appSvcCgoDtoList);
            List<AppSvcCgoDto> oldAppSvcCgoDtos = copyAppSvcCgo(oldAppSvcCgoDtoList);
            n.get(0).setAppSvcCgoDtoList(appSvcCgoDtos);
            o.get(0).setAppSvcCgoDtoList(oldAppSvcCgoDtos);
        }
        List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = n.get(0).getAppSvcMedAlertPersonList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcMedAlertPersonList = o.get(0).getAppSvcMedAlertPersonList();
        if(appSvcMedAlertPersonList!=null&&oldAppSvcMedAlertPersonList!=null){
            List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonDtos = copyMedaler(appSvcMedAlertPersonList);
            List<AppSvcPrincipalOfficersDto> oldAppSvcMedAlertPersonDtos = copyMedaler(oldAppSvcMedAlertPersonList);
            n.get(0).setAppSvcMedAlertPersonList(appSvcMedAlertPersonDtos);
            o.get(0).setAppSvcMedAlertPersonList(oldAppSvcMedAlertPersonDtos);
        }
        if(!o.equals(n)){

           return true;
        }
        return false;
    }

    public static void premisesDocToSvcDoc( AppSubmissionDto appSubmissionDtoByLicenceId){
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDtoByLicenceId.getAppGrpPrimaryDocDtos();
        List<AppSvcDocDto> appSvcDocDtoLits = appSubmissionDtoByLicenceId.getAppSvcRelatedInfoDtoList().get(0).getAppSvcDocDtoLit();
        if(!StringUtil.isEmpty(appSvcDocDtoLits)){
            for(AppSvcDocDto appSvcDocDto : appSvcDocDtoLits){
                String svcDocId = appSvcDocDto.getSvcDocId();
                if(StringUtil.isEmpty(svcDocId)){
                    continue;
                }

            }
        }
        List<AppSvcDocDto> appSvcDocDtoList=IaisCommonUtils.genNewArrayList();
        if(appGrpPrimaryDocDtos!=null){
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtos){
                AppSvcDocDto appSvcDocDto = MiscUtil.transferEntityDto(appGrpPrimaryDocDto, AppSvcDocDto.class);
                appSvcDocDto.setSvcDocId(appGrpPrimaryDocDto.getSvcComDocId());
                appSvcDocDto.setDocName(appGrpPrimaryDocDto.getDocName());
                appSvcDocDtoList.add(appSvcDocDto);
            }
            appSubmissionDtoByLicenceId.setAppGrpPrimaryDocDtos(null);
        }
        List<AppSvcDocDto> appSvcDocDtoLit = appSubmissionDtoByLicenceId.getAppSvcRelatedInfoDtoList().get(0).getAppSvcDocDtoLit();
        if(appSvcDocDtoLit!=null){
            appSvcDocDtoList.addAll(appSvcDocDtoLit);
        }
        appSubmissionDtoByLicenceId.getAppSvcRelatedInfoDtoList().get(0).setAppSvcDocDtoLit(appSvcDocDtoList);
    }

    private  List<AppSubmissionDto> personContact(BaseProcessClass bpc,AppSubmissionDto appSubmissionDto,AppSubmissionDto oldAppSubmissionDto) throws  Exception{
        AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
        appEditSelectDto.setServiceEdit(true);
        appEditSelectDto.setPremisesEdit(false);
        appEditSelectDto.setDocEdit(false);
        appEditSelectDto.setPoEdit(false);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto1 = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        if(appSvcRelatedInfoDto1==null||appSvcRelatedInfoDto==null){
            return null;
        }
        List<AppSvcCgoDto> appSvcCgoDtoList1 = appSvcRelatedInfoDto1.getAppSvcCgoDtoList();
        List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList1 = appSvcRelatedInfoDto1.getAppSvcMedAlertPersonList();
        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList1 = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();

        List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
        List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = appSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
        Set<String> set=IaisCommonUtils.genNewHashSet();
        List<String> list=IaisCommonUtils.genNewArrayList();
        boolean b = eqCgo(appSvcCgoDtoList1, appSvcCgoDtoList);
        if(b){
            if(appSvcCgoDtoList!=null){
                List<String> cgoIdNo=IaisCommonUtils.genNewArrayList();
                for(int i=0;i< appSvcCgoDtoList.size();i++){
                    String idNo = appSvcCgoDtoList.get(i).getIdNo();
                    for(AppSvcCgoDto appSvcCgoDto : appSvcCgoDtoList1){
                        String idNo1 = appSvcCgoDto.getIdNo();
                        if(idNo.equals(idNo1)){
                            cgoIdNo.add(idNo);
                            set.add(idNo);
                        }
                    }
                }
            }
        }
        boolean eqMeadrterResult = eqMeadrter(appSvcMedAlertPersonList1, appSvcMedAlertPersonList);
        if(eqMeadrterResult){
            if(appSvcMedAlertPersonList!=null){
                for(int i=0;i<appSvcMedAlertPersonList.size();i++){
                    String idNo = appSvcMedAlertPersonList.get(i).getIdNo();
                    for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcMedAlertPersonList1){
                        String idNo1 = appSvcPrincipalOfficersDto.getIdNo();
                        if(idNo.equals(idNo1)){
                            set.add(idNo);
                        }
                    }
                }
            }
        }

        boolean eqSvcPrincipalOfficersResult = eqSvcPrincipalOfficers(appSvcPrincipalOfficersDtoList1, appSvcPrincipalOfficersDtoList);

        if(eqSvcPrincipalOfficersResult){
            if(appSvcPrincipalOfficersDtoList!=null){
                for(int i=0;i<appSvcPrincipalOfficersDtoList.size();i++){
                    String idNo = appSvcPrincipalOfficersDtoList.get(i).getIdNo();
                    for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcPrincipalOfficersDtoList1){
                        String idNo1 = appSvcPrincipalOfficersDto.getIdNo();
                        if(idNo.equals(idNo1)){
                            set.add(idNo);
                        }
                    }
                }
            }
        }
        list.addAll(set);
        List<LicKeyPersonnelDto> licKeyPersonnelDtos=IaisCommonUtils.genNewArrayList();
        for(String string :list){
            List<String> personnelDtoByIdNo = requestForChangeService.getPersonnelIdsByIdNo(string);
            List<LicKeyPersonnelDto> licKeyPersonnelDtoByPerId = requestForChangeService.getLicKeyPersonnelDtoByPerId(personnelDtoByIdNo);
            licKeyPersonnelDtos.addAll(licKeyPersonnelDtoByPerId);
        }
        Set<String> licenceId =IaisCommonUtils.genNewHashSet();
        List<String> licenceIdList=IaisCommonUtils.genNewArrayList();
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = loginContext.getLicenseeId();
        if(StringUtil.isEmpty(licenseeId)){
            return null;
        }
        for(LicKeyPersonnelDto licKeyPersonnelDto: licKeyPersonnelDtos){
            if(licenseeId.equals(licKeyPersonnelDto.getLicenseeId())){
                licenceId.add(licKeyPersonnelDto.getLicenceId());
            }
        }
        licenceIdList.addAll(licenceId);
        List<AppSubmissionDto> appSubmissionDtoList=IaisCommonUtils.genNewArrayList();
        for(String string : licenceIdList){
            AppSubmissionDto appSubmissionDtoByLicenceId = requestForChangeService.getAppSubmissionDtoByLicenceId(string);
            if(appSubmissionDtoByLicenceId==null|| appSubmissionDtoByLicenceId.getAppSvcRelatedInfoDtoList()==null){
                continue;
            }
            AppSvcRelatedInfoDto appSvcRelatedInfoDto2 = appSubmissionDtoByLicenceId.getAppSvcRelatedInfoDtoList().get(0);

            List<AppSvcCgoDto> appSvcCgoDtoList2 = appSvcRelatedInfoDto2.getAppSvcCgoDtoList();
            if(appSvcCgoDtoList2!=null&&appSvcCgoDtoList1!=null){
                appSvcRelatedInfoDto2.setAppSvcCgoDtoList(appSvcCgoDtoList1);
            }
            List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList2 = appSubmissionDtoByLicenceId.getAppSvcRelatedInfoDtoList().get(0).getAppSvcMedAlertPersonList();
            if(appSvcMedAlertPersonList2!=null&&appSvcMedAlertPersonList1!=null){
                appSvcRelatedInfoDto2.setAppSvcMedAlertPersonList(appSvcMedAlertPersonList1);
            }
            List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList2 = appSubmissionDtoByLicenceId.getAppSvcRelatedInfoDtoList().get(0).getAppSvcPrincipalOfficersDtoList();
            if(appSvcPrincipalOfficersDtoList2!=null&&appSvcPrincipalOfficersDtoList1!=null){
                appSvcRelatedInfoDto2.setAppSvcPrincipalOfficersDtoList(appSvcPrincipalOfficersDtoList1);
            }
            appSubmissionDtoByLicenceId.setAppEditSelectDto(appEditSelectDto);
            appSubmissionDtoByLicenceId.setPartPremise(appSubmissionDtoByLicenceId.isGroupLic());
            appSubmissionDtoByLicenceId.setGetAppInfoFromDto(true);
            RequestForChangeMenuDelegator.oldPremiseToNewPremise(appSubmissionDtoByLicenceId);
            appSubmissionDtoByLicenceId.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
            appSubmissionService.transform(appSubmissionDtoByLicenceId,appSubmissionDto.getLicenseeId());
            requestForChangeService.premisesDocToSvcDoc(appSubmissionDtoByLicenceId);
            appSubmissionDtoByLicenceId.setAutoRfc(true);
            appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
            appSubmissionDto.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
            appSubmissionDtoList.add(appSubmissionDtoByLicenceId);
        }

        return appSubmissionDtoList;
    }

 /*   private void changeCgoInfo( List<AppSvcCgoDto> appSvcCgoDtoList, List<AppSvcCgoDto> oldAppSvcCgoDtoList, List<AppSubmissionDto> appSubmissionDtoList){
        if(appSubmissionDtoList.isEmpty()){
            return;
        }
        for(AppSvcCgoDto appSvcCgoDto : appSvcCgoDtoList){
            for(AppSvcCgoDto oldAppSvcCgo : oldAppSvcCgoDtoList){
                if(appSvcCgoDto.getIdNo().equals(oldAppSvcCgo.getIdNo())){
                    for(AppSubmissionDto appSubmissionDto : appSubmissionDtoList){
                        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
                        List<AppSvcCgoDto> appSvcCgoDtoList1 = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
                        List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = appSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
                        if(appSvcCgoDtoList1!=null){
                            for(AppSvcCgoDto appSvcCgoDto1 : appSvcCgoDtoList1){
                                if(appSvcCgoDto1.getIdNo().equals(appSvcCgoDto.getIdNo())){
                                    appSvcCgoDto1= MiscUtil.transferEntityDto(appSvcCgoDto,AppSvcCgoDto.class);
                                }
                            }
                        }
                        if(appSvcPrincipalOfficersDtoList!=null){
                           for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcPrincipalOfficersDtoList){
                               if(appSvcPrincipalOfficersDto.getIdNo().equals(appSvcCgoDto.getIdNo())){
                                   appSvcPrincipalOfficersDto = MiscUtil.transferEntityDto(appSvcCgoDto, AppSvcPrincipalOfficersDto.class);
                               }
                           }
                        }
                        if(appSvcMedAlertPersonList!=null){
                            for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcMedAlertPersonList){
                                if(appSvcPrincipalOfficersDto.getIdNo().equals(appSvcCgoDto.getIdNo())){
                                    appSvcPrincipalOfficersDto= MiscUtil.transferEntityDto(appSvcCgoDto,AppSvcPrincipalOfficersDto.class);
                                }
                            }

                        }
                    }

                }
            }
        }
    }
    private void changePo(  List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList,  List<AppSvcPrincipalOfficersDto> oldAppSvcPrincipalOfficersDtoList,List<AppSubmissionDto> appSubmissionDtoList){
        if(appSubmissionDtoList==null){
            return;
        }
        for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcPrincipalOfficersDtoList){
            for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto1 : oldAppSvcPrincipalOfficersDtoList){
                if(appSvcPrincipalOfficersDto.getIdNo().equals(appSvcPrincipalOfficersDto1.getIdNo())){
                   for(AppSubmissionDto appSubmissionDto : appSubmissionDtoList){
                       AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
                       List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                       List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList1 = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
                       List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = appSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
                       if(appSvcCgoDtoList!=null){
                           for(AppSvcCgoDto appSvcCgoDto : appSvcCgoDtoList){
                               if(appSvcCgoDto.getIdNo().equals(appSvcPrincipalOfficersDto.getIdNo())){
                                   appSvcCgoDto=MiscUtil.transferEntityDto(appSvcPrincipalOfficersDto,AppSvcCgoDto.class,new HashMap<>(),appSvcCgoDto);
                               }
                           }
                       }
                       if(appSvcPrincipalOfficersDtoList1!=null){
                           for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto2 : appSvcPrincipalOfficersDtoList1){
                               if(appSvcPrincipalOfficersDto2.getIdNo().equals(appSvcPrincipalOfficersDto.getIdNo())){
                                   appSvcPrincipalOfficersDto2= MiscUtil.transferEntityDto(appSvcPrincipalOfficersDto,AppSvcPrincipalOfficersDto.class,new HashMap<>(),appSvcPrincipalOfficersDto2);
                               }
                           }
                       }
                       if(appSvcMedAlertPersonList!=null){
                           for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto2 : appSvcMedAlertPersonList){
                               if(appSvcPrincipalOfficersDto2.getIdNo().equals(appSvcPrincipalOfficersDto.getIdNo())){
                                   appSvcPrincipalOfficersDto2= MiscUtil.transferEntityDto(appSvcPrincipalOfficersDto,AppSvcPrincipalOfficersDto.class,new HashMap<>(),appSvcPrincipalOfficersDto2);
                               }
                           }
                       }
                   }

                }
            }
        }
    }

    private void changeMerder( List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList,  List<AppSvcPrincipalOfficersDto> oldAppSvcMedAlertPersonList,List<AppSubmissionDto> appSubmissionDtoList){
        if(appSubmissionDtoList.isEmpty()){
            return;
        }

        for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcMedAlertPersonList){
            for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto1 : oldAppSvcMedAlertPersonList){
                if(appSvcPrincipalOfficersDto.getIdNo().equals(appSvcPrincipalOfficersDto1.getIdNo())){
                    for(AppSubmissionDto appSubmissionDto : appSubmissionDtoList){
                        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);

                    }
                }
            }
        }

    }*/
    private boolean eqCgo( List<AppSvcCgoDto> appSvcCgoDtoList, List<AppSvcCgoDto> oldAppSvcCgoDtoList) throws  Exception{
       if(appSvcCgoDtoList!=null&&oldAppSvcCgoDtoList!=null){
           List<AppSvcCgoDto> n = copyAppSvcCgo(appSvcCgoDtoList);
           List<AppSvcCgoDto> o =copyAppSvcCgo(oldAppSvcCgoDtoList);
            if(!n.equals(o)){
                return true;
            }
       }else if(appSvcCgoDtoList!=null&&oldAppSvcCgoDtoList==null||appSvcCgoDtoList==null&&oldAppSvcCgoDtoList!=null){
           return true;
       }
        return false;
    }

    private  List<AppSvcCgoDto> copyAppSvcCgo( List<AppSvcCgoDto> appSvcCgoDtoList) throws  Exception{
        List<AppSvcCgoDto> n =(List<AppSvcCgoDto>)CopyUtil.copyMutableObject(appSvcCgoDtoList);
        for(AppSvcCgoDto appSvcCgoDto : n){
            appSvcCgoDto.setLicPerson(false);
            appSvcCgoDto.setSelectDropDown(false);
            appSvcCgoDto.setNeedSpcOptList(false);
            appSvcCgoDto.setPreferredMode(null);
            appSvcCgoDto.setSpcOptList(null);
            appSvcCgoDto.setSpecialityHtml(null);
            appSvcCgoDto.setCgoIndexNo(null);
            appSvcCgoDto.setAssignSelect(null);
            appSvcCgoDto.setOfficeTelNo(null);
        }
        return n;
    }
    private boolean eqMeadrter(  List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList,  List<AppSvcPrincipalOfficersDto> oldAppSvcMedAlertPersonList1) throws  Exception{
        if(appSvcMedAlertPersonList!=null&&oldAppSvcMedAlertPersonList1!=null){
            List<AppSvcPrincipalOfficersDto> n=copyMedaler(appSvcMedAlertPersonList);
            List<AppSvcPrincipalOfficersDto> o=copyMedaler(oldAppSvcMedAlertPersonList1);

            if(!n.equals(o)){
                return true;
            }
        }else if(appSvcMedAlertPersonList!=null&&oldAppSvcMedAlertPersonList1==null||appSvcMedAlertPersonList==null&&oldAppSvcMedAlertPersonList1!=null){
            return true;
        }
        return false;
    }

    private boolean eqSvcPrincipalOfficers( List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList , List<AppSvcPrincipalOfficersDto> oldAppSvcPrincipalOfficersDtoList) throws  Exception{
        if(appSvcPrincipalOfficersDtoList!=null&&oldAppSvcPrincipalOfficersDtoList!=null){
            List<AppSvcPrincipalOfficersDto> n= copyAppSvcPo(appSvcPrincipalOfficersDtoList);
            List<AppSvcPrincipalOfficersDto> o=copyAppSvcPo(oldAppSvcPrincipalOfficersDtoList);
            if(!n.equals(o)){
                return true;
            }
        }else if(appSvcPrincipalOfficersDtoList==null&&oldAppSvcPrincipalOfficersDtoList!=null||appSvcPrincipalOfficersDtoList!=null&&oldAppSvcPrincipalOfficersDtoList==null){
            return true;
        }
        return false;
    }
    private List<AppSvcPrincipalOfficersDto> copyMedaler( List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList) throws Exception{
        List<AppSvcPrincipalOfficersDto> n=(List<AppSvcPrincipalOfficersDto>) CopyUtil.copyMutableObject(appSvcMedAlertPersonList);
        for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto: n){
            appSvcPrincipalOfficersDto.setLicPerson(false);
            appSvcPrincipalOfficersDto.setSelectDropDown(false);
            appSvcPrincipalOfficersDto.setNeedSpcOptList(false);
            appSvcPrincipalOfficersDto.setSpecialityHtml(null);
            appSvcPrincipalOfficersDto.setSpcOptList(null);
            appSvcPrincipalOfficersDto.setCgoIndexNo(null);
            appSvcPrincipalOfficersDto.setAssignSelect(null);
            appSvcPrincipalOfficersDto.setOfficeTelNo(null);
        }
        return n;
    }

    private List<AppSvcPrincipalOfficersDto>  copyAppSvcPo(List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList) throws  Exception{
        List<AppSvcPrincipalOfficersDto> n=(List<AppSvcPrincipalOfficersDto>) CopyUtil.copyMutableObject(appSvcPrincipalOfficersDtoList);
        for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : n){
            appSvcPrincipalOfficersDto.setLicPerson(false);
            appSvcPrincipalOfficersDto.setSelectDropDown(false);
            appSvcPrincipalOfficersDto.setNeedSpcOptList(false);
            appSvcPrincipalOfficersDto.setSpecialityHtml(null);
            appSvcPrincipalOfficersDto.setSpcOptList(null);
            appSvcPrincipalOfficersDto.setCgoIndexNo(null);
            appSvcPrincipalOfficersDto.setAssignSelect(null);
            appSvcPrincipalOfficersDto.setPreferredMode(null);
        }
        return n;
    }
    public static boolean compareHciName(AppGrpPremisesDto premisesListQueryDto, AppGrpPremisesDto appGrpPremisesDto){

        String newHciName = "";
        String oldHciName = "";
        if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesListQueryDto.getPremisesType())){
            oldHciName = premisesListQueryDto.getHciName();
        }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesListQueryDto.getPremisesType())){
            oldHciName = premisesListQueryDto.getConveyanceVehicleNo();
        }
        if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){
            newHciName = appGrpPremisesDto.getHciName();
        }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
            newHciName = appGrpPremisesDto.getConveyanceVehicleNo();
        }
        if(!newHciName.equals(oldHciName)){
            return false;
        }

        return true;
    }
    private AppSubmissionDto  getPersonAppsubmit( AppSubmissionDto oldAppSubmissionDto,AppSubmissionDto appSubmissionDto ,BaseProcessClass bpc) throws  Exception{
        AppEditSelectDto appEditSelectDto=new AppEditSelectDto();
        AppSubmissionDto changePerson=(AppSubmissionDto)CopyUtil.copyMutableObject(oldAppSubmissionDto);
        boolean b = changePersonAuto(oldAppSubmissionDto, appSubmissionDto);
        changePerson.setAppSvcRelatedInfoDtoList(appSubmissionDto.getAppSvcRelatedInfoDtoList());
        changePerson.setAutoRfc(!b);
        String changePersonDraftNo = changePerson.getDraftNo();
        if(StringUtil.isEmpty(changePersonDraftNo)){
            if(StringUtil.isEmpty(changePerson.getDraftNo())){
                String draftNo = appSubmissionService.getDraftNo(changePerson.getAppType());
                changePerson.setDraftNo(draftNo);
            }
        }
        if(b){
            appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
            appSubmissionDto.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
        }else {
            appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
            appSubmissionDto.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
        }
        changePerson.setAmount(0.0);
        changePerson.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        changePerson.setStatus(ApplicationConsts.APPLICATION_STATUS_REQUEST_FOR_CHANGE_SUBMIT);
        changePerson.setIsNeedNewLicNo(AppConsts.YES);
        changePerson.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        appSubmissionService.transform(changePerson,appSubmissionDto.getLicenseeId());
        PreOrPostInspectionResultDto preOrPostInspectionResultDto1 = appSubmissionService.judgeIsPreInspection(changePerson);
        if (preOrPostInspectionResultDto1 == null) {
            changePerson.setPreInspection(true);
            changePerson.setRequirement(true);
        } else {
            changePerson.setPreInspection(preOrPostInspectionResultDto1.isPreInspection());
            changePerson.setRequirement(preOrPostInspectionResultDto1.isRequirement());
        }
        appSubmissionService.setRiskToDto(changePerson);
        appEditSelectDto.setPremisesEdit(false);
        appEditSelectDto.setServiceEdit(true);
        changePerson.setAppEditSelectDto(appEditSelectDto);
        changePerson.setChangeSelectDto(appEditSelectDto);

        return changePerson;
    }

    private boolean changePersonAuto(AppSubmissionDto oldAppSubmissionDto,AppSubmissionDto appSubmissionDto){
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
        List<AppSvcCgoDto> oldAppSvcCgoDtoList = oldAppSvcRelatedInfoDto.getAppSvcCgoDtoList();
        if(oldAppSvcCgoDtoList!=null){
            if(oldAppSvcCgoDtoList.size()!=appSvcCgoDtoList.size()){
                return true;
            }else if(oldAppSvcCgoDtoList.size()==appSvcCgoDtoList.size()){
                int i=0;
                for(AppSvcCgoDto appSvcCgoDto : oldAppSvcCgoDtoList){
                    for(AppSvcCgoDto appSvcCgoDto1 : appSvcCgoDtoList){
                        if(appSvcCgoDto.getIdNo().equals(appSvcCgoDto1.getIdNo())){
                            i++;
                            break;
                        }
                    }
                }
                if(i!=appSvcCgoDtoList.size()){
                    return true;
                }
            }
        }
        List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = appSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcMedAlertPersonList = oldAppSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
        if(oldAppSvcMedAlertPersonList!=null){
            if(oldAppSvcMedAlertPersonList.size()!=appSvcMedAlertPersonList.size()){
                return true;
            }else if(oldAppSvcMedAlertPersonList.size()==appSvcMedAlertPersonList.size()){
                int i=0;
                for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcMedAlertPersonList){
                    for( AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto1 : oldAppSvcMedAlertPersonList){
                        if(appSvcPrincipalOfficersDto.getIdNo().equals(appSvcPrincipalOfficersDto1.getIdNo())){
                            i++;
                            break;
                        }
                    }
                }
                if(i!=oldAppSvcMedAlertPersonList.size()){
                    return true;
                }
            }
        }
        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcPrincipalOfficersDtoList = oldAppSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
        if(oldAppSvcPrincipalOfficersDtoList!=null){
            if(oldAppSvcPrincipalOfficersDtoList.size()!=appSvcPrincipalOfficersDtoList.size()){
                return true;
            }else if(oldAppSvcPrincipalOfficersDtoList.size()==appSvcPrincipalOfficersDtoList.size()){
                int i=0;
                for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcPrincipalOfficersDtoList){
                    for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto1 : oldAppSvcPrincipalOfficersDtoList){
                        if(appSvcPrincipalOfficersDto.getIdNo().equals(appSvcPrincipalOfficersDto1.getIdNo())){
                            i++;
                            break;
                        }
                    }
                }
                if(i!=oldAppSvcPrincipalOfficersDtoList.size()){
                    return true;
                }
            }
        }

        return false;
    }
    /**
     * StartStep: doSubmit
     *
     * @param bpc
     * @throws
     */
    public void doSubmit(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("the do doSubmit start ...."));

        Map<String, String> map = doPreviewAndSumbit(bpc);
        if(!map.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request,"Msg",map);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE,"preview");
            HashMap<String,String> coMap=(HashMap<String, String>)bpc.request.getSession().getAttribute("coMap");
            coMap.put("previewli","");
            bpc.request.getSession().setAttribute("coMap",coMap);
            return;
        }else {
            HashMap<String,String> coMap=(HashMap<String, String>)bpc.request.getSession().getAttribute("coMap");
            coMap.put("previewli","previewli");
            bpc.request.getSession().setAttribute("coMap",coMap);
        }
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        //sync person data
        Map<String,AppSvcPersonAndExtDto> personMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.PERSONSELECTMAP);
        NewApplicationHelper.syncPsnData(appSubmissionDto,personMap);

        String draftNo = appSubmissionDto.getDraftNo();
        if(StringUtil.isEmpty(draftNo)){
            draftNo = appSubmissionService.getDraftNo(appSubmissionDto.getAppType());
            appSubmissionDto.setDraftNo(draftNo);
        }
        //get appGroupNo
        String appGroupNo = appSubmissionService.getGroupNo(appSubmissionDto.getAppType());
        log.info(StringUtil.changeForLog("the appGroupNo is -->:"+ appGroupNo) );
        appSubmissionDto.setAppGrpNo(appGroupNo);
        //get Amount
        FeeDto feeDto = appSubmissionService.getGroupAmount(appSubmissionDto);
        appSubmissionDto.setFeeInfoDtos(feeDto.getFeeInfoDtos());
        Double amount = feeDto.getTotal();
        log.info(StringUtil.changeForLog("the amount is -->:"+ amount) );
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
        setPsnDroTo(appSubmissionDto,bpc);
        //rfi select control
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
        appEditSelectDto.setPremisesEdit(true);
        appEditSelectDto.setDocEdit(true);
        appEditSelectDto.setServiceEdit(true);
        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
            if(StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo()) || StringUtil.isEmpty(appSvcRelatedInfoDto.getAlignLicenceNo())){
                appEditSelectDto.setPremisesEdit(false);
                break;
            }
        }
        appSubmissionDto.setChangeSelectDto(appEditSelectDto);

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
        String  crudActionValue = (String) ParamUtil.getRequestAttr(bpc.request,IaisEGPConstant.ISVALID);
        if(StringUtil.isEmpty(crudActionValue)){
            crudActionValue = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
            if (StringUtil.isEmpty(crudActionValue)) {
                crudActionValue = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
            }
        }
        Object requestInformationConfig = ParamUtil.getSessionAttr(bpc.request,REQUESTINFORMATIONCONFIG);
        if((ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())
                ||ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) && requestInformationConfig == null){
            String crud_action_additional = ParamUtil.getString(bpc.request, "crud_action_additional");
            if("rfcSaveDraft".equals(crud_action_additional)){
                crudActionValue = "saveDraft";
            }
        }
        if ("saveDraft".equals(crudActionValue) || "ack".equals(crudActionValue)) {
            switch2 = crudActionValue;
        }else if("doSubmit".equals(crudActionValue)){
            if(requestInformationConfig == null){
                switch2 = crudActionValue;
                if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())){
                    switch2 = "requstChange";
                }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())){
                    switch2 = "renew";
                }
            }else{
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
        if(StringUtil.isEmpty(payMethod)){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE,"payment");
            return;
        }
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        Double totalAmount = appSubmissionDto.getAmount();
        if(totalAmount == 0.0){
            StringBuilder url = new StringBuilder();
            url.append("https://")
                    .append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTERNET/MohNewApplication/PrepareAckPage");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
            return;
        }
        String a=appSubmissionDto.getPaymentMethod();
        appSubmissionDto.setPaymentMethod(payMethod);
        ParamUtil.setSessionAttr(bpc.request,APPSUBMISSIONDTO,appSubmissionDto);
        if(ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT.equals(payMethod)
                || ApplicationConsts.PAYMENT_METHOD_NAME_NETS.equals(payMethod)
                || ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW.equals(payMethod)){
            //send email
            try {
                inspectionDateSendNewApplicationPaymentOnlineEmail(appSubmissionDto,bpc);
            }catch (Exception e){
                log.error(StringUtil.changeForLog("send email error ...."));
            }
            String amount = String.valueOf(appSubmissionDto.getAmount());
            Map<String, String> fieldMap = new HashMap<String, String>();
            fieldMap.put(GatewayConstants.AMOUNT_KEY, amount);
            fieldMap.put(GatewayConstants.PYMT_DESCRIPTION_KEY, payMethod);
            fieldMap.put(GatewayConstants.SVCREF_NO, appSubmissionDto.getAppGrpNo());
            try {
                String html= GatewayAPI.create_partner_trade_by_buyer(fieldMap,bpc.request, GatewayConfig.return_url);
                ParamUtil.setRequestAttr(bpc.request,"jumpHtml",html);
            } catch (Exception e) {
                log.info(e.getMessage(),e);
            }

            return;
        }else if(ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod)){
            //send email
            try {
                sendNewApplicationPaymentGIROEmail(appSubmissionDto,bpc);
            }catch (Exception e){
                log.error(StringUtil.changeForLog("send email error ...."));
            }
            String appGrpId = appSubmissionDto.getAppGrpId();
            ApplicationGroupDto appGrp = new ApplicationGroupDto();
            appGrp.setId(appGrpId);
            appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_SUCCESS);
            serviceConfigService.updatePaymentStatus(appGrp);
            ParamUtil.setRequestAttr(bpc.request, "PmtStatus", ApplicationConsts.PAYMENT_METHOD_NAME_GIRO);
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
    public void prepareErrorAck(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the do prepareErrorAck start ...."));

        log.info(StringUtil.changeForLog("the do prepareErrorAck end ...."));
    }

    public void doErrorAck(BaseProcessClass bpc){
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
        String txnRefNo=(String)bpc.request.getSession().getAttribute("txnDt");
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        String licenceId = appSubmissionDto.getLicenceId();
        if(!StringUtil.isEmpty(licenceId)){
            List<ApplicationSubDraftDto> entity = applicationClient.getDraftByLicAppId(licenceId).getEntity();
            for(ApplicationSubDraftDto applicationSubDraftDto : entity){
                applicationClient.deleteDraftByNo(applicationSubDraftDto.getDraftNo());
            }
        }
        if(StringUtil.isEmpty(txnRefNo)){
            String txnDt = DateUtil.formatDate(new Date(), "dd/MM/yyyy");
            ParamUtil.setSessionAttr(bpc.request,"txnDt",txnDt);
        }
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto)ParamUtil.getSessionAttr(bpc.request,"INTER_INBOX_USER_INFO");
        String licenseeId = null;
        if(interInboxUserDto!=null){
            licenseeId = interInboxUserDto.getLicenseeId();
        }else{
            log.error(StringUtil.changeForLog("interInboxUserDto null"));
        }
        List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(licenseeId);
        String emailAddress = WithOutRenewalDelegator.emailAddressesToString(licenseeEmailAddrs);
        ParamUtil.setRequestAttr(bpc.request,"emailAddress",emailAddress);
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
        String action = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);
        if(StringUtil.isEmpty(action)){
            action = ParamUtil.getRequestString(bpc.request, "nextStep");
        }
        if(RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)){
            AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.OLDAPPSUBMISSIONDTO);
            AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
            AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto();
            if(appEditSelectDto.isPremisesEdit()){
                appSubmissionDto.setAppGrpPremisesDtoList(oldAppSubmissionDto.getAppGrpPremisesDtoList());
            }
            if(appEditSelectDto.isDocEdit()){
                appSubmissionDto.setAppGrpPrimaryDocDtos(oldAppSubmissionDto.getAppGrpPrimaryDocDtos());
            }
            if(appEditSelectDto.isServiceEdit()){
                appSubmissionDto.setAppSvcRelatedInfoDtoList(oldAppSubmissionDto.getAppSvcRelatedInfoDtoList());
            }
            ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.APPSUBMISSIONDTO,appSubmissionDto);
        }

        log.info(StringUtil.changeForLog("the do prepareJump end ...."));
    }

    //=============================================================================
    //private method
    //=============================================================================

    private void sendNewApplicationPaymentOnlineSuccesedEmail(AppSubmissionDto appSubmissionDto,String paymentMethod,String pmtRefNo) {
        MsgTemplateDto msgTemplateDto = appSubmissionService.getMsgTemplateById(MsgTemplateConstants.MSG_TEMPLATE_SUCCESSED_PAYMENT_ID);
        if(msgTemplateDto != null) {
            Double amount = appSubmissionDto.getAmount();
            String licenseeId = appSubmissionDto.getLicenseeId();
            List<ApplicationDto> applicationDtos = appSubmissionDto.getApplicationDtos();
            List<String> applicationNos = new ArrayList<String>();
            if(!IaisCommonUtils.isEmpty(applicationDtos)){
                for(ApplicationDto applicationDto : applicationDtos){
                    String applicationNo = applicationDto.getApplicationNo();
                    if(!StringUtil.isEmpty(applicationNo)){
                        applicationNos.add(applicationNo);
                    }
                }
            }
            String appGrpNo = appSubmissionDto.getAppGrpNo();
            String subject = " " + msgTemplateDto.getTemplateName() + " " + appGrpNo;
            Map<String, Object> map = IaisCommonUtils.genNewHashMap();
            map.put("applications", applicationNos);
            map.put("paymentType", paymentMethod);
            map.put("pmtRefNo", pmtRefNo);
            map.put("paymentDate", Formatter.formatDateTime(new Date(),Formatter.DATE_EMAIL_DAY));
            map.put("paymentAmount",Formatter.formatNumber(amount));
            map.put("MOH_AGENCY_NAME",AppConsts.MOH_AGENCY_NAME);
            String mesContext = null;
            try {
                mesContext = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), map);
            } catch (IOException | TemplateException e) {
                log.error(e.getMessage(),e);
            }
            EmailDto emailDto = new EmailDto();
            emailDto.setContent(mesContext);
            emailDto.setSubject(subject);
            emailDto.setSender(mailSender);
            emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
            if(appSubmissionDto.getAppGrpId()!=null){
                emailDto.setClientQueryCode(appSubmissionDto.getAppGrpId());
                //send email
                appSubmissionService.feSendEmail(emailDto);
            }

            //send message
            HashMap<String, String> maskParams = IaisCommonUtils.genNewHashMap();
            List<String> serviceCodeList = IaisCommonUtils.genNewArrayList();
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if(appSvcRelatedInfoDtoList != null){
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList){
                    String serviceId = appSvcRelatedInfoDto.getServiceId();
                    HcsaServiceDto serviceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
                    if(serviceDto != null){
                        serviceCodeList.add(serviceDto.getSvcCode());
                    }
                }
                String serviceCodeString = getServiceCodeString(serviceCodeList);
                if(!StringUtil.isEmpty(serviceCodeString)){
                    sendMessageHelper(subject,MessageConstants.MESSAGE_TYPE_NOTIFICATION,AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY,serviceCodeString,licenseeId,mesContext,maskParams);
                }
            }
        }
    }

    private String getServiceCodeString(List<String> serviceCodeList){
        StringBuilder serviceCodeString = new StringBuilder(10);
        for(String code : serviceCodeList){
            serviceCodeString.append(code);
            serviceCodeString.append('@');
        }
        return serviceCodeString.toString();
    }

    private void sendMessageHelper(String subject, String messageType, String srcSystemId,String serviceId, String licenseeId, String templateMessageByContent, HashMap<String, String> maskParams){
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

    private void sendNewApplicationPaymentGIROEmail(AppSubmissionDto appSubmissionDto,BaseProcessClass bpc) {
        String GIROAccountNumber = "xxxxxxxx";
        Double amount = appSubmissionDto.getAmount();
        String licenseeId = appSubmissionDto.getLicenseeId();
        List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request,AppServicesConsts.HCSASERVICEDTOLIST);
        List<String> serviceNames = IaisCommonUtils.genNewArrayList();
        String appGrpNo = appSubmissionDto.getAppGrpNo();
        String clientQueryCode = appSubmissionDto.getAppGrpId();
        for(HcsaServiceDto hcsaServiceDto : hcsaServiceDtos){
            String svcName = hcsaServiceDto.getSvcName();
            if(!StringUtil.isEmpty(svcName)){
                serviceNames.add(svcName);
            }
        }
        String appType = appSubmissionDto.getAppType();
        if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
            Map<String, Object> map = IaisCommonUtils.genNewHashMap();
            map.put("serviceNames", serviceNames);
            map.put("paymentAmount", Formatter.formatNumber(amount));
            map.put("GIROAccountNumber", GIROAccountNumber);
            map.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);

            String subject = appGrpNo;
            //send email
            String mesContext = sendEmailHelper(map, MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_PAYMENT_ID, subject, licenseeId, clientQueryCode);

            //send message
            HashMap<String, String> maskParams = IaisCommonUtils.genNewHashMap();
            List<String> serviceCodeList = IaisCommonUtils.genNewArrayList();
            MsgTemplateDto msgTemplateDto = appSubmissionService.getMsgTemplateById(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_PAYMENT_ID);
            subject = " " + msgTemplateDto.getTemplateName() + " " + appGrpNo;
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if(appSvcRelatedInfoDtoList != null){
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList){
                    String serviceId = appSvcRelatedInfoDto.getServiceId();
                    HcsaServiceDto serviceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
                    if(serviceDto != null){
                        serviceCodeList.add(serviceDto.getSvcCode());
                    }
                }
                String serviceCodeString = getServiceCodeString(serviceCodeList);
                if(!StringUtil.isEmpty(serviceCodeString)){
                    sendMessageHelper(subject,MessageConstants.MESSAGE_TYPE_NOTIFICATION,AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY,serviceCodeString,licenseeId,mesContext,maskParams);
                }
            }
        }
    }

    //send email helper
    private String sendEmailHelper(Map<String ,Object> tempMap,String msgTemplateId,String subject,String licenseeId,String clientQueryCode){
        MsgTemplateDto msgTemplateDto = appSubmissionService.getMsgTemplateById(msgTemplateId);
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
        appSubmissionService.feSendEmail(emailDto);

        return mesContext;
    }

    private void inspectionDateSendNewApplicationPaymentOnlineEmail(AppSubmissionDto appSubmissionDto,BaseProcessClass bpc) {
        MsgTemplateDto msgTemplateDto = appSubmissionService.getMsgTemplateById(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_PAYMENT_ONLINE_ID);
        if(msgTemplateDto != null) {
            Double amount = appSubmissionDto.getAmount();
            String licenseeId = appSubmissionDto.getLicenseeId();
            List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request,AppServicesConsts.HCSASERVICEDTOLIST);
            List<String> serviceNames = new ArrayList<String>();
            for(HcsaServiceDto hcsaServiceDto : hcsaServiceDtos){
                String svcName = hcsaServiceDto.getSvcName();
                if(!StringUtil.isEmpty(svcName)){
                    serviceNames.add(svcName);
                }
            }
            String appGrpNo = appSubmissionDto.getAppGrpNo();
            String subject = " " + msgTemplateDto.getTemplateName() + " " + appGrpNo;
            Map<String, Object> map = IaisCommonUtils.genNewHashMap();
            map.put("serviceNames", serviceNames);
            map.put("paymentAmount",Formatter.formatNumber(amount));
            map.put("MOH_AGENCY_NAME",AppConsts.MOH_AGENCY_NAME);
            String mesContext = null;
            try {
                mesContext = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), map);
            } catch (IOException | TemplateException e) {
                log.error(e.getMessage(),e);
            }
            EmailDto emailDto = new EmailDto();
            emailDto.setContent(mesContext);
            emailDto.setSubject(subject);
            emailDto.setSender(mailSender);
            emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
            if(appSubmissionDto.getAppGrpId()!=null){
                emailDto.setClientQueryCode(appSubmissionDto.getAppGrpId());
                //send email
                appSubmissionService.feSendEmail(emailDto);
            }

            //send message
            HashMap<String, String> maskParams = IaisCommonUtils.genNewHashMap();
            List<String> serviceCodeList = IaisCommonUtils.genNewArrayList();
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if(appSvcRelatedInfoDtoList != null){
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList){
                    String serviceId = appSvcRelatedInfoDto.getServiceId();
                    HcsaServiceDto serviceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
                    if(serviceDto != null){
                        serviceCodeList.add(serviceDto.getSvcCode());
                    }
                }
                String serviceCodeString = getServiceCodeString(serviceCodeList);
                if(!StringUtil.isEmpty(serviceCodeString)){
                    sendMessageHelper(subject,MessageConstants.MESSAGE_TYPE_NOTIFICATION,AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY,serviceCodeString,licenseeId,mesContext,maskParams);
                }
            }
        }
    }

    private Map<String,String> doComChange( AppSubmissionDto appSubmissionDto,AppSubmissionDto oldAppSubmissionDto){
        StringBuilder sB=new StringBuilder(10);
        Map<String,String> result=IaisCommonUtils.genNewHashMap();
        AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto();
        if(appEditSelectDto!=null){
            if(!appEditSelectDto.isPremisesEdit()){
                List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
                for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList){
                    appGrpPremisesDto.setLicenceDtos(null);
                }
                if(!appSubmissionDto.getAppGrpPremisesDtoList().equals(oldAppSubmissionDto.getAppGrpPremisesDtoList())){
                   log.info(StringUtil.changeForLog("appGrpPremisesDto"+JsonUtil.parseToJson(appSubmissionDto.getAppGrpPremisesDtoList())));
                    log.info(StringUtil.changeForLog("oldappGrpPremisesDto"+JsonUtil.parseToJson(oldAppSubmissionDto.getAppGrpPremisesDtoList())));
                    result.put("premiss","UC_CHKLMD001_ERR001");
                }
            }
            List<AppGrpPrimaryDocDto> dtoAppGrpPrimaryDocDtos = oldAppSubmissionDto.getAppGrpPrimaryDocDtos();
            if(dtoAppGrpPrimaryDocDtos!=null){
                for(AppGrpPrimaryDocDto appGrpPrimaryDocDto : dtoAppGrpPrimaryDocDtos){
                    appGrpPrimaryDocDto.setPassValidate(true);
                }
            }
            List<AppGrpPrimaryDocDto> dtoAppGrpPrimaryDocDtos1 = appSubmissionDto.getAppGrpPrimaryDocDtos();
            if(dtoAppGrpPrimaryDocDtos1!=null){
                for(AppGrpPrimaryDocDto appGrpPrimaryDocDto : dtoAppGrpPrimaryDocDtos1){
                    appGrpPrimaryDocDto.setPassValidate(true);
                }
            }
            if(!appEditSelectDto.isDocEdit()){
                if(!appSubmissionDto.getAppGrpPrimaryDocDtos().equals(oldAppSubmissionDto.getAppGrpPrimaryDocDtos())){
                    log.info(StringUtil.changeForLog("appGrpPrimaryDocDto"+JsonUtil.parseToJson(appSubmissionDto.getAppGrpPrimaryDocDtos())));
                    log.info(StringUtil.changeForLog("oldAppGrpPrimaryDocDto"+JsonUtil.parseToJson(oldAppSubmissionDto.getAppGrpPrimaryDocDtos())));
                    result.put("document","UC_CHKLMD001_ERR001");
                }
            }

            if(!appEditSelectDto.isServiceEdit()){
                if(!appSubmissionDto.getAppSvcRelatedInfoDtoList().equals(oldAppSubmissionDto.getAppSvcRelatedInfoDtoList())){
                    log.info(StringUtil.changeForLog("AppSvcRelatedInfoDtoList"+JsonUtil.parseToJson(appSubmissionDto.getAppSvcRelatedInfoDtoList())));
                    log.info(StringUtil.changeForLog("oldAppSvcRelatedInfoDtoList"+JsonUtil.parseToJson(oldAppSubmissionDto.getAppSvcRelatedInfoDtoList())));
                    result.put("serviceId","UC_CHKLMD001_ERR001");
                }
            }
        }
        return result;
    }

    private boolean compareAndSendEmail(AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto){
        boolean isAuto = true;

        AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto();
        if(appEditSelectDto != null ){
            if(appEditSelectDto.isPremisesEdit()){
                if(!appSubmissionDto.getAppGrpPremisesDtoList().equals(oldAppSubmissionDto.getAppGrpPremisesDtoList())){
                    isAuto =  compareHciName(appSubmissionDto.getAppGrpPremisesDtoList(), oldAppSubmissionDto.getAppGrpPremisesDtoList());
                    if(isAuto){
                        isAuto = compareLocation(appSubmissionDto.getAppGrpPremisesDtoList(), oldAppSubmissionDto.getAppGrpPremisesDtoList());
                    }
                    //send eamil

                }
            }

            if(appEditSelectDto.isMedAlertEdit()){
                //todo:
            }
            //one svc
            AppSvcRelatedInfoDto appSvcRelatedInfoDtoList = getAppSvcRelatedInfoDto(appSubmissionDto.getAppSvcRelatedInfoDtoList());
            AppSvcRelatedInfoDto oldAppSvcRelatedInfoDtoList = getAppSvcRelatedInfoDto(oldAppSubmissionDto.getAppSvcRelatedInfoDtoList());
            if(appEditSelectDto.isPoEdit() || appEditSelectDto.isDpoEdit() || appEditSelectDto.isServiceEdit()){
                //remove
                List<AppSvcPrincipalOfficersDto> newAppSvcPrincipalOfficersDto  = appSvcRelatedInfoDtoList.getAppSvcPrincipalOfficersDtoList();
                List<AppSvcPrincipalOfficersDto> oldAppSvcPrincipalOfficersDto = oldAppSvcRelatedInfoDtoList.getAppSvcPrincipalOfficersDtoList();
                if(newAppSvcPrincipalOfficersDto.size() > oldAppSvcPrincipalOfficersDto.size()){
                    isAuto = false;
                    //send eamil
                }
                //change
                List<String> newPoIdNos = IaisCommonUtils.genNewArrayList();
                List<String> oldPoIdNos = IaisCommonUtils.genNewArrayList();
                List<String> newDpoIdNos = IaisCommonUtils.genNewArrayList();
                List<String> olddDpoIdNos = IaisCommonUtils.genNewArrayList();

                for(AppSvcPrincipalOfficersDto item:newAppSvcPrincipalOfficersDto){
                    if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(item.getPsnType())){
                        newPoIdNos.add(item.getIdNo());
                    }else if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(item.getPsnType())){
                        newDpoIdNos.add(item.getIdNo());
                    }
                }
                for(AppSvcPrincipalOfficersDto item:oldAppSvcPrincipalOfficersDto){
                    if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(item.getPsnType())){
                        oldPoIdNos.add(item.getIdNo());
                    }else if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(item.getPsnType())){
                        olddDpoIdNos.add(item.getIdNo());
                    }
                }
                if(!newPoIdNos.equals(oldPoIdNos)){
                    isAuto = false;
                }
                if(!newDpoIdNos.equals(olddDpoIdNos)){
                    isAuto = false;
                }
            }

            if(appEditSelectDto.isServiceEdit()){
                List<AppSvcCgoDto> newAppSvcCgoDto =  appSvcRelatedInfoDtoList.getAppSvcCgoDtoList();
                List<AppSvcCgoDto> oldAppSvcCgoDto =  oldAppSvcRelatedInfoDtoList.getAppSvcCgoDtoList();
                List<String> newCgoIdNos = IaisCommonUtils.genNewArrayList();
                List<String> oldCgoIdNos = IaisCommonUtils.genNewArrayList();
                if(!IaisCommonUtils.isEmpty(newAppSvcCgoDto) && !IaisCommonUtils.isEmpty(oldAppSvcCgoDto)){
                    for(AppSvcCgoDto item:newAppSvcCgoDto){
                        newCgoIdNos.add(item.getIdNo());
                    }
                    for(AppSvcCgoDto item:oldAppSvcCgoDto){
                        oldCgoIdNos.add(item.getIdNo());
                    }
                    if(!newCgoIdNos.equals(oldCgoIdNos)){
                        isAuto = false;
                    }

                }

                List<AppSvcLaboratoryDisciplinesDto> newDisciplinesDto = appSvcRelatedInfoDtoList.getAppSvcLaboratoryDisciplinesDtoList();
                List<AppSvcLaboratoryDisciplinesDto> oldDisciplinesDto = oldAppSvcRelatedInfoDtoList.getAppSvcLaboratoryDisciplinesDtoList();

                if(!IaisCommonUtils.isEmpty(newDisciplinesDto) && !IaisCommonUtils.isEmpty(oldDisciplinesDto)){
                    for(AppSvcLaboratoryDisciplinesDto item:newDisciplinesDto){
                        String hciName = item.getPremiseVal();
                        AppSvcLaboratoryDisciplinesDto oldAppSvcLaboratoryDisciplinesDto = getDisciplinesDto(oldDisciplinesDto,hciName);
                        List<AppSvcChckListDto> newCheckList =  item.getAppSvcChckListDtoList();
                        List<AppSvcChckListDto> oldCheckList = oldAppSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                        if(!IaisCommonUtils.isEmpty(newCheckList) && !IaisCommonUtils.isEmpty(oldCheckList)){
                            List<String> newCheckListIds = IaisCommonUtils.genNewArrayList();
                            List<String> oldCheckListIds = IaisCommonUtils.genNewArrayList();
                            for(AppSvcChckListDto checBox:oldCheckList){
                                oldCheckListIds.add(checBox.getChkLstConfId());
                            }
                            for(AppSvcChckListDto checBox:newCheckList){
                                if(!oldCheckListIds.contains(checBox.getChkLstConfId())){
                                    isAuto = false;
                                    break;
                                }
                            }
                        }
                    }
                    if(!newDisciplinesDto.equals(oldDisciplinesDto)){

                    }

                }


            }

            if(appEditSelectDto.isDocEdit()){
            /*if(!appSubmissionDto.getAppGrpPrimaryDocDtos().equals(oldAppSubmissionDto.getAppGrpPrimaryDocDtos()) ||
                    !appSvcRelatedInfoDtoList.getAppSvcDocDtoLit().equals(oldAppSvcRelatedInfoDtoList.getAppSvcDocDtoLit())){
                //send eamil

            }*/

            }
        }

        return isAuto;
    }

    private AppSvcLaboratoryDisciplinesDto getDisciplinesDto(List<AppSvcLaboratoryDisciplinesDto> disciplinesDto,String hciName){
        for(AppSvcLaboratoryDisciplinesDto iten:disciplinesDto){
            if(hciName.equals(iten.getPremiseVal())){
                return iten;
            }
        }
        return new AppSvcLaboratoryDisciplinesDto();
    }

    private boolean compareHciName(List<AppGrpPremisesDto> appGrpPremisesDtos,List<AppGrpPremisesDto> oldAppGrpPremisesDtos){
        int length = appGrpPremisesDtos.size();
        int oldLength = oldAppGrpPremisesDtos.size();
        if(length == oldLength){
            for(int i=0; i<length; i++){
                AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtos.get(0);
                AppGrpPremisesDto oldAppGrpPremisesDto = oldAppGrpPremisesDtos.get(0);
                if(!getHciName(appGrpPremisesDto).equals(getHciName(oldAppGrpPremisesDto))){
                    return false;
                }
            }
        }
        //is same
        return true;
    }

    private boolean compareLocation(List<AppGrpPremisesDto> appGrpPremisesDtos,List<AppGrpPremisesDto> oldAppGrpPremisesDtos){
        int length = appGrpPremisesDtos.size();
        int oldLength = oldAppGrpPremisesDtos.size();
        if(length == oldLength){
            for(int i=0; i<length; i++){
                AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtos.get(0);
                AppGrpPremisesDto oldAppGrpPremisesDto = oldAppGrpPremisesDtos.get(0);
                if(!appGrpPremisesDto.getAddress().equals(oldAppGrpPremisesDto.getAddress())){
                    return false;
                }
            }
        }
        //is same
        return true;
    }

    private String getHciName(AppGrpPremisesDto appGrpPremisesDto){
        String hciName = "";
        if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){
            hciName = appGrpPremisesDto.getHciName();
        }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
            hciName = appGrpPremisesDto.getConveyanceVehicleNo();
        }
        return hciName;
    }

    private AmendmentFeeDto getAmendmentFeeDto(AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto){
        AmendmentFeeDto amendmentFeeDto = new AmendmentFeeDto();
        boolean changeHciName = compareHciName(appSubmissionDto.getAppGrpPremisesDtoList(), oldAppSubmissionDto.getAppGrpPremisesDtoList());
        boolean changeLocation = compareLocation(appSubmissionDto.getAppGrpPremisesDtoList(), oldAppSubmissionDto.getAppGrpPremisesDtoList());
        amendmentFeeDto.setChangeInLicensee(Boolean.FALSE);
        amendmentFeeDto.setChangeInHCIName(!changeHciName);
        amendmentFeeDto.setChangeInLocation(!changeLocation);
        return amendmentFeeDto;
    }

    private AppSvcRelatedInfoDto getAppSvcRelatedInfoDto(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos){
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
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
        chose(request,pageCon);

        return dto;
    }

    private void chose(HttpServletRequest request,String type){
        if("valPremiseList".equals(type)){
            List<AppGrpPremisesDto> list = genAppGrpPremisesDtoList(request);
            ParamUtil.setRequestAttr(request, "valPremiseList", list);
        }if("prinOffice".equals(type)){
            List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDto =
                    (List<AppSvcPrincipalOfficersDto>)ParamUtil.getSessionAttr(request, "AppSvcPrincipalOfficersDto");
            ParamUtil.setRequestAttr(request,"prinOffice",appSvcPrincipalOfficersDto);
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
    public static List<AppGrpPremisesDto> genAppGrpPremisesDtoList(HttpServletRequest request){
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request,APPSUBMISSIONDTO);
        boolean onlySpecifiedSvc = false;
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo()) || !StringUtil.isEmpty(appSvcRelatedInfoDto.getAlignLicenceNo())){
                    onlySpecifiedSvc = true;
                    break;
                }
            }
        }
        if(onlySpecifiedSvc){
            return appSubmissionDto.getAppGrpPremisesDtoList();
        }

        List<AppGrpPremisesDto> appGrpPremisesDtoList = IaisCommonUtils.genNewArrayList();
        Object requestInformationConfig = ParamUtil.getSessionAttr(request,REQUESTINFORMATIONCONFIG);
        int count = 0;
        String [] premisesType = ParamUtil.getStrings(request, "premType");
        String [] hciName = ParamUtil.getStrings(request, "onSiteHciName");
        if(premisesType != null){
            count = premisesType.length;
        }
        String [] premisesIndexNo = ParamUtil.getStrings(request,"premisesIndexNo");
        //onsite
        String [] premisesSelect = ParamUtil.getStrings(request, "onSiteSelect");
        String [] postalCode = ParamUtil.getStrings(request,  "onSitePostalCode");
        String [] blkNo = ParamUtil.getStrings(request, "onSiteBlkNo");
        String [] streetName = ParamUtil.getStrings(request, "onSiteStreetName");
        String [] floorNo = ParamUtil.getStrings(request, "onSiteFloorNo");
        String [] unitNo = ParamUtil.getStrings(request, "onSiteUnitNo");
        String [] buildingName = ParamUtil.getStrings(request, "onSiteBuildingName");
        String [] siteAddressType = ParamUtil.getStrings(request, "onSiteAddressType");
        String [] offTelNo= ParamUtil.getStrings(request,"onSiteOffTelNo");
        String [] scdfRefNo = ParamUtil.getStrings(request, "onSiteScdfRefNo");
        String [] onsiteStartHH = ParamUtil.getStrings(request, "onSiteStartHH");
        String [] onsiteStartMM = ParamUtil.getStrings(request, "onSiteStartMM");
        String [] onsiteEndHHS = ParamUtil.getStrings(request, "onSiteEndHH");
        String [] onsiteEndMMS = ParamUtil.getStrings(request, "onSiteEndMM");
        String [] fireSafetyCertIssuedDateStr  = ParamUtil.getStrings(request, "onSiteFireSafetyCertIssuedDate");
        String [] isOtherLic = ParamUtil.getStrings(request, "onSiteIsOtherLic");
        //conveyance
        String [] conPremisesSelect = ParamUtil.getStrings(request, "conveyanceSelect");
        String [] conVehicleNo = ParamUtil.getStrings(request, "conveyanceVehicleNo");
        String [] conPostalCode = ParamUtil.getStrings(request,  "conveyancePostalCode");
        String [] conBlkNo = ParamUtil.getStrings(request, "conveyanceBlkNo");
        String [] conStreetName = ParamUtil.getStrings(request, "conveyanceStreetName");
        String [] conFloorNo = ParamUtil.getStrings(request, "conveyanceFloorNo");
        String [] conUnitNo = ParamUtil.getStrings(request, "conveyanceUnitNo");
        String [] conBuildingName = ParamUtil.getStrings(request, "conveyanceBuildingName");
        String [] conSiteAddressType = ParamUtil.getStrings(request, "conveyanceAddrType");
        String [] conStartHH = ParamUtil.getStrings(request, "conveyanceStartHH");
        String [] conStartMM = ParamUtil.getStrings(request, "conveyanceStartMM");
        String [] conEndHHS = ParamUtil.getStrings(request, "conveyanceEndHH");
        String [] conEndMMS = ParamUtil.getStrings(request, "conveyanceEndMM");
        //offSite
        String [] offSitePremisesSelect = ParamUtil.getStrings(request, "offSiteSelect");
        String [] offSitePostalCode = ParamUtil.getStrings(request,  "offSitePostalCode");
        String [] offSiteBlkNo = ParamUtil.getStrings(request, "offSiteBlkNo");
        String [] offSiteStreetName = ParamUtil.getStrings(request, "offSiteStreetName");
        String [] offSiteFloorNo = ParamUtil.getStrings(request, "offSiteFloorNo");
        String [] offSiteUnitNo = ParamUtil.getStrings(request, "offSiteUnitNo");
        String [] offSiteBuildingName = ParamUtil.getStrings(request, "offSiteBuildingName");
        String [] offSiteSiteAddressType = ParamUtil.getStrings(request, "offSiteAddrType");
        String [] offSiteStartHH = ParamUtil.getStrings(request, "offSiteStartHH");
        String [] offSiteStartMM = ParamUtil.getStrings(request, "offSiteStartMM");
        String [] offSiteEndHHS = ParamUtil.getStrings(request, "offSiteEndHH");
        String [] offSiteEndMMS = ParamUtil.getStrings(request, "offSiteEndMM");

        List<SelectOption> publicHolidayDtos = (List<SelectOption>) ParamUtil.getSessionAttr(request,"publicHolidaySelect");
        //every prem's ph length
        String [] phLength = ParamUtil.getStrings(request,"phLength");
        String [] premValue = ParamUtil.getStrings(request, "premValue");
        String [] isParyEdit = ParamUtil.getStrings(request,"isPartEdit");
        String [] chooseExistData = ParamUtil.getStrings(request,"chooseExistData");
        Map<String,AppGrpPremisesDto> licAppGrpPremisesDtoMap = (Map<String, AppGrpPremisesDto>) ParamUtil.getSessionAttr(request, LICAPPGRPPREMISESDTOMAP);
        for(int i =0 ; i<count;i++){
            AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
            String premisesSel = "";
            if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesType[i])){
                premisesSel = premisesSelect[i];
            }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesType[i])){
                premisesSel = conPremisesSelect[i];
            }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesType[i])){
                premisesSel = offSitePremisesSelect[i];
            }
            String premIndexNo = "";
            try {
                premIndexNo = premisesIndexNo[i];
            }catch (Exception e){
                log.error(e.getMessage(), e);
            }
            String appType = appSubmissionDto.getAppType();
            boolean newApp =  requestInformationConfig ==null&&!ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) && !ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType) ;
            if(newApp){
                if(!StringUtil.isEmpty(premisesSel) && !premisesSel.equals("-1") && !premisesSel.equals(ApplicationConsts.NEW_PREMISES)){
                    appGrpPremisesDto = licAppGrpPremisesDtoMap.get(premisesSel);
                    if(appGrpPremisesDto != null){
                        //get value for jsp page
                        if(StringUtil.isEmpty(premisesIndexNo[i])){
                            appGrpPremisesDto.setPremisesIndexNo(UUID.randomUUID().toString());
                        }else{
                            appGrpPremisesDto.setPremisesIndexNo(premisesIndexNo[i]);
                        }
                        appGrpPremisesDtoList.add(appGrpPremisesDto);
                    }
                    continue;
                }
            }else if(!StringUtil.isEmpty(isParyEdit[i])){
                if(!AppConsts.YES.equals(isParyEdit[i])){
                    List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
                    for(AppGrpPremisesDto prem:appGrpPremisesDtos){
                        if(prem.getPremisesIndexNo().equals(premIndexNo)){
                            appGrpPremisesDto = prem;
                            break;
                        }
                    }
                    appGrpPremisesDtoList.add(appGrpPremisesDto);
                    continue;
                }
                if(AppConsts.YES.equals(chooseExistData[i])){
                    appGrpPremisesDto = licAppGrpPremisesDtoMap.get(premisesSel);
                    //get value for jsp page
                    if(StringUtil.isEmpty(premisesIndexNo[i])){
                        appGrpPremisesDto.setPremisesIndexNo(UUID.randomUUID().toString());
                    }else{
                        appGrpPremisesDto.setPremisesIndexNo(premisesIndexNo[i]);
                    }
                    appGrpPremisesDtoList.add(appGrpPremisesDto);
                    continue;
                }
                //set hciCode
                List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
                for(AppGrpPremisesDto premDto:appGrpPremisesDtos){
                    if(!StringUtil.isEmpty(premisesIndexNo[i]) && premisesIndexNo[i].equals(premDto.getPremisesIndexNo())){
                        appGrpPremisesDto.setHciCode(premDto.getHciCode());
                        break;
                    }
                }
            }

            //get value for session , this is the subtype's checkbox
            if(StringUtil.isEmpty(premisesIndexNo[i])){
                appGrpPremisesDto.setPremisesIndexNo(UUID.randomUUID().toString());
            }else {
                appGrpPremisesDto.setPremisesIndexNo(premisesIndexNo[i]);
            }

            appGrpPremisesDto.setPremisesType(premisesType[i]);
            List<AppPremPhOpenPeriodDto> appPremPhOpenPeriods = IaisCommonUtils.genNewArrayList();
            int length = 0;
            try {
                length = Integer.parseInt(phLength[i]);
            }catch (Exception e){
                log.error(StringUtil.changeForLog("length can not parse to int"));
            }
            if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesType[i])){
                appGrpPremisesDto.setOnsiteStartHH(onsiteStartHH[i]);
                appGrpPremisesDto.setOnsiteStartMM(onsiteStartMM[i]);
                appGrpPremisesDto.setOnsiteEndHH(onsiteEndHHS[i]);
                appGrpPremisesDto.setOnsiteEndMM(onsiteEndMMS[i]);
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
                Date fireSafetyCertIssuedDateDate = DateUtil.parseDate(fireSafetyCertIssuedDateStr[i], Formatter.DATE);
                appGrpPremisesDto.setCertIssuedDt(fireSafetyCertIssuedDateDate);
                String certIssuedDtStr = Formatter.formatDate(fireSafetyCertIssuedDateDate);
                appGrpPremisesDto.setCertIssuedDtStr(certIssuedDtStr);
                if(AppConsts.YES.equals(isOtherLic[i])){
                    appGrpPremisesDto.setLocateWithOthers(AppConsts.YES);
                }else if(AppConsts.NO.equals(isOtherLic[i])){
                    appGrpPremisesDto.setLocateWithOthers(AppConsts.NO);
                }
                for(int j =0; j<length; j++){
                    AppPremPhOpenPeriodDto appPremPhOpenPeriod = new AppPremPhOpenPeriodDto();
                    String onsitePubHolidayName = premValue[i]+"onSitePubHoliday"+j;
                    String onsitePbHolDayStartHHName = premValue[i]+"onSitePbHolDayStartHH"+j;
                    String onsitePbHolDayStartMMName = premValue[i]+"onSitePbHolDayStartMM"+j;
                    String onsitePbHolDayEndHHName = premValue[i]+"onSitePbHolDayEndHH"+j;
                    String onsitePbHolDayEndMMName = premValue[i]+"onSitePbHolDayEndMM"+j;

                    String onsitePubHoliday = ParamUtil.getDate(request, onsitePubHolidayName);
                    String onsitePbHolDayStartHH = ParamUtil.getString(request, onsitePbHolDayStartHHName);
                    String onsitePbHolDayStartMM = ParamUtil.getString(request, onsitePbHolDayStartMMName);
                    String onsitePbHolDayEndHH = ParamUtil.getString(request, onsitePbHolDayEndHHName);
                    String onsitePbHolDayEndMM = ParamUtil.getString(request, onsitePbHolDayEndMMName);
                    appPremPhOpenPeriod.setPhDateStr(onsitePubHoliday);
                    Date phDate = DateUtil.parseDate(onsitePubHoliday, Formatter.DATE);
                    appPremPhOpenPeriod.setPhDate(phDate);
                    appPremPhOpenPeriod.setOnsiteStartFromHH(onsitePbHolDayStartHH);
                    appPremPhOpenPeriod.setOnsiteStartFromMM(onsitePbHolDayStartMM);
                    appPremPhOpenPeriod.setOnsiteEndToHH(onsitePbHolDayEndHH);
                    appPremPhOpenPeriod.setOnsiteEndToMM(onsitePbHolDayEndMM);
                    String phName = NewApplicationHelper.getPhName(publicHolidayDtos,onsitePubHoliday);
                    appPremPhOpenPeriod.setDayName(phName);
                    if(!StringUtil.isEmpty(onsitePubHoliday)||!StringUtil.isEmpty(onsitePbHolDayStartHH) || !StringUtil.isEmpty(onsitePbHolDayStartMM)
                            ||!StringUtil.isEmpty(onsitePbHolDayEndHH) ||!StringUtil.isEmpty(onsitePbHolDayEndMM)){
                        appPremPhOpenPeriods.add(appPremPhOpenPeriod);
                    }
                }

            }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesType[i])){
                appGrpPremisesDto.setConStartHH(conStartHH[i]);
                appGrpPremisesDto.setConStartMM(conStartMM[i]);
                appGrpPremisesDto.setConEndHH(conEndHHS[i]);
                appGrpPremisesDto.setConEndMM(conEndMMS[i]);
                appGrpPremisesDto.setPremisesSelect(conPremisesSelect[i]);
                appGrpPremisesDto.setConveyanceVehicleNo(conVehicleNo[i]);
                appGrpPremisesDto.setConveyancePostalCode(conPostalCode[i]);
                appGrpPremisesDto.setConveyanceBlockNo(conBlkNo[i]);
                appGrpPremisesDto.setConveyanceStreetName(conStreetName[i]);
                appGrpPremisesDto.setConveyanceFloorNo(conFloorNo[i]);
                appGrpPremisesDto.setConveyanceUnitNo(conUnitNo[i]);
                appGrpPremisesDto.setConveyanceBuildingName(conBuildingName[i]);
                appGrpPremisesDto.setConveyanceAddressType(conSiteAddressType[i]);
                for(int j =0; j<length; j++){
                    AppPremPhOpenPeriodDto appPremPhOpenPeriod = new AppPremPhOpenPeriodDto();
                    String convPubHolidayName = premValue[i]+"conveyancePubHoliday"+j;
                    String convPbHolDayStartHHName = premValue[i]+"conveyancePbHolDayStartHH"+j;
                    String convPbHolDayStartMMName = premValue[i]+"conveyancePbHolDayStartMM"+j;
                    String convPbHolDayEndHHName = premValue[i]+"conveyancePbHolDayEndHH"+j;
                    String convPbHolDayEndMMName = premValue[i]+"conveyancePbHolDayEndMM"+j;
                    String convPubHoliday = ParamUtil.getDate(request, convPubHolidayName);
                    String convPbHolDayStartHH = ParamUtil.getString(request, convPbHolDayStartHHName);
                    String convPbHolDayStartMM = ParamUtil.getString(request, convPbHolDayStartMMName);
                    String convPbHolDayEndHH = ParamUtil.getString(request, convPbHolDayEndHHName);
                    String convPbHolDayEndMM = ParamUtil.getString(request, convPbHolDayEndMMName);
                    appPremPhOpenPeriod.setPhDateStr(convPubHoliday);
                    Date phDate = DateUtil.parseDate(convPubHoliday, Formatter.DATE);
                    appPremPhOpenPeriod.setPhDate(phDate);
                    appPremPhOpenPeriod.setConvStartFromHH(convPbHolDayStartHH);
                    appPremPhOpenPeriod.setConvStartFromMM(convPbHolDayStartMM);
                    appPremPhOpenPeriod.setConvEndToHH(convPbHolDayEndHH);
                    appPremPhOpenPeriod.setConvEndToMM(convPbHolDayEndMM);
                    String phName = NewApplicationHelper.getPhName(publicHolidayDtos,convPubHoliday);
                    appPremPhOpenPeriod.setDayName(phName);
                    if(!StringUtil.isEmpty(convPubHoliday)||!StringUtil.isEmpty(convPbHolDayStartHH) || !StringUtil.isEmpty(convPbHolDayStartMM)
                            ||!StringUtil.isEmpty(convPbHolDayEndHH) ||!StringUtil.isEmpty(convPbHolDayEndMM)){
                        appPremPhOpenPeriods.add(appPremPhOpenPeriod);
                    }
                }
            }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesType[i])){
                appGrpPremisesDto.setPremisesSelect(offSitePremisesSelect[i]);
                appGrpPremisesDto.setOffSitePostalCode(offSitePostalCode[i]);
                appGrpPremisesDto.setOffSiteBlockNo(offSiteBlkNo[i]);
                appGrpPremisesDto.setOffSiteStreetName(offSiteStreetName[i]);
                appGrpPremisesDto.setOffSiteFloorNo(offSiteFloorNo[i]);
                appGrpPremisesDto.setOffSiteUnitNo(offSiteUnitNo[i]);
                appGrpPremisesDto.setOffSiteBuildingName(offSiteBuildingName[i]);
                appGrpPremisesDto.setOffSiteAddressType(offSiteSiteAddressType[i]);
                appGrpPremisesDto.setOffSiteStartHH(offSiteStartHH[i]);
                appGrpPremisesDto.setOffSiteStartMM(offSiteStartMM[i]);
                appGrpPremisesDto.setOffSiteEndHH(offSiteEndHHS[i]);
                appGrpPremisesDto.setOffSiteEndMM(offSiteEndMMS[i]);
                for(int j =0; j<length; j++){
                    AppPremPhOpenPeriodDto appPremPhOpenPeriod = new AppPremPhOpenPeriodDto();
                    String offSitePubHolidayName = premValue[i]+"offSitePubHoliday"+j;
                    String offSitePbHolDayStartHHName = premValue[i]+"offSitePbHolDayStartHH"+j;
                    String offSitePbHolDayStartMMName = premValue[i]+"offSitePbHolDayStartMM"+j;
                    String offSitePbHolDayEndHHName = premValue[i]+"offSitePbHolDayEndHH"+j;
                    String offSitePbHolDayEndMMName = premValue[i]+"offSitePbHolDayEndMM"+j;

                    String offSitePubHoliday = ParamUtil.getDate(request, offSitePubHolidayName);
                    String offSitePbHolDayStartHH = ParamUtil.getString(request, offSitePbHolDayStartHHName);
                    String offSitePbHolDayStartMM = ParamUtil.getString(request, offSitePbHolDayStartMMName);
                    String offSitePbHolDayEndHH = ParamUtil.getString(request, offSitePbHolDayEndHHName);
                    String offSitePbHolDayEndMM = ParamUtil.getString(request, offSitePbHolDayEndMMName);
                    appPremPhOpenPeriod.setPhDateStr(offSitePubHoliday);
                    Date phDate = DateUtil.parseDate(offSitePubHoliday, Formatter.DATE);
                    appPremPhOpenPeriod.setPhDate(phDate);
                    appPremPhOpenPeriod.setOffSiteStartFromHH(offSitePbHolDayStartHH);
                    appPremPhOpenPeriod.setOffSiteStartFromMM(offSitePbHolDayStartMM);
                    appPremPhOpenPeriod.setOffSiteEndToHH(offSitePbHolDayEndHH);
                    appPremPhOpenPeriod.setOffSiteEndToMM(offSitePbHolDayEndMM);
                    String phName = NewApplicationHelper.getPhName(publicHolidayDtos,offSitePubHoliday);
                    appPremPhOpenPeriod.setDayName(phName);
                    if(!StringUtil.isEmpty(offSitePubHoliday)||!StringUtil.isEmpty(offSitePbHolDayStartHH) || !StringUtil.isEmpty(offSitePbHolDayStartMM)
                            ||!StringUtil.isEmpty(offSitePbHolDayEndHH) ||!StringUtil.isEmpty(offSitePbHolDayEndMM)){
                        appPremPhOpenPeriods.add(appPremPhOpenPeriod);
                    }
                }
            }
            appGrpPremisesDto.setAppPremPhOpenPeriodList(appPremPhOpenPeriods);
            appGrpPremisesDtoList.add(appGrpPremisesDto);
        }
        if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType()) ||
                ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())){
            //set premises edit status
            NewApplicationHelper.setPremEditStatus(appGrpPremisesDtoList,getAppGrpPremisesDtos(appSubmissionDto.getOldAppSubmissionDto()));
        }
        return  appGrpPremisesDtoList;
    }

    public Map<String,String> doPreviewAndSumbit( BaseProcessClass bpc){
        StringBuilder sB=new StringBuilder(10);
        Map<String,String> previewAndSubmitMap=IaisCommonUtils.genNewHashMap();
        //
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,OLDAPPSUBMISSIONDTO);
        HashMap<String,String> coMap=(HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
        List<String> premisesHciList = (List<String>) ParamUtil.getSessionAttr(bpc.request,NewApplicationConstant.PREMISES_HCI_LIST);
        String keyWord = MasterCodeUtil.getCodeDesc("MS001");
        boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
        Map<String, String> premissMap = requestForChangeService.doValidatePremiss(appSubmissionDto,oldAppSubmissionDto,premisesHciList,keyWord,isRfi);
        premissMap.remove("hciNameUsed");
        if(!premissMap.isEmpty()){
            previewAndSubmitMap.put("premiss","UC_CHKLMD001_ERR001");
            String premissMapStr = JsonUtil.parseToJson(premissMap);
            log.info(StringUtil.changeForLog("premissMap json str:"+premissMapStr));
            coMap.put("premises","");
        }else {
            coMap.put("premises","premises");
        }
        //
        Map<String,AppSvcPrincipalOfficersDto> licPersonMap = (Map<String, AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.LICPERSONSELECTMAP);
        //
        Map<String, List<HcsaSvcPersonnelDto>> allSvcAllPsnConfig = getAllSvcAllPsnConfig(bpc.request);
        List<AppSvcRelatedInfoDto> dto = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        ServiceStepDto serviceStepDto = new ServiceStepDto();
        for(int i=0;i< dto.size();i++ ){
            String serviceId = dto.get(i).getServiceId();
            List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = serviceConfigService.getHcsaServiceStepSchemesByServiceId(serviceId);
            serviceStepDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemeDtos);
            List<HcsaSvcPersonnelDto>  currentSvcAllPsnConfig= serviceConfigService.getSvcAllPsnConfig(hcsaServiceStepSchemeDtos, serviceId);
            Map<String, String> map = doCheckBox(bpc,sB,allSvcAllPsnConfig,currentSvcAllPsnConfig, dto.get(i));
            if(!map.isEmpty()){
                previewAndSubmitMap.putAll(map);
                String mapStr = JsonUtil.parseToJson(map);
                log.info(StringUtil.changeForLog("map json str:"+mapStr));
            }
        }
        Map<String,String> documentMap=IaisCommonUtils.genNewHashMap();
        documentValid(bpc.request,documentMap);
        doCommomDocument(bpc.request,documentMap);
        if(!documentMap.isEmpty()){
            previewAndSubmitMap.put("document","UC_CHKLMD001_ERR001");
            String documentMapStr = JsonUtil.parseToJson(documentMap);
            log.info(StringUtil.changeForLog("documentMap json str:"+documentMapStr));
            coMap.put("document","");
        }else {
            coMap.put("document","document");
        }
        if(!StringUtil.isEmpty(sB.toString())){
            previewAndSubmitMap.put("serviceId",sB.toString());
            coMap.put("information","");
        }else {
            coMap.put("information","information");
        }
        bpc.request.getSession().setAttribute("coMap",coMap);
        return previewAndSubmitMap;
    }

    private void doCommomDocument(HttpServletRequest request, Map<String, String> documentMap) {
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);

        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList  = appSubmissionDto.getAppGrpPrimaryDocDtos();
        List<HcsaSvcDocConfigDto> commonHcsaSvcDocConfigList = (List<HcsaSvcDocConfigDto>)   request.getSession().getAttribute(COMMONHCSASVCDOCCONFIGDTO);
        if(commonHcsaSvcDocConfigList==null){
            List<HcsaSvcDocConfigDto> hcsaSvcDocDtos = serviceConfigService.getAllHcsaSvcDocs(null);
            if (hcsaSvcDocDtos != null) {
                List<HcsaSvcDocConfigDto> commonHcsaSvcDocConfigDto = IaisCommonUtils.genNewArrayList();
                for (HcsaSvcDocConfigDto hcsaSvcDocConfigDto : hcsaSvcDocDtos) {
                    if ("0".equals(hcsaSvcDocConfigDto.getDupForPrem())) {
                        commonHcsaSvcDocConfigDto.add(hcsaSvcDocConfigDto);
                    }
                }
                commonHcsaSvcDocConfigList=commonHcsaSvcDocConfigDto;
            }else {
                return;
            }
        }
        for(HcsaSvcDocConfigDto comm : commonHcsaSvcDocConfigList){
            String name = "common"+comm.getId();

            Boolean isMandatory = comm.getIsMandatory();
            if(isMandatory&&appGrpPrimaryDocDtoList==null||isMandatory&&appGrpPrimaryDocDtoList.isEmpty()){
                documentMap.put(name, MessageUtil.replaceMessage("GENERAL_ERR0006","Document","field"));
            }else if(isMandatory&&!appGrpPrimaryDocDtoList.isEmpty()){
                Boolean flag=Boolean.FALSE;
                for(AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtoList){
                    String svcComDocId = appGrpPrimaryDocDto.getSvcComDocId();
                    if(comm.getId().equals(svcComDocId)){
                        flag=Boolean.TRUE;
                        break;
                    }
                }
                if(!flag){
                    documentMap.put(name, MessageUtil.replaceMessage("GENERAL_ERR0006","Document","field"));
                }
            }

        }



    }

    //todo

    public static Map<String,String> doCheckBox( BaseProcessClass bpc,StringBuilder sB,Map<String, List<HcsaSvcPersonnelDto>> allSvcAllPsnConfig, List<HcsaSvcPersonnelDto>  currentSvcAllPsnConfig, AppSvcRelatedInfoDto dto){
        String serviceId = dto.getServiceId();
        Map<String,String> errorMap=IaisCommonUtils.genNewHashMap();
        for(HcsaSvcPersonnelDto hcsaSvcPersonnelDto :currentSvcAllPsnConfig){
            String psnType = hcsaSvcPersonnelDto.getPsnType();
            int mandatoryCount = hcsaSvcPersonnelDto.getMandatoryCount();
            if("PO".equals(psnType)){
                List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = dto.getAppSvcPrincipalOfficersDtoList();
                if(appSvcPrincipalOfficersDtoList==null){
                    if(mandatoryCount>0) {
                        errorMap.put("error", "PO");
                        sB.append(serviceId);
                        log.info("PO null");
                    }
                }else if(appSvcPrincipalOfficersDtoList.size()<mandatoryCount){
                    errorMap.put("error", "PO");
                    sB.append(serviceId);
                    log.info("PO mandatoryCount");
                }
            }else if("SVCPSN".equals(psnType)){
                List<AppSvcPersonnelDto> appSvcPersonnelDtoList = dto.getAppSvcPersonnelDtoList();
                if(appSvcPersonnelDtoList==null){
                    if(mandatoryCount>0) {
                        errorMap.put("error", "SVCPSN");
                        sB.append(serviceId);
                        log.info("SVCPSN null");
                    }
                }else if(appSvcPersonnelDtoList.size()<mandatoryCount){
                    errorMap.put("error", "SVCPSN");
                    sB.append(serviceId);
                    log.info("SVCPSN mandatoryCount");
                }
            }else if("CGO".equals(psnType)){
                List<AppSvcCgoDto> appSvcCgoDtoList = dto.getAppSvcCgoDtoList();
                if(appSvcCgoDtoList==null){
                    if(mandatoryCount>0) {
                        errorMap.put("error", "CGO");
                        sB.append(serviceId);
                        log.info("CGO null");
                    }
                }else if(appSvcCgoDtoList.size()<mandatoryCount){
                    errorMap.put("error", "CGO");
                    sB.append(serviceId);
                    log.info("CGO mandatoryCount");
                }
            }else if("MAP".equals(psnType)){
                List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = dto.getAppSvcMedAlertPersonList();
                if(appSvcMedAlertPersonList==null){
                    if(mandatoryCount>0) {
                        errorMap.put("error", "MAP");
                        sB.append(serviceId);
                        log.info("MAP null");
                    }
                }else if(appSvcMedAlertPersonList.size()<mandatoryCount){
                    errorMap.put("error", "MAP");
                    sB.append(serviceId);
                    log.info("MAP mandatoryCount");
                }
            }
        }
        List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = dto.getAppSvcMedAlertPersonList();
        Map<String,AppSvcPersonAndExtDto> licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.LICPERSONSELECTMAP);
        Map<String, String> map = NewApplicationHelper.doValidateMedAlertPsn(appSvcMedAlertPersonList,licPersonMap,dto.getServiceCode());
        log.info(JsonUtil.parseToJson(map));
        if(!map.isEmpty()){
            sB.append(serviceId);
            errorMap.put("Medaler","error");
        }
        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = dto.getAppSvcLaboratoryDisciplinesDtoList();
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = allSvcAllPsnConfig.get(serviceId);
        dolabory(errorMap,appSvcLaboratoryDisciplinesDtoList,serviceId,sB);
        log.info(sB.toString());
        List<AppSvcCgoDto> appSvcCgoDtoList = dto.getAppSvcCgoDtoList();
        doAppSvcCgoDto(hcsaSvcPersonnelDtos,errorMap,appSvcCgoDtoList,serviceId,sB);
        log.info(sB.toString());
        List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList = dto.getAppSvcDisciplineAllocationDtoList();
        doSvcDis(errorMap,appSvcDisciplineAllocationDtoList,serviceId,sB);
        log.info(StringUtil.changeForLog(JsonUtil.parseToJson(errorMap)+"doSvcDis"));
        doSvcDisdolabory(errorMap,appSvcDisciplineAllocationDtoList,appSvcLaboratoryDisciplinesDtoList,serviceId,sB);
        log.info(StringUtil.changeForLog(JsonUtil.parseToJson(errorMap)+"doSvcDisdolabory"));
        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = dto.getAppSvcPrincipalOfficersDtoList();
        Map<String, String> govenMap = NewApplicationHelper.doValidateGovernanceOfficers(dto.getAppSvcCgoDtoList(),licPersonMap,dto.getServiceCode());
        log.info(StringUtil.changeForLog(JsonUtil.parseToJson(govenMap)));
        if(!govenMap.isEmpty()){
            sB.append(serviceId);
            log.info("govenMap is error");
        }
        doPO(hcsaSvcPersonnelDtos,errorMap,appSvcPrincipalOfficersDtoList,serviceId,sB);
        log.info(sB.toString());
        List<AppSvcPersonnelDto> appSvcPersonnelDtoList = dto.getAppSvcPersonnelDtoList();
        doAppSvcPersonnelDtoList(hcsaSvcPersonnelDtos,errorMap,appSvcPersonnelDtoList,serviceId,sB);
        log.info(sB.toString());
        List<AppSvcDocDto> appSvcDocDtoLit = dto.getAppSvcDocDtoLit();
        doSvcDocument(errorMap,appSvcDocDtoLit,serviceId,sB);
        log.info(sB.toString());

        log.info(StringUtil.changeForLog(JsonUtil.parseToJson(errorMap)));

        return  errorMap;
    }

    private static void doSvcDocument(Map<String,String> map,   List<AppSvcDocDto> appSvcDocDtoLit,String serviceId,StringBuilder sB ){
        if(appSvcDocDtoLit!=null){
            for (AppSvcDocDto appSvcDocDto:appSvcDocDtoLit){
                Integer docSize = appSvcDocDto.getDocSize();
                String docName = appSvcDocDto.getDocName();
                Boolean flag=Boolean.FALSE;
                if(docSize>4*1024*1024){
                    flag=Boolean.TRUE;
                }
                String substring = docName.substring(docName.lastIndexOf('.') + 1);
                FileType[] fileType = FileType.values();
                for(FileType f:fileType){
                    if(f.name().equalsIgnoreCase(substring)){
                        flag=Boolean.TRUE;
                    }
                }

                if(!flag){
                       sB.append(serviceId);
                }
            }


        }


    }

    private static void dolabory(Map<String,String> map ,List<AppSvcLaboratoryDisciplinesDto> list,String serviceId, StringBuilder sB){
        if(list!=null&&list.isEmpty()){

        }
    }

    private static void doAppSvcPersonnelDtoList(List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos ,Map map,List<AppSvcPersonnelDto> appSvcPersonnelDtos,String serviceId, StringBuilder sB){
        if(appSvcPersonnelDtos==null){
            if(hcsaSvcPersonnelDtos!=null){
                for(HcsaSvcPersonnelDto every:hcsaSvcPersonnelDtos){
                    String psnType = every.getPsnType();
                    if(ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL.equals(psnType)){
                        sB.append(serviceId);
                        return;
                    }
                }
            }

            return;
        }


        boolean flag =false;
        for(int i=0;i<appSvcPersonnelDtos.size();i++){
            String personnelSel = appSvcPersonnelDtos.get(i).getPersonnelType();
            if(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE.equals(personnelSel)){
                String profRegNo = appSvcPersonnelDtos.get(i).getProfRegNo();
                String name = appSvcPersonnelDtos.get(i).getName();
                if(StringUtil.isEmpty(name)){
                    map.put("name"+i,"UC_CHKLMD001_ERR001");
                    flag=true;
                }
                if(StringUtil.isEmpty(profRegNo)){
                    map.put("regnNo"+i,"UC_CHKLMD001_ERR001");
                    flag=true;
                }
            }
            if(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL.equals(personnelSel)){
                String name = appSvcPersonnelDtos.get(i).getName();
                String designation = appSvcPersonnelDtos.get(i).getDesignation();
                String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                String qualification = appSvcPersonnelDtos.get(i).getQualification();

                if(StringUtil.isEmpty(name)){
                    map.put("name"+i,"UC_CHKLMD001_ERR001");
                    flag=true;
                }
                if(StringUtil.isEmpty(designation)){
                    map.put("designation"+i,"UC_CHKLMD001_ERR001");
                    flag=true;
                }
                if(StringUtil.isEmpty(wrkExpYear)){
                    map.put("wrkExpYear"+i,"UC_CHKLMD001_ERR001");
                    flag=true;
                }else {
                    if(!wrkExpYear.matches("^[0-9]*$")){
                        map.put("wrkExpYear"+i,"CHKLMD001_ERR003");
                        flag=true;
                    }
                }
                if(StringUtil.isEmpty(qualification)){
                    map.put("qualification"+i,"UC_CHKLMD001_ERR001");
                    flag=true;
                }
            }

            if(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST.equals(personnelSel)){
                String name = appSvcPersonnelDtos.get(i).getName();
                String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                String quaification = appSvcPersonnelDtos.get(i).getQualification();
                if(StringUtil.isEmpty(name)){
                    map.put("name"+i,"UC_CHKLMD001_ERR001");
                    flag=true;
                }
                if(StringUtil.isEmpty(wrkExpYear)){
                    map.put("wrkExpYear"+i,"UC_CHKLMD001_ERR001");
                    flag=true;
                }
                else {
                    if(!wrkExpYear.matches("^[0-9]*$")){
                        map.put("wrkExpYear"+i,"CHKLMD001_ERR003");
                        flag=true;
                    }
                }
                if(StringUtil.isEmpty(quaification)){
                    map.put("quaification"+i,"UC_CHKLMD001_ERR001");
                    flag=true;
                }
            }
            if(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER.equals(personnelSel)){
                String name = appSvcPersonnelDtos.get(i).getName();
                if(StringUtil.isEmpty(name)){
                    map.put("name"+i,"UC_CHKLMD001_ERR001");
                    flag=true;
                }
            }
            if(flag){
                sB.append(serviceId);
            }
        }

    }

    private static void doAppSvcCgoDto(  List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos, Map map ,List<AppSvcCgoDto> list,String serviceId,StringBuilder sB){
        if(list==null){
            if(hcsaSvcPersonnelDtos!=null){
                for(HcsaSvcPersonnelDto every:hcsaSvcPersonnelDtos){
                    String psnType = every.getPsnType();
                    if(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnType)){
                        log.info("PERSONNEL_PSN_TYPE_CGO null");
                        sB.append(serviceId);
                        return;
                    }
                }
            }

            return;
        }


        boolean flag =false;
        for(int i=0;i<list.size();i++ ){
            String assignSelect = list.get(i).getAssignSelect();
            if("".equals(assignSelect)||assignSelect==null){
                map.put("cgoassignSelect"+i,"UC_CHKLMD001_ERR001");
                flag =true;
            }
            String idType = list.get(i).getIdType();
            if(StringUtil.isEmpty(idType)){
                map.put("cgotype"+i,"UC_CHKLMD001_ERR001");
                flag =true;
            }
            String mobileNo = list.get(i).getMobileNo();
            if(StringUtil.isEmpty(mobileNo)){
                map.put("cgomobileNo"+i,"UC_CHKLMD001_ERR001");
                flag =true;
            }else {
                if(!mobileNo.matches("^[8|9][0-9]{7}$")){
                    map.put("cgomobileNo"+i,"CHKLMD001_ERR004");
                    flag =true;
                }
            }
            String emailAddr = list.get(i).getEmailAddr();
            if(StringUtil.isEmpty(emailAddr)){
                map.put("cgoemailAddr"+i,"UC_CHKLMD001_ERR001");
                flag =true;
            }else {
                if(!ValidationUtils.isEmail(emailAddr)){
                    map.put("cgoemailAddr"+i,"CHKLMD001_ERR006");
                    flag =true;
                }
            }
            if( flag ){
                sB.append(serviceId);

            }

        }
    }

    private static void doSvcDis( Map map ,List<AppSvcDisciplineAllocationDto> list,String serviceId,StringBuilder sB){
        if(list==null){
            return;
        }else {
            for(AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto : list){
                String idNo = appSvcDisciplineAllocationDto.getIdNo();
                if(StringUtil.isEmpty(idNo)){
                    map.put("idNo","idNo empty");
                    sB.append(serviceId);
                    return;
                }
            }
        }
    }

    private static void doSvcDisdolabory(Map map ,List<AppSvcDisciplineAllocationDto> appSvcDislist,List<AppSvcLaboratoryDisciplinesDto> appSvclaborlist,String serviceId,StringBuilder sB){
        if(appSvclaborlist==null||appSvclaborlist.isEmpty()){
            return;
        }else if(appSvclaborlist!=null&&!appSvclaborlist.isEmpty()){
            List<AppSvcChckListDto> appSvcChckListDtoList =IaisCommonUtils.genNewArrayList();
            for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto :appSvclaborlist ){
                appSvcChckListDtoList.addAll(appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList());
            }

            if(appSvcChckListDtoList!=null){
                if(appSvcDislist==null){
                    map.put("appSvcDislist","appSvcDislist null");
                    sB.append(serviceId);
                    return;
                }else {
                  /*  if( appSvcChckListDtoList.size()!=appSvcDislist.size()){
                        log.info(appSvcChckListDtoList.size()+" appSvcChckListDtoList ");
                        log.info(appSvcDislist.size()+" appSvcDislist ");
                        map.put("appSvcChckListDtoListsize","size");
                        sB.append(serviceId);
                        return;
                    }
*/
                }
            }
        }
    }
    private static void doPO( List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos, Map oneErrorMap ,List<AppSvcPrincipalOfficersDto> poDto,String serviceId,StringBuilder sB){
        if(poDto==null){
            log.info("podto is null");
            if(hcsaSvcPersonnelDtos!=null){
                for(HcsaSvcPersonnelDto every :hcsaSvcPersonnelDtos){
                    String psnType = every.getPsnType();
                    if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnType)){
                        sB.append(serviceId);
                        return;
                    }
                }
            }

            return;
        }


        boolean flag=false;
        int poIndex=0;
        int dpoIndex=0;
        List<String> stringList=IaisCommonUtils.genNewArrayList();
        for (int i=0;i< poDto.size();i++) {
            String psnType = poDto.get(i).getPsnType();
            if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnType)){

                StringBuilder stringBuilder =new StringBuilder(10);

                String assignSelect = poDto.get(i).getAssignSelect();
                if ("-1".equals(assignSelect)) {
                    oneErrorMap.put("assignSelect"+i, "UC_CHKLMD001_ERR001");
                } else {
                    //do by wenkang
                    String mobileNo = poDto.get(i).getMobileNo();
                    String officeTelNo = poDto.get(i).getOfficeTelNo();
                    String emailAddr = poDto.get(i).getEmailAddr();
                    String idNo = poDto.get(i).getIdNo();
                    String name = poDto.get(i).getName();
                    String salutation = poDto.get(i).getSalutation();
                    String designation = poDto.get(i).getDesignation();
                    String idType = poDto.get(i).getIdType();

                    if("-1".equals(idType)){
                        oneErrorMap.put("idType"+poIndex,"UC_CHKLMD001_ERR001");
                    }
                    if(StringUtil.isEmpty(name)){
                        oneErrorMap.put("name"+poIndex,"UC_CHKLMD001_ERR001");
                    }else if (name.length()>66){

                    }
                    if(StringUtil.isEmpty(salutation)){
                        oneErrorMap.put("salutation"+poIndex,"UC_CHKLMD001_ERR001");
                    }
                    if(StringUtil.isEmpty(designation)){
                        oneErrorMap.put("designation"+poIndex,"UC_CHKLMD001_ERR001");
                    }
                    if(!StringUtil.isEmpty(idNo)){
                        if("FIN".equals(idType)){
                            boolean b = SgNoValidator.validateFin(idNo);
                            if(!b){
                                oneErrorMap.put("NRICFIN","CHKLMD001_ERR005");
                            }else {
                                stringBuilder.append(idType).append(idNo);

                            }
                        }
                        if("NRIC".equals(idType)){
                            boolean b1 = SgNoValidator.validateNric(idNo);
                            if(!b1){
                                oneErrorMap.put("NRICFIN","CHKLMD001_ERR005");
                            }else {
                                stringBuilder.append(idType).append(idNo);

                            }
                        }
                    }else {
                        oneErrorMap.put("NRICFIN","UC_CHKLMD001_ERR001");
                    }
                    if(!StringUtil.isEmpty(mobileNo)){

                        if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                            oneErrorMap.put("mobileNo"+poIndex, "CHKLMD001_ERR004");
                        }
                    }else {
                        oneErrorMap.put("mobileNo"+poIndex, "UC_CHKLMD001_ERR001");
                    }
                    if(!StringUtil.isEmpty(emailAddr)) {
                        if (!  ValidationUtils.isEmail(emailAddr)) {
                            oneErrorMap.put("emailAddr"+poIndex, "CHKLMD001_ERR006");
                        }else if(emailAddr.length()>66){

                        }
                    }else {
                        oneErrorMap.put("emailAddr"+poIndex, "UC_CHKLMD001_ERR001");
                    }
                    if(!StringUtil.isEmpty(officeTelNo)) {
                        if (!officeTelNo.matches("^[6][0-9]{7}$")) {
                            oneErrorMap.put("officeTelNo"+poIndex, "GENERAL_ERR0015");
                        }
                    }else {
                        oneErrorMap.put("officeTelNo"+poIndex, "UC_CHKLMD001_ERR001");
                    }
                }
                poIndex++;
                String s = stringBuilder.toString();

                if(stringList.contains(s)) {

                    oneErrorMap.put("NRICFIN", "UC_CHKLMD001_ERR002");

                }else {
                    stringList.add(stringBuilder.toString());
                }
            }

            if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(psnType)){
                StringBuilder stringBuilder =new StringBuilder(10);
                String salutation = poDto.get(i).getSalutation();
                String name = poDto.get(i).getName();
                String idType = poDto.get(i).getIdType();
                String mobileNo = poDto.get(i).getMobileNo();
                String emailAddr = poDto.get(i).getEmailAddr();
                String idNo = poDto.get(i).getIdNo();
                String designation = poDto.get(i).getDesignation();
                String officeTelNo = poDto.get(i).getOfficeTelNo();
                /*if(StringUtil.isEmpty(modeOfMedAlert)||"-1".equals(modeOfMedAlert)){
                    oneErrorMap.put("modeOfMedAlert"+dpoIndex,"UC_CHKLMD001_ERR001");
                }*/

                if(StringUtil.isEmpty(designation)||"-1".equals(designation)){
                    oneErrorMap.put("deputyDesignation"+dpoIndex,"UC_CHKLMD001_ERR001");
                }
                if(StringUtil.isEmpty(salutation)||"-1".equals(salutation)){
                    oneErrorMap.put("deputySalutation"+dpoIndex,"UC_CHKLMD001_ERR001");
                }

                if(StringUtil.isEmpty(idType)||"-1".equals(idType)){
                    oneErrorMap.put("deputyIdType"+dpoIndex,"UC_CHKLMD001_ERR001");
                }
                if(StringUtil.isEmpty(name)){
                    oneErrorMap.put("deputyName"+dpoIndex,"UC_CHKLMD001_ERR001");
                }else if(name.length()>66){

                }
                if(StringUtil.isEmpty(officeTelNo)){
                    oneErrorMap.put("deputyofficeTelNo"+dpoIndex,"UC_CHKLMD001_ERR001");
                }else {
                    if(!officeTelNo.matches("^[6][0-9]{7}$")){
                        oneErrorMap.put("deputyofficeTelNo"+dpoIndex,"GENERAL_ERR0015");
                    }
                }
                if(StringUtil.isEmpty(idNo)){
                    oneErrorMap.put("deputyIdNo"+dpoIndex,"UC_CHKLMD001_ERR001");
                }
                if("FIN".equals(idType)){
                    boolean b = SgNoValidator.validateFin(idNo);
                    if(!b){
                        oneErrorMap.put("deputyIdNo"+dpoIndex,"CHKLMD001_ERR005");
                    }else {
                        stringBuilder.append(idType).append(idNo);
                    }
                }
                if("NRIC".equals(idType)){
                    boolean b1 = SgNoValidator.validateNric(idNo);
                    if(!b1){
                        oneErrorMap.put("deputyIdNo"+dpoIndex,"CHKLMD001_ERR005");
                    }else {
                        stringBuilder.append(idType).append(idNo);
                    }
                }

                if(StringUtil.isEmpty(mobileNo)){
                    oneErrorMap.put("deputyMobileNo"+dpoIndex,"UC_CHKLMD001_ERR001");
                }
                else {
                    if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                        oneErrorMap.put("deputyMobileNo"+dpoIndex, "CHKLMD001_ERR004");
                    }
                }
                if(StringUtil.isEmpty(emailAddr)){
                    oneErrorMap.put("deputyEmailAddr"+dpoIndex,"UC_CHKLMD001_ERR001");
                }else {
                    if (!ValidationUtils.isEmail(emailAddr)) {
                        oneErrorMap.put("deputyEmailAddr"+dpoIndex, "CHKLMD001_ERR006");
                    }else if(emailAddr.length()>66){

                    }
                }
                dpoIndex++;

                String s = stringBuilder.toString();

                if(stringList.contains(s)&&!StringUtil.isEmpty(s)) {

                    oneErrorMap.put("NRICFIN", "UC_CHKLMD001_ERR002");

                }else {
                    stringList.add(stringBuilder.toString());
                }
            }


        }
        if(flag){
            sB.append(serviceId);
        }
        log.info(StringUtil.changeForLog(JsonUtil.parseToJson(oneErrorMap)+"oneErrorMap"));
    }

    private void loadingCoMap( AppSubmissionDto appSubmissionDto,HttpServletRequest request){
        if(appSubmissionDto!=null){
            List<String> stepColor = appSubmissionDto.getStepColor();
            if(stepColor!=null){
                HashMap<String,String> coMap=new HashMap<>(5);
                coMap.put("premises","");
                coMap.put("document","");
                coMap.put("information","");
                coMap.put("previewli","");
                if(!stepColor.isEmpty()){
                    for(String str : stepColor){
                        if("premises".equals(str)){
                            coMap.put("premises",str);
                        }else if("document".equals(str)){
                            coMap.put("document",str);
                        }else if("information".equals(str)){
                            coMap.put("information",str);
                        }else if("previewli".equals(str)){
                            coMap.put("previewli",str);
                        }else if("".equals(str)){
                            coMap.put("serviceConfig",str);
                        }

                    }

                }

                request.getSession().setAttribute("coMap",coMap);

            }

        }
    }

    private void loadingDraft(BaseProcessClass bpc, String draftNo) {
        log.info(StringUtil.changeForLog("the do loadingDraft start ...."));
        Object draftNumber = bpc.request.getSession().getAttribute("DraftNumber");
        if(draftNumber!=null){
            draftNo=(String)draftNumber;
        }
        //draftNo = "DN191118000001";
        if(!StringUtil.isEmpty(draftNo)){
            log.info(StringUtil.changeForLog("draftNo is not empty"));
            AppSubmissionDto appSubmissionDto = serviceConfigService.getAppSubmissionDtoDraft(draftNo);
            if(appSubmissionDto!=null){
                if(IaisCommonUtils.isEmpty(appSubmissionDto.getAppSvcRelatedInfoDtoList())){
                    log.info(StringUtil.changeForLog("appSvcRelatedInfoDtoList is empty"));
                }
                List<String> stepColor = appSubmissionDto.getStepColor();
                if(stepColor!=null){
                    HashMap<String,String> coMap=new HashMap<>(4);
                    coMap.put("premises","");
                    coMap.put("document","");
                    coMap.put("information","");
                    coMap.put("previewli","");
                    if(!stepColor.isEmpty()){
                        for(String str : stepColor){
                            if("premises".equals(str)){
                                coMap.put("premises",str);
                            }else if("document".equals(str)){
                                coMap.put("document",str);
                            }else if("information".equals(str)){
                                coMap.put("information",str);
                            }else if("previewli".equals(str)){
                                coMap.put("previewli",str);
                            }else {
                                bpc.request.getSession().setAttribute("serviceConfig",str);
                            }
                        }
                    }
                    bpc.getSession().setAttribute("coMap",coMap);
                }
                String personMapStr = appSubmissionDto.getDropDownPsnMapStr();

//                if(!StringUtil.isEmpty(personMapStr)){
//                    Map<String,LinkedHashMap<String,Object>> personLinkedMap = JsonUtil.parseToObject(personMapStr,Map.class);
//                    Map<String,AppSvcPersonAndExtDto> personMap = IaisCommonUtils.genNewHashMap();
//                    personLinkedMap.forEach((k,v)->{
//                        AppSvcPersonAndExtDto person = new AppSvcPersonAndExtDto();
//                        AppSvc
//
//                        appSvcPrincipalOfficersDto.setPsnType((String) v.get("psnType"));
//                        appSvcPrincipalOfficersDto.setAssignSelect((String) v.get("assignSelect"));
//                        appSvcPrincipalOfficersDto.setSalutation((String) v.get("salutation"));
//                        appSvcPrincipalOfficersDto.setName((String) v.get("name"));
//                        appSvcPrincipalOfficersDto.setIdType((String) v.get("idType"));
//                        appSvcPrincipalOfficersDto.setIdNo((String) v.get("idNo"));
//                        appSvcPrincipalOfficersDto.setDesignation((String) v.get("designation"));
//                        appSvcPrincipalOfficersDto.setMobileNo((String) v.get("mobileNo"));
//                        appSvcPrincipalOfficersDto.setOfficeTelNo((String) v.get("officeTelNo"));
//                        appSvcPrincipalOfficersDto.setEmailAddr((String) v.get("emailAddr"));
//                        appSvcPrincipalOfficersDto.setPreferredMode((String) v.get("preferredMode"));
//                        appSvcPrincipalOfficersDto.setProfessionType((String) v.get("professionType"));
//                        appSvcPrincipalOfficersDto.setProfRegNo((String) v.get("profRegNo"));
//                        appSvcPrincipalOfficersDto.setSpeciality((String) v.get("speciality"));
//                        appSvcPrincipalOfficersDto.setSpecialityOther((String) v.get("specialityOther"));
//                        appSvcPrincipalOfficersDto.setSubSpeciality((String) v.get("subSpeciality"));
//                        appSvcPrincipalOfficersDto.setNeedSpcOptList((boolean) v.get("needSpcOptList"));
//                        List<LinkedHashMap<String,String>> spcOptLinkedMap= (List<LinkedHashMap<String, String>>) v.get("spcOptList");
//                        List<SelectOption> spcOptList = IaisCommonUtils.genNewArrayList();
//                        if(!IaisCommonUtils.isEmpty(spcOptLinkedMap)){
//                            for(LinkedHashMap<String,String> item:spcOptLinkedMap){
//                                spcOptList.add(new SelectOption(item.get("value"),item.get("text")));
//                            }
//                        }
//                        appSvcPrincipalOfficersDto.setSpcOptList(spcOptList);
//                        appSvcPrincipalOfficersDto.setSpecialityHtml((String) v.get("specialityHtml"));
//                        personMap.put(k,appSvcPrincipalOfficersDto);
//                    });
//                    ParamUtil.setSessionAttr(bpc.request,PERSONSELECTMAP, (Serializable) personMap);
//                }
                if(appSubmissionDto.getAppGrpPremisesDtoList() != null && appSubmissionDto.getAppGrpPremisesDtoList().size() >0){
                    ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
                }else{
                    ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, null);
                }
                ParamUtil.setSessionAttr(bpc.request,DRAFTCONFIG,"test");
            }
            bpc.request.getSession().setAttribute(SELECT_DRAFT_NO,null);
        }
        if(draftNumber!=null){

            ParamUtil.setSessionAttr(bpc.request,DRAFTCONFIG,null);
        }
        log.info(StringUtil.changeForLog("the do loadingDraft end ...."));
    }

    private void requestForChangeOrRenewLoading(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the do requestForChangeLoading start ...."));
        String appType = (String) ParamUtil.getRequestAttr(bpc.request,"appType");
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getRequestAttr(bpc.request,RfcConst.APPSUBMISSIONDTORFCATTR);
        String currentEdit = (String) ParamUtil.getRequestAttr(bpc.request,RfcConst.RFC_CURRENT_EDIT);
        if((ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType))
                && appSubmissionDto!= null && !StringUtil.isEmpty(currentEdit)){
            ParamUtil.setSessionAttr(bpc.request,"hasDetail","Y");
            ParamUtil.setSessionAttr(bpc.request,"isSingle","Y");
            AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
            if(RfcConst.EDIT_PREMISES.equals(currentEdit)){
                appEditSelectDto.setPremisesEdit(true);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE,"premises");
            }else if(RfcConst.EDIT_PRIMARY_DOC.equals(currentEdit)){
                appEditSelectDto.setDocEdit(true);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE,"documents");
            }else if(RfcConst.EDIT_SERVICE.equals(currentEdit)){
                appEditSelectDto.setServiceEdit(true);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE,"serviceForms");
            }
            appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
            appSubmissionDto.setNeedEditController(true);
            ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
            HashMap<String,String> coMap=(HashMap<String,String>) bpc.request.getSession().getAttribute("coMap");
            coMap.put("premises","premises");
            coMap.put("document","document");
            coMap.put("information","information");
            coMap.put("previewli","previewli");
            bpc.request.getSession().setAttribute("coMap",coMap);
        }
        log.info(StringUtil.changeForLog("the do requestForChangeLoading end ...."));
    }

    private void renewLicence(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the do renewLicence start ...."));
        String licenceId = ParamUtil.getString(bpc.request, "licenceId");
        String type = ParamUtil.getString(bpc.request, "type");
        log.info(StringUtil.changeForLog("The type is -->:"+type));
        if(!StringUtil.isEmpty(licenceId) && ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(type)){
            AppSubmissionDto appSubmissionDto = appSubmissionService.getAppSubmissionDtoByLicenceId(licenceId);
            if(appSubmissionDto != null){
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

    private void requestForInformationLoading(BaseProcessClass bpc,String appNo) throws CloneNotSupportedException {
        log.info(StringUtil.changeForLog("the do requestForInformationLoading start ...."));
        String msgId = (String) ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        //msgId = "415199C2-4AAA-42BF-B068-9B019BF1ED1C";
        if(!StringUtil.isEmpty(appNo) && !StringUtil.isEmpty(msgId)){
            AppSubmissionDto appSubmissionDto = appSubmissionService.getAppSubmissionDtoByAppNo(appNo);
            if(appSubmissionDto!=null){
                if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())||ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())){
                    requestForChangeService.svcDocToPresmise(appSubmissionDto);
                    List<AppGrpPremisesDto> appGrpPremisesDtoList= appSubmissionService.getAppGrpPremisesDto(appNo);
                    List<String> ids=IaisCommonUtils.genNewArrayList();
                    String premisesIndexNo=null;
                    for(AppGrpPremisesDto appGrpPremisesDto : appSubmissionDto.getAppGrpPremisesDtoList()){
                        for(AppGrpPremisesDto appGrpPremisesDto1 : appGrpPremisesDtoList){
                            if(appGrpPremisesDto.getId().equals(appGrpPremisesDto1.getId())){
                                appGrpPremisesDto1.setPremisesIndexNo(appGrpPremisesDto.getPremisesIndexNo());
                            }
                        }
                    }
                    if(appGrpPremisesDtoList!=null&&!appGrpPremisesDtoList.isEmpty()){
                        for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList){
                            ids.add(appGrpPremisesDto.getId());
                            premisesIndexNo=appGrpPremisesDto.getPremisesIndexNo();
                        }
                    }
                    appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtoList);

                    ApplicationDto entity = applicationClient.getApplicationDtoByVersion(appNo).getEntity();
                    List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos =IaisCommonUtils.genNewArrayList();
                    List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
                    for(AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList){
                        if(entity.getServiceId().equals(appSvcRelatedInfoDto.getServiceId())){
                            /*  appSvcRelatedInfoDto.setAppSvcDocDtoLit(null);*/
                            appSvcRelatedInfoDtos.add(appSvcRelatedInfoDto);
                            List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
                            List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtos=IaisCommonUtils.genNewArrayList();
                            for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvcLaboratoryDisciplinesDtoList){
                                if(appSvcLaboratoryDisciplinesDto.getPremiseVal().equals(premisesIndexNo)){
                                    appSvcLaboratoryDisciplinesDtos.add(appSvcLaboratoryDisciplinesDto);
                                }

                            }
                            appSvcRelatedInfoDto.setAppSvcLaboratoryDisciplinesDtoList(appSvcLaboratoryDisciplinesDtos);
                        }
                    }
                    appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtos);

                }
            }
            InterMessageDto interMessageDto = appSubmissionService.getInterMessageById(msgId);
            if(MessageConstants.MESSAGE_STATUS_RESPONSE.equals(interMessageDto.getStatus())){
                appSubmissionDto = null;
            }

            if(appSubmissionDto != null){
                //clear svcDoc id
                List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
                if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
                    for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                        List<AppSvcDocDto> appSvcDocDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getAppSvcDocDtoLit();
                        if(!IaisCommonUtils.isEmpty(appSvcDocDtos)){
                            for(AppSvcDocDto appSvcDocDto:appSvcDocDtos){
                                appSvcDocDto.setId(null);
                            }
                            appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtos);
                        }
                    }
                    appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtos);
                }

                String appType =  appSubmissionDto.getAppType();
                boolean isRenewalOrRfc = ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType) || ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType);
                appSubmissionDto.setNeedEditController(true);
                AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto();
                if(appEditSelectDto == null){
                    appEditSelectDto = new AppEditSelectDto();
                    appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
                }
                if (isRenewalOrRfc){
                    // set the required information
                    String licenceId = appSubmissionDto.getLicenceId();
                    appSubmissionDto.setLicenceNo(withOutRenewalService.getLicenceNumberByLicenceId(licenceId));
                }
                ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
                //ParamUtil.setSessionAttr(bpc.request,OLDAPPSUBMISSIONDTO,oldAppSubmissionDto);
                HashMap<String,String> coMap=(HashMap<String,String>) bpc.request.getSession().getAttribute("coMap");
                coMap.put("premises","premises");
                coMap.put("document","document");
                coMap.put("information","information");
                coMap.put("previewli","previewli");
                bpc.request.getSession().setAttribute("coMap",coMap);
            }else{
                ApplicationDto applicationDto = appSubmissionService.getMaxVersionApp(appNo);
                if(applicationDto != null){
                    HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(applicationDto.getServiceId());
                    if(hcsaServiceDto != null){
                        List<HcsaServiceDto> hcsaServiceDtoList = IaisCommonUtils.genNewArrayList();
                        hcsaServiceDtoList.add(hcsaServiceDto);
                        ParamUtil.setSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) hcsaServiceDtoList);
                    }
                    String errMsg = "You have already replied to this RFI";
                    jumpToAckPage(bpc,NewApplicationConstant.ACK_STATUS_ERROR,errMsg);
                }
            }
            ParamUtil.setSessionAttr(bpc.request,REQUESTINFORMATIONCONFIG,"test");
        }
        log.info(StringUtil.changeForLog("the do requestForInformationLoading end ...."));
    }

    private boolean loadingServiceConfig(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.info(StringUtil.changeForLog("the do loadingServiceConfig start ...."));
        //loading the service
        List<String> serviceConfigIds = IaisCommonUtils.genNewArrayList();
        List<String> names = IaisCommonUtils.genNewArrayList();
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        if(appSubmissionDto != null ){
            // from draft,rfi
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtoList)){
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtoList){
                    if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getServiceId())){
                        serviceConfigIds.add(appSvcRelatedInfoDto.getServiceId());
                    }
                    //if get the data from licence, only have the serviceName
                    if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getServiceName())){
                        names.add(appSvcRelatedInfoDto.getServiceName());
                    }

                }
            }
        }else {
            List<String> licenceIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, "licence");
            List<String> baseServiceIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, "baseServiceChecked");
            List<String> specifiedServiceIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, "specifiedServiceChecked");
            if(IaisCommonUtils.isEmpty(licenceIds)){
                if(!IaisCommonUtils.isEmpty(baseServiceIds)){
                    serviceConfigIds.addAll(baseServiceIds);
                }
                if(!IaisCommonUtils.isEmpty(specifiedServiceIds)){
                    serviceConfigIds.addAll(specifiedServiceIds);
                }
            }
        }

        if(IaisCommonUtils.isEmpty(serviceConfigIds) && IaisCommonUtils.isEmpty(names)){
            log.info(StringUtil.changeForLog("service id is empty"));
            String errMsg = "you have encountered some problems, please contact the administrator !!!";
            jumpToAckPage(bpc,NewApplicationConstant.ACK_STATUS_ERROR,errMsg);
            return false;
        }

        List<HcsaServiceDto> hcsaServiceDtoList = null;
        if(!serviceConfigIds.isEmpty()){
            hcsaServiceDtoList = serviceConfigService.getHcsaServiceDtosById(serviceConfigIds);
        }else if(!names.isEmpty()){
            hcsaServiceDtoList = serviceConfigService.getHcsaServiceByNames(names);
        }
        if(hcsaServiceDtoList!=null){
            hcsaServiceDtoList = sortHcsaServiceDto(hcsaServiceDtoList);
        }
        ParamUtil.setSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) hcsaServiceDtoList);
        log.info(StringUtil.changeForLog("the do loadingServiceConfig end ...."));
        return true;
    }

    private List<HcsaServiceDto> sortHcsaServiceDto(List<HcsaServiceDto> hcsaServiceDtoList) {
        List<HcsaServiceDto> baseList = new ArrayList();
        List<HcsaServiceDto> specifiedList = new ArrayList();
        List<HcsaServiceDto> subList = new ArrayList();
        List<HcsaServiceDto> otherList = new ArrayList();
        //class
        for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtoList) {
            switch (hcsaServiceDto.getSvcType()) {
                case ApplicationConsts.SERVICE_CONFIG_TYPE_BASE:
                    baseList.add(hcsaServiceDto);
                    break;
                case ApplicationConsts.SERVICE_CONFIG_TYPE_SPECIFIED:
                    subList.add(hcsaServiceDto);
                    break;
                case ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED:
                    specifiedList.add(hcsaServiceDto);
                    break;
                default:
                    otherList.add(hcsaServiceDto);
                    break;
            }
        }
        //Sort
        sortService(baseList);
        sortService(specifiedList);
        sortService(subList);
        sortService(otherList);
        hcsaServiceDtoList = IaisCommonUtils.genNewArrayList();
        hcsaServiceDtoList.addAll(baseList);
        hcsaServiceDtoList.addAll(specifiedList);
        hcsaServiceDtoList.addAll(subList);
        hcsaServiceDtoList.addAll(otherList);
        return hcsaServiceDtoList;
    }

    private void sortService(List<HcsaServiceDto> list) {
        list.sort((h1, h2) -> h1.getSvcName().compareTo(h2.getSvcName()));
    }

    private static int validateTime(Map<String, String> errorMap, String onsiteHH, String onsiteMM, int date, String key, int i){
        try {
            int i1 = Integer.parseInt(onsiteHH);
            int i2= Integer.parseInt(onsiteMM);
            date=  i1*60+i2*1;
            if(i1>=24){
                errorMap.put(key+i,"UC_CHKLMD001_ERR009");
            }else if(i2>=60){
                errorMap.put(key+i,"UC_CHKLMD001_ERR010");
            }
        }catch (Exception e){
            errorMap.put(key+i,"CHKLMD001_ERR003");
        }
        return date;
    }

    //todo:move to NewApplicationHelper
    public static Map<String, String> doValidatePremiss(BaseProcessClass bpc) { //NOSONAR
        log.info("the do doValidatePremiss start ...."); //NOSONAR
        //do validate one premiss
        List<String> list=IaisCommonUtils.genNewArrayList();
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,APPSUBMISSIONDTO);
        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,OLDAPPSUBMISSIONDTO);
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        List<String> premisesHciList = (List<String>) ParamUtil.getSessionAttr(bpc.request,NewApplicationConstant.PREMISES_HCI_LIST);
        Set<String> distinctVehicleNo = IaisCommonUtils.genNewHashSet();
        for(int i=0;i<appGrpPremisesDtoList.size();i++){
            String premiseType = appGrpPremisesDtoList.get(i).getPremisesType();
            if (StringUtil.isEmpty(premiseType)) {
                errorMap.put("premisesType"+i, "UC_CHKLMD001_ERR001");
            }else {
                String premisesSelect = appGrpPremisesDtoList.get(i).getPremisesSelect();
                String appType = appSubmissionDto.getAppType();
                boolean needValidate = false;

                if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){
                    String oldPremSel = oldAppSubmissionDto.getAppGrpPremisesDtoList().get(0).getPremisesSelect();
                    if(!StringUtil.isEmpty(oldPremSel) && oldPremSel.equals(premisesSelect)){
                        needValidate = true;
                    }
                }
                AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtoList.get(i);
                if (StringUtil.isEmpty(premisesSelect) || "-1".equals(premisesSelect)) {
                    errorMap.put("premisesSelect"+i, "UC_CHKLMD001_ERR001");
                } else if ( needValidate||!StringUtil.isEmpty(premisesSelect)||"newPremise".equals(premisesSelect) ) {
                    StringBuilder stringBuilder=new StringBuilder(10);
                    if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premiseType)) {
                        String onsiteStartHH = appGrpPremisesDtoList.get(i).getOnsiteStartHH();
                        String onsiteStartMM = appGrpPremisesDtoList.get(i).getOnsiteStartMM();
                        int startDate=0;
                        int endDate=0;
                        if(StringUtil.isEmpty(onsiteStartHH)||StringUtil.isEmpty(onsiteStartMM)){
                            errorMap.put("onsiteStartMM"+i,"UC_CHKLMD001_ERR001");
                        }else {
                            startDate = validateTime(errorMap, onsiteStartHH, onsiteStartMM, startDate, "onsiteStartMM", i);
                        }

                        String onsiteEndHH = appGrpPremisesDtoList.get(i).getOnsiteEndHH();
                        String onsiteEndMM = appGrpPremisesDtoList.get(i).getOnsiteEndMM();
                        if(StringUtil.isEmpty(onsiteEndHH)||StringUtil.isEmpty(onsiteEndMM)){
                            errorMap.put("onsiteEndMM"+i,"UC_CHKLMD001_ERR001");
                        }else {
                            endDate = validateTime(errorMap, onsiteEndHH, onsiteEndMM, endDate, "onsiteEndMM", i);
                        }
                        if(!StringUtil.isEmpty(onsiteStartHH)&&!StringUtil.isEmpty(onsiteStartMM)&&!StringUtil.isEmpty(onsiteEndHH)&&!StringUtil.isEmpty(onsiteEndMM)){
                            if(endDate<startDate){
                                errorMap.put("onsiteEndMM"+i,"UC_CHKLMD001_ERR008");
                            }
                        }

                        /*Boolean isOtherLic = appGrpPremisesDtoList.get(i).isLocateWithOthers();
                        if(StringUtil.isEmpty(isOtherLic)){
                            errorMap.put("isOtherLic"+i,"UC_CHKLMD001_ERR002");
                        }*/

                        //set  time
                        if(!errorMap.containsKey("onsiteStartMM"+i) && !errorMap.containsKey("onsiteEndMM"+i)){
                            LocalTime startTime = LocalTime.of(Integer.parseInt(onsiteStartHH),Integer.parseInt(onsiteStartMM));
                            appGrpPremisesDtoList.get(i).setWrkTimeFrom(Time.valueOf(startTime));

                            LocalTime endTime = LocalTime.of(Integer.parseInt(onsiteEndHH),Integer.parseInt(onsiteEndMM));
                            appGrpPremisesDtoList.get(i).setWrkTimeTo(Time.valueOf(endTime));
                        }

                        List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodList = appGrpPremisesDtoList.get(i).getAppPremPhOpenPeriodList();
                        if(!IaisCommonUtils.isEmpty(appPremPhOpenPeriodList)){
                            for(int j=0;j<appPremPhOpenPeriodList.size();j++){
                                AppPremPhOpenPeriodDto appPremPhOpenPeriodDto = appPremPhOpenPeriodList.get(j);
                                String convStartFromHH = appPremPhOpenPeriodDto.getOnsiteStartFromHH();
                                String convStartFromMM = appPremPhOpenPeriodDto.getOnsiteStartFromMM();
                                String onsiteEndToHH = appPremPhOpenPeriodDto.getOnsiteEndToHH();
                                String onsiteEndToMM = appPremPhOpenPeriodDto.getOnsiteEndToMM();
                                Date phDate = appPremPhOpenPeriodDto.getPhDate();
                                if(!StringUtil.isEmpty(phDate)){
                                    if(StringUtil.isEmpty(convStartFromHH)||StringUtil.isEmpty(convStartFromMM)){
                                        errorMap.put("onsiteStartToMM"+i+j,"UC_CHKLMD001_ERR001");
                                    }
                                    if(StringUtil.isEmpty(onsiteEndToHH)||StringUtil.isEmpty(onsiteEndToMM)){
                                        errorMap.put("onsiteEndToMM"+i+j,"UC_CHKLMD001_ERR001");
                                    }
                                }else if(StringUtil.isEmpty(phDate)){
                                    errorMap.put("onsitephDate"+i+j,"UC_CHKLMD001_ERR001");
                                }
                                if(!StringUtil.isEmpty(convStartFromHH)&&!StringUtil.isEmpty(convStartFromMM)&&!StringUtil.isEmpty(onsiteEndToHH)
                                        &&!StringUtil.isEmpty(onsiteEndToMM)||StringUtil.isEmpty(convStartFromHH)&&StringUtil.isEmpty(convStartFromMM)
                                        &&StringUtil.isEmpty(onsiteEndToHH)&&StringUtil.isEmpty(onsiteEndToMM)){
                                    if(!StringUtil.isEmpty(convStartFromHH)&&!StringUtil.isEmpty(convStartFromMM)&&!StringUtil.isEmpty(onsiteEndToHH)
                                            &&!StringUtil.isEmpty(onsiteEndToMM)){
                                        try {
                                            int i1 = Integer.parseInt(convStartFromHH);
                                            int i2 = Integer.parseInt(convStartFromMM);

                                            if(i1>=24){
                                                errorMap.put("onsiteStartToMM"+i+j,"UC_CHKLMD001_ERR009");
                                            }else if(i2>=60){
                                                errorMap.put("onsiteStartToMM"+i+j,"UC_CHKLMD001_ERR010");
                                            }

                                        }catch (Exception e){
                                            errorMap.put("onsiteStartToMM"+i+j,"CHKLMD001_ERR003");
                                        }
                                        try {
                                            int i3 = Integer.parseInt(onsiteEndToHH);
                                            int i4 = Integer.parseInt(onsiteEndToMM);
                                            if(i3>=24){
                                                errorMap.put("onsiteEndToMM"+i+j,"UC_CHKLMD001_ERR009");
                                            }else if(i4>=60){
                                                errorMap.put("onsiteEndToMM"+i+j,"UC_CHKLMD001_ERR010");
                                            }
                                        }catch (Exception e){
                                            errorMap.put("onsiteEndToMM"+i+j,"CHKLMD001_ERR003");
                                        }
                                        try {
                                            int i1 = Integer.parseInt(convStartFromHH);
                                            int i2 = Integer.parseInt(convStartFromMM);
                                            int i3 = Integer.parseInt(onsiteEndToHH);
                                            int i4 = Integer.parseInt(onsiteEndToMM);
                                            if((i1*60+i2)>(i3*60+i4)){
                                                errorMap.put("onsiteEndToMM"+i+j,"UC_CHKLMD001_ERR008");
                                            }
                                        }catch (Exception e){
                                        }
                                    }


                                }else {
                                    if(StringUtil.isEmpty(convStartFromHH)&&StringUtil.isEmpty(convStartFromMM)||StringUtil.isEmpty(convStartFromMM)||StringUtil.isEmpty(convStartFromHH)){
                                        errorMap.put("onsiteStartToMM"+i+j,"UC_CHKLMD001_ERR001");
                                    }else {
                                        try {
                                            int i1 = Integer.parseInt(convStartFromHH);
                                            int i2 = Integer.parseInt(convStartFromMM);

                                            if(i1>=24){
                                                errorMap.put("onsiteStartToMM"+i+j,"UC_CHKLMD001_ERR009");
                                            }else if(i2>=60){
                                                errorMap.put("onsiteStartToMM"+i+j,"UC_CHKLMD001_ERR010");
                                            }

                                        }catch (Exception e){
                                            errorMap.put("onsiteStartToMM"+i+j,"CHKLMD001_ERR003");
                                        }
                                    }
                                    if(StringUtil.isEmpty(onsiteEndToHH)&&StringUtil.isEmpty(onsiteEndToMM)||StringUtil.isEmpty(onsiteEndToHH)||StringUtil.isEmpty(onsiteEndToMM)){
                                        errorMap.put("onsiteEndToMM"+i+j,"UC_CHKLMD001_ERR001");
                                    }else {
                                        try {
                                            int i3 = Integer.parseInt(onsiteEndToHH);
                                            int i4 = Integer.parseInt(onsiteEndToMM);
                                            if(i3>=24){
                                                errorMap.put("onsiteEndToMM"+i+j,"UC_CHKLMD001_ERR009");
                                            }else if(i4>=60){
                                                errorMap.put("onsiteEndToMM"+i+j,"UC_CHKLMD001_ERR010");
                                            }
                                        }catch (Exception e){
                                            errorMap.put("onsiteEndToMM"+i+j,"CHKLMD001_ERR003");
                                        }

                                    }
                                }
                                //set ph time
                                String errorOnsiteStartToMM = errorMap.get("onsiteStartToMM"+i+j);
                                String errorOnsiteEndToMM = errorMap.get("onsiteEndToMM"+i+j);
                                if(StringUtil.isEmpty(errorOnsiteEndToMM) && StringUtil.isEmpty(errorOnsiteStartToMM) && !IaisCommonUtils.isEmpty(appPremPhOpenPeriodList)){
                                    LocalTime startTime = LocalTime.of(Integer.parseInt(convStartFromHH),Integer.parseInt(convStartFromMM));
                                    appPremPhOpenPeriodDto.setStartFrom(Time.valueOf(startTime));
                                    LocalTime endTime = LocalTime.of(Integer.parseInt(onsiteEndToHH),Integer.parseInt(onsiteEndToMM));
                                    appPremPhOpenPeriodDto.setEndTo(Time.valueOf(endTime));
                                }
                            }
                        }
                        String hciName = appGrpPremisesDtoList.get(i).getHciName();
                        if(StringUtil.isEmpty(hciName)){
                            errorMap.put("hciName"+i,"UC_CHKLMD001_ERR001");
                        } {
                            Object masterCodeDto = bpc.request.getAttribute("masterCodeDto");
                            if(masterCodeDto!=null){
                                MasterCodeDto masterCode=(MasterCodeDto)masterCodeDto;
                                String codeValue = masterCode.getCodeValue();
                                String[] s = codeValue.split(" ");
                                for(int index=0;index<s.length;index++){
                                    if(hciName.toUpperCase().contains(s[index].toUpperCase())){
                                        errorMap.put("hciName"+i,"CHKLMD001_ERR002");
                                    }
                                }
                            }
                        }
                        String offTelNo = appGrpPremisesDtoList.get(i).getOffTelNo();
                        if(StringUtil.isEmpty(offTelNo)){
                            errorMap.put("offTelNo"+i,"UC_CHKLMD001_ERR001");
                        }else {
                            boolean matches = offTelNo.matches("^[6][0-9]{7}$");
                            if(!matches) {
                                errorMap.put("offTelNo"+i,"GENERAL_ERR0015");
                            }
                        }

                        String streetName = appGrpPremisesDtoList.get(i).getStreetName();
                        if(StringUtil.isEmpty(streetName)){
                            errorMap.put("streetName"+i,"UC_CHKLMD001_ERR001");
                        }

                        String addrType = appGrpPremisesDtoList.get(i).getAddrType();

                        if(StringUtil.isEmpty(addrType)){
                            errorMap.put("addrType"+i, "UC_CHKLMD001_ERR001");
                        }else {
                            boolean empty = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getFloorNo());
                            boolean empty1 = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getBlkNo());
                            boolean empty2 = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getUnitNo());
                            if (ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(addrType)) {

                                if (empty) {
                                    errorMap.put("floorNo"+i, "UC_CHKLMD001_ERR001");
                                }
                                if (empty1) {
                                    errorMap.put("blkNo"+i, "UC_CHKLMD001_ERR001");
                                }
                                if (empty2) {
                                    errorMap.put("unitNo"+i, "UC_CHKLMD001_ERR001");
                                }
                            }
                            String floorNoErr = errorMap.get("floorNo"+i);
                            String floorNo = appGrpPremisesDtoList.get(i).getFloorNo();
                            if(StringUtil.isEmpty(floorNoErr) && !StringUtil.isEmpty(floorNo)){
                                Pattern pattern = compile("[0-9]*");
                                boolean noFlag =  pattern.matcher(floorNo).matches();
                                if (noFlag) {
                                    int floorNum = Integer.parseInt(floorNo);
                                    if (10 > floorNum) {
                                        floorNo = "0" + floorNum;
                                        appGrpPremisesDtoList.get(i).setFloorNo(floorNo);
                                    }
                                }

                            }
                            if(!empty&&!empty1&&!empty2){
                                stringBuilder.append(appGrpPremisesDtoList.get(i).getFloorNo())
                                        .append(appGrpPremisesDtoList.get(i).getBlkNo())
                                        .append(appGrpPremisesDtoList.get(i).getUnitNo());
                            }
                        }
                        String postalCode = appGrpPremisesDtoList.get(i).getPostalCode();
                        if (!StringUtil.isEmpty(postalCode)) {
                            if (!postalCode.matches("^[0-9]{6}$")) {
                                errorMap.put("postalCode"+i, "NEW_ERR0004");
                            }else {

                                if(!StringUtil.isEmpty(stringBuilder.toString())){
                                    stringBuilder.append(postalCode);
                                    if(list.contains(stringBuilder.toString())){
                                        errorMap.put("postalCode"+i,"There is a duplicated entry for this premises address");

                                    }else {
                                        list.add(stringBuilder.toString());
                                    }
                                }
                            }
                        }else {
                            errorMap.put("postalCode"+i, "UC_CHKLMD001_ERR001");
                        }
                        //0062204
                        String currentHci = hciName + IaisCommonUtils.genPremisesKey(postalCode,appGrpPremisesDto.getBlkNo(),appGrpPremisesDto.getFloorNo(),appGrpPremisesDto.getUnitNo());
                        String hciNameErr = errorMap.get("hciName"+i);
                        String postalCodeErr =errorMap.get("postalCode"+i);
                        String blkNoErr =errorMap.get("blkNo"+i);
                        String floorNoErr =errorMap.get("floorNo"+i);
                        String unitNoErr =errorMap.get("unitNo"+i);
                        boolean hciFlag =  StringUtil.isEmpty(hciNameErr) && StringUtil.isEmpty(postalCodeErr) && StringUtil.isEmpty(blkNoErr) && StringUtil.isEmpty(floorNoErr) && StringUtil.isEmpty(unitNoErr);
                        log.info(StringUtil.changeForLog("hciFlag:"+hciFlag));
                        boolean newTypeFlag = ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType());
                        if(newTypeFlag && hciFlag){
                            if(!IaisCommonUtils.isEmpty(premisesHciList) && premisesHciList.contains(currentHci)){
                                errorMap.put("premisesHci"+i,"NEW_ERR0005");
                            }
                        }
                    } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premiseType)) {
                        String conStartHH = appGrpPremisesDtoList.get(i).getConStartHH();
                        String conStartMM = appGrpPremisesDtoList.get(i).getConStartMM();
                        int conStartDate=0;
                        int conEndDate=0;

                        if(StringUtil.isEmpty(conStartHH)||StringUtil.isEmpty(conStartMM)){
                            errorMap.put("conStartMM"+i,"UC_CHKLMD001_ERR001");
                        }else {
                            conStartDate = validateTime(errorMap, conStartHH, conStartMM, conStartDate, "conStartMM", i);
                        }
                        String conEndHH = appGrpPremisesDtoList.get(i).getConEndHH();
                        String conEndMM = appGrpPremisesDtoList.get(i).getConEndMM();
                        if(StringUtil.isEmpty(conEndHH)||StringUtil.isEmpty(conEndMM)){
                            errorMap.put("conEndMM"+i,"UC_CHKLMD001_ERR001");
                        }else {
                            conEndDate = validateTime(errorMap, conEndHH, conEndMM, conEndDate, "conEndMM", i);
                        }
                        if(!StringUtil.isEmpty(conStartHH)&&!StringUtil.isEmpty(conStartMM)&&!StringUtil.isEmpty(conEndHH)&&!StringUtil.isEmpty(conEndMM)){
                            if(conEndDate<conStartDate){
                                errorMap.put("conEndMM"+i,"UC_CHKLMD001_ERR008");
                            }
                        }


                        //set  time
                        String errorStartMM = errorMap.get("conStartMM"+i);
                        String errorEndMM = errorMap.get("conEndMM"+i);
                        if(StringUtil.isEmpty(errorStartMM) && StringUtil.isEmpty(errorEndMM)){
                            LocalTime startTime = LocalTime.of(Integer.parseInt(conStartHH),Integer.parseInt(conStartMM));
                            appGrpPremisesDtoList.get(i).setWrkTimeFrom(Time.valueOf(startTime));

                            LocalTime endTime = LocalTime.of(Integer.parseInt(conEndHH),Integer.parseInt(conEndMM));
                            appGrpPremisesDtoList.get(i).setWrkTimeTo(Time.valueOf(endTime));
                        }

                        List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodList = appGrpPremisesDtoList.get(i).getAppPremPhOpenPeriodList();
                        if(appPremPhOpenPeriodList!=null){
                            for(int j=0;j<appPremPhOpenPeriodList.size();j++){
                                AppPremPhOpenPeriodDto appPremPhOpenPeriodDto = appPremPhOpenPeriodList.get(j);
                                String convEndToHH = appPremPhOpenPeriodDto.getConvEndToHH();
                                String convEndToMM = appPremPhOpenPeriodDto.getConvEndToMM();
                                String convStartFromHH = appPremPhOpenPeriodDto.getConvStartFromHH();
                                String convStartFromMM = appPremPhOpenPeriodDto.getConvStartFromMM();
                                Date phDate = appPremPhOpenPeriodDto.getPhDate();
                                if(!StringUtil.isEmpty(phDate)){
                                    if(StringUtil.isEmpty(convEndToHH)||StringUtil.isEmpty(convEndToMM)){
                                        errorMap.put("convEndToHH"+i+j,"UC_CHKLMD001_ERR001");
                                    }
                                    if(StringUtil.isEmpty(convStartFromHH)||StringUtil.isEmpty(convStartFromMM)){
                                        errorMap.put("convStartToHH"+i+j,"UC_CHKLMD001_ERR001");
                                    }
                                }else if(StringUtil.isEmpty(phDate)){
                                    errorMap.put("convphDate"+i+j,"UC_CHKLMD001_ERR001");
                                }

                                if(StringUtil.isEmpty(convEndToHH)&&StringUtil.isEmpty(convEndToMM)&StringUtil.isEmpty(convStartFromHH)
                                        &StringUtil.isEmpty(convStartFromMM)||!StringUtil.isEmpty(convEndToHH)&&!StringUtil.isEmpty(convEndToMM)
                                        &&!StringUtil.isEmpty(convStartFromHH)&!StringUtil.isEmpty(convStartFromMM)){
                                    if(!StringUtil.isEmpty(convEndToHH)&&!StringUtil.isEmpty(convEndToMM)
                                            &&!StringUtil.isEmpty(convStartFromHH)&!StringUtil.isEmpty(convStartFromMM)){
                                        try {
                                            int i1 = Integer.parseInt(convStartFromHH);
                                            int i2 = Integer.parseInt(convStartFromMM);
                                            if(i1>=24){
                                                errorMap.put("convStartToHH"+i+j,"UC_CHKLMD001_ERR009");
                                            }else if(i2>=60){
                                                errorMap.put("convStartToHH"+i+j,"UC_CHKLMD001_ERR010");
                                            }

                                        }catch (Exception e){
                                            errorMap.put("convStartToHH"+i+j,"CHKLMD001_ERR003");
                                        }
                                        try {
                                            int i3 = Integer.parseInt(convEndToHH);
                                            int i4 = Integer.parseInt(convEndToMM);
                                            if(i3>=24){
                                                errorMap.put("convEndToHH"+i+j,"UC_CHKLMD001_ERR009");
                                            }else if(i4>=60){
                                                errorMap.put("convEndToHH"+i+j,"UC_CHKLMD001_ERR010");
                                            }
                                        }catch (Exception e){
                                            errorMap.put("convEndToHH"+i+j,"CHKLMD001_ERR003");
                                        }
                                        try {
                                            int i1 = Integer.parseInt(convStartFromHH);
                                            int i2 = Integer.parseInt(convStartFromMM);
                                            int i3 = Integer.parseInt(convEndToHH);
                                            int i4 = Integer.parseInt(convEndToMM);
                                            if((i1*60+i2)>(i3*60+i4)){
                                                errorMap.put("convEndToHH"+i+j,"UC_CHKLMD001_ERR008");
                                            }
                                        }catch (Exception e){

                                        }
                                    }
                                }else {
                                    if(StringUtil.isEmpty(convStartFromHH)||StringUtil.isEmpty(convStartFromMM)||StringUtil.isEmpty(convStartFromMM)&&StringUtil.isEmpty(convStartFromHH)){
                                        errorMap.put("convStartToHH"+i+j,"UC_CHKLMD001_ERR001");
                                    }else {
                                        try {
                                            int i1 = Integer.parseInt(convStartFromHH);
                                            int i2 = Integer.parseInt(convStartFromMM);
                                            if(i1>=24){
                                                errorMap.put("convStartToHH"+i+j,"UC_CHKLMD001_ERR009");
                                            }else if(i2>=60){
                                                errorMap.put("convStartToHH"+i+j,"UC_CHKLMD001_ERR010");
                                            }
                                        }catch (Exception e){


                                        }
                                    }
                                    if(StringUtil.isEmpty(convEndToHH)||StringUtil.isEmpty(convEndToMM)||StringUtil.isEmpty(convEndToHH)&&StringUtil.isEmpty(convEndToMM)){
                                        errorMap.put("convEndToHH"+i+j,"UC_CHKLMD001_ERR001");
                                    }else {
                                        try {
                                            int i3 = Integer.parseInt(convEndToHH);
                                            int i4 = Integer.parseInt(convEndToMM);
                                            if(i3>=24){
                                                errorMap.put("convEndToHH"+i+j,"UC_CHKLMD001_ERR009");
                                            }else if(i4>=60){
                                                errorMap.put("convEndToHH"+i+j,"UC_CHKLMD001_ERR010");
                                            }

                                        }catch (Exception e){


                                        }
                                    }
                                }
                                //set ph time
                                String errorConvStartToMM = errorMap.get("convStartToHH"+i+j);
                                String errorConvEndToMM = errorMap.get("convEndToHH"+i+j);
                                if(StringUtil.isEmpty(errorConvStartToMM) && StringUtil.isEmpty(errorConvEndToMM) && !IaisCommonUtils.isEmpty(appPremPhOpenPeriodList)){
                                    LocalTime startTime = LocalTime.of(Integer.parseInt(convStartFromHH),Integer.parseInt(convStartFromMM));
                                    appPremPhOpenPeriodDto.setStartFrom(Time.valueOf(startTime));
                                    LocalTime endTime = LocalTime.of(Integer.parseInt(convEndToHH),Integer.parseInt(convEndToMM));
                                    appPremPhOpenPeriodDto.setEndTo(Time.valueOf(endTime));
                                }
                            }
                        }
                        String conveyanceVehicleNo = appGrpPremisesDtoList.get(i).getConveyanceVehicleNo();
                        validateVehicleNo(errorMap, distinctVehicleNo, i, conveyanceVehicleNo);

                        String cStreetName = appGrpPremisesDtoList.get(i).getConveyanceStreetName();
                        if(StringUtil.isEmpty(cStreetName)){
                            errorMap.put("conveyanceStreetName"+i,"UC_CHKLMD001_ERR001");
                        }
                        String conveyanceAddressType = appGrpPremisesDtoList.get(i).getConveyanceAddressType();
                        if(StringUtil.isEmpty(conveyanceAddressType)){
                            errorMap.put("conveyanceAddressType"+i, "UC_CHKLMD001_ERR001");
                        }else {
                            boolean empty = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getConveyanceFloorNo());
                            boolean empty1 = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getConveyanceBlockNo());
                            boolean empty2 = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getConveyanceUnitNo());
                            if (ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(conveyanceAddressType)) {

                                if (empty) {
                                    errorMap.put("conveyanceFloorNo"+i, "UC_CHKLMD001_ERR001");
                                }
                                if (empty1) {
                                    errorMap.put("conveyanceBlockNos"+i, "UC_CHKLMD001_ERR001");
                                }
                                if (empty2) {
                                    errorMap.put("conveyanceUnitNo"+i, "UC_CHKLMD001_ERR001");
                                }

                            }
                            String floorNoErr = errorMap.get("conveyanceFloorNo"+i);
                            String floorNo = appGrpPremisesDtoList.get(i).getConveyanceFloorNo();
                            if(StringUtil.isEmpty(floorNoErr) && !StringUtil.isEmpty(floorNo)){
                                Pattern pattern = compile("[0-9]*");
                                boolean noFlag =  pattern.matcher(floorNo).matches();
                                if (noFlag) {
                                    int floorNum = Integer.parseInt(floorNo);
                                    if (10 > floorNum) {
                                        floorNo = "0" + floorNum;
                                        appGrpPremisesDtoList.get(i).setConveyanceFloorNo(floorNo);
                                    }
                                }

                            }
                            if(!empty&&!empty1&&!empty2){
                                stringBuilder.append(appGrpPremisesDtoList.get(i).getConveyanceFloorNo())
                                        .append(appGrpPremisesDtoList.get(i).getConveyanceBlockNo())
                                        .append(appGrpPremisesDtoList.get(i).getConveyanceUnitNo());
                            }
                        }
                        String conveyancePostalCode = appGrpPremisesDtoList.get(i).getConveyancePostalCode();
                        if(StringUtil.isEmpty(conveyancePostalCode)){
                            errorMap.put("conveyancePostalCode"+i,"UC_CHKLMD001_ERR001");
                        }else {
                            if(!conveyancePostalCode.matches("^[0-9]{6}$")){
                                errorMap.put("conveyancePostalCode"+i, "NEW_ERR0004");
                            }else {
                                if(!StringUtil.isEmpty(stringBuilder.toString())){
                                    stringBuilder.append(conveyancePostalCode);
                                    if(list.contains(stringBuilder.toString())){
                                        errorMap.put("conveyancePostalCode"+i,"There is a duplicated entry for this premises address.");
                                    }else {
                                        list.add(stringBuilder.toString());
                                    }
                                }
                            }
                        }

                        //0062204
                        String currentHci = conveyanceVehicleNo + IaisCommonUtils.genPremisesKey(conveyancePostalCode,appGrpPremisesDto.getConveyanceBlockNo(),appGrpPremisesDto.getConveyanceFloorNo(),appGrpPremisesDto.getConveyanceUnitNo());
                        String vehicleNo = errorMap.get("conveyanceVehicleNo"+i);
                        String postalCodeErr =errorMap.get("conveyancePostalCode"+i);
                        String blkNoErr =errorMap.get("conveyanceBlockNos"+i);
                        String floorNoErr =errorMap.get("conveyanceFloorNo"+i);
                        String unitNoErr =errorMap.get("conveyanceUnitNo"+i);
                        boolean hciFlag =  StringUtil.isEmpty(vehicleNo) && StringUtil.isEmpty(postalCodeErr) && StringUtil.isEmpty(blkNoErr) && StringUtil.isEmpty(floorNoErr) && StringUtil.isEmpty(unitNoErr);
                        log.info(StringUtil.changeForLog("hciFlag:"+hciFlag));
                        boolean newTypeFlag = ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType());
                        if(newTypeFlag && hciFlag){
                            if(!IaisCommonUtils.isEmpty(premisesHciList) && premisesHciList.contains(currentHci)){
                                errorMap.put("premisesHci"+i,"NEW_ERR0005");
                            }
                        }
                    }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premiseType)){

                        String offSitePostalCode = appGrpPremisesDtoList.get(i).getOffSitePostalCode();
                        if (!StringUtil.isEmpty(offSitePostalCode)) {
                            if (!offSitePostalCode.matches("^[0-9]{6}$")) {
                                errorMap.put("offSitePostalCode"+i, "NEW_ERR0004");
                            }else {

                                if(!StringUtil.isEmpty(stringBuilder.toString())){
                                    stringBuilder.append(offSitePostalCode);
                                    if(list.contains(stringBuilder.toString())){
                                        errorMap.put("offSitePostalCode"+i,"There is a duplicated entry for this premises address");

                                    }else {
                                        list.add(stringBuilder.toString());
                                    }
                                }
                            }
                        }else {
                            errorMap.put("offSitePostalCode"+i, "UC_CHKLMD001_ERR001");
                        }

                        String offSiteStreetName = appGrpPremisesDtoList.get(i).getOffSiteStreetName();
                        if(StringUtil.isEmpty(offSiteStreetName)){
                            errorMap.put("offSiteStreetName"+i,"UC_CHKLMD001_ERR001");
                        }

                        String offSiteAddressType = appGrpPremisesDtoList.get(i).getOffSiteAddressType();

                        if(StringUtil.isEmpty(offSiteAddressType)){
                            errorMap.put("offSiteAddressType"+i, "UC_CHKLMD001_ERR001");
                        }else {
                            boolean empty = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getOffSiteFloorNo());
                            boolean empty1 = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getOffSiteBlockNo());
                            boolean empty2 = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getOffSiteUnitNo());
                            if (ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(offSiteAddressType)) {

                                if (empty) {
                                    errorMap.put("offSiteFloorNo"+i, "UC_CHKLMD001_ERR001");
                                }
                                if (empty1) {
                                    errorMap.put("offSiteBlockNo"+i, "UC_CHKLMD001_ERR001");
                                }
                                if (empty2) {
                                    errorMap.put("offSiteUnitNo"+i, "UC_CHKLMD001_ERR001");
                                }
                            }
                            String floorNoErr = errorMap.get("offSiteFloorNo"+i);
                            String floorNo = appGrpPremisesDtoList.get(i).getOffSiteFloorNo();
                            if(StringUtil.isEmpty(floorNoErr) && !StringUtil.isEmpty(floorNo)){
                                Pattern pattern = compile("[0-9]*");
                                boolean noFlag =  pattern.matcher(floorNo).matches();
                                if (noFlag) {
                                    int floorNum = Integer.parseInt(floorNo);
                                    if (10 > floorNum) {
                                        floorNo = "0" + floorNum;
                                        appGrpPremisesDtoList.get(i).setOffSiteFloorNo(floorNo);
                                    }
                                }

                            }
                            if(!empty&&!empty1&&!empty2){
                                stringBuilder.append(appGrpPremisesDtoList.get(i).getFloorNo())
                                        .append(appGrpPremisesDtoList.get(i).getBlkNo())
                                        .append(appGrpPremisesDtoList.get(i).getUnitNo());
                            }
                        }
                        String offSiteStartHH = appGrpPremisesDtoList.get(i).getOffSiteStartHH();
                        String offSiteStartMM = appGrpPremisesDtoList.get(i).getOffSiteStartMM();
                        int startDate=0;
                        int endDate=0;
                        if(StringUtil.isEmpty(offSiteStartHH)||StringUtil.isEmpty(offSiteStartMM)){
                            errorMap.put("offSiteStartMM"+i,"UC_CHKLMD001_ERR001");
                        }else {
                            startDate = validateTime(errorMap, offSiteStartHH, offSiteStartMM, startDate, "offSiteStartMM", i);
                        }

                        String offSiteEndHH = appGrpPremisesDtoList.get(i).getOffSiteEndHH();
                        String offSiteEndMM = appGrpPremisesDtoList.get(i).getOffSiteEndMM();
                        if(StringUtil.isEmpty(offSiteEndHH)||StringUtil.isEmpty(offSiteEndMM)){
                            errorMap.put("offSiteEndMM"+i,"UC_CHKLMD001_ERR001");
                        }else {
                            endDate = validateTime(errorMap, offSiteEndHH, offSiteEndMM, endDate, "offSiteEndMM", i);
                        }
                        if(!StringUtil.isEmpty(offSiteStartHH)&&!StringUtil.isEmpty(offSiteStartMM)&&!StringUtil.isEmpty(offSiteEndHH)&&!StringUtil.isEmpty(offSiteEndMM)){
                            if(endDate<startDate){
                                errorMap.put("offSiteEndMM"+i,"UC_CHKLMD001_ERR008");
                            }
                        }

                        //set  time
                        String errorStartMM = errorMap.get("offSiteStartMM"+i);
                        String errorEndMM = errorMap.get("offSiteEndMM"+i);
                        if(StringUtil.isEmpty(errorStartMM) && StringUtil.isEmpty(errorEndMM)){
                            LocalTime startTime = LocalTime.of(Integer.parseInt(offSiteStartHH),Integer.parseInt(offSiteStartMM));
                            appGrpPremisesDtoList.get(i).setWrkTimeFrom(Time.valueOf(startTime));

                            LocalTime endTime = LocalTime.of(Integer.parseInt(offSiteEndHH),Integer.parseInt(offSiteEndMM));
                            appGrpPremisesDtoList.get(i).setWrkTimeTo(Time.valueOf(endTime));
                        }

                        List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodList = appGrpPremisesDtoList.get(i).getAppPremPhOpenPeriodList();
                        if(appPremPhOpenPeriodList!=null){

                            for(int j=0;j<appPremPhOpenPeriodList.size();j++){
                                AppPremPhOpenPeriodDto appPremPhOpenPeriodDto = appPremPhOpenPeriodList.get(j);
                                String offSiteEndToHH = appPremPhOpenPeriodDto.getOffSiteEndToHH();
                                String offSiteEndToMM = appPremPhOpenPeriodDto.getOffSiteEndToMM();
                                String offSiteStartFromHH = appPremPhOpenPeriodDto.getOffSiteStartFromHH();
                                String offSiteStartFromMM = appPremPhOpenPeriodDto.getOffSiteStartFromMM();
                                Date phDate = appPremPhOpenPeriodDto.getPhDate();
                                if(!StringUtil.isEmpty(phDate)){
                                    if(StringUtil.isEmpty(offSiteEndToHH)||StringUtil.isEmpty(offSiteEndToMM)){
                                        errorMap.put("offSiteEndToHH"+i+j,"UC_CHKLMD001_ERR001");
                                    }
                                    if(StringUtil.isEmpty(offSiteStartFromHH)||StringUtil.isEmpty(offSiteStartFromMM)){
                                        errorMap.put("offSiteStartToHH"+i+j,"UC_CHKLMD001_ERR001");
                                    }
                                }else if(StringUtil.isEmpty(phDate)){
                                    errorMap.put("offSitephDate"+i+j,"UC_CHKLMD001_ERR001");
                                }

                                if(StringUtil.isEmpty(offSiteEndToHH)&&StringUtil.isEmpty(offSiteEndToMM)&StringUtil.isEmpty(offSiteStartFromHH)
                                        &StringUtil.isEmpty(offSiteStartFromMM)||!StringUtil.isEmpty(offSiteEndToHH)&&!StringUtil.isEmpty(offSiteEndToMM)
                                        &&!StringUtil.isEmpty(offSiteStartFromHH)&!StringUtil.isEmpty(offSiteStartFromMM)){
                                    if(!StringUtil.isEmpty(offSiteEndToHH)&&!StringUtil.isEmpty(offSiteEndToMM)
                                            &&!StringUtil.isEmpty(offSiteEndToHH)&!StringUtil.isEmpty(offSiteStartFromMM)){
                                        try {
                                            int i1 = Integer.parseInt(offSiteStartFromHH);
                                            int i2 = Integer.parseInt(offSiteStartFromMM);
                                            if(i1>=24){
                                                errorMap.put("offSiteStartToHH"+i+j,"UC_CHKLMD001_ERR009");
                                            }else if(i2>=60){
                                                errorMap.put("offSiteStartToHH"+i+j,"UC_CHKLMD001_ERR010");
                                            }

                                        }catch (Exception e){
                                            errorMap.put("offSiteStartToHH"+i+j,"CHKLMD001_ERR003");
                                        }
                                        try {
                                            int i3 = Integer.parseInt(offSiteEndToHH);
                                            int i4 = Integer.parseInt(offSiteEndToMM);
                                            if(i3>=24){
                                                errorMap.put("offSiteEndToHH"+i+j,"UC_CHKLMD001_ERR009");
                                            }else if(i4>=60){
                                                errorMap.put("offSiteEndToHH"+i+j,"UC_CHKLMD001_ERR010");
                                            }
                                        }catch (Exception e){
                                            errorMap.put("offSiteEndToHH"+i+j,"CHKLMD001_ERR003");
                                        }
                                        try {
                                            int i1 = Integer.parseInt(offSiteStartFromHH);
                                            int i2 = Integer.parseInt(offSiteStartFromMM);
                                            int i3 = Integer.parseInt(offSiteEndToHH);
                                            int i4 = Integer.parseInt(offSiteEndToMM);
                                            if((i1*60+i2)>(i3*60+i4)){
                                                errorMap.put("offSiteEndToHH"+i+j,"UC_CHKLMD001_ERR008");
                                            }
                                        }catch (Exception e){

                                        }
                                    }
                                }else {
                                    if(StringUtil.isEmpty(offSiteStartFromHH)||StringUtil.isEmpty(offSiteStartFromMM)||StringUtil.isEmpty(offSiteStartFromHH)&&StringUtil.isEmpty(offSiteStartFromMM)){
                                        errorMap.put("offSiteStartToHH"+i+j,"UC_CHKLMD001_ERR001");
                                    }else {
                                        try {
                                            int i1 = Integer.parseInt(offSiteStartFromHH);
                                            int i2 = Integer.parseInt(offSiteStartFromMM);
                                            if(i1>=24){
                                                errorMap.put("offSiteStartToHH"+i+j,"UC_CHKLMD001_ERR009");
                                            }else if(i2>=60){
                                                errorMap.put("offSiteStartToHH"+i+j,"UC_CHKLMD001_ERR010");
                                            }
                                        }catch (Exception e){
                                            errorMap.put("offSiteStartToHH"+i+j,"CHKLMD001_ERR003");

                                        }
                                    }
                                    if(StringUtil.isEmpty(offSiteEndToHH)||StringUtil.isEmpty(offSiteEndToMM)||StringUtil.isEmpty(offSiteEndToHH)&&StringUtil.isEmpty(offSiteEndToMM)){
                                        errorMap.put("offSiteEndToHH"+i+j,"UC_CHKLMD001_ERR001");
                                    }else {

                                        try {
                                            int i3 = Integer.parseInt(offSiteEndToHH);
                                            int i4 = Integer.parseInt(offSiteEndToMM);
                                            if(i3>=24){
                                                errorMap.put("offSiteEndToHH"+i+j,"UC_CHKLMD001_ERR009");
                                            }else if(i4>=60){
                                                errorMap.put("offSiteEndToHH"+i+j,"UC_CHKLMD001_ERR010");
                                            }

                                        }catch (Exception e){
                                            errorMap.put("offSiteEndToHH"+i+j,"CHKLMD001_ERR003");

                                        }
                                    }
                                }

                                //set ph time
                                String errorOffSiteStartToMM = errorMap.get("offSiteStartToHH"+i+j);
                                String errorOffSiteEndToMM = errorMap.get("offSiteEndToHH"+i+j);
                                if(StringUtil.isEmpty(errorOffSiteStartToMM) && StringUtil.isEmpty(errorOffSiteEndToMM) && !IaisCommonUtils.isEmpty(appPremPhOpenPeriodList)){
                                    LocalTime startTime = LocalTime.of(Integer.parseInt(offSiteStartFromHH),Integer.parseInt(offSiteStartFromMM));
                                    appPremPhOpenPeriodDto.setStartFrom(Time.valueOf(startTime));
                                    LocalTime endTime = LocalTime.of(Integer.parseInt(offSiteEndToHH),Integer.parseInt(offSiteEndToMM));
                                    appPremPhOpenPeriodDto.setEndTo(Time.valueOf(endTime));
                                }
                            }
                        }
                        //0062204
                        String currentHci = IaisCommonUtils.genPremisesKey(offSitePostalCode,appGrpPremisesDto.getOffSiteBlockNo(),appGrpPremisesDto.getOffSiteFloorNo(),appGrpPremisesDto.getOffSiteUnitNo());
                        String postalCodeErr =errorMap.get("offSitePostalCode"+i);
                        String blkNoErr =errorMap.get("offSiteBlockNo"+i);
                        String floorNoErr =errorMap.get("offSiteFloorNo"+i);
                        String unitNoErr =errorMap.get("offSiteUnitNo"+i);
                        boolean hciFlag = StringUtil.isEmpty(postalCodeErr) && StringUtil.isEmpty(blkNoErr) && StringUtil.isEmpty(floorNoErr) && StringUtil.isEmpty(unitNoErr);
                        log.info(StringUtil.changeForLog("hciFlag:"+hciFlag));
                        boolean newTypeFlag = ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType());
                        if(newTypeFlag && hciFlag){
                            if(!IaisCommonUtils.isEmpty(premisesHciList) && premisesHciList.contains(currentHci)){
                                errorMap.put("premisesHci"+i,"NEW_ERR0005");
                            }
                        }

                    }


                } else {
                    //premiseSelect = organization hci code

                    if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premiseType)){
                        String conveyanceVehicleNo = appGrpPremisesDtoList.get(i).getConveyanceVehicleNo();
                        validateVehicleNo(errorMap, distinctVehicleNo, i, conveyanceVehicleNo);
                    }

                }
            }
        }
        log.info(StringUtil.changeForLog("the do doValidatePremiss end ...."));

        return errorMap;
    }

    private static void validateVehicleNo(Map<String, String> errorMap, Set<String> distinctVehicleNo, int numberCount, String conveyanceVehicleNo){
        if(StringUtil.isEmpty(conveyanceVehicleNo)){
            errorMap.put("conveyanceVehicleNo"+numberCount,"UC_CHKLMD001_ERR001");
        }else {
            boolean b = VehNoValidator.validateNumber(conveyanceVehicleNo);
            if(!b){
                errorMap.put("conveyanceVehicleNo"+numberCount,"CHKLMD001_ERR008");
            }

            if (distinctVehicleNo.contains(conveyanceVehicleNo)){
                errorMap.put("conveyanceVehicleNo"+numberCount, "CHKLMD001_ERR009");
            }else {
                distinctVehicleNo.add(conveyanceVehicleNo);
            }
        }
    }



    private Map<String,String> doValidatePremissCgo(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the do doValidatePremiss start ...."));
        //do validate premiss
        Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
        AppSvcCgoDto appSvcCgoDto=  (AppSvcCgoDto) ParamUtil.getSessionAttr(bpc.request,"AppSvcCgoDto");
        String mobileNo = appSvcCgoDto.getMobileNo();
        String emailAddr = appSvcCgoDto.getEmailAddr();
        if(!mobileNo.startsWith("8")||!mobileNo.startsWith("9")){
            errorMap.put("mobileNo","Please key in a valid mobile number");
        }
        if(!emailAddr.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")){
            errorMap.put("emailAddr","Please key in a valid email address");
        }
        log.info(StringUtil.changeForLog("the do doValidatePremiss end ...."));
        return errorMap;
    }

    private static AppSubmissionDto getAppSubmissionDto(HttpServletRequest request){
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, APPSUBMISSIONDTO);
        if(appSubmissionDto == null){
            log.info(StringUtil.changeForLog("appSubmissionDto is empty "));
            appSubmissionDto = new AppSubmissionDto();
        }
        return appSubmissionDto;
    }

    private void initSession(BaseProcessClass bpc) throws CloneNotSupportedException {
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        if(appSubmissionDto == null){
            appSubmissionDto = new AppSubmissionDto();
            appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
            List<AppGrpPremisesDto> appGrpPremisesDtoList = IaisCommonUtils.genNewArrayList();
            AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
            appGrpPremisesDtoList.add(appGrpPremisesDto);
            appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtoList);
            List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request,  AppServicesConsts.HCSASERVICEDTOLIST);
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = IaisCommonUtils.genNewArrayList();
            AppSvcRelatedInfoDto appSvcRelatedInfoDto = null;
            for(HcsaServiceDto svc:hcsaServiceDtos){
                appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
                appSvcRelatedInfoDto.setServiceId(svc.getId());
                appSvcRelatedInfoDto.setServiceCode(svc.getSvcCode());
                appSvcRelatedInfoDto.setServiceType(svc.getSvcType());
                appSvcRelatedInfoDto.setServiceName(svc.getSvcName());
                appSvcRelatedInfoDtoList.add(appSvcRelatedInfoDto);
            }
            appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtoList);
            //set licseeId and psn drop down
            setLicseeAndPsnDropDown(appSubmissionDto,bpc);
        }else{
            //set svc info,this fun will set oldAppSubmission
            appSubmissionDto = NewApplicationHelper.setSubmissionDtoSvcData(bpc.request, appSubmissionDto);
            Object rfi = ParamUtil.getSessionAttr(bpc.request,REQUESTINFORMATIONCONFIG);
            //rfi just show one service
            if(rfi != null){
                List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request,AppServicesConsts.HCSASERVICEDTOLIST);
                List<HcsaServiceDto> oneHcsaServiceDto = IaisCommonUtils.genNewArrayList();
                for(HcsaServiceDto hcsaServiceDto:hcsaServiceDtos){
                    if(hcsaServiceDto.getId().equals(appSubmissionDto.getRfiServiceId())){
                        oneHcsaServiceDto.add(hcsaServiceDto);
                        break;
                    }
                }
                ParamUtil.setSessionAttr(bpc.request,"rfiHcsaService", (Serializable) hcsaServiceDtos);
                ParamUtil.setSessionAttr(bpc.request,AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) oneHcsaServiceDto);
            }

            //set premises info
            List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
            List<SelectOption> publicHolidayList = serviceConfigService.getPubHolidaySelect();
            if(!IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
                for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                    appGrpPremisesDto = NewApplicationHelper.setWrkTime(appGrpPremisesDto);
                    List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodDtos = appGrpPremisesDto.getAppPremPhOpenPeriodList();
                    if(!IaisCommonUtils.isEmpty(appPremPhOpenPeriodDtos)){
                        for(AppPremPhOpenPeriodDto appPremPhOpenPeriodDto:appPremPhOpenPeriodDtos){
                            String dayName = appPremPhOpenPeriodDto.getDayName();
                            String phDateStr = appPremPhOpenPeriodDto.getPhDateStr();
                            if(StringUtil.isEmpty(dayName) && !StringUtil.isEmpty(phDateStr)){
                                dayName = NewApplicationHelper.getPhName(publicHolidayList,phDateStr);
                                appPremPhOpenPeriodDto.setDayName(dayName);
                            }
                        }
                    }
                    appGrpPremisesDto.setAppPremPhOpenPeriodList(appPremPhOpenPeriodDtos);
                }
            }

            //set licseeId and psn drop down
            setLicseeAndPsnDropDown(appSubmissionDto,bpc);

            Map<String,AppSvcPersonAndExtDto> personMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.PERSONSELECTMAP);
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
                List<HcsaSvcDocConfigDto> primaryDocConfig = serviceConfigService.getAllHcsaSvcDocs(null);
                List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                    String currentSvcId = appSvcRelatedInfoDto.getServiceId();
                    List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = null;
                    if(!StringUtil.isEmpty(currentSvcId)){
                        hcsaSvcSubtypeOrSubsumedDtos= serviceConfigService.loadLaboratoryDisciplines(currentSvcId);
                        //set doc name
                        List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                        List<HcsaSvcDocConfigDto> svcDocConfig = serviceConfigService.getAllHcsaSvcDocs(currentSvcId);
                        NewApplicationHelper.setDocInfo(appGrpPrimaryDocDtos,appSvcDocDtos,primaryDocConfig,svcDocConfig);
                    }
                    //set AppSvcLaboratoryDisciplinesDto
                    if(!IaisCommonUtils.isEmpty(hcsaSvcSubtypeOrSubsumedDtos)){
                        NewApplicationHelper.setLaboratoryDisciplinesInfo(appGrpPremisesDtos,appSvcRelatedInfoDto,hcsaSvcSubtypeOrSubsumedDtos);
                    }
                    //set AppSvcDisciplineAllocationDto
                    //NewApplicationHelper.setDisciplineAllocationDtoInfo(appSvcRelatedInfoDto);
                    if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())
                            ||ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())
                            || rfi != null){
                        //gen dropdown map
                        String svcCode = appSvcRelatedInfoDto.getServiceCode();
                        List<AppSvcPrincipalOfficersDto> appSvcCgoDtos = NewApplicationHelper.transferCgoToPsnDtoList(appSvcRelatedInfoDto.getAppSvcCgoDtoList());
                        NewApplicationHelper.initSetPsnIntoSelMap(personMap,appSvcCgoDtos,svcCode);
                        //reset dto
                        List<AppSvcCgoDto> newCgoDtoList = IaisCommonUtils.genNewArrayList();
                        for(AppSvcPrincipalOfficersDto item:appSvcCgoDtos){
                            newCgoDtoList.add(MiscUtil.transferEntityDto(item,AppSvcCgoDto.class));
                        }
                        appSvcRelatedInfoDto.setAppSvcCgoDtoList(newCgoDtoList);
                        NewApplicationHelper.initSetPsnIntoSelMap(personMap,appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList(),svcCode);
                        NewApplicationHelper.initSetPsnIntoSelMap(personMap,appSvcRelatedInfoDto.getAppSvcMedAlertPersonList(),svcCode);
                    }
                    //set dpo select flag
                    List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
                    if(!IaisCommonUtils.isEmpty(appSvcPrincipalOfficersDtos)){
                        for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto:appSvcPrincipalOfficersDtos){
                            if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(appSvcPrincipalOfficersDto.getPsnType())){
                                appSvcRelatedInfoDto.setDeputyPoFlag(AppConsts.YES);
                                break;
                            }
                        }
                    }
                }
            }
            if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType()) || rfi != null){
                //set oldAppSubmission when rfi,rfc,renew
                AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto)CopyUtil.copyMutableObject(appSubmissionDto);
                ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.OLDAPPSUBMISSIONDTO,oldAppSubmissionDto);
            }else if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())){
                AppSubmissionDto oldAppSubmissionDto = appSubmissionDto.getOldAppSubmissionDto();
                ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.OLDAPPSUBMISSIONDTO,oldAppSubmissionDto);
            }
        }
        AppEditSelectDto changeSelectDto1 = appSubmissionDto.getChangeSelectDto()==null?new AppEditSelectDto():appSubmissionDto.getChangeSelectDto();
        appSubmissionDto.setChangeSelectDto(changeSelectDto1);

        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        ParamUtil.setSessionAttr(bpc.request, "IndexNoCount", 0);

        //reload
        Map<String,AppGrpPrimaryDocDto> initBeforeReloadDocMap = IaisCommonUtils.genNewHashMap();
        ParamUtil.setSessionAttr(bpc.request, RELOADAPPGRPPRIMARYDOCMAP, (Serializable) initBeforeReloadDocMap);

        //error_msg
        ParamUtil.setSessionAttr(bpc.request, ERRORMAP_PREMISES, null);
        ParamUtil.setSessionAttr(bpc.request, APPGRPPRIMARYDOCERRMSGMAP, null);

        //init svc psn conifg
        Map<String,List<HcsaSvcPersonnelDto>> svcConfigInfo =  null;
        ParamUtil.setSessionAttr(bpc.request, SERVICEALLPSNCONFIGMAP, (Serializable) svcConfigInfo);

    }

    private void initOldSession(BaseProcessClass bpc) throws CloneNotSupportedException {
        AppSubmissionDto test1= (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,OLDAPPSUBMISSIONDTO);
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, "oldSubmitAppSubmissionDto");
        if(appSubmissionDto != null){
            appSubmissionDto = NewApplicationHelper.setSubmissionDtoSvcData(bpc.request, appSubmissionDto);
            Object rfi = ParamUtil.getSessionAttr(bpc.request,REQUESTINFORMATIONCONFIG);
            //rfi just show one service
            if(rfi != null){
                List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request,AppServicesConsts.HCSASERVICEDTOLIST);
                List<HcsaServiceDto> oneHcsaServiceDto = IaisCommonUtils.genNewArrayList();
                for(HcsaServiceDto hcsaServiceDto:hcsaServiceDtos){
                    if(hcsaServiceDto.getId().equals(appSubmissionDto.getRfiServiceId())){
                        oneHcsaServiceDto.add(hcsaServiceDto);
                        break;
                    }
                }
                ParamUtil.setSessionAttr(bpc.request,AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) oneHcsaServiceDto);
            }

            //set premises info
            List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
            if(!IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
                for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                    appGrpPremisesDto = NewApplicationHelper.setWrkTime(appGrpPremisesDto);
                }
            }
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
                List<HcsaSvcDocConfigDto> primaryDocConfig = serviceConfigService.getAllHcsaSvcDocs(null);
                List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                    String currentSvcId = appSvcRelatedInfoDto.getServiceId();
                    List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = null;
                    if(!StringUtil.isEmpty(currentSvcId)){
                        hcsaSvcSubtypeOrSubsumedDtos= serviceConfigService.loadLaboratoryDisciplines(currentSvcId);
                        //set doc name
                        List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                        List<HcsaSvcDocConfigDto> svcDocConfig = serviceConfigService.getAllHcsaSvcDocs(currentSvcId);
                        NewApplicationHelper.setDocInfo(appGrpPrimaryDocDtos,appSvcDocDtos,primaryDocConfig,svcDocConfig);
                    }
                    //set AppSvcLaboratoryDisciplinesDto
                    if(!IaisCommonUtils.isEmpty(hcsaSvcSubtypeOrSubsumedDtos)){
                        NewApplicationHelper.setLaboratoryDisciplinesInfo(appGrpPremisesDtos,appSvcRelatedInfoDto,hcsaSvcSubtypeOrSubsumedDtos);
                    }
                    //set AppSvcDisciplineAllocationDto
                    //NewApplicationHelper.setDisciplineAllocationDtoInfo(appSvcRelatedInfoDto);
                    if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())
                            ||ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())
                            || rfi != null){
                        //gen dropdown map
                        String svcCode = appSvcRelatedInfoDto.getServiceCode();
                        List<AppSvcPrincipalOfficersDto> appSvcCgoDtos = NewApplicationHelper.transferCgoToPsnDtoList(appSvcRelatedInfoDto.getAppSvcCgoDtoList());
                        Map<String,AppSvcPersonAndExtDto> personMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.PERSONSELECTMAP);
                        NewApplicationHelper.setPsnIntoSelMap(personMap,appSvcCgoDtos,svcCode);
                        NewApplicationHelper.setPsnIntoSelMap(personMap,appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList(),svcCode);
                        NewApplicationHelper.setPsnIntoSelMap(personMap,appSvcRelatedInfoDto.getAppSvcMedAlertPersonList(),svcCode);
                    }
                }
            }
            if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType()) || rfi != null){
                //set oldAppSubmission when rfi,rfc,renew
                AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto)CopyUtil.copyMutableObject(appSubmissionDto);
                ParamUtil.setSessionAttr(bpc.request,"oldSubmitAppSubmissionDto",oldAppSubmissionDto);

            }else if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())){
                AppSubmissionDto oldAppSubmissionDto = appSubmissionDto.getOldAppSubmissionDto();
                ParamUtil.setSessionAttr(bpc.request,"oldSubmitAppSubmissionDto",oldAppSubmissionDto);

            }
            AppSubmissionDto test= (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,OLDAPPSUBMISSIONDTO);
            log.info("");
        }

    }

    private void  documentValid(HttpServletRequest request, Map<String,String> errorMap){
        log.info(StringUtil.changeForLog("the do doValidatePremiss start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList  = appSubmissionDto.getAppGrpPrimaryDocDtos();
        if(appGrpPrimaryDocDtoList == null){
            return;
        }
        for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDocDtoList){
            String keyName = "";
            if(StringUtil.isEmpty(appGrpPrimaryDocDto.getPremisessName()) && StringUtil.isEmpty(appGrpPrimaryDocDto.getPremisessType())){
                //common
                keyName = "common"+appGrpPrimaryDocDto.getSvcComDocId();
            }else{
                keyName = "prem"+appGrpPrimaryDocDto.getSvcComDocId()+appGrpPrimaryDocDto.getPremisessName();
            }
            long length = appGrpPrimaryDocDto.getRealDocSize();
            int uploadFileLimit = systemParamConfig.getUploadFileLimit();
            if(length/1024/1024 >uploadFileLimit){
                errorMap.put(keyName,MessageUtil.replaceMessage("GENERAL_ERR0019", String.valueOf(uploadFileLimit),"sizeMax"));
                continue;
            }
            Boolean flag=Boolean.FALSE;
            String name = appGrpPrimaryDocDto.getDocName();
            String substring = name.substring(name.lastIndexOf('.')+1);
            String sysFileType = systemParamConfig.getUploadFileType();
            String[] sysFileTypeArr = FileUtils.fileTypeToArray(sysFileType);
            for(String f:sysFileTypeArr){
                if(f.equalsIgnoreCase(substring)){
                    flag=Boolean.TRUE;
                }
            }
            if(!flag){
                errorMap.put(keyName,MessageUtil.replaceMessage("GENERAL_ERR0018", sysFileType,"fileType"));
            }
            String errMsg = errorMap.get(keyName);
            if(StringUtil.isEmpty(errMsg)){
                appGrpPrimaryDocDto.setPassValidate(true);
            }
        }
        appSubmissionDto.setAppGrpPrimaryDocDtos(appGrpPrimaryDocDtoList);
        ParamUtil.setSessionAttr(request,APPSUBMISSIONDTO,appSubmissionDto);
    }

    private Map<String,List<HcsaSvcPersonnelDto>> getAllSvcAllPsnConfig(HttpServletRequest request){
        Map<String,List<HcsaSvcPersonnelDto>> svcAllPsnConfig = (Map<String, List<HcsaSvcPersonnelDto>>) ParamUtil.getSessionAttr(request, SERVICEALLPSNCONFIGMAP);
        if(svcAllPsnConfig == null){
            AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            List<String> svcIds = IaisCommonUtils.genNewArrayList();
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                svcIds.add(appSvcRelatedInfoDto.getServiceId());
            }
            List<HcsaServiceStepSchemeDto>  svcStepConfigs = serviceConfigService.getHcsaServiceStepSchemesByServiceId(svcIds);
            svcAllPsnConfig = serviceConfigService.getAllSvcAllPsnConfig(svcStepConfigs, svcIds);
        }
        return svcAllPsnConfig;
    }

    private void loadingNewAppInfo(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the do loadingSpecifiedInfo start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,APPSUBMISSIONDTO);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = (List<AppSvcRelatedInfoDto>) ParamUtil.getSessionAttr(bpc.request,ServiceMenuDelegator.APP_SVC_RELATED_INFO_LIST);
        AppSelectSvcDto appSelectSvcDto = (AppSelectSvcDto) ParamUtil.getSessionAttr(bpc.request,ServiceMenuDelegator.APP_SELECT_SERVICE);
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos) && appSubmissionDto == null && appSelectSvcDto != null){
            appSubmissionDto = new AppSubmissionDto();
            appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
            appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtos);
            String premisesId = "";
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                String premId = appSvcRelatedInfoDto.getLicPremisesId();
                if(!StringUtil.isEmpty(premId) && !"-1".equals(premId)){
                    premisesId = premId;
                    break;
                }
            }
            if(!StringUtil.isEmpty(premisesId)){
                List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionService.getLicPremisesInfo(premisesId);
                for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                    appGrpPremisesDto.setPremisesSelect(NewApplicationConstant.NEW_PREMISES);
                }
                appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtos);
            }else{
                List<AppGrpPremisesDto> appGrpPremisesDtos = IaisCommonUtils.genNewArrayList();
                AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
                appGrpPremisesDtos.add(appGrpPremisesDto);
                appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtos);
            }
            ParamUtil.setSessionAttr(bpc.request,APPSUBMISSIONDTO,appSubmissionDto  );
        }
        log.info(StringUtil.changeForLog("the do loadingSpecifiedInfo start ...."));
    }

    private static List<AppGrpPremisesDto> getAppGrpPremisesDtos(AppSubmissionDto appSubmissionDto){
        List<AppGrpPremisesDto> appGrpPremisesDtos = IaisCommonUtils.genNewArrayList();
        if(appSubmissionDto != null){
            appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        }
        return appGrpPremisesDtos;
    }

    private static void jumpToAckPage(BaseProcessClass bpc, String ackStatus, String errorMsg){
        String actionType = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(StringUtil.isEmpty(actionType)){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "errorAck");
            if(NewApplicationConstant.ACK_STATUS_ERROR.equals(ackStatus)){
                ParamUtil.setRequestAttr(bpc.request,ACKSTATUS,"error");
                ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, errorMsg);
            }
        }
    }

    private void setLicseeAndPsnDropDown(AppSubmissionDto appSubmissionDto,BaseProcessClass bpc){
        //set licenseeId
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
        Map<String,AppSvcPersonAndExtDto> licPersonMap = IaisCommonUtils.genNewHashMap();
        if(loginContext != null){
            appSubmissionDto.setLicenseeId(loginContext.getLicenseeId());
            List<PersonnelListQueryDto> licPersonList = requestForChangeService.getLicencePersonnelListQueryDto(loginContext.getLicenseeId());
            licPersonMap = NewApplicationHelper.getLicPsnIntoSelMap(licPersonList);
            ParamUtil.setSessionAttr(bpc.request,LICPERSONSELECTMAP, (Serializable) licPersonMap);
            Object draft = ParamUtil.getSessionAttr(bpc.request,DRAFTCONFIG);
            //set data into psnMap
            Map<String,AppSvcPersonAndExtDto> personMap = IaisCommonUtils.genNewHashMap();
            personMap.putAll(licPersonMap);
            if(draft != null){
                List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
                if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
                    for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                        String svcCode = appSvcRelatedInfoDto.getServiceCode();
                        List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                        List<AppSvcPrincipalOfficersDto> appSvcCgoDtos = NewApplicationHelper.transferCgoToPsnDtoList(appSvcCgoDtoList);
                        personMap = NewApplicationHelper.initSetPsnIntoSelMap(personMap, appSvcCgoDtos, svcCode);
                        personMap = NewApplicationHelper.initSetPsnIntoSelMap(personMap, appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList(), svcCode);
                        personMap = NewApplicationHelper.initSetPsnIntoSelMap(personMap, appSvcRelatedInfoDto.getAppSvcMedAlertPersonList(), svcCode);
                    }
                }
            }
            ParamUtil.setSessionAttr(bpc.request,PERSONSELECTMAP, (Serializable) personMap);
        }else{
            appSubmissionDto.setLicenseeId("");
            ParamUtil.setSessionAttr(bpc.request,LICPERSONSELECTMAP, (Serializable) licPersonMap);
            log.info(StringUtil.changeForLog("user info is empty....."));
        }
        //change
//        Map<String,AppSvcPrincipalOfficersDto> licPersonMap = IaisCommonUtils.genNewHashMap();
//        if(loginContext != null){
//            appSubmissionDto.setLicenseeId(loginContext.getLicenseeId());
//            List<PersonnelListQueryDto> licPersonList = requestForChangeService.getLicencePersonnelListQueryDto(loginContext.getLicenseeId());
//            //exchange order
//            licPersonMap = NewApplicationHelper.getLicPsnIntoSelMap(bpc.request,licPersonList);
//            licPersonMap.forEach((k,v)->{
//                //set empty field
//                AppPsnEditDto appPsnEditDto = NewApplicationHelper.setNeedEditField(v);
//                v.setPsnEditDto(appPsnEditDto);
//            });
//            ParamUtil.setSessionAttr(bpc.request,LICPERSONSELECTMAP, (Serializable) licPersonMap);
//            Map<String,AppSvcPrincipalOfficersDto> personMap = (Map<String, AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(bpc.request, PERSONSELECTMAP);
//            if(personMap != null){
//                licPersonMap.forEach((k,v)->{
//                    personMap.put(k,v);
//                });
//                ParamUtil.setSessionAttr(bpc.request,PERSONSELECTMAP, (Serializable) personMap);
//            }else{
//                ParamUtil.setSessionAttr(bpc.request,PERSONSELECTMAP, (Serializable) licPersonMap);
//            }
//        }else{
//            appSubmissionDto.setLicenseeId("");
//            ParamUtil.setSessionAttr(bpc.request,LICPERSONSELECTMAP, (Serializable) licPersonMap);
//            log.info(StringUtil.changeForLog("user info is empty....."));
//        }
    }

    private void setPsnDroTo(AppSubmissionDto appSubmissionDto,BaseProcessClass bpc){
        Map<String,AppSvcPrincipalOfficersDto> personMap = (Map<String, AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.PERSONSELECTMAP);
        String personMapStr = JsonUtil.parseToJson(personMap);
        appSubmissionDto.setDropDownPsnMapStr(personMapStr);
    }
    private List<AppGrpPremisesDto> copyAppGrpPremises( List<AppGrpPremisesDto> appGrpPremisesDtoList) throws Exception{
        List<AppGrpPremisesDto> n=( List<AppGrpPremisesDto>)CopyUtil.copyMutableObject(appGrpPremisesDtoList);
        for(AppGrpPremisesDto appGrpPremisesDto : n){
            appGrpPremisesDto.setLicenceDtos(null);
            if(StringUtil.isEmpty(appGrpPremisesDto.getOffTelNo())){
                appGrpPremisesDto.setOffTelNo(null);
            }
            if(StringUtil.isEmpty(appGrpPremisesDto.getCertIssuedDtStr())){
                appGrpPremisesDto.setCertIssuedDtStr(null);
            }
            appGrpPremisesDto.setExistingData(null);
        }
        return n;
    }

    private boolean eqGrpPremises( List<AppGrpPremisesDto> appGrpPremisesDtoList, List<AppGrpPremisesDto> oldAppGrpPremisesDtoList) throws  Exception{
        List<AppGrpPremisesDto> appGrpPremisesDtos = copyAppGrpPremises(appGrpPremisesDtoList);
        List<AppGrpPremisesDto> oldAppGrpPremisesDtos = copyAppGrpPremises(oldAppGrpPremisesDtoList);
        if(!appGrpPremisesDtos.equals(oldAppGrpPremisesDtos)){
            return true;
        }
        return false;
    }
}

