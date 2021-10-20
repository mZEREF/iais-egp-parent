package sg.gov.moh.iais.egp.bsb.dto.revocation;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.entity.Application;
import sg.gov.moh.iais.egp.bsb.entity.ApplicationMisc;
import sg.gov.moh.iais.egp.bsb.entity.Approval;
import sg.gov.moh.iais.egp.bsb.entity.RoutingHistory;

/**
 * @author Zhu Tangtang
 * @date 2021/8/13 14:46
 */
@Data
public class SubmitRevokeDto {
    public Application application;
    public Approval approval;
}
