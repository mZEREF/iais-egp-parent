package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AppAlignLicQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.MenuLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.AppSelectSvcDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    private static final String CHOOSE_BASE_SVC = "chooseBaseSvc";
    private static final String CHOOSE_ALIGN = "chooseAlign";
    private static final String CHOOSE_LICENCE = "chooseLic";
    private static final String ERROR_ATTR = "err";
    private static final String BACK_ATTR = "back";
    private static final String NEXT = "next";

    private static final String SERVIC_STEP = "serviceStep";
    private static final String APP_SELECT_SERVICE = "appSelectSvc";
    private static final String HAS_EXISTING_BASE = "hasExistingBase";
    private static final String URL_HTTPS = "https://";
    private static final String ONLY_BASE_SVC = "onlyBaseSvc";
    private static final String APP_ALIGN_LIC = "appAlignLic";
    private static final String BASEANDSPCSVCMAP = "baseAndSpcSvcMap";
    private static final String RETAIN_LIC_PREMISES_LIST =  "retainLicPremisesList";
    private static final String NO_EXIST_BASE_LIC = "noExistBaseLic";
//    private static final String BASE_SERVICE_SORT = "baseServiceSort";
//    private static final String SPECIFIED_SERVICE_SORT = "specifiedServiceSort";
    public static final String APP_SVC_RELATED_INFO_LIST = "appSvcRelatedInfoList";
    private static final String RELOAD_BASE_SVC_SELECTED = "reloadBaseSvcSelected";
    private static final String BASE_LIC_PREMISES_MAP = "baseLicPremisesMap";
    private static final String LIC_ALIGN_SEARCH_PARAM = "licAlignSearchParam";
    private static final String LIC_ALIGN_SEARCH_RESULT= "licAlignSearchResult";

    List<HcsaServiceDto> allbaseService;
    List<HcsaServiceDto> allspecifiedService;
    @Autowired
    private ServiceConfigService serviceConfigService;
    @Autowired
    private LicenceViewService licenceViewService;
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private AppSubmissionService appSubmissionService;
    public void doStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the  doStart start 1...."));
        ParamUtil.setSessionAttr(bpc.request, "licence", null);
        ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR_CHECKED, null);
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR_CHECKED, null);
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR, null);
        ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR, null);
        ParamUtil.setSessionAttr(bpc.request,"DraftNumber",null);
        ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.SELECT_DRAFT_NO,null);
        ParamUtil.setSessionAttr(bpc.request,APP_SELECT_SERVICE,null);
        ParamUtil.setSessionAttr(bpc.request,ONLY_BASE_SVC,null);
        ParamUtil.setSessionAttr(bpc.request,APP_ALIGN_LIC, null);
        ParamUtil.setSessionAttr(bpc.request,BASEANDSPCSVCMAP, null);
        ParamUtil.setSessionAttr(bpc.request,RETAIN_LIC_PREMISES_LIST, null);
        ParamUtil.setSessionAttr(bpc.request,RELOAD_BASE_SVC_SELECTED, null);
        ParamUtil.setSessionAttr(bpc.request,LIC_ALIGN_SEARCH_PARAM,null);
        ParamUtil.setSessionAttr(bpc.request,LIC_ALIGN_SEARCH_RESULT,null);
        ParamUtil.setSessionAttr(bpc.request,APP_SVC_RELATED_INFO_LIST,null);
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
                String err = "Base service should be selected.";
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
            if(licenceJudge == null){
                ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, ERROR_ATTR);
                String err = "Please select at least one licence.";
                ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err);
                ParamUtil.setRequestAttr(bpc.request, "action","err");
            }else if("1".equals(licenceJudge)) {
                if(licence == null){
                    ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, ERROR_ATTR);
                    String err = "Please select at least one licence.";
                    ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err);
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
        if(NEXT.equals(step)){
            ParamUtil.setSessionAttr(bpc.request,"licenceJudge",licenceJudge);
            String crud_action_value=bpc.request.getParameter("crud_action_value");
            String crud_action_additional  =bpc.request.getParameter("crud_action_additional");
            getDraft(bpc);
            String attribute =(String)bpc.request.getAttribute(NewApplicationDelegator.SELECT_DRAFT_NO);
            if("continue".equals(crud_action_value)){
                bpc.request.getSession().setAttribute(NewApplicationDelegator.SELECT_DRAFT_NO, crud_action_additional);
                bpc.request.getSession().setAttribute("DraftNumber", null);
            }else if("resume".equals(crud_action_value)){
                bpc.request.getSession().setAttribute("DraftNumber", crud_action_additional);
            }else if(attribute!=null){
                ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, ERROR_ATTR);
                return;
            }
        }

        ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, step);
    }

    public void prepareData(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("prepareData start ..."));
        String action = (String) ParamUtil.getRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE);
        if(StringUtil.isEmpty(action)){
            action = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
            if(StringUtil.isEmpty(action)){
                action = "chooseSvc";
            }
        }
        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,action);
        log.info(StringUtil.changeForLog("prepareData start ..."));
    }


    public void preChooseSvc(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("prepare choose svc start ..."));
        List<HcsaServiceDto> hcsaServiceDtoList = serviceConfigService.getServicesInActive();
        if (IaisCommonUtils.isEmpty(hcsaServiceDtoList)){
            log.debug("can not find hcsa service list in service menu delegator!");
            return;
        }
        List<HcsaServiceDto> allbaseService = hcsaServiceDtoList.stream()
                .filter(hcsaServiceDto -> BASE_SERVICE.equals(hcsaServiceDto.getSvcType())).collect(Collectors.toList());
        List<HcsaServiceDto> allspecifiedService = hcsaServiceDtoList.stream()
                .filter(hcsaServiceDto -> SPECIFIED_SERVICE.equals(hcsaServiceDto.getSvcType())).collect(Collectors.toList());
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR, (Serializable) allbaseService);
        ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR, (Serializable) allspecifiedService);
        AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
        appSelectSvcDto.setChooseBaseSvc(false);
        ParamUtil.setSessionAttr(bpc.request,APP_SELECT_SERVICE,appSelectSvcDto);
        log.info(StringUtil.changeForLog("prepare data end ..."));
        log.info(StringUtil.changeForLog("prepare choose svc end ..."));
    }

    public void preChooseBaseSvc(BaseProcessClass bpc) throws Exception {
        log.info(StringUtil.changeForLog("prepare choose base svc start ..."));
        boolean noExistBaseLic = false;
        //svcCode,baseSvcDtoList
        Map<String,List<HcsaServiceDto>> baseAndSpcSvcMap = IaisCommonUtils.genNewHashMap();
        //get correlatrion base service
        AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
        List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList =  serviceConfigService.getCorrelation();
        List<HcsaServiceDto> specSvcDtos = appSelectSvcDto.getSpeSvcDtoList();
        List<HcsaServiceDto> baseSvcDtoList = IaisCommonUtils.genNewArrayList();
        for(HcsaServiceDto hcsaServiceDto:specSvcDtos){
            List<HcsaServiceDto> baseServiceDtos = getBaseBySpc(hcsaServiceCorrelationDtoList,hcsaServiceDto.getId());
            baseSvcDtoList.addAll(baseServiceDtos);
            baseAndSpcSvcMap.put(hcsaServiceDto.getSvcCode(),baseServiceDtos);
        }
        ParamUtil.setSessionAttr(bpc.request,BASEANDSPCSVCMAP, (Serializable) baseAndSpcSvcMap);
        List<String> svcNameList = IaisCommonUtils.genNewArrayList();
        //svcName,List<AppAlignLicQueryDto> => for retain
        Map<String,List<AppAlignLicQueryDto>> commPremises = IaisCommonUtils.genNewHashMap();
        for(HcsaServiceDto hcsaServiceDto:baseSvcDtoList){
            svcNameList.add(hcsaServiceDto.getSvcName());
            List<AppAlignLicQueryDto> appAlignLicQueryDtos = IaisCommonUtils.genNewArrayList();
            commPremises.put(hcsaServiceDto.getSvcName(),appAlignLicQueryDtos);
        }
        String licenseeId = "";
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
        if(loginContext != null){
            licenseeId = loginContext.getLicenseeId();
        }
        List<AppAlignLicQueryDto> appAlignLicQueryDtos = appSubmissionService.getAppAlignLicQueryDto(licenseeId,svcNameList);
        //svcCode,List<AppAlignLicQueryDto> => for get data again
        Map<String,List<AppAlignLicQueryDto>> baseLicMap = IaisCommonUtils.genNewHashMap();
        for(AppAlignLicQueryDto appAlignLicQueryDto:appAlignLicQueryDtos){
            AppAlignLicQueryDto baseLicDto = (AppAlignLicQueryDto) CopyUtil.copyMutableObject(appAlignLicQueryDto);
            List<AppAlignLicQueryDto> appAlignLicQueryDtoList = commPremises.get(appAlignLicQueryDto.getSvcName());
            List<AppAlignLicQueryDto> baseLicDtoList = baseLicMap.get(baseLicDto.getHciCode());
            if(IaisCommonUtils.isEmpty(baseLicDtoList)){
                baseLicDtoList = IaisCommonUtils.genNewArrayList();
            }
            baseLicDtoList.add(baseLicDto);
            baseLicMap.put(baseLicDto.getHciCode(),baseLicDtoList);
            if(IaisCommonUtils.isEmpty(appAlignLicQueryDtoList)){
                appAlignLicQueryDtoList = IaisCommonUtils.genNewArrayList();
            }
            //for retain
            String svcName = appAlignLicQueryDto.getSvcName();
            appAlignLicQueryDto.setPremisesId("");
            appAlignLicQueryDto.setSvcName("");
            appAlignLicQueryDto.setLicenceNo("");
            appAlignLicQueryDto.setLicExpiryDate(null);
            appAlignLicQueryDtoList.add(appAlignLicQueryDto);
            commPremises.put(svcName,appAlignLicQueryDtoList);
        }
        List<List<AppAlignLicQueryDto>> appAlignLicQueryDtoList = IaisCommonUtils.genNewArrayList();
        if(commPremises.size() == 0){
            noExistBaseLic = true;
        }else{
            for(List<AppAlignLicQueryDto> item:commPremises.values()){
                if(IaisCommonUtils.isEmpty(item)){
                    noExistBaseLic = true;
                    break;
                }
                appAlignLicQueryDtoList.add(item);
            }
        }

        if(!noExistBaseLic){
            List<AppAlignLicQueryDto> retainList = retainElementList(appAlignLicQueryDtoList);
            if(IaisCommonUtils.isEmpty(retainList)){
                noExistBaseLic = true;
            }
            //todo:remove existing and pending premises address when same service

            ParamUtil.setSessionAttr(bpc.request,RETAIN_LIC_PREMISES_LIST, (Serializable) retainList);
        }

        ParamUtil.setSessionAttr(bpc.request,BASE_LIC_PREMISES_MAP, (Serializable) baseLicMap);
        ParamUtil.setSessionAttr(bpc.request,NO_EXIST_BASE_LIC,noExistBaseLic);



//        List<HcsaServiceDto> hcsaServiceDtosMap = getBaseInSpe(speSvcIds,getAllBaseService(bpc));
//        Map<String ,HcsaServiceDto> specifiedName = IaisCommonUtils.genNewHashMap();
//        for (HcsaServiceDto item:allspecifiedService
//                ) {
//            specifiedName.put(item.getId(),item);
//        }
//
//
//
//        List<String> basechkslist = (List<String>)ParamUtil.getSessionAttr(bpc.request, BASE_SERVICE_ATTR_CHECKED);
//        List<String> basechksNamelist = IaisCommonUtils.genNewArrayList();
//        basechksNamelist = BaseIdToName(basechkslist);
//        SearchResult searchResult = getLicense(bpc,basechksNamelist);
//        ParamUtil.setRequestAttr(bpc.request, VALIDATION_ATTR, "licence");
//        ParamUtil.setRequestAttr(bpc.request, "licence", searchResult);
//        ParamUtil.setSessionAttr(bpc.request, "baseName", basechksNamelist.get(0));

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
        AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
        List<HcsaServiceDto> baseHcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request, BASE_SERVICE_ATTR);
        List<String> excludeChkBase = IaisCommonUtils.genNewArrayList();
        List<String> chkBaseSvcIds = (List<String>) ParamUtil.getSessionAttr(bpc.request,BASE_SERVICE_ATTR_CHECKED);
        for(HcsaServiceDto hcsaServiceDto:baseHcsaServiceDtos){
            excludeChkBase.add(hcsaServiceDto.getId());
        }
        if(appSelectSvcDto.isChooseBaseSvc()){
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = (List<AppSvcRelatedInfoDto>) ParamUtil.getSessionAttr(bpc.request,APP_SVC_RELATED_INFO_LIST);
            List<String> baseSvcIds = IaisCommonUtils.genNewArrayList();
            if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                    baseSvcIds.add(appSvcRelatedInfoDto.getBaseServiceId());
                }
            }
            excludeChkBase.removeAll(baseSvcIds);
        }else{
            excludeChkBase.removeAll(chkBaseSvcIds);
        }
        String licenseeId = "";
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
        if(loginContext != null){
            licenseeId = loginContext.getLicenseeId();
        }
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request,LIC_ALIGN_SEARCH_PARAM);
        if(searchParam == null){
            searchParam = new SearchParam(MenuLicenceDto.class.getName());
            searchParam.setPageSize(10);
            searchParam.setPageNo(1);
            searchParam.setSort("START_DATE",SearchParam.ASCENDING);
        }
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
        searchParam.addFilter("licenseeId",licenseeId,true);
        QueryHelp.setMainSql("applicationQuery", "getLicenceBySerName",searchParam);
        SearchResult<MenuLicenceDto> searchResult = licenceViewService.getMenuLicence(searchParam);
        if(StringUtil.isEmpty(appSelectSvcDto.getAlignLicPremId())){
            boolean flag = true;
            ParamUtil.setRequestAttr(bpc.request,"chooseFirst",flag);
        }
        ParamUtil.setSessionAttr(bpc.request,LIC_ALIGN_SEARCH_PARAM,searchParam);
        ParamUtil.setSessionAttr(bpc.request,LIC_ALIGN_SEARCH_RESULT,searchResult);


