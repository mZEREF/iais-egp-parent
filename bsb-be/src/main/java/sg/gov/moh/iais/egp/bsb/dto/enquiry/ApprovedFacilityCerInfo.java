package sg.gov.moh.iais.egp.bsb.dto.enquiry;


import com.ecquaria.cloud.moh.iais.common.annotation.ExcelSheetProperty;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@ToString
@ExcelSheetProperty(sheetName = "ApprovalFacilityCertifierInformation")
@Table(name = "view_facility_admin_org")
public class ApprovedFacilityCerInfo {

  @Id
  @Column(name = "name")
  private String organisationName;
  @Column(name = "ADMIN_NAME")
  private String facilityAdministrator;
  @Column(name = "FACILITY_STATUS")
  private String facilityStatus;
  @Column(name = "TEAM_MEMBER_NAME")
  private String teamMemberName;
  @Column(name = "ID_NUMBER")
  private String idNumber;
  @Column(name = "APPROVAL_DT")
  private Date approvalDt;
  @Column(name = "ADDRESS")
  private String address;
  @Column(name = "EXPIRYED_DT")
  private Date expiryedDt;

}
