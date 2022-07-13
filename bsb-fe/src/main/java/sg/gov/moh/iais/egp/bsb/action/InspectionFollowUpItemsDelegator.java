package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.*;
import sg.gov.moh.iais.egp.bsb.dto.inspection.insfollowup.FollowUpSaveDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.insfollowup.FollowUpViewDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.InspectionService;
import sg.gov.moh.iais.egp.bsb.service.RfiService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_APPLICANT_SEND_FOLLOW_UP_ITEMS;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INSPECTION;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_ACK_MSG;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_FOLLOW_UP_VIEW_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_RECTIFY_SAVED_DOC_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALIDATION_ERRORS;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_CONFIRM_RFI;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_CONFIRM_RFI_Y;

@Slf4j
@Delegator("followUpItemsDelegator")
public class InspectionFollowUpItemsDelegator {
    private static final String PARAM_FOLLOW_UP_APP_ID = "followUpAppId";
    private static final String PARAM_PREPARE = "prepare";
    private static final String PARAM_NEXT = "next";

    private final InspectionClient inspectionClient;
    private final InspectionService inspectionService;
    private final RfiService rfiService;

    @Autowired
    public InspectionFollowUpItemsDelegator(InspectionClient inspectionClient, InspectionService inspectionService, RfiService rfiService) {
        this.inspectionClient = inspectionClient;
        this.inspectionService = inspectionService;
        this.rfiService = rfiService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_APP_ID);
        session.removeAttribute(KEY_FOLLOW_UP_VIEW_DTO);
        session.removeAttribute(KEY_RECTIFY_SAVED_DOC_DTO);

        String maskedAppId = ParamUtil.getString(request, KEY_APP_ID);
        if (maskedAppId != null) {
            String appId = MaskUtil.unMaskValue(PARAM_FOLLOW_UP_APP_ID, maskedAppId);
            if (appId == null || appId.equals(maskedAppId)) {
                throw new IllegalArgumentException("Invalid masked app ID:" + LogUtil.escapeCrlf(maskedAppId));
            }
            ParamUtil.setSessionAttr(request, KEY_APP_ID, appId);
        }

        // if rfi module
        rfiService.clearAndSetAppIdInSession(request);
        AuditTrailHelper.auditFunction(MODULE_INSPECTION, FUNCTION_APPLICANT_SEND_FOLLOW_UP_ITEMS);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        //get application id
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        FollowUpViewDto dto = (FollowUpViewDto) ParamUtil.getSessionAttr(request, KEY_FOLLOW_UP_VIEW_DTO);
        if (dto == null) {
            //search inspection follow-up items finding list
            ResponseDto<FollowUpViewDto> responseDto = inspectionClient.getFollowUpShowDtoByAppId(appId);
            if (responseDto.ok()) {
                dto = responseDto.getEntity();
            } else {
                log.warn("get followUpViewDto API doesn't return ok, the response is {}", responseDto);
                dto = new FollowUpViewDto();
            }
        }
        ParamUtil.setSessionAttr(request, KEY_FOLLOW_UP_VIEW_DTO, dto);
        ParamUtil.setRequestAttr(request, KEY_ROUTING_HISTORY_LIST, dto.getProcessHistoryDtoList());
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
        ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, actionType);
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
        //judge is rfi
        ResponseDto<String> responseDto;
        String confirmRfi = (String) ParamUtil.getSessionAttr(request, KEY_CONFIRM_RFI);
        if (confirmRfi != null && confirmRfi.equals(KEY_CONFIRM_RFI_Y)) {
            //save rfi data
            responseDto = rfiService.saveInspectionFollowUp(request, followUpSaveDto);
        } else {
            responseDto = inspectionClient.saveFollowUpData(followUpSaveDto);
        }

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
