package sg.gov.moh.iais.egp.bsb.dto.inbox;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;

import java.util.Date;
import java.util.List;


@Data
public class InboxApprovalFacAdminResultDto {
    private PageInfo pageInfo;
    private List<ApprovalInfo> approvalInfos;
    private long unreadMsgAmt;

    @Data
    public static class ApprovalInfo {
        private String id;
        private String approveNo;
        private String processType;
        private String status;
        private Date approvalStartDate;
        private Date approvalExpiryDate;
        private String renewable;
        private String facilityName;
        private String postalCode;
        private String blkNo;
        private String floorNo;
        private String unitNo;
        private String streetName;
    }
}
