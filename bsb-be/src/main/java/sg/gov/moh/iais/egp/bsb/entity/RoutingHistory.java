package sg.gov.moh.iais.egp.bsb.entity;


import com.ecquaria.cloud.moh.iais.common.base.BaseEntity;
import lombok.Data;

@Data
public class RoutingHistory extends BaseEntity {

  private String id;

  private String internalRemarks;

  private String processDecision;

  private String appStatus;

  private String actionBy;

  private String roleId;

  private String applicationNo;

  private String externalRemarks;
}
