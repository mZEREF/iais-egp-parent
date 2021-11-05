package sg.gov.moh.iais.egp.bsb.entity;



import lombok.Data;
import lombok.EqualsAndHashCode;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;


@Data
@EqualsAndHashCode(callSuper = true)
public class Organization extends BaseEntity {


  private String id;

  private String name;

  private String postalCode;

  private String addrType;

  private String blkNo;

  private String floorNo;

  private String unitNo;

  private String streetName;

  private String buildingName;

  private String uenNo;


}
