package sg.gov.moh.iais.egp.bsb.entity;
import com.ecquaria.cloud.moh.iais.common.base.BaseEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
public class Approval extends BaseEntity{

    private String id;

    private String processType;

    private String approveNo;

    private String status;

    private Date approvalDate;

    private Date approvalStartDate;

    private Date approvalExpiryDate;
    private List<FacilityActivity> facilityActivities;
    private List<FacilityBiologicalAgent> facilityBiologicalAgents;
    private Facility facility;
}
