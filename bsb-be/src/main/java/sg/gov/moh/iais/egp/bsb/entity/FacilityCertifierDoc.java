package sg.gov.moh.iais.egp.bsb.entity;
import com.ecquaria.cloud.moh.iais.common.base.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
public class FacilityCertifierDoc extends BaseEntity implements java.io.Serializable {

	private String id;

	private FacilityCertifierReg certifierReg;

	private String docName;

	private Integer docSize;

	private String fileRepoId;

	private Date submitDt;

	private String submitBy;

	private String docType;

}
