package sg.gov.moh.iais.egp.bsb.dto.incident.entity;

import lombok.Data;


/**
 * @author YiMing
 * @version 2021/12/27 17:40
 **/
@Data
public class CommiteeDto {
    private String name;
    private String nationality;
    private String idNumber;
    private String designation;
    private String contactNo;
    private String emailAddr;
    private String employmentDate;
    private String areaOfExpertise;
    private String role;
    private String employeeOfComp;

}
