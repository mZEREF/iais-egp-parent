package sg.gov.moh.iais.egp.bsb.dto.appointment;

import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import lombok.Getter;
import lombok.Setter;
import sg.gov.moh.iais.egp.bsb.dto.entity.TaskDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class BsbApptInspectionDateDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<String> inspectionDate;

    private String processDec;

    private String actionValue;

    private Date specificDate;

    private Date specificStartDate;

    private Date specificEndDate;

    private String startHours;

    private String endHours;

    private String amPm;

    private String actionButtonFlag;

    private String sysInspDateFlag;

    private String sysSpecDateFlag;

    private String tcuAuditAnnouncedFlag;

    private List<TaskDto> taskDtos;
    private TaskDto taskDto;
    private Map<String, List<ApptUserCalendarDto>> inspectionDateMap;


    /**
     * System Date Dto
     */
    private BsbAppointmentDto bsbAppointmentDto;

    /**
     * Specific Date Dto
     */
    private BsbAppointmentDto bsbSpecificApptDto;

    private List<String> refNo;

    private List<String> refShowNo;

    private String apptFeSpecificDate;

    private String apptFeReason;

    private List<BsbApptAppInfoShowDto> applicationInfoShow;

}
