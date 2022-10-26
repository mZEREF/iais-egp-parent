package sg.gov.moh.iais.egp.bsb.dto.followup;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author YiMing
 * @version 2022/1/10 17:12
 **/
@Data
public class FollowupInfoBDto implements Serializable {
    private String entityId;

    private String referenceNo;

    private String draftAppNo;

    private String incidentId;

    private String incidentInvestId;

    private String personnelName;

    private String followupDes;

    private String testResult;

    private String isExpected;

    private String followupDuration;

    private String followupDate;

    private String remarks;


    private static final String KEY_FOLLOW_UP_DESCRIPTION = "followupDes";
    private static final String KEY_INTERPRETATION_TEST_RESULT = "testResult";
    private static final String KEY_IS_EXPECTED = "isExpected";
    private static final String KEY_FOLLOW_UP_DURATION = "followupDuration";
    private static final String KEY_FOLLOW_UP_DATE = "followupDate";
    private static final String KEY_REMARKS = "remarks";
    public void reqObjMapping(HttpServletRequest request){
        this.followupDes = ParamUtil.getString(request,KEY_FOLLOW_UP_DESCRIPTION);
        this.testResult = ParamUtil.getString(request,KEY_INTERPRETATION_TEST_RESULT);
        this.isExpected = ParamUtil.getString(request,KEY_IS_EXPECTED);
        this.followupDuration = ParamUtil.getString(request,KEY_FOLLOW_UP_DURATION);
        this.followupDate = ParamUtil.getString(request,KEY_FOLLOW_UP_DATE);
        this.remarks = ParamUtil.getString(request,KEY_REMARKS);
    }
}
