package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.ApprovalBatAndActivityClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalBatAndActivityDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.ApprovalToLargeDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.FacProfileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.approval.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.register.bat.BiologicalAgentToxinDto;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sg.gov.moh.iais.egp.bsb.service.DocSettingService;
import sg.gov.moh.iais.egp.bsb.service.ViewAppService;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_APPROVAL_APPLICATION;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_VIEW_APPLICATION;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_BAT_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_FAC_AUTHORISED_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_FAC_PROFILE_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_OTHER_DOC_TYPES;
import static sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants.KEY_PROCESS_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ViewApplicationConstants.KEY_APPROVAL_BAT_AND_ACTIVITY_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ViewApplicationConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.ViewApplicationConstants.KEY_MASKED_EDIT_APP_ID;

@Delegator(value = "viewApprovalBatAndActivityDelegator")
@Slf4j
public class ViewApprovalBatAndActivityDelegator {
    private final ApprovalBatAndActivityClient approvalBatAndActivityClient;
    private final DocSettingService docSettingService;

    public ViewApprovalBatAndActivityDelegator(ApprovalBatAndActivityClient approvalBatAndActivityClient, DocSettingService docSettingService) {
        this.approvalBatAndActivityClient = approvalBatAndActivityClient;
        this.docSettingService = docSettingService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_APP_ID);
        session.removeAttribute(KEY_MASKED_EDIT_APP_ID);
        session.removeAttribute(KEY_APPROVAL_BAT_AND_ACTIVITY_DTO);
        AuditTrailHelper.auditFunction(MODULE_VIEW_APPLICATION, FUNCTION_APPROVAL_APPLICATION);
    }

    public void init(BaseProcessClass bpc) {
        ViewAppService.init(bpc);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        ApprovalBatAndActivityDto approvalBatAndActivityDto = getApprovalBatAndActivityDto(request, appId);

        FacProfileDto facProfileDto = approvalBatAndActivityDto.getFacProfileDto();
        ParamUtil.setRequestAttr(request, KEY_FAC_PROFILE_DTO, facProfileDto);

        String processType = approvalBatAndActivityDto.getApprovalSelectionDto().getProcessType();
        ParamUtil.setRequestAttr(request, KEY_PROCESS_TYPE, processType);

        switch (processType) {
            case MasterCodeConstants.PROCESS_TYPE_APPROVE_POSSESS:
                BiologicalAgentToxinDto dto = approvalBatAndActivityDto.getApprovalToPossessDto();
                ParamUtil.setRequestAttr(request, KEY_BAT_INFO, dto);
                break;
            case MasterCodeConstants.PROCESS_TYPE_APPROVE_LSP:
                ApprovalToLargeDto lspDto = approvalBatAndActivityDto.getApprovalToLargeDto();
                ParamUtil.setRequestAttr(request, KEY_BAT_INFO, lspDto);
                break;
            case MasterCodeConstants.PROCESS_TYPE_SP_APPROVE_HANDLE:
                ParamUtil.setRequestAttr(request, KEY_BAT_INFO, approvalBatAndActivityDto.getApprovalToSpecialDto());
                ParamUtil.setRequestAttr(request, KEY_FAC_AUTHORISED_DTO, approvalBatAndActivityDto.getFacAuthorisedDto());
                break;
            case MasterCodeConstants.PROCESS_TYPE_APPROVAL_FOR_FACILITY_ACTIVITY_TYPE:
                ParamUtil.setRequestAttr(request, KEY_BAT_INFO, approvalBatAndActivityDto.getApprovalToActivityDto());
                break;
            default:
                log.info("no such processType {}", StringUtils.normalizeSpace(processType));
                break;
        }
        List<DocSetting> approvalAppDocSettings = docSettingService.getApprovalAppDocSettings(processType);
        ParamUtil.setRequestAttr(request, "docSettings", approvalAppDocSettings);
        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(approvalBatAndActivityDto.getDocRecordInfos(), DocRecordInfo::getRepoId));
        Map<String, List<DocRecordInfo>> savedFiles = primaryDocDto.getExistDocTypeMap();
        ParamUtil.setRequestAttr(request, "savedFiles", savedFiles);

        Set<String> otherDocTypes = DocSettingService.computeOtherDocTypes(approvalAppDocSettings, savedFiles.keySet());
        ParamUtil.setRequestAttr(request, KEY_OTHER_DOC_TYPES, otherDocTypes);
    }

    public ApprovalBatAndActivityDto getApprovalBatAndActivityDto(HttpServletRequest request, String appId) {
        ApprovalBatAndActivityDto approvalBatAndActivityDto = (ApprovalBatAndActivityDto) ParamUtil.getSessionAttr(request, KEY_APPROVAL_BAT_AND_ACTIVITY_DTO);
        if (approvalBatAndActivityDto == null) {
            ResponseDto<ApprovalBatAndActivityDto> resultDto = approvalBatAndActivityClient.getApprovalAppAppDataByApplicationId(appId);
            if (!resultDto.ok()) {
                throw new IaisRuntimeException("Fail to retrieve app data");
            }
            approvalBatAndActivityDto = resultDto.getEntity();
            ParamUtil.setSessionAttr(request, KEY_APPROVAL_BAT_AND_ACTIVITY_DTO, approvalBatAndActivityDto);
        }
        return approvalBatAndActivityDto;
    }
}
