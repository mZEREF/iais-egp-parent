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
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.process.MohProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.process.SubmitDetailsDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static sg.gov.moh.iais.egp.bsb.constant.ProcessContants.*;

/**
 * @author : LiRan
 * @date : 2021/11/22
 */
@Delegator(value = "hmScreeningDelegator")
@Slf4j
public class MohHMScreeningDelegator {
    private static final String FUNCTION_NAME = "HM Screening";
    private static final String PROCESS_FLOW = "HMScreening";

    private final ProcessClient processClient;

    @Autowired
    public MohHMScreeningDelegator(ProcessClient processClient) {
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
                    MohDOScreeningDelegator.setBatList(request, submitDetailsDto);

                    ParamUtil.setSessionAttr(request, LAST_DO_APPLICATION_MISC, submitDetailsDto.getApplicationMiscDtoMap().get("DO"));
                    ParamUtil.setSessionAttr(request, LAST_AO_APPLICATION_MISC, submitDetailsDto.getApplicationMiscDtoMap().get("AO"));

                    MohProcessDto mohProcessDto = new MohProcessDto();
                    mohProcessDto.setProcessFlow(PROCESS_FLOW);
                    mohProcessDto.setProcessType(submitDetailsDto.getProcessType());
                    mohProcessDto.setAppId(appId);
                    mohProcessDto.setTaskId(taskId);
                    ParamUtil.setSessionAttr(request, KEY_MOH_PROCESS_DTO, mohProcessDto);

                    //prepare doc
                    MohDOScreeningDelegator.setApplicantDoc(request,submitDetailsDto);
                }
            }
            if (failLoadSubmitDetailsData) {
                throw new IaisRuntimeException(ERR_MSG_FAIL_LOAD_SUBMIT_DETAILS);
            }
        }
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
}