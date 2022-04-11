package sg.gov.moh.iais.egp.bsb.dto.appointment;

import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author tangtang
 * @date 2022/3/29 13:37
 */
@Data
public class AppointmentViewDto implements Serializable {
    //display field
    private String id;
    private String appId;
    private String maskedAppId;
    private String applicationNo;
    private String applicationType;
    private String processType;
    private String applicationStatus;
    private Date inspectionDateAndTime;

    //user entered data
    private String reason;
    private String newStartDate;
    private String newEndDate;

    //system generated date
    private Date inspNewDate;
    private List<ApptUserCalendarDto> userCalendarDtos;
    private String apptRefNo;

    private String module;
}
