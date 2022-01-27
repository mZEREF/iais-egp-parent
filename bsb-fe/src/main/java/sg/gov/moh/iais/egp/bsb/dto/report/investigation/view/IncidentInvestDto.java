package sg.gov.moh.iais.egp.bsb.dto.report.investigation.view;

import lombok.Data;

import java.util.List;

/**
 * @author YiMing
 * @version 2022/1/4 17:25
 **/
@Data
public class IncidentInvestDto {
    private String backgroundInfo;
    private String incidentDesc;
    private List<IncidentInvestCauseDto> incidentCauses;
}
