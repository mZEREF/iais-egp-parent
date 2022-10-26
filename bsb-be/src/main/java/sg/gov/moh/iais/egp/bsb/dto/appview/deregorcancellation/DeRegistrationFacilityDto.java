package sg.gov.moh.iais.egp.bsb.dto.appview.deregorcancellation;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @author : LiRan
 * @date : 2022/1/11
 */
@Data
public class DeRegistrationFacilityDto implements Serializable {
    private String approvalNo;
    private String processType;
    private String draftAppNo;
    private String activityType;

    private String facilityName;
    private String facilityAddress;
    private String facilityClassification;
    private String reasons;
    private String remarks;
    private List<ApprovalInfo> approvalInfoList;

    private List<DocRecordInfo> docRecordInfos;

    @Data
    public static class ApprovalInfo implements Serializable{
        private String approvalType;
        private String biologicalAgentToxin;
        private String status;
        private String physicalPossession;
    }
}
