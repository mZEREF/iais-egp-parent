package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class FacilityProfileDto implements Serializable {
    private List<FacilityProfileInfo> infoList;
}
