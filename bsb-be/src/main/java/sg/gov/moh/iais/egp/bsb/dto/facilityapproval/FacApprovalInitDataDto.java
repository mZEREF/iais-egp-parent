package sg.gov.moh.iais.egp.bsb.dto.facilityapproval;


import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacApprovalInitDataDto implements Serializable {
    private SubmissionDetailsInfo submissionDetailsInfo;
    private FacilityDetailsInfo facilityDetailsInfo;
    private List<DocDisplayDto> supportDocDisplayDtoList;
    private List<ProcessHistoryDto> processHistoryDtoList;
    private List<SelectOption> selectRouteToAO;
    private List<SelectOption> selectRouteToHM;
    private FacApprovalProcessDto processDto;
}
