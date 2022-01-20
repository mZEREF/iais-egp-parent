package sg.gov.moh.iais.egp.bsb.dto.deregorcancellation;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author : LiRan
 * @date : 2022/1/11
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeRegistrationFacilityDto implements Serializable {
    private String approvalNo;
    private String processType;
    private String draftAppNo;
    private String activityType;

    private String facilityName;
    private String facilityAddress;
    private String facilityClassification;
    private String reasons;
    private String remarks;
    private List<ApprovalInfo> approvalInfoList;
    private String declaration1;
    private String declaration2;
    private String declaration3;

    private List<DocRecordInfo> docRecordInfos;
    // for validation
    private List<DocMeta> docMetas;
    private Set<String> toBeDeletedDocIds;

    @Data
    public static class ApprovalInfo implements Serializable{
        private String approvalType;
        private String biologicalAgentToxin;
        private String status;
        private String physicalPossession;
    }

    private static final String KEY_REASONS = "reasons";
    private static final String KEY_REMARKS = "remarks";
    private static final String KEY_DECLARATION_1 = "declaration1";
    private static final String KEY_DECLARATION_2 = "declaration2";
    private static final String KEY_DECLARATION_3 = "declaration3";

    public void reqObjMapping(HttpServletRequest request) {
        this.setReasons(ParamUtil.getString(request, KEY_REASONS));
        this.setRemarks(ParamUtil.getString(request, KEY_REMARKS));
        this.setDeclaration1(ParamUtil.getString(request, KEY_DECLARATION_1));
        this.setDeclaration2(ParamUtil.getString(request, KEY_DECLARATION_2));
        this.setDeclaration3(ParamUtil.getString(request, KEY_DECLARATION_3));
    }
}
