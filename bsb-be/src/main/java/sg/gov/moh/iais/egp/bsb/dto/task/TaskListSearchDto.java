package sg.gov.moh.iais.egp.bsb.dto.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.dto.PagingAndSortingDto;


/**
 * Search DTO for task list module under bsb-be
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TaskListSearchDto extends PagingAndSortingDto {
    private String searchFacName;
    private String searchFacAddr;
    private String searchFacType;
    private String searchAppNo;
    private String searchAppType;
    private String searchAppStatus;
    private String searchProcessType;
    private String searchAppDateFrom;
    private String searchAppDateTo;
}
