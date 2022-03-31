package sg.gov.moh.iais.egp.bsb.dto.info.facility;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FacilityActivityInfo extends FacilityActivityBasicInfo{
    private String status;
}
