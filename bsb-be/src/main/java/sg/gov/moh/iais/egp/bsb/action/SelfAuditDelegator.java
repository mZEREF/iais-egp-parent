package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppIntranetDocDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sg.gov.moh.iais.egp.bsb.client.AuditClient;
import sg.gov.moh.iais.egp.bsb.client.DocClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.constant.AuditConstants;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditDocDto;
import sg.gov.moh.iais.egp.bsb.entity.*;
import sg.gov.moh.iais.egp.bsb.util.JoinAddress;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Zhu Tangtang
 */
@Slf4j
@Delegator(value = "selfAuditDelegator")
public class SelfAuditDelegator {

    @Autowired
    private AuditClient auditClient;

    @Autowired
    private FileRepoClient fileRepoClient;

    @Autowired
    private DocClient docClient;

    /**
     * StartStep: startStep
     *
     * @param bpc
     * @throws IllegalAccessException
     */
    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(AuditConstants.MODULE_AUDIT, AuditConstants.FUNCTION_AUDIT);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, AuditConstants.class);
        ParamUtil.setSessionAttr(request, AuditConstants.PARAM_AUDIT_SEARCH, null);
    }

    /**
     * AutoStep: prepareData
     *
     * @param bpc
     */
    public void prepareFacilitySelfAuditData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,AuditConstants.FACILITY,null);
        ParamUtil.setSessionAttr(request,AuditConstants.AUDIT_DOC_DTO, null);

        String auditId = ParamUtil.getMaskedString(request, AuditConstants.AUDIT_ID);
        FacilityAudit facilityAudit = auditClient.getFacilityAuditById(auditId).getEntity();

        Facility facility = facilityAudit.getFacility();
        Application application = new Application();
        application.setFacility(facility);
        String facilityAddress = JoinAddress.joinAddress(application);
        facility.setFacilityAddress(facilityAddress);

        ParamUtil.setSessionAttr(request,AuditConstants.FACILITY,facility);
        ParamUtil.setRequestAttr(request, AuditConstants.FACILITY_AUDIT, facilityAudit);
    }

    /**
     * StartStep: doDocument
     *
     * @param bpc
     * @throws
     */
