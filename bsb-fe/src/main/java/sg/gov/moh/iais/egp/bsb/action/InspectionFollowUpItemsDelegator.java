package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.client.RfiClient;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;
import sg.gov.moh.iais.egp.bsb.dto.file.NewFileSyncDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.RectifyInsReportDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.insfollowup.FollowUpInitDataDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.insfollowup.FollowUpViewDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.DocSettingService;
import sg.gov.moh.iais.egp.bsb.service.InspectionService;
import sg.gov.moh.iais.egp.bsb.service.RfiService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sg.gov.moh.iais.egp.bsb.util.mastercode.MasterCodeHolder;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_APPLICANT_SEND_FOLLOW_UP_ITEMS;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INSPECTION;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_DOC_TYPES_JSON;
import static sg.gov.moh.iais.egp.bsb.constant.FacRegisterConstants.KEY_OPTIONS_DOC_TYPES;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_ACK_MSG;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_FOLLOW_UP_VIEW_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_NEW_SAVED_DOCUMENT;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_RECTIFY_SAVED_DOC_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_REMARK_HISTORY_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_SAVED_DOCUMENT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_INDEED_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_IND_AFTER_SAVE_AS_DRAFT;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALIDATION_ERRORS;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_CONFIRM_RFI;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_CONFIRM_RFI_Y;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_RFT_DATA_ID;

@Slf4j
@Delegator("followUpItemsDelegator")
@RequiredArgsConstructor
public class InspectionFollowUpItemsDelegator {
    private static final String PARAM_FOLLOW_UP_APP_ID = "followUpAppId";
    private static final String ACTION_INVALID = "invalid";
    private static final String ACTION_SAVE = "save";
    private static final String ACTION_SAVE_DRAFT = "saveDraft";

    private final InspectionClient inspectionClient;
    private final RfiService rfiService;
    private final RfiClient rfiClient;

    private final InspectionService inspectionService;
    private final DocSettingService docSettingService;


    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_APP_ID);
        session.removeAttribute(KEY_FOLLOW_UP_VIEW_DTO);
        session.removeAttribute(KEY_RECTIFY_SAVED_DOC_DTO);

        String appId;
        String maskedAppId = ParamUtil.getString(request, KEY_APP_ID);
        if (maskedAppId != null) {
            appId = MaskHelper.unmask(PARAM_FOLLOW_UP_APP_ID, maskedAppId);
            ParamUtil.setSessionAttr(request, KEY_APP_ID, appId);
        }
        // if rfi module
        rfiService.clearAndSetAppIdInSession(request);
        appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);

        AuditTrailHelper.auditFunction(MODULE_INSPECTION, FUNCTION_APPLICANT_SEND_FOLLOW_UP_ITEMS);


        // initialize data
        FollowUpInitDataDto initDataDto = inspectionClient.getFollowUpShowDtoByAppId(appId);
        FollowUpViewDto viewDto = initDataDto.getFollowUpViewDto();
        ParamUtil.setSessionAttr(request, KEY_FOLLOW_UP_VIEW_DTO, viewDto);
        ParamUtil.setRequestAttr(request, KEY_REMARK_HISTORY_LIST, viewDto.getProcessHistoryDtoList());

        RectifyInsReportDto docDto = initDataDto.getDocDto();
        ParamUtil.setSessionAttr(request, KEY_RECTIFY_SAVED_DOC_DTO, docDto);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        RectifyInsReportDto docDto = inspectionService.getRectifyNcsSavedDocDto(request);
        ParamUtil.setRequestAttr(request,KEY_NEW_SAVED_DOCUMENT, docDto.getNewDocMap().values());
        ParamUtil.setRequestAttr(request,KEY_SAVED_DOCUMENT, docDto.getSavedDocMap().values());

        List<SelectOption> docTypeOps = MasterCodeHolder.DOCUMENT_TYPE.customOptions(docSettingService.getRFAndCFAndUCFAndBMFAndAFCDocTypeSet().toArray(new String[0]));
        ParamUtil.setRequestAttr(request, KEY_OPTIONS_DOC_TYPES, docTypeOps);
        String docTypeOpsJson = JsonUtil.parseToJson(docTypeOps);
        ParamUtil.setRequestAttr(request, KEY_DOC_TYPES_JSON, docTypeOpsJson);
    }

    public void validate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        if (ACTION_SAVE.equals(actionType) || ACTION_SAVE_DRAFT.equals(actionType)) {
            // read DTO, DOCs
            FollowUpViewDto dto = getDisplayDto(request);
            bindParam(request, dto);
            RectifyInsReportDto docDto = inspectionService.getRectifyNcsSavedDocDto(request);
            docDto.reqObjMapping(request,null);
            List<DocMeta> docMetas = docDto.convertToDocMetaList();
            dto.setDocMetas(docMetas);

            ParamUtil.setSessionAttr(request, KEY_FOLLOW_UP_VIEW_DTO, dto);
            ParamUtil.setSessionAttr(request, KEY_RECTIFY_SAVED_DOC_DTO, docDto);

            // validation
            ValidationResultDto validationResultDto = inspectionClient.validateFollowUpItems(dto);
            if (validationResultDto.isPass()) {
                ParamUtil.setRequestAttr(request, KEY_INDEED_ACTION_TYPE, actionType);
            } else {
                ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
                ParamUtil.setRequestAttr(request, KEY_INDEED_ACTION_TYPE, ACTION_INVALID);
            }
        } else {
            ParamUtil.setRequestAttr(request, KEY_INDEED_ACTION_TYPE, ACTION_INVALID);
        }
    }


    public void saveDraft(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        FollowUpViewDto viewDto = getDisplayDto(request);
        RectifyInsReportDto docDto = inspectionService.getRectifyNcsSavedDocDto(request);

        FollowUpInitDataDto initDataDto = new FollowUpInitDataDto();
        initDataDto.setFollowUpViewDto(viewDto);
        initDataDto.setDocDto(docDto);
        inspectionClient.saveFollowUpDataDraft(initDataDto);
        ParamUtil.setRequestAttr(request, KEY_IND_AFTER_SAVE_AS_DRAFT, Boolean.TRUE);
    }


    public void submitFormData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        FollowUpViewDto viewDto = getDisplayDto(request);
        // clear useless data after submission
        viewDto.setProcessHistoryDtoList(null);
        viewDto.setDocMetas(null);

        RectifyInsReportDto docDto = inspectionService.getRectifyNcsSavedDocDto(request);
        // save new uploaded doc
        List<NewFileSyncDto> newFilesToSync = inspectionService.saveNewUploadedDoc(docDto);

        FollowUpInitDataDto initDataDto = new FollowUpInitDataDto();
        initDataDto.setFollowUpViewDto(viewDto);
        initDataDto.setDocDto(docDto);


        //judge is rfi
        ResponseDto<String> responseDto;
        String confirmRfi = (String) ParamUtil.getSessionAttr(request, KEY_CONFIRM_RFI);
        if (confirmRfi != null && confirmRfi.equals(KEY_CONFIRM_RFI_Y)) {
            String rfiDataId = (String) ParamUtil.getSessionAttr(request, KEY_RFT_DATA_ID);
            responseDto = rfiClient.saveInspectionFollowUp(initDataDto, rfiDataId);
        } else {
            responseDto = inspectionClient.saveFollowUpData(initDataDto);
        }

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
