package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * AUTHOR: YiMing
 * DATE:2021/7/26 10:48
 * DESCRIPTION: TODO
 **/

@Getter
@Setter
@Entity
@ToString
@ExcelSheetProperty(sheetName = "ApplicationInformation")
@Table(name = "view_bsb_biological_application_facility")
public class ApplicationInfoDto implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "APPLICATION_NO")
    @ExcelProperty(cellIndex = 1, cellName = "Application No")
    private String applicationNo;

    @Column(name = "APP_TYPE")
    @ExcelProperty(cellIndex = 2, cellName = "Application Type")
    private String applicationType;

    @Column(name =  "STATUS")
    @ExcelProperty(cellIndex = 3, cellName = "Application Status")
    private String applicationStatus;

    @Column(name = "APPLICATION_DT")
    @ExcelProperty(cellIndex = 4, cellName = "Application Submission Date")
    private Date applicationSubmissionDate;

    @Column (name = "APPROVAL_DATE")
    @ExcelProperty(cellIndex = 5, cellName = "Approval  Date")
    private Date approvalDate;

    @Column(name = "FACILITY_CLASSIFICATION")
    @ExcelProperty(cellIndex = 6, cellName = "Facility Classfiication")
    private String facilityClassification;

    @Column(name = "FACILITY_TYPE")
    @ExcelProperty(cellIndex = 7,cellName = "Facility Type")
    private String facilityType;

    @Column(name = "FACILITY_NAME")
    @ExcelProperty(cellIndex = 8, cellName = "Facility Name")
    private String facilityName;

    @Column(name = "NAME")
    @ExcelProperty(cellIndex = 9, cellName = "Biological Agent/Login")
    private String biologicalAgent;

    @Column(name = "SCHEDULE_TYPE")
    @ExcelProperty(cellIndex = 10, cellName = "Schedule Type")
    private String scheduleType;

    @Column(name = "RISK_LEVEL")
    @ExcelProperty(cellIndex = 11, cellName = "Risk Level Of The Biological Agent/Login")
    private String riskLevelOfTheBiologicalAgent;

    @Column(name = "PROCESS_TYPE")
    @ExcelProperty(cellIndex = 12, cellName = "Process Type")
    private String processType;

    @Column(name = "DO_VERIFIED_DT")
    @ExcelProperty(cellIndex = 13, cellName = "Verified By DO")
    private Date verifiedByDO;

    @Column(name = "AO_VERIFIED_DT")
    @ExcelProperty(cellIndex = 14, cellName = "Verified By AO")
    private Date verifiedByAO;

    @Column(name = "HM_VERIFIED_DT")
    @ExcelProperty(cellIndex = 15, cellName = "Verified By HM")
    private Date verifiedByHM;
//    private String action;
}
