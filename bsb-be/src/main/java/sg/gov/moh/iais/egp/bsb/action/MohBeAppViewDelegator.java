package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.AppViewClient;
import sg.gov.moh.iais.egp.bsb.constant.ResponseConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.afc.FacilityCertifierRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.approval.ApprovalAppDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.approval.ApprovalProfileDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.facility.BiologicalAgentToxinDto;
import sg.gov.moh.iais.egp.bsb.dto.appview.facility.FacilityRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.ApplicationDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.*;

/**
 * @author : LiRan
 * @date : 2021/12/27
 */
@Delegator
@Slf4j
public class MohBeAppViewDelegator {
    public static final String KEY_APPLICATION_DTO                           = "applicationDto";

    private static final String MODULE_NAME                                  = "MohBeAppView";
    private static final String FUNCTION_NAME                                = "MohBeAppView";

    private static final String NODE_NAME_FAC_PROFILE                        = "facProfile";
    private static final String NODE_NAME_FAC_OPERATOR                       = "facOperator";
    private static final String NODE_NAME_FAC_AUTH                           = "facAuth";
    private static final String NODE_NAME_FAC_ADMIN                          = "facAdmin";
    private static final String NODE_NAME_FAC_OFFICER                        = "facOfficer";
    private static final String NODE_NAME_FAC_COMMITTEE                      = "facCommittee";
    private static final String KEY_BAT_LIST                                 = "batList";

    private static final String KEY_APPROVAL_PROFILE_LIST                    = "approvalProfileList";

    public static final String NODE_NAME_ORG_PROFILE                         = "orgProfile";
    public static final String NODE_NAME_ORG_CERTIFYING_TEAM                 = "orgCerTeam";
    public static final String NODE_NAME_ORG_FAC_ADMINISTRATOR               = "orgAdmin";

    private final AppViewClient appViewClient;

    @Autowired
    public MohBeAppViewDelegator(AppViewClient appViewClient) {
        this.appViewClient = appViewClient;
    }

    public void start(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME);
    }

    public void prepareViewForm(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ApplicationDto applicationDto = (ApplicationDto) ParamUtil.getSessionAttr(request, KEY_APPLICATION_DTO);
        if (applicationDto != null){
            String applicationId = applicationDto.getId();
            String processType = applicationDto.getProcessType();
            switch (processType) {
                case PROCESS_TYPE_FAC_REG:
                    retrieveFacReg(request, applicationId);
                    break;
                case PROCESS_TYPE_APPROVE_POSSESS:
                case PROCESS_TYPE_APPROVE_LSP:
                case PROCESS_TYPE_SP_APPROVE_HANDLE:
                    retrieveApprovalApp(request, applicationId);
                    break;
                case PROCESS_TYPE_FAC_CERTIFIER_REG:
                    retrieveFacCerReg(request, applicationId);
                    break;
                default:
                    log.info("don't have such processType {}", StringUtils.normalizeSpace(processType));
                    break;
            }
        }
    }

    private void retrieveFacReg(HttpServletRequest request, String applicationId) {
        ResponseDto<FacilityRegisterDto> resultDto = appViewClient.getFacRegDtoByAppId(applicationId);
        if (resultDto.ok()){
            FacilityRegisterDto facilityRegisterDto = resultDto.getEntity();

            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_PROFILE, facilityRegisterDto.getFacilityProfileDto());
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_OPERATOR, facilityRegisterDto.getFacilityOperatorDto());
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_AUTH, facilityRegisterDto.getFacilityAuthoriserDto());
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_ADMIN, facilityRegisterDto.getFacilityAdministratorDto());
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_OFFICER, facilityRegisterDto.getFacilityOfficerDto());
            ParamUtil.setRequestAttr(request, NODE_NAME_FAC_COMMITTEE, facilityRegisterDto.getFacilityCommitteeDto());

            List<BiologicalAgentToxinDto> batList = new ArrayList<>(facilityRegisterDto.getBiologicalAgentToxinMap().values());
            ParamUtil.setRequestAttr(request, KEY_BAT_LIST, batList);

            PrimaryDocDto primaryDocDto = new PrimaryDocDto();
            primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(facilityRegisterDto.getDocRecordInfos(), DocRecordInfo::getRepoId));
            Map<String, List<DocRecordInfo>> saveFiles = primaryDocDto.getExistDocTypeMap();
            Set<String> docTypes = saveFiles.keySet();
            ParamUtil.setRequestAttr(request, "docTypes", docTypes);
            ParamUtil.setRequestAttr(request, "savedFiles", saveFiles);
            ParamUtil.setRequestAttr(request, "primaryDocDto", primaryDocDto);
        } else {
            throw new IaisRuntimeException(ResponseConstants.ERR_MSG_FAIL_RETRIEVAL);
        }
    }

    private void retrieveApprovalApp(HttpServletRequest request, String applicationId) {
        ResponseDto<ApprovalAppDto> resultDto = appViewClient.getApprovalAppDtoByAppId(applicationId);
        if (resultDto.ok()){
            ApprovalAppDto approvalAppDto = resultDto.getEntity();
            List<ApprovalProfileDto> approvalProfileDtoList = new ArrayList<>(approvalAppDto.getApprovalProfileMap().values());
            ParamUtil.setRequestAttr(request, KEY_APPROVAL_PROFILE_LIST, approvalProfileDtoList);
        } else {
            throw new IaisRuntimeException(ResponseConstants.ERR_MSG_FAIL_RETRIEVAL);
        }
    }

    private void retrieveFacCerReg(HttpServletRequest request, String applicationId) {
        ResponseDto<FacilityCertifierRegisterDto> resultDto = appViewClient.getFacCerRegDtoByAppId(applicationId);
        if (resultDto.ok()){
            FacilityCertifierRegisterDto facilityCertifierRegisterDto = resultDto.getEntity();
            ParamUtil.setRequestAttr(request, NODE_NAME_ORG_PROFILE, facilityCertifierRegisterDto.getProfileDto());
            ParamUtil.setRequestAttr(request, NODE_NAME_ORG_CERTIFYING_TEAM, facilityCertifierRegisterDto.getCertifyingTeamDto());
            ParamUtil.setRequestAttr(request, NODE_NAME_ORG_FAC_ADMINISTRATOR, facilityCertifierRegisterDto.getAdministratorDto());
        } else {
            throw new IaisRuntimeException(ResponseConstants.ERR_MSG_FAIL_RETRIEVAL);
        }
    }
}
