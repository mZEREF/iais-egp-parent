package sg.gov.moh.iais.egp.bsb.dto.revocation;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.entity.*;

import java.util.List;

/**
 * @author Zhu Tangtang
 * @date 2021/8/13 14:46
 */
@Data
public class SubmitRevokeDto {
    public Application application;
    public Approval approval;
    private List<FacilityBiologicalAgent> agents;
    private List<FacilityActivity> activities;
}
