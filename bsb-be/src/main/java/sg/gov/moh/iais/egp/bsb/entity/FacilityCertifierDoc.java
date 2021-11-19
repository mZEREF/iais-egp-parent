package sg.gov.moh.iais.egp.bsb.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FacilityCertifierDoc extends BaseEntity {

	private String id;

	private FacilityCertifierReg certifierReg;

	private String docName;

	private Integer docSize;

	private String fileRepoId;

	private Date submitDt;

	private String submitBy;

	private String docType;

}
