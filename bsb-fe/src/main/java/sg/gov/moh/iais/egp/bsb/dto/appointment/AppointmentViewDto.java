package sg.gov.moh.iais.egp.bsb.dto.appointment;

import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author tangtang
 */
@Data
public class AppointmentViewDto implements Serializable {
    //display field
    private String id;
    private String appId;
    private String maskedAppId;
    private String applicationNo;
    private Date inspectionDateAndTime;
    //facility
    private String facilityName;

    private String facilityClassification;

    private String address;
    //user entered data
    private String reason;
    private String newStartDt;
    private String newEndDt;

    //system generated date
    private Date inspNewDate;
    private List<ApptUserCalendarDto> userCalendarDtos;
    private String apptRefNo;

    private String module;
}
