package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.IncidentProcessClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.incident.*;
import sg.gov.moh.iais.egp.bsb.dto.incident.entity.IncidentDocDto;

import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author YiMing
 * @version 2022/1/21 10:45
 **/
@Service
@Slf4j
public class IncidentFollowupProcessService {
    private static final String KEY_APPLICATION_ID = "appId";
    private static final String PARAM_REPO_ID_FILE_MAP = "repoIdDocMap";
    private static final String KEY_FOLLOW_UP_PROCESS_DTO = "followupDto";
    private static final String KEY_FOLLOW_UP_VIEW_INFO = "followupInfo";
    private static final String MESSAGE_APPLICATION_ID_IS_NULL = "application id is null";
    private final IncidentProcessClient incidentProcessClient;

    public IncidentFollowupProcessService(IncidentProcessClient incidentProcessClient) {
        this.incidentProcessClient = incidentProcessClient;
    }

    public void preFollowup1AProcessingData(HttpServletRequest request) {
        String maskedAppId = ParamUtil.getString(request, TaskModuleConstants.PARAM_NAME_APP_ID);
        String maskedTaskId = ParamUtil.getString(request,TaskModuleConstants.PARAM_NAME_TASK_ID);
        FollowupViewDto followupViewDto = getIncidentFollowup1ADto(request);
        preViewInfo(request,followupViewDto,maskedAppId,maskedTaskId);
    }
    public void preFollowup1BProcessingData(HttpServletRequest request) {
        String maskedAppId = ParamUtil.getString(request, TaskModuleConstants.PARAM_NAME_APP_ID);
        String maskedTaskId = ParamUtil.getString(request,TaskModuleConstants.PARAM_NAME_TASK_ID);
        FollowupViewDto followupViewDto =  getIncidentFollowup1BDto(request);
        preViewInfo(request,followupViewDto,maskedAppId,maskedTaskId);
    }

    public void preViewIncidentFollowup1A(HttpServletRequest request){
        String appId = (String) ParamUtil.getSessionAttr(request,KEY_APPLICATION_ID);
        Followup1AViewDto followup1AViewDto = incidentProcessClient.getFollowup1AViewDtoByApplicationId(appId).getEntity();
        ParamUtil.setRequestAttr(request,"view",followup1AViewDto);
        List<DocRecordInfo> docRecordInfos = followup1AViewDto.getDocRecordInfos();
        viewBasicDoc(docRecordInfos,request);
        ParamUtil.setRequestAttr(request,"docSettings",getFollowupDocSettings());
    }

    public void preViewIncidentFollowup1B(HttpServletRequest request){
        String appId = (String) ParamUtil.getSessionAttr(request,KEY_APPLICATION_ID);
        Followup1BViewDto followup1BViewDto = incidentProcessClient.getFollowup1BViewDtoByApplicationId(appId).getEntity();
        ParamUtil.setRequestAttr(request,"view",followup1BViewDto);
        List<DocRecordInfo> docRecordInfos = followup1BViewDto.getDocRecordInfos();
        viewBasicDoc(docRecordInfos,request);
        ParamUtil.setRequestAttr(request,"docSettings",getFollowupDocSettings());
    }

    private void preViewInfo(HttpServletRequest request, FollowupViewDto followupViewDto, String maskedAppId, String maskedTaskId){
        //split string and join unMasterCode service type
        IncidentBatViewDto incidentBatViewDto = followupViewDto.getIncidentBatViewDto();
        incidentBatViewDto.setServiceType(replaceServiceType(incidentBatViewDto.getServiceType()));
        followupViewDto.setIncidentBatViewDto(incidentBatViewDto);
        List<IncidentDocDto> incidentDocs = followupViewDto.getIncidentDocDtoList();
        Map<String,IncidentDocDto> repoIdDocMap = CollectionUtils.uniqueIndexMap(incidentDocs,IncidentDocDto::getFileRepoId);
        ParamUtil.setRequestAttr(request,PARAM_REPO_ID_FILE_MAP,new HashMap<>(repoIdDocMap));
        //consider to validate error and no appId and taskId in request cause to error recover
        if(StringUtils.hasLength(maskedAppId) && StringUtils.hasLength(maskedTaskId)){
            String appId = MaskUtil.unMaskValue(TaskModuleConstants.MASK_PARAM_ID,maskedAppId);
            String taskId = MaskUtil.unMaskValue(TaskModuleConstants.MASK_PARAM_ID,maskedTaskId);
            Assert.hasLength(appId,MESSAGE_APPLICATION_ID_IS_NULL);
            Assert.hasLength(taskId,"task id is null");
            FollowupProcessDto followupProcessDto = getFollowupProcessDto(request);
            followupProcessDto.setApplicationId(appId);
            followupProcessDto.setTaskId(taskId);
            ParamUtil.setSessionAttr(request,KEY_FOLLOW_UP_PROCESS_DTO,followupProcessDto);
        }
        ParamUtil.setRequestAttr(request,KEY_FOLLOW_UP_VIEW_INFO,followupViewDto);
    }

    private FollowupViewDto getIncidentFollowup1ADto(HttpServletRequest request){
        String maskAppId = ParamUtil.getString(request, TaskModuleConstants.PARAM_NAME_APP_ID);
        Assert.hasLength(maskAppId,MESSAGE_APPLICATION_ID_IS_NULL);
        String appId = MaskUtil.unMaskValue(TaskModuleConstants.MASK_PARAM_ID,maskAppId);
        return incidentProcessClient.getFollowup1AByAppId(appId).getEntity();
    }

    private FollowupViewDto getIncidentFollowup1BDto(HttpServletRequest request){
        String maskAppId = ParamUtil.getString(request, TaskModuleConstants.PARAM_NAME_APP_ID);
        Assert.hasLength(maskAppId,MESSAGE_APPLICATION_ID_IS_NULL);
        String appId = MaskUtil.unMaskValue(TaskModuleConstants.MASK_PARAM_ID,maskAppId);
        return incidentProcessClient.getFollowup1BByAppId(appId).getEntity();
    }

    private String replaceServiceType(String serviceType){
        Assert.hasLength(serviceType,"service type is null");
        String[] strings = serviceType.split(",");
        return Arrays.stream(strings).map(MasterCodeUtil::getCodeDesc).collect(Collectors.joining(","));
    }

    private void viewBasicDoc(List<DocRecordInfo> docRecordInfos,HttpServletRequest request){
        Map<String,List<DocRecordInfo>> docMap = CollectionUtils.groupCollectionToMap(docRecordInfos,DocRecordInfo::getDocType);
        Map<String,DocRecordInfo> repoIdDocMap = CollectionUtils.uniqueIndexMap(docRecordInfos,DocRecordInfo::getRepoId);
        ParamUtil.setSessionAttr(request,PARAM_REPO_ID_FILE_MAP,new HashMap<>(repoIdDocMap));
        ParamUtil.setRequestAttr(request,"docMap",docMap);
    }

    private List<DocSetting> getFollowupDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(1);
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_OTHERS, "Others", false));
        return docSettings;
    }

    public FollowupProcessDto getFollowupProcessDto(HttpServletRequest request){
        FollowupProcessDto followupProcessDto = (FollowupProcessDto) ParamUtil.getSessionAttr(request,KEY_FOLLOW_UP_PROCESS_DTO);
        return followupProcessDto == null?new FollowupProcessDto():followupProcessDto;
    }


}
