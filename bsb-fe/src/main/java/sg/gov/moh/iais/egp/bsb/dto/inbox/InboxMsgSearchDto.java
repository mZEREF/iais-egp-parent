package sg.gov.moh.iais.egp.bsb.dto.inbox;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.dto.PagingAndSortingDto;


/**
 * Search DTO used by inbox tab of inbox module
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class InboxMsgSearchDto extends PagingAndSortingDto {
    private String searchMsgType;
    private String searchAppType;
    private String searchSubject;
    private String dateFrom;
    private String dateTo;


    /**
     * The empty values are intended for the select options since we use empty string for 'All' meaning
     */
    public void clearAllFields() {
        searchMsgType = "";
        searchAppType = "";
        searchSubject = "";
        dateFrom = "";
        dateTo = "";
    }
}
