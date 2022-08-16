package sg.gov.moh.iais.egp.bsb.dto.inbox;

import lombok.Data;
import lombok.EqualsAndHashCode;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.info.approval.ApprovalBasicInfo;

import java.util.List;


@Data
public class InboxApprovalFacAdminResultDto {
    private PageInfo pageInfo;
    private List<ApprovalInfo> approvalInfos;

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class ApprovalInfo extends ApprovalBasicInfo {
        private String renewable;
        private String facilityName;
        private String postalCode;
        private String blkNo;
        private String floorNo;
        private String unitNo;
        private String streetName;
    }
}
