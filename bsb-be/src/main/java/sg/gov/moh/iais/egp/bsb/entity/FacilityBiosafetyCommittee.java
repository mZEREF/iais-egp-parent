package sg.gov.moh.iais.egp.bsb.entity;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
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
