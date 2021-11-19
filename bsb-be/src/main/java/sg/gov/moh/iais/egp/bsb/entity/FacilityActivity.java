package sg.gov.moh.iais.egp.bsb.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
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
