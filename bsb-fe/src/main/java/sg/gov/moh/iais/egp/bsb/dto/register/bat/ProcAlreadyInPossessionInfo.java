package sg.gov.moh.iais.egp.bsb.dto.register.bat;

import lombok.Data;
import java.io.Serializable;


@Data
public class ProcAlreadyInPossessionInfo implements Serializable {
    private String facNameS;
    private String postalCodeS;
    private String addressTypeS;
    private String blockNoS;
    private String floorNoS;
    private String unitNoS;
    private String streetNameS;
    private String buildingNameS;
}
