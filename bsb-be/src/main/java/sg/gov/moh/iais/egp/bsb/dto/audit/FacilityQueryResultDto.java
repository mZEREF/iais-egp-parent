package sg.gov.moh.iais.egp.bsb.dto.audit;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Zhu Tangtang
 **/

@Data
public class FacilityQueryResultDto {
    private PageInfo pageInfo;
    private List<FacInfo> tasks;

    @Data
    public static class FacInfo implements Serializable{
        private String facId;
        private String approvalId;
        private String processType;
        private String facName;
        private String facClassification;
        private String activityType;
        private Date lastAuditDate;
        private Date auditDate;
        private String auditType;
        private String scenarioCategory;
        private String auditOutcome;
        private String cancelReason;
        private String auditId;
    }
}
