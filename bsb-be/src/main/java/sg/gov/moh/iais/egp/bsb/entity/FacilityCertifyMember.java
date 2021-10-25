package sg.gov.moh.iais.egp.bsb.entity;

import com.ecquaria.cloud.moh.iais.common.base.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
public class FacilityCertifyMember extends BaseEntity implements java.io.Serializable {

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
