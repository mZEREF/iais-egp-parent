package sg.gov.moh.iais.egp.bsb.dto.appview.facility;

import lombok.Data;

import java.util.List;

@Data
public class FacilitySelectionDto {
    private String draftAppNo;
    private String facClassification;
    private List<String> activityTypes;
}
