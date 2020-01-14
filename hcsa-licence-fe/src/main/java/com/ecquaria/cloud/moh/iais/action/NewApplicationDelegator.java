package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.base.FileType;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.VehNoValidator;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.ApplicationValidateDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.sql.SqlMap;
import com.ecquaria.sz.commons.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;
import sop.util.CopyUtil;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * NewApplicationDelegator
 *
 * @author suocheng
 * @date 9/23/2019
 */
@Delegator("newApplicationDelegator")
@Slf4j
public class NewApplicationDelegator {
    private static final String ERRORMAP_PREMISES = "errorMap_premises";
    public static final String CURRENTSERVICEID = "currentServiceId";
    public static final String CURRENTSVCCODE = "currentSvcCode";
    private static final String PREMISESTYPE = "premisesType";
    public static final String APPSUBMISSIONDTO = "AppSubmissionDto";
    public static final String OLDAPPSUBMISSIONDTO = "oldAppSubmissionDto";
    public static final String COMMONHCSASVCDOCCONFIGDTO = "commonHcsaSvcDocConfigDto";
    public static final String PREMHCSASVCDOCCONFIGDTO = "premHcsaSvcDocConfigDto";
    public static final String RELOADAPPGRPPRIMARYDOCMAP = "reloadAppGrpPrimaryDocMap";
    public static final String  APPGRPPRIMARYDOCERRMSGMAP = "appGrpPrimaryDocErrMsgMap";

    private static final String REQUESTINFORMATIONCONFIG  = "requestInformationConfig";
    public static final String ACKMESSAGE = "AckMessage";
    public static final String SERVICEALLPSNCONFIGMAP = "ServiceAllPsnConfigMap";
    public static final String FIRESTOPTION = "Please Select";
    
    //page name
    public static final String APPLICATION_PAGE_NAME_PREMISES                       = "APPPN01";
    public static final String APPLICATION_PAGE_NAME_PRIMARY                        = "APPPN02";  
    public static final String APPLICATION_SVC_PAGE_NAME_LABORATORY                 = "APPSPN01";
    public static final String APPLICATION_SVC_PAGE_NAME_GOVERNANCE_OFFICERS        = "APPSPN02";
    public static final String APPLICATION_SVC_PAGE_NAME_PRINCIPAL_OFFICERS         = "APPSPN03";
    public static final String APPLICATION_SVC_PAGE_NAME_DEPUTY_PRINCIPAL_OFFICERS  = "APPSPN04";
    public static final String APPLICATION_SVC_PAGE_NAME_DOCUMENT                   = "APPSPN05";
    public static final String APPLICATION_SVC_PAGE_NAME_SERVICE_PERSONNEL          = "APPSPN06";
    
    
    
    @Autowired
    private ServiceConfigService serviceConfigService;

