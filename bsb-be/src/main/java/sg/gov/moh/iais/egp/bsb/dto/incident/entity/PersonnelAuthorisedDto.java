package sg.gov.moh.iais.egp.bsb.dto.incident.entity;

import lombok.Data;

import java.io.Serializable;


/**
 * @author YiMing
 * @version 2021/12/27 17:45
 **/
@Data
public class PersonnelAuthorisedDto implements Serializable {
    private String name;
    private String nationality;
    private String idNumber;
    private String designation;
    private String contactNo;
    private String email;
    private String employmentDate;
    private String employmentPeriod;
    private String clearanceDate;
    private String workArea;

}
