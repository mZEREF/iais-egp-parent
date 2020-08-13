package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.assessmentGuide.GuideConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AppAlignLicQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.MenuLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.*;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.AppSelectSvcDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.AssessmentGuideService;
import com.ecquaria.cloud.moh.iais.service.InboxService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
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
    public static final String SELECT_DRAFT_NO          ="selectDraftNo";
    private static final String LIC_ALIGN_SEARCH_PARAM = "licAlignSearchParam";
    private static final String LIC_ALIGN_SEARCH_RESULT= "licAlignSearchResult";

    @Autowired
    private InboxService inboxService;

    @Autowired
    RequestForChangeService requestForChangeService;

    @Autowired
    AssessmentGuideService assessmentGuideService;

    private String licenseeId;

    public void start(BaseProcessClass bpc) {
        log.info("****start ******");
        AuditTrailHelper.auditFunction("HalpAssessmentGuideDelegator", "HalpAssessmentGuideDelegators");
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        log.info("****end ******");
    }


    public void perDate(BaseProcessClass bpc) {
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        licenseeId = loginContext.getLicenseeId();

    }

    public void newApp1(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("prepareData start ..."));
        String action = (String) ParamUtil.getRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE);
        if(StringUtil.isEmpty(action)){
            action = "chooseSvc";
        }
        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,action);
        log.info(StringUtil.changeForLog("prepareData start ..."));
    }

    public void renewLic(BaseProcessClass bpc) throws IOException {
        log.info("****start ******");
        SearchParam renewLicSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.RENEW_LICENCE_SEARCH_PARAM,PremisesListQueryDto.class.getName(),"PREMISES_TYPE",SearchParam.DESCENDING,false);
        renewLicSearchParam.addFilter("licenseeId", licenseeId, true);
        QueryHelp.setMainSql("interInboxQuery", "queryPremises", renewLicSearchParam);
        SearchResult<PremisesListQueryDto> renewLicSearchResult = requestForChangeService.searchPreInfo(renewLicSearchParam);
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
        }

        String switchStep = ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE);
        if(NEXT.equals(switchStep)){
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
                        appSvcRelatedInfoDto.setBaseServiceId(baseServiceDto.getId());
                        appSvcRelatedInfoDto.setServiceName(speServiceDto.getSvcName());
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
                        appSvcRelatedInfoDto.setRelLicenceNo(appAlignLicQueryDto.getLicenceNo());
                        appSvcRelatedInfoDto.setBaseServiceId(baseServiceDto.getId());
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
            serviceCodeList.sort(String::compareTo);
            Map<String, Object> map = new HashMap<>();
            map.put("serviceCodesList", serviceCodeList);
            map.put("appType", ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
            map.put("licenseeId",licenseeId);
            String entity = assessmentGuideService.selectDarft(map);
            bpc.request.setAttribute(SELECT_DRAFT_NO, entity);
        }
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

    private static String StringTransfer(String str){
        return StringUtil.isEmpty(str)?"":str;
    }

    public void preChooseBaseSvc(BaseProcessClass bpc) throws Exception {
        log.info(StringUtil.changeForLog("prepare choose base svc start ..."));
        boolean noExistBaseLic = false;
        //svcCode,baseSvcDtoList
        Map<String,List<HcsaServiceDto>> baseAndSpcSvcMap = IaisCommonUtils.genNewHashMap();
        //get correlatrion base service
        AppSelectSvcDto appSelectSvcDto = getAppSelectSvcDto(bpc);
        List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList =  assessmentGuideService.getCorrelation();
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
        List<AppAlignLicQueryDto> appAlignLicQueryDtos = assessmentGuideService.getAppAlignLicQueryDto(licenseeId,svcNameList);
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
            appAlignLicQueryDto.setSvcName("");
            appAlignLicQueryDto.setPremisesId("");
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
            ParamUtil.setSessionAttr(bpc.request,RETAIN_LIC_PREMISES_LIST, (Serializable) retainList);
        }
        ParamUtil.setSessionAttr(bpc.request,NO_EXIST_BASE_LIC,noExistBaseLic);
        ParamUtil.setSessionAttr(bpc.request,BASE_LIC_PREMISES_MAP, (Serializable) baseLicMap);
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
            List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList =  assessmentGuideService.getCorrelation();
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
                List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtoList =  assessmentGuideService.getCorrelation();
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
                                errMsg =  errMsg.replace("<service name>",specSvcName);
                                String baseNameStr = baseSvcNameList.stream().collect(Collectors.joining(","));
                                errMsg = errMsg.replace("<base service name>",baseNameStr);
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

        }
