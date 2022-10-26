package sg.gov.moh.iais.egp.bsb.dto.inbox;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.dto.PagingAndSortingDto;


@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class InboxApprovalSearchDto extends PagingAndSortingDto {
    // "Approval No."
    private String searchApprovalNo;
    // "Application Sub-Type"
    private String searchProcessType;
    // "Facility Name"
    private String searchFacilityName;
    // "Approval Status"
    private String searchStatus;
    // "Approval Date From"
    private String searchStartDateFrom;
    // "Approval Date To"
    private String searchStartDateTo;
    // "Expiry Date From"
    private String searchExpiryDateFrom;
    // "Expiry Date To"
    private String searchExpiryDateTo;
}
