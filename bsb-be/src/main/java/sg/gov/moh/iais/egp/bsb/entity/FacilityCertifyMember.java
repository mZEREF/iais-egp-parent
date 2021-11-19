package sg.gov.moh.iais.egp.bsb.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FacilityCertifyMember extends BaseEntity {

	private String id;

	private FacilityCertifierReg certifierReg;

	private String name;

	private String idType;

	private String idNumber;

	private Date dob;

	private String sex;

	private String nationality;

	private String telNo;

	private String jobDesignation;

	private String leadCertifier;

	private String areaOfExpertise;

	private String expInCertification;

	private String expInCommission;

	private String expInOthers;

	private String educationBackground;

	private String positionActivity;

	private String relevatnCertificate;

	private String facilityResearches;

}
