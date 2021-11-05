package sg.gov.moh.iais.egp.bsb.entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;

/**
 * @author : LiRan
 * @date : 2021/8/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FacilityBiologicalAgent extends BaseEntity {

	private String id;

	private Facility facility;

	private FacilitySchedule facilitySchedule;

	private String biologicalId;

}