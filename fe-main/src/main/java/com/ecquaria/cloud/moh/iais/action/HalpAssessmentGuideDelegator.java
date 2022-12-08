package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.assessment.guide.GuideConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppAlignAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppLicBundleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationSubDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AppAlignLicQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.MenuLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnlAssessQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.SelfPremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserRoleAccessMatrixDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.dto.AppSelectSvcDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.memorypage.PaginationHandler;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.HalpSearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AssessmentGuideService;
import com.ecquaria.cloud.moh.iais.service.InboxService;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.client.AppInboxClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceInboxClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * HalpAssessmentGuideDelegator
 *
 * @author junyu
 * @date 2020/6/8
 */
@Delegator(value = "halpAssessmentGuideDelegator")
@Slf4j
public class HalpAssessmentGuideDelegator {

    private static final String BASE_SERVICE_ATTR = "baseService";
    private static final String SPECIFIED_SERVICE_ATTR = "specifiedService";
    private static final String BASE_SERVICE = "SVTP001";
    private static final String SPECIFIED_SERVICE = "SVTP003";

    private static final String BASE_SERVICE_CHECK_BOX_ATTR = "basechk";
    private static final String SPECIFIED_SERVICE_CHECK_BOX_ATTR = "sepcifiedchk";
    private static final String BASE_SERVICE_ATTR_CHECKED = "baseServiceChecked";
    private static final String SPECIFIED_SERVICE_ATTR_CHECKED = "specifiedServiceChecked";
    private static final String VALIDATION_ATTR = "switch_action_type";
    private static final String CRUD_ACTION_ADDITIONAL = "crud_action_additional";

    private static final String CHOOSE_BASE_SVC = "chooseBaseSvc";
    private static final String CHOOSE_SERVICE= "chooseSvc";
    private static final String CHOOSE_ALIGN = "chooseAlign";
    private static final String CHOOSE_LICENCE = "chooseLic";
    private static final String ERROR_ATTR = "err";
    private static final String ERROR_ATTR_LIST = "errList";
    private static final String BACK_ATTR = "back";
    private static final String NEXT = "next";

    public static final String APP_SVC_RELATED_INFO_LIST = "appSvcRelatedInfoList";
    public static final String APP_SELECT_SERVICE = "appSelectSvc";
    private static final String HAS_EXISTING_BASE = "hasExistingBase";
    private static final String ONLY_BASE_SVC = "onlyBaseSvc";
    private static final String BASEANDSPCSVCMAP = "baseAndSpcSvcMap";
    private static final String RETAIN_LIC_PREMISES_LIST =  "retainLicPremisesList";
    private static final String NO_EXIST_BASE_LIC = "noExistBaseLic";
    private static final String NO_EXIST_BASE_APP = "noExistBaseApp";
    private static final String DRAFT_NUMBER                                 = "DraftNumber";

    private static final String RELOAD_BASE_SVC_SELECTED = "reloadBaseSvcSelected";
    private static final String BASE_LIC_PREMISES_MAP = "baseLicPremisesMap";
    public static final String SELECT_DRAFT_NO          ="selectDraftNo";
    private static final String LIC_ALIGN_SEARCH_PARAM = "licAlignSearchParam";
    private static final String LIC_ALIGN_SEARCH_RESULT= "licAlignSearchResult";
    private static final String BASE_SVC_PREMISES_MAP = "baseSvcPremisesMap";
    private static final String MANDATORY_ERR_MSG = "GENERAL_ERR0006";

    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private InboxService inboxService;
    @Autowired
    AppInboxClient appInboxClient;
    @Autowired
    private LicenceInboxClient licenceInboxClient;
    @Autowired
    RequestForChangeService requestForChangeService;
    @Autowired
    OrgUserManageService orgUserManageService;
    @Autowired
    AssessmentGuideService assessmentGuideService;
    @Autowired
    private SystemParamConfig systemParamConfig;

