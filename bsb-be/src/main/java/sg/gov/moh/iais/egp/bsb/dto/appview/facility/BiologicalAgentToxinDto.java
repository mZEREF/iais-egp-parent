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

        private String postalCode;
        private String addressType;
        private String blockNo;
        private String floorNo;
        private String unitNo;
        private String streetName;
        private String buildingName;

        private String contactPersonName;
        private String emailAddress;
        private String contactNo;
        private String expectedDate;
        private String courierServiceProviderName;
        private String remarks;
    }

    private String activityEntityId;
    private String activityType;

    private List<BATInfo> batInfos;
}