//    public void doDocument(BaseProcessClass bpc) {
//        log.debug(StringUtil.changeForLog("the do doDocument start ...."));
//        String doDocument = ParamUtil.getString(bpc.request, "uploadFile");
//        String interalFileId = ParamUtil.getMaskedString(bpc.request, "interalFileId");
////        if (!StringUtil.isEmpty(interalFileId)) {
////            fillUpCheckListGetAppClient.deleteAppIntranetDocsById(interalFileId);
////        }
//
////        if ("Y".equals(doDocument)) {
////            HcsaApplicationProcessUploadFileValidate uploadFileValidate = new HcsaApplicationProcessUploadFileValidate();
////            Map<String, String> errorMap = uploadFileValidate.validate(bpc.request);
////            if (!errorMap.isEmpty()) {
////                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
////                ParamUtil.setRequestAttr(bpc.request, "uploadFileValidate", "Y");
////            } else {
//                MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
//                CommonsMultipartFile selectedFile = (CommonsMultipartFile) mulReq.getFile("selectedFile");
//                AppIntranetDocDto appIntranetDocDto = new AppIntranetDocDto();
//                FacilityDoc facilityDoc = new FacilityDoc();
//                if (selectedFile != null) {
//                    //size
//                    Facility facility=new Facility();
//                    facility.setId("FBD9D87E-48F0-EB11-8B7D-000C293F0C97");
//                    facilityDoc.setFacility(facility);
//
//                    facilityDoc.setSubmitAt(new Date());
//                    facilityDoc.setSubmitBy(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
//
//                    long size = selectedFile.getSize();
//                    facilityDoc.setSize(size / 1024);
//                    if (!StringUtil.isEmpty(selectedFile.getOriginalFilename())) {
//                        log.info(StringUtil.changeForLog("HcsaApplicationAjaxController uploadInternalFile OriginalFilename ==== " + selectedFile.getOriginalFilename()));
//                        //type
//                        String[] fileSplit = selectedFile.getOriginalFilename().split("\\.");
////                        String fileType = fileSplit[fileSplit.length - 1];
////                        facilityDoc.setType(fileType);
//                        //name
//                        String fileName = fileSplit[0];
//                        facilityDoc.setName(fileName);
//
//                        String fileRemark = ParamUtil.getString(bpc.request, "fileRemark");
////                        if (StringUtil.isEmpty(fileRemark)) {
////                            fileRemark = fileName;
////                        }
//                        //set document
////                        facilityDoc.setDocDesc(fileRemark);
//                        //status
////                        facilityDoc.setDocStatus(AppConsts.COMMON_STATUS_ACTIVE);
//                        //set audit
//                        FileRepoDto fileRepoDto = new FileRepoDto();
//                        fileRepoDto.setFileName(fileName);
//                        fileRepoDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
//                        fileRepoDto.setRelativePath(AppConsts.FALSE);
//
//                        //save file to file DB
//                        String repo_id = fileRepoClient.saveFiles(selectedFile, JsonUtil.parseToJson(fileRepoDto)).getEntity();
//                        facilityDoc.setFileRepoId(repo_id);
//                    }
//                }
//                docClient.saveFacilityDoc(facilityDoc).getEntity();
////                appIntranetDocDto.setId(id);
////            }
////        }
//        log.debug(StringUtil.changeForLog("the do doDocument end ...."));
//    }

    /**
     * AutoStep: submit
     *
     * @param bpc
     */
    public void submitSelfAuditReport(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String auditId = ParamUtil.getMaskedString(request, AuditConstants.AUDIT_ID);
        String scenarioCategory = ParamUtil.getRequestString(request,AuditConstants.PARAM_SCENARIO_CATEGORY);
        FacilityAudit audit = new FacilityAudit();
        audit.setScenarioCategory(scenarioCategory);
        audit.setId(auditId);
        audit.setStatus(AuditConstants.PARAM_AUDIT_STATUS_PENDING_DO);
        FacilityAuditApp facilityAuditApp = auditClient.saveSelfAuditReport(audit).getEntity();
    }

    /**
     * AutoStep: prepareData
     *
     * @param bpc
     */
    public void prepareDOProcessSelfAuditData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,AuditConstants.FACILITY,null);
        ParamUtil.setSessionAttr(request,AuditConstants.AUDIT_DOC_DTO, null);

        String auditAppId = "2228B667-3815-EC11-BE6E-000C298D317C";
        FacilityAuditApp facilityAuditApp = auditClient.getFacilityAuditAppById(auditAppId).getEntity();
        FacilityAudit facilityAudit = auditClient.getFacilityAuditById(facilityAuditApp.getFacilityAudit().getId()).getEntity();
        facilityAuditApp.setFacilityAudit(facilityAudit);

        Facility facility = facilityAudit.getFacility();
        Application application = new Application();
        application.setFacility(facility);
        String facilityAddress = JoinAddress.joinAddress(application);
        facility.setFacilityAddress(facilityAddress);

        List<FacilityDoc> facilityDocList = docClient.getFacilityDocByFacId(facility.getId()).getEntity();
        List<FacilityDoc> docList = new ArrayList<>();
        for (FacilityDoc facilityDoc : facilityDocList) {
            //这里拿不到，只能拿到当前用户名
            String submitByName = IaisEGPHelper.getCurrentAuditTrailDto().getMohUserId();
            facilityDoc.setSubmitByName(submitByName);
            docList.add(facilityDoc);
        }
        AuditDocDto auditDocDto = new AuditDocDto();
        auditDocDto.setFacilityDocs(docList);

        List<FacilityAuditAppHistory> histories = auditClient.getAllHistoryByAuditAppId(facilityAuditApp.getId()).getEntity();

        ParamUtil.setSessionAttr(request,AuditConstants.FACILITY,facility);
        ParamUtil.setRequestAttr(request, AuditConstants.FACILITY_AUDIT_APP, facilityAuditApp);
        ParamUtil.setSessionAttr(request,AuditConstants.AUDIT_DOC_DTO, auditDocDto);
        ParamUtil.setRequestAttr(request,AuditConstants.PARAM_HISTORY,histories);
    }

    /**
     * audit app status change to AUDITST005
     * @param bpc
     */
    public void DOVerified(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityAuditApp auditApp = before(request);
        auditApp.setStatus(AuditConstants.PARAM_AUDIT_STATUS_PENDING_AO);
        auditClient.processAuditDate(auditApp).getEntity();
        FacilityAuditAppHistory auditAppHistory = abHistory(request);
        auditAppHistory.setAppStatus(auditApp.getStatus());
        auditClient.saveHistory(auditAppHistory);
    }

    /**
     * audit app status change to AUDITST002
     * @param bpc
     */
    public void DORequestForInformation(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityAuditApp auditApp = before(request);
        auditApp.setStatus(AuditConstants.PARAM_AUDIT_STATUS_PENDING_APPLICANT_INPUT);
        auditClient.processAuditDate(auditApp);
        FacilityAuditAppHistory auditAppHistory = abHistory(request);
        auditAppHistory.setAppStatus(auditApp.getStatus());
        auditClient.saveHistory(auditAppHistory);
    }

    /**
     * audit app status change to AUDITST005
     * @param bpc
     */
    public void DOReject(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityAuditApp auditApp = before(request);
        auditApp.setStatus(AuditConstants.PARAM_AUDIT_STATUS_PENDING_AO);
        auditClient.processAuditDate(auditApp);
        FacilityAuditAppHistory auditAppHistory = abHistory(request);
        auditAppHistory.setAppStatus(auditApp.getStatus());
        auditClient.saveHistory(auditAppHistory);
    }

    /**
     * AutoStep: prepareData
     *
     * @param bpc
     */
    public void prepareAOProcessSelfAuditData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,AuditConstants.FACILITY,null);
        ParamUtil.setSessionAttr(request,AuditConstants.AUDIT_DOC_DTO, null);

        String auditAppId = "2228B667-3815-EC11-BE6E-000C298D317C";
        FacilityAuditApp facilityAuditApp = auditClient.getFacilityAuditAppById(auditAppId).getEntity();
        FacilityAudit facilityAudit = auditClient.getFacilityAuditById(facilityAuditApp.getFacilityAudit().getId()).getEntity();
        facilityAuditApp.setFacilityAudit(facilityAudit);

        Facility facility = facilityAudit.getFacility();
        Application application = new Application();
        application.setFacility(facility);
        String facilityAddress = JoinAddress.joinAddress(application);
        facility.setFacilityAddress(facilityAddress);

        List<FacilityDoc> facilityDocList = docClient.getFacilityDocByFacId(facility.getId()).getEntity();
        List<FacilityDoc> docList = new ArrayList<>();
        for (FacilityDoc facilityDoc : facilityDocList) {
            //这里拿不到，只能拿到当前用户名
            String submitByName = IaisEGPHelper.getCurrentAuditTrailDto().getMohUserId();
            facilityDoc.setSubmitByName(submitByName);
            docList.add(facilityDoc);
        }
        AuditDocDto auditDocDto = new AuditDocDto();
        auditDocDto.setFacilityDocs(docList);

        List<FacilityAuditAppHistory> histories = auditClient.getAllHistoryByAuditAppId(facilityAuditApp.getId()).getEntity();

        ParamUtil.setSessionAttr(request,AuditConstants.FACILITY,facility);
        ParamUtil.setRequestAttr(request, AuditConstants.FACILITY_AUDIT_APP, facilityAuditApp);
        ParamUtil.setSessionAttr(request,AuditConstants.AUDIT_DOC_DTO, auditDocDto);
        ParamUtil.setRequestAttr(request,AuditConstants.PARAM_HISTORY,histories);
    }

    /**
     * audit app status change to AUDITST004
     * @param bpc
     */
    public void AOInternalClarifications(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityAuditApp auditApp = before(request);
        auditApp.setStatus(AuditConstants.PARAM_AUDIT_STATUS_PENDING_DO);
        auditApp.getFacilityAudit().setStatus(AuditConstants.PARAM_AUDIT_STATUS_PENDING_DO);
        auditClient.processAuditDate(auditApp);
        FacilityAuditAppHistory auditAppHistory = abHistory(request);
        auditAppHistory.setAppStatus(auditApp.getStatus());
        auditClient.saveHistory(auditAppHistory);
    }

    /**
     * audit app status change to AUDITST003
     * @param bpc
     */
    public void AOApproved(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityAuditApp auditApp = before(request);
        auditApp.setStatus(AuditConstants.PARAM_AUDIT_STATUS_COMPLETED);
        auditApp.getFacilityAudit().setStatus(AuditConstants.PARAM_AUDIT_STATUS_COMPLETED);
        auditClient.processAuditDate(auditApp);
        FacilityAuditAppHistory auditAppHistory = abHistory(request);
        auditAppHistory.setAppStatus(auditApp.getStatus());
        auditClient.saveHistory(auditAppHistory);
    }


    private FacilityAuditApp before(HttpServletRequest request){
        String auditAppId = ParamUtil.getMaskedString(request,AuditConstants.AUDIT_APP_ID);
        String auditOutCome = ParamUtil.getRequestString(request,AuditConstants.AUDIT_OUTCOME);
        String remark = ParamUtil.getRequestString(request,AuditConstants.PARAM_REMARKS);
        String aoRemark = ParamUtil.getRequestString(request,AuditConstants.AO_REMARKS);
        String finalRemark = ParamUtil.getRequestString(request,AuditConstants.FINAL_REMARK);//on or null
        String decision = ParamUtil.getRequestString(request,AuditConstants.PARAM_DECISION);

        FacilityAudit audit = new FacilityAudit();
        FacilityAuditApp auditApp = new FacilityAuditApp();
        auditApp.setId(auditAppId);
        auditApp.setDoRemarks(remark);
        auditApp.setAoRemarks(aoRemark);
        auditApp.setDoDecision(decision);
        audit.setAuditOutcome(auditOutCome);
        audit.setRemarks(aoRemark);
        if (finalRemark==null) {
            audit.setFinalRemarks("No");
        }else {
            audit.setFinalRemarks("Yes");
        }
        auditApp.setFacilityAudit(audit);
        return auditApp;
    }

    private FacilityAuditAppHistory abHistory(HttpServletRequest request){
        FacilityAuditApp auditApp = before(request);
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        FacilityAuditAppHistory auditAppHistory = new FacilityAuditAppHistory();
        auditAppHistory.setActionBy(loginContext.getUserName());
        auditAppHistory.setProcessDecision(auditApp.getDoDecision());
        auditAppHistory.setAuditAppId(auditApp.getId());
        if (StringUtil.isNotEmpty(auditApp.getDoRemarks())) {
            auditAppHistory.setInternalRemarks(auditApp.getDoRemarks());
        }
        if (StringUtil.isNotEmpty(auditApp.getAoRemarks())) {
            auditAppHistory.setInternalRemarks(auditApp.getAoRemarks());
        }

        return auditAppHistory;
    }
}
