package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.entity.FacilityActivity;

import java.util.List;


@Data
public class FacilityResultDto {
    private PageInfo pageInfo;

    private List<FacilityActivity> bsbFac;
}
