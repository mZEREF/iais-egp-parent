package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.IncidentProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.IncidentProcessConstants;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.incident.IncidentBatViewDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.IncidentNotificationDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.ProcessingDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.entity.IncidentDocDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.entity.IncidentViewDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.entity.InvestViewDto;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author YiMing
 * @version 2022/1/5 14:36
 **/
@Service
@Slf4j
public class IncidentMOHProcessService {
    private static final String PARAM_INCIDENT_PROCESS_DTO = "processDto";
    private static final String PARAM_REPO_ID_FILE_MAP = "repoIdDocMap";
    private static final String PARAM_PROCESS_KEY_DO = "DO";
    private static final String PARAM_PROCESS_KEY_AO = "AO";
    private static final String PARAM_PROCESS_KEY_HM = "HM";
    private static final String PARAM_PLEASE_SELECT = "Please Select";
    private static final String PARAM_PROCESS_KEY = "key";
    private static final String KEY_APPLICATION_ID = "appId";
    private static final String MESSAGE_APPLICATION_ID_IS_NULL = "application id is null";
    private final IncidentProcessClient incidentProcessClient;

    public IncidentMOHProcessService(IncidentProcessClient incidentProcessClient) {
        this.incidentProcessClient = incidentProcessClient;
    }
    public void preIncidentProcessingData(HttpServletRequest request) {
        String maskedAppId = ParamUtil.getString(request,TaskModuleConstants.PARAM_NAME_APP_ID);
        String maskedTaskId = ParamUtil.getString(request,TaskModuleConstants.PARAM_NAME_TASK_ID);
        IncidentNotificationDto incidentNotificationDto = getIncidentNotificationDto(request);
        showBasicInformation(request,incidentNotificationDto,maskedAppId,maskedTaskId);
    }

    public void preInvestProcessingData(HttpServletRequest request) {
        String maskedAppId = ParamUtil.getString(request,TaskModuleConstants.PARAM_NAME_APP_ID);
        String maskedTaskId = ParamUtil.getString(request,TaskModuleConstants.PARAM_NAME_TASK_ID);
        IncidentNotificationDto incidentNotificationDto = getInvestigationReportDto(request);
        showBasicInformation(request,incidentNotificationDto,maskedAppId,maskedTaskId);
    }

    public void preViewInvestReport(HttpServletRequest request){
        String appId = (String) ParamUtil.getSessionAttr(request,KEY_APPLICATION_ID);
        InvestViewDto investViewDto  = incidentProcessClient.getInvestViewDtoByApplicationId(appId).getEntity();
        ParamUtil.setRequestAttr(request,"view",investViewDto);
        List<DocRecordInfo> docRecordInfos = investViewDto.getDocRecordInfos();
        viewBasicDoc(docRecordInfos,request);
        ParamUtil.setRequestAttr(request,"docSettings",getInvestReportDocSettings());
    }

    public void preViewIncidentNotification(HttpServletRequest request){
        String appId = (String) ParamUtil.getSessionAttr(request,KEY_APPLICATION_ID);
        IncidentViewDto incidentViewDto = incidentProcessClient.getIncidentViewDtoByApplicationId(appId).getEntity();
        ParamUtil.setRequestAttr(request,"view",incidentViewDto);
        List<DocRecordInfo> docRecordInfos = incidentViewDto.getDocRecordInfos();
        viewBasicDoc(docRecordInfos,request);
        ParamUtil.setRequestAttr(request,"docSettings",getIncidentNotDocSettings());
    }

    public void handleProcessing(HttpServletRequest request) {
        String actionType = ParamUtil.getString(request, IncidentProcessConstants.PARAM_ACTION_TYPE);
        if(IncidentProcessConstants.ACTION_TYPE_SUBMIT.equals(actionType)){
            ProcessingDto processingDto = getCurrentProcessDto(request);
            processingDto.reqObjMapping(request);
            IncidentNotificationDto incidentNotificationDto = (IncidentNotificationDto) ParamUtil.getSessionAttr(request,PARAM_INCIDENT_PROCESS_DTO);
            incidentNotificationDto.setProcessingDto(processingDto);
            //do validation
            if(processingDto.doValidation()){
                ParamUtil.setRequestAttr(request,IncidentProcessConstants.KEY_INDEED_ACTION_TYPE,IncidentProcessConstants.ACTION_TYPE_SUBMIT);
            }else{
                ParamUtil.setRequestAttr(request,IncidentProcessConstants.KEY_INDEED_ACTION_TYPE,IncidentProcessConstants.ACTION_TYPE_PREPARE);
                ParamUtil.setRequestAttr(request, ValidationConstants.KEY_SHOW_ERROR_SWITCH,ValidationConstants.YES);
            }
            ParamUtil.setSessionAttr(request,PARAM_INCIDENT_PROCESS_DTO,incidentNotificationDto);
        }
    }

