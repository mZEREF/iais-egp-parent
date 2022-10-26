package sg.gov.moh.iais.egp.bsb.dto.process;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.rfi.PageAppEditSelectDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiProcessDto;

import java.io.Serializable;
import java.util.List;

@Data
public class DOVerificationDto implements Serializable {
    // display
    private SubmissionDetailsInfo submissionDetailsInfo;
    private FacilityDetailsInfo facilityDetailsInfo;
    private List<ProcessHistoryDto> processHistoryDtoList;

    private String currentStatus;
    private String processingDecision;
    private String remarks;

    private RfiProcessDto rfiProcessDto;

    // which sections of this application can do RFI
    private PageAppEditSelectDto pageAppEditSelectDto;
    private String commentsToApplicant;
}