//        List<HcsaServiceDto> spcHcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR);
//        List<String> svcNameList = IaisCommonUtils.genNewArrayList();
//        svcNameList = setSvcName(svcNameList,baseHcsaServiceDtos);
//        svcNameList = setSvcName(svcNameList,spcHcsaServiceDtos);
//        List<AppAlignLicQueryDto> appAlignLicQueryDtos = appSubmissionService.getAppAlignLicQueryDto(licenseeId,svcNameList);
//        ParamUtil.setSessionAttr(bpc.request,APP_ALIGN_LIC, (Serializable) appAlignLicQueryDtos);

        log.info(StringUtil.changeForLog("prepare choose licence end ..."));
    }

    public void doChooseService(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("do choose svc start ..."));
        boolean onlyBaseSvc = false;
        ParamUtil.setSessionAttr(bpc.request,ONLY_BASE_SVC,onlyBaseSvc);
        String additional = ParamUtil.getString(bpc.request,CRUD_ACTION_ADDITIONAL);
        if(BACK_ATTR.equals(additional)){
            return;
        }
        List<HcsaServiceDto> allbaseService = getAllBaseService(bpc);
        List<HcsaServiceDto> allspecifiedService = getAllSpeService(bpc);
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
            List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList =  serviceConfigService.getCorrelation();
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
                    err = "There is no base service in specified services.";
                    ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err);
                }else{
                    //to step2
                    nextstep = CHOOSE_BASE_SVC;
                }
            }else{
                //no spe err
                nextstep = currentPage;
                err = "Please select at least one service.";
                ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err);
            }
        }else{
            for (String item:basechks) {
                basecheckedlist.add(item);
                baseSvcSort.add(HcsaServiceCacheHelper.getServiceById(item));
            }
            //base
            Map<String ,String> specifiedName = IaisCommonUtils.genNewHashMap();
            Map<String ,String> baseName = IaisCommonUtils.genNewHashMap();

            for (HcsaServiceDto item:allbaseService
                    ) {
                baseName.put(item.getId(),item.getSvcName());
            }
            for (HcsaServiceDto item:allspecifiedService
                    ) {
                specifiedName.put(item.getId(),item.getSvcName());
            }

            if(sepcifiedchk != null){
                List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList =  serviceConfigService.getCorrelation();
                for (String item:sepcifiedchk) {
                    sepcifiedcheckedlist.add(item);
                    speSvcSort.add(HcsaServiceCacheHelper.getServiceById(item));
                }
                //spe
                Map<String ,String> necessaryBaseServiceList = necessaryBase(basecheckedlist,sepcifiedcheckedlist,hcsaServiceCorrelationDtoList);
                List<String> extrabaselist = IaisCommonUtils.genNewArrayList();
                extrabaselist.addAll(basecheckedlist);
                List<HcsaServiceDto> hcsaServiceDtosMap = getBaseInSpe(sepcifiedcheckedlist,allbaseService,hcsaServiceCorrelationDtoList);
                List<String> removeNecessary = IaisCommonUtils.genNewArrayList();
                for (String extra: extrabaselist
                        ) {
                    for (HcsaServiceDto hc:hcsaServiceDtosMap
                            ) {
                        if(extra.equals(hc.getId())){
                            removeNecessary.add(extra);
                        }
                    }
                }
                List<String> allBaseSvcId = IaisCommonUtils.genNewArrayList();
                for(HcsaServiceDto hcsaServiceDto:allbaseService){
                    allBaseSvcId.add(hcsaServiceDto.getId());
                }
                Map<String,List<String>> baseRelSpe = baseRelSpe(allBaseSvcId,sepcifiedcheckedlist,hcsaServiceCorrelationDtoList);
                extrabaselist.removeAll(removeNecessary);
                if(necessaryBaseServiceList.size() > 0){
                    //no match
                    nextstep = currentPage;
                    if(extrabaselist.size() == 0){
                        err = baseName.get(getKeyOrNull(necessaryBaseServiceList)) + " should be selected.";
                    }else{
                        for(Map.Entry<String, String> entry : necessaryBaseServiceList.entrySet()){
                            err = "The chosen base service ";
                            err = err + baseName.get(extrabaselist.get(0));
                            err = err + " is not the prerequisite of ";
                            err = err + specifiedName.get(entry.getValue()) + ".";
                            break;
                        }
                    }
                    ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err);
                }else{
                    if (baseRelSpe.size() == 0){
                        nextstep = currentPage;
                        err = "There is no base service in specified services.";
                        ParamUtil.setRequestAttr(bpc.request, ERROR_ATTR, err);
                    }else{
                        nextstep = CHOOSE_BASE_SVC;
                    }
                }
            }else{
                //new app
                nextstep = CHOOSE_ALIGN;
                onlyBaseSvc = true;
                ParamUtil.setSessionAttr(bpc.request,ONLY_BASE_SVC,onlyBaseSvc);
            }

        }
