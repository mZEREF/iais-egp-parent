package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.DataSubmissionClient;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.submission.DataSubmissionDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.submission.DataSubmissionInfo;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Delegator("viewDataSubmissionDelegator")
public class ViewDataSubmissionDelegator {
    private static final String KEY_SUBMISSION_ID = "submissionId";
    private static final String KEY_VIEW_DATA_SUBMISSION = "dataSubInfo";
    private final DataSubmissionClient dataSubmissionClient;
    @Autowired
    public ViewDataSubmissionDelegator(DataSubmissionClient dataSubmissionClient) {
        this.dataSubmissionClient = dataSubmissionClient;
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String maskedSubmissionId = request.getParameter(KEY_SUBMISSION_ID);
        String submissionId = MaskUtil.unMaskValue("id", maskedSubmissionId);
        if (maskedSubmissionId == null || submissionId == null || maskedSubmissionId.equals(submissionId)) {
            throw new IaisRuntimeException("Invalid App ID");
        }
        ParamUtil.setRequestAttr(request, KEY_SUBMISSION_ID, submissionId);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String submissionId = (String) ParamUtil.getRequestAttr(request, KEY_SUBMISSION_ID);
        // retrieve  data of submission
        ResponseDto<DataSubmissionInfo> resultDto = dataSubmissionClient.getDataSubmissionInfo(submissionId);
        if (resultDto.ok()) {
            DataSubmissionInfo entity = resultDto.getEntity();
            List<DataSubmissionDocInfo> docs = entity.getDocs();
            List<DataSubmissionDocInfo> otherDoc = new ArrayList<>();
            List<DataSubmissionDocInfo> files = new ArrayList<>();
            for (DataSubmissionDocInfo doc : docs) {
                if (doc.getDocType().equals("others")){
                    otherDoc.add(doc);
                }else{
                    files.add(doc);
                }
            }
            ParamUtil.setRequestAttr(request,KEY_VIEW_DATA_SUBMISSION,entity);
            ParamUtil.setRequestAttr(request,"otherDocs",otherDoc);
            ParamUtil.setRequestAttr(request,"docs",files);
        } else {
            throw new IaisRuntimeException("Fail to retrieve submission data");
        }
    }
}
