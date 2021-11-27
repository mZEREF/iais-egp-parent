package sg.gov.moh.iais.egp.bsb.dto.process.facility;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class BiologicalAgentToxinDto implements Serializable{
    @Data
    @NoArgsConstructor
    public static class BATInfo implements Serializable {
        private String schedule;
        private String batName;
        private List<String> sampleType;
        private String otherSampleType;
        private Map<String, String> sampleEntityIdMap;
    }

    private String activityEntityId;
    private String activityType;
    private List<BATInfo> batInfos;
}
