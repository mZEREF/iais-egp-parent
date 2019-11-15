package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.AppGrpPremisesService;
import com.ecquaria.cloud.moh.iais.service.AppGrpPrimaryDocService;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.submission.client.model.SubmitReq;
import com.ecquaria.submission.client.model.SubmitResp;
import com.ecquaria.submission.client.wrapper.SubmissionClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * NewApplicationDelegator
 *
 * @author suocheng
 * @date 9/23/2019
 */
@Delegator("newApplicationDelegator")
@Slf4j
public class NewApplicationDelegator {
    private static final String APPGRPPREMISESDTO = "appGrpPremisesDto";
    private static final String APPGRPPRIMARYDOCDTO = "AppGrpPrimaryDocDto";
    private static final String ERRORMAP_PREMISES    = "errorMap_premises";
    public static final String SERVICEID = "serviceId";
    private static final String PREMISESTYPE = "premisesType";
    public static final String APPSUBMISSIONDTO = "AppSubmissionDto";
    private static final String HCSASVCDOCCONFIGDTOMAP = "HcsaSvcDocConfigDtoMap";

    @Autowired
    private AppGrpPremisesService appGrpPremisesService;

    @Autowired
    private AppGrpPrimaryDocService appGrpPrimaryDocService;

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
    public void doStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do Start start ...."));
        AuditTrailHelper.auditFunction("hcsa-application", "hcsa application");
        ParamUtil.setSessionAttr(bpc.request,APPGRPPREMISESDTO,null);
        ParamUtil.setSessionAttr(bpc.request,APPGRPPRIMARYDOCDTO,null);
        //for loading the draft by appId
        //loadingDraft(bpc);
        //for loading Service Config
        loadingServiceConfig(bpc);

