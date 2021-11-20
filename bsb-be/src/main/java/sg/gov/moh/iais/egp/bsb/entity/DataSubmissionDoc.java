package sg.gov.moh.iais.egp.bsb.entity;
import com.ecquaria.cloud.moh.iais.common.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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
