package sg.gov.moh.iais.egp.bsb.dto.appview.facility;

import lombok.Data;

import java.io.Serializable;


@Data
public class FacilityOperatorDto implements Serializable {
    private String facOperator;
    private String designeeName;
    private String nationality;  // use custom validation, need to call DB to get the master code
    private String idType;
    private String idNumber;
    private String designation;
    private String contactNo;
    private String email;
    private String employmentStartDt;
}
