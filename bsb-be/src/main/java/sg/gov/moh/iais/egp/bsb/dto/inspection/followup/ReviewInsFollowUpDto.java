package sg.gov.moh.iais.egp.bsb.dto.inspection.followup;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.FollowUpDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;

import java.io.Serializable;
import java.util.List;

@Data
public class ReviewInsFollowUpDto implements Serializable {
    //follow up application id
    private String appId;
    //inspection application id
    private String insAppId;
    private String currentStatus;
    private String requestExtension;
    private String reasonForExtension;
    private String doDecision;
    //info page
    private SubmissionDetailsInfo submissionDetailsInfo;
    //facility detail page
    private FacilityDetailsInfo facilityDetailsInfo;
    //follow up page
    private List<FollowUpDisplayDto> followUpDisplayDtos;
    private List<DocDisplayDto> followUpDocDisplayDtoList;
    private List<RemarkHistoryItemDto> remarksHistoryList;
    //Processing page
    private List<ProcessHistoryDto> processHistoryDtoList;
    private List<SelectOption> selectRouteToMoh;
}
