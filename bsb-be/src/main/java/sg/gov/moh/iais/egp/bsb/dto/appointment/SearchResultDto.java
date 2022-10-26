package sg.gov.moh.iais.egp.bsb.dto.appointment;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;

import java.util.List;

@Data
public class SearchResultDto {
    private PageInfo pageInfo;
    private List<AppointmentViewDto> appointmentViewDtos;
}
