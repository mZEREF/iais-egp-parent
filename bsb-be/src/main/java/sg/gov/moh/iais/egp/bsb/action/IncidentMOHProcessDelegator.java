package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.IncidentProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.IncidentProcessConstants;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.incident.IncidentBatViewDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.IncidentNotificationDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.ProcessingDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.entity.IncidentDocDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.entity.IncidentViewDto;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

import java.util.*;
import java.util.stream.Collectors;

import static sg.gov.moh.iais.egp.bsb.constant.ProcessContants.MODULE_NAME;

/**
 * @author YiMing
 * @version 2021/12/29 13:06
 **/
@Delegator(value = "incidentMOHProcessDelegator")
@Slf4j
public class IncidentMOHProcessDelegator {
    private static final String FUNCTION_NAME_DO = "DO Processing";
    private static final String FUNCTION_NAME_AO = "AO Processing";
    private static final String FUNCTION_NAME_HM = "HM Processing";
    private static final String PARAM_INCIDENT_PROCESS_DTO = "processDto";
    private static final String PARAM_REPO_ID_FILE_MAP = "repoIdDocMap";
    private final IncidentProcessClient incidentProcessClient;

    public IncidentMOHProcessDelegator(IncidentProcessClient incidentProcessClient) {
        this.incidentProcessClient = incidentProcessClient;
    }


