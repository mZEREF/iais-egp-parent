package sg.gov.moh.iais.egp.bsb.entity;

import lombok.Data;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FacilityAuditApp extends BaseEntity {

	private String id;

	private FacilityAudit facilityAudit;

	private String auditType;

	private String status;

	private String doDecision;

	private String doReason;

	private String aoReason;

	private Date requestAuditDt;

	private String aoRemarks;

	private String doRemarks;

}
