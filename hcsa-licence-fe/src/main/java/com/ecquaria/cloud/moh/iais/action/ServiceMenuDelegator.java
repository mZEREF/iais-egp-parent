package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationSubDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AppAlignLicQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.MenuLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
    private static final String BASE_SERVICE = "SVTP001";
    private static final String SPECIFIED_SERVICE = "SVTP003";


    private static final String BASE_SERVICE_CHECK_BOX_ATTR = "basechk";
    private static final String SPECIFIED_SERVICE_CHECK_BOX_ATTR = "sepcifiedchk";
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

    private static final String SERVIC_STEP = "serviceStep";
    public static final String APP_SELECT_SERVICE = "appSelectSvc";
    private static final String HAS_EXISTING_BASE = "hasExistingBase";
    private static final String URL_HTTPS = "https://";
    private static final String ONLY_BASE_SVC = "onlyBaseSvc";
    private static final String APP_ALIGN_LIC = "appAlignLic";
    private static final String BASEANDSPCSVCMAP = "baseAndSpcSvcMap";
    private static final String RETAIN_LIC_PREMISES_LIST =  "retainLicPremisesList";
    private static final String BASE_SVC_PREMISES_MAP = "baseSvcPremisesMap";
    private static final String NO_EXIST_BASE_LIC = "noExistBaseLic";
//    private static final String BASE_SERVICE_SORT = "baseServiceSort";
//    private static final String SPECIFIED_SERVICE_SORT = "specifiedServiceSort";
    public static final String APP_SVC_RELATED_INFO_LIST = "appSvcRelatedInfoList";
    private static final String RELOAD_BASE_SVC_SELECTED = "reloadBaseSvcSelected";
    private static final String BASE_LIC_PREMISES_MAP = "baseLicPremisesMap";
    private static final String LIC_ALIGN_SEARCH_PARAM = "licAlignSearchParam";
    private static final String LIC_ALIGN_SEARCH_RESULT = "licAlignSearchResult";

    public static final String DRAFT_NUMBER = HcsaAppConst.DRAFT_NUMBER;

    List<HcsaServiceDto> allbaseService;
    //List<HcsaServiceDto> allspecifiedService;
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

    public void doStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the  doStart start 1...."));
        ParamUtil.setSessionAttr(bpc.request, "licence", null);
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
        ParamUtil.setSessionAttr(bpc.request,APP_SVC_RELATED_INFO_LIST,null);
        ParamUtil.setSessionAttr(bpc.request,BASE_SVC_PREMISES_MAP,null);
        HcsaServiceCacheHelper.flushServiceMapping();
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_NEW_APPLICATION, AuditTrailConsts.FUNCTION_NEW_APPLICATION);
        log.debug(StringUtil.changeForLog("the  doStart end 1...."));
    }

    public void beforeJump(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the  before jump start 1...."));
    }

    public void validation(BaseProcessClass bpc){
//

    }

