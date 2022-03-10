package sg.gov.moh.iais.egp.bsb.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Facility extends BaseEntity {
    private String id;

    private List<FacilityDoc> docs;

    private FacilityOperator operator;

    private FacilityOfficer officer;

    private List<FacilityAdmin> admins;

    private List<FacilityActivity> facilityActivities;

    private List<FacilityAuthoriser> authorizers;

    private List<FacilityBiosafetyCommittee> bioCommittees;

    private Application application;

    private String facilityName;

    private String facilityClassification;

    private String postalCode;

    private String blkNo;

    private String floorNo;

    private String unitNo;

    private String streetName;

    private String isProtected;

    //self audit
    private String facilityAddress;

    private String approval;

    private String approvalStatus;
}
