package sg.gov.moh.iais.egp.bsb.dto.inspection;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


    private static final String SEPARATOR                               = "--v--";
    private static final String KEY_INSPECTION_PURPOSE                  = "inspectionPurpose";
    private static final String KEY_FACILITY_REPRESENTATIVE             = "facilityRepresentative";
    private static final String KEY_OBSERVATION                         = "observation";
    private static final String KEY_OBSERVATION_REMARKS                 = "observationRemarks";
    private static final String KEY_FINDING                             = "finding";
    private static final String KEY_ACTION_REQUIRED                     = "actionRequired";
    private static final String KEY_EXCLUDE_FROM_APPLICANT_VERSION      = "excludeFromApplicantVersion";
    private static final String KEY_DEFICIENCY                          = "deficiency";
    private static final String KEY_OUTCOME                             = "outcome";
    private static final String KEY_RECOMMENDATION_REMARKS              = "recommendationRemarks";
    private static final String KEY_DUE_DATE                            = "dueDt";


    public void reqObjMapping(HttpServletRequest request) {
        this.inspectionPurpose = ParamUtil.getString(request, KEY_INSPECTION_PURPOSE);
        this.facilityRepresentative = ParamUtil.getString(request, KEY_FACILITY_REPRESENTATIVE);
        this.observation = ParamUtil.getString(request, KEY_OBSERVATION);
        this.observationRemarks = ParamUtil.getString(request, KEY_OBSERVATION_REMARKS);

        String[] deficiencyArray = ParamUtil.getStrings(request, KEY_DEFICIENCY);
        if (deficiencyArray != null && deficiencyArray.length > 0) {
            this.deficiency = new ArrayList<>(Arrays.asList(deficiencyArray));
        } else {
            this.deficiency = new ArrayList<>(0);
        }

        if (!CollectionUtils.isEmpty(checkListItemBsbList)) {
            for (NCDisplayDto ncDisplayDto : checkListItemBsbList) {
                String id = ncDisplayDto.getId();
                ncDisplayDto.setFinding(ParamUtil.getString(request, KEY_FINDING + SEPARATOR + id));
                ncDisplayDto.setActionRequired(ParamUtil.getString(request, KEY_ACTION_REQUIRED + SEPARATOR + id));
                ncDisplayDto.setExcludeFromApplicantVersion(ParamUtil.getString(request, KEY_EXCLUDE_FROM_APPLICANT_VERSION + SEPARATOR + id));
            }
        }

        if (!CollectionUtils.isEmpty(followUpItemGeneralList)) {
            for (FollowUpDisplayDto followUpDisplayDto : followUpItemGeneralList) {
                String id = followUpDisplayDto.getId();
                followUpDisplayDto.setObservation(ParamUtil.getString(request, KEY_OBSERVATION + SEPARATOR + id));
                followUpDisplayDto.setActionRequired(ParamUtil.getString(request, KEY_ACTION_REQUIRED + SEPARATOR + id));
                followUpDisplayDto.setDueDt(ParamUtil.getString(request, KEY_DUE_DATE + SEPARATOR + id));
                followUpDisplayDto.setExcludeFromApplicantVersion(ParamUtil.getString(request, KEY_EXCLUDE_FROM_APPLICANT_VERSION + SEPARATOR + id));
            }
        }

        this.outcome = ParamUtil.getString(request, KEY_OUTCOME);
        this.recommendationRemarks = ParamUtil.getString(request, KEY_RECOMMENDATION_REMARKS);
    }
}
