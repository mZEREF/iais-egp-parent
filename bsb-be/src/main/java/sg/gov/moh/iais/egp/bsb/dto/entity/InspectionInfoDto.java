package sg.gov.moh.iais.egp.bsb.dto.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class InspectionInfoDto implements Serializable {
    private String id;
    private String applicationId;
    private String commonChkLstConfigId;
    private String checkListConfigId;
    private String finalReportData;
    private String taskRefNo;
    private String apptRefNo;
    private String apptDateStatus;
    private Date insStartDate;
    private Date insEndDate;
    private String rescheduleReason;
    private Integer rescheduleCount;
    private Boolean editNCNotificationEmail;
}