//        appSelectSvcDto.setBaseSvcIds(basecheckedlist);
//        appSelectSvcDto.setSpecifiedSvcIds(sepcifiedcheckedlist);
        appSelectSvcDto.setBaseSvcDtoList(baseSvcSort);
        appSelectSvcDto.setSpeSvcDtoList(speSvcSort);
        ParamUtil.setSessionAttr(bpc.request,APP_SELECT_SERVICE,appSelectSvcDto);
        ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR_CHECKED, (Serializable) sepcifiedcheckedlist);
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR_CHECKED, (Serializable) basecheckedlist);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, nextstep);
        //test
        //ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
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
        if(!IaisCommonUtils.isEmpty(speSvcDtoList)){
            Map<String,List<AppAlignLicQueryDto>> baseLicMap = (Map<String, List<AppAlignLicQueryDto>>) ParamUtil.getSessionAttr(bpc.request,BASE_LIC_PREMISES_MAP);
            //reload
            for(HcsaServiceDto speServiceDto:speSvcDtoList){
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
                        appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
                        appSvcRelatedInfoDto.setServiceId(baseServiceDto.getId());
                        appSvcRelatedInfoDto.setServiceName(baseServiceDto.getSvcName());
                        appSvcRelatedInfoDto.setBaseServiceId(baseServiceDto.getId());
                        appSvcRelatedInfoDtos.add(appSvcRelatedInfoDto);
                        appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
                        appSvcRelatedInfoDto.setServiceId(speServiceDto.getId());
                        appSvcRelatedInfoDto.setServiceName(speServiceDto.getSvcName());
                        appSvcRelatedInfoDto.setBaseServiceId(baseServiceDto.getId());
                        appSvcRelatedInfoDtos.add(appSvcRelatedInfoDto);
                        //for reload
                        baseReloadDto.setServiceCode(baseServiceDto.getSvcCode());
                        baseReloadDto.setLicPremisesId("");
                        baseReloadDtoMap.put(speServiceDto.getSvcCode(),baseReloadDto);
                    }else{
                        chooseExist = true;
                        String premIndexNo = ParamUtil.getString(bpc.request,speServiceDto.getSvcCode());
                        String hciCode = ParamUtil.getMaskedString(bpc.request,premIndexNo+"-hciCode");
                        AppAlignLicQueryDto appAlignLicQueryDto = getAppAlignLicQueryDto(baseLicMap,baseServiceDto.getSvcName(),hciCode);
                        appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
                        appSvcRelatedInfoDto.setServiceId(speServiceDto.getId());
                        appSvcRelatedInfoDto.setServiceName(speServiceDto.getSvcName());
                        appSvcRelatedInfoDto.setBaseServiceId(baseServiceDto.getId());
                        appSvcRelatedInfoDto.setRelLicenceNo(appAlignLicQueryDto.getLicenceNo());
                        appSvcRelatedInfoDto.setLicPremisesId(appAlignLicQueryDto.getPremisesId());
                        appSvcRelatedInfoDto.setHciCode(appAlignLicQueryDto.getHciCode());
                        appSvcRelatedInfoDtos.add(appSvcRelatedInfoDto);
                        //for reload
                        baseReloadDto.setServiceCode(baseServiceDto.getSvcCode());
                        baseReloadDto.setLicPremisesId(appAlignLicQueryDto.getPremisesId());
                        baseReloadDto.setHciCode(appAlignLicQueryDto.getHciCode());
                        baseReloadDtoMap.put(speServiceDto.getSvcCode(),baseReloadDto);
                    }
                }
            }
            //sort
            appSvcRelatedInfoDtos.sort((h1,h2)->h1.getServiceName().compareTo(h2.getServiceName()));
            ParamUtil.setSessionAttr(bpc.request,APP_SVC_RELATED_INFO_LIST, (Serializable) appSvcRelatedInfoDtos);
            ParamUtil.setSessionAttr(bpc.request,RELOAD_BASE_SVC_SELECTED, (Serializable) baseReloadDtoMap);
        }

        String additional = ParamUtil.getString(bpc.request,CRUD_ACTION_ADDITIONAL);
        if(BACK_ATTR.equals(additional)){
            return;
        }
        //validate
        String erroMsg = "";
        int chkCount = 0;
        if(chooseExist && chooseDiff){
            erroMsg = MessageUtil.getMessageDesc("NEW_ERR0007");
        }else if(chooseExist && !chooseDiff){
            chkCount = appSelectSvcDto.getSpeSvcDtoList().size();
        }else if(!chooseExist && chooseDiff){
            chkCount = appSelectSvcDto.getSpeSvcDtoList().size() * 2;
        }
        if(StringUtil.isEmpty(erroMsg)){
            if(chkCount != appSvcRelatedInfoDtos.size()){
                //todo:may be change
                erroMsg = MessageUtil.getMessageDesc("NEW_ERR0007");
            }else if(appSvcRelatedInfoDtos.size()>1){
                //NEW_ERR0007
                String hciCodeMark = StringTransfer(appSvcRelatedInfoDtos.get(0).getHciCode());
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                    String hciCode = StringTransfer(appSvcRelatedInfoDto.getHciCode());
                    if(!hciCodeMark.equals(hciCode)){
                        erroMsg = MessageUtil.getMessageDesc("NEW_ERR0007");
                    }
                }
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
                    String attribute =(String)bpc.request.getAttribute(NewApplicationDelegator.SELECT_DRAFT_NO);
                    if("continue".equals(crud_action_value)){
                        bpc.request.getSession().setAttribute(NewApplicationDelegator.SELECT_DRAFT_NO, draftNo);
                        bpc.request.getSession().setAttribute("DraftNumber", null);
                    }else if("resume".equals(crud_action_value)){
                        bpc.request.getSession().setAttribute("DraftNumber", draftNo);
                    }else if(attribute!=null){
                        //back to curr page
                        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,CHOOSE_BASE_SVC);
                        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,"loading");
                    }
                }
            }else{
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,CHOOSE_LICENCE);
            }

            String switchStep = ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE);
            if(NEXT.equals(switchStep)){
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
        }


        String additional = ParamUtil.getString(bpc.request,CRUD_ACTION_ADDITIONAL);
        if(BACK_ATTR.equals(additional)){
            return;
        }
        if(AppConsts.YES.equals(isAlign)){
            nextStep = CHOOSE_LICENCE;
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,nextStep);
        }else{
            //to next
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
            String nextstep = ParamUtil.getString(bpc.request,"crud_action_additional");
            if(NEXT.equals(nextstep)){
                String crud_action_value=bpc.request.getParameter("crud_action_value");
                String draftNo  =bpc.request.getParameter("draftNo");
                getDraft(bpc);
                String attribute =(String)bpc.request.getAttribute(NewApplicationDelegator.SELECT_DRAFT_NO);
                if("continue".equals(crud_action_value)){
                    bpc.request.getSession().setAttribute(NewApplicationDelegator.SELECT_DRAFT_NO, draftNo);
                    bpc.request.getSession().setAttribute("DraftNumber", null);
                }else if("resume".equals(crud_action_value)){
                    bpc.request.getSession().setAttribute("DraftNumber", draftNo);
                }else if(attribute!=null){
                    //back to curr page
                    ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,CHOOSE_ALIGN);
                    ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,"loading");
                }
            }
        }

        String switchStep = ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE);
        if(NEXT.equals(switchStep)){
            appSelectSvcDto.setAlignPage(true);
        }

        ParamUtil.setSessionAttr(bpc.request,APP_SELECT_SERVICE,appSelectSvcDto);
        log.info(StringUtil.changeForLog("do choose align end ..."));
    }

    public void doChooseLic(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("do choose lic start ..."));
        String additional = ParamUtil.getString(bpc.request,CRUD_ACTION_ADDITIONAL);
        AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
        String alignLic = ParamUtil.getString(bpc.request,"alignLic");
        String alignLicenceNo = "";
        String licPremiseId = "";
        if(StringUtil.isEmpty(alignLic)){
            log.info(StringUtil.changeForLog("alignLic is null"));
            //default choose first
            SearchResult<MenuLicenceDto>  searchResult = (SearchResult<MenuLicenceDto>) ParamUtil.getSessionAttr(bpc.request,LIC_ALIGN_SEARCH_RESULT);
            if(searchResult.getRowCount()>0){
                MenuLicenceDto menuLicenceDto = searchResult.getRows().get(0);
                if(!"first".equals(menuLicenceDto.getSvcName())){
                    alignLicenceNo = menuLicenceDto.getLicenceNo();
                    licPremiseId = menuLicenceDto.getPremisesId();
                }
            }

        }else if("-1".equals(alignLic)){
            log.info(StringUtil.changeForLog("not align"));
        } else{
            alignLicenceNo = ParamUtil.getMaskedString(bpc.request,"licenceNo"+alignLic);
            licPremiseId = ParamUtil.getMaskedString(bpc.request,"premisesId"+alignLic);
        }
        appSelectSvcDto.setAlignLicPremId(licPremiseId);
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
            for(HcsaServiceDto hcsaServiceDto:appSelectSvcDto.getBaseSvcDtoList()){
                AppSvcRelatedInfoDto appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
                appSvcRelatedInfoDto.setServiceId(hcsaServiceDto.getId());
                appSvcRelatedInfoDto.setServiceCode(hcsaServiceDto.getSvcCode());
                appSvcRelatedInfoDto.setBaseServiceId(hcsaServiceDto.getId());
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
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,CHOOSE_ALIGN);
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
            String attribute =(String)bpc.request.getAttribute(NewApplicationDelegator.SELECT_DRAFT_NO);
            if("continue".equals(crud_action_value)){
                bpc.request.getSession().setAttribute(NewApplicationDelegator.SELECT_DRAFT_NO, draftNo);
                bpc.request.getSession().setAttribute("DraftNumber", null);
            }else if("resume".equals(crud_action_value)){
                bpc.request.getSession().setAttribute("DraftNumber", draftNo);
            }else if(attribute!=null){
                //back to curr page
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,CHOOSE_LICENCE);
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,"loading");
            }
        }
        String switchStep = ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE);
        if(NEXT.equals(switchStep)){
            appSelectSvcDto.setLicPage(true);
            ParamUtil.setSessionAttr(bpc.request,APP_SELECT_SERVICE,appSelectSvcDto);
        }

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
        ParamUtil.setRequestAttr(bpc.request,"switch",action);
        log.info(StringUtil.changeForLog("control switch end ..."));
    }

    public void backInbox(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("back inbox start ..."));
        StringBuilder url = new StringBuilder();
        url.append(URL_HTTPS)
                .append(bpc.request.getServerName())
                .append("/main-web/eservice/INTERNET/MohInternetInbox");
        String tokenUrl = RedirectUtil. changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
        bpc.response.sendRedirect(tokenUrl);

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
        appSelectSvcDto.setAlignLicPremId("");
        log.info(StringUtil.changeForLog("doPage end ..."));
    }


    //=============================================================================
    //private method
    //=============================================================================

    private void getDraft(BaseProcessClass bpc){
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId;
        if(loginContext!=null){
             licenseeId = loginContext.getLicenseeId();
        }else {
            licenseeId = "9ED45E34-B4E9-E911-BE76-000C29C8FBE4";
        }

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
            List<HcsaServiceDto> hcsaServiceDtosById = serviceConfigService.getHcsaServiceDtosById(serviceConfigIds);
            List<String> serviceCodeList = new ArrayList<>(hcsaServiceDtosById.size());
            for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtosById) {
                serviceCodeList.add(hcsaServiceDto.getSvcCode());
            }
            serviceCodeList.sort(String::compareTo);
            Map<String, Object> map = new HashMap<>();
            map.put("serviceCodesList", serviceCodeList);
            map.put("appType", ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
            map.put("licenseeId",licenseeId);
            String entity = applicationClient.selectDarft(map).getEntity();
            bpc.request.setAttribute(NewApplicationDelegator.SELECT_DRAFT_NO, entity);
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

    public void showLicensee(BaseProcessClass bpc) {
        try {
            StringBuilder url = new StringBuilder();
            url.append("https://").append(bpc.request.getServerName())
                    .append("/main-web/eservice/INTERNET/MohLicenseeCompanyDetail")
                    .append("?licenseView=licensee");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
        }catch (Exception e){
            log.info(e.getMessage());
        }
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
                for(String baseSvcId:baseSvcIdList){
                    if(baseSvcIds.contains(baseSvcId)){
                        currFlag = true;
                        break;
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
                    hcsaServiceDtos.add(HcsaServiceCacheHelper.getServiceById(corr.getBaseSvcId()));
                }
            }
        }
        return  hcsaServiceDtos;
    }

    private List<HcsaServiceDto> getAllBaseService(BaseProcessClass bpc){
        List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request,BASE_SERVICE_ATTR);
        if(IaisCommonUtils.isEmpty(hcsaServiceDtos)){
            hcsaServiceDtos = IaisCommonUtils.genNewArrayList();
        }
        return hcsaServiceDtos;
    }

    private List<HcsaServiceDto> getAllSpeService(BaseProcessClass bpc){
        List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request,SPECIFIED_SERVICE_ATTR);
        if(IaisCommonUtils.isEmpty(hcsaServiceDtos)){
            hcsaServiceDtos = IaisCommonUtils.genNewArrayList();
        }
        return hcsaServiceDtos;
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

    private static String StringTransfer(String str){
        return StringUtil.isEmpty(str)?"":str;
    }

}
