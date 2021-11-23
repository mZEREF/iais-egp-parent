package sg.gov.moh.iais.egp.bsb.entity;
import com.ecquaria.cloud.moh.iais.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class DataSubmissionBat extends BaseEntity {

	private String id;

	private DataSubmission dataSubmission;

	private BigDecimal expectedQty;

	private BigDecimal actualQty;

	private BigDecimal transferredQty;

	private String measurementUnit;

	private String destructionMethod;

	private String destructionProceduresDetails;

	private String discrepantReason;

	private String handleType;

	private String biologicalId;

	private String transferredUnit;

	private String reason;
}
