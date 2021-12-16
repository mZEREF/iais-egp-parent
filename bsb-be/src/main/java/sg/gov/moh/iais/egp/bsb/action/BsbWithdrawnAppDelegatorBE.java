package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.WithdrawnClient;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.withdrawn.AppSubmitWithdrawnDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

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

    public BsbWithdrawnAppDelegatorBE(WithdrawnClient withdrawnClient) {
        this.withdrawnClient = withdrawnClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction(MODULE_NAME, MODULE_NAME);
        ParamUtil.setSessionAttr(request, WITHDRAWN_APP_DTO, null);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AppSubmitWithdrawnDto dto = getWithdrawnDto(request);

        ParamUtil.setSessionAttr(request, WITHDRAWN_APP_DTO, dto);
    }

    public void doValidate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AppSubmitWithdrawnDto dto = getWithdrawnDto(request);
        //validation
        String actionType = "";
        ValidationResultDto validationResultDto = withdrawnClient.validateWithdrawnDto(dto);
        if (!validationResultDto.isPass()){
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            actionType = ACTION_TYPE_PREPARE;
        }else {
            actionType = ACTION_TYPE_SAVE;
        }
        ParamUtil.setRequestAttr(request, ACTION_TYPE, actionType);
    }

    public void doSave(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AppSubmitWithdrawnDto dto = getWithdrawnDto(request);

    }

    private AppSubmitWithdrawnDto getWithdrawnDto(HttpServletRequest request) {
        AppSubmitWithdrawnDto auditDto = (AppSubmitWithdrawnDto) ParamUtil.getSessionAttr(request, WITHDRAWN_APP_DTO);
        return auditDto == null ? getDefaultWithdrawnDto() : auditDto;
    }

    private AppSubmitWithdrawnDto getDefaultWithdrawnDto() {
        return new AppSubmitWithdrawnDto();
    }
}
