package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.annotations.JGlobalMap;
import com.googlecode.jmapper.annotations.JMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JGlobalMap(excluded = {"auditId"})
public class FacilityAuditAppDto extends BaseEntityDto {

	private String id;

	@JMap(value = "${facilityAudit.id}")
	private String auditId;

	private String auditType;

	private String status;

	private String doDecision;

	private String doReason;

	private String aoReason;

	private Date requestAuditDt;

	private String aoRemarks;

	private String doRemarks;
}



