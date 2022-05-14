package sg.gov.moh.iais.egp.bsb.dto.inspection;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;

import java.util.List;
@Data
public class InsCertificationInitDataDto {
    private SubmissionDetailsInfo submissionDetailsInfo;
    private FacilityDetailsInfo facilityDetailsInfo;
    private List<DocDisplayDto> supportDocDisplayDtoList;
    private List<ProcessHistoryDto> processHistoryDtoList;
    private List<SelectOption> selectRouteToMoh;
}
