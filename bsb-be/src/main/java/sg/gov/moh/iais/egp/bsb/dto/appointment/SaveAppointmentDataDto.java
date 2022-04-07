package sg.gov.moh.iais.egp.bsb.dto.appointment;

import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.entity.AppInspectorCorrelationDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.InspectionAppointmentDto;

import java.util.List;

/**
 * @author tangtang
 * @date 2022/3/14 13:26
 */
@Data
public class SaveAppointmentDataDto {
    private List<InspectionAppointmentDto> appointmentDtos;
    private List<String> apptRefNos;
    private List<TaskDto> taskDtos;
    private List<AppInspectorCorrelationDto> appInspCorrelationDtos;
}
