package sg.gov.moh.iais.egp.bsb.dto.inspection;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.entity.InspectionChecklistDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.InspectionOutcomeDto;


@Data
public class InsSubmitFindingDataDto {
    private InsInfoDto insInfoDto;
    private InsFindingFormDto findingFormDto;
    private InspectionChecklistDto checklistDto;
    private ChecklistConfigDto configDto;
    private InspectionOutcomeDto outcomeDto;
}
