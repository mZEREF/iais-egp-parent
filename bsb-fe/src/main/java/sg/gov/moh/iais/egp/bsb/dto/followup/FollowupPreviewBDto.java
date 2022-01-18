package sg.gov.moh.iais.egp.bsb.dto.followup;

import lombok.Data;

import java.io.Serializable;


/**
 * @author YiMing
 * @version 2022/1/10 14:15
 **/
@Data
public class FollowupPreviewBDto implements Serializable {
    private String referenceNo;

    private String incidentId;

    private String incidentInvestId;

    private String addPersonnelName;

    private String description;

    private String addTestResult;

    private String addMedicalFollowup;

    private Integer addFpDuration;

}
