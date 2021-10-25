package sg.gov.moh.iais.egp.bsb.dto.revocation;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.entity.Application;
import sg.gov.moh.iais.egp.bsb.entity.FacilityActivity;
import sg.gov.moh.iais.egp.bsb.entity.FacilityBiologicalAgent;
import sg.gov.moh.iais.egp.bsb.entity.FacilityCertifierReg;

/**
 * @author Zhu Tangtang
 */
@Data
public class ViewSelectedRevokeApplicationDto {
    private Application application;
    private FacilityActivity activity;
    private FacilityCertifierReg facilityCertifierReg;
    private FacilityBiologicalAgent facilityBiologicalAgent;
}
