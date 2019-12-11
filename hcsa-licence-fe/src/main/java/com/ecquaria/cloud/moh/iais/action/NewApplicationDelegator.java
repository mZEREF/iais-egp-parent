package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
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
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HtmlElementHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.sql.SqlMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
    public static final String COMMONHCSASVCDOCCONFIGDTO = "commonHcsaSvcDocConfigDto";
    public static final String PREMHCSASVCDOCCONFIGDTO = "premHcsaSvcDocConfigDto";
    public static final String APPGRPPREMISESLIST = "appGrpPremisesList";
    public static final String RELOADAPPGRPPRIMARYDOCMAP = "reloadAppGrpPrimaryDocMap";
    public static final String APPGRPPRIMARYDOCLIST = "appGrpPrimaryDocList";
    public static final String  APPGRPPRIMARYDOCERRMSGMAP = "appGrpPrimaryDocErrMsgMap";
    public static final String FIRESTOPTION = "Select One";


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
        //wait to delete one premises
        ParamUtil.setSessionAttr(bpc.request, APPGRPPREMISESDTO, null);

        ParamUtil.setSessionAttr(bpc.request, APPGRPPREMISESLIST, null);
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
        List<HcsaSvcDocConfigDto> hcsaSvcDocDtos = serviceConfigService.getAllHcsaSvcDocs(currentSvcId);
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
       /* List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = new ArrayList<>();
        appSvcRelatedInfoMap.keySet().forEach(key -> appSvcRelatedInfoDtoList.add(appSvcRelatedInfoMap.get(key)));*/
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = new ArrayList<>();
        appSvcRelatedInfoDtoList.add(appSvcRelatedInfoMap.get("35F99D15-820B-EA11-BE7D-000C29F371DC"));
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
        //
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        List<AppGrpPremisesDto> appGrpPremisesDtoList = genAppGrpPremisesDtoList(bpc.request);

        appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtoList);
        //:todo delete
        AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtoList.get(0);
        appSubmissionDto.setAppGrpPremisesDto(appGrpPremisesDto);

        //:todo delete
        ParamUtil.setSessionAttr(bpc.request, APPGRPPREMISESDTO, appGrpPremisesDto);
        ParamUtil.setSessionAttr(bpc.request, APPGRPPREMISESLIST, (Serializable) appGrpPremisesDtoList);

        Map<String, Map<String,String>> errorMap = doValidatePremiss(bpc);
        for(Map<String,String> value:errorMap.values()){
            if(value.size()>0){
                ParamUtil.setRequestAttr(bpc.request, ERRORMAP_PREMISES, errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE,"premises");
                return;
            }
        }
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
        CommonsMultipartFile file = null;

        List<HcsaSvcDocConfigDto> commonHcsaSvcDocConfigList = (List<HcsaSvcDocConfigDto>) ParamUtil.getSessionAttr(bpc.request, COMMONHCSASVCDOCCONFIGDTO);
        List<HcsaSvcDocConfigDto> premHcsaSvcDocConfigList = (List<HcsaSvcDocConfigDto>) ParamUtil.getSessionAttr(bpc.request, PREMHCSASVCDOCCONFIGDTO);
        List<AppGrpPremisesDto> appGrpPremisesList = (List<AppGrpPremisesDto>) ParamUtil.getSessionAttr(bpc.request, APPGRPPREMISESLIST);
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = new ArrayList<>();
        Map<String,String> errorMap = new HashMap<>();
        Map<String,AppGrpPrimaryDocDto> reloadDocMap = new HashMap();
        Map<String,AppGrpPrimaryDocDto> beforeReloadDocMap = (Map<String, AppGrpPrimaryDocDto>) ParamUtil.getSessionAttr(bpc.request, RELOADAPPGRPPRIMARYDOCMAP);
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
                    //if  common ==> set null
                    appGrpPrimaryDocDto.setPremisessName("");
                    appGrpPrimaryDocDto.setPremisessType("");
                    String fileRepoGuid = serviceConfigService.saveFileToRepo(file);
                    appGrpPrimaryDocDto.setFileRepoId(fileRepoGuid);
                    appGrpPrimaryDocDtoList.add(appGrpPrimaryDocDto);
                    reloadDocMap.put(name, appGrpPrimaryDocDto);
                }
            }else if("N".equals(delFlagValue)){
                AppGrpPrimaryDocDto beforeDto = (AppGrpPrimaryDocDto) beforeReloadDocMap.get(name);
                if(beforeDto != null){
                    appGrpPrimaryDocDtoList.add(beforeDto);
                    reloadDocMap.put(name, beforeDto);
                }
            } else{
                if(comm.getIsMandatory()){
                    errorMap.put(name, "can not is empty");
                }
            }
        }
        for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesList){
            for(HcsaSvcDocConfigDto prem:premHcsaSvcDocConfigList){
                String name = "prem"+prem.getId()+appGrpPremisesDto.getHciName();
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
                        appGrpPrimaryDocDto.setPremisessName(appGrpPremisesDto.getHciName());
                        appGrpPrimaryDocDto.setPremisessType(appGrpPremisesDto.getPremisesType());
                        String fileRepoGuid = serviceConfigService.saveFileToRepo(file);
                        appGrpPrimaryDocDto.setFileRepoId(fileRepoGuid);
                        appGrpPrimaryDocDtoList.add(appGrpPrimaryDocDto);
                        reloadDocMap.put(name, appGrpPrimaryDocDto);
                    }
                }else if("N".equals(delFlagValue)){
                    AppGrpPrimaryDocDto beforeDto = (AppGrpPrimaryDocDto) beforeReloadDocMap.get(name);
                    if(beforeDto != null){
                        reloadDocMap.put(name, beforeDto);
                        appGrpPrimaryDocDtoList.add(beforeDto);
                    }
                } else{
                    if(prem.getIsMandatory()) {
                        errorMap.put(name, "can not is empty");
                    }
                }
            }
        }
        ParamUtil.setSessionAttr(bpc.request, APPGRPPRIMARYDOCLIST, (Serializable) appGrpPrimaryDocDtoList);
        ParamUtil.setSessionAttr(bpc.request, RELOADAPPGRPPRIMARYDOCMAP, (Serializable) reloadDocMap);
        // do by wenkang

        documentValid(bpc.request, errorMap);
        if(errorMap.size()>0){
            ParamUtil.setSessionAttr(bpc.request, APPGRPPRIMARYDOCERRMSGMAP, (Serializable) errorMap);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "documents");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, "documents");
            return;
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
                //appGrp.setId(appGrpId);
                appGrp.setId("DCB99B5E-0618-EA11-BE78-000C29D29DB0");
                appGrp.setPmtRefNo("AN1911136062");
                appGrp.setPmtStatus(pmtStatus);
                serviceConfigService.updatePaymentStatus(appGrp);
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
       // Map<String, Map<String, String>> validateResult = doValidate(bpc);
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

        appSubmissionDto = appSubmissionService.submit(appSubmissionDto, bpc.process);
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);

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

    @RequestMapping(value = "/premises-html", method = RequestMethod.GET)
    public @ResponseBody String addPremisesHtml(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the add premises html start ...."));
        String currentLength = ParamUtil.getRequestString(request, "currentLength");
        log.debug(StringUtil.changeForLog("currentLength : "+currentLength));
        String premIndexNo = "prem";
        try {
            Integer IndexNoCount = Integer.parseInt(currentLength);
            ParamUtil.setSessionAttr(request, "IndexNoCount", IndexNoCount);
            premIndexNo = premIndexNo+currentLength;
            log.debug(StringUtil.changeForLog("premIndexNo : "+premIndexNo));
        }catch (Exception e){
            return null;
        }


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
        Map<String,String> premisesOnsiteOpt = new HashMap<>();
        for(SelectOption sp:premisesOnSite){
            premisesOnsiteOpt.put(sp.getValue(), sp.getText());
        }
        Map<String,String> premisesOnSiteAttr = new HashMap<>();
        premisesOnSiteAttr.put("class", "premSelect");
        premisesOnSiteAttr.put("id", "premOnsiteSel");
        premisesOnSiteAttr.put("name", premIndexNo+"premOnSiteSelect");
        premisesOnSiteAttr.put("style", "display: none;");
        String premOnSiteSelectStr = generateDropDownHtml(premisesOnSiteAttr, premisesOnSite, null);

        //premiseSelect -- conveyance
        List<SelectOption> premisesConv= (List) ParamUtil.getSessionAttr(request, "conveyancePremSel");
        Map<String,String> premisesConvOpt = new HashMap<>();
        for(SelectOption sp:premisesOnSite){
            premisesConvOpt.put(sp.getValue(), sp.getText());
        }
        Map<String,String> premisesConvAttr = new HashMap<>();
        premisesConvAttr.put("class", "premSelect");
        premisesConvAttr.put("id", "premConSel");
        premisesConvAttr.put("name", premIndexNo+"premConSelect");
        premisesConvAttr.put("style", "display: none;");
        String premConvSelectStr = generateDropDownHtml(premisesConvAttr, premisesConv, null);

        //Address Type on-site
        List<SelectOption> addrTypes= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ADDRESS_TYPE);
        Map<String,String> addrTypeOpt = new HashMap<>();
        addrTypeOpt.put("-1", FIRESTOPTION);
        for(SelectOption addrType:addrTypes){
            addrTypeOpt.put(addrType.getValue(), addrType.getText());
        }
        Map<String,String> addrTypesAttr = new HashMap<>();
        addrTypesAttr.put("id", "siteAddressType");
        addrTypesAttr.put("name", premIndexNo+"addrType");
        addrTypesAttr.put("style", "display: none;");
        String addrTypeSelect = HtmlElementHelper.generateSelect(addrTypesAttr,addrTypeOpt,null,null,-1,false);
        String addrTypeSelectStr = generateDropDownHtml(addrTypesAttr, addrTypes, FIRESTOPTION);

        //Address Type conveyance
        List<SelectOption> conAddrTypes= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ADDRESS_TYPE);
        Map<String,String> conAddrTypeOpt = new HashMap<>();
        conAddrTypeOpt.put("-1", FIRESTOPTION);
        for(SelectOption addrType:addrTypes){
            addrTypeOpt.put(addrType.getValue(), addrType.getText());
        }
        Map<String,String> conAddrTypesAttr = new HashMap<>();
        conAddrTypesAttr.put("id", "siteAddressType");
        conAddrTypesAttr.put("name", premIndexNo+"conveyanceAddrType");
        conAddrTypesAttr.put("style", "display: none;");
        String conAddrTypeSelect = HtmlElementHelper.generateSelect(conAddrTypesAttr,conAddrTypeOpt,null,null,-1,false);
        String conAddrTypeSelectStr = generateDropDownHtml(conAddrTypesAttr, conAddrTypes, FIRESTOPTION);

        sql = sql.replace("(0)", premIndexNo);
        sql = sql.replace("(1)", premTypeBuffer.toString());
        sql = sql.replace("(2)", premOnSiteSelectStr);
        sql = sql.replace("(3)", premConvSelectStr);
        sql = sql.replace("(4)", addrTypeSelectStr);
        sql = sql.replace("(5)", conAddrTypeSelectStr);

        log.debug(StringUtil.changeForLog("the add premises html end ...."));
        return sql;
    }



    @RequestMapping(value = "/file-repo", method = RequestMethod.GET)
    public @ResponseBody void fileDownload(HttpServletRequest request, HttpServletResponse response) {
        String fileRepo = ParamUtil.getString(request, "fileRepo");
        byte[] fileData =serviceConfigService.downloadFile(fileRepo);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "testFielName");
        headers.setContentType( MediaType.APPLICATION_OCTET_STREAM);
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

