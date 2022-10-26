package sg.gov.moh.iais.egp.bsb.dto.register.bat;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityProfileInfo;

import java.io.Serializable;

@Data
public class SourceFacDetails implements Serializable {
    private String facilityName;
    private String postalCode;
    private String addressType;
    private String blkNo;
    private String floorNo;
    private String unitNo;
    private String streetName;
    private String building;


    public static SourceFacDetails of(FacilityProfileInfo info){
        SourceFacDetails sourceFacDetails = new SourceFacDetails();
        sourceFacDetails.setFacilityName(info.getFacName());
        sourceFacDetails.setPostalCode(info.getPostalCode());
        sourceFacDetails.setAddressType(info.getAddressType());
        sourceFacDetails.setBlkNo(info.getBlock());
        sourceFacDetails.setFloorNo(info.getFloor());
        sourceFacDetails.setUnitNo(info.getUnitNo());
        sourceFacDetails.setStreetName(info.getStreetName());
        sourceFacDetails.setBuilding(info.getBuilding());
        return sourceFacDetails;
    }
}
