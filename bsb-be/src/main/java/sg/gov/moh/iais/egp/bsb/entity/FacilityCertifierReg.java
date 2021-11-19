package sg.gov.moh.iais.egp.bsb.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FacilityCertifierReg extends BaseEntity {

	private String id;

	private List<FacilityCertifierAdmin> certifierAdmins;

	private List<FacilityCertifierDoc> certDocs;

	private List<FacilityCertifyMember> certifyMembers;

	private Application application;

	private Approval approval;

	private String orgName;

	private String addressType;

	private String floorNo;

	private String unitNo;

	private String building;

	private String streetName;

	private String address1;

	private String address2;

	private String address3;

	private String postalCode;

	private String yearEstablished;

	private String eamilAddr;

	private String contactNo;

	private String contactPerson;

	private String city;

	private String state;

	private String country;

	private String remark;

	private String adminName;

}
