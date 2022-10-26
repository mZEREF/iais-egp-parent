package sg.gov.moh.iais.egp.bsb.dto.incident.entity;

import lombok.Data;



/**
 * @author YiMing
 * @version 2022/1/20 9:54
 **/
@Data
public class Followup1ACauseDto {
    private String incidentCause;

    private String causeExplain;

    private String correctiveMeasure;

    private String dueEntityDt;

    private String identifiedCorrective;

    private String elaborateReason;

    private String expectedDt;

    private String remark;
}
