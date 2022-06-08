package sg.gov.moh.iais.egp.bsb.dto.register.bat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class BiologicalAgentToxinDto implements Serializable {
    private String activityEntityId;
    private String activityType;

    private List<BATInfo> batInfos;

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityEntityId() {
        return activityEntityId;
    }

    public void setActivityEntityId(String activityEntityId) {
        this.activityEntityId = activityEntityId;
    }

    public List<BATInfo> getBatInfos() {
        return new ArrayList<>(batInfos);
    }
}
