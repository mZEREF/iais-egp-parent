package sg.gov.moh.iais.egp.bsb.dto.appointment;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.entity.AppInspectorCorrelationDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.InspectionInfoDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.TaskDto;

import java.util.List;

/**
 * @author tangtang
 * @date 2022/3/14 13:26
 */
@Data
public class SaveAppointmentDataDto {
    private List<TaskDto> taskDtos;
    private List<AppInspectorCorrelationDto> appInspCorrelationDtos;
    private InspectionInfoDto inspectionInfoDto;
}
