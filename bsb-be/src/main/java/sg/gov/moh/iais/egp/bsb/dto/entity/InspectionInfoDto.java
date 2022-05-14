package sg.gov.moh.iais.egp.bsb.dto.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther chenlei on 4/28/2022.
 */
@Setter
@Getter
public class InspectionInfoDto implements Serializable {

    private static final long serialVersionUID = -9105959248141652167L;

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
}
