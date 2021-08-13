package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * AUTHOR: YiMing
 * DATE:2021/7/26 15:51
 * DESCRIPTION: TODO
 **/

@Data
@Entity
@ToString
@ExcelSheetProperty(sheetName = "ApprovalInformation")
@Table(name = "view_facility_bio_bfb")
public class ApprovalInfoDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "APPROVAL_TYPE")
    private String approvalType;
    @Column(name = "APPROVAL_STATUS")
    private String approvalStatus;
    @Column(name = "FACILITY_CLASSIFICATION")
    private String facilityClassification;
    @Column(name = "FACILITY_TYPE")
    private String facilityType;
    @Column(name = "FACILITY_NAME")
    private String facilityName;
    @Column(name = "ADDRESS")
    private String facilityAddress;
    @Column(name = "FACILITY_STATUS")
    private String facilityStatus;
    @Column(name = "NAME")
    private String agent;
    @Column(name = "SAMPLE_NATURE")
    private String natureOfTheSample;
    @Column(name = "RISK_LEVEL")
    private String riskLevelOfTheBiologicalAgent;
    @Column(name = "APPROVAL_SUBMISSION_DT")
    private Date approvalSubDt;
    @Column(name = "APPROVAL_DT")
    private Date approvalDt;
    @Column(name = "SCHEDULE_TYPE")
    private String scheduleType;

//    private String physicalPossessionOfBA;
//    private String action;

}