    public void doDOProcessing(HttpServletRequest request) {
        ProcessingDto processingDto = getCurrentProcessDto(request);
        incidentProcessClient.saveDOProcess(processingDto);
    }

    public void doAOProcessing(HttpServletRequest request) {
        ProcessingDto processingDto = getCurrentProcessDto(request);
        incidentProcessClient.saveAOProcess(processingDto);
    }

    public void doHMProcessing(HttpServletRequest request) {
        ProcessingDto processingDto = getCurrentProcessDto(request);
        incidentProcessClient.saveHMProcess(processingDto);
    }

    public void doDOInvestProcessing(HttpServletRequest request) {
        ProcessingDto processingDto = getCurrentProcessDto(request);
        incidentProcessClient.saveDOInvestProcess(processingDto);
    }

    public void doAOInvestProcessing(HttpServletRequest request) {
        ProcessingDto processingDto = getCurrentProcessDto(request);
        incidentProcessClient.saveAOInvestProcess(processingDto);
    }

    public void doHMInvestProcessing(HttpServletRequest request) {
        ProcessingDto processingDto = getCurrentProcessDto(request);
        incidentProcessClient.saveHMInvestProcess(processingDto);
    }

    private IncidentNotificationDto getIncidentNotificationDto(HttpServletRequest request){
        String maskAppId = ParamUtil.getString(request, TaskModuleConstants.PARAM_NAME_APP_ID);
        Assert.hasLength(maskAppId,MESSAGE_APPLICATION_ID_IS_NULL);
        String appId = MaskUtil.unMaskValue(TaskModuleConstants.MASK_PARAM_ID,maskAppId);
        return incidentProcessClient.getIncidentNotificationByAppId(appId).getEntity();
    }

    private IncidentNotificationDto getInvestigationReportDto(HttpServletRequest request){
        String maskAppId = ParamUtil.getString(request, TaskModuleConstants.PARAM_NAME_APP_ID);
        Assert.hasLength(maskAppId,MESSAGE_APPLICATION_ID_IS_NULL);
        String appId = MaskUtil.unMaskValue(TaskModuleConstants.MASK_PARAM_ID,maskAppId);
        return incidentProcessClient.getInvestReportByAppId(appId).getEntity();
    }

