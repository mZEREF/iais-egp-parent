package sg.gov.moh.iais.egp.bsb.entity;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
public class Facility implements Serializable {
    private String id;

    private Biological biological;

    private List<FacilityDoc> docs;

    private List<FacilityAdmin> admins;

    private List<FacilitySchedule> facilitySchedules;

    private List<FacilityBiologicalAgent> facilityBiologicalAgents;

    private List<Application> applications;

    private List<FacilityAuthoriser> authorizers;

    private List<FacilityAudit> audits;

    private List<FacilityActivity> facilityActivities;

    private String facilityName;

    private String facilityType;

    private String facilityClassification;

    private String facilityStatus;

    private String facilityAddress;

    private String postalCode;

    private String blkNo;

    private String floorNo;

    private String unitNo;

    private String streetName;

    private String isProtected;

    private FacilityOperator operator;

    private String idType;

    private String idNumber;

    private String designation;

    private String contactNo;

    private String emailAddr;

    private Date employmentStartDt;

    private String scheduleType;

    private String approval;

    private String approvalType;

    private String approvalStatus;

    private String selectedAfc;

    private Date approvalSubmissionDate;

    private Date approvalDate;

    private Date expiryDt;

    private Date createdAt;

    private String createdBy;

    private Date modifiedAt;

    private String modifiedBy;

    private String riskLevel;

    private String riskLevelComments;

    private Date erpReportDt;

    private Date redTeamingReportDt;

    private Date lentivirusReportDt;

    private Date internalInspectionReportDt;

    private Date validityStartDt;

    private Date validityEndDt;

    private String activeType;

    private String admin;
}
