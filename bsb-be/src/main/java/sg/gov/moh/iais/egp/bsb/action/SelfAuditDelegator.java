package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppIntranetDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sg.gov.moh.iais.egp.bsb.client.AuditClient;
import sg.gov.moh.iais.egp.bsb.client.DocClient;
import sg.gov.moh.iais.egp.bsb.client.FileRepoClient;
import sg.gov.moh.iais.egp.bsb.constant.AuditConstants;
import sg.gov.moh.iais.egp.bsb.constant.RevocationConstants;
import sg.gov.moh.iais.egp.bsb.dto.audit.AuditDocDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.AODecisionDto;
import sg.gov.moh.iais.egp.bsb.entity.*;
import sg.gov.moh.iais.egp.bsb.util.JoinAddress;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Zhu Tangtang
 */
@Slf4j
@Delegator(value = "selfAuditDelegator")
public class SelfAuditDelegator {

    private static final String KEY_AUDIT_PAGE_INFO = "pageInfo";
    private static final String KEY_AUDIT_DATA_LIST = "dataList";
    private static final String KEY_ACTION_VALUE = "action_value";
    private static final String KEY_ACTION_ADDT = "action_additional";

    private static final String KEY_PAGE_SIZE = "pageJumpNoPageSize";
    private static final String KEY_PAGE_NO = "pageJumpNoTextchangePage";

    private static final String FACILITY = "facility";
    private static final String FACILITY_AUDIT_LIST = "facilityAuditList";
    private static final String FACILITY_AUDIT = "facilityAudit";
    private static final String FACILITY_AUDIT_APP = "facilityAuditAPP";
    private static final String AUDIT_ID = "auditId";
    private static final String LAST_AUDIT_DATE = "lastAuditDt";
    private static final String AUDIT_DOC_DTO = "auditDocDto";
    private static final String AUDIT_OUTCOME = "auditOutcome";
    private static final String FINAL_REMARK = "finalRemark";

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
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_SYSTEM_CONFIG, AuditTrailConsts.FUNCTION_ERROR_MESSAGES_MANAGEMENT);
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
        ParamUtil.setSessionAttr(request,FACILITY,null);
        ParamUtil.setSessionAttr(request,AUDIT_DOC_DTO, null);

        String auditId = ParamUtil.getMaskedString(request, AUDIT_ID);
        FacilityAudit facilityAudit = auditClient.getFacilityAuditById(auditId).getEntity();

        Facility facility = facilityAudit.getFacility();
        Application application = new Application();
        application.setFacility(facility);
        String facilityAddress = JoinAddress.joinAddress(application);
        facility.setFacilityAddress(facilityAddress);

        ParamUtil.setSessionAttr(request,FACILITY,facility);
        ParamUtil.setRequestAttr(request, FACILITY_AUDIT, facilityAudit);
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
        String auditId = ParamUtil.getMaskedString(request, AUDIT_ID);
        String scenarioCategory = ParamUtil.getRequestString(request,"scenarioCategory");
        FacilityAudit audit = new FacilityAudit();
        audit.setScenarioCategory(scenarioCategory);
        audit.setId(auditId);
        audit.setStatus("AUDITST004");
        auditClient.updateAudit(audit);
    }

    /**
     * AutoStep: prepareData
     *
     * @param bpc
     */
    public void prepareDOProcessSelfAuditData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,FACILITY,null);
        ParamUtil.setSessionAttr(request,AUDIT_DOC_DTO, null);

        String auditId = "AE21FDD6-810C-EC11-BE6E-000C298D317C";
        FacilityAudit facilityAudit = auditClient.getFacilityAuditById(auditId).getEntity();

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

        ParamUtil.setSessionAttr(request,FACILITY,facility);
        ParamUtil.setRequestAttr(request, FACILITY_AUDIT, facilityAudit);
        ParamUtil.setSessionAttr(request,AUDIT_DOC_DTO, auditDocDto);
    }

    /**
     * audit status change to AUDITST006
     * audit app status change to AUDITST005
     * @param bpc
     */
    public void DOVerified(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String auditId = ParamUtil.getMaskedString(request,AUDIT_ID);
        String auditOutCome = ParamUtil.getRequestString(request,AUDIT_OUTCOME);
        String remark = ParamUtil.getRequestString(request,AuditConstants.PARAM_REMARKS);
        String finalRemark = ParamUtil.getRequestString(request,FINAL_REMARK);//on or null
        String decision = ParamUtil.getRequestString(request,AuditConstants.PARAM_DECISION);

        FacilityAudit facilityAudit = auditClient.getFacilityAuditById(auditId).getEntity();
        if (StringUtil.isNotEmpty(auditOutCome)){
            facilityAudit.setAuditOutcome(auditOutCome);
        }
        if (finalRemark.equals("on")){
            facilityAudit.setFinalRemarks("Yes");
        }else{
            facilityAudit.setFinalRemarks("No");
        }
        facilityAudit.setStatus("AUDITST006");
        auditClient.updateAudit(facilityAudit);

        FacilityAudit audit = new FacilityAudit();
        audit.setId(facilityAudit.getId());

        FacilityAuditApp auditApp = new FacilityAuditApp();
        auditApp.setAuditType(facilityAudit.getAuditType());
        auditApp.setFacilityAudit(audit);
        auditApp.setDoRemarks(remark);
        auditApp.setDoDecision(decision);
        auditApp.setStatus("AUDITST005");


    }

    /**
     * audit status change to AUDITST006
     * audit app status change to AUDITST005
     * @param bpc
     */
    public void DORequestForInformation(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
    }

    /**
     * audit status change to AUDITST006
     * audit app status change to AUDITST005
     * @param bpc
     */
    public void DOReject(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
    }
}
