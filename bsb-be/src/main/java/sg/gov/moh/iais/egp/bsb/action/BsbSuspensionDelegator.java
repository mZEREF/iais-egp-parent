package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : tangtang
 */
@Slf4j
@Delegator("bsbSuspensionDelegator")
public class BsbSuspensionDelegator {
    private static final String MODULE_NAME = "Suspension";
    private static final String ACTION_TYPE_SAVE = "doSave";
    private static final String ACTION_TYPE_PREPARE = "prepare";
    private static final String ACTION_TYPE_NEXT = "doNext";
    private static final String ACTION_TYPE = "action_type";

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction(MODULE_NAME, MODULE_NAME);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

    }

    public void doSuspensionValidate(BaseProcessClass bpc) {
        //validate duty officer submitted data
        HttpServletRequest request = bpc.request;
        String actionType = "";
        actionType = ACTION_TYPE_NEXT;
        ParamUtil.setRequestAttr(request, ACTION_TYPE, actionType);
    }

    public void otherValidate(BaseProcessClass bpc) {
        //validate duty officer submitted data
        HttpServletRequest request = bpc.request;
        String actionType = "";
        actionType = ACTION_TYPE_SAVE;
        ParamUtil.setRequestAttr(request, ACTION_TYPE, actionType);
    }

    public void doSave(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

    }

    public void aoValidate(BaseProcessClass bpc) {
        //validate approval officer submitted data
        HttpServletRequest request = bpc.request;
    }

    public void aoSave(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

    }

//    private AppSubmitWithdrawnDto getWithdrawnDto(HttpServletRequest request) {
//        AppSubmitWithdrawnDto auditDto = (AppSubmitWithdrawnDto) ParamUtil.getSessionAttr(request, WITHDRAWN_APP_DTO);
//        return auditDto == null ? getDefaultWithdrawnDto() : auditDto;
//    }
//
//    private AppSubmitWithdrawnDto getDefaultWithdrawnDto() {
//        return new AppSubmitWithdrawnDto();
//    }

//    private void validateData(AppSubmitWithdrawnDto dto,HttpServletRequest request){
//        validation
//        String actionType = "";
//        ValidationResultDto validationResultDto = withdrawnClient.validateWithdrawnDto(dto);
//        if (!validationResultDto.isPass()){
//            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
//            actionType = ACTION_TYPE_PREPARE;
//        }else {
//            actionType = ACTION_TYPE_NEXT;
//        }
//        ParamUtil.setRequestAttr(request, ACTION_TYPE, actionType);
//    }
}
