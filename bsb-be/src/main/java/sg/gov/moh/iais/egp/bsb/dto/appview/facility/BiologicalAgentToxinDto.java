package sg.gov.moh.iais.egp.bsb.dto.appview.facility;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
public class BiologicalAgentToxinDto {
    @Data
    @NoArgsConstructor
    public static class BATInfo implements Serializable {
        private String schedule;
        private String batName;
        private List<String> sampleType;
        private List<String> workType;
        private String sampleWorkDetail;
        private String procurementMode;
        private String postalCodeT;
        private String addressTypeT;
        private String blockNoT;
        private String floorNoT;
        private String unitNoT;
        private String streetNameT;
        private String buildingNameT;
        private String facNameE;
        private String postalCodeE;
        private String blockNoE;
        private String floorNoE;
        private String unitNoE;
        private String streetNameE;
        private String countryE;
        private String stateE;
        private String contactPersonNameT;
        private String emailAddressT;
        private String contactNoT;
        private String expectedDateT;
        private String courierServiceProviderNameT;
        private String remarksT;
        private String contactPersonNameE;
        private String emailAddressE;
        private String contactNoE;
        private String expectedDateE;
        private String courierServiceProviderNameE;
        private String remarksE;
        private String toxinRegulationDeclare;
    }

    private String activityEntityId;
    private String activityType;

    private List<BATInfo> batInfos;
}
