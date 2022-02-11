package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.IncidentProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.incident.FollowupProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.FollowupViewDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.IncidentBatViewDto;
import sg.gov.moh.iais.egp.bsb.service.IncidentFollowupProcessService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
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
    private static final String PARAM_MODULE_KEY = "module";
    public static final String KEY_APP_ID = "appId";
    public static final String KEY_TASK_ID = "taskId";
    private static final String KEY_FOLLOW_UP_VIEW_INFO = "processDto";
    public static final String KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST    = "supportDocDisplayDtoList";
    public static final String KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST   = "internalDocDisplayDtoList";
    private final IncidentProcessClient processClient;
    private final IncidentFollowupProcessService followupService;

    public IncidentFollowupProcessDelegator(IncidentProcessClient processClient, IncidentFollowupProcessService followupService) {
        this.processClient = processClient;
        this.followupService = followupService;
    }

    public void start(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        followupService.clearSession(request);
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME_DO);
    }

    public void initFollowA(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);
        //get application id
        String appId = (String) ParamUtil.getSessionAttr(request,KEY_APP_ID);
        FollowupViewDto followupViewDto = processClient.getFollowup1AByAppId(appId).getEntity();
        //replace maskedCode service type
        IncidentBatViewDto incidentBatViewDto = followupViewDto.getIncidentBatViewDto();
        incidentBatViewDto.setServiceType(followupService.replaceServiceType(incidentBatViewDto.getServiceType()));
        followupViewDto.setIncidentBatViewDto(incidentBatViewDto);
        ParamUtil.setSessionAttr(request,KEY_FOLLOW_UP_VIEW_INFO,followupViewDto);
    }

    public void initFollowB(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);
        //get application id
        String appId = (String) ParamUtil.getSessionAttr(request,KEY_APP_ID);
        FollowupViewDto followupViewDto = processClient.getFollowup1BByAppId(appId).getEntity();
        //replace maskedCode service type
        IncidentBatViewDto incidentBatViewDto = followupViewDto.getIncidentBatViewDto();
        incidentBatViewDto.setServiceType(followupService.replaceServiceType(incidentBatViewDto.getServiceType()));
        followupViewDto.setIncidentBatViewDto(incidentBatViewDto);
        ParamUtil.setSessionAttr(request,KEY_FOLLOW_UP_VIEW_INFO,followupViewDto);
    }


    public void preProcessing1AData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request,PARAM_MODULE_KEY,"followup1A");
        followupService.prepareData(request);
    }

    public void preProcessing1BData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request,PARAM_MODULE_KEY,"followup1B");
        followupService.prepareData(request);
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

    public void preViewFollowup1A(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        followupService.preViewIncidentFollowup1A(request);
    }

    public void preViewFollowup1B(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        followupService.preViewIncidentFollowup1B(request);
    }

    public void preSuccessData(BaseProcessClass bpc){

    }

}
