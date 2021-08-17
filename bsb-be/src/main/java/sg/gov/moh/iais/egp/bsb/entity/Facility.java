package sg.gov.moh.iais.egp.bsb.entity;


import lombok.Data;
import java.util.Date;
import java.util.List;


@Data
public class Facility {
    private String id;

    private FacilityBiological facBiological;

    private List<FacilityDoc> docs;

    private List<FacilityAdmin> admins;

    private List<FacilityAuthoriser> authorizers;

    private List<FacilityAudit> audits;

    private String facilityName;

    private String facilityClassification;

    private String facilityType;

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

    private String scheduleType;

    private String uenNo;

    private String approval;

    private String approvalStatus;

    private Date expiryDt;

    private Date createdAt;

    private String createdBy;

    private Date modifiedAt;

    private String modifiedBy;
}
