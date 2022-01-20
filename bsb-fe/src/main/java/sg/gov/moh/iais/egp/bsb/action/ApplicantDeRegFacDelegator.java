package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.DeRegOrCancellationClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.deregorcancellation.DeRegistrationFacilityDto;
import sg.gov.moh.iais.egp.bsb.dto.file.*;
import sg.gov.moh.iais.egp.bsb.service.DeRegOrCancellationService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.CessationAndDeRegConstants.*;


/**
 * @author : LiRan
 * @date : 2022/1/10
 */
@Slf4j
@Delegator("applicantDeRegFacDelegator")
public class ApplicantDeRegFacDelegator {
    private final DeRegOrCancellationClient deRegOrCancellationClient;
    private final DeRegOrCancellationService deRegOrCancellationService;

    @Autowired
    public ApplicantDeRegFacDelegator(DeRegOrCancellationClient deRegOrCancellationClient, DeRegOrCancellationService deRegOrCancellationService) {
        this.deRegOrCancellationClient = deRegOrCancellationClient;
        this.deRegOrCancellationService = deRegOrCancellationService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_PROCESS_TYPE);
        request.getSession().removeAttribute(KEY_DE_REGISTRATION_FACILITY_DTO);
        request.getSession().removeAttribute(DocConstants.KEY_COMMON_DOC_DTO);
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME_DE_REGISTRATION_FACILITY);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String maskApprovalId = request.getParameter(KEY_APPROVAL_ID);
        String maskEditId = request.getParameter(KEY_EDIT_ID);
        CommonDocDto commonDocDto = deRegOrCancellationService.getCommonDocDoc(request);
        if (StringUtils.hasLength(maskApprovalId)){
            String approvalId = MaskUtil.unMaskValue(KEY_APPROVAL_ID, maskApprovalId);
            if (approvalId != null && !maskApprovalId.equals(approvalId)){
                ResponseDto<DeRegistrationFacilityDto> resultDto = deRegOrCancellationClient.getDeRegistrationFacilityData(approvalId);
                if (resultDto.ok()){
                    DeRegistrationFacilityDto deRegistrationFacilityDto = resultDto.getEntity();
                    ParamUtil.setSessionAttr(request, KEY_DE_REGISTRATION_FACILITY_DTO, deRegistrationFacilityDto);
                    ParamUtil.setSessionAttr(request, KEY_PROCESS_TYPE, deRegistrationFacilityDto.getProcessType());
                }
            }
        }else if (StringUtils.hasLength(maskEditId)){
            String applicationId = MaskUtil.unMaskValue(KEY_EDIT_ID, maskEditId);
            if (applicationId != null && !maskEditId.equals(applicationId)){
                ResponseDto<DeRegistrationFacilityDto> resultDto = deRegOrCancellationClient.getDraftDeRegistrationFacilityData(applicationId);
                if (resultDto.ok()){
                    DeRegistrationFacilityDto deRegistrationFacilityDto = resultDto.getEntity();
                    ParamUtil.setSessionAttr(request, KEY_DE_REGISTRATION_FACILITY_DTO, deRegistrationFacilityDto);
                    ParamUtil.setSessionAttr(request, KEY_PROCESS_TYPE, deRegistrationFacilityDto.getProcessType());

                    Collection<DocRecordInfo> docRecordInfoList = deRegistrationFacilityDto.getDocRecordInfos();
                    commonDocDto.setSavedDocMap(sg.gov.moh.iais.egp.bsb.util.CollectionUtils.uniqueIndexMap(docRecordInfoList, DocRecordInfo::getRepoId));
                }
            }
        }
        ParamUtil.setSessionAttr(request, DocConstants.KEY_COMMON_DOC_DTO, commonDocDto);
        deRegOrCancellationService.setDocSettingAndData(request, commonDocDto);
    }

    public void validCommit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        DeRegistrationFacilityDto deRegistrationFacilityDto = (DeRegistrationFacilityDto) ParamUtil.getSessionAttr(request, KEY_DE_REGISTRATION_FACILITY_DTO);
        deRegistrationFacilityDto.reqObjMapping(request);

        CommonDocDto commonDocDto = deRegOrCancellationService.getCommonDocDoc(request);
        commonDocDto.reqObjMapping(request);
        List<DocMeta> docMetaList = commonDocDto.convertToDocMetaList("deRegistration");
        deRegistrationFacilityDto.setDocMetas(docMetaList);
        ParamUtil.setSessionAttr(request, DocConstants.KEY_COMMON_DOC_DTO, commonDocDto);
        ParamUtil.setSessionAttr(request, KEY_DE_REGISTRATION_FACILITY_DTO, deRegistrationFacilityDto);

        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        String action1 = "";

        if (actionType.equals(KEY_ACTION_TYPE_JUMP)){
            ValidationResultDto validationResultDto = deRegOrCancellationClient.validateDeRegistrationFacilityDto(deRegistrationFacilityDto);
            if (!validationResultDto.isPass()){
                ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
                action1 = KEY_ACTION_INIT;
            }else {
                action1 = KEY_ACTION_PREPARE_PREVIEW;
            }
        }else if (actionType.equals(KEY_ACTION_TYPE_DRAFT)){
            //save draft logic
            action1 = KEY_ACTION_INIT;
            deRegOrCancellationService.saveDeRegistrationFacilityDraft(request);
        }
        ParamUtil.setRequestAttr(request, KEY_ACTION_1, action1);
    }

    public void preparePreview(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        CommonDocDto commonDocDto = deRegOrCancellationService.getCommonDocDoc(request);
        deRegOrCancellationService.setDocSettingAndData(request, commonDocDto);
    }

    public void doSaveSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        //save docs
        CommonDocDto commonDocDto = deRegOrCancellationService.getCommonDocDoc(request);
        List<NewFileSyncDto> newFileSyncDtoList = deRegOrCancellationService.saveNewUploadedDoc(commonDocDto);

        //save data
        DeRegistrationFacilityDto deRegistrationFacilityDto = (DeRegistrationFacilityDto) ParamUtil.getSessionAttr(request, KEY_DE_REGISTRATION_FACILITY_DTO);
        deRegistrationFacilityDto.setDocRecordInfos(new ArrayList<>(commonDocDto.getSavedDocMap().values()));
        ResponseDto<String> responseDto = deRegOrCancellationClient.saveDeRegistrationFacilityDto(deRegistrationFacilityDto);
        log.info("save new deRegistrationFacilityDto response: {}", org.apache.commons.lang.StringUtils.normalizeSpace(responseDto.toString()));

        deRegOrCancellationService.deleteAndSyncDocs(commonDocDto, newFileSyncDtoList);

        LocalDate localDate = LocalDate.now();
        ParamUtil.setRequestAttr(request, KEY_CURRENT_DATE, localDate.toString());
    }

    public void doPreview(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        String action2 = "";
        if (actionType.equals(KEY_ACTION_TYPE_JUMP)){
            if (actionValue.equals(KEY_ACTION_VALUE_NEXT)){
                action2 = KEY_ACTION_DO_SAVE_SUBMIT;
            }else if (actionValue.equals(KEY_ACTION_VALUE_BACK)){
                action2 = KEY_ACTION_INIT;
            }
        }else if (actionType.equals(KEY_ACTION_TYPE_DRAFT)){
            action2 = KEY_ACTION_PREPARE_PREVIEW;
            deRegOrCancellationService.saveDeRegistrationFacilityDraft(request);
        }
        ParamUtil.setRequestAttr(request, KEY_ACTION_2, action2);
    }
}
