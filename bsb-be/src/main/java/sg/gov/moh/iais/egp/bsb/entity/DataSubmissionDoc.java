package sg.gov.moh.iais.egp.bsb.entity;
import com.ecquaria.cloud.moh.iais.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class DataSubmissionDoc extends BaseEntity {


	private String id;

	private DataSubmission dataSubmission;

	private String docName;

	private Long docSize;

	private String fileRepoId;

	private Date submitDt;

	private String submitBy;

	private String docType;
}
