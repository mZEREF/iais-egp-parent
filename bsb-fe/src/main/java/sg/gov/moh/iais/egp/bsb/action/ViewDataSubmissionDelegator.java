package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.DataSubmissionClient;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.file.CommonDocDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.submission.DataSubmissionInfo;
import sg.gov.moh.iais.egp.bsb.entity.DocSetting;
import sg.gov.moh.iais.egp.bsb.util.CollectionUtils;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static sg.gov.moh.iais.egp.bsb.constant.DocConstants.KEY_COMMON_DOC_DTO;

@Slf4j
@Delegator("viewDataSubmissionDelegator")
public class ViewDataSubmissionDelegator {
    private static final String KEY_SUBMISSION_ID = "submissionId";
    private static final String KEY_VIEW_DATA_SUBMISSION = "dataSubInfo";
    private static final String KEY_DOC_SETTINGS = "docSettings";
    private static final String KEY_SAVED_FILES = "savedFiles";

    private final DataSubmissionClient dataSubmissionClient;
    @Autowired
    public ViewDataSubmissionDelegator(DataSubmissionClient dataSubmissionClient) {
        this.dataSubmissionClient = dataSubmissionClient;
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String maskedSubmissionId = request.getParameter(KEY_SUBMISSION_ID);
        String submissionId = MaskUtil.unMaskValue("id", maskedSubmissionId);
        if (maskedSubmissionId == null || submissionId == null || maskedSubmissionId.equals(submissionId)) {
            throw new IaisRuntimeException("Invalid Submission ID");
        }
        // retrieve  data of submission
        ResponseDto<DataSubmissionInfo> resultDto = dataSubmissionClient.getDataSubmissionInfo(submissionId);
        if (resultDto.ok()){
            DataSubmissionInfo dataSubmissionInfo = resultDto.getEntity();
            ParamUtil.setRequestAttr(request, KEY_VIEW_DATA_SUBMISSION, dataSubmissionInfo);
            ParamUtil.setRequestAttr(request, KEY_DOC_SETTINGS, getDataSubmissionDocSettings());
            CommonDocDto commonDocDto = new CommonDocDto();
            commonDocDto.setSavedDocMap(CollectionUtils.uniqueIndexMap(dataSubmissionInfo.getDocs(), DocRecordInfo::getRepoId));
            Map<String, List<DocRecordInfo>> saveFiles = commonDocDto.getExistDocTypeMap();
            ParamUtil.setRequestAttr(request, KEY_SAVED_FILES, saveFiles);
            ParamUtil.setSessionAttr(request, KEY_COMMON_DOC_DTO, commonDocDto);
        }else {
            throw new IaisRuntimeException("Fail to retrieve submission data");
        }
    }


    /* Will be removed in future, will get this from config mechanism */
    public List<DocSetting> getDataSubmissionDocSettings () {
        List<DocSetting> docSettings = new ArrayList<>(3);
        docSettings.add(new DocSetting("ityBat", "ItyBat", false));
        docSettings.add(new DocSetting("ityToxin", "ItyToxin", false));
        docSettings.add(new DocSetting("others", "Others", false));
        return docSettings;
    }
}
