package sg.gov.moh.iais.egp.bsb.entity;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
public class Facility implements Serializable {
    private String id;

    private Biological biological;

    private List<FacilityDoc> docs;

    private List<FacilityAdmin> admins;

    private List<FacilityAuthoriser> authorizers;

    private List<FacilityAudit> audits;

    private String facilityName;

    private String facilityClassification;

    private String facilityStatus;

    private String facilityType;

    private String facilityAddress;

    private String postalCode;

    private String blkNo;

    private String floorNo;

    private String unitNo;

    private String streetName;

    private String isProtected;

    private String operatorType;

    private String operatorName;

    private String idType;

    private String idNumber;

    private String designation;

    private String contactNo;

    private String emailAddr;

    private Date employmentStartDt;

    private Organization organization;

    private String scheduleType;

    private String uenNo;

    private String approval;

    private String approvalType;

    private String approvalStatus;

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
}
