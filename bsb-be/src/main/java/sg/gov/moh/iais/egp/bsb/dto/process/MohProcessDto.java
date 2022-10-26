package sg.gov.moh.iais.egp.bsb.dto.process;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.rfi.PageAppEditSelectDto;

import java.io.Serializable;
import java.util.List;


@Data
@Slf4j
public class MohProcessDto implements Serializable {
    private SubmissionDetailsInfo submissionDetailsInfo;
    private FacilityDetailsInfo facilityDetailsInfo;

    private List<SelectOption> selectRouteToMoh;
    private List<ProcessHistoryDto> processHistoryDtoList;
    // It is used to determine whether the last role is DO or HM when AO Screening and Approval.
    private String lastRole;

    private String lastRecommendation;
    private String lastRemarks;
    private String remarks;
    private String processingDecision;
    // cannot use Boolean or boolean, jsp has check null logic
    private String inspectionRequired;
    private String certificationRequired;
    private String selectMohUser;
    private String reasonForRejection;
    private String aoRecommendation;
    private String facValidityEndDate;

    // which sections of this application can do RFI
    private PageAppEditSelectDto pageAppEditSelectDto;
    private String commentsToApplicant;

    //fill these data when apptype = APP_TYPE_RENEW
    private String deferDate;
    private String deferReason;
}
