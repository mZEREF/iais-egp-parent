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
    private String applicantRemarks;
    // Attachments
    private List<DocRecordInfo> docRecordInfo;
    private List<DocMeta> docMeta;

    private Set<String> toBeDeletedDocIds;

    private static final String KEY_APPLICANT_REMARKS = "applicantRemarks";

    public void reqObjMapping(HttpServletRequest request) {
        this.setApplicantRemarks(ParamUtil.getString(request, KEY_APPLICANT_REMARKS));
    }
}
