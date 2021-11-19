package sg.gov.moh.iais.egp.bsb.entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;

/**
 * @author : LiRan
 * @date : 2021/8/26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FacilitySchedule extends BaseEntity {

	private String id;

	private Facility facility;

	private String schedule;

}
