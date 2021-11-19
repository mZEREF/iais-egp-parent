package sg.gov.moh.iais.egp.bsb.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;

import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FacilityCertifierAdmin extends BaseEntity {

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
