package sg.gov.moh.iais.egp.bsb.dto.revocation;

import com.ecquaria.cloud.moh.iais.common.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Zhu Tangtang
 * @date 2021/7/26 18:01
 */
@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "view_bsb_AO_search_result")
public class AOQueryInfoDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="ID")
    private String id;

    @Column(name="FACILITYID")
    private String facilityId;

    @Column(name="APPLICATION_NO")
    private String applicationNo;

    @Column(name="APP_TYPE")
    private String appType;

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

    @Column(name="FACILITY_TYPE")
    private String facilityType;

    @Column(name="PROCESS_TYPE")
    private String processType;

    @Column(name="NAME")
    private String biologicalAgentsAndToxins;

    @Column(name="APPLICATION_DT")
    private Date applicationDt;

    @Column(name="APPROVAL_DATE")
    private Date approvalDate;

    @Column(name="STATUS")
    private String status;

    @Column(name = "FACILITY_STATUS")
    private String facilityStatus;

    @Column(name = "CREATED_BY", updatable = false)
    private String createdBy;

    @Column(name = "CREATED_DT", updatable = false)
    private Date createdDt;

    @Column(name = "UPDATED_BY")
    private String modifiedBy;

    @Column(name = "UPDATED_DT")
    private Date modifiedDt;
}
