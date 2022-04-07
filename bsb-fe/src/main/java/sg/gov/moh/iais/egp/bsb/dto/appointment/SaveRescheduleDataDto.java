package sg.gov.moh.iais.egp.bsb.dto.appointment;

import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author tangtang
 * @date 2022/4/1 16:43
 */
@Data
public class SaveRescheduleDataDto {
    private String appId;
    private String appNo;
    private Date specInsDate;
    private Date startDate;
    private Date endDate;
    private String reason;

    private List<ApptUserCalendarDto> userCalendarDtos;
    private String apptRefNo;
}
