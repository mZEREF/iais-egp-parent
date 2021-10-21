package sg.gov.moh.iais.egp.bsb.entity;
import com.ecquaria.cloud.moh.iais.common.base.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data

public class FacilityCertifierAdmin extends BaseEntity implements java.io.Serializable {

	private String id;

	private FacilityCertifierReg certifierReg;

	private String adminName;

	private String adminType;

	private String idType;

	private String idNumber;

	private String designation;

	private String contactNo;

	private String emailAddr;

	private Date employmentStartDt;

	private String nationality;

}
