package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import lombok.Data;

import java.io.Serializable;


@Data
public class FacilityProfileDto implements Serializable {
    private String facilityEntityId;
    private String facName;
    private String facType;
    private String facTypeDetails;
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
