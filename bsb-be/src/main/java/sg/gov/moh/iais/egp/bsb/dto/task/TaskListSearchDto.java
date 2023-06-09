package sg.gov.moh.iais.egp.bsb.dto.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.dto.PagingAndSortingDto;

import java.util.Set;


/**
 * Search DTO for task list module under bsb-web
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TaskListSearchDto extends PagingAndSortingDto {
    private String searchAppNo;
    private String searchAppType;
    private String searchAppSubType;
    private String searchSubmissionType;
    private String searchAppStatus;
    private String searchAppDateFrom;
    private String searchAppDateTo;
    private String searchModifiedDateFrom;
    private String searchModifiedDateTo;
    private String userId;
    private Set<String> roleIds;
}
