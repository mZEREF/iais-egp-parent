package sg.gov.moh.iais.egp.bsb.dto.inspection;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;

import java.util.List;

/**
 * Load inspection report data dto
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsSubmitReportDataDto {
    private SubmissionDetailsInfo submissionDetailsInfo;
    private FacilityDetailsInfo facilityDetailsInfo;
    private ReportDto reportDto;
    private List<ProcessHistoryDto> processHistoryDtoList;
    private List<SelectOption> selectRouteToMoh;
}
