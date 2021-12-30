package sg.gov.moh.iais.egp.bsb.dto.incident;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.incident.entity.BatInfoDto;

import java.util.List;
import java.util.Map;


/**
 * @author YiMing
 * @version 2021/12/28 8:54
 **/
@Data
public class IncidentBatViewDto {
    private String facClassification;
    private String serviceType;
    private Map<String, List<BatInfoDto>> batInfoDtoMap;

}
