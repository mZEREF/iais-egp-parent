package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JGlobalMap
public class FacilityAuditDto extends BaseEntityDto {

	private String id;

	private ApprovalDto approval;

	private ApplicationDto application;

	private List<FacilityAuditAppDto> auditApps;

	private String auditType;

	private String remarks;

	private String status;

	private Date auditDt;

	private String changeReason;

	private String scenarioCategory;

	private String auditOutcome;

	private String finalRemarks;

	private String cancelReason;
}



