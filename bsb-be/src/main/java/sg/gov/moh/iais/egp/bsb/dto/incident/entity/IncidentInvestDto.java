package sg.gov.moh.iais.egp.bsb.dto.incident.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author YiMing
 * @version 2022/1/4 17:25
 **/
@Data
public class IncidentInvestDto implements Serializable {
    private String backgroundInfo;
    private String incidentDesc;
    private List<IncidentInvestCauseDto> incidentCauses;
}
