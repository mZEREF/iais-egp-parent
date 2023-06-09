package sg.gov.moh.iais.egp.bsb.dto.audit;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.entity.FacilityAuditDto;

import java.util.List;

/**
 * @author Zhu Tangtang
 **/

@Data
public class AuditQueryResultDto {
    private PageInfo pageInfo;
    private List<FacilityAuditDto> tasks;
}
