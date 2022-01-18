package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.RoutingHistoryClient;
import sg.gov.moh.iais.egp.bsb.client.WithdrawnClient;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.RoutingHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.withdrawn.AppSubmitWithdrawnDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.MASK_PARAM_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_TASK_ID;

/**
 * @author : tangtang
 */
@Slf4j
@Delegator("bsbWithDrawnAppDelegatorBE")
public class BsbWithdrawnAppDelegatorBE {
    private static final String MODULE_NAME = "Withdrawn Application";
    private static final String ACTION_TYPE_SAVE = "doSave";
    private static final String ACTION_TYPE_PREPARE = "prepare";
    private static final String ACTION_TYPE = "action_type";
    public static final String WITHDRAWN_APP_DTO = "withdrawnDto";

    private final WithdrawnClient withdrawnClient;
    private final RoutingHistoryClient routingHistoryClient;

    public BsbWithdrawnAppDelegatorBE(WithdrawnClient withdrawnClient, RoutingHistoryClient routingHistoryClient) {
        this.withdrawnClient = withdrawnClient;
        this.routingHistoryClient = routingHistoryClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction(MODULE_NAME, MODULE_NAME);
        ParamUtil.setSessionAttr(request, WITHDRAWN_APP_DTO, null);
        request.getSession().removeAttribute(KEY_ROUTING_HISTORY_LIST);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AppSubmitWithdrawnDto dto = getWithdrawnDto(request);
        ParamUtil.setSessionAttr(request, WITHDRAWN_APP_DTO, dto);
        if (StringUtils.isEmpty(dto.getAppId())){
            String maskedAppId = request.getParameter(PARAM_NAME_APP_ID);
            String maskedTaskId = request.getParameter(PARAM_NAME_TASK_ID);
            if (log.isInfoEnabled()) {
                log.info("masked app ID: {}", org.apache.commons.lang.StringUtils.normalizeSpace(maskedAppId));
                log.info("masked task ID: {}", org.apache.commons.lang.StringUtils.normalizeSpace(maskedTaskId));
            }
            String appId = MaskUtil.unMaskValue(MASK_PARAM_ID, maskedAppId);
            String taskId = MaskUtil.unMaskValue(MASK_PARAM_ID, maskedTaskId);
            if (appId != null && taskId != null){
                ResponseDto<AppSubmitWithdrawnDto> responseDto = withdrawnClient.getWithdrawnDataByApplicationId(appId);
                if (responseDto.ok()){
                    dto = responseDto.getEntity();
                    dto.setTaskId(taskId);
                    ParamUtil.setSessionAttr(request, WITHDRAWN_APP_DTO, dto);
                    //show routingHistory list
                    List<RoutingHistoryDto> routingHistoryDtoList = routingHistoryClient.getRoutingHistoryListByAppNo(dto.getAppNo()).getEntity();
                    ParamUtil.setSessionAttr(request, KEY_ROUTING_HISTORY_LIST, (Serializable) routingHistoryDtoList);
                }else {
                    log.warn("get withdrawn API doesn't return ok, the response is {}", responseDto);
                    ParamUtil.setRequestAttr(request, WITHDRAWN_APP_DTO, new AppSubmitWithdrawnDto());
                }
            }
        }
    }

    public void doValidate(BaseProcessClass bpc) {
        //validate duty officer submitted data
        HttpServletRequest request = bpc.request;
        AppSubmitWithdrawnDto dto = getWithdrawnDto(request);
        dto.reqObjMapping(request);
        dto.setModule("doProcessWithdrawn");
        validateData(dto,request);
        ParamUtil.setSessionAttr(request, WITHDRAWN_APP_DTO, dto);
    }

    public void doSave(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AppSubmitWithdrawnDto dto = getWithdrawnDto(request);
        withdrawnClient.doProcessWithdrawnApp(dto);
    }

    public void aoValidate(BaseProcessClass bpc) {
        //validate approval officer submitted data
        HttpServletRequest request = bpc.request;
        AppSubmitWithdrawnDto dto = getWithdrawnDto(request);
        dto.reqObjMapping(request);
        dto.setModule("aoProcessWithdrawn");
        validateData(dto,request);
        ParamUtil.setSessionAttr(request, WITHDRAWN_APP_DTO, dto);
    }

    public void aoSave(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AppSubmitWithdrawnDto dto = getWithdrawnDto(request);
        withdrawnClient.aoProcessWithdrawnApp(dto);
    }

    private AppSubmitWithdrawnDto getWithdrawnDto(HttpServletRequest request) {
        AppSubmitWithdrawnDto auditDto = (AppSubmitWithdrawnDto) ParamUtil.getSessionAttr(request, WITHDRAWN_APP_DTO);
        return auditDto == null ? getDefaultWithdrawnDto() : auditDto;
    }

    private AppSubmitWithdrawnDto getDefaultWithdrawnDto() {
        return new AppSubmitWithdrawnDto();
    }

    private void validateData(AppSubmitWithdrawnDto dto,HttpServletRequest request){
        //validation
        String actionType;
        ValidationResultDto validationResultDto = withdrawnClient.validateWithdrawnDto(dto);
        if (!validationResultDto.isPass()){
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            actionType = ACTION_TYPE_PREPARE;
        }else {
            actionType = ACTION_TYPE_SAVE;
        }
        ParamUtil.setRequestAttr(request, ACTION_TYPE, actionType);
    }
}
