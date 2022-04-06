package sg.gov.moh.iais.egp.bsb.dto.appview.facility;

import lombok.Data;


@Data
public class FacilityProfileDto {
    private String facilityEntityId;
    private String facName;
    private String facType;
    private String sameAddress;
    private String block;
    private String addressType;
    private String streetName;
    private String floor;
    private String unitNo;
    private String postalCode;
    private String building;
    private String facilityProtected;
}
