package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import sg.gov.moh.iais.egp.bsb.client.IncidentProcessClient;
import sg.gov.moh.iais.egp.bsb.client.InternalDocClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.incident.*;

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
    private static final String KEY_APP_ID = "appId";
    private static final String PARAM_REPO_ID_FILE_MAP = "repoIdDocMap";
    private static final String KEY_FOLLOW_UP_PROCESS_DTO = "followupDto";
    private static final String KEY_FOLLOW_UP_VIEW_INFO = "processDto";
    public static final String KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST    = "supportDocDisplayDtoList";
    public static final String KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST   = "internalDocDisplayDtoList";
    private final IncidentProcessClient incidentProcessClient;
    private final InternalDocClient internalDocClient;

    public IncidentFollowupProcessService(IncidentProcessClient incidentProcessClient, InternalDocClient internalDocClient) {
        this.incidentProcessClient = incidentProcessClient;
        this.internalDocClient = internalDocClient;
    }

    public void prepareData(HttpServletRequest request) {
        String appId = (String) ParamUtil.getSessionAttr(request,KEY_APP_ID);
        FollowupViewDto followupViewDto  = (FollowupViewDto) ParamUtil.getSessionAttr(request,KEY_FOLLOW_UP_VIEW_INFO);
        //set support document display
        ParamUtil.setRequestAttr(request,KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST,followupViewDto.getSupportDocDisplayDtoList());
        //set internal document
        List<DocDisplayDto> internalDocDisplayDto = internalDocClient.getInternalDocForDisplay(appId);
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST, internalDocDisplayDto);
    }

    public void preViewIncidentFollowup1A(HttpServletRequest request){
        String appId = (String) ParamUtil.getSessionAttr(request,KEY_APP_ID);
        Followup1AViewDto followup1AViewDto = incidentProcessClient.getFollowup1AViewDtoByApplicationId(appId).getEntity();
        ParamUtil.setRequestAttr(request,"view",followup1AViewDto);
        List<DocRecordInfo> docRecordInfos = followup1AViewDto.getDocRecordInfos();
        viewBasicDoc(docRecordInfos,request);
        ParamUtil.setRequestAttr(request,"docSettings",getFollowupDocSettings());
    }

    public void preViewIncidentFollowup1B(HttpServletRequest request){
        String appId = (String) ParamUtil.getSessionAttr(request,KEY_APP_ID);
        Followup1BViewDto followup1BViewDto = incidentProcessClient.getFollowup1BViewDtoByApplicationId(appId).getEntity();
        ParamUtil.setRequestAttr(request,"view",followup1BViewDto);
        List<DocRecordInfo> docRecordInfos = followup1BViewDto.getDocRecordInfos();
        viewBasicDoc(docRecordInfos,request);
        ParamUtil.setRequestAttr(request,"docSettings",getFollowupDocSettings());
    }


    public String replaceServiceType(String serviceType){
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

    public void clearSession(HttpServletRequest request){
        request.getSession().removeAttribute(KEY_FOLLOW_UP_PROCESS_DTO);
        request.getSession().removeAttribute(PARAM_REPO_ID_FILE_MAP);
        request.getSession().removeAttribute(KEY_APP_ID);
    }


}
