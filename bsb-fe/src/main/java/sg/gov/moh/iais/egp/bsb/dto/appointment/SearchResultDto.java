package sg.gov.moh.iais.egp.bsb.dto.appointment;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;

import java.util.List;

/**
 * @author tangtang
 * @date 2022/3/29 13:35
 */
@Data
public class SearchResultDto {
    private PageInfo pageInfo;
    private List<AppointmentViewDto> appointmentDtos;
}
