package sg.gov.moh.iais.egp.bsb.dto.report.investigation;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @author YiMing
 * @version 2021/12/17 17:02
 **/
@Slf4j
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IncidentDto implements Serializable {
    private String id;
    private String referenceNo;
    private String incidentType;
    private String facName;
    private String facType;
    private String incidentDate;
    private String location;
    private String batNames;
}
