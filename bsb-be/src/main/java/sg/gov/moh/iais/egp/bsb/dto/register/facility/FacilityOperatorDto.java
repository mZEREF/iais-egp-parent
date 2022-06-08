package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import lombok.Data;

import java.io.Serializable;


@Data
public class FacilityOperatorDto implements Serializable {
    private String facOperator;
    private String salutation;
    private String designeeName;
    private String nationality;
    private String idType;
    private String idNumber;
    private String designation;
    private String contactNo;
    private String email;
    private String employmentStartDt;
}
