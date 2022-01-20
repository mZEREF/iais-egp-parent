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
public class DeRegistrationAFCDto implements Serializable {
    private String approvalNo;
    private String processType;
    private String draftAppNo;

    private String organisationName;
    private String organisationAddress;
    private String reasons;
    private String remarks;
    private String declaration;

    private List<DocRecordInfo> docRecordInfos;
    // for validation
    private List<DocMeta> docMetas;
    private Set<String> toBeDeletedDocIds;

    private static final String KEY_REASONS = "reasons";
    private static final String KEY_REMARKS = "remarks";
    private static final String KEY_DECLARATION = "declaration";

    public void reqObjMapping(HttpServletRequest request) {
        this.setReasons(ParamUtil.getString(request, KEY_REASONS));
        this.setRemarks(ParamUtil.getString(request, KEY_REMARKS));
        this.setDeclaration(ParamUtil.getString(request, KEY_DECLARATION));
    }
}
