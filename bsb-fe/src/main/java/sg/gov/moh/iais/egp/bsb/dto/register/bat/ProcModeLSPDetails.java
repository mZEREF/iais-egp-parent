package sg.gov.moh.iais.egp.bsb.dto.register.bat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProcModeLSPDetails extends ProcModeDetails{
    private String facNameS;
    private String postalCodeS;
    private String addressTypeS;
    private String blockNoS;
    private String floorNoS;
    private String unitNoS;
    private String streetNameS;
    private String buildingNameS;
}
