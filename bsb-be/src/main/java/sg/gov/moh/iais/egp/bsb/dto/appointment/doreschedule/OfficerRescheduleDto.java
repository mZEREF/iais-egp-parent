package sg.gov.moh.iais.egp.bsb.dto.appointment.doreschedule;


import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OfficerRescheduleDto implements Serializable {
    private String appId;
    private ApptAppInfoDto apptAppInfoDto;
    private List<String> availableDate;
    private AppointmentDto appointmentDto;
    private List<String> userIds;
    private String cancelApptRefNo;
    private String refNo;
    private String processDec;
    // user specify new date
    private String specifyStartDate;
    private String specifyEndDate;
    private String specifyStartHour;
    private String specifyEndHour;

    private String module;
}
