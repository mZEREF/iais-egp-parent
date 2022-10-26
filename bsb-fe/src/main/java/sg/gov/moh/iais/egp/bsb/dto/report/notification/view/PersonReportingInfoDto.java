package sg.gov.moh.iais.egp.bsb.dto.report.notification.view;

import lombok.Data;



/**
 * @author YiMing
 * @version 2021/12/27 17:56
 **/
@Data
public class PersonReportingInfoDto {
    private String name;
    private String orgName;
    private String address;
    private String officeTelNo;
    private String mobileTelNo;
    private String email;
    private String roleDesignation;
    private String facName;
    private String facType;
    private String activityType;
    private String entityIncidentEDate;
    private String location;
    private String batNames;
    private String incidentDesc;
    private String batReleasePossibility;
    private String incidentPersonInvolved;
    private String emergencyResponse;
    private String immCorrectiveAction;
}
