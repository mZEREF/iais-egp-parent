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
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
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
import com.ecquaria.cloud.moh.iais.constant.FeMainConst;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
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
import java.util.Arrays;
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

    private static final String BASE_SERVICE_CHECK_BOX_ATTR = "basechk";
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
    private static final String NO_EXIST_BASE_LIC = "noExistBaseLic";
    private static final String NO_EXIST_BASE_APP = "noExistBaseApp";
    private static final String DRAFT_NUMBER                                 = "DraftNumber";

    private static final String RELOAD_BASE_SVC_SELECTED = "reloadBaseSvcSelected";
    public static final String SELECT_DRAFT_NO          ="selectDraftNo";
    private static final String LIC_ALIGN_SEARCH_PARAM = "licAlignSearchParam";
    private static final String FIRST = "first";
    private static final String NOT_CONTAINED = "notContained";
    private static final String LICPAGEDIV_SESSION_ATTR = "licPagDiv__SessionAttr";
    private static final String APPPAGDIV_SESSION_ATTR = "appPagDiv__SessionAttr";
    private static final String BUNDLE_ACH_OR_MS = "bundleAchOrMs";
    private static final String CHOOSE_BASE_ERR = "chooseBaseErr";
    private static final String START  = "****start ******";
    private static final String END  = "****end ******";
    private static final String PERSONNELOPTIONS  = "personnelOptions";
    private static final String PREMISES_TYPE  ="PREMISES_TYPE";
    private static final String LICENSEEID  = "licenseeId";
    private static final String SERVICE_TYPES_SHOW  = "serviceTypesShow";
    private static final String QUERY_PREMISES  = "queryPremises";
    private static final String INTER_INBOX_QUERY = "interInboxQuery";
    private static final String CRUD_ACTION_VALUE = "crud_action_value";
    private static final String DRAFT_NO = "draftNo";
    private static final String CONTINUE = "continue";
    private static final String RESUME = "resume";
    private static final String DOBACK = "doBack";
    private static final String AUTO_BUNDLE = "autoBundle";
    private static final String SERNAME = "serName";
    private static final String ITEMKEY = "itemKey";
    private static final String PREM_TYPE_LIST = "premTypeList";
    private static final String REDIRECT = "redirect";
    private static final String GUIDE_BACK_ACTION = "guide_back_action";
    private static final String LICENCE_ERR_LIST = "licence_err_list";
    private static final String AMEND_LICENSE_ID = "amendLicenseId";
    private static final String IS_NEED_DELETE = "isNeedDelete";
    private static final String DELETE = "delete";
    private static final String MOH_REQUEST_FOR_CHANGE = "MohRequestForChange";
    private static final String LICENCEID = "licenceId";
    private static final String AMEND_HCI_SEARCH_PARAM = "amendHCISearchParam";
    private static final String CESSATION_ERROR = "cessationError";
    private static final String DS_MODLE_SELECT = "DsModleSelect";
    private static final String AMEND_ACTION_TYPE = "amend_action_type";

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
        log.info(START);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_MAIN_FUNCTION, AuditTrailConsts.FUNCTION_ASEESSMENT_GUIDE);
        ParamUtil.setSessionAttr(bpc.request, PERSONNELOPTIONS, null);
        ParamUtil.setSessionAttr(bpc.request, GuideConsts.RENEW_LICENCE_SEARCH_PARAM, null);
        ParamUtil.setSessionAttr(bpc.request, GuideConsts.RENEW_LICENCE_UPDATE_SEARCH_PARAM, null);
        ParamUtil.setSessionAttr(bpc.request, GuideConsts.AMEND_DETAILS_SEARCH_PARAM, null);
        ParamUtil.setSessionAttr(bpc.request, FeMainConst.PREMISESLISTDTOS, null);
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
        String inboxAck016 = MessageUtil.getMessageDesc("INBOX_ACK016");
        String inboxAck017 = MessageUtil.getMessageDesc("INBOX_ACK017");
        String inboxAck018 = MessageUtil.getMessageDesc("INBOX_ACK018");
        String inboxAck019 = MessageUtil.getMessageDesc("INBOX_ACK019");
        String inboxAck020 = MessageUtil.getMessageDesc("INBOX_ACK020");
        String inboxAck021 = MessageUtil.getMessageDesc("INBOX_ACK021");
        String inboxAck022 = MessageUtil.getMessageDesc("INBOX_ACK022");
        String inboxAck023 = MessageUtil.getMessageDesc("INBOX_ACK023");
        String inboxAck024 = MessageUtil.getMessageDesc("INBOX_ACK024");
        String selfAck001 = MessageUtil.getMessageDesc("SELF_ACK001");
        String selfAck002 = MessageUtil.getMessageDesc("SELF_ACK002");
        String selfAck003 = MessageUtil.getMessageDesc("SELF_ACK003");
        String selfAck004 = MessageUtil.getMessageDesc("SELF_ACK004");
        String selfAck005 = MessageUtil.getMessageDesc("SELF_ACK005");
        String selfAck006 = MessageUtil.getMessageDesc("SELF_ACK006");
        String selfAck007 = MessageUtil.getMessageDesc("SELF_ACK007");
        String selfAck008 = MessageUtil.getMessageDesc("SELF_ACK008");
        String selfAck009 = MessageUtil.getMessageDesc("SELF_ACK009");
        String selfAck010 = MessageUtil.getMessageDesc("SELF_ACK010");
        String selfAck011 = MessageUtil.getMessageDesc("SELF_ACK011");
        String selfAck012 = MessageUtil.getMessageDesc("SELF_ACK012");
        String selfAck013 = MessageUtil.getMessageDesc("SELF_ACK013");
        String selfAck014 = MessageUtil.getMessageDesc("SELF_ACK014");
        String selfAck015 = MessageUtil.getMessageDesc("SELF_ACK015");

        ParamUtil.setSessionAttr(bpc.request,"inbox_ack016",inboxAck016);
        ParamUtil.setSessionAttr(bpc.request,"inbox_ack017",inboxAck017);
        ParamUtil.setSessionAttr(bpc.request,"inbox_ack018",inboxAck018);
        ParamUtil.setSessionAttr(bpc.request,"inbox_ack019",inboxAck019);
        ParamUtil.setSessionAttr(bpc.request,"inbox_ack020",inboxAck020);
        ParamUtil.setSessionAttr(bpc.request,"inbox_ack021",inboxAck021);
        ParamUtil.setSessionAttr(bpc.request,"inbox_ack022",inboxAck022);
        ParamUtil.setSessionAttr(bpc.request,"inbox_ack023",inboxAck023);
        ParamUtil.setSessionAttr(bpc.request,"inbox_ack024",inboxAck024);
        ParamUtil.setSessionAttr(bpc.request,"self_ack001",selfAck001);
        ParamUtil.setSessionAttr(bpc.request,"self_ack002",selfAck002);
        ParamUtil.setSessionAttr(bpc.request,"self_ack003",selfAck003);
        ParamUtil.setSessionAttr(bpc.request,"self_ack004",selfAck004);
        ParamUtil.setSessionAttr(bpc.request,"self_ack005",selfAck005);
        ParamUtil.setSessionAttr(bpc.request,"self_ack006",selfAck006);
        ParamUtil.setSessionAttr(bpc.request,"self_ack007",selfAck007);
        ParamUtil.setSessionAttr(bpc.request,"self_ack008",selfAck008);
        ParamUtil.setSessionAttr(bpc.request,"self_ack009",selfAck009);
        ParamUtil.setSessionAttr(bpc.request,"self_ack010",selfAck010);
        ParamUtil.setSessionAttr(bpc.request,"self_ack011",selfAck011);
        ParamUtil.setSessionAttr(bpc.request,"self_ack012",selfAck012);
        ParamUtil.setSessionAttr(bpc.request,"self_ack013",selfAck013);
        ParamUtil.setSessionAttr(bpc.request,"self_ack014",selfAck014);
        ParamUtil.setSessionAttr(bpc.request,"self_ack015",selfAck015);
        log.info(END);
    }

    public void perDate(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("perDate"));
    }

    public void newApp1(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("prepareData start ..."));
        ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR_CHECKED, null);
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR_CHECKED, null);
        ParamUtil.setSessionAttr(bpc.request,SELECT_DRAFT_NO,null);
        String action = (String) ParamUtil.getRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE);
        if(StringUtil.isEmpty(action)){
            action = CHOOSE_SERVICE;
        }
        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,action);
        log.info(StringUtil.changeForLog("prepareData start ..."));
    }

    public void renewLic(BaseProcessClass bpc) throws IOException {
        log.info(START);
        SearchParam renewLicSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.RENEW_LICENCE_SEARCH_PARAM,SelfPremisesListQueryDto.class.getName(),PREMISES_TYPE,SearchParam.DESCENDING,false);
        String licenseeId = getLicenseeId(bpc.request);
        renewLicSearchParam.addFilter(LICENSEEID, licenseeId, true);
        LoginContext lc = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = lc.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
        HalpSearchResultHelper.setLicParamByField(renewLicSearchParam,SERVICE_TYPES_SHOW,HcsaServiceCacheHelper.controlServices(2,userRoleAccessMatrixDtos));
        QueryHelp.setMainSql(INTER_INBOX_QUERY, QUERY_PREMISES, renewLicSearchParam);
        SearchResult<SelfPremisesListQueryDto> renewLicSearchResult = requestForChangeService.searchPreInfo(renewLicSearchParam);
        if (!StringUtil.isEmpty(renewLicSearchResult)) {
            ParamUtil.setSessionAttr(bpc.request, GuideConsts.RENEW_LICENCE_SEARCH_PARAM, renewLicSearchParam);
            ParamUtil.setRequestAttr(bpc.request, GuideConsts.RENEW_LICENCE_SEARCH_RESULT, renewLicSearchResult);
        }
        log.info(END);
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
            boolean hasExistBase = searchResult != null && searchResult.getRowCount() > 0;
            ParamUtil.setRequestAttr(bpc.request,HAS_EXISTING_BASE,hasExistBase);
        }
        log.info(StringUtil.changeForLog("prepare choose align end ..."));
    }

    public void doChooseAlign(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("do choose align start ..."));
        ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        //dont have existing base
        String isAlign = ParamUtil.getString(bpc.request,"isAlign");
        AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
        appSelectSvcDto.setAlign(AppConsts.YES.equals(isAlign));
        String additional = ParamUtil.getString(bpc.request,CRUD_ACTION_ADDITIONAL);
        if(BACK_ATTR.equals(additional)){
            return;
        }
        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
        String nextstep = ParamUtil.getString(bpc.request,CRUD_ACTION_ADDITIONAL);
        if(NEXT.equals(nextstep)){
            getDraft(bpc);
            String crudActionValue=bpc.request.getParameter(CRUD_ACTION_VALUE);
            String draftNo  =bpc.request.getParameter(DRAFT_NO);
            String attribute =(String)bpc.request.getAttribute(SELECT_DRAFT_NO);
            if(CONTINUE.equals(crudActionValue)){
                bpc.request.getSession().setAttribute(DRAFT_NUMBER, null);
                bpc.request.getSession().setAttribute(SELECT_DRAFT_NO, draftNo);
            }else if(RESUME.equals(crudActionValue)){
                bpc.request.getSession().setAttribute(DRAFT_NUMBER, draftNo);
            }else if(attribute!=null){
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,DOBACK);
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,CHOOSE_ALIGN);
            }
        }

        String switchStep = ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE);
        if(NEXT.equals(switchStep)){
            String crudActionValue=bpc.request.getParameter(CRUD_ACTION_VALUE);
            String draftNo  =bpc.request.getParameter(DRAFT_NO);
            getDraft(bpc);
            String attribute =(String)bpc.request.getAttribute( SELECT_DRAFT_NO);
            if(CONTINUE.equals(crudActionValue)){
                List<String> list=new ArrayList<>(1);
                list.add(draftNo);
                log.info(StringUtil.changeForLog("delete draft start ..."));
                appInboxClient.deleteDraftNUmber(list);
                bpc.request.getSession().setAttribute(DRAFT_NUMBER, null);
            }else if(RESUME.equals(crudActionValue)){
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
        boolean bundleAchOrMs = (boolean) ParamUtil.getSessionAttr(bpc.request, BUNDLE_ACH_OR_MS);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = IaisCommonUtils.genNewArrayList();
        List<AppLicBundleDto> appLicBundleDtoList=IaisCommonUtils.genNewArrayList();
        String licenceId="";
        String applicationNo="";
        String erroMsg = "";

        if (!noExistBaseLic){
            PaginationHandler<AppAlignLicQueryDto> paginationHandler = (PaginationHandler<AppAlignLicQueryDto>) ParamUtil.getSessionAttr(bpc.request,LICPAGEDIV_SESSION_ATTR);
            AppAlignLicQueryDto checkData = new AppAlignLicQueryDto();
            if (paginationHandler!=null){
                paginationHandler.keepCurrentPageChecked();
                List<AppAlignLicQueryDto> allCheckedData = paginationHandler.getAllCheckedData();
                if(allCheckedData != null && !allCheckedData.isEmpty()){
                    checkData = allCheckedData.get(0);
                }
                if (checkData.getSvcName()==null){
                    ParamUtil.setRequestAttr(bpc.request,CHOOSE_BASE_ERR,IaisEGPConstant.ERR_MANDATORY);
                }
            }
            if (!FIRST.equals(checkData.getSvcName())&&checkData.getSvcName()!=null){
                HcsaServiceDto baseServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(checkData.getSvcName());
                AppLicBundleDto appLicBundleDto=new AppLicBundleDto();
                appLicBundleDto.setSvcName(baseServiceDto.getSvcName());
                appLicBundleDto.setSvcCode(baseServiceDto.getSvcCode());
                appLicBundleDto.setSvcId(baseServiceDto.getId());
                appLicBundleDto.setLicenceId(checkData.getLicenceId());
                appLicBundleDto.setLicenceNo(checkData.getLicenceNo());
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
                licenceId=checkData.getLicenceId();
                AppAlignLicQueryDto autoBundleDto= (AppAlignLicQueryDto) ParamUtil.getSessionAttr(bpc.request, AUTO_BUNDLE);
                if (autoBundleDto!=null){
                    HcsaServiceDto autoServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(autoBundleDto.getSvcName());
                    AppLicBundleDto autoDto=new AppLicBundleDto();
                    autoDto.setSvcName(autoServiceDto.getSvcName());
                    autoDto.setSvcCode(autoServiceDto.getSvcCode());
                    autoDto.setSvcId(autoServiceDto.getId());
                    autoDto.setLicenceId(autoBundleDto.getLicenceId());
                    autoDto.setPremisesId(autoBundleDto.getPremisesId());
                    autoDto.setPremisesType(autoBundleDto.getPremisesType());
                    autoDto.setBoundCode(appLicBundleDtoList.get(0).getBoundCode());
                    autoDto.setLicOrApp(true);
                    appLicBundleDtoList.add(autoDto);
                }
            }
            appSelectSvcDto.setInitPagHandler(false);
        }else if(!noExistBaseApp){
            PaginationHandler<AppAlignAppQueryDto> paginationHandler = (PaginationHandler<AppAlignAppQueryDto>) ParamUtil.getSessionAttr(bpc.request,APPPAGDIV_SESSION_ATTR);
            AppAlignAppQueryDto checkData = new AppAlignAppQueryDto();
            if (paginationHandler!=null){
                paginationHandler.keepCurrentPageChecked();
                List<AppAlignAppQueryDto> allCheckedData = paginationHandler.getAllCheckedData();
                if(allCheckedData != null && !allCheckedData.isEmpty()){
                    checkData = allCheckedData.get(0);
                }
                if (checkData.getSvcName()==null){
                    erroMsg=MessageUtil.getMessageDesc(IaisEGPConstant.ERR_MANDATORY);
                    ParamUtil.setRequestAttr(bpc.request,CHOOSE_BASE_ERR,erroMsg);
                }
            }
            if (!FIRST.equals(checkData.getSvcName())&&checkData.getSvcName()!=null){
                HcsaServiceDto baseServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(checkData.getSvcName());
                AppLicBundleDto appLicBundleDto=new AppLicBundleDto();
                appLicBundleDto.setSvcId(baseServiceDto.getId());
                appLicBundleDto.setSvcName(baseServiceDto.getId());
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
                applicationNo=checkData.getApplicationNo();
                AppAlignAppQueryDto autoBundleDto= (AppAlignAppQueryDto) ParamUtil.getSessionAttr(bpc.request, AUTO_BUNDLE);
                if (autoBundleDto!=null){
                    HcsaServiceDto autoServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(autoBundleDto.getSvcName());
                    AppLicBundleDto autoDto=new AppLicBundleDto();
                    autoDto.setSvcName(autoServiceDto.getSvcName());
                    autoDto.setSvcCode(autoServiceDto.getSvcCode());
                    autoDto.setSvcId(autoServiceDto.getId());
                    autoDto.setLicenceId(autoBundleDto.getApplicationNo());
                    autoDto.setPremisesId(autoBundleDto.getPremisesId());
                    autoDto.setPremisesType(autoBundleDto.getPremisesType());
                    autoDto.setBoundCode(appLicBundleDtoList.get(0).getBoundCode());
                    autoDto.setLicOrApp(true);
                    appLicBundleDtoList.add(autoDto);
                }
            }
            appSelectSvcDto.setInitPagHandler(false);
        }
        if (IaisCommonUtils.isNotEmpty(baseSvcDtoList)){
            for (HcsaServiceDto hcsaServiceDto : baseSvcDtoList) {
                AppSvcRelatedInfoDto appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
                appSvcRelatedInfoDto.setServiceId(hcsaServiceDto.getId());
                appSvcRelatedInfoDto.setServiceName(hcsaServiceDto.getSvcName());
                appSvcRelatedInfoDto.setServiceCode(hcsaServiceDto.getSvcCode());
                appSvcRelatedInfoDto.setServiceType(hcsaServiceDto.getSvcType());
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
        if(StringUtil.isEmpty(erroMsg)&&bundleAchOrMs){
            if (!noExistBaseLic&&StringUtil.isNotEmpty(licenceId)){
                List<AppLicBundleDto> licBundleDtos = assessmentGuideService.getBundleList(licenceId, true);
                if (IaisCommonUtils.isNotEmpty(licBundleDtos)){
                    erroMsg=MessageUtil.getMessageDesc(IaisEGPConstant.ERR_BINGING_MAX);
                }
            } else if (!noExistBaseApp&&StringUtil.isNotEmpty(applicationNo)) {
                List<AppLicBundleDto> licBundleDtos = assessmentGuideService.getBundleList(applicationNo, false);
                if (IaisCommonUtils.isNotEmpty(licBundleDtos)){
                    erroMsg=MessageUtil.getMessageDesc(IaisEGPConstant.ERR_BINGING_MAX);
                }
            }
        }
        String licenseeId = "";
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
        if(loginContext!=null){
            licenseeId  = loginContext.getLicenseeId();
        }
        if(StringUtil.isEmpty(erroMsg)){
            List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                svcCodeList.add(appSvcRelatedInfoDto.getServiceCode());
            }
            List<ApplicationSubDraftDto> applicationSubDraftDtos = assessmentGuideService.getDraftListBySvcCodeAndStatus(svcCodeList,ApplicationConsts.DRAFT_STATUS_PENDING_PAYMENT,licenseeId,ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
            if(!IaisCommonUtils.isEmpty(applicationSubDraftDtos)){
                erroMsg = MessageUtil.getMessageDesc("NEW_ERR0023");
            }
        }
        if(StringUtil.isEmpty(erroMsg)){
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
            checkAction(CHOOSE_BASE_SVC, bpc);
        }else{
            ParamUtil.setRequestAttr(bpc.request,CHOOSE_BASE_ERR,erroMsg);
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
            log.info(StringUtil.changeForLog("item"+item));
            i++;
        }
        String inSql = sb.substring(0, sb.length() - 1) + ")";
        searchParamGroup.addParam(SERNAME, inSql);
        i = 0;
        for (String item: baselist) {
            searchParamGroup.addFilter(ITEMKEY + i,
                    item);
            i ++;
        }
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        searchParamGroup.addFilter(LICENSEEID,loginContext.getLicenseeId(),true);
        QueryHelp.setMainSql(INTER_INBOX_QUERY, "getLicenceBySerName",searchParamGroup);
        return  assessmentGuideService.getMenuLicence(searchParamGroup);
    }

    private List<String> svcIdListTransferNameList(List<String> baseSvcIdList){
        List<String> svcNameList = IaisCommonUtils.genNewArrayList();
        for(String id:baseSvcIdList){
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(id);
            svcNameList.add(hcsaServiceDto.getSvcName());
        }
        return svcNameList;
    }

    public static AppAlignLicQueryDto getAppAlignLicQueryDto(Map<String,List<AppAlignLicQueryDto>> baseLicMap,String svcName,String hciCode){
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
                    if(serviceCode!=null && !serviceCodeList.contains(serviceCode)){
                        serviceCodeList.add(serviceCode);
                    }
                }
            }
            serviceCodeList.sort(String::compareTo);
            Map<String, Object> map = new HashMap<>();
            map.put("serviceCodesList", serviceCodeList);
            map.put("appType", ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
            map.put(LICENSEEID,licenseeId);
            String entity = assessmentGuideService.selectDarft(map);
            String newAck001 = MessageUtil.getMessageDesc("NEW_ACK001");
            bpc.request.setAttribute("new_ack001",newAck001);
            bpc.request.setAttribute(SELECT_DRAFT_NO, entity);
            bpc.request.getSession().setAttribute(SELECT_DRAFT_NO, entity);
        }
    }

    public List<HcsaServiceDto> getBaseBySpc(List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList,String spcSvcId){
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

    public static String stringTransfer(String str){
        return StringUtil.isEmpty(str)?"":str;
    }

    public void preChooseBaseSvc(BaseProcessClass bpc) throws Exception {
        log.info(StringUtil.changeForLog("prepare choose base svc start ..."));
        AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
        boolean noExistBaseLic = (boolean) ParamUtil.getSessionAttr(bpc.request, NO_EXIST_BASE_LIC);
        boolean noExistBaseApp = (boolean) ParamUtil.getSessionAttr(bpc.request, NO_EXIST_BASE_APP);
        boolean bundleAchOrMs = (boolean) ParamUtil.getSessionAttr(bpc.request, BUNDLE_ACH_OR_MS);
        if (!noExistBaseLic){
            PaginationHandler<AppAlignLicQueryDto> paginationHandler = (PaginationHandler<AppAlignLicQueryDto>) ParamUtil.getSessionAttr(bpc.request,LICPAGEDIV_SESSION_ATTR);
            if (paginationHandler!=null){
                if(appSelectSvcDto.isInitPagHandler()){
                    List<AppAlignLicQueryDto> appAlignLicQueryDtos = IaisCommonUtils.genNewArrayList();
                    appAlignLicQueryDtos.add(paginationHandler.getDisplayData().get(0).getRecord());
                    paginationHandler.setDefaultChecked(appAlignLicQueryDtos);
                }
                paginationHandler.preLoadingPage();
            }else {
                ParamUtil.setRequestAttr(bpc.request, "notShow",AppConsts.TRUE);
            }
        }else if (!noExistBaseApp){
            PaginationHandler<AppAlignAppQueryDto> paginationHandler = (PaginationHandler<AppAlignAppQueryDto>) ParamUtil.getSessionAttr(bpc.request,APPPAGDIV_SESSION_ATTR);
            if (paginationHandler!=null){
                if(appSelectSvcDto.isInitPagHandler()){
                    List<AppAlignAppQueryDto> appAlignAppQueryDtoList = IaisCommonUtils.genNewArrayList();
                    appAlignAppQueryDtoList.add(paginationHandler.getDisplayData().get(0).getRecord());
                    paginationHandler.setDefaultChecked(appAlignAppQueryDtoList);
                }
                paginationHandler.preLoadingPage();
            }else {
                ParamUtil.setRequestAttr(bpc.request, "notShow",AppConsts.TRUE);
            }
        }
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = "";
        if(loginContext!=null){
            licenseeId  = loginContext.getLicenseeId();
        }
        List<HcsaServiceDto> baseSvcDtoList = appSelectSvcDto.getBaseSvcDtoList();
        List<String> serviceNameList = baseSvcDtoList.stream().filter(item -> AppServicesConsts.SERVICE_CODE_MEDICAL_SERVICE.equals(item.getSvcCode()) || AppServicesConsts.SERVICE_CODE_DENTAL_SERVICE.equals(item.getSvcCode()))
                .map(HcsaServiceDto::getSvcName).collect(Collectors.toList());
        List<ApplicationDto> applicationDtoList = assessmentGuideService.getApplicationsByLicenseeId(licenseeId);
        boolean existPendMS=false;
        if (IaisCommonUtils.isNotEmpty(applicationDtoList)){
            boolean exist = applicationDtoList.stream().anyMatch(item -> {
                HcsaServiceDto serviceDto = HcsaServiceCacheHelper.getServiceById(item.getServiceId());
                return serviceNameList.contains(serviceDto.getSvcName());
            });
            if (exist&&!bundleAchOrMs){
                existPendMS=true;
            }
        }
        List<LicenceDto> licenceDtoList = licenceInboxClient.getApproveLicenceDtoByLicenseeId(licenseeId).getEntity();
        if (IaisCommonUtils.isNotEmpty(licenceDtoList)){
            boolean exist = licenceDtoList.stream().anyMatch(item -> serviceNameList.contains(item.getSvcName()));
            if (exist&&!bundleAchOrMs){
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
                .sorted(Comparator.comparing(HcsaServiceDto::getSvcName))
                .collect(Collectors.toList());
        /*List<HcsaServiceDto> allspecifiedService = hcsaServiceDtoList.stream()
                .filter(hcsaServiceDto -> SPECIFIED_SERVICE.equals(hcsaServiceDto.getSvcType()))
                .filter(hcsaServiceDto -> accessSvcCodes.contains(hcsaServiceDto.getSvcCode()))
                .collect(Collectors.toList());*/
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
                .filter(elementList -> IaisCommonUtils.isNotEmpty(elementList))
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
        String currentPage = CHOOSE_SERVICE;
        String[] basechks = ParamUtil.getStrings(bpc.request, BASE_SERVICE_CHECK_BOX_ATTR);
        String err;
        String nextstep;
        List<String> basecheckedlist = IaisCommonUtils.genNewArrayList();
        List<HcsaServiceDto> baseSvcSort = IaisCommonUtils.genNewArrayList();
        List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList =  assessmentGuideService.getActiveSvcCorrelation();
        log.info(StringUtil.changeForLog("hcsaServiceCorrelationDtoList size:"+hcsaServiceCorrelationDtoList.size()));
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = "";
        if(loginContext!=null){
            licenseeId  = loginContext.getLicenseeId();
        }
        if(IaisCommonUtils.isEmpty(basechks)){
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
            }else if (baseSvcNames.contains(AppServicesConsts.SERVICE_CODE_MEDICAL_SERVICE)||baseSvcNames.contains(AppServicesConsts.SERVICE_CODE_DENTAL_SERVICE)){
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
            boolean newLicensee;
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
                appSelectSvcDto.setInitPagHandler(true);
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
                    chkBase.addAll(Arrays.asList(basechks));
                    allBaseId.removeAll(chkBase);
                    //get prem type intersection
                    Set<String> premisesTypeList = assessmentGuideService.getAppGrpPremisesTypeBySvcId(chkBase);
                    int alignMinExpiryMonth = systemParamConfig.getAlignMinExpiryMonth();
                    log.debug(StringUtil.changeForLog("alignMinExpiryMonth:-->"+alignMinExpiryMonth));
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
        ParamUtil.setSessionAttr(bpc.request,BUNDLE_ACH_OR_MS, bundleAchOrMs);
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
        String crudActionValue = request.getParameter(IaisEGPConstant.CRUD_ACTION_VALUE);
        String attribute = (String) request.getAttribute(SELECT_DRAFT_NO);
        if (CONTINUE.equals(crudActionValue)) {
            if (!StringUtil.isEmpty(type)) {
                String draftNo  = request.getParameter(DRAFT_NO);
                List<String> list=new ArrayList<>(1);
                list.add(draftNo);
                log.info(StringUtil.changeForLog("delete draft start ..."));
                appInboxClient.deleteDraftNUmber(list);
            }
            ParamUtil.setSessionAttr(request, DRAFT_NUMBER, null);
        } else if (RESUME.equals(crudActionValue)) {
            ParamUtil.setSessionAttr(request, DRAFT_NUMBER, attribute);
        } else if (attribute != null) {
            //back to curr page
            if (StringUtil.isEmpty(type) || CHOOSE_SERVICE.equals(type)) {
                ParamUtil.setRequestAttr(request, VALIDATION_ATTR, ERROR_ATTR);
            }
            if (!StringUtil.isEmpty(type)) {
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, type);
                ParamUtil.setRequestAttr(request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,DOBACK);
            }
        }
    }

    private void initChooseBaseSvc(BaseProcessClass bpc, boolean bundleAchOrMs, List<HcsaServiceDto> allbaseService, List<HcsaServiceDto> baseSvcSort, String licenseeId) {
        boolean noExistBaseLic = true;
        boolean noExistBaseApp = true;
        cleanChooseBaseSvcSession(bpc);
        List<HcsaServiceDto> needContainedSvc =IaisCommonUtils.genNewArrayList();
        Optional<HcsaServiceDto> clbService = allbaseService.stream().filter(s -> AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY.equals(s.getSvcCode())).findAny();
        clbService.ifPresent(needContainedSvc::add);
        Optional<HcsaServiceDto> rdsService = allbaseService.stream().filter(s -> AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES.equals(s.getSvcCode())).findAny();
        if (rdsService.isPresent()){
            needContainedSvc.add(rdsService.get());
        }
        List<String> svcCodes = baseSvcSort.stream().map(HcsaServiceDto::getSvcCode).collect(Collectors.toList());
        List<HcsaServiceDto> notContainedSvc=IaisCommonUtils.genNewArrayList();
        if (bundleAchOrMs){
            notContainedSvc=needContainedSvc.stream().filter(s->!svcCodes.contains(s.getSvcCode())).collect(Collectors.toList());
        }else {
            List<HcsaServiceDto> serviceDtos = baseSvcSort.stream().filter(item -> AppServicesConsts.SERVICE_CODE_MEDICAL_SERVICE.equals(item.getSvcCode()) || AppServicesConsts.SERVICE_CODE_DENTAL_SERVICE.equals(item.getSvcCode()))
                    .collect(Collectors.toList());
            if (IaisCommonUtils.isNotEmpty(serviceDtos)){
                notContainedSvc.addAll(serviceDtos);
            }
        }
        List<String> svcNameList = IaisCommonUtils.genNewArrayList();
        List<String> svcIdList = IaisCommonUtils.genNewArrayList();
        for(HcsaServiceDto hcsaServiceDto:notContainedSvc){
            svcNameList.add(hcsaServiceDto.getSvcName());
            svcIdList.add(hcsaServiceDto.getId());
            ParamUtil.setSessionAttr(bpc.request,NOT_CONTAINED+hcsaServiceDto.getSvcCode(), AppConsts.YES);
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
        List<HcsaServiceDto> hcsaServiceByNames = hcsaConfigClient.getHcsaServiceByNames(svcNameList).getEntity();
        List<String> collect = hcsaServiceByNames.stream().map(HcsaServiceDto::getId).collect(Collectors.toList());
        List<AppAlignAppQueryDto> bundleApp = appInboxClient.getActiveApplicationsAddress(licenseeId, collect).getEntity();
        for (int i = 0; i < bundleApp.size(); i++) {
            AppAlignAppQueryDto appAlignAppQueryDto = bundleApp.get(i);
            appAlignAppQueryDto.setSvcName(HcsaServiceCacheHelper.getServiceById(appAlignAppQueryDto.getSvcId()).getSvcName());
        }
        //pagination
        if(IaisCommonUtils.isNotEmpty(bundleLic)&&bundleLic.size()>1){
            noExistBaseLic=false;
            if (!bundleAchOrMs){
                bundleLic.remove(0);
            }
            if (bundleAchOrMs || !bundleAchOrMs){
                initBundlePaginationHandler(bundleLic);
            }
        }else if (IaisCommonUtils.isNotEmpty(bundleApp)){
            noExistBaseApp=false;
            AppAlignAppQueryDto appAlignAppQueryDto=new AppAlignAppQueryDto();
            appAlignAppQueryDto.setSvcName(FIRST);
            if (bundleAchOrMs){
                bundleApp.add(0,appAlignAppQueryDto);
            }
            if (bundleAchOrMs&&bundleApp.size()>1){
                initBundleAppPaginationHandler(bundleApp);
            }else if (!bundleAchOrMs&&bundleApp.size()>0){
                initBundleAppPaginationHandler(bundleApp);
            }
        }
        if((!bundleAchOrMs && noExistBaseLic) ){
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
        }
        ParamUtil.setSessionAttr(bpc.request,NO_EXIST_BASE_LIC, noExistBaseLic);
        ParamUtil.setSessionAttr(bpc.request, NO_EXIST_BASE_APP, noExistBaseApp);
        ParamUtil.setSessionAttr(bpc.request, "bundleLic", (Serializable) bundleLic);
        ParamUtil.setSessionAttr(bpc.request, "bundleApp", (Serializable) bundleApp);
        ParamUtil.setSessionAttr(bpc.request, "notContainedSvc", (Serializable) notContainedSvc);
        ParamUtil.setSessionAttr(bpc.request, "notContainedSvcSize",notContainedSvc.size());
    }

    private void cleanChooseBaseSvcSession(BaseProcessClass bpc){
        ParamUtil.clearSession(bpc.request,AUTO_BUNDLE);
        ParamUtil.clearSession(bpc.request,NOT_CONTAINED+AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY,NOT_CONTAINED+AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES);
        ParamUtil.clearSession(bpc.request,LICPAGEDIV_SESSION_ATTR,APPPAGDIV_SESSION_ATTR);
    }

    private PaginationHandler<AppAlignLicQueryDto> initBundlePaginationHandler(List<AppAlignLicQueryDto> newAppLicDtos){
        PaginationHandler<AppAlignLicQueryDto> paginationHandler = new PaginationHandler<>("licPagDiv","licBodyDiv");
        paginationHandler.setAllData(newAppLicDtos);
        paginationHandler.setCheckType(PaginationHandler.CHECK_TYPE_RADIO);
        paginationHandler.setPageSize(10);
        paginationHandler.preLoadingPage();
        return paginationHandler;
    }

    private PaginationHandler<AppAlignAppQueryDto> initBundleAppPaginationHandler(List<AppAlignAppQueryDto> newAppLicDtos){
        PaginationHandler<AppAlignAppQueryDto> paginationHandler = new PaginationHandler<>("appPagDiv","appBodyDiv");
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
            searchParam.addParam(SERNAME, inSql);
            i = 0;
            for(String baseSvcId:excludeChkBase){
                searchParam.addFilter(ITEMKEY + i,
                        HcsaServiceCacheHelper.getServiceById(baseSvcId).getSvcName());
                i ++;
            }
        }else{
            String serName = "('')";
            searchParam.addParam(SERNAME, serName);
        }
        //add premType filter
        if(!IaisCommonUtils.isEmpty(premisesTypeList)){
            int i = 0;
            StringBuilder premTypeItem = new StringBuilder("(");
            for(String premisesType:premisesTypeList){
                premTypeItem.append(":premType").append(i).append(',');
                log.info(StringUtil.changeForLog("premisesType-->"+premisesType));
                i++;
            }
            String premTypeItemStr = premTypeItem.substring(0, premTypeItem.length() - 1) + ")";
            searchParam.addParam(PREM_TYPE_LIST, premTypeItemStr);
            i = 0;
            for(String premisesType:premisesTypeList){
                searchParam.addFilter("premType" + i, premisesType);
                i ++;
            }
        }else{
            String premType = "('')";
            searchParam.addParam(PREM_TYPE_LIST, premType);
            log.debug(StringUtil.changeForLog("No intersection data ..."));
        }
        log.debug(StringUtil.changeForLog("alignMinExpiryMonth :" + alignMinExpiryMonth));
        searchParam.addFilter("alignMinExpiryMonth", alignMinExpiryMonth,true);

        searchParam.addFilter(LICENSEEID,licenseeId,true);
        QueryHelp.setMainSql(INTER_INBOX_QUERY, "getLicenceBySvcName",searchParam);
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

    public Map<String,List<String>> baseRelSpe(List<String> baseSvcIdList,List<String> specSvcIdList,List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList){
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
        String action = DOBACK;
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
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = (List<AppSvcRelatedInfoDto>) ParamUtil.getSessionAttr(bpc.request,APP_SVC_RELATED_INFO_LIST);
            if(IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
                appSvcRelatedInfoDtos = IaisCommonUtils.genNewArrayList();
            }
            //add other service
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
            String url= "https://" + bpc.request.getServerName() +
                    "/main-web/eservice/INTERNET/MohLicenseeCompanyDetail";
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
        ParamUtil.setRequestAttr(bpc.request,VALIDATION_ATTR,action);
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

    public static SearchParam initSearParam(){
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
        if(IaisCommonUtils.isNotEmpty(menuLicenceDtos)){
            menuLicenceDto = menuLicenceDtos.get(0);
        }
        String alignLicenceNo = menuLicenceDto.getLicenceNo();
        String licenceId=menuLicenceDto.getLicenceId();
        String licPremiseId = menuLicenceDto.getPremisesId();
        if(FIRST.equals(menuLicenceDto.getSvcName())){
            alignLicenceNo = "";
            licenceId = "";
            licPremiseId = "";
        }
        appSelectSvcDto.setInitPagHandler(false);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = IaisCommonUtils.genNewArrayList();
        List<AppLicBundleDto> appLicBundleDtoList=IaisCommonUtils.genNewArrayList();
        if(appSelectSvcDto.isChooseBaseSvc()){
            appSvcRelatedInfoDtos = (List<AppSvcRelatedInfoDto>) ParamUtil.getSessionAttr(bpc.request,APP_SVC_RELATED_INFO_LIST);
            if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                    appSvcRelatedInfoDto.setAlignLicenceNo(alignLicenceNo);
                    appSvcRelatedInfoDto.setLicPremisesId(licPremiseId);
                }
            }

        }else{
            List<HcsaServiceDto> baseSvcDtoList = appSelectSvcDto.getBaseSvcDtoList();
            HcsaServiceDto baseServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(menuLicenceDto.getSvcName());
            for(HcsaServiceDto hcsaServiceDto:baseSvcDtoList){
                AppSvcRelatedInfoDto appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
                appSvcRelatedInfoDto.setServiceId(hcsaServiceDto.getId());
                appSvcRelatedInfoDto.setServiceCode(hcsaServiceDto.getSvcCode());
                appSvcRelatedInfoDto.setServiceName(hcsaServiceDto.getSvcName());
                appSvcRelatedInfoDto.setAlignLicenceNo(alignLicenceNo);
                appSvcRelatedInfoDto.setLicPremisesId(licPremiseId);
                appSvcRelatedInfoDtos.add(appSvcRelatedInfoDto);
                if (baseServiceDto!=null
                        && (AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(hcsaServiceDto.getSvcCode())
                        || AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(hcsaServiceDto.getSvcCode()))) {
                    AppLicBundleDto appLicBundleDto = new AppLicBundleDto();
                    appLicBundleDto.setLicenceId(licenceId);
                    appLicBundleDto.setLicenceNo(menuLicenceDto.getLicenceNo());
                    appLicBundleDto.setPremisesId(licPremiseId);
                    appLicBundleDto.setSvcCode(baseServiceDto.getSvcCode());
                    appLicBundleDto.setSvcId(baseServiceDto.getId());
                    appLicBundleDto.setSvcName(menuLicenceDto.getSvcName());
                    appLicBundleDto.setPremisesType(menuLicenceDto.getPremisesType());
                    appLicBundleDto.setLicOrApp(true);
                    appLicBundleDtoList.add(appLicBundleDto);
                }
            }
            List<String> svcCodeList = baseSvcDtoList.stream().map(HcsaServiceDto::getSvcCode).collect(Collectors.toList());
            if (baseServiceDto!=null && (svcCodeList.contains(AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY)
                    ||svcCodeList.contains(AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES))
                    && AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL.equals(baseServiceDto.getSvcCode())){
                AppLicBundleDto appLicBundleDto = new AppLicBundleDto();
                appLicBundleDto.setLicenceId(licenceId);
                appLicBundleDto.setLicenceNo(menuLicenceDto.getLicenceNo());
                appLicBundleDto.setPremisesId(licPremiseId);
                appLicBundleDto.setSvcCode(baseServiceDto.getSvcCode());
                appLicBundleDto.setSvcId(baseServiceDto.getId());
                appLicBundleDto.setSvcName(menuLicenceDto.getSvcName());
                appLicBundleDto.setPremisesType(menuLicenceDto.getPremisesType());
                appLicBundleDto.setLicOrApp(true);
                appLicBundleDtoList.add(appLicBundleDto);
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
        String nextstep = ParamUtil.getString(bpc.request,CRUD_ACTION_ADDITIONAL);
        if(NEXT.equals(nextstep)){
            String crudActionValue=bpc.request.getParameter(CRUD_ACTION_VALUE);
            String draftNo  =bpc.request.getParameter(DRAFT_NO);
            getDraft(bpc);
            String attribute =(String)bpc.request.getAttribute(SELECT_DRAFT_NO);
            if(CONTINUE.equals(crudActionValue)){
                bpc.request.getSession().setAttribute(SELECT_DRAFT_NO, draftNo);
                bpc.request.getSession().setAttribute(DRAFT_NUMBER, null);
            }else if(RESUME.equals(crudActionValue)){
                bpc.request.getSession().setAttribute(DRAFT_NUMBER, attribute);
            }else if(attribute!=null){
                //back to curr page
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,DOBACK);
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,CHOOSE_LICENCE);
            }
        }
        String switchStep = ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE);
        if(NEXT.equals(switchStep)){
            String crudActionValue=bpc.request.getParameter(CRUD_ACTION_VALUE);
            String draftNo  =bpc.request.getParameter(DRAFT_NO);
            getDraft(bpc);
            String attribute =(String)bpc.request.getAttribute(SELECT_DRAFT_NO);
            if(CONTINUE.equals(crudActionValue)){
                bpc.request.getSession().setAttribute(SELECT_DRAFT_NO, draftNo);
                List<String> list=new ArrayList<>(1);
                list.add(draftNo);
                assessmentGuideService.deleteDraftNUmber(list);
                bpc.request.getSession().setAttribute(DRAFT_NUMBER, null);
            }else if(RESUME.equals(crudActionValue)){
                bpc.request.getSession().setAttribute(DRAFT_NUMBER, attribute);
            }else if(attribute!=null){
                //back to curr page
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,DOBACK);
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
        SearchParam renewLicUpdateSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.RENEW_LICENCE_UPDATE_SEARCH_PARAM,SelfPremisesListQueryDto.class.getName(),PREMISES_TYPE,SearchParam.DESCENDING,false);
        renewLicUpdateSearchParam.addFilter(LICENSEEID, getLicenseeId(bpc.request), true);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = loginContext.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
        HalpSearchResultHelper.setLicParamByField(renewLicUpdateSearchParam,SERVICE_TYPES_SHOW,HcsaServiceCacheHelper.controlServices(2,userRoleAccessMatrixDtos));
        QueryHelp.setMainSql(INTER_INBOX_QUERY, QUERY_PREMISES, renewLicUpdateSearchParam);
        SearchResult<SelfPremisesListQueryDto> renewLicUpdateSearchResult = requestForChangeService.searchPreInfo(renewLicUpdateSearchParam);
        if (!StringUtil.isEmpty(renewLicUpdateSearchResult)) {
            ParamUtil.setSessionAttr(bpc.request, GuideConsts.RENEW_LICENCE_UPDATE_SEARCH_PARAM, renewLicUpdateSearchParam);
            ParamUtil.setRequestAttr(bpc.request, GuideConsts.RENEW_LICENCE_UPDATE_SEARCH_RESULT, renewLicUpdateSearchResult);
        }
        log.info(END);
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
                ParamUtil.setRequestAttr(bpc.request, GUIDE_BACK_ACTION, REDIRECT);
                ParamUtil.setSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_LIC_ID_LIST_ATTR, (Serializable) licenceIds);
            } else {
                if ("renew".equals(actionType)) {
                    ParamUtil.setRequestAttr(bpc.request, GUIDE_BACK_ACTION, "backRenewUpdate");
                } else {
                    ParamUtil.setRequestAttr(bpc.request, GUIDE_BACK_ACTION, "backRenew");
                }
                ParamUtil.setSessionAttr(bpc.request, LICENCE_ERR_LIST, (Serializable) licenceIds);
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
        url.append(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                .append(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.URL_LICENCE_WEB_MODULE + "MohWithOutRenewal");
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
    }

    public void showLicensee(BaseProcessClass bpc) {
        String type = ParamUtil.getRequestString(bpc.request,CRUD_ACTION_ADDITIONAL);
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
        log.info(START);
        String licId = ParamUtil.getString(bpc.request, AMEND_LICENSE_ID);
        String isNeedDelete = bpc.request.getParameter(IS_NEED_DELETE);
        String licIdValue = ParamUtil.getMaskedString(bpc.request, licId);
        if(!StringUtil.isEmpty(licIdValue)){
            List<ApplicationSubDraftDto> draftByLicAppId = inboxService.getDraftByLicAppId(licIdValue);
            if(DELETE.equals(isNeedDelete)){
                for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                    inboxService.deleteDraftByNo(applicationSubDraftDto.getDraftNo());
                }
                StringBuilder url = new StringBuilder();
                url.append(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.URL_HTTPS)
                        .append(bpc.request.getServerName())
                        .append(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.URL_LICENCE_WEB_MODULE+MOH_REQUEST_FOR_CHANGE)
                        .append("?licenceId=")
                        .append(MaskUtil.maskValue(LICENCEID,licIdValue));
                String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
                IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
            }
        }
        SearchParam amendDetailsSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.AMEND_DETAILS_SEARCH_PARAM,SelfPremisesListQueryDto.class.getName(),PREMISES_TYPE,SearchParam.DESCENDING,false);
        amendDetailsSearchParam.addFilter(LICENSEEID, getLicenseeId(bpc.request), true);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = loginContext.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
        HalpSearchResultHelper.setLicParamByField(amendDetailsSearchParam,SERVICE_TYPES_SHOW,HcsaServiceCacheHelper.controlServices(2,userRoleAccessMatrixDtos));
        QueryHelp.setMainSql(INTER_INBOX_QUERY, QUERY_PREMISES, amendDetailsSearchParam);
        SearchResult<SelfPremisesListQueryDto> amendHCISearchResult = requestForChangeService.searchPreInfo(amendDetailsSearchParam);
        if (amendHCISearchResult != null && amendHCISearchResult.getRowCount() > 0) {
            ParamUtil.setSessionAttr(bpc.request, GuideConsts.AMEND_DETAILS_SEARCH_PARAM, amendDetailsSearchParam);
            ParamUtil.setRequestAttr(bpc.request, GuideConsts.AMEND_DETAILS_SEARCH_RESULT, amendHCISearchResult);
            ArrayList<PremisesListQueryDto> newList = IaisCommonUtils.genNewArrayList(amendHCISearchResult.getRowCount());
            for (SelfPremisesListQueryDto se : amendHCISearchResult.getRows()) {
                newList.add(MiscUtil.transferEntityDto(se, PremisesListQueryDto.class));
            }
            ParamUtil.setSessionAttr(bpc.request, FeMainConst.PREMISESLISTDTOS, newList);
        }
        log.info(END);
    }

    public void amendLic1_2(BaseProcessClass bpc) {
        log.info(START);
        SearchParam amendDetailsRemoveSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.AMEND_DETAILS_REMOVE_SEARCH_PARAM,SelfPremisesListQueryDto.class.getName(),PREMISES_TYPE,SearchParam.DESCENDING,false);
        amendDetailsRemoveSearchParam.addFilter(LICENSEEID, getLicenseeId(bpc.request), true);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = loginContext.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
        HalpSearchResultHelper.setLicParamByField(amendDetailsRemoveSearchParam,SERVICE_TYPES_SHOW,HcsaServiceCacheHelper.controlServices(2,userRoleAccessMatrixDtos));
        QueryHelp.setMainSql(INTER_INBOX_QUERY, QUERY_PREMISES, amendDetailsRemoveSearchParam);
        SearchResult<SelfPremisesListQueryDto> amendDetailsRemoveSearchResult = requestForChangeService.searchPreInfo(amendDetailsRemoveSearchParam);
        if (!StringUtil.isEmpty(amendDetailsRemoveSearchResult)) {
            ParamUtil.setSessionAttr(bpc.request, GuideConsts.AMEND_DETAILS_REMOVE_SEARCH_PARAM, amendDetailsRemoveSearchParam);
            ParamUtil.setRequestAttr(bpc.request,GuideConsts.AMEND_DETAILS_REMOVE_SEARCH_RESULT, amendDetailsRemoveSearchResult);
        }
        log.info(END);
    }

    public void amendLic2(BaseProcessClass bpc) {
        log.info(START);
        SearchParam amendHCISearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, AMEND_HCI_SEARCH_PARAM,SelfPremisesListQueryDto.class.getName(),"LICENCE_ID",SearchParam.DESCENDING,false);
        amendHCISearchParam.addFilter(LICENSEEID, getLicenseeId(bpc.request), true);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = loginContext.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
        HalpSearchResultHelper.setLicParamByField(amendHCISearchParam,SERVICE_TYPES_SHOW,HcsaServiceCacheHelper.controlServices(2,userRoleAccessMatrixDtos));
        QueryHelp.setMainSql(INTER_INBOX_QUERY, QUERY_PREMISES, amendHCISearchParam);
        SearchResult<SelfPremisesListQueryDto> amendHCISearchResult = requestForChangeService.searchPreInfo(amendHCISearchParam);
        if (!StringUtil.isEmpty(amendHCISearchResult)) {
            ParamUtil.setSessionAttr(bpc.request, AMEND_HCI_SEARCH_PARAM, amendHCISearchParam);
            ParamUtil.setRequestAttr(bpc.request, "amendHCISearchResult", amendHCISearchResult);
            List<SelfPremisesListQueryDto> selfPremisesListQueryDtoList = amendHCISearchResult.getRows();
            List<PremisesListQueryDto> premisesListQueryDtoList = IaisCommonUtils.genNewArrayList();
            for (SelfPremisesListQueryDto selfPremisesListQueryDto:selfPremisesListQueryDtoList
                 ) {
                PremisesListQueryDto premisesListQueryDto = MiscUtil.transferEntityDto(selfPremisesListQueryDto, PremisesListQueryDto.class);
                premisesListQueryDtoList.add(premisesListQueryDto);
            }

            ParamUtil.setSessionAttr(bpc.request, FeMainConst.PREMISESLISTDTOS, (Serializable) premisesListQueryDtoList);
        }
        log.info(END);
    }

    public void amendLic5(BaseProcessClass bpc) throws IOException {
        log.info(START);
        String licId = ParamUtil.getString(bpc.request, AMEND_LICENSE_ID);
        String isNeedDelete = bpc.request.getParameter(IS_NEED_DELETE);
        String licIdValue = ParamUtil.getMaskedString(bpc.request, licId);
        if(!StringUtil.isEmpty(licIdValue)){
            List<ApplicationSubDraftDto> draftByLicAppId = inboxService.getDraftByLicAppId(licIdValue);
            if(DELETE.equals(isNeedDelete)){
                for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                    inboxService.deleteDraftByNo(applicationSubDraftDto.getDraftNo());
                }
                StringBuilder url = new StringBuilder();
                url.append(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.URL_HTTPS)
                        .append(bpc.request.getServerName())
                        .append(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.URL_LICENCE_WEB_MODULE+MOH_REQUEST_FOR_CHANGE)
                        .append("?licenceId=")
                        .append(MaskUtil.maskValue(LICENCEID,licIdValue));
                String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
                IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
            }
        }
        SearchParam amendUpdateVehiclesSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.AMEND_UPDATE_VEHICLES_SEARCH_PARAM,SelfPremisesListQueryDto.class.getName(),PREMISES_TYPE,SearchParam.DESCENDING,false);
        amendUpdateVehiclesSearchParam.addFilter(LICENSEEID, getLicenseeId(bpc.request), true);
        amendUpdateVehiclesSearchParam.addFilter("premisesType", ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE, true);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = loginContext.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
        HalpSearchResultHelper.setLicParamByField(amendUpdateVehiclesSearchParam,SERVICE_TYPES_SHOW,HcsaServiceCacheHelper.controlServices(2,userRoleAccessMatrixDtos));
        QueryHelp.setMainSql(INTER_INBOX_QUERY, QUERY_PREMISES, amendUpdateVehiclesSearchParam);
        SearchResult<SelfPremisesListQueryDto> amendUpdateVehiclesSearchResult = requestForChangeService.searchPreInfo(amendUpdateVehiclesSearchParam);
        if (amendUpdateVehiclesSearchResult != null && amendUpdateVehiclesSearchResult.getRowCount() > 0) {
            ParamUtil.setSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_VEHICLES_SEARCH_PARAM, amendUpdateVehiclesSearchParam);
            ParamUtil.setRequestAttr(bpc.request, GuideConsts.AMEND_UPDATE_VEHICLES_SEARCH_RESULT, amendUpdateVehiclesSearchResult);
            ArrayList<PremisesListQueryDto> newList = IaisCommonUtils.genNewArrayList(amendUpdateVehiclesSearchResult.getRowCount());
            for (SelfPremisesListQueryDto se : amendUpdateVehiclesSearchResult.getRows()) {
                newList.add(MiscUtil.transferEntityDto(se, PremisesListQueryDto.class));
            }
            ParamUtil.setSessionAttr(bpc.request, FeMainConst.PREMISESLISTDTOS, newList);
        }
        log.info(END);
    }

    public void amendLic3_1(BaseProcessClass bpc) {
        log.info(START);
        SearchParam amendDetailsSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.AMEND_UPDATE_LICENSEE_SEARCH_PARAM,SelfPremisesListQueryDto.class.getName(),PREMISES_TYPE,SearchParam.DESCENDING,false);
        amendDetailsSearchParam.addFilter(LICENSEEID, getLicenseeId(bpc.request), true);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = loginContext.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
        HalpSearchResultHelper.setLicParamByField(amendDetailsSearchParam,SERVICE_TYPES_SHOW,HcsaServiceCacheHelper.controlServices(2,userRoleAccessMatrixDtos));
        QueryHelp.setMainSql(INTER_INBOX_QUERY, QUERY_PREMISES, amendDetailsSearchParam);
        SearchResult<SelfPremisesListQueryDto> amendDetailsSearchResult = requestForChangeService.searchPreInfo(amendDetailsSearchParam);
        if (!StringUtil.isEmpty(amendDetailsSearchResult)) {
            ParamUtil.setSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_LICENSEE_SEARCH_PARAM, amendDetailsSearchParam);
            ParamUtil.setRequestAttr(bpc.request, GuideConsts.AMEND_UPDATE_LICENSEE_SEARCH_RESULT, amendDetailsSearchResult);
        }
        log.info(END);
    }

    public void amendLic3_2(BaseProcessClass bpc) {
        log.info(START);
        LoginContext loginContext= (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        FeUserDto feUserDto=orgUserManageService.getUserAccount(loginContext.getUserId());
        if(feUserDto.getUserId().contains("_")){
            ParamUtil.setRequestAttr(bpc.request, "login_action_type","company");
        }else{
            ParamUtil.setRequestAttr(bpc.request, "login_action_type","solo");
        }
        log.info(END);
    }

    public void amendLic4_1(BaseProcessClass bpc) {
        log.info(START);
        SearchParam amendDetailsSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.AMEND_UPDATE_PERSONNEL_SEARCH_PARAM,SelfPremisesListQueryDto.class.getName(),PREMISES_TYPE,SearchParam.DESCENDING,false);
        amendDetailsSearchParam.addFilter(LICENSEEID, getLicenseeId(bpc.request), true);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = loginContext.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
        HalpSearchResultHelper.setLicParamByField(amendDetailsSearchParam,SERVICE_TYPES_SHOW,HcsaServiceCacheHelper.controlServices(2,userRoleAccessMatrixDtos));
        QueryHelp.setMainSql(INTER_INBOX_QUERY, QUERY_PREMISES, amendDetailsSearchParam);
        SearchResult<SelfPremisesListQueryDto> amendDetailsSearchResult = requestForChangeService.searchPreInfo(amendDetailsSearchParam);
        if (!StringUtil.isEmpty(amendDetailsSearchResult)) {
            ParamUtil.setSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_PERSONNEL_SEARCH_PARAM, amendDetailsSearchParam);
            ParamUtil.setRequestAttr(bpc.request, GuideConsts.AMEND_UPDATE_PERSONNEL_SEARCH_RESULT, amendDetailsSearchResult);
        }
        log.info(END);

    }

    public void amendLic4_2(BaseProcessClass bpc) {
        //amendLic4_1(bpc);
        log.info(START);
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();
        List<PersonnelListQueryDto> persons = requestForChangeService.getLicencePersonnelListQueryDto(getLicenseeId(bpc.request));
        if (!IaisCommonUtils.isEmpty(persons)) {
            List<String> idNos = IaisCommonUtils.genNewArrayList();
            for (PersonnelListQueryDto dto : persons) {
                String personKey = IaisCommonUtils.getPersonKey(dto.getNationality(), dto.getIdType(), dto.getIdNo());
                if (!idNos.contains(personKey)) {
                    idNos.add(personKey);
                    String idNo = dto.getIdNo();
                    String idType = dto.getIdType();
                    String name = dto.getName();
                    SelectOption s = new SelectOption(personKey, name + ", " + idNo + " (" + MasterCodeUtil.getCodeDesc(idType) + ")");
                    selectOptions.add(s);
                }
            }
        }
        SearchParam amendDetailsSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.AMEND_UPDATE_CONTACT_SEARCH_PARAM,PersonnlAssessQueryDto.class.getName(),"T3.ID",SearchParam.DESCENDING,false);
        amendDetailsSearchParam.addFilter("licenseeId", getLicenseeId(bpc.request), true);
        String idNo = ParamUtil.getString(bpc.request,PERSONNELOPTIONS);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = loginContext.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
        HalpSearchResultHelper.setLicParamByField(amendDetailsSearchParam,"serviceTypesShow",HcsaServiceCacheHelper.controlServices(2,userRoleAccessMatrixDtos));
        if (idNo != null && !"Please Select".equals(idNo)){
            String[] data = IaisCommonUtils.getPersonKeys(idNo);
            if (data.length == 3) {
                amendDetailsSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request,
                        GuideConsts.AMEND_UPDATE_CONTACT_SEARCH_PARAM, PersonnlAssessQueryDto.class.getName(), "T3.ID",
                        SearchParam.DESCENDING, false);
                amendDetailsSearchParam.addFilter("licenseeId", getLicenseeId(bpc.request), true);
                amendDetailsSearchParam.addFilter("idNo", data[2], true);
                amendDetailsSearchParam.addFilter("idType", data[1], true);
                amendDetailsSearchParam.addFilter("nationality", StringUtil.getNonNull(data[0]), true);
                QueryHelp.setMainSql("interInboxQuery", "appPersonnelQuery", amendDetailsSearchParam);
                SearchResult<PersonnlAssessQueryDto> amendDetailsSearchResult = requestForChangeService.searchAssessPsnInfo(
                        amendDetailsSearchParam);
                if (!StringUtil.isEmpty(amendDetailsSearchResult)) {
                    ParamUtil.setSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_CONTACT_SEARCH_PARAM, amendDetailsSearchParam);
                    ParamUtil.setRequestAttr(bpc.request, GuideConsts.AMEND_UPDATE_CONTACT_SEARCH_RESULT, amendDetailsSearchResult);
                }
            }
        } else {
            QueryHelp.setMainSql("interInboxQuery", "appPersonnelQuery", amendDetailsSearchParam);
        }
        ParamUtil.setSessionAttr(bpc.request, PERSONNELOPTIONS, (Serializable) selectOptions);
        log.info(END);
    }

    public void searchByIdNo(BaseProcessClass bpc) {
        String idNo = ParamUtil.getString(bpc.request,PERSONNELOPTIONS);
        if (idNo != null && !"Please Select".equals(idNo)){
            String[] data = IaisCommonUtils.getPersonKeys(idNo);
            if (data.length == 3){
                SearchParam amendDetailsSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.AMEND_UPDATE_CONTACT_SEARCH_PARAM,PersonnlAssessQueryDto.class.getName(),"T3.ID",SearchParam.DESCENDING,false);
                amendDetailsSearchParam.addFilter("licenseeId", getLicenseeId(bpc.request), true);
                amendDetailsSearchParam.addFilter("idNo", data[2], true);
                amendDetailsSearchParam.addFilter("idType", data[1], true);
                amendDetailsSearchParam.addFilter("nationality", StringUtil.getNonNull(data[0]), true);
                LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
                List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = loginContext.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
                HalpSearchResultHelper.setLicParamByField(amendDetailsSearchParam,SERVICE_TYPES_SHOW,HcsaServiceCacheHelper.controlServices(2,userRoleAccessMatrixDtos));
                QueryHelp.setMainSql(INTER_INBOX_QUERY, "appPersonnelQuery", amendDetailsSearchParam);
                SearchResult<PersonnlAssessQueryDto> amendDetailsSearchResult = requestForChangeService.searchAssessPsnInfo(amendDetailsSearchParam);
                if (!StringUtil.isEmpty(amendDetailsSearchResult)) {
                    ParamUtil.setSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_CONTACT_SEARCH_PARAM, amendDetailsSearchParam);
                    ParamUtil.setRequestAttr(bpc.request, GuideConsts.AMEND_UPDATE_CONTACT_SEARCH_RESULT, amendDetailsSearchResult);
                }
            }
        }
    }

    public void prepareSwitch(BaseProcessClass bpc) throws IOException {
        log.info(START);
        ParamUtil.setSessionAttr(bpc.request,LICENCE_ERR_LIST,null);
    }

    public void ceaseLic(BaseProcessClass bpc) {
        log.info(START);
        SearchParam ceaseLicenceParam = HalpSearchResultHelper.gainSearchParam(bpc.request,GuideConsts.CEASE_LICENCE_SEARCH_PARAM,SelfPremisesListQueryDto.class.getName(),PREMISES_TYPE,SearchParam.DESCENDING,false);
        ceaseLicenceParam.addFilter(LICENSEEID, getLicenseeId(bpc.request), true);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = loginContext.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
        HalpSearchResultHelper.setLicParamByField(ceaseLicenceParam,SERVICE_TYPES_SHOW,HcsaServiceCacheHelper.controlServices(2,userRoleAccessMatrixDtos));
        QueryHelp.setMainSql(INTER_INBOX_QUERY, QUERY_PREMISES, ceaseLicenceParam);
        SearchResult<SelfPremisesListQueryDto> ceaseLicenceResult = requestForChangeService.searchPreInfo(ceaseLicenceParam);
        if (!StringUtil.isEmpty(ceaseLicenceResult)) {
            ParamUtil.setSessionAttr(bpc.request, GuideConsts.CEASE_LICENCE_SEARCH_PARAM, ceaseLicenceParam);
            ParamUtil.setRequestAttr(bpc.request, GuideConsts.CEASE_LICENCE_SEARCH_RESULT, ceaseLicenceResult);
        }
        log.info(END);
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
        String cessationError;
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
        String inboxAck011 = MessageUtil.getMessageDesc("INBOX_ACK011");
        for(String licId : licIdValue){
            LicenceDto licenceDto = licenceInboxClient.getLicDtoById(licId).getEntity();
            if(licenceDto==null){
                ParamUtil.setRequestAttr(bpc.request, com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.LIC_CEASED_ERR_RESULT,Boolean.TRUE);
                bpc.request.setAttribute(CESSATION_ERROR,inboxAck011);
                ParamUtil.setSessionAttr(bpc.request,LICENCE_ERR_LIST,(Serializable) licIdValue);
                return ;
            }else {
                if( !ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(licenceDto.getStatus())){
                    if(!(IaisEGPHelper.isActiveMigrated() &&ApplicationConsts.LICENCE_STATUS_APPROVED.equals(licenceDto.getStatus())&&licenceDto.getMigrated()!=0)){
                        ParamUtil.setRequestAttr(bpc.request, com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.LIC_CEASED_ERR_RESULT,Boolean.TRUE);
                        bpc.request.setAttribute(CESSATION_ERROR,inboxAck011);
                        ParamUtil.setSessionAttr(bpc.request,LICENCE_ERR_LIST,(Serializable) licIdValue);
                        return ;
                    }
                }
            }
        }
        Map<String, Boolean> resultMap = inboxService.listResultCeased(licIdValue);
        for (Map.Entry<String, Boolean> entry : resultMap.entrySet()) {
            if (Boolean.TRUE.equals(!entry.getValue())) {
                result = true;
                break;
            }
        }
        List<ApplicationSubDraftDto> draftByLicAppId = inboxService.getDraftByLicAppId(licIdValue.get(0));
        String isNeedDelete = bpc.request.getParameter(IS_NEED_DELETE);
        if(!draftByLicAppId.isEmpty()){
            StringBuilder stringBuilder=new StringBuilder();
            for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                stringBuilder.append(applicationSubDraftDto.getDraftNo()).append(' ');
            }
            if(DELETE.equals(isNeedDelete)){
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
                ParamUtil.setSessionAttr(bpc.request,LICENCE_ERR_LIST,(Serializable) licPremIdValue);
                return;
            }
        }
        if (result) {
            ParamUtil.setRequestAttr(bpc.request, com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.LIC_CEASED_ERR_RESULT, Boolean.TRUE);
            cessationError = MessageUtil.getMessageDesc("CESS_ERR002");
            bpc.request.setAttribute(CESSATION_ERROR,cessationError);
            ParamUtil.setSessionAttr(bpc.request,LICENCE_ERR_LIST,(Serializable) licPremIdValue);
        } else {
            ParamUtil.setSessionAttr(bpc.request, "licIds", (Serializable) licIdValue);
            StringBuilder url = new StringBuilder();
            url.append(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.URL_LICENCE_WEB_MODULE + "MohCessationApplication");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }
    }

    public void withdrawApp(BaseProcessClass bpc) {
        SearchParam withdrawAppParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.WITHDRAW_APPLICATION_SEARCH_PARAM,InboxAppQueryDto.class.getName(),"CREATED_DT",SearchParam.DESCENDING,false);
        withdrawAppParam.addFilter(LICENSEEID, getLicenseeId(bpc.request), true);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = loginContext.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
        HalpSearchResultHelper.setParamByFieldOrSearch(withdrawAppParam,"appServicesShow",HcsaServiceCacheHelper.controlServices(3,userRoleAccessMatrixDtos),"code");
        QueryHelp.setMainSql(INTER_INBOX_QUERY, "assessmentWithdrawAppQuery", withdrawAppParam);
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
        if (Boolean.TRUE.equals(!appInboxClient.isApplicationWithdrawal(appId).getEntity())) {
            String withdrawalError = MessageUtil.getMessageDesc("WDL_EER001");
            ParamUtil.setRequestAttr(bpc.request,"licIsWithdrawal",Boolean.TRUE);
            bpc.request.setAttribute(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.LIC_ACTION_ERR_MSG,withdrawalError);
        } else {
            StringBuilder url = new StringBuilder();
            url.append(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.URL_LICENCE_WEB_MODULE + "MohWithdrawalApplication")
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
        draftAppSearchParam.addFilter(LICENSEEID, licenseeId, true);
        List<String> inParams = IaisCommonUtils.genNewArrayList();
        inParams.add(ApplicationConsts.APPLICATION_STATUS_DRAFT);
        SqlHelper.builderInSql(draftAppSearchParam, "B.status", "appStatus", inParams);
        List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = loginContext.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
        HalpSearchResultHelper.setAppParamByField(draftAppSearchParam,"appServicesShow",HcsaServiceCacheHelper.controlServices(3,userRoleAccessMatrixDtos));
        QueryHelp.setMainSql(INTER_INBOX_QUERY, "applicationQuery", draftAppSearchParam);
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
        if(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.APP_DO_DRAFT_TYPE_RFC.equals(appType)){
            StringBuilder url = new StringBuilder();
            url.append(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.URL_LICENCE_WEB_MODULE+"MohRequestForChange/prepareDraft")
                    .append("?DraftNumber=")
                    .append(MaskUtil.maskValue(DRAFT_NUMBER,appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }else if(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.APP_DO_DRAFT_TYPE_RENEW.equals(appType)){
            StringBuilder url = new StringBuilder();
            url.append(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.URL_LICENCE_WEB_MODULE+"MohWithOutRenewal")
                    .append("?DraftNumber=")
                    .append(MaskUtil.maskValue(DRAFT_NUMBER,appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }else if(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.APP_DO_DRAFT_TYPE_APPEAL.equals(appType)){
            StringBuilder url = new StringBuilder();
            url.append(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.URL_LICENCE_WEB_MODULE+"MohAppealApplication")
                    .append("?DraftNumber=")
                    .append(MaskUtil.maskValue(DRAFT_NUMBER,appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }
        else {
            StringBuilder url = new StringBuilder();
            url.append(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.URL_LICENCE_WEB_MODULE+"MohNewApplication")
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
                ParamUtil.setSessionAttr(bpc.request,DS_MODLE_SELECT,"AR");break;
            case DataSubmissionConsts.DS_DRP:
                ParamUtil.setSessionAttr(bpc.request,DS_MODLE_SELECT,"DP");break;
            case DataSubmissionConsts.DS_LDT:
                ParamUtil.setSessionAttr(bpc.request,DS_MODLE_SELECT,"LDT");break;
            case DataSubmissionConsts.DS_TOP:
                ParamUtil.setSessionAttr(bpc.request,DS_MODLE_SELECT,"TP"); break;
            case DataSubmissionConsts.DS_VSS:
                ParamUtil.setSessionAttr(bpc.request,DS_MODLE_SELECT,"VS");break;
            default:
                ParamUtil.setSessionAttr(bpc.request,DS_MODLE_SELECT,"LDT");
        }
        url.append(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.URL_HTTPS)
                .append(bpc.request.getServerName())
                .append(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.URL_LICENCE_WEB_MODULE+"MohDataSubmission/PrepareCompliance").append("?selfAssessmentGuide=true");
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
    }

    public void subDateMoh(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("subDateMoh:"));

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
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, AMEND_HCI_SEARCH_PARAM);
        HalpSearchResultHelper.doPage(bpc.request,searchParam);
    }

    public void updateHCISort(BaseProcessClass bpc) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, AMEND_HCI_SEARCH_PARAM);
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

    public void doAmendLicStep(BaseProcessClass bpc) throws IOException{
        String action = ParamUtil.getString(bpc.request, "guide_action_type");
        String licId = ParamUtil.getString(bpc.request, AMEND_LICENSE_ID);
        String idNoPersonnal = ParamUtil.getString(bpc.request, PERSONNELOPTIONS);
        String licIdValue = ParamUtil.getMaskedString(bpc.request, licId);
        ParamUtil.setSessionAttr(bpc.request,LICENCE_ERR_LIST,licIdValue);
        boolean flag = false;
        if (idNoPersonnal != null){
            /*String id = idNoPersonnal.split(",")[1];
            List<String> idNos = IaisCommonUtils.genNewArrayList();
            idNos.add(id);
            List<PersonnelListDto> personnelListDtoList = requestForChangeService.getPersonnelListAssessment(idNos,getOrgId(bpc.request));
            ParamUtil.setSessionAttr(bpc.request, "personnelListDtos", (Serializable) personnelListDtoList);
            if("amendLic7".equals(action)) {
                StringBuilder url = new StringBuilder();
                url.append(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.URL_HTTPS)
                        .append(bpc.request.getServerName())
                        .append(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.URL_LICENCE_WEB_MODULE + "MohRfcPersonnelList/initPsnEditInfo")
                        .append("?personnelNo=")
                        .append(MaskUtil.maskValue("personnelNo", id));
                ParamUtil.setRequestAttr(bpc.request, "url", url.toString());
                ParamUtil.setRequestAttr(bpc.request, "amend_action_type", "redirect");
            }*/
        }
        if(licIdValue != null){
            Map<String, String> errorMap = inboxService.checkRfcStatus(licIdValue);
            List<ApplicationSubDraftDto> draftByLicAppId = inboxService.getDraftByLicAppId(licIdValue);
            String isNeedDelete = bpc.request.getParameter(IS_NEED_DELETE);
                StringBuilder stringBuilder=new StringBuilder();
            if(!draftByLicAppId.isEmpty()){
                for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                    stringBuilder.append(applicationSubDraftDto.getDraftNo()).append(' ');
                }
                if(DELETE.equals(isNeedDelete)){
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
                 {
                    StringBuilder url = new StringBuilder();
                    url.append(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.URL_HTTPS)
                            .append(bpc.request.getServerName())
                            .append(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.URL_LICENCE_WEB_MODULE+MOH_REQUEST_FOR_CHANGE)
                            .append("?licenceId=")
                            .append(MaskUtil.maskValue(LICENCEID,licIdValue));
                    ParamUtil.setRequestAttr(bpc.request, "url", url.toString());
                    ParamUtil.setRequestAttr(bpc.request, AMEND_ACTION_TYPE, REDIRECT);
                }
            }else{
                if ("amendLic2".equals(action)){
                    ParamUtil.setRequestAttr(bpc.request,AMEND_ACTION_TYPE,"toamend2");
                }else if("amendLic1".equals(action)){
                    ParamUtil.setRequestAttr(bpc.request,AMEND_ACTION_TYPE,"toamend1_2");
                }else if("amendLic4".equals(action)){
                    ParamUtil.setRequestAttr(bpc.request,AMEND_ACTION_TYPE,"toamend1_1");
                }
                else if("amendLic3".equals(action)){
                    ParamUtil.setRequestAttr(bpc.request,AMEND_ACTION_TYPE,"toamend3_1");
                }
                else if("amendLic5".equals(action)){
                    ParamUtil.setRequestAttr(bpc.request,AMEND_ACTION_TYPE,"toamend4_1");
                }
                else if("amendLic6".equals(action)){
                    ParamUtil.setRequestAttr(bpc.request,AMEND_ACTION_TYPE,"toamend3_2");
                }
                else if("amendLic7".equals(action)){
                    ParamUtil.setRequestAttr(bpc.request,AMEND_ACTION_TYPE,"toamend4_2");
                }
                else if("amendLic8".equals(action)){
                    ParamUtil.setRequestAttr(bpc.request,AMEND_ACTION_TYPE,"toamend5");
                }
                if (!flag){
                    ParamUtil.setRequestAttr(bpc.request,"licIsAmend",Boolean.TRUE);
                    ParamUtil.setRequestAttr(bpc.request, com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.LIC_ACTION_ERR_MSG,errorMap.get("errorMessage"));
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
        url.append(com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst.URL_HTTPS).append(request.getServerName())
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
            action = CHOOSE_SERVICE;
        }
        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,action);
        log.info(StringUtil.changeForLog("prepareJump end..."));
    }

    public static List<String> getAppAlignLicQueryHci(Map<String,List<AppAlignLicQueryDto>> baseSvcPremMap,String svcName){
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
                log.info(StringUtil.changeForLog("baseSvcId:"+baseSvcId));
                i++;
            }
            String inSql = placeholder.substring(0, placeholder.length() - 1) + ")";
            searchParam.addParam(SERNAME, inSql);
            i = 0;
            for(String baseSvcId:excludeChkBase){
                searchParam.addFilter(ITEMKEY + i,
                        HcsaServiceCacheHelper.getServiceById(baseSvcId).getSvcName());
                i ++;
            }
        }else{
            String serName = "('')";
            searchParam.addParam(SERNAME, serName);
        }
        //add premType filter
        if(!IaisCommonUtils.isEmpty(premisesTypeList)){
            int i = 0;
            StringBuilder premTypeItem = new StringBuilder("(");
            for(String premisesType:premisesTypeList){
                premTypeItem.append(":premType").append(i).append(',');
                log.info(StringUtil.changeForLog("premisesType:"+premisesType));
                i++;
            }
            String premTypeItemStr = premTypeItem.substring(0, premTypeItem.length() - 1) + ")";
            searchParam.addParam(PREM_TYPE_LIST, premTypeItemStr);
            i = 0;
            for(String premisesType:premisesTypeList){
                searchParam.addFilter("premType" + i, premisesType);
                i ++;
            }
        }else{
            String premType = "('')";
            searchParam.addParam(PREM_TYPE_LIST, premType);
            log.debug(StringUtil.changeForLog("No intersection data ..."));
        }
        log.debug(StringUtil.changeForLog("alignMinExpiryMonth-->" + alignMinExpiryMonth));
        searchParam.addFilter("alignMinExpiryMonth", alignMinExpiryMonth,true);

        searchParam.addFilter(LICENSEEID,licenseeId,true);
        QueryHelp.setMainSql(INTER_INBOX_QUERY, "getLicenceBySerName",searchParam);
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

    public List<String> transferToList(Set<String> targetSet){
        List<String> result = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(targetSet)){
            targetSet.forEach(val-> result.add(val));
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
