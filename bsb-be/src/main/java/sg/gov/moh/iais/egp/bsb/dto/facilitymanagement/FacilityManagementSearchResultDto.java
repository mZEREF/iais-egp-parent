package sg.gov.moh.iais.egp.bsb.dto.facilitymanagement;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;

import java.util.List;


@Data
public class FacilityManagementSearchResultDto {
    private PageInfo pageInfo;
    private List<FacilityManagementDisplayDto> displayDtos;
}
