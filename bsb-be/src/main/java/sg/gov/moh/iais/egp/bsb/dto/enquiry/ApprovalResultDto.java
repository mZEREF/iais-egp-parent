package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.entity.Approval;



import java.util.List;


/**
 *@author YiMing
 * @version 2021/10/15 14:16
 **/
@Data
public class ApprovalResultDto {

    private PageInfo pageInfo;

    private List<ApprovalInfo> bsbApproval;

    @Data
    public static class ApprovalInfo {
        private String id;
        private String type;
        private String status;
        private String facClassification;
        private String facType;
        private String facName;
        private String facAddress;
        private String facStatus;
        private String bat;
        private String sampleName;
        private String riskLevel;
    }

}
