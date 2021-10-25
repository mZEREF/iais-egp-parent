package sg.gov.moh.iais.egp.bsb.entity;


import lombok.Data;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;

import java.util.Date;

@Data
public class FacilityBiosafetyCommittee extends BaseEntity {
  private String id;

  private Facility facility;

  private String name;

  private String idType;

  private String idNumber;

  private String nationality;

  private String designation;

  private String contactNo;

  private String emailAddr;

  private Date employmentStartDt;

  private String areaOfExpertise;

  private String role;

  private String employeeOfComp;

  private String externalCompName;
}
