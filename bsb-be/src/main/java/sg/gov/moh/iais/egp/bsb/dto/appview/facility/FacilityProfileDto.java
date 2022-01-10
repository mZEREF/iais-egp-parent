package sg.gov.moh.iais.egp.bsb.dto.appview.facility;

import lombok.Data;

import java.io.Serializable;

@Data
public class FacilityProfileDto implements Serializable {
    private String facilityEntityId;
    private String facName;
    private String block;
    private String streetName;
    private String floor;
    private String unitNo;
    private String postalCode;
    private String isFacilityProtected;
}
