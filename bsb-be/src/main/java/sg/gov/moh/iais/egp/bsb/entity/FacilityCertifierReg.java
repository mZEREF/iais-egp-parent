package sg.gov.moh.iais.egp.bsb.entity;
import com.ecquaria.cloud.moh.iais.common.base.BaseEntity;
import lombok.Data;

import java.util.List;

@Data

public class FacilityCertifierReg extends BaseEntity implements java.io.Serializable {

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
