package sg.gov.moh.iais.egp.bsb.entity;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;

/**
 * @author : LiRan
 * @date : 2021/8/26
 */
@Data
public class FacilitySchedule extends BaseEntity {

	private String id;

	private Facility facility;

	private String schedule;

}
