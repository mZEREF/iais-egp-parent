package sg.gov.moh.iais.egp.bsb.dto.process.facility;

import lombok.Data;

import java.io.Serializable;

@Data
public class FacilityOfficerDto implements Serializable {
    private String officerName;
    private String nationality;  // use custom validation, need to call DB to get the master code
    private String idType;
    private String idNumber;
    private String designation;
    private String contactNo;
    private String email;
    private String employmentStartDate;
}
