package sg.gov.moh.iais.egp.bsb.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;

import java.util.Date;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Approval extends BaseEntity {

    private String id;

    private String processType;

    private String approveNo;

    private String status;

    private Date approvalDate;

    private Date approvalStartDate;

    private Date approvalExpiryDate;

    private List<FacilityActivity> facilityActivities;
    private List<FacilityBiologicalAgent> facilityBiologicalAgents;
    private List<FacilityAudit> facilityAudits;
    private Facility facility;
    private String activeType;
}