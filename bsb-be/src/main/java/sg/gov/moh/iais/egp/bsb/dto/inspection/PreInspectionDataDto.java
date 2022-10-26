package sg.gov.moh.iais.egp.bsb.dto.inspection;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;

import java.io.Serializable;
import java.util.List;


@Data
public class PreInspectionDataDto implements Serializable {
    private SubmissionDetailsInfo submissionDetailsInfo;

    private FacilityDetailsInfo facilityDetailsInfo;

    private List<ProcessHistoryDto> processHistoryDtoList;

    private ChecklistConfigDto commonChecklistConfigDto;
    private ChecklistConfigDto bsbChecklistConfigDto;
}