        log.debug(StringUtil.changeForLog("the do Start end ...."));
    }


    /**
     * StartStep: Prepare
     *
     * @param bpc
     * @throws
     */
    public void prepare(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepare start ...."));
        String action = ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        if(StringUtil.isEmpty(action)){
            action = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
            if(StringUtil.isEmpty(action)){
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE,"premises");
            }
        }
        log.debug(StringUtil.changeForLog("the do prepare end ...."));
    }
    /**
     * StartStep: PreparePremises
     *
     * @param bpc
     * @throws
     */
    public void preparePremises(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do preparePremises start ...."));
        //get svcCode to get svcId
        List<HcsaServiceDto> hcsaServiceDtoList = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST);
        List<String> svcIds = new ArrayList<>();
        hcsaServiceDtoList.forEach(item -> svcIds.add(item.getId()));
        List premisesSelect = new ArrayList<SelectOption>();
        String loginId="internet";
        //?
        List<AppGrpPremisesDto> list = appGrpPremisesService.getAppGrpPremisesDtoByLoginId(loginId);
        SelectOption sp0 = new SelectOption("-1","Select One");
        premisesSelect.add(sp0);
        SelectOption sp1 = new SelectOption("newPremise","Add a new premises");
        premisesSelect.add(sp1);
        if(list !=null){
            for (AppGrpPremisesDto item : list){
                if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(item.getPremisesType())){
                    SelectOption sp2 = new SelectOption(item.getId(),item.getAddress());
                    premisesSelect.add(sp2);
                }
            }
        }
        ParamUtil.setRequestAttr(bpc.request,"premisesSelect",premisesSelect);
        //get premises type
        Set<String> premisesType= appGrpPremisesService.getAppGrpPremisesTypeBySvcId(svcIds);
        ParamUtil.setSessionAttr(bpc.request, PREMISESTYPE, (Serializable) premisesType);
        log.debug(StringUtil.changeForLog("the do preparePremises end ...."));
    }
    /**
     * StartStep: PrepareDocuments
     *
     * @param bpc
     * @throws
     */
    public void prepareDocuments(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepareDocuments start ...."));

        String serviceId = (String) ParamUtil.getSessionAttr(bpc.request, SERVICEID);
        Map<String,List<HcsaSvcDocConfigDto>> hcsaSvcCommonDocDtoMap = appGrpPrimaryDocService.getAllHcsaSvcDocs(serviceId);
        if(hcsaSvcCommonDocDtoMap!=null){
            ParamUtil.setSessionAttr(bpc.request, HCSASVCDOCCONFIGDTOMAP, (Serializable) hcsaSvcCommonDocDtoMap);
        }
        log.debug(StringUtil.changeForLog("the do prepareDocuments end ...."));
    }
    /**
     * StartStep: PrepareForms
     *
     * @param bpc
     * @throws
     */
    public void prepareForms(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepareForms start ...."));

        log.debug(StringUtil.changeForLog("the do prepareForms end ...."));
    }
    /**
     * StartStep: PreparePreview
     *
     * @param bpc
     * @throws
     */
    public void preparePreview(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do preparePreview start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        log.debug(StringUtil.changeForLog("the do preparePreview end ...."));
    }
    /**
     * StartStep: PreparePayment
     *
     * @param bpc
     * @throws
     */
    public void preparePayment(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do preparePayment start ...."));

        log.debug(StringUtil.changeForLog("the do preparePayment end ...."));
    }
    /**
     * StartStep: DoPremises
     *
     * @param bpc
     * @throws
     */
    public void doPremises(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doPremises start ...."));
        //gen dto
        AppGrpPremisesDto appGrpPremisesDto = genAppGrpPremisesDto(bpc.request);
        //set value into AppSubmissionDto
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        appSubmissionDto.setAppGrpPremisesDto(appGrpPremisesDto);
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);

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
        String crudActionType =  mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
        String crudActionValue = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_VALUE);

        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE,crudActionType);
        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE,crudActionValue);

        AppGrpPrimaryDocDto appGrpPrimaryDocDto = null;
        List<MultipartFile> files = null;
        List<MultipartFile> oneFile = null;
        for (Iterator<String> en = mulReq.getFileNames(); en.hasNext(); ) {
            String name = en.next();
            files = mulReq.getFiles(name);
        }

        String [] docConfig = mulReq.getParameterValues("docConfig");
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = new ArrayList<>();
        if(files != null && docConfig !=null){
            for(MultipartFile file:files){
                if(!StringUtil.isEmpty(file.getOriginalFilename())){
                    String[] config = docConfig[0].split(";");

                    appGrpPrimaryDocDto = new AppGrpPrimaryDocDto();
                    appGrpPrimaryDocDto.setSvcComDocId(config[0]);
                    appGrpPrimaryDocDto.setDocName(file.getOriginalFilename());
                    long size = file.getSize()/1024;
                    appGrpPrimaryDocDto.setDocSize(Integer.valueOf(String.valueOf(size)));
                    oneFile = new ArrayList<>();
                    oneFile.add(file);
                    //api side not get value
                    List<String> fileRepoGuidList = appGrpPrimaryDocService.saveFileToRepo(oneFile);
                    appGrpPrimaryDocDto.setFileRepoId(fileRepoGuidList.get(0));
                    //if config[1] equals common ==> set null
                    appGrpPrimaryDocDto.setPremisessName("");
                    appGrpPrimaryDocDto.setPremisessType("");

                    appGrpPrimaryDocDtoList.add(appGrpPrimaryDocDto);
                }
            }
        }

        //set value into AppSubmissionDto
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        appSubmissionDto.setAppGrpPrimaryDocDtos(appGrpPrimaryDocDtoList);
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        log.debug(StringUtil.changeForLog("the do doDocument end ...."));
    }
    /**
     * StartStep: doForms
     *
     * @param bpc
     * @throws
     */
    public void doForms(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doForms start ...."));

        log.debug(StringUtil.changeForLog("the do doForms end ...."));
    }
    /**
     * StartStep: doPreview
     *
     * @param bpc
     * @throws
     */
    public void doPreview(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doPreview start ...."));

        log.debug(StringUtil.changeForLog("the do doPreview end ...."));
    }
    /**
     * StartStep: doPreview
     *
     * @param bpc
     * @throws
     */
    public void doPayment(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doPayment start ...."));

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
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto)ParamUtil.getSessionAttr(bpc.request,APPSUBMISSIONDTO);
        appSubmissionDto = appSubmissionService.doSaveDraft(appSubmissionDto);
        ParamUtil.setSessionAttr(bpc.request,APPSUBMISSIONDTO,appSubmissionDto);
        log.debug(StringUtil.changeForLog("the do doSaveDraft end ...."));
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
        Map<String,Map<String,String>> validateResult = doValidate(bpc);
        //save the app and appGroup
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        appSubmissionDto = appSubmissionService.submit(appSubmissionDto);
        ParamUtil.setSessionAttr(bpc.request,APPSUBMISSIONDTO,appSubmissionDto);
        //asynchronous save the other data.
         eventBus(appSubmissionDto,bpc);
        //get wrokgroup
        log.debug(StringUtil.changeForLog("the do doSubmit end ...."));
    }



    /**
     * StartStep: ControlSwitch
     *
     * @param bpc
     * @throws
     */
    public void controlSwitch(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do controlSwitch start ...."));
        String switch2 = "loading";
        String crudActionValue = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);
        if(StringUtil.isEmpty(crudActionValue)){
            crudActionValue = (String)ParamUtil.getRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);
        }
        if("saveDraft".equals(crudActionValue) || "ack".equals(crudActionValue)
                || "doSubmit".equals(crudActionValue)){
            switch2 = crudActionValue;
        }
        ParamUtil.setRequestAttr(bpc.request,"Switch2",switch2);
        log.debug(StringUtil.changeForLog("the do controlSwitch end ...."));

    }
    /**
     * StartStep: PrepareAckPage
     *
     * @param bpc
     * @throws
     */
    public void prepareAckPage(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepareAckPage start ...."));

        log.debug(StringUtil.changeForLog("the do prepareAckPage end ...."));
    }

    /**
     * @description: ajax
     * @author: zixia
     * @param
     */
    @RequestMapping(value = "/loadPremisesByCode.do", method = RequestMethod.GET)
    public @ResponseBody PostCodeDto loadPremisesByPostCode(HttpServletRequest request){
        log.debug(StringUtil.changeForLog("the do loadPremisesByPostCode start ...."));
        String searchField = ParamUtil.getDate(request, "searchField");
        String filterValue = ParamUtil.getDate(request, "filterValue");
        PostCodeDto postCodeDto = appGrpPremisesService.getPremisesByPostalCode(searchField, filterValue);

        log.debug(StringUtil.changeForLog("the do loadPremisesByPostCode end ...."));
        return postCodeDto;
    }

    /**
     * @description: for the page validate call.
     * @param request
     * @return
     */
    public AppSubmissionDto getValueFromPage(HttpServletRequest request){
        return (AppSubmissionDto) ParamUtil.getSessionAttr(request, APPSUBMISSIONDTO);
    }

    //=============================================================================
    //private method
    //=============================================================================
    private  void loadingDraft(BaseProcessClass bpc){
       //todo
    }

    private void loadingServiceConfig(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do loadingServiceConfig start ...."));
        //loading the service
        List<String> serviceConfigIds = new ArrayList<>();
        serviceConfigIds.add("AA1A7D00-2AEB-E911-BE76-000C29C8FBE4");
        serviceConfigIds.add("C3E7715A-29EB-E911-BE76-000C29C8FBE4");
        List<HcsaServiceDto> hcsaServiceDtoList = serviceConfigService.getHcsaServiceDtosById(serviceConfigIds);
        sortHcsaServiceDto(hcsaServiceDtoList);
        ParamUtil.setSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) hcsaServiceDtoList);
        log.debug(StringUtil.changeForLog("the do loadingServiceConfig end ...."));
    }
    private void sortHcsaServiceDto(List<HcsaServiceDto> hcsaServiceDtoList){
        List<HcsaServiceDto> baseList = new ArrayList();
        List<HcsaServiceDto> specifiedList = new ArrayList();
        List<HcsaServiceDto> subList = new ArrayList();
        List<HcsaServiceDto> otherList = new ArrayList();
        //class
        for (HcsaServiceDto hcsaServiceDto:hcsaServiceDtoList){
            switch (hcsaServiceDto.getSvcCode()){
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

    private void sortService(List<HcsaServiceDto> list){
        list.sort((h1, h2) -> h1.getSvcName().compareTo(h2.getSvcName()));
    }
    private Map<String,Map<String,String>> doValidate(BaseProcessClass bpc){
        Map<String,Map<String,String>> reuslt = new HashMap<>();
        //do validate premiss
        Map<String,String> premises =  doValidatePremiss(bpc);
        reuslt.put("premises",premises);
        return reuslt;
    }
    private Map<String,String> doValidatePremiss(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doValidatePremiss start ...."));
        //do validate premiss
        Map<String,String> errorMap = new HashMap<>();
//        AppGrpPremisesDto appGrpPremisesDto = (AppGrpPremisesDto)ParamUtil.getSessionAttr(bpc.request,APPGRPPREMISESDTO);
//        String premiseType = appGrpPremisesDto.getPremisesType();
//        if(StringUtil.isEmpty(premiseType)){
//            errorMap.put("premisesType","Please select the premises Type");
//        }else if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premiseType)){
//            ValidationResult validationResult = WebValidationHelper.validateProperty(appGrpPremisesDto,AppServicesConsts.VALIDATE_PROFILES_CREATE+","+AppServicesConsts.VALIDATE_PROFILES_ON_SITE);
//            if (validationResult.isHasErrors()){
//                errorMap = validationResult.retrieveAll();
//            }
//        }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premiseType)){
//            ValidationResult validationResult = WebValidationHelper.validateProperty(appGrpPremisesDto,AppServicesConsts.VALIDATE_PROFILES_CREATE+","+AppServicesConsts.VALIDATE_PROFILES_CONVEYANCE);
//            if (validationResult.isHasErrors()){
//                errorMap = validationResult.retrieveAll();
//            }
//        }
//        ParamUtil.setRequestAttr(bpc.request,ERRORMAP_PREMISES,errorMap);
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
    private AppGrpPremisesDto genAppGrpPremisesDto(HttpServletRequest request){
        AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
        String premisesType = ParamUtil.getString(request, "premisesType");
        String premisesSelect = ParamUtil.getString(request, "premisesSelect");
        String hciName = ParamUtil.getString(request, "hciName");
        String postalCode = ParamUtil.getString(request,  "postalCode");
        String blkNo = ParamUtil.getString(request, "blkNo");
        String streetName = ParamUtil.getString(request, "streetName");
        String floorNo = ParamUtil.getString(request, "floorNo");
        String unitNo = ParamUtil.getString(request, "unitNo");
        String buildingName = ParamUtil.getString(request, "buildingName");
        String siteAddressType = ParamUtil.getString(request, "siteAddressType");
        String siteSafefyNo = ParamUtil.getString(request, "siteSafefyNo");
        String addrType = ParamUtil.getString(request, "addrType");
        appGrpPremisesDto.setPremisesType(premisesType);
        appGrpPremisesDto.setPremisesSelect(premisesSelect);
        appGrpPremisesDto.setHciName(hciName);
        appGrpPremisesDto.setPostalCode(postalCode);
        appGrpPremisesDto.setBlkNo(blkNo);
        appGrpPremisesDto.setStreetName(streetName);
        appGrpPremisesDto.setFloorNo(floorNo);
        appGrpPremisesDto.setUnitNo(unitNo);
        appGrpPremisesDto.setBuildingName(buildingName);
        appGrpPremisesDto.setSiteSafefyNo(siteAddressType);
        appGrpPremisesDto.setSiteSafefyNo(siteSafefyNo);
        appGrpPremisesDto.setAddrType(addrType);
        //add index for dto refer
        appGrpPremisesDto.setPremisesIndexNo("prem01");
        return  appGrpPremisesDto;
    }

    private AppSubmissionDto getAppSubmissionDto(HttpServletRequest request){
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, APPSUBMISSIONDTO);
        if(appSubmissionDto == null){
            appSubmissionDto = new AppSubmissionDto();
        }
        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
        return appSubmissionDto;
    }
    private  void eventBus( AppSubmissionDto appSubmissionDto,BaseProcessClass bpc){
        SubmissionClient client = new SubmissionClient();
        //prepare request parameters
        appSubmissionDto.setEventRefNo(appSubmissionDto.getAppGrpNo());
        SubmitReq req = new SubmitReq();
        req.setSubmissionId(appSubmissionDto.getAppGrpId());
        req.setProject(bpc.process.getCurrentProject());
        req.setProcess(bpc.process.getCurrentProcessName());
        req.setStep(bpc.process.getCurrentComponentName());
        req.setService("appsubmit");
        req.setOperation("Create");
        req.setSopUrl("https://egp.sit.inter.iais.com/hcsaapplication/eservice/INTERNET/MohNewApplication");
        req.setData(JsonUtil.parseToJson(appSubmissionDto));
        req.setCallbackUrl(null);
        req.setUserId("SOP");
        //req.setWait(true);
        req.setTotalWait(30);
        //
        SubmitResp submitResp = client.submit(AppConsts.REST_PROTOCOL_TYPE + RestApiUrlConsts.EVENT_BUS, req);
    }
}
