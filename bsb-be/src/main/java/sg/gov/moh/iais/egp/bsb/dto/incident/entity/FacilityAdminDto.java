package sg.gov.moh.iais.egp.bsb.dto.incident.entity;

import lombok.Data;

import java.io.Serializable;


/**
 * @author YiMing
 * @version 2021/12/27 17:36
 **/
@Data

public class FacilityAdminDto implements Serializable {
    private String name;
    private String organisation;
    private String address;
    private String designation;
    private String contactNo;
    private String email;
    private String employmentDate;
    private String type;
    private String isMain;

}
