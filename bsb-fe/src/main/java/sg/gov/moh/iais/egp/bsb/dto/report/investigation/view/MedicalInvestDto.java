package sg.gov.moh.iais.egp.bsb.dto.report.investigation.view;

import lombok.Data;

/**
 * @author YiMing
 * @version 2022/1/4 17:25
 **/
@Data
public class MedicalInvestDto {
    private String personnelName;

    private String medicalUpdate;

    private String testResult;

    private String medicalFollowup;

    private Integer fpDuration;

    private String isIdentified;

    private String addPersonnelName;

    private String involvementDesc;

    private String description;

    private String addTestResult;

    private String addMedicalFollowup;

    private Integer addFpDuration;
}
