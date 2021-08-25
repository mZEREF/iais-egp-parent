package sg.gov.moh.iais.egp.bsb.entity;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FacilityAgentSample implements Serializable {

	private String id;

	private Facility facility;

	private FacilityBiologicalAgent facilityBiologicalAgent;

	private String sampleNature;

	private String sampleNatureOth;

}
