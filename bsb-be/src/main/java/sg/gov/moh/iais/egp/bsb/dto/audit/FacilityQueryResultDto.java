package sg.gov.moh.iais.egp.bsb.dto.audit;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.entity.FacilityActivity;

import java.util.List;

/**
 * @author Zhu Tangtang
 **/

@Data
public class FacilityQueryResultDto {
    private PageInfo pageInfo;
    private List<FacilityActivity> tasks;
}
