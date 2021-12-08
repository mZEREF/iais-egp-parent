package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.assessmentGuide.GuideConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationSubDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AppAlignLicQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.MenuLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnlAssessQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.SelfPremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

    private static final String APP_SELECT_SERVICE = "appSelectSvc";
    private static final String HAS_EXISTING_BASE = "hasExistingBase";
    private static final String ONLY_BASE_SVC = "onlyBaseSvc";
    private static final String BASEANDSPCSVCMAP = "baseAndSpcSvcMap";
    private static final String RETAIN_LIC_PREMISES_LIST =  "retainLicPremisesList";
    private static final String NO_EXIST_BASE_LIC = "noExistBaseLic";
    public static final String APP_SVC_RELATED_INFO_LIST = "appSvcRelatedInfoList";
    private static final String RELOAD_BASE_SVC_SELECTED = "reloadBaseSvcSelected";
    private static final String BASE_LIC_PREMISES_MAP = "baseLicPremisesMap";
    public static final String SELECT_DRAFT_NO          ="selectDraftNo";
    private static final String LIC_ALIGN_SEARCH_PARAM = "licAlignSearchParam";
    private static final String LIC_ALIGN_SEARCH_RESULT= "licAlignSearchResult";
    private static final String BASE_SVC_PREMISES_MAP = "baseSvcPremisesMap";

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
                bpc.request.getSession().setAttribute("DraftNumber", null);
                bpc.request.getSession().setAttribute(SELECT_DRAFT_NO, draftNo);
            }else if("resume".equals(crud_action_value)){
                bpc.request.getSession().setAttribute("DraftNumber", draftNo);
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
                bpc.request.getSession().setAttribute("DraftNumber", null);
            }else if("resume".equals(crud_action_value)){
                bpc.request.getSession().setAttribute("DraftNumber", attribute);
            }
            appSelectSvcDto.setAlignPage(true);
        }

        ParamUtil.setSessionAttr(bpc.request,APP_SELECT_SERVICE,appSelectSvcDto);
        log.info(StringUtil.changeForLog("do choose align end ..."));
    }

    public void doChooseBaseSvc(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("do choose base svc start ..."));
        AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
        List<HcsaServiceDto> speSvcDtoList = appSelectSvcDto.getSpeSvcDtoList();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = IaisCommonUtils.genNewArrayList();
        Map<String,AppSvcRelatedInfoDto>  baseReloadDtoMap = IaisCommonUtils.genNewHashMap();
        boolean chooseExist = false;
        boolean chooseDiff = false;
        //deal multi spec choose one base svc
        Set<String> baseSvcCodes = IaisCommonUtils.genNewHashSet();
        Set<String> premHcis = IaisCommonUtils.genNewHashSet();
        List<String> newSpeBaseSvcNames = IaisCommonUtils.genNewArrayList();
        List<String> speSvcIdList = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(speSvcDtoList)){
            Map<String,List<AppAlignLicQueryDto>> baseLicMap = (Map<String, List<AppAlignLicQueryDto>>) ParamUtil.getSessionAttr(bpc.request,BASE_LIC_PREMISES_MAP);
            //reload
            for(HcsaServiceDto speServiceDto:speSvcDtoList){
                speSvcIdList.add(speServiceDto.getId());
                AppSvcRelatedInfoDto appSvcRelatedInfoDto;
                AppSvcRelatedInfoDto baseReloadDto;
                //specified svc
                String baseSvcMaskCode = ParamUtil.getString(bpc.request,speServiceDto.getSvcCode()+"-base");
                String baseSvcCode = ParamUtil.getString(bpc.request,baseSvcMaskCode);
                HcsaServiceDto baseServiceDto = HcsaServiceCacheHelper.getServiceByCode(baseSvcCode);
                baseReloadDto = new AppSvcRelatedInfoDto();
                if(!StringUtil.isEmpty(baseSvcMaskCode) && !StringUtil.isEmpty(baseSvcCode)){
                    if(baseSvcMaskCode.contains("-new")){
                        //new base+spec
                        chooseDiff = true;
                        if(!baseSvcCodes.contains(baseServiceDto.getSvcCode())){
                            appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
                            appSvcRelatedInfoDto.setServiceId(baseServiceDto.getId());
                            appSvcRelatedInfoDto.setServiceName(baseServiceDto.getSvcName());
                            appSvcRelatedInfoDto.setServiceCode(baseServiceDto.getSvcCode());
                            appSvcRelatedInfoDto.setServiceType(baseServiceDto.getSvcType());
                            appSvcRelatedInfoDto.setBaseServiceId(baseServiceDto.getId());
                            appSvcRelatedInfoDtos.add(appSvcRelatedInfoDto);
                            baseSvcCodes.add(baseServiceDto.getSvcCode());
                        }
                        appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
                        appSvcRelatedInfoDto.setServiceId(speServiceDto.getId());
                        appSvcRelatedInfoDto.setServiceName(speServiceDto.getSvcName());
                        appSvcRelatedInfoDto.setServiceCode(speServiceDto.getSvcCode());
                        appSvcRelatedInfoDto.setServiceType(speServiceDto.getSvcType());
                        appSvcRelatedInfoDto.setBaseServiceId(baseServiceDto.getId());
                        appSvcRelatedInfoDtos.add(appSvcRelatedInfoDto);
                        newSpeBaseSvcNames.add(baseServiceDto.getSvcName());
                        //for reload
                        baseReloadDto.setServiceCode(baseServiceDto.getSvcCode());
                        baseReloadDto.setServiceName(baseServiceDto.getSvcName());
                        baseReloadDto.setLicPremisesId("");
                        baseReloadDtoMap.put(speServiceDto.getSvcCode(),baseReloadDto);
                    }else{
                        chooseExist = true;
                        String premIndexNo = ParamUtil.getString(bpc.request,speServiceDto.getSvcCode());
                        String hciCode = ParamUtil.getMaskedString(bpc.request,premIndexNo+"-hciCode");
                        AppAlignLicQueryDto appAlignLicQueryDto = getAppAlignLicQueryDto(baseLicMap,baseServiceDto.getSvcName(),hciCode);
                        if(appAlignLicQueryDto != null){
                            String premHci = getPremisesHci(appAlignLicQueryDto);
                            premHcis.add(premHci);
                            appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
                            appSvcRelatedInfoDto.setServiceId(speServiceDto.getId());
                            appSvcRelatedInfoDto.setServiceName(speServiceDto.getSvcName());
                            appSvcRelatedInfoDto.setServiceCode(speServiceDto.getSvcCode());
                            appSvcRelatedInfoDto.setServiceType(speServiceDto.getSvcType());
                            appSvcRelatedInfoDto.setBaseServiceId(baseServiceDto.getId());
                            appSvcRelatedInfoDto.setRelLicenceNo(appAlignLicQueryDto.getLicenceNo());
                            appSvcRelatedInfoDto.setLicPremisesId(appAlignLicQueryDto.getPremisesId());
                            appSvcRelatedInfoDto.setHciCode(appAlignLicQueryDto.getHciCode());
                            appSvcRelatedInfoDtos.add(appSvcRelatedInfoDto);
                            //for reload
                            baseReloadDto.setServiceCode(baseServiceDto.getSvcCode());
                            baseReloadDto.setServiceName(baseServiceDto.getSvcName());
                            baseReloadDto.setLicPremisesId(appAlignLicQueryDto.getPremisesId());
                            baseReloadDto.setHciCode(appAlignLicQueryDto.getHciCode());
                            baseReloadDtoMap.put(speServiceDto.getSvcCode(),baseReloadDto);
                        }
                        List<String> list=new ArrayList<>(1);
                        list.add(baseServiceDto.getSvcCode());
                        bpc.request.getSession().setAttribute("licence",list);
                    }
                }
            }
            //other base


            //sort
            appSvcRelatedInfoDtos = sortAppSvcRelatDto(appSvcRelatedInfoDtos);
            ParamUtil.setSessionAttr(bpc.request,APP_SVC_RELATED_INFO_LIST, (Serializable) appSvcRelatedInfoDtos);
            ParamUtil.setSessionAttr(bpc.request,RELOAD_BASE_SVC_SELECTED, (Serializable) baseReloadDtoMap);
        }

        String additional = ParamUtil.getString(bpc.request,CRUD_ACTION_ADDITIONAL);
        if(BACK_ATTR.equals(additional)){
            return;
        }
        //validate
        String erroMsg = "";
        int premHcisLength = premHcis.size();
        boolean allSpecNew = chooseDiff && !chooseExist && premHcisLength == 0;
        boolean existAndNew = chooseExist && chooseDiff;
        String errSeven = MessageUtil.getMessageDesc("NEW_ERR0007");
        if(premHcisLength > 1){
            //choose existing premises not same
            erroMsg = errSeven;
        }else if(allSpecNew){
            //all choose new
        }else if(existAndNew && premHcisLength == 1){
            //choose existing and new
            Map<String,List<AppAlignLicQueryDto>> baseSvcPremMap = (Map<String, List<AppAlignLicQueryDto>>) ParamUtil.getSessionAttr(bpc.request,BASE_SVC_PREMISES_MAP);
            Iterator<String> iterator = premHcis.iterator();
            String premHci = "";
            if(iterator.hasNext()){
                premHci = iterator.next();
            }
            for(String svcName:newSpeBaseSvcNames){
                List<String> currSvcPremHcis = getAppAlignLicQueryHci(baseSvcPremMap,svcName);
                if(currSvcPremHcis.contains(premHci)){
                    erroMsg = errSeven;
                    break;
                }
            }
        }

        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = "";
        if(loginContext!=null){
            licenseeId  = loginContext.getLicenseeId();
        }
        //
        if(StringUtil.isEmpty(erroMsg)){
            List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                svcCodeList.add(appSvcRelatedInfoDto.getServiceCode());
            }
            List<ApplicationSubDraftDto> applicationSubDraftDtos = assessmentGuideService.getDraftListBySvcCodeAndStatus(svcCodeList,ApplicationConsts.DRAFT_STATUS_PENDING_PAYMENT,licenseeId,ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
            if(!IaisCommonUtils.isEmpty(applicationSubDraftDtos)){
                ParamUtil.setRequestAttr(bpc.request,"chooseBaseErr2",MessageUtil.getMessageDesc("NEW_ERR0023"));
            }
        }

        if(StringUtil.isEmpty(erroMsg)){
            //choose existing
            if(chooseExist){
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
                String nextstep = ParamUtil.getString(bpc.request,"crud_action_additional");
                if(NEXT.equals(nextstep)){
                    String crud_action_value=bpc.request.getParameter("crud_action_value");
                    String draftNo  =bpc.request.getParameter("draftNo");
                    getDraft(bpc);
                    String attribute =(String)bpc.request.getAttribute(SELECT_DRAFT_NO);
                    if("continue".equals(crud_action_value)){
                        bpc.request.getSession().setAttribute(SELECT_DRAFT_NO, draftNo);
                        bpc.request.getSession().setAttribute("DraftNumber", null);
                    }else if("resume".equals(crud_action_value)){
                        bpc.request.getSession().setAttribute("DraftNumber", draftNo);
                    }else if(attribute!=null){
                        //back to curr page
                        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,CHOOSE_BASE_SVC);
                        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,"doBack");
                    }
                }
            }else{
                boolean newLicensee = appSelectSvcDto.isNewLicensee();
                if(newLicensee){
                    //Scenario 8:at least 2 base svc
                    Set<String> svcCodeList = IaisCommonUtils.genNewHashSet();
                    List<HcsaServiceDto> baseSvcDtos = appSelectSvcDto.getBaseSvcDtoList();
                    if(baseSvcCodes != null){
                        for(HcsaServiceDto hcsaServiceDto:baseSvcDtos){
                            svcCodeList.add(hcsaServiceDto.getSvcCode());
                        }
                    }
                    if(!IaisCommonUtils.isEmpty(baseSvcCodes)){
                        svcCodeList.addAll(baseSvcCodes);
                    }
                    //107348
                    /*if(svcCodeList.size()>1){
                        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,CHOOSE_ALIGN);
                    }else{
                        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
                    }*/
                    ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
                }else{
                    if(chooseExist){
                        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
                    }else if(!chooseExist){
                        /*LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
                        String licenseeId = "";
                        if(loginContext!=null){
                            licenseeId = loginContext.getLicenseeId();
                        }*/
                        //new
                        //judge whether had existing licence
                        List<String> chkBase = IaisCommonUtils.genNewArrayList();
                        List<HcsaServiceDto> baseHcsaSvcChkDto = appSelectSvcDto.getBaseSvcDtoList();
                        for(HcsaServiceDto hcsaServiceDto:baseHcsaSvcChkDto){
                            chkBase.add(hcsaServiceDto.getId());
                        }
                        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                            if(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())){
                                chkBase.add(appSvcRelatedInfoDto.getServiceId());
                            }
                        }
                        List<String> allBaseId = IaisCommonUtils.genNewArrayList();
                        List<HcsaServiceDto> hcsaServiceDtoList = assessmentGuideService.getServicesInActive();
                        List<HcsaServiceDto> allbaseService = hcsaServiceDtoList.stream()
                                .filter(hcsaServiceDto -> BASE_SERVICE.equals(hcsaServiceDto.getSvcType())).collect(Collectors.toList());
                        for(HcsaServiceDto hcsaServiceDto:allbaseService){
                            allBaseId.add(hcsaServiceDto.getId());
                        }
                        allBaseId.removeAll(chkBase);
                        //get prem type intersection
                        List<String> allChekSvcIdList = IaisCommonUtils.genNewArrayList();
                        speSvcIdList.forEach(svcId->{
                            if(!allChekSvcIdList.contains(svcId)){
                                allChekSvcIdList.add(svcId);
                            }
                        });
                        chkBase.forEach(svcId->{
                            if(!allChekSvcIdList.contains(svcId)){
                                allChekSvcIdList.add(svcId);
                            }
                        });
                        Set<String> premisesTypeList = assessmentGuideService.getAppGrpPremisesTypeBySvcId(allChekSvcIdList);
                        int alignMinExpiryMonth = systemParamConfig.getAlignMinExpiryMonth();
                        log.debug(StringUtil.changeForLog("alignMinExpiryMonth:"+alignMinExpiryMonth));
                        SearchResult<MenuLicenceDto> searchResult = getAlignLicPremInfo(allBaseId,licenseeId,premisesTypeList,alignMinExpiryMonth);
                        //filter pending and existing data
                        List<MenuLicenceDto> newAppLicDtos = removePendAndExistPrem(chkBase,searchResult.getRows(),licenseeId);
                        //pagination
                        if(!IaisCommonUtils.isEmpty(newAppLicDtos)){
                            initPaginationHandler(newAppLicDtos);
                            appSelectSvcDto.setInitPagHandler(true);
                        }
                        if(IaisCommonUtils.isEmpty(newAppLicDtos) || (!IaisCommonUtils.isEmpty(newAppLicDtos) && newAppLicDtos.size() <= 1)){
                            //107348
                            //ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,CHOOSE_ALIGN);
                            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
                        }else{
                            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,CHOOSE_LICENCE);
                        }
                    }
                }
            }

            String switchStep = ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE);
            if(NEXT.equals(switchStep)){
                String crud_action_value=bpc.request.getParameter("crud_action_value");
                String draftNo  =bpc.request.getParameter("draftNo");
                getDraft(bpc);
                String attribute =(String)bpc.request.getAttribute(SELECT_DRAFT_NO);
                if("continue".equals(crud_action_value)){
                    List<String> list=new ArrayList<>(1);
                    list.add(draftNo);
                    log.info(StringUtil.changeForLog("delete draft start ..."));
                    appInboxClient.deleteDraftNUmber(list);
                    bpc.request.getSession().setAttribute("DraftNumber", null);
                }else if("resume".equals(crud_action_value)){
                    bpc.request.getSession().setAttribute("DraftNumber", attribute);
                }else if(attribute!=null){
                    //back to curr page
                    ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,CHOOSE_BASE_SVC);
                    ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,"doBack");
                }
                appSelectSvcDto.setBasePage(true);
            }
        }else{
            ParamUtil.setRequestAttr(bpc.request,"chooseBaseErr",erroMsg);
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,CHOOSE_BASE_SVC);
        }

        //for next page
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
        boolean noExistBaseLic = false;
        //specSvcCode,baseSvcDtoList
        Map<String,List<HcsaServiceDto>> baseAndSpcSvcMap = IaisCommonUtils.genNewHashMap();
        //get correlatrion base service
        AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
        List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList =  assessmentGuideService.getActiveSvcCorrelation();
        List<HcsaServiceDto> specSvcDtos = appSelectSvcDto.getSpeSvcDtoList();
        List<HcsaServiceDto> baseSvcDtoList = IaisCommonUtils.genNewArrayList();
        Set<String> allChkSvcIds = IaisCommonUtils.genNewHashSet();
        for(HcsaServiceDto hcsaServiceDto:specSvcDtos){
            List<HcsaServiceDto> baseServiceDtos = getBaseBySpc(hcsaServiceCorrelationDtoList,hcsaServiceDto.getId());
            baseSvcDtoList.addAll(baseServiceDtos);
            baseAndSpcSvcMap.put(hcsaServiceDto.getSvcCode(),baseServiceDtos);
            allChkSvcIds.add(hcsaServiceDto.getId());
        }
        List<HcsaServiceDto> pendAndLicPremSvc = IaisCommonUtils.genNewArrayList();
        List<HcsaServiceDto> chkBaseSvcDtos = appSelectSvcDto.getBaseSvcDtoList();
        if(!IaisCommonUtils.isEmpty(chkBaseSvcDtos) && !IaisCommonUtils.isEmpty(baseSvcDtoList)){
            List<String> alignBaseSvcCodes = IaisCommonUtils.genNewArrayList();
            for(HcsaServiceDto hcsaServiceDto:baseSvcDtoList){
                alignBaseSvcCodes.add(hcsaServiceDto.getSvcCode());
            }
            //remove align base svc
            List<HcsaServiceDto> newBaseSvcDots = IaisCommonUtils.genNewArrayList();
            for(HcsaServiceDto chkBaseSvcDto:chkBaseSvcDtos){
                if(!alignBaseSvcCodes.contains(chkBaseSvcDto.getSvcCode())){
                    newBaseSvcDots.add(chkBaseSvcDto);
                }
            }
            pendAndLicPremSvc.addAll(newBaseSvcDots);
        }
        pendAndLicPremSvc.addAll(specSvcDtos);
        ParamUtil.setSessionAttr(bpc.request,BASEANDSPCSVCMAP, (Serializable) baseAndSpcSvcMap);
        List<String> svcNameList = IaisCommonUtils.genNewArrayList();
        //init map ->svcName,List<AppAlignLicQueryDto> =>
        Map<String,List<AppAlignLicQueryDto>> svcPremises = IaisCommonUtils.genNewHashMap();
        for(HcsaServiceDto hcsaServiceDto:baseSvcDtoList){
            svcNameList.add(hcsaServiceDto.getSvcName());
            allChkSvcIds.add(hcsaServiceDto.getId());
//            List<AppAlignLicQueryDto> appAlignLicQueryDtos = IaisCommonUtils.genNewArrayList();
            //commPremises.put(hcsaServiceDto.getSvcName(),appAlignLicQueryDtos);
        }
        String licenseeId = "";
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
        if(loginContext != null){
            licenseeId = loginContext.getLicenseeId();
        }
        List<String> allChkSvcIdList = transferToList(allChkSvcIds);
        Set<String> premisesTypeList = assessmentGuideService.getAppGrpPremisesTypeBySvcId(allChkSvcIdList);
        log.debug("premises Type size {}",premisesTypeList.size());
        List<AppAlignLicQueryDto> appAlignLicQueryDtos = assessmentGuideService.getAppAlignLicQueryDto(licenseeId,svcNameList,transferToList(premisesTypeList));
        List<String> pendAndLicPremHci = assessmentGuideService.getHciFromPendAppAndLic(licenseeId,pendAndLicPremSvc);
        //remove item when same svc and same premises(hci)
        List<AppAlignLicQueryDto> newAppAlignLicQueryDtos = IaisCommonUtils.genNewArrayList();
        for(AppAlignLicQueryDto appAlignLicQueryDto:appAlignLicQueryDtos){
            boolean pendPremOrExistLic = false;
            PremisesDto premisesDto = MiscUtil.transferEntityDto(appAlignLicQueryDto,PremisesDto.class);
            List<String> premisesHciList = genPremisesHciList(premisesDto);
            for(String premisesHci:premisesHciList){
                if(pendAndLicPremHci.contains(premisesHci)){
                    pendPremOrExistLic = true;
                    break;
                }
            }
            if(!pendPremOrExistLic){
                newAppAlignLicQueryDtos.add(appAlignLicQueryDto);
            }
        }

        //hciCode,List<AppAlignLicQueryDto> => for get data again
        Map<String,List<AppAlignLicQueryDto>> baseLicMap = IaisCommonUtils.genNewHashMap();
        for(AppAlignLicQueryDto appAlignLicQueryDto:newAppAlignLicQueryDtos){
            AppAlignLicQueryDto baseLicDto = (AppAlignLicQueryDto) CopyUtil.copyMutableObject(appAlignLicQueryDto);
            List<AppAlignLicQueryDto> appAlignLicQueryDtoList = svcPremises.get(appAlignLicQueryDto.getSvcName());
            List<AppAlignLicQueryDto> baseLicDtoList = baseLicMap.get(baseLicDto.getHciCode());
            if(IaisCommonUtils.isEmpty(baseLicDtoList)){
                baseLicDtoList = IaisCommonUtils.genNewArrayList();
            }
            baseLicDtoList.add(baseLicDto);
            baseLicMap.put(baseLicDto.getHciCode(),baseLicDtoList);
            if(IaisCommonUtils.isEmpty(appAlignLicQueryDtoList)){
                appAlignLicQueryDtoList = IaisCommonUtils.genNewArrayList();
            }
            String svcName = appAlignLicQueryDto.getSvcName();
            appAlignLicQueryDtoList.add(appAlignLicQueryDto);
            svcPremises.put(svcName,appAlignLicQueryDtoList);
        }
        if(svcPremises.size() == 0) {
            noExistBaseLic = true;
        }
        ParamUtil.setSessionAttr(bpc.request,BASE_SVC_PREMISES_MAP, (Serializable) svcPremises);
        ParamUtil.setSessionAttr(bpc.request,BASE_LIC_PREMISES_MAP, (Serializable) baseLicMap);
        ParamUtil.setSessionAttr(bpc.request,NO_EXIST_BASE_LIC,noExistBaseLic);
        log.info(StringUtil.changeForLog("prepare choose base svc end ..."));
    }

    public void doNewAppStep(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("prepare choose svc start ..."));
        List<HcsaServiceDto> hcsaServiceDtoList = assessmentGuideService.getServicesInActive();
        if (IaisCommonUtils.isEmpty(hcsaServiceDtoList)){
            log.debug("can not find hcsa service list in service menu delegator!");
            return;
        }
        List<HcsaServiceDto> allbaseService = hcsaServiceDtoList.stream()
                .filter(hcsaServiceDto -> BASE_SERVICE.equals(hcsaServiceDto.getSvcType())).collect(Collectors.toList());
        List<HcsaServiceDto> allspecifiedService = hcsaServiceDtoList.stream()
                .filter(hcsaServiceDto -> SPECIFIED_SERVICE.equals(hcsaServiceDto.getSvcType())).collect(Collectors.toList());
        //sort
        allbaseService.sort((h1,h2)->h1.getSvcName().compareTo(h2.getSvcName()));
        allspecifiedService.sort((h1,h2)->h1.getSvcName().compareTo(h2.getSvcName()));
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR, (Serializable) allbaseService);
        ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR, (Serializable) allspecifiedService);
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

    public void doChooseService(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("do choose svc start ..."));
        boolean onlyBaseSvc = false;
        ParamUtil.setSessionAttr(bpc.request,ONLY_BASE_SVC,onlyBaseSvc);
        String additional = ParamUtil.getString(bpc.request,CRUD_ACTION_ADDITIONAL);
        if(BACK_ATTR.equals(additional)){
            return;
        }
        List<HcsaServiceDto> allbaseService = getAllBaseService(bpc);
