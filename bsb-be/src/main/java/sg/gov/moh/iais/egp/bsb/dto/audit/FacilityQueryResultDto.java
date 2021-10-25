package sg.gov.moh.iais.egp.bsb.dto.audit;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.entity.Facility;

import java.util.List;

/**
 * @author Zhu Tangtang
 **/

@Data
public class FacilityQueryResultDto {
    private PageInfo pageInfo;
    private List<Facility> tasks;
}
