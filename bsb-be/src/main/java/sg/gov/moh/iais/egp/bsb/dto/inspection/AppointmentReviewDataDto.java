package sg.gov.moh.iais.egp.bsb.dto.inspection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.rfi.PageAppEditSelectDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiAppSelectDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
public class AppointmentReviewDataDto implements Serializable {
    // application info page
    private SubmissionDetailsInfo submissionDetailsInfo;
    private FacilityDetailsInfo facilityDetailsInfo;
    private List<ProcessHistoryDto> processHistoryDtoList;
    // user specify new date
    private String specifyStartDt;
    private String specifyEndDt;
    private String specifyStartHour;
    private String specifyEndHour;

    // display
    private String applicationNo;
    private String applicationStatus;
    private String preferredInspectionDate;
    private List<String> assignedMohOfficerList;
    private String proposedDate;

    // save
    private String processingDecision;
    private String sectionsAllowedForChange;

    private String startDate;
    private String endDate;
    private String apptRefNo;

    @JsonIgnore
    private Date startDt;
    @JsonIgnore
    private Date endDt;

    private String remarksToApplicant;
    private PageAppEditSelectDto pageAppEditSelectDto;
    private RfiAppSelectDto rfiAppSelectDto;
}
