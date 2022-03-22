package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.action.HcsaFileAjaxController;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.helper.dataSubmission.DsConfigHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.client.ComFileRepoClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.VssDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.VssUploadFileService;
import com.ecquaria.cloud.moh.iais.utils.SingeFileUtil;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import static com.ecquaria.cloud.moh.iais.action.HcsaFileAjaxController.SEESION_FILES_MAP_AJAX;

/**
 * Process: MohVSSDataSubmission
 *
 * @Description VssDataSubmissionDelegator
 * @Auther chenlei on 12/13/2021.
 */
@Slf4j
@Delegator("vssDataSubmissionDelegator")
public class VssDataSubmissionDelegator {

    @Autowired
    private VssDataSubmissionService vssDataSubmissionService;

    @Autowired
    private ComFileRepoClient comFileRepoClient;

    public static final String ACTION_TYPE_CONFIRM = "confirm";

    /**
     * Step: Start
     *
     * @param bpc
     */
    public void doStart(BaseProcessClass bpc) {
        log.info(" -----VssDataSubmissionDelegator Start ------ ");
        DsConfigHelper.clearVssSession(bpc.request);
        DsConfigHelper.initVssConfig(bpc.request);
        DataSubmissionHelper.clearSession(bpc.request);
        ParamUtil.setSessionAttr(bpc.request, "vssFiles", null);
        ParamUtil.setSessionAttr(bpc.request,"seesion_files_map_ajax_feselectedVssFile",null);
        ParamUtil.setSessionAttr(bpc.request,"seesion_files_map_ajax_feselectedVssFile_MaxIndex",null);
        ParamUtil.clearSession(bpc.request,HcsaFileAjaxController.SEESION_FILES_MAP_AJAX+"selectedVssFile",SEESION_FILES_MAP_AJAX
                + "selectedVssFile" + HcsaFileAjaxController.SEESION_FILES_MAP_AJAX_MAX_INDEX,HcsaFileAjaxController.GLOBAL_MAX_INDEX_SESSION_ATTR);

    /*    String orgId = Optional.ofNullable(DataSubmissionHelper.getLoginContext(bpc.request))
                .map(LoginContext::getOrgId).orElse("");
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = vssDataSubmissionService.getVssSuperDataSubmissionDtoDraftByConds(orgId,DataSubmissionConsts.VSS_TYPE_SBT_VSS);
        if (vssSuperDataSubmissionDto != null) {
            ParamUtil.setRequestAttr(bpc.request, "hasDraft", Boolean.TRUE);
        }*/
    }


