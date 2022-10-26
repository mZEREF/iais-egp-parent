package sg.gov.moh.iais.egp.bsb.dto.facilitymanagement;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.dto.PagingAndSortingDto;

import java.util.Set;

/**
 * Search DTO for Facility Management module under bsb-web
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FacilityManagementSearchDto extends PagingAndSortingDto {
    private String searchKeyword;
    private String searchKeywordType;
    private String searchFacilityClassification;
    private String searchFacilityActivityType;
    private String searchFacilityStatus;
    private Set<String> roleIds;
}
