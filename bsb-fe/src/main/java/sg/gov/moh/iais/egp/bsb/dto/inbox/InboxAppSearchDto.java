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
public class InboxAppSearchDto extends PagingAndSortingDto {
    // "Application No."
    private String searchAppNo;
    // "Application Sub-Type"
    private String searchProcessType;
    // "Facility Name"
    private String searchFacilityName;
    // "Application Status"
    private String searchStatus;
    // "Application Type"
    private String searchAppType;
    // "Date Saved/Submitted From"
    private String searchSubmissionDateFrom;
    // "Date Saved/Submitted To"
    private String searchSubmissionDateTo;
}
