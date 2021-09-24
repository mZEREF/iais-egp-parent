package sg.gov.moh.iais.egp.bsb.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FacilityBiologicalAgent implements Serializable {

	private String id;

	private Facility facility;

	private FacilitySchedule facilitySchedule;

	private String riskLevel;

	private String biologicalId;

	private List<FacilityAgentSample> facilityAgentSamples;

	private String bioName;

	private String admin;

}
