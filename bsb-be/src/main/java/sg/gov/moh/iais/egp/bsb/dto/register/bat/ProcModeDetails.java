package sg.gov.moh.iais.egp.bsb.dto.register.bat;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProcModeDetails implements Serializable {
    private String procurementMode;

    private String facNameT;
    private String postalCodeT;
    private String addressTypeT;
    private String blockNoT;
    private String floorNoT;
    private String unitNoT;
    private String streetNameT;
    private String buildingNameT;

    private String contactPersonNameT;
    private String emailAddressT;
    private String contactNoT;
    private String expectedDateT;
    private String courierServiceProviderNameT;
    private String remarksT;

    private String facNameE;
    private String postalCodeE;
    private String addressTypeE;
    private String blockNoE;
    private String floorNoE;
    private String unitNoE;
    private String streetNameE;
    private String buildingNameE;
    private String countryE;
    private String cityE;
    private String stateE;

    private String contactPersonNameE;
    private String emailAddressE;
    private String contactNoE;
    private String expectedDateE;
    private String courierServiceProviderNameE;
    private String remarksE;
}
