package sg.gov.moh.iais.egp.bsb.dto.inspection;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;

import java.util.List;

@Data
public class PreInspectionDataDto {
    private SubmissionDetailsInfo submissionDetailsInfo;

    private List<DocDisplayDto> supportDocDisplayDtoList;
    private List<DocDisplayDto> internalDocDisplayDtoList;

    private FacilityDetailsInfo facilityDetailsInfo;

    private List<ProcessHistoryDto> processHistoryDtoList;

    private ChecklistConfigDto commonChecklistConfigDto;
    private ChecklistConfigDto bsbChecklistConfigDto;
}
