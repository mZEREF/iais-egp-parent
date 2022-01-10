package sg.gov.moh.iais.egp.bsb.dto.incident.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author YiMing
 * @version 2021/12/27 18:16
 **/
@Data
public class PersonInvolvedInfoDto implements Serializable {
    private String name;
    private String gender;
    private String telNo;
    private String designation;
    private String personnelInjured;
    private String personnelInvolvement;
    private String involvementDesc;
    private String medicalPerson;
    private String practitionerName;
    private String hospitalName;
    private String medicalDesc;
    private String medicalFollowup;
}
