package sg.gov.moh.iais.egp.bsb.dto.report.investigation.view;


import lombok.Data;


/**
 * @author YiMing
 * @version 2022/1/4 17:33
 **/
@Data
public class IncidentInfoInvestDto {
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
