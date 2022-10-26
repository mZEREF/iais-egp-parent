package sg.gov.moh.iais.egp.bsb.dto.incident.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author YiMing
 * @version 2022/1/4 17:28
 **/
@Data
public class IncidentInvestCauseDto implements Serializable {
    private String incidentCause;

    private String otherCause;

    private String explainCause;

    private String measure;

    private Date implementDate;
}
