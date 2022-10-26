package sg.gov.moh.iais.egp.bsb.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;



@Data
@NoArgsConstructor
public class BatFacilityDetail implements Serializable {
    private String facilityName;

    private String postalCode;

    private String addressType;

    private String blockNo;

    private String floorNo;

    private String unitNo;

    private String streetName;

    private String building;
}
