package sg.gov.moh.iais.egp.bsb.dto.withdrawn;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author tangtang
 * @date 2021/12/10 10:41
 */
@Data
public class AppSubmitWithdrawnDto implements Serializable {
    //search info
    private String taskId;
    private String appId;
    private String appNo;
    private String currentStatus;
    //officer input
    private String doRemarks;
    private String doDecision;
    private String commentForApplicant;
    //
    private String module;
    //
    private SubmissionDetailsInfo submissionDetailsInfo;

    private static final String KEY_DO_REMARKS = "doRemarks";
    private static final String KEY_DO_DECISION = "doDecision";
    private static final String KEY_COMMENT_FOR_APPLICANT = "commentForApplicant";

    public void reqObjMapping(HttpServletRequest request) {
        this.setDoRemarks(ParamUtil.getString(request, KEY_DO_REMARKS));
        this.setDoDecision(ParamUtil.getString(request, KEY_DO_DECISION));
        this.setCommentForApplicant(ParamUtil.getString(request, KEY_COMMENT_FOR_APPLICANT));
    }
}
