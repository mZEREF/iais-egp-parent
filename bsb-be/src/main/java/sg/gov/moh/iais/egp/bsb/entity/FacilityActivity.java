package sg.gov.moh.iais.egp.bsb.entity;

import com.ecquaria.cloud.moh.iais.common.base.BaseEntity;
import lombok.Data;
import java.util.List;


@Data
public class FacilityActivity extends BaseEntity {

    private String id;


    private Facility facility;

    private Application application;

    private String activityType;

    private List<FacilityBiologicalAgent> biologicalAgents;

    private Approval approval;

    private String isCloned;

    private String useStatus;

    private String bioName;

    private String riskLevel;

    private String admin;
}
