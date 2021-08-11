package sg.gov.moh.iais.bsb.dto.revocation;

import com.ecquaria.cloud.moh.iais.common.base.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class BsbRoutingHistoryDto extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "ID")
  private String id;

  @Column(name = "INTERNAL_REMARKS")
  private String internalRemarks;

  @Column(name = "PROCESS_DECISION")
  private String processDecision;

  @Column(name = "APP_STATUS")
  private String appStatus;

  @Column(name = "ACTION_BY")
  private String actionBy;

  @Column(name = "ROLE_ID")
  private String roleId;

  @Column(name = "APPLICATION_NO")
  private String applicationNo;

  @Column(name = "EXTERNAL_REMARKS")
  private String externalRemarks;
}
