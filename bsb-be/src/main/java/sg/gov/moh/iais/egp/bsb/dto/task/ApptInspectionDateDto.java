package sg.gov.moh.iais.egp.bsb.dto.task;

import com.ecquaria.cloud.moh.iais.common.annotation.CustomMsg;
import com.ecquaria.cloud.moh.iais.common.annotation.CustomValidate;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import lombok.Getter;
import lombok.Setter;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;
import sg.gov.moh.iais.egp.bsb.dto.entity.TaskDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2020/2/11 10:45
 **/
@Setter
@Getter
@CustomValidate(impClass = "com.ecquaria.cloud.moh.iais.validation.ApptInspectionDateValidate",
        properties = {"specific"})
public class ApptInspectionDateDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<String> inspectionDate;

    private String processDec;

    private String actionValue;

    private Date specificDate;

    @NotNull(message = "GENERAL_ERR0006", profiles = {"specific"})
    @NotBlank(message = "GENERAL_ERR0006", profiles = {"specific"})
    @CustomMsg(placeHolders = "field", replaceVals = "Date")
    private Date specificStartDate;

    @NotNull(message = "GENERAL_ERR0006", profiles = {"specific"})
    @NotBlank(message = "GENERAL_ERR0006", profiles = {"specific"})
    @CustomMsg(placeHolders = "field", replaceVals = "Date")
    private Date specificEndDate;

    @NotNull(message = "GENERAL_ERR0006", profiles = {"specific"})
    @NotBlank(message = "GENERAL_ERR0006", profiles = {"specific"})
    @CustomMsg(placeHolders = "field", replaceVals = "Time")
    private String startHours;

    @NotNull(message = "GENERAL_ERR0006", profiles = {"specific"})
    @NotBlank(message = "GENERAL_ERR0006", profiles = {"specific"})
    @CustomMsg(placeHolders = "field", replaceVals = "Time")
    private String endHours;

    private String amPm;

    private String actionButtonFlag;

    private String sysInspDateFlag;

    private String sysSpecDateFlag;

    private String tcuAuditAnnouncedFlag;

    private AppPremisesInspecApptDto appPremisesInspecApptDto;
    /**
     * Operating list
     */
    private List<AppPremisesInspecApptDto> appPremisesInspecApptDtos;

    private List<TaskDto> taskDtos;
    private TaskDto taskDto;
    private Map<String, List<ApptUserCalendarDto>> inspectionDateMap;

    private List<ApptUserCalendarDto> apptUserCalendarDtoListAll;

    /**
     * System Date Dto
     */
    private AppointmentDto appointmentDto;

    /**
     * Specific Date Dto
     */
    private AppointmentDto specificApptDto;

    private List<String> refNo;

    private List<String> refShowNo;

    private String apptFeSpecificDate;

    private String apptFeReason;

    private Map<String, ApplicationDto> corrAppMap;
    private List<ApptAppInfoShowDto> applicationInfoShow;

    /**
     * Create And Update Field
     */
    private List<AppPremisesInspecApptDto> appPremisesInspecApptUpdateList;
    private List<AppPremisesInspecApptDto> appPremisesInspecApptCreateList;

    private List<ApplicationDto> applicationDtos;
    private List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos;
    private List<AppInspectionStatusDto> appInspectionStatusDtos;
    private List<AppPremisesRecommendationDto> appPremisesRecommendationDtos;
}
