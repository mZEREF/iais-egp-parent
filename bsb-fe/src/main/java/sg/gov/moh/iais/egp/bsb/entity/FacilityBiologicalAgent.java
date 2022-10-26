package sg.gov.moh.iais.egp.bsb.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FacilityBiologicalAgent extends BaseEntity {

	private String id;

	private Facility facility;

	private Application application;

	private FacilityActivity facilityActivity;

	private String approveType;

	private String biologicalId;

	private String status;

	private Approval approval;

	private String procurementMode;

	private BatLspInfo lspInfo;

	private BatSpecialHandleInfo specialHandleInfo;

	private List<BatSample> facilityAgentSamples;

	private List<BatWorkActivity> agentWorkActivities;

	private List<BatSpWorkActivity> agentSpecialHandleWorkActivities;

	private BatFacilityDetail transferringFacility;

	private BatContactPerson transferringFacilityContactPerson;

	private String useStatus;

	private Boolean blockFlag;

}