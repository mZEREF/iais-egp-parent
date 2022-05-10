package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.InspectionAFCClient;
import sg.gov.moh.iais.egp.bsb.constant.RoleConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.AFCCommonDocDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.AFCSaveDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.CertificationDocDisPlayDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.afc.ReviewAFCReportDto;
import sg.gov.moh.iais.egp.bsb.service.InsAFCReportService;
import sg.gov.moh.iais.egp.bsb.service.InspectionService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.DocConstants.KEY_COMMON_DOC_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.DocConstants.PARAM_REPO_ID_DOC_MAP;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.*;

@Slf4j
@Delegator("insApplicantReportDelegator")
public class InspectionApplicantReportDelegator {
    private static final String PARAM_AFC_REPORT_APP_ID = "afcCertReportAppId";
    private final InspectionAFCClient inspectionAFCClient;
    private final InsAFCReportService insAFCReportService;
    private final InspectionService inspectionService;

    public InspectionApplicantReportDelegator(InspectionAFCClient inspectionAFCClient, InsAFCReportService insAFCReportService, InspectionService inspectionService) {
        this.inspectionAFCClient = inspectionAFCClient;
        this.insAFCReportService = insAFCReportService;
        this.inspectionService = inspectionService;
    }
    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_APP_ID);
        session.removeAttribute(KEY_REVIEW_AFC_REPORT_DTO);
        session.removeAttribute(KEY_COMMON_DOC_DTO);
        session.removeAttribute(PARAM_REPO_ID_DOC_MAP);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INSPECTION, "Applicant Certification upload report");
    }
    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ReviewAFCReportDto dto = insAFCReportService.getDisplayDto(request);
        AFCCommonDocDto commonDocDto = insAFCReportService.getAFCCommonDocDto(request);
        if (!StringUtils.hasLength(dto.getAppId())) {
            String maskedAppId = request.getParameter(KEY_APP_ID);
            String applicationId = MaskUtil.unMaskValue(PARAM_AFC_REPORT_APP_ID, maskedAppId);
            if (maskedAppId == null || applicationId == null || maskedAppId.equals(applicationId)) {
                throw new IaisRuntimeException("Invalid Application ID");
            }
            ResponseDto<ReviewAFCReportDto> responseDto = inspectionAFCClient.getReviewAFCReportDto(applicationId);
            if (responseDto.ok()) {
                dto = responseDto.getEntity();
            } else {
                log.warn("get AFC API doesn't return ok, the response is {}", responseDto);
                ParamUtil.setSessionAttr(request, KEY_REVIEW_AFC_REPORT_DTO, new ReviewAFCReportDto());
            }
        }
        List<CertificationDocDisPlayDto> certificationDocDisPlayDtos = dto.getCertificationDocDisPlayDtos();
        if(certificationDocDisPlayDtos==null){
            certificationDocDisPlayDtos = new ArrayList<>(0);
        }
        for (int i=0; i<certificationDocDisPlayDtos.size();i++){
            certificationDocDisPlayDtos.get(i).setMaskedRepoId(MaskUtil.maskValue("file",certificationDocDisPlayDtos.get(i).getRepoId()));
        }
        dto.setCertificationDocDisPlayDtos(certificationDocDisPlayDtos);


        insAFCReportService.setSavedDocMap(dto, request);
        ParamUtil.setSessionAttr(request, KEY_COMMON_DOC_DTO, commonDocDto);
        ParamUtil.setRequestAttr(request, KEY_DASHBOARD_MSG, KEY_AFC_DASHBOARD_MSG);
        ParamUtil.setRequestAttr(request, PARAM_CAN_ACTION_ROLE, RoleConstants.ROLE_APPLICANT);
        ParamUtil.setSessionAttr(request, KEY_REVIEW_AFC_REPORT_DTO, dto);
    }
    public void handleSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ReviewAFCReportDto dto = insAFCReportService.getDisplayDto(request);
        AFCCommonDocDto commonDocDto = insAFCReportService.getAFCCommonDocDto(request);
        commonDocDto.reqObjMapping(request);
        String actionValue = ParamUtil.getString(request, ModuleCommonConstants.KEY_ACTION_VALUE);
        String actionType;
        if (actionValue.equals("uploadDoc")) {
            actionType = InspectionConstants.PARAM_PREPARE;
        } else if (actionValue.equals("submitDoc")) {
            actionType = insAFCReportService.validateApplicant(request);
        } else {
            actionType = InspectionConstants.PARAM_PREPARE;
        }
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.KEY_ACTION_TYPE, actionType);
        ParamUtil.setSessionAttr(request, KEY_COMMON_DOC_DTO, commonDocDto);
        ParamUtil.setSessionAttr(request, KEY_REVIEW_AFC_REPORT_DTO, dto);
    }


    public void submitFormData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        //do doc sync
        AFCCommonDocDto docDto = insAFCReportService.getAFCCommonDocDto(request);
        AFCSaveDto afcSaveDto = new AFCSaveDto();
        ReviewAFCReportDto displayDto = insAFCReportService.getDisplayDto(request);
        afcSaveDto.setAppId(displayDto.getAppId());
        //set doc
        List<NewFileSyncDto> newFilesToSync = insAFCReportService.saveNewUploadedDoc(docDto);
        //judge is upload new or action on previous uploaded
        boolean uploadNewDoc = false;
        if (!CollectionUtils.isEmpty(docDto.getSavedDocMap())) {
            uploadNewDoc = true;
            afcSaveDto.setNewCertDocDtoList(new ArrayList<>(docDto.getSavedDocMap().values()));
        }
        afcSaveDto.setUploadNewDoc(uploadNewDoc);
        if (!CollectionUtils.isEmpty(displayDto.getCertificationDocDisPlayDtos())){
            afcSaveDto.setSavedCertDocDtoList(displayDto.getCertificationDocDisPlayDtos());
        }
        ResponseDto<String> responseDto = inspectionAFCClient.saveApplicantCertificationData(afcSaveDto);
        if (responseDto.ok()) {
            ParamUtil.setRequestAttr(request,KEY_ACK_MSG,"You have successfully submitted the data");
        } else {
            ParamUtil.setRequestAttr(request,KEY_ACK_MSG,"Failed to submit the data");
        }
        try {
            // sync docs
            inspectionService.syncNewDocsAndDeleteFiles(newFilesToSync, null);
        } catch (Exception e) {
            log.error("Fail to sync files to BE", e);
        }
    }

}
