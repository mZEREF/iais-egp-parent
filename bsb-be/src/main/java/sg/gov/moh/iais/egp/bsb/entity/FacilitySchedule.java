package sg.gov.moh.iais.egp.bsb.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class FacilitySchedule implements Serializable {

	private String id;

	private Facility facility;

	private List<FacilityBiologicalAgent> facilityBiologicalAgents;

	private String schedule;

}
