package sg.gov.moh.iais.egp.bsb.dto.inspection;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.inspection.insfollowup.FollowUpDisplayDto;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Inspection report page show data dto
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportDto implements Serializable {
    private String appId;
    // Section A (Facility Details)
    private String facName;
    private String classification;
    private String activityType;
    private String facilityAddress;
    private String adminName;
    private String adminMobileNo;
    private String adminEmail;
    // Section B (Inspection Details)
    private String inspectionDt;
    private String inspectionPurpose;
    private String facilityRepresentative;
    private String mohInspector;
    // Section C (Inspection Findings)
    // part 1
    private String checkListUsed;
    // part 2
    private String observation;
    private String observationRemarks;
    private List<NCDisplayDto> checkListItemBsbList;
    // part 3
    private List<FollowUpDisplayDto> followUpItemGeneralList;
    // Section D (Recommendation)
    private List<String> deficiency;
    private String outcome;
    private String recommendationRemarks;
    private String applicantRemarks;
    // Attachments
    private List<DocRecordInfo> docRecordInfo;
    private List<DocMeta> docMeta;

    private Set<String> toBeDeletedDocIds;

    private static final String KEY_APPLICANT_REMARKS = "applicantRemarks";
    private static final String KEY_APPLICANT_INPUT  = "applicantInput";
    private static final String SEPARATOR            = "--v--";

    public void reqObjMapping(HttpServletRequest request) {
        if(checkListItemBsbList != null && !checkListItemBsbList.isEmpty()) {
            for (NCDisplayDto nc : checkListItemBsbList) {
                if(!"Y".equals(nc.getExcludeFromApplicantVersion())) {
                    nc.setApplicantInput(ParamUtil.getString(request, KEY_APPLICANT_INPUT + SEPARATOR + nc.getId()));
                }
            }
        }
        if(followUpItemGeneralList != null && !followUpItemGeneralList.isEmpty()) {
            for (FollowUpDisplayDto followup : followUpItemGeneralList) {
                if(!"Y".equals(followup.getExcludeFromApplicantVersion())) {
                    followup.setApplicantInput(ParamUtil.getString(request, KEY_APPLICANT_INPUT + SEPARATOR + followup.getId()));
                }
            }
        }
        this.setApplicantRemarks(ParamUtil.getString(request, KEY_APPLICANT_REMARKS));
    }

    public boolean getIsUnExcludedCheckListItemBsbListExist() {
        if(this.checkListItemBsbList != null && !this.checkListItemBsbList.isEmpty()) {
            List<NCDisplayDto> unExcludedChecklist = this.checkListItemBsbList.stream().filter(it->!"Y".equals(it.getExcludeFromApplicantVersion())).collect(Collectors.toList());
            return !unExcludedChecklist.isEmpty();
        }
        return false;
    }

    public boolean getIsUnExcludedFollowUpItemListExist() {
        if(this.followUpItemGeneralList != null && !this.followUpItemGeneralList.isEmpty()) {
            List<FollowUpDisplayDto> unExcludedFollowupList = this.followUpItemGeneralList.stream().filter(it->!"Y".equals(it.getExcludeFromApplicantVersion())).collect(Collectors.toList());
            return !unExcludedFollowupList.isEmpty();
        }
        return false;
    }

}