    private List<DocSetting> getIncidentNotDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(3);
        docSettings.add(new DocSetting(DocConstants.DOC_INCIDENT_REPORT, "Incident Report", false));
        docSettings.add(new DocSetting(DocConstants.DOC_INCIDENT_ACTION_REPORT, "Incident Action Report", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_OTHERS, "Others", false));
        return docSettings;
    }

    private List<DocSetting> getInvestReportDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(1);
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

    public List<String> getMohOfficer(){
        List<String> moh = new ArrayList<>(3);
        moh.add("DO");
        moh.add("AO");
        moh.add("HM");
        return moh;
    }

    public List<SelectOption> getDOTempDecisionOps(){
        List<SelectOption> selectOptions = new ArrayList<>(3);
        selectOptions.add(new SelectOption("",PARAM_PLEASE_SELECT));
        selectOptions.add(new SelectOption(MasterCodeConstants.MOH_PROCESSING_DECISION_APPROVE,"Acknowledged"));
        selectOptions.add(new SelectOption(MasterCodeConstants.MOH_PROCESSING_DECISION_REQUEST_FOR_INFO,"Request for Information"));
        return selectOptions;
    }

    public List<SelectOption> getAOTempDecisionOps(){
        List<SelectOption> selectOptions = new ArrayList<>(5);
        selectOptions.add(new SelectOption("",PARAM_PLEASE_SELECT));
        selectOptions.add(new SelectOption(MasterCodeConstants.MOH_PROCESSING_DECISION_APPROVE,"Approve"));
        selectOptions.add(new SelectOption(MasterCodeConstants.MOH_PROCESSING_DECISION_REJECT,"Reject"));
        selectOptions.add(new SelectOption(MasterCodeConstants.MOH_PROCESSING_DECISION_ROUTE_BACK_TO_HM,"Route To HM"));
        selectOptions.add(new SelectOption(MasterCodeConstants.MOH_PROCESSING_DECISION_REQUEST_FOR_INFO,"Request for Information"));
        return selectOptions;
    }

    public List<SelectOption> getHMTempDecisionOps(){
        List<SelectOption> selectOptions = new ArrayList<>(4);
        selectOptions.add(new SelectOption("",PARAM_PLEASE_SELECT));
        selectOptions.add(new SelectOption(MasterCodeConstants.MOH_PROCESSING_DECISION_APPROVE,"Approve"));
        selectOptions.add(new SelectOption(MasterCodeConstants.MOH_PROCESSING_DECISION_REJECT,"Reject"));
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
        request.getSession().removeAttribute(PARAM_PROCESS_KEY);
        request.getSession().removeAttribute(KEY_APPLICATION_ID);
    }

    private List<SelectOption> getDemandDecision(HttpServletRequest request){
        String key = (String) ParamUtil.getSessionAttr(request,PARAM_PROCESS_KEY);
        Assert.hasLength(key,"process key is null");
        List<SelectOption> currentOps = new ArrayList<>();
        switch (key){
            case PARAM_PROCESS_KEY_DO:
                currentOps = getDOTempDecisionOps();
                break;
            case PARAM_PROCESS_KEY_AO:
                currentOps = getAOTempDecisionOps();
                break;
            case PARAM_PROCESS_KEY_HM:
                currentOps = getHMTempDecisionOps();
                break;
            default:
                break;
        }
        return currentOps;
    }


    private void showBasicInformation(HttpServletRequest request,IncidentNotificationDto incidentNotificationDto,String maskedAppId,String maskedTaskId){
        //split string and join unMasterCode service type
        IncidentBatViewDto incidentBatViewDto = incidentNotificationDto.getIncidentBatViewDto();
        incidentBatViewDto.setServiceType(replaceServiceType(incidentBatViewDto.getServiceType()));
        incidentNotificationDto.setIncidentBatViewDto(incidentBatViewDto);
        List<IncidentDocDto> incidentDocs = incidentNotificationDto.getIncidentDocDtoList();
        Map<String,IncidentDocDto> repoIdDocMap = CollectionUtils.uniqueIndexMap(incidentDocs,IncidentDocDto::getFileRepoId);
        ParamUtil.setSessionAttr(request,PARAM_REPO_ID_FILE_MAP,new HashMap<>(repoIdDocMap));
        ParamUtil.setRequestAttr(request,"scheduleType",getScheduleTypeList());
        ParamUtil.setRequestAttr(request,"decisionOps",getDemandDecision(request));
        ParamUtil.setRequestAttr(request,"mohRoles",getMohOfficer());
        ProcessingDto processingDto = incidentNotificationDto.getProcessingDto();

        //consider to validate error and no appId and taskId in request cause to error recover
        if(StringUtils.hasLength(maskedAppId) && StringUtils.hasLength(maskedTaskId)){
            String appId = MaskUtil.unMaskValue(TaskModuleConstants.MASK_PARAM_ID,maskedAppId);
            String taskId = MaskUtil.unMaskValue(TaskModuleConstants.MASK_PARAM_ID,maskedTaskId);
            if(!maskedAppId.equals(appId)){
                processingDto.setApplicationId(appId);
                ParamUtil.setSessionAttr(request,KEY_APPLICATION_ID,appId);
            }
            if(!maskedTaskId.equals(taskId)){
                processingDto.setTaskId(taskId);
            }
        }
        incidentNotificationDto.setProcessingDto(processingDto);
        ParamUtil.setSessionAttr(request,PARAM_INCIDENT_PROCESS_DTO,incidentNotificationDto);
        String needShowError = ParamUtil.getString(request,ValidationConstants.KEY_SHOW_ERROR_SWITCH);
        if(ValidationConstants.YES.equals(needShowError)){
            ParamUtil.setRequestAttr(request,ValidationConstants.KEY_VALIDATION_ERRORS,processingDto.retrieveValidationResult());
        }
    }

    private void viewBasicDoc(List<DocRecordInfo> docRecordInfos,HttpServletRequest request){
        Map<String,List<DocRecordInfo>> docMap = CollectionUtils.groupCollectionToMap(docRecordInfos,DocRecordInfo::getDocType);
        Map<String,DocRecordInfo> repoIdDocMap = CollectionUtils.uniqueIndexMap(docRecordInfos,DocRecordInfo::getRepoId);
        ParamUtil.setSessionAttr(request,PARAM_REPO_ID_FILE_MAP,new HashMap<>(repoIdDocMap));
        ParamUtil.setRequestAttr(request,"docMap",docMap);
    }

}
