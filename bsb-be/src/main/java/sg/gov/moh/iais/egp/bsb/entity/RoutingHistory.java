package sg.gov.moh.iais.egp.bsb.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;


@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class RoutingHistory extends BaseEntity {

  private String id;

  private RoutingStage routingStage;

  private String internalRemarks;

  private String processDecision;

  private String appStatus;

  private String actionBy;

  private String roleId;

  private String applicationNo;

  private String externalRemarks;
}
