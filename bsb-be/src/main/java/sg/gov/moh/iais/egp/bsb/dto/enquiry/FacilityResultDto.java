package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.entity.FacilityBiologicalAgent;

import java.util.List;

/**
 * AUTHOR: YiMing
 * DATE:2021/8/12 13:12
 * DESCRIPTION: TODO
 **/
@Data
public class FacilityResultDto {
    private PageInfo pageInfo;

    private List<FacilityBiologicalAgent> bsbFac;
}
