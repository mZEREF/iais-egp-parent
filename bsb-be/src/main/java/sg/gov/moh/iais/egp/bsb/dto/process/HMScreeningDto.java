package sg.gov.moh.iais.egp.bsb.dto.process;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author : LiRan
 * @date : 2021/12/29
 */
@Data
public class HMScreeningDto implements Serializable {
    private String riskLevel;
    private String riskLevelComments;
    private String doRecommendation;
    private String aoReviewDecision;
    private String aoRemarks;
    private String validityStartDate;
    private String validityEndDate;

    private String hmRemarks;
    private String processingDecision;

    private static final String KEY_HM_REMARKS = "hmRemarks";
    private static final String KEY_PROCESSING_DECISION = "processingDecision";

    public void reqObjMapping(HttpServletRequest request) {
        this.setHmRemarks(ParamUtil.getString(request, KEY_HM_REMARKS));
        this.setProcessingDecision(ParamUtil.getString(request, KEY_PROCESSING_DECISION));
    }
}
