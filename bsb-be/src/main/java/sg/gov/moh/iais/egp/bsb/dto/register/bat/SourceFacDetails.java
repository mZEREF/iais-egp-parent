package sg.gov.moh.iais.egp.bsb.dto.register.bat;


import lombok.Data;

@Data
public class SourceFacDetails {
    private String facilityName;
    private String postalCode;
    private String addressType;
    private String blkNo;
    private String floorNo;
    private String unitNo;
    private String streetName;
    private String building;
}
