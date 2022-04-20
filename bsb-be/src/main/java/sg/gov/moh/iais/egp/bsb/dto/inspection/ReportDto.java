package sg.gov.moh.iais.egp.bsb.dto.inspection;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

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
    // Section A (Facility Details)
    private String facName;
    private String classification;
    private String blk;
    private String street;
    private String floor;
    private String unit;
    private String postalCode;
    private String adminName;
    private String adminMobileNo;
    private String adminEmail;
    // Section B (Inspection Details)
    private String inspectionDate;
    private String inspectionPurpose;
    private String facilityRepresentative;
    private String mohInspector;
    // Section C (Inspection Findings)
    // part 1
    private String checkListUsed;
    // part 2
    private String observation;
    private String observationRemarks;
    private List<NCDisplayDto> checkListItemGeneralList;
    private List<NCDisplayDto> checkListItemBsbList;
    // part 3
    private List<FollowUpDisplayDto> followUpItemGeneralList;
    // Section D (Recommendation)
    private List<String> deficiency;
    private String outcome;
    private String recommendationRemarks;
    private String facilityValidityDate;
    // Attachments

    private static final String KEY_OBSERVATION                         = "observation";
    private static final String KEY_OBSERVATION_REMARKS                 = "observationRemarks";
    private static final String KEY_DEFICIENCY                          = "deficiency";
    private static final String KEY_OUTCOME                             = "outcome";
    private static final String KEY_RECOMMENDATION_REMARKS              = "recommendationRemarks";
    private static final String KEY_FACILITY_VALIDITY_DATE              = "facilityValidityDate";

    public void reqObjMapping(HttpServletRequest request) {
        this.observation = ParamUtil.getString(request, KEY_OBSERVATION);
        this.observationRemarks = ParamUtil.getString(request, KEY_OBSERVATION_REMARKS);

        String[] deficiencyArray = ParamUtil.getStrings(request, KEY_DEFICIENCY);
        if (deficiencyArray != null && deficiencyArray.length > 0) {
            this.deficiency = new ArrayList<>(Arrays.asList(deficiencyArray));
        } else {
            this.deficiency = new ArrayList<>(0);
        }

        this.outcome = ParamUtil.getString(request, KEY_OUTCOME);
        this.recommendationRemarks = ParamUtil.getString(request, KEY_RECOMMENDATION_REMARKS);
        this.facilityValidityDate = ParamUtil.getString(request, KEY_FACILITY_VALIDITY_DATE);
    }
}