//        List<HcsaServiceDto> allspecifiedService = getAllSpeService(bpc);
        AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
        String currentPage = "chooseSvc";
        String[] basechks = ParamUtil.getStrings(bpc.request, BASE_SERVICE_CHECK_BOX_ATTR);
        String[] sepcifiedchk = ParamUtil.getStrings(bpc.request, SPECIFIED_SERVICE_CHECK_BOX_ATTR);
        String err = "";
        String nextstep = "";
        List<String> basecheckedlist = IaisCommonUtils.genNewArrayList();
        List<String> sepcifiedcheckedlist = IaisCommonUtils.genNewArrayList();
        List<HcsaServiceDto> baseSvcSort = IaisCommonUtils.genNewArrayList();
        List<HcsaServiceDto> speSvcSort = IaisCommonUtils.genNewArrayList();
        if(basechks == null){
            log.info(StringUtil.changeForLog("basechks is null ..."));
            List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList =  assessmentGuideService.getActiveSvcCorrelation();
            //no base service
            if(sepcifiedchk != null){
//                //spe choose base
                for (String item:sepcifiedchk) {
                    sepcifiedcheckedlist.add(item);
                    speSvcSort.add(HcsaServiceCacheHelper.getServiceById(item));
                }

                //judge matching base svc
                List<String> allBaseSvcId = IaisCommonUtils.genNewArrayList();
                for(HcsaServiceDto hcsaServiceDto:allbaseService){
                    allBaseSvcId.add(hcsaServiceDto.getId());
                }
                Map<String,List<String>> baseRelSpe = baseRelSpe(allBaseSvcId,sepcifiedcheckedlist,hcsaServiceCorrelationDtoList);
                if (baseRelSpe.size() == 0){
                    nextstep = currentPage;
                    err = MessageUtil.getMessageDesc("NEW_ERR0002");
                    ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err);
                }else{
                    //to step2
                    nextstep = CHOOSE_BASE_SVC;
                }
            }else{
                //no spe err
                nextstep = currentPage;
                err = MessageUtil.getMessageDesc("NEW_ERR0001");
                ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err);
            }
        }else{
            log.info(StringUtil.changeForLog("basechks is not null ..."));
            for (String item:basechks) {
                basecheckedlist.add(item);
                baseSvcSort.add(HcsaServiceCacheHelper.getServiceById(item));
            }
            //base
//            Map<String ,String> specifiedName = IaisCommonUtils.genNewHashMap();
//            Map<String ,String> baseName = IaisCommonUtils.genNewHashMap();
//
//            for (HcsaServiceDto item:allbaseService
//                    ) {
//                baseName.put(item.getId(),item.getSvcName());
//            }
//            for (HcsaServiceDto item:allspecifiedService
//                    ) {
//                specifiedName.put(item.getId(),item.getSvcName());
//            }

            if(sepcifiedchk != null){
                log.info(StringUtil.changeForLog("sepcifiedchk is not null ..."));
                List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList =  assessmentGuideService.getActiveSvcCorrelation();
                log.info(StringUtil.changeForLog("hcsaServiceCorrelationDtoList size:"+hcsaServiceCorrelationDtoList.size()));
                for (String item:sepcifiedchk) {
                    sepcifiedcheckedlist.add(item);
                    speSvcSort.add(HcsaServiceCacheHelper.getServiceById(item));
                }
                List<String> svcNameList = IaisCommonUtils.genNewArrayList();
                //specified id,base name List
                Map<String,List<String>> baseAlignSpec = IaisCommonUtils.genNewHashMap();
                for(HcsaServiceCorrelationDto hcsaServiceCorrelationDto:hcsaServiceCorrelationDtoList){
                    String specSvcId = hcsaServiceCorrelationDto.getSpecifiedSvcId();
                    String baseSvcId = hcsaServiceCorrelationDto.getBaseSvcId();
                    if(sepcifiedcheckedlist.contains(specSvcId)){
                        List<String> baseSvcNameList = baseAlignSpec.get(specSvcId);
                        if(IaisCommonUtils.isEmpty(baseSvcNameList)){
                            baseSvcNameList = IaisCommonUtils.genNewArrayList();
                        }
                        String baseSvcName = HcsaServiceCacheHelper.getServiceById(baseSvcId).getSvcName();
                        baseSvcNameList.add(baseSvcName);
                        baseAlignSpec.put(specSvcId,baseSvcNameList);
                        svcNameList.add(baseSvcName);
                    }
                }
                String licenseeId = "";
                LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
                if(loginContext != null){
                    licenseeId = loginContext.getLicenseeId();
                }else{
                    log.info(StringUtil.changeForLog("can not found licenseeId"));
                }


                //check align whether correct
                List<String> errMsgList = IaisCommonUtils.genNewArrayList();
//                    String specSvcName = "";
                boolean flag = true;
                for(Map.Entry<String,List<String>> entry:baseAlignSpec.entrySet()){
                    String specSvcId = entry.getKey();
                    List<String> baseSvcNameList = entry.getValue();
                    if(!IaisCommonUtils.isEmpty(baseSvcNameList)){
                        int i = 0;
                        for(String baseSvcId:basechks){
                            String baseSvcName = HcsaServiceCacheHelper.getServiceById(baseSvcId).getSvcName();
                            if(baseSvcNameList.contains(baseSvcName)){
                                break;
                            }
                            if(i == basechks.length-1){
                                flag = false;
                                String specSvcName = HcsaServiceCacheHelper.getServiceById(specSvcId).getSvcName();
//                                    String errMsg = "You are not allowed to apply for <service name> as you do not have the required base service licence, please apply for <base service name>";
                                String errMsg = MessageUtil.getMessageDesc("NEW_ERR0008");
                                errMsg =  errMsg.replace("{service name}",specSvcName);
                                String baseNameStr = baseSvcNameList.stream().collect(Collectors.joining(","));
                                errMsg = errMsg.replace("{base service name}",baseNameStr);
                                errMsgList.add(errMsg);
                            }
                            i++;
                        }
                    }
                }
                if(flag){
                    nextstep = CHOOSE_BASE_SVC;
                }else{
                    nextstep = currentPage;
                    ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR_LIST, errMsgList);
                }
            }else{
                //new app
                nextstep = CHOOSE_ALIGN;
                onlyBaseSvc = true;
                ParamUtil.setSessionAttr(bpc.request,ONLY_BASE_SVC,onlyBaseSvc);
            }
            if(!currentPage.equals(nextstep)){
                List<String> chkSvcIdList = IaisCommonUtils.genNewArrayList();
                chkSvcIdList.addAll(basecheckedlist);
                chkSvcIdList.addAll(sepcifiedcheckedlist);
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

        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = "";
        if(loginContext!=null){
            licenseeId  = loginContext.getLicenseeId();
        }
        //
        if(!currentPage.equals(nextstep)){
            List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
            for(HcsaServiceDto hcsaServiceDto:baseSvcSort){
                svcCodeList.add(hcsaServiceDto.getSvcCode());
            }
            for(HcsaServiceDto hcsaServiceDto:speSvcSort){
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

//        appSelectSvcDto.setBaseSvcIds(basecheckedlist);
//        appSelectSvcDto.setSpecifiedSvcIds(sepcifiedcheckedlist);
        appSelectSvcDto.setBaseSvcDtoList(baseSvcSort);
        appSelectSvcDto.setSpeSvcDtoList(speSvcSort);
        ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR_CHECKED, (Serializable) sepcifiedcheckedlist);
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR_CHECKED, (Serializable) basecheckedlist);
        List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList =  assessmentGuideService.getActiveSvcCorrelation();
        //control switch
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = null;
        if(!currentPage.equals(nextstep)){
            boolean newLicensee  = true;
            newLicensee =  assessmentGuideService.isNewLicensee(licenseeId);
            appSelectSvcDto.setNewLicensee(newLicensee);
            if(newLicensee){
                if(nextstep.equals(CHOOSE_BASE_SVC)){
                    //64570 (1base+1spec)
                    boolean jumpToNext = (basechks != null && basechks.length == 1) && (sepcifiedchk != null && sepcifiedchk.length ==1);
                    if(jumpToNext){
                        appSvcRelatedInfoDtos = IaisCommonUtils.genNewArrayList();
                        Set<String> svcIds = IaisCommonUtils.genNewHashSet();
                        for(String svcId:sepcifiedchk){
                            svcIds.add(svcId);
                            List<HcsaServiceDto> baseServiceDtos = getBaseBySpc(hcsaServiceCorrelationDtoList,svcId);
                            //default first
                            if(IaisCommonUtils.isEmpty(baseServiceDtos)){
                                log.info(StringUtil.changeForLog("can not found base svc by spec svc id ..."));
                            }else{
                                svcIds.add(baseServiceDtos.get(0).getId());
                            }
                        }
                        if(basechks != null){
                            for(String svcId:basechks){
                                svcIds.add(svcId);
                            }
                        }
                        for(String svcId:svcIds){
                            AppSvcRelatedInfoDto appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
                            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(svcId);
                            appSvcRelatedInfoDto.setServiceId(hcsaServiceDto.getId());
                            appSvcRelatedInfoDto.setServiceName(hcsaServiceDto.getSvcName());
                            appSvcRelatedInfoDto.setServiceCode(hcsaServiceDto.getSvcCode());
                            appSvcRelatedInfoDto.setServiceType(hcsaServiceDto.getSvcType());
                            appSvcRelatedInfoDto.setBaseServiceId(hcsaServiceDto.getId());
                            if(ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED.equals(hcsaServiceDto.getSvcType())){
                                List<HcsaServiceDto> baseServiceDtos =  getBaseBySpc(hcsaServiceCorrelationDtoList,hcsaServiceDto.getId());
                                for(HcsaServiceDto hcsaServiceDto1:baseServiceDtos){
                                    if(svcIds.contains(hcsaServiceDto.getId())){
                                        appSvcRelatedInfoDto.setBaseServiceId(hcsaServiceDto1.getId());
                                        break;
                                    }
                                }
                            }
                            appSvcRelatedInfoDtos.add(appSvcRelatedInfoDto);
                        }
                        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
                    }else{
                        nextstep = CHOOSE_BASE_SVC;
                    }
                }else if(nextstep.equals(CHOOSE_ALIGN)){
                    //107348
//                    if(basechks.length == 1){
//                        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
//                    }else{
//                        nextstep = CHOOSE_ALIGN;
//                    }
                    ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
                }
            }else{
                if(nextstep.equals(CHOOSE_BASE_SVC)){

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
                    int relMinExpiryMonth = systemParamConfig.getRelMinExpiryMonth();
                    int alignMinExpiryMonth = systemParamConfig.getAlignMinExpiryMonth();
                    log.debug(StringUtil.changeForLog("relMinExpiryMonth:"+relMinExpiryMonth));
                    log.debug(StringUtil.changeForLog("alignMinExpiryMonth:"+alignMinExpiryMonth));
                    int maxExpiryMonth;
                    if(relMinExpiryMonth > alignMinExpiryMonth){
                        maxExpiryMonth = relMinExpiryMonth;
                    }else{
                        maxExpiryMonth = alignMinExpiryMonth;
                    }
                    SearchResult<MenuLicenceDto> searchResult = getAlignLicPremInfo(allBaseId,licenseeId,premisesTypeList,maxExpiryMonth);
                    //filter pending and existing data
                    List<MenuLicenceDto> newAppLicDtos = removePendAndExistPrem(chkBase,searchResult.getRows(),licenseeId);
                    //pagination
                    if(!IaisCommonUtils.isEmpty(newAppLicDtos)){
                        initPaginationHandler(newAppLicDtos);
                    }

                    //only not align option
                    if(IaisCommonUtils.isEmpty(newAppLicDtos) || (!IaisCommonUtils.isEmpty(newAppLicDtos) && newAppLicDtos.size() <= 1)){
                        //107348
                        /*if(basechks.length > 1){
                            //0066206
                            nextstep = CHOOSE_ALIGN;
                        }else{
                            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
                        }*/
                        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
                    }

                }
                appSelectSvcDto.setInitPagHandler(true);
            }
        }
        log.info(StringUtil.changeForLog("do choose svc next step:"+nextstep));
        ParamUtil.setSessionAttr(bpc.request,APP_SELECT_SERVICE,appSelectSvcDto);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, nextstep);
        //reset
        ParamUtil.setSessionAttr(bpc.request,APP_SVC_RELATED_INFO_LIST, (Serializable) appSvcRelatedInfoDtos);
        ParamUtil.setSessionAttr(bpc.request,RELOAD_BASE_SVC_SELECTED, null);
        appSelectSvcDto.setAlign(false);
        ParamUtil.setSessionAttr(bpc.request,APP_SELECT_SERVICE,appSelectSvcDto);
        ParamUtil.setRequestAttr(bpc.request,"firstVisitFor","Y");
        String requestAttr = (String)ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE);
        if(NEXT.equals(requestAttr)){
            String crud_action_value=bpc.request.getParameter("crud_action_value");
            String draftNo  =bpc.request.getParameter("draftNo");
            getDraft(bpc);
            String attribute =(String)bpc.request.getAttribute(SELECT_DRAFT_NO);
            if("continue".equals(crud_action_value)){
                bpc.request.getSession().setAttribute(SELECT_DRAFT_NO, draftNo);
                bpc.request.getSession().setAttribute("DraftNumber", null);
            }else if("resume".equals(crud_action_value)){
                bpc.request.getSession().setAttribute("DraftNumber", attribute);
            }else if(attribute!=null){
                //back to curr page
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,"doBack");
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,CHOOSE_SERVICE);
            }
        }
        //test
        //ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
        log.info(StringUtil.changeForLog("do choose svc end ..."));
    }

    private List<HcsaServiceDto> getAllBaseService(BaseProcessClass bpc){
        List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request,BASE_SERVICE_ATTR);
        if(IaisCommonUtils.isEmpty(hcsaServiceDtos)){
            hcsaServiceDtos = IaisCommonUtils.genNewArrayList();
        }
        return hcsaServiceDtos;
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
                    if(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(hcsaServiceDto.getSvcType())){
                        baseSvcIds.add(hcsaServiceDto.getId());
                        if(appSelectSvcDto.isAlign()){
                            appSvcRelatedInfoDto.setAlignFlag(alignFlag);
                        }
                    }else if(ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED.equals(hcsaServiceDto.getSvcType())){
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
                bpc.request.getSession().setAttribute("DraftNumber", null);
            }else if("resume".equals(crud_action_value)){
                bpc.request.getSession().setAttribute("DraftNumber", attribute);
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
                bpc.request.getSession().setAttribute("DraftNumber", null);
            }else if("resume".equals(crud_action_value)){
                bpc.request.getSession().setAttribute("DraftNumber", attribute);
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
                    appSvcRelatedInfoDto.setServiceType(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE);
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
        String actionType = ParamUtil.getString(bpc.request,"guide_action_type");
        String [] licIds = ParamUtil.getStrings(bpc.request, "renewLicenId");
        Map<String, String> renewErrorMap = IaisCommonUtils.genNewHashMap();
        String tmp = MessageUtil.getMessageDesc("INBOX_ACK015");
        StringBuilder renewErrorMessage = new StringBuilder();
        boolean result = true;
        if(licIds != null){
            List<String> licIdValue = IaisCommonUtils.genNewArrayList();
            for(String item:licIds){
                licIdValue.add(ParamUtil.getMaskedString(bpc.request,item));
            }
            ParamUtil.setSessionAttr(bpc.request,"licence_err_list",(Serializable) licIdValue);
            for (String licId:licIdValue) {
                renewErrorMap = inboxService.checkRenewalStatus(licId);
                if(!(renewErrorMap.isEmpty())){
                    String licenseNo = renewErrorMap.get("errorMessage");
                    if(!StringUtil.isEmpty(licenseNo)){
                        if(StringUtil.isEmpty(renewErrorMessage.toString())){
                            renewErrorMessage.append(tmp).append(licenseNo);
                        }else{
                            renewErrorMessage.append(", ").append(licenseNo);
                        }
                    }
                    result = false;
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
                    for(ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId){
                        inboxService.deleteDraftByNo(applicationSubDraftDto.getDraftNo());
                    }
                }else {
                    String ack030 = MessageUtil.getMessageDesc("GENERAL_ACK030");
                    String replace = ack030.replace("{draft application no}", stringBuilder.toString());
                    bpc.request.setAttribute("draftByLicAppId",replace);
                    bpc.request.setAttribute("isAppealShow","1");
                    bpc.request.setAttribute("appealApplication",licIdValue.get(0));
                    ParamUtil.setSessionAttr(bpc.request,"licence_err_list",(Serializable) licIdValue);
                    if ("renew".equals(actionType)){
                        ParamUtil.setRequestAttr(bpc.request,"guide_back_action","backRenewUpdate");
                    }else{
                        ParamUtil.setRequestAttr(bpc.request,"guide_back_action","backRenew");
                    }
                    return;
                }
            }
            if (result){
                StringBuilder url = new StringBuilder();
                url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                        .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohWithOutRenewal");
                ParamUtil.setSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_LIC_ID_LIST_ATTR, (Serializable) licIdValue);
                String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
                IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
            }else{
                ParamUtil.setRequestAttr(bpc.request,"licIsRenewed",result);
                if(StringUtil.isEmpty(renewErrorMessage.toString())){
                    String errorMessage2 = renewErrorMap.get("errorMessage2");
                    if(StringUtil.isEmpty(errorMessage2)){
                        renewErrorMessage.append(MessageUtil.getMessageDesc("RFC_ERR011"));
                    }else{
                        renewErrorMessage.append(errorMessage2);
                    }
                }
                ParamUtil.setRequestAttr(bpc.request,InboxConst.LIC_ACTION_ERR_MSG,renewErrorMessage.toString());
                if ("renew".equals(actionType)){
                    ParamUtil.setRequestAttr(bpc.request,"guide_back_action","backRenewUpdate");
                }else{
                    ParamUtil.setRequestAttr(bpc.request,"guide_back_action","backRenew");
                }
            }
        }
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
        QueryHelp.setMainSql("interInboxQuery", "queryPremises", amendDetailsSearchParam);
        SearchResult<SelfPremisesListQueryDto> amendDetailsSearchResult = requestForChangeService.searchPreInfo(amendDetailsSearchParam);
        if (!StringUtil.isEmpty(amendDetailsSearchResult)) {
            ParamUtil.setSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_PERSONNEL_SEARCH_PARAM, amendDetailsSearchParam);
            ParamUtil.setRequestAttr(bpc.request, GuideConsts.AMEND_UPDATE_PERSONNEL_SEARCH_RESULT, amendDetailsSearchResult);
        }
        log.info("****end ******");

    }

    public void amendLic4_2(BaseProcessClass bpc) {
        log.info("****start ******");
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
        log.info("****end ******");
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
                    .append(MaskUtil.maskValue("DraftNumber",appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }else if(InboxConst.APP_DO_DRAFT_TYPE_RENEW.equals(appType)){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohWithOutRenewal")
                    .append("?DraftNumber=")
                    .append(MaskUtil.maskValue("DraftNumber",appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }else if(InboxConst.APP_DO_DRAFT_TYPE_APPEAL.equals(appType)){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohAppealApplication")
                    .append("?DraftNumber=")
                    .append(MaskUtil.maskValue("DraftNumber",appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }
        else {
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohNewApplication")
                    .append("?DraftNumber=")
                    .append(MaskUtil.maskValue("DraftNumber",appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }
    }

    public void submitDateMohStep(BaseProcessClass bpc) throws IOException {
        StringBuilder url = new StringBuilder();
        url.append(InboxConst.URL_HTTPS)
                .append(bpc.request.getServerName())
                .append(InboxConst.URL_MAIN_WEB_MODULE+"IaisSubmissionData").append("?selfAssessmentGuide=true");
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

    public void doAmenfLicStep(BaseProcessClass bpc) throws IOException {
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
                StringBuilder url2 = new StringBuilder();
                url2.append(InboxConst.URL_HTTPS)
                        .append(bpc.request.getServerName())
                        .append(InboxConst.URL_LICENCE_WEB_MODULE + "MohRfcPersonnelList/initPsnEditInfo")
                        .append("?personnelNo=")
                        .append(MaskUtil.maskValue("personnelNo", id));
                String tokenUrl2 = RedirectUtil.appendCsrfGuardToken(url2.toString(), bpc.request);
                IaisEGPHelper.redirectUrl(bpc.response, tokenUrl2);
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
                    StringBuilder url3 = new StringBuilder();
                    url3.append(InboxConst.URL_HTTPS)
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
                    String tokenUrl2 = RedirectUtil.appendCsrfGuardToken(url3.toString(), bpc.request);
                    IaisEGPHelper.redirectUrl(bpc.response, tokenUrl2);
                }
                else{
                    StringBuilder url = new StringBuilder();
                    url.append(InboxConst.URL_HTTPS)
                            .append(bpc.request.getServerName())
                            .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohRequestForChange")
                            .append("?licenceId=")
                            .append(MaskUtil.maskValue("licenceId",licIdValue));
                    String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
                    IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
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
        String additional;
        if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(item.getPremisesType())) {
            additional = item.getHciName() + item.getVehicleNo();
        } else {
            additional = item.getHciName();
        }
        return MiscUtil.getPremisesKey(additional, item.getPostalCode(), item.getBlkNo(), item.getStreetName(),
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
                if(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(serviceType)){
                    baseDtos.add(appSvcRelatedInfoDto);
                }else if (ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED.equals(serviceType)){
                    specDtos.add(appSvcRelatedInfoDto);
                }
            }

            if(!IaisCommonUtils.isEmpty(baseDtos)){
                baseDtos.sort((h1,h2)->h1.getServiceName().compareTo(h2.getServiceName()));
                newAppSvcDto.addAll(baseDtos);
            }
            if(!IaisCommonUtils.isEmpty(specDtos)){
                specDtos.sort((h1,h2)->h1.getServiceName().compareTo(h2.getServiceName()));
                newAppSvcDto.addAll(specDtos);
            }
            appSvcRelatedInfoDtos = newAppSvcDto;
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
        List<String> premisesHciList = IaisCommonUtils.genNewArrayList();
        if(premisesDto != null){
            String premisesHciPre = "";
            if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesDto.getPremisesType())){
                premisesHciPre = premisesDto.getHciName() + premisesDto.getPostalCode() + premisesDto.getBlkNo();
            }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesDto.getPremisesType())){
                premisesHciPre = premisesDto.getHciName()+premisesDto.getVehicleNo() + premisesDto.getPostalCode() + premisesDto.getBlkNo();
            }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesDto.getPremisesType())){
                premisesHciPre = premisesDto.getHciName()+premisesDto.getPostalCode() + premisesDto.getBlkNo();
            }else if(ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(premisesDto.getPremisesType())){
                premisesHciPre = premisesDto.getHciName()+premisesDto.getPostalCode() + premisesDto.getBlkNo();
            }
            premisesHciList.add(premisesHciPre + premisesDto.getFloorNo() + premisesDto.getUnitNo());
            List<PremisesOperationalUnitDto> operationalUnitDtos = premisesDto.getPremisesOperationalUnitDtos();
            if(!IaisCommonUtils.isEmpty(operationalUnitDtos)){
                for(PremisesOperationalUnitDto operationalUnitDto:operationalUnitDtos){
                    premisesHciList.add(premisesHciPre + operationalUnitDto.getFloorNo() + operationalUnitDto.getUnitNo());
                }
            }
        }
        return premisesHciList;
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

}
