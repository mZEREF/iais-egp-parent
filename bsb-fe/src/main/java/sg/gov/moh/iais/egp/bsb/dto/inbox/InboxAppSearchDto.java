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
    private String searchAppNo;
    private String searchProcessType;
    private String searchStatus;
    private String searchSubmissionDateFrom;
    private String searchSubmissionDateTo;
}
