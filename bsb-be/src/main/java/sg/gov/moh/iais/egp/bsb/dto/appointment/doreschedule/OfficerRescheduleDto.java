package sg.gov.moh.iais.egp.bsb.dto.appointment.doreschedule;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.appointment.BsbAppointmentDto;

import java.io.Serializable;
import java.util.List;

@Data
public class OfficerRescheduleDto implements Serializable {
    private String appId;
    private ApptAppInfoDto apptAppInfoDto;
    private List<String> availableDate;
    private BsbAppointmentDto appointmentDto;
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
