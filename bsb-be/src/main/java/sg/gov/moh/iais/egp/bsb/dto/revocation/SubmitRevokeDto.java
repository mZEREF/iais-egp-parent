package sg.gov.moh.iais.egp.bsb.dto.revocation;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;
import sg.gov.moh.iais.egp.bsb.dto.file.DocRecordInfo;
import sg.gov.moh.iais.egp.bsb.dto.suspension.PrimaryDocDto;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;

import static sg.gov.moh.iais.egp.bsb.constant.RevocationConstants.*;

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

    private List<DocRecordInfo> savedInfos;
    private List<DocMeta> docMetas;
    //Used to store files retrieved from the database,key is fileRepoId
    private Map<String, DocRecordInfo> queryDocMap;
    /* to be deleted docs (which already saved), the string is repoId, used to delete doc in internalDoc */
    private Set<String> toBeDeletedDocIds;

    public void doReqObjMapping(HttpServletRequest request){
        String doRevokeReason = ParamUtil.getString(request, PARAM_REASON);
        String doRevokeRemarks = ParamUtil.getString(request, PARAM_DO_REMARKS);
        //get user name
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        this.setLoginUser(loginContext.getUserName());
        this.setApplicationDt(new Date());
//        this.setReason(PARAM_REASON_TYPE_DO);
        this.setReasonContent(doRevokeReason);
        this.setRemarks(doRevokeRemarks);
        this.setModule("doRevoke");
        this.setAppType(PARAM_APPLICATION_TYPE_REVOCATION);
        this.setStatus(PARAM_APPLICATION_STATUS_PENDING_AO);
    }

    public void aoReqObjMapping(HttpServletRequest request){
        String aoRevokeReason = ParamUtil.getString(request, PARAM_REASON);
        String aoRevokeRemarks = ParamUtil.getString(request, PARAM_AO_REMARKS);
        String aoProcessDecision = ParamUtil.getString(request, PARAM_AO_DECISION);

        String[] strArr = aoRevokeReason.split(";");
        String a = strArr[strArr.length - 1];
        char[] charStr = a.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : charStr) {
            sb.append(c);
        }
        boolean contains = reason.contains(sb.toString());
        //get user name
        if (!contains) {
//            this.setReason(PARAM_REASON_TYPE_AO);
            this.setReasonContent(aoRevokeReason);
        }
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        this.setLoginUser(loginContext.getUserName());
        this.setRemarks(aoRevokeRemarks);
        this.setAoRemarks(aoRevokeRemarks);
        this.setAoDecision(aoProcessDecision);
        this.setModule("aoRevoke");
    }
}
