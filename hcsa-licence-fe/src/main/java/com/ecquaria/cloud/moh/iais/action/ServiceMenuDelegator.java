package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppAlignAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppLicBundleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationSubDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AppAlignLicQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.MenuLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.AppSelectSvcDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.memorypage.PaginationHandler;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.ConfigCommService;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author guyin
 * @date 2019/12/12 20:54
 */
@Slf4j
@Delegator("serviceMenuDelegator")
public class ServiceMenuDelegator {
    private static final String BASE_SERVICE = HcsaConsts.SERVICE_TYPE_BASE;


    private static final String BASE_SERVICE_CHECK_BOX_ATTR = "basechk";
    private static final String BASE_SERVICE_ATTR = "baseService";
    private static final String BASE_SERVICE_ATTR_CHECKED = "baseServiceChecked";
    private static final String SPECIFIED_SERVICE_ATTR = "specifiedService";
    private static final String SPECIFIED_SERVICE_ATTR_CHECKED = "specifiedServiceChecked";
    private static final String VALIDATION_ATTR = "switch_action_type";
    private static final String CRUD_ACTION_ADDITIONAL = "crud_action_additional";

    private static final String CHOOSE_SERVICE= "chooseSvc";
    private static final String CHOOSE_BASE_SVC = "chooseBaseSvc";
    private static final String CHOOSE_ALIGN = "chooseAlign";
    private static final String CHOOSE_LICENCE = "chooseLic";
    private static final String ERROR_ATTR = "err";
    private static final String ERROR_ATTR_LIST = "errList";
    private static final String BACK_ATTR = "back";
    private static final String NEXT = "next";

    public static final String APP_SVC_RELATED_INFO_LIST = HcsaAppConst.APP_SVC_RELATED_INFO_LIST;
    public static final String APP_SELECT_SERVICE = HcsaAppConst.APP_SELECT_SERVICE;
    public static final String APP_LIC_BUNDLE_LIST = HcsaAppConst.APP_LIC_BUNDLE_LIST;
    private static final String HAS_EXISTING_BASE = "hasExistingBase";
    private static final String URL_HTTPS = "https://";
    private static final String ONLY_BASE_SVC = "onlyBaseSvc";
    private static final String APP_ALIGN_LIC = "appAlignLic";
    private static final String BASEANDSPCSVCMAP = "baseAndSpcSvcMap";
    private static final String RETAIN_LIC_PREMISES_LIST =  "retainLicPremisesList";
    private static final String BASE_SVC_PREMISES_MAP = "baseSvcPremisesMap";
    private static final String NO_EXIST_BASE_LIC = "noExistBaseLic";
    private static final String NO_EXIST_BASE_APP = "noExistBaseApp";
    private static final String RELOAD_BASE_SVC_SELECTED = "reloadBaseSvcSelected";
    private static final String LIC_ALIGN_SEARCH_PARAM = "licAlignSearchParam";
    private static final String LIC_ALIGN_SEARCH_RESULT = "licAlignSearchResult";
    private static final String LICENCE = "licence";
    private static final String ACTION = "action";
    private static final String LICENCE_JUDGE = "licenceJudge";
    private static final String BUNDLE_ACH_OR_MS = "bundleAchOrMs";
    private static final String LICPAGEDIV_SESSION_ATTR = "licPagDiv__SessionAttr";
    private static final String APPPAGDIV_SESSION_ATTR = "appPagDiv__SessionAttr";
    private static final String NOT_CONTAINED = "notContained";
    private static final String FIRST = "first";
    private static final String CHOOSE_BASE_ERR = "chooseBaseErr";
    private static final String LICENSEE_ID = "licenseeId";

    public static final String DRAFT_NUMBER = HcsaAppConst.DRAFT_NUMBER;


    List<HcsaServiceDto> allbaseService;
    @Autowired
    private ServiceConfigService serviceConfigService;
    @Autowired
    private LicenceViewService licenceViewService;
    @Autowired
    private ApplicationFeClient applicationFeClient;
    @Autowired
    private AppSubmissionService appSubmissionService;
    @Autowired
    private RequestForChangeService requestForChangeService;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private AppCommService appCommService;
    @Autowired
    private ConfigCommService configCommService;

