package sg.gov.moh.iais.egp.bsb.dto.process;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;

import java.util.List;


@Data
public class ApprovalLetterInitDataDto {
    private SubmissionDetailsInfo submissionDetailsInfo;
    private FacilityDetailsInfo facilityDetailsInfo;
    private ApprovalLetterDto insApprovalLetterDto;
    private List<ProcessHistoryDto> processHistoryDtoList;
    private List<SelectOption> selectRouteToMoh;
}