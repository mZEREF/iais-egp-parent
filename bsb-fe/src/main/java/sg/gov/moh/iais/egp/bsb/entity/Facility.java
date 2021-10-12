package sg.gov.moh.iais.egp.bsb.entity;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;

import java.util.Date;
import java.util.List;

@Data
public class Facility extends BaseEntity {
    private String id;

    private FacilityBiological facBiological;

    private List<FacilityDoc> docs;

    private List<FacilityAdmin> admins;

    private List<FacilityAuthoriser> authorizers;

    private List<FacilityAudit> audits;

    private List<FacilityActivity> facilityActivities;

    private String facilityName;

    private String facilityClassification;

    private String postalCode;

    private String blkNo;

    private String floorNo;

    private String unitNo;

    private String streetName;

    private String isProtected;

    private String scheduleType;

    private String approval;

    private String approvalStatus;

    private Date expiryDt;

    private String facilityAddress;

}
