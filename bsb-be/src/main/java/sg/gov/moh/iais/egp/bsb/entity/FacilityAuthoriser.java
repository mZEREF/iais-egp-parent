package sg.gov.moh.iais.egp.bsb.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FacilityAuthoriser extends BaseEntity {
    private String id;

    private Facility facility;

    private String salutation;

    private String name;

    private String idType;

    private String idNumber;

    private String nationality;

    private String designation;

    private String contactNo;

    private String email;

    private Date employmentStartDate;

    private String workArea;

    private Date securityClearanceDate;
}
