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
import sg.gov.moh.iais.egp.bsb.dto.appview.AppViewDto;
import sg.gov.moh.iais.egp.bsb.dto.process.AOScreeningDto;
import sg.gov.moh.iais.egp.bsb.dto.process.MohProcessDto;
import sg.gov.moh.iais.egp.bsb.service.ProcessHistoryService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.YES;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.MASK_PARAM_ID;

/**
 * @author : LiRan
 * @date : 2021/11/22
 */
@Delegator(value = "aoScreeningDelegator")
@Slf4j
public class MohAOScreeningDelegator {
    private static final String FUNCTION_NAME = "AO Screening";

    private final ProcessClient processClient;
    private final ProcessHistoryService processHistoryService;

    @Autowired
    public MohAOScreeningDelegator(ProcessClient processClient, ProcessHistoryService processHistoryService) {
        this.processClient = processClient;
        this.processHistoryService = processHistoryService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_MOH_PROCESS_DTO);
        request.getSession().removeAttribute(MohBeAppViewDelegator.KEY_APP_VIEW_DTO);
        request.getSession().removeAttribute(KEY_ROUTING_HISTORY_LIST);
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String maskedAppId = request.getParameter(PARAM_NAME_APP_ID);
        String maskedTaskId = request.getParameter(PARAM_NAME_TASK_ID);
        if (StringUtils.hasLength(maskedAppId) && StringUtils.hasLength(maskedTaskId)){
            if (log.isInfoEnabled()) {
                log.info("masked app ID: {}", org.apache.commons.lang.StringUtils.normalizeSpace(maskedAppId));
                log.info("masked task ID: {}", org.apache.commons.lang.StringUtils.normalizeSpace(maskedTaskId));
            }
            boolean failLoadData = true;
            String appId = MaskUtil.unMaskValue(MASK_PARAM_ID, maskedAppId);
            String taskId = MaskUtil.unMaskValue(MASK_PARAM_ID, maskedTaskId);
            if (appId != null && taskId != null){
                ResponseDto<MohProcessDto> mohProcessDtoResponseDto = processClient.getAOScreeningDataByAppId(appId);
                if (mohProcessDtoResponseDto.ok()){
                    failLoadData = false;
                    MohProcessDto mohProcessDto = mohProcessDtoResponseDto.getEntity();
                    mohProcessDto.setTaskId(taskId);
                    mohProcessDto.setApplicationId(appId);
                    ParamUtil.setSessionAttr(request, KEY_MOH_PROCESS_DTO, mohProcessDto);
                    //view application process need an applicationDto
                    AppViewDto appViewDto = new AppViewDto();
                    appViewDto.setApplicationId(appId);
                    appViewDto.setProcessType(mohProcessDto.getSubmitDetailsDto().getProcessType());
                    appViewDto.setAppType(mohProcessDto.getSubmitDetailsDto().getAppType());
                    ParamUtil.setSessionAttr(request, MohBeAppViewDelegator.KEY_APP_VIEW_DTO, appViewDto);
                    //show routingHistory list
                    processHistoryService.getAndSetHistoryInSession(mohProcessDto.getSubmitDetailsDto().getApplicationNo(), request);
                }
            }
            if (failLoadData) {
                throw new IaisRuntimeException(ERR_MSG_FAIL_LOAD_SUBMIT_DETAILS);
            }
        }
    }

    public void prepareSwitch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        MohProcessDto mohProcessDto = (MohProcessDto) ParamUtil.getSessionAttr(request, KEY_MOH_PROCESS_DTO);
        AOScreeningDto aoScreeningDto = mohProcessDto.getAoScreeningDto();
        aoScreeningDto.reqObjMapping(request);
        mohProcessDto.setAoScreeningDto(aoScreeningDto);

        //validation
        String crudActionType;
        ValidationResultDto validationResultDto = processClient.validateAOScreeningDto(aoScreeningDto);
        if (!validationResultDto.isPass()){
            ParamUtil.setRequestAttr(request, MOH_PROCESS_PAGE_VALIDATION, YES);
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            crudActionType = CRUD_ACTION_TYPE_PREPARE;
        }else {
            crudActionType = CRUD_ACTION_TYPE_PROCESS;
        }
        ParamUtil.setRequestAttr(request, KEY_CRUD_ACTION_TYPE, crudActionType);
        ParamUtil.setSessionAttr(request, KEY_MOH_PROCESS_DTO, mohProcessDto);
    }

    public void process(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        MohProcessDto mohProcessDto = (MohProcessDto) ParamUtil.getSessionAttr(request, KEY_MOH_PROCESS_DTO);
        //decision: SCREENED_BY_DO, REQUEST_FOR_INFORMATION, REJECT
        processClient.saveAOScreening(mohProcessDto);
        //If the processDecision is reject and the application is saved normally, send the notification to applicant
    }
}