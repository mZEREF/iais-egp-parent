package sg.gov.moh.iais.egp.bsb.dto.revocation;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BsbRoutingHistoryDto implements Serializable {
  private static final long serialVersionUID = 1L;
  private String id;
  private String internalRemarks;
  private String processDecision;
  private String appStatus;
  private String actionBy;
  private String roleId;
  private String applicationNo;
  private String externalRemarks;
  private String createdBy;
  private Date createdAt;
  private String modifiedBy;
  private Date modifiedAt;
}
