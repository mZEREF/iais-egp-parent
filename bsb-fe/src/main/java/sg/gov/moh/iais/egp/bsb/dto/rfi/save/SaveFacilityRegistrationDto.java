package sg.gov.moh.iais.egp.bsb.dto.rfi.save;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityRegisterDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiDisplayDto;

@Data
public class SaveFacilityRegistrationDto {
    private RfiDisplayDto rfiDisplayDto;
    private FacilityRegisterDto facilityRegisterDto;
}
