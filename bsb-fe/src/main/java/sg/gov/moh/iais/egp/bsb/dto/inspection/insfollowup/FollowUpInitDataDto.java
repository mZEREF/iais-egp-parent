package sg.gov.moh.iais.egp.bsb.dto.inspection.insfollowup;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.inspection.RectifyInsReportDto;


@Data
public class FollowUpInitDataDto {
    private FollowUpViewDto followUpViewDto;
    private RectifyInsReportDto docDto;
}
