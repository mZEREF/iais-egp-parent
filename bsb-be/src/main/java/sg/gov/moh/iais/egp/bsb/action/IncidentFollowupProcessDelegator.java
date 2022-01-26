package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.IncidentProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.incident.FollowupProcessDto;
import sg.gov.moh.iais.egp.bsb.service.IncidentFollowupProcessService;
import sg.gov.moh.iais.egp.bsb.service.IncidentMOHProcessService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import static sg.gov.moh.iais.egp.bsb.constant.module.ProcessContants.MODULE_NAME;

/**
 * @author YiMing
 * @version 2022/1/21 13:39
 **/
@Delegator(value = "followupProcessDelegator")
@Slf4j
public class IncidentFollowupProcessDelegator {
    private static final String FUNCTION_NAME_DO = "DO Processing";
    private static final String KEY_PREPARE = "prepare";
    private static final String KEY_CLOSE = "close";
    private final IncidentMOHProcessService processService;
    private final IncidentProcessClient processClient;
    private final IncidentFollowupProcessService followupService;

    public IncidentFollowupProcessDelegator(IncidentMOHProcessService processService, IncidentProcessClient processClient, IncidentFollowupProcessService followupService) {
        this.processService = processService;
        this.processClient = processClient;
        this.followupService = followupService;
    }

    public void start(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        processService.clearSession(request);
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME_DO);
    }


    public void preProcessing1AData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        followupService.preFollowup1AProcessingData(request);
    }

    public void preProcessing1BData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        followupService.preFollowup1BProcessingData(request);
    }

    public void handleProcessing(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        FollowupProcessDto processDto = followupService.getFollowupProcessDto(request);
        String actionType = ParamUtil.getString(request,ModuleCommonConstants.KEY_ACTION_TYPE);
        if("add".equals(actionType)){
            processDto.reqObjMapping(request);
            processClient.saveNoteFromMOHDO(processDto);
        } else if(KEY_CLOSE.equals(actionType)){
            processClient.closeDOTask(processDto.getTaskId());
        }

    }

    public void actionFilter(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String actionType = ParamUtil.getString(request,ModuleCommonConstants.KEY_ACTION_TYPE);
        if("add".equals(actionType)){
            ParamUtil.setRequestAttr(request,ModuleCommonConstants.KEY_INDEED_ACTION_TYPE,KEY_PREPARE);
        }else if(KEY_CLOSE.equals(actionType)){
            ParamUtil.setRequestAttr(request,ModuleCommonConstants.KEY_INDEED_ACTION_TYPE,KEY_CLOSE);
        }
    }

    public void preSuccessData(BaseProcessClass bpc){

    }

}