    public void startDO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        clearSession(request);
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME_DO);
    }

    public void startAO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        clearSession(request);
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME_AO);
    }

    public void startHM(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        clearSession(request);
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME_HM);
    }


    public void preProcessingData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = ParamUtil.getString(request,IncidentProcessConstants.KEY_APP_ID);
        String taskId = ParamUtil.getString(request,IncidentProcessConstants.KEY_TASK_ID);
        IncidentNotificationDto incidentNotificationDto = getIncidentNotificationDto(request);
        //split string and join unMasterCode service type
        IncidentBatViewDto incidentBatViewDto = incidentNotificationDto.getIncidentBatViewDto();
        incidentBatViewDto.setServiceType(replaceServiceType(incidentBatViewDto.getServiceType()));
        incidentNotificationDto.setIncidentBatViewDto(incidentBatViewDto);
        List<IncidentDocDto> incidentDocs = incidentNotificationDto.getIncidentDocDtoList();
        Map<String,IncidentDocDto> repoIdDocMap = CollectionUtils.uniqueIndexMap(incidentDocs,IncidentDocDto::getFileRepoId);
        ParamUtil.setSessionAttr(request,PARAM_REPO_ID_FILE_MAP,new HashMap<>(repoIdDocMap));
        ParamUtil.setRequestAttr(request,"scheduleType",getScheduleTypeList());
        ParamUtil.setRequestAttr(request,"decisionOps",getTempDecisionOps());
        ParamUtil.setRequestAttr(request,"mohRoles",getMohOfficer());
        ProcessingDto processingDto = incidentNotificationDto.getProcessingDto();

        //consider to validate error and no appId and taskId in request cause to error recover
        if(StringUtils.hasLength(appId) && StringUtils.hasLength(taskId)){
            processingDto.setApplicationId(appId);
            processingDto.setTaskId(taskId);
        }
        incidentNotificationDto.setProcessingDto(processingDto);
        ParamUtil.setSessionAttr(request,PARAM_INCIDENT_PROCESS_DTO,incidentNotificationDto);
        String needShowError = ParamUtil.getString(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if(ValidationConstants.YES.equals(needShowError)){
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_VALIDATION_ERRORS,processingDto.retrieveValidationResult());
        }
    }

    public void preViewNotification(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        IncidentNotificationDto incidentNotificationDto = (IncidentNotificationDto) ParamUtil.getSessionAttr(request,PARAM_INCIDENT_PROCESS_DTO);
        IncidentViewDto incidentViewDto = incidentNotificationDto.getApplicationInfoDto().getIncidentViewDto();
        ParamUtil.setRequestAttr(request,"view",incidentViewDto);
        List<DocRecordInfo> docRecordInfos = incidentViewDto.getDocRecordInfos();
        Map<String,List<DocRecordInfo>> docMap = CollectionUtils.groupCollectionToMap(docRecordInfos,DocRecordInfo::getDocType);
        Map<String,DocRecordInfo> repoIdDocMap = CollectionUtils.uniqueIndexMap(docRecordInfos,DocRecordInfo::getRepoId);
        ParamUtil.setSessionAttr(request,PARAM_REPO_ID_FILE_MAP,new HashMap<>(repoIdDocMap));
        ParamUtil.setRequestAttr(request,"docMap",docMap);
        ParamUtil.setRequestAttr(request,"docSettings",getIncidentNotDocSettings());
    }

    public void handleProcessing(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String actionType = ParamUtil.getString(request,IncidentProcessConstants.PARAM_ACTION_TYPE);
        if(IncidentProcessConstants.ACTION_TYPE_BACK.equals(actionType)){
            ParamUtil.setRequestAttr(request,IncidentProcessConstants.PARAM_ACTION_TYPE,IncidentProcessConstants.ACTION_TYPE_BACK);
        }else if(IncidentProcessConstants.ACTION_TYPE_SUBMIT.equals(actionType)){
            ProcessingDto processingDto = getCurrentProcessDto(request);
            processingDto.reqObjMapping(request);
            IncidentNotificationDto incidentNotificationDto = (IncidentNotificationDto) ParamUtil.getSessionAttr(request,PARAM_INCIDENT_PROCESS_DTO);
            incidentNotificationDto.setProcessingDto(processingDto);
            //do validation
            if(processingDto.doValidation()){
                ParamUtil.setRequestAttr(request,IncidentProcessConstants.PARAM_ACTION_TYPE,IncidentProcessConstants.ACTION_TYPE_SUBMIT);
            }else{
                ParamUtil.setRequestAttr(request,IncidentProcessConstants.PARAM_ACTION_TYPE,IncidentProcessConstants.ACTION_TYPE_BACK);
                ParamUtil.setRequestAttr(request, ValidationConstants.KEY_SHOW_ERROR_SWITCH,ValidationConstants.YES);
            }
            ParamUtil.setSessionAttr(request,PARAM_INCIDENT_PROCESS_DTO,incidentNotificationDto);
        }else if(IncidentProcessConstants.ACTION_TYPE_VIEW_APPLICATION.equals(actionType)){
            ParamUtil.setRequestAttr(request,IncidentProcessConstants.PARAM_ACTION_TYPE,IncidentProcessConstants.ACTION_TYPE_VIEW_APPLICATION);
        }
    }

    public void doDOProcessing(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ProcessingDto processingDto = getCurrentProcessDto(request);
        incidentProcessClient.saveDOProcess(processingDto);
    }

    public void doAOProcessing(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ProcessingDto processingDto = getCurrentProcessDto(request);
        incidentProcessClient.saveAOProcess(processingDto);
    }

    public void doHMProcessing(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ProcessingDto processingDto = getCurrentProcessDto(request);
        incidentProcessClient.saveHMProcess(processingDto);
    }

    public IncidentNotificationDto getIncidentNotificationDto(HttpServletRequest request){
//        String appId = ParamUtil.getString(request, IncidentProcessConstants.KEY_APP_ID);
//        Assert.hasLength(appId,"application id is null");
        return incidentProcessClient.getIncidentNotificationByAppId("A80939C0-CF62-EC11-BE74-000C298D317C").getEntity();
    }

    private List<DocSetting> getIncidentNotDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(3);
        docSettings.add(new DocSetting(DocConstants.DOC_INCIDENT_REPORT, "Incident Report", false));
        docSettings.add(new DocSetting(DocConstants.DOC_INCIDENT_ACTION_REPORT, "Incident Action Report", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_OTHERS, "Others", false));
        return docSettings;
    }

    private List<String> getScheduleTypeList(){
        List<String> scheduleTypeList = new ArrayList<>(6);
        scheduleTypeList.add(MasterCodeConstants.FIRST_SCHEDULE_PART_I);
        scheduleTypeList.add(MasterCodeConstants.FIRST_SCHEDULE_PART_II);
        scheduleTypeList.add(MasterCodeConstants.SECOND_SCHEDULE);
        scheduleTypeList.add(MasterCodeConstants.THIRD_SCHEDULE);
        scheduleTypeList.add(MasterCodeConstants.FOURTH_SCHEDULE);
        scheduleTypeList.add(MasterCodeConstants.FIFTH_SCHEDULE);
        return scheduleTypeList;
    }

    private List<String> getMohOfficer(){
        List<String> moh = new ArrayList<>(3);
        moh.add("DO");
        moh.add("AO");
        moh.add("HM");
        return moh;
    }

    private List<SelectOption> getTempDecisionOps(){
        List<SelectOption> selectOptions = new ArrayList<>(3);
        selectOptions.add(new SelectOption("","Please Select"));
        selectOptions.add(new SelectOption(MasterCodeConstants.MOH_PROCESSING_DECISION_APPROVE,"Acknowledged"));
        selectOptions.add(new SelectOption(MasterCodeConstants.MOH_PROCESSING_DECISION_REQUEST_FOR_INFO,"Request for Information"));
        return selectOptions;
    }

    private ProcessingDto getCurrentProcessDto(HttpServletRequest request) {
        IncidentNotificationDto dto = (IncidentNotificationDto) ParamUtil.getSessionAttr(request,PARAM_INCIDENT_PROCESS_DTO);
        return dto.getProcessingDto();
    }

    private String replaceServiceType(String serviceType){
        Assert.hasLength(serviceType,"service type is null");
        String[] strings = serviceType.split(",");
        return Arrays.stream(strings).map(MasterCodeUtil::getCodeDesc).collect(Collectors.joining(","));
    }

    public void clearSession(HttpServletRequest request){
        request.getSession().removeAttribute(PARAM_INCIDENT_PROCESS_DTO);
        request.getSession().removeAttribute(PARAM_REPO_ID_FILE_MAP);
    }
}
