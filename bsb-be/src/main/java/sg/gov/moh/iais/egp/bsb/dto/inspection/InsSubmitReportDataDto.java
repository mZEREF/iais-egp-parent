package sg.gov.moh.iais.egp.bsb.dto.inspection;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.entity.InspectionOutcomeDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;

import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsSubmitReportDataDto {
    private SubmissionDetailsInfo submissionDetailsInfo;
    private List<InsFindingDisplayDto> findingDtoList;
    private InspectionOutcomeDto outcomeDto;
}
