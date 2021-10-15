package sg.gov.moh.iais.egp.bsb.dto.revocation;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.entity.Approval;

import java.util.List;

/**
 * @author Zhu Tangtang
 **/

@Data
public class ApprovalQueryResultDto {
    private PageInfo pageInfo;
    private List<Approval> tasks;
}