    public void start(BaseProcessClass bpc) {
        log.info("****start ******");
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_MAIN_FUNCTION, AuditTrailConsts.FUNCTION_ASEESSMENT_GUIDE);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        log.info("****end ******");
        ParamUtil.setSessionAttr(bpc.request, "personnelOptions", null);
        ParamUtil.setSessionAttr(bpc.request, GuideConsts.RENEW_LICENCE_SEARCH_PARAM, null);
        ParamUtil.setSessionAttr(bpc.request, GuideConsts.RENEW_LICENCE_UPDATE_SEARCH_PARAM, null);
        ParamUtil.setSessionAttr(bpc.request, GuideConsts.AMEND_DETAILS_SEARCH_PARAM, null);
        ParamUtil.setSessionAttr(bpc.request, RfcConst.PREMISESLISTDTOS, null);
        ParamUtil.setSessionAttr(bpc.request, GuideConsts.AMEND_DETAILS_REMOVE_SEARCH_PARAM, null);
        ParamUtil.setSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_LICENSEE_SEARCH_PARAM, null);
        ParamUtil.setSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_CONTACT_SEARCH_PARAM, null);
        ParamUtil.setSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_VEHICLES_SEARCH_PARAM, null);
        ParamUtil.setSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_VEHICLES_SEARCH_RESULT, null);
        ParamUtil.setSessionAttr(bpc.request, GuideConsts.CEASE_LICENCE_SEARCH_PARAM, null);
        ParamUtil.setSessionAttr(bpc.request, GuideConsts.WITHDRAW_APPLICATION_SEARCH_PARAM, null);
        ParamUtil.setSessionAttr(bpc.request, GuideConsts.DRAFT_APPLICATION_SEARCH_PARAM, null);
        ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR_CHECKED, null);
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR_CHECKED, null);
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR, null);
        ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR, null);
        String inbox_ack016 = MessageUtil.getMessageDesc("INBOX_ACK016");
        String inbox_ack017 = MessageUtil.getMessageDesc("INBOX_ACK017");
        String inbox_ack018 = MessageUtil.getMessageDesc("INBOX_ACK018");
        String inbox_ack019 = MessageUtil.getMessageDesc("INBOX_ACK019");
        String inbox_ack020 = MessageUtil.getMessageDesc("INBOX_ACK020");
        String inbox_ack021 = MessageUtil.getMessageDesc("INBOX_ACK021");
        String inbox_ack022 = MessageUtil.getMessageDesc("INBOX_ACK022");
        String inbox_ack023 = MessageUtil.getMessageDesc("INBOX_ACK023");
        String inbox_ack024 = MessageUtil.getMessageDesc("INBOX_ACK024");
        String self_ack001 = MessageUtil.getMessageDesc("SELF_ACK001");
        String self_ack002 = MessageUtil.getMessageDesc("SELF_ACK002");
        String self_ack003 = MessageUtil.getMessageDesc("SELF_ACK003");
        String self_ack004 = MessageUtil.getMessageDesc("SELF_ACK004");
        String self_ack005 = MessageUtil.getMessageDesc("SELF_ACK005");
        String self_ack006 = MessageUtil.getMessageDesc("SELF_ACK006");
        String self_ack007 = MessageUtil.getMessageDesc("SELF_ACK007");
        String self_ack008 = MessageUtil.getMessageDesc("SELF_ACK008");
        String self_ack009 = MessageUtil.getMessageDesc("SELF_ACK009");
        String self_ack010 = MessageUtil.getMessageDesc("SELF_ACK010");
        String self_ack011 = MessageUtil.getMessageDesc("SELF_ACK011");
        String self_ack012 = MessageUtil.getMessageDesc("SELF_ACK012");
        String self_ack013 = MessageUtil.getMessageDesc("SELF_ACK013");
        String self_ack014 = MessageUtil.getMessageDesc("SELF_ACK014");
        String self_ack015 = MessageUtil.getMessageDesc("SELF_ACK015");

        ParamUtil.setSessionAttr(bpc.request,"inbox_ack016",inbox_ack016);
        ParamUtil.setSessionAttr(bpc.request,"inbox_ack017",inbox_ack017);
        ParamUtil.setSessionAttr(bpc.request,"inbox_ack018",inbox_ack018);
        ParamUtil.setSessionAttr(bpc.request,"inbox_ack019",inbox_ack019);
        ParamUtil.setSessionAttr(bpc.request,"inbox_ack020",inbox_ack020);
        ParamUtil.setSessionAttr(bpc.request,"inbox_ack021",inbox_ack021);
        ParamUtil.setSessionAttr(bpc.request,"inbox_ack022",inbox_ack022);
        ParamUtil.setSessionAttr(bpc.request,"inbox_ack023",inbox_ack023);
        ParamUtil.setSessionAttr(bpc.request,"inbox_ack024",inbox_ack024);
        ParamUtil.setSessionAttr(bpc.request,"self_ack001",self_ack001);
        ParamUtil.setSessionAttr(bpc.request,"self_ack002",self_ack002);
        ParamUtil.setSessionAttr(bpc.request,"self_ack003",self_ack003);
        ParamUtil.setSessionAttr(bpc.request,"self_ack004",self_ack004);
        ParamUtil.setSessionAttr(bpc.request,"self_ack005",self_ack005);
        ParamUtil.setSessionAttr(bpc.request,"self_ack006",self_ack006);
        ParamUtil.setSessionAttr(bpc.request,"self_ack007",self_ack007);
        ParamUtil.setSessionAttr(bpc.request,"self_ack008",self_ack008);
        ParamUtil.setSessionAttr(bpc.request,"self_ack009",self_ack009);
        ParamUtil.setSessionAttr(bpc.request,"self_ack010",self_ack010);
        ParamUtil.setSessionAttr(bpc.request,"self_ack011",self_ack011);
        ParamUtil.setSessionAttr(bpc.request,"self_ack012",self_ack012);
        ParamUtil.setSessionAttr(bpc.request,"self_ack013",self_ack013);
        ParamUtil.setSessionAttr(bpc.request,"self_ack014",self_ack014);
        ParamUtil.setSessionAttr(bpc.request,"self_ack015",self_ack015);

    }

    public void perDate(BaseProcessClass bpc) {

    }

    public void newApp1(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("prepareData start ..."));
        ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR_CHECKED, null);
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR_CHECKED, null);
        ParamUtil.setSessionAttr(bpc.request,SELECT_DRAFT_NO,null);
        String action = (String) ParamUtil.getRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE);
        if(StringUtil.isEmpty(action)){
            action = "chooseSvc";
        }
        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,action);
        log.info(StringUtil.changeForLog("prepareData start ..."));
    }

    public void renewLic(BaseProcessClass bpc) throws IOException {
        log.info("****start ******");
        SearchParam renewLicSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.RENEW_LICENCE_SEARCH_PARAM,SelfPremisesListQueryDto.class.getName(),"PREMISES_TYPE",SearchParam.DESCENDING,false);
        String licenseeId = getLicenseeId(bpc.request);
        renewLicSearchParam.addFilter("licenseeId", licenseeId, true);
        LoginContext lc = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = lc.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
        HalpSearchResultHelper.setLicParamByField(renewLicSearchParam,"serviceTypesShow",HcsaServiceCacheHelper.controlServices(2,userRoleAccessMatrixDtos));
        QueryHelp.setMainSql("interInboxQuery", "queryPremises", renewLicSearchParam);
        SearchResult<SelfPremisesListQueryDto> renewLicSearchResult = requestForChangeService.searchPreInfo(renewLicSearchParam);
        if (!StringUtil.isEmpty(renewLicSearchResult)) {
            ParamUtil.setSessionAttr(bpc.request, GuideConsts.RENEW_LICENCE_SEARCH_PARAM, renewLicSearchParam);
            ParamUtil.setRequestAttr(bpc.request, GuideConsts.RENEW_LICENCE_SEARCH_RESULT, renewLicSearchResult);
        }
        log.info("****end ******");
    }

    public void renewLicPage(BaseProcessClass bpc) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, GuideConsts.RENEW_LICENCE_SEARCH_PARAM);
        HalpSearchResultHelper.doPage(bpc.request,searchParam);
    }

    public void preChooseAlign(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("prepare choose align start ..."));
        List<String> baseSvcList = (List<String>) ParamUtil.getSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR_CHECKED);
        if(!IaisCommonUtils.isEmpty(baseSvcList)){
            List<String> svcNameList = svcIdListTransferNameList(baseSvcList);
            SearchResult searchResult = getLicense(bpc,svcNameList);
            boolean hasExistBase = false;
            if(searchResult != null && searchResult.getRowCount() > 0){
                hasExistBase = true;
            }
            ParamUtil.setRequestAttr(bpc.request,HAS_EXISTING_BASE,hasExistBase);
        }
        AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
        Map<HcsaServiceDto,List<HcsaServiceDto>> baseAndSpcSvcMap = (Map<HcsaServiceDto, List<HcsaServiceDto>>) ParamUtil.getSessionAttr(bpc.request,BASEANDSPCSVCMAP);
        log.info(StringUtil.changeForLog("prepare choose align end ..."));
    }

    public void doChooseAlign(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("do choose align start ..."));
        String nextStep = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        //dont have existing base
        String isAlign = ParamUtil.getString(bpc.request,"isAlign");
        AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
        if(AppConsts.YES.equals(isAlign)){
            appSelectSvcDto.setAlign(true);
        }else {
            appSelectSvcDto.setAlign(false);
        }
        String additional = ParamUtil.getString(bpc.request,CRUD_ACTION_ADDITIONAL);
        if(BACK_ATTR.equals(additional)){
            return;
        }
        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
        String nextstep = ParamUtil.getString(bpc.request,"crud_action_additional");
        if(NEXT.equals(nextstep)){
            getDraft(bpc);
            String crud_action_value=bpc.request.getParameter("crud_action_value");
            String draftNo  =bpc.request.getParameter("draftNo");
            String attribute =(String)bpc.request.getAttribute(SELECT_DRAFT_NO);
            if("continue".equals(crud_action_value)){
                bpc.request.getSession().setAttribute(DRAFT_NUMBER, null);
                bpc.request.getSession().setAttribute(SELECT_DRAFT_NO, draftNo);
            }else if("resume".equals(crud_action_value)){
                bpc.request.getSession().setAttribute(DRAFT_NUMBER, draftNo);
            }else if(attribute!=null){
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,"doBack");
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,CHOOSE_ALIGN);
            }
        }

        String switchStep = ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE);
        if(NEXT.equals(switchStep)){
            String crud_action_value=bpc.request.getParameter("crud_action_value");
            String draftNo  =bpc.request.getParameter("draftNo");
            getDraft(bpc);
            String attribute =(String)bpc.request.getAttribute( SELECT_DRAFT_NO);
            if("continue".equals(crud_action_value)){
                List<String> list=new ArrayList<>(1);
                list.add(draftNo);
                log.info(StringUtil.changeForLog("delete draft start ..."));
                appInboxClient.deleteDraftNUmber(list);
                bpc.request.getSession().setAttribute(DRAFT_NUMBER, null);
            }else if("resume".equals(crud_action_value)){
                bpc.request.getSession().setAttribute(DRAFT_NUMBER, attribute);
            }
            appSelectSvcDto.setAlignPage(true);
        }

        ParamUtil.setSessionAttr(bpc.request,APP_SELECT_SERVICE,appSelectSvcDto);
        log.info(StringUtil.changeForLog("do choose align end ..."));
    }

    public void doChooseBaseSvc(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("do choose base svc start ..."));
        AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
        List<HcsaServiceDto> baseSvcDtoList = appSelectSvcDto.getBaseSvcDtoList();
        boolean noExistBaseLic = (boolean) ParamUtil.getSessionAttr(bpc.request, NO_EXIST_BASE_LIC);
        boolean noExistBaseApp = (boolean) ParamUtil.getSessionAttr(bpc.request, NO_EXIST_BASE_APP);
        boolean bundleAchOrMs = (boolean) ParamUtil.getSessionAttr(bpc.request, "bundleAchOrMs");
        List<HcsaServiceDto> notContainedSvc = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request, "notContainedSvc");
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = IaisCommonUtils.genNewArrayList();
        List<AppLicBundleDto> appLicBundleDtoList=IaisCommonUtils.genNewArrayList();
        List<String> addressList=IaisCommonUtils.genNewArrayList();
        Map<String,String> licenceIdMap=IaisCommonUtils.genNewHashMap();
        Map<String,String> applicationNoMap=IaisCommonUtils.genNewHashMap();
        String erroMsg = "";
        String subErrorMsg = "";

        if (!noExistBaseLic){
            for (HcsaServiceDto hcsaServiceDto : notContainedSvc) {
                PaginationHandler<AppAlignLicQueryDto> paginationHandler = (PaginationHandler<AppAlignLicQueryDto>) ParamUtil.getSessionAttr(bpc.request,hcsaServiceDto.getSvcCode()+"licPagDiv__SessionAttr");
                if (paginationHandler==null){
                    continue;
                }
                paginationHandler.keepCurrentPageChecked();
                List<AppAlignLicQueryDto> allCheckedData = paginationHandler.getAllCheckedData();
                AppAlignLicQueryDto checkData = new AppAlignLicQueryDto();
                if(allCheckedData != null && allCheckedData.size() > 0){
                    checkData = allCheckedData.get(0);
                }
                if (checkData.getSvcName()==null){
//                    subErrorMsg=MessageUtil.getMessageDesc("GENERAL_ERR0006");
                    ParamUtil.setRequestAttr(bpc.request,hcsaServiceDto.getSvcCode()+"chooseBaseErr", MANDATORY_ERR_MSG);
                    continue;
                }
                if (!"first".equals(checkData.getSvcName())){
                    HcsaServiceDto baseServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(checkData.getSvcName());
                    AppLicBundleDto appLicBundleDto=new AppLicBundleDto();
                    appLicBundleDto.setSvcCode(baseServiceDto.getSvcCode());
                    appLicBundleDto.setLicenceId(checkData.getLicenceId());
                    List<AppLicBundleDto> licBundleDtos = assessmentGuideService.getBundleList(checkData.getLicenceId(), true);
                    if (IaisCommonUtils.isNotEmpty(licBundleDtos)){
                        appLicBundleDto.setBoundCode(licBundleDtos.get(0).getBoundCode());
                        for (AppLicBundleDto licBundleDto : licBundleDtos) {
                            if (Objects.equals(checkData.getLicenceId(), licBundleDto.getLicenceId())) {
                                continue;
                            }
                            if (!StringUtil.isEmpty(licBundleDto.getSvcName())) {
                                HcsaServiceDto hcsaService = HcsaServiceCacheHelper.getServiceByServiceName(
                                        licBundleDto.getSvcName());
                                licBundleDto.setSvcCode(hcsaService.getSvcCode());
                                licBundleDto.setSvcId(hcsaService.getId());
                            }
                            if (!StringUtil.isEmpty(licBundleDto.getSvcId())) {
                                HcsaServiceDto hcsaService = HcsaServiceCacheHelper.getServiceById(licBundleDto.getSvcId());
                                licBundleDto.setSvcCode(hcsaService.getSvcCode());
                                licBundleDto.setSvcName(hcsaService.getSvcName());
                            }
                            appLicBundleDtoList.add(licBundleDto);
                        }
                    }
                    appLicBundleDto.setPremisesId(checkData.getPremisesId());
                    appLicBundleDto.setPremisesType(checkData.getPremisesType());
                    appLicBundleDto.setLicOrApp(true);
                    appLicBundleDtoList.add(0, appLicBundleDto);
                    addressList.add(checkData.getAddress());
                    licenceIdMap.put(hcsaServiceDto.getSvcCode(),checkData.getLicenceId());
                }
                appSelectSvcDto.setInitPagHandler(false);
            }
        }else if(!noExistBaseApp){
            for (HcsaServiceDto hcsaServiceDto : notContainedSvc) {
                PaginationHandler<AppAlignAppQueryDto> paginationHandler = (PaginationHandler<AppAlignAppQueryDto>) ParamUtil.getSessionAttr(bpc.request,hcsaServiceDto.getSvcCode()+"appPagDiv__SessionAttr");
                if (paginationHandler==null){
                    continue;
                }
                paginationHandler.keepCurrentPageChecked();
                List<AppAlignAppQueryDto> allCheckedData = paginationHandler.getAllCheckedData();
                AppAlignAppQueryDto checkData = new AppAlignAppQueryDto();
                if(allCheckedData != null && allCheckedData.size() > 0){
                    checkData = allCheckedData.get(0);
                }
                if (checkData.getSvcName()==null){
//                    subErrorMsg=MessageUtil.getMessageDesc("GENERAL_ERR0006");
                    ParamUtil.setRequestAttr(bpc.request,hcsaServiceDto.getSvcCode()+"chooseBaseErr", MANDATORY_ERR_MSG);
                    continue;
                }
                if (!"first".equals(checkData.getSvcName())){
                    HcsaServiceDto baseServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(checkData.getSvcName());
                    AppLicBundleDto appLicBundleDto=new AppLicBundleDto();
                    appLicBundleDto.setSvcCode(baseServiceDto.getSvcCode());
                    appLicBundleDto.setApplicationNo(checkData.getApplicationNo());
                    appLicBundleDto.setPremisesId(checkData.getPremisesId());
                    appLicBundleDto.setPremisesType(checkData.getPremisesType());
                    appLicBundleDto.setLicOrApp(false);
                    List<AppLicBundleDto> licBundleDtos = assessmentGuideService.getBundleList(checkData.getApplicationNo(), false);
                    if (IaisCommonUtils.isNotEmpty(licBundleDtos)){
                        appLicBundleDto.setBoundCode(licBundleDtos.get(0).getBoundCode());
                    }
                    appLicBundleDtoList.add(appLicBundleDto);
                    addressList.add(checkData.getAddress());
                    applicationNoMap.put(hcsaServiceDto.getSvcCode(),checkData.getApplicationNo());
                }
                appSelectSvcDto.setInitPagHandler(false);
            }
        }
        if (IaisCommonUtils.isNotEmpty(baseSvcDtoList)){
            for (HcsaServiceDto hcsaServiceDto : baseSvcDtoList) {
                AppSvcRelatedInfoDto appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
                appSvcRelatedInfoDto.setServiceId(hcsaServiceDto.getId());
                appSvcRelatedInfoDto.setServiceName(hcsaServiceDto.getSvcName());
                appSvcRelatedInfoDto.setServiceCode(hcsaServiceDto.getSvcCode());
                appSvcRelatedInfoDto.setServiceType(hcsaServiceDto.getSvcType());
                appSvcRelatedInfoDto.setBaseServiceId(hcsaServiceDto.getId());
                if (IaisCommonUtils.isNotEmpty(appLicBundleDtoList)){
                    appSvcRelatedInfoDto.setLicPremisesId(appLicBundleDtoList.get(0).getPremisesId());
                }
                appSvcRelatedInfoDtos.add(appSvcRelatedInfoDto);
            }
        }
        //sort
        appSvcRelatedInfoDtos = sortAppSvcRelatDto(appSvcRelatedInfoDtos);
        ParamUtil.setSessionAttr(bpc.request,APP_SVC_RELATED_INFO_LIST, (Serializable) appSvcRelatedInfoDtos);
        ParamUtil.setSessionAttr(bpc.request, "appLicBundleDtoList", (Serializable) appLicBundleDtoList);
        String additional = ParamUtil.getString(bpc.request,CRUD_ACTION_ADDITIONAL);
        if(BACK_ATTR.equals(additional)){
            return;
        }else if("doPage".equals(additional)){
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,CHOOSE_BASE_SVC);
            return;
        }
        //validate
        int bundleMsCount=0;
        if(addressList.size() > 1){
            boolean isdifferent=false;
            String address=addressList.get(0);
            for (int i = 1; i < addressList.size(); i++) {
                if (!addressList.get(i).equals(address)){
                    isdifferent=true;
                }
            }
            if (isdifferent){
                erroMsg = MessageUtil.getMessageDesc("NEW_ERR0007");
            }
        }
        if(StringUtil.isEmpty(erroMsg)&&StringUtil.isEmpty(subErrorMsg)){
            if(bundleAchOrMs){
                if (!noExistBaseLic){
                    for (HcsaServiceDto hcsaServiceDto : notContainedSvc) {
                        String licenceId = licenceIdMap.get(hcsaServiceDto.getSvcCode());
                        if (StringUtil.isNotEmpty(licenceId)){
                            List<AppLicBundleDto> licBundleDtos = assessmentGuideService.getBundleList(licenceId, true);
                            if (IaisCommonUtils.isNotEmpty(licBundleDtos)){
                                bundleMsCount = licBundleDtos.size();
                            }
                            if (bundleMsCount!=0){
                                subErrorMsg=MessageUtil.getMessageDesc("GENERAL_ERR0077");
                                ParamUtil.setRequestAttr(bpc.request,hcsaServiceDto.getSvcCode()+"chooseBaseErr",subErrorMsg);
                            }
                        }
                    }
                } else if (!noExistBaseApp) {
                    for (HcsaServiceDto hcsaServiceDto : notContainedSvc) {
                        String applicationNo = applicationNoMap.get(hcsaServiceDto.getSvcCode());
                        if (StringUtil.isNotEmpty(applicationNo)){
                            List<AppLicBundleDto> licBundleDtos = assessmentGuideService.getBundleList(applicationNo, false);
                            if (IaisCommonUtils.isNotEmpty(licBundleDtos)){
                                bundleMsCount = licBundleDtos.size();
                            }
                            if (bundleMsCount!=0){
                                subErrorMsg=MessageUtil.getMessageDesc("GENERAL_ERR0077");
                                ParamUtil.setRequestAttr(bpc.request,hcsaServiceDto.getSvcCode()+"chooseBaseErr",subErrorMsg);
                            }
                        }
                    }
                }
            }/*else{
                String licenceId = licenceIdMap.get(notContainedSvc.get(0).getSvcCode());
                if (!noExistBaseLic&&StringUtil.isNotEmpty(licenceId)){
                    List<AppLicBundleDto> licBundleDtos = appSubmissionService.getBundleMsCount(licenceId, true);
                    if (IaisCommonUtils.isNotEmpty(licBundleDtos)){
                        bundleMsCount = licBundleDtos.size();
                    }
                    if (bundleMsCount>=3){
                        erroMsg=MessageUtil.getMessageDesc("GENERAL_ERR0077");
                    }
                }
            }*/
        }
        String licenseeId = "";
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
        if(loginContext!=null){
            licenseeId  = loginContext.getLicenseeId();
        }
        if(StringUtil.isEmpty(erroMsg)&&StringUtil.isEmpty(subErrorMsg)){
            List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                svcCodeList.add(appSvcRelatedInfoDto.getServiceCode());
            }
            List<ApplicationSubDraftDto> applicationSubDraftDtos = assessmentGuideService.getDraftListBySvcCodeAndStatus(svcCodeList,ApplicationConsts.DRAFT_STATUS_PENDING_PAYMENT,licenseeId,ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
            if(!IaisCommonUtils.isEmpty(applicationSubDraftDtos)){
                erroMsg = MessageUtil.getMessageDesc("NEW_ERR0023");
            }
        }
        if(StringUtil.isEmpty(erroMsg)&&StringUtil.isEmpty(subErrorMsg)){
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
            checkAction(CHOOSE_BASE_SVC, bpc);
        }else{
            ParamUtil.setRequestAttr(bpc.request,"chooseBaseErr",erroMsg);
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,CHOOSE_BASE_SVC);
        }
        appSelectSvcDto.setChooseBaseSvc(true);
        ParamUtil.setSessionAttr(bpc.request,APP_SELECT_SERVICE,appSelectSvcDto);
        log.info(StringUtil.changeForLog("do choose base svc end ..."));
    }

    private SearchResult getLicense(BaseProcessClass bpc,List<String> baselist){
        SearchParam searchParamGroup;
        searchParamGroup = new SearchParam(MenuLicenceDto.class.getName());
        searchParamGroup.setPageSize(200);
        searchParamGroup.setPageNo(1);
        searchParamGroup.setSort("START_DATE", SearchParam.ASCENDING);
        StringBuilder sb = new StringBuilder("(");
        int i =0;
        for (String item: baselist) {
            sb.append(":itemKey").append(i).append(',');
            i++;
        }
        String inSql = sb.substring(0, sb.length() - 1) + ")";
        searchParamGroup.addParam("serName", inSql);
        i = 0;
        for (String item: baselist) {
            searchParamGroup.addFilter("itemKey" + i,
                    item);
            i ++;
        }
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        searchParamGroup.addFilter("licenseeId",loginContext.getLicenseeId(),true);
        QueryHelp.setMainSql("interInboxQuery", "getLicenceBySerName",searchParamGroup);
        SearchResult<MenuLicenceDto> searchResult = assessmentGuideService.getMenuLicence(searchParamGroup);
        return  searchResult;
    }

    private List<String> svcIdListTransferNameList(List<String> baseSvcIdList){
        List<String> svcNameList = IaisCommonUtils.genNewArrayList();
        for(String id:baseSvcIdList){
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(id);
            svcNameList.add(hcsaServiceDto.getSvcName());
        }
        return svcNameList;
    }

    private static AppAlignLicQueryDto getAppAlignLicQueryDto(Map<String,List<AppAlignLicQueryDto>> baseLicMap,String svcName,String hciCode){
        AppAlignLicQueryDto result = new AppAlignLicQueryDto();
        if(baseLicMap != null && !StringUtil.isEmpty(svcName) && !StringUtil.isEmpty(hciCode)){
            List<AppAlignLicQueryDto> appAlignLicQueryDtos = baseLicMap.get(hciCode);
            if(!IaisCommonUtils.isEmpty(appAlignLicQueryDtos)){
                for(AppAlignLicQueryDto appAlignLicQueryDto:appAlignLicQueryDtos){
                    if(svcName.equals(appAlignLicQueryDto.getSvcName())){
                        result = appAlignLicQueryDto;
                        break;
                    }
                }
            }
        }
        return result;
    }

    private void getDraft(BaseProcessClass bpc){
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId;
        if(loginContext!=null){
            licenseeId = loginContext.getLicenseeId();
        }else {
            licenseeId = "9ED45E34-B4E9-E911-BE76-000C29C8FBE4";
        }
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos=(List<AppSvcRelatedInfoDto>)ParamUtil.getSessionAttr(bpc.request,APP_SVC_RELATED_INFO_LIST);
        List<String> licenceList =(List<String>) ParamUtil.getSessionAttr(bpc.request, "licence");
        List<String> baseServiceIds=IaisCommonUtils.genNewArrayList();
        if(StringUtil.isEmpty(licenceList)){
            baseServiceIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, BASE_SERVICE_ATTR_CHECKED);
        }
        List<String> specifiedServiceIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR_CHECKED);
        List<String> serviceConfigIds = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(baseServiceIds)){
            serviceConfigIds.addAll(baseServiceIds);
        }
        if(!IaisCommonUtils.isEmpty(specifiedServiceIds)){
            serviceConfigIds.addAll(specifiedServiceIds);
        }
        if(!serviceConfigIds.isEmpty()) {
            List<HcsaServiceDto> hcsaServiceDtosById = assessmentGuideService.getHcsaServiceDtosById(serviceConfigIds);
            List<String> serviceCodeList = new ArrayList<>(hcsaServiceDtosById.size());
            for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtosById) {
                serviceCodeList.add(hcsaServiceDto.getSvcCode());
            }
            if(appSvcRelatedInfoDtos!=null){
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos){
                    String serviceCode = appSvcRelatedInfoDto.getServiceCode();
                    if(serviceCode!=null){
                        if(!serviceCodeList.contains(serviceCode)){
                            serviceCodeList.add(serviceCode);
                        }
                    }
                }
            }
            serviceCodeList.sort(String::compareTo);
            Map<String, Object> map = new HashMap<>();
            map.put("serviceCodesList", serviceCodeList);
            map.put("appType", ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
            map.put("licenseeId",licenseeId);
            String entity = assessmentGuideService.selectDarft(map);
            String new_ack001 = MessageUtil.getMessageDesc("NEW_ACK001");
            bpc.request.setAttribute("new_ack001",new_ack001);
            bpc.request.setAttribute(SELECT_DRAFT_NO, entity);
            bpc.request.getSession().setAttribute(SELECT_DRAFT_NO, entity);
        }
    }

    private List<HcsaServiceDto> getBaseBySpc(List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList,String spcSvcId){
        List<HcsaServiceDto> hcsaServiceDtos = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(hcsaServiceCorrelationDtoList)){
            for(HcsaServiceCorrelationDto corr:hcsaServiceCorrelationDtoList){
                if(corr.getSpecifiedSvcId().equals(spcSvcId)){
                    HcsaServiceDto serviceById = assessmentGuideService.getServiceDtoById(corr.getBaseSvcId());
                    if(serviceById!=null) {
                        if (AppConsts.COMMON_STATUS_ACTIVE.equals(serviceById.getStatus())) {
                            hcsaServiceDtos.add(HcsaServiceCacheHelper.getServiceById(corr.getBaseSvcId()));
                        }
                    }
                }
            }
        }
        return  hcsaServiceDtos;
    }

    private static String StringTransfer(String str){
        return StringUtil.isEmpty(str)?"":str;
    }

    public void preChooseBaseSvc(BaseProcessClass bpc) throws Exception {
        log.info(StringUtil.changeForLog("prepare choose base svc start ..."));
        boolean noExistBaseLic = (boolean) ParamUtil.getSessionAttr(bpc.request, NO_EXIST_BASE_LIC);
        boolean noExistBaseApp = (boolean) ParamUtil.getSessionAttr(bpc.request, NO_EXIST_BASE_APP);
        boolean bundleAchOrMs = (boolean) ParamUtil.getSessionAttr(bpc.request, "bundleAchOrMs");
        List<HcsaServiceDto> notContainedSvc = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request, "notContainedSvc");
        if (!noExistBaseLic){
            for (HcsaServiceDto hcsaServiceDto : notContainedSvc) {
                PaginationHandler<AppAlignLicQueryDto> paginationHandler = (PaginationHandler<AppAlignLicQueryDto>) ParamUtil.getSessionAttr(bpc.request,hcsaServiceDto.getSvcCode()+"licPagDiv__SessionAttr");
                if (paginationHandler==null){
                    ParamUtil.setRequestAttr(bpc.request, "notShow"+hcsaServiceDto.getSvcCode(),AppConsts.YES);
                    continue;
                }
                AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
                if(appSelectSvcDto.isInitPagHandler()){
                    List<AppAlignLicQueryDto> appAlignLicQueryDtos = IaisCommonUtils.genNewArrayList();
                    appAlignLicQueryDtos.add(paginationHandler.getDisplayData().get(0).getRecord());
                    paginationHandler.setDefaultChecked(appAlignLicQueryDtos);
                }
                paginationHandler.preLoadingPage();
            }
        }else if (!noExistBaseApp){
            for (HcsaServiceDto hcsaServiceDto : notContainedSvc) {
                PaginationHandler<AppAlignAppQueryDto> paginationHandler = (PaginationHandler<AppAlignAppQueryDto>) ParamUtil.getSessionAttr(bpc.request,hcsaServiceDto.getSvcCode()+"appPagDiv__SessionAttr");
                if (paginationHandler==null){
                    ParamUtil.setRequestAttr(bpc.request, "notShow"+hcsaServiceDto.getSvcCode(),AppConsts.YES);
                    continue;
                }
                AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
                if(appSelectSvcDto.isInitPagHandler()){
                    List<AppAlignAppQueryDto> appAlignAppQueryDtoList = IaisCommonUtils.genNewArrayList();
                    appAlignAppQueryDtoList.add(paginationHandler.getDisplayData().get(0).getRecord());
                    paginationHandler.setDefaultChecked(appAlignAppQueryDtoList);
                }
                paginationHandler.preLoadingPage();
            }
        }
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = "";
        if(loginContext!=null){
            licenseeId  = loginContext.getLicenseeId();
        }
        List<ApplicationDto> applicationDtoList = assessmentGuideService.getApplicationsByLicenseeId(licenseeId);
        HcsaServiceDto hcsaServiceDto = hcsaConfigClient.getActiveHcsaServiceDtoByName(AppServicesConsts.SERVICE_NAME_MEDICAL_SERVICE).getEntity();
        boolean existPendMS=false;
        if (hcsaServiceDto!=null&&IaisCommonUtils.isNotEmpty(applicationDtoList)){
            String svcId = hcsaServiceDto.getId();
            int count = (int) applicationDtoList.stream().filter(item -> svcId.equals(item.getServiceId())).count();
            if (count!=0&&!bundleAchOrMs){
                existPendMS=true;
            }
        }
        List<LicenceDto> licenceDtoList = licenceInboxClient.getApproveLicenceDtoByLicenseeId(licenseeId).getEntity();
        if (IaisCommonUtils.isNotEmpty(licenceDtoList)){
            int count = (int) licenceDtoList.stream()
                    .filter(item -> AppServicesConsts.SERVICE_NAME_MEDICAL_SERVICE.equals(item.getSvcName()))
                    .count();
            if (count!=0&&!bundleAchOrMs){
                existPendMS=true;
            }
        }
        ParamUtil.setRequestAttr(bpc.request,"existPendMS",existPendMS);
        log.info(StringUtil.changeForLog("prepare choose base svc end ..."));
    }

    public void doNewAppStep(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("prepare choose svc start ..."));
        List<HcsaServiceDto> hcsaServiceDtoList = assessmentGuideService.getServicesInActive();
        if (IaisCommonUtils.isEmpty(hcsaServiceDtoList)){
            log.debug("can not find hcsa service list in service menu delegator!");
            return;
        }
        List<String> accessSvcCodes = HcsaServiceCacheHelper.getAccessSvcCodes(bpc.request);

        List<HcsaServiceDto> allbaseService = hcsaServiceDtoList.stream()
                .filter(hcsaServiceDto -> BASE_SERVICE.equals(hcsaServiceDto.getSvcType()))
                .filter(hcsaServiceDto -> accessSvcCodes.contains(hcsaServiceDto.getSvcCode()))
                .collect(Collectors.toList());
        /*List<HcsaServiceDto> allspecifiedService = hcsaServiceDtoList.stream()
                .filter(hcsaServiceDto -> SPECIFIED_SERVICE.equals(hcsaServiceDto.getSvcType()))
                .filter(hcsaServiceDto -> accessSvcCodes.contains(hcsaServiceDto.getSvcCode()))
                .collect(Collectors.toList());*/
        //sort
        allbaseService.sort(Comparator.comparing(HcsaServiceDto::getSvcName));
        //allspecifiedService.sort(Comparator.comparing(HcsaServiceDto::getSvcName));
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR, (Serializable) allbaseService);
        //ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR, (Serializable) allspecifiedService);
        AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
        appSelectSvcDto.setChooseBaseSvc(false);
        ParamUtil.setSessionAttr(bpc.request,APP_SELECT_SERVICE,appSelectSvcDto);
        log.info(StringUtil.changeForLog("prepare data end ..."));
        log.info(StringUtil.changeForLog("prepare choose svc end ..."));
    }

    private static List<AppAlignLicQueryDto> retainElementList(List<List<AppAlignLicQueryDto>> elementLists) {

        Optional<List<AppAlignLicQueryDto>> result = elementLists.parallelStream()
                .filter(elementList -> elementList != null && ((List) elementList).size() != 0)
                .reduce((a, b) -> {
                    a.retainAll(b);
                    return a;
                });
        List<AppAlignLicQueryDto> appAlignLicQueryDtos = IaisCommonUtils.genNewArrayList();
        if(result.isPresent()){
            appAlignLicQueryDtos = result.get();
        }
        return appAlignLicQueryDtos;
    }

    private AppSelectSvcDto getAppSelectSvcDto(BaseProcessClass bpc){
        AppSelectSvcDto appSelectSvcDto = (AppSelectSvcDto) ParamUtil.getSessionAttr(bpc.request,APP_SELECT_SERVICE);
        if(appSelectSvcDto == null){
            appSelectSvcDto = new AppSelectSvcDto();
        }
        return appSelectSvcDto;
    }

    public void doChooseService(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("do choose svc start ..."));
        boolean onlyBaseSvc = false;
        boolean bundleAchOrMs=true;
        ParamUtil.setSessionAttr(bpc.request, DRAFT_NUMBER, null);
        ParamUtil.setSessionAttr(bpc.request,ONLY_BASE_SVC,onlyBaseSvc);
        String additional = ParamUtil.getString(bpc.request,CRUD_ACTION_ADDITIONAL);
        if(BACK_ATTR.equals(additional)){
            return;
        }
        List<HcsaServiceDto> allbaseService = getAllBaseService(bpc);
        AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
        String currentPage = "chooseSvc";
        String[] basechks = ParamUtil.getStrings(bpc.request, BASE_SERVICE_CHECK_BOX_ATTR);
        String err = "";
        String nextstep = "";
        List<String> basecheckedlist = IaisCommonUtils.genNewArrayList();
        List<HcsaServiceDto> baseSvcSort = IaisCommonUtils.genNewArrayList();
        List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList =  assessmentGuideService.getActiveSvcCorrelation();
        log.info(StringUtil.changeForLog("hcsaServiceCorrelationDtoList size:"+hcsaServiceCorrelationDtoList.size()));
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = "";
        if(loginContext!=null){
            licenseeId  = loginContext.getLicenseeId();
        }
        if(basechks == null){
            log.info(StringUtil.changeForLog("basechks is null ..."));
            //no base service
            nextstep = currentPage;
            err = MessageUtil.getMessageDesc("NEW_ERR0001");
            ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err);
            //set audit
            Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
            errorMap.put(ERROR_ATTR,err);
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
        }else{
            log.info(StringUtil.changeForLog("basechks is not null ..."));
            for (String item:basechks) {
                basecheckedlist.add(item);
                baseSvcSort.add(HcsaServiceCacheHelper.getServiceById(item));
            }
            List<String> baseSvcNames = baseSvcSort.stream().map(HcsaServiceDto::getSvcCode).collect(Collectors.toList());
            boolean isAllContain=baseSvcNames.contains(AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY)&&baseSvcNames.contains(AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES);
            if (baseSvcNames.contains(AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL)&&!isAllContain){
                bundleAchOrMs=true;
                nextstep = CHOOSE_BASE_SVC;
            }else if (baseSvcNames.contains(AppServicesConsts.SERVICE_CODE_MEDICAL_SERVICE)&&baseSvcNames.size()==1){
                bundleAchOrMs=false;
                nextstep = CHOOSE_BASE_SVC;
            } else {
                nextstep = CHOOSE_ALIGN;
            }
            onlyBaseSvc = true;
            ParamUtil.setSessionAttr(bpc.request,ONLY_BASE_SVC,onlyBaseSvc);
            if(!currentPage.equals(nextstep)){
                List<String> chkSvcIdList = IaisCommonUtils.genNewArrayList();
                chkSvcIdList.addAll(basecheckedlist);
                //validate premises type intersection
                Set<String> premisesTypeList = assessmentGuideService.getAppGrpPremisesTypeBySvcId(chkSvcIdList);
                if(IaisCommonUtils.isEmpty(premisesTypeList)){
                    nextstep = currentPage;
                    err = MessageUtil.getMessageDesc("NEW_ERR0026");
                    ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err);
                    //set audit
                    Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
                    errorMap.put(ERROR_ATTR,err);
                    WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                }else{
                    //EAS and MTS licence only one active/approve licence
                    List<HcsaServiceDto> hcsaServiceDtos = IaisCommonUtils.genNewArrayList();
                    for(String baseId:basecheckedlist){
                        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(baseId);
                        if(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(hcsaServiceDto.getSvcCode()) || AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(hcsaServiceDto.getSvcCode())){
                            hcsaServiceDtos.add(hcsaServiceDto);
                        }
                    }
                    if(!IaisCommonUtils.isEmpty(hcsaServiceDtos)){
                        boolean canCreateEasOrMts = assessmentGuideService.canApplyEasOrMts(getLicenseeId(bpc.request),hcsaServiceDtos);
                        if(!canCreateEasOrMts){
                            nextstep = currentPage;
                            err = MessageUtil.getMessageDesc("NEW_ERR0029");
                            ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err);
                            //set audit
                            Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
                            errorMap.put(ERROR_ATTR,err);
                            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                        }
                    }
                }
            }
        }
        if(!currentPage.equals(nextstep)){
            List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
            for(HcsaServiceDto hcsaServiceDto:baseSvcSort){
                svcCodeList.add(hcsaServiceDto.getSvcCode());
            }
            List<ApplicationSubDraftDto> applicationSubDraftDtos = assessmentGuideService.getDraftListBySvcCodeAndStatus(svcCodeList,ApplicationConsts.DRAFT_STATUS_PENDING_PAYMENT,licenseeId,ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
            if(!IaisCommonUtils.isEmpty(applicationSubDraftDtos)){
                nextstep = currentPage;
                err = MessageUtil.getMessageDesc("NEW_ERR0023");
                ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err);
                //set audit
                Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
                errorMap.put(ERROR_ATTR_LIST,err);
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            }
        }
        appSelectSvcDto.setBaseSvcDtoList(baseSvcSort);
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR_CHECKED, (Serializable) basecheckedlist);

        //control switch
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = null;
        if(!currentPage.equals(nextstep)){
            boolean newLicensee  = true;
            //note: As long as you select the specified service, you don't go and select align
            newLicensee =  assessmentGuideService.isNewLicensee(licenseeId);
            appSelectSvcDto.setNewLicensee(newLicensee);
            if(newLicensee){
                if(nextstep.equals(CHOOSE_BASE_SVC)){
                    if (bundleAchOrMs){
                        initChooseBaseSvc(bpc, bundleAchOrMs, allbaseService, baseSvcSort, licenseeId);
                    }else {
                        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
                    }
                }else if(nextstep.equals(CHOOSE_ALIGN)){
                    ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
                }
            }else{
                if(nextstep.equals(CHOOSE_BASE_SVC)){
                    initChooseBaseSvc(bpc, bundleAchOrMs, allbaseService, baseSvcSort, licenseeId);
                }else if(nextstep.equals(CHOOSE_ALIGN)){
                    nextstep = CHOOSE_LICENCE;
                    //judge whether had existing licence
                    List<String> allBaseId = IaisCommonUtils.genNewArrayList();
                    for(HcsaServiceDto hcsaServiceDto:allbaseService){
                        allBaseId.add(hcsaServiceDto.getId());
                    }
                    List<String> chkBase = IaisCommonUtils.genNewArrayList();
                    for(String baseId:basechks){
                        chkBase.add(baseId);
                    }
                    allBaseId.removeAll(chkBase);
                    //get prem type intersection
                    Set<String> premisesTypeList = assessmentGuideService.getAppGrpPremisesTypeBySvcId(chkBase);
                    int alignMinExpiryMonth = systemParamConfig.getAlignMinExpiryMonth();
                    log.debug(StringUtil.changeForLog("alignMinExpiryMonth:"+alignMinExpiryMonth));
                    SearchResult<MenuLicenceDto> searchResult = getAlignLicPremInfo(allBaseId,licenseeId,premisesTypeList,alignMinExpiryMonth);
                    //filter pending and existing data
                    List<MenuLicenceDto> newAppLicDtos = removePendAndExistPrem(chkBase,searchResult.getRows(),licenseeId);
                    //pagination
                    if(!IaisCommonUtils.isEmpty(newAppLicDtos)){
                        log.debug(StringUtil.changeForLog("newAppLicDtos size:"+newAppLicDtos.size()));
                        initPaginationHandler(newAppLicDtos);
                    }

                    //only not align option
                    if(IaisCommonUtils.isEmpty(newAppLicDtos) || (!IaisCommonUtils.isEmpty(newAppLicDtos) && newAppLicDtos.size() <= 1)){
                        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
                    }
                }
                appSelectSvcDto.setInitPagHandler(true);
            }
        }
        log.info(StringUtil.changeForLog("do choose svc next step:"+nextstep));
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, nextstep);
        //reset
        ParamUtil.setSessionAttr(bpc.request,APP_SVC_RELATED_INFO_LIST, (Serializable) appSvcRelatedInfoDtos);
        ParamUtil.setSessionAttr(bpc.request,RELOAD_BASE_SVC_SELECTED, null);
        appSelectSvcDto.setAlign(false);
        ParamUtil.setSessionAttr(bpc.request,APP_SELECT_SERVICE,appSelectSvcDto);
        ParamUtil.setSessionAttr(bpc.request,"bundleAchOrMs", bundleAchOrMs);
        //test
        String value = ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE);
        if(NEXT.equals(value)){
            checkAction(CHOOSE_SERVICE, bpc);
        }

        log.info(StringUtil.changeForLog("do choose svc end ..."));
    }

    private void checkAction(String type, BaseProcessClass bpc) {
        HttpServletRequest request=bpc.request;
        getDraft(bpc);
        String crud_action_value = request.getParameter(IaisEGPConstant.CRUD_ACTION_VALUE);
        String attribute = (String) request.getAttribute(DRAFT_NUMBER);
        if ("continue".equals(crud_action_value)) {
            if (!StringUtil.isEmpty(type)) {
                String draftNo  = request.getParameter("draftNo");
                List<String> list=new ArrayList<>(1);
                list.add(draftNo);
                log.info(StringUtil.changeForLog("delete draft start ..."));
                appInboxClient.deleteDraftNUmber(list);
            }
            ParamUtil.setSessionAttr(request, DRAFT_NUMBER, null);
        } else if ("resume".equals(crud_action_value)) {
            ParamUtil.setSessionAttr(request, DRAFT_NUMBER, attribute);
        } else if (attribute != null) {
            //back to curr page
            if (StringUtil.isEmpty(type) || CHOOSE_SERVICE.equals(type)) {
                ParamUtil.setRequestAttr(request, VALIDATION_ATTR, ERROR_ATTR);
            }
            if (!StringUtil.isEmpty(type)) {
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, type);
                ParamUtil.setRequestAttr(request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,"loading");
            }
        }
    }

    private void initChooseBaseSvc(BaseProcessClass bpc, boolean bundleAchOrMs, List<HcsaServiceDto> allbaseService, List<HcsaServiceDto> baseSvcSort, String licenseeId) {
        boolean noExistBaseLic = true;
        boolean noExistBaseApp = true;
        List<HcsaServiceDto> needContainedSvc =IaisCommonUtils.genNewArrayList();
        Optional<HcsaServiceDto> clbService = allbaseService.stream().filter(s -> AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY.equals(s.getSvcCode())).findAny();
        if (clbService.isPresent()){
            needContainedSvc.add(clbService.get());
        }
        Optional<HcsaServiceDto> rdsService = allbaseService.stream().filter(s -> AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES.equals(s.getSvcCode())).findAny();
        if (rdsService.isPresent()){
            needContainedSvc.add(rdsService.get());
        }
        List<String> svcCodes = baseSvcSort.stream().map(HcsaServiceDto::getSvcCode).collect(Collectors.toList());
        List<HcsaServiceDto> notContainedSvc;
        if (bundleAchOrMs){
            notContainedSvc=needContainedSvc.stream().filter(s->!svcCodes.contains(s.getSvcCode())).collect(Collectors.toList());
        }else {
            notContainedSvc= Collections.singletonList(allbaseService.stream()
                    .filter(s->AppServicesConsts.SERVICE_CODE_MEDICAL_SERVICE.equals(s.getSvcCode())).findAny().get());
        }
        List<String> svcNameList = IaisCommonUtils.genNewArrayList();
        List<String> svcIdList = IaisCommonUtils.genNewArrayList();
        ParamUtil.clearSession(bpc.request,"notContained"+AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY,"notContained"+AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES);
        for(HcsaServiceDto hcsaServiceDto:notContainedSvc){
            svcNameList.add(hcsaServiceDto.getSvcName());
            svcIdList.add(hcsaServiceDto.getId());
            ParamUtil.setSessionAttr(bpc.request,"notContained"+hcsaServiceDto.getSvcCode(), AppConsts.YES);
        }
        Set<String> premisesTypeList=IaisCommonUtils.genNewHashSet();
        premisesTypeList.add(ApplicationConsts.PREMISES_TYPE_PERMANENT);
        if(!bundleAchOrMs){
            premisesTypeList.add(ApplicationConsts.PREMISES_TYPE_CONVEYANCE);
            premisesTypeList.add(ApplicationConsts.PREMISES_TYPE_MOBILE);
            premisesTypeList.add(ApplicationConsts.PREMISES_TYPE_REMOTE);
        }
        int alignMinExpiryMonth = systemParamConfig.getAlignMinExpiryMonth();
        List<AppAlignLicQueryDto> bundleLic = getBundleLicPremInfo(svcIdList, licenseeId,premisesTypeList,alignMinExpiryMonth).getRows();
        Map<String, List<AppAlignLicQueryDto>> bundleLicMap = bundleLic.stream().collect(Collectors.groupingBy(AppAlignLicQueryDto::getSvcName));
        List<HcsaServiceDto> hcsaServiceByNames = hcsaConfigClient.getHcsaServiceByNames(svcNameList).getEntity();
        List<String> collect = hcsaServiceByNames.stream().map(HcsaServiceDto::getId).collect(Collectors.toList());
        List<AppAlignAppQueryDto> bundleApp = appInboxClient.getActiveApplicationsAddress(licenseeId, collect).getEntity();
        for (int i = 0; i < bundleApp.size(); i++) {
            AppAlignAppQueryDto appAlignAppQueryDto = bundleApp.get(i);
            appAlignAppQueryDto.setSvcName(HcsaServiceCacheHelper.getServiceById(appAlignAppQueryDto.getSvcId()).getSvcName());
        }
        Map<String, List<AppAlignAppQueryDto>> bundleAppMap = bundleApp.stream().collect(Collectors.groupingBy(AppAlignAppQueryDto::getSvcName));
        //pagination
        if(IaisCommonUtils.isNotEmpty(bundleLic)&&bundleLic.size()>1){
            noExistBaseLic=false;
            for (HcsaServiceDto hcsaServiceDto : notContainedSvc) {
                List<AppAlignLicQueryDto> appAlignLicQueryDtoList=IaisCommonUtils.genNewArrayList();
                AppAlignLicQueryDto appAlignLicQueryDto=new AppAlignLicQueryDto();
                appAlignLicQueryDto.setSvcName("first");
                if (bundleAchOrMs){
                    appAlignLicQueryDtoList.add(appAlignLicQueryDto);
                }
                List<AppAlignLicQueryDto> appAlignLicQueryDtos = bundleLicMap.get(hcsaServiceDto.getSvcName());
                if (IaisCommonUtils.isNotEmpty(appAlignLicQueryDtos)){
                    appAlignLicQueryDtoList.addAll(appAlignLicQueryDtos);
                }
                if (bundleAchOrMs&&appAlignLicQueryDtoList.size()>1){
                    initBundlePaginationHandler(appAlignLicQueryDtoList,hcsaServiceDto.getSvcCode());
                }else if (!bundleAchOrMs&&appAlignLicQueryDtoList.size()>0){
                    initBundlePaginationHandler(appAlignLicQueryDtoList,hcsaServiceDto.getSvcCode());
                }
            }
        }else if (IaisCommonUtils.isNotEmpty(bundleApp)){
            noExistBaseApp=false;
            for (HcsaServiceDto hcsaServiceDto : notContainedSvc) {
                List<AppAlignAppQueryDto> appAlignAppQueryDtoList=IaisCommonUtils.genNewArrayList();
                AppAlignAppQueryDto appAlignAppQueryDto=new AppAlignAppQueryDto();
                appAlignAppQueryDto.setSvcName("first");
                if (bundleAchOrMs){
                    appAlignAppQueryDtoList.add(appAlignAppQueryDto);
                }
                List<AppAlignAppQueryDto> appAlignAppQueryDtos = bundleAppMap.get(hcsaServiceDto.getSvcName());
                if (IaisCommonUtils.isNotEmpty(appAlignAppQueryDtos)){
                    appAlignAppQueryDtoList.addAll(appAlignAppQueryDtos);
                }
                if (bundleAchOrMs&&appAlignAppQueryDtoList.size()>1){
                    initBundleAppPaginationHandler(appAlignAppQueryDtoList,hcsaServiceDto.getSvcCode());
                }else if (!bundleAchOrMs&&appAlignAppQueryDtoList.size()>0){
                    initBundleAppPaginationHandler(appAlignAppQueryDtoList,hcsaServiceDto.getSvcCode());
                }
            }
        }
        if((!bundleAchOrMs &&noExistBaseLic) ){
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
        }
        ParamUtil.setSessionAttr(bpc.request,NO_EXIST_BASE_LIC, noExistBaseLic);
        ParamUtil.setSessionAttr(bpc.request, NO_EXIST_BASE_APP, noExistBaseApp);
        ParamUtil.setSessionAttr(bpc.request, "notContainedSvc", (Serializable) notContainedSvc);
        ParamUtil.setSessionAttr(bpc.request, "notContainedSvcSize",notContainedSvc.size());
    }

    private PaginationHandler<AppAlignLicQueryDto> initBundlePaginationHandler(List<AppAlignLicQueryDto> newAppLicDtos,String svcCode){
        PaginationHandler<AppAlignLicQueryDto> paginationHandler = new PaginationHandler<>(svcCode+"licPagDiv",svcCode+"licBodyDiv");
        paginationHandler.setAllData(newAppLicDtos);
        paginationHandler.setCheckType(PaginationHandler.CHECK_TYPE_RADIO);
        paginationHandler.setPageSize(10);
        paginationHandler.preLoadingPage();
        return paginationHandler;
    }

    private PaginationHandler<AppAlignAppQueryDto> initBundleAppPaginationHandler(List<AppAlignAppQueryDto> newAppLicDtos,String svcCode){
        PaginationHandler<AppAlignAppQueryDto> paginationHandler = new PaginationHandler<>(svcCode+"appPagDiv",svcCode+"appBodyDiv");
        paginationHandler.setAllData(newAppLicDtos);
        paginationHandler.setCheckType(PaginationHandler.CHECK_TYPE_RADIO);
        paginationHandler.setPageSize(10);
        paginationHandler.preLoadingPage();
        return paginationHandler;
    }

    private SearchResult<AppAlignLicQueryDto> getBundleLicPremInfo(List<String> excludeChkBase,String licenseeId,
                                                                   Set<String> premisesTypeList, int alignMinExpiryMonth){
        if(StringUtil.isEmpty(licenseeId)){
            return null;
        }
        if(IaisCommonUtils.isEmpty(excludeChkBase)){
            excludeChkBase = IaisCommonUtils.genNewArrayList();
        }
        SearchParam searchParam = new SearchParam(AppAlignLicQueryDto.class.getName());
        if(!IaisCommonUtils.isEmpty(excludeChkBase)){
            StringBuilder placeholder = new StringBuilder("(");
            int i =0;
            for(String baseSvcId:excludeChkBase){
                placeholder.append(":itemKey").append(i).append(',');
                i++;
            }
            String inSql = placeholder.substring(0, placeholder.length() - 1) + ")";
            searchParam.addParam("serName", inSql);
            i = 0;
            for(String baseSvcId:excludeChkBase){
                searchParam.addFilter("itemKey" + i,
                        HcsaServiceCacheHelper.getServiceById(baseSvcId).getSvcName());
                i ++;
            }
        }else{
            String serName = "('')";
            searchParam.addParam("serName", serName);
        }
        //add premType filter
        if(!IaisCommonUtils.isEmpty(premisesTypeList)){
            int i = 0;
            StringBuilder premTypeItem = new StringBuilder("(");
            for(String premisesType:premisesTypeList){
                premTypeItem.append(":premType").append(i).append(',');
                i++;
            }
            String premTypeItemStr = premTypeItem.substring(0, premTypeItem.length() - 1) + ")";
            searchParam.addParam("premTypeList", premTypeItemStr);
            i = 0;
            for(String premisesType:premisesTypeList){
                searchParam.addFilter("premType" + i, premisesType);
                i ++;
            }
        }else{
            String premType = "('')";
            searchParam.addParam("premTypeList", premType);
            log.debug(StringUtil.changeForLog("No intersection data ..."));
        }
        log.debug(StringUtil.changeForLog("alignMinExpiryMonth:" + alignMinExpiryMonth));
        searchParam.addFilter("alignMinExpiryMonth", alignMinExpiryMonth,true);

        searchParam.addFilter("licenseeId",licenseeId,true);
        QueryHelp.setMainSql("interInboxQuery", "getLicenceBySvcName",searchParam);
        return licenceInboxClient.getBundleLicence(searchParam).getEntity();
    }


    private List<HcsaServiceDto> getAllBaseService(BaseProcessClass bpc) {
        List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request, BASE_SERVICE_ATTR);
        if (IaisCommonUtils.isEmpty(hcsaServiceDtos)) {
            return IaisCommonUtils.genNewArrayList();
        }
        List<String> accessSvcCodes = HcsaServiceCacheHelper.getAccessSvcCodes(bpc.request);
        return hcsaServiceDtos.stream()
                .filter(hcsaServiceDto -> accessSvcCodes.contains(hcsaServiceDto.getSvcCode()))
                .collect(Collectors.toList());
    }

    private Map<String,List<String>> baseRelSpe(List<String> baseSvcIdList,List<String> specSvcIdList,List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList){
        boolean flag = false;
        Map<String,List<String>> result = IaisCommonUtils.genNewHashMap();
        if(!IaisCommonUtils.isEmpty(baseSvcIdList) && !IaisCommonUtils.isEmpty(specSvcIdList)){
            Map<String,List<String>> baseInSpec = IaisCommonUtils.genNewHashMap();
            //specSvcId,baseSvcIdList
            for(HcsaServiceCorrelationDto hcsaServiceCorrelationDto:hcsaServiceCorrelationDtoList){
                String corrBaseSvcId = hcsaServiceCorrelationDto.getBaseSvcId();
                String corrSpecSvcId = hcsaServiceCorrelationDto.getSpecifiedSvcId();
                if(specSvcIdList.contains(corrSpecSvcId)){
                    List<String> baseSvcIds = baseInSpec.get(corrSpecSvcId);
                    if(IaisCommonUtils.isEmpty(baseSvcIds)){
                        baseSvcIds = IaisCommonUtils.genNewArrayList();
                    }
                    baseSvcIds.add(corrBaseSvcId);
                    baseInSpec.put(corrSpecSvcId,baseSvcIds);
                }
            }
            for(int i=0; i<specSvcIdList.size();i++){
                String specSvcId = specSvcIdList.get(i);
                List<String> baseSvcIds = baseInSpec.get(specSvcId);
                boolean currFlag = false;
                for(String baseSvcId:baseSvcIdList){
                    if(baseSvcIds.contains(baseSvcId)){
                        currFlag = true;
                        break;
                    }
                }
                if(i == specSvcIdList.size()-1){
                    flag = true;
                }
                if(!currFlag){
                    break;
                }
            }
            if(flag){
                result = baseInSpec;
            }
        }
        return result;
    }

    public void controlSwitch(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("control switch start ..."));
        String action = "doBack";
        String value = ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE);
        if(StringUtil.isEmpty(value)){
            value = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM);
        }
        if(!StringUtil.isEmpty(value)){
            action = value;
        }
        if(NEXT.equals(value)){
            //init before Start page data
            AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
            List<HcsaServiceDto> hcsaServiceDtos = appSelectSvcDto.getBaseSvcDtoList();
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = (List<AppSvcRelatedInfoDto>) ParamUtil.getSessionAttr(bpc.request,APP_SVC_RELATED_INFO_LIST);
            if(IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
                appSvcRelatedInfoDtos = IaisCommonUtils.genNewArrayList();
            }
            //add other service
            appSvcRelatedInfoDtos = addOtherSvcInfo(appSvcRelatedInfoDtos,hcsaServiceDtos,false);
            appSvcRelatedInfoDtos = sortAppSvcRelatDto(appSvcRelatedInfoDtos);
            List<String> baseSvcIds = IaisCommonUtils.genNewArrayList();
            List<String> speSvcIds = IaisCommonUtils.genNewArrayList();
            String alignFlag = String.valueOf(System.currentTimeMillis());
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByCode(appSvcRelatedInfoDto.getServiceCode());
                if(hcsaServiceDto != null){
                    if(HcsaConsts.SERVICE_TYPE_BASE.equals(hcsaServiceDto.getSvcType())){
                        baseSvcIds.add(hcsaServiceDto.getId());
                        if(appSelectSvcDto.isAlign()){
                            appSvcRelatedInfoDto.setAlignFlag(alignFlag);
                        }
                    }else if(HcsaConsts.SERVICE_TYPE_SPECIFIED.equals(hcsaServiceDto.getSvcType())){
                        speSvcIds.add(hcsaServiceDto.getId());
                    }
                }
            }
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
            LicenseeDto licenseeDto = requestForChangeService.getLicenseeByOrgId(loginContext.getOrgId());
            List<LicenseeKeyApptPersonDto> keyApptPersonDtos =  requestForChangeService.getLicenseeKeyApptPersonDtoListByLicenseeId(licenseeDto.getId());

            //            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            String url= "https://" + bpc.request.getServerName() +
                    "/main-web/eservice/INTERNET/MohLicenseeCompanyDetail";
