package sg.gov.moh.iais.egp.bsb.dto.followup;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import io.jsonwebtoken.lang.Assert;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;


/**
 * @author YiMing
 * @version 2022/1/10 17:11
 **/
public class FollowupInfoADto implements Serializable {
    private String draftAppNo;

    private String incidentId;

    private String incidentInvestId;

    private List<InfoADto> infoADtoList;


    @Data
    @NoArgsConstructor
    public static class InfoADto implements Serializable {
        private String entityId;

        private String incidentCause;

        private String explainCause;

        private String measure;

        private String implementEntityDate;

        private String isImplemented;

        private String delayReason;

        private String correctiveDate;

        private String remarks;
    }

    public String getDraftAppNo() {
        return draftAppNo;
    }

    public void setDraftAppNo(String draftAppNo) {
        this.draftAppNo = draftAppNo;
    }

    public String getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(String incidentId) {
        this.incidentId = incidentId;
    }

    public String getIncidentInvestId() {
        return incidentInvestId;
    }

    public void setIncidentInvestId(String incidentInvestId) {
        this.incidentInvestId = incidentInvestId;
    }

    public List<InfoADto> getInfoADtoList() {
        return infoADtoList;
    }

    public void setInfoADtoList(List<InfoADto> infoADtoList) {
        this.infoADtoList = infoADtoList;
    }

    private static final String SEPARATOR  = "--v--";
    private static final String KEY_IS_IMPLEMENTED = "isImplemented";
    private static final String KEY_DELAY_REASON = "delayReason";
    private static final String KEY_EXPECTED_CORRECTIVE_DATE = "correctiveDate";
    private static final String KEY_REMARKS = "remarks";
    public void reqObjMapping(HttpServletRequest request){
        Assert.notEmpty(this.infoADtoList,"list is empty");
        for (InfoADto dto : this.infoADtoList) {
            String incidentCause = dto.getIncidentCause();
            dto.setIsImplemented(ParamUtil.getString(request,KEY_IS_IMPLEMENTED+SEPARATOR+incidentCause));
            dto.setDelayReason(ParamUtil.getString(request,KEY_DELAY_REASON+SEPARATOR+incidentCause));
            dto.setCorrectiveDate(ParamUtil.getString(request,KEY_EXPECTED_CORRECTIVE_DATE+SEPARATOR+incidentCause));
            dto.setRemarks(ParamUtil.getString(request,KEY_REMARKS+SEPARATOR+incidentCause));
        }
    }

}
