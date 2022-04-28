package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.RoleConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.*;
import sg.gov.moh.iais.egp.bsb.dto.inspection.insfollowup.FollowUpSaveDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.insfollowup.FollowUpViewDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.InspectionService;
import sg.gov.moh.iais.egp.bsb.service.ProcessHistoryService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALIDATION_ERRORS;

@Slf4j
@Delegator("followUpItemsDelegator")
public class InspectionFollowUpItemsDelegator {
    private static final String PARAM_FOLLOW_UP_APP_ID = "followUpAppId";
    private static final String PARAM_FOLLOW_UP_APP_NO = "followUpAppNo";
    private static final String PARAM_PREPARE = "prepare";
    private static final String PARAM_NEXT = "next";

    private final InspectionClient inspectionClient;
    private final InspectionService inspectionService;
    private final ProcessHistoryService processHistoryService;

    @Autowired
    public InspectionFollowUpItemsDelegator(InspectionClient inspectionClient, InspectionService inspectionService, ProcessHistoryService processHistoryService) {
        this.inspectionClient = inspectionClient;
        this.inspectionService = inspectionService;
        this.processHistoryService = processHistoryService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_APP_ID);
        session.removeAttribute(KEY_FOLLOW_UP_VIEW_DTO);
        session.removeAttribute(KEY_RECTIFY_SAVED_DOC_DTO);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INSPECTION, "Applicant send follow-up items");
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FollowUpViewDto dto = getDisplayDto(request);
        if (!StringUtils.hasLength(dto.getAppId())) {
            String maskedAppId = request.getParameter(PARAM_FOLLOW_UP_APP_ID);
            String maskedAppNo = request.getParameter(PARAM_FOLLOW_UP_APP_NO);
            String applicationId = MaskUtil.unMaskValue(PARAM_FOLLOW_UP_APP_ID, maskedAppId);
            String applicationNo = MaskUtil.unMaskValue(PARAM_FOLLOW_UP_APP_NO, maskedAppNo);
            if (maskedAppId == null || applicationId == null || maskedAppId.equals(applicationId)) {
                throw new IaisRuntimeException("Invalid Application ID");
            }
            if (maskedAppNo == null || applicationNo == null || maskedAppNo.equals(applicationNo)) {
                throw new IaisRuntimeException("Invalid Application NO.");
            }
            //search inspection follow-up items finding list
            ResponseDto<FollowUpViewDto> responseDto = inspectionClient.getFollowUpShowDtoByAppId(applicationId);
            if (responseDto.ok()) {
                dto = responseDto.getEntity();
                //data->session
                ParamUtil.setSessionAttr(request, KEY_FOLLOW_UP_VIEW_DTO, dto);
                //show routingHistory list
                processHistoryService.getAndSetHistoryInRequest(applicationNo, new ArrayList<>(RoleConstants.MOH_OFFICER), request);
            } else {
                log.warn("get withdrawn API doesn't return ok, the response is {}", responseDto);
                ParamUtil.setSessionAttr(request, KEY_FOLLOW_UP_VIEW_DTO, new RectifyFindingFormDto());
            }
        }
    }

    public void validate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FollowUpViewDto dto = getDisplayDto(request);
        bindParam(request, dto);
        RectifyInsReportDto docDto = inspectionService.getRectifyNcsSavedDocDto(request);
        docDto.reqObjMapping(request,DocConstants.DOC_TYPE_FOLLOW_UP,null);
        List<DocMeta> docMetas = docDto.convertToDocMetaList();
        dto.setDocMetas(docMetas);

        String actionType;
        ValidationResultDto validationResultDto = inspectionClient.validateFollowUpItems(dto);
        if (!validationResultDto.isPass()){
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            actionType = PARAM_PREPARE;
        }else {
            actionType = PARAM_NEXT;
        }
        ParamUtil.setRequestAttr(request, ModuleCommonConstants.KEY_ACTION_TYPE, actionType);
        ParamUtil.setSessionAttr(request, KEY_FOLLOW_UP_VIEW_DTO, dto);
        ParamUtil.setSessionAttr(request, KEY_RECTIFY_SAVED_DOC_DTO, docDto);
    }

    public void submitFormData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        //do doc sync
        RectifyInsReportDto docDto = inspectionService.getRectifyNcsSavedDocDto(request);
        FollowUpSaveDto followUpSaveDto = new FollowUpSaveDto();
        FollowUpViewDto displayDto = getDisplayDto(request);
        followUpSaveDto.setRemarks(displayDto.getRemarks());
        followUpSaveDto.setRequestExtension(displayDto.getRequestExtension());
        followUpSaveDto.setAppId(displayDto.getAppId());
        followUpSaveDto.setReasonForExtension(displayDto.getReasonForExtension());
        //set doc
        List<NewFileSyncDto> newFilesToSync = inspectionService.saveNewUploadedDoc(docDto);
        if (!docDto.getSavedDocMap().isEmpty()) {
            followUpSaveDto.setAttachmentList(new ArrayList<>(docDto.getSavedDocMap().values()));
        }
        ResponseDto<String> responseDto = inspectionClient.saveFollowUpData(followUpSaveDto);
        if (responseDto.ok()) {
            ParamUtil.setRequestAttr(request,KEY_ACK_MSG,"You have successfully submitted the data");
        } else {
            ParamUtil.setRequestAttr(request,KEY_ACK_MSG,"Failed to submit the data");
        }
        try {
            // delete docs
            List<String> toBeDeletedRepoIds = inspectionService.deleteUnwantedDoc(docDto);
            // sync docs
            inspectionService.syncNewDocsAndDeleteFiles(newFilesToSync, toBeDeletedRepoIds);
        } catch (Exception e) {
            log.error("Fail to sync files to BE", e);
        }
    }

    private FollowUpViewDto getDisplayDto(HttpServletRequest request) {
        FollowUpViewDto dto = (FollowUpViewDto) ParamUtil.getSessionAttr(request, KEY_FOLLOW_UP_VIEW_DTO);
        if (dto == null) {
            dto = new FollowUpViewDto();
        }
        return dto;
    }

    private void bindParam(HttpServletRequest request, FollowUpViewDto dto) {
        String requestExtension = ParamUtil.getString(request, "requestExtension");
        String reason = ParamUtil.getString(request, "reasonForExtension");
        String remarks = ParamUtil.getString(request, "remarks");
        dto.setRequestExtension(requestExtension);
        dto.setReasonForExtension(reason);
        dto.setRemarks(remarks);
    }
}
