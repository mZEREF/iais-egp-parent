package sg.gov.moh.iais.egp.bsb.dto.revocation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Zhu Tangtang
 */
@Data
public class SubmitRevokeDto implements Serializable {
    //approval
    private String approvalNo;
    private String approvalId;
    private String approvalStatus;
    //facility
    private String facName;
    private String facId;
    private String facAddress;
    private String facClassification;
    private String activityType;
    //application
    private String appId;
    private String processType;
    private String appType;
    private String status;
    private Date applicationDt;
    private String applicationNo;
    //applicationMisc
    private String reason;
    private String reasonContent;
    private String remarks;
    private String aoRemarks;
    private String doRemarks;
    //
    private String doReason;
    private String aoDecision;
    //
    private String loginUser;
    private String module;
    private String taskId;
    private Collection<DocRecordInfo> docRecordInfos;

    @JsonIgnore
    private PrimaryDocDto primaryDocDto;

    private List<PrimaryDocDto.DocRecordInfo> savedInfos;

    private List<DocMeta> docMetas;

    @JsonIgnore
    private List<PrimaryDocDto.NewDocInfo> newDocInfos;
    @JsonIgnore
    private String docType;
    @JsonIgnore
    private String repoIdNewString;
}