/*    private Map<String, Map<String, String>> doValidate(BaseProcessClass bpc) {
        Map<String, Map<String, String>> reuslt = new HashMap<>();
        //do validate premiss
        Map<String, String> premises = doValidatePremiss(bpc);
        reuslt.put("premises", premises);
        return reuslt;
    }*/

    private Map<String, Map<String,String>> doValidatePremiss(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doValidatePremiss start ...."));
        //do validate one premiss
        Map<String,Map<String,String>> errorMaps = new HashMap<>();
        List<AppGrpPremisesDto> appGrpPremisesDtoList = (List<AppGrpPremisesDto>) ParamUtil.getSessionAttr(bpc.request, APPGRPPREMISESLIST);
        for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtoList){
            Map<String, String> errorMap = new HashMap<>();
            String premiseType = appGrpPremisesDto.getPremisesType();
            if (StringUtil.isEmpty(premiseType)) {
                errorMap.put("premisesType", "Please select the premises Type");
            }else {
                String premisesSelect = appGrpPremisesDto.getPremisesSelect();
                if (StringUtil.isEmpty(premisesSelect) || "-1".equals(premisesSelect)) {
                    errorMap.put("premisesSelect", "Please select the premises from");
                } else if ("newPremise".equals(premisesSelect)) {
                    if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premiseType)) {
                        ValidationResult validationResult = WebValidationHelper.validateProperty(appGrpPremisesDto,AppServicesConsts.VALIDATE_PROFILES_ON_SITE);
                        if (validationResult.isHasErrors()) {
                            errorMap = validationResult.retrieveAll();
                        }

                        String offTelNo = appGrpPremisesDto.getOffTelNo();
                        if(StringUtil.isEmpty(offTelNo)){
                            errorMap.put("officeTel","cannot be blank!");
                        }
                        boolean matches = offTelNo.matches("^[6][0-9]{7}$");
                        if(!matches) {
                            errorMap.put("officeTel","Please key in a valid phone number!");
                        }
                        String streetName = appGrpPremisesDto.getStreetName();
                        if(StringUtil.isEmpty(streetName)){
                            errorMap.put("stressName","cannot be blank!");
                        }
                        //do by wenkang
                        String addrType = appGrpPremisesDto.getAddrType();
                        if(StringUtil.isEmpty(addrType)){
                            errorMap.put("addrType", "can not is null");
                        }else {
                            if (ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(addrType)) {
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
                                errorMap.put("postalCode", "the postal code must be numbers ");
                            }
                        }
                    } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premiseType)) {
                        ValidationResult validationResult = WebValidationHelper.validateProperty(appGrpPremisesDto, AppServicesConsts.VALIDATE_PROFILES_CONVEYANCE);
                        if (validationResult.isHasErrors()) {
                            errorMap = validationResult.retrieveAll();
                        }
                        String streetName = appGrpPremisesDto.getStreetName();
                        if(StringUtil.isEmpty(streetName)){
                            errorMap.put("stressName","cannot be blank!");
                        }
                        String addrType = appGrpPremisesDto.getConveyanceAddressType();

                        if(StringUtil.isEmpty(addrType)){
                            errorMap.put("conveyanceAddressType", "can not is null");
                        }else {
                            if (ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(addrType)) {
                                boolean empty = StringUtil.isEmpty(appGrpPremisesDto.getConveyanceFloorNo());
                                boolean empty1 = StringUtil.isEmpty(appGrpPremisesDto.getConveyanceBlockNo());
                                boolean empty2 = StringUtil.isEmpty(appGrpPremisesDto.getConveyanceUnitNo());
                                if (empty) {
                                    errorMap.put("conveyanceFloorNo", "can not is null");
                                }
                                if (empty1) {
                                    errorMap.put("conveyanceBlockNo", "can not is null");
                                }
                                if (empty2) {
                                    errorMap.put("conveyanceUnitNo", "can not is null");
                                }
                            }
                        }
                    }
                } else {
                    //premiseSelect = organization hci code

                }
            }
            errorMaps.put(appGrpPremisesDto.getPremisesIndexNo(), errorMap);
        }
        log.debug(StringUtil.changeForLog("the do doValidatePremiss end ...."));
        return errorMaps;
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
                String idNo = poDto.getIdNo();
                if(!StringUtil.isEmpty(idNo)){
                    boolean b = SgNoValidator.validateFin(idNo);
                    boolean b1 = SgNoValidator.validateNric(idNo);
                    if(!(b||b1)){
                        oneErrorMap.put("NRIC/FIN","Please key in a valid NRIC/FIN");
                    }
                }
                if(!StringUtil.isEmpty(mobileNo)){
                    if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                        oneErrorMap.put("mobileNo", "Please key in a valid mobile number");
                    }
                }
                if(!StringUtil.isEmpty(emailAddr)) {
                    if (!emailAddr.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
                        oneErrorMap.put("emailAddr", "Please key in a valid email address");
                    }
                }
                if(!StringUtil.isEmpty(officeTelNo)) {
                    if (!officeTelNo.matches("^[6][0-9]{7}$")) {
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
    private List<AppGrpPremisesDto> genAppGrpPremisesDtoList(HttpServletRequest request){
        List<AppGrpPremisesDto> appGrpPremisesDtoList = new ArrayList<>();
        Integer count = (Integer) ParamUtil.getSessionAttr(request, "IndexNoCount");
        for(int i =0 ; i<=count;i++){
            AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
            String premisesIndexNo = "prem"+ i;
            String premisesType = ParamUtil.getString(request, premisesIndexNo+"premType");
            appGrpPremisesDto.setPremisesType(premisesType);
            appGrpPremisesDto.setPremisesIndexNo(premisesIndexNo);
            if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesType)){
                String premisesSelect = ParamUtil.getString(request, premisesIndexNo+"premOnSiteSelect");
                String hciName = ParamUtil.getString(request, premisesIndexNo+"hciName");
                String postalCode = ParamUtil.getString(request,  premisesIndexNo+"postalCode");
                String blkNo = ParamUtil.getString(request, premisesIndexNo+"blkNo");
                String streetName = ParamUtil.getString(request, premisesIndexNo+"streetName");
                String floorNo = ParamUtil.getString(request, premisesIndexNo+"floorNo");
                String unitNo = ParamUtil.getString(request, premisesIndexNo+"unitNo");
                String buildingName = ParamUtil.getString(request, premisesIndexNo+"buildingName");
                String siteAddressType = ParamUtil.getString(request, premisesIndexNo+"siteAddressType");
                String siteSafefyNo = ParamUtil.getString(request, premisesIndexNo+"siteSafefyNo");
                String addrType = ParamUtil.getString(request, premisesIndexNo+"addrType");
                String fireSafetyCertIssuedDate  = ParamUtil.getString(request, premisesIndexNo+"fireSafetyCertIssuedDate");
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
                appGrpPremisesDto.setPremisesIndexNo(premisesIndexNo);

            }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesType)){
                String premisesSelect = ParamUtil.getString(request, premisesIndexNo+"premConSelect");
                String vehicleNo = ParamUtil.getString(request, premisesIndexNo+"conveyanceVehicleNo");
                String postalCode = ParamUtil.getString(request,  premisesIndexNo+"conveyancePostalCode");
                String blkNo = ParamUtil.getString(request, premisesIndexNo+"conveyanceBlockNo");
                String streetName = ParamUtil.getString(request, premisesIndexNo+"conveyanceStreetName");
                String floorNo = ParamUtil.getString(request, premisesIndexNo+"conveyanceFloorNo");
                String unitNo = ParamUtil.getString(request, premisesIndexNo+"conveyanceUnitNo");
                String buildingName = ParamUtil.getString(request, premisesIndexNo+"conveyanceBuildingName");
                String siteAddressType = ParamUtil.getString(request, premisesIndexNo+"conveyanceAddrType");
                appGrpPremisesDto.setPremisesSelect(premisesSelect);
                appGrpPremisesDto.setConveyanceVehicleNo(vehicleNo);
                appGrpPremisesDto.setConveyancePostalCode(postalCode);
                appGrpPremisesDto.setConveyanceBlockNo(blkNo);
                appGrpPremisesDto.setConveyanceStreetName(streetName);
                appGrpPremisesDto.setConveyanceFloorNo(floorNo);
                appGrpPremisesDto.setConveyanceUnitNo(unitNo);
                appGrpPremisesDto.setConveyanceBuildingName(buildingName);
                appGrpPremisesDto.setConveyanceAddressType(siteAddressType);
            }
            appGrpPremisesDtoList.add(appGrpPremisesDto);
        }



        return  appGrpPremisesDtoList;
    }

    private AppSubmissionDto getAppSubmissionDto(HttpServletRequest request){
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, APPSUBMISSIONDTO);
        if(appSubmissionDto == null){
            appSubmissionDto = new AppSubmissionDto();
        }
        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
        return appSubmissionDto;
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
            appSvcRelatedInfoDto.setServiceName(svc.getSvcName());
            appSvcRelatedInfoDtoList.add(appSvcRelatedInfoDto);
            svcRelatedMap.put(svc.getId(), appSvcRelatedInfoDto);
        }
        appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtoList);
        //AppGrpPremisesDtoList
        List<AppGrpPremisesDto> appGrpPremisesDtoList = new ArrayList<>();
        AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
        appGrpPremisesDtoList.add(appGrpPremisesDto);
        appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtoList);


        ParamUtil.setSessionAttr(bpc.request, "IndexNoCount", 0);

        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        ParamUtil.setSessionAttr(bpc.request, ClinicalLaboratoryDelegator.APPSVCRELATEDINFOMAP, (Serializable) svcRelatedMap);


        ParamUtil.setSessionAttr(bpc.request, ClinicalLaboratoryDelegator.GOVERNANCEOFFICERSDTOLIST, null);
        Map<String,AppGrpPrimaryDocDto> initBeforeReloadDocMap = new HashMap<>();
        ParamUtil.setSessionAttr(bpc.request, RELOADAPPGRPPRIMARYDOCMAP, (Serializable) initBeforeReloadDocMap);

        //error_msg
        ParamUtil.setSessionAttr(bpc.request, ERRORMAP_PREMISES, null);
        ParamUtil.setSessionAttr(bpc.request, APPGRPPRIMARYDOCERRMSGMAP, null);
        ParamUtil.setSessionAttr(bpc.request, ClinicalLaboratoryDelegator.ERRORMAP_GOVERNANCEOFFICERS, null);

    }

    private void  documentValid(HttpServletRequest request, Map<String,String> errorMap){
        log.debug(StringUtil.changeForLog("the do doValidatePremiss start ...."));
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList  = (List<AppGrpPrimaryDocDto>) ParamUtil.getSessionAttr(request, APPGRPPRIMARYDOCLIST);
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
            if(length>1024*1024){
                errorMap.put(keyName,"File size is too large!");
                continue;
            }
            Boolean flag=false;
            String name = appGrpPrimaryDocDto.getDocName();
            String substring = name.substring(name.lastIndexOf(".")+1);
            for(String fileType: AppServicesConsts.FILE_TYPE){
                if(fileType.equals(substring)){
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

    public String generateDropDownHtml(Map<String,String> premisesOnSiteAttr,List<SelectOption> selectOptionList, String firestOption){
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
            sBuffer.append(" <li data-value="+kv.getValue()+" class=\"option\">"+kv.getText()+"</li>");
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

}

