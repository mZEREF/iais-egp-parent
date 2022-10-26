package sg.gov.moh.iais.egp.bsb.dto.adhocInspection;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.dto.PagingAndSortingDto;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AdhocInspectionSearchDto extends PagingAndSortingDto {
    private String searchKeyword;
    private String searchKeywordType;
    private String searchFacilityClassification;
    private String searchFacilityActivityType;
    private String searchFacilityStatus;
    private String searchFacilityId;
}