//        appSelectSvcDto.setBaseSvcIds(basecheckedlist);
//        appSelectSvcDto.setSpecifiedSvcIds(sepcifiedcheckedlist);
        appSelectSvcDto.setBaseSvcDtoList(baseSvcSort);
        appSelectSvcDto.setSpeSvcDtoList(speSvcSort);
        ParamUtil.setSessionAttr(bpc.request, SPECIFIED_SERVICE_ATTR_CHECKED, (Serializable) sepcifiedcheckedlist);
        ParamUtil.setSessionAttr(bpc.request, BASE_SERVICE_ATTR_CHECKED, (Serializable) basecheckedlist);

        //control switch
        if(!currentPage.equals(nextstep)){
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
            boolean newLicensee  = true;
            if(loginContext!=null){
                newLicensee =  assessmentGuideService.isNewLicensee(loginContext.getLicenseeId());
            }
            appSelectSvcDto.setNewLicensee(newLicensee);
            if(newLicensee){
                if(nextstep.equals(CHOOSE_BASE_SVC)){
                    //64570
                    if(basechks != null && basechks.length == 1){
                        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
                    }
                }else if(nextstep.equals(CHOOSE_ALIGN)){
                    ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
                    //todo open
//                    if(basechks.length == 1){
//                        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,NEXT);
//                    }else{
//                        //todo: wait design if choose align
//                        nextstep = CHOOSE_ALIGN;
//                    }
                }
            }else{
                //todo logic
                if(nextstep.equals(CHOOSE_BASE_SVC)){

                }else if(nextstep.equals(CHOOSE_ALIGN)){
                    nextstep = CHOOSE_LICENCE;
                }
                //init search param
                SearchParam searchParam = initSearParam();
                ParamUtil.setSessionAttr(bpc.request,LIC_ALIGN_SEARCH_PARAM,searchParam);
            }
        }
        log.info(StringUtil.changeForLog("do choose svc next step:"+nextstep));
        ParamUtil.setSessionAttr(bpc.request,APP_SELECT_SERVICE,appSelectSvcDto);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, nextstep);
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
        ParamUtil.setRequestAttr(bpc.request,"switch_action_type",action);
        log.info(StringUtil.changeForLog("control switch end ..."));
    }

    public void preChooseLic(BaseProcessClass bpc) {
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
            excludeChkBase.removeAll(chkBaseSvcIds);
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
            searchParam = initSearParam();
        }
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
        searchParam.addFilter("licenseeId",licenseeId,true);
        QueryHelp.setMainSql("interInboxQuery", "getLicenceBySerName",searchParam);
        SearchResult<MenuLicenceDto> searchResult = assessmentGuideService.getMenuLicence(searchParam);
        if(StringUtil.isEmpty(appSelectSvcDto.getAlignLicPremId())){
            boolean flag = true;
            ParamUtil.setRequestAttr(bpc.request,"chooseFirst",flag);
        }
        ParamUtil.setSessionAttr(bpc.request,LIC_ALIGN_SEARCH_RESULT,searchResult);
        ParamUtil.setSessionAttr(bpc.request,LIC_ALIGN_SEARCH_PARAM,searchParam);
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
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,"loading");
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
                bpc.request.getSession().setAttribute("DraftNumber", null);
            }else if("resume".equals(crud_action_value)){
                bpc.request.getSession().setAttribute("DraftNumber", attribute);
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
        SearchParam renewLicUpdateSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.RENEW_LICENCE_UPDATE_SEARCH_PARAM,PremisesListQueryDto.class.getName(),"PREMISES_TYPE",SearchParam.DESCENDING,false);
        renewLicUpdateSearchParam.addFilter("licenseeId", licenseeId, true);
        QueryHelp.setMainSql("interInboxQuery", "queryPremises", renewLicUpdateSearchParam);
        SearchResult<PremisesListQueryDto> renewLicUpdateSearchResult = requestForChangeService.searchPreInfo(renewLicUpdateSearchParam);
        if (!StringUtil.isEmpty(renewLicUpdateSearchResult)) {
            ParamUtil.setSessionAttr(bpc.request, GuideConsts.RENEW_LICENCE_UPDATE_SEARCH_PARAM, renewLicUpdateSearchParam);
            ParamUtil.setRequestAttr(bpc.request, GuideConsts.RENEW_LICENCE_UPDATE_SEARCH_RESULT, renewLicUpdateSearchResult);
        }
        log.info("****end ******");
    }

    private static List<AppSvcRelatedInfoDto> addOtherSvcInfo(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos,List<HcsaServiceDto> hcsaServiceDtos,boolean needSort){
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos) && !IaisCommonUtils.isEmpty(hcsaServiceDtos)){
            List<HcsaServiceDto> otherSvcDtoList = IaisCommonUtils.genNewArrayList();
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
                sortAppSvcRelatDto(appSvcRelatedInfoDtos);
            }
        }
        return appSvcRelatedInfoDtos;
    }

    public static void sortAppSvcRelatDto(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos){
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
        List<AppSvcRelatedInfoDto> newAppSvcDto = IaisCommonUtils.genNewArrayList();
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

    public void doRenewStep(BaseProcessClass bpc) throws IOException {
        String [] licIds = ParamUtil.getStrings(bpc.request, "renewLicenId");
        Map<String, String> renewErrorMap = IaisCommonUtils.genNewHashMap();
        String tmp = "The selected licence(s) is/are not eligible for renewal: ";
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
            if (result){
                StringBuilder url = new StringBuilder();
                url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                        .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohWithOutRenewal");
                ParamUtil.setSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_LIC_ID_LIST_ATTR, (Serializable) licIdValue);
                String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
                bpc.response.sendRedirect(tokenUrl);
            }else{
                ParamUtil.setRequestAttr(bpc.request,"licIsRenewed",result);
                if(StringUtil.isEmpty(renewErrorMessage.toString())){
                    String errorMessage2 = renewErrorMap.get("errorMessage2");
                    if(StringUtil.isEmpty(errorMessage2)){
                        renewErrorMessage.append("There is already a pending application for the selected licence");
                    }else{
                        renewErrorMessage.append(errorMessage2);
                    }
                }
                ParamUtil.setRequestAttr(bpc.request,InboxConst.LIC_ACTION_ERR_MSG,renewErrorMessage.toString());
                String actionType = ParamUtil.getString(bpc.request,"guide_action_type");
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
            bpc.response.sendRedirect(tokenUrl);
        }catch (Exception e){
            log.info(e.getMessage());
        }
    }

    public void backChooseSvc(BaseProcessClass bpc) {

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
                    .append("/hcsa-licence-web/eservice/INTERNET/MohNewApplication");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
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

    public void amendLic1_1(BaseProcessClass bpc) {
        log.info("****start ******");
        SearchParam amendDetailsSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.AMEND_DETAILS_SEARCH_PARAM,PremisesListQueryDto.class.getName(),"PREMISES_TYPE",SearchParam.DESCENDING,false);
        amendDetailsSearchParam.addFilter("licenseeId", licenseeId, true);
        QueryHelp.setMainSql("interInboxQuery", "queryPremises", amendDetailsSearchParam);
        SearchResult<PremisesListQueryDto> amendDetailsSearchResult = requestForChangeService.searchPreInfo(amendDetailsSearchParam);
        if (!StringUtil.isEmpty(amendDetailsSearchResult)) {
            ParamUtil.setSessionAttr(bpc.request, GuideConsts.AMEND_DETAILS_SEARCH_PARAM, amendDetailsSearchParam);
            ParamUtil.setRequestAttr(bpc.request, GuideConsts.AMEND_DETAILS_SEARCH_RESULT, amendDetailsSearchResult);
        }
        log.info("****end ******");
    }

    public void amendLic1_2(BaseProcessClass bpc) {
        log.info("****start ******");
        SearchParam amendDetailsRemoveSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.AMEND_DETAILS_REMOVE_SEARCH_PARAM,PremisesListQueryDto.class.getName(),"PREMISES_TYPE",SearchParam.DESCENDING,false);
        amendDetailsRemoveSearchParam.addFilter("licenseeId", licenseeId, true);
        QueryHelp.setMainSql("interInboxQuery", "queryPremises", amendDetailsRemoveSearchParam);
        SearchResult<PremisesListQueryDto> amendDetailsRemoveSearchResult = requestForChangeService.searchPreInfo(amendDetailsRemoveSearchParam);
        if (!StringUtil.isEmpty(amendDetailsRemoveSearchResult)) {
            ParamUtil.setSessionAttr(bpc.request, GuideConsts.AMEND_DETAILS_REMOVE_SEARCH_PARAM, amendDetailsRemoveSearchParam);
            ParamUtil.setRequestAttr(bpc.request,GuideConsts.AMEND_DETAILS_REMOVE_SEARCH_RESULT, amendDetailsRemoveSearchResult);
        }
        log.info("****end ******");
    }

    public void amendLic2(BaseProcessClass bpc) {
        log.info("****start ******");
        SearchParam amendHCISearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, "amendHCISearchParam",PremisesListQueryDto.class.getName(),"PREMISES_TYPE",SearchParam.DESCENDING,false);
        amendHCISearchParam.addFilter("licenseeId", licenseeId, true);
        QueryHelp.setMainSql("interInboxQuery", "queryPremises", amendHCISearchParam);
        SearchResult<PremisesListQueryDto> amendHCISearchResult = requestForChangeService.searchPreInfo(amendHCISearchParam);
        if (!StringUtil.isEmpty(amendHCISearchResult)) {
            ParamUtil.setSessionAttr(bpc.request, "amendHCISearchParam", amendHCISearchParam);
            ParamUtil.setRequestAttr(bpc.request, "amendHCISearchResult", amendHCISearchResult);
        }
        log.info("****end ******");
    }

    public void amendLic3_1(BaseProcessClass bpc) {
        log.info("****start ******");
        SearchParam amendDetailsSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.AMEND_UPDATE_LICENSEE_SEARCH_PARAM,PremisesListQueryDto.class.getName(),"PREMISES_TYPE",SearchParam.DESCENDING,false);
        amendDetailsSearchParam.addFilter("licenseeId", licenseeId, true);
        QueryHelp.setMainSql("interInboxQuery", "queryPremises", amendDetailsSearchParam);
        SearchResult<PremisesListQueryDto> amendDetailsSearchResult = requestForChangeService.searchPreInfo(amendDetailsSearchParam);
        if (!StringUtil.isEmpty(amendDetailsSearchResult)) {
            ParamUtil.setSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_LICENSEE_SEARCH_PARAM, amendDetailsSearchParam);
            ParamUtil.setRequestAttr(bpc.request, GuideConsts.AMEND_UPDATE_LICENSEE_SEARCH_RESULT, amendDetailsSearchResult);
        }
        log.info("****end ******");
    }

    public void amendLic3_2(BaseProcessClass bpc) {
        log.info("****start ******");
        SearchParam amendDetailsSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.AMEND_UPDATE_LICENSEES_SEARCH_PARAM,PremisesListQueryDto.class.getName(),"PREMISES_TYPE",SearchParam.DESCENDING,false);
        amendDetailsSearchParam.addFilter("licenseeId", licenseeId, true);
        QueryHelp.setMainSql("interInboxQuery", "queryPremises", amendDetailsSearchParam);
        SearchResult<PremisesListQueryDto> amendDetailsSearchResult = requestForChangeService.searchPreInfo(amendDetailsSearchParam);
        if (!StringUtil.isEmpty(amendDetailsSearchResult)) {
            ParamUtil.setSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_LICENSEES_SEARCH_PARAM, amendDetailsSearchParam);
            ParamUtil.setRequestAttr(bpc.request, GuideConsts.AMEND_UPDATE_LICENSEES_SEARCH_RESULT, amendDetailsSearchResult);
        }
        log.info("****end ******");
    }

    public void amendLic4_1(BaseProcessClass bpc) {
        log.info("****start ******");
        SearchParam amendDetailsSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.AMEND_UPDATE_PERSONNEL_SEARCH_PARAM,PremisesListQueryDto.class.getName(),"PREMISES_TYPE",SearchParam.DESCENDING,false);
        amendDetailsSearchParam.addFilter("licenseeId", licenseeId, true);
        QueryHelp.setMainSql("interInboxQuery", "queryPremises", amendDetailsSearchParam);
        SearchResult<PremisesListQueryDto> amendDetailsSearchResult = requestForChangeService.searchPreInfo(amendDetailsSearchParam);
        if (!StringUtil.isEmpty(amendDetailsSearchResult)) {
            ParamUtil.setSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_PERSONNEL_SEARCH_PARAM, amendDetailsSearchParam);
            ParamUtil.setRequestAttr(bpc.request, GuideConsts.AMEND_UPDATE_PERSONNEL_SEARCH_RESULT, amendDetailsSearchResult);
        }
        log.info("****end ******");

    }

    public void amendLic4_2(BaseProcessClass bpc) {
        log.info("****start ******");
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();
        List<PersonnelListQueryDto> persons = requestForChangeService.getLicencePersonnelListQueryDto(licenseeId);
        if (!IaisCommonUtils.isEmpty(persons)) {
            List<String> idNos = IaisCommonUtils.genNewArrayList();
            for (PersonnelListQueryDto dto : persons) {
                String idNo = dto.getIdNo();
                if (!idNos.contains(idNo)) {
                    idNos.add(idNo);
                    String idType = dto.getIdType();
                    String name = dto.getName();
                    SelectOption s = new SelectOption(idType + "," + idNo, name + ", " + idNo + " (" + idType + ")");
                    selectOptions.add(s);
                }
            }
        }
        SearchParam amendDetailsSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.AMEND_UPDATE_CONTACT_SEARCH_PARAM,PremisesListQueryDto.class.getName(),"PREMISES_TYPE",SearchParam.DESCENDING,false);
        amendDetailsSearchParam.addFilter("licenseeId", licenseeId, true);
        QueryHelp.setMainSql("interInboxQuery", "queryPremises", amendDetailsSearchParam);
        SearchResult<PremisesListQueryDto> amendDetailsSearchResult = requestForChangeService.searchPreInfo(amendDetailsSearchParam);
        if (!StringUtil.isEmpty(amendDetailsSearchResult)) {
            ParamUtil.setSessionAttr(bpc.request, GuideConsts.AMEND_UPDATE_CONTACT_SEARCH_PARAM, amendDetailsSearchParam);
            ParamUtil.setSessionAttr(bpc.request, "personnelOptions", (Serializable) selectOptions);
            ParamUtil.setRequestAttr(bpc.request, GuideConsts.AMEND_UPDATE_CONTACT_SEARCH_RESULT, amendDetailsSearchResult);
        }
        log.info("****end ******");
    }

    public void prepareSwitch(BaseProcessClass bpc) throws IOException {
        log.info("****start ******");
    }

    public void ceaseLic(BaseProcessClass bpc) {
        log.info("****start ******");
        SearchParam ceaseLicenceParam = HalpSearchResultHelper.gainSearchParam(bpc.request,GuideConsts.CEASE_LICENCE_SEARCH_PARAM,PremisesListQueryDto.class.getName(),"PREMISES_TYPE",SearchParam.DESCENDING,false);
        ceaseLicenceParam.addFilter("licenseeId", licenseeId, true);
        QueryHelp.setMainSql("interInboxQuery", "queryPremises", ceaseLicenceParam);
        SearchResult<PremisesListQueryDto> ceaseLicenceResult = requestForChangeService.searchPreInfo(ceaseLicenceParam);
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
        List<String> licIdValue = IaisCommonUtils.genNewArrayList();
        String[] licIds = ParamUtil.getStrings(bpc.request, "ceaseLicIds");
        boolean result = false;
        for (String item : licIds) {
            licIdValue.add(ParamUtil.getMaskedString(bpc.request, item));
        }
        Map<String, Boolean> resultMap = inboxService.listResultCeased(licIdValue);
        for (Map.Entry<String, Boolean> entry : resultMap.entrySet()) {
            if (!entry.getValue()) {
                result = true;
                break;
            }
        }
        if (result) {
            ParamUtil.setRequestAttr(bpc.request, InboxConst.LIC_CEASED_ERR_RESULT, Boolean.TRUE);
        } else {
            ParamUtil.setSessionAttr(bpc.request, "licIds", (Serializable) licIdValue);
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE + "MohCessationApplication");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
        }
    }

    public void withdrawApp(BaseProcessClass bpc) {
        SearchParam withdrawAppParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.WITHDRAW_APPLICATION_SEARCH_PARAM,InboxAppQueryDto.class.getName(),"CREATED_DT",SearchParam.DESCENDING,false);
        withdrawAppParam.addFilter("licenseeId", licenseeId, true);