    public void doStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the  doStart start 1...."));
        ParamUtil.setSessionAttr(bpc.request, LICENCE, null);
        ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR_CHECKED, null);
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR_CHECKED, null);
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR, null);
        ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR, null);
        ParamUtil.setSessionAttr(bpc.request,DRAFT_NUMBER,null);
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.SELECT_DRAFT_NO,null);
        ParamUtil.setSessionAttr(bpc.request,APP_SELECT_SERVICE,null);
        ParamUtil.setSessionAttr(bpc.request,ONLY_BASE_SVC,null);
        ParamUtil.setSessionAttr(bpc.request,APP_ALIGN_LIC, null);
        ParamUtil.setSessionAttr(bpc.request,BASEANDSPCSVCMAP, null);
        ParamUtil.setSessionAttr(bpc.request,RETAIN_LIC_PREMISES_LIST, null);
        ParamUtil.setSessionAttr(bpc.request,RELOAD_BASE_SVC_SELECTED, null);
        ParamUtil.setSessionAttr(bpc.request,LIC_ALIGN_SEARCH_PARAM,null);
        ParamUtil.setSessionAttr(bpc.request,LIC_ALIGN_SEARCH_RESULT,null);
        ParamUtil.setSessionAttr(bpc.request, APP_SVC_RELATED_INFO_LIST, null);
        ParamUtil.setSessionAttr(bpc.request, APP_LIC_BUNDLE_LIST, null);
        ParamUtil.setSessionAttr(bpc.request,BASE_SVC_PREMISES_MAP,null);
        HcsaServiceCacheHelper.flushServiceMapping();
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_NEW_APPLICATION, AuditTrailConsts.FUNCTION_NEW_APPLICATION);
        log.debug(StringUtil.changeForLog("the  doStart end 1...."));
    }

    public void beforeJump(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the  before jump start 1...."));
    }

    public void validation(BaseProcessClass bpc){

    }

    public void baseValidation(BaseProcessClass bpc){
        String action = ParamUtil.getString(bpc.request, ACTION);
        String[] basechks = ParamUtil.getStrings(bpc.request, BASE_SERVICE_CHECK_BOX_ATTR);
        if(BACK_ATTR.equals(action)){
            ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, BACK_ATTR);
        }else{
            if(basechks == null){
                //no choose all
                String err = MessageUtil.getMessageDesc("NEW_ERR0009");
                ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err);
                ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, ERROR_ATTR);
            }else{
                List<String> basechkslist = IaisCommonUtils.genNewArrayList();
                Collections.addAll(basechkslist,basechks);
                if(basechkslist.size() == 1){
                    ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR,NEXT);
                }else{
                    String err = "Base service can choose one only.";
                    ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err);
                    ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR,ERROR_ATTR);
                }
                ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR_CHECKED, (Serializable) basechkslist);
            }
        }

    }


    public void chooseLicence(BaseProcessClass bpc){
        List<String> basechkslist = (List<String>)ParamUtil.getSessionAttr(bpc.request, BASE_SERVICE_ATTR_CHECKED);
        List<String> basechksNamelist ;
        basechksNamelist = BaseIdToName(basechkslist);
        SearchResult searchResult = getLicense(bpc,basechksNamelist);
        ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, LICENCE);
        ParamUtil.setRequestAttr(bpc.request, LICENCE, searchResult);
        ParamUtil.setSessionAttr(bpc.request, "baseName", basechksNamelist.get(0));
    }

    public void licenseValidation(BaseProcessClass bpc){
        String licenceJudgeD=(String)bpc.request.getSession().getAttribute(LICENCE_JUDGE);
        List<String> licenceD=(List<String>)bpc.request.getSession().getAttribute(LICENCE);
        String action = ParamUtil.getString(bpc.request, ACTION);
        String licenceJudge = ParamUtil.getString(bpc.request, LICENCE_JUDGE);
        String[] licence = ParamUtil.getStrings(bpc.request, LICENCE);
        if(!StringUtil.isEmpty(licenceD)){
            licence=new String[licenceD.size()];
            for(int i=0;i<licenceD.size();i++){
                licence[i]=licenceD.get(i);
            }
        }
        if(licenceJudgeD!=null){
            licenceJudge=licenceJudgeD;
        }
        if(BACK_ATTR.equals(action)){
            ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, BACK_ATTR);
        }else {
            String err010 = MessageUtil.getMessageDesc("NEW_ERR0010");
            if(licenceJudge == null){
                ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, ERROR_ATTR);

                ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err010);
                ParamUtil.setRequestAttr(bpc.request, ACTION,"err");
            }else if("1".equals(licenceJudge)) {
                if(licence == null){
                    ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, ERROR_ATTR);
                    ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err010);
                    ParamUtil.setRequestAttr(bpc.request, ACTION,"err");
                }else{
                    List<String> licenceList = IaisCommonUtils.genNewArrayList();
                    Collections.addAll(licenceList,licence);
                    ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, NEXT);
                    ParamUtil.setSessionAttr(bpc.request, LICENCE, (Serializable) licenceList);
                }

            } else {
                ParamUtil.setSessionAttr(bpc.request, LICENCE, null);
                ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, NEXT);
            }
        }
        String  step =(String) bpc.request.getAttribute(VALIDATION_ATTR);
        ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, step);
        if(NEXT.equals(step)){
            ParamUtil.setSessionAttr(bpc.request,LICENCE_JUDGE,licenceJudge);
            checkAction(null ,bpc.request);
        }
    }

    public void prepareData(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("prepareData start ..."));
        String action = (String) ParamUtil.getRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE);
        if(StringUtil.isEmpty(action)){
            log.info(StringUtil.changeForLog("action is empty"));
            action = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
            if(StringUtil.isEmpty(action)){
                action = CHOOSE_SERVICE;
            }
        }

        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,action);
        log.info(StringUtil.changeForLog("prepareData end ..."));
    }


    public void preChooseSvc(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("prepare choose svc start ..."));
        List<HcsaServiceDto> hcsaServiceDtoList = serviceConfigService.getServicesInActive();
        if (IaisCommonUtils.isEmpty(hcsaServiceDtoList)){
            log.debug("can not find hcsa service list in service menu delegator!");
            return;
        }
        List<String> accessSvcCodes = HcsaServiceCacheHelper.getAccessSvcCodes(bpc.request);
        List<HcsaServiceDto> allbaseService = hcsaServiceDtoList.stream()
                .filter(hcsaServiceDto -> BASE_SERVICE.equals(hcsaServiceDto.getSvcType()))
                .filter(hcsaServiceDto -> accessSvcCodes.contains(hcsaServiceDto.getSvcCode()))
                .collect(Collectors.toList());
        //sort
        allbaseService.sort(Comparator.comparing(HcsaServiceDto::getSvcName));
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR, (Serializable) allbaseService);
        AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
        appSelectSvcDto.setChooseBaseSvc(false);
        ParamUtil.setSessionAttr(bpc.request,APP_SELECT_SERVICE,appSelectSvcDto);
        log.info(StringUtil.changeForLog("prepare data end ..."));
        log.info(StringUtil.changeForLog("prepare choose svc end ..."));
    }

    public void preChooseBaseSvc(BaseProcessClass bpc) {
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
        List<ApplicationDto> applicationDtoList = appSubmissionService.getApplicationsByLicenseeId(licenseeId);
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
        List<LicenceDto> licenceDtoList = licenceViewService.getApproveLicenceDtoByLicenseeId(licenseeId);
        if (IaisCommonUtils.isNotEmpty(licenceDtoList)){
            boolean exist = licenceDtoList.stream().anyMatch(item -> serviceNameList.contains(item.getSvcName()));
            if (exist&&!bundleAchOrMs){
                existPendMS=true;
            }
        }
        ParamUtil.setRequestAttr(bpc.request,"existPendMS",existPendMS);
        log.info(StringUtil.changeForLog("prepare choose base svc end ..."));
    }

    public void preChooseAlign(BaseProcessClass bpc){
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

    public void preChooseLic(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("prepare choose licence start ..."));
        PaginationHandler<MenuLicenceDto> paginationHandler = (PaginationHandler<MenuLicenceDto>) ParamUtil.getSessionAttr(bpc.request,LICPAGEDIV_SESSION_ATTR);
        AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
        if(appSelectSvcDto.isInitPagHandler()){
            List<MenuLicenceDto> menuLicenceDtos = IaisCommonUtils.genNewArrayList();
            menuLicenceDtos.add(paginationHandler.getDisplayData().get(0).getRecord());
            paginationHandler.setDefaultChecked(menuLicenceDtos);
        }
        paginationHandler.preLoadingPage();
        log.info(StringUtil.changeForLog("prepare choose licence end ..."));
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
        String err;
        String nextstep;
        List<String> basecheckedlist = IaisCommonUtils.genNewArrayList();
        List<HcsaServiceDto> baseSvcSort = IaisCommonUtils.genNewArrayList();
        List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList =  serviceConfigService.getActiveSvcCorrelation();
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
                Set<String> premisesTypeList = serviceConfigService.getAppGrpPremisesTypeBySvcId(chkSvcIdList);
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
                        boolean canCreateEasOrMts = appSubmissionService.canApplyEasOrMts(licenseeId,hcsaServiceDtos);
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
            List<ApplicationSubDraftDto> applicationSubDraftDtos = appSubmissionService.getDraftListBySvcCodeAndStatus(svcCodeList,ApplicationConsts.DRAFT_STATUS_PENDING_PAYMENT,licenseeId,ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
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
            newLicensee =  appSubmissionService.isNewLicensee(licenseeId);
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
                    Collections.addAll(chkBase,basechks);
                    allBaseId.removeAll(chkBase);
                    //get prem type intersection
                    Set<String> premisesTypeList = serviceConfigService.getAppGrpPremisesTypeBySvcId(chkBase);
                    int alignMinExpiryMonth = systemParamConfig.getAlignMinExpiryMonth();
                    log.debug(StringUtil.changeForLog("alignMinExpiryMonth:::"+alignMinExpiryMonth));
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
        appSvcRelatedInfoDtos = getAppSvcRelatedInfoDtos(baseSvcSort, null);
        ParamUtil.setSessionAttr(bpc.request,APP_SVC_RELATED_INFO_LIST, (Serializable) appSvcRelatedInfoDtos);
        ParamUtil.setSessionAttr(bpc.request,RELOAD_BASE_SVC_SELECTED, null);
        appSelectSvcDto.setAlign(false);
        ParamUtil.setSessionAttr(bpc.request,APP_SELECT_SERVICE,appSelectSvcDto);
        ParamUtil.setSessionAttr(bpc.request,BUNDLE_ACH_OR_MS, bundleAchOrMs);
        //test
        String value = ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE);
        if(NEXT.equals(value)){
            checkAction(CHOOSE_SERVICE, bpc.request);
        }

        log.info(StringUtil.changeForLog("do choose svc end ..."));
    }

    private void initChooseBaseSvc(BaseProcessClass bpc, boolean bundleAchOrMs, List<HcsaServiceDto> allbaseService, List<HcsaServiceDto> baseSvcSort, String licenseeId) {
        boolean noExistBaseLic = true;
        boolean noExistBaseApp = true;
        cleanChooseBaseSvcSession(bpc);
        List<HcsaServiceDto> needContainedSvc =IaisCommonUtils.genNewArrayList();
        Optional<HcsaServiceDto> clbService = allbaseService.stream().filter(s -> AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY.equals(s.getSvcCode())).findAny();
        clbService.ifPresent(needContainedSvc::add);
        Optional<HcsaServiceDto> rdsService = allbaseService.stream().filter(s -> AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES.equals(s.getSvcCode())).findAny();
        rdsService.ifPresent(needContainedSvc::add);
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
        List<HcsaServiceDto> hcsaServiceByNames = configCommService.getHcsaServiceByNames(svcNameList);
        List<String> collect = hcsaServiceByNames.stream().map(HcsaServiceDto::getId).collect(Collectors.toList());
        List<AppAlignAppQueryDto> bundleApp = appSubmissionService.getAppAlignAppQueryDto(licenseeId, collect);
        for (AppAlignAppQueryDto appAlignAppQueryDto : bundleApp) {
            appAlignAppQueryDto.setSvcName(HcsaServiceCacheHelper.getServiceById(appAlignAppQueryDto.getSvcId()).getSvcName());
        }
        //pagination
        if(IaisCommonUtils.isNotEmpty(bundleLic)&&bundleLic.size()>1){
            noExistBaseLic=false;
            if (!bundleAchOrMs){
                bundleLic.remove(0);
            }
            if (bundleAchOrMs&&bundleLic.size()>1){
                initBundlePaginationHandler(bundleLic);
            }else if (!bundleAchOrMs&&bundleLic.size()>0){
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
        ParamUtil.clearSession(bpc.request,"autoBundle");
        ParamUtil.clearSession(bpc.request,NOT_CONTAINED+AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY,NOT_CONTAINED+AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES);
        ParamUtil.clearSession(bpc.request,LICPAGEDIV_SESSION_ATTR,APPPAGDIV_SESSION_ATTR);
    }

    /**
     * Do the bundle
     *
     * @param bpc
     */
    public void doChooseBaseSvc(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("do choose base svc start ..."));
        AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
        List<HcsaServiceDto> baseSvcDtoList = appSelectSvcDto.getBaseSvcDtoList();
        boolean noExistBaseLic = (boolean) ParamUtil.getSessionAttr(bpc.request, NO_EXIST_BASE_LIC);
        boolean noExistBaseApp = (boolean) ParamUtil.getSessionAttr(bpc.request, NO_EXIST_BASE_APP);
        boolean bundleAchOrMs = (boolean) ParamUtil.getSessionAttr(bpc.request, BUNDLE_ACH_OR_MS);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos;
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
                List<AppLicBundleDto> licBundleDtos = appSubmissionService.getBundleList(checkData.getLicenceId(), true);
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
                AppAlignLicQueryDto autoBundleDto= (AppAlignLicQueryDto) ParamUtil.getSessionAttr(bpc.request, "autoBundle");
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
                    erroMsg=MessageUtil.getMessageDesc("GENERAL_ERR0006");
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
                List<AppLicBundleDto> licBundleDtos = appSubmissionService.getBundleList(checkData.getApplicationNo(), false);
                if (IaisCommonUtils.isNotEmpty(licBundleDtos)){
                    appLicBundleDto.setBoundCode(licBundleDtos.get(0).getBoundCode());
                }
                appLicBundleDtoList.add(appLicBundleDto);
                applicationNo=checkData.getApplicationNo();
                AppAlignAppQueryDto autoBundleDto= (AppAlignAppQueryDto) ParamUtil.getSessionAttr(bpc.request, "autoBundle");
                if (autoBundleDto!=null){
                    HcsaServiceDto autoServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(autoBundleDto.getSvcName());
                    AppLicBundleDto autoDto=new AppLicBundleDto();
                    autoDto.setSvcName(autoServiceDto.getSvcName());
                    autoDto.setSvcCode(autoServiceDto.getSvcCode());
                    autoDto.setSvcId(autoServiceDto.getId());
                    autoDto.setApplicationNo(autoBundleDto.getApplicationNo());
                    autoDto.setPremisesId(autoBundleDto.getPremisesId());
                    autoDto.setPremisesType(autoBundleDto.getPremisesType());
                    autoDto.setBoundCode(appLicBundleDtoList.get(0).getBoundCode());
                    autoDto.setLicOrApp(true);
                    appLicBundleDtoList.add(autoDto);
                }
            }
            appSelectSvcDto.setInitPagHandler(false);
        }
        appSvcRelatedInfoDtos = getAppSvcRelatedInfoDtos(baseSvcDtoList, appLicBundleDtoList);
        ParamUtil.setSessionAttr(bpc.request,APP_SVC_RELATED_INFO_LIST, (Serializable) appSvcRelatedInfoDtos);
        ParamUtil.setSessionAttr(bpc.request, APP_LIC_BUNDLE_LIST, (Serializable) appLicBundleDtoList);
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
                List<AppLicBundleDto> licBundleDtos = appSubmissionService.getBundleList(licenceId, true);
                if (IaisCommonUtils.isNotEmpty(licBundleDtos)){
                    erroMsg=MessageUtil.getMessageDesc(IaisEGPConstant.ERR_BINGING_MAX);
                }
            } else if (!noExistBaseApp && StringUtil.isNotEmpty(applicationNo)) {
                List<AppLicBundleDto> licBundleDtos = appSubmissionService.getBundleList(applicationNo, false);
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
            List<ApplicationSubDraftDto> applicationSubDraftDtos = appSubmissionService.getDraftListBySvcCodeAndStatus(svcCodeList,ApplicationConsts.DRAFT_STATUS_PENDING_PAYMENT,licenseeId,ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
            if(!IaisCommonUtils.isEmpty(applicationSubDraftDtos)){
                erroMsg = MessageUtil.getMessageDesc("NEW_ERR0023");
            }
        }
        if(StringUtil.isEmpty(erroMsg)){
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
            checkAction(CHOOSE_BASE_SVC, bpc.request);
        }else{
            ParamUtil.setRequestAttr(bpc.request,CHOOSE_BASE_ERR,erroMsg);
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,CHOOSE_BASE_SVC);
        }
        appSelectSvcDto.setChooseBaseSvc(true);
        ParamUtil.setSessionAttr(bpc.request,APP_SELECT_SERVICE,appSelectSvcDto);
        log.info(StringUtil.changeForLog("do choose base svc end ..."));
    }

    private List<AppSvcRelatedInfoDto> getAppSvcRelatedInfoDtos(List<HcsaServiceDto> baseSvcDtoList,
            List<AppLicBundleDto> appLicBundleDtoList) {
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = IaisCommonUtils.genNewArrayList();
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
        appSvcRelatedInfoDtos = ApplicationHelper.sortAppSvcRelatDto(appSvcRelatedInfoDtos);
        return appSvcRelatedInfoDtos;
    }

    public void doChooseAlign(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("do choose align start ..."));
        String nextStep = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        //dont have existing base
        String isAlign = ParamUtil.getString(bpc.request,"isAlign");
        AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
        appSelectSvcDto.setAlign(AppConsts.YES.equals(isAlign));

        String additional = ParamUtil.getString(bpc.request,CRUD_ACTION_ADDITIONAL);
        if(BACK_ATTR.equals(additional)){
            return;
        }

        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, NEXT);
        checkAction(CHOOSE_ALIGN, bpc.request);
        appSelectSvcDto.setAlignPage(true);

        ParamUtil.setSessionAttr(bpc.request,APP_SELECT_SERVICE,appSelectSvcDto);
        log.info(StringUtil.changeForLog("do choose align end ..."));
    }

    public void doChooseLic(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("do choose lic start ..."));
        String additional = ParamUtil.getString(bpc.request,CRUD_ACTION_ADDITIONAL);
        AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
        PaginationHandler<MenuLicenceDto> paginationHandler = (PaginationHandler<MenuLicenceDto>) ParamUtil.getSessionAttr(bpc.request,LICPAGEDIV_SESSION_ATTR);
        paginationHandler.keepCurrentPageChecked();
        List<MenuLicenceDto> menuLicenceDtos = paginationHandler.getAllCheckedData();
        MenuLicenceDto menuLicenceDto = new MenuLicenceDto();
        if(menuLicenceDtos != null && !menuLicenceDtos.isEmpty()){
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
        List<AppLicBundleDto> appLicBundleDtoList=IaisCommonUtils.genNewArrayList();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = IaisCommonUtils.genNewArrayList();
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
        ParamUtil.setSessionAttr(bpc.request, APP_LIC_BUNDLE_LIST, (Serializable) appLicBundleDtoList);
        ParamUtil.setSessionAttr(bpc.request,ServiceMenuDelegator.APP_SVC_RELATED_INFO_LIST, (Serializable) appSvcRelatedInfoDtos);
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
        checkAction(CHOOSE_LICENCE, bpc.request);
        appSelectSvcDto.setLicPage(true);
        ParamUtil.setSessionAttr(bpc.request,APP_SELECT_SERVICE,appSelectSvcDto);
        log.info(StringUtil.changeForLog("do choose lic end ..."));
    }

    public void controlSwitch(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("control switch start ..."));
        String action = "loading";
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
            appSvcRelatedInfoDtos = ApplicationHelper.sortAppSvcRelatDto(appSvcRelatedInfoDtos);
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
            String urlTo= "https://" + bpc.request.getServerName() +
                    "/main-web/eservice/INTERNET/MohLicenseeCompanyDetail";
            String licenseeurl;
            if("LICT001".equals(licenseeDto.getLicenseeType())){
                licenseeurl = urlTo + "?licenseView=Licensee";
            }else{
                licenseeurl = urlTo + "?licenseView=Solo";
            }

            String authorisedUrl = urlTo + "?licenseView=Authorised";
            String medAlertUrl= urlTo + "?licenseView=MedAlert";
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
            log.debug(StringUtil.changeForLog("control switch service Info:"+JsonUtil.parseToJson(appSvcRelatedInfoDtos)));
        }
        ParamUtil.setRequestAttr(bpc.request,"switch",action);
        log.info(StringUtil.changeForLog("control switch end ..."));
    }

    public void backInbox(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("back inbox start ..."));
        StringBuilder url = new StringBuilder();
        url.append(URL_HTTPS)
                .append(bpc.request.getServerName())
                .append("/main-web/eservice/INTERNET/MohInternetInbox");
        String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);

        log.info(StringUtil.changeForLog("back inbox end ..."));
    }

    public void backChooseSvc(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("back choose svc start ..."));
        AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);

        if(appSelectSvcDto.isBasePage()){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, CHOOSE_BASE_SVC);
        }else if(appSelectSvcDto.isAlignPage()){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, CHOOSE_ALIGN);
        }else if(appSelectSvcDto.isLicPage()){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, CHOOSE_LICENCE);
        }else if(appSelectSvcDto.isChooseBaseSvc()){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, CHOOSE_BASE_SVC);
        }
        //reset
        appSelectSvcDto.setBasePage(false);
        appSelectSvcDto.setAlignPage(false);
        appSelectSvcDto.setLicPage(false);
        appSelectSvcDto.setChooseBaseSvc(false);
        ParamUtil.setSessionAttr(bpc.request,APP_SELECT_SERVICE,appSelectSvcDto);

        log.info(StringUtil.changeForLog("back choose svc end ..."));
    }

    public void doPage(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("doPage start ..."));
        SearchParam searchParamGroup = (SearchParam) ParamUtil.getSessionAttr(bpc.request,LIC_ALIGN_SEARCH_PARAM);
        CrudHelper.doPaging(searchParamGroup,bpc.request);
        log.info(StringUtil.changeForLog("doPage end ..."));
    }

    //=============================================================================
    //private method
    //=============================================================================

    private void checkAction(String type, HttpServletRequest request) {
        getDraft(request);
        String crudActionValue = request.getParameter(IaisEGPConstant.CRUD_ACTION_VALUE);
        String attribute = (String) request.getAttribute(HcsaAppConst.SELECT_DRAFT_NO);
        if ("continue".equals(crudActionValue)) {
            if (!StringUtil.isEmpty(type)) {
                String draftNo  = request.getParameter("draftNo");
                List<String> list=new ArrayList<>(1);
                list.add(draftNo);
                log.info(StringUtil.changeForLog("delete draft start ..."));
                applicationFeClient.deleteDraftNUmber(list);
            }
            ParamUtil.setSessionAttr(request, DRAFT_NUMBER, null);
        } else if ("resume".equals(crudActionValue)) {
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

    private void getDraft(HttpServletRequest request){
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request,AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = loginContext.getLicenseeId();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos=(List<AppSvcRelatedInfoDto>)ParamUtil.getSessionAttr(request,APP_SVC_RELATED_INFO_LIST);
        List<String> licenceList =(List<String>) ParamUtil.getSessionAttr(request, LICENCE);
        List<String> baseServiceIds=IaisCommonUtils.genNewArrayList();
        if(StringUtil.isEmpty(licenceList)){
            baseServiceIds = (List<String>) ParamUtil.getSessionAttr(request, BASE_SERVICE_ATTR_CHECKED);
        }
        List<String> specifiedServiceIds = (List<String>) ParamUtil.getSessionAttr(request, SPECIFIED_SERVICE_ATTR_CHECKED);
        List<String> serviceConfigIds = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(baseServiceIds)){
            serviceConfigIds.addAll(baseServiceIds);
        }
        if(!IaisCommonUtils.isEmpty(specifiedServiceIds)){
            serviceConfigIds.addAll(specifiedServiceIds);
        }
        if(!serviceConfigIds.isEmpty()) {
            List<HcsaServiceDto> hcsaServiceDtosById = serviceConfigService.getHcsaServiceDtosById(serviceConfigIds);
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
            map.put(LICENSEE_ID,licenseeId);
            String entity = applicationFeClient.selectDarft(map).getEntity();
            String newAck001 = MessageUtil.getMessageDesc("NEW_ACK001");
            request.setAttribute("new_ack001",newAck001);
            request.setAttribute(HcsaAppConst.SELECT_DRAFT_NO, entity);
        }
    }

    private List<String> BaseIdToName(List<String> baseId){
        List<String> baseName = IaisCommonUtils.genNewArrayList();
        for (HcsaServiceDto item:allbaseService
        ) {
            for (String id:baseId
            ) {
                if(item.getId().equals(id)){
                    baseName.add(item.getSvcName());
                }
            }

        }
        return baseName;
    }

    public void doBeforStart(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doBeforStart start ...."));

        log.debug(StringUtil.changeForLog("the doBeforStart end ...."));
    }

    public void backAppBefore(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the doBeforStart end ...."));
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
        searchParamGroup.addFilter(LICENSEE_ID,loginContext.getLicenseeId(),true);
        QueryHelp.setMainSql("applicationQuery", "getLicenceBySerName",searchParamGroup);
        return licenceViewService.getMenuLicence(searchParamGroup);
    }

    private List<String> svcIdListTransferNameList(List<String> baseSvcIdList){
        List<String> svcNameList = IaisCommonUtils.genNewArrayList();
        for(String id:baseSvcIdList){
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(id);
            svcNameList.add(hcsaServiceDto.getSvcName());
        }
        return svcNameList;
    }

    private AppSelectSvcDto getAppSelectSvcDto(BaseProcessClass bpc){
        AppSelectSvcDto appSelectSvcDto = (AppSelectSvcDto) ParamUtil.getSessionAttr(bpc.request,APP_SELECT_SERVICE);
        if(appSelectSvcDto == null){
            appSelectSvcDto = new AppSelectSvcDto();
        }
        return appSelectSvcDto;
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
            for(String ignored :excludeChkBase){
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

        searchParam.addFilter(LICENSEE_ID,licenseeId,true);
        QueryHelp.setMainSql("applicationQuery", "getLicenceBySerName",searchParam);
        return licenceViewService.getMenuLicence(searchParam);
    }

    private List<MenuLicenceDto> removePendAndExistPrem(List<String> excludeChkBase,List<MenuLicenceDto> menuLicenceDtos,String licenseeId){
        List<MenuLicenceDto> newAppLicDtos = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(excludeChkBase) && !IaisCommonUtils.isEmpty(menuLicenceDtos) && !StringUtil.isEmpty(licenseeId)){
            menuLicenceDtos = appSubmissionService.setPremAdditionalInfo(menuLicenceDtos);
            List<HcsaServiceDto> hcsaServiceDtos = IaisCommonUtils.genNewArrayList();
            for(String svcId:excludeChkBase){
                HcsaServiceDto svcDto = HcsaServiceCacheHelper.getServiceById(svcId);
                if(svcDto != null){
                    hcsaServiceDtos.add(svcDto);
                }
            }
            List<String> pendAndLicPremHci = appCommService.getHciFromPendAppAndLic(licenseeId, hcsaServiceDtos, null, null);
            for(MenuLicenceDto menuLicenceDto:menuLicenceDtos){
                PremisesDto premisesDto = MiscUtil.transferEntityDto(menuLicenceDto,PremisesDto.class);
                List<String> premisesHciList = ApplicationHelper.genPremisesHciList(premisesDto);
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
            for(String ignored :excludeChkBase){
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
        log.debug(StringUtil.changeForLog("alignMinExpiryMonth::" + alignMinExpiryMonth));
        searchParam.addFilter("alignMinExpiryMonth", alignMinExpiryMonth,true);

        searchParam.addFilter(LICENSEE_ID,licenseeId,true);
        QueryHelp.setMainSql("applicationQuery", "getLicenceBySvcName",searchParam);
        return licenceViewService.getBundleLicence(searchParam);
    }

}
