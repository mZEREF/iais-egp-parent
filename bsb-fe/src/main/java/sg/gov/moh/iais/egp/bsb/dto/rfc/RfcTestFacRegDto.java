package sg.gov.moh.iais.egp.bsb.dto.rfc;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityRegisterDto;

import java.io.Serializable;

/**
 * @author : LiRan
 * @date : 2021/11/2
 */
@Data
public class RfcTestFacRegDto implements Serializable {

    private static final String NOTIFICATION = "notification";
    private static final String AMENDMENT = "amendment";

    private FacilityRegisterDto oldFacRegDto;

    private FacilityRegisterDto newFacRegDto;

    private String flowType;

    public RfcTestFacRegDto() {
    }

    public void compareOldAndNew(){
        // if flag is true, Notification; if flag is false, Amendment
        boolean flag = true;
        if (!oldFacRegDto.getFacilityProfileDto().getFacName().equals(newFacRegDto.getFacilityProfileDto().getFacName())){
            oldFacRegDto.getFacilityProfileDto().setFacName(null);
            flag = false;
        }
        //TODO compare each of these attributes
        if (flag){
            this.setFlowType(NOTIFICATION);
        }else {
            this.setFlowType(AMENDMENT);
        }
    }
}
