package sg.gov.moh.iais.bsb.dto.biosafetyEnquiry;

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
 * DATE:2021/7/26 15:38
 * DESCRIPTION: TODO
 **/

@Getter
@Setter
@Entity
@ToString
@ExcelSheetProperty(sheetName = "FacilityInformation")
@Table(name = "view_bsb_facility_biological_admin")
public class FacilityInfoDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "ID")
    private String facilityId;

    @Column(name = "FACILITY_NAME")
    @ExcelProperty(cellIndex = 1, cellName = "Facility Name")
    private String facilityName;

    @Column(name = "ADDRESS")
    @ExcelProperty(cellIndex = 2, cellName = "Facility Address")
    private String facilityAddress;

    @Column(name = "FACILITY_CLASSIFICATION")
    @ExcelProperty(cellIndex = 3, cellName = "Facility Classification")
    private String facilityClassification;

    @Column(name = "FACILITY_TYPE")
    @ExcelProperty(cellIndex = 4, cellName = "Facility Type")
    private String facilityType;

    @Column(name = "NAME")
    @ExcelProperty(cellIndex = 5, cellName = "Biological Agent/Login")
    private String biologicalAgent;

    @Column(name = "RISK_LEVEL")
    @ExcelProperty(cellIndex = 6, cellName = "Risk Of The Bioloical Agent/Login")
    private String riskLevelOfTheBiologicalAgent;

    @Column(name = "EXPIRYED_DT")
    @ExcelProperty(cellIndex = 7, cellName = "Facility Expiry Date")
    private Date facilityExpiryDate;

    @Column(name = "IS_PROTECTED")
    @ExcelProperty(cellIndex = 8, cellName = "Gazetted Area")
    private String gazettedArea;

    @Column(name = "OPERATOR_NAME")
    @ExcelProperty(cellIndex = 9, cellName = "Facility Operator")
    private String facilityOperator;

    @Column(name = "ADMIN_NAME")
    @ExcelProperty(cellIndex = 10, cellName = "Facility Admin")
    private String facilityAdmin;

    @Column(name = "APPROVAL_STATUS")
    @ExcelProperty(cellIndex = 11, cellName = "Current Facility Status")
    private String currentFacilityStatus;

    @Column(name = "APPROVAL")
    @ExcelProperty(cellIndex = 12, cellName = "Approval Facility Certifier")
    private String approvedFacilityCertifier;

//    @Transient
//    private String action;
}
