package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.ProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.file.PrimaryDocDto;
import sg.gov.moh.iais.egp.bsb.dto.process.MohProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.process.SubmitDetailsDto;
import sg.gov.moh.iais.egp.bsb.dto.process.approval.ApprovalProfileDto;
import sg.gov.moh.iais.egp.bsb.dto.process.facility.BiologicalAgentToxinDto;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.util.*;

import static sg.gov.moh.iais.egp.bsb.constant.ProcessContants.*;

/**
 * @author : LiRan
 * @date : 2021/11/22
 */
@Delegator(value = "doScreeningDelegator")
@Slf4j
public class MohDOScreeningDelegator {
    private static final String FUNCTION_NAME = "DO Screening";
    private static final String PROCESS_FLOW = "DOScreening";

    private final ProcessClient processClient;

    @Autowired
    public MohDOScreeningDelegator(ProcessClient processClient) {
        this.processClient = processClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_SUBMIT_DETAILS_DTO);
        request.getSession().removeAttribute(KEY_MOH_PROCESS_DTO);
        request.getSession().removeAttribute(KEY_BAT_LIST);
        request.getSession().removeAttribute(KEY_APPROVAL_PROFILE_LIST);
        request.getSession().removeAttribute(LAST_DO_APPLICATION_MISC);
        request.getSession().removeAttribute(LAST_AO_APPLICATION_MISC);
        request.getSession().removeAttribute(LAST_HM_APPLICATION_MISC);
        request.getSession().removeAttribute("primaryDocDto");
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String maskedAppId = request.getParameter(KEY_APP_ID);
        String maskedTaskId = request.getParameter(KEY_TASK_ID);
        if (StringUtils.hasLength(maskedAppId) && StringUtils.hasLength(maskedTaskId)){
            if (log.isInfoEnabled()) {
                log.info("masked app ID: {}", org.apache.commons.lang.StringUtils.normalizeSpace(maskedAppId));
                log.info("masked task ID: {}", org.apache.commons.lang.StringUtils.normalizeSpace(maskedTaskId));
            }
            boolean failLoadSubmitDetailsData = true;
            String appId = MaskUtil.unMaskValue(KEY_ID, maskedAppId);
            String taskId = MaskUtil.unMaskValue(KEY_ID, maskedTaskId);
            if (appId != null && taskId != null){
                ResponseDto<SubmitDetailsDto> submitDetailsDtoResponseDto = processClient.getSubmitDetailsByAppId(appId);
                if (submitDetailsDtoResponseDto.ok()){
                    failLoadSubmitDetailsData = false;
                    SubmitDetailsDto submitDetailsDto = submitDetailsDtoResponseDto.getEntity();
                    ParamUtil.setSessionAttr(request, KEY_SUBMIT_DETAILS_DTO, submitDetailsDto);
                    setBatList(request, submitDetailsDto);

                    MohProcessDto mohProcessDto = new MohProcessDto();
                    mohProcessDto.setProcessFlow(PROCESS_FLOW);
                    mohProcessDto.setProcessType(submitDetailsDto.getProcessType());
                    mohProcessDto.setAppId(appId);
                    mohProcessDto.setTaskId(taskId);
                    ParamUtil.setSessionAttr(request, KEY_MOH_PROCESS_DTO, mohProcessDto);

                    //prepare doc
                    setApplicantDoc(request,submitDetailsDto);
                }
            }
            if (failLoadSubmitDetailsData) {
                throw new IaisRuntimeException(ERR_MSG_FAIL_LOAD_SUBMIT_DETAILS);
            }
        }
    }

    public static void setApplicantDoc(HttpServletRequest request, SubmitDetailsDto submitDetailsDto){
        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        String processType = submitDetailsDto.getProcessType();
        if (processType.equals(MasterCodeConstants.PROCESS_TYPE_FAC_REG)) {
            primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(submitDetailsDto.getFacilityRegisterDto().getDocRecordInfos(), DocRecordInfo::getRepoId));
        } else if (processType.equals(MasterCodeConstants.PROCESS_TYPE_APPROVE_POSSESS) || processType.equals(MasterCodeConstants.PROCESS_TYPE_APPROVE_LSP) || processType.equals(MasterCodeConstants.PROCESS_TYPE_SP_APPROVE_HANDLE)) {
            primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(submitDetailsDto.getApprovalAppDto().getDocRecordInfos(), DocRecordInfo::getRepoId));
        } else if (processType.equals(MasterCodeConstants.PROCESS_TYPE_FAC_CERTIFIER_REG)){
            primaryDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(submitDetailsDto.getFacilityCertifierRegisterDto().getDocRecordInfos(), DocRecordInfo::getRepoId));
        }
        Map<String, List<DocRecordInfo>> saveFiles = primaryDocDto.getExistDocTypeMap();
        Set<String> docTypes = saveFiles.keySet();
        ParamUtil.setRequestAttr(request, "docTypes", docTypes);
        ParamUtil.setRequestAttr(request, "savedFiles", saveFiles);
        ParamUtil.setSessionAttr(request, "primaryDocDto", primaryDocDto);
    }

    public void prepareSwitch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        MohProcessDto mohProcessDto = (MohProcessDto) ParamUtil.getSessionAttr(request, KEY_MOH_PROCESS_DTO);
        mohProcessDto.reqObjMapping(request);

        //validation
        String crudActionType = "";
        ValidationResultDto validationResultDto = processClient.validateMohProcessDto(mohProcessDto);
        if (!validationResultDto.isPass()){
            String doProcess = "Y";
            ParamUtil.setRequestAttr(request, MOH_PROCESS, doProcess);
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            crudActionType = CRUD_ACTION_TYPE_PREPARE;
        }else {
            crudActionType = CRUD_ACTION_TYPE_PROCESS;
        }
        ParamUtil.setRequestAttr(request, CRUD_ACTION_TYPE, crudActionType);
        ParamUtil.setSessionAttr(request, KEY_MOH_PROCESS_DTO, mohProcessDto);
    }

    public void process(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        MohProcessDto mohProcessDto = (MohProcessDto) ParamUtil.getSessionAttr(request, KEY_MOH_PROCESS_DTO);
        processClient.saveMohProcess(mohProcessDto);
    }

    public static void setBatList(HttpServletRequest request, SubmitDetailsDto submitDetailsDto) {
        String processType = submitDetailsDto.getProcessType();
        if (processType.equals(MasterCodeConstants.PROCESS_TYPE_FAC_REG)) {
            List<BiologicalAgentToxinDto> batList = new ArrayList<>(submitDetailsDto.getFacilityRegisterDto().getBiologicalAgentToxinMap().values());
            ParamUtil.setSessionAttr(request, KEY_BAT_LIST, (Serializable) batList);
        } else if (processType.equals(MasterCodeConstants.PROCESS_TYPE_APPROVE_POSSESS) || processType.equals(MasterCodeConstants.PROCESS_TYPE_APPROVE_LSP) || processType.equals(MasterCodeConstants.PROCESS_TYPE_SP_APPROVE_HANDLE)) {
            List<ApprovalProfileDto> approvalProfileList = new ArrayList<>(submitDetailsDto.getApprovalAppDto().getApprovalProfileMap().values());
            ParamUtil.setSessionAttr(request, KEY_APPROVAL_PROFILE_LIST, (Serializable) approvalProfileList);
        }
    }
}