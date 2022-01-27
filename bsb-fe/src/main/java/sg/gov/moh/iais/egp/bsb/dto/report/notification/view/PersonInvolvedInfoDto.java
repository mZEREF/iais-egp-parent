package sg.gov.moh.iais.egp.bsb.dto.report.notification.view;

import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.Data;

/**
 * @author YiMing
 * @version 2021/12/27 18:16
 **/
@Data
@JGlobalMap
public class PersonInvolvedInfoDto {
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
