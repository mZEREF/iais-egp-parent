package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FacilitySelectionDto implements Serializable {
    private String facClassification;
    private List<String> activityTypes;
}
