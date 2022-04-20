package sg.gov.moh.iais.egp.bsb.dto.inspection;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;

import java.util.List;


@Data
public class InsNCRectificationDataDto {
    private SubmissionDetailsInfo submissionDetailsInfo;
    private FacilityDetailsInfo facilityDetailsInfo;
    private List<InsRectificationDisplayDto> rectificationDisplayDtoList;
    private List<DocRecordInfo> rectificationDoc;
    private List<ProcessHistoryDto> processHistoryDtoList;
    private List<SelectOption> selectRouteToMoh;
    //AO preview DO decision
    private String decision;
}