//        ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE;
        String moduleStr = SqlHelper.constructInCondition("appType", 2);
        withdrawAppParam.addParam("appTypes", moduleStr);

        withdrawAppParam.addFilter("appType"+0, ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
        withdrawAppParam.addFilter("appType"+1, ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);

        withdrawAppParam.addFilter("appStatus",ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING, true);
        QueryHelp.setMainSql("interInboxQuery", "assessmentWithdrawAppQuery", withdrawAppParam);
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
        String appId = ParamUtil.getMaskedString(request, "withdrawAppId");
        String appNo = ParamUtil.getMaskedString(request, "withdrawAppNo");
        StringBuilder url = new StringBuilder();
        url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                .append(InboxConst.URL_LICENCE_WEB_MODULE + "MohWithdrawalApplication")
                .append("?withdrawAppId=")
                .append(MaskUtil.maskValue("withdrawAppId", appId))
                .append("&withdrawAppNo=")
                .append(MaskUtil.maskValue("withdrawAppNo", appNo));
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        bpc.response.sendRedirect(tokenUrl);
    }

    public void resumeDraftApp(BaseProcessClass bpc) {
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = loginContext.getLicenseeId();
        SearchParam draftAppSearchParam = HalpSearchResultHelper.gainSearchParam(bpc.request, GuideConsts.DRAFT_APPLICATION_SEARCH_PARAM,InboxAppQueryDto.class.getName(),"CREATED_DT",SearchParam.DESCENDING,false);
        draftAppSearchParam.addFilter("licenseeId", licenseeId, true);
        draftAppSearchParam.addFilter("appStatus", "APST008", true);

        QueryHelp.setMainSql("interInboxQuery", "applicationQuery", draftAppSearchParam);
        SearchResult<InboxAppQueryDto> draftAppSearchResult = inboxService.appDoQuery(draftAppSearchParam);

        if (!StringUtil.isEmpty(draftAppSearchResult)) {
            ParamUtil.setSessionAttr(bpc.request, GuideConsts.DRAFT_APPLICATION_SEARCH_PARAM, draftAppSearchParam);
            ParamUtil.setRequestAttr(bpc.request, GuideConsts.DRAFT_APPLICATION_SEARCH_RESULT, draftAppSearchResult);
        }
    }

    public void resumeDraftAppStep(BaseProcessClass bpc) throws IOException {
        log.debug("The prepareEdit start ...");
        HttpServletRequest request = bpc.request;
        String appNo = ParamUtil.getString(request, "draftAppNo");
        String appType = MasterCodeUtil.getCodeDesc(ParamUtil.getString(request, "draftAppType")).trim();
        if(InboxConst.APP_DO_DRAFT_TYPE_RFC.equals(appType)){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohRequestForChange/prepareDraft")
                    .append("?DraftNumber=")
                    .append(MaskUtil.maskValue("DraftNumber",appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
        }else if(InboxConst.APP_DO_DRAFT_TYPE_RENEW.equals(appType)){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohWithOutRenewal")
                    .append("?DraftNumber=")
                    .append(MaskUtil.maskValue("DraftNumber",appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
        }else if(InboxConst.APP_DO_DRAFT_TYPE_APPEAL.equals(appType)){
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohAppealApplication")
                    .append("?DraftNumber=")
                    .append(MaskUtil.maskValue("DraftNumber",appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
        }
        else {
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS).append(bpc.request.getServerName())
                    .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohNewApplication")
                    .append("?DraftNumber=")
                    .append(MaskUtil.maskValue("DraftNumber",appNo));
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
        }
    }

    public void subDateMoh(BaseProcessClass bpc) {

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


    public void doAmenfLicStep(BaseProcessClass bpc) throws IOException {
        String action = ParamUtil.getString(bpc.request, "guide_action_type");
        String licId = ParamUtil.getString(bpc.request, "amendLicenseId");
        String licIdValue = ParamUtil.getMaskedString(bpc.request, licId);
        if(licIdValue != null){
            Map<String, String> errorMap = inboxService.checkRfcStatus(licIdValue);
            if(errorMap.isEmpty()){
                StringBuilder url = new StringBuilder();
                url.append(InboxConst.URL_HTTPS)
                        .append(bpc.request.getServerName())
                        .append(InboxConst.URL_LICENCE_WEB_MODULE+"MohRequestForChange")
                        .append("?licenceId=")
                        .append(MaskUtil.maskValue("licenceId",licIdValue));
                String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
                bpc.response.sendRedirect(tokenUrl);
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

                ParamUtil.setRequestAttr(bpc.request,"licIsAmend",Boolean.TRUE);
                ParamUtil.setRequestAttr(bpc.request,InboxConst.LIC_ACTION_ERR_MSG,errorMap.get("errorMessage"));
            }
        }
    }

    public void updateAdminPers(BaseProcessClass bpc) throws IOException {
        HttpServletRequest request = bpc.request;
        StringBuilder url = new StringBuilder();
        url.append(InboxConst.URL_HTTPS).append(request.getServerName())
                .append(InboxConst.URL_LICENCE_WEB_MODULE + "MohRfcPersonnelList");
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), request);
        bpc.response.sendRedirect(tokenUrl);
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

}
