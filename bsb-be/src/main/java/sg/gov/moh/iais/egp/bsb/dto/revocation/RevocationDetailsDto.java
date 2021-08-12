package sg.gov.moh.iais.egp.bsb.dto.revocation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Zhu Tangtang
 * @date 2021/7/26 17:08
 */
@Getter
@Setter
@NoArgsConstructor
@Table(name="view_bsb_revocation_details")
@Entity
@ToString
public class RevocationDetailsDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="APPID")
    private String applicationId;

    @Column(name="FACILITYID")
    private String facilityId;

    @Column(name="FACILITY_NAME")
    private String facilityName;

    @Column(name="BLK_NO")
    private String blockNo;

    @Column(name="POSTAL_CODE")
    private String postalCode;

    @Column(name="FLOOR_NO")
    private String floorNo;

    @Column(name="UNIT_NO")
    private String unitNo;

    @Column(name="STREET_NAME")
    private String streetName;

    @Column(name="FACILITY_CLASSIFICATION")
    private String facilityClassification;

    @Column(name="FACILITY_TYPE")
    private String facilityType;

    @Column(name="APPROVAL")
    private String approval;

    @Column(name="APPROVAL_STATUS")
    private String approvalStatus;

    @Column(name="REASON_CONTENT")
    private String reasonContent;

    @Column(name="REMARKS")
    private String remarks;
//
//    private String AORemarks;

//    private String processingDecision;
    @Column(name="STATUS")
    private String currentStatus;
    //uploadFile
}
