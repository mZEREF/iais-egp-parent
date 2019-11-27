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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
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
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    private static final String APPGRPPREMISESDTO = "appGrpPremisesDto";
    private static final String APPGRPPRIMARYDOCDTO = "AppGrpPrimaryDocDto";
    private static final String ERRORMAP_PREMISES = "errorMap_premises";
    public static final String CURRENTSERVICEID = "currentServiceId";
    public static final String CURRENTSVCCODE = "currentSvcCode";
    private static final String PREMISESTYPE = "premisesType";
    public static final String APPSUBMISSIONDTO = "AppSubmissionDto";
    private static final String HCSASVCDOCCONFIGDTOMAP = "HcsaSvcDocConfigDtoMap";


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
    public void doStart(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do Start start ...."));
        AuditTrailHelper.auditFunction("hcsa-application", "hcsa application");
        ParamUtil.setSessionAttr(bpc.request, APPGRPPREMISESDTO, null);
        ParamUtil.setSessionAttr(bpc.request, APPGRPPRIMARYDOCDTO, null);
        //for loading the draft by appId
        //loadingDraft(bpc);
        //for loading Service Config
        loadingServiceConfig(bpc);
        initSession(bpc);
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
            if (StringUtil.isEmpty(action)) {
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
                }
            }
        }
        ParamUtil.setRequestAttr(bpc.request, "premisesSelect", premisesSelect);
        //get premises type
        if (svcIds.size() > 0) {
            Set<String> premisesType = serviceConfigService.getAppGrpPremisesTypeBySvcId(svcIds);
            ParamUtil.setSessionAttr(bpc.request, PREMISESTYPE, (Serializable) premisesType);
        }
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
        Map<String, List<HcsaSvcDocConfigDto>> hcsaSvcCommonDocDtoMap = serviceConfigService.getAllHcsaSvcDocs(currentSvcId);
        if (hcsaSvcCommonDocDtoMap != null) {
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
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        Map<String, AppSvcRelatedInfoDto> appSvcRelatedInfoMap = (Map<String, AppSvcRelatedInfoDto>) ParamUtil.getSessionAttr(bpc.request, ClinicalLaboratoryDelegator.APPSVCRELATEDINFOMAP);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = new ArrayList<>();
        appSvcRelatedInfoMap.keySet().forEach(key -> appSvcRelatedInfoDtoList.add(appSvcRelatedInfoMap.get(key)));
        appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtoList);
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
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
        AppGrpPremisesDto appGrpPremisesDto = genAppGrpPremisesDto(bpc.request);
        //set value into AppSubmissionDto
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        appSubmissionDto.setAppGrpPremisesDto(appGrpPremisesDto);
        ParamUtil.setSessionAttr(bpc.request, APPGRPPREMISESDTO, appGrpPremisesDto);

        /*Map<String,String> errorMap = doValidatePremiss(bpc);
        if(errorMap.size()>0){
           ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE,"premises");
        }else {
            ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        }*/
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
        String crudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
        String crudActionValue = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_VALUE);

        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE, crudActionValue);

        AppGrpPrimaryDocDto appGrpPrimaryDocDto = null;
        List<MultipartFile> files = null;
        List<MultipartFile> oneFile = null;
        for (Iterator<String> en = mulReq.getFileNames(); en.hasNext(); ) {
            String name = en.next();
            files = mulReq.getFiles(name);
        }

        String[] docConfig = mulReq.getParameterValues("docConfig");
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = new ArrayList<>();
        if (files != null && docConfig != null) {
            for (MultipartFile file : files) {
                if (!StringUtil.isEmpty(file.getOriginalFilename())) {
                    String[] config = docConfig[0].split(";");

                    //do by wenkang



                    appGrpPrimaryDocDto = new AppGrpPrimaryDocDto();
                    appGrpPrimaryDocDto.setSvcComDocId(config[0]);
                    appGrpPrimaryDocDto.setDocName(file.getOriginalFilename());
                    long size = file.getSize() / 1024;
                    appGrpPrimaryDocDto.setDocSize(Integer.valueOf(String.valueOf(size)));
                    oneFile = new ArrayList<>();
                    oneFile.add(file);
                    //api side not get value
                    List<String> fileRepoGuidList = serviceConfigService.saveFileToRepo(oneFile);
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
        String result = bpc.request.getParameter("result");
        String switch2 = "loading";
        if (!StringUtil.isEmpty(result)) {
            log.debug(StringUtil.changeForLog("payment result:" + result));
            if ("success".equals(result)) {
                switch2 = "ack";
                //update status
                AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
                String appGrpId = appSubmissionDto.getAppGrpId();
                //String pmtRefNo = bpc.request.getParameter("pmtRefNo");
                //default online payment
                String pmtStatus = MasterCodeUtil.getCodeDesc(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                ApplicationGroupDto appGrp = new ApplicationGroupDto();
                appGrp.setId(appGrpId);
                appGrp.setPmtRefNo("AN1911136061");
                appGrp.setPmtStatus(pmtStatus);
                RestApiUtil.update(RestApiUrlConsts.UPDATE_APPLICATION_GROUP, appGrp, String.class);
            }
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
        String draftNo = appSubmissionService.getDraftNo(appSubmissionDto.getAppType());
        log.debug(StringUtil.changeForLog("the draftNo -->:") + draftNo);
        appSubmissionDto = appSubmissionService.doSaveDraft(appSubmissionDto);
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
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
        Map<String, Map<String, String>> validateResult = doValidate(bpc);
        //save the app and appGroup
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
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
        appSubmissionDto.setPreInspection(preOrPostInspectionResultDto.isPreInspection());
        appSubmissionDto.setRequirement(preOrPostInspectionResultDto.isRequirement());
        //set Risk Score
        appSubmissionService.setRiskToDto(appSubmissionDto);

        appSubmissionDto = appSubmissionService.submit(appSubmissionDto);
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        //asynchronous save the other data.
        eventBus(appSubmissionDto, bpc);
        //get wrokgroup
        log.debug(StringUtil.changeForLog("the do doSubmit end ...."));
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
        if ("saveDraft".equals(crudActionValue) || "ack".equals(crudActionValue)
                || "doSubmit".equals(crudActionValue)) {
            switch2 = crudActionValue;
        }
        ParamUtil.setRequestAttr(bpc.request, "Switch2", switch2);
        log.debug(StringUtil.changeForLog("the do controlSwitch end ...."));

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
        String searchField = ParamUtil.getDate(request, "searchField");
        String filterValue = ParamUtil.getDate(request, "filterValue");
        log.debug(StringUtil.changeForLog("searchField :"+searchField));
        log.debug(StringUtil.changeForLog("filterValue :"+filterValue));
        PostCodeDto postCodeDto = null;
        try {
            postCodeDto = serviceConfigService.getPremisesByPostalCode(searchField, filterValue);
        }catch (Exception e){
            log.debug(StringUtil.changeForLog("call retrieve address api failed"));
            postCodeDto = null;
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
     * @param request
     * @return
     * @description: for the page validate call.
     */
    public AppSubmissionDto getValueFromPage(HttpServletRequest request) {
        return (AppSubmissionDto) ParamUtil.getSessionAttr(request, APPSUBMISSIONDTO);
    }

    //=============================================================================
    //private method
    //=============================================================================
    private void loadingDraft(BaseProcessClass bpc) {
        //todo
    }

    private void loadingServiceConfig(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do loadingServiceConfig start ...."));
        //loading the service
        List<String> serviceConfigIds = new ArrayList<>();
        serviceConfigIds.add("34F99D15-820B-EA11-BE7D-000C29F371DC");
        serviceConfigIds.add("35F99D15-820B-EA11-BE7D-000C29F371DC");
        List<HcsaServiceDto> hcsaServiceDtoList = serviceConfigService.getHcsaServiceDtosById(serviceConfigIds);
        sortHcsaServiceDto(hcsaServiceDtoList);
        ParamUtil.setSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) hcsaServiceDtoList);
        log.debug(StringUtil.changeForLog("the do loadingServiceConfig end ...."));
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

    private Map<String, Map<String, String>> doValidate(BaseProcessClass bpc) {
        Map<String, Map<String, String>> reuslt = new HashMap<>();
        //do validate premiss
        Map<String, String> premises = doValidatePremiss(bpc);
        reuslt.put("premises", premises);
        return reuslt;
    }

    private Map<String, String> doValidatePremiss(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doValidatePremiss start ...."));
        //do validate one premiss
        Map<String, String> errorMap = new HashMap<>();
        AppGrpPremisesDto appGrpPremisesDto = (AppGrpPremisesDto) ParamUtil.getSessionAttr(bpc.request, APPGRPPREMISESDTO);
        String premiseType = appGrpPremisesDto.getPremisesType();
        if (StringUtil.isEmpty(premiseType)) {
            errorMap.put("premisesType", "Please select the premises Type");
        }
        String premisesSelect = appGrpPremisesDto.getPremisesSelect();
        if (StringUtil.isEmpty(premisesSelect) || "-1".equals(premisesSelect)) {
            errorMap.put("premisesSelect", "Please select the premises from");
        } else if ("newPremise".equals(premisesSelect)) {
            if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premiseType)) {
                ValidationResult validationResult = WebValidationHelper.validateProperty(appGrpPremisesDto, AppServicesConsts.VALIDATE_PROFILES_CREATE + "," + AppServicesConsts.VALIDATE_PROFILES_ON_SITE);
                if (validationResult.isHasErrors()) {
                    errorMap = validationResult.retrieveAll();
                }
                //do by wenkang
                String addrType = appGrpPremisesDto.getAddrType();
                if (!StringUtil.isEmpty(addrType)) {
                    if ("Apt Blk".equals(addrType)) {
                        boolean empty = StringUtil.isEmpty(appGrpPremisesDto.getFloorNo());
                        boolean empty1 = StringUtil.isEmpty(appGrpPremisesDto.getBlkNo());
                        boolean empty2 = StringUtil.isEmpty(appGrpPremisesDto.getUnitNo());
                        if (empty) {
                            errorMap.put("floorNo", "can not is null");
                        }
                        if (empty1) {
                            errorMap.put("blkNo", "can not is null");
                        }
                        if (empty2) {
                            errorMap.put("unitNo", "can not is null");
                        }
                    }
                }
                String postalCode = appGrpPremisesDto.getPostalCode();
                if (!StringUtil.isEmpty(postalCode)) {
                    if (!postalCode.matches("^[0-9]*$")) {
                        errorMap.put("postalCode", "");
                    }
                }
            } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premiseType)) {
                ValidationResult validationResult = WebValidationHelper.validateProperty(appGrpPremisesDto, AppServicesConsts.VALIDATE_PROFILES_CREATE + "," + AppServicesConsts.VALIDATE_PROFILES_CONVEYANCE);
                if (validationResult.isHasErrors()) {
                    errorMap = validationResult.retrieveAll();
                }
            }
        } else {
            //premiseSelect = organization hci code

        }
        ParamUtil.setRequestAttr(bpc.request, ERRORMAP_PREMISES, errorMap);
        log.debug(StringUtil.changeForLog("the do doValidatePremiss end ...."));
        return errorMap;
    }


    public static Map<String, Map<String, String>> doValidatePo(HttpServletRequest request) {
        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDto = (List<AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(request, "AppSvcPrincipalOfficersDto");
        Map<String, Map<String, String>> errorMap = new HashMap<>();
        Map<String, String> oneErrorMap = null;
        for (AppSvcPrincipalOfficersDto poDto : appSvcPrincipalOfficersDto) {
            oneErrorMap = new HashMap<>();
            String assignSelect = poDto.getAssignSelect();
            if (StringUtil.isEmpty(assignSelect)) {
                oneErrorMap.put("assignSelect", "assignSelect can not null");
            } else {
                ValidationResult validationResult = WebValidationHelper.validateProperty(appSvcPrincipalOfficersDto, AppServicesConsts.VALIDATE_PROFILES_CREATE);
                if (validationResult.isHasErrors()) {
                    oneErrorMap = validationResult.retrieveAll();
                }
                //do by wenkang
                String mobileNo = poDto.getMobileNo();
                String officeTelNo = poDto.getOfficeTelNo();
                String emailAddr = poDto.getEmailAddr();
                if(!StringUtil.isEmpty(mobileNo)){
                    if (!mobileNo.startsWith("8") && !mobileNo.startsWith("9")) {
                        oneErrorMap.put("mobileNo", "Please key in a valid mobile number");
                    }
                }
                if(!StringUtil.isEmpty(emailAddr)) {
                    if (!emailAddr.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
                        oneErrorMap.put("emailAddr", "Please key in a valid email address");
                    }
                }
                if(!StringUtil.isEmpty(officeTelNo)) {
                    if (!officeTelNo.startsWith("6")) {
                        oneErrorMap.put("officeTelNo", "Please key in a valid phone number");
                    }
                }
            }
            if(oneErrorMap.size()>0){
                errorMap.put("po1", oneErrorMap);
            }

        }
        return errorMap;
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

    private void initSession(BaseProcessClass bpc){
        AppSubmissionDto appSubmissionDto = new AppSubmissionDto();
        //hard-code app type
        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
        List<HcsaServiceDto> HcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request,  AppServicesConsts.HCSASERVICEDTOLIST);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = new ArrayList<>();
        Map<String,AppSvcRelatedInfoDto>  svcRelatedMap = new HashMap<>();
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = null;
        for(HcsaServiceDto svc:HcsaServiceDtos){
            appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
            appSvcRelatedInfoDto.setServiceId(svc.getId());
            appSvcRelatedInfoDto.setServiceCode(svc.getSvcCode());
            appSvcRelatedInfoDto.setServiceType(svc.getSvcType());
            appSvcRelatedInfoDtoList.add(appSvcRelatedInfoDto);
            svcRelatedMap.put(svc.getId(), appSvcRelatedInfoDto);
        }
        appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtoList);
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        ParamUtil.setSessionAttr(bpc.request, ClinicalLaboratoryDelegator.APPSVCRELATEDINFOMAP, (Serializable) svcRelatedMap);
    }

    private List getKeyWords(){
        List keyWords = new ArrayList();
        keyWords.add("asd");
        keyWords.add("mzx");
        return keyWords;
    }

        private Map<String,String>  documentValid(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doValidatePremiss start ...."));

        Map errorMap=new HashMap();
        File file  =new File("");
        if(file.exists()){
            long length = file.length();
            if(length>1024*1024){
                errorMap.put("file","File size is too large!");
            }
        }
        Boolean flag=false;
            String name = file.getName();
            String substring = name.substring(name.lastIndexOf("."));
            for(String fileType: AppServicesConsts.FILE_TYPE){
                if(fileType.equals(substring)){
                    flag=true;
                }
            }
            if(!flag){
                errorMap.put("fileType","Wrong file type");
            }
            return errorMap;
    }
}

