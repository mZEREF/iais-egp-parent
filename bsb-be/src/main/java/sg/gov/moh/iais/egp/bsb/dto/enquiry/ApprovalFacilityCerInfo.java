package sg.gov.moh.iais.egp.bsb.dto.enquiry;


import lombok.Data;

import java.util.Date;

@Data
public class ApprovalFacilityCerInfo {

  private String name;
  private String adminName;
  private String facilityStatus;
  private String teamMemberName;
  private String idNumber;
  private Date approvalDt;
  private String address;
  private Date expiryedDt;

}
