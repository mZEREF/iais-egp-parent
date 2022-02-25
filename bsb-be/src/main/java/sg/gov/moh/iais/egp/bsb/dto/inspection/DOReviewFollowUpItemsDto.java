package sg.gov.moh.iais.egp.bsb.dto.inspection;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author : LiRan
 * @date : 2022/2/22
 */
@Data
public class DOReviewFollowUpItemsDto implements Serializable {
    private String currentStatus;
    private String mohRemarks;
    private String applicantRemarks;

    private String processingDecision;
    private String newDueDate;
    private String remarks;

    private static final String KEY_DECISION = "processingDecision";
    private static final String KEY_NEW_DUE_DATE = "newDueDate";
    private static final String KEY_REMARK = "remarks";

    public void reqObjMapping(HttpServletRequest request) {
        this.processingDecision = ParamUtil.getString(request, KEY_DECISION);
        this.newDueDate = ParamUtil.getString(request, KEY_NEW_DUE_DATE);
        this.remarks = ParamUtil.getString(request, KEY_REMARK);
    }
}