//            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            StringBuilder licenseeurl = new StringBuilder();
            if("LICT001".equals(licenseeDto.getLicenseeType())){
                licenseeurl.append(url).append("?licenseView=Licensee") ;
            }else{
                licenseeurl.append(url).append("?licenseView=Solo") ;
            }

            StringBuilder authorisedUrl=new StringBuilder();
            authorisedUrl.append(url).append("?licenseView=Authorised");
            StringBuilder medAlertUrl=new StringBuilder();
            medAlertUrl.append(url).append("?licenseView=MedAlert") ;
            List<FeUserDto> feUserDtos = requestForChangeService.getAccountByOrgId(loginContext.getOrgId());

            ParamUtil.setSessionAttr(bpc.request,"licenseeurl",licenseeurl);
            ParamUtil.setSessionAttr(bpc.request,"authorisedUrl",authorisedUrl);
            ParamUtil.setSessionAttr(bpc.request,"medAlertUrl",medAlertUrl);
            ParamUtil.setSessionAttr(bpc.request,"licensee",licenseeDto);
            ParamUtil.setSessionAttr(bpc.request,"keyperson",(Serializable) keyApptPersonDtos);
            ParamUtil.setSessionAttr(bpc.request,"feUserDtos",(Serializable) feUserDtos);
            ParamUtil.setSessionAttr(bpc.request, "baseSvcIdList", (Serializable) baseSvcIds);
            ParamUtil.setSessionAttr(bpc.request, "speSvcIdList", (Serializable) speSvcIds);
            ParamUtil.setSessionAttr(bpc.request,APP_SVC_RELATED_INFO_LIST, (Serializable) appSvcRelatedInfoDtos);
        }
        getDraft(bpc);
        ParamUtil.setRequestAttr(bpc.request,"switch_action_type",action);
        log.info(StringUtil.changeForLog("control switch end ..."));
    }

    public void preChooseLic(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("prepare choose licence start ..."));
        PaginationHandler<MenuLicenceDto> paginationHandler = (PaginationHandler<MenuLicenceDto>) ParamUtil.getSessionAttr(bpc.request,"licPagDiv__SessionAttr");
        AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
        if(appSelectSvcDto.isInitPagHandler()){
            List<MenuLicenceDto> menuLicenceDtos = IaisCommonUtils.genNewArrayList();
            menuLicenceDtos.add(paginationHandler.getDisplayData().get(0).getRecord());
            paginationHandler.setDefaultChecked(menuLicenceDtos);
        }
        paginationHandler.preLoadingPage();
        log.info(StringUtil.changeForLog("prepare choose licence end ..."));
    }

    private static SearchParam initSearParam(){
        SearchParam searchParam = new SearchParam(MenuLicenceDto.class.getName());
        searchParam.setPageNo(1);
        searchParam.setPageSize(10);
        searchParam.setSort("START_DATE",SearchParam.ASCENDING);
        return searchParam;
    }

    public void doChooseLic(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("do choose lic start ..."));
        String additional = ParamUtil.getString(bpc.request,CRUD_ACTION_ADDITIONAL);
        AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
        PaginationHandler<MenuLicenceDto> paginationHandler = (PaginationHandler<MenuLicenceDto>) ParamUtil.getSessionAttr(bpc.request,"licPagDiv__SessionAttr");
        paginationHandler.keepCurrentPageChecked();
        List<MenuLicenceDto> menuLicenceDtos = paginationHandler.getAllCheckedData();
        MenuLicenceDto menuLicenceDto = new MenuLicenceDto();
        if(menuLicenceDtos != null && menuLicenceDtos.size() > 0){
            menuLicenceDto = menuLicenceDtos.get(0);
        }
        String alignLicenceNo = menuLicenceDto.getLicenceNo();
        String licPremiseId = menuLicenceDto.getPremisesId();
        if("first".equals(menuLicenceDto.getSvcName())){
            alignLicenceNo = "";
            licPremiseId = "";
        }
        appSelectSvcDto.setInitPagHandler(false);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = IaisCommonUtils.genNewArrayList();
        if(appSelectSvcDto.isChooseBaseSvc()){
            appSvcRelatedInfoDtos = (List<AppSvcRelatedInfoDto>) ParamUtil.getSessionAttr(bpc.request,APP_SVC_RELATED_INFO_LIST);
            List<HcsaServiceDto> hcsaServiceDtos = appSelectSvcDto.getBaseSvcDtoList();
            appSvcRelatedInfoDtos = addOtherSvcInfo(appSvcRelatedInfoDtos,hcsaServiceDtos,true);
            if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                    appSvcRelatedInfoDto.setAlignLicenceNo(alignLicenceNo);
                    appSvcRelatedInfoDto.setLicPremisesId(licPremiseId);
                }
            }

        }else{
            for(HcsaServiceDto hcsaServiceDto:appSelectSvcDto.getBaseSvcDtoList()){
                AppSvcRelatedInfoDto appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
                appSvcRelatedInfoDto.setServiceId(hcsaServiceDto.getId());
                appSvcRelatedInfoDto.setServiceCode(hcsaServiceDto.getSvcCode());
                appSvcRelatedInfoDto.setServiceName(hcsaServiceDto.getSvcName());
                appSvcRelatedInfoDto.setAlignLicenceNo(alignLicenceNo);
                appSvcRelatedInfoDto.setBaseServiceId(hcsaServiceDto.getId());
                appSvcRelatedInfoDto.setLicPremisesId(licPremiseId);
                appSvcRelatedInfoDtos.add(appSvcRelatedInfoDto);
            }
        }
        ParamUtil.setSessionAttr(bpc.request,APP_SELECT_SERVICE,appSelectSvcDto);
        ParamUtil.setSessionAttr(bpc.request,APP_SVC_RELATED_INFO_LIST, (Serializable) appSvcRelatedInfoDtos);
        if(BACK_ATTR.equals(additional)){
            if(appSelectSvcDto.isChooseBaseSvc()){
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,CHOOSE_BASE_SVC);
            }else{
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,CHOOSE_SERVICE);
            }
            return;
        }else if("doPage".equals(additional)){
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,CHOOSE_LICENCE);
            return;
        }


        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
        String nextstep = ParamUtil.getString(bpc.request,"crud_action_additional");
        if(NEXT.equals(nextstep)){
            String crud_action_value=bpc.request.getParameter("crud_action_value");
            String draftNo  =bpc.request.getParameter("draftNo");
            getDraft(bpc);
            String attribute =(String)bpc.request.getAttribute(SELECT_DRAFT_NO);
            if("continue".equals(crud_action_value)){
                bpc.request.getSession().setAttribute(SELECT_DRAFT_NO, draftNo);
                bpc.request.getSession().setAttribute(DRAFT_NUMBER, null);
            }else if("resume".equals(crud_action_value)){
                bpc.request.getSession().setAttribute(DRAFT_NUMBER, attribute);
            }else if(attribute!=null){
                //back to curr page
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,"doBack");
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,CHOOSE_LICENCE);
            }
        }
        String switchStep = ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE);
        if(NEXT.equals(switchStep)){
            String crud_action_value=bpc.request.getParameter("crud_action_value");
            String draftNo  =bpc.request.getParameter("draftNo");
            getDraft(bpc);
            String attribute =(String)bpc.request.getAttribute(SELECT_DRAFT_NO);
            if("continue".equals(crud_action_value)){
                bpc.request.getSession().setAttribute(SELECT_DRAFT_NO, draftNo);
                List<String> list=new ArrayList<>(1);
                list.add(draftNo);
                assessmentGuideService.deleteDraftNUmber(list);
                bpc.request.getSession().setAttribute(DRAFT_NUMBER, null);
            }else if("resume".equals(crud_action_value)){
                bpc.request.getSession().setAttribute(DRAFT_NUMBER, attribute);
            }else if(attribute!=null){
                //back to curr page
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,"doBack");
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,CHOOSE_LICENCE);
            }
            appSelectSvcDto.setLicPage(true);
            ParamUtil.setSessionAttr(bpc.request,APP_SELECT_SERVICE,appSelectSvcDto);
        }
        log.info(StringUtil.changeForLog("do choose lic end ..."));
    }

    public void renewLicSort(BaseProcessClass bpc) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, GuideConsts.RENEW_LICENCE_SEARCH_PARAM);
        HalpSearchResultHelper.doSort(bpc.request,searchParam);
    }

    public void renewLicUpdate(BaseProcessClass bpc) {
        SearchParam renewLicUpdateSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.RENEW_LICENCE_UPDATE_SEARCH_PARAM,SelfPremisesListQueryDto.class.getName(),"PREMISES_TYPE",SearchParam.DESCENDING,false);
        renewLicUpdateSearchParam.addFilter("licenseeId", getLicenseeId(bpc.request), true);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = loginContext.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
        HalpSearchResultHelper.setLicParamByField(renewLicUpdateSearchParam,"serviceTypesShow",HcsaServiceCacheHelper.controlServices(2,userRoleAccessMatrixDtos));
        QueryHelp.setMainSql("interInboxQuery", "queryPremises", renewLicUpdateSearchParam);
        SearchResult<SelfPremisesListQueryDto> renewLicUpdateSearchResult = requestForChangeService.searchPreInfo(renewLicUpdateSearchParam);
        if (!StringUtil.isEmpty(renewLicUpdateSearchResult)) {
            ParamUtil.setSessionAttr(bpc.request, GuideConsts.RENEW_LICENCE_UPDATE_SEARCH_PARAM, renewLicUpdateSearchParam);
            ParamUtil.setRequestAttr(bpc.request, GuideConsts.RENEW_LICENCE_UPDATE_SEARCH_RESULT, renewLicUpdateSearchResult);
        }
        log.info("****end ******");
    }

    private static List<AppSvcRelatedInfoDto> addOtherSvcInfo(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos,List<HcsaServiceDto> hcsaServiceDtos,boolean needSort){
        if(!IaisCommonUtils.isEmpty(hcsaServiceDtos)){
            List<HcsaServiceDto> otherSvcDtoList = IaisCommonUtils.genNewArrayList();
            if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
                //
                for(HcsaServiceDto hcsaServiceDto:hcsaServiceDtos){
                    String svcCode = hcsaServiceDto.getSvcCode();
                    int i = 0;
                    for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                        if(svcCode.equals(appSvcRelatedInfoDto.getServiceCode())){
                            break;
                        }
                        String baseSvcId = appSvcRelatedInfoDto.getBaseServiceId();
                        //specified svc
                        if(!StringUtil.isEmpty(baseSvcId)){
                            HcsaServiceDto baseSvcDto = HcsaServiceCacheHelper.getServiceById(baseSvcId);
                            if(baseSvcDto == null){
                                log.info(StringUtil.changeForLog("current svc id is dirty data ..."));
                                continue;
                            }
                            if(svcCode.equals(baseSvcDto.getSvcCode())){
                                break;
                            }
                        }
                        if(i == appSvcRelatedInfoDtos.size()-1){
                            otherSvcDtoList.add(hcsaServiceDto);
                        }
                        i++;
                    }
                }
            }else{
                otherSvcDtoList.addAll(hcsaServiceDtos);
            }
            //create other appSvcDto
            if(!IaisCommonUtils.isEmpty(otherSvcDtoList)){
                for(HcsaServiceDto hcsaServiceDto:otherSvcDtoList){
                    AppSvcRelatedInfoDto appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
                    appSvcRelatedInfoDto.setServiceId(hcsaServiceDto.getId());
                    appSvcRelatedInfoDto.setServiceCode(hcsaServiceDto.getSvcCode());
                    appSvcRelatedInfoDto.setServiceName(hcsaServiceDto.getSvcName());
                    appSvcRelatedInfoDto.setServiceType(HcsaConsts.SERVICE_TYPE_BASE);
                    appSvcRelatedInfoDtos.add(appSvcRelatedInfoDto);
                }
            }
            if(needSort){
                appSvcRelatedInfoDtos = sortAppSvcRelatDto(appSvcRelatedInfoDtos);
            }
        }
        return appSvcRelatedInfoDtos;
    }

    public void doRenewStep(BaseProcessClass bpc) throws IOException {
        String actionType = ParamUtil.getString(bpc.request, "guide_action_type");
        String[] licIds = ParamUtil.getStrings(bpc.request, "renewLicenId");
        if (licIds != null) {
            List<String> licenceIds = IaisCommonUtils.genNewArrayList();
            for (String item : licIds) {
                licenceIds.add(ParamUtil.getMaskedString(bpc.request, item));
            }
            boolean result = inboxService.checkRenewalStatus(licenceIds, bpc.request);
            log.info(StringUtil.changeForLog("Check Renewal Status Result: " + result));
            if (result) {
                ParamUtil.setRequestAttr(bpc.request, "guide_back_action", "redirect");
                ParamUtil.setSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_LIC_ID_LIST_ATTR, (Serializable) licenceIds);
            } else {
                if ("renew".equals(actionType)) {
                    ParamUtil.setRequestAttr(bpc.request, "guide_back_action", "backRenewUpdate");
                } else {
                    ParamUtil.setRequestAttr(bpc.request, "guide_back_action", "backRenew");
                }
                ParamUtil.setSessionAttr(bpc.request, "licence_err_list", (Serializable) licenceIds);
            }
        }
    }

    /**
     * Step: RedirectToAmend
     *
     * @param bpc
     * @throws Exception
     */
    public void redirectToRenewal(BaseProcessClass bpc) throws Exception {
        StringBuilder url = new StringBuilder();
        url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                .append(InboxConst.URL_LICENCE_WEB_MODULE + "MohWithOutRenewal");
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
    }

    public void showLicensee(BaseProcessClass bpc) {
        String type = ParamUtil.getRequestString(bpc.request,"crud_action_additional");
        try {
            StringBuilder url = new StringBuilder();
            url.append("https://")
                    .append(bpc.request.getServerName())
                    .append("/main-web/eservice/INTERNET/MohLicenseeCompanyDetail")
                    .append("?licenseView=").append(type);
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }catch (Exception e){
            log.info(e.getMessage());
        }
    }

    public void backChooseSvc(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("back choose svc start ..."));
        AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);

        if(appSelectSvcDto.isBasePage()){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, CHOOSE_BASE_SVC);
        }else if(appSelectSvcDto.isAlignPage()){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, CHOOSE_ALIGN);
        }else if(appSelectSvcDto.isLicPage()){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, CHOOSE_LICENCE);
        }
        //reset
        appSelectSvcDto.setBasePage(false);
        appSelectSvcDto.setAlignPage(false);
        appSelectSvcDto.setLicPage(false);
        ParamUtil.setSessionAttr(bpc.request,APP_SELECT_SERVICE,appSelectSvcDto);
        getDraft(bpc);
        log.info(StringUtil.changeForLog("back choose svc end ..."));
    }

    public void doPage(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("doPage start ..."));
        SearchParam searchParamGroup = (SearchParam) ParamUtil.getSessionAttr(bpc.request,LIC_ALIGN_SEARCH_PARAM);
        CrudHelper.doPaging(searchParamGroup,bpc.request);
        AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
        appSelectSvcDto.setAlignLicPremId("");
    }

    public void beforeJump(BaseProcessClass bpc) {
        try {
            StringBuilder url = new StringBuilder();
            url.append("https://").append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTERNET/MohNewApplication?entryType=assessment");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }catch (Exception e){
            log.info(e.getMessage());
        }
    }

    public void doRenewSort(BaseProcessClass bpc) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, GuideConsts.RENEW_LICENCE_UPDATE_SEARCH_PARAM);
        HalpSearchResultHelper.doSort(bpc.request,searchParam);
    }

    public void doRenewPage(BaseProcessClass bpc) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, GuideConsts.RENEW_LICENCE_UPDATE_SEARCH_PARAM);
        HalpSearchResultHelper.doPage(bpc.request,searchParam);
    }

    public void amendLic1_1(BaseProcessClass bpc) throws IOException {
        log.info("****start ******");
        String licId = ParamUtil.getString(bpc.request, "amendLicenseId");
        String isNeedDelete = bpc.request.getParameter("isNeedDelete");
//        String licId = ParamUtil.getString(bpc.request, "licenceNo");
        String licIdValue = ParamUtil.getMaskedString(bpc.request, licId);
        if(!StringUtil.isEmpty(licIdValue)){
            List<ApplicationSubDraftDto> draftByLicAppId = inboxService.getDraftByLicAppId(licIdValue);
            if("delete".equals(isNeedDelete)){
                for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                    inboxService.deleteDraftByNo(applicationSubDraftDto.getDraftNo());
                }
                StringBuilder url = new StringBuilder();
                url.append(InboxConst.URL_HTTPS)
                        .append(bpc.request.getServerName())
                        .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohRequestForChange")
                        .append("?licenceId=")
                        .append(MaskUtil.maskValue("licenceId",licIdValue));
                String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
                IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
            }
        }
        SearchParam amendDetailsSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.AMEND_DETAILS_SEARCH_PARAM,SelfPremisesListQueryDto.class.getName(),"PREMISES_TYPE",SearchParam.DESCENDING,false);
        amendDetailsSearchParam.addFilter("licenseeId", getLicenseeId(bpc.request), true);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = loginContext.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
        HalpSearchResultHelper.setLicParamByField(amendDetailsSearchParam,"serviceTypesShow",HcsaServiceCacheHelper.controlServices(2,userRoleAccessMatrixDtos));
        QueryHelp.setMainSql("interInboxQuery", "queryPremises", amendDetailsSearchParam);
        SearchResult<SelfPremisesListQueryDto> amendHCISearchResult = requestForChangeService.searchPreInfo(amendDetailsSearchParam);
        if (amendHCISearchResult != null && amendHCISearchResult.getRowCount() > 0) {
            ParamUtil.setSessionAttr(bpc.request, GuideConsts.AMEND_DETAILS_SEARCH_PARAM, amendDetailsSearchParam);
            ParamUtil.setRequestAttr(bpc.request, GuideConsts.AMEND_DETAILS_SEARCH_RESULT, amendHCISearchResult);
            ArrayList<PremisesListQueryDto> newList = IaisCommonUtils.genNewArrayList(amendHCISearchResult.getRowCount());
            for (SelfPremisesListQueryDto se : amendHCISearchResult.getRows()) {
                newList.add(MiscUtil.transferEntityDto(se, PremisesListQueryDto.class));
            }
            ParamUtil.setSessionAttr(bpc.request, RfcConst.PREMISESLISTDTOS, newList);
        }
        log.info("****end ******");
    }

    public void amendLic1_2(BaseProcessClass bpc) {
        log.info("****start ******");
        SearchParam amendDetailsRemoveSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.AMEND_DETAILS_REMOVE_SEARCH_PARAM,SelfPremisesListQueryDto.class.getName(),"PREMISES_TYPE",SearchParam.DESCENDING,false);
        amendDetailsRemoveSearchParam.addFilter("licenseeId", getLicenseeId(bpc.request), true);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = loginContext.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
        HalpSearchResultHelper.setLicParamByField(amendDetailsRemoveSearchParam,"serviceTypesShow",HcsaServiceCacheHelper.controlServices(2,userRoleAccessMatrixDtos));
        QueryHelp.setMainSql("interInboxQuery", "queryPremises", amendDetailsRemoveSearchParam);
        SearchResult<SelfPremisesListQueryDto> amendDetailsRemoveSearchResult = requestForChangeService.searchPreInfo(amendDetailsRemoveSearchParam);
        if (!StringUtil.isEmpty(amendDetailsRemoveSearchResult)) {
            ParamUtil.setSessionAttr(bpc.request, GuideConsts.AMEND_DETAILS_REMOVE_SEARCH_PARAM, amendDetailsRemoveSearchParam);
            ParamUtil.setRequestAttr(bpc.request,GuideConsts.AMEND_DETAILS_REMOVE_SEARCH_RESULT, amendDetailsRemoveSearchResult);
        }
        log.info("****end ******");
    }

    public void amendLic2(BaseProcessClass bpc) {
        log.info("****start ******");
        SearchParam amendHCISearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, "amendHCISearchParam",SelfPremisesListQueryDto.class.getName(),"LICENCE_ID",SearchParam.DESCENDING,false);
        amendHCISearchParam.addFilter("licenseeId", getLicenseeId(bpc.request), true);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = loginContext.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
        HalpSearchResultHelper.setLicParamByField(amendHCISearchParam,"serviceTypesShow",HcsaServiceCacheHelper.controlServices(2,userRoleAccessMatrixDtos));
        QueryHelp.setMainSql("interInboxQuery", "queryPremises", amendHCISearchParam);
        SearchResult<SelfPremisesListQueryDto> amendHCISearchResult = requestForChangeService.searchPreInfo(amendHCISearchParam);
        if (!StringUtil.isEmpty(amendHCISearchResult)) {
            ParamUtil.setSessionAttr(bpc.request, "amendHCISearchParam", amendHCISearchParam);
            ParamUtil.setRequestAttr(bpc.request, "amendHCISearchResult", amendHCISearchResult);
            List<SelfPremisesListQueryDto> selfPremisesListQueryDtoList = amendHCISearchResult.getRows();
            List<PremisesListQueryDto> premisesListQueryDtoList = IaisCommonUtils.genNewArrayList();
            for (SelfPremisesListQueryDto selfPremisesListQueryDto:selfPremisesListQueryDtoList
                 ) {
                PremisesListQueryDto premisesListQueryDto = MiscUtil.transferEntityDto(selfPremisesListQueryDto, PremisesListQueryDto.class);
                premisesListQueryDtoList.add(premisesListQueryDto);
            }

            ParamUtil.setSessionAttr(bpc.request, RfcConst.PREMISESLISTDTOS, (Serializable) premisesListQueryDtoList);
        }
        log.info("****end ******");
    }

    public void amendLic5(BaseProcessClass bpc) throws IOException {
        log.info("****start ******");
        String licId = ParamUtil.getString(bpc.request, "amendLicenseId");
        String isNeedDelete = bpc.request.getParameter("isNeedDelete");
//        String licId = ParamUtil.getString(bpc.request, "licenceNo");
        String licIdValue = ParamUtil.getMaskedString(bpc.request, licId);
        if(!StringUtil.isEmpty(licIdValue)){
            List<ApplicationSubDraftDto> draftByLicAppId = inboxService.getDraftByLicAppId(licIdValue);
            if("delete".equals(isNeedDelete)){
                for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                    inboxService.deleteDraftByNo(applicationSubDraftDto.getDraftNo());
                }
                StringBuilder url = new StringBuilder();
                url.append(InboxConst.URL_HTTPS)
                        .append(bpc.request.getServerName())
                        .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohRequestForChange")
                        .append("?licenceId=")
                        .append(MaskUtil.maskValue("licenceId",licIdValue));
                String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
                IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
            }
        }
        SearchParam amendUpdateVehiclesSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.AMEND_UPDATE_VEHICLES_SEARCH_PARAM,SelfPremisesListQueryDto.class.getName(),"PREMISES_TYPE",SearchParam.DESCENDING,false);
        amendUpdateVehiclesSearchParam.addFilter("licenseeId", getLicenseeId(bpc.request), true);
        amendUpdateVehiclesSearchParam.addFilter("premisesType", ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE, true);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = loginContext.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
        HalpSearchResultHelper.setLicParamByField(amendUpdateVehiclesSearchParam,"serviceTypesShow",HcsaServiceCacheHelper.controlServices(2,userRoleAccessMatrixDtos));
        QueryHelp.setMainSql("interInboxQuery", "queryPremises", amendUpdateVehiclesSearchParam);
        SearchResult<SelfPremisesListQueryDto> amendUpdateVehiclesSearchResult = requestForChangeService.searchPreInfo(amendUpdateVehiclesSearchParam);
        if (amendUpdateVehiclesSearchResult != null && amendUpdateVehiclesSearchResult.getRowCount() > 0) {
            ParamUtil.setSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_VEHICLES_SEARCH_PARAM, amendUpdateVehiclesSearchParam);
            ParamUtil.setRequestAttr(bpc.request, GuideConsts.AMEND_UPDATE_VEHICLES_SEARCH_RESULT, amendUpdateVehiclesSearchResult);
            ArrayList<PremisesListQueryDto> newList = IaisCommonUtils.genNewArrayList(amendUpdateVehiclesSearchResult.getRowCount());
            for (SelfPremisesListQueryDto se : amendUpdateVehiclesSearchResult.getRows()) {
                newList.add(MiscUtil.transferEntityDto(se, PremisesListQueryDto.class));
            }
            ParamUtil.setSessionAttr(bpc.request, RfcConst.PREMISESLISTDTOS, newList);
        }
        log.info("****end ******");
    }

    public void amendLic3_1(BaseProcessClass bpc) {
        log.info("****start ******");
        SearchParam amendDetailsSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.AMEND_UPDATE_LICENSEE_SEARCH_PARAM,SelfPremisesListQueryDto.class.getName(),"PREMISES_TYPE",SearchParam.DESCENDING,false);
        amendDetailsSearchParam.addFilter("licenseeId", getLicenseeId(bpc.request), true);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = loginContext.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
        HalpSearchResultHelper.setLicParamByField(amendDetailsSearchParam,"serviceTypesShow",HcsaServiceCacheHelper.controlServices(2,userRoleAccessMatrixDtos));
        QueryHelp.setMainSql("interInboxQuery", "queryPremises", amendDetailsSearchParam);
        SearchResult<SelfPremisesListQueryDto> amendDetailsSearchResult = requestForChangeService.searchPreInfo(amendDetailsSearchParam);
        if (!StringUtil.isEmpty(amendDetailsSearchResult)) {
            ParamUtil.setSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_LICENSEE_SEARCH_PARAM, amendDetailsSearchParam);
            ParamUtil.setRequestAttr(bpc.request, GuideConsts.AMEND_UPDATE_LICENSEE_SEARCH_RESULT, amendDetailsSearchResult);
        }
        log.info("****end ******");
    }

    public void amendLic3_2(BaseProcessClass bpc) {
        log.info("****start ******");
        LoginContext loginContext= (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        FeUserDto feUserDto=orgUserManageService.getUserAccount(loginContext.getUserId());
        if(feUserDto.getUserId().contains("_")){
            ParamUtil.setRequestAttr(bpc.request, "login_action_type","company");
        }else{
            ParamUtil.setRequestAttr(bpc.request, "login_action_type","solo");
        }
        log.info("****end ******");
    }

    public void amendLic4_1(BaseProcessClass bpc) {
        log.info("****start ******");
        SearchParam amendDetailsSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.AMEND_UPDATE_PERSONNEL_SEARCH_PARAM,SelfPremisesListQueryDto.class.getName(),"PREMISES_TYPE",SearchParam.DESCENDING,false);
        amendDetailsSearchParam.addFilter("licenseeId", getLicenseeId(bpc.request), true);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = loginContext.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
        HalpSearchResultHelper.setLicParamByField(amendDetailsSearchParam,"serviceTypesShow",HcsaServiceCacheHelper.controlServices(2,userRoleAccessMatrixDtos));
        QueryHelp.setMainSql("interInboxQuery", "queryPremises", amendDetailsSearchParam);
        SearchResult<SelfPremisesListQueryDto> amendDetailsSearchResult = requestForChangeService.searchPreInfo(amendDetailsSearchParam);
        if (!StringUtil.isEmpty(amendDetailsSearchResult)) {
            ParamUtil.setSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_PERSONNEL_SEARCH_PARAM, amendDetailsSearchParam);
            ParamUtil.setRequestAttr(bpc.request, GuideConsts.AMEND_UPDATE_PERSONNEL_SEARCH_RESULT, amendDetailsSearchResult);
        }
        log.info("****end ******");

    }

    public void amendLic4_2(BaseProcessClass bpc) {
        amendLic4_1(bpc);
        /*log.info("****start ******");
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();
        List<PersonnelListQueryDto> persons = requestForChangeService.getLicencePersonnelListQueryDto(getLicenseeId(bpc.request));
        if (!IaisCommonUtils.isEmpty(persons)) {
            List<String> idNos = IaisCommonUtils.genNewArrayList();
            for (PersonnelListQueryDto dto : persons) {
                String idNo = dto.getIdNo();
                if (!idNos.contains(idNo)) {
                    idNos.add(idNo);
                    String idType = dto.getIdType();
                    String name = dto.getName();
                    SelectOption s = new SelectOption(idType + "," + idNo, name + ", " + idNo + " (" + MasterCodeUtil.getCodeDesc(idType) + ")");
                    selectOptions.add(s);
                }
            }
        }
        SearchParam amendDetailsSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.AMEND_UPDATE_CONTACT_SEARCH_PARAM,PersonnlAssessQueryDto.class.getName(),"T3.ID",SearchParam.DESCENDING,false);
        amendDetailsSearchParam.addFilter("licenseeId", getLicenseeId(bpc.request), true);
        String idNo = ParamUtil.getString(bpc.request,"personnelOptions");
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = loginContext.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
        HalpSearchResultHelper.setLicParamByField(amendDetailsSearchParam,"serviceTypesShow",HcsaServiceCacheHelper.controlServices(2,userRoleAccessMatrixDtos));
        if (idNo != null && !"Please Select".equals(idNo)){
            List<String> idNos = IaisCommonUtils.genNewArrayList();
            String id = idNo.split(",")[1];
            idNos.add(id);
            if (idNos.size() >0){
                amendDetailsSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.AMEND_UPDATE_CONTACT_SEARCH_PARAM,PersonnlAssessQueryDto.class.getName(),"T3.ID",SearchParam.DESCENDING,false);
                amendDetailsSearchParam.addFilter("licenseeId", getLicenseeId(bpc.request), true);
                amendDetailsSearchParam.addFilter("idNo",idNos,true);
                QueryHelp.setMainSql("interInboxQuery", "appPersonnelQuery", amendDetailsSearchParam);
                SearchResult<PersonnlAssessQueryDto> amendDetailsSearchResult = requestForChangeService.searchAssessPsnInfo(amendDetailsSearchParam);
                if (!StringUtil.isEmpty(amendDetailsSearchResult)) {
                    ParamUtil.setSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_CONTACT_SEARCH_PARAM, amendDetailsSearchParam);
                    ParamUtil.setRequestAttr(bpc.request, GuideConsts.AMEND_UPDATE_CONTACT_SEARCH_RESULT, amendDetailsSearchResult);
                }
            }
        }
        QueryHelp.setMainSql("interInboxQuery", "appPersonnelQuery", amendDetailsSearchParam);
        ParamUtil.setSessionAttr(bpc.request, "personnelOptions", (Serializable) selectOptions);
        log.info("****end ******");*/
    }

    public void searchByIdNo(BaseProcessClass bpc) {
        String idNo = ParamUtil.getString(bpc.request,"personnelOptions");
        if (idNo != null && !"Please Select".equals(idNo)){
            List<String> idNos = IaisCommonUtils.genNewArrayList();
            String id = idNo.split(",")[1];
            idNos.add(id);
            if (idNos.size() >0){
                SearchParam amendDetailsSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.AMEND_UPDATE_CONTACT_SEARCH_PARAM,PersonnlAssessQueryDto.class.getName(),"T3.ID",SearchParam.DESCENDING,false);
                amendDetailsSearchParam.addFilter("licenseeId", getLicenseeId(bpc.request), true);
                amendDetailsSearchParam.addFilter("idNo",idNos,true);
                LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
                List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = loginContext.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
                HalpSearchResultHelper.setLicParamByField(amendDetailsSearchParam,"serviceTypesShow",HcsaServiceCacheHelper.controlServices(2,userRoleAccessMatrixDtos));
                QueryHelp.setMainSql("interInboxQuery", "appPersonnelQuery", amendDetailsSearchParam);
                SearchResult<PersonnlAssessQueryDto> amendDetailsSearchResult = requestForChangeService.searchAssessPsnInfo(amendDetailsSearchParam);
                if (!StringUtil.isEmpty(amendDetailsSearchResult)) {
                    ParamUtil.setSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_CONTACT_SEARCH_PARAM, amendDetailsSearchParam);
                    ParamUtil.setRequestAttr(bpc.request, GuideConsts.AMEND_UPDATE_CONTACT_SEARCH_RESULT, amendDetailsSearchResult);
                }
            }
        }
    }

    public void prepareSwitch(BaseProcessClass bpc) throws IOException {
        log.info("****start ******");
        ParamUtil.setSessionAttr(bpc.request,"licence_err_list",null);
    }

    public void ceaseLic(BaseProcessClass bpc) {
        log.info("****start ******");
        SearchParam ceaseLicenceParam = HalpSearchResultHelper.gainSearchParam(bpc.request,GuideConsts.CEASE_LICENCE_SEARCH_PARAM,SelfPremisesListQueryDto.class.getName(),"PREMISES_TYPE",SearchParam.DESCENDING,false);
        ceaseLicenceParam.addFilter("licenseeId", getLicenseeId(bpc.request), true);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = loginContext.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
        HalpSearchResultHelper.setLicParamByField(ceaseLicenceParam,"serviceTypesShow",HcsaServiceCacheHelper.controlServices(2,userRoleAccessMatrixDtos));
        QueryHelp.setMainSql("interInboxQuery", "queryPremises", ceaseLicenceParam);
        SearchResult<SelfPremisesListQueryDto> ceaseLicenceResult = requestForChangeService.searchPreInfo(ceaseLicenceParam);
        if (!StringUtil.isEmpty(ceaseLicenceResult)) {
            ParamUtil.setSessionAttr(bpc.request, GuideConsts.CEASE_LICENCE_SEARCH_PARAM, ceaseLicenceParam);
            ParamUtil.setRequestAttr(bpc.request, GuideConsts.CEASE_LICENCE_SEARCH_RESULT, ceaseLicenceResult);
        }
        log.info("****end ******");
    }

    public void doCeasLicSort(BaseProcessClass bpc) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, GuideConsts.CEASE_LICENCE_SEARCH_PARAM);
        HalpSearchResultHelper.doSort(bpc.request,searchParam);
    }

    public void doCeasLicPage(BaseProcessClass bpc) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, GuideConsts.CEASE_LICENCE_SEARCH_PARAM);
        HalpSearchResultHelper.doPage(bpc.request,searchParam);
    }

    public void doCeasLicStep(BaseProcessClass bpc) throws IOException {
        String cessationError = null ;
        List<String> licIdValue = IaisCommonUtils.genNewArrayList();
        List<String> licPremIdValue = IaisCommonUtils.genNewArrayList();
        String[] licIds = ParamUtil.getStrings(bpc.request, "ceaseLicIds");
        boolean result = false;
        for (String item : licIds) {
            licPremIdValue.add(ParamUtil.getMaskedString(bpc.request, item));
        }
        for (String item : licPremIdValue) {
            licIdValue.add(licenceInboxClient.getlicPremisesCorrelationsByPremises(item).getEntity().getLicenceId());
        }
        String inbox_ack011 = MessageUtil.getMessageDesc("INBOX_ACK011");
        for(String licId : licIdValue){
            LicenceDto licenceDto = licenceInboxClient.getLicDtoById(licId).getEntity();
            if(licenceDto==null){
                ParamUtil.setRequestAttr(bpc.request,InboxConst.LIC_CEASED_ERR_RESULT,Boolean.TRUE);
                bpc.request.setAttribute("cessationError",inbox_ack011);
                ParamUtil.setSessionAttr(bpc.request,"licence_err_list",(Serializable) licIdValue);
                return ;
            }else {
                if( !ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(licenceDto.getStatus())){
                    if(!(IaisEGPHelper.isActiveMigrated() &&ApplicationConsts.LICENCE_STATUS_APPROVED.equals(licenceDto.getStatus())&&licenceDto.getMigrated()!=0)){
                        ParamUtil.setRequestAttr(bpc.request,InboxConst.LIC_CEASED_ERR_RESULT,Boolean.TRUE);
                        bpc.request.setAttribute("cessationError",inbox_ack011);
                        ParamUtil.setSessionAttr(bpc.request,"licence_err_list",(Serializable) licIdValue);
                        return ;
                    }
                }
            }
        }
        Map<String, Boolean> resultMap = inboxService.listResultCeased(licIdValue);
        for (Map.Entry<String, Boolean> entry : resultMap.entrySet()) {
            if (!entry.getValue()) {
                result = true;
                break;
            }
        }
        List<ApplicationSubDraftDto> draftByLicAppId = inboxService.getDraftByLicAppId(licIdValue.get(0));
        String isNeedDelete = bpc.request.getParameter("isNeedDelete");
        if(!draftByLicAppId.isEmpty()){
            StringBuilder stringBuilder=new StringBuilder();
            for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                stringBuilder.append(applicationSubDraftDto.getDraftNo()).append(' ');
            }
            if("delete".equals(isNeedDelete)){
                bpc.request.setAttribute("isDelete","1");
                for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                    inboxService.deleteDraftByNo(applicationSubDraftDto.getDraftNo());
                }
            }else {
                String ack030 = MessageUtil.getMessageDesc("GENERAL_ACK030");
                String replace = ack030.replace("{draft application no}", stringBuilder.toString());
                bpc.request.setAttribute("draftByLicAppId",replace);
                bpc.request.setAttribute("isCeasedShow","1");
                bpc.request.setAttribute("appealApplication",licIdValue.get(0));
                ParamUtil.setSessionAttr(bpc.request,"licence_err_list",(Serializable) licPremIdValue);
                return;
            }
        }
        if (result) {
            ParamUtil.setRequestAttr(bpc.request, InboxConst.LIC_CEASED_ERR_RESULT, Boolean.TRUE);
            cessationError = MessageUtil.getMessageDesc("CESS_ERR002");
            bpc.request.setAttribute("cessationError",cessationError);
            ParamUtil.setSessionAttr(bpc.request,"licence_err_list",(Serializable) licPremIdValue);
        } else {
            ParamUtil.setSessionAttr(bpc.request, "licIds", (Serializable) licIdValue);
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE + "MohCessationApplication");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }
    }

    public void withdrawApp(BaseProcessClass bpc) {
        SearchParam withdrawAppParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.WITHDRAW_APPLICATION_SEARCH_PARAM,InboxAppQueryDto.class.getName(),"CREATED_DT",SearchParam.DESCENDING,false);
        withdrawAppParam.addFilter("licenseeId", getLicenseeId(bpc.request), true);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = loginContext.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
        HalpSearchResultHelper.setParamByFieldOrSearch(withdrawAppParam,"appServicesShow",HcsaServiceCacheHelper.controlServices(3,userRoleAccessMatrixDtos),"code");
        QueryHelp.setMainSql("interInboxQuery", "assessmentWithdrawAppQuery", withdrawAppParam);
        String repalceService = getRepalceService();
        withdrawAppParam.setMainSql(withdrawAppParam.getMainSql().replace("repalceService",repalceService));
        SearchResult<InboxAppQueryDto> withdrawAppResult = inboxService.appDoQuery(withdrawAppParam);
        if (withdrawAppResult != null) {
            ParamUtil.setSessionAttr(bpc.request, GuideConsts.WITHDRAW_APPLICATION_SEARCH_PARAM, withdrawAppParam);
            ParamUtil.setRequestAttr(bpc.request, GuideConsts.WITHDRAW_APPLICATION_SEARCH_RESULT, withdrawAppResult);
        }
    }

    public void doWithdrawalSort(BaseProcessClass bpc) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, GuideConsts.WITHDRAW_APPLICATION_SEARCH_PARAM);
        HalpSearchResultHelper.doSort(bpc.request,searchParam);
    }

    public void doWithdrawalPage(BaseProcessClass bpc) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, GuideConsts.WITHDRAW_APPLICATION_SEARCH_PARAM);
        HalpSearchResultHelper.doPage(bpc.request,searchParam);
    }

    public void doWithdrawalStep(BaseProcessClass bpc) throws IOException {
        HttpServletRequest request = bpc.request;
        String radioAppId = ParamUtil.getString(request, "withdrawApp");
        String appId = ParamUtil.getMaskedString(request, radioAppId+"Id");
        String appNo = ParamUtil.getMaskedString(request, radioAppId+"No");
        if (!appInboxClient.isApplicationWithdrawal(appId).getEntity()) {
            String withdrawalError = MessageUtil.getMessageDesc("WDL_EER001");
            ParamUtil.setRequestAttr(bpc.request,"licIsWithdrawal",Boolean.TRUE);
            bpc.request.setAttribute(InboxConst.LIC_ACTION_ERR_MSG,withdrawalError);
        } else {
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE + "MohWithdrawalApplication")
                    .append("?withdrawAppId=")
                    .append(MaskUtil.maskValue("withdrawAppId", appId))
                    .append("&withdrawAppNo=")
                    .append(MaskUtil.maskValue("withdrawAppNo", appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }

    }

    public void resumeDraftApp(BaseProcessClass bpc) {
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = loginContext.getLicenseeId();
        SearchParam draftAppSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.DRAFT_APPLICATION_SEARCH_PARAM,InboxAppQueryDto.class.getName(),"CREATED_DT",SearchParam.DESCENDING,false);
        draftAppSearchParam.addFilter("licenseeId", licenseeId, true);
        List<String> inParams = IaisCommonUtils.genNewArrayList();
        inParams.add(ApplicationConsts.APPLICATION_STATUS_DRAFT);
        SqlHelper.builderInSql(draftAppSearchParam, "B.status", "appStatus", inParams);
        List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = loginContext.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
        HalpSearchResultHelper.setAppParamByField(draftAppSearchParam,"appServicesShow",HcsaServiceCacheHelper.controlServices(3,userRoleAccessMatrixDtos));
        QueryHelp.setMainSql("interInboxQuery", "applicationQuery", draftAppSearchParam);
        String repalceService = getRepalceService();
        draftAppSearchParam.setMainSql(draftAppSearchParam.getMainSql().replace("repalceService",repalceService));
        SearchResult<InboxAppQueryDto> draftAppSearchResult = inboxService.appDoQuery(draftAppSearchParam);

        if (!StringUtil.isEmpty(draftAppSearchResult)) {
            ParamUtil.setSessionAttr(bpc.request, GuideConsts.DRAFT_APPLICATION_SEARCH_PARAM, draftAppSearchParam);
            ParamUtil.setRequestAttr(bpc.request, GuideConsts.DRAFT_APPLICATION_SEARCH_RESULT, draftAppSearchResult);
        }
    }

    public  String getRepalceService(){
        List<HcsaServiceDto> hcsaServiceDtos = hcsaConfigClient.getActiveServices().getEntity();
        if(IaisCommonUtils.isEmpty(hcsaServiceDtos)){
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" ( CASE app.service_id ");
        for(HcsaServiceDto hcsaServiceDto :hcsaServiceDtos){
            stringBuilder.append(" WHEN '").append(hcsaServiceDto.getId()).append("' Then '").append(hcsaServiceDto.getSvcCode()).append("'  ");
        }
        stringBuilder.append("ELSE  'N/A' END )");
        return  stringBuilder.toString();
    }

    public void resumeDraftAppStep(BaseProcessClass bpc) throws IOException {
        log.debug("The prepareEdit start ...");
        HttpServletRequest request = bpc.request;
        String radioappNo = ParamUtil.getString(request, "resumeAppNos");
        String appNo = ParamUtil.getString(request, radioappNo+"No");
        String appType = MasterCodeUtil.getCodeDesc(ParamUtil.getString(request, radioappNo+"Type")).trim();
        if(InboxConst.APP_DO_DRAFT_TYPE_RFC.equals(appType)){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohRequestForChange/prepareDraft")
                    .append("?DraftNumber=")
                    .append(MaskUtil.maskValue(DRAFT_NUMBER,appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }else if(InboxConst.APP_DO_DRAFT_TYPE_RENEW.equals(appType)){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohWithOutRenewal")
                    .append("?DraftNumber=")
                    .append(MaskUtil.maskValue(DRAFT_NUMBER,appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }else if(InboxConst.APP_DO_DRAFT_TYPE_APPEAL.equals(appType)){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohAppealApplication")
                    .append("?DraftNumber=")
                    .append(MaskUtil.maskValue(DRAFT_NUMBER,appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }
        else {
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohNewApplication")
                    .append("?DraftNumber=")
                    .append(MaskUtil.maskValue(DRAFT_NUMBER,appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }
    }

    public void submitDateMohStep(BaseProcessClass bpc) throws IOException {
        String additional = ParamUtil.getString(bpc.request,CRUD_ACTION_ADDITIONAL);
        StringBuilder url = new StringBuilder();
        switch (additional){
            case DataSubmissionConsts.DS_AR:
                ParamUtil.setSessionAttr(bpc.request,"DsModleSelect","AR");break;
            case DataSubmissionConsts.DS_DRP:
                ParamUtil.setSessionAttr(bpc.request,"DsModleSelect","DP");break;
            case DataSubmissionConsts.DS_LDT:
                ParamUtil.setSessionAttr(bpc.request,"DsModleSelect","LDT");break;
            case DataSubmissionConsts.DS_TOP:
                ParamUtil.setSessionAttr(bpc.request,"DsModleSelect","TP"); break;
            case DataSubmissionConsts.DS_VSS:
                ParamUtil.setSessionAttr(bpc.request,"DsModleSelect","VS");break;
            default:
                ParamUtil.setSessionAttr(bpc.request,"DsModleSelect","LDT");
        }
        url.append(InboxConst.URL_HTTPS)
                .append(bpc.request.getServerName())
                .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohDataSubmission/PrepareCompliance").append("?selfAssessmentGuide=true");
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
    }

    public void subDateMoh(BaseProcessClass bpc) throws IOException {

    }

    public void resumeSort(BaseProcessClass bpc) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, GuideConsts.DRAFT_APPLICATION_SEARCH_PARAM);
        HalpSearchResultHelper.doSort(bpc.request,searchParam);
    }

    public void resumePage(BaseProcessClass bpc) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, GuideConsts.DRAFT_APPLICATION_SEARCH_PARAM);
        HalpSearchResultHelper.doPage(bpc.request,searchParam);
    }

    public void updateHCIPage(BaseProcessClass bpc) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, "amendHCISearchParam");
        HalpSearchResultHelper.doPage(bpc.request,searchParam);
    }

    public void updateHCISort(BaseProcessClass bpc) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, "amendHCISearchParam");
        HalpSearchResultHelper.doSort(bpc.request,searchParam);
    }

    public void amendUpdateVehiclesPage(BaseProcessClass bpc) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_VEHICLES_SEARCH_PARAM);
        HalpSearchResultHelper.doPage(bpc.request,searchParam);
    }

    public void amendUpdateVehiclesSort(BaseProcessClass bpc) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_VEHICLES_SEARCH_PARAM);
        HalpSearchResultHelper.doSort(bpc.request,searchParam);
    }

    public void doAmendLicStep(BaseProcessClass bpc) throws IOException {
        String action = ParamUtil.getString(bpc.request, "guide_action_type");
        String licId = ParamUtil.getString(bpc.request, "amendLicenseId");
        String idNoPersonnal = ParamUtil.getString(bpc.request, "personnelOptions");
        String licIdValue = ParamUtil.getMaskedString(bpc.request, licId);
        String hiddenIndex = ParamUtil.getMaskedString(bpc.request, licId+"hiddenIndex");
        String premiseIdValue = ParamUtil.getMaskedString(bpc.request, licId+"premiseId");
        ParamUtil.setSessionAttr(bpc.request,"licence_err_list",licIdValue);
        boolean flag = false;
        if (idNoPersonnal != null){
            String id = idNoPersonnal.split(",")[1];
            List<String> idNos = IaisCommonUtils.genNewArrayList();
            idNos.add(id);
            List<PersonnelListDto> personnelListDtoList = requestForChangeService.getPersonnelListAssessment(idNos,getOrgId(bpc.request));
            ParamUtil.setSessionAttr(bpc.request, "personnelListDtos", (Serializable) personnelListDtoList);
            if("amendLic7".equals(action)) {
                StringBuilder url = new StringBuilder();
                url.append(InboxConst.URL_HTTPS)
                        .append(bpc.request.getServerName())
                        .append(InboxConst.URL_LICENCE_WEB_MODULE + "MohRfcPersonnelList/initPsnEditInfo")
                        .append("?personnelNo=")
                        .append(MaskUtil.maskValue("personnelNo", id));
                ParamUtil.setRequestAttr(bpc.request, "url", url.toString());
                ParamUtil.setRequestAttr(bpc.request, "amend_action_type", "redirect");
            }
        }
        if(licIdValue != null){
            Map<String, String> errorMap = inboxService.checkRfcStatus(licIdValue);
            List<ApplicationSubDraftDto> draftByLicAppId = inboxService.getDraftByLicAppId(licIdValue);
            String isNeedDelete = bpc.request.getParameter("isNeedDelete");
                StringBuilder stringBuilder=new StringBuilder();
            if(!draftByLicAppId.isEmpty()){
                for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                    stringBuilder.append(applicationSubDraftDto.getDraftNo()).append(' ');
                }
                if("delete".equals(isNeedDelete)){
                    for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                        inboxService.deleteDraftByNo(applicationSubDraftDto.getDraftNo());
                    }
                }else {
                    String ack030 = MessageUtil.getMessageDesc("GENERAL_ACK030");
                    String replace = ack030.replace("{draft application no}", stringBuilder.toString());
                    bpc.request.setAttribute("draftByLicAppId",replace);
                    bpc.request.setAttribute("isAmendShow","1");
                    bpc.request.setAttribute("appealApplication",licIdValue);
                }
                flag = true;
                errorMap.put("err","amend do draft");
            }
            if(errorMap.isEmpty()){
                if ("amendLic2".equals(action)){
                    StringBuilder url = new StringBuilder();
                    url.append(InboxConst.URL_HTTPS)
                            .append(bpc.request.getServerName())
                            .append(InboxConst.URL_LICENCE_WEB_MODULE + "MohRfcPermisesList/doPremisesList")
                            .append("?hiddenIndex=")
                            .append(hiddenIndex)
                            .append("&premisesId")
                            .append(hiddenIndex)
                            .append('=')
                            .append(MaskUtil.maskValue("premisesId"+hiddenIndex, premiseIdValue))
                            .append("&crud_action_type")
                            .append("=prePremisesEdit")
                            .append("&licId")
                            .append(hiddenIndex)
                            .append('=')
                            .append(MaskUtil.maskValue("licId"+hiddenIndex, licIdValue));
                    ParamUtil.setRequestAttr(bpc.request, "url", url.toString());
                    ParamUtil.setRequestAttr(bpc.request, "amend_action_type", "redirect");
                } else {
                    StringBuilder url = new StringBuilder();
                    url.append(InboxConst.URL_HTTPS)
                            .append(bpc.request.getServerName())
                            .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohRequestForChange")
                            .append("?licenceId=")
                            .append(MaskUtil.maskValue("licenceId",licIdValue));
                    ParamUtil.setRequestAttr(bpc.request, "url", url.toString());
                    ParamUtil.setRequestAttr(bpc.request, "amend_action_type", "redirect");
                }
            }else{
                if ("amendLic2".equals(action)){
                    ParamUtil.setRequestAttr(bpc.request,"amend_action_type","toamend2");
                }else if("amendLic1".equals(action)){
                    ParamUtil.setRequestAttr(bpc.request,"amend_action_type","toamend1_2");
                }else if("amendLic4".equals(action)){
                    ParamUtil.setRequestAttr(bpc.request,"amend_action_type","toamend1_1");
                }
                else if("amendLic3".equals(action)){
                    ParamUtil.setRequestAttr(bpc.request,"amend_action_type","toamend3_1");
                }
                else if("amendLic5".equals(action)){
                    ParamUtil.setRequestAttr(bpc.request,"amend_action_type","toamend4_1");
                }
                else if("amendLic6".equals(action)){
                    ParamUtil.setRequestAttr(bpc.request,"amend_action_type","toamend3_2");
                }
                else if("amendLic7".equals(action)){
                    ParamUtil.setRequestAttr(bpc.request,"amend_action_type","toamend4_2");
                }
                else if("amendLic8".equals(action)){
                    ParamUtil.setRequestAttr(bpc.request,"amend_action_type","toamend5");
                }
                if (!flag){
                    ParamUtil.setRequestAttr(bpc.request,"licIsAmend",Boolean.TRUE);
                    ParamUtil.setRequestAttr(bpc.request,InboxConst.LIC_ACTION_ERR_MSG,errorMap.get("errorMessage"));
                }
            }
        }
    }

    /**
     * Step: RedirectToAmend
     *
     * @param bpc
     * @throws IOException
     */
    public void redirectToAmend(BaseProcessClass bpc) throws IOException {
        HttpServletRequest request = bpc.request;
        String url = (String) ParamUtil.getRequestAttr(request, "url");
        log.info(StringUtil.changeForLog("URL: " + url));
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url, request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
    }

    public void updateAdminPers(BaseProcessClass bpc) throws IOException {
        HttpServletRequest request = bpc.request;
        StringBuilder url = new StringBuilder();
        url.append(InboxConst.URL_HTTPS).append(request.getServerName())
                .append("/main-web/eservice/INTERNET/MohFeAdminUserManagement");
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
    }

    public void updateLicenceSort(BaseProcessClass bpc) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_LICENSEE_SEARCH_PARAM);
        HalpSearchResultHelper.doPage(bpc.request,searchParam);
    }

    public void updateLicencePage(BaseProcessClass bpc) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_LICENSEE_SEARCH_PARAM);
        HalpSearchResultHelper.doPage(bpc.request,searchParam);
    }

    public void addServicePage(BaseProcessClass bpc) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, GuideConsts.AMEND_DETAILS_SEARCH_PARAM);
        HalpSearchResultHelper.doPage(bpc.request,searchParam);
    }

    public void addServiceSort(BaseProcessClass bpc) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, GuideConsts.AMEND_DETAILS_SEARCH_PARAM);
        HalpSearchResultHelper.doSort(bpc.request,searchParam);
    }

    public void removeServicePage(BaseProcessClass bpc) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, GuideConsts.AMEND_DETAILS_REMOVE_SEARCH_PARAM);
        HalpSearchResultHelper.doPage(bpc.request,searchParam);
    }

    public void removeServiceSort(BaseProcessClass bpc) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, GuideConsts.AMEND_DETAILS_REMOVE_SEARCH_PARAM);
        HalpSearchResultHelper.doSort(bpc.request,searchParam);
    }

    public void addRemovePersonalPage(BaseProcessClass bpc) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_PERSONNEL_SEARCH_PARAM);
        HalpSearchResultHelper.doPage(bpc.request,searchParam);
    }

    public void addRemovePersonalSort(BaseProcessClass bpc) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_PERSONNEL_SEARCH_PARAM);
        HalpSearchResultHelper.doSort(bpc.request,searchParam);
    }

    public void updateLicenceesPage(BaseProcessClass bpc) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_LICENSEES_SEARCH_PARAM);
        HalpSearchResultHelper.doPage(bpc.request,searchParam);
    }

    public void updateLicenceesSort(BaseProcessClass bpc) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_LICENSEES_SEARCH_PARAM);
        HalpSearchResultHelper.doSort(bpc.request,searchParam);
    }

    public void updateContactSort(BaseProcessClass bpc) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_CONTACT_SEARCH_PARAM);
        HalpSearchResultHelper.doSort(bpc.request,searchParam);
    }

    public void updateContactPage(BaseProcessClass bpc) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_CONTACT_SEARCH_PARAM);
        HalpSearchResultHelper.doPage(bpc.request,searchParam);
    }

    public void jumpInstructionPage(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("jumpInstructionPage start..."));


        log.info(StringUtil.changeForLog("jumpInstructionPage end..."));
    }

    public void prepareJump(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("prepareJump start..."));
        String action = (String) ParamUtil.getRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE);
        if(StringUtil.isEmpty(action)){
            action = "chooseSvc";
        }
        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,action);
        log.info(StringUtil.changeForLog("prepareJump end..."));
    }

    private static List<String> getAppAlignLicQueryHci(Map<String,List<AppAlignLicQueryDto>> baseSvcPremMap,String svcName){
        List<String> premHcis = IaisCommonUtils.genNewArrayList();
        if(baseSvcPremMap != null && !StringUtil.isEmpty(svcName)){
            List<AppAlignLicQueryDto> appAlignLicQueryDtos = baseSvcPremMap.get(svcName);
            if(!IaisCommonUtils.isEmpty(appAlignLicQueryDtos)){
                for(AppAlignLicQueryDto appAlignLicQueryDto:appAlignLicQueryDtos){
                    premHcis.add(getPremisesHci(appAlignLicQueryDto));
                }
            }
        }
        return premHcis;
    }

    private static String getPremisesHci(AppAlignLicQueryDto item){
        String additional = item.getPremisesType() + ApplicationConsts.DELIMITER + item.getHciName();
        if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(item.getPremisesType())) {
            additional += ApplicationConsts.DELIMITER + item.getVehicleNo();
        }
        return IaisCommonUtils.getPremisesKey(additional, item.getPostalCode(), item.getBlkNo(), item.getStreetName(),
                item.getBuildingName(), item.getFloorNo(), item.getUnitNo(),
                MiscUtil.transferEntityDtos(item.getPremisesOperationalUnitDtos(),
                        AppPremisesOperationalUnitDto.class));
    }

    private static List<AppSvcRelatedInfoDto> sortAppSvcRelatDto(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos){
        List<AppSvcRelatedInfoDto> newAppSvcDto = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            List<AppSvcRelatedInfoDto> baseDtos = IaisCommonUtils.genNewArrayList();
            List<AppSvcRelatedInfoDto> specDtos = IaisCommonUtils.genNewArrayList();
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                String svcCode = appSvcRelatedInfoDto.getServiceCode();
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByCode(svcCode);
                if(hcsaServiceDto == null){
                    log.info(StringUtil.changeForLog("svc code:"+svcCode+" can not found HcsaServiceDto"));
                    continue;
                }
                String serviceType = hcsaServiceDto.getSvcType();
                appSvcRelatedInfoDto.setServiceName(hcsaServiceDto.getSvcName());
                if(HcsaConsts.SERVICE_TYPE_BASE.equals(serviceType)){
                    baseDtos.add(appSvcRelatedInfoDto);
                }else if (HcsaConsts.SERVICE_TYPE_SPECIFIED.equals(serviceType)){
                    specDtos.add(appSvcRelatedInfoDto);
                }
            }

            if(!IaisCommonUtils.isEmpty(baseDtos)){
                baseDtos.sort(Comparator.comparing(AppSvcRelatedInfoDto::getServiceName));
                newAppSvcDto.addAll(baseDtos);
            }
            if(!IaisCommonUtils.isEmpty(specDtos)){
                specDtos.sort(Comparator.comparing(AppSvcRelatedInfoDto::getServiceName));
                newAppSvcDto.addAll(specDtos);
            }
        }
        return newAppSvcDto;
    }

    private PaginationHandler<MenuLicenceDto> initPaginationHandler(List<MenuLicenceDto> newAppLicDtos){
        PaginationHandler<MenuLicenceDto> paginationHandler = new PaginationHandler<>("licPagDiv","licBodyDiv");
        paginationHandler.setAllData(newAppLicDtos);
        paginationHandler.setCheckType(PaginationHandler.CHECK_TYPE_RADIO);
        paginationHandler.setPageSize(10);
        paginationHandler.preLoadingPage();
        return paginationHandler;
    }

    private SearchResult<MenuLicenceDto> getAlignLicPremInfo(List<String> excludeChkBase,String licenseeId,Set<String> premisesTypeList, int alignMinExpiryMonth){
        if(StringUtil.isEmpty(licenseeId)){
            return null;
        }
        if(IaisCommonUtils.isEmpty(excludeChkBase)){
            excludeChkBase = IaisCommonUtils.genNewArrayList();
        }
        SearchParam searchParam = new SearchParam(MenuLicenceDto.class.getName());
        if(!IaisCommonUtils.isEmpty(excludeChkBase)){
            StringBuilder placeholder = new StringBuilder("(");
            int i =0;
            for(String baseSvcId:excludeChkBase){
                placeholder.append(":itemKey").append(i).append(',');
                i++;
            }
            String inSql = placeholder.substring(0, placeholder.length() - 1) + ")";
            searchParam.addParam("serName", inSql);
            i = 0;
            for(String baseSvcId:excludeChkBase){
                searchParam.addFilter("itemKey" + i,
                        HcsaServiceCacheHelper.getServiceById(baseSvcId).getSvcName());
                i ++;
            }
        }else{
            String serName = "('')";
            searchParam.addParam("serName", serName);
        }
        //add premType filter
        if(!IaisCommonUtils.isEmpty(premisesTypeList)){
            int i = 0;
            StringBuilder premTypeItem = new StringBuilder("(");
            for(String premisesType:premisesTypeList){
                premTypeItem.append(":premType").append(i).append(',');
                i++;
            }
            String premTypeItemStr = premTypeItem.substring(0, premTypeItem.length() - 1) + ")";
            searchParam.addParam("premTypeList", premTypeItemStr);
            i = 0;
            for(String premisesType:premisesTypeList){
                searchParam.addFilter("premType" + i, premisesType);
                i ++;
            }
        }else{
            String premType = "('')";
            searchParam.addParam("premTypeList", premType);
            log.debug(StringUtil.changeForLog("No intersection data ..."));
        }
        log.debug(StringUtil.changeForLog("alignMinExpiryMonth:" + alignMinExpiryMonth));
        searchParam.addFilter("alignMinExpiryMonth", alignMinExpiryMonth,true);

        searchParam.addFilter("licenseeId",licenseeId,true);
        QueryHelp.setMainSql("interInboxQuery", "getLicenceBySerName",searchParam);
        return assessmentGuideService.getMenuLicence(searchParam);
    }

    private List<MenuLicenceDto> removePendAndExistPrem(List<String> excludeChkBase,List<MenuLicenceDto> menuLicenceDtos,String licenseeId){
        List<MenuLicenceDto> newAppLicDtos = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(excludeChkBase) && !IaisCommonUtils.isEmpty(menuLicenceDtos) && !StringUtil.isEmpty(licenseeId)){
            menuLicenceDtos = assessmentGuideService.setPremAdditionalInfo(menuLicenceDtos);
            List<HcsaServiceDto> hcsaServiceDtos = IaisCommonUtils.genNewArrayList();
            for(String svcId:excludeChkBase){
                HcsaServiceDto svcDto = HcsaServiceCacheHelper.getServiceById(svcId);
                if(svcDto != null){
                    hcsaServiceDtos.add(svcDto);
                }
            }
            List<String> pendAndLicPremHci = assessmentGuideService.getHciFromPendAppAndLic(licenseeId,hcsaServiceDtos);
            for(MenuLicenceDto menuLicenceDto:menuLicenceDtos){
                PremisesDto premisesDto = MiscUtil.transferEntityDto(menuLicenceDto,PremisesDto.class);
                List<String> premisesHciList = genPremisesHciList(premisesDto);
                boolean pendPremOrExistLic = false;
                for(String premisesHci:premisesHciList){
                    if(pendAndLicPremHci.contains(premisesHci)){
                        pendPremOrExistLic = true;
                        break;
                    }
                }
                if(!pendPremOrExistLic){
                    newAppLicDtos.add(menuLicenceDto);
                }
            }
        }
        return newAppLicDtos;
    }

    private static List<String> genPremisesHciList(PremisesDto premisesDto){
        return IaisCommonUtils.getPremisesHciList(premisesDto);
    }

    private List<String> transferToList(Set<String> targetSet){
        List<String> result = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(targetSet)){
            targetSet.forEach(val->{
                result.add(val);
            });
        }
        return result;
    }

    private String getLicenseeId(HttpServletRequest request) {
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        return loginContext.getLicenseeId();
    }

    private String getOrgId(HttpServletRequest request) {
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        return loginContext.getOrgId();
    }

    public static void setParamByField(SearchParam searchParam,String key,String value,boolean isTemplateParam,String allValue){
        if(StringUtil.isEmpty(value) || value.equalsIgnoreCase(allValue)){
            searchParam.removeFilter(key);
            if(isTemplateParam){
                searchParam.removeParam(key);
            }
        }else {
            searchParam.addFilter(key,value,isTemplateParam);
        }
    }

    public static void setParamByField(SearchParam searchParam,String key,String value,boolean isTemplateParam){
        setParamByField(searchParam,key,value,isTemplateParam,null);
    }

    public static void setParamByField(SearchParam searchParam,String key,List<String> values){
        HalpSearchResultHelper.setParamByField(searchParam,key,values);
    }

    public static void setParamForDate(HttpServletRequest request,SearchParam searchParam,String key,String value){
        try {
            String dateString = ParamUtil.getDate(request, value);
            Date lastDateStart = Formatter.parseDate(dateString);
            if(lastDateStart!=null){
                log.info(StringUtil.changeForLog("---------"+ lastDateStart));
                dateString = Formatter.formatDateTime(lastDateStart,"yyyy-MM-dd HH:mm:ss");
                log.info(StringUtil.changeForLog("----- dateString : " + dateString));
                searchParam.addFilter(key,dateString,true);
            }else {
                searchParam.removeFilter(key);
                searchParam.removeParam(key);
            }
        }catch (ParseException parseException){
            log.error(parseException.getMessage(),parseException);
            searchParam.removeFilter(key);
            searchParam.removeParam(key);
        }
    }

}
