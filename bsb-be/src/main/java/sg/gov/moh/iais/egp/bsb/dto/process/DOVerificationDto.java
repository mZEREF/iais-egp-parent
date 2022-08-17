package sg.gov.moh.iais.egp.bsb.dto.process;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.RFFacilityDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiProcessDto;

import java.io.Serializable;
import java.util.List;

@Data
public class DOVerificationDto implements Serializable {
    // display
    private SubmissionDetailsInfo submissionDetailsInfo;
    private RFFacilityDetailsInfo rfFacilityDetailsInfo;
    private List<DocDisplayDto> supportDocDisplayDtoList;
    private List<ProcessHistoryDto> processHistoryDtoList;

    private String currentStatus;
    private String processingDecision;
    private String remarks;
    private String commentsToApplicant;

    private RfiProcessDto rfiProcessDto;
}
