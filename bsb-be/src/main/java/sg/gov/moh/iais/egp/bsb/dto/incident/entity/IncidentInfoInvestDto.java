package sg.gov.moh.iais.egp.bsb.dto.incident.entity;

import lombok.Data;

import java.io.Serializable;


/**
 * @author YiMing
 * @version 2022/1/4 17:33
 **/
@Data
public class IncidentInfoInvestDto implements Serializable {
    private String referenceNo;
    private String incidentType;
    private String facName;
    private String facType;
    private String incidentEntityDate;
    private String location;
    private String involvedBat;
    private String investigatorLead;
    private String otherInvestigator;

}