    /**
     * Step: PrepareSwitch
     *
     * @param bpc
     */
    public void prepareSwitch(BaseProcessClass bpc) {
        log.info(" ----- PrepareSwitch ------ ");
      /*  VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(bpc.request);
        if (DataSubmissionConsts.DS_APP_TYPE_RFC.equals(vssSuperDataSubmissionDto.getAppType())) {
            String crud_action_type = "rfc";
            ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_VSS, crud_action_type);
        }*/
        String actionType = getActionType(bpc.request);
        log.info(StringUtil.changeForLog("Action Type: " + actionType));
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_VSS, actionType);
        ParamUtil.setRequestAttr(bpc.request, "title", DataSubmissionHelper.getMainTitle(DataSubmissionConsts.DS_APP_TYPE_NEW));
        DsConfig config = DsConfigHelper.getCurrentConfig(DataSubmissionConsts.DS_VSS, bpc.request);
        String smallTitle = "";
        if (config != null) {
            smallTitle = config.getText();
        }
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>" + "Voluntary Sterilisation" + "</strong>");


    }

    private String getActionType(HttpServletRequest request) {
        String actionType = (String) ParamUtil.getRequestAttr(request, DataSubmissionConstant.CRUD_ACTION_TYPE_VSS);
        if (StringUtil.isEmpty(actionType)) {
            actionType = DataSubmissionHelper.initAction(DataSubmissionConsts.DS_VSS, DsConfigHelper.VSS_STEP_TREATMENT, request);
        }
        return actionType;
    }

    /**
     * Step: PrepareStepData
     *
     * @param bpc
     */
    public void prepareStepData(BaseProcessClass bpc) {
        log.info(" -----PrepareStepData ------ ");
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(bpc.request);
        if(vssSuperDataSubmissionDto == null){
            vssSuperDataSubmissionDto = initVssSuperDataSubmissionDto(bpc.request);
        }
        DataSubmissionHelper.setCurrentVssDataSubmission(vssSuperDataSubmissionDto, bpc.request);
        String pageStage = DataSubmissionConstant.PAGE_STAGE_PAGE;
        DsConfig currentConfig = DsConfigHelper.getCurrentConfig(DataSubmissionConsts.DS_VSS, bpc.request);
        if (DsConfigHelper.VSS_STEP_PREVIEW.equals(currentConfig.getCode())) {
            pageStage = DataSubmissionConstant.PAGE_STAGE_PREVIEW;
        }
       /* String crud_action_type = ParamUtil.getRequestString(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_VSS);
        //draft
        if (crud_action_type.equals("resume")) {
            vssSuperDataSubmissionDto = vssDataSubmissionService.getVssSuperDataSubmissionDtoDraftByConds(vssSuperDataSubmissionDto.getOrgId(),vssSuperDataSubmissionDto.getSubmissionType());
            if (vssSuperDataSubmissionDto == null) {
                log.warn("Can't resume data!");
                vssSuperDataSubmissionDto = new VssSuperDataSubmissionDto();
            }
            DataSubmissionHelper.setCurrentVssDataSubmission(vssSuperDataSubmissionDto,bpc.request);
            ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_VSS,DataSubmissionConstant.PAGE_STAGE_PAGE);
        } else if (crud_action_type.equals("delete")) {
            vssDataSubmissionService.deleteVssSuperDataSubmissionDtoDraftByConds(vssSuperDataSubmissionDto.getOrgId(), DataSubmissionConsts.VSS_TYPE_SBT_VSS);
        }*/
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, pageStage);
        String currentCode = currentConfig.getCode();
        log.info(StringUtil.changeForLog(" ----- PrepareStepData Step Code: " + currentCode + " ------ "));
        if (DsConfigHelper.VSS_STEP_TREATMENT.equals(currentCode)) {
            prepareTreatment(bpc.request);
        }else if (DsConfigHelper.VSS_STEP_CONSENT_PARTICULARS.equals(currentCode)) {
            prepareConsentParticulars(bpc.request);
        }else if (DsConfigHelper.VSS_STEP_TFSSP_PARTICULARS.equals(currentCode)) {
            prepareTfsspParticulars(bpc.request);

        }else if (DsConfigHelper.VSS_STEP_PREVIEW.equals(currentCode)) {
            preparePreview(bpc.request);
        }
    }

    private VssSuperDataSubmissionDto initVssSuperDataSubmissionDto(HttpServletRequest request) {
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = new VssSuperDataSubmissionDto();

        String orgId = Optional.ofNullable(DataSubmissionHelper.getLoginContext(request))
                .map(LoginContext::getOrgId).orElse("");
        vssSuperDataSubmissionDto.setOrgId(orgId);
        vssSuperDataSubmissionDto.setSubmissionType(DataSubmissionConsts.VSS_TYPE_SBT_VSS);
        vssSuperDataSubmissionDto.setAppType(DataSubmissionConsts.DS_APP_TYPE_NEW);
        vssSuperDataSubmissionDto.setFe(true);
        vssSuperDataSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());

        vssSuperDataSubmissionDto.setCycleDto(DataSubmissionHelper.initCycleDto(vssSuperDataSubmissionDto,
                DataSubmissionHelper.getLicenseeId(request),false));
        vssSuperDataSubmissionDto.setDataSubmissionDto(DataSubmissionHelper.initDataSubmission(vssSuperDataSubmissionDto, false));

        return vssSuperDataSubmissionDto;
    }

    /**
     * Step: DoStep
     *
     * @param bpc
     */
    public void doStep(BaseProcessClass bpc) {
        log.info(" ----- DoStep ------ ");
        String crudType = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        if ("return".equals(crudType)) {
            return;
        }
        DsConfig currentConfig = DsConfigHelper.getCurrentConfig(DataSubmissionConsts.DS_VSS, bpc.request);
        String currentCode = currentConfig.getCode();
        log.info(StringUtil.changeForLog(" ----- DoStep Step Code: " + currentCode + " ------ "));
        int status = 0;// 0: current page; -1: back / previous; 1: next; 2: submission
        if("previous".equals(crudType)){
            currentConfig.setStatus(status);
            DsConfigHelper.setConfig(DataSubmissionConsts.DS_VSS, currentConfig, bpc.request);
        }
        if (DsConfigHelper.VSS_STEP_TREATMENT.equals(currentCode)) {
            status = doTreatment(bpc.request);
        }else if (DsConfigHelper.VSS_STEP_CONSENT_PARTICULARS.equals(currentCode)) {
            status = doConsentParticulars(bpc.request);
        }else if (DsConfigHelper.VSS_STEP_TFSSP_PARTICULARS.equals(currentCode)) {
            status = doTfsspParticulars(bpc.request);

        }else if (DsConfigHelper.VSS_STEP_PREVIEW.equals(currentCode)) {
            status = doPreview(bpc.request);
        }
        if("next".equals(crudType)||DataSubmissionHelper.isToNextAction(bpc.request)){
            currentConfig.setStatus(status);
            DsConfigHelper.setConfig(DataSubmissionConsts.DS_VSS, currentConfig, bpc.request);
        }else {
            status = 0;
            currentConfig.setStatus(status);
            DsConfigHelper.setConfig(DataSubmissionConsts.DS_VSS, currentConfig, bpc.request);
        }

        log.info(StringUtil.changeForLog(" ----- DoStep Status: " + status + " ------ "));
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.ACTION_STATUS, status);
        ParamUtil.setRequestAttr(bpc.request, "currentStage", DataSubmissionConstant.PAGE_STAGE_PAGE);
    }

    private void prepareTreatment(HttpServletRequest request) {
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(request);
        VssTreatmentDto vssTreatmentDto = vssSuperDataSubmissionDto.getVssTreatmentDto() == null ? new VssTreatmentDto() : vssSuperDataSubmissionDto.getVssTreatmentDto();
        TreatmentDto treatmentDto = vssTreatmentDto.getTreatmentDto() == null ? new TreatmentDto() : vssTreatmentDto.getTreatmentDto();
        if(StringUtil.isEmpty(treatmentDto.getSterilizationReason())){
            treatmentDto.setSterilizationReason(DataSubmissionConsts.MAIN_REASON_FOR_STERILIZATION_MENTAL_ILLNESS);
        }
        vssTreatmentDto.setTreatmentDto(treatmentDto);
        vssSuperDataSubmissionDto.setVssTreatmentDto(vssTreatmentDto);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION, vssSuperDataSubmissionDto);

    }

    private int doTreatment(HttpServletRequest request) {
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(request);
        vssSuperDataSubmissionDto = vssSuperDataSubmissionDto  == null ? new VssSuperDataSubmissionDto() : vssSuperDataSubmissionDto;
        VssTreatmentDto vssTreatmentDto = vssSuperDataSubmissionDto.getVssTreatmentDto() == null ? new VssTreatmentDto() : vssSuperDataSubmissionDto.getVssTreatmentDto();
        TreatmentDto treatmentDto = vssTreatmentDto.getTreatmentDto() == null ? new TreatmentDto() : vssTreatmentDto.getTreatmentDto();
        ControllerHelper.get(request,treatmentDto);
        try {
           int age= -Formatter.compareDateByDay(treatmentDto.getBirthData());
            treatmentDto.setAge(age/365);
        }catch (Exception e){
           log.error(e.getMessage(),e);
        }
        if (StringUtil.isNotEmpty(treatmentDto.getEthnicGroup()) && !treatmentDto.getEthnicGroup().equals("ECGP004")) {
            treatmentDto.setOtherEthnicGroup(null);
        }
        if (StringUtil.isNotEmpty(treatmentDto.getOccupation()) && !treatmentDto.getOccupation().equals("VSSOP011")) {
            treatmentDto.setOtherOccupation(null);
        }
        if (StringUtil.isNotEmpty(treatmentDto.getSterilizationReason()) && !treatmentDto.getSterilizationReason().equals("VSSRFS009")) {
            treatmentDto.setOtherSterilizationReason(null);
        }
        vssTreatmentDto.setTreatmentDto(treatmentDto);
        vssSuperDataSubmissionDto.setVssTreatmentDto(vssTreatmentDto);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION, vssSuperDataSubmissionDto);
        Map<String,String> errMap = IaisCommonUtils.genNewHashMap();
        String actionType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
        if("next".equals(actionType) || DataSubmissionHelper.isToNextAction(request)){
            ValidationResult result = WebValidationHelper.validateProperty(treatmentDto,"VSS");
            if(result !=null){
                errMap.putAll(result.retrieveAll());
                if(errMap.isEmpty()){
                    treatmentDto.setHeadStatus(true);
                    vssTreatmentDto.setTreatmentDto(treatmentDto);
                    vssSuperDataSubmissionDto.setVssTreatmentDto(vssTreatmentDto);
                    ParamUtil.setSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION, vssSuperDataSubmissionDto);
                }
            }
        }

        if(!errMap.isEmpty()){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
            return 0;
        }
        return 1;
    }

    private void prepareConsentParticulars(HttpServletRequest request) {
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(request);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION, vssSuperDataSubmissionDto);
    }

    private int doConsentParticulars(HttpServletRequest request) {
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(request);
        vssSuperDataSubmissionDto = vssSuperDataSubmissionDto  == null ? new VssSuperDataSubmissionDto() : vssSuperDataSubmissionDto;
        VssTreatmentDto vssTreatmentDto = vssSuperDataSubmissionDto.getVssTreatmentDto() == null ? new VssTreatmentDto() : vssSuperDataSubmissionDto.getVssTreatmentDto();
        GuardianAppliedPartDto guardianAppliedPartDto = vssTreatmentDto.getGuardianAppliedPartDto() == null ? new GuardianAppliedPartDto() : vssTreatmentDto.getGuardianAppliedPartDto();
        ControllerHelper.get(request,guardianAppliedPartDto);
        String appliedPartBirthday = ParamUtil.getString(request,"appliedPartBirthday");
        String guardianBirthday = ParamUtil.getString(request,"guardianBirthday");
        String courtOrderIssueDate = ParamUtil.getString(request,"courtOrderIssueDate");
        try {
            Date aDate = Formatter.parseDate(appliedPartBirthday);
            Date gDate = Formatter.parseDate(guardianBirthday);
            Date cDate = Formatter.parseDate(courtOrderIssueDate);
            guardianAppliedPartDto.setAppliedPartBirthday(aDate);
            guardianAppliedPartDto.setGuardianBirthday(gDate);
            guardianAppliedPartDto.setCourtOrderIssueDate(cDate);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        guardianAppliedPartDto.setVssDocumentDto( IaisCommonUtils.isNotEmpty(guardianAppliedPartDto.getVssDocumentDto()) ? guardianAppliedPartDto.getVssDocumentDto() : IaisCommonUtils.genNewArrayList());
        setFiles(guardianAppliedPartDto,request);
        vssTreatmentDto.setGuardianAppliedPartDto(guardianAppliedPartDto);
        vssSuperDataSubmissionDto.setVssTreatmentDto(vssTreatmentDto);

        ParamUtil.setSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION, vssSuperDataSubmissionDto);

        Map<String,String> errMap = IaisCommonUtils.genNewHashMap();
        String actionType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
        if("next".equals(actionType) || DataSubmissionHelper.isToNextAction(request)){
            ValidationResult result = WebValidationHelper.validateProperty(guardianAppliedPartDto,"VSS");
            if(result !=null){
                errMap.putAll(result.retrieveAll());
                if(errMap.isEmpty()){
                    guardianAppliedPartDto.setHeadStatus(true);
                    vssTreatmentDto.setGuardianAppliedPartDto(guardianAppliedPartDto);
                    vssSuperDataSubmissionDto.setVssTreatmentDto(vssTreatmentDto);
                    ParamUtil.setSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION, vssSuperDataSubmissionDto);
                }
            }
        }
        if(!errMap.isEmpty()){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
            return 0;
        }
        return 1;
    }

    private void prepareTfsspParticulars(HttpServletRequest request) {
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(request);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION, vssSuperDataSubmissionDto);
        ParamUtil.setRequestAttr(request, DataSubmissionConstant.CURRENT_PAGE_STAGE,ACTION_TYPE_CONFIRM);
    }

    private int doTfsspParticulars(HttpServletRequest request) {
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(request);
        vssSuperDataSubmissionDto = vssSuperDataSubmissionDto  == null ? new VssSuperDataSubmissionDto() : vssSuperDataSubmissionDto;
        VssTreatmentDto vssTreatmentDto = vssSuperDataSubmissionDto.getVssTreatmentDto() == null ? new VssTreatmentDto() : vssSuperDataSubmissionDto.getVssTreatmentDto();
        SexualSterilizationDto sexualSterilizationDto = vssTreatmentDto.getSexualSterilizationDto() == null ? new SexualSterilizationDto() : vssTreatmentDto.getSexualSterilizationDto();
        TreatmentDto treatmentDto = vssTreatmentDto.getTreatmentDto() == null ? new TreatmentDto() : vssTreatmentDto.getTreatmentDto();
        String doctorReignNo = ParamUtil.getString(request,"doctorReignNo");
        String doctorName = ParamUtil.getString(request,"doctorName");
        String sterilizationMethod = ParamUtil.getString(request,"sterilizationMethod");
        String operationDate = ParamUtil.getString(request,"operationDate");
        String reviewedByHec = ParamUtil.getString(request,"reviewedByHec");
        String hecReviewDate = ParamUtil.getString(request,"hecReviewDate");
        String disinfectionPlace = ParamUtil.getString(request,"disinfectionPlace");
        sexualSterilizationDto.setDisinfectionPlace(disinfectionPlace);
        sexualSterilizationDto.setDoctorReignNo(doctorReignNo);
        sexualSterilizationDto.setDoctorName(doctorName);
        sexualSterilizationDto.setSterilizationMethod(sterilizationMethod);
        if(StringUtil.isNotEmpty(reviewedByHec)){
            sexualSterilizationDto.setReviewedByHec(reviewedByHec.equals("true") ? true : false);
        }
        try {
            Date oDate = Formatter.parseDate(operationDate);
            Date hDate = Formatter.parseDate(hecReviewDate);
            sexualSterilizationDto.setOperationDate(oDate);
            sexualSterilizationDto.setHecReviewDate(hDate);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        if(StringUtil.isNotEmpty(sexualSterilizationDto.getSterilizationMethod())){
            if(sexualSterilizationDto.getSterilizationMethod().equals(DataSubmissionConsts.METHOD_OF_STERILIZATION_TUBAL_LIGATION) && treatmentDto.getGender().equals(DataSubmissionConsts.GENDER_MALE)){
                sexualSterilizationDto.setUnreasonable(true);
            }
        }
        vssTreatmentDto.setSexualSterilizationDto(sexualSterilizationDto);
        vssSuperDataSubmissionDto.setVssTreatmentDto(vssTreatmentDto);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION, vssSuperDataSubmissionDto);
        Map<String,String> errMap = IaisCommonUtils.genNewHashMap();
        String actionType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
        if("next".equals(actionType) || DataSubmissionHelper.isToNextAction(request)){
            ValidationResult result = WebValidationHelper.validateProperty(sexualSterilizationDto,"VSS");
            if(result !=null){
                errMap.putAll(result.retrieveAll());
                if(errMap.isEmpty()){
                    sexualSterilizationDto.setHeadStatus(true);
                    vssTreatmentDto.setSexualSterilizationDto(sexualSterilizationDto);
                    vssSuperDataSubmissionDto.setVssTreatmentDto(vssTreatmentDto);
                    ParamUtil.setSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION, vssSuperDataSubmissionDto);
                }
            }
        }
        if(!errMap.isEmpty()){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errMap));
            return 0;
        }
        return 1;
    }

    private void setFiles(GuardianAppliedPartDto guardianAppliedPartDto,HttpServletRequest request){
        List<VssDocumentDto> vssDoc = guardianAppliedPartDto.getVssDocumentDto();
        vssDoc.clear();
        log.info("-----------ajax-upload-file start------------");
        Map<String, File> map = (Map<String, File>) ParamUtil.getSessionAttr(request, SEESION_FILES_MAP_AJAX+"selectedVssFile");
        IaisEGPHelper.getCurrentAuditTrailDto().getMohUserId();
        if(IaisCommonUtils.isNotEmpty(map)){
            Date date = new Date();
            List<File> files =IaisCommonUtils.genNewArrayList(1);
            String mapKey = "selectedVssFile";
            map.forEach( (k, v)->{
                if (v != null) {
                    VssDocumentDto vssDocumentDto = new VssDocumentDto();
                    vssDocumentDto.setDocName(v.getName());
                    long size = v.length() / 1024;
                    vssDocumentDto.setDocSize(Integer.parseInt(String.valueOf(size)));
                    String md5Code = SingeFileUtil.getInstance().getFileMd5(v);
                    vssDocumentDto.setMd5Code(md5Code);
                    vssDocumentDto.setSubmitBy(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
                    vssDocumentDto.setSubmitDt(date);
                    vssDocumentDto.setStatus(DataSubmissionConsts.VSS_NEED_SYSN_BE_STATUS);
                    FileRepoDto fileRepoDto = new FileRepoDto();
                    fileRepoDto.setFileName(v.getName());
                    fileRepoDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    fileRepoDto.setRelativePath(AppConsts.FALSE);
                    files.add(v);
                    //save file to file DB
                    String repoId = comFileRepoClient.saveFileRepo(files).get(0);
                    files.clear();
                    vssDocumentDto.setFileRepoId(repoId);
                    vssDocumentDto.setSeqNum(Integer.parseInt(k.replace(mapKey,"")));
                    vssDoc.add(vssDocumentDto);
                }

            });
            vssDoc.sort(Comparator.comparing(VssDocumentDto :: getSeqNum));
        }
        request.getSession().setAttribute("vssFiles", vssDoc);
        request.getSession().setAttribute("seesion_files_map_ajax_feselectedVssFile", map);
        request.getSession().setAttribute("seesion_files_map_ajax_feselectedVssFile_MaxIndex", vssDoc.size());

    }

    private void preparePreview(HttpServletRequest request) {
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(request);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION, vssSuperDataSubmissionDto);
        ParamUtil.setRequestAttr(request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_VSS);
    }

    private int doPreview(HttpServletRequest request) {
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(request);
        vssSuperDataSubmissionDto = vssSuperDataSubmissionDto  == null ? new VssSuperDataSubmissionDto() : vssSuperDataSubmissionDto;
        VssTreatmentDto vssTreatmentDto = vssSuperDataSubmissionDto.getVssTreatmentDto() == null ? new VssTreatmentDto() : vssSuperDataSubmissionDto.getVssTreatmentDto();
        TreatmentDto treatmentDto = vssTreatmentDto.getTreatmentDto() == null ? new TreatmentDto() : vssTreatmentDto.getTreatmentDto();
        GuardianAppliedPartDto guardianAppliedPartDto = vssTreatmentDto.getGuardianAppliedPartDto() == null ? new GuardianAppliedPartDto() : vssTreatmentDto.getGuardianAppliedPartDto();
        SexualSterilizationDto sexualSterilizationDto = vssTreatmentDto.getSexualSterilizationDto() == null ? new SexualSterilizationDto() : vssTreatmentDto.getSexualSterilizationDto();
        Map<String,String> errMap = IaisCommonUtils.genNewHashMap();
        String actionType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
        if("next".equals(actionType) || DataSubmissionHelper.isToNextAction(request)){
            ValidationResult treatmentDtoResult = WebValidationHelper.validateProperty(treatmentDto,"VSS");
            ValidationResult gapResult = WebValidationHelper.validateProperty(guardianAppliedPartDto,"VSS");
            ValidationResult ssResult = WebValidationHelper.validateProperty(sexualSterilizationDto,"VSS");
            if(treatmentDtoResult !=null){
                errMap.putAll(treatmentDtoResult.retrieveAll());
            }
            if(gapResult !=null){
                errMap.putAll(gapResult.retrieveAll());
            }
            if(ssResult !=null){
                errMap.putAll(ssResult.retrieveAll());
            }
        }
        if(!errMap.isEmpty()){
            return 0;
        }
        return 2;
    }
    /**
     * Step: DoSubmission
     *
     * @param bpc
     */
    public void doSubmission(BaseProcessClass bpc){
        log.info(" ----- DoSubmission ------ ");
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(bpc.request);

        DataSubmissionDto dataSubmissionDto = vssSuperDataSubmissionDto.getDataSubmissionDto();
        CycleDto cycle = vssSuperDataSubmissionDto.getCycleDto();
        String cycleType = cycle.getCycleType();
       String status = cycle.getStatus();
        if (StringUtil.isEmpty(dataSubmissionDto.getSubmissionNo())) {
            String submissionNo = vssDataSubmissionService.getSubmissionNo(DataSubmissionConsts.DS_VSS);
            dataSubmissionDto.setSubmissionNo(submissionNo);
        }
        if (DataSubmissionConsts.DS_APP_TYPE_RFC.equals(dataSubmissionDto.getAppType())) {
            dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_AMENDED);
        } else if (StringUtil.isEmpty(dataSubmissionDto.getStatus())) {
            dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_COMPLETED);
        }

        if (StringUtil.isEmpty(status)) {
            status = DataSubmissionConsts.DS_STATUS_ACTIVE;
        }
        cycle.setStatus(status);
        vssSuperDataSubmissionDto.setCycleDto(cycle);
        LoginContext loginContext = DataSubmissionHelper.getLoginContext(bpc.request);
        if (loginContext != null) {
            dataSubmissionDto.setSubmitBy(loginContext.getUserId());
            dataSubmissionDto.setSubmitDt(new Date());
        }
        VssTreatmentDto vssTreatmentDto = vssSuperDataSubmissionDto.getVssTreatmentDto();
        SexualSterilizationDto sexualSterilizationDto = vssTreatmentDto.getSexualSterilizationDto();

        if(sexualSterilizationDto.getOperationDate() != null ) {
            try {
                if(Formatter.compareDateByDay(dataSubmissionDto.getSubmitDt(),sexualSterilizationDto.getOperationDate())>30){
                    sexualSterilizationDto.setLateSubmit(true);
                }
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }

        }
        vssTreatmentDto.setSexualSterilizationDto(sexualSterilizationDto);
        vssSuperDataSubmissionDto.setVssTreatmentDto(vssTreatmentDto);

        vssSuperDataSubmissionDto = vssDataSubmissionService.saveVssSuperDataSubmissionDto(vssSuperDataSubmissionDto);
        try {
             vssSuperDataSubmissionDto = vssDataSubmissionService.saveVssSuperDataSubmissionDtoToBE(vssSuperDataSubmissionDto);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("The Eic saveVssSuperDataSubmissionDtoToBE failed ===>" + e.getMessage()), e);
        }
        if (!StringUtil.isEmpty(vssSuperDataSubmissionDto.getDraftId())) {
            vssDataSubmissionService.updateDataSubmissionDraftStatus(vssSuperDataSubmissionDto.getDraftId(),
                    DataSubmissionConsts.DS_STATUS_INACTIVE);
        }
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.VSS_DATA_SUBMISSION, vssSuperDataSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request, "emailAddress", DataSubmissionHelper.getLicenseeEmailAddrs(bpc.request));
        ParamUtil.setRequestAttr(bpc.request, "submittedBy", DataSubmissionHelper.getLoginContext(bpc.request).getUserName());
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, DataSubmissionConstant.PAGE_STAGE_ACK);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_ACKVSS);
    }

    /**
     * Step: DoDraft
     *
     * @param bpc
     */
    public void doDraft(BaseProcessClass bpc) {
        log.info(" ----- DoDraft ------ ");
        String currentStage = (String) ParamUtil.getRequestAttr(bpc.request, "currentStage");
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_VSS, currentStage);
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(bpc.request);
        if (vssSuperDataSubmissionDto != null) {
            vssSuperDataSubmissionDto.setDraftNo(vssDataSubmissionService.getDraftNo(DataSubmissionConsts.DS_VSS,
                    vssSuperDataSubmissionDto.getDraftNo()));
            vssSuperDataSubmissionDto = vssDataSubmissionService.saveDataSubmissionDraft(vssSuperDataSubmissionDto);
            DataSubmissionHelper.setCurrentVssDataSubmission(vssSuperDataSubmissionDto, bpc.request);
            ParamUtil.setRequestAttr(bpc.request, "saveDraftSuccess", "success");
        } else {
            log.info(StringUtil.changeForLog("The vssSuperDataSubmission is null"));
        }
    }

    /**
     * Step: DoRfc
     *
     * @param bpc
     */
    public void doRfc(BaseProcessClass bpc) {
        log.info(" ----- DoRfc ------ ");
        if(isRfc(bpc.request)){
            VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getOldVssSuperDataSubmissionDto(bpc.request);
            VssTreatmentDto vssTreatmentDto = vssSuperDataSubmissionDto.getVssTreatmentDto();
            if(vssSuperDataSubmissionDto != null && vssSuperDataSubmissionDto.getVssTreatmentDto()!= null && vssTreatmentDto.equals(vssSuperDataSubmissionDto.getVssTreatmentDto())){
                ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, DataSubmissionConstant.PAGE_STAGE_PAGE);
            }
        }
    }

    /**
     * Step: DoWithdraw
     *
     * @param bpc
     */
    public void doWithdraw(BaseProcessClass bpc) {
        log.info(" ----- DoWithdraw ------ ");
    }

    /**
     * Step: DoControl
     *
     * @param bpc
     */
    public void doControl(BaseProcessClass bpc) {
        log.info(" ----- DoControl ------ ");
        String crudType = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        String actionType = null;
        if ("return".equals(crudType)) {
            actionType = "return";
        } else if ("next".equals(crudType)) {
            Integer status = (Integer) ParamUtil.getRequestAttr(bpc.request, DataSubmissionConstant.ACTION_STATUS);
            if (status == null || 0 == status) {// current
                actionType = DataSubmissionHelper.setCurrentAction(DataSubmissionConsts.DS_VSS, bpc.request);
            } else if (-1 == status) { // previous
                actionType = DataSubmissionHelper.setPreviousAction(DataSubmissionConsts.DS_VSS, bpc.request);
            } else if (1 == status) { // next
                actionType = DataSubmissionHelper.setNextAction(DataSubmissionConsts.DS_VSS, bpc.request);
            } else if (2 == status) {// to submission
                actionType = "submission";
            }
        } else if (DataSubmissionHelper.isToNextAction(bpc.request)) {
            Integer status = (Integer) ParamUtil.getRequestAttr(bpc.request, DataSubmissionConstant.ACTION_STATUS);
            if (status == null || 0 == status) {// current
                actionType = DataSubmissionHelper.setCurrentAction(DataSubmissionConsts.DS_VSS, bpc.request);
            } else if (1 == status) { // nexts
                actionType = crudType;
                DsConfigHelper.setActiveConfig(actionType, bpc.request);
            }
        }else if ("previous".equals(crudType)) {
            actionType = DataSubmissionHelper.setPreviousAction(DataSubmissionConsts.DS_VSS, bpc.request);
        } else if ("preview".equals(crudType)) {
                actionType = crudType;
        } else {
            actionType = crudType;
            DsConfigHelper.setActiveConfig(actionType, bpc.request);
        }
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_VSS, actionType);
    }

    /**
     * Step: DoReturn
     *
     * @param bpc
     */
    public void doReturn(BaseProcessClass bpc) throws IOException {
        log.info(" ----- DoReturn ------ ");
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(bpc.request);
        String target = InboxConst.URL_MAIN_WEB_MODULE + "MohInternetInbox";
        if (vssSuperDataSubmissionDto != null && DataSubmissionConsts.DS_APP_TYPE_NEW.equals(vssSuperDataSubmissionDto.getAppType())) {
            target = InboxConst.URL_LICENCE_WEB_MODULE + "MohDataSubmission";
        }
        StringBuilder url = new StringBuilder();
        url.append(InboxConst.URL_HTTPS)
                .append(bpc.request.getServerName())
                .append(target);
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
    }

    private boolean isRfc(HttpServletRequest request) {
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(request);
        return vssSuperDataSubmissionDto != null && vssSuperDataSubmissionDto.getDataSubmissionDto() != null && DataSubmissionConsts.DS_APP_TYPE_RFC.equalsIgnoreCase(vssSuperDataSubmissionDto.getDataSubmissionDto().getAppType());
    }
}
