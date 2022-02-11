package sg.gov.moh.iais.egp.bsb.dto.report.investigation.view;

import lombok.Data;

import java.util.Date;

/**
 * @author YiMing
 * @version 2022/1/4 17:28
 **/
@Data
public class IncidentInvestCauseDto {
    private String incidentCause;

    private String otherCause;

    private String explainCause;

    private String measure;

    private Date implementDate;
}
