package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.ReportableEventClient;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.followup.view.Followup1AViewDto;
import sg.gov.moh.iais.egp.bsb.dto.followup.view.Followup1BViewDto;
import sg.gov.moh.iais.egp.bsb.dto.report.investigation.view.InvestViewDto;
import sg.gov.moh.iais.egp.bsb.dto.report.notification.view.IncidentViewDto;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Delegator("bsbInboxReportableEventViewDelegator")
public class BsbInboxReportableEventViewDelegator {
    private static final String MASK_KEY_ID = "id";
    private static final String KEY_VIEW = "view";
    private static final String MASK_KEY_REFER_NO = "referNo";
    private static final String KEY_REFERENCE_NO = "referenceNo";
    private static final String KEY_INCIDENT_ID = "incidentId";
    private static final String PARAM_REPO_ID_FILE_MAP = "repoIdDocMap";
    private static final String KEY_DOC_SETTINGS = "docSettings";
    private static final String KEY_MASKED_EDIT_ID = "maskedEditId";
    public static final String KEY_EDIT_APP_ID = "editId";
    private static final String MESSAGE_MASKED_REFERENCE_NO_IS_NULL = "masked reference no is null";

    private final ReportableEventClient reportableEventClient;

    @Autowired
    public BsbInboxReportableEventViewDelegator(ReportableEventClient reportableEventClient) {
        this.reportableEventClient = reportableEventClient;
    }

    public void preNotificationData(BaseProcessClass bpc) {
        //get incidentNotification view info by application info
        HttpServletRequest request = bpc.request;
        String maskedIncidentId = request.getParameter(KEY_INCIDENT_ID);
        Assert.hasLength(maskedIncidentId,"masked incident id is null");
        String incidentId = MaskUtil.unMaskValue(MASK_KEY_ID,maskedIncidentId);
        if(StringUtils.hasLength(incidentId) && !maskedIncidentId.equals(incidentId)){
            IncidentViewDto incidentViewDto =  reportableEventClient.findIncidentViewDtoByIncidentId(incidentId).getEntity();
            ParamUtil.setRequestAttr(request,KEY_VIEW,incidentViewDto);
            ParamUtil.setRequestAttr(request,KEY_DOC_SETTINGS,getIncidentNotDocSettings());
            viewBasicDoc(incidentViewDto.getDocRecordInfos(),request);
        }
    }

    public void preInvestigationData(BaseProcessClass bpc) {
        //get incidentNotification view info by application info
        HttpServletRequest request = bpc.request;
        String maskedReferenceNo= request.getParameter(KEY_REFERENCE_NO);
        Assert.hasLength(maskedReferenceNo,MESSAGE_MASKED_REFERENCE_NO_IS_NULL);
        String referenceNo = MaskUtil.unMaskValue(MASK_KEY_REFER_NO,maskedReferenceNo);
        if(StringUtils.hasLength(referenceNo) && !maskedReferenceNo.equals(referenceNo)){
            InvestViewDto investViewDto = reportableEventClient.findInvestViewDtoByReferenceNo(referenceNo).getEntity();
            ParamUtil.setRequestAttr(request,KEY_VIEW,investViewDto);
            ParamUtil.setRequestAttr(request,KEY_DOC_SETTINGS,getOthersDocSettings());
            viewBasicDoc(investViewDto.getDocRecordInfos(),request);
        }
    }

    public void preFollowup1AData(BaseProcessClass bpc) {
        //get incidentNotification view info by application info
        HttpServletRequest request = bpc.request;
        String maskedReferenceNo= request.getParameter(KEY_REFERENCE_NO);
        Assert.hasLength(maskedReferenceNo,MESSAGE_MASKED_REFERENCE_NO_IS_NULL);
        String referenceNo = MaskUtil.unMaskValue(MASK_KEY_REFER_NO,maskedReferenceNo);
        if(StringUtils.hasLength(referenceNo) && !maskedReferenceNo.equals(referenceNo)){
            Followup1AViewDto followup1AViewDto =  reportableEventClient.findFollowup1AViewDtoByReferenceNo(referenceNo).getEntity();
            ParamUtil.setRequestAttr(request,KEY_VIEW,followup1AViewDto);
            ParamUtil.setRequestAttr(request,KEY_DOC_SETTINGS,getOthersDocSettings());
            ParamUtil.setRequestAttr(request,KEY_MASKED_EDIT_ID,MaskUtil.maskValue(KEY_EDIT_APP_ID,followup1AViewDto.getApplicationId()));
            viewBasicDoc(followup1AViewDto.getDocRecordInfos(),request);
        }
    }

    public void preFollowup1BData(BaseProcessClass bpc) {
        //get incidentNotification view info by application info
        HttpServletRequest request = bpc.request;
        String maskedReferenceNo= request.getParameter(KEY_REFERENCE_NO);
        Assert.hasLength(maskedReferenceNo,MESSAGE_MASKED_REFERENCE_NO_IS_NULL);
        String referenceNo = MaskUtil.unMaskValue(MASK_KEY_REFER_NO,maskedReferenceNo);
        if(StringUtils.hasLength(referenceNo) && !maskedReferenceNo.equals(referenceNo)){
            Followup1BViewDto followup1BViewDto =  reportableEventClient.findFollowup1BViewDtoByReferenceNo(referenceNo).getEntity();
            ParamUtil.setRequestAttr(request,KEY_VIEW,followup1BViewDto);
            ParamUtil.setRequestAttr(request,KEY_DOC_SETTINGS,getOthersDocSettings());
            ParamUtil.setRequestAttr(request,KEY_MASKED_EDIT_ID,MaskUtil.maskValue(KEY_EDIT_APP_ID,followup1BViewDto.getApplicationId()));
            viewBasicDoc(followup1BViewDto.getDocRecordInfos(),request);
        }
    }

    private List<DocSetting> getIncidentNotDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(3);
        docSettings.add(new DocSetting(DocConstants.DOC_INCIDENT_REPORT, "Incident Report", false));
        docSettings.add(new DocSetting(DocConstants.DOC_INCIDENT_ACTION_REPORT, "Incident Action Report", false));
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_OTHERS, "Others", false));
        return docSettings;
    }

    private List<DocSetting> getOthersDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(1);
        docSettings.add(new DocSetting(DocConstants.DOC_TYPE_OTHERS, "Others", false));
        return docSettings;
    }

    private void viewBasicDoc(List<DocRecordInfo> docRecordInfos, HttpServletRequest request){
        Map<String,List<DocRecordInfo>> docMap = CollectionUtils.groupCollectionToMap(docRecordInfos,DocRecordInfo::getDocType);
        Map<String,DocRecordInfo> repoIdDocMap = CollectionUtils.uniqueIndexMap(docRecordInfos,DocRecordInfo::getRepoId);
        ParamUtil.setSessionAttr(request,PARAM_REPO_ID_FILE_MAP,new HashMap<>(repoIdDocMap));
        ParamUtil.setRequestAttr(request,"docMap",docMap);
    }
}
