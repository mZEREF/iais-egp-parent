package sg.gov.moh.iais.egp.bsb.dto.requestForChange;

import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityRegisterDto;

/**
 * @author : LiRan
 * @date : 2021/11/2
 */
public class rfcDto {

    private FacilityRegisterDto oldFacRegDto;

    private FacilityRegisterDto newFacRegDto;

    public void compareOldAndNew(){
        if (newFacRegDto.getFacilityOfficerDto().getEmail().equals(oldFacRegDto.getFacilityOfficerDto().getEmail())){
            oldFacRegDto.getFacilityOfficerDto().setEmail(null);
        }
    }
}
