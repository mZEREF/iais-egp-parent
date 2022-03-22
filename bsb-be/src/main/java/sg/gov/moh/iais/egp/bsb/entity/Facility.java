package sg.gov.moh.iais.egp.bsb.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
public class Facility implements Serializable {

    private String id;

    private List<FacilityDoc> docs;

    private FacilityOperator operator;

    private List<FacilityOfficer> officers;

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

    private String facilityNo;

    private Date createdAt;

    private String createdBy;

    private Date modifiedAt;

    private String modifiedBy;
}
