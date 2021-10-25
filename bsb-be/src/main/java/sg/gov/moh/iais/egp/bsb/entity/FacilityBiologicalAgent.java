package sg.gov.moh.iais.egp.bsb.entity;
import com.ecquaria.cloud.moh.iais.common.base.BaseEntity;
import lombok.Data;

import java.util.List;

@Data
public class FacilityBiologicalAgent extends BaseEntity {

	private String id;

	private Facility facility;

	private Application application;

	private FacilityActivity facilityActivity;

	private String approveType;

	private String biologicalId;

	private List<FacilityAgentSample> facilityAgentSamples;

	private Approval approval;


	private String useStatus;
}