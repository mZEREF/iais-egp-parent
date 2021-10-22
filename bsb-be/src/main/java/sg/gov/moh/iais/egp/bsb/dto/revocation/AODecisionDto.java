package sg.gov.moh.iais.egp.bsb.dto.revocation;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.entity.*;

/**
 * @author Zhu Tangtang
 * @date 2021/8/13 14:46
 */
@Data
public class AODecisionDto {
    private Application application;
    private RoutingHistory history;
    private ApplicationMisc misc;
    private Approval approval;
}