    @Autowired
    private AppSubmissionService appSubmissionService;

    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do Start start ...."));
        AuditTrailHelper.auditFunction("hcsa-application", "hcsa application");
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, null);
        List<AppSvcPrincipalOfficersDto> list=new ArrayList<>();
        AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto=new AppSvcPrincipalOfficersDto();
        list.add(appSvcPrincipalOfficersDto);
        ParamUtil.setSessionAttr(bpc.request,"AppSvcPrincipalOfficersDto",(Serializable) list);
        
        //Primary Documents
        ParamUtil.setSessionAttr(bpc.request, COMMONHCSASVCDOCCONFIGDTO, null);
        ParamUtil.setSessionAttr(bpc.request, PREMHCSASVCDOCCONFIGDTO, null);
        ParamUtil.setSessionAttr(bpc.request, RELOADAPPGRPPRIMARYDOCMAP, null);

        //request For Information Loading
        ParamUtil.setSessionAttr(bpc.request,REQUESTINFORMATIONCONFIG,null);
        requestForChangeLoading(bpc);
        renewLicence(bpc);
        requestForInformationLoading(bpc);
        //for loading the draft by appId
        loadingDraft(bpc);
        //for loading Service Config
        boolean flag = loadingServiceConfig(bpc);
        if(flag){
            initSession(bpc);
        }
        log.debug(StringUtil.changeForLog("the do Start end ...."));
    }


    /**
     * StartStep: Prepare
     *
     * @param bpc
     * @throws
     */
    public void prepare(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do prepare start ...."));
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
        log.debug(StringUtil.changeForLog("the do prepare end ...."));
    }
    /**
     * StartStep: PreparePremises
     *
     * @param bpc
     * @throws
     */
    public void preparePremises(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do preparePremises start ...."));
        //get svcCode to get svcId
        List<HcsaServiceDto> hcsaServiceDtoList = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST);
        List<String> svcIds = new ArrayList<>();
        if (hcsaServiceDtoList != null) {
            hcsaServiceDtoList.forEach(item -> svcIds.add(item.getId()));
        }
        List premisesSelect = new ArrayList<SelectOption>();
        String loginId = "internet";
        //?
        List<AppGrpPremisesDto> list = serviceConfigService.getAppGrpPremisesDtoByLoginId(loginId);
        SelectOption sp0 = new SelectOption("-1", "Select One");
        premisesSelect.add(sp0);
        SelectOption sp1 = new SelectOption("newPremise", "Add a new premises");
        premisesSelect.add(sp1);
        if (list != null) {
            for (AppGrpPremisesDto item : list) {
                if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(item.getPremisesType())) {
                    SelectOption sp2 = new SelectOption(item.getId(), item.getAddress());
                    premisesSelect.add(sp2);
                    //todo:1231
                }
            }
        }
        //his to do
        List conveyancePremSel = new ArrayList<SelectOption>();
        SelectOption cps1 = new SelectOption("-1", "Select One");
        SelectOption cps2 = new SelectOption("newPremise", "Add a new premises");
        conveyancePremSel.add(cps1);
        conveyancePremSel.add(cps2);
        ParamUtil.setSessionAttr(bpc.request, "premisesSelect", (Serializable) premisesSelect);
        ParamUtil.setSessionAttr(bpc.request, "conveyancePremSel", (Serializable) conveyancePremSel);
        //get premises type
        if (svcIds.size() > 0) {
            log.debug(StringUtil.changeForLog("svcId not null"));
            Set<String> premisesType = serviceConfigService.getAppGrpPremisesTypeBySvcId(svcIds);
            ParamUtil.setSessionAttr(bpc.request, PREMISESTYPE, (Serializable) premisesType);
        }
        
        //todo continue
        //reload 
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        /*List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        if(!IaisCommonUtils.isEmpty(appGrpPremisesDtoList)){
            for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtoList){
                String wrkTimeFrom = appGrpPremisesDto.getWrkTimeFrom();
                String wrkTimeTo = appGrpPremisesDto.getWrkTimeTo();
                if(!StringUtil.isEmpty(wrkTimeFrom)){
                    LocalTime localTimeFrom = LocalTime.parse(wrkTimeFrom);
                    if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){
                        appGrpPremisesDto.setOnsiteStartHH(String.valueOf(localTimeFrom.getHour()));
                        appGrpPremisesDto.setOnsiteStartMM(String.valueOf(localTimeFrom.getMinute()));
                    }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
                        appGrpPremisesDto.setConStartHH(String.valueOf(localTimeFrom.getHour()));
                        appGrpPremisesDto.setConStartMM(String.valueOf(localTimeFrom.getMinute()));
                    }
                }
                if(!StringUtil.isEmpty(wrkTimeTo)){
                    LocalTime localTimeTo = LocalTime.parse(wrkTimeTo);
                    if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){
                        appGrpPremisesDto.setOnsiteEndHH(String.valueOf(localTimeTo.getHour()));
                        appGrpPremisesDto.setOnsiteEndMM(String.valueOf(localTimeTo.getMinute()));
                    }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
                        appGrpPremisesDto.setConEndHH(String.valueOf(localTimeTo.getHour()));
                        appGrpPremisesDto.setConEndMM(String.valueOf(localTimeTo.getMinute()));
                    }
                    
                }
                
                List<AppPremPhOpenPeriod> appPremPhOpenPeriods = appGrpPremisesDto.getAppPremPhOpenPeriodList();
                if(!IaisCommonUtils.isEmpty(appPremPhOpenPeriods)){
                    for(AppPremPhOpenPeriod appPremPhOpenPeriod:appPremPhOpenPeriods){
                        String start = appPremPhOpenPeriod.getStartFrom();
                        String end = appPremPhOpenPeriod.getStartFrom();
                        if(!StringUtil.isEmpty(start)){
                            LocalTime localTimeStart = LocalTime.parse(start);
                            if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){
                                appPremPhOpenPeriod.setOnsiteStartFromHH(String.valueOf(localTimeStart.getHour()));
                                appPremPhOpenPeriod.setOnsiteStartFromMM(String.valueOf(localTimeStart.getMinute()));
                            }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
                                appPremPhOpenPeriod.setConvStartFromHH(String.valueOf(localTimeStart.getHour()));
                                appPremPhOpenPeriod.setConvStartFromMM(String.valueOf(localTimeStart.getMinute()));
                            }
                        }
                        if(!StringUtil.isEmpty(end)){
                            LocalTime localTimeEnd = LocalTime.parse(end);
                            if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){
                                appPremPhOpenPeriod.setOnsiteEndToHH(String.valueOf(localTimeEnd.getHour()));
                                appPremPhOpenPeriod.setOnsiteEndToMM(String.valueOf(localTimeEnd.getMinute()));
                            }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
                                appPremPhOpenPeriod.setConvEndToHH(String.valueOf(localTimeEnd.getHour()));
                                appPremPhOpenPeriod.setConvEndToMM(String.valueOf(localTimeEnd.getMinute()));
                            }
                        }
                    }
                }
            }
        }*/
        log.debug(StringUtil.changeForLog("the do preparePremises end ...."));
    }

    /**
     * StartStep: PrepareDocuments
     *
     * @param bpc
     * @throws
     */
    public void prepareDocuments(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do prepareDocuments start ...."));

        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        List<HcsaSvcDocConfigDto> hcsaSvcDocDtos = serviceConfigService.getAllHcsaSvcDocs(null);
        if (hcsaSvcDocDtos != null) {
            List<HcsaSvcDocConfigDto> commonHcsaSvcDocConfigDto = new ArrayList<>();
            List<HcsaSvcDocConfigDto> premHcsaSvcDocConfigDto = new ArrayList<>();
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
            Map<String,AppGrpPrimaryDocDto> reloadDocMap = new HashMap();
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDocDtoList){
                reloadDocMap.put(appGrpPrimaryDocDto.getPrimaryDocReloadName(), appGrpPrimaryDocDto);
            }
            ParamUtil.setSessionAttr(bpc.request, RELOADAPPGRPPRIMARYDOCMAP, (Serializable) reloadDocMap);
        }else{
            ParamUtil.setSessionAttr(bpc.request, RELOADAPPGRPPRIMARYDOCMAP, (Serializable) new HashMap());
        }

        log.debug(StringUtil.changeForLog("the do prepareDocuments end ...."));
    }



    /**
     * StartStep: PrepareForms
     *
     * @param bpc
     * @throws
     */
    public void prepareForms(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do prepareForms start ...."));

        log.debug(StringUtil.changeForLog("the do prepareForms end ...."));
    }

    /**
     * StartStep: PreparePreview
     *
     * @param bpc
     * @throws
     */
    public void preparePreview(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do preparePreview start ...."));

        log.debug(StringUtil.changeForLog("the do preparePreview end ...."));
    }

    /**
     * StartStep: PreparePayment
     *
     * @param bpc
     * @throws
     */
    public void preparePayment(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do preparePayment start ...."));
        log.debug(StringUtil.changeForLog("the do preparePayment end ...."));
    }

    /**
     * StartStep: DoPremises
     *
     * @param bpc
     * @throws
     */
    public void doPremises(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doPremises start ...."));
        
        //gen dto
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        
        String isEdit = ParamUtil.getString(bpc.request, "isEdit");
        boolean isGetDataFromPage = isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_PREMISES_INFORMATION, isEdit);
        log.debug("isGetDataFromPage:"+isGetDataFromPage);
        if(isGetDataFromPage){
            List<AppGrpPremisesDto> appGrpPremisesDtoList = genAppGrpPremisesDtoList(bpc.request);
            appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtoList);
            if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())){
                Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null? new HashSet<>() :appSubmissionDto.getClickEditPage();
                clickEditPages.add(APPLICATION_PAGE_NAME_PREMISES);
                appSubmissionDto.setClickEditPage(clickEditPages);
            }
            
            Map<String, String> errorMap= doValidatePremiss(bpc);
            if(errorMap.size()>0){
                ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE,"premises");
                return;
            }

            ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        }
        

        log.debug(StringUtil.changeForLog("the do doPremises end ...."));
    }



    /**
     * StartStep: DoDocument
     *
     * @param bpc
     * @throws
     */
    public void doDocument(BaseProcessClass bpc) throws IOException {
        log.debug(StringUtil.changeForLog("the do doDocument start ...."));
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String crudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
        String crudActionValue = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_VALUE);

        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE, crudActionValue);

        AppGrpPrimaryDocDto appGrpPrimaryDocDto = null;
        CommonsMultipartFile file = null;
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        List<HcsaSvcDocConfigDto> commonHcsaSvcDocConfigList = (List<HcsaSvcDocConfigDto>) ParamUtil.getSessionAttr(bpc.request, COMMONHCSASVCDOCCONFIGDTO);
        List<HcsaSvcDocConfigDto> premHcsaSvcDocConfigList = (List<HcsaSvcDocConfigDto>) ParamUtil.getSessionAttr(bpc.request, PREMHCSASVCDOCCONFIGDTO);
        List<AppGrpPremisesDto> appGrpPremisesList = appSubmissionDto.getAppGrpPremisesDtoList();
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = new ArrayList<>();
        Map<String,String> errorMap = new HashMap<>();
        Map<String,AppGrpPrimaryDocDto> beforeReloadDocMap = (Map<String, AppGrpPrimaryDocDto>) ParamUtil.getSessionAttr(bpc.request, RELOADAPPGRPPRIMARYDOCMAP);

        Map<String,CommonsMultipartFile> commonsMultipartFileMap = new HashMap<>();
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
                if(comm.getIsMandatory()){
                    errorMap.put(name, "can not is empty");
                }
            }
        }
        for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesList){
            for(HcsaSvcDocConfigDto prem:premHcsaSvcDocConfigList){

                String premisesIndexNo = "";
                if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){
                    premisesIndexNo = appGrpPremisesDto.getHciName();
                }else  if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
                    premisesIndexNo = appGrpPremisesDto.getConveyanceVehicleNo();
                }
                String name = "prem"+prem.getId()+premisesIndexNo;
                file = (CommonsMultipartFile) mulReq.getFile(name);
                String delFlag = name+"flag";
                String delFlagValue =  mulReq.getParameter(delFlag);
                if(file != null && file.getSize() != 0){
                    if (!StringUtil.isEmpty(file.getOriginalFilename())) {
                        file.getFileItem().setFieldName("selectedFile");
                        appGrpPrimaryDocDto = new AppGrpPrimaryDocDto();
                        appGrpPrimaryDocDto.setSvcComDocId(prem.getId());
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
                    if(prem.getIsMandatory()) {
                        errorMap.put(name, "UC_CHKLMD001_ERR001");
                    }
                }
            }
        }
        //set value into AppSubmissionDto
        appSubmissionDto.setAppGrpPrimaryDocDtos(appGrpPrimaryDocDtoList);
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);

        // do by wenkang
        String crud_action_values = ParamUtil.getRequestString(bpc.request, "crud_action_value");
        if("next".equals(crud_action_values)){
            documentValid(bpc.request, errorMap);
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



        log.debug(StringUtil.changeForLog("the do doDocument end ...."));
    }


    private void isMantory(  List<HcsaSvcDocConfigDto> hcsaSvcDocDtos ,Map errorMap ){
        for(int i=0;i<hcsaSvcDocDtos.size();i++ ){
            Boolean isMandatory = hcsaSvcDocDtos.get(i).getIsMandatory();
            if(isMandatory){
                errorMap.put("documents","UC_CHKLMD001_ERR002");
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
        log.debug(StringUtil.changeForLog("the do doForms start ...."));

        log.debug(StringUtil.changeForLog("the do doForms end ...."));
    }

    /**
     * StartStep: doPreview
     *
     * @param bpc
     * @throws
     */
    public void doPreview(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doPreview start ...."));

        log.debug(StringUtil.changeForLog("the do doPreview end ...."));
    }

    /**
     * StartStep: doPreview
     *
     * @param bpc
     * @throws
     */
    public void doPayment(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doPayment start ...."));
        String switch2 = "loading";
        String pmtStatus = (String) ParamUtil.getRequestAttr(bpc.request, "PmtStatus");
        if(!StringUtil.isEmpty(pmtStatus) && "GIRO".equals(pmtStatus)){
            switch2 = "ack";
        }
        String result = bpc.request.getParameter("result");

        if (!StringUtil.isEmpty(result)) {
            log.debug(StringUtil.changeForLog("payment result:" + result));
            String pmtRefNo = bpc.request.getParameter("reqRefNo");
            if ("success".equals(result) && !StringUtil.isEmpty(pmtRefNo)) {
                switch2 = "ack";
                //update status
                AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
                String appGrpId = appSubmissionDto.getAppGrpId();
                ApplicationGroupDto appGrp = new ApplicationGroupDto();
                appGrp.setId(appGrpId);
                appGrp.setPmtRefNo(pmtRefNo);
                appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                serviceConfigService.updatePaymentStatus(appGrp);
            }
        }

        if("ack".equals(switch2)){
            ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, "payment success !!!");
        }

        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE, switch2);
        log.debug(StringUtil.changeForLog("the do doPayment end ...."));
    }

    /**
     * StartStep: doSaveDraft
     *
     * @param bpc
     * @throws
     */
    public void doSaveDraft(BaseProcessClass bpc) throws IOException {
        log.debug(StringUtil.changeForLog("the do doSaveDraft start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        if(StringUtil.isEmpty(appSubmissionDto.getDraftNo())){
            String draftNo = appSubmissionService.getDraftNo(appSubmissionDto.getAppType());
            log.debug(StringUtil.changeForLog("the draftNo -->:") + draftNo);
            appSubmissionDto.setDraftNo(draftNo);
        }
        appSubmissionDto = appSubmissionService.doSaveDraft(appSubmissionDto);
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        log.debug(StringUtil.changeForLog("the do doSaveDraft end ...."));
    }

    /**
     * StartStep: doRequestInformationSubmit
     *
     * @param bpc
     * @throws
     */
    public void doRequestInformationSubmit(BaseProcessClass bpc) throws IOException {
        log.debug(StringUtil.changeForLog("the do doRequestInformationSubmit start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, OLDAPPSUBMISSIONDTO);
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        oldAppSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        AppSubmissionRequestInformationDto appSubmissionRequestInformationDto = new AppSubmissionRequestInformationDto();
        appSubmissionRequestInformationDto.setAppSubmissionDto(appSubmissionDto);
        appSubmissionRequestInformationDto.setOldAppSubmissionDto(oldAppSubmissionDto);
        appSubmissionDto = appSubmissionService.submitRequestInformation(appSubmissionRequestInformationDto, bpc.process);
       // ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        log.debug(StringUtil.changeForLog("the do doRequestInformationSubmit end ...."));
    }

    /**
     * StartStep: doSubmit
     *
     * @param bpc
     * @throws
     */
    public void doSubmit(BaseProcessClass bpc) throws IOException {
        log.debug(StringUtil.changeForLog("the do doSubmit start ...."));
        //do validate
       // Map<String, Map<String, String>> validateResult = doValidate(bpc);
        //save the app and appGroup
        Map<String, String> map = doPreviewAndSumbit(bpc);
        if(!map.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request,"Msg",map);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE,"preview");
            return;
        }
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        String draftNo = appSubmissionDto.getDraftNo();
        if(StringUtil.isEmpty(draftNo)){
            draftNo = appSubmissionService.getDraftNo(appSubmissionDto.getAppType());
            appSubmissionDto.setDraftNo(draftNo);
        }
        //get appGroupNo
        String appGroupNo = appSubmissionService.getGroupNo(appSubmissionDto.getAppType());
        log.debug(StringUtil.changeForLog("the appGroupNo is -->:") + appGroupNo);
        appSubmissionDto.setAppGrpNo(appGroupNo);
        //get Amount
        Double amount = appSubmissionService.getGroupAmount(appSubmissionDto);
        log.debug(StringUtil.changeForLog("the amount is -->:") + amount);
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

        appSubmissionDto = appSubmissionService.submit(appSubmissionDto, bpc.process);
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);

        //get wrokgroup
        log.debug(StringUtil.changeForLog("the do doSubmit end ...."));
    }


    private Map<String,String> doPreviewAndSumbit( BaseProcessClass bpc){
        StringBuilder sB=new StringBuilder();
        Map<String,String> previewAndSubmitMap=new HashMap<>();
        //
        Map<String, String> premissMap = doValidatePremiss(bpc);
        if(!premissMap.isEmpty()){
            previewAndSubmitMap.put("premiss","UC_CHKLMD001_ERR001");
        }
        //
        Map<String, String> poMap = doValidatePo(bpc.request);
        if(!poMap.isEmpty()){
            previewAndSubmitMap.put("po","UC_CHKLMD001_ERR001");
        }
        //
        Map<String, String> govenMap = ClinicalLaboratoryDelegator.doValidateGovernanceOfficers(bpc.request);
        if(!govenMap.isEmpty()){
            previewAndSubmitMap.put("goven","UC_CHKLMD001_ERR001");

        }
        //
        Map<String, String> map = doCheckBox(bpc,sB);
        if(!map.isEmpty()){
            previewAndSubmitMap.putAll(map);

        }

        Map<String,String> documentMap=new HashMap<>();
        documentValid(bpc.request,documentMap);
        if(!documentMap.isEmpty()){
            previewAndSubmitMap.put("document","UC_CHKLMD001_ERR001");
        }

        if(!StringUtil.isEmpty(sB.toString())){
            previewAndSubmitMap.put("serviceId",sB.toString());
        }

        return previewAndSubmitMap;
    }

    //todo

    private Map<String,String> doCheckBox( BaseProcessClass bpc,StringBuilder sB){

        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        Map<String,String> errorMap=new HashMap<>();
        List<AppSvcRelatedInfoDto> dto = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        Map<String, List<HcsaSvcPersonnelDto>> allSvcAllPsnConfig = getAllSvcAllPsnConfig(bpc.request);
        for(int i=0;i< dto.size();i++ ){
            String serviceId = dto.get(i).getServiceId();
            List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = dto.get(i).getAppSvcLaboratoryDisciplinesDtoList();
            List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = allSvcAllPsnConfig.get(serviceId);
            dolabory(errorMap,appSvcLaboratoryDisciplinesDtoList,serviceId,sB);
            List<AppSvcCgoDto> appSvcCgoDtoList = dto.get(i).getAppSvcCgoDtoList();
            doAppSvcCgoDto(hcsaSvcPersonnelDtos,errorMap,appSvcCgoDtoList,serviceId,sB);
            List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList = dto.get(i).getAppSvcDisciplineAllocationDtoList();
            doSvcDis(errorMap,appSvcDisciplineAllocationDtoList,serviceId,sB);
            List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = dto.get(i).getAppSvcPrincipalOfficersDtoList();
            doPO(hcsaSvcPersonnelDtos,errorMap,appSvcPrincipalOfficersDtoList,serviceId,sB);
            List<AppSvcPersonnelDto> appSvcPersonnelDtoList = dto.get(i).getAppSvcPersonnelDtoList();
            doAppSvcPersonnelDtoList(hcsaSvcPersonnelDtos,errorMap,appSvcPersonnelDtoList,serviceId,sB);

        }


        return  errorMap;
    }

    private void dolabory(Map<String,String> map ,List<AppSvcLaboratoryDisciplinesDto> list,String serviceId, StringBuilder sB){
        if(list!=null&&list.isEmpty()){

        }
    }


    private void doAppSvcPersonnelDtoList(List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos ,Map map,List<AppSvcPersonnelDto> appSvcPersonnelDtos,String serviceId, StringBuilder sB){
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
                String qualification = appSvcPersonnelDtos.get(i).getQuaification();

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
                String quaification = appSvcPersonnelDtos.get(i).getQuaification();
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
    private void doAppSvcCgoDto(  List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos, Map map ,List<AppSvcCgoDto> list,String serviceId,StringBuilder sB){
     if(list==null){
         if(hcsaSvcPersonnelDtos!=null){
             for(HcsaSvcPersonnelDto every:hcsaSvcPersonnelDtos){
                 String psnType = every.getPsnType();
                 if(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnType)){
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
                if(!emailAddr.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")){
                    map.put("cgoemailAddr"+i,"CHKLMD001_ERR006");
                    flag =true;
                }
            }
            if( flag ){
                sB.append(serviceId);

            }

        }
    }


    private void doSvcDis( Map map ,List<AppSvcDisciplineAllocationDto> list,String serviceId,StringBuilder sB){
        if(list==null){

        }
    }

    private void doPO( List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos, Map map ,List<AppSvcPrincipalOfficersDto> list,String serviceId,StringBuilder sB){
     if(list==null){
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
        StringBuilder stringBuilder=new StringBuilder();

        for(int i=0;i<list.size();i++){
            StringBuilder stringBuilder1=new StringBuilder();
            String mobileNo = list.get(i).getMobileNo();
            String emailAddr = list.get(i).getEmailAddr();
            String modeOfMedAlert = list.get(i).getModeOfMedAlert();
            String psnType = list.get(i).getPsnType();
            String assignSelect = list.get(i).getAssignSelect();
            if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(psnType)){
                if(StringUtil.isEmpty(modeOfMedAlert)){
                    map.put("modeOfMedAlert"+i,"UC_CHKLMD001_ERR001");
                    flag=true;
                }
            }
            if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnType)){
                if(StringUtil.isEmpty(assignSelect)){
                    map.put("assignSelect"+i,"UC_CHKLMD001_ERR001");
                    flag=true;
                }
                String officeTelNo = list.get(i).getOfficeTelNo();
                if(StringUtil.isEmpty(officeTelNo)){
                    map.put("POofficeTelNo"+i,"UC_CHKLMD001_ERR001");
                    flag=true;
                }else {
                    if(!officeTelNo.matches("^[6][0-9]{7}$")){
                        map.put("POofficeTelNo"+i,"CHKLMD001_ERR007");
                        flag=true;
                    }
                }
            }
            if(StringUtil.isEmpty(emailAddr)){
                map.put("POeamilAddr"+i,"UC_CHKLMD001_ERR001");
                flag=true;
            }else {
                if (!emailAddr.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")){
                    map.put("POeamilAddr"+i,"CHKLMD001_ERR006");
                    flag=true;
                }
            }

            if(StringUtil.isEmpty(mobileNo)){
                map.put("POmobileNo"+i,"UC_CHKLMD001_ERR001");
                flag=true;
            }else {
                if(!mobileNo.matches("^[8|9][0-9]{7}$")){
                    map.put("POmobileNo"+i,"CHKLMD001_ERR004");
                    flag=true;
                }
            }

            String idType = list.get(i).getIdType();
            if(StringUtil.isEmpty(idType)){
                map.put("POidType"+i,"UC_CHKLMD001_ERR001");
                flag=true;
            }
            String idNo = list.get(i).getIdNo();
            if(StringUtil.isEmpty(idNo)){
                map.put("POidNo"+i,"UC_CHKLMD001_ERR001");
                flag=true;
            }else {
                //todo change is error
                if("FIN".equals(idType)){
                    boolean b = SgNoValidator.validateFin(idNo);
                    if(!b){
                        map.put("POidNo"+i,"CHKLMD001_ERR005");
                        flag=true;
                    }else {
                        stringBuilder1.append(idType).append(idNo);
                    }
                }
                if("NRIC".equals(idType)){
                    boolean b1 = SgNoValidator.validateNric(idNo);
                    if(!b1){
                        map.put("POidNo"+i,"CHKLMD001_ERR005");
                        flag=true;
                    }else {
                        stringBuilder1.append(idType).append(idNo);
                    }
                }

            }
            String salutation = list.get(i).getSalutation();
            if(StringUtil.isEmpty(salutation)){
                map.put("POsalutation"+i,"UC_CHKLMD001_ERR001");
                flag=true;
            }
            String designation = list.get(i).getDesignation();
            if(StringUtil.isEmpty(designation)){
                map.put("POdesignation"+i,"UC_CHKLMD001_ERR001");
                flag=true;
            }
            String name = list.get(i).getName();
            if(StringUtil.isEmpty(name)){
                map.put("POname"+i,"UC_CHKLMD001_ERR001");
                flag=true;
            }
            String s = stringBuilder.toString();
            if(!StringUtil.isEmpty(stringBuilder1.toString())){
                if(s.contains(stringBuilder1.toString())){
                    map.put("POidNo","error");
                    flag=true;
                }else {
                    stringBuilder.append(stringBuilder1.toString());
                }
            }

        }
        if(flag){
            sB.append(serviceId);

        }

    }

    /**
     * StartStep: ControlSwitch
     *
     * @param bpc
     * @throws
     */
    public void controlSwitch(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do controlSwitch start ...."));
        String switch2 = "loading";
        String crudActionValue = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        if (StringUtil.isEmpty(crudActionValue)) {
            crudActionValue = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        }
        Object requestInformationConfig = ParamUtil.getSessionAttr(bpc.request,REQUESTINFORMATIONCONFIG);
        if ("saveDraft".equals(crudActionValue) || "ack".equals(crudActionValue)) {
            switch2 = crudActionValue;
        }else if("doSubmit".equals(crudActionValue)){
             if(requestInformationConfig == null){
                 switch2 = crudActionValue;
             }else{
                 switch2 = "information";
             }
        }
        ParamUtil.setRequestAttr(bpc.request, "Switch2", switch2);
        log.debug(StringUtil.changeForLog("the do controlSwitch end ...."));

    }
    /**
     * StartStep: ControlSwitch
     *
     * @param bpc
     * @throws
     */
    public void jumpBank(BaseProcessClass bpc) throws IOException {
        log.debug(StringUtil.changeForLog("the do jumpBank start ...."));
        String payMethod = ParamUtil.getString(bpc.request, "payMethod");
        if(StringUtil.isEmpty(payMethod)){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE,"payment");
            return;
        }
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        if("Credit".equals(payMethod)){
            StringBuffer url = new StringBuffer();
            url.append("https://").append(bpc.request.getServerName())
                    .append("/payment-web/eservice/INTERNET/PaymentRequest")
                    .append("?amount=").append(appSubmissionDto.getAmount())
                    .append("&payMethod=").append(payMethod)
                    .append("&reqNo=").append(appSubmissionDto.getAppGrpNo());
            String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
            return;
        }else if("GIRO".equals(payMethod)){
            String appGrpId = appSubmissionDto.getAppGrpId();
            ApplicationGroupDto appGrp = new ApplicationGroupDto();
            appGrp.setId(appGrpId);
            appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PENDING_GIRO);
            serviceConfigService.updatePaymentStatus(appGrp);
            ParamUtil.setRequestAttr(bpc.request, "PmtStatus", "GIRO");
        }
        log.debug(StringUtil.changeForLog("the do jumpBank end ...."));
    }

    /**
     * StartStep: PrePareErrorAckPage
     *
     * @param bpc
     * @throws
     */
    public void prepareErrorAck(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepareErrorAck start ...."));

        log.debug(StringUtil.changeForLog("the do prepareErrorAck end ...."));
    }

    public void doErrorAck(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doErrorAck start ...."));


        log.debug(StringUtil.changeForLog("the do doErrorAck end ...."));
    }



    /**
     * StartStep: PrepareAckPage
     *
     * @param bpc
     * @throws
     */
    public void prepareAckPage(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do prepareAckPage start ...."));

        log.debug(StringUtil.changeForLog("the do prepareAckPage end ...."));
    }

    /**
     * @param
     * @description: ajax
     * @author: zixia
     */
    @RequestMapping(value = "/retrieve-address")
    public @ResponseBody PostCodeDto retrieveYourAddress(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the do loadPremisesByPostCode start ...."));
        String postalCode = ParamUtil.getDate(request, "postalCode");
        if(StringUtil.isEmpty(postalCode)){
            log.debug(StringUtil.changeForLog("postCode is null"));
            return null;
        }
        PostCodeDto postCodeDto = null;
        try {
            postCodeDto = serviceConfigService.getPremisesByPostalCode(postalCode);
        }catch (Exception e){
            log.debug(StringUtil.changeForLog("api exception"));
        }

        log.debug(StringUtil.changeForLog("the do loadPremisesByPostCode end ...."));
        return postCodeDto;
    }

    /**
     * @param
     * @description: ajax
     * @author: zixia
     */
    @RequestMapping(value = "/loadSvcBySvcId.do", method = RequestMethod.GET)
    public AppSvcRelatedInfoDto loadSvcInfoBySvcId(HttpServletRequest request) {
        String svcId = ParamUtil.getRequestString(request, "svcId");
        if (StringUtil.isEmpty(svcId)) {
            return null;
        }
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        for (AppSvcRelatedInfoDto appSvcDto : appSvcRelatedInfoDtoList) {
            if (svcId.equals(appSvcDto.getServiceId())) {
                //return this dto
            }

        }
        return null;
    }

    /**
     * @param
     * @description: ajax
     * @author: zixia
     */
    @RequestMapping(value = "/premises-html", method = RequestMethod.GET)
    public @ResponseBody String addPremisesHtml(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the add premises html start ...."));
        String currentLength = ParamUtil.getRequestString(request, "currentLength");
        log.debug(StringUtil.changeForLog("currentLength : "+currentLength));

        String sql = SqlMap.INSTANCE.getSql("premises", "premisesHtml").getSqlStr();
        Set<String> premType = (Set<String>) ParamUtil.getSessionAttr(request, PREMISESTYPE);
        StringBuffer premTypeBuffer = new StringBuffer();

        for(String type:premType){
            premTypeBuffer.append("<div class=\"col-xs-6 col-md-2\">")
                    .append("<div class=\"form-check\">")
                    .append("<input class=\"form-check-input premTypeRadio\"  type=\"radio\" name=\"premType"+currentLength+"\" value = "+type+" aria-invalid=\"false\">");
            if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(type)){
                premTypeBuffer.append(" <label class=\"form-check-label\" ><span class=\"check-circle\"></span>On-site</label>");
            }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(type)){
                premTypeBuffer.append(" <label class=\"form-check-label\" ><span class=\"check-circle\"></span>Conveyance</label>");
            }
            premTypeBuffer.append("</div>")
                    .append("</div>");
        }

        //premiseSelect -- on-site
        List<SelectOption> premisesOnSite= (List) ParamUtil.getSessionAttr(request, "premisesSelect");
        Map<String,String> premisesOnSiteAttr = new HashMap<>();
        premisesOnSiteAttr.put("class", "premSelect");
        premisesOnSiteAttr.put("id", "premOnsiteSel");
        premisesOnSiteAttr.put("name", "premOnSiteSelect");
        premisesOnSiteAttr.put("style", "display: none;");
        String premOnSiteSelectStr = generateDropDownHtml(premisesOnSiteAttr, premisesOnSite, null);

        //premiseSelect -- conveyance
        List<SelectOption> premisesConv= (List) ParamUtil.getSessionAttr(request, "conveyancePremSel");
        Map<String,String> premisesConvAttr = new HashMap<>();
        premisesConvAttr.put("class", "premSelect");
        premisesConvAttr.put("id", "premConSel");
        premisesConvAttr.put("name", "premConSelect");
        premisesConvAttr.put("style", "display: none;");
        String premConvSelectStr = generateDropDownHtml(premisesConvAttr, premisesConv, null);

        //Address Type on-site
        List<SelectOption> addrTypes= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ADDRESS_TYPE);
        Map<String,String> addrTypesAttr = new HashMap<>();
        addrTypesAttr.put("id", "siteAddressType");
        addrTypesAttr.put("name", "siteAddressType");
        addrTypesAttr.put("style", "display: none;");
        String addrTypeSelectStr = generateDropDownHtml(addrTypesAttr, addrTypes, FIRESTOPTION);

        //Address Type conveyance
        List<SelectOption> conAddrTypes= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ADDRESS_TYPE);
        Map<String,String> conAddrTypesAttr = new HashMap<>();
        conAddrTypesAttr.put("id", "siteAddressType");
        conAddrTypesAttr.put("name", "conveyanceAddrType");
        conAddrTypesAttr.put("style", "display: none;");
        String conAddrTypeSelectStr = generateDropDownHtml(conAddrTypesAttr, conAddrTypes, FIRESTOPTION);

        sql = sql.replace("(0)", currentLength);
        sql = sql.replace("(1)", premTypeBuffer.toString());
        sql = sql.replace("(2)", premOnSiteSelectStr);
        sql = sql.replace("(3)", premConvSelectStr);
        sql = sql.replace("(4)", addrTypeSelectStr);
        sql = sql.replace("(5)", conAddrTypeSelectStr);

        log.debug(StringUtil.changeForLog("the add premises html end ...."));
        return sql;
    }


    /**
     * @param
     * @description: ajax
     * @author: zixia
     */
    @RequestMapping(value = "/file-repo", method = RequestMethod.GET)
    public @ResponseBody void fileDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug(StringUtil.changeForLog("file-repo start ...."));
        String fileRepoName = ParamUtil.getRequestString(request, "fileRepoName");
        String maskFileRepoIdName = ParamUtil.getRequestString(request, "filerepo");
        String fileRepoId = ParamUtil.getMaskedString(request, maskFileRepoIdName);
        if(StringUtil.isEmpty(fileRepoId)){
            log.debug(StringUtil.changeForLog("file-repo id is empty"));
            return;
        }
        byte[] fileData =serviceConfigService.downloadFile(fileRepoId);
        response.addHeader("Content-Disposition", "attachment;filename=" + fileRepoName);
        response.addHeader("Content-Length", "" + fileData.length);
        response.setContentType("application/x-octet-stream");
        OutputStream ops = new BufferedOutputStream(response.getOutputStream());
        ops.write(fileData);
        ops.close();
        ops.flush();
        log.debug(StringUtil.changeForLog("file-repo end ...."));
    }
    
    

    /**
     * @param request
     * @return
     * @description: for the page validate call.
     */
    public ApplicationValidateDto getValueFromPage(HttpServletRequest request) {
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

    //=============================================================================
    //private method
    //=============================================================================
    private void loadingDraft(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do loadingDraft start ...."));
        String draftNo = (String) ParamUtil.getString(bpc.request, "DraftNumber");
        //draftNo = "DN191118000001";
        if(!StringUtil.isEmpty(draftNo)){
            log.debug(StringUtil.changeForLog("draftNo is not empty"));
            AppSubmissionDto appSubmissionDto = serviceConfigService.getAppSubmissionDtoDraft(draftNo);
            if(appSubmissionDto.getAppGrpPremisesDtoList() != null && appSubmissionDto.getAppGrpPremisesDtoList().size() >0){
                ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
            }else{
                ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, null);
            }

        }
        log.debug(StringUtil.changeForLog("the do loadingDraft end ...."));
    }
    private void requestForChangeLoading(BaseProcessClass bpc) throws CloneNotSupportedException{
        log.debug(StringUtil.changeForLog("the do requestForChangeLoading start ...."));
        String licenceId = ParamUtil.getString(bpc.request, "licenceId");
        String amendTypes = ParamUtil.getString(bpc.request, "amendTypes");
        if(!StringUtil.isEmpty(licenceId) && !StringUtil.isEmpty(amendTypes)){
            AppSubmissionDto appSubmissionDto = appSubmissionService.getAppSubmissionDtoByLicenceId(licenceId);
            String [] amendTypeArr= amendTypes.split(";");
            List<String> amendTypeList = new ArrayList<>();
            for(String type:amendTypeArr){
                amendTypeList.add(type);
            }
            
            appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
            appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_STATUS_REQUEST_FOR_CHANGE_AMEND);
            appSubmissionDto.setAmendTypes(amendTypeList);
            AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto)CopyUtil.copyMutableObject(appSubmissionDto);
            ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
            ParamUtil.setSessionAttr(bpc.request,OLDAPPSUBMISSIONDTO,oldAppSubmissionDto);
        }
        log.debug(StringUtil.changeForLog("the do requestForChangeLoading end ...."));
    }
    private void renewLicence(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do renewLicence start ...."));
        String licenceId = ParamUtil.getString(bpc.request, "licenceId");
        String type = ParamUtil.getString(bpc.request, "type");
        if(!StringUtil.isEmpty(licenceId) && ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(type)){
            AppSubmissionDto appSubmissionDto = appSubmissionService.getAppSubmissionDtoByLicenceId(licenceId);

            appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
            ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        }
        log.debug(StringUtil.changeForLog("the do renewLicence end ...."));
    }

    private void requestForInformationLoading(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do requestForInformationLoading start ...."));
        String appNo = ParamUtil.getString(bpc.request,"appNo");
        if(!StringUtil.isEmpty(appNo)){
            AppSubmissionDto appSubmissionDto = appSubmissionService.getAppSubmissionDtoByAppNo(appNo);
            AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto)CopyUtil.copyMutableObject(appSubmissionDto);
            ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
            ParamUtil.setSessionAttr(bpc.request,OLDAPPSUBMISSIONDTO,oldAppSubmissionDto);
            ParamUtil.setSessionAttr(bpc.request,REQUESTINFORMATIONCONFIG,"test");
        }
        log.debug(StringUtil.changeForLog("the do requestForInformationLoading end ...."));
    }

    private boolean loadingServiceConfig(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do loadingServiceConfig start ...."));
        //loading the service
        List<String> serviceConfigIds = new ArrayList<>();
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        if(appSubmissionDto != null ){
            // from draft,rfi
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if(appSvcRelatedInfoDtoList != null && appSvcRelatedInfoDtoList.size()>0){
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtoList){
                    serviceConfigIds.add(appSvcRelatedInfoDto.getServiceId());
                }
            }
        }else {

            List<String> baseServiceIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, "baseService");
            List<String> specifiedServiceIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, "specifiedService");
            if(baseServiceIds != null && !baseServiceIds.isEmpty()){
                for(String id:baseServiceIds){
                    serviceConfigIds.add(id);
                }
            }

            if(specifiedServiceIds != null && !specifiedServiceIds.isEmpty()){
                for(String id:specifiedServiceIds){
                    serviceConfigIds.add(id);
                }
            }
        }
        if(serviceConfigIds == null || serviceConfigIds.isEmpty()){
            log.debug(StringUtil.changeForLog("service id is empty"));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "errorAck");
            ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, "You have encountered some problems, please contact the administrator !!!");
            return false;
        }

        List<HcsaServiceDto> hcsaServiceDtoList = serviceConfigService.getHcsaServiceDtosById(serviceConfigIds);
        sortHcsaServiceDto(hcsaServiceDtoList);
        ParamUtil.setSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) hcsaServiceDtoList);
        log.debug(StringUtil.changeForLog("the do loadingServiceConfig end ...."));
        return true;
    }

    private void sortHcsaServiceDto(List<HcsaServiceDto> hcsaServiceDtoList) {
        List<HcsaServiceDto> baseList = new ArrayList();
        List<HcsaServiceDto> specifiedList = new ArrayList();
        List<HcsaServiceDto> subList = new ArrayList();
        List<HcsaServiceDto> otherList = new ArrayList();
        //class
        for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtoList) {
            switch (hcsaServiceDto.getSvcCode()) {
                case ApplicationConsts.SERVICE_CONFIG_TYPE_BASE:
                    baseList.add(hcsaServiceDto);
                    break;
                case ApplicationConsts.SERVICE_CONFIG_TYPE_SPECIFIED:
                    specifiedList.add(hcsaServiceDto);
                    break;
                case ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED:
                    subList.add(hcsaServiceDto);
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
        hcsaServiceDtoList = new ArrayList<>();
        hcsaServiceDtoList.addAll(baseList);
        hcsaServiceDtoList.addAll(specifiedList);
        hcsaServiceDtoList.addAll(subList);
        hcsaServiceDtoList.addAll(otherList);
    }

    private void sortService(List<HcsaServiceDto> list) {
        list.sort((h1, h2) -> h1.getSvcName().compareTo(h2.getSvcName()));
    }

/*    private Map<String, Map<String, String>> doValidate(BaseProcessClass bpc) {
        Map<String, Map<String, String>> reuslt = new HashMap<>();
        //do validate premiss
        Map<String, String> premises = doValidatePremiss(bpc);
        reuslt.put("premises", premises);
        return reuslt;
    }*/

    private Map<String, String> doValidatePremiss(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doValidatePremiss start ...."));
        //do validate one premiss

        Map<String, String> errorMap = new HashMap<>();
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        for(int i=0;i<appGrpPremisesDtoList.size();i++){
            String premiseType = appGrpPremisesDtoList.get(i).getPremisesType();
            if (StringUtil.isEmpty(premiseType)) {
                errorMap.put("premisesType"+i, "UC_CHKLMD001_ERR002");
            }else {
                String premisesSelect = appGrpPremisesDtoList.get(i).getPremisesSelect();
                if (StringUtil.isEmpty(premisesSelect) || "-1".equals(premisesSelect)) {
                    errorMap.put("premisesSelect"+i, "UC_CHKLMD001_ERR002");
                } else if ("newPremise".equals(premisesSelect)) {
                    if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premiseType)) {
                        String onsiteStartHH = appGrpPremisesDtoList.get(i).getOnsiteStartHH();
                        String onsiteStartMM = appGrpPremisesDtoList.get(i).getOnsiteStartMM();
                        int startDate=0;
                        int endDate=0;
                        if(StringUtil.isEmpty(onsiteStartHH)||StringUtil.isEmpty(onsiteStartMM)){
                            errorMap.put("onsiteStartMM"+i,"UC_CHKLMD001_ERR001");
                        }else {
                            try {
                                int i1 = Integer.parseInt(onsiteStartHH);
                                int i2= Integer.parseInt(onsiteStartMM);
                                startDate=  i1*60+i2*1;
                                if(i1>=24||i2>=60){
                                    errorMap.put("onsiteStartMM"+i,"UC_CHKLMD001_ERR001");
                                }
                            }catch (Exception e){
                                errorMap.put("onsiteStartMM"+i,"UC_CHKLMD001_ERR001");
                            }

                        }
                        String onsiteEndHH = appGrpPremisesDtoList.get(i).getOnsiteEndHH();
                        String onsiteEndMM = appGrpPremisesDtoList.get(i).getOnsiteEndMM();
                        if(StringUtil.isEmpty(onsiteEndHH)||StringUtil.isEmpty(onsiteEndMM)){
                            errorMap.put("onsiteEndMM"+i,"UC_CHKLMD001_ERR001");
                        }else {
                            try {
                                int i1 = Integer.parseInt(onsiteEndHH);
                                int i2 = Integer.parseInt(onsiteEndMM);
                                endDate=i1*60+i2*1;
                                if(i1>=24||i2>=60){
                                    errorMap.put("onsiteEndMM"+i,"UC_CHKLMD001_ERR001");
                                }
                            }catch (Exception e){
                                errorMap.put("onsiteEndMM"+i,"UC_CHKLMD001_ERR001");

                            }

                        }
                        if(endDate<startDate){
                            errorMap.put("onsiteEndMM"+i,"UC_CHKLMD001_ERR001");
                        }


                        String hciName = appGrpPremisesDtoList.get(i).getHciName();
                        if(StringUtil.isEmpty(hciName)){
                            errorMap.put("hciName"+i,"UC_CHKLMD001_ERR002");
                        }else {

                        }
                        String offTelNo = appGrpPremisesDtoList.get(i).getOffTelNo();
                        if(StringUtil.isEmpty(offTelNo)){
                            errorMap.put("offTelNo"+i,"UC_CHKLMD001_ERR002");
                        }else {
                            boolean matches = offTelNo.matches("^[6][0-9]{7}$");
                            if(!matches) {
                                errorMap.put("offTelNo"+i,"CHKLMD001_ERR007");
                            }
                        }

                        String streetName = appGrpPremisesDtoList.get(i).getStreetName();
                        if(StringUtil.isEmpty(streetName)){
                            errorMap.put("streetName"+i,"UC_CHKLMD001_ERR002");
                        }
                        //do by wenkang
                        String addrType = appGrpPremisesDtoList.get(i).getAddrType();
                        if(StringUtil.isEmpty(addrType)){
                            errorMap.put("addrType"+i, "UC_CHKLMD001_ERR002");
                        }else {
                            if (ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(addrType)) {
                                boolean empty = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getFloorNo());
                                boolean empty1 = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getBlkNo());
                                boolean empty2 = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getUnitNo());
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
                        }
                        String postalCode = appGrpPremisesDtoList.get(i).getPostalCode();
                        if (!StringUtil.isEmpty(postalCode)) {
                            if (!postalCode.matches("^[0-9]{6}$")) {
                                errorMap.put("postalCode"+i, "CHKLMD001_ERR003");
                            }
                        }else {
                            errorMap.put("postalCode"+i, "UC_CHKLMD001_ERR001");
                        }
                        
                        try {
                            LocalTime wrkTimeFrom = LocalTime.of(Integer.parseInt(appGrpPremisesDtoList.get(i).getOnsiteStartHH()), Integer.parseInt(appGrpPremisesDtoList.get(i).getOnsiteStartMM()));
                            LocalTime wrkTimeTo = LocalTime.of(Integer.parseInt(appGrpPremisesDtoList.get(i).getOnsiteEndHH()),Integer.parseInt(appGrpPremisesDtoList.get(i).getOnsiteEndMM()) );
                            appGrpPremisesDtoList.get(i).setWrkTimeFrom(Time.valueOf(wrkTimeFrom));
                            appGrpPremisesDtoList.get(i).setWrkTimeTo(Time.valueOf(wrkTimeTo));
                            
                        }catch (Exception e){
                            log.error(StringUtil.changeForLog("number formatter exception..."));
                        }
                        
                    } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premiseType)) {
                        String conStartHH = appGrpPremisesDtoList.get(i).getConStartHH();
                        String conStartMM = appGrpPremisesDtoList.get(i).getConStartMM();
                        int conStartDate=0;
                        int conEndDate=0;
                        if(StringUtil.isEmpty(conStartHH)||StringUtil.isEmpty(conStartMM)){
                            errorMap.put("conStartMM"+i,"UC_CHKLMD001_ERR001");
                        }else {
                            try {
                                int i1 = Integer.parseInt(conStartHH);
                                int i2= Integer.parseInt(conStartMM);
                                conStartDate=i1*60+i2*1;
                                if(i1>=24||i2>=60){
                                    errorMap.put("onsiteStartMM"+i,"UC_CHKLMD001_ERR001");
                                }
                            }catch (Exception e){
                                errorMap.put("onsiteStartMM"+i,"UC_CHKLMD001_ERR001");
                            }
                        }
                        String conEndHH = appGrpPremisesDtoList.get(i).getConEndHH();
                        String conEndMM = appGrpPremisesDtoList.get(i).getConEndMM();
                        if(StringUtil.isEmpty(conEndHH)||StringUtil.isEmpty(conEndMM)){
                            errorMap.put("conEndMM"+i,"UC_CHKLMD001_ERR001");
                        }else {

                            try {
                                int i1 = Integer.parseInt(conEndHH);
                                int i2 = Integer.parseInt(conEndMM);
                                conEndDate=i1*60+i2*1;
                                if(i1>=24||i2>=60){

                                    errorMap.put("conEndMM"+i,"UC_CHKLMD001_ERR001");
                                }

                            }catch (Exception e){
                                errorMap.put("conEndMM"+i,"UC_CHKLMD001_ERR001");
                            }
                        }
                        if(conEndDate<conStartDate){
                            errorMap.put("conEndMM"+i,"UC_CHKLMD001_ERR001");
                        }
                        String conveyanceVehicleNo = appGrpPremisesDtoList.get(i).getConveyanceVehicleNo();
                        if(StringUtil.isEmpty(conveyanceVehicleNo)){
                            errorMap.put("conveyanceVehicleNo"+i,"UC_CHKLMD001_ERR001");
                        }else {
                            boolean b = VehNoValidator.validateNumber(conveyanceVehicleNo);
                            if(!b){
                                errorMap.put("conveyanceVehicleNo"+i,"CHKLMD001_ERR008");
                            }
                        }
                        String conveyancePostalCode = appGrpPremisesDtoList.get(i).getConveyancePostalCode();
                        if(StringUtil.isEmpty(conveyancePostalCode)){
                            errorMap.put("conveyancePostalCode"+i,"UC_CHKLMD001_ERR001");
                        }else {
                            if(!conveyancePostalCode.matches("^[0-9]{6}$")){
                                errorMap.put("conveyancePostalCode"+i, "CHKLMD001_ERR003");
                            }
                        }

                        String cStreetName = appGrpPremisesDtoList.get(i).getConveyanceStreetName();

                        if(StringUtil.isEmpty(cStreetName)){
                            errorMap.put("conveyanceStreetName"+i,"UC_CHKLMD001_ERR001");
                        }
                        String conveyanceAddressType = appGrpPremisesDtoList.get(i).getConveyanceAddressType();
                        if(StringUtil.isEmpty(conveyanceAddressType)){
                            errorMap.put("conveyanceAddressType"+i, "UC_CHKLMD001_ERR002");
                        }else {
                            if (ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(conveyanceAddressType)) {
                                boolean empty = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getConveyanceFloorNo());
                                boolean empty1 = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getConveyanceBlockNo());
                                boolean empty2 = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getConveyanceUnitNo());
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
                        }
                        try {
                            LocalTime wrkTimeFrom = LocalTime.of(Integer.parseInt(appGrpPremisesDtoList.get(i).getConStartHH()), Integer.parseInt(appGrpPremisesDtoList.get(i).getConStartMM()));
                            LocalTime wrkTimeTo = LocalTime.of(Integer.parseInt(appGrpPremisesDtoList.get(i).getConEndHH()),Integer.parseInt(appGrpPremisesDtoList.get(i).getConEndMM()) );
                            appGrpPremisesDtoList.get(i).setWrkTimeFrom(Time.valueOf(wrkTimeFrom));
                            appGrpPremisesDtoList.get(i).setWrkTimeTo(Time.valueOf(wrkTimeTo));

                        }catch (Exception e){
                            log.error(StringUtil.changeForLog("number formatter exception..."));
                        }
                        
                    }
                } else {
                    //premiseSelect = organization hci code

                }
            }

        }
        log.debug(StringUtil.changeForLog("the do doValidatePremiss end ...."));

        return errorMap;
    }


    public static Map<String,  String> doValidatePo(HttpServletRequest request) {
        List<AppSvcPrincipalOfficersDto> poDto = (List<AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(request, "AppSvcPrincipalOfficersDto");
        Map<String, String> oneErrorMap = new HashMap<>();
        StringBuilder stringBuilder =new StringBuilder();
        for (int i=0;i< poDto.size();i++) {
            StringBuilder stringBuilder1 =new StringBuilder();
            String psnType = poDto.get(i).getPsnType();
            if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnType)){
                String assignSelect = poDto.get(i).getAssignSelect();
                if (StringUtil.isEmpty(assignSelect)) {
                    oneErrorMap.put("assignSelect", "UC_CHKLMD001_ERR002");
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
                        oneErrorMap.put("idType","UC_CHKLMD001_ERR001");
                    }
                    if(StringUtil.isEmpty(name)){
                        oneErrorMap.put("name","UC_CHKLMD001_ERR001");
                    }
                    if(StringUtil.isEmpty(salutation)){
                        oneErrorMap.put("salutation","UC_CHKLMD001_ERR001");
                    }
                    if(StringUtil.isEmpty(designation)){
                        oneErrorMap.put("designation","UC_CHKLMD001_ERR001");
                    }
                    if(!StringUtil.isEmpty(idNo)){
                        if("FIN".equals(idType)){
                            boolean b = SgNoValidator.validateFin(idNo);
                            if(!b){
                                oneErrorMap.put("NRICFIN","CHKLMD001_ERR005");
                            }else {
                                stringBuilder1.append(idType).append(idNo);
                            }
                        }
                        if("NRIC".equals(idType)){
                            boolean b1 = SgNoValidator.validateNric(idNo);
                            if(!b1){
                                oneErrorMap.put("NRICFIN","CHKLMD001_ERR005");
                            }else {
                                stringBuilder1.append(idType).append(idNo);
                            }
                        }
                    }else {
                        oneErrorMap.put("NRICFIN","UC_CHKLMD001_ERR001");
                    }
                    if(!StringUtil.isEmpty(mobileNo)){
                        if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                            oneErrorMap.put("mobileNo", "CHKLMD001_ERR004");
                        }
                    }else {
                        oneErrorMap.put("mobileNo", "UC_CHKLMD001_ERR001");
                    }
                    if(!StringUtil.isEmpty(emailAddr)) {
                        if (!emailAddr.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
                            oneErrorMap.put("emailAddr", "CHKLMD001_ERR006");
                        }
                    }else {
                        oneErrorMap.put("emailAddr", "UC_CHKLMD001_ERR001");
                    }
                    if(!StringUtil.isEmpty(officeTelNo)) {
                        if (!officeTelNo.matches("^[6][0-9]{7}$")) {
                            oneErrorMap.put("officeTelNo", "CHKLMD001_ERR007");
                        }
                    }else {
                        oneErrorMap.put("officeTelNo", "UC_CHKLMD001_ERR001");
                    }
                }
            }

            if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(psnType)){
                String salutation = poDto.get(i).getSalutation();
                String name = poDto.get(i).getName();
                String idType = poDto.get(i).getIdType();
                String mobileNo = poDto.get(i).getMobileNo();
                String emailAddr = poDto.get(i).getEmailAddr();
                String idNo = poDto.get(i).getIdNo();
                String modeOfMedAlert = poDto.get(i).getModeOfMedAlert();
                String designation = poDto.get(i).getDesignation();
                if(StringUtil.isEmpty(modeOfMedAlert)||"-1".equals(modeOfMedAlert)){
                    oneErrorMap.put("modeOfMedAlert","UC_CHKLMD001_ERR001");
                }

                if(StringUtil.isEmpty(designation)||"-1".equals(designation)){
                    oneErrorMap.put("deputyDesignation","UC_CHKLMD001_ERR001");
                }
                if(StringUtil.isEmpty(salutation)){
                    oneErrorMap.put("deputySalutation","UC_CHKLMD001_ERR001");
                }

                if(StringUtil.isEmpty(idType)||"-1".equals(idType)){
                    oneErrorMap.put("deputyIdType","UC_CHKLMD001_ERR001");
                }
                if(StringUtil.isEmpty(name)){
                    oneErrorMap.put("deputyName","UC_CHKLMD001_ERR001");
                }
                if(StringUtil.isEmpty(idNo)){
                    oneErrorMap.put("deputyIdNo","UC_CHKLMD001_ERR001");
                }
                if("FIN".equals(idType)){
                    boolean b = SgNoValidator.validateFin(idNo);
                    if(!b){
                        oneErrorMap.put("deputyIdNo","CHKLMD001_ERR005");
                    }else {
                        stringBuilder1.append(idType).append(idNo);
                    }
                }
                if("NRIC".equals(idType)){
                    boolean b1 = SgNoValidator.validateNric(idNo);
                    if(!b1){
                        oneErrorMap.put("deputyIdNo","CHKLMD001_ERR005");
                    }else {
                        stringBuilder1.append(idType).append(idNo);
                    }
                }

                if(StringUtil.isEmpty(mobileNo)){
                    oneErrorMap.put("deputyMobileNo","UC_CHKLMD001_ERR001");
                }
                else {
                    if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                        oneErrorMap.put("deputyMobileNo", "CHKLMD001_ERR004");
                    }
                }
                if(StringUtil.isEmpty(emailAddr)){
                    oneErrorMap.put("deputyEmailAddr","UC_CHKLMD001_ERR001");
                }else {
                    if (!emailAddr.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
                        oneErrorMap.put("deputyEmailAddr", "CHKLMD001_ERR006");
                    }
                }


            }
            String s = stringBuilder.toString();

            if(!StringUtil.isEmpty(stringBuilder1.toString())){
                if(s.contains(stringBuilder1.toString())){

                    oneErrorMap.put("NRICFIN","UC_CHKLMD001_ERR002");

                }else {
                    stringBuilder.append(stringBuilder1.toString());
                }

            }

        }
        return oneErrorMap;
    }

    private Map<String,String> doValidatePremissCgo(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doValidatePremiss start ...."));
        //do validate premiss
        Map<String,String> errorMap = new HashMap<>();
        AppSvcCgoDto appSvcCgoDto=  (AppSvcCgoDto) ParamUtil.getSessionAttr(bpc.request,"AppSvcCgoDto");
        String mobileNo = appSvcCgoDto.getMobileNo();
        String emailAddr = appSvcCgoDto.getEmailAddr();
        if(!mobileNo.startsWith("8")||!mobileNo.startsWith("9")){
            errorMap.put("mobileNo","Please key in a valid mobile number");
        }
        if(!emailAddr.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")){
            errorMap.put("emailAddr","Please key in a valid email address");
        }
        log.debug(StringUtil.changeForLog("the do doValidatePremiss end ...."));
        return errorMap;
    }

    /**
     * @description: get data from page
     * @author: zixian
     * @date: 11/6/2019 5:05 PM
     * @param: request
     * @return: AppGrpPremisesDto
     */
    public List<AppGrpPremisesDto> genAppGrpPremisesDtoList(HttpServletRequest request){
        List<AppGrpPremisesDto> appGrpPremisesDtoList = new ArrayList<>();
        int count = 0;
        String [] premisesType = ParamUtil.getStrings(request, "premType");
        if(premisesType != null){
            count = premisesType.length;
        }
        //onsite
        String [] premisesSelect = ParamUtil.getStrings(request, "premOnSiteSelect");
        String [] hciName = ParamUtil.getStrings(request, "hciName");
        String [] postalCode = ParamUtil.getStrings(request,  "postalCode");
        String [] blkNo = ParamUtil.getStrings(request, "blkNo");
        String [] streetName = ParamUtil.getStrings(request, "streetName");
        String [] floorNo = ParamUtil.getStrings(request, "floorNo");
        String [] unitNo = ParamUtil.getStrings(request, "unitNo");
        String [] buildingName = ParamUtil.getStrings(request, "buildingName");
        String [] siteAddressType = ParamUtil.getStrings(request, "siteAddressType");
        String [] offTelNo= ParamUtil.getStrings(request,"offTelNo");
        String [] scdfRefNo = ParamUtil.getStrings(request, "scdfRefNo");
        String [] onsiteStartHH = ParamUtil.getStrings(request, "onsiteStartHH");
        String [] onsiteStartMM = ParamUtil.getStrings(request, "onsiteStartMM");
        String [] onsiteEndHHS = ParamUtil.getStrings(request, "onsiteEndHH");
        String [] onsiteEndMMS = ParamUtil.getStrings(request, "onsiteEndMM");
        String [] fireSafetyCertIssuedDateStr  = ParamUtil.getStrings(request, "fireSafetyCertIssuedDate");
        String [] isOtherLic = ParamUtil.getStrings(request, "isOtherLic");
        //conveyance
        String [] conPremisesSelect = ParamUtil.getStrings(request, "premConSelect");
        String [] conVehicleNo = ParamUtil.getStrings(request, "conveyanceVehicleNo");
        String [] conPostalCode = ParamUtil.getStrings(request,  "conveyancePostalCode");
        String [] conBlkNo = ParamUtil.getStrings(request, "conveyanceBlockNo");
        String [] conStreetName = ParamUtil.getStrings(request, "conveyanceStreetName");
        String [] conFloorNo = ParamUtil.getStrings(request, "conveyanceFloorNo");
        String [] conUnitNo = ParamUtil.getStrings(request, "conveyanceUnitNo");
        String [] conBuildingName = ParamUtil.getStrings(request, "conveyanceBuildingName");
        String [] conSiteAddressType = ParamUtil.getStrings(request, "conveyanceAddrType");
        String [] conStartHH = ParamUtil.getStrings(request, "conStartHH");
        String [] conStartMM = ParamUtil.getStrings(request, "conStartMM");
        String [] conEndHHS = ParamUtil.getStrings(request, "conEndHH");
        String [] conEndMMS = ParamUtil.getStrings(request, "conEndMM");
        //every prem's ph length
        String [] phLength = ParamUtil.getStrings(request,"phLength");
        String [] premValue = ParamUtil.getStrings(request, "premValue");
        for(int i =0 ; i<count;i++){
            AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
            appGrpPremisesDto.setPremisesType(premisesType[i]);
            List<AppPremPhOpenPeriodDto> appPremPhOpenPeriods = new ArrayList<>();
            int length = 0;
            try {
                length = Integer.parseInt(phLength[i]);
            }catch (Exception e){
                log.info(StringUtil.changeForLog("length can not parse to int"));
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
                appGrpPremisesDto.setIsOtherLic(isOtherLic[i]);
                for(int j =0; j<length; j++){
                    AppPremPhOpenPeriodDto appPremPhOpenPeriod = new AppPremPhOpenPeriodDto();
                    String onsitePubHolidayName = premValue[i]+"onsitePubHoliday"+j;
                    String onsitePbHolDayStartHHName = premValue[i]+"onsitePbHolDayStartHH"+j;
                    String onsitePbHolDayStartMMName = premValue[i]+"onsitePbHolDayStartMM"+j;
                    String onsitePbHolDayEndHHName = premValue[i]+"onsitePbHolDayEndHH"+j;
                    String onsitePbHolDayEndMMName = premValue[i]+"onsitePbHolDayEndMM"+j;
                    
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
                    appPremPhOpenPeriods.add(appPremPhOpenPeriod);
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
                    String convPubHolidayName = premValue[i]+"convPubHoliday"+j;
                    String convPbHolDayStartHHName = premValue[i]+"convPbHolDayStartHH"+j;
                    String convPbHolDayStartMMName = premValue[i]+"convPbHolDayStartMM"+j;
                    String convPbHolDayEndHHName = premValue[i]+"convPbHolDayEndHH"+j;
                    String convPbHolDayEndMMName = premValue[i]+"convPbHolDayEndMM"+j;

                    String convPubHoliday = ParamUtil.getDate(request, convPubHolidayName);
                    String convPbHolDayStartHH = ParamUtil.getString(request, convPbHolDayStartHHName);
                    String convPbHolDayStartMM = ParamUtil.getString(request, convPbHolDayStartMMName);
                    String convPbHolDayEndHH = ParamUtil.getString(request, convPbHolDayEndHHName);
                    String convPbHolDayEndMM = ParamUtil.getString(request, convPbHolDayEndMMName);
                    appPremPhOpenPeriod.setPhDateStr(convPubHoliday);
                    Date phDate = DateUtil.parseDate(convPubHoliday, "dd/mm/yyyy");
                    appPremPhOpenPeriod.setPhDate(phDate);
                    appPremPhOpenPeriod.setConvStartFromHH(convPbHolDayStartHH);
                    appPremPhOpenPeriod.setConvStartFromMM(convPbHolDayStartMM);
                    appPremPhOpenPeriod.setConvEndToHH(convPbHolDayEndHH);
                    appPremPhOpenPeriod.setConvEndToMM(convPbHolDayEndMM);
                    appPremPhOpenPeriods.add(appPremPhOpenPeriod);
                }
            }
            appGrpPremisesDto.setAppPremPhOpenPeriodList(appPremPhOpenPeriods);
            appGrpPremisesDtoList.add(appGrpPremisesDto);
        }
        return  appGrpPremisesDtoList;
    }

    private AppSubmissionDto getAppSubmissionDto(HttpServletRequest request){
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, APPSUBMISSIONDTO);
        if(appSubmissionDto == null){
            log.debug(StringUtil.changeForLog("appSubmissionDto is empty "));
            appSubmissionDto = new AppSubmissionDto();
        }
        return appSubmissionDto;
    }

    private void initSession(BaseProcessClass bpc){
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        if(appSubmissionDto == null){
            appSubmissionDto = new AppSubmissionDto();
            appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
            List<AppGrpPremisesDto> appGrpPremisesDtoList = new ArrayList<>();
            AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
            appGrpPremisesDtoList.add(appGrpPremisesDto);
            appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtoList);
            List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request,  AppServicesConsts.HCSASERVICEDTOLIST);
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = new ArrayList<>();
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
        }
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        ParamUtil.setSessionAttr(bpc.request, "IndexNoCount", 0);

        //reload
        Map<String,AppGrpPrimaryDocDto> initBeforeReloadDocMap = new HashMap<>();
        ParamUtil.setSessionAttr(bpc.request, RELOADAPPGRPPRIMARYDOCMAP, (Serializable) initBeforeReloadDocMap);

        //error_msg
        ParamUtil.setSessionAttr(bpc.request, ERRORMAP_PREMISES, null);
        ParamUtil.setSessionAttr(bpc.request, APPGRPPRIMARYDOCERRMSGMAP, null);

        //init svc conifg
        Map<String,List<HcsaSvcPersonnelDto>> svcConfigInfo =  null;
        ParamUtil.setSessionAttr(bpc.request, SERVICEALLPSNCONFIGMAP, (Serializable) svcConfigInfo);

    }

    private void  documentValid(HttpServletRequest request, Map<String,String> errorMap){
        log.debug(StringUtil.changeForLog("the do doValidatePremiss start ...."));
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
            if(length>4*1024*1024){
                errorMap.put(keyName,"File size is too large!");
                continue;
            }
            Boolean flag=false;
            String name = appGrpPrimaryDocDto.getDocName();
            String substring = name.substring(name.lastIndexOf(".")+1);
            FileType[] fileType = FileType.values();
            for(FileType f:fileType){
                if(f.name().equalsIgnoreCase(substring)){
                    flag=true;
                }
            }
            if(!flag){
                errorMap.put(keyName,"Wrong file type");
            }
        }
    }

    /**
     * @description:
     * @author: zixian
     * @date: 12/9/2019 2:47 PM
     * @param:  [baseDropDown] --- DropDown Html    [category] --Master Code Categorys
     * @return: dropdown html
     */
    public String generateDropDownHtml(String baseDropDown, String category, String firestOption){
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append(baseDropDown)
                .append("<div class=\"nice-select input-large\" tabindex=\"0\">")
                .append("<span class=\"current\">"+firestOption+"</span>")
                .append("<ul class=\"list mCustomScrollbar _mCS_3 mCS_no_scrollbar\">")
                .append("<div id=\"mCSB_3\" class=\"mCustomScrollBox mCS-light mCSB_vertical mCSB_inside\" tabindex=\"0\" style=\"max-height: none;\">")
                .append("<div id=\"mCSB_3_container\" class=\"mCSB_container mCS_y_hidden mCS_no_scrollbar_y\" style=\"position:relative; top:0; left:0;\" dir=\"ltr\">")
                .append("<li data-value=\"-1\" class=\"option selected\">"+firestOption+"</li>");
        List<SelectOption> selectOptionList= MasterCodeUtil.retrieveOptionsByCate(category);
        for(SelectOption kv:selectOptionList){
            sBuffer.append(" <li data-value="+kv.getValue()+" class=\"option\">"+kv.getText()+"</li>");
        }
        sBuffer.append("</div>")
                .append("<div id=\"mCSB_3_scrollbar_vertical\" class=\"mCSB_scrollTools mCSB_3_scrollbar mCS-light mCSB_scrollTools_vertical\" style=\"display: none;\">")
                .append("<div class=\"mCSB_draggerContainer\">")
                .append(" <div id=\"mCSB_3_dragger_vertical\" class=\"mCSB_dragger\" style=\"position: absolute; min-height: 30px; top: 0px;\">")
                .append(" <div class=\"mCSB_dragger_bar\" style=\"line-height: 30px;\"></div>")
                .append("</div>")
                .append("<div class=\"mCSB_draggerRail\"></div>")
                .append("</div>")
                .append("</div>")
                .append("</div>")
                .append("</ul>")
                .append("</div>");
        return sBuffer.toString();
    }

    public static String generateDropDownHtml(Map<String, String> premisesOnSiteAttr, List<SelectOption> selectOptionList, String firestOption){
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("<select ");
        for(Map.Entry<String, String> entry : premisesOnSiteAttr.entrySet()){
            sBuffer.append(entry.getKey()+"=\""+entry.getValue()+"\" ");
        }
        sBuffer.append(" >");
        for(SelectOption sp:selectOptionList){
            sBuffer.append("<option value=\""+sp.getValue()+"\">"+ sp.getText() +"</option>");
        }
        sBuffer.append("</select>");
        sBuffer.append("<div class=\"nice-select premSelect\" tabindex=\"0\">");
        if(StringUtil.isEmpty(firestOption)){
            sBuffer.append("<span class=\"current\">"+selectOptionList.get(0).getText()+"</span>");
        }else {
            sBuffer.append("<span class=\"current\">"+firestOption+"</span>");
        }
        sBuffer.append("<ul class=\"list mCustomScrollbar _mCS_2 mCS_no_scrollbar\">")
                .append("<div id=\"mCSB_2\" class=\"mCustomScrollBox mCS-light mCSB_vertical mCSB_inside\" tabindex=\"0\" style=\"max-height: none;\">")
                .append("<div id=\"mCSB_2_container\" class=\"mCSB_container mCS_y_hidden mCS_no_scrollbar_y\" style=\"position:relative; top:0; left:0;\" dir=\"ltr\">");
        if(!StringUtil.isEmpty(firestOption)){
            sBuffer.append("<li data-value=\"-1\" class=\"option selected\">"+firestOption+"</li>");
        }
        for(SelectOption kv:selectOptionList){
            sBuffer.append(" <li data-value=\""+kv.getValue()+"\" class=\"option\">"+kv.getText()+"</li>");
        }
        sBuffer.append("</div>")
                .append("<div id=\"mCSB_2_scrollbar_vertical\" class=\"mCSB_scrollTools mCSB_2_scrollbar mCS-light mCSB_scrollTools_vertical\" style=\"display: none;\">")
                .append("<div class=\"mCSB_draggerContainer\">")
                .append("<div id=\"mCSB_2_dragger_vertical\" class=\"mCSB_dragger\" style=\"position: absolute; min-height: 30px; top: 0px; height: 0px;\">")
                .append("<div class=\"mCSB_dragger_bar\" style=\"line-height: 30px;\">")
                .append("</div>")
                .append("</div>")
                .append("<div class=\"mCSB_draggerRail\"></div>")
                .append("</div>")
                .append("</div>")
                .append("</div>")
                .append("</ul>")
                .append("</div>");
        return sBuffer.toString();
    }

    public Map<String,List<HcsaSvcPersonnelDto>> getAllSvcAllPsnConfig(HttpServletRequest request){
        Map<String,List<HcsaSvcPersonnelDto>> svcAllPsnConfig = (Map<String, List<HcsaSvcPersonnelDto>>) ParamUtil.getSessionAttr(request, SERVICEALLPSNCONFIGMAP);
        if(svcAllPsnConfig == null){
            AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            List<String> svcIds = new ArrayList<>();
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                svcIds.add(appSvcRelatedInfoDto.getServiceId());
            }
            List<HcsaServiceStepSchemeDto>  svcStepConfigs = serviceConfigService.getHcsaServiceStepSchemesByServiceId(svcIds);
            svcAllPsnConfig = serviceConfigService.getAllSvcAllPsnConfig(svcStepConfigs, svcIds);
        }
        return svcAllPsnConfig;
    }
    
    private static boolean checkCanEdit(List<String> amendTypes, String currentType){
        boolean rfcCanEdit = false;
        if(amendTypes != null){
            for(String type:amendTypes){
                if(currentType.equals(type)){
                    rfcCanEdit = true;
                    break;
                }
            }
        }
        return rfcCanEdit;
    }
    
    public static boolean isGetDataFromPage(AppSubmissionDto appSubmissionDto, String currentType, String isEdit){
        if(appSubmissionDto == null){
            return true;
        }
        boolean otherType = !ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equalsIgnoreCase(appSubmissionDto.getAppType());
        boolean rfcType = false;
        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equalsIgnoreCase(appSubmissionDto.getAppType())){
            boolean canEdit = checkCanEdit(appSubmissionDto.getAmendTypes(), currentType);
            rfcType = canEdit && AppConsts.YES.equals(isEdit);
        }
        return otherType || rfcType;
    }
    
}