//    public void chooseBase(BaseProcessClass bpc){
//        //get correlatrion base service
//        List<String> speString = (List<String>)ParamUtil.getSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR_CHECKED);
//        List<HcsaServiceDto> hcsaServiceDtosMap = getBaseInSpe(speString);
//        Map<String ,HcsaServiceDto> specifiedName = IaisCommonUtils.genNewHashMap();
//        for (HcsaServiceDto item:allspecifiedService
//        ) {
//            specifiedName.put(item.getId(),item);
//        }
//
//        ParamUtil.setRequestAttr(bpc.request, SPECIFIED_SERVICE_ATTR, (Serializable) specifiedName);
//        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR, (Serializable) hcsaServiceDtosMap);
//        ParamUtil.setRequestAttr(bpc.request, "baseInSpe", (Serializable) hcsaServiceDtosMap);
//    }

    public void baseValidation(BaseProcessClass bpc){
        String action = ParamUtil.getString(bpc.request, "action");
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
                for (String item:basechks
                ) {
                    basechkslist.add(item);
                }
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
        List<String> basechksNamelist = IaisCommonUtils.genNewArrayList();
        basechksNamelist = BaseIdToName(basechkslist);
        SearchResult searchResult = getLicense(bpc,basechksNamelist);
        ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, "licence");
        ParamUtil.setRequestAttr(bpc.request, "licence", searchResult);
        ParamUtil.setSessionAttr(bpc.request, "baseName", basechksNamelist.get(0));
    }

    public void licenseValidation(BaseProcessClass bpc){
        String licenceJudgeD=(String)bpc.request.getSession().getAttribute("licenceJudge");
        List<String> licenceD=(List<String>)bpc.request.getSession().getAttribute("licence");
        String action = ParamUtil.getString(bpc.request, "action");
        String licenceJudge = ParamUtil.getString(bpc.request, "licenceJudge");
        String[] licence = ParamUtil.getStrings(bpc.request, "licence");
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
                ParamUtil.setRequestAttr(bpc.request, "action","err");
            }else if("1".equals(licenceJudge)) {
                if(licence == null){
                    ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, ERROR_ATTR);
                    ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err010);
                    ParamUtil.setRequestAttr(bpc.request, "action","err");
                }else{
                    List<String> licenceList = IaisCommonUtils.genNewArrayList();
                    for (String item : licence
                    ) {
                        licenceList.add(item);
                    }
                    ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, NEXT);
                    ParamUtil.setSessionAttr(bpc.request, "licence", (Serializable) licenceList);
                }

            } else {
                ParamUtil.setSessionAttr(bpc.request, "licence", null);
                ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, NEXT);
            }
        }
        String  step =(String) bpc.request.getAttribute(VALIDATION_ATTR);
        ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, step);
        if(NEXT.equals(step)){
            ParamUtil.setSessionAttr(bpc.request,"licenceJudge",licenceJudge);
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
        List<HcsaServiceDto> allspecifiedService = hcsaServiceDtoList.stream()
                .filter(hcsaServiceDto -> SPECIFIED_SERVICE.equals(hcsaServiceDto.getSvcType()))
                .filter(hcsaServiceDto -> accessSvcCodes.contains(hcsaServiceDto.getSvcCode()))
                .collect(Collectors.toList());
        //sort
        allbaseService.sort(Comparator.comparing(HcsaServiceDto::getSvcName));
        allspecifiedService.sort(Comparator.comparing(HcsaServiceDto::getSvcName));
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR, (Serializable) allbaseService);
        ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR, (Serializable) allspecifiedService);
        AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
        appSelectSvcDto.setChooseBaseSvc(false);
        ParamUtil.setSessionAttr(bpc.request,APP_SELECT_SERVICE,appSelectSvcDto);
        log.info(StringUtil.changeForLog("prepare data end ..."));
        log.info(StringUtil.changeForLog("prepare choose svc end ..."));
    }

    public void preChooseBaseSvc(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("prepare choose base svc start ..."));
        boolean noExistBaseLic = false;
        //specSvcCode,baseSvcDtoList
        Map<String,List<HcsaServiceDto>> baseAndSpcSvcMap = IaisCommonUtils.genNewHashMap();
        //get correlatrion base service
        AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
        List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList =  serviceConfigService.getActiveSvcCorrelation();
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
        }
        String licenseeId = "";
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
        if(loginContext != null){
            licenseeId = loginContext.getLicenseeId();
        }
        List<String> allChkSvcIdList = transferToList(allChkSvcIds);
        Set<String> premisesTypeList = serviceConfigService.getAppGrpPremisesTypeBySvcId(allChkSvcIdList);
        log.debug("premises Type size {}",premisesTypeList.size());
        List<AppAlignLicQueryDto> appAlignLicQueryDtos = appSubmissionService.getAppAlignLicQueryDto(licenseeId,svcNameList,transferToList(premisesTypeList));
        List<String> pendAndLicPremHci = appCommService.getHciFromPendAppAndLic(licenseeId,pendAndLicPremSvc);
        //remove item when same svc and same premises(hci)
        List<AppAlignLicQueryDto> newAppAlignLicQueryDtos = IaisCommonUtils.genNewArrayList();
        for(AppAlignLicQueryDto appAlignLicQueryDto:appAlignLicQueryDtos){
            boolean pendPremOrExistLic = false;
            PremisesDto premisesDto = MiscUtil.transferEntityDto(appAlignLicQueryDto,PremisesDto.class);
            List<String> premisesHciList = ApplicationHelper.genPremisesHciList(premisesDto);
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

    public void preChooseAlign(BaseProcessClass bpc){
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

    public void preChooseLic(BaseProcessClass bpc){
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

    public void doChooseService(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("do choose svc start ..."));
        boolean onlyBaseSvc = false;
        ParamUtil.setSessionAttr(bpc.request, DRAFT_NUMBER, null);
        ParamUtil.setSessionAttr(bpc.request,ONLY_BASE_SVC,onlyBaseSvc);
        String additional = ParamUtil.getString(bpc.request,CRUD_ACTION_ADDITIONAL);
        if(BACK_ATTR.equals(additional)){
            return;
        }
        List<HcsaServiceDto> allbaseService = getAllBaseService(bpc);
        // List<HcsaServiceDto> allspecifiedService = getAllSpeService(bpc);
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
        List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList =  serviceConfigService.getActiveSvcCorrelation();
        log.info(StringUtil.changeForLog("hcsaServiceCorrelationDtoList size:"+hcsaServiceCorrelationDtoList.size()));
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = "";
        if(loginContext!=null){
            licenseeId  = loginContext.getLicenseeId();
        }
        if(basechks == null){
            log.info(StringUtil.changeForLog("basechks is null ..."));
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
                    //set audit
                    Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
                    errorMap.put(ERROR_ATTR,err);
                    WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                }else{
                    //to step2
                    nextstep = CHOOSE_BASE_SVC;
                }
            }else{
                //no spe err
                nextstep = currentPage;
                err = MessageUtil.getMessageDesc("NEW_ERR0001");
                ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err);
                //set audit
                Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
                errorMap.put(ERROR_ATTR,err);
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            }
        }else{
            log.info(StringUtil.changeForLog("basechks is not null ..."));
            for (String item:basechks) {
                basecheckedlist.add(item);
                baseSvcSort.add(HcsaServiceCacheHelper.getServiceById(item));
            }
            if(sepcifiedchk != null){
                log.info(StringUtil.changeForLog("sepcifiedchk is not null ..."));
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
                    //set audit
                    Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
                    errorMap.put(ERROR_ATTR_LIST,JsonUtil.parseToJson(errMsgList));
                    WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
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
        //
        if(!currentPage.equals(nextstep)){
            List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
            for(HcsaServiceDto hcsaServiceDto:baseSvcSort){
                svcCodeList.add(hcsaServiceDto.getSvcCode());
            }
            for(HcsaServiceDto hcsaServiceDto:speSvcSort){
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

//        appSelectSvcDto.setBaseSvcIds(basecheckedlist);
//        appSelectSvcDto.setSpecifiedSvcIds(sepcifiedcheckedlist);
        appSelectSvcDto.setBaseSvcDtoList(baseSvcSort);
        appSelectSvcDto.setSpeSvcDtoList(speSvcSort);
        ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR_CHECKED, (Serializable) sepcifiedcheckedlist);
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR_CHECKED, (Serializable) basecheckedlist);

        //control switch
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = null;
        if(!currentPage.equals(nextstep)){
            boolean newLicensee  = true;
            //note: As long as you select the specified service, you don't go and select align
            newLicensee =  appSubmissionService.isNewLicensee(licenseeId);
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
                            if(ApplicationConsts.SERVICE_TYPE_SPECIFIED.equals(hcsaServiceDto.getSvcType())){
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
                    /*if(basechks.length == 1){
                        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
                    }else{
                        nextstep = CHOOSE_ALIGN;
                    }*/
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
                    Set<String> premisesTypeList = serviceConfigService.getAppGrpPremisesTypeBySvcId(chkBase);
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
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, nextstep);
        //reset
        ParamUtil.setSessionAttr(bpc.request,APP_SVC_RELATED_INFO_LIST, (Serializable) appSvcRelatedInfoDtos);
        ParamUtil.setSessionAttr(bpc.request,RELOAD_BASE_SVC_SELECTED, null);
        appSelectSvcDto.setAlign(false);
        ParamUtil.setSessionAttr(bpc.request,APP_SELECT_SERVICE,appSelectSvcDto);
        //test
        String value = ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE);
        if(NEXT.equals(value)){
            checkAction(CHOOSE_SERVICE, bpc.request);
        }

        log.info(StringUtil.changeForLog("do choose svc end ..."));
    }

    public void doChooseBaseSvc(BaseProcessClass bpc){
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
                            String premHci = ApplicationHelper.getPremisesHci(appAlignLicQueryDto);
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
            appSvcRelatedInfoDtos = ApplicationHelper.sortAppSvcRelatDto(appSvcRelatedInfoDtos);
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
        String err007 = MessageUtil.getMessageDesc("NEW_ERR0007");
        if(premHcisLength > 1){
            //choose existing premises not same
            erroMsg = err007;
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
                    erroMsg = err007;
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
            List<ApplicationSubDraftDto> applicationSubDraftDtos = appSubmissionService.getDraftListBySvcCodeAndStatus(svcCodeList,ApplicationConsts.DRAFT_STATUS_PENDING_PAYMENT,licenseeId,ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
            if(!IaisCommonUtils.isEmpty(applicationSubDraftDtos)){
                erroMsg = MessageUtil.getMessageDesc("NEW_ERR0023");
            }
        }


        if(StringUtil.isEmpty(erroMsg)){
            //choose existing
            if(chooseExist){
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
                String nextstep = ParamUtil.getString(bpc.request,CRUD_ACTION_ADDITIONAL);
                if(NEXT.equals(nextstep)){
                    checkAction(CHOOSE_BASE_SVC, bpc.request);
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
                    /* if(svcCodeList.size()>1){
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
                            if(ApplicationConsts.SERVICE_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())){
                                chkBase.add(appSvcRelatedInfoDto.getServiceId());
                            }
                        }
                        List<String> accessSvcCodes = HcsaServiceCacheHelper.getAccessSvcCodes(bpc.request);
                        List<String> allBaseId = IaisCommonUtils.genNewArrayList();
                        List<HcsaServiceDto> hcsaServiceDtoList = serviceConfigService.getServicesInActive();
                        List<HcsaServiceDto> allbaseService = hcsaServiceDtoList.stream()
                                .filter(hcsaServiceDto -> BASE_SERVICE.equals(hcsaServiceDto.getSvcType()))
                                .filter(hcsaServiceDto -> accessSvcCodes.contains(hcsaServiceDto.getSvcCode()))
                                .collect(Collectors.toList());
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
                        Set<String> premisesTypeList = serviceConfigService.getAppGrpPremisesTypeBySvcId(allChekSvcIdList);
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
                            appSelectSvcDto.setInitPagHandler(true);
                        }
                        if(IaisCommonUtils.isEmpty(newAppLicDtos) || (!IaisCommonUtils.isEmpty(newAppLicDtos) && newAppLicDtos.size() <= 1)){
                            //107348
//                            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,CHOOSE_ALIGN);
                            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
                        }else{
                            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,CHOOSE_LICENCE);
                        }
                    }
                }
            }

            String switchStep = ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE);
            if(NEXT.equals(switchStep)){
                checkAction(CHOOSE_BASE_SVC, bpc.request);
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
            appSvcRelatedInfoDtos = ApplicationHelper.addOtherSvcInfo(appSvcRelatedInfoDtos,hcsaServiceDtos,true);
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
                appSvcRelatedInfoDto.setBaseServiceId(hcsaServiceDto.getId());
                appSvcRelatedInfoDto.setBaseServiceName(hcsaServiceDto.getSvcName());
                appSvcRelatedInfoDto.setAlignLicenceNo(alignLicenceNo);
                appSvcRelatedInfoDto.setLicPremisesId(licPremiseId);
                appSvcRelatedInfoDtos.add(appSvcRelatedInfoDto);
            }
        }
        ParamUtil.setSessionAttr(bpc.request,APP_SELECT_SERVICE,appSelectSvcDto);
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
            List<HcsaServiceDto> hcsaServiceDtos = appSelectSvcDto.getBaseSvcDtoList();
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = (List<AppSvcRelatedInfoDto>) ParamUtil.getSessionAttr(bpc.request,APP_SVC_RELATED_INFO_LIST);
            if(IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
                appSvcRelatedInfoDtos = IaisCommonUtils.genNewArrayList();
            }
            //add other service
            appSvcRelatedInfoDtos = ApplicationHelper.addOtherSvcInfo(appSvcRelatedInfoDtos,hcsaServiceDtos,false);
            appSvcRelatedInfoDtos = ApplicationHelper.sortAppSvcRelatDto(appSvcRelatedInfoDtos);
            List<String> baseSvcIds = IaisCommonUtils.genNewArrayList();
            List<String> speSvcIds = IaisCommonUtils.genNewArrayList();
            String alignFlag = String.valueOf(System.currentTimeMillis());
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByCode(appSvcRelatedInfoDto.getServiceCode());
                if(hcsaServiceDto != null){
                    if(ApplicationConsts.SERVICE_TYPE_BASE.equals(hcsaServiceDto.getSvcType())){
                        baseSvcIds.add(hcsaServiceDto.getId());
                        if(appSelectSvcDto.isAlign()){
                            appSvcRelatedInfoDto.setAlignFlag(alignFlag);
                        }
                    }else if(ApplicationConsts.SERVICE_TYPE_SPECIFIED.equals(hcsaServiceDto.getSvcType())){
                        speSvcIds.add(hcsaServiceDto.getId());
                    }
                }
            }
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
            LicenseeDto licenseeDto = requestForChangeService.getLicenseeByOrgId(loginContext.getOrgId());
            List<LicenseeKeyApptPersonDto> keyApptPersonDtos =  requestForChangeService.getLicenseeKeyApptPersonDtoListByLicenseeId(licenseeDto.getId());

            //            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            String urlTo= "https://" + bpc.request.getServerName() +
                    "/main-web/eservice/INTERNET/MohLicenseeCompanyDetail";
//            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            String licenseeurl = "";
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
        }
        //reset
        appSelectSvcDto.setBasePage(false);
        appSelectSvcDto.setAlignPage(false);
        appSelectSvcDto.setLicPage(false);
        ParamUtil.setSessionAttr(bpc.request,APP_SELECT_SERVICE,appSelectSvcDto);

        log.info(StringUtil.changeForLog("back choose svc end ..."));
    }

    public void doPage(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("doPage start ..."));
        SearchParam searchParamGroup = (SearchParam) ParamUtil.getSessionAttr(bpc.request,LIC_ALIGN_SEARCH_PARAM);
        CrudHelper.doPaging(searchParamGroup,bpc.request);
        AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
//        appSelectSvcDto.setAlignLicPremId("");
        log.info(StringUtil.changeForLog("doPage end ..."));
    }

    //=============================================================================
    //private method
    //=============================================================================

    private void checkAction(String type, HttpServletRequest request) {
        getDraft(request);
        String crud_action_value = request.getParameter(IaisEGPConstant.CRUD_ACTION_VALUE);
        String attribute = (String) request.getAttribute(HcsaAppConst.SELECT_DRAFT_NO);
        if ("continue".equals(crud_action_value)) {
            if (!StringUtil.isEmpty(type)) {
                String draftNo  = request.getParameter("draftNo");
                List<String> list=new ArrayList<>(1);
                list.add(draftNo);
                log.info(StringUtil.changeForLog("delete draft start ..."));
                applicationFeClient.deleteDraftNUmber(list);
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

    private void getDraft(HttpServletRequest request){
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request,AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = loginContext.getLicenseeId();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos=(List<AppSvcRelatedInfoDto>)ParamUtil.getSessionAttr(request,APP_SVC_RELATED_INFO_LIST);
        List<String> licenceList =(List<String>) ParamUtil.getSessionAttr(request, "licence");
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
            String entity = applicationFeClient.selectDarft(map).getEntity();
            String new_ack001 = MessageUtil.getMessageDesc("NEW_ACK001");
            request.setAttribute("new_ack001",new_ack001);
            request.setAttribute(HcsaAppConst.SELECT_DRAFT_NO, entity);
        }
    }

    private void loadingServiceConfig(BaseProcessClass bpc){
        List<String> serviceConfigIds = IaisCommonUtils.genNewArrayList();
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

    public void showLicensee(BaseProcessClass bpc) {
//        String type = ParamUtil.getRequestString(bpc.request,"crud_action_additional");
//        try {
//            StringBuilder url = new StringBuilder();
//            url.append("/main-web/eservice/INTERNET/MohLicenseeCompanyDetail");
//            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
//            tokenUrl = tokenUrl + "&licenseView="+type;
//            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
//        }catch (Exception e){
//            log.info(e.getMessage());
//        }
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
        QueryHelp.setMainSql("applicationQuery", "getLicenceBySerName",searchParamGroup);
        SearchResult<MenuLicenceDto> searchResult = licenceViewService.getMenuLicence(searchParamGroup);
        return  searchResult;
    }

    private Map<String ,String> necessaryBase(List<String> basechkslist, List<String> sepcifiedchkslist,List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList){
        List<String> chkslistcopy = IaisCommonUtils.genNewArrayList();
        chkslistcopy.addAll(basechkslist);
        Map<String ,String> necessaryBaseServiceList = IaisCommonUtils.genNewHashMap();
        for (String item: sepcifiedchkslist) {
            for (HcsaServiceCorrelationDto dto:hcsaServiceCorrelationDtoList) {
                if(dto.getSpecifiedSvcId().equals(item)){
                    necessaryBaseServiceList.put(dto.getBaseSvcId(),dto.getSpecifiedSvcId());
                }
            }
        }
        //remove chosed base service
        Iterator<String> iter = necessaryBaseServiceList.keySet().iterator();
        while(iter.hasNext()){
            String key = iter.next();
            for (String basechk:chkslistcopy) {
                if (basechk.equals(key)) {
                    iter.remove();
                }
            }
        }
        return necessaryBaseServiceList;

    }

    private Map<String,List<String>> baseRelSpe(List<String> baseSvcIdList,List<String> specSvcIdList,List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList){
        boolean flag = false;
        Map<String,List<String>> result = IaisCommonUtils.genNewHashMap();
        if(!IaisCommonUtils.isEmpty(baseSvcIdList) && !IaisCommonUtils.isEmpty(specSvcIdList)){
            Map<String,List<String>> baseInSpec = IaisCommonUtils.genNewHashMap();
            //specSvcId,baseSvcIdList
            for(HcsaServiceCorrelationDto hcsaServiceCorrelationDto:hcsaServiceCorrelationDtoList){
                String corrSpecSvcId = hcsaServiceCorrelationDto.getSpecifiedSvcId();
                String corrBaseSvcId = hcsaServiceCorrelationDto.getBaseSvcId();
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
                if(!IaisCommonUtils.isEmpty(baseSvcIds)){
                    for(String baseSvcId:baseSvcIdList){
                        if(baseSvcIds.contains(baseSvcId)){
                            currFlag = true;
                            break;
                        }
                    }
                }
                if(!currFlag){
                    break;
                }
                if(i == specSvcIdList.size()-1){
                    flag = true;
                }
            }
            if(flag){
                result = baseInSpec;
            }
        }
        return result;
    }

    private List<HcsaServiceDto> getBaseInSpe(List<String> sepcifiedlist,List<HcsaServiceDto> allbaseService,List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList){
        Map<String ,HcsaServiceDto> baseIdMap = IaisCommonUtils.genNewHashMap();
        for (HcsaServiceDto item:allbaseService
        ) {
            baseIdMap.put(item.getId(),item);
        }

        List<HcsaServiceDto> same = IaisCommonUtils.genNewArrayList();
        for(int i = 0; i < sepcifiedlist.size() ;i++){
            List<HcsaServiceDto> baseListInSpe = IaisCommonUtils.genNewArrayList();
            for (HcsaServiceCorrelationDto dto:hcsaServiceCorrelationDtoList) {
                if(dto.getSpecifiedSvcId().equals(sepcifiedlist.get(i))){
                    baseListInSpe.add(baseIdMap.get(dto.getBaseSvcId()));
                }
            }
            if(i == 0){
                same = baseListInSpe;
            }else{
                same.retainAll(baseListInSpe);
            }
        }

        return same;
    }

    private String getValueOrNull(Map<String, String> map) {
        String obj = null;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            obj = entry.getValue();
            if (obj != null) {
                break;
            }
        }
        return  obj;
    }

    private String getKeyOrNull(Map<String, String> map) {
        String obj = null;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            obj = entry.getKey();
            if (obj != null) {
                break;
            }
        }
        return  obj;
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

    private List<String> setSvcName(List<String> svcNameList, List<HcsaServiceDto> hcsaServiceDtos){
        if(!IaisCommonUtils.isEmpty(hcsaServiceDtos)){
            for(HcsaServiceDto hcsaServiceDto:hcsaServiceDtos){
                svcNameList.add(hcsaServiceDto.getSvcName());
            }
        }
        return svcNameList;
    }

    private List<HcsaServiceDto> getBaseBySpc(List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList,String spcSvcId){
        List<HcsaServiceDto> hcsaServiceDtos = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(hcsaServiceCorrelationDtoList)){
            for(HcsaServiceCorrelationDto corr:hcsaServiceCorrelationDtoList){
                if(corr.getSpecifiedSvcId().equals(spcSvcId)){
                    //cache status is incorrect
//                    HcsaServiceDto serviceById = HcsaServiceCacheHelper.getServiceById(corr.getBaseSvcId());
                    HcsaServiceDto serviceById = serviceConfigService.getServiceDtoById(corr.getBaseSvcId());
                    if(serviceById!=null){
                        if(AppConsts.COMMON_STATUS_ACTIVE.equals(serviceById.getStatus())){
                            hcsaServiceDtos.add(HcsaServiceCacheHelper.getServiceById(corr.getBaseSvcId()));
                        }
                    }
                }
            }
        }
        return  hcsaServiceDtos;
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

    //from internet
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

    private static AppAlignLicQueryDto getAppAlignLicQueryDto(Map<String,List<AppAlignLicQueryDto>> baseLicMap,String svcName,String hciCode){
        AppAlignLicQueryDto result = null;
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
    private static List<String> getAppAlignLicQueryHci(Map<String,List<AppAlignLicQueryDto>> baseSvcPremMap,String svcName){
        List<String> premHcis = IaisCommonUtils.genNewArrayList();
        if(baseSvcPremMap != null && !StringUtil.isEmpty(svcName)){
            List<AppAlignLicQueryDto> appAlignLicQueryDtos = baseSvcPremMap.get(svcName);
            if(!IaisCommonUtils.isEmpty(appAlignLicQueryDtos)){
                for(AppAlignLicQueryDto appAlignLicQueryDto:appAlignLicQueryDtos){
                    premHcis.add(ApplicationHelper.getPremisesHci(appAlignLicQueryDto));
                }
            }
        }
        return premHcis;
    }


    private static String StringTransfer(String str){
        return StringUtil.isEmpty(str)?"":str;
    }

    private static SearchParam initSearParam(){
        SearchParam searchParam = new SearchParam(MenuLicenceDto.class.getName());
        searchParam.setPageSize(10);
        searchParam.setPageNo(1);
        searchParam.setSort("START_DATE",SearchParam.ASCENDING);
        return searchParam;
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
            List<String> pendAndLicPremHci = appCommService.getHciFromPendAppAndLic(licenseeId,hcsaServiceDtos);
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

    private List<String> transferToList(Set<String> targetSet){
        List<String> result = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(targetSet)){
            targetSet.forEach(val->{
                result.add(val);
            });
        }
        return result;
    }

}
