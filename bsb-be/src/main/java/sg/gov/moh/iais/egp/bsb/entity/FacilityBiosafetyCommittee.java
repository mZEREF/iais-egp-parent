package sg.gov.moh.iais.egp.bsb.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
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
}
